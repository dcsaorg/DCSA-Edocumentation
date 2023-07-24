
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Implementation specific SQL for the reference implementation.

-- DDT-1356

CREATE TABLE assigned_equipment (
    id uuid PRIMARY KEY,
    shipment_id uuid NOT NULL REFERENCES shipment(id),
    requested_equipment_group_id uuid NOT NULL REFERENCES requested_equipment_group(id)
);

CREATE TABLE assigned_equipment_references (
    assigned_equipment_id uuid REFERENCES assigned_equipment (id),
    equipment_reference varchar(15) NOT NULL REFERENCES equipment (equipment_reference),

    -- A equipment can only be used once per requested_equipment_group
    UNIQUE (assigned_equipment_id, equipment_reference)
);


-- DDT-1058
ALTER TABLE shipment_event ADD document_reference varchar(100) NOT NULL;


-- DDT-1221

CREATE TABLE event_cache_queue (
    event_id uuid NOT NULL PRIMARY KEY,
    event_type varchar(16) NOT NULL CONSTRAINT event_type CHECK (event_type IN ('SHIPMENT','TRANSPORT', 'EQUIPMENT'))
);


CREATE TABLE event_cache_queue_dead (
    event_id uuid NOT NULL PRIMARY KEY,
    event_type varchar(16) NOT NULL CONSTRAINT event_type CHECK (event_type IN ('SHIPMENT','TRANSPORT', 'EQUIPMENT')),
    failure_reason_type varchar(200),
    failure_reason_message text
);


CREATE TABLE event_cache (
    event_id uuid NOT NULL PRIMARY KEY,
    event_type varchar(16) NOT NULL CONSTRAINT event_type CHECK (event_type IN ('SHIPMENT','TRANSPORT', 'EQUIPMENT')),
    content jsonb NOT NULL,
    document_references text,
    "references" text,
    event_created_date_time timestamp with time zone NOT NULL,
    event_date_time timestamp with time zone NOT NULL
);
CREATE INDEX ON event_cache (event_created_date_time);
CREATE INDEX ON event_cache (event_date_time);

CREATE OR REPLACE FUNCTION queue_shipment_event() RETURNS TRIGGER AS $$
    BEGIN
      INSERT INTO event_cache_queue (event_id, event_type) VALUES(NEW.event_id, 'SHIPMENT');
      RETURN NULL;
    END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER queue_shipment_events AFTER INSERT ON shipment_event
    FOR EACH ROW EXECUTE PROCEDURE queue_shipment_event();


/* Views to assist with finding references for GET /events endpoint.
 * It provide the following information:
 *
 * It provides a link_type and a document_reference (for ShipmentEvent)
 * or transport_call_id (for TransportCall based Events)
 * and a utilized_transport_equipment_id (for EquipmentEvent)
 *
 * These provided values can be used for to find references for the specific event.
 * Below is an example showing how to find references for a transport_call based event:
 * FROM aggregated_events ae
 * JOIN
 * (SELECT transport_call_id,
 *        reference_value,
 *        reference_type_code
 * FROM event_reference) er ON ae.transport_call_id = er.transport_call_id
 *
 * NOTE: VIEWS ARE MADE SEPARATELY BELOW THAN MERGED AS ONE VIEW
 */

/*
* View to extract references for for ShipmentEvent
*/
CREATE VIEW shipment_event_reference AS
  (-- For CBR document reference ShipmentEvents
 SELECT DISTINCT reference.reference_value,
                 reference.reference_type_code,
                 NULL::UUID AS transport_call_id,
                 b.id AS document_id,
                 NULL::UUID AS utilized_transport_equipment_id,
                 'CBR' AS link_type
   FROM booking b
   JOIN shipment s ON b.id = s.booking_id
   JOIN consignment_item con ON con.shipment_id = s.id
   JOIN shipping_instruction AS si ON con.shipping_instruction_id = si.id
   JOIN reference AS reference ON reference.consignment_item_id = con.id
   OR reference.shipment_id = s.id
   OR reference.shipping_instruction_id = si.id
   OR reference.booking_id = b.id
   UNION ALL
      -- For BKG document reference ShipmentEvents
 SELECT DISTINCT reference.reference_value,
                 reference.reference_type_code,
                 NULL::UUID AS transport_call_id,
                 s.id AS document_id,
                 NULL::UUID AS utilized_transport_equipment_id,
                 'BKG' AS link_type
   FROM shipment s
   JOIN booking b ON b.id = s.booking_id
   JOIN consignment_item con ON con.shipment_id = s.id
   JOIN shipping_instruction AS si ON con.shipping_instruction_id = si.id
   JOIN reference AS reference ON reference.consignment_item_id = con.id
   OR reference.shipment_id = s.id
   OR reference.shipping_instruction_id = si.id
   OR reference.booking_id = b.id
   UNION ALL -- For SHI document reference ShipmentEvents
 SELECT DISTINCT reference.reference_value,
                 reference.reference_type_code,
                 NULL::UUID AS transport_call_id,
                 si.id AS document_id,
                 NULL::UUID AS utilized_transport_equipment_id,
                 'SHI' AS link_type
   FROM shipping_instruction si
   JOIN consignment_item con ON con.shipping_instruction_id = si.id
   JOIN shipment s ON s.id = con.shipment_id
   JOIN booking b ON b.id = s.booking_id
   JOIN reference AS reference ON reference.consignment_item_id = con.id
   OR reference.shipment_id = s.id
   OR reference.shipping_instruction_id = si.id
   OR reference.booking_id = b.id);

CREATE TABLE ebl_solution_provider_type (
    ebl_solution_provider_name varchar(50) NOT NULL,
    ebl_solution_provider_code varchar(5) PRIMARY KEY,
    ebl_solution_provider_url varchar(100) NOT NULL,
    ebl_solution_provider_description varchar(250) NULL
);


--- DDT-948
ALTER TABLE booking ADD valid_until timestamp with time zone NULL;
CREATE UNIQUE INDEX unq_valid_until_booking_idx ON booking(carrier_booking_request_reference) WHERE valid_until IS NULL;

ALTER TABLE shipment ADD valid_until timestamp with time zone NULL;
CREATE UNIQUE INDEX unq_valid_until_shipment_idx ON shipment(carrier_booking_reference) WHERE valid_until IS NULL;

ALTER TABLE shipping_instruction ADD valid_until timestamp with time zone NULL;
CREATE UNIQUE INDEX unq_valid_until_si_idx ON shipping_instruction(shipping_instruction_reference) WHERE valid_until IS NULL;

ALTER TABLE transport_document ADD valid_until timestamp with time zone NULL;
CREATE UNIQUE INDEX unq_valid_until_td_idx ON transport_document(transport_document_reference) WHERE valid_until IS NULL;

-- DDT-1353 - remodel-equipment<->commodity link
ALTER TABLE requested_equipment_group ADD commodity_id uuid NULL REFERENCES commodity (id);
CREATE INDEX ON requested_equipment_group (commodity_id);

