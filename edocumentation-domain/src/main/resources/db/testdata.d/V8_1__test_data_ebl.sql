
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

INSERT INTO booking_data (
    id,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
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
    terms_and_conditions,
    invoice_payable_at_id
) VALUES (
    'b521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'CY',
    'CFS',
    'FCL',
    'BB',
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
    'TERMS AND CONDITIONS!',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'
);

INSERT INTO booking (
  carrier_booking_request_reference,
  carrier_booking_reference,
  booking_status,
  booking_data_id,
  last_confirmed_booking_data_id
) VALUES (
  'CARRIER_BOOKING_REQUEST_REFERENCE_01',
  'BR1239719871',
  'CONFIRMED',
  'b521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
  'b521dbdb-a12b-48f5-b489-8594349731bf'::uuid
);

INSERT INTO booking_data (
    id,
    receipt_type_at_origin,
    delivery_type_at_destination,
    cargo_movement_type_at_origin,
    cargo_movement_type_at_destination,
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
    terms_and_conditions,
    place_of_issue_id
) VALUES (
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'CY',
    'CFS',
    'FCL',
    'BB',
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
    'TERMS AND CONDITIONS!',
    '01670315-a51f-4a11-b947-ce8e245128eb'
);


INSERT INTO booking (
  carrier_booking_request_reference,
  carrier_booking_reference,
  booking_status,
  booking_data_id,
  last_confirmed_booking_data_id
) values (
  'CARRIER_BOOKING_REQUEST_REFERENCE_02',
  'CR1239719872',
  'CONFIRMED',
  'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
  'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid
);

/**
 * Data used in integration tests - Do not modify - make your own data
 */


INSERT INTO booking_data (
  id,
  receipt_type_at_origin,
  delivery_type_at_destination,
  cargo_movement_type_at_origin,
  cargo_movement_type_at_destination,
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
  terms_and_conditions,
  invoice_payable_at_id
) VALUES (
  'f521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
  'CY',
  'CFS',
  'FCL',
  'BB',
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
  'TERMS AND CONDITIONS!',
  'c703277f-84ca-4816-9ccf-fad8e202d3b6'
);

INSERT INTO booking (
  carrier_booking_request_reference,
  carrier_booking_reference,
  booking_status,
  booking_data_id,
  last_confirmed_booking_data_id
) VALUES (
  'CARRIER_BOOKING_REQUEST_REFERENCE_03',
  'AR1239719871',
  'CONFIRMED',
  'f521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
  'f521dbdb-a12b-48f5-b489-8594349731bf'::uuid
);

INSERT INTO reference (
    reference_type_code,
    reference_value,
    booking_data_id
) VALUES (
    'CR',
    'AB-123743CR',
    (SELECT booking.booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871')
), (
    'PO',
    'PO0027',
    (SELECT booking.booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871')
), (
    'CR',
    'BC-346267CR',
    (SELECT booking.booking_data_id FROM booking WHERE carrier_booking_reference = 'CR1239719872')
), (
    'PO',
    'PO0028',
    (SELECT booking.booking_data_id FROM booking WHERE carrier_booking_reference = 'CR1239719872')
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
    booking_data_id,
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
    dcsa_transport_type
) VALUES (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
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
    'TRUCK'
), (
  (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
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
    'VESSEL'
), (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
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
    'RAIL'
), (
  (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
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
  'VESSEL'
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
    phone
) VALUES (
    'b24d099e-a6f6-404e-b082-776f7f589023'::uuid,
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9',
    'DCSA',
    'info@dcsa.org',
    '+31123456789'
), (
    'b24d099e-a6f6-404e-b082-776f7f589064'::uuid,
    '8dd9a4c4-4039-11eb-8770-0b2b19847fab',
    'DCSA',
    'info@dcsa.org',
    '+31123456789'
), (
    'b24d099e-a6f6-404e-b082-776f7f589022'::uuid,
    '9dd9a4c4-4039-11eb-8770-0b2b19847fab',
    'DCSA',
    'info@dcsa.org',
    '+31123456789'
);

INSERT INTO document_party (
    party_id,
    booking_data_id,
    party_function,
    is_to_be_notified
) VALUES (
    '4e448f26-4035-11eb-a49d-7f9eb9bc8dd9',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    'OS',
    true
), (
    '8dd9a4c4-4039-11eb-8770-0b2b19847fab',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    'CN',
    true
), (
      '9dd9a4c4-4039-11eb-8770-0b2b19847fab',
      (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
      'CN',
      true
);

INSERT INTO shipment_location (
    booking_data_id,
    location_id,
    shipment_location_type_code
) VALUES (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    uuid('84bfcf2e-403b-11eb-bc4a-1fc4aa7d879d'),
    'PRE'
), (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    uuid('770b7624-403d-11eb-b44b-d3f4ad185386'),
    'POL'
), (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    uuid('7f29ce3c-403d-11eb-9579-6bd2f4cf4ed6'),
    'POD'
), (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    uuid('770b7624-403d-11eb-b44b-d3f4ad185387'),
    'PDE'
), (
   (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
   uuid('770b7624-403d-11eb-b44b-d3f4ad185386'),
   'POL'
), (
  (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
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
), (
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
    'APPROVED',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2022-01-24',
    DATE '2022-01-31'
), (
    '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
    'SI_REF_4',
    'RECEIVED',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-02-08',
    DATE '2021-02-09'
), (
      '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
      'SI_REF_7',
      'APPROVED',
      TRUE,
      2,
      4,
      TRUE,
      TRUE,
      DATE '2022-03-01',
      DATE '2022-03-07'
);

INSERT INTO requested_equipment_group (
    id,
    booking_data_id,
    iso_equipment_code,
    units,
    is_shipper_owned
) VALUES (
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    '22GP',
    1,
    false
);


INSERT INTO commodity (
    id,
    requested_equipment_group_id,
    commodity_type,
    commodity_subreference,
    cargo_gross_weight,
    cargo_gross_weight_unit
) VALUES (
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Expensive Shoes',
    'commodity-subref-1',
    4000,
    'KGM'
), (
  '5e900ef3-f929-4954-b145-eec22007f31b',
  'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
  'Expensive Shoes',
  'commodity-subref-2',
  4000,
  'KGM'
), (
    '2219e859-e3b5-4e87-80b8-32e9f77cca04',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Massive Yacht',
    'commodity-subref-3',
    4000,
    'KGM'
), (
    '877ce0f8-3126-45f5-b22e-2d1d27d42d85',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Leather Jackets',
    'commodity-subref-4',
    4000,
    'KGM'
), (
    '770f11e5-aae2-4ae4-b27e-0c689ed2e333',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Air ballons',
    'commodity-subref-5',
    4000,
    'KGM'
), (
    'cb6354c9-1ceb-452c-aed0-3cb25a04647a',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Leather Jackets',
    'commodity-subref-6',
    4000,
    'KGM'
), (
    '8fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Leather Jackets',
    'commodity-subref-7',
    4000,
    'KGM'
), (
    '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    'd5ca0842-d90a-49fd-b929-1ef3fa375eec',
    'Leather Jackets',
    'commodity-subref-8',
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
  '5e900ef3-f929-4954-b145-eec22007f31b',
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
    carrier_booking_reference,
    booking_id,
    commodity_id,
    commodity_subreference,
    description_of_goods,
    si_entry_order
) VALUES (
    '10f41e70-0cae-47cd-8eb8-4ee6f05e85c1',
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    'BR1239719871',
    (SELECT id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    (SELECT commodity_subreference FROM commodity WHERE id = '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0'),
    'Expensive Shoes',
    0
), (
    'c7104528-66d5-4d11-9b82-7af30e84d664',
    '9d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
    'BR1239719871',
    (SELECT id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    '5e900ef3-f929-4954-b145-eec22007f31b',
    (SELECT commodity_subreference FROM commodity WHERE id = '5e900ef3-f929-4954-b145-eec22007f31b'),
    'Massive Yacht',
    1
), (
    '1829548e-5938-4adc-a08e-3af55d8ccf63',
    '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    'AR1239719871',
    (SELECT id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
    '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d',
    (SELECT commodity_subreference FROM commodity WHERE id = '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d'),
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
    equipment_reference,
    utilized_transport_equipment_id
) VALUES (
    '10f41e70-0cae-47cd-8eb8-4ee6f05e85c1',
    50.0,
    'KGM',
    (SELECT equipment_reference FROM utilized_transport_equipment WHERE id = '6824b6ca-f3da-4154-96f1-264886b68d53'),
    uuid('6824b6ca-f3da-4154-96f1-264886b68d53')
), (
    'c7104528-66d5-4d11-9b82-7af30e84d664',
    1000.0,
    'KGM',
    (SELECT equipment_reference FROM utilized_transport_equipment WHERE id = '44068608-da9b-4039-b074-d9ac27ddbfbf'),
    uuid('44068608-da9b-4039-b074-d9ac27ddbfbf')
);

INSERT INTO cargo_item (
  id,
  consignment_item_id,
  weight,
  weight_unit,
  equipment_reference,
  utilized_transport_equipment_id
) VALUES (
  '2d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
  (SELECT id FROM consignment_item WHERE shipping_instruction_id = '9fbb78cc-e7c6-4e17-9a23-24dc3ad0378d'),
  23.5,
  'KGM',
  (SELECT equipment_reference FROM utilized_transport_equipment WHERE id = 'aa030eb6-009b-411c-985c-527ce008b35a'),
  uuid('aa030eb6-009b-411c-985c-527ce008b35a')
);

INSERT INTO shipping_mark (
  cargo_item,
  shipping_mark
) VALUES (
  '2d5965a5-9e2f-4c78-b8cb-fbb7095e13a0',
  'shipping marks'
);

INSERT INTO requested_equipment_group (
  id,
  booking_data_id,
  iso_equipment_code,
  units,
  is_shipper_owned
) VALUES (
    '90ead08c-8e39-4fd9-8006-b2c01a463cb2',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_request_reference = 'CARRIER_BOOKING_REQUEST_REFERENCE_01'),
    '22GP',
    1,
    false
);

INSERT INTO commodity (
    id,
    requested_equipment_group_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    export_license_issue_date,
    export_license_expiry_date
) VALUES (
    'a5b681bf-68a0-4f90-8cc6-79bf77d3b2a1'::uuid,
    '90ead08c-8e39-4fd9-8006-b2c01a463cb2',
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

----------------- Data for ApproveTransportDocument BEGIN ----------------
------------------------------ DO NOT MODIFY -----------------------------

INSERT INTO charge (
    id,
    transport_document_id,
    booking_data_id,
    charge_name,
    currency_amount,
    currency_code,
    payment_term_code,
    calculation_basis,
    unit_price,
    quantity
) VALUES (
    '4816e01a-446b-4bc0-812e-a3a447c85668',
    (SELECT id FROM transport_document WHERE transport_document_reference = '9b02401c-b2fb-5009'),
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
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

