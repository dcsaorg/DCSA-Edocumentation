
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

