<?php


/*
 * Created on 20 avr. 2012
 *
 */

/**
 * Configure mantis in management way.
 * Create one project and three attached subproject and theirs configuration files (Management - Action - Risk - Decision)
 * Status and workflow is manageable.
 * A decision can be linked to and action which can be linked to a risk
 *
 * @param string $p_username  The name of the user trying to access the versions.
 * @param string $p_password  The password of the user.
 * @return boolean  indicating if configuration is ok or not
 */
function mc_configure_management($p_username, $p_password, $p_project_name) {
	$cpt = 0;
	$ret = false;
	$t_user_id = mci_check_login($p_username, $p_password);

	#log failed
	if ($t_user_id === false)
		return mci_soap_fault_login_failed();

	#not admin
	if (!mci_has_administrator_access($t_user_id))
		return mci_soap_fault_access_denied($t_user_id);

	#project creation
	$project_ids = projects_creation($p_project_name);
	foreach ($project_ids as $id) {
		if (null == $id || 0 == $id)
			$cpt++;
	}

	#customs fields creation
	if (!custom_fields_creation())
		$cpt++;

	#customs fields association
	if (!custom_fields_association($project_ids))
		$cpt++;

	#categories creation
	if (!categories_creation($project_ids))
		$cpt++;

	#conf file creation
	if (!conf_instance_files_creation($project_ids))
		$cpt++;

	if ($cpt == 0)
		$ret = true;
	return $ret;
}

function mc_management_update($p_username, $p_password, $p_project_id, stdClass  $p_project) {

	$ret = false;
	$t_user_id = mci_check_login($p_username, $p_password);

	#log failed
	if ($t_user_id === false)
		return mci_soap_fault_login_failed();

	#not admin
	if (!mci_has_administrator_access($t_user_id))
		return mci_soap_fault_access_denied($t_user_id);

	#project not exists
	if (!project_exists($p_project_id))
		return  SoapObjectsFactory::newSoapFault('Client',"Project '$p_project_id' does not exist.");
		
	$p_project = SoapObjectsFactory::unwrapObject( $p_project );
	if (!isset ($p_project['name'])) {
		return  SoapObjectsFactory::newSoapFault('Client', 'Missing Field', 'Required Field Missing');
	} else {
		$t_name = $p_project['name'];
	}
	#check that the project_id is the parent project
	if( project_get_type_management($p_project_id) != 'management' )
		return SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' is not the parent project.");
	
	$t_description = $p_project['description'];
	$p_project = (Object) $p_project ;

	$cpt = 0;
	$project_ids = get_project_ids_by_parent_project($p_project_id);
	foreach ($project_ids as $type => $project_id) {
	    $p_project = SoapObjectsFactory::unwrapObject( $p_project );
		switch ($type) {
			case 'management' :
				$p_project['name'] = $t_name;
				$p_project['description'] = $t_name;
				break;
			case 'action' :
				$p_project['name'] = $t_name . '_' . $type;
				$p_project['description'] = "Ce projet est le sous-projet action.";
				break;
			case 'risk' :
				$p_project['name'] = $t_name . '_' . $type;
				$p_project['description'] = "Ce projet est le sous-projet risque.";
				break;
			case 'decision' :
				$p_project['name'] = $t_name . '_' . $type;
				$p_project['description'] = "Ce projet est le sous-projet décision.";
				break;
			case 'evolution' :
				$p_project['name'] = $t_name . '_' . $type;
				$p_project['description'] = "Ce projet est le sous-projet évolution.";
				break;
			default :
				break;
		}
		$p_project = (object) $p_project ;
		if (!mc_project_update($p_username, $p_password, $project_id, $p_project))
			$cpt++;
	}
	if (0 == $cpt)
		$ret = true;
	return $ret;

}

function mc_management_delete($p_username, $p_password, $p_project_id) {
	$t_user_id = mci_check_login($p_username, $p_password);

	#log failed
	if ($t_user_id === false)
		return mci_soap_fault_login_failed();

	#not admin
	if (!mci_has_administrator_access($t_user_id))
		return mci_soap_fault_access_denied($t_user_id);

	#project not exists
	if (!project_exists($p_project_id))
		return  SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' does not exist.");
		
	#check that the project_id is the parent project
	if( project_get_type_management($p_project_id) != 'management' )
		return SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' is not the parent project.");

	$parent_project_name = project_get_name($p_project_id);
	$action_project_id = project_get_other_type_project_id('action', $p_project_id);
	$risk_project_id = project_get_other_type_project_id('risk', $p_project_id);
	$decision_project_id = project_get_other_type_project_id('decision', $p_project_id);
	$evolution_project_id = project_get_other_type_project_id('evolution', $p_project_id);

	$project_ids = get_project_ids_by_parent_project($p_project_id);
	foreach ($project_ids as $type => $project_id) {
		project_delete($project_id);
	}

	#check
	$cpt = 0;
	if (project_exists($p_project_id) || project_exists($action_project_id) || project_exists($risk_project_id) || project_exists($decision_project_id) || project_exists($evolution_project_id))
		$cpt++;
	if (0 != $cpt)
		return false;
	return true;

}

/**
 * Link a user to a project with a specified access level.
 *
 * @param string $p_username  The name of the user trying to add the user (must be administrator)
 * @param string $p_password  The password of the user.
 * @param integer $p_project_id  The id of the project to link user to.
 * @param integer $p_user_id  The id of the user to link to the project.
 * @param integer $p_access access level.
 * @return bool returns true or false depending on the success of the action
 */
function mc_management_add_user($p_username, $p_password, $p_project_id, $p_user_id, $p_access) {
	$t_user_id = mci_check_login($p_username, $p_password);
	if ($t_user_id === false) {
		return SoapObjectsFactory::newSoapFault('Client','Access Denied');
	}
	if (!mci_has_administrator_access($t_user_id)) {
		return  SoapObjectsFactory::newSoapFault('Client', 'Access Denied. User does not have administrator access');
	}

	if (!project_exists($p_project_id)) {
		return  SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' does not exist.");
	}
	if (!user_exists($p_user_id)) {
		return  SoapObjectsFactory::newSoapFault('Client', "User '$p_user_id' does not exist.");
	}
	
	#check that the project_id is the parent project
	if( project_get_type_management($p_project_id) != 'management' )
		return SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' is not the parent project.");

	// add the user to the project
	$project_ids = get_project_ids_by_parent_project($p_project_id);
	foreach ($project_ids as $type => $project_id) {
		project_set_user_access($project_id, $p_user_id, $p_access);
	}

	return true;
}

/**
 * Remove a user from management projects.
 *
 * @param string $p_username  The name of the user trying to add the user (must be administrator)
 * @param string $p_password  The password of the user.
 * @param integer $p_project_id  The id of the parent project to unlink user to.
 * @param integer $p_user_id  The id of the user to unlink to the management projects.
 * @return bool returns true or false depending on the success of the action
 */
function mc_management_remove_user($p_username, $p_password, $p_project_id, $p_user_id) {
	$t_user_id = mci_check_login($p_username, $p_password);
	if ($t_user_id === false) {
		return  SoapObjectsFactory::newSoapFault('Client', 'Access Denied');
	}
	if (!mci_has_administrator_access($t_user_id)) {
		return  SoapObjectsFactory::newSoapFault('Client', 'Access Denied. User does not have administrator access');
	}
	if (!project_exists($p_project_id)) {
		return  SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' does not exist.");
	}
	if (!user_exists($p_user_id)) {
		return  SoapObjectsFactory::newSoapFault('Client', "User '$p_user_id' does not exist.");
	}
	
	#check that the project_id is the parent project
	if( project_get_type_management($p_project_id) != 'management' )
		return SoapObjectsFactory::newSoapFault('Client', "Project '$p_project_id' is not the parent project.");

	// remove the user to the management projects
	$project_ids = get_project_ids_by_parent_project($p_project_id);
	foreach ($project_ids as $type => $project_id) {
		project_remove_user($project_id, $p_user_id);
	}

	return true;
}

/**
 * 
 * @param string $p_project_name  The name of the project.
 * @return Array  of issues
 */
function mc_get_all_issues_by_project($p_username, $p_password, $p_project_name) {
	$project_id = project_get_id_by_name($p_project_name);
	$page_number = 1;
	$per_page = -1;
	return mc_project_get_issues($p_username, $p_password, $project_id, $page_number, $per_page);
}

/**
 * Creation of the 3 subproject Action Risk & Decision and their parent project Management
 * Add hierarchy
 * Creation of their configuration files
 */
function projects_creation($p_project_name) {
	global $g_management_projects_status, $g_management_projects_view_state, $g_action_name, $g_action_description, $g_risk_name, $g_risk_description, $g_decision_name, $g_decision_description, $g_evolution_name, $g_evolution_description;

	//management project creation
	$t_projects_status = mci_get_project_status_id($g_management_projects_status);
	$t_projects_view_state = mci_get_project_view_state_id($g_management_projects_view_state);
	$parent_project_description = explode('(', $p_project_name);
	$parent_project_description = str_replace(' ', '', $parent_project_description[0]);
	$t_parent_project_id = project_create($p_project_name, $parent_project_description, $t_projects_status, $t_projects_view_state);

	//ARD projects creation
	$t_action_id = project_create($p_project_name . '_' . $g_action_name, $g_action_description, $t_projects_status, $t_projects_view_state);
	$t_risk_id = project_create($p_project_name . '_' . $g_risk_name, $g_risk_description, $t_projects_status, $t_projects_view_state);
	$t_decision_id = project_create($p_project_name . '_' . $g_decision_name, $g_decision_description, $t_projects_status, $t_projects_view_state);
	$t_evolution_id = project_create($p_project_name . '_' . $g_evolution_name, $g_evolution_description, $t_projects_status, $t_projects_view_state);

	//conf file creation for each subproject
	instance_configuration_create($t_action_id);
	instance_configuration_create($t_risk_id);
	instance_configuration_create($t_decision_id);
	instance_configuration_create($t_evolution_id);

	// link to parent
	project_hierarchy_add($t_action_id, $t_parent_project_id);
	project_hierarchy_add($t_risk_id, $t_parent_project_id);
	project_hierarchy_add($t_decision_id, $t_parent_project_id);
	project_hierarchy_add($t_evolution_id, $t_parent_project_id);

	return array (
		"management" => $t_parent_project_id,
		"action" => $t_action_id,
		"risk" => $t_risk_id,
		"decision" => $t_decision_id,
		"evolution" => $t_evolution_id
	);

}
/**
 * Return an array with all ids attached to the given parent project's name
 */
function get_project_ids_by_parent_project($p_project_id) {
	$action_project_id = project_get_other_type_project_id('action', $p_project_id);
	$risk_project_id = project_get_other_type_project_id('risk', $p_project_id);
	$decision_project_id = project_get_other_type_project_id('decision', $p_project_id);
	$evolution_project_id = project_get_other_type_project_id('evolution', $p_project_id);
	
	return array (
		'management' => $p_project_id,
		'action' => $action_project_id,
		'risk' => $risk_project_id,
		'decision' => $decision_project_id,
		'evolution' => $evolution_project_id
	);
}

/**
 * Create all customs fields
 */
function custom_fields_creation() {
	global $g_risk_control, $g_responsables;
	//context
	if (!custom_field_get_id_from_name('context')) {
		$t_custom_field_id = custom_field_create('context');
		$t_values = values_custom_field_creation('context', 10, null, null, null, 10, 10, 0, 250, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//impact
	if (!custom_field_get_id_from_name('impact')) {
		$t_custom_field_id = custom_field_create('impact');
		$t_values = values_custom_field_creation('impact', 10, null, null, null, 10, 10, 0, 250, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//reference
	if (!custom_field_get_id_from_name('reference')) {
		$t_custom_field_id = custom_field_create('reference');
		$t_values = values_custom_field_creation('reference', 0, null, null, null, 10, 10, 0, 250, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//control
	if (!custom_field_get_id_from_name('control')) {
		$t_possible_values = '';
		foreach ($g_risk_control as $t_risk_control) {
			$t_possible_values .= $t_risk_control . '|';
		}
		$t_possible_values = substr($t_possible_values, 0, strlen($t_possible_values) - 1);
		$t_custom_field_id = custom_field_create('control');
		$t_values = values_custom_field_creation('control', 3, $t_possible_values, null, null, 10, 10, null, null, true, true, true, true, true, true, true, true, true);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//responsable
	if (!custom_field_get_id_from_name('responsable')) {
		$t_possible_values = 'none|';
		foreach ($g_responsables as $g_responsable) {
			$t_possible_values .= $g_responsable . '|';
		}
		$t_possible_values = substr($t_possible_values, 0, strlen($t_possible_values) - 1);
		$t_custom_field_id = custom_field_create('responsable');
		$t_values = values_custom_field_creation('responsable', 3, $t_possible_values, null, null, 10, 10, null, null, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//estimate_end
	if (!custom_field_get_id_from_name('estimate_end')) {
		$t_custom_field_id = custom_field_create('estimate_end');
		$t_values = values_custom_field_creation('estimate_end', 8, null, null, null, 10, 10, null, null, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//reestimate_end
	if (!custom_field_get_id_from_name('reestimate_end')) {
		$t_custom_field_id = custom_field_create('reestimate_end');
		$t_values = values_custom_field_creation('reestimate_end', 8, null, null, null, 10, 10, null, null, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//real_end
	if (!custom_field_get_id_from_name('real_end')) {
		$t_custom_field_id = custom_field_create('real_end');
		$t_values = values_custom_field_creation('real_end', 8, null, null, null, 10, 10, null, null, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//charge
	if (!custom_field_get_id_from_name('charge')) {
		$t_custom_field_id = custom_field_create('charge');
		$t_values = values_custom_field_creation('charge', 0, null, null, null, 10, 10, 0, 250, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//delivery
	if (!custom_field_get_id_from_name('delivery')) {
		$t_custom_field_id = custom_field_create('delivery');
		$t_values = values_custom_field_creation('delivery', 8, null, null, null, 10, 10, null, null, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//mep
	if (!custom_field_get_id_from_name('mep')) {
		$t_custom_field_id = custom_field_create('mep');
		$t_values = values_custom_field_creation('mep', 8, null, null, null, 10, 10, null, null, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//uc
	if (!custom_field_get_id_from_name('uc')) {
		$t_custom_field_id = custom_field_create('uc');
		$t_values = values_custom_field_creation('uc', 0, null, null, null, 10, 10, 0, 250, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	//it
	if (!custom_field_get_id_from_name('it')) {
		$t_custom_field_id = custom_field_create('it');
		$t_values = values_custom_field_creation('it', 0, null, null, null, 10, 10, 0, 250, true, true, true, true, true, false, false, false, false);
		custom_field_update($t_custom_field_id, $t_values);
	}
	return true;
}

/**
 * Associate given value in the return table, format to create a custom field in db
 * @param string $p_name  The name of the custom field.
 * @param string $p_type  The type of the custom field (0 text,3 list,8 date, 10 textarea)
 * @param string $p_possible_value  The possible values of the custom field.
 * @param string $p_default_value  The default value of the custom field.
 * @param string $p_valid_regex  The regex of the custom field.
 * @param int $p_access_level_r  The reading access level of the custom field.
 * @param int $p_access_level_rw  The reading and writing access level of the custom field.
 * @param int $p_length_min  The minimum length of the custom field.
 * @param int $p_length_max  The maximum length of the custom field.
 * @param boolean $p_filter_by  True if custom field can be filter.
 * @param boolean $p_display_report  True if custom field is displayed in report page.
 * @param boolean $p_display_update  True if custom field is displayed in update page.
 * @param boolean $p_display_resolved  True if custom field is displayed in resolved page.
 * @param boolean $p_display_closed  True if custom field is displayed in closed page.
 * @param boolean $p_require_report  True if custom field is required in report page.
 * @param boolean $p_require_update  True if custom field is required in update page.
 * @param boolean $p_require_resolved  True if custom field is required in resolved page.
 * @param boolean $p_require_closed  True if custom field is required in closed page.
 * @return array  $values in correct format to create a custom field
 */
function values_custom_field_creation($p_name, $p_type, $p_possible_value, $p_default_value, $p_valid_regex, $p_access_level_r, $p_access_level_rw, $p_length_min, $p_length_max, $p_filter_by, $p_display_report, $p_display_update, $p_display_resolved, $p_display_closed, $p_require_report, $p_require_update, $p_require_resolved, $p_require_closed) {
	$t_values['name'] = $p_name;
	$t_values['type'] = $p_type;
	$t_values['possible_values'] = $p_possible_value;
	$t_values['default_value'] = $p_default_value;
	$t_values['valid_regexp'] = $p_valid_regex;
	$t_values['access_level_r'] = $p_access_level_r;
	$t_values['access_level_rw'] = $p_access_level_rw;
	$t_values['length_min'] = $p_length_min;
	$t_values['length_max'] = $p_length_max;
	$t_values['filter_by'] = $p_filter_by;
	$t_values['display_report'] = $p_display_report;
	$t_values['display_update'] = $p_display_update;
	$t_values['display_resolved'] = $p_display_resolved;
	$t_values['display_closed'] = $p_display_closed;
	$t_values['require_report'] = $p_require_report;
	$t_values['require_update'] = $p_require_update;
	$t_values['require_resolved'] = $p_require_resolved;
	$t_values['require_closed'] = $p_require_closed;
	return $t_values;
}

/**
 * Associate custom fields for each subproject
 */
function custom_fields_association($project_ids) {
	global $g_action_custom_fields, $g_risk_custom_fields, $g_decision_custom_fields, $g_evolution_custom_fields;

	/*
	 *  ACTION
	 */
	$cpt = 0;
	foreach ($g_action_custom_fields as $custom_field) {
		$t_custom_field_id = custom_field_get_id_from_name($custom_field);
		custom_field_link($t_custom_field_id, $project_ids['action']);
		custom_field_set_sequence($t_custom_field_id, $project_ids['action'], $cpt);
		$cpt++;
	}
	/*
	 *  RISK
	 */
	$cpt = 0;
	foreach ($g_risk_custom_fields as $custom_field) {
		$t_custom_field_id = custom_field_get_id_from_name($custom_field);
		custom_field_link($t_custom_field_id, $project_ids['risk']);
		custom_field_set_sequence($t_custom_field_id, $project_ids['risk'], $cpt);
		$cpt++;
	}
	/*
	 *  DECISION
	 */
	$cpt = 0;
	foreach ($g_decision_custom_fields as $custom_field) {
		$t_custom_field_id = custom_field_get_id_from_name($custom_field);
		custom_field_link($t_custom_field_id, $project_ids['decision']);
		custom_field_set_sequence($t_custom_field_id, $project_ids['decision'], $cpt);
		$cpt++;
	}
	/*
	 * EVOLUTION
	 */
	$cpt = 0;
	foreach ($g_evolution_custom_fields as $custom_field) {
		$t_custom_field_id = custom_field_get_id_from_name($custom_field);
		custom_field_link($t_custom_field_id, $project_ids['evolution']);
		custom_field_set_sequence($t_custom_field_id, $project_ids['evolution'], $cpt);
		$cpt++;
	}
	return true;
}

/**
 * Create categories for each subproject
 */
function categories_creation($project_ids) {
	global $g_risk_criteria_categories, $g_action_categories, $g_risk_categories, $g_decision_categories, $g_evolution_categories;
	/*
	 * RISK CRITERIA
	 */
	foreach ($g_risk_criteria_categories as $g_risk_criteria_category) {
		if (category_is_unique(-1, $g_risk_criteria_category)) {
			category_add(-1, $g_risk_criteria_category);
		}
	}
	/*
	 * ACTION
	 */
	foreach ($g_action_categories as $g_action_category) {
		if (category_is_unique($project_ids['action'], $g_action_category)) {
			category_add($project_ids['action'], $g_action_category);
		}
	}
	/*
	 * RISK
	 */
	foreach ($g_risk_categories as $g_risk_category) {
		if (category_is_unique($project_ids['risk'], $g_risk_category)) {
			category_add($project_ids['risk'], $g_risk_category);
		}
	}
	/*
	 * DECISION
	 */
	foreach ($g_decision_categories as $g_decision_category) {
		if (category_is_unique($project_ids['decision'], $g_decision_category)) {
			category_add($project_ids['decision'], $g_decision_category);
		}
	}
	/*
	 * EVOLUTION
	 */
	foreach ($g_evolution_categories as $g_evolution_category) {
		if (category_is_unique($project_ids['evolution'], $g_evolution_category)) {
			category_add($project_ids['evolution'], $g_evolution_category);
		}
	}
	return true;
}

/**
 * 3 subproject's configuration file
 */
function conf_instance_files_creation($project_ids) {
	$cpt = 0;
	$ret = false;
	if (!action_conf_instance_file_creation($project_ids['action']))
		$cpt++;
	if (!risk_conf_instance_file_creation($project_ids['risk']))
		$cpt++;
	if (!decision_conf_instance_file_creation($project_ids['decision']))
		$cpt++;
	if (!evolution_conf_instance_file_creation($project_ids['evolution']))
		$cpt++;
	if ($cpt == 0)
		$ret = true;
	return $ret;
}

/**
 * Write in action configuration file all needed datas
 */
function action_conf_instance_file_creation(& $p_project_id) {
	global $g_action_report_page_fields, $g_action_view_page_fields, $g_action_print_page_fields, $g_action_update_page_fields, $g_action_change_status_page_fields, $g_action_allow_no_priority, $g_action_allow_no_category, $s_title_french, $s_title_english, $s_note_french, $s_note_english,
	#status
	$g_action_fixed_status, $g_action_fixed_status_prev, $g_action_fixed_status_next, $g_action_fixed_status_next, $g_action_bug_reopen_status, $g_action_bug_feedback_status, $g_action_bug_resolved_status_threshold, $g_action_bug_readonly_status_threshold, $g_action_bug_assigned_status, 
	$g_action_status_fixed, $g_management_language_status, $g_action_status, $g_action_status_colors, $s_action_status_french, $s_action_status_english, $s_action_status_progress_title_french, $s_action_status_progress_title_english, $s_action_status_new_title_french, $s_action_status_new_title_english, 
	$s_action_status_closed_title_french, $s_action_status_closed_title_english, $s_action_status_new_button_french, $s_action_status_new_button_english, $s_action_status_progress_button_french, $s_action_status_progress_button_english, $s_action_status_closed_button_french, $s_action_status_closed_button_english, 
	$s_action_status_new_email_french, $s_action_status_new_email_english, $s_action_status_progress_email_french, $s_action_status_progress_email_english, $s_action_status_closed_email_french, $s_action_status_closed_email_english;

	# get the conf instance file
	$t_project_id = $p_project_id;
	if (file_exists(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php')) {
		$t_file = file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php');
		#current fields
		$t_line = get_line_in_file($t_file, "#fields") + 1;
		$t_fields = "\t".'$g_bug_report_page_fields = array(' . "\n";
		foreach ($g_action_report_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_view_page_fields = array(' . "\n";
		foreach ($g_action_view_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_print_page_fields = array(' . "\n";
		foreach ($g_action_print_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_update_page_fields = array(' . "\n";
		foreach ($g_action_update_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_change_status_page_fields = array(' . "\n";
		foreach ($g_action_change_status_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_fields, $t_line);
		#priority field
		$t_line = get_line_in_file($t_file, "#priority") + 1;
		$t_field = "\t".'$g_allow_no_priority = ' . $g_action_allow_no_priority . ";\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#category field
		$t_line = get_line_in_file($t_file, "#category") + 1;
		$t_field = "\t".'$g_allow_no_category = ' . $g_action_allow_no_category . ";\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title field
		$t_line = get_line_in_file($t_file, "#summary") + 1;
		$t_field = "\t".'$s_summary_french = \'' . $s_title_french . "';\n";
		$t_field .= "\t".'$s_summary_english = \'' . $s_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#note field
		$t_line = get_line_in_file($t_file, "#additional_information") + 1;
		$t_field = "\t".'$s_additional_information_french = \'' . $s_note_french . "';\n";
		$t_field .= "\t".'$s_additional_information_english = \'' . $s_note_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#status
		$t_line = get_line_in_file($t_file, "#status") + 1;
		$t_field = "\t".'$g_default_status_prev = ' . $g_action_fixed_status_prev . ";\n";
		$t_field .= "\t".'$g_default_status_next = ' . $g_action_fixed_status_next . ";\n";
		$t_field .= "\t".'$g_default_status_enum_string = \'' . $g_action_status . "';\n";
		$t_field .= "\t".'$g_bug_reopen_status = ' . $g_action_bug_reopen_status . ";\n"; 
		$t_field .= "\t".'$g_bug_feedback_status = ' . $g_action_bug_feedback_status . ";\n"; 
		$t_field .= "\t".'$g_bug_resolved_status_threshold = ' . $g_action_bug_resolved_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_readonly_status_threshold = ' . $g_action_bug_readonly_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_assigned_status = ' . $g_action_bug_assigned_status . ";\n"; 
		$t_field .= "\t".'$g_status_enum_string = \'' . $g_action_status . "';\n";
		$t_field .= "\t".'$g_language_status = array(';
		foreach ($g_management_language_status as $language) {
			$t_field .= "'$language', ";
		}
		$t_field .= ");\n";
		$t_field .= "\t".'$g_status_colors = array(' . "\n";
		foreach ($g_action_status_colors as $status => $color) {
			$t_field .= "\t\t'$status' => '$color',\n";
		}
		$t_field .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#name status
		$t_line = get_line_in_file($t_file, "#name") + 1;
		$t_field = "\t".'$s_status_enum_string_french = \'' . $s_action_status_french . "';\n";
		$t_field .= "\t".'$s_status_enum_string_english = \'' . $s_action_status_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title status
		$t_line = get_line_in_file($t_file, "#title") + 1;
		$t_field = "\t".'$s_new_bug_title_french = \'' . $s_action_status_new_title_french . "';\n";
		$t_field .= "\t".'$s_new_bug_title_english = \'' . $s_action_status_new_title_english . "';\n";
		$t_field .= "\t".'$s_progress_bug_title_french = \'' . $s_action_status_progress_title_french . "';\n";
		$t_field .= "\t".'$s_progress_bug_title_english = \'' . $s_action_status_progress_title_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_french = \'' . $s_action_status_closed_title_french . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_english = \'' . $s_action_status_closed_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#button status
		$t_line = get_line_in_file($t_file, "#button") + 1;
		$t_field = "\t".'$s_new_bug_button_french = \'' . $s_action_status_new_button_french . "';\n";
		$t_field .= "\t".'$s_new_bug_button_english = \'' . $s_action_status_new_button_english . "';\n";
		$t_field .= "\t".'$s_progress_bug_button_french = \'' . $s_action_status_progress_button_french . "';\n";
		$t_field .= "\t".'$s_progress_bug_button_english = \'' . $s_action_status_progress_button_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_french = \'' . $s_action_status_closed_button_french . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_english = \'' . $s_action_status_closed_button_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#email status
		$t_line = get_line_in_file($t_file, "#email") + 1;
		$t_field = "\t".'$s_email_notification_title_for_status_bug_new_french = \'' . $s_action_status_new_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_new_english = \'' . $s_action_status_new_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_progress_french = \'' . $s_action_status_progress_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_progress_english = \'' . $s_action_status_progress_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_french = \'' . $s_action_status_closed_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_english = \'' . $s_action_status_closed_email_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#workflow
		$t_line = get_line_in_file($t_file, "#workflow") + 1;
		$t_field = "\t".'$g_status_enum_workflow[10]' . " ='20:progress';\n";
		$t_field .= "\t".'$g_status_enum_workflow[20]' . " ='90:closed';\n";
		$t_field .= "\t".'$g_status_enum_workflow[90]' . " ='20:progress';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		# --Update File--
		write_file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php', $t_file);
		return true;
	} else {
		return false;
	}
}

/**
 * Write in risk configuration file all needed datas
 */
function risk_conf_instance_file_creation(& $p_project_id) {
	global $g_risk_report_page_fields, $g_risk_view_page_fields, $g_risk_print_page_fields, $g_risk_update_page_fields, $g_risk_change_status_page_fields, $g_risk_urgencies, $s_risk_urgencies_french, $s_risk_urgencies_english, $g_default_bug_urgency, $g_allow_no_urgency, $s_urgency_french, $s_urgency_english, $g_risk_impact, $s_risk_impact_french, $s_risk_impact_english, $g_default_bug_impact, $g_allow_no_impact, $s_impact_french, $s_impact_english, $g_risk_probability, $s_risk_probability_french, $s_risk_probability_english, $g_default_bug_probability, $g_allow_no_probability, $s_probability_french, $s_probability_english, $s_title_french, $s_title_english, $s_note_french, $s_note_english, $g_default_bug_trigger_condition, $s_trigger_condition_french, $s_trigger_condition_english,
	#status
	$g_risk_fixed_status, $g_risk_fixed_status_prev, $g_risk_fixed_status_next, $g_risk_bug_reopen_status, $g_risk_bug_feedback_status, $g_risk_bug_resolved_status_threshold, $g_risk_bug_readonly_status_threshold, $g_risk_bug_assigned_status, 
	$g_risk_status_fixed, $g_management_language_status, $g_risk_status, $g_risk_status_colors, $s_risk_status_french, $s_risk_status_english, $s_risk_status_new_title_french, $s_risk_status_new_title_english, $s_risk_status_progress_title_french, 
	$s_risk_status_progress_title_english, $s_risk_status_closed_title_french, $s_risk_status_closed_title_english, $s_risk_status_new_button_french, $s_risk_status_new_button_english, $s_risk_status_progress_button_french, $s_risk_status_progress_button_english, 
	$s_risk_status_closed_button_french, $s_risk_status_closed_button_english, $s_risk_status_new_email_french, $s_risk_status_new_email_english, $s_risk_status_progress_email_french, $s_risk_status_progress_email_english, $s_risk_status_closed_email_french, 
	$s_risk_status_closed_email_english;

	# get the conf instance file
	$t_project_id = $p_project_id;

	if (file_exists(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php')) {
		$t_file = file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php');
		#current fields
		$t_line = get_line_in_file($t_file, "#fields") + 1;
		$t_fields = "\t".'$g_bug_report_page_fields = array(' . "\n";
		foreach ($g_risk_report_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_view_page_fields = array(' . "\n";
		foreach ($g_risk_view_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_print_page_fields = array(' . "\n";
		foreach ($g_risk_print_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_update_page_fields = array(' . "\n";
		foreach ($g_risk_update_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_change_status_page_fields = array(' . "\n";
		foreach ($g_risk_change_status_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_fields, $t_line);
		#priority field
		$t_line = get_line_in_file($t_file, "#priority") + 1;
		$t_field = "\t".'$g_priority_enum_string = \'' . $g_risk_urgencies . "';\n";
		$t_field .= "\t".'$g_default_bug_priority = ' . $g_default_bug_urgency . ";\n";
		$t_field .= "\t".'$g_allow_no_priority = ' . $g_allow_no_urgency . ";\n";
		$t_field .= "\t".'$s_priority_enum_string_french = \'' . $s_risk_urgencies_french . "';\n";
		$t_field .= "\t".'$s_priority_enum_string_english = \'' . $s_risk_urgencies_english . "';\n";
		$t_field .= "\t".'$s_priority_french = \'' . $s_urgency_french . "';\n";
		$t_field .= "\t".'$s_priority_english = \'' . $s_urgency_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#severity field
		$t_line = get_line_in_file($t_file, "#severity") + 1;
		$t_field = "\t".'$g_severity_enum_string = \'' . $g_risk_impact . "';\n";
		$t_field .= "\t".'$g_default_bug_severity = ' . $g_default_bug_impact . ";\n";
		$t_field .= "\t".'$g_allow_no_severity = ' . $g_allow_no_impact . ";\n";
		$t_field .= "\t".'$s_severity_enum_string_french = \'' . $s_risk_impact_french . "';\n";
		$t_field .= "\t".'$s_severity_enum_string_english = \'' . $s_risk_impact_english . "';\n";
		$t_field .= "\t".'$s_severity_french = \'' . $s_impact_french . "';\n";
		$t_field .= "\t".'$s_severity_english = \'' . $s_impact_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#probability field
		$t_line = get_line_in_file($t_file, "#reproducibility") + 1;
		$t_field = "\t".'$g_reproducibility_enum_string = \'' . $g_risk_probability . "';\n";
		$t_field .= "\t".'$g_default_bug_reproducibility = ' . $g_default_bug_probability . ";\n";
		$t_field .= "\t".'$g_allow_no_reproducibility = ' . $g_allow_no_probability . ";\n";
		$t_field .= "\t".'$s_reproducibility_enum_string_french = \'' . $s_risk_probability_french . "';\n";
		$t_field .= "\t".'$s_reproducibility_enum_string_english = \'' . $s_risk_probability_english . "';\n";
		$t_field .= "\t".'$s_reproducibility_french = \'' . $s_probability_french . "';\n";
		$t_field .= "\t".'$s_reproducibility_english = \'' . $s_probability_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title field
		$t_line = get_line_in_file($t_file, "#summary") + 1;
		$t_field = "\t".'$s_summary_french = \'' . $s_title_french . "';\n";
		$t_field .= "\t".'$s_summary_english = \'' . $s_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#trigger_conition field
		$t_line = get_line_in_file($t_file, "#steps_to_reproduce") + 1;
		$t_field = "\t".'$g_default_bug_steps_to_reproduce = \'' . $g_default_bug_trigger_condition . "';\n";
		$t_field .= "\t".'$s_steps_to_reproduce_french = \'' . $s_trigger_condition_french . "';\n";
		$t_field .= "\t".'$s_steps_to_reproduce_english = \'' . $s_trigger_condition_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#note field
		$t_line = get_line_in_file($t_file, "#additional_information") + 1;
		$t_field = "\t".'$s_additional_information_french = \'' . $s_note_french . "';\n";
		$t_field .= "\t".'$s_additional_information_english = \'' . $s_note_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#status field
		$t_line = get_line_in_file($t_file, "#status") + 1;
		$t_field = "\t".'$g_default_status_prev = ' . $g_risk_fixed_status_prev . ";\n";
		$t_field .= "\t".'$g_default_status_next = ' . $g_risk_fixed_status_next . ";\n";
		$t_field .= "\t".'$g_default_status_enum_string = \'' . $g_risk_status . "';\n";
		$t_field .= "\t".'$g_bug_reopen_status = ' . $g_risk_bug_reopen_status . ";\n"; 
		$t_field .= "\t".'$g_bug_feedback_status = ' . $g_risk_bug_feedback_status . ";\n"; 
		$t_field .= "\t".'$g_bug_resolved_status_threshold = ' . $g_risk_bug_resolved_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_readonly_status_threshold = ' . $g_risk_bug_readonly_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_assigned_status = ' . $g_risk_bug_assigned_status . ";\n"; 
		$t_field .= "\t".'$g_status_enum_string = \'' . $g_risk_status . "';\n";			
		$t_field .= "\t".'$g_language_status = array(';
		foreach ($g_management_language_status as $language) {
			$t_field .= "'$language', ";
		}
		$t_field .= ");\n";
		$t_field .= "\t".'$g_status_colors = array(' . "\n";
		foreach ($g_risk_status_colors as $status => $color) {
			$t_field .= "\t'$status' => '$color',\n";
		}
		$t_field .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#name status
		$t_line = get_line_in_file($t_file, "#name") + 1;
		$t_field = "\t".'$s_status_enum_string_french = \'' . $s_risk_status_french . "';\n";
		$t_field .= "\t".'$s_status_enum_string_english = \'' . $s_risk_status_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title status
		$t_line = get_line_in_file($t_file, "#title") + 1;
		$t_field = "\t".'$s_new_bug_title_french = \'' . $s_risk_status_new_title_french . "';\n";
		$t_field .= "\t".'$s_new_bug_title_english = \'' . $s_risk_status_new_title_english . "';\n";
		$t_field .= "\t".'$s_progress_bug_title_french = \'' . $s_risk_status_progress_title_french . "';\n";
		$t_field .= "\t".'$s_progress_bug_title_english = \'' . $s_risk_status_progress_title_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_french = \'' . $s_risk_status_closed_title_french . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_english = \'' . $s_risk_status_closed_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#button status
		$t_line = get_line_in_file($t_file, "#button") + 1;
		$t_field = "\t".'$s_new_bug_button_french = \'' . $s_risk_status_new_button_french . "';\n";
		$t_field .= "\t".'$s_new_bug_button_english = \'' . $s_risk_status_new_button_english . "';\n";
		$t_field .= "\t".'$s_progress_bug_button_french = \'' . $s_risk_status_progress_button_french . "';\n";
		$t_field .= "\t".'$s_progress_bug_button_english = \'' . $s_risk_status_progress_button_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_french = \'' . $s_risk_status_closed_button_french . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_english = \'' . $s_risk_status_closed_button_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#email status
		$t_line = get_line_in_file($t_file, "#email") + 1;
		$t_field = "\t".'$s_email_notification_title_for_status_bug_new_french = \'' . $s_risk_status_new_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_new_english = \'' . $s_risk_status_new_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_progress_french = \'' . $s_risk_status_progress_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_progress_english = \'' . $s_risk_status_progress_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_french = \'' . $s_risk_status_closed_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_english = \'' . $s_risk_status_closed_email_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#workflow
		$t_line = get_line_in_file($t_file, "#workflow") + 1;
		$t_field = "\t".'$g_status_enum_workflow[10]' . " ='20:progress';\n";
		$t_field .= "\t".'$g_status_enum_workflow[20]' . " ='90:closed';\n";
		$t_field .= "\t".'$g_status_enum_workflow[90]' . " ='20:progress';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		# --Update File--
		write_file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php', $t_file);
		return true;
	} else {
		return false;
	}
}

/**
 * Write in decision configuration file all needed datas
 */
function decision_conf_instance_file_creation(& $p_project_id) {
	global $project_ids, $g_decision_description, $g_decision_custom_fields, $g_decision_report_page_fields, $g_decision_view_page_fields, $g_decision_print_page_fields, $g_decision_update_page_fields, $g_decision_change_status_page_fields, $g_decision_allow_no_priority, $g_decision_allow_no_category,
	#general
	$s_title_french, $s_title_english, $s_note_french, $s_note_english, $g_management_language_status,
	#status
	$g_decision_fixed_status, $g_decision_fixed_status_prev, $g_decision_fixed_status_next, $g_decision_bug_reopen_status, $g_decision_bug_feedback_status, $g_decision_bug_resolved_status_threshold, $g_decision_bug_readonly_status_threshold, $g_decision_bug_assigned_status, 
	$g_decision_status_fixed, $g_decision_status, $g_decision_status_colors, $s_decision_status_french, $s_decision_status_english, $s_decision_status_new_title_french, $s_decision_status_new_title_english, $s_decision_status_progress_title_french, $s_decision_status_progress_title_english, 
	$s_decision_status_tovalidate_title_french, $s_decision_status_tovalidate_title_english, $s_decision_status_validate_title_french, $s_decision_status_validate_title_english, $s_decision_status_invalidate_title_french, $s_decision_status_invalidate_title_english, $s_decision_status_cancel_title_french, 
	$s_decision_status_closed_title_french, $s_decision_status_cancel_title_english, $s_decision_status_closed_title_english, $s_decision_status_tovalidate_button_french, $s_decision_status_tovalidate_button_english, $s_decision_status_validate_button_french, $s_decision_status_validate_button_english, 
	$s_decision_status_invalidate_button_french, $s_decision_status_invalidate_button_english, $s_decision_status_new_button_french, $s_decision_status_new_button_english, $s_decision_status_progress_button_french, $s_decision_status_progress_button_english, $s_decision_status_cancel_button_french, 
	$s_decision_status_cancel_button_english, $s_decision_status_closed_button_french, $s_decision_status_closed_button_english, $s_decision_status_new_email_french, $s_decision_status_new_email_english, $s_decision_status_progress_email_french, $s_decision_status_progress_email_english, $s_decision_status_tovalidate_email_french, 
	$s_decision_status_tovalidate_email_english, $s_decision_status_validate_email_french, $s_decision_status_validate_email_english, $s_decision_status_invalidate_email_french, $s_decision_status_invalidate_email_english, $s_decision_status_cancel_email_french, $s_decision_status_cancel_email_english, $s_decision_status_closed_email_french, $s_decision_status_closed_email_english;

	# get the conf instance file
	$t_project_id = $p_project_id;
	if (file_exists(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php')) {
		$t_file = file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php');
		#current fields
		$t_line = get_line_in_file($t_file, "#fields") + 1;
		$t_fields = "\t".'$g_bug_report_page_fields = array(' . "\n";
		foreach ($g_decision_report_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_view_page_fields = array(' . "\n";
		foreach ($g_decision_view_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .=  "\t".");\n";
		$t_fields .= "\t".'$g_bug_print_page_fields = array(' . "\n";
		foreach ($g_decision_print_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .=  "\t".");\n";
		$t_fields .= "\t".'$g_bug_update_page_fields = array(' . "\n";
		foreach ($g_decision_update_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_change_status_page_fields = array(' . "\n";
		foreach ($g_decision_change_status_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_fields, $t_line);
		#priority field
		$t_line = get_line_in_file($t_file, "#priority") + 1;
		$t_field = "\t".'$g_allow_no_priority = ' . $g_decision_allow_no_priority . ";\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#category field
		$t_line = get_line_in_file($t_file, "#category") + 1;
		$t_field = "\t".'$g_allow_no_category = ' . $g_decision_allow_no_category . ";\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title field
		$t_line = get_line_in_file($t_file, "#summary") + 1;
		$t_field = "\t".'$s_summary_french = \'' . $s_title_french . "';\n";
		$t_field .= "\t".'$s_summary_english = \'' . $s_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#note field
		$t_line = get_line_in_file($t_file, "#additional_information") + 1;
		$t_field = "\t".'$s_additional_information_french = \'' . $s_note_french . "';\n";
		$t_field .= "\t".'$s_additional_information_english = \'' . $s_note_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#status
		$t_line = get_line_in_file($t_file, "#status") + 1;
		$t_field = "\t".'$g_default_status_prev = ' . $g_decision_fixed_status_prev . ";\n";
		$t_field .= "\t".'$g_default_status_next = ' . $g_decision_fixed_status_next . ";\n";
		$t_field .= "\t".'$g_default_status_enum_string = \'' . $g_decision_status . "';\n";	
		$t_field .= "\t".'$g_bug_reopen_status = ' . $g_decision_bug_reopen_status . ";\n"; 
		$t_field .= "\t".'$g_bug_feedback_status = ' . $g_decision_bug_feedback_status . ";\n"; 
		$t_field .= "\t".'$g_bug_resolved_status_threshold = ' . $g_decision_bug_resolved_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_readonly_status_threshold = ' . $g_decision_bug_readonly_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_assigned_status = ' . $g_decision_bug_assigned_status . ";\n"; 
		$t_field .= "\t".'$g_status_enum_string = \'' . $g_decision_status . "';\n";
		$t_field .= "\t".'$g_language_status = array(';
		foreach ($g_management_language_status as $language) {
			$t_field .= "'$language', ";
		}
		$t_field .= ");\n";
		$t_field .= "\t".'$g_status_colors = array(' . "\n";
		foreach ($g_decision_status_colors as $status => $color) {
			$t_field .= "\t\t'$status' => '$color',\n";
		}
		$t_field .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#name status
		$t_line = get_line_in_file($t_file, "#name") + 1;
		$t_field = "\t".'$s_status_enum_string_french = \'' . $s_decision_status_french . "';\n";
		$t_field .= "\t".'$s_status_enum_string_english = \'' . $s_decision_status_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title status
		$t_line = get_line_in_file($t_file, "#title") + 1;
		$t_field = "\t".'$s_new_bug_title_french = \'' . $s_decision_status_new_title_french . "';\n";
		$t_field .= "\t".'$s_new_bug_title_english = \'' . $s_decision_status_new_title_english . "';\n";
		$t_field .= "\t".'$s_progress_bug_title_french = \'' . $s_decision_status_progress_title_french . "';\n";
		$t_field .= "\t".'$s_progress_bug_title_english = \'' . $s_decision_status_progress_title_english . "';\n";
		$t_field .= "\t".'$s_tovalidate_bug_title_french = \'' . $s_decision_status_tovalidate_title_french . "';\n";
		$t_field .= "\t".'$s_tovalidate_bug_title_english = \'' . $s_decision_status_validate_title_english . "';\n";
		$t_field .= "\t".'$s_validate_bug_title_french = \'' . $s_decision_status_validate_title_french . "';\n";
		$t_field .= "\t".'$s_validate_bug_title_english = \'' . $s_decision_status_tovalidate_title_english . "';\n";
		$t_field .= "\t".'$s_invalidate_bug_title_french = \'' . $s_decision_status_invalidate_title_french . "';\n";
		$t_field .= "\t".'$s_invalidate_bug_title_english = \'' . $s_decision_status_invalidate_title_english . "';\n";
		$t_field .= "\t".'$s_cancel_bug_title_french = \'' . $s_decision_status_cancel_title_french . "';\n";
		$t_field .= "\t".'$s_cancel_bug_title_english = \'' . $s_decision_status_cancel_title_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_french = \'' . $s_decision_status_closed_title_french . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_english = \'' . $s_decision_status_closed_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#button status
		$t_line = get_line_in_file($t_file, "#button") + 1;
		$t_field = "\t".'$s_new_bug_button_french = \'' . $s_decision_status_new_button_french . "';\n";
		$t_field .= "\t".'$s_new_bug_button_english = \'' . $s_decision_status_new_button_english . "';\n";
		$t_field .= "\t".'$s_progress_bug_button_french = \'' . $s_decision_status_progress_button_french . "';\n";
		$t_field .= "\t".'$s_progress_bug_button_english = \'' . $s_decision_status_progress_button_english . "';\n";
		$t_field .= "\t".'$s_tovalidate_bug_button_french = \'' . $s_decision_status_tovalidate_button_french . "';\n";
		$t_field .= "\t".'$s_tovalidate_bug_button_english = \'' . $s_decision_status_validate_button_english . "';\n";
		$t_field .= "\t".'$s_validate_bug_button_french = \'' . $s_decision_status_validate_button_french . "';\n";
		$t_field .= "\t".'$s_validate_bug_button_english = \'' . $s_decision_status_tovalidate_button_english . "';\n";
		$t_field .= "\t".'$s_invalidate_bug_button_french = \'' . $s_decision_status_invalidate_button_french . "';\n";
		$t_field .= "\t".'$s_invalidate_bug_button_english = \'' . $s_decision_status_invalidate_button_english . "';\n";
		$t_field .= "\t".'$s_cancel_bug_button_french = \'' . $s_decision_status_cancel_button_french . "';\n";
		$t_field .= "\t".'$s_cancel_bug_button_english = \'' . $s_decision_status_cancel_button_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_french = \'' . $s_decision_status_closed_button_french . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_english = \'' . $s_decision_status_closed_button_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#email status
		$t_line = get_line_in_file($t_file, "#email") + 1;
		$t_field = "\t".'$s_email_notification_title_for_status_bug_new_french = \'' . $s_decision_status_new_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_new_english = \'' . $s_decision_status_new_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_progress_french = \'' . $s_decision_status_progress_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_progress_english = \'' . $s_decision_status_progress_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_tovalidate_french = \'' . $s_decision_status_tovalidate_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_tovalidate_english = \'' . $s_decision_status_validate_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_validate_french = \'' . $s_decision_status_validate_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_validate_english = \'' . $s_decision_status_tovalidate_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_invalidate_french = \'' . $s_decision_status_invalidate_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_invalidate_english = \'' . $s_decision_status_invalidate_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_cancel_french = \'' . $s_decision_status_cancel_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_cancel_english = \'' . $s_decision_status_cancel_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_french = \'' . $s_decision_status_closed_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_english = \'' . $s_decision_status_closed_email_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#workflow
		$t_line = get_line_in_file($t_file, "#workflow") + 1;
		$t_field = "\t".'$g_status_enum_workflow[10]' . " ='20:progress,30:to_validate,80:cancel';\n";
		$t_field .= "\t".'$g_status_enum_workflow[20]' . " ='30:to_validate,80:cancel';\n";
		$t_field .= "\t".'$g_status_enum_workflow[30]' . " ='40:validate,50:invalidate,80:cancel';\n";
		$t_field .= "\t".'$g_status_enum_workflow[40]' . " ='20:progress,90:closed';\n";
		$t_field .= "\t".'$g_status_enum_workflow[50]' . " ='20:progress,90:closed';\n";
		$t_field .= "\t".'$g_status_enum_workflow[80]' . " ='20:progress,90:closed';\n";
		$t_field .= "\t".'$g_status_enum_workflow[90]' . " ='20:progress,30:to_validate';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		# --Update File--
		write_file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php', $t_file);
		return true;
	} else {
		return false;
	}
}

/**
 * Write in evolution configuration file all needed datas
 */
function evolution_conf_instance_file_creation(& $p_project_id) {
	global $g_management_language_status, $g_evolution_report_page_fields, $g_evolution_view_page_fields, $g_evolution_print_page_fields, $g_evolution_update_page_fields, $g_evolution_change_status_page_fields, $g_evolution_custom_fields, $g_evolution_allow_no_priority, $g_evolution_allow_no_category,
	#status
	$g_evolution_fixed_status, $g_evolution_fixed_status_prev, $g_evolution_fixed_status_next, $g_evolution_bug_reopen_status, $g_evolution_bug_feedback_status, $g_evolution_bug_resolved_status_threshold, $g_evolution_bug_readonly_status_threshold, $g_evolution_bug_assigned_status, 
	$g_evolution_status_fixed, $g_evolution_status, $g_evolution_status_colors, $s_evolution_status_french, $s_evolution_status_english, $s_evolution_status_new_title_french, $s_evolution_status_new_title_english, $s_evolution_status_waiting_encryption_title_french, $s_evolution_status_waiting_encryption_title_english, 
	$s_evolution_status_waiting_moa_title_french, $s_evolution_status_waiting_moa_title_english, $s_evolution_status_accepted_title_french, $s_evolution_status_accepted_title_english, $s_evolution_status_cancel_title_french, $s_evolution_status_cancel_title_english, $s_evolution_status_new_button_french, $s_evolution_status_new_button_english, 
	$s_evolution_status_waiting_encryption_button_french, $s_evolution_status_waiting_encryption_button_english, $s_evolution_status_waiting_moa_button_french, $s_evolution_status_waiting_moa_button_english, $s_evolution_status_accepted_button_french, $s_evolution_status_accepted_button_english, $s_evolution_status_cancel_button_french, 
	$s_evolution_status_cancel_button_english, $s_evolution_status_new_email_french, $s_evolution_status_new_email_english, $s_evolution_status_waiting_encryption_email_french, $s_evolution_status_waiting_encryption_email_english, $s_evolution_status_waiting_moa_email_french, $s_evolution_status_waiting_moa_email_english, $s_evolution_status_accepted_email_french, 
	$s_evolution_status_accepted_email_english, $s_evolution_status_cancel_email_french, $s_evolution_status_cancel_email_english, $s_evolution_status_closed_title_french, $s_evolution_status_closed_title_english, $s_evolution_status_closed_button_french, $s_evolution_status_closed_button_english, $s_evolution_status_closed_email_french, $s_evolution_status_closed_email_english;

	# get the conf instance file
	$t_project_id = $p_project_id;
	if (file_exists(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php')) {
		$t_file = file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php');
		#current fields
		$t_line = get_line_in_file($t_file, "#fields") + 1;
		$t_fields = "\t".'$g_bug_report_page_fields = array(' . "\n";
		foreach ($g_evolution_report_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_view_page_fields = array(' . "\n";
		foreach ($g_evolution_view_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_print_page_fields = array(' . "\n";
		foreach ($g_evolution_print_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_update_page_fields = array(' . "\n";
		foreach ($g_evolution_update_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_fields .= "\t".'$g_bug_change_status_page_fields = array(' . "\n";
		foreach ($g_evolution_change_status_page_fields as $t_field) {
			$t_fields .= "\t\t '" . $t_field . "', \n";
		}
		$t_fields .= "\t".");\n";
		$t_file = insert_in_file($t_file, $t_fields, $t_line);
		#priority field
		$t_line = get_line_in_file($t_file, "#priority") + 1;
		$t_field = "\t".'$g_allow_no_priority = ' . $g_evolution_allow_no_priority . ";\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#category field
		$t_line = get_line_in_file($t_file, "#category") + 1;
		$t_field = "\t".'$g_allow_no_category = ' . $g_evolution_allow_no_category . ";\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#status
		$t_line = get_line_in_file($t_file, "#status") + 1;
		$t_field = "\t".'$g_default_status_prev = ' . $g_evolution_fixed_status_prev . ";\n";
		$t_field .= "\t".'$g_default_status_next = ' . $g_evolution_fixed_status_next . ";\n";
		$t_field .= "\t".'$g_default_status_enum_string = \'' . $g_evolution_status . "';\n";
		$t_field .= "\t".'$g_bug_reopen_status = ' . $g_evolution_bug_reopen_status . ";\n"; 
		$t_field .= "\t".'$g_bug_feedback_status = ' . $g_evolution_bug_feedback_status . ";\n"; 
		$t_field .= "\t".'$g_bug_resolved_status_threshold = ' . $g_evolution_bug_resolved_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_readonly_status_threshold = ' . $g_evolution_bug_readonly_status_threshold . ";\n"; 
		$t_field .= "\t".'$g_bug_assigned_status = ' . $g_evolution_bug_assigned_status . ";\n"; 
		$t_field .= "\t".'$g_status_enum_string = \'' . $g_evolution_status . "';\n";
		$t_field .= "\t".'$g_language_status = array(';
		foreach ($g_management_language_status as $language) {
			$t_field .= "'$language', ";
		}
		$t_field .= ");\n";
		$t_field .= "\t".'$g_status_colors = array(' . "\n";
		foreach ($g_evolution_status_colors as $status => $color) {
			$t_field .= "\t\t'$status' => '$color',\n";
		}
		$t_field .= "\t);\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#name status
		$t_line = get_line_in_file($t_file, "#name") + 1;
		$t_field = "\t".'$s_status_enum_string_french = \'' . $s_evolution_status_french . "';\n";
		$t_field .= "\t".'$s_status_enum_string_english = \'' . $s_evolution_status_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#title status
		$t_line = get_line_in_file($t_file, "#title") + 1;
		$t_field = "\t".'$s_new_bug_title_french = \'' . $s_evolution_status_new_title_french . "';\n";
		$t_field .= "\t".'$s_new_bug_title_english = \'' . $s_evolution_status_new_title_english . "';\n";
		$t_field .= "\t".'$s_waiting_encryption_bug_title_french = \'' . $s_evolution_status_waiting_encryption_title_french . "';\n";
		$t_field .= "\t".'$s_waiting_encryption_bug_title_english = \'' . $s_evolution_status_waiting_encryption_title_english . "';\n";
		$t_field .= "\t".'$s_waiting_moa_bug_title_french = \'' . $s_evolution_status_waiting_moa_title_french . "';\n";
		$t_field .= "\t".'$s_waiting_moa_bug_title_english = \'' . $s_evolution_status_waiting_moa_title_english . "';\n";
		$t_field .= "\t".'$s_accepted_bug_title_french = \'' . $s_evolution_status_accepted_title_french . "';\n";
		$t_field .= "\t".'$s_accepted_bug_title_english = \'' . $s_evolution_status_accepted_title_english . "';\n";
		$t_field .= "\t".'$s_cancel_bug_title_french = \'' . $s_evolution_status_cancel_title_french . "';\n";
		$t_field .= "\t".'$s_cancel_bug_title_english = \'' . $s_evolution_status_cancel_title_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_title_french = \'' . $s_evolution_status_closed_title_french . "';\n";
        $t_field .= "\t".'$s_closed_bug_title_english = \'' . $s_evolution_status_closed_title_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#button status
		$t_line = get_line_in_file($t_file, "#button") + 1;
		$t_field = "\t".'$s_new_bug_button_french = \'' . $s_evolution_status_new_button_french . "';\n";
		$t_field .= "\t".'$s_new_bug_button_english = \'' . $s_evolution_status_new_button_english . "';\n";
		$t_field .= "\t".'$s_waiting_encryption_bug_button_french = \'' . $s_evolution_status_waiting_encryption_button_french . "';\n";
		$t_field .= "\t".'$s_waiting_encryption_bug_button_english = \'' . $s_evolution_status_waiting_encryption_button_english . "';\n";
		$t_field .= "\t".'$s_waiting_moa_bug_button_french = \'' . $s_evolution_status_waiting_moa_button_french . "';\n";
		$t_field .= "\t".'$s_waiting_moa_bug_button_english = \'' . $s_evolution_status_waiting_moa_button_english . "';\n";
		$t_field .= "\t".'$s_accepted_bug_button_french = \'' . $s_evolution_status_accepted_button_french . "';\n";
		$t_field .= "\t".'$s_accepted_bug_button_english = \'' . $s_evolution_status_accepted_button_english . "';\n";
		$t_field .= "\t".'$s_cancel_bug_button_french = \'' . $s_evolution_status_cancel_button_french . "';\n";
		$t_field .= "\t".'$s_cancel_bug_button_english = \'' . $s_evolution_status_cancel_button_english . "';\n";
		$t_field .= "\t".'$s_closed_bug_button_french = \'' . $s_evolution_status_closed_button_french . "';\n";
        $t_field .= "\t".'$s_closed_bug_button_english = \'' . $s_evolution_status_closed_button_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		#email status
		$t_line = get_line_in_file($t_file, "#email") + 1;
		$t_field = "\t".'$s_email_notification_title_for_status_bug_new_french = \'' . $s_evolution_status_new_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_new_english = \'' . $s_evolution_status_new_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_waiting_encryption_french = \'' . $s_evolution_status_waiting_encryption_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_waiting_encryption_english = \'' . $s_evolution_status_waiting_encryption_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_waiting_moa_french = \'' . $s_evolution_status_waiting_moa_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_waiting_moa_english = \'' . $s_evolution_status_waiting_moa_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_accepted_french = \'' . $s_evolution_status_accepted_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_accepted_english = \'' . $s_evolution_status_accepted_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_cancel_french = \'' . $s_evolution_status_cancel_email_french . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_cancel_english = \'' . $s_evolution_status_cancel_email_english . "';\n";
		$t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_french = \'' . $s_evolution_status_closed_email_french . "';\n";
        $t_field .= "\t".'$s_email_notification_title_for_status_bug_closed_english = \'' . $s_evolution_status_closed_email_english . "';\n";
		$t_file = insert_in_file($t_file, $t_field, $t_line);
		# --Update File--
		write_file(dirname(dirname( dirname( __FILE__ ) ) ) . DIRECTORY_SEPARATOR . 'conf-instance' . DIRECTORY_SEPARATOR . 'config_status_' . $t_project_id . '.php', $t_file);
		return true;
	} else {
		return false;
	}
}
?>
