<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * Copyright 2017  Atos, NovaForge Version 3 and above.
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
 
# This page update the listed status

require_once( 'core.php' );
require_api( 'form_api.php' );

form_security_validate( 'manage_config_update_status' );
# ------------------
# Check if the input (specified by its name) is filled
function check_if_filled(&$language_status) {
	$filled = array();
	if ( isset($_POST['label_status']) and $_POST['label_status'] != "" ) {
		$filled['label_status'] = $_POST['label_status'];
	}
	if ( isset($_POST['language_status']) and $_POST['language_status'] != "" ) {
		$filled['language_status'] = $_POST['language_status'];
		$language_status =  explode(",",$_POST['language_status']);
	}
	$filled['color_status'] = $_POST['color_status'];
	foreach ( $language_status as $language ) {
		$filled['name_status_'.$language] = $_POST['name_status_'.$language];
		$filled['title_status_'.$language] = $_POST['title_status_'.$language];
		$filled['button_status_'.$language] = $_POST['button_status_'.$language];
		$filled['email_status_'.$language] = $_POST['email_status_'.$language];
	}
	if ( isset($_POST['cur_project_id']) and $_POST['cur_project_id'] != "" ) {
		$filled['cur_project_id'] = $_POST['cur_project_id'];
	}
    else
   	{
		$filled['cur_project_id'] = helper_get_current_project();
	}
	session_set( 'filled_status', $filled );
}

/**
 * Delete session variable specific to status creation :  filled_status
 * @return void
 */
function delete_status_session()
{
	if (count(session_get('filled_status',array())) != 0 ) {
		session_delete('filled_status');
	}
}

/**
 * Retrieve currentProjectId from the session
 * @return currentProjectId
 */
function getCurProjectId(){
	if (count(session_get('filled_status',array())) != 0 )
	{
		$filled = session_get('filled_status');
		$cur_project_id = $filled['cur_project_id'];
	} 
	else {
		$cur_project_id = '';
	}	
	return $cur_project_id;
}

# ------------------
# Check if the form is ok, and if so, update the status
function update_status(&$p_label_status) {

	check_if_filled($language_status);	
	
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
	foreach ( $language_status as $language ) {
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
	# --ADD OR DELETE LANGUAGE--
    if ( ereg("add_language", $key) or  ereg("delete_language", $key)) {
	   
		check_if_filled($t_language_status);	
		
		if ( ereg("add_language", $key) ) {
    		$t_label_status = str_replace('add_language_', '', $key);
			array_push($t_language_status, gpc_get_string('language_to_add'));
    	}
    	if ( ereg("delete_language", $key) ) {
    		$t_label_status = str_replace('delete_language_', '', $key);
			unset($t_language_status[array_search(gpc_get_string('language_to_delete'), $t_language_status)]);
    	}	
        sort($t_language_status);		
		$t_language_status = implode(",",$t_language_status);
		$cur_project_id = getCurProjectId();		
		$redirect = 'manage_config_status_view.php?cur_project_id='.$cur_project_id.'&action=update&label='.$t_label_status.'&language_status='.$t_language_status;
    	break;
	}
	# --UPDATE STATUS--
	elseif ( ereg("status_update", $key) ) {
    	$t_label_status = str_replace('status_update_', '', $key);
		update_status($t_label_status);		
		$cur_project_id = getCurProjectId();
		delete_status_session();
		form_security_purge( 'manage_config_update_status' );
		$redirect = 'manage_config_status_page.php?cur_project_id='.$cur_project_id;
    	break;
	}
	# --DELETE STATUS--
	elseif ( ereg("status_delete", $key) ) {
		check_if_filled($t_language_status);	
    	$t_label_status = str_replace('status_delete_', '', $key);
		delete_status($t_label_status);
		$redirect = 'manage_config_status_view.php?action=delete&label='.$t_label_status;
    	break;
	}
	# --CONFIRM DELETE STATUS--
	elseif ( ereg("status_confirm_delete", $key) ) {
    	$t_label_status = str_replace('status_confirm_delete_', '', $key);
		$cur_project_id = gpc_get_string('cur_project_id');	
		status_delete($t_label_status);
		delete_status_session();
		form_security_purge( 'manage_config_update_status' );
		$redirect = 'manage_config_status_page.php?cur_project_id='.$cur_project_id;
    	break;
	}
	else {
		//ERROR NO ACTION
	}
}

print_successful_redirect( $redirect );	

//EOF	
?>
