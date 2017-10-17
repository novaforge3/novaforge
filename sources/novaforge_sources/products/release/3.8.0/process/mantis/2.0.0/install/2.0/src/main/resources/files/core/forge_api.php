<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * @copyright Copyright 2017  Atos, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */

require_once( 'project_hierarchy_api.php' );

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
		//$args = array('arg0' => $p_instance);
        //$result = $clientSOAP->getToolProjectId($args); 
		
		$args = array($instance_method => array('arg0' => $p_instance));	
	    $result = $clientSOAP->__soapCall(config_get('instance_method'),$args); 
	   	  		
	}
	catch (SoapFault $fault) 
	{	
		error_log("SOAP Fault: (faultcode: {$fault->faultcode}, faultstring: {$fault->faultstring})", E_USER_ERROR);			
	}
	$projectId = $result->return;
	$url ="set_project.php?project_id=";
	if (project_hierarchy_is_toplevel($projectId) == false) 
	{
		$parentId = project_hierarchy_get_parent($projectId);
		$url = $url . $parentId . ";";
	}

	$url = $url . $projectId;
        return string_url( $url );
}

?>
