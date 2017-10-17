DROP TABLE IF EXISTS bugtracker.correspondance_etat;
CREATE TABLE bugtracker.correspondance_etat (
  codeState INTEGER UNSIGNED NOT NULL,
  valeurEtat VARCHAR(100) NOT NULL,
  affectation VARCHAR(20) NOT NULL,
  PRIMARY KEY (codeState)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS bugtracker.correspondance_severite;
CREATE TABLE bugtracker.correspondance_severite (
  codeSeverite INTEGER UNSIGNED NOT NULL,
  valeur VARCHAR(45) NOT NULL,
  PRIMARY KEY (codeSeverite)
)ENGINE=InnoDB;

INSERT INTO correspondance_etat (codestate, valeuretat, affectation ) 
VALUES  ( 10, 'Ouvert/Affecté', 'Fournisseur' ),
( 20, 'Affecté Client', 'Client' ),
( 38, 'Annulé/Refusé', 'Client' ),
( 40, 'Annulé/Refusé', 'Client' ),
( 50, 'Ouvert/Affecté', 'Fournisseur' ),
( 80, 'Résolu', 'Fournisseur' ),
( 82, 'Testé', 'Fournisseur' ),
( 85, 'Livré', 'Client' );

INSERT INTO correspondance_severite (codeSeverite, valeur) 
VALUES  ( 10, 'mineure'),
( 20, 'mineure'),
( 30, 'mineure'),
( 40, 'mineure'),
( 50, 'mineure'),
( 60, 'majeure'),
( 70, 'majeure'),
( 80, 'bloquante');

DROP TABLE IF EXISTS `nova_histo`;
CREATE TABLE `nova_histo` (
  `dt` datetime NOT NULL default '0000-00-00 00:00:00',
  `EtatAno` varchar(128) default NULL,
  `blocante` int(11) default NULL,
  `majeure` int(11) default NULL,
  `mineure` int(11) default NULL,
  `total` int(11) default NULL,
  `bull` int(11) default '0',
  KEY `dt` (`dt`),
  KEY `bull` (`bull`)
) ENGINE=MyISAM;