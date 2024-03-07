SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `customer_address`;
CREATE TABLE `customer_address` (
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `customer_id` int(11) NOT NULL,
                                    `street` varchar(50) NOT NULL,
                                    `complement` varchar(45) DEFAULT NULL,
                                    `city` varchar(45) NOT NULL,
                                    `county` varchar(30) NOT NULL,
                                    `eircode` varchar(9) NOT NULL,
                                    `country` varchar(45) DEFAULT NULL,
                                    PRIMARY KEY (`id`),
                                    KEY `caddress_idx_1` (`customer_id`),
                                    CONSTRAINT `caddress_fk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `customer_document`;
CREATE TABLE `customer_document` (
                                     `id` int(11) NOT NULL AUTO_INCREMENT,
                                     `customer_id` int(11) NOT NULL,
                                     `type` enum('DRIVER_LICENSE','PPS','PASSPORT') NOT NULL,
                                     `document_number` varchar(45) NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `cdocument_idx_1` (`customer_id`),
                                     CONSTRAINT `cdocument_fk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `customer_email`;
CREATE TABLE `customer_email` (
                                  `id` int(11) NOT NULL AUTO_INCREMENT,
                                  `customer_id` int(11) NOT NULL,
                                  `type` enum('WORK','PERSONAL') NOT NULL,
                                  `email` varchar(60) NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `cemail_idx_1` (`customer_id`),
                                  CONSTRAINT `cemail_fk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `first_name` varchar(50) NOT NULL,
                            `last_name` varchar(50) NOT NULL,
                            `birthday` date NOT NULL,
                            `is_active` tinyint(4) NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS=1;