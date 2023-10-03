
INSERT INTO shipment_cutoff_time (
    shipment_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'A379021B7782'),
    'AFD',
    DATE '2021-03-09'
);

INSERT INTO shipment_cutoff_time (
    shipment_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871'),
    'DCO',
    DATE '2021-05-01'
);

INSERT INTO shipment_cutoff_time (
    shipment_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'CR1239719872'),
    'ECP',
    DATE '2020-07-07'
);

INSERT INTO shipment_cutoff_time (
    shipment_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'E379021B7782'),
    'EFC',
    DATE '2020-01-06'
);

INSERT INTO party (
    id,
    party_name
) VALUES (
    'c49ea2d6-3806-46c8-8490-294affc71286',
    'FDM Quality Control'
);

INSERT INTO party_contact_details (
    id,
    party_id,
    name,
    email,
    phone
) VALUES (
    'be59706b-b059-455b-bb20-aeb8d79605fe'::uuid,
    'c49ea2d6-3806-46c8-8490-294affc71286',
    'FDM',
    'info@fdm.org',
    '+31123456789'
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
    'be038e58-5365',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6',
    DATE '2020-11-25',
    DATE '2020-12-24',
    DATE '2020-12-31',
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    '01670315-a51f-4a11-b947-ce8e245128eb'::uuid,
    12,
    '2021-11-28T14:12:56+01:00'::timestamptz,
    '2021-12-01T07:41:00+08:30'::timestamptz,
    'c49ea2d6-3806-46c8-8490-294affc71286'
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
    'f9d3c9ae-89c1-4394-a5fc-8e73538aaac4'::uuid,
    (SELECT id FROM transport_document WHERE  transport_document_reference = 'be038e58-5365'),
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'A379021B7782'),
    'TBD',
    12.12,
    'UMB',
    'PRE',
    'WHAT',
    12.12,
    123.321
);

INSERT INTO carrier_clauses (
    id,
    clause_content
) VALUES (
    'b8e312ad-7b00-4026-88ad-9881242ca4f4'::uuid,
    'Carrier Clauses entity: comprises clauses, added by the carrier to the Shipment, which are subject to local rules/guidelines or certain mandatory information required to be shared with the customer. Usually printed below the cargo description.'
);

INSERT INTO carrier_clauses (
    id,
    clause_content
) VALUES (
    '93eedc86-f8a3-4ec3-8d30-ad1eb8a079d2'::uuid,
    'Shipment Carrier Clauses entity: address the carrier clauses for a shipment.'
);

INSERT INTO carrier_clauses (
    id,
    clause_content
) VALUES (
    'cbe900e7-7ad9-45fc-8d9e-0d1628a1b4f7'::uuid,
    'Incoterms entity: Transport obligations, costs and risks as agreed between buyer and seller.'
);

INSERT INTO carrier_clauses (
    id,
    clause_content
) VALUES (
    '3991a845-6cc8-404a-ac25-a1393e1d93a9'::uuid,
    'Value Added Service Request entity: An entity containing data on requests for value added services. '
);


INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    shipment_id
) VALUES (
    'b8e312ad-7b00-4026-88ad-9881242ca4f4',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'A379021B7782')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    shipment_id
) VALUES (
    '93eedc86-f8a3-4ec3-8d30-ad1eb8a079d2',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719871')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    shipment_id
) VALUES (
    'cbe900e7-7ad9-45fc-8d9e-0d1628a1b4f7',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'CR1239719872')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    shipment_id
) VALUES (
    '3991a845-6cc8-404a-ac25-a1393e1d93a9',
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'BR1239719971')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    shipment_id,
    transport_document_id
) VALUES (
    'b8e312ad-7b00-4026-88ad-9881242ca4f4'::uuid,
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'AR1239719871'),
    (SELECT id FROM transport_document WHERE transport_document_reference = '9b02401c-b2fb-5009')
);

INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    booking_status,
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
    is_ams_aci_filing_required,
    is_destination_filing_required,
    contract_quotation_reference,
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
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'ef223019-ff16-4870-be69-9dbaaaae9b11',
    'PENDING UPDATE',
    'CY',
    'CY',
    'FCL',
    'LCL',
    '2021-11-03 02:11:00.000',
    'Test',
     NULL,
     true,
     true,
     'Export declaration reference',
     true,
     'Import declaration reference',
     true,
     true,
     NULL,
     NULL,
     'c703277f-84ca-4816-9ccf-fad8e202d3b6',
     DATE '2020-03-07',
     NULL,
     NULL,
     NULL,
     'AO',
     true,
     NULL,
    'WTK',
    12.12,
     NULL,
     DATE '2021-12-01');

INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    booking_status,
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
    is_ams_aci_filing_required,
    is_destination_filing_required,
    contract_quotation_reference,
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
    '31629725-418b-41e1-9d10-521763c656c4'::uuid,
    '52c2caa0-0137-44b7-9947-68687b3b4ae6',
    'PENDING UPDATES CONFIRMATION',
    'CY',
    'CY',
    'FCL',
    'LCL',
    '2021-11-03 02:11:00.000',
    'Test',
     NULL,
     true,
     true,
     'Export declaration reference',
     true,
     'Import declaration reference',
     true,
     true,
     NULL,
     NULL,
     'c703277f-84ca-4816-9ccf-fad8e202d3b6',
     DATE '2020-03-07',
     NULL,
     NULL,
     NULL,
     'AO',
     true,
     NULL,
    'WTK',
    12.12,
     NULL,
     DATE '2021-12-01');

INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    booking_status,
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
    is_ams_aci_filing_required,
    is_destination_filing_required,
    contract_quotation_reference,
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
    '8b78219e-d049-4c68-8d9e-f40bf9a85140'::uuid,
    'a3a34f10-acc5-4e23-b52e-146f63458c90',
    'CONFIRMED',
    'CY',
    'CY',
    'FCL',
    'LCL',
    '2021-12-20 02:11:00.000',
    'Test',
     NULL,
     true,
     true,
     'Export declaration reference',
     true,
     'Import declaration reference',
     true,
     true,
     NULL,
     NULL,
     'c703277f-84ca-4816-9ccf-fad8e202d3b6',
     DATE '2020-03-07',
     NULL,
     NULL,
     NULL,
     'AO',
     true,
     NULL,
    'WTK',
    12.12,
     NULL,
     DATE '2021-12-01');

INSERT INTO commodity(
    id,
    booking_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    export_license_issue_date,
    export_license_expiry_date
    ) VALUES (
    '85b681bf-68a0-4f90-8cc6-79bf77d3b2a1'::uuid,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'Hand Bags',
    1200.0,
    'KGM',
    NULL,
    NULL);

INSERT INTO commodity (
    id,
    booking_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    export_license_issue_date,
    export_license_expiry_date
    ) VALUES (
    '54c9b7fb-b641-4ccc-b1be-70a63fac17d6'::uuid,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'Bloom',
    2000.0,
    'LBR',
    NULL,
    NULL);

INSERT INTO commodity (
    id,
    booking_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    export_license_issue_date,
    export_license_expiry_date
    ) VALUES (
    'bf93f6fb-98b8-4268-a4dc-23a40eab95a9'::uuid,
    '8b78219e-d049-4c68-8d9e-f40bf9a85140'::uuid,
    'Bloom',
    2000.0,
    'LBR',
    NULL,
    NULL);

INSERT INTO commodity (
    id,
    booking_id,
    commodity_type,
    cargo_gross_weight,
    cargo_gross_weight_unit,
    export_license_issue_date,
    export_license_expiry_date
    ) VALUES (
    'bf93f6fb-98b8-4268-a4dc-23a40eab95a8'::uuid,
    (SELECT booking_id FROM shipment WHERE carrier_booking_reference = 'A379021B7782'),
    'Bloom',
    2000.0,
    'LBR',
    NULL,
    NULL);

INSERT INTO hs_code_item (
  commodity_id,
  hs_code
) VALUES (
  '85b681bf-68a0-4f90-8cc6-79bf77d3b2a1'::uuid,
  '411510'
), (
  '54c9b7fb-b641-4ccc-b1be-70a63fac17d6'::uuid,
  '720711'
), (
  'bf93f6fb-98b8-4268-a4dc-23a40eab95a9'::uuid,
  '720711'
), (
  'bf93f6fb-98b8-4268-a4dc-23a40eab95a8'::uuid,
  '720711'
);

INSERT INTO shipment (
    id,
    booking_id,
    carrier_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
    ) VALUES (
    '6e4035d9-3200-43ee-a7ea-6d8108c5f0c8'::uuid,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    'DCR987876762',
    'TERMS AND CONDITIONS!',
    '2021-12-12 12:12:12.000',
    DATE '2021-12-12T12:12:12');

INSERT INTO shipment (
    id,
    booking_id,
    carrier_id,
    carrier_booking_reference,
    terms_and_conditions,
    confirmation_datetime,
    updated_date_time
    ) VALUES (
    'cb40eee5-9e24-41ee-8276-2e03aa2744ed'::uuid,
    '8b78219e-d049-4c68-8d9e-f40bf9a85140'::uuid,
    (SELECT id FROM carrier WHERE smdg_code = 'HLC'),
    'C501576CD94F',
    'TERMS AND CONDITIONS!',
    '2022-02-02 02:22:22.000',
    DATE '2022-03-03T12:12:12');

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
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'DCR987876762'),
    1,
    'MNC',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'::uuid,
    '01670315-a51f-4a11-b947-ce8e245128eb'::uuid,
    '2021-12-01',
    '2021-12-03',
    '9321483',
    'Emma Mærsk',
    '2106W',
    '2107E',
    null,
    'VESSEL',
    false
), (
    (SELECT id FROM shipment WHERE carrier_booking_reference = 'C501576CD94F'),
    1,
    'MNC',
    'c703277f-84ca-4816-9ccf-fad8e202d3b6'::uuid,
    '01670315-a51f-4a11-b947-ce8e245128eb'::uuid,
    '2021-12-01',
    '2021-12-03',
    '9321483',
    'Emma Mærsk',
    '2106W',
    '2107E',
    null,
    'VESSEL',
    false
);

INSERT INTO reference (
    id,
    reference_type_code,
    reference_value,
    shipment_id,
    shipping_instruction_id,
    booking_id
    ) VALUES (
    '48fbbce6-54f7-4e87-b9aa-525dda865962'::uuid,
    'FF',
    'test',
    '6e4035d9-3200-43ee-a7ea-6d8108c5f0c8'::uuid,
    NULL,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid);


INSERT INTO requested_equipment_group (
    id,
    booking_id,
    shipment_id,
    iso_equipment_code,
    units,
    is_shipper_owned
    ) VALUES (
    '1a595981-c2d9-46a9-a870-3086735b4529'::uuid,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    '6e4035d9-3200-43ee-a7ea-6d8108c5f0c8'::uuid,
    '22GP',
    3,
    true);

INSERT INTO party (
    id,
    party_name,
    tax_reference_1,
    tax_reference_2,
    public_key,
    address_id
) VALUES (
    '7bf6f428-58f0-4347-9ce8-d6be2f5d5745',
    'Hapag Lloyd',
    'CVR-25645774',
    'CVR-25645774',
    'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFzaW',
    '8fecc6d0-2a78-401d-948a-b9753f6b53d5'::uuid
);

INSERT INTO party_contact_details (
    id,
    party_id,
    name,
    email,
    phone
) VALUES (
    'c4645dd4-56a7-4c32-898f-cc86d5252248'::uuid,
    '7bf6f428-58f0-4347-9ce8-d6be2f5d5745',
    'Hapag Lloyd',
    'info@h-l.org',
    '+31123456789'
);

INSERT INTO party_identifying_code (
    id,
    dcsa_responsible_agency_code,
    party_id,
    code_list_name,
    party_code
    ) VALUES (
    '3fce10f1-e524-49c3-ac47-4260ceccc7f6'::uuid,
    'SCAC',
    '7bf6f428-58f0-4347-9ce8-d6be2f5d5745',
    'LCL',
    'MMCU');


INSERT INTO displayed_address (
    id,
    address_line_1,
    address_line_2
) VALUES (
    '207e0ee4-9750-4b41-8fe2-ca65b1e11c2c'::uuid,
    'Gubener Str. 42',
    'Rhinstrasse 87'
);

INSERT INTO document_party (
    id,
    party_id,
    shipping_instruction_id,
    shipment_id,
    party_function,
    is_to_be_notified,
    booking_id,
    displayed_address_id
    ) VALUES (
    'c678ce03-3859-4db3-a23f-d7c3f998fd0a'::uuid,
    '7bf6f428-58f0-4347-9ce8-d6be2f5d5745',
    NULL,
    '6e4035d9-3200-43ee-a7ea-6d8108c5f0c8'::uuid,
    'DDS',
    true,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    '207e0ee4-9750-4b41-8fe2-ca65b1e11c2c'::uuid
    );

INSERT INTO party_contact_details (
    id,
    party_id,
    name,
    email,
    phone
    ) VALUES (
    'b24d099e-a6f6-404e-b082-776f7f589061'::uuid,
    '7bf6f428-58f0-4347-9ce8-d6be2f5d5745',
    'DCSA',
    'info@dcsa.org',
    '+31123456789'
);

INSERT INTO shipment_location (
    id,
    shipment_id,
    booking_id,
    location_id,
    shipment_location_type_code,
    event_date_time
    ) VALUES (
    'd3001e47-07e9-4bba-87a7-019d3e12f3aa'::uuid,
    '6e4035d9-3200-43ee-a7ea-6d8108c5f0c8'::uuid,
    'a521dbdb-a12b-48f5-b489-8594349731bf'::uuid,
    'c703277f-84ca-4816-9ccf-fad8e202d3b6',
    'POL',
     NULL);


/* Start: Test data for events with carrierBookingReference and carrierBookingRequestReference */
INSERT INTO booking (
    id,
    carrier_booking_request_reference,
    booking_status,
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
    is_ams_aci_filing_required,
    is_destination_filing_required,
    contract_quotation_reference,
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
    'b8376516-0c1c-4b6f-b51f-6707812c8ff4'::uuid, /* id */
    'cbrr-b83765166707812c8ff4', /* carrier_booking_request_reference */
    'PENDING UPDATE', /* booking_status */
    'CY', /* receipt_type_at_origin */
    'CY', /* delivery_type_at_destination */
    'FCL', /* cargo_movement_type_at_origin */
    'LCL', /* cargo_movement_type_at_destination */
    '2021-11-03 02:11:00.000', /* booking_request_datetime */
    'Test', /* service_contract_reference */
     NULL, /* payment_term_code */
     true, /* is_partial_load_allowed */
     true, /* is_export_declaration_required */
     'Export declaration reference', /* export_declaration_reference */
     true, /* is_import_license_required */
     'Import declaration reference', /* import_license_reference */
     true, /* is_ams_aci_filing_required */
     true, /* is_destination_filing_required */
     NULL, /* contract_quotation_reference */
     NULL, /* incoterms */
     NULL, /* invoice_payable_at */
     DATE '2020-03-07', /* expected_departure_date */
     NULL, /* transport_document_type_code */
     NULL, /* transport_document_reference */
     NULL, /* booking_channel_reference */
     'AO', /* communication_channel_code */
     true, /* is_equipment_substitution_allowed */
     NULL, /* vessel_id */
    'WTK',  /* declared_value_currency */
    12.12, /* declared_value */
     NULL, /* place_of_issue */
     DATE '2021-12-01' /* updated_date_time */
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
    'b8376516-0c1c-4b6f-b51f-6707812c8ff4'::uuid,
    'cbr-b83765166707812c8ff4',
    'TERMS AND CONDITIONS!',
    DATE '2020-03-07T12:12:12',
    DATE '2020-04-07T12:12:12'
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
    'c144c6df-440e-4065-8430-f46b9fa67e65',
    'c144c6dff46b9fa67e65',
    'RECE',
    TRUE,
    2,
    4,
    TRUE,
    TRUE,
    DATE '2021-12-24',
    DATE '2021-12-31'
);

/* End: Test data for events with carrierBookingReference and carrierBookingRequestReference */

