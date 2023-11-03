
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Implementation specific SQL for the reference implementation.

-- DDT-1356

CREATE TABLE ebl_solution_provider_type (
    ebl_solution_provider_name varchar(50) NOT NULL,
    ebl_solution_provider_code varchar(5) PRIMARY KEY,
    ebl_solution_provider_url varchar(100) NOT NULL,
    ebl_solution_provider_description varchar(250) NULL
);

--- DDT-948
ALTER TABLE shipping_instruction ADD valid_until timestamp with time zone NULL;
CREATE UNIQUE INDEX unq_valid_until_si_idx ON shipping_instruction(shipping_instruction_reference) WHERE valid_until IS NULL;

ALTER TABLE transport_document ADD valid_until timestamp with time zone NULL;
CREATE UNIQUE INDEX unq_valid_until_td_idx ON transport_document(transport_document_reference) WHERE valid_until IS NULL;

-- DDT-1353 - remodel-equipment<->commodity link
ALTER TABLE requested_equipment_group ADD commodity_id uuid NULL REFERENCES commodity (id);
CREATE INDEX ON requested_equipment_group (commodity_id);

