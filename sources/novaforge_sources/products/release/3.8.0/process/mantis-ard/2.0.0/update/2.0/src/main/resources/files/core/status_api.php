<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
* @copyright Copyright 2017  Atos, NovaForge Version 3 and above.
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
 
require_once( dirname( __FILE__ ) . DIRECTORY_SEPARATOR . 'error_api.php' );
require_api( 'workflow_api.php');

define ("NAME" , "name");
define ("TITLE" , "title");
define ("BUTTON" , "button");
define ("EMAIL" , "email");
define ("NOTIFICATION_EMAIL" , "notification_email");
if ( auth_is_user_authenticated() ) {
	define ("DEFAULT_LANGUAGE" , user_pref_get( auth_get_current_user_id() )->language);
}
else {
	define ("DEFAULT_LANGUAGE" , lang_get_default());
}
define ("CREATE" , "create");
define ("UPDATE" , "update");
define ("DELETE" , "delete");

### Status API ###
#//TODO remplace get_line_in_file par array_search() avec des patterns


#############################
#			LABEL           #
#############################
/**
 * Create a label status, and insert it in the status_enum_string with a calculated step
 * @param array $p_instance_file
 * @param string $p_label_status
 * @param array $p_new_enum_status : the new status_enum_string with the p_label_status inserted
 */
function create_label_status(&$p_instance_file, &$p_label_status, &$p_new_enum_status) {	

	$p_working_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	$t_default_status_prev = config_get( 'default_status_prev' );
    $t_default_status_next = config_get( 'default_status_next' );

	# search the biggest unused id between two markers 
	foreach ( $p_working_enum_status as $t_status_id => $t_status_label ){
	  if ($t_status_id >= $t_default_status_prev && $t_status_id < $t_default_status_next-1 ) {
	     $t_new_id = $t_status_id;
	  }
	}
	if ($t_new_id >= $t_default_status_prev && $t_new_id <$t_default_status_next-1 ) {
		$t_new_id = $t_new_id+1;
		if (array_key_exists ($t_new_id,$p_working_enum_status)){
			error_parameters( $p_working_enum_status[$t_new_id], $p_working_enum_status[($t_new_id+1)] );
			trigger_error( ERROR_STATUS_ADDING, ERROR );
		}
		else {
			$p_working_enum_status[$t_new_id] = $p_label_status;
			
			$p_new_enum_status = label_status_refactoring($t_new_id,$p_working_enum_status);
			
			#create the new variable
			$new_status_enum = "\t".'$g_status_enum_string = \'';
			foreach ( $p_new_enum_status as $t_to_status_id => $t_to_status_label ) {
				$new_status_enum .= $t_to_status_id.":$t_to_status_label,";
			}
			$new_status_enum = substr($new_status_enum,0,-1);
			$new_status_enum .= "';\n";
					
			#create the new status_enum
			$line = get_line_in_file($p_instance_file, "g_status_enum_string");
			
			#update the line in the array file
			$p_instance_file[$line] = $new_status_enum;
		}
	}
}

/**
 * Calculate new status_id of a given status_id according to a list of status_id in status_enum_string
 * This new status_id is calculated to be between the nearest default_status_id and/or status_id in used in Mantis
 * if several status around the new status_id are found and not in used in Mantis, they are taken into account for the new status_id computing 
 * @param int $p_status_id : status_id to compute
 * @param array $p_working_enum_status : list of status inside of which the status_id is inserted or deleted
 * @return array $t_new_enum_status : the new list of status with status_id computed
 */
 
function label_status_refactoring($p_status_id,$p_working_enum_status ){

    ksort($p_working_enum_status);
    $t_default_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get_global( 'default_status_enum_string' ));

	foreach ( $p_working_enum_status as $t_status_id => $t_status_label ){
	  $t_is_status_in_use = is_status_in_use($t_status_id);
	  
	  if ($t_status_id < $p_status_id && (array_key_exists ($t_status_id,$t_default_enum_status) || $t_is_status_in_use['return'] )){
			$t_previous_ident = $t_status_id;
	  }
	  if ($t_status_id > $p_status_id && (array_key_exists ($t_status_id,$t_default_enum_status) || $t_is_status_in_use['return']) && $t_next_ident==null) {
	        $t_next_ident = $t_status_id;
	  }
	}
	
	$t_tmp_enum_status = array();
	$t_new_enum_status = array();
	foreach ( $p_working_enum_status as $t_status_id => $t_status_label ){
	   if ($t_status_id > $t_previous_ident && $t_status_id < $t_next_ident) {
	      $t_tmp_enum_status[$t_status_id]= $t_status_label;
	   }
	   else{
	      $t_new_enum_status[$t_status_id]= $t_status_label;
	   }
	}
	
	step_status_computing ($t_new_enum_status, $t_tmp_enum_status, $t_previous_ident, $t_next_ident);
	
	ksort( $t_new_enum_status);

	return $t_new_enum_status;
}

/**
 * Delete a label status into status_enum_string
 * @param array $p_instance_file
 * @param string $p_label_status : Status to delete
 * @param array $p_new_enum_status : the new status_enum_string with the status deleted
 */

function delete_label_status(&$p_instance_file, &$p_label_status, &$p_new_enum_status) {
	
	$t_working_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	
	$t_id_to_delete = array_search($p_label_status, $t_working_enum_status);
	
	if ($t_id_to_delete) {
		unset($t_working_enum_status[$t_id_to_delete]);
		$p_new_enum_status = label_status_refactoring($t_id_to_delete,$t_working_enum_status);
		
		#create the new variable
		$new_status_enum = "\t".'$g_status_enum_string = \'';
		foreach ( $p_new_enum_status as $t_to_status_id => $t_to_status_label ) {
			$new_status_enum .= $t_to_status_id.":$t_to_status_label,";
		}
		$new_status_enum = substr($new_status_enum,0,-1);
		$new_status_enum .= "';\n";
			
		#create the new status_enum
		$line = get_line_in_file($p_instance_file, "g_status_enum_string");
		
		#update the line in the array file
		$p_instance_file[$line] = $new_status_enum;	
	}
}
#############################
#         	COLOR         	#
#############################
/**
 * Create a color specified by the label' status
 * @param array $p_instance_file
 * @param string $p_label_status
 * @param string $p_color_status
 */
function create_color_status(&$p_instance_file, &$p_label_status, &$p_color_status) {
	$line = get_line_in_file($p_instance_file, "g_status_colors")+1;
	$status_colors = "\t\t'".$p_label_status."'\t=> '#".$p_color_status."',\n";
	$p_instance_file = insert_in_file($p_instance_file, $status_colors, $line);
}

/**
 * Update a color specified by the label' status
 * @param array $p_instance_file
 * @param string $p_label_status
 * @param string $p_color_status
 */
function update_color_status(&$p_instance_file, &$p_label_status, &$p_color_status) {
	delete_color_status($p_instance_file, $p_label_status);
	create_color_status($p_instance_file, $p_label_status, $p_color_status);
}

/**
 * Delete a color specified by the label' status
 * @param array $p_instance_file
 * @param string $p_label_status
 */
function delete_color_status(&$p_instance_file, &$p_label_status) {
	$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	$line = get_line_in_file($p_instance_file, "g_status_colors")+1;
	for($i=$line; $i<($line+count($t_enum_status)); $i++) {
		if ( ereg($p_label_status, $p_instance_file[$i]) ) {
			$p_instance_file = remove_in_file($p_instance_file, $i );
		}
	}
}

#############################
#			NAME        	#
#############################

/**
 * Add new status name  for all languages
 * @param array $p_instance_file
 * @param multi-array $p_status_multi_language
 * @param string $p_label_status : the status to add
 * @param array $p_new_enum_status : the status_enum_string with the status added, it is the reference for the update of status_enum_string_language 
 */
function create_name_status(&$p_instance_file, $p_status_multi_language, $p_label_status, $p_new_enum_status) {

	$t_current_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	
    /* search new_id for the label _status */	
	$t_new_ident = array_search($p_label_status, $p_new_enum_status);
	
	if ($t_new_ident) {
	    
	    $t_status_languages = array_keys($p_status_multi_language);
		name_status_language_refactoring ($p_instance_file,$p_status_multi_language,$t_status_languages,$t_current_enum_status,$p_new_enum_status,$t_new_ident,null);
	       		
		# add by default english name for all languages
		/* Only for MantisBT
		$t_default_languages = array_diff(config_get( 'language_choices_arr' ), session_get('error_language'), $t_status_languages);
		#name_status_language_refactoring ($p_instance_file,$p_status_multi_language,$t_default_languages,$t_current_enum_status,$p_new_enum_status, $t_new_ident,null);
		*/
			
	} /* END if $t_new_ident */
}

/**
 * Recalculate id for all status languages from current_enum_status and new_enum_status
 * @param array $p_current_enum_status
 * @param array $p_new_enum_status
 * @param array $p_enum_status_language
 * @return array $p_new_enum_status_language : the new enum status language
 */
function name_status_refactoring($p_current_enum_status, $p_new_enum_status, $p_enum_status_language){

    $t_new_enum_status_language = array();
	foreach ( $p_enum_status_language as $t_status_language_id => $t_status_language_label ) {
		/* récupération de la valeur depuis current */
		if (array_key_exists($t_status_language_id, $p_current_enum_status)) {
			$t_current_label = $p_current_enum_status[$t_status_language_id];
			$t_status_language_id = array_search($t_current_label, $p_new_enum_status);
		}
		$t_new_enum_status_language[$t_status_language_id] = $t_status_language_label ;
	}

	ksort($t_new_enum_status_language);
	
	return $t_new_enum_status_language;
}

/**
 * Recalculate id for all status languages for new_enum_status from current_enum_status
 * add or delete one status language
 * @param array $p_instance_file
 * @param multi-array $p_status_multi_language : specific status languages
 * @param array $p_status_languages : all status languages
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string 
 * @param  int $p_id_to_add  : status id to add
 * @param  int $p_id_to_delete : status id to delete 
 **/
function name_status_language_refactoring (&$p_instance_file, $p_status_multi_language = null, $p_status_languages, $p_current_enum_status, $p_new_enum_status, $p_id_to_add = null, $p_id_to_delete = null){
			
	foreach ( $p_status_languages as $t_language) {
	
			$t_update_config = false; /* marker to update configuration file */
			$t_refactoring = 0;	/* marker to check if refactoring is necessary */
			$t_enum_status_language = MantisEnum::getAssocArrayIndexedByValues( lang_get( 'status_enum_string_'.$t_language ) );
			
			$t_refactoring = count(array_diff_assoc($p_new_enum_status, $p_current_enum_status));
			if ($p_id_to_add) {
			   $t_refactoring = count(array_diff_assoc($p_current_enum_status, $p_new_enum_status));
			}
						
			/* id to delete */
			if ($p_id_to_delete) {
				unset($t_enum_status_language[$p_id_to_delete]);
				$t_update_config = true;	
			}
			/* check if name status refactoring is necessary */
			if ($t_refactoring > 0 ) {
				$t_new_enum_status_language = name_status_refactoring($p_current_enum_status,$p_new_enum_status,$t_enum_status_language);
				$t_update_config = true;
			} else {
			    $t_new_enum_status_language = $t_enum_status_language;
			}
			/* id to add */
			if ($p_id_to_add) {
			    $t_status_language = $p_status_multi_language[$t_language];
				if ($t_status_language){
					$t_new_enum_status_language[$p_id_to_add] = $t_status_language[NAME];				
				} else {
				   $t_new_enum_status_language[$p_id_to_add] = $p_status_multi_language[DEFAULT_LANGUAGE][NAME];
				}
				ksort($t_new_enum_status_language);
				$t_update_config = true;
			}
			
			/* Update status config_file if necessary */
			if ($t_update_config ){
						
				#create the new variable
				$new_status_name = "\t".'$s_status_enum_string_'.$t_language." = '";
				foreach ( $t_new_enum_status_language as $t_status_id => $t_status_label ) {
					if ( !($t_status_label == null or $t_status_label == "")) {
						  $new_status_name .= $t_status_id.':'. addslashes(stripslashes($t_status_label)).',';			
					}
				}
				$new_status_name = substr($new_status_name, 0, -1);
				$new_status_name .= "';\n";
								
				# get the line to put the $new_status_enum_string and update the line in the array file
				$line = get_line_in_file($p_instance_file, "status_enum_string_".$t_language);
								
				#update the line in the array file
				$p_instance_file[$line] = $new_status_name;	
			}			
		}
}

/**
 * Update name for a specified status for all languages
 * @param array $p_instance_file
 * @param string $p_label_status
 * @param multi-array $p_status_multi_language
 * @param array $p_languages_to_add
 */
function update_name_status(&$p_instance_file, &$p_label_status, &$p_status_multi_language) {
	global $g_language_status;
	$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	foreach ( $g_language_status as $language) {
		$t_new_name_status = addslashes($p_status_multi_language[$language][NAME]);
		
		$t_name_status_array = MantisEnum::getAssocArrayIndexedByValues( lang_get( 'status_enum_string_'.$language ) );
		$line = get_line_in_file($p_instance_file, "status_enum_string_".$language);
		$t_actual_id_status = array_search($p_label_status, $t_enum_status);
		$t_new_names_status = "\t".'$s_status_enum_string_'.$language." = '";
		foreach ( $t_name_status_array as $t_id_status => $t_name_status ) {
			if ( $t_actual_id_status == $t_id_status ) {
				$t_new_names_status .= $t_id_status.':'.$t_new_name_status.',';	
			}
			else {
				$t_new_names_status .= $t_id_status.':'.$t_name_status.',';
			}
		}
		$t_new_names_status = substr($t_new_names_status, 0, -1);
		$t_new_names_status .= "';\n";
		$p_instance_file[$line] = $t_new_names_status;
	}
	
}

/**
 * Delete name for a specified status for all languages
 * @param array $p_instance_file
 * @param string $p_label_status : Status to delete
 * @param array $p_new_enum_status : the new status_enum_string with the status deleted
 */
function delete_name_status(&$p_instance_file, &$p_label_status, &$p_new_enum_status) {
    global $g_language_status;

    $t_working_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	
	/* search new_id for the label _status */	
	$t_id_to_delete = array_search($p_label_status, $t_working_enum_status);
			
	//$t_status_languages = array_diff(config_get( 'language_choices_arr' ), session_get('error_language'));
	$t_status_languages = $g_language_status;
	
	if ($t_id_to_delete) {
		name_status_language_refactoring ($p_instance_file, null,$t_status_languages,$t_working_enum_status,$p_new_enum_status, null,$t_id_to_delete);	
	}
}


#############################
#			FIELD          	#
#############################

/**
 * Create specified field for a specified status
 * @param array $p_instance_file
 * @param string $p_label_status
 * @param string $p_field, the type of field to create
 * @param string $p_value
 * @param string $p_language - null by default
 */
function create_field_status(&$p_instance_file, &$p_label_status, $p_field, $p_value, &$p_language = null) {
	$p_value = addslashes($p_value);
	switch ( $p_field ) {
		case TITLE:
			$t_sentence = "\t".'$s_'.$p_label_status."_bug_title_$p_language = '".$p_value."';\n";
			break;
		case BUTTON:
			$t_sentence = "\t".'$s_'.$p_label_status."_bug_button_$p_language = '".$p_value."';\n";
			break;
		case EMAIL:
			$t_sentence = "\t".'$s_email_notification_title_for_status_bug_'.$p_label_status."_$p_language = '".$p_value."';\n";
			break;
		case NOTIFICATION_EMAIL:
			$t_sentence = "\t".'$g_default_email_on_'.$p_label_status." = $p_value;\n";
			break;
		default:
			break;
	}
	
	$line = get_line_in_file($p_instance_file, "#".$p_field);
	if( $line != false ) {
		$line += 1;
		$p_instance_file = insert_in_file($p_instance_file, $t_sentence, $line);	
	}

}
/**
 * Delete specified field for a specified status
 * @param array $p_instance_file
 * @param string $p_label_status
 * @param string $p_field, the type of field to delete
 * @param string $p_language - null by default
 */
function delete_field_status(&$p_instance_file, &$p_label_status, $p_field, &$p_language = null) {
	switch ( $p_field ) {
		case TITLE:
			$t_indice = "s_".$p_label_status."_bug_title_".$p_language;
			break;
		case BUTTON:
			$t_indice = "s_".$p_label_status."_bug_button_".$p_language;
			break;
		case EMAIL:
			$t_indice = "s_email_notification_title_for_status_bug_".$p_label_status."_".$p_language;
			break;
		case NOTIFICATION_EMAIL:
			$t_indice = "g_default_email_on_".$p_label_status;
			break;
		default:
			break;
	}
	while (get_line_in_file($p_instance_file, $t_indice) != false ) {
		$p_instance_file = remove_in_file($p_instance_file, get_line_in_file($p_instance_file, $t_indice));	
	}
}
#############################
#			STATUS        	#
#############################

/**
 * Create a new bug status
 * @param string $p_label_status
 * @param string $p_color_status
 * @param multi-array $p_status_multi_language
 */
function status_create(&$p_label_status, &$p_color_status, &$p_status_multi_language) {
	
	# get the conf instance file
	$instance = helper_get_current_project();
	$instance_file = file("conf-instance/config_status_$instance.php");
	
	# --Label--	
	create_label_status($instance_file, $p_label_status, $p_new_enum_status);
	
	# --Color--
	create_color_status($instance_file, $p_label_status, $p_color_status);
	
	# --Name--
	create_name_status($instance_file, $p_status_multi_language, $p_label_status, $p_new_enum_status);
	
	# --Title Button & Email--
	$cpt = 0;
	foreach ( $p_status_multi_language as $language => $status_language ) {		
		create_field_status($instance_file, $p_label_status, TITLE, $status_language[TITLE], $language);
		create_field_status($instance_file, $p_label_status, BUTTON, $status_language[BUTTON], $language);
		create_field_status($instance_file, $p_label_status, EMAIL, $status_language[EMAIL], $language);
			
		# --Notification Email-- 
		if ( is_blank($p_status_multi_language[$language][EMAIL]) ) {
			$cpt++;
		}
	}
	if ( $cpt > 0 ) {
		$notification_email = "OFF";
	} else {  
		$notification_email = "ON";
}
	
	create_field_status($instance_file, $p_label_status, NOTIFICATION_EMAIL, $notification_email, $language);
	
	# --Update File--
	write_file("conf-instance/config_status_$instance.php", $instance_file);
	
}

/**
 * Update a status
 * @param string $p_label_status
 * @param string $p_color_status
 * @param multi-array $p_status_multi_language
 */
function status_update(&$p_label_status, &$p_color_status, &$p_status_multi_language) {

	# get the conf instance file
	$instance = helper_get_current_project();
	$instance_file = file("conf-instance/config_status_$instance.php");
	
	# --About the color--
	update_color_status($instance_file, $p_label_status, $p_color_status);
	
	# --Name--
	update_name_status($instance_file, $p_label_status, $p_status_multi_language);
	
	# --Manage Title Button & Email--
	$cpt = 0;
	foreach ( $p_status_multi_language as $language => $status_language ) {
		# --About the title--
		$title = $p_label_status.'_bug_title_'.$language;
		if ( lang_get($title) != $status_language[TITLE] ) {
			delete_field_status($instance_file, $p_label_status, TITLE, $language);
			create_field_status($instance_file, $p_label_status, TITLE, $status_language[TITLE], $language);
		}
		# --About the button--
		$button = $p_label_status.'_bug_button_'.$language;
		if ( lang_get($button) != $status_language[BUTTON] ) {
			delete_field_status($instance_file, $p_label_status, BUTTON, $language);
			create_field_status($instance_file, $p_label_status, BUTTON, $status_language[BUTTON], $language);
		}
		
		# --About the email--
		$email = 'email_notification_title_for_status_bug_'.$p_label_status.'_'.$language;
		if ( lang_get($email) != $status_language[EMAIL] ) {
			delete_field_status($instance_file, $p_label_status, EMAIL, $language);
			create_field_status($instance_file, $p_label_status, EMAIL, $status_language[EMAIL], $language);
		}
		
		# --Notification Email-- 
		if ( is_blank($p_status_multi_language[$language][EMAIL]) ) {
			$cpt++;
		}
	}
	if ( $cpt > 0 ) {
		$notification_email = "OFF";
	} else {
		$notification_email = "ON";
	}
	
	delete_field_status($instance_file, $p_label_status, NOTIFICATION_EMAIL);
	create_field_status($instance_file, $p_label_status, NOTIFICATION_EMAIL, $notification_email);
	
	# --Update File--
	write_file("conf-instance/config_status_$instance.php", $instance_file);
}

/**
 * Delete a status
 * @param string $p_label_status
 */
function status_delete( &$p_label_status) {
	global $g_language_status;
	# get the conf instance file
	$instance = helper_get_current_project();
	$instance_file = file("conf-instance/config_status_$instance.php");
	
	# --Label
	delete_label_status($instance_file, $p_label_status,$p_new_enum_status);
	
	# --Color
	delete_color_status($instance_file, $p_label_status);
	
	# --Name
	delete_name_status($instance_file, $p_label_status, $p_new_enum_status);
	
	# --For each languages--
	#get all the existing languages
	foreach ( $g_language_status as $language ) {	
		delete_field_status($instance_file, $p_label_status, TITLE, $language);
		delete_field_status($instance_file, $p_label_status, BUTTON, $language);	
		delete_field_status($instance_file, $p_label_status, EMAIL, $language);	
	} 

    # --Email notification 	
	delete_field_status($instance_file, $p_label_status, NOTIFICATION_EMAIL);
	
	# --Update File--
	write_file("conf-instance/config_status_$instance.php", $instance_file);
}

	
/**
 * Update classification of status_enum_string according to p_classification_status (label_status => order)
 * Check that : 
 * - the first label status p_classification_status is the 'new' status.
 * - the last label status p_classification_status is the 'closed' status.
 * - the p_classification_status order matches with default Mantis status 
 *   and used customized status
 * and compute new key for non default and unused customized status
 * @param array $p_classification_status
 */
function update_classification(&$p_classification_status) {
    global $g_language_status;

	$t_status_languages = $g_language_status;
    
    $t_previous_ident= '';
	$t_next_ident='';
    asort($p_classification_status);
    # get the conf instance file
	$instance = helper_get_current_project();
	$instance_file = file("conf-instance/config_status_$instance.php");
	
	$t_current_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	$t_default_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get_global( 'default_status_enum_string' ) );
	$t_new_enum_status = array(); 	/* array for final status_enum_string */
	$t_tmp_enum_status = array(); 	/* array for status key computing */
	$t_computing_status = false;  	/* marker for computing the key status */
	$t_previous_ident = 0; 			/* Begin marker for status key computing  */
	$t_next_ident = 0; 				/* End marker for status key computing  */
	$t_update_config = 0 ; 			/* marker to check the config file has to be updated (if one or more computing has been done) */
	
	/* check that the first status is the NEW status and that the last status is CLOSED */
	reset($p_classification_status);
	$t_first_value = key($p_classification_status);
	if ($t_first_value <> 'new' ){
		trigger_error( ERROR_STATUS_CLASSIFICATION_NEW, ERROR );
	}
	end($p_classification_status);
	$t_last_value = key($p_classification_status);
	if ($t_last_value <> 'closed' ){
		trigger_error( ERROR_STATUS_CLASSIFICATION_CLOSED, ERROR );
	}
    /* check that the new classification matches with the order of used status and default status mantis  */
	$t_sorted_enum_status = array();
	foreach ($t_current_enum_status as $t_status_id => $t_status_label){	
		if (array_key_exists($t_status_id,$t_default_enum_status)) {
			$t_sorted_enum_status[$t_status_id] = $t_status_label;
		} else {
			$t_is_status_in_use = is_status_in_use($t_status_id);
			if ($t_is_status_in_use['return']) {
				$t_sorted_enum_status[$t_status_id] = $t_status_label;
			}
		}	
	}
	$t_arr_to_check = array_keys($p_classification_status);
    if (!is_well_sorted ($t_sorted_enum_status,$t_arr_to_check)){
	   trigger_error( ERROR_STATUS_CLASSIFICATION_ORDER, ERROR );
	}
		
	# --Update status_enum_string--
	# rewrite the $s_status_enum_string_line variable
	foreach ( $p_classification_status as $t_current_label => $t_weight ) {
	
		$t_previous_computing_status = $t_computing_status;
		
	    $t_current_ident = MantisEnum::getValue( config_get('status_enum_string'), $t_current_label);
			
		/*  Default Mantis status can't be key computing */
		if (in_array($t_current_label, $t_default_enum_status)){
		    $t_new_enum_status [$t_current_ident]= $t_current_label;
			$t_computing_status = false;
		}
		else
		{
		    /* Status in used in Mantis can't be key computing */
			$t_is_status_in_use = is_status_in_use ($t_current_ident);
			if ($t_is_status_in_use['return']){
			     $t_new_enum_status [$t_current_ident]= $t_current_label;
				 $t_computing_status = false;
			}
			else
			{
			   $t_computing_status = true;
			}
		}
		/* Prepare begin marker and end marker for computing non default and no-used status  */
		if (!$t_computing_status) {
		   /* Initialization - End marker setting  et Begin marker unsetting  */
		   if ( $t_previous_ident == 0 && $t_next_ident == 0){
		      $t_previous_ident = $t_current_ident;
			  $t_next_ident = '';
		   } 
		   /* End marker setting */
		   else if ($t_previous_ident != 0 && $t_next_ident == 0){
			  $t_next_ident = $t_current_ident;
		   } 
		   /* Begin marker setting and End marker unsetting */
		   else if ($t_previous_ident != 0 && $t_next_ident != 0){
		      $t_previous_ident = $t_next_ident;
			  $t_next_ident = $t_current_ident;
		   }
		}
		else {/* status for key computing */ 			
			$t_tmp_enum_status[$t_current_ident] = $t_current_label;
		}		
		/* check that the list of new status if complete in order to compute key */
		if ($t_previous_computing_status && !$t_computing_status){
			
			if (!array_key_exists($t_previous_ident, $t_new_enum_status)) {
			   error_log('status_api - update_classification - Begin marker : '.$t_previous_ident);
			   trigger_error( ERROR_STATUS_TECHNICAL, ERROR );	
			} 
			if ($t_next_ident != null) {
				if (!array_key_exists($t_next_ident, $t_new_enum_status)) {
					error_log('status_api - update_classification - End marker : '.$t_next_ident);
					trigger_error( ERROR_STATUS_TECHNICAL, ERROR );
				}
				else {
					/* status keys computing */	
					step_status_computing ($t_new_enum_status, $t_tmp_enum_status, $t_previous_ident, $t_next_ident);
																			
					$t_tmp_enum_status = array();
					$t_step ='';	
				}
			}
		} // end computing
	}	// fin foreach
	
	# Update status config file 
	# Check if config file has to be updated
    $t_update_config = count(array_diff_assoc($t_new_enum_status,$t_current_enum_status));

    if( $t_update_config > 0){
	
	    # --Update status_enum_string--
		#create the new variable
		$new_status_enum_string = "\t".'$g_status_enum_string = \'';
		foreach ( $t_new_enum_status as $t_to_status_id => $t_to_status_label ) {
				$new_status_enum_string .= $t_to_status_id.":$t_to_status_label,";
		}
		$new_status_enum_string = substr($new_status_enum_string,0,-1);
		$new_status_enum_string .= "';\n";

		# get the line to put the $new_status_enum_string
		$status_enum_string_line = get_line_in_file($instance_file, "g_status_enum_string");
		$instance_file[$status_enum_string_line] = $new_status_enum_string;
		
		#--Update status name--
		# rewrite the $s_status_enum_string_language line variable
		name_status_language_refactoring($instance_file,null,$t_status_languages,$t_current_enum_status,$t_new_enum_status,null,null);
		
		# --Update File--
		write_file("conf-instance/config_status_$instance.php", $instance_file);	
	}						
}


/**
 * Check if 
 * - the status is not referenced in bugs, history, workflow transitions and workflow threshold
 *  @param int $p_status_id
 *  @return array $t_is_status_in_use ('return' => boolean, 'error_msg' => string, 'error_parameter' => string)
 */
function is_status_in_use ($p_status_id){

    $t_is_status_in_use = array();
	$t_is_status_in_use['return'] = false;
	$t_is_status_in_use['error_msg'] = '';
	$t_is_status_in_use['error_parameter'] = '';
	
	$t_current_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	$project_id = helper_get_current_project();
	
	/* check if status is used in 'bug' table */
	$count_bug = bug_count_by_status($project_id ,$p_status_id) ;

	if ($count_bug > 0) {
	    $t_is_status_in_use ['return'] = true;
		$t_is_status_in_use ['error_msg'] = ERROR_STATUS_DELETE_USED_IN_BUGS;
		$t_is_status_in_use ['error_parameter'] = $t_current_enum_status[$p_status_id];
		return $t_is_status_in_use;	
	}
    /* check if status is used in 'bug_history' table */
	$count_history_bug = history_count_by_status($project_id ,$p_status_id) ;
	if ($count_history_bug > 0) {
	    $t_is_status_in_use ['return'] = true;
		$t_is_status_in_use ['error_msg'] = ERROR_STATUS_DELETE_USED_IN_BUGS;
		$t_is_status_in_use ['error_parameter'] = $t_current_enum_status[$p_status_id];
		return $t_is_status_in_use;		
	}
	/* check if status is used in workflow */
	$t_project = helper_get_current_project();
	$t_project_workflow = workflow_parse( config_get( 'status_enum_workflow', null, ALL_USERS, $t_project ) );
	$t_status_enum_string = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	
	foreach ( $t_status_enum_string as $t_status_id => $t_status_label ) {
	
		$t_entry = $t_project_workflow['entry'][$t_status_id] ;
		if ($p_status_id == $t_status_id  && count($t_entry) > 0) {
			$t_is_status_in_use ['return'] = true;
			$t_is_status_in_use ['error_msg'] = ERROR_STATUS_DELETE_USED_IN_WORKFLOW;
			$t_is_status_in_use ['error_parameter'] = $t_current_enum_status[$p_status_id];
			return $t_is_status_in_use;		
		}
		if ($p_status_id != $t_status_id ) {
			foreach ( $t_entry	as $status_id => $value) {
				if ($p_status_id == $status_id ) {
				    $t_is_status_in_use ['return'] = true;
					$t_is_status_in_use ['error_msg'] = ERROR_STATUS_DELETE_USED_IN_WORKFLOW;
					$t_is_status_in_use ['error_parameter'] = $t_current_enum_status[$p_status_id];
					return $t_is_status_in_use;	
				}
			}
		}
		
		if ( $p_status_id == $t_project_workflow['default'][$t_status_id] ) {
			$t_is_status_in_use ['return'] = true;
			$t_is_status_in_use ['error_msg'] = ERROR_STATUS_DELETE_USED_IN_WORKFLOW;
			$t_is_status_in_use ['error_parameter'] = $t_current_enum_status[$p_status_id];
			return $t_is_status_in_use;		
		}
	}
		
	/* check if status is used in workflow_threshold */
	$t_workflow_thresholds = array('bug_submit_status', 'bug_reopen_status', 'bug_resolved_status_threshold', 'bug_readonly_status_threshold', 'bug_assigned_status' );

	foreach ( $t_workflow_thresholds as $threshold ) {
       $t_status_id = config_get( $threshold );
		if ( $p_status_id == $t_status_id) {			
			$t_is_status_in_use ['return'] = true;
			$t_is_status_in_use ['error_msg'] = ERROR_STATUS_DELETE_USED_IN_THRESHOLD;
			$t_is_status_in_use ['error_parameter'] = $t_current_enum_status[$p_status_id];
			return $t_is_status_in_use;			
		}
	}
	return $t_is_status_in_use;	
}

/**
 * Insert values from one list to another one  between two integer values
 * - This function calculates new keys for values which have to be inserted
 *   according to begin and end markers.
 *  @param array $p_new_status_enum (int key => string value ) : list in which values are inserted
 *  @param array $p_add_status_enum (int key => string value ) : list of values to insert
 *  @param int $p_begin_marker  : begin marker
 *  @param int $p_end_marker : end marker
 */
function step_status_computing ( &$p_new_status_enum, $p_add_status_enum, $p_begin_marker, $p_end_marker){
	
	$t_size = $p_end_marker - $p_begin_marker;
	$t_count = count($p_add_status_enum);
	
	if ( $t_count > 0) {
	
		if ( $p_begin_marker >= $p_end_marker ){
			error_log('status_api - step_status_computing - Begin marker : '. $p_begin_marker.'- End marker : '. $p_end_marker);
			trigger_error( ERROR_STATUS_TECHNICAL, ERROR );
		}
		if ($t_count >= $t_size ) {	
			error_log('status_api - step_status_computing - Nb element : '. $t_count.'- Size : '. $t_size);
			trigger_error( ERROR_STATUS_TECHNICAL, ERROR );
		}
		if ($t_size < 2 ){
		    error_log('status_api - step_status_computing - size < 2 :' . $t_size);
			trigger_error( ERROR_STATUS_TECHNICAL, ERROR );
		}
		
		/* step calculation */
		$t_step = floor ($t_size / ($t_count+1));
		
		if ($t_step < 1) {
		   error_log('status_api - step_status_computing - step < 1 :' . $t_step);
		   trigger_error( ERROR_STATUS_TECHNICAL, ERROR );
		}
	
		$t_key = $p_begin_marker;
		foreach ( $p_add_status_enum as $t_status_id => $t_status_label ){
		   $t_key = $t_key + $t_step ;
		   $p_new_status_enum[$t_key] = $t_status_label;
		}
		ksort($p_new_status_enum);
	}
}

/**
 * Return an array with the differents actions (create - update - delete) possible with the given status
 * @param string $p_status
 */
function get_actions_with_status(&$p_label_status) {
    $t_default_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get_global( 'default_status_enum_string' ));
			
	$ret = array('update');
	if ( !array_search($p_label_status, $t_default_enum_status) ) {
		array_push($ret, 'delete');
	}
	return $ret;
}


#EOF
?>
