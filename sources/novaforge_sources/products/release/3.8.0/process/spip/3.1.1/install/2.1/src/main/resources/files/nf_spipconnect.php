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

// NuSOAP already performs compression,
// so we prevent a double-compression.
// See issue #11868 for details
define( 'COMPRESSION_DISABLED', true);
ini_set( 'zlib.output_compression', false );

require_once( 'nusoap/nusoap.php' );

# create server
$l_oServer = new soap_server();

# namespace
$t_namespace = 'http://novaforge.bull.net';

# wsdl generation
$l_oServer->debug_flag = false;
$l_oServer->configureWSDL( 'SpipConnect', $t_namespace );
$l_oServer->wsdl->schemaTargetNamespace = $t_namespace;
$l_oServer->xml_encoding = "UTF-8";
$l_oServer->soap_defencoding = "UTF-8";
$l_oServer->decode_utf8 = false;
$l_oServer->style = 'document';

//---------- registration of structure/objects
### StringArray
$l_oServer->wsdl->addComplexType(
	'StringArray',
	'complexType',
	'array',
	'',
	'SOAP-ENC:Array',
	array(),
	array(array(
		'ref'				=> 'SOAP-ENC:arrayType',
		'wsdl:arrayType'	=> 'xsd:string[]'
	)),
	'xsd:string'
);

### UserData
$l_oServer->wsdl->addComplexType(
    'UserData',
    'complexType',
    'struct',
    'all',
    '',
array(
        'nom' => array('name' => 'nom', 'type' => 'xsd:string'),
		'email' => array('name' => 'email', 'type' => 'xsd:string'),
		'login' => array('name' => 'login', 'type' => 'xsd:string'),
		'pass' => array('name' => 'pass', 'type' => 'xsd:string')  
)
);


$l_oServer->wsdl->addComplexType(
    'UserSite',
    'complexType',
    'struct',
    'all',
    '',
array(
        'url_site' => array('name' => 'url_site', 'type' => 'xsd:string'),
		'nom_site' => array('name' => 'nom_site', 'type' => 'xsd:string')
)
);

$l_oServer->wsdl->addComplexType(
    'UserInfo',
    'complexType',
    'struct',
    'all',
    '',
array(
        'nom' => array('name' => 'nom', 'type' => 'xsd:string'),	
		'id' => array('name' => 'id', 'type' => 'xsd:string'),
		'login' => array('name' => 'login', 'type' => 'xsd:string'),
		'email' => array('name' => 'email', 'type' => 'xsd:string'),
		'webmestre' => array('name' => 'webmestre', 'type' => 'xsd:string'),
		'statut' => array('name' => 'statut', 'type' => 'xsd:string'),
		'nom_site' => array('name' => 'nom_site', 'type' => 'xsd:string'),
		'url_site' => array('name' => 'url_site', 'type' => 'xsd:string')
)
);

$l_oServer->wsdl->addComplexType(
    'SiteInfo',
    'complexType',
    'struct',
    'all',
    '',
array(
        'url_site' => array('name' => 'url_site', 'type' => 'xsd:string'),
		'nom_site' => array('name' => 'nom_site', 'type' => 'xsd:string'),
		'email_webmaster' => array('name' => 'email_webmaster', 'type' => 'xsd:string'),
		'descriptif_site' => array('name' => 'descriptif_site', 'type' => 'xsd:string')
)
);


$l_oServer->wsdl->addComplexType(
    'SiteInput',
    'complexType',
    'struct',
    'all',
    '',
array(
        'site_id' => array('name' => 'site_id', 'type' => 'xsd:string'),
		'nom_site' => array('name' => 'nom_site', 'type' => 'xsd:string'),
		'descriptif_site' => array('name' => 'descriptif_site', 'type' => 'xsd:string')
)
);


//----------- registration of the methods ------------------------------------------

//----- connect ---
$l_oServer->register( 'connect',
array(
		'login'	=>	'xsd:string',
		'password'	=>	'xsd:string'
		),
		array(
		'return'	=>	'xsd:string'
		),
		$t_namespace,
		false, false, false,
	'Return the connected user or error message !'
	);

	//------- get roles used to be mapped with novaforge roles. -----
	$l_oServer->register( 'get_roles',
	array(),
	array(
		'return'	=>	'tns:StringArray'
	),
	$t_namespace,
	false, false, false,
	'get available roles for spip (statut,webmestre properties of spip_auteurs).'
);
	
	//----- add user account ---
	$l_oServer->register( 'add_user',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'userData' => 'tns:UserData'),        
	array(
		'return'	=>	'xsd:integer'
		),
		$t_namespace,
		false, false, false,
	'Return the user id'
	);


	//----- get user infos -------
	$l_oServer->register( 'get_user_info',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'login' => 'xsd:string'),
	array(
		'return'	=>	'tns:UserInfo'
		),
		$t_namespace,
		false, false, false,
	'Return result message'
	);

	//----- get user id -------
	$l_oServer->register( 'get_user_id',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'login' => 'xsd:string'),
	array(
		'return'	=>	'xsd:integer'
		),
		$t_namespace,
		false, false, false,
	'Return the user id'
	);

	//----- update user ---
	$l_oServer->register( 'update_user',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'login'	=>	'xsd:string',
			'userData' => 'tns:UserData'),
	array(
		'return'	=>	'xsd:boolean'
		),
		$t_namespace,
		false, false, false,
	'Return true if has been updated'
	);

	//----------- delete user ----------
	$l_oServer->register( 'delete_user',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'login' => 'xsd:string'),
	array(
		'return'	=>	'xsd:boolean'
		),
		$t_namespace,
		false, false, false,
	'Return true if has been deleted'
	);
	
	//------- get statuts into spip --------
	

	//----- create spip site ---
	$l_oServer->register( 'create_site',

	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'siteInput' => 'tns:SiteInput'),
	array(
		'return'	=>	'xsd:string'
		),
		$t_namespace,
		false, false, false,
	'Return the created site'
	);

	//----- delete spip site ---
	$l_oServer->register( 'delete_site',

	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'siteId' => 'xsd:string'),
	array(
		'return'	=>	'xsd:boolean'
		),
		$t_namespace,
		false, false, false,
	'Return true if site been deleted'
	);

	//----- update spip site ---
	$l_oServer->register( 'update_site',

	array('username' => 'xsd:string',
		'password' => 'xsd:string',
		'siteInput' => 'tns:SiteInput'),
	array(
		'return'	=>	'xsd:boolean'
		),
		$t_namespace,
		false, false, false,
	'Return true if has been updated'
	);


	//----- get site infos-------
	//--- not done into mantis
	$l_oServer->register( 'get_site',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',),
	array(
		'return'	=>	'tns:SiteInfo'
		),
		$t_namespace,
		false, false, false,
	'Return result message'
	);

	//----- add site for a user ---
	$l_oServer->register( 'add_user_site',
	array('username' => 'xsd:string',
		'password' => 'xsd:string',	
		'login' => 'xsd:string', 
		'siteId' => 'xsd:string',
		'role' => 'xsd:string' ),
	array(
		'return'	=>	'xsd:integer'
		),
		$t_namespace,
		false, false, false,
	'Return result message'
	);

	//--------------------------------------- other utilities functions ------------------------------
	/**
	* Checks if the request for the webservice is a documentation request (eg:
	* WSDL) or an actual webservice call.
	*
	* The implementation of this method is based on soap_server::service().
	*
	* @param $p_service    The webservice class instance.
	* @param $p_data       The input that is based on the post data.
	*/
	function mci_is_webservice_call( $p_service, $p_data )
	{
		global $QUERY_STRING;
		global $_SERVER;

		if ( isset( $_SERVER['QUERY_STRING'] ) ) {
			$t_qs = $_SERVER['QUERY_STRING'];
		} else if( isset( $GLOBALS['QUERY_STRING'] ) ) {
			$t_qs = $GLOBALS['QUERY_STRING'];
		} else if( isset( $QUERY_STRING ) && $QUERY_STRING != '' ) {
			$t_qs = $QUERY_STRING;
		}

		if ( isset( $t_qs ) && preg_match( '/wsdl/', $t_qs ) ){
			return false;
		} else if ( $p_data == '' && $p_service->wsdl ) {
			return false;
		} else {
			return true;
		}
	}
	# pass incoming (posted) data
	if ( isset( $HTTP_RAW_POST_DATA ) ) {
		$t_input = $HTTP_RAW_POST_DATA;
	} else {
		$t_input = implode( "\r\n", file( 'php://input' ) );
	}

	# only include related files, if the current
	# request is a webservice call (rather than webservice documentation request,
	# eg: WSDL).
	if ( mci_is_webservice_call( $l_oServer, $t_input ) ) {
		require_once( 'nf_core.php' );
	} else {
		# if we have a documentation request, do some tidy up to prevent lame bot loops e.g. /mantisconnect.php/mc_enum_etas/mc_project_get_versions/
		$parts = explode ( 'nf_core.php/', strtolower($_SERVER['SCRIPT_NAME'] ), 2 );
		if (isset( $parts[1] ) && (strlen ( $parts[1] ) > 0 ) ) {
			echo 'This is not a SOAP webservice request, for documentation, see ' .  $parts[0] . 'nf_spipconnect.php';
			exit();
		}
	}
	# Execute whatever is requested from the webservice.
	$l_oServer->service( $t_input );
	?>