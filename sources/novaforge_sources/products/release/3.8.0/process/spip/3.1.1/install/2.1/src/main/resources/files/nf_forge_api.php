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

require_once( 'nusoap/nusoap.php' );

/**
 * Return project id for spip instance
 * @param string $p_instance uuid send by forge
 * @access public
 */
function get_spip_instance_project_id( $p_instance ) {

	ini_set("soap.wsdl_cache_enabled", "0");

	$clientSOAP = new nusoap_client(_G_INSTANCE_WSDL,true);
	$clientSOAP->setUseCurl(false);
	$result = $clientSOAP->call(_G_INSTANCE_METHOD, array('arg0' => $p_instance));
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

	return $result['return'];
}
?>
