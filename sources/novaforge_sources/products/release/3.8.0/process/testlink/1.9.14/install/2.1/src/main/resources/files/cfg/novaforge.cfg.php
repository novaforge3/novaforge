<?php
/**
* TestLink Open Source Project - http://testlink.sourceforge.net/ 
* $Id: gforge.cfg.php,v 1.0.0.0 2008/08/21 17:12:00 john.wanke
* $Id: gforge.cfg.php,v 1.2 2008/11/04 19:58:22 franciscom Exp $ 
*
* Contributed by john.wanke
* 
* Constants used throughout TestLink are defined within this file
* they should be changed for your environment
* 
*/
$conf['forge']['instance']['endpoint']  = 'http://127.0.0.1:8181/cxf/testlinkInstance?wsdl';
$conf['forge']['instance']['method']   	= 'getToolProjectId';
$conf['forge']['instance']['id']   	= 'instance_id';

$conf['forge']['functionnal']['endpoint']  = 'http://127.0.0.1:8181/cxf/testlinkFunctional?wsdl';
?>
