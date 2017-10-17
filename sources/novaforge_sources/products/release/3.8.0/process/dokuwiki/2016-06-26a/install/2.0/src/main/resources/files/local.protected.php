<?php

/**
 * Configuration for Forge integration
 */
$conf['forge']['instance']['endpoint']  = 'http://127.0.0.1:8181/cxf/dokuwikiInstance?wsdl';
$conf['forge']['instance']['method']   	= 'getToolProjectId';
$conf['forge']['instance']['id']   	= 'instance_id';
$conf['forge']['auth']['forwardDoubleHash'] = 1;

/**
 * Global configuration for Dokuwiki
 */
$conf['basedir'] = '/dokuwiki-default/dokuwiki';

$conf['showuseras'] = 'username';
$conf['disableactions'] = 'register,resendpwd,profile';
$conf['autopasswd'] = false;
$conf['defaultgroup'] = 'user';
$conf['rememberme'] = false;
$conf['useacl'] = 1;
$conf['authtype'] = 'authplaincas';
$conf['superuser'] = '@admin';
$conf['remote'] = 1;
$conf['remoteuser'] = '@admin';

/**
 * Configuration for Auth Plain Cas Plugin
 * See https://www.dokuwiki.org/plugin:authmysql for details and explanation
 */
 
//database access
$conf['plugin']['authplaincas']['serverBDD']   = 'localhost';
$conf['plugin']['authplaincas']['user']     = 'dokuwiki';
$conf['plugin']['authplaincas']['password'] = 'dokuwiki';
$conf['plugin']['authplaincas']['database'] = 'dokuwiki';

// CAS access 
$conf['plugin']['authplaincas']['server'] = 'novadev';
$conf['plugin']['authplaincas']['rootcas'] = '/cas';
$conf['plugin']['authplaincas']['port'] = '443';
$conf['plugin']['authplaincas']['handlelogoutrequest'] = 0;
$conf['plugin']['authplaincas']['autologinout'] = 1;
$conf['plugin']['authplaincas']['debug'] = 'TRUE';

//Login administrateur de la forge
$conf['plugin']['authplaincas']['admin'] = 'admin1';
