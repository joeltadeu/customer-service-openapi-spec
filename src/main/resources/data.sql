LOCK TABLES `customer` WRITE;
INSERT INTO `customer`
VALUES (1, 'Aoife', 'Murphy', '1980-12-13', 1),
       (2, 'Kelly', 'O’Brien', '1985-10-09', 1),
       (3, 'Ryan', 'O’Sullivan', '1988-04-25', 1),
       (4, 'Duffy', 'Connor', '1975-02-18', 1);
UNLOCK TABLES;

LOCK TABLES `customer_address` WRITE;
INSERT INTO `customer_address`
VALUES (1, 1, '6 Bridge St.', '', 'Cork', 'County Cork', 'N36RP84', 'Ireland'),
       (2, 2, 'Main st', '', 'Athlone', 'Co. Westmeath', 'N35RP31', 'Ireland'),
       (3, 3, 'The Launderette, Green St.', '', 'Moate', 'Co. Westmeath', 'N33RP21', 'Ireland'),
       (4, 4, '6/7 High st Galway', '', 'Galway', 'County Galway', 'N21RP45', 'Ireland');
UNLOCK TABLES;

LOCK TABLES `customer_document` WRITE;
INSERT INTO `customer_document`
VALUES (1, 1, 'PPS', '48378IA'),
       (2, 1, 'PASSPORT', 'FA3891IU'),
       (3, 2, 'PPS', '87565IA'),
       (4, 2, 'PASSPORT', 'FT4782KB'),
       (5, 3, 'PPS', '12456IA'),
       (6, 3, 'PASSPORT', 'KL8736ON'),
       (7, 3, 'DRIVER_LICENSE', '9827878BV'),
       (8, 4, 'DRIVER_LICENSE', '765218SX'),
       (9, 4, 'PPS', '65411IA');
UNLOCK TABLES;

LOCK TABLES `customer_email` WRITE;
INSERT INTO `customer_email`
VALUES (1, 1, 'WORK', 'aoife.murphy@aib.ie'),
       (2, 1, 'PERSONAL', 'aoife.murphy@gmail.com'),
       (3, 2, 'WORK', 'kelly.obrien@vodafone.ie'),
       (4, 2, 'PERSONAL', 'kelly.obrien@yahoo.com'),
       (5, 3, 'WORK', 'ryan.osullivan@tesco.ie'),
       (6, 3, 'PERSONAL', 'ryan.osullivan@yahoo.com'),
       (7, 4, 'WORK', 'duffy.connor@dunnes.ie'),
       (8, 4, 'PERSONAL', 'duffy.connor@outlook.com');
UNLOCK TABLES;