CREATE TABLE shipment_event_type (
    shipment_event_type_code varchar(4) PRIMARY KEY,
    shipment_event_type_name varchar(30) NOT NULL,
    shipment_event_type_description varchar(350) NOT NULL
);


CREATE TABLE unit_of_measure (
    unit_of_measure_code varchar(3) PRIMARY KEY,
    unit_of_measure_description varchar(50) NOT NULL
);


CREATE TABLE hs_code (
    hs_code varchar(10) PRIMARY KEY,
    hs_code_description varchar(250) NOT NULL
);

CREATE TABLE receipt_delivery_type (
    receipt_delivery_type_code varchar(3) PRIMARY KEY,
    receipt_delivery_type_name varchar(50) NOT NULL,
    receipt_delivery_type_description varchar(300) NOT NULL
);


CREATE TABLE cargo_movement_type (
    cargo_movement_type_code varchar(3) PRIMARY KEY,
    cargo_movement_type_name varchar(50) NOT NULL,
    cargo_movement_type_description varchar(300) NOT NULL
);


CREATE TABLE address (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    name varchar(100) NULL,
    street varchar(100) NULL,
    street_number varchar(50) NULL,
    floor varchar(50) NULL,
    postal_code varchar(10) NULL,
    city varchar(65) NULL,
    state_region varchar(65) NULL,
    country varchar(75) NULL
);


CREATE TABLE location (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    location_name varchar(100) NULL,
    location_type varchar(4) NOT NULL,
    un_location_code varchar(5) NULL,
    address_id uuid NULL REFERENCES address (id),
    facility_code varchar(6) NULL,
    facility_code_list_provider varchar(4) NULL
);


CREATE TABLE carrier (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_name varchar(100),
    smdg_code varchar(3) NULL,
    nmfta_code varchar(4) NULL
);


CREATE TABLE party (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_name varchar(100) NULL,
    tax_reference_1 varchar(20) NULL,
    tax_reference_2 varchar(20) NULL,
    public_key varchar(500) NULL,
    address_id uuid NULL REFERENCES address (id)
);


CREATE TABLE party_contact_details (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_id uuid NOT NULL REFERENCES party(id),
    name varchar(100) NOT NULL,
    email varchar(100) NULL,
    phone varchar(30) NULL
);

CREATE TABLE party_identifying_code (
    dcsa_responsible_agency_code varchar(5) NOT NULL,
    party_id uuid NOT NULL REFERENCES party(id),
    code_list_name varchar(100),
    party_code varchar(100) NOT NULL
);


CREATE TABLE transport_document_type (
    transport_document_type_code varchar(3) PRIMARY KEY,
    transport_document_type_name varchar(20) NULL,
    transport_document_type_description varchar(500) NULL
);


CREATE TABLE vessel_type (
    vessel_type_code varchar(4) PRIMARY KEY,
    vessel_type_name varchar(100) NULL,
    unece_concatenated_means_of_transport_code varchar(4),
    vessel_type_description varchar(100) NOT NULL
);


CREATE TABLE vessel (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vessel_imo_number varchar(7) NULL UNIQUE,
    vessel_name varchar(35) NULL,
    vessel_flag char(2) NULL,
    vessel_call_sign varchar(10) NULL,
    vessel_operator_carrier_id uuid NULL REFERENCES carrier (id),
    is_dummy boolean NOT NULL default false,
    length_overall numeric NULL,
    width numeric NULL,
    vessel_type_code varchar(4) NULL REFERENCES vessel_type (vessel_type_code),
    dimension_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CONSTRAINT dimension_unit CHECK (dimension_unit IN ('FOT','MTR'))
);


CREATE TABLE vessel_sharing_agreement_type (
    vessel_sharing_agreement_type_code varchar(3) NOT NULL PRIMARY KEY,
    vessel_sharing_agreement_type_name varchar(50) NULL,
    vessel_sharing_agreement_type_description varchar(250) NOT NULL
);


CREATE TABLE vessel_sharing_agreement (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    vessel_sharing_agreement_name varchar(50) NULL,
    vessel_sharing_agreement_type_code varchar(3) NOT NULL REFERENCES vessel_sharing_agreement_type(vessel_sharing_agreement_type_code)
);


CREATE TABLE tradelane (
    id varchar(8) PRIMARY KEY,
    tradelane_name varchar(150) NOT NULL,
    vessel_sharing_agreement_id uuid NOT NULL REFERENCES vessel_sharing_agreement(id)
);


CREATE TABLE service (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_id uuid NULL REFERENCES carrier (id),
    carrier_service_code varchar(11),
    carrier_service_name varchar(50),
    tradelane_id varchar(8) NULL REFERENCES tradelane(id),
    universal_service_reference varchar(8) NULL CHECK (universal_service_reference ~ '^SR\d{5}[A-Z]$')
);


CREATE TABLE voyage (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_voyage_number varchar(50) NOT NULL,
    universal_voyage_reference varchar(5) NULL,
    service_id uuid NULL REFERENCES service (id) INITIALLY DEFERRED
);


CREATE TABLE booking (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    carrier_booking_request_reference varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type(shipment_event_type_code) CHECK(document_status IN ('RECE', 'PENU', 'REJE', 'CONF','PENC', 'CANC', 'DECL', 'CMPL')),
    receipt_type_at_origin varchar(3) NOT NULL REFERENCES receipt_delivery_type(receipt_delivery_type_code),
    delivery_type_at_destination varchar(3) NOT NULL REFERENCES receipt_delivery_type(receipt_delivery_type_code),
    cargo_movement_type_at_origin varchar(3) NOT NULL REFERENCES cargo_movement_type(cargo_movement_type_code),
    cargo_movement_type_at_destination varchar(3) NOT NULL REFERENCES cargo_movement_type(cargo_movement_type_code),
    booking_request_datetime timestamp with time zone NOT NULL,
    service_contract_reference varchar(30) NULL,
    payment_term_code varchar(3) NULL,
    is_partial_load_allowed boolean NOT NULL,
    is_export_declaration_required boolean NOT NULL,
    export_declaration_reference varchar(35) NULL,
    is_import_license_required boolean NOT NULL,
    import_license_reference varchar(35) NULL,
    is_ams_aci_filing_required boolean NULL,
    is_destination_filing_required boolean NULL,
    contract_quotation_reference varchar(35) NULL,
    incoterms varchar(3) NULL,
    invoice_payable_at_id uuid NULL REFERENCES location(id),
    expected_departure_date date NULL,
    expected_arrival_at_place_of_delivery_start_date date NULL CHECK ((expected_arrival_at_place_of_delivery_start_date IS NULL) OR (expected_arrival_at_place_of_delivery_end_date IS NULL) OR expected_arrival_at_place_of_delivery_start_date <= expected_arrival_at_place_of_delivery_end_date),
    expected_arrival_at_place_of_delivery_end_date date NULL,
    transport_document_type_code varchar(3) NULL REFERENCES transport_document_type(transport_document_type_code),
    transport_document_reference varchar(20) NULL,
    booking_channel_reference varchar(20) NULL,
    communication_channel_code varchar(2) NOT NULL,
    is_equipment_substitution_allowed boolean NOT NULL,
    vessel_id uuid NULL REFERENCES vessel(id),
    declared_value_currency_code varchar(3) NULL,
    declared_value real NULL,
    place_of_issue_id uuid NULL REFERENCES location(id),
    voyage_id UUID NULL REFERENCES voyage(id)
);

CREATE INDEX ON booking (id);


CREATE TABLE shipment (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    booking_id uuid NOT NULL REFERENCES booking(id),
    carrier_id uuid NOT NULL REFERENCES carrier(id),
    carrier_booking_reference varchar(35) NOT NULL UNIQUE,
    terms_and_conditions text NULL,
    confirmation_datetime timestamp with time zone NOT NULL
);


CREATE TABLE active_reefer_settings (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    is_gen_set_required boolean NOT NULL,
    is_pre_cooling_required boolean NOT NULL,
    is_cold_treatment_required boolean NOT NULL,
    is_ventilation_open boolean NOT NULL,
    is_drainholes_open boolean NOT NULL,
    is_bulb_mode boolean NOT NULL,
    is_controlled_atmosphere_required boolean NOT NULL,
    temperature_setpoint real NOT NULL,
    temperature_unit varchar(3) NOT NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (temperature_unit IN ('CEL','FAH')),
    o2_setpoint real NULL,
    co2_setpoint real NULL,
    humidity_setpoint real NULL,
    air_exchange_setpoint real NULL,
    air_exchange_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (air_exchange_unit IN ('MQH','FQH'))
);


CREATE TABLE requested_equipment_group (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    booking_id uuid NULL REFERENCES booking (id),
    shipment_id uuid NULL REFERENCES shipment (id),
    iso_equipment_code varchar(4) NOT NULL,
    units int NOT NULL,
    is_shipper_owned boolean NOT NULL DEFAULT false,
    active_reefer_settings_id uuid NULL REFERENCES active_reefer_settings (id)
);

CREATE INDEX ON requested_equipment_group (booking_id);


CREATE TABLE confirmed_equipment (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  shipment_id uuid NULL REFERENCES shipment (id),
  iso_equipment_code varchar(4) NOT NULL,
  units int NOT NULL CHECK (units > 0),
  list_order int NOT NULL DEFAULT 0
);


CREATE TABLE commodity (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    booking_id uuid NOT NULL REFERENCES booking(id),
    commodity_type varchar(550) NOT NULL,
    cargo_gross_weight real NULL,
    cargo_gross_weight_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (cargo_gross_weight_unit IN ('KGM','LBR')),
    cargo_gross_volume real NULL,
    cargo_gross_volume_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (cargo_gross_volume_unit IN ('MTQ','FTQ')),
    number_of_packages integer NULL,
    export_license_issue_date date NULL,
    export_license_expiry_date date NULL
);

CREATE INDEX ON commodity (booking_id);


CREATE TABLE requested_equipment_commodity (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    requested_equipment_id uuid NOT NULL REFERENCES requested_equipment_group (id),
    commodity_id uuid NOT NULL REFERENCES commodity(id)
);


CREATE TABLE shipment_cutoff_time (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    shipment_id uuid NOT NULL REFERENCES shipment(id),
    cut_off_time_code varchar(3) NOT NULL,
    cut_off_time timestamp with time zone NOT NULL,
    list_order int NOT NULL default 0,
    UNIQUE (shipment_id, cut_off_time_code)
);



CREATE TABLE displayed_address (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    address_line_1 varchar(35),
    address_line_2 varchar(35),
    address_line_3 varchar(35),
    address_line_4 varchar(35),
    address_line_5 varchar(35)
);


CREATE TABLE shipping_instruction (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    shipping_instruction_reference  varchar(100) NOT NULL DEFAULT uuid_generate_v4()::text,
    document_status varchar(4) NOT NULL REFERENCES shipment_event_type(shipment_event_type_code) CHECK(document_status IN ('RECE','PENU','DRFT','PENA','APPR','ISSU','SURR','VOID')),
    is_shipped_onboard_type boolean NOT NULL,
    number_of_copies_with_charges integer NULL,
    number_of_copies_without_charges integer NULL,
    number_of_originals_with_charges integer NULL,
    number_of_originals_without_charges integer NULL,
    is_electronic boolean NOT NULL,
    is_to_order boolean NOT NULL,
    place_of_issue_id uuid NULL REFERENCES location(id),
    invoice_payable_at_id uuid NULL REFERENCES location(id),
    transport_document_type_code varchar(3) NULL REFERENCES transport_document_type(transport_document_type_code),
    displayed_name_for_place_of_receipt uuid NULL REFERENCES displayed_address(id),
    displayed_name_for_port_of_load uuid NULL REFERENCES displayed_address(id),
    displayed_name_for_port_of_discharge uuid NULL REFERENCES displayed_address(id),
    displayed_name_for_place_of_delivery uuid NULL REFERENCES displayed_address(id),
    amendment_to_transport_document_id uuid NULL
);


CREATE TABLE transport_document (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    transport_document_reference varchar(20) NOT NULL DEFAULT LEFT(uuid_generate_v4()::text, 20),
    place_of_issue_id uuid NULL REFERENCES location(id),
    issue_date date NULL,
    shipped_onboard_date date NULL,
    received_for_shipment_date date NULL,
    carrier_id uuid NOT NULL REFERENCES carrier(id),
    shipping_instruction_id uuid NOT NULL REFERENCES shipping_instruction (id),
    number_of_rider_pages integer NULL,
    issuing_party_id uuid NOT NULL REFERENCES party(id),
    declared_value_currency_code varchar(3) NULL,
    declared_value real NULL
);

ALTER TABLE shipping_instruction
    ADD FOREIGN KEY (amendment_to_transport_document_id) REFERENCES transport_document (id);


CREATE TABLE bkg_requested_change (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    path varchar(500) NULL,
    message varchar(500) NOT NULL,
    booking_id uuid REFERENCES booking (id),
    element_order int NOT NULL default 0
);


CREATE TABLE si_requested_change (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    path varchar(500) NULL,
    message varchar(500) NOT NULL,
    shipping_instruction_id uuid REFERENCES shipping_instruction (id),
    element_order int NOT NULL default 0
);

CREATE TABLE carrier_clauses (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    clause_content text NOT NULL
);


CREATE TABLE shipment_carrier_clauses (
    carrier_clause_id uuid NOT NULL REFERENCES carrier_clauses (id),
    shipment_id uuid NULL REFERENCES shipment (id),
    transport_document_id uuid NULL REFERENCES transport_document (id)
);

CREATE TABLE document_party (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    party_id uuid NOT NULL REFERENCES party (id),
    shipping_instruction_id uuid NULL REFERENCES shipping_instruction (id),
    shipment_id uuid NULL REFERENCES shipment (id),
    party_function varchar(3) NOT NULL,
    is_to_be_notified boolean NOT NULL,
    booking_id uuid NULL REFERENCES booking(id),
    displayed_address_id uuid NULL REFERENCES displayed_address(id)
);

-- Supporting FK constraints
CREATE INDEX ON document_party (party_id);
CREATE INDEX ON document_party (shipment_id);
CREATE INDEX ON document_party (shipping_instruction_id);
CREATE INDEX ON document_party (booking_id);


CREATE TABLE charge (
    id varchar(100) PRIMARY KEY,
    transport_document_id uuid NOT NULL REFERENCES transport_document(id),
    shipment_id uuid NULL REFERENCES shipment (id),
    charge_name varchar(50) NOT NULL,
    currency_amount real NOT NULL,
    currency_code varchar(3) NOT NULL,
    payment_term_code varchar(3) NOT NULL,
    calculation_basis varchar(50) NOT NULL,
    unit_price real NOT NULL,
    quantity real NOT NULL
);


CREATE TABLE equipment (
    equipment_reference varchar(15) PRIMARY KEY,    -- The unique identifier for the equipment, which should follow the BIC ISO Container Identification Number where possible. According to ISO 6346, a container identification code consists of a 4-letter prefix and a 7-digit number (composed of a 3-letter owner code, a category identifier, a serial number and a check-digit). If a container does not comply with ISO 6346, it is suggested to follow Recommendation #2 “Container with non-ISO identification” from SMDG.
    iso_equipment_code varchar(4) NOT NULL,
    tare_weight real NOT NULL,
    total_max_weight real null,
    weight_unit varchar(3) NOT NULL REFERENCES unit_of_measure(unit_of_measure_code)  CHECK (weight_unit IN ('KGM','LBR'))
);

-- Supporting FK constraints
CREATE INDEX ON equipment (equipment_reference);



CREATE TABLE package_code (
    package_code varchar(3) PRIMARY KEY,
    package_code_description varchar(50) NOT NULL
);


CREATE TABLE utilized_transport_equipment (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    equipment_reference varchar(15) NOT NULL REFERENCES equipment (equipment_reference),
    cargo_gross_weight real NULL,
    cargo_gross_weight_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (cargo_gross_weight_unit IN ('KGM','LBR')),
    is_shipper_owned boolean NOT NULL
);

-- Supporting FK constraints
CREATE INDEX ON utilized_transport_equipment (equipment_reference);


CREATE TABLE consignment_item (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    description_of_goods text NOT NULL,
    shipping_instruction_id uuid NOT NULL REFERENCES shipping_instruction (id),
    shipment_id uuid NOT NULL REFERENCES shipment (id),
    commodity_id uuid NULL REFERENCES commodity (id),
    si_entry_order int NOT NULL default 0
);

CREATE TABLE hs_code_item (
    commodity_id uuid NULL REFERENCES commodity (id),
    consignment_item_id uuid NULL REFERENCES consignment_item (id),
    hs_code varchar(10) NOT NULL REFERENCES hs_code (hs_code),
    element_order int NOT NULL default 0
);

CREATE INDEX ON hs_code_item (commodity_id);
CREATE INDEX ON hs_code_item (consignment_item_id);


-- Supporting FK constraints
CREATE INDEX ON consignment_item (shipping_instruction_id);
CREATE INDEX ON consignment_item (shipment_id);
CREATE INDEX ON consignment_item (commodity_id);


CREATE TABLE cargo_item (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    consignment_item_id uuid NOT NULL REFERENCES consignment_item(id),
    weight real NOT NULL,
    volume real NULL,
    weight_unit varchar(3) NOT NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (weight_unit IN ('KGM','LBR')),
    volume_unit varchar(3) NULL REFERENCES unit_of_measure(unit_of_measure_code) CHECK (volume_unit IN ('MTQ','FTQ')),
    utilized_transport_equipment_id uuid NOT NULL REFERENCES utilized_transport_equipment (id)
);

-- Supporting FK constraints
CREATE INDEX ON cargo_item (consignment_item_id);
CREATE INDEX ON cargo_item (utilized_transport_equipment_id);


CREATE TABLE shipping_mark (
    cargo_item uuid NOT NULL REFERENCES cargo_item (id),
    shipping_mark varchar(500) NOT NULL,
    element_order int NOT NULL default 0
);

CREATE INDEX ON shipping_mark (cargo_item);


CREATE TABLE reference (
    reference_type_code varchar(3) NOT NULL,
    reference_value varchar(100) NOT NULL,
    shipment_id uuid NULL REFERENCES shipment (id),
    shipping_instruction_id uuid NULL REFERENCES shipping_instruction (id),
    booking_id uuid NULL REFERENCES booking(id),
    consignment_item_id uuid NULL REFERENCES consignment_item(id)
);

CREATE INDEX ON reference (booking_id);
CREATE INDEX ON reference (consignment_item_id);


CREATE TABLE customs_reference (
  id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  shipping_instruction_id uuid NULL REFERENCES shipping_instruction (id),
  consignment_item_id uuid NULL REFERENCES consignment_item(id),
  utilized_transport_equipment_id uuid NULL REFERENCES utilized_transport_equipment(id),
  list_order int NOT NULL default 0,
  type varchar(50) NOT NULL,
  country_code varchar(2) NOT NULL CHECK ( country_code ~ '^[A-Z]{2}$' ),
  value varchar(100) NOT NULL
);


CREATE TABLE seal (
    utilized_transport_equipment_id uuid NOT NULL REFERENCES utilized_transport_equipment (id),
    seal_number varchar(15) NOT NULL,
    seal_source_code varchar(5),
    seal_type_code varchar(5)
);
-- Supporting FK constraints
CREATE INDEX ON seal (utilized_transport_equipment_id);
CREATE INDEX ON seal (seal_source_code);
CREATE INDEX ON seal (seal_type_code);



CREATE TABLE shipment_location (
    shipment_id uuid NULL REFERENCES shipment (id),
    booking_id uuid NULL REFERENCES booking(id),
    location_id uuid NOT NULL REFERENCES location (id),
    shipment_location_type_code varchar(3) NOT NULL,
    event_date_time timestamp with time zone NULL --optional datetime indicating when the event at the location takes place
);

-- Supporting FK constraints
-- Note the omission of INDEX for "location_id" is deliberate; we rely on the implicit INDEX from the
-- UNIQUE constraint for that.
CREATE INDEX ON shipment_location (shipment_location_type_code);
CREATE INDEX ON shipment_location (shipment_id);
CREATE INDEX ON shipment_location (booking_id);


CREATE TABLE port_call_status_type (
    port_call_status_type_code varchar(4) NOT NULL PRIMARY KEY,
    port_call_status_type_name varchar(30) NOT NULL,
    port_call_status_type_description varchar(250) NOT NULL
);


CREATE TABLE transport_plan_stage_type (
    transport_plan_stage_code varchar(3) PRIMARY KEY,
    transport_plan_stage_name varchar(100) NOT NULL,
    transport_plan_stage_description varchar(250) NOT NULL
);

-- NOT a 1:1 with the DCSA IM version of the similarly named entity
CREATE TABLE shipment_transport (
    shipment_id uuid NULL REFERENCES shipment(id),
    transport_plan_stage_sequence_number integer NOT NULL,
    transport_plan_stage_code varchar(3) NOT NULL REFERENCES transport_plan_stage_type(transport_plan_stage_code),
    dcsa_transport_type varchar(50) NULL,
    planned_arrival_date date NOT NULL,
    planned_departure_date date NOT NULL,
    load_location_id uuid NOT NULL REFERENCES location(id),
    discharge_location_id uuid NOT NULL REFERENCES location(id),
    vessel_imo_number varchar(7) NULL,
    vessel_name varchar(35) NULL,
    carrier_import_voyage_number varchar(50) NULL,
    carrier_export_voyage_number varchar(50) NULL,
    carrier_service_code varchar(11) NULL,
    universal_import_voyage_reference varchar(5) NULL,
    universal_export_voyage_reference varchar(5) NULL,
    universal_service_reference varchar(8) NULL
);
CREATE TABLE advance_manifest_filing (
	manifest_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  shipment_id uuid NOT NULL REFERENCES shipment(id),
  manifest_type_code varchar(50) NOT NULL,
  country_code varchar(2) NOT NULL,
	list_order int NOT NULL default 0
);
CREATE TABLE advance_manifest_filing_ebl (
	manifest_id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
  shipping_instruction_id uuid REFERENCES shipping_instruction (id),
  manifest_type_code varchar(50) NOT NULL,
  country_code varchar(2) NOT NULL,
  filing_performed_by varchar(10) NOT NULL,
  self_filer_code varchar(100),
	list_order int NOT NULL default 0
);
