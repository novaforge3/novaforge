<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
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
require_once('lib/nusoap/nusoap.php' );
/** TBD */ 
require_once("users.inc.php");
require_once("roles.inc.php");
require_once('cfg/novaforge.cfg.php');


/** 
 * get the project by name and set session data related to a Test project
 *
 */
function setProjectSession(&$db,$project_id)
{
	$tprojectMgr = new testproject($db);

        $existProject = $tprojectMgr-> get_by_id($project_id);
        if(!$existProject){
            	return false;
        }else{
	 	$tprojectMgr->setSessionProject($existProject["id"]);
		return true;
        }
}

/**
 * Return url to redirect user to its project
 * @param string $p_instance uuid send by forge
 * @access public
 */
function getProjectId( $p_instance ) {
		global $conf;
        ini_set("soap.wsdl_cache_enabled", "0");
        $clientSOAP = new nusoap_client($conf['forge']['instance']['endpoint'],true);
        //$clientSOAP->setUseCurl(false);
        $result = $clientSOAP->call($conf['forge']['instance']['method'], array('arg0' => $p_instance));         
	// Check for a fault
	if ($clientSOAP->fault) {
		error_log( "fault :'" . $result['faultstring'] ."'");
		return false;
	} else {
		// Check for errors
		$err = $clientSOAP->getError();
		if ($err) {
		        // Display the error
			error_log( "err :'" . $err ."'");
			return false;
		} 
	}
	
        return string_url($result['return']);
}