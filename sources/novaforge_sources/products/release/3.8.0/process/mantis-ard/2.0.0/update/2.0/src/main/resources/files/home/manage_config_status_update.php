<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2007-2009 BULL SAS
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
 
# This page update the listed status

require_once( 'core.php' );

form_security_validate( 'manage_config_update_status' );

# ------------------
# Check if the input (specified by its name) is filled
function check_if_filled() {
	global $g_language_status;
	$filled = array();
	if ( isset($_POST['label_status']) and $_POST['label_status'] != "" ) {
		$filled['label_status'] = $_POST['label_status'];
	}
	$filled['color_status'] = $_POST['color_status'];
	foreach ( $g_language_status as $language ) {
		$filled['name_status_'.$language] = $_POST['name_status_'.$language];
		$filled['title_status_'.$language] = $_POST['title_status_'.$language];
		$filled['button_status_'.$language] = $_POST['button_status_'.$language];
		$filled['email_status_'.$language] = $_POST['email_status_'.$language];
	}
	session_set( 'filled_status', $filled );
}

# ------------------
# Check if the form is ok, and if so, update the status
function update_status(&$p_label_status) {
	global $g_language_status;
	check_if_filled($_POST);
	
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
	
	status_update( $p_label_status, $f_color_status, $t_status_multi_language);		
}


# ------------------
# Delete all reference about the specified status in the workflow
function delete_workflow(&$p_label_status) {
	$t_project_id = helper_get_current_project();
	$t_global_workflow = parse_workflow( config_get( 'status_enum_workflow', null, null, $t_project_id) );
	$t_id_status = MantisEnum::getValue( config_get('status_enum_string'), $p_label_status);
	$t_workflow = array();
	foreach ( $t_global_workflow as $key => $t_type ) {
		if ( $key == "entry" ) {
			foreach ( $t_type as $t_id => $value ) {
				if ( $t_id != 0 and $t_id !=  $t_id_status) {
					$t_workflow_enum = "";
					foreach ( $value as $key => $value3 ) {
						if ( $key != 0 and $key !=  $t_id_status) {
							$t_label_status = MantisEnum::getLabel( config_get('status_enum_string'), $key);
							$t_workflow_enum .= $key.":".$t_label_status.",";
						}
					} 
					$t_workflow_enum = substr(trim($t_workflow_enum),0,-1);
					$t_workflow[$t_id] = $t_workflow_enum;
				}
			} 
		}
	}
	config_set( 'status_enum_workflow', $t_workflow, ALL_USERS, $t_project_id);
}


/**
 * Check if 
 * - the status is not a default status in mantis
 * - the status is not referenced in bugs, history, workflow transitions and workflow threshold
 * If so the status can be deleted.
 */
function delete_status(&$p_label_status) {	
	 $t_status_id = MantisEnum::getValue( config_get('status_enum_string'), $p_label_status);
	
	/* check that the status to delete is not a default MantisBT status */
    $t_default_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get_global( 'default_status_enum_string' ));	
	if (array_key_exists($t_status_id,$t_default_enum_status)) {
	    trigger_error( ERROR_STATUS_DELETE_DEFAULT_STATUS, ERROR );
	}

	/* check that the status to delete is not in used : bugs, history, workflow transitions, workflow thresholds */	
	$t_is_status_in_use = is_status_in_use($t_status_id);
	if ($t_is_status_in_use['return']) {
		error_parameters( $t_is_status_in_use['error_parameter']);
	    trigger_error( $t_is_status_in_use['error_msg'], ERROR );
	}	
}

$t_label_status = null;
foreach ( $_POST as $key => $value ) {

	# --UPDATE STATUS--
	if ( ereg("status_update", $key) ) {
    	$t_label_status = str_replace('status_update_', '', $key);
		update_status($t_label_status);
		form_security_purge( 'manage_config_update_status' );
		$redirect = 'manage_config_status_page.php';
    	break;
	}
	# --DELETE STATUS--
	elseif ( ereg("status_delete", $key) ) {
    	$t_label_status = str_replace('status_delete_', '', $key);
		delete_status($t_label_status);
		$redirect = 'manage_config_status_view.php?action=delete&label='.$t_label_status;
    	break;
	}
	# --CONFIRM DELETE STATUS--
	elseif ( ereg("status_confirm_delete", $key) ) {
    	$t_label_status = str_replace('status_confirm_delete_', '', $key);
		status_delete($t_label_status);
		form_security_purge( 'manage_config_update_status' );
		$redirect = 'manage_config_status_page.php';
    	break;
	}
	else {
		//ERROR NO ACTION
	}
}

print_successful_redirect( $redirect );	

//EOF	
?>
