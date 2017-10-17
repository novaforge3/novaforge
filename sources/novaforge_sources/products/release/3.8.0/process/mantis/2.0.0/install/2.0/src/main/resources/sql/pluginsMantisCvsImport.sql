-- --------------------------------------------------------
--
-- Database: `bugtracker`
--
use bugtracker;


DELETE FROM mantis_plugin_table WHERE basename='Csv_import';
INSERT INTO mantis_plugin_table VALUES ('Csv_import',1,0,3);
DELETE FROM mantis_config_table WHERE config_id='plugin_Csv_import_schema';
INSERT INTO mantis_config_table VALUES ('plugin_Csv_import_schema',0,0,90,1,'-1');
