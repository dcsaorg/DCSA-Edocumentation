
CREATE TABLE booking (
    id uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    carrier_booking_request_reference varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    content jsonb NOT NULL,
    valid_until timestamp with time zone NULL,
    updated_date_time timestamp with time zone NOT NULL
);
CREATE INDEX ON booking (carrier_booking_request_reference);
CREATE UNIQUE INDEX unq_valid_until_booking_idx ON booking (carrier_booking_request_reference) WHERE valid_until IS NULL;
