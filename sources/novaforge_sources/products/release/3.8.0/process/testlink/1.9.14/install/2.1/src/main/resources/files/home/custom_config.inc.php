<?php

$g_interface_forge = 'NOVAFORGE';
$g_forgeInterfaceOn = true;
$tlCfg->config_check_warning_mode = 'SILENT';

//Need because of php version used by Novaforge
date_default_timezone_set('Europe/Paris');
$tlCfg->default_roleid = TL_ROLES_NO_RIGHTS;

?>