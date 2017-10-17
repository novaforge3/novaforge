-- --------------------------------------------------------
--
-- Database: `sonar`
--
use sonar;

-- --------------------------------------------------------
--
-- Sonar STMP procedures
--

DELIMITER ';;'

DROP PROCEDURE IF EXISTS configure_sonar_smtp;;
  
CREATE PROCEDURE configure_sonar_smtp() 
BEGIN
   IF EXISTS (SELECT * FROM information_schema.TABLES WHERE TABLE_NAME='properties' AND TABLE_SCHEMA='sonar') THEN   

		IF EXISTS (SELECT * FROM properties WHERE prop_key='email.smtp_host.secured') THEN   
			UPDATE properties SET text_value='@SMTP_HOST@' WHERE prop_key='email.smtp_host.secured';
		ELSE 
			INSERT INTO properties SET text_value='@SMTP_HOST@', prop_key='email.smtp_host.secured';
		END IF;     

		IF EXISTS (SELECT * FROM properties WHERE prop_key='email.smtp_port.secured') THEN   
			UPDATE properties SET text_value='@SMTP_PORT@' where prop_key='email.smtp_port.secured';
		ELSE 
			INSERT INTO properties SET text_value='@SMTP_PORT@', prop_key='email.smtp_port.secured';
		END IF;     

		IF EXISTS (SELECT * FROM properties WHERE prop_key='email.smtp_username.secured') THEN   
			UPDATE properties SET text_value='@SMTP_USERNAME@' where prop_key='email.smtp_username.secured';
		ELSE 
			INSERT INTO properties SET text_value='@SMTP_USERNAME@', prop_key='email.smtp_username.secured';
		END IF;     

		IF EXISTS (SELECT * FROM properties WHERE prop_key='email.smtp_password.secured') THEN   
			UPDATE properties SET text_value='@SMTP_PASSWORD@' where prop_key='email.smtp_password.secured';
		ELSE 
			INSERT INTO properties SET text_value='@SMTP_PASSWORD@', prop_key='email.smtp_password.secured';  
		END IF;     

		IF EXISTS (SELECT * FROM properties WHERE prop_key='email.from') THEN   
			UPDATE properties SET text_value='@SMTP_NOREPLY@' where prop_key='email.from';
		ELSE 
			INSERT INTO properties SET text_value='@SMTP_NOREPLY@', prop_key='email.from';      
		END IF;     
	END IF;    
END;;  

  
DELIMITER ';'  

-- --------------------------------------------------------
--
-- call procedure
--
call configure_sonar_smtp();  
  
DROP PROCEDURE IF EXISTS configure_sonar_smtp;
