
INSERT INTO shipment_cutoff_time (
    confirmed_booking_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'A379021B7782'),
    'AFD',
    DATE '2021-03-09'
);

INSERT INTO shipment_cutoff_time (
    confirmed_booking_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'BR1239719871'),
    'DCO',
    DATE '2021-05-01'
);

INSERT INTO shipment_cutoff_time (
    confirmed_booking_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'CR1239719872'),
    'ECP',
    DATE '2020-07-07'
);

INSERT INTO shipment_cutoff_time (
    confirmed_booking_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'E379021B7782'),
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
    confirmed_booking_id
) VALUES (
    'b8e312ad-7b00-4026-88ad-9881242ca4f4',
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'A379021B7782')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    confirmed_booking_id
) VALUES (
    '93eedc86-f8a3-4ec3-8d30-ad1eb8a079d2',
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'BR1239719871')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    confirmed_booking_id
) VALUES (
    'cbe900e7-7ad9-45fc-8d9e-0d1628a1b4f7',
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'CR1239719872')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    confirmed_booking_id
) VALUES (
    '3991a845-6cc8-404a-ac25-a1393e1d93a9',
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'BR1239719971')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    confirmed_booking_id,
    transport_document_id
) VALUES (
    'b8e312ad-7b00-4026-88ad-9881242ca4f4'::uuid,
    (SELECT id FROM confirmed_booking WHERE carrier_booking_reference = 'AR1239719871'),
    (SELECT id FROM transport_document WHERE transport_document_reference = '9b02401c-b2fb-5009')
);

INSERT INTO booking_request (
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


INSERT INTO requested_equipment_group (
    id,
    booking_request_id,
    iso_equipment_code,
    units,
    is_shipper_owned
) VALUES (
    '98c4d459-42a3-42bc-992d-e1de670a9c08',
    (SELECT booking_request_id FROM confirmed_booking WHERE carrier_booking_reference = 'A379021B7782'),
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
    'bf93f6fb-98b8-4268-a4dc-23a40eab95a8'::uuid,
    '98c4d459-42a3-42bc-992d-e1de670a9c08',
    'Bloom',
    2000.0,
    'LBR',
    NULL,
    NULL);

INSERT INTO hs_code_item (
  commodity_id,
  hs_code
) VALUES (
  'bf93f6fb-98b8-4268-a4dc-23a40eab95a8'::uuid,
  '720711'
);

