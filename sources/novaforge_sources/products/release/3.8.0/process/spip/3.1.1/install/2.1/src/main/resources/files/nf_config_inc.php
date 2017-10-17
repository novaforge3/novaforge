<?php
//prefix for the BD scheme (_NF_SAFRAN.<siteId>)
define('_NF_SAFRAN', "nfsafran_");

define('_SCHEME', "https");
define('_HOSTNAME', "@HOSTNAME@");

define('_G_INSTANCE_WSDL', "http://127.0.0.1:@KARAF_PORT@/cxf/spipInstance?wsdl");
define('_G_INSTANCE_METHOD', "getToolProjectId");

// spip statuts
define('_SPIP_1comite', "1comite");
define('_SPIP_0minirezo', "0minirezo");
define('_SPIP_5poubelle', "5poubelle");
define('_SPIP_6forum', "6forum");

// spip webmestre column
define('_SPIP_WEBMESTRE_NON', "non");
define('_SPIP_WEBMESTRE_OUI', "oui");

//defined spip roles for mapping with forge roles
define('_NF_ADMINISTRATOR', "administrator");
define('_NF_AUTHOR', "author");
define('_NF_WEBMASTER', "webmaster");
define('_NF_VISITOR', "visitor");

//DB. server type. Not validated for other type
define('_NF_SERVER_DB', "mysql");

//choice to have independent shema for each spip instance
define('_NF_CHOIX_DB', "new_spip");

//rights at spip configuration
define('_NF_CHMOD_DB', "511");

/*
 *   To be configured by database admin
 *   ----------------
 */
define('_NF_PORT_DB', "@MARIADB_PORT@");
define('_NF_LOGIN_DB', "@MARIADB_SPIP_USER@");
define('_NF_PASS_DB', "@MARIADB_SPIP_PWD@");

/*
 * admin user added into each site DB. (at site creation)
 *       To be configured
 *       ----------------
 */
define('_NF_LOGIN_ADMIN_SPIP', "@SPIP_ADMIN_LOGIN@");
define('_NF_PASS_ADMIN_SPIP', "@SPIP_ADMIN_PWD@");
define('_NF_NOM_ADMIN_SPIP', "@SPIP_ADMIN_NAME@");
define('_NF_EMAIL_ADMIN_SPIP', "@SPIP_ADMIN_EMAIL@");
?>
