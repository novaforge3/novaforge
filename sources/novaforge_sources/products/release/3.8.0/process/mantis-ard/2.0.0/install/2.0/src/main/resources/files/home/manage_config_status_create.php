<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2007-2009 BULL SAS
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
 
# This page create the defined status

require_once( 'core.php' );

form_security_validate( 'manage_config_status_create' );
 
# --Check if the input (specified by its name) is filled
function check_if_filled() {
	global $g_language_status;
	$filled = array();
	$filled['label_status'] = $_POST['label_status'];
	$filled['color_status'] = $_POST['color_status'];
	foreach ( $g_language_status as $language ) {
		$filled['name_status_'.$language] = $_POST['name_status_'.$language];
		$filled['title_status_'.$language] = $_POST['title_status_'.$language];
		$filled['button_status_'.$language] = $_POST['button_status_'.$language];
		$filled['email_status_'.$language] = $_POST['email_status_'.$language];
	}
	session_set( 'filled_status', $filled );
}

function create_status() {
	global $g_language_status;
		
	# --Label--
	$f_label_status = gpc_get_string( 'label_status' );
	#make sure the label status is not empty
	if ( is_blank( $f_label_status ) ) {
		error_parameters( 'label_status' );
		trigger_error( ERROR_EMPTY_FIELD, ERROR );
	}
	#make sure the label is only alphanumerical caracters
	elseif( !preg_match('/^[a-zA-Z0-9]+$/i', $f_label_status) ) {
			error_parameters( 'label_status' );
			trigger_error( ERROR_STATUS_INVALID_LABEL, ERROR );
	}	
	$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	foreach ( $t_enum_status as $t_to_status_id => $t_to_status_label ) {
		if ( $f_label_status == $t_to_status_label ) {
			error_parameters( 'label_status' );
			trigger_error( ERROR_STATUS_DUPLICATE_LABEL, ERROR );
		}
	}
	
	# --Name--
	$f_label_status_french = gpc_get_string( 'name_status_french' );
	#make sure the french name status is not already existing
	$t_enum_status_french = MantisEnum::getAssocArrayIndexedByValues( lang_get( 'status_enum_string_french' ) );
	foreach ( $t_enum_status_french as $t_to_status_id => $t_to_status_label ) {
		if ( $f_label_status_french == $t_to_status_label ) {
			error_parameters( 'name_french' );
			trigger_error( ERROR_STATUS_DUPLICATE_NAME_FRENCH, ERROR );
		}
	}
	
	$f_label_status_english = gpc_get_string( 'name_status_english' );
	#make sure the english name status is not already existing
	$t_enum_status_english = MantisEnum::getAssocArrayIndexedByValues( lang_get( 'status_enum_string_english' ) );
	foreach ( $t_enum_status_english as $t_to_status_id => $t_to_status_label ) {
		if ( $f_label_status_english == $t_to_status_label ) {
			error_parameters( 'name_english' );
			trigger_error( ERROR_STATUS_DUPLICATE_NAME_ENGLISH, ERROR );
		}
	}
	
	# --Color--
	$f_color_status = gpc_get_string( 'color_status' );
	#make sure the color status is a three hexadecimal number
	if ( !is_blank( $f_color_status) ) {
		if ( !preg_match('/^([0-9a-f]{2}){3}$/i', $f_color_status) ) {
			error_parameters( 'color status' );
			trigger_error( ERROR_STATUS_INVALID_COLOR, ERROR );
		}
	}
	#color by default
	else {
		$f_color_status = "C8C8C8";
	}
	
	# --title, button & email for each language
	foreach ( $g_language_status as $language ) {
		# --Name--
		$f_name_status = gpc_get_string( 'name_status_'.$language );
		if( is_blank($f_name_status) ) {
			error_parameters( $language.' name status' );
			trigger_error( ERROR_EMPTY_FIELD, ERROR );
		}
		$t_status_multi_language[$language]['name'] = $f_name_status;
		
		# --Title--
		$f_title_status = gpc_get_string( 'title_status_'.$language );
		if( is_blank($f_title_status) ) {
			error_parameters( $language.' title status' );
			trigger_error( ERROR_EMPTY_FIELD, ERROR );
		}
		$t_status_multi_language[$language]['title'] = $f_title_status;
			
		# --Button--
		$f_button_status = gpc_get_string( 'button_status_'.$language );
		if( is_blank($f_button_status) ) {
			error_parameters( $language.' button status' );
			trigger_error( ERROR_EMPTY_FIELD, ERROR );
		}
		$t_status_multi_language[$language]['button'] = $f_button_status;
		
		# --Email--
		$f_email_status = gpc_get_string( 'email_status_'.$language );
		if( !is_blank(gpc_get_string( 'email_status_'.DEFAULT_LANGUAGE )) ) {
			if ( is_blank($f_email_status) ) {
				error_parameters( $language.' email status' );
				trigger_error( ERROR_EMPTY_FIELD, ERROR );		
			}
		}
		else {
			if ( !is_blank($f_email_status) ) {
				error_parameters( $language.' email status' );
				trigger_error( ERROR_STATUS_OTHER_LANGUAGE_EMAIL, ERROR );		
			}
		}
		$t_status_multi_language[$language]['email'] = $f_email_status;  	
	}
	status_create( $f_label_status, $f_color_status, $t_status_multi_language);
}

if ( $_POST ) {
	#put in session filled data
	check_if_filled($_POST);
	
	# --Create submit
	if ( $_POST['status_create'] ) {
		create_status();
		$redirect = 'manage_config_status_page.php';
	}	
	
	form_security_purge( 'manage_config_status_create' );
	
	print_successful_redirect( $redirect );
}

//EOF
?>
