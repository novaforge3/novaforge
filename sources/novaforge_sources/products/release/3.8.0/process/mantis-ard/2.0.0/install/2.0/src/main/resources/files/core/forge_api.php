<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2010-2011 BULL SAS
* Copyright (C) 2017  Atos, NovaForge Version 3 and above.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * Return url to redirect user to its project
 * @param string $p_instance uuid send by forge
 * @access public
 */
function get_project_return_url( $p_instance ) {
	ini_set("soap.wsdl_cache_enabled", "0");
    try
	{
		$clientSOAP = new SoapClient(config_get( 'instance_wsdl' ));
				 
        $instance_method = 	config_get('instance_method');
 		
		$args = array($instance_method => array('arg0' => $p_instance));	
	    $result = $clientSOAP->__soapCall(config_get('instance_method'),$args); 
	   	  		
	}
	catch (SoapFault $fault) 
	{	
		error_log("SOAP Fault: (faultcode: {$fault->faultcode}, faultstring: {$fault->faultstring})", E_USER_ERROR);			
	}
	$projectId = $result->return;
	return string_url( "set_project.php?project_id=" . $projectId );
}

/**
 * Return project id for mantis instance
 * @param string $p_instance uuid send by forge
 * @access public
 */
function get_mantis_instance_project_id( $p_instance ) {
ini_set("soap.wsdl_cache_enabled", "0");
    try
	{
		$clientSOAP = new SoapClient(config_get( 'instance_wsdl' ));
				 
        $instance_method = 	config_get('instance_method');
 		
		$args = array($instance_method => array('arg0' => $p_instance));	
	    $result = $clientSOAP->__soapCall(config_get('instance_method'),$args); 
	   	  		
	}
	catch (SoapFault $fault) 
	{	
		error_log("SOAP Fault: (faultcode: {$fault->faultcode}, faultstring: {$fault->faultstring})", E_USER_ERROR);			
	}
	$projectId = $result->return;
	return $projectId;
}


?>
