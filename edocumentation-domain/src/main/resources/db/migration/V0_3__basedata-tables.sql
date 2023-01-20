CREATE TABLE document_type (
    document_type_code varchar(3) PRIMARY KEY,
    document_type_name varchar(100) NOT NULL,
    document_type_description varchar(250) NOT NULL
);
insert into document_type (document_type_code, document_type_name, document_type_description)
  values
    ('CBR','Carrier Booking Request',''),
    ('BKG','Booking',''),
    ('SHI','Shipping Instruction',''),
    ('TRD','Transport Document',''),
    ('DEI','Delivery Instructions',''),
    ('DEO','Delivery Order',''),
    ('TRO','Transport Order',''),
    ('CRO','Container Release Order',''),
    ('ARN','Arrival Notice',''),
    ('VGM','Verified Gross Mass',''),
    ('CAS','Cargo Survey',''),
    ('CUC','Customs Clearance',''),
    ('DGD','Dangerous Goods Declaration',''),
    ('OOG','Out of Gauge',''),
    ('CQU','Contract Quotation',''),
    ('INV','Invoice','')
;


CREATE TABLE event_classifier (
    event_classifier_code varchar(3) PRIMARY KEY,
    event_classifier_name varchar(30) NOT NULL,
    event_classifier_description varchar(250) NOT NULL
);
insert into event_classifier (event_classifier_code, event_classifier_name, event_classifier_description)
  values
    ('ACT','Actual',''),
    ('PLN','Planned',''),
    ('EST','Estimated',''),
    ('REQ','Requested','')
;


CREATE TABLE shipment_event_type (
    shipment_event_type_code varchar(4) PRIMARY KEY,
    shipment_event_type_name varchar(30) NOT NULL,
    shipment_event_type_description varchar(350) NOT NULL
);
insert into shipment_event_type (shipment_event_type_code, shipment_event_type_name, shipment_event_type_description)
  values
    ('RECE','Received','Indicates that a document is received by the carrier or shipper'),
    ('DRFT','Drafted','Indicates that a document is in draft mode being updated by either the shipper or the carrier.'),
    ('PENA','Pending Approval','Indicates that a document has been submitted by the carrier and is now awaiting approval by the shipper. '),
    ('PENU','Pending Update','Indicates that the carrier requested an update from the shipper which is not received yet.'),
    ('PENC','Pending Confirmation','Indicates that a document has been submitted by the shipper and is now awaiting approval by the carrier.'),
    ('REJE','Rejected','Indicates that a document has been rejected by the carrier.'),
    ('APPR','Approved','Indicates that a document has been approved by the counterpart.'),
    ('ISSU','Issued','Indicates that a document has been issued by the carrier.'),
    ('SURR','Surrendered','Indicates that a document has been surrendered by the customer to the carrier.'),
    ('SUBM','Submitted','Indicates that a document has been submitted by the customer to the carrier.'),
    ('VOID','Void','Cancellation of an original document.'),
    ('CONF','Confirmed','Indicates that the document is confirmed.'),
    ('REQS','Requested','"A status indicator that can be used with a number of identifiers to denote that a certain activity, service or document has been requested by the carrier, customer or authorities. This status remains constant until the requested activity is  “Completed”."'),
    ('CMPL','Completed','"A status indicator that can be used with a number of activity identifiers to denote that a certain activity, service or document has been completed."'),
    ('HOLD','On Hold','"A status indicator that can be used with a number of activity identifiers to denote that a container or shipment has been placed on hold i.e. can’t  progress in the process."'),
    ('RELS','Released','"A status indicator that can be used with a number of activity identifiers to denote that a container or shipment has been released i.e. allowed to move from depot or terminal by authorities or service provider."'),
    ('CANC','Cancelled',' "A status indicator to be used when the booking is cancelled by the Shipper"')
;
