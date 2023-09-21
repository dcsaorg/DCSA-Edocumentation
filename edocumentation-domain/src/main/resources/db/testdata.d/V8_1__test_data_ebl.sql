
INSERT INTO address (
    id,
    name,
    street,
    street_number,
    floor,
    postal_code,
    city,
    state_region,
    country
) VALUES (
    '8fecc6d0-2a78-401d-948a-b9753f6b53d5'::uuid,
    'Lukas',
    'Rohrdamm',
    '81',
    '5',
    '32108',
    'Bad Salzuflen Grastrup-hölsen',
    'Nordrhein-Westfalen',
    'Germany'
);

INSERT INTO location (
    id,
    location_name,
    location_type,
    un_location_code,
    address_id
) VALUES (
    'c703277f-84ca-4816-9ccf-fad8e202d3b6',
    'Hamburg',
    'ADDR',
    'DEHAM',
    '8fecc6d0-2a78-401d-948a-b9753f6b53d5'::uuid
);


INSERT INTO location (
    id,
    location_name,
    location_type,
    un_location_code
) VALUES (
    uuid('84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d'),
    'The Factory',
    'UNLO',
    'NLRTM'
), (
    uuid('286c605e-4043-11eb-9c0b-7b4196cf71fa'),
    'Singapore',
    'UNLO',
    'SGSIN'
), (
    uuid('770b7624-403d-11eb-b44b-d3f4ad185386'),
    'Rotterdam',
    'UNLO',
    'NLRTM'
), (
    uuid('770b7624-403d-11eb-b44b-d3f4ad185387'),
    'Genneb',
    'UNLO',
    'USMIA'
), (
    uuid('770b7624-403d-11eb-b44b-d3f4ad185388'),
    'Nijmegen',
    'UNLO',
    'USMIA'
), (
    uuid('7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'),
    'Miami',
    'UNLO',
    'USMIA'
);

INSERT INTO location (
    id,
    location_name,
    location_type,
    un_location_code
) VALUES (
    '01670315-a51f-4a11-b947-ce8e245128eb',
    'København',
    'UNLO',
    'DKCPH'
);

INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time,
    invoice_payable_at_id
) VALUES (
    'b521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'CARRIER_BOOKING_REQUEST_REFERENCE_01',
    'RECE',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_01',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_01',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_01',
    TRUE,
    'FCA',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_01',
    'BOOKING_CHA_REF_01',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    NULL,
    DATE '2021-12-09',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'
);

INSERT INTO booking (
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    invoice_payable_at_id,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time
) VALUES (
    'CARRIER_BOOKING_REQUEST_REFERENCE_02',
    'RECE',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_02',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_02',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_02',
    TRUE,
    'FCA',
    '84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_02',
    'BOOKING_CHA_REF_02',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2021-12-16'
);


INSERT INTO shipment (
    carrier_id,
    booking_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
) VALUES (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    'BR1239719871',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
);

INSERT INTO shipment (
    carrier_id,
    booking_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
) VALUES (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_02'),
    'CR1239719872',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
);

/**
 * Data used in integration tests - Do not modify - make your own data
 */
INSERT INTO shipment (
    carrier_id,
    booking_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
) VALUES (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    'bca68f1d3b804ff88aaa1e43055432f7',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    '832deb4bd4ea4b728430b857c59bd057',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    '994f0c2b590347ab86ad34cd1ffba505',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    '02c965382f5a41feb9f19b24b5fe2906',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    'AR1239719871',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
);

INSERT INTO reference (
    reference_type_code,
    reference_value,
    shipment_id
) VALUES (
    'CR',
    'AB-123743CR',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871')
), (
    'PO',
    'PO0027',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871')
), (
    'CR',
    'BC-346267CR',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'CR1239719872')
), (
    'PO',
    'PO0028',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'CR1239719872')
);

INSERT INTO voyage (
    carrier_voyage_number
) VALUES
    ('2106W'),
    ('2107E'),
    ('2108W'),
    ('2218W'),
    ('2219E'),
    ('2418W'),
    ('2419E'),
    ('3418W'),
    ('3419E');

INSERT INTO vessel (
    vessel_imo_number,
    vessel_name,
    vessel_flag,
    vessel_call_sign,
    vessel_operator_carrier_id
) VALUES (
    '9321483',
    'Emma Maersk',
    'DK',
    null,
    (SELECT id FROM carrier WHERE smdg_code = 'MSK')
);

INSERT INTO shipment_transport (
    shipment_id,
    transport_plan_stage_sequence_number,
    transport_plan_stage_code,
    load_location_id,
    discharge_location_id,
    planned_departure_date,
    planned_arrival_date,
    vessel_imo_number,
    vessel_name,
    carrier_import_voyage_number,
    carrier_export_voyage_number,
    carrier_service_code,
    dcsa_transport_type,
    is_under_shippers_responsibility
) VALUES (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    1,
    'PRC',
    '84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d'::uuid,
    '770b7624-403d-11eb-b44b-d3f4ad185386'::uuid,
    '2021-12-01',
    '2021-12-01',
    null,
    null,
    null,
    null,
    null,
    'TRUCK',
    false
), (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    2,
    'MNC',
    '770b7624-403d-11eb-b44b-d3f4ad185386'::uuid,
    '7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'::uuid,
    '2021-12-01',
    '2021-12-10',
    '9321483',
    'Emma Maersk',
    '2106W',
    '2107E',
    null,
    'VESSEL',
    false
), (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    3,
    'ONC',
    '7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'::uuid,
    '770b7624-403d-11eb-b44b-d3f4ad185387'::uuid,
    '2021-12-10',
    '2021-12-11',
    null,
    null,
    null,
    null,
    null,
    'RAIL',
    false
), (
  (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
  1,
  'MNC',
  '770b7624-403d-11eb-b44b-d3f4ad185386'::uuid,
  '7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'::uuid,
  '2021-12-01',
  '2021-12-10',
  '9321483',
  'Emma Maersk',
  '2106W',
  '2107E',
  null,
  'VESSEL',
  false
);

INSERT INTO party (
    id,
    party_name
) VALUES (
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9',
    'Malwart'
), (
    '8dd9a4c4-4039-11eb-8770-0b2b19847fab',
    'Malwart Düsseldorf'
), (
     '9dd9a4c4-4039-11eb-8770-0b2b19847fab',
     'Malwart Lyngy'
 );

 INSERT INTO party_contact_details (
    id,
    party_id,
    name,
    email,
    phone,
    url
) VALUES (
    'b24d099e-a6f6-404e-b082-776f7f589023'::uuid,
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9',
    'DCSA',
    'info@dcsa.org',
    '+31123456789',
    'https://www.dcsa.org'
), (
    'b24d099e-a6f6-404e-b082-776f7f589064'::uuid,
    '8dd9a4c4-4039-11eb-8770-0b2b19847fab',
    'DCSA',
    'info@dcsa.org',
    '+31123456789',
    'https://www.dcsa.org'
), (
    'b24d099e-a6f6-404e-b082-776f7f589022'::uuid,
    '9dd9a4c4-4039-11eb-8770-0b2b19847fab',
    'DCSA',
    'info@dcsa.org',
    '+31123456789',
    'https://www.dcsa.org'
);

INSERT INTO document_party (
    party_id,
    shipment_id,
    party_function,
    is_to_be_notified,
    booking_id
) VALUES (
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    'OS',
    true,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01')
), (
    '8dd9a4c4-4039-11eb-8770-0b2b19847fab',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    'CN',
    true,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01')
), (
      '9dd9a4c4-4039-11eb-8770-0b2b19847fab',
      (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
      'CN',
      true,
      (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01')
  );

INSERT INTO shipment_location (
    shipment_id,
    booking_id,
    location_id,
    shipment_location_type_code
) VALUES (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    uuid('84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d'),
    'PRE'
), (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    uuid('770b7624-403d-11eb-b44b-d3f4ad185386'),
    'POL'
), (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    uuid('7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'),
    'POD'
), (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    uuid('770b7624-403d-11eb-b44b-d3f4ad185387'),
    'PDE'
), (
   (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
   (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
   uuid('770b7624-403d-11eb-b44b-d3f4ad185386'),
   'POL'
), (
   (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
   (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
   uuid('7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'),
   'POD'
);

INSERT INTO equipment (
    equipment_reference,
    iso_equipment_code,
    tare_weight,
    weight_unit
) VALUES (
    'BMOU2149612',
    '22G1',
    2000,
    'KGM'
);

/**
 * Data used in integration tests - Do not modify - make your own data
 */
INSERT INTO utilized_transport_equipment (
    id,
    equipment_reference,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    is_shipper_owned
) VALUES (
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53'),
    'BMOU2149612',
    4000,
    'KGM',
    false
),(
    uuid('44068608-da9b-4039-b074-d9ac27ddbfbf'),
    'BMOU2149612',
    4000,
    'KGM',
    false
),(
    uuid('56812ad8-5d0b-4cbc-afca-e97f2f3c89de'),
    'BMOU2149612',
    4000,
    'KGM',
    false
),(
    uuid('ca030eb6-009b-411c-985c-527ce008b35a'),
    'BMOU2149612',
    4000,
    'KGM',
    false
),(
    uuid('aa030eb6-009b-411c-985c-527ce008b35a'),
    'BMOU2149612',
    4000,
    'KGM',
    false
);

INSERT INTO shipping_instruction (
    id,
    shipping_instruction_reference,
    document_status,
    is_shipped_onboard_type,
    number_of_copies_with_charges,
    number_of_originals_with_charges,
    is_electronic,
    is_to_order,
    created_date_time,
    updated_date_time
) VALUES (
    '01670315-a51f-4a11-b947-ce8e245128eb',
    'SI_REF_1',
    'RECE',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-12-24',
    DATE '2021-12-31'
);

/**
 * Data used in integration tests - Do not modify - make your own data
 */
INSERT INTO shipping_instruction (
    id,
    shipping_instruction_reference,
    document_status,
    is_shipped_onboard_type,
    number_of_copies_with_charges,
    number_of_originals_with_charges,
    is_electronic,
    is_to_order,
    created_date_time,
    updated_date_time
) VALUES (
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    'SI_REF_2',
    'APPR',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2022-01-24',
    DATE '2022-01-31'
),(
    '877ce0f8-3126-45f5-b22e-2d1d27d42d85',
    'SI_REF_3',
    'RECE',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2022-02-01',
    DATE '2022-02-07'
),(
    '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
    'SI_REF_4',
    'RECE',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-02-08',
    DATE '2021-02-09'
),(
    'cb6354c9-1ceb-452c-aed0-3cb25a04647a',
    'SI_REF_5',
    'PENU',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-02-08',
    DATE '2021-02-09'
),(
    '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    'SI_REF_6',
    'APPR',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2022-03-01',
    DATE '2022-03-07'
),(
      '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
      'SI_REF_7',
      'APPR',
      TRUE,
      2,
      4,
      TRUE,
      TRUE,
      DATE '2022-03-01',
      DATE '2022-03-07'
);


INSERT INTO commodity (
    id,
    booking_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit
) VALUES (
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Expensive Shoes',
    4000,
    'KGM'
), (
    '2219e859-e3b5-4e87-80b8-32e9f77cca04',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Massive Yacht',
    4000,
    'KGM'
), (
    '877ce0f8-3126-45f5-b22e-2d1d27d42d85',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Leather Jackets',
    4000,
    'KGM'
), (
    '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Air ballons',
    4000,
    'KGM'
), (
    'cb6354c9-1ceb-452c-aed0-3cb25a04647a',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Leather Jackets',
    4000,
    'KGM'
), (
    '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Leather Jackets',
    4000,
    'KGM'
), (
    '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    (SELECT booking.id FROM booking JOIN shipment ON booking.id=shipment.booking_id WHERE carrier_booking_reference = 'BR1239719871'),
    'Leather Jackets',
    4000,
    'KGM'
);

INSERT INTO hs_code_item (
  commodity_id,
  hs_code
) VALUES (
  '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
  '411510'
), (
  '2219e859-e3b5-4e87-80b8-32e9f77cca04',
  '720711'
), (
  '877ce0f8-3126-45f5-b22e-2d1d27d42d85',
  '411510'
), (
  '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
  '411510'
), (
  'cb6354c9-1ceb-452c-aed0-3cb25a04647a',
  '411510'
), (
  '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
  '411510'
), (
  '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
  '411510'
);

INSERT INTO consignment_item (
    id,
    shipping_instruction_id,
    shipment_id,
    commodity_id,
    description_of_goods,
    si_entry_order
) VALUES (
    '10f41e70-0cae-47cd-8eb8-4ee6f05e85c1',
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    'Expensive Shoes',
    0
), (
    'c7104528-66d5-4d11-9b82-7af30e84d664',
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    'Massive Yacht',
    1
), (
    '20e8aca5-4524-4ff9-a258-96c506966388',
    '877ce0f8-3126-45f5-b22e-2d1d27d42d85',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'bca68f1d3b804ff88aaa1e43055432f7'),
    '877ce0f8-3126-45f5-b22e-2d1d27d42d85',
    'Leather Jackets',
    0
), (
    'ca4ff535-407f-41ab-a009-830ddf06bdba',
    '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
    (SELECT id FROM shipment WHERE carrier_booking_reference = '832deb4bd4ea4b728430b857c59bd057'),
    '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
    'Air ballons',
    0
), (
    '83ec9f50-2eab-42f7-892d-cad2d25f3b9e',
    'cb6354c9-1ceb-452c-aed0-3cb25a04647a',
    (SELECT id FROM shipment WHERE carrier_booking_reference = '994f0c2b590347ab86ad34cd1ffba505'),
    'cb6354c9-1ceb-452c-aed0-3cb25a04647a',
    'Leather Jackets',
    0
), (
    '824e8fed-d181-4079-b6ca-9d9069a2a738',
    '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    (SELECT id FROM shipment WHERE carrier_booking_reference = '02c965382f5a41feb9f19b24b5fe2906'),
    '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    'Leather Jackets',
    0
), (
    '1829548e-5938-4adc-a08e-3af55d8ccf63',
    '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
    '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    'Leather Jackets',
    0
);

INSERT INTO hs_code_item (
  consignment_item_id,
  hs_code   -- TODO: HS Code probably does not match the description of the goods for any of these.
) VALUES (
  '10f41e70-0cae-47cd-8eb8-4ee6f05e85c1',
  '411510'
), (
  'c7104528-66d5-4d11-9b82-7af30e84d664',
  '720711'
), (
  '20e8aca5-4524-4ff9-a258-96c506966388',
  '411510'
), (
  'ca4ff535-407f-41ab-a009-830ddf06bdba',
  '411510'
), (
  '83ec9f50-2eab-42f7-892d-cad2d25f3b9e',
  '411510'
), (
  '824e8fed-d181-4079-b6ca-9d9069a2a738',
  '411510'
), (
  '1829548e-5938-4adc-a08e-3af55d8ccf63',
  '411510'
);


INSERT INTO transport_document (
    transport_document_reference,
    place_of_issue_id,
    issue_date,
    shipped_onboard_date,
    received_for_shipment_date,
    carrier_id,
    shipping_instruction_id,
    number_of_rider_pages,
    created_date_time,
    updated_date_time,
    issuing_party_id
) VALUES (
    '1af13f0b-a1ea-4ff8',
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2020-11-25',
    DATE '2020-12-24',
    DATE '2020-12-31',
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    '877ce0f8-3126-45f5-b22e-2d1d27d42d85'::uuid,
    12,
    '2021-11-28T14:12:56+01:00'::timestamptz,
    '2021-12-01T07:41:00+08:30'::timestamptz,
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9'
), (
    '2b02401c-b2fb-5009',
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2020-11-25',
    DATE '2020-12-24',
    DATE '2020-12-31',
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d'::uuid,
    12,
    '2022-03-03T18:22:53Z'::timestamptz,
    '2022-03-05T13:56:12Z'::timestamptz,
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9'
), (
   '9b02401c-b2fb-5009',
   '01670315-a51f-4a11-b947-ce8e245128eb',
   DATE '2020-11-25',
   DATE '2020-12-24',
   DATE '2020-12-31',
   (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
   '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d'::uuid,
   12,
   '2022-03-03T18:22:53Z'::timestamptz,
   '2022-03-05T13:56:12Z'::timestamptz,
   '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9'
 );


INSERT INTO package_code(
    package_code,
    package_code_description
) VALUES (
    '123',
    'test description1'
), (
    '234',
    'test description2'
), (
    '456',
    'test description3'
), (
    '789',
    'test description4'
);

/**
 * Data used in integration tests - Do not modify - make your own data
 */
INSERT INTO cargo_item (
    consignment_item_id,
    weight,
    weight_unit,
    utilized_transport_equipment_id
) VALUES (
    '10f41e70-0cae-47cd-8eb8-4ee6f05e85c1',
    50.0,
    'KGM',
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53')
), (
    'c7104528-66d5-4d11-9b82-7af30e84d664',
    1000.0,
    'KGM',
    uuid('44068608-da9b-4039-b074-d9ac27ddbfbf')
), (
    (SELECT id FROM consignment_item WHERE shipping_instruction_id = '877ce0f8-3126-45f5-b22e-2d1d27d42d85'),
    23.5,
    'KGM',
    uuid('56812ad8-5d0b-4cbc-afca-e97f2f3c89de')
), (
    (SELECT id FROM consignment_item WHERE shipping_instruction_id = '877ce0f8-3126-45f5-b22e-2d1d27d42d85'),
    99.9,
    'KGM',
    uuid('44068608-da9b-4039-b074-d9ac27ddbfbf')
), (
    (SELECT id FROM consignment_item WHERE shipping_instruction_id = '770f11e5-aae2-4ae4-b27e-0c689ed2e333'),
    99.9,
    'KGM',
   uuid('44068608-da9b-4039-b074-d9ac27ddbfbf')
), (
    (SELECT id FROM consignment_item WHERE shipping_instruction_id = 'cb6354c9-1ceb-452c-aed0-3cb25a04647a'),
    23.5,
    'KGM',
    uuid('ca030eb6-009b-411c-985c-527ce008b35a')
), (
    (SELECT id FROM consignment_item WHERE shipping_instruction_id = '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d'),
    23.5,
    'KGM',
    uuid('ca030eb6-009b-411c-985c-527ce008b35a')
);

INSERT INTO cargo_item (
  id,
  consignment_item_id,
  weight,
  weight_unit,
  utilized_transport_equipment_id
  ) VALUES (
  '2d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
  (SELECT id FROM consignment_item WHERE shipping_instruction_id = '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d'),
  23.5,
  'KGM',
  uuid('aa030eb6-009b-411c-985c-527ce008b35a')
);

INSERT INTO shipping_mark (
  cargo_item,
  shipping_mark
) VALUES (
  '2d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
  'shipping marks'
);

INSERT INTO booking (
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time
) VALUES (
    'CARRIER_BOOKING_REQUEST_REFERENCE_03',
    'CONF',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_03',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_03',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_03',
    TRUE,
    'FCA',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_03',
    'BOOKING_CHA_REF_03',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    NULL,
    DATE '2021-12-09'
);

INSERT INTO booking (
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    invoice_payable_at_id,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time
) VALUES (
    'CARRIER_BOOKING_REQUEST_REFERENCE_04',
    'CONF',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_04',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_04',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_04',
    TRUE,
    'FCA',
    '84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_04',
    'BOOKING_CHA_REF_04',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2021-12-16'
);


INSERT INTO booking (
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time
) VALUES (
    'CARRIER_BOOKING_REQUEST_REFERENCE_05',
    'CONF',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_05',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_05',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_05',
    TRUE,
    'FCA',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_05',
    'BOOKING_CHA_REF_05',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    NULL,
    DATE '2021-12-09'
);

INSERT INTO booking (
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    invoice_payable_at_id,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time
) VALUES (
    'CARRIER_BOOKING_REQUEST_REFERENCE_06',
    'CONF',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_06',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_06',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_06',
    TRUE,
    'FCA',
    '84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_06',
    'BOOKING_CHA_REF_06',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2021-12-16'
);



INSERT INTO shipment (
    carrier_id,
    booking_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
) VALUES (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_03'),
    '43f615138efc4d3286b36402405f851b',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_04'),
    'e8e9d64172934a40aec82e4308cdf97a',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_05'),
    '6fe84758a4cc471fb5eb-4de63ddadc41',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
),
(
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_06'),
    '5dc92988f48a420495b786c224efce7d',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
);

INSERT INTO commodity(
    id,
    booking_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    export_license_issue_date,
    export_license_expiry_date
) VALUES (
    'a5b681bf-68a0-4f90-8cc6-79bf77d3b2a1'::uuid,
    'b521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'Hand Bags',
    1200.0,
    'KGM',
    NULL,
    NULL
);

INSERT INTO hs_code_item (
  commodity_id,
  hs_code
) VALUES (
  'a5b681bf-68a0-4f90-8cc6-79bf77d3b2a1'::uuid,
  '411510'
);

-------- ShipmentLocationRepository.findByTransportDocumentID BEGIN --------
------------------------------- DO NOT MODIFY ------------------------------

INSERT INTO party (
    id,
    party_name
) VALUES (
    '499918a2-d12d-4df6-840c-dd92357002df',
    'FTL International'
);

INSERT INTO party_contact_details (
    id,
    party_id,
    name,
    email,
    phone,
    url
) VALUES (
    '0a42252d-c8d5-4a0e-ab93-fa355992fb29'::uuid,
    '499918a2-d12d-4df6-840c-dd92357002df',
    'DCSA',
    'info@dcsa.org',
    '+31123456789',
    'https://www.dcsa.org'
);

INSERT INTO displayed_address (
    id,
    address_line_1,
    address_line_2
) VALUES (
    '8a5ccfed-f106-405b-a61f-c3a633e57ead'::uuid,
    'Gubener Str. 42',
    'Rhinstrasse 87'
);

INSERT INTO shipping_instruction (
    id,
    shipping_instruction_reference,
    document_status,
    is_shipped_onboard_type,
    number_of_copies_with_charges,
    number_of_originals_with_charges,
    is_electronic,
    is_to_order,
    created_date_time,
    updated_date_time,
    displayed_name_for_place_of_delivery
) VALUES (
    'a1c7b95d-3004-40a5-bae1-e379021b7782',
    'SI_REF_9',
    'RECE',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-12-24',
    DATE '2021-12-31',
    '8a5ccfed-f106-405b-a61f-c3a633e57ead'::uuid
);

INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time,
    invoice_payable_at_id
) VALUES (
    'a169d494-d6dd-4334-b951-512e4e16f075'::uuid,
    'KUBERNETES_IN_ACTION_01',
    'RECE',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_01',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_01',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_01',
    TRUE,
    'FCA',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_01',
    'BOOKING_CHA_REF_01',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    NULL,
    DATE '2021-12-09',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'
), (
    '59ede518-2224-4ecf-a0d0-4d641d365e1b'::uuid,
    'KUBERNETES_IN_ACTION_02',
    'RECE',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_01',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_01',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_01',
    TRUE,
    'FCA',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_01',
    'BOOKING_CHA_REF_01',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    NULL,
    DATE '2021-12-09',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'
);

INSERT INTO shipment (
    carrier_id,
    booking_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
) VALUES (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_01'),
    'E379021B7782',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
), (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_02'),
    'A379021B7782',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
);

INSERT INTO consignment_item (
    id,
    shipping_instruction_id,
    shipment_id,
    description_of_goods,
    si_entry_order
) VALUES (
    '0e98eef4-6ebd-47eb-bd6e-d3878b341b7f',
    'a1c7b95d-3004-40a5-bae1-e379021b7782',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'E379021B7782'),
    'Expensive shoes',
    0
), (
    '06c0e716-3128-4172-be09-7f82b7ec02ca',
    'a1c7b95d-3004-40a5-bae1-e379021b7782',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'E379021B7782'),
    'Slightly less expensive shoes',
    1
), (
    'cf1798fe-9447-4ea8-a4a6-9515de751d5e',
    'a1c7b95d-3004-40a5-bae1-e379021b7782',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'A379021B7782'),
    'Even more expensive shoes',
    2
);

INSERT INTO hs_code_item (
  consignment_item_id,
  hs_code -- TODO: HS Code probably does not match the description of the goods for any of these1
) VALUES (
  '0e98eef4-6ebd-47eb-bd6e-d3878b341b7f',
  '411510'
), (
  '06c0e716-3128-4172-be09-7f82b7ec02ca',
  '411510'
), (
  'cf1798fe-9447-4ea8-a4a6-9515de751d5e',
  '411510'
);


INSERT INTO transport_document (
    id,
    transport_document_reference,
    place_of_issue_id,
    issue_date,
    shipped_onboard_date,
    received_for_shipment_date,
    carrier_id,
    shipping_instruction_id,
    number_of_rider_pages,
    created_date_time,
    updated_date_time,
    issuing_party_id
) VALUES (
    'de561650-d43d-46af-88c3-0ab380bb5365'::uuid,
    '0cc0bef0-a7c8-4c03',
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2020-11-25',
    DATE '2020-12-24',
    DATE '2020-12-31',
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    'a1c7b95d-3004-40a5-bae1-e379021b7782'::uuid,
    12,
    '2021-11-28T14:12:56+01:00'::timestamptz,
    '2021-12-01T07:41:00+08:30'::timestamptz,
    '499918a2-d12d-4df6-840c-dd92357002df'
);

INSERT INTO location (
    id,
    location_name,
    location_type,
    un_location_code
) VALUES (
    'b4454ae5-dcd4-4955-8080-1f986aa5c6c3',
    'Copenhagen',
    'UNLO',
    'USMIA'
),(
    '1d09e9e9-dba3-4de1-8ef8-3ab6d32dbb40',
    'Orlando',
    'UNLO',
    'USMIA'
),(
    'ea9af21d-8471-47ac-aa59-e949ea74b08e',
    'Miami',
    'UNLO',
    'USMIA'
);

INSERT INTO shipment_location (
    shipment_id,
    booking_id,
    location_id,
    shipment_location_type_code
) VALUES (
    null,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_01'),
    uuid('b4454ae5-dcd4-4955-8080-1f986aa5c6c3'),
    'PRE'
),  (
    null,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_01'),
    uuid('1d09e9e9-dba3-4de1-8ef8-3ab6d32dbb40'),
    'POL'
), (
    null,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_01'),
    uuid('ea9af21d-8471-47ac-aa59-e949ea74b08e'),
    'POD'
), (
    null,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_02'),
    uuid('b4454ae5-dcd4-4955-8080-1f986aa5c6c3'),
    'PRE'
),  (
    null,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_02'),
    uuid('1d09e9e9-dba3-4de1-8ef8-3ab6d32dbb40'),
    'POL'
), (
    null,
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_02'),
    uuid('ea9af21d-8471-47ac-aa59-e949ea74b08e'),
    'POD'
);

INSERT INTO cargo_item (
    consignment_item_id,
    weight,
    weight_unit,
    utilized_transport_equipment_id
) VALUES (
    '0e98eef4-6ebd-47eb-bd6e-d3878b341b7f'::uuid,
    50.0,
    'KGM',
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53')
), (
    '06c0e716-3128-4172-be09-7f82b7ec02ca'::uuid,
    50.0,
    'KGM',
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53')
), (
    'cf1798fe-9447-4ea8-a4a6-9515de751d5e'::uuid,
    50.0,
    'KGM',
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53')
);

-------- ShipmentLocationRepository.findByTransportDocumentID END --------
------------------------------ DO NOT MODIFY -----------------------------





----------------- Data for ApproveTransportDocument BEGIN ----------------
------------------------------ DO NOT MODIFY -----------------------------

INSERT INTO party (
    id,
    party_name
) VALUES (
    '8e463a84-0a2d-47cd-9332-51e6cb36b635',
    'Superdæk Albertslund'
);

INSERT INTO party_contact_details (
    id,
    party_id,
    name,
    email,
    phone,
    url
) VALUES (
    '0ffc61f0-c74d-4a57-8d32-009a32247c29'::uuid,
    '8e463a84-0a2d-47cd-9332-51e6cb36b635',
    'DCSA',
    'info@dcsa.org',
    '+31123456789',
    'https://www.dcsa.org'
);

INSERT INTO shipping_instruction (
    id,
    shipping_instruction_reference,
    document_status,
    is_shipped_onboard_type,
    number_of_copies_with_charges,
    number_of_originals_with_charges,
    is_electronic,
    is_to_order,
    created_date_time,
    updated_date_time
) VALUES (
    '2c337fcc-2814-42b3-a752-f1847e74cba7',
    'SI_REF_10',
    'DRFT',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-12-24',
    DATE '2021-12-31'
);

INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    document_status,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
    booking_request_datetime,
    service_contract_reference,
    payment_term_code,
    is_partial_load_allowed,
    is_export_declaration_required,
    export_declaration_reference,
    is_import_license_required,
    import_license_reference,
    is_destination_filing_required,
    incoterms,
    expected_departure_date,
    transport_document_type_code,
    transport_document_reference,
    booking_channel_reference,
    communication_channel_code,
    is_equipment_substitution_allowed,
    vessel_id,
    declared_value_currency_code,
    declared_value,
    place_of_issue_id,
    updated_date_time,
    invoice_payable_at_id
) VALUES (
    '66802442-4702-4464-9d61-d659fdb7e33c'::uuid,
    'KUBERNETES_IN_ACTION_03',
    'CONF',
    'CY',
    'CFS',
    'FCL',
    'BB',
    DATE '2020-03-07',
    'SERVICE_CONTRACT_REFERENCE_01',
    'PRE',
    TRUE,
    TRUE,
    'EXPORT_DECLARATION_REFERENCE_01',
    FALSE,
    'IMPORT_LICENSE_REFERENCE_01',
    TRUE,
    'FCA',
    DATE '2020-03-07',
    'SWB',
    'TRANSPORT_DOC_REF_01',
    'BOOKING_CHA_REF_01',
    'EI',
    FALSE,
    (SELECT vessel.id FROM vessel WHERE vessel_imo_number = '9321483'),
    'WTK',
    12.12,
    NULL,
    DATE '2021-12-09',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'
);

INSERT INTO shipment (
    carrier_id,
    booking_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
) VALUES (
    (SELECT id FROM carrier WHERE smdg_code = 'MSK'),
    (SELECT id FROM booking WHERE carrier_booking_request_reference = 'KUBERNETES_IN_ACTION_03'),
    'D659FDB7E33C',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
);

INSERT INTO consignment_item (
    id,
    shipping_instruction_id,
    shipment_id,
    description_of_goods
) VALUES (
    '5d943239-23fc-4d5c-ab70-a33a469f9e59',
    '2c337fcc-2814-42b3-a752-f1847e74cba7',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'D659FDB7E33C'),
    'Expensive shoes'
);

INSERT INTO hs_code_item (
  consignment_item_id,
  hs_code
) VALUES (
  '5d943239-23fc-4d5c-ab70-a33a469f9e59',
  '411510' -- TODO: HS Code probably does not match the description of the goods.
);

INSERT INTO transport_document (
    id,
    transport_document_reference,
    place_of_issue_id,
    issue_date,
    shipped_onboard_date,
    received_for_shipment_date,
    carrier_id,
    shipping_instruction_id,
    number_of_rider_pages,
    created_date_time,
    updated_date_time,
    issuing_party_id
) VALUES (
    'cf48ad0a-9a4b-48a7-b752-c248fb5d88d9'::uuid,
    'c90a0ed6-ccc9-48e3',
    '01670315-a51f-4a11-b947-ce8e245128eb',
    DATE '2022-05-16',
    DATE '2022-05-15',
    DATE '2022-05-14',
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    '2c337fcc-2814-42b3-a752-f1847e74cba7'::uuid,
    12,
    '2021-11-28T14:12:56+01:00'::timestamptz,
    '2021-12-01T07:41:00+08:30'::timestamptz,
    '8e463a84-0a2d-47cd-9332-51e6cb36b635'
);

INSERT INTO cargo_item (
    consignment_item_id,
    weight,
    weight_unit,
    utilized_transport_equipment_id
) VALUES (
    '5d943239-23fc-4d5c-ab70-a33a469f9e59'::uuid,
    50.0,
    'KGM',
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53')
);

INSERT INTO charge (
    id,
    transport_document_id,
    shipment_id,
    charge_type,
    currency_amount,
    currency_code,
    payment_term_code,
    calculation_basis,
    unit_price,
    quantity
) VALUES (
    '4816e01a-446b-4bc0-812e-a3a447c85668',
    (SELECT id FROM transport_document WHERE transport_document_reference = '9b02401c-b2fb-5009'),
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
    'TBD',
    100.12,
    'EUR',
    'PRE',
    'WHAT',
    100.12,
    1.0
);

------------------ Data for ApproveTransportDocument END -----------------
------------------------------ DO NOT MODIFY -----------------------------

