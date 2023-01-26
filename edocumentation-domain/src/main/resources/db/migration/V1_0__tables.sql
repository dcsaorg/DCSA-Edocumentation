

CREATE TABLE booking (
    id uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    carrier_booking_request_reference varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type (shipment_event_type_code) CHECK(document_status IN ('RECE', 'PENU', 'REJE', 'CONF','PENC', 'CANC', 'DECL', 'CMPL')),
    content jsonb NOT NULL,
    valid_until timestamp with time zone NULL,
    booking_request_datetime timestamp with time zone NOT NULL,
    updated_date_time timestamp with time zone NOT NULL
);
CREATE INDEX ON booking (carrier_booking_request_reference);
CREATE UNIQUE INDEX unq_valid_until_booking_idx ON booking (carrier_booking_request_reference) WHERE valid_until IS NULL;
CREATE INDEX ON booking (booking_request_datetime);
CREATE INDEX ON booking (updated_date_time);


CREATE TABLE shipping_instruction (
    id uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    shipping_instruction_reference varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type (shipment_event_type_code) CHECK(document_status IN ('RECE','PENU','DRFT','PENA','APPR','ISSU','SURR','VOID')),
    transport_document_type_code varchar(3) NULL REFERENCES transport_document_type (transport_document_type_code),
    content jsonb NOT NULL,
    valid_until timestamp with time zone NULL,
    created_date_time timestamp with time zone NOT NULL,
    updated_date_time timestamp with time zone NOT NULL
);
CREATE INDEX ON shipping_instruction (shipping_instruction_reference);
CREATE UNIQUE INDEX unq_valid_until_si_idx ON shipping_instruction (shipping_instruction_reference) WHERE valid_until IS NULL;
CREATE INDEX ON shipping_instruction (created_date_time);
CREATE INDEX ON shipping_instruction (updated_date_time);


CREATE TABLE event (
    event_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    event_classifier_code varchar(3) NOT NULL REFERENCES event_classifier (event_classifier_code),
    event_created_date_time timestamp with time zone DEFAULT now() NOT NULL,
    event_date_time timestamp with time zone NOT NULL
);

CREATE TABLE shipment_event (
    shipment_event_type_code varchar(4) NOT NULL REFERENCES shipment_event_type (shipment_event_type_code),
    document_type_code varchar(3) NOT NULL REFERENCES document_type (document_type_code),
    document_id uuid NOT NULL,
    document_reference varchar(100) NOT NULL,
    reason varchar(250) NULL
) INHERITS (event);
