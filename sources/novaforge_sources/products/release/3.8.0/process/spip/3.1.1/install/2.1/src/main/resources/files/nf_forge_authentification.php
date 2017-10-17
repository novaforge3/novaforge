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

require_once( 'nf_core.php' );
require_once( 'nf_forge_api.php' );
require_once( 'inc/cookie.php' );
require_once( 'inc/session.php' );
require_once( 'base/connect_sql.php' );

$f_username		= '';
$f_password		= '';
$f_perm_login = '';
if (isset($_REQUEST['instance_id']) and $_REQUEST['instance_id'] !="")
{
	$instance_id = $_REQUEST['instance_id'];
	$projectToolId = get_spip_instance_project_id($instance_id);
	preg_match(',/@DEFAULT_ALIAS@/@TOOL_ALIAS@/([a-zA-Z0-9_-]+)/?,',$_SERVER['REQUEST_URI'],$r);
	$site_id = $r[1];
	// case first call to the tool (without projectToolId into the url)
	if($site_id == "ecrire") {
		$uri_to_replace = $_SERVER['REQUEST_URI'];
		$uri = str_replace("@DEFAULT_ALIAS@/@TOOL_ALIAS@","@DEFAULT_ALIAS@/@TOOL_ALIAS@/$projectToolId",$uri_to_replace);
		$url = _SCHEME . '://' . _HOSTNAME . '/' . $uri;
		header( "Location: $url" );
	} else {
		//redirect to the spip tool (admin and author page).
		$url = _SCHEME . '://' . _HOSTNAME . '/' . "@DEFAULT_ALIAS@/@TOOL_ALIAS@/" . $projectToolId . "/index.php" ;
		header( "Location: $url" );
	}
}
?>
