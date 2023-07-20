
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

CREATE OR REPLACE FUNCTION queue_transport_event() RETURNS TRIGGER AS $$
    BEGIN
      INSERT INTO event_cache_queue (event_id, event_type) VALUES(NEW.event_id, 'TRANSPORT');
      RETURN NULL;
    END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER queue_transport_events AFTER INSERT ON transport_event
    FOR EACH ROW EXECUTE PROCEDURE queue_transport_event();

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
* View to extract references for equipmentEvent
*/
CREATE VIEW equipment_event_reference AS
  SELECT DISTINCT  reference.reference_value,
                   reference.reference_type_code,
                   NULL::UUID AS transport_call_id,
                   NULL::UUID AS document_id,
                   ci.utilized_transport_equipment_id AS utilized_transport_equipment_id,
                   'EQ_ID' AS link_type
   FROM cargo_item AS ci
   JOIN consignment_item AS con ON ci.consignment_item_id = con.id
   JOIN shipping_instruction AS si ON con.shipping_instruction_id = si.id
   JOIN shipment AS shipment ON con.shipment_id = shipment.id
   JOIN booking AS booking ON shipment.booking_id = booking.id
   JOIN reference AS reference ON reference.consignment_item_id = con.id
   OR reference.shipment_id = shipment.id
   OR reference.shipping_instruction_id = si.id
   OR reference.booking_id = booking.id;

/*
* View to extract references for for TransportCall based event
*/
CREATE VIEW transport_based_event_reference AS
  SELECT DISTINCT  reference.reference_value,
                   reference.reference_type_code,
                   tc.id AS transport_call_id,
                   NULL::UUID AS document_id,
                   NULL::UUID AS utilized_transport_equipment_id,
                   'TC_ID' AS link_type
   FROM consignment_item AS con
   JOIN shipping_instruction AS si ON con.shipping_instruction_id = si.id
   JOIN shipment AS shipment ON con.shipment_id = shipment.id
   JOIN booking AS booking ON shipment.booking_id = booking.id
   JOIN shipment_transport st ON st.shipment_id = shipment.id
   JOIN
     (SELECT DISTINCT tc.id,
                      t.id AS transport_id
      FROM transport_call tc
      JOIN transport t ON t.load_transport_call_id = tc.id
      OR t.discharge_transport_call_id = tc.id) tc ON tc.transport_id = st.transport_id
   JOIN reference AS reference ON reference.consignment_item_id = con.id
   OR reference.shipment_id = shipment.id
   OR reference.shipping_instruction_id = si.id
   OR reference.booking_id = booking.id;

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

/*
* COMBINE references view for all event types
*/
CREATE VIEW event_reference AS
  SELECT uuid_generate_v4() AS random_id,
                               *
  FROM
  (SELECT *
   FROM equipment_event_reference
   UNION ALL SELECT *
   FROM transport_based_event_reference
   UNION ALL SELECT *
   FROM shipment_event_reference) AS foo;

/* View to assist with the GET /events endpoint.  It provide the following information:
 *
 * It provides a link_type and a document_id (for ShipmentEvent) or transport_call_id (other events).
 * These can be used for JOIN'ing between aggregated_events using something like:
 *     FROM aggregated_events ae
 *     JOIN event_document_reference edr ON (ae.link_type = edr.link_type AND (
 *                   ae.transport_call_id = edr.transport_call_id
 *                OR ae.document_id = edr.document_id
 *     )
 *
 * Additionally, this view provides the following columns:
 *   * document_reference_type (enum value to be used in the documentReferences payload)
 *   * document_reference_value (the actual reference to the document to be used in the documentReferences payload)
 *   * carrier_booking_request_reference (used for query parameters)
 *   * carrier_booking_reference (used for query parameters)
 *   * transport_document_reference (used for query parameters)
 *
 * The query parameter based columns are technical redundant with document_reference_value (+ a filter on the relevant
 * type).  However, they are easier to use / reason about in case multiple query parameters are used.  As an example,
 * given the query parameters:
 *     carrierBookingReference=X&transportDocumentReference=Y
 * When we have them as separate columns, this can trivially be translated into:
 *     "WHERE carrier_booking_reference = 'X' AND transport_document_reference = 'Y'"
 *
 * However, the equivalent query using document_reference_value + document_reference_type would be considerably more
 * complex to write and even harder to convince people that it was correct.
 *
 */
CREATE VIEW event_document_reference AS
SELECT uuid_generate_v4() AS random_id,
                             *
FROM (
        (-- For Transport Call based events
            SELECT DISTINCT tc.id AS transport_call_id,
                            null::uuid AS document_id,
                            'TC_ID' AS link_type,
                            'CBR' AS document_reference_type,
                            b.carrier_booking_request_reference AS document_reference_value,
                            b.carrier_booking_request_reference AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM booking b
            JOIN shipment s ON s.booking_id = b.id
            JOIN shipment_transport st ON st.shipment_id = s.id
            JOIN (SELECT DISTINCT tc.id, t.id AS transport_id
                         FROM transport_call tc
                         JOIN transport t ON t.load_transport_call_id = tc.id
                              OR t.discharge_transport_call_id = tc.id
                         ) tc ON tc.transport_id = st.transport_id
        UNION ALL
            SELECT DISTINCT tc.id AS transport_call_id,
                            null::uuid AS document_id,
                            'TC_ID' AS link_type,
                            'BKG' AS document_reference_type,
                            s.carrier_booking_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            s.carrier_booking_reference AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipment s
            JOIN shipment_transport st ON st.shipment_id = s.id
            JOIN (SELECT DISTINCT tc.id, t.id AS transport_id
                  FROM transport_call tc
                  JOIN transport t ON t.load_transport_call_id = tc.id
                       OR t.discharge_transport_call_id = tc.id
            ) tc ON tc.transport_id = st.transport_id
        UNION ALL
            SELECT DISTINCT tc.id AS transport_call_id,
                            null::uuid AS document_id,
                            'TC_ID' AS link_type,
                            'SHI' AS document_reference_type,
                            si.shipping_instruction_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipping_instruction si
            JOIN consignment_item ci ON ci.shipping_instruction_id = si.id
            JOIN shipment_transport st ON st.shipment_id = ci.shipment_id
            JOIN (SELECT DISTINCT tc.id, t.id AS transport_id
                  FROM transport_call tc
                  JOIN transport t ON t.load_transport_call_id = tc.id
                       OR t.discharge_transport_call_id = tc.id
            ) tc ON tc.transport_id = st.transport_id
        UNION ALL
            SELECT DISTINCT tc.id AS transport_call_id,
                            null::uuid AS document_id,
                            'TC_ID' AS link_type,
                            'TRD' AS document_reference_type,
                            td.transport_document_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            td.transport_document_reference AS transport_document_reference
            FROM transport_document td
            JOIN consignment_item ci ON ci.shipping_instruction_id = td.shipping_instruction_id
            JOIN shipment_transport st ON st.shipment_id = ci.shipment_id
            JOIN (SELECT DISTINCT tc.id, t.id AS transport_id
                  FROM transport_call tc
                  JOIN transport t ON t.load_transport_call_id = tc.id
                       OR t.discharge_transport_call_id = tc.id
            ) tc ON tc.transport_id = st.transport_id
    ) UNION ALL (
            -- For CBR related ShipmentEvents
            -- DISTINCT by definition
            SELECT          NULL::uuid as transport_call_id,
                            b.id AS document_id,
                            'CBR' AS link_type,
                            'CBR' AS document_reference_type,
                            b.carrier_booking_request_reference AS document_reference_value,
                            b.carrier_booking_request_reference AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM booking b
        UNION ALL
            -- DISTINCT. It is a 1:N relation but all the shipments will have unique CBRs
            SELECT          NULL::uuid as transport_call_id,
                            b.id AS document_id,
                            'CBR' AS link_type,
                            'BKG' AS document_reference_type,
                            s.carrier_booking_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            s.carrier_booking_reference AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM booking b
            JOIN shipment s ON b.id = s.booking_id
        UNION ALL
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            b.id AS document_id,
                            'CBR' AS link_type,
                            'SHI' AS document_reference_type,
                            si.shipping_instruction_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM booking b
            JOIN shipment s ON b.id = s.booking_id
            JOIN consignment_item ci ON ci.shipment_id = s.id
            JOIN shipping_instruction si ON si.id = ci.shipping_instruction_id
        UNION ALL
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            b.id AS document_id,
                            'CBR' AS link_type,
                            'TRD' AS document_reference_type,
                            td.transport_document_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            td.transport_document_reference AS transport_document_reference
            FROM booking b
            JOIN shipment s ON b.id = s.booking_id
            JOIN consignment_item ci ON ci.shipment_id = s.id
            JOIN transport_document td ON td.shipping_instruction_id = ci.shipping_instruction_id
    ) UNION ALL (
            -- For BKG related ShipmentEvents
            -- DISTINCT - all the shipments are associated with exactly on booking.
            SELECT          NULL::uuid as transport_call_id,
                            s.id AS document_id,
                            'BKG' AS link_type,
                            'CBR' AS document_reference_type,
                            b.carrier_booking_request_reference AS document_reference_value,
                            b.carrier_booking_request_reference AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipment s
            JOIN booking b ON s.booking_id = b.id
        UNION ALL
            -- DISTINCT by definition
            SELECT          NULL::uuid as transport_call_id,
                            s.id AS document_id,
                            'BKG' AS link_type,
                            'BKG' AS document_reference_type,
                            s.carrier_booking_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            s.carrier_booking_reference AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipment s
        UNION ALL
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            s.id AS document_id,
                            'BKG' AS link_type,
                            'SHI' AS document_reference_type,
                            si.shipping_instruction_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipment s
            JOIN consignment_item ci ON ci.shipment_id = s.id
            JOIN shipping_instruction si ON si.id = ci.shipping_instruction_id
        UNION ALL
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            s.id AS document_id,
                            'BKG' AS link_type,
                            'TRD' AS document_reference_type,
                            td.transport_document_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            td.transport_document_reference AS transport_document_reference
            FROM shipment s
            JOIN consignment_item ci ON ci.shipment_id = s.id
            JOIN transport_document td ON td.shipping_instruction_id = ci.shipping_instruction_id
    ) UNION ALL (
            -- For SHI related ShipmentEvents
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            si.id AS document_id,
                            'SHI' AS link_type,
                            'CBR' AS document_reference_type,
                            b.carrier_booking_request_reference AS document_reference_value,
                            b.carrier_booking_request_reference AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipping_instruction si
            JOIN consignment_item ci ON ci.shipping_instruction_id = si.id
            JOIN shipment s ON s.id = ci.shipment_id
            JOIN booking b ON s.booking_id = b.id
        UNION ALL
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            si.id AS document_id,
                            'SHI' AS link_type,
                            'BKG' AS document_reference_type,
                            s.carrier_booking_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            s.carrier_booking_reference AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipping_instruction si
            JOIN consignment_item ci ON ci.shipping_instruction_id = si.id
            JOIN shipment s ON s.id = ci.shipment_id
        UNION ALL
            -- DISTINCT by definition
            SELECT          NULL::uuid as transport_call_id,
                            si.id AS document_id,
                            'SHI' AS link_type,
                            'SHI' AS document_reference_type,
                            si.shipping_instruction_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM shipping_instruction si
        UNION ALL
            -- DISTINCT due to 1:1 relation
            SELECT          NULL::uuid as transport_call_id,
                            si.id AS document_id,
                            'SHI' AS link_type,
                            'TRD' AS document_reference_type,
                            td.transport_document_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            td.transport_document_reference AS transport_document_reference
            FROM shipping_instruction si
            JOIN transport_document td ON td.shipping_instruction_id = si.id
    ) UNION ALL (
            -- For TRD related ShipmentEvents
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            td.id AS document_id,
                            'TRD' AS link_type,
                            'CBR' AS document_reference_type,
                            b.carrier_booking_request_reference AS document_reference_value,
                            b.carrier_booking_request_reference AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM transport_document td
            JOIN shipping_instruction si ON si.id = td.shipping_instruction_id
            JOIN consignment_item ci ON ci.shipping_instruction_id = si.id
            JOIN shipment s ON s.id = ci.shipment_id
            JOIN booking b ON s.booking_id = b.id
        UNION ALL
            SELECT DISTINCT NULL::uuid as transport_call_id,
                            td.id AS document_id,
                            'TRD' AS link_type,
                            'BKG' AS document_reference_type,
                            s.carrier_booking_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            s.carrier_booking_reference AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM transport_document td
            JOIN shipping_instruction si ON si.id = td.shipping_instruction_id
            JOIN consignment_item ci ON ci.shipping_instruction_id = si.id
            JOIN shipment s ON s.id = ci.shipment_id
        UNION ALL
            -- DISTINCT due to 1:1 relation
            SELECT          NULL::uuid as transport_call_id,
                            td.id AS document_id,
                            'TRD' AS link_type,
                            'SHI' AS document_reference_type,
                            si.shipping_instruction_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            NULL::text AS transport_document_reference
            FROM transport_document td
            JOIN shipping_instruction si ON si.id = td.shipping_instruction_id
        UNION ALL
            -- DISTINCT by definition
            SELECT          NULL::uuid as transport_call_id,
                            td.id AS document_id,
                            'TRD' AS link_type,
                            'TRD' AS document_reference_type,
                            td.transport_document_reference AS document_reference_value,
                            NULL::text AS carrier_booking_request_reference,
                            NULL::text AS carrier_booking_reference,
                            td.transport_document_reference AS transport_document_reference
            FROM transport_document td)
) AS foo;

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

