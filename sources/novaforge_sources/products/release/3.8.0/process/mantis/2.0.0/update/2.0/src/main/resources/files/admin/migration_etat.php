<?php
/**
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
 
# This page migrates status
# Load the MantisDB core in maintenance mode. This mode will assume that
# config/config_inc.php hasn't been specified. Thus the database will not be opened
# and plugins will not be loaded.

require_once( dirname( dirname( __FILE__ ) ) . '/core.php' );
require_api( 'status_api.php' );

set_time_limit(0);

define ('CONF_INSTANCE_FILE_PATH', dirname( dirname( __FILE__ ) ).DIRECTORY_SEPARATOR.'conf-instance'.DIRECTORY_SEPARATOR);


$error_languages = array("auto", "urdu", "korean", "japanese", "chinese_traditional", "chinese_simplified", "arabicegyptianspoken", "arabic", "hebrew","asturian");
session_set( 'error_language', $error_languages);

class StatusData {

	public $status_label;
	public $status_label_next;
	public $status_position;
}


#####################################
#   	FUNCTIONS 					#
#####################################

/**
 * Recalculate status_enum_string 
 *
 * Reorder current_status_enum_string according to default Mantis status order
 * 		=>  '10:new,20:feedback,30:acknowledged,40:confirmed,50:assigned,80:resolved,90:closed'
 * set the first label status of  current_status_enum_string to the 'new' status.
 * set the last label status of  current_status_enum_string to the 'closed' status.
 * if customized status are setted after the closed status they are reordered between assigned (50) and resolved (80) status.
 * and then Compute new keys for non default status
 * 
 * Write the new status_enum_string to the conf-instance file
 * Rewrite status_enum_string_languages to the conf-instance file
 *
 * @param string  $p_instance : project_id number
 * @param string  $p_file_name : conf-instance filename 
 * @param multi-array $p_status_multi_language : status_enum_string languagues (cf load_status_languages())
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param boolean p_update : true if p_current_enum_status is different from p_new_enum_status
 * @param array $p_result : log result
 */
function migration_update_classification($p_instance, $p_file_name, $p_status_multi_language, &$p_current_enum_status, &$p_new_enum_status, &$p_update, &$p_result) {

	$t_status_languages = array_diff(config_get( 'language_choices_arr' ), session_get('error_language'));
	$p_update = false;
	$p_result = array();
	$t_arr_to_check = array();
	$t_arr_sorted = array();
    $t_previous_ident= '';
	$t_next_ident='';
    # get the conf instance file
	//$instance_file = file("conf-instance/config_status_$instance.php");
	$t_instance_file = file (CONF_INSTANCE_FILE_PATH.$p_file_name);
	
	/* g_status_enum_string */
	$line = get_line_in_file($t_instance_file, "g_status_enum_string");
	$t_current_enum_status = MantisEnum::getAssocArrayIndexedByValues( substr(trim($t_instance_file[$line]),25,-2) );
	$t_default_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get_global( 'default_status_enum_string' ) );
	
	$t_default_status_prev_ident = config_get_global('default_status_prev');
	$t_default_status_prev_label = $t_default_enum_status [$t_default_status_prev_ident];

	$t_new_enum_status = array(); 	/* array for final status_enum_string */
	$t_tmp_enum_status = array(); 	/* array for status key computing */
	$t_computing_status = false;  	/* marker for computing the key status */
	$t_previous_ident = 0; 			/* Begin marker for status key computing  */
	$t_next_ident = 0; 				/* End marker for status key computing  */
	$t_update_config = 0 ; 			/* marker to check the config file has to be updated (if one or more computing has been done) */
	$t_check_computing = true;      /* marker to check that no errors occurs during computing */

	$p_result['default_status_enum_string'] = config_get_global( 'default_status_enum_string' );
	$p_result['current_status_enum_string'] = substr(trim($t_instance_file[$line]),25,-2);
		
    /* check that the current_status_enum_string matches with the order default status mantis  */
	$t_arr_to_check = array_values($t_current_enum_status);
    if (!is_well_sorted ($t_default_enum_status,$t_arr_to_check)){	
		$t_working_enum_status = migration_sort_array($t_default_enum_status, $t_current_enum_status, $t_default_status_prev_label, $p_result);
	} else {
	    /* the first element has to be the 'new' status */
		set_value_to_first_position ($t_arr_to_check, 'new');
		/* the last element has to be the 'closed' status */
		/* the values after closed has to be set after $t_default_status_prev_label*/
		set_values_after_status_prev ($t_arr_to_check, $t_default_enum_status,$t_default_status_prev_label);
		
		$t_working_enum_status = $t_arr_to_check;
	}
	
	$p_result['working_status_enum_string'] = array_to_string($t_working_enum_status);
					
	# --Update status_enum_string--
	# rewrite the $s_status_enum_string_line variable
	foreach ( $t_working_enum_status as $t_current_ident => $t_current_label ) {
	
		$t_previous_computing_status = $t_computing_status;
				
		/*  Default Mantis status can't be key computing */
		if (in_array($t_current_label, $t_default_enum_status)){
		    $t_current_ident = MantisEnum::getValue( config_get_global( 'default_status_enum_string' ), $t_current_label);
		    $t_new_enum_status [$t_current_ident]= $t_current_label;
			$t_computing_status = false;
		}
		else
		{
			   $t_computing_status = true;
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
			   //echo 'status_api - update_classification - Begin marker : '.$t_previous_ident.'<br>';
			   $p_result['error'] = error_string(ERROR_STATUS_TECHNICAL);
			   return;	
			} 
			if ($t_next_ident != null) {
				if (!array_key_exists($t_next_ident, $t_new_enum_status)) {
					//echo 'status_api - update_classification - End marker : '.$t_next_ident.'<br>';
					$p_result['error'] = error_string(ERROR_STATUS_TECHNICAL);
					return;
				}
				else {
					/* status keys computing */						
					$t_compute = migration_step_status_computing ($t_new_enum_status, $t_tmp_enum_status, $t_previous_ident, $t_next_ident, $p_result);
					
					if ($t_compute == false){
					    $t_check_computing = false;
					}
																			
					$t_tmp_enum_status = array();
					$t_step ='';	
				}
			}
		} // end computing
	}	// fin foreach
	
	# Update status config file 
	# Check if config file has to be updated
    $t_update_config = count(array_diff_assoc($t_new_enum_status,$t_current_enum_status));
	
	if ($t_update_config > 0 && $t_check_computing) {
	  $p_update = true;
	}
	
	if ( $p_update ) {
		$p_result['migration update classification'] = 'Mise à jour de la valeur "status_enum_string"';
	} else {
	    $p_result['migration update classification'] = 'Pas de mise à jour de la valeur "status_enum_string"';
	}
	
	$p_current_enum_status = $t_current_enum_status;
	$p_new_enum_status = $t_new_enum_status;
			
    if( $p_update ){
	
	    # --Update status_enum_string--
		#create the new variable
		$new_status_enum_string = "\t".'$g_status_enum_string = \'';
		foreach ( $t_new_enum_status as $t_to_status_id => $t_to_status_label ) {
				$new_status_enum_string .= $t_to_status_id.":$t_to_status_label,";
		}
		$new_status_enum_string = substr(trim($new_status_enum_string),0,-1);
		$new_status_enum_string .= "';\n";
		
		$p_result['new_status_enum_string'] = array_to_string ($t_new_enum_status);

		# get the line to put the $new_status_enum_string
		$status_enum_string_line = get_line_in_file($t_instance_file, "g_status_enum_string");
		$t_instance_file[$status_enum_string_line] = $new_status_enum_string;
		
		#--Update status name--
		# rewrite the $s_status_enum_string_language line variable	
		migration_name_status_language_refactoring($t_instance_file,$p_status_multi_language,$t_status_languages,$t_current_enum_status,$t_new_enum_status);
		
		$line = get_line_in_file($t_instance_file, "s_status_enum_string_english");
		$p_result['$s_status_enum_string_english'] = substr(trim($t_instance_file[$line]),strlen('$s_status_enum_string_english')+4 ,-2);
		$line = get_line_in_file($t_instance_file, "s_status_enum_string_french");
		$p_result['$s_status_enum_string_french'] = substr(trim($t_instance_file[$line]),strlen('$s_status_enum_string_french')+4 ,-2);

		# --Update File--
		write_file(CONF_INSTANCE_FILE_PATH.DIRECTORY_SEPARATOR.$p_file_name, $t_instance_file);	
	}						
}

/**
 * Insert values from one list to another one  between two integer values
 * - This function calculates new keys for values which have to be inserted
 *   according to begin and end markers.
 *  @param array $p_new_status_enum (int key => string value ) : list in which values are inserted
 *  @param array $p_add_status_enum (int key => string value ) : list of values to insert
 *  @param int $p_begin_marker  : begin marker
 *  @param int $p_end_marker : end marker
  * @param array $p_result : log result
 */
function migration_step_status_computing ( &$p_new_status_enum, $p_add_status_enum, $p_begin_marker, $p_end_marker, &$p_result){
	
	$t_size = $p_end_marker - $p_begin_marker;
	$t_count = count($p_add_status_enum);
	$t_compute = true;
	
	if ( $t_count > 0) {
	
		if ( $p_begin_marker >= $p_end_marker ){
			$p_result['[ERROR] status_api - step_status_computing '] = 'Begin marker : '. $p_begin_marker.'- End marker : '. $p_end_marker;
			$t_compute = false;
		} else if ($t_count >= $t_size ) {	
			$p_result['[ERROR] status_api - step_status_computing '] = 'Nb element : '. $t_count.'- Size : '. $t_size;
			$t_compute = false;
		} else if ($t_size < 2 ){
			$p_result['[ERROR]  status_api - step_status_computing '] = 'size < 2 :' . $t_size;
			$t_compute = false;
		} else {
			/* step calculation */
			$t_step = floor ($t_size / ($t_count+1));
			
			if ($t_step < 1) {
			   $p_result['[ERROR]  status_api - step_status_computing '] = 'step < 1 :' . $t_step;
			   $t_compute = false;
			}
		}
		
		if ($t_compute ) {
			$t_key = $p_begin_marker;
			foreach ( $p_add_status_enum as $t_status_id => $t_status_label ){
			   $t_key = $t_key + $t_step ;
			   $p_new_status_enum[$t_key] = $t_status_label;
			}
			ksort($p_new_status_enum);
		}
	} 
	
	return $t_compute;
}


/**
 * Update database according to current_status_enum_string and new_status_enum_string 
 * 			mantis_bug_table
 *			mantis_bug_history_table
 * 			mantis_config_table (status_enum_workflow)
 * 			mantis_config_table (bug_submit_status, bug_reopen_status, bug_resolved_status_threshold, bug_readonly_status_threshold, bug_assigned_status)
 *
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param boolean p_update : true if p_current_enum_status is different from p_new_enum_status
 * @param array $p_result : log result
 */

function migration_update_database($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_update, &$p_result ){
   $p_result = array();
   if ($p_update ) {
     $p_result['migration update database '] = 'Mise à jour base de données requise';
	 
	 update_mantis_bug_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, $p_result);
	 update_mantis_bug_history_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, $p_result);
	 update_status_enum_workflow ($p_project_id, $p_current_enum_status, $p_new_enum_status, $p_result);
	 update_status_threshold ($p_project_id, $p_current_enum_status, $p_new_enum_status, $p_result);
	 update_mantis_config_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, $p_result);
	 update_mantis_filters_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, $p_result);
   } else {
     $p_result['migration update database '] = 'Pas de mise à jour base de la base de données ';
   }
}

/**
 * Update mantis_bug_table (database) according to current_status_enum_string and new_status_enum_string 
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param array $p_result : log result
 */
function update_mantis_bug_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_result){
      
		db_param_push();
		$t_query = 'SELECT status,id FROM {bug} WHERE project_id=' . db_param();
		$t_result = db_query( $t_query, array( $p_project_id) );
		$t_count = 0;
				
		while( $t_bug = db_fetch_array( $t_result ) ) {
		    
			$c_status = (int) $t_bug ['status'];
			$c_id = (int) $t_bug ['id'];
			
            /* recherche label sur current */
			if ( isset ($p_current_enum_status [$c_status])){
			    $t_count = $t_count +1;
				$t_label = $p_current_enum_status [$c_status];
				$t_new_ident = array_search ( $t_label, $p_new_enum_status);
				
				db_param_push();
				$t_query = 'UPDATE {bug}
						   SET status=' . db_param() . '
						   WHERE id=' . db_param() ;				
				db_query( $t_query, array( $t_new_ident, $c_id) );
			}
		}

		$p_result['migration update_mantis_bug_table '] = 'Project_id : '. $p_project_id .' - Nombre mise à jour : '.$t_count;		
}

/**
 * Update mantis_bug_history_table (database) according to current_status_enum_string and new_status_enum_string 
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param array $p_result : log result
 */
function update_mantis_bug_history_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_result){

		db_param_push();
		$t_query = "SELECT old_value, new_value, h.id from {bug_history} h
					JOIN {bug} b on b.id = h.bug_id
					WHERE b.project_id = ". db_param() ."
					and h.field_name = 'status'";
	
		$t_result = db_query( $t_query, array( $p_project_id) );
		$t_count = 0;
		
		while( $t_bug_history = db_fetch_array( $t_result ) ) {
		    
			$c_id = (int) $t_bug_history ['id'];
			
			$c_old_status = (int) $t_bug_history ['old_value'];
			if ( isset($p_current_enum_status [$c_old_status])){
				$t_old_new_ident = array_search ( $p_current_enum_status [$c_old_status], $p_new_enum_status);
			}
			$c_new_status = (int) $t_bug_history ['new_value'];
			if ( isset ($p_current_enum_status [$c_new_status])){
				$t_new_new_ident = array_search ( $p_current_enum_status [$c_new_status], $p_new_enum_status);
			}
			if ( isset ($p_current_enum_status [$c_new_status]) && isset($p_current_enum_status [$c_old_status])){
			    $t_count = $t_count +1;
				db_param_push();
				$t_query = 'UPDATE {bug_history}
						   SET old_value = ' . db_param() . ',
						   new_value = ' . db_param() . '
						   WHERE id=' . db_param() ;				
				db_query( $t_query, array( $t_old_new_ident,$t_new_new_ident, $c_id) );
			}
		}
		
		$p_result['migration update_mantis_bug_history_table '] = 'Project_id : '. $p_project_id .' - Nombre mise à jour : '.$t_count;	
				
}
/**
 * Update mantis config_table(database) according to current_status_enum_string and new_status_enum_string 
 * 		=> status_enum_workflow
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param array $p_result : log result
 */
 
function update_status_enum_workflow ($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_result){

		db_param_push();
		$t_query = "SELECT value from {config} h
					WHERE h.project_id = ". db_param() ."
					and h.config_id = 'status_enum_workflow'";
	
		$t_result = db_query( $t_query, array( $p_project_id) );
		$t_count = 0;
		
		while( $t_status_workflow = db_fetch_array( $t_result ) ) {
		 		  
			  $t_workflow = json_decode( $t_status_workflow ['value'], true );
			  $t_new_workflow = array();   		   
			   foreach ( $t_workflow as $t_status => $t_workflow_row) {
					$t_new_status = array_search( $p_current_enum_status[$t_status], $p_new_enum_status);
					
					if ($t_new_status > 0) {
						$t_next_arr = MantisEnum::getAssocArrayIndexedByValues($t_workflow_row);
						$t_next_workflow = array();
							foreach ($t_next_arr as $t_next => $t_next_label) {				
								$t_new_next = array_search( $p_current_enum_status[$t_next], $p_new_enum_status);
								$t_new_next_label = $p_new_enum_status [$t_new_next];
								if ( $t_new_next > 0) {
									$t_next_workflow[$t_new_next] =  $t_new_next_label;
								}
							}
							//ksort($t_next_workflow);
							$t_temp_workflow = array();
							foreach ( $t_next_workflow as $t_next => $t_next_label){
								$t_temp_workflow[] = $t_next . ':' . $t_next_label;
							}
							$t_new_workflow[$t_new_status] = implode( ',', $t_temp_workflow);
					}
			   }
		   
				ksort($t_new_workflow);
		   
	   
			   if (count($t_new_workflow) > 0) {
			   
					$t_count = $t_count + 1;
					   
					db_param_push();
					$t_query = "UPDATE {config} h
								SET value = ". db_param() ."
								WHERE  h.project_id = ". db_param() ."
								and h.config_id = 'status_enum_workflow'";
					
			
					db_query( $t_query, array( json_encode( $t_new_workflow ), $p_project_id) );
			   }			
				$p_result['		migration update_status_enum_workflow - Old workflow'] = 'Project_id : '. $p_project_id .' - '. array_to_string($t_workflow);	
			    $p_result['		migration update_status_enum_workflow - New workflow'] = 'Project_id : '. $p_project_id .' - '. array_to_string($t_new_workflow);		   

		   
		}
		$p_result['migration update_status_enum_workflow '] = 'Project_id : '. $p_project_id .' - Nombre mise à jour : '.$t_count;
}

/**
 * Update mantis config_table(database) according to current_status_enum_string and new_status_enum_string 
 * 		=> set_statuus_threshold
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param array $p_result : log result
 */
 
function update_status_threshold ($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_result){

		db_param_push();
		$t_query = "SELECT value from {config} h
					WHERE h.project_id = ". db_param() ."
					and h.config_id = 'set_status_threshold'";
	
		$t_result = db_query( $t_query, array( $p_project_id) );
		$t_count = 0;
		
		while( $t_status_threshold = db_fetch_array( $t_result ) ) {
		 		  
		  $t_threshold = json_decode( $t_status_threshold ['value'], true );
		  $t_new_threshold = array();   		   
		   foreach ( $t_threshold as $t_status_key => $t_role_value) {
				if (isset($p_current_enum_status[$t_status_key])){
					$t_new_status_key = array_search( $p_current_enum_status[$t_status_key], $p_new_enum_status);
					
					$t_new_threshold[$t_new_status_key] = $t_role_value;
				}	
		   }		   
			ksort($t_new_threshold);
		    
			if (count($t_new_threshold) > 0) {
				   
				$t_count = $t_count + 1;
						   
					db_param_push();
					$t_query = "UPDATE {config} h
								SET value = ". db_param() ."
								WHERE  h.project_id = ". db_param() ."
								and h.config_id = 'set_status_threshold'";
					
			
					db_query( $t_query, array( json_encode( $t_new_threshold ), $p_project_id) );
			}			
			$p_result['		migration update_status_threshold - Old threshold'] = 'Project_id : '. $p_project_id .' - '. array_to_string($t_threshold);	
			$p_result['		migration update_status_threshold - New threshold'] = 'Project_id : '. $p_project_id .' - '. array_to_string($t_new_threshold);		   

		   
		}
		$p_result['migration update_status_threshold '] = 'Project_id : '. $p_project_id .' - Nombre mise à jour : '.$t_count;
}

/**
 * Update mantis config_table (database) according to current_status_enum_string and new_status_enum_string 
 *  => bug_submit_status, bug_reopen_status, bug_resolved_status_threshold, bug_readonly_status_threshold, bug_assigned_status
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param array $p_result : log result
 */
function update_mantis_config_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_result){

	$t_workflow_thresholds = array('bug_submit_status', 'bug_reopen_status', 'bug_resolved_status_threshold', 'bug_readonly_status_threshold', 'bug_assigned_status' );
		
	foreach ( $t_workflow_thresholds as $t_threshold) {
	
		db_param_push();
		$t_query = "SELECT value from {config} h
					WHERE h.project_id = ". db_param() ."
					and h.config_id = ". db_param() ;
	
		$t_result = db_query( $t_query, array( $p_project_id, $t_threshold) );
		$t_count = 0;
		while( $t_threshold_row = db_fetch_array( $t_result ) ) {
		      
		    $t_threshold_value = $t_threshold_row['value'];
		    $t_new_status = array_search( $p_current_enum_status[$t_threshold_value], $p_new_enum_status);
					
			if ($t_new_status){
			
			   $t_count = $t_count +1;
			
				db_param_push();
				$t_query = "UPDATE {config} h
							SET value = ". db_param() ."
							WHERE  h.project_id = ". db_param() ."
							and h.config_id = ". db_param() ;
					
			
				db_query( $t_query, array( $t_new_status, $p_project_id, $t_threshold) );
				
				$p_result['migration update_mantis_config_table - '. $t_threshold] = 'Project_id : '. $p_project_id .' - Old value : '.$t_threshold_value . ' - New value :'. $t_new_status;
						
			}
		}
		$p_result['migration update_mantis_config_table - '. $t_threshold] = 'Project_id : '. $p_project_id .' - Nombre mise à jour : '.$t_count;		
	}
}
/**
 * Update mantis filters_table (database) according to current_status_enum_string and new_status_enum_string 
 *  => ststus field
 * @param string $p_project_id : project_id number
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string
 * @param array $p_result : log result
 */
 
function update_mantis_filters_table ($p_project_id, $p_current_enum_status, $p_new_enum_status, &$p_result){

		db_param_push();
		$t_query = "SELECT id, filter_string from {filters} f
					WHERE f.project_id = ". db_param();
	
		$t_result = db_query( $t_query, array( $p_project_id));
		$t_count = 0;
		while( $t_filters_row = db_fetch_array( $t_result ) ) {
					
			$t_setting_arr = explode( '#', $t_filters_row['filter_string'], 2 );

			$t_filter_arr = json_decode( $t_setting_arr[1], /* assoc array */ true );
			if (isset($t_filter_arr['status']) && $t_filter_arr['status'][0] <> 0 ){	    
			    $t_old_filter_arr = $t_filter_arr['status'];
			    $t_new_filter_arr = array(); 
				$i=-1;
				foreach ( $t_old_filter_arr as $t_status) {
				        $i=$i+1;
						$t_new_status = array_search( $p_current_enum_status[$t_status], $p_new_enum_status);
						$t_new_filter_arr[$i]= $t_new_status;		
				}
				sort($t_new_filter_arr);
				
				$t_update_filters = count(array_diff($t_old_filter_arr,$t_new_filter_arr));
				 
				if (  $t_update_filters > 0) {
				 
					$t_filter_arr['status'] = $t_new_filter_arr;
										
					$t_filter_serialized = json_encode( $t_filter_arr );
					$t_filter_string = 'v9' . '#' . $t_filter_serialized;
					
					db_param_push();
					$t_update_query = 'UPDATE {filters} SET filter_string=' . db_param() . ' WHERE id=' . db_param();
					$t_update_result = db_query( $t_update_query, array( $t_filter_string, $t_filters_row['id'] ) );
					
					$t_count = $t_count +1;
					
					$p_result['migration update_mantis_filters_table - filter_id '. $t_filters_row['id'] ] = 'Project_id : '. $p_project_id .' - Old value : '. array_to_string ($t_old_filter_arr) . ' New value :' . array_to_string ($t_new_filter_arr) ;
				}
			}
	  
		}
		
		$p_result['migration update_mantis_filters_table - '] = 'Project_id : '. $p_project_id .' - Nombre mise à jour : '.$t_count;

}
/**
 * Print Test result
 * @param array $p_result  : message - value
 * @return void
 */
function print_test( $p_result) {
    foreach ($p_result as $p_result_key => $p_result_label) {
		echo '<tr>';
		echo '	<td class="width-25">' . $p_result_key . '</td>';
		echo '	<td>' . $p_result_label . '</td>';
		echo '</tr>' . "\n";
	}
}

/**
 * Print key array to a string
 * @param array $p_array : key array
 * @return string
 */
function array_to_string ($p_array){ 
    $t_string='';
	foreach ( $p_array as $t_id => $t_label ) {
			$t_string .= $t_id .":$t_label,";
	}
	return $t_string;
}

/**
 * set the $p_value to the first position of the $p_array key array
 * @param array $p_array : key array
 * @param string $p_value : the value to set to first position
 * @return void
 */
function set_value_to_first_position (&$p_array, $p_value){

	reset($p_array);
	$t_first_value = current($p_array);
	$t_ident_value = array_search($p_value, $p_array);

	if ($t_ident_value && $t_first_value != $p_value ) {
		unset($p_array[$t_ident_value]);
		array_unshift ($p_array,$p_value);
	}
}

/**
 * set the $p_value to the last position of the $p_array key array
 * @param array $p_array : key array
 * @param string $p_value : the value to set to last position
 * @return void
 */
function set_value_to_last_position (&$p_array, $p_value){

	$t_last_value = end($p_array);
	$t_ident_value = array_search($p_value, $p_array);

	if ($t_ident_value && $t_last_value != $p_value ) {
	  unset($p_array[$t_ident_value]);
	  $p_array[count($p_array)+1] = $p_value;
	}

}

/**
 * set values setted after 'closed' value, after  $p_default_status_prev_label in $p_array
 * @param array $p_array : key array
 * @param array $p_default_enum_status : default status enum values
 * @param string  $p_default_status_prev_label : status label after which after 'closed' values must be set 
 * the values after closed has to be set after $t_default_status_prev_label
 * @return void
 */
function set_values_after_status_prev (&$p_array, $p_default_enum_status,$p_default_status_prev_label ){

	/* retrieve values after closed */
	$t_after_closed = array();
	$t_ident_value = array_search('closed', $p_array);
	foreach( $p_array as $t_ident => $t_value) {
		if ($t_ident > $t_ident_value && !array_search ($t_value,$p_default_enum_status)  ){
			$t_after_closed[$t_ident] = $t_value;
			unset($p_array[$t_ident]);
		}
	}
	if ( count($t_after_closed) > 0) {
	
		$t_array = array_values($p_array);
	
		/* retrieve values after default_status_prev */
		$t_found = false;
		$j = -1;
		while (!$t_found ) {	 
			 foreach ($t_array as $t_ident => $t_value ){
			   $j++;
				if ( $t_value === $p_default_status_prev_label){
					$t_found = true;
					break;
				}
			}
		}
		$k = -1;
		foreach ($t_array as $t_ident => $t_value ){
		   $k++;
			if ( $k > $j ){
				$t_after_assigned[$t_ident] = $t_value;
			}
		}
		
		$p_array= array();
		$i = -1;
		/* setting value before default_status_prev */
		$t_found = false;
		while (!$t_found ) {
		   foreach ($t_array as $t_ident => $t_value ){
				$i++;
				if ( $t_value !== $p_default_status_prev_label){
					$p_array [$i] = $t_value;   
				} else {
					$p_array [$i] = $t_value;   
					$t_found = true;
					break;
				}
			}
		}
		
		/* setting non default after default_status_prev value after default_status_prev */
		$t_found = false;
		while (!$t_found){
			foreach ($t_after_assigned as $t_ident => $t_value ) {
				if (!array_search ($t_value,$p_default_enum_status)){
					$i++;
					$p_array [$i] = $t_value;  
					unset($t_after_assigned[$t_ident]);
				} else {
				  $t_found = true;
				  break;
				}
			}
		}
		
		/* setting after closed value after default_status_prev */
		foreach ( $t_after_closed as $t_ident => $t_value) {
		  $i++;
		  $p_array [$i] = $t_value;   
		}
			
		/* setting default after default_status_prev value after default_status_prev */ 
		foreach ( $t_after_assigned as $t_ident => $t_value) {
		  $i++;
		  $p_array [$i] = $t_value;   
		}
	} /* end count($t_after_closed) */
}

/**
 * Retrieve status_enum_string default language from lang/strings_language.txt
 * @return array $p_status_language
 */
function load_status_languagues () {

	$t_default_languages = array_diff(config_get( 'language_choices_arr' ), session_get('error_language'));
	
	foreach ($t_default_languages as $t_language ){

	  $t_language_file = 'strings_'.$t_language.'.txt';		
	  $t_language_instance_file = file (dirname( dirname( __FILE__ ) ).DIRECTORY_SEPARATOR.'lang'.DIRECTORY_SEPARATOR.$t_language_file);
	  $t_language_line = get_line_in_file($t_language_instance_file, "status_enum_string");
	  $t_status_enum_string = MantisEnum::getAssocArrayIndexedByValues(substr(trim($t_language_instance_file[$t_language_line]),strlen('$s_status_enum_string')+4 ,-2));
	  
	  $p_status_language [$t_language] = $t_status_enum_string;
	}
	
	return $p_status_language;				
}

/**
 * sort an array ($p_arr_to_sort) according to a default sorted array ($p_arr_sorted)
 * @param array $p_arr_sorted : the default sorted array ('10:new,20:feedback,30:acknowledged,40:confirmed,50:assigned,80:resolved,90:closed')
 * @param array $p_arr_to_sort : the array to be sorted
 * @param string $p_default_status_prev_label : status after which status after 'closed' should be set 
 * @param array $p_result : log result
 * @return array $t_sorted_array : the sorted array 
 */
function migration_sort_array ($p_arr_sorted, &$p_arr_to_sort, $p_default_status_prev_label, &$p_result) {

	/* Add default values in $t_current_enum_status that is not present */
    $t_arr_sorted = array_values($p_arr_sorted);		
	$t_arr_to_sort = array_values(array_unique(array_merge(array_values($p_arr_to_sort), $t_arr_sorted)));

	$p_result['migration_sort_array - BEFORE setting new and closed '] = array_to_string ($t_arr_to_sort);
	/* the first element has to be the 'new' status */
	set_value_to_first_position ($t_arr_to_sort, 'new');
	/* the values after closed has to be set after $p_default_status_prev_label status */
	set_values_after_status_prev ($t_arr_to_sort, $p_arr_sorted, $p_default_status_prev_label);
	/* the last element has to be the 'closed' status */
	set_value_to_last_position ($t_arr_to_sort, 'closed');
	
	$p_result['migration_sort_array - BEFORE ordering '] = array_to_string ($t_arr_to_sort);
		
	/* tableau d'objets */
	$t_status_array = array();
	$i=0;
	
	$t_next_arr_to_sort = $t_arr_to_sort;
	reset($t_next_arr_to_sort);
	
	foreach ($t_arr_to_sort as $t_unused_key => $t_status_values){
	    $t_position = array_search($t_status_values, $t_arr_sorted);
		$t_status_data = new StatusData;
		$t_status_data->status_label= $t_status_values;
		$t_status_data->status_label_next = next($t_next_arr_to_sort);
		$t_status_data->status_position = $t_position;
		$t_status_array[$i]=$t_status_data;
		$i++;
	}

	$j=0;
	$k=0;
	/* Valorisation premier élément du tableau */
	$t_sorted_array = array();
	foreach ($t_status_array as $t_key => $t_status_object){
		  if ( $t_status_object -> status_position == $k ) {
			$t_sorted_array[$j] = $t_status_object -> status_label;
			$t_next_label = $t_status_object->status_label_next;
			$k++;
			$j++;
			break;
		  }
	}

	while ($t_next_label ) {
			// recherche valeur suivante 
			foreach ($t_status_array as $t_key => $t_status_object){
			  $t_found = false;
			     
			  if ( $t_status_object -> status_label == $t_next_label ) {
				$t_next_position = $t_status_object -> status_position;
				$t_next_label = $t_status_object->status_label_next;	
				
				if ($t_next_position == '') {
					$t_sorted_array[$j] = $t_status_object -> status_label;
					$j++;
					break;
				} else {
					$t_found = true;
					break;
				}	
			  }
			}
			// recherche valeur suivante tableau 1
			if ($t_found ) {
				$t_default_label = $t_arr_sorted[$k];
				$k++;
				foreach ($t_status_array as $t_key => $t_status_object){
					if ( $t_status_object -> status_label == $t_default_label ) {
						$t_sorted_array[$j] = $t_status_object -> status_label;
						$t_next_label = $t_status_object->status_label_next;
						$j++;
						break;				
					}
				}
				
			}
	} /* end while */
	
	$p_result['migration_sort_array - AFTER ordering'] = array_to_string ($t_sorted_array);
	
	return $t_sorted_array;		
}

/**
 * Recalculate id for all status languages for new_enum_status from current_enum_status
 * if default status present in new_anum_status are not in current_enum_status there are retrieved from $p_status_multi_language
 * @param array $p_instance_file
 * @param multi-array $p_status_multi_language : specific status languages
 * @param array $p_status_languages : all status languages
 * @param array $p_current_enum_status : current status_enum_string
 * @param array $p_new_enum_status : new status_enum_string 
 **/

function migration_name_status_language_refactoring (&$p_instance_file, $p_status_multi_language = null, $p_status_languages, $p_current_enum_status, $p_new_enum_status){
	
    /* default language for status name */
	$t_status_enum_string = $p_status_multi_language['english'];
	
	foreach ( $p_status_languages as $t_language) {
	
	        $t_current_enum_status = array();
			$t_new_enum_status = array();
			$t_add_enum_status = array();
			
			$t_update_config = false; /* marker to update configuration file */
			$t_refactoring = 0;	/* marker to check if refactoring is necessary */
			# get the line to put the $new_status_enum_string and update the line in the array file
			$line = get_line_in_file($p_instance_file, "status_enum_string_".$t_language);
			
			if ( $line != false ) {
			
			
				$t_enum_status_language = MantisEnum::getAssocArrayIndexedByValues(substr(trim($p_instance_file[$line]),strlen('$s_status_enum_string_'.$t_language)+4 ,-2));
							
				/* search values present in $p_new_enum_status but not present in $p_current_enum_status : => id to add */
				foreach ( $p_new_enum_status as $t_key => $t_value ){
				  $t_current_ident = array_search($t_value, $p_current_enum_status);
				  $t_new_ident = array_search($t_value, $p_new_enum_status);
				  if ($t_current_ident) {
					 $t_current_enum_status [$t_current_ident] = $t_value;
					 $t_new_enum_status [$t_new_ident] = $t_value;
				  }
				  else {
					 $t_add_enum_status [$t_new_ident] = $t_value;
				  }
				}
								
				$t_refactoring = count(array_diff_assoc($t_new_enum_status, $t_current_enum_status));
				
				/* check if name status refactoring is necessary */
				if ($t_refactoring > 0 ) {
					$t_new_enum_status_language = name_status_refactoring($t_current_enum_status,$t_new_enum_status,$t_enum_status_language);
					$t_update_config = true;
				} else {
					$t_new_enum_status_language = $t_enum_status_language;
				}
							
				/* ids to add */	
				if ($p_status_multi_language[$t_language]) {
				   $t_status_enum_string = $p_status_multi_language[$t_language];
				}
						
				if (count($t_add_enum_status) > 0 ) {
					 foreach ($t_add_enum_status as $t_id_to_add => $t_value_to_add ){
					 
						if ( !($t_status_enum_string[$t_id_to_add] == null or $t_status_enum_string[$t_id_to_add] == "")) {
							$t_new_enum_status_language[$t_id_to_add] = $t_status_enum_string[$t_id_to_add];
						}
					 }
					 ksort($t_new_enum_status_language);
					 $t_update_config = true;
				}
							
				/* Update status config_file if necessary */
				if ($t_update_config ){
										
					#create the new variable
					$new_status_name = "\t".'$s_status_enum_string_'.$t_language." = '";
					foreach ( $t_new_enum_status_language as $t_status_id => $t_status_label ) {
						$new_status_name .= $t_status_id.':'.addslashes(stripslashes($t_status_label)).',';			
					}
					$new_status_name = substr($new_status_name, 0, -1);
					$new_status_name .= "';\n";
														
					#update the line in the array file
					$p_instance_file[$line] = $new_status_name;	
				}
			} // end ( if line != false )		
		}
}

/**
 * Main Programm
 * call load_status_languagues
 * call migration_update_classification
 * call migration_update_database
 */
function migration_etat(){

    if (is_dir(CONF_INSTANCE_FILE_PATH)){

		$t_files  = array_diff(scandir(CONF_INSTANCE_FILE_PATH), array('..', '.'));
		$t_status_multi_language = load_status_languagues();
			
		foreach ($t_files as $t_key => $t_file) {
		   if (1 == preg_match("/^config_status_[0-9]+(.{1})php$/", $t_file)){
				$explode = explode('_',substr( $t_file, 0, strpos( $t_file, '.php' )));
				$t_project_id = $explode[2];
				?>
				
				
				<div class="col-md-12 col-xs-12">
				<div class="space-10"></div>

				<div class="widget-box widget-color-blue2">
				<div class="widget-header widget-header-small">
				<h4 class="widget-title lighter">
						<?php echo 'File : '. CONF_INSTANCE_FILE_PATH.$t_file ?>
				</h4>
				</div>
				
				<div class="widget-body">
				<div class="widget-main no-padding">
				<div class="table-responsive">
				<table class="table table-bordered table-condensed">

				
				<?php
				migration_update_classification($t_project_id, $t_file, $t_status_multi_language, $t_current_enum_status, $t_new_enum_status, $t_update, $t_result);
				print_test( $t_result);
				migration_update_database($t_project_id, $t_current_enum_status, $t_new_enum_status, $t_update, $t_result);
				print_test( $t_result);
				?>
				</table>
				</div>
				</div>
				</div>
				</div>
				</div>
				</div>
				<?php
			}
		}
	}
}


#####################################
#   	Main Programm				#
#####################################

layout_page_header_begin( 'Administration - Status migration' );

?>
<div class="col-md-12 col-xs-12">
	<div class="space-10"></div>

	<div class="page-header">
	<h1>
<?php
	echo 'Status migration';
?>
	</h1>
	</div>
</div>
<?php
	migration_etat();
?>
<div class="col-md-12 col-xs-12">
	<div class="space-10"></div>

	<div class="well">
	<h1>
<?php
	 echo lang_get( 'operation_successful' ) . '<br />';
?>
	</h1>
	</div>
</div>

<?php
layout_admin_page_end();
?>
