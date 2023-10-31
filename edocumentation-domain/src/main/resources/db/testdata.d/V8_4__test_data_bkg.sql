
INSERT INTO shipment_cutoff_time (
    booking_data_id,
    cut_off_time_code,
    cut_off_time
) VALUES (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871'),
    'DCO',
    DATE '2021-05-01'
), (
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'CR1239719872'),
    'ECP',
    DATE '2020-07-07'
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
), (
    'cbe900e7-7ad9-45fc-8d9e-0d1628a1b4f7'::uuid,
    'Incoterms entity: Transport obligations, costs and risks as agreed between buyer and seller.'
), (
    '3991a845-6cc8-404a-ac25-a1393e1d93a9'::uuid,
    'Value Added Service Request entity: An entity containing data on requests for value added services. '
);


INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    booking_data_id
) VALUES (
    '93eedc86-f8a3-4ec3-8d30-ad1eb8a079d2',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719871')
), (
    'cbe900e7-7ad9-45fc-8d9e-0d1628a1b4f7',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'CR1239719872')
), (
    '3991a845-6cc8-404a-ac25-a1393e1d93a9',
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'BR1239719971')
);

INSERT INTO shipment_carrier_clauses (
    carrier_clause_id,
    booking_data_id,
    transport_document_id
) VALUES (
    'b8e312ad-7b00-4026-88ad-9881242ca4f4'::uuid,
    (SELECT booking_data_id FROM booking WHERE carrier_booking_reference = 'AR1239719871'),
    (SELECT id FROM transport_document WHERE transport_document_reference = '9b02401c-b2fb-5009')
);



