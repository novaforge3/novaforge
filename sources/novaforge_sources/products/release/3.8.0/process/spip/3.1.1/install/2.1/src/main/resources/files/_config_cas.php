<?php
/**
*** CICAS PLUGIN CONFIGURATION
**/
// CAS ONLY : oui
// CAS AND SPIP : hybride
// ONLY SPIP OTHERWISE
$GLOBALS['ciconfig']['cicas'] = 'oui';

// Define which user field is the id
$GLOBALS['ciconfig']['cicasuid'] = 'login';

// CAS server URL
$GLOBALS['ciconfig']['cicasurldefaut'] = '@CAS_HOST@';

// CAS server DIRECTORY
$GLOBALS['ciconfig']['cicasrepertoire'] = '@CAS_URI@';

// CAS server PORT
$GLOBALS['ciconfig']['cicasport'] = '@CAS_PORT@';
?>
