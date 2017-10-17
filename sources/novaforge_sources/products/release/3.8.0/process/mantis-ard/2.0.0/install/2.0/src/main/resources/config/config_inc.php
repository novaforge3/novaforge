<?php
$g_db_type = 'mysql';
$g_database_name = 'mantis_ard';
$g_db_username = '@MANTISUSER@';
$g_db_password = '@PASSMANTIS@';

$g_allow_signup = OFF;
$g_reauthentication = OFF;
$g_enable_email_notification = ON;
$g_create_user_notification	= OFF;

/***********************
 * Mantis CAS Settings *
 ***********************/
$g_login_method	 = CAS_AUTH;
$g_cas_uri 	= '@CAS_URI@';
//$g_cas_debug = '@MANTIS_ARD_HOME@/log/cas';
$g_instance_method = 'getToolProjectId';
$g_cas_server = '@CAS_HOST@';
$g_hostname = '@HOSTMANTIS_ARD@';
$g_cas_port = @CAS_PORT@;

$g_instance_wsdl		= 'http://127.0.0.1:@PORTKARAF@/cxf/mantisARDInstance?wsdl';
$g_cas_validate 		= 'https://@CAS_HOST@@CAS_SERVICE_URI@';

$g_smtp_password = '@PASSSMTP@';
$g_smtp_connection_mode = '@AUTHSMTPMANTIS@';
$g_smtp_port = '@RELAISMTPPORT@';

//about language
$g_language_choices_arr = array (
	'auto',
	'english',
	'french',
	
);

//Need because of php version used by Novaforge
date_default_timezone_set('@DATETIMEZONE@');

/* base url for email notification */ 
$g_path = "@HTTPD_BASE_URL@/@MANTIS_DEFAULT_ALIAS@/@MANTIS_TOOL_ALIAS@/";

#menu link enable
$g_enable_project_documentation = OFF;
$g_wiki_enable = OFF;
$g_roadmap_enable = OFF;
$g_changelog_enable = OFF;
$g_enable_status_creation = OFF;
$g_enable_mantis_management = OFF;

#add textarea custom field
$g_custom_field_type_enum_string = '0:string,1:numeric,2:float,3:enum,4:email,5:checkbox,6:list,7:multiselection list,8:date,9:radio,10:textarea';
#add db
$g_db_table['mantis_risk_criteria_table'] = '%db_table_prefix%_risk_criteria%db_table_suffix%';
$g_db_table['mantis_risk_evaluation_history_table'] = '%db_table_prefix%_risk_evaluation_history%db_table_suffix%';
#column bug
$g_view_issues_page_columns_management = array (
	'selection',
	'edit',
	'category_id',
	'date_submitted',
	'id',
	'summary',
	'custom_estimate_end',
	'custom_reestimate_end',
	'custom_real_end',
	'custom_responsable',
	'status',
	'priority'
);
$g_view_issues_page_columns_action = array (
	'selection',
	'edit',
	'category_id',
	'date_submitted',
	'id',
	'summary',
	'custom_estimate_end',
	'custom_reestimate_end',
	'custom_real_end',
	'custom_responsable',
	'status',
	'priority'
);
$g_view_issues_page_columns_risk = array (
	'selection',
	'edit',
	'category_id',
	'date_submitted',
	'id',
	'summary',
	'custom_estimate_end',
	'custom_reestimate_end',
	'custom_real_end',
	'custom_responsable',
	'custom_reference',
	'status',
	'priority'
);
$g_view_issues_page_columns_decision = array (
	'selection',
	'edit',
	'category_id',
	'date_submitted',
	'id',
	'summary',
	'custom_estimate_end',
	'custom_reestimate_end',
	'custom_real_end',
	'custom_responsable',
	'status',
	'priority'
);
$g_view_issues_page_columns_evolution = array (
	'selection',
	'edit',
	'category_id',
	'date_submitted',
	'id',
	'summary',
	'status',
	'priority'
);


/****************************************
 * Parent project management attributes *
 ****************************************/
$g_parent_project_description = 'Ce projet est le projet parent de pilotage.';


/****************************
 * Risk criteria attributes *
 ****************************/
$g_risk_criteria_categories = array('duration', 'integrity', 'management_commitment', 'team_commitment', 'effort', 'others');


/********************************
 * Subproject action attributes *
 ********************************/
$g_action_name = 'action';	
$g_action_description = 'Ce projet est le sous-projet action.'; 
#Fields
$g_action_report_page_fields =  array('category_id', 'view_state', 'handler', 'priority', 'summary', 'description', 
	'due_date', 'attachments');
$g_action_view_page_fields =  array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_action_print_page_fields =   array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_action_update_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_action_change_status_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_action_custom_fields =  array('responsable', 'estimate_end', 'reestimate_end' ,'real_end');
#Priority
$g_action_allow_no_priority = "ON";
#Category
$g_action_allow_no_category = "OFF";
$g_action_categories = array('commitment_backlog', 'climb_backlog', 'management_backlog');
#Status
$g_action_fixed_status = "ON";
$g_action_fixed_status_prev = "20";
$g_action_fixed_status_next = "90";
$g_action_bug_reopen_status = "20";
$g_action_bug_feedback_status = "20";
$g_action_bug_resolved_status_threshold = "90";
$g_action_bug_readonly_status_threshold = "90";
$g_action_bug_assigned_status = "20";
$g_action_status = '10:new,20:progress,90:closed';
$g_action_status_fixed = array('new', 'progress', 'closed');
$g_action_status_colors = array( 'new'			=> '#fcbdbd', // red    (scarlet red #ef2929)
		 'progress'		=> '#d2f5b0', // green  (chameleon   #8ae234)
		 'closed'		=> '#c9ccc4'); // grey  (aluminum    #babdb6)
#Status name
$s_action_status_french = '10:nouveau,20:en cours,90:fermé';
$s_action_status_english = '10:new,20:in progress,90:closed'; 
#Status title
$s_action_status_new_title_french = 'Nouvelle action';
$s_action_status_new_title_english = 'New action';
$s_action_status_progress_title_french = 'Action en cours';
$s_action_status_progress_title_english = 'Action in progress';
$s_action_status_closed_title_french = 'Action fermé';
$s_action_status_closed_title_english = 'Action closed';
#Status button
$s_action_status_new_button_french = 'Nouvelle action';
$s_action_status_new_button_english = 'New action';
$s_action_status_progress_button_french = "Démarrer l\'action";
$s_action_status_progress_button_english = 'Start action';
$s_action_status_closed_button_french = "Fermer l\'action";
$s_action_status_closed_button_english = 'Close action';
#Status notification email
#Status email
$s_action_status_new_email_french = "L\'action suivante est maintenant à  l\'état « nouveau » (encore).";
$s_action_status_new_email_english = 'The following action is now in status « new ».';
$s_action_status_progress_email_french = "L\'action suivante est maintenant  à  l\'état « en cours ».";
$s_action_status_progress_email_english = 'The following action is now in status « in progress ».';
$s_action_status_closed_email_french = "L\'action suivante a été fermée.";
$s_action_status_closed_email_english = 'The following action has been closed.';
#Workflow


/******************************
 * Subproject risk attributes *
 ******************************/
$g_risk_name = 'risk';	
$g_risk_description = 'Ce projet est le sous-projet risque.';
#Fields
$g_risk_report_page_fields =  array('category_id', 'view_state', 'handler', 'priority', 'severity', 'reproducibility', 
	'summary', 'description', 'steps_to_reproduce', 'due_date', 'attachments');
$g_risk_view_page_fields =  array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 'reporter', 
	'handler', 'priority', 'severity', 'reproducibility', 'status', 'resolution', 'gravity', 'projection', 'eta', 'summary', 'description', 
	'steps_to_reproduce', 'due_date', 'attachments');
$g_risk_print_page_fields =  array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 'reporter', 
	'handler', 'priority', 'severity', 'reproducibility', 'status', 'resolution', 'projection', 'eta', 'summary', 'description', 
	'steps_to_reproduce', 'due_date', 'attachments');
$g_risk_update_page_fields =  array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 'reporter', 
	'handler', 'priority', 'severity', 'reproducibility', 'status', 'resolution', 'projection', 'eta', 'summary', 'description', 
	'steps_to_reproduce', 'due_date', 'attachments');
$g_risk_change_status_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 'reporter', 
	'handler', 'priority', 'severity', 'reproducibility', 'status', 'resolution', 'projection', 'eta', 'summary', 'description', 
	'steps_to_reproduce', 'due_date', 'attachments');
$g_risk_custom_fields =  array('context', 'impact', 'reference', 'control', 'estimate_end', 'reestimate_end' ,'real_end');
#Urgency
$g_risk_urgencies = '10:urgency_none,20:urgency_month,30:urgency_fifteen,40:urgency_week,50:urgency_immediate';
$g_default_bug_urgency = 10;
$g_allow_no_urgency = "OFF";
$s_risk_urgencies_french = '10:non urgent,20:dans le mois,30:dans la quinzaine,40:dans la semaine,50:immédiate';
$s_risk_urgencies_english = '10:not urgent,20:in the month,30:in the fifteen,40:in the weed,50:immediate';
$s_urgency_french = 'Urgence';
$s_urgency_english = 'Urgency';
#Impact	 
$g_risk_impact = '10:minor,20:middle,30:important,40:major';
$g_default_bug_impact = 10;
$g_allow_no_impact = "OFF";
$s_risk_impact_french =  '10:mineur [1],20:moyen [2],30:important [3],40:majeur [4]';
$s_risk_impact_english = '10:minor [1],20:middle [2],30:important [3],40:major [4]';
$s_impact_french = 'Impact';
$s_impact_english = 'Impact';
#Probability	 
$g_risk_probability = '10:low,20:middle,30:high,40:vhigh';
$g_default_bug_probability = 10;
$g_allow_no_probability = "OFF";
$s_risk_probability_french =  '10:faible [1],20:moyen [2],30:fort [3],40:très fort [4]';
$s_risk_probability_english = '10:low [1],20:middle [2],30:high [3],40:very high [4]';
$s_probability_french = 'Probabilité';
$s_probability_english = 'Probability';
#Trigger condition
$g_default_bug_trigger_condition = '';
$s_trigger_condition_french = 'Condition de déclenchement';
$s_trigger_condition_english = 'Trigger condition';
#Control
$g_risk_control = array('control_vlow', 'control_low', 'control_middle', 'control_high', 'control_vhigh');
#Category
$g_risk_allow_no_category = "OFF";
$g_risk_categories = array('functional', 'organizational', 'technical');
#Status
$g_risk_fixed_status = "ON";
$g_risk_fixed_status_prev = "20";
$g_risk_fixed_status_next = "90";
$g_risk_bug_reopen_status = "20";
$g_risk_bug_feedback_status = "20";
$g_risk_bug_resolved_status_threshold = "90";
$g_risk_bug_readonly_status_threshold = "90";
$g_risk_bug_assigned_status = "20";
$g_risk_status = '10:new,20:progress,90:closed';
$g_risk_status_fixed = array('new', 'progress', 'closed');
$g_risk_status_colors = array( 'new'			=> '#fcbdbd', // red    (scarlet red #ef2929)
		 'progress'		=> '#d2f5b0', // green  (chameleon   #8ae234)
		 'closed'		=> '#c9ccc4'); // grey  (aluminum    #babdb6)
#Status name
$s_risk_status_french = '10:nouveau,20:en cours,90:fermé';
$s_risk_status_english = '10:new,20:in progress,90:closed'; 
#Status title
$s_risk_status_new_title_french = 'Nouveau risque';
$s_risk_status_new_title_english = 'New risk';
$s_risk_status_progress_title_french = 'Risque en cours';
$s_risk_status_progress_title_english = 'Risk in progress';
$s_risk_status_closed_title_french = 'Risque fermé';
$s_risk_status_closed_title_english = 'Risk closed';
#Status button
$s_risk_status_new_button_french = 'Nouveau risque';
$s_risk_status_new_button_english = 'New risk';
$s_risk_status_progress_button_french = 'Démarrer le risque';
$s_risk_status_progress_button_english = 'Start risk';
$s_risk_status_closed_button_french = 'Fermer le risque';
$s_risk_status_closed_button_english = 'Close risk';
#Status notification email
#Status email
$s_risk_status_new_email_french = "Le risque suivant est maintenant à  l\'état « nouveau » (encore).";
$s_risk_status_new_email_english = 'The following risk is now in status « new ».';
$s_risk_status_progress_email_french = "Le risque suivant est maintenant  à  l\'état « en cours »";
$s_risk_status_progress_email_english = 'The following risk is now in status « in progress ».';
$s_risk_status_closed_email_french = 'Le risque suivant a été fermé.';
$s_risk_status_closed_email_english = 'The following risk has been closed.';
#Workflow


/**********************************
 * Subproject decision attributes *
 **********************************/ 
$g_decision_name = 'decision';
$g_decision_description = 'Ce projet est le sous-projet décision.'; 
#Fields
$g_decision_report_page_fields =  array('category_id', 'view_state', 'handler', 'priority', 'summary', 'description', 
	'due_date', 'attachments');
$g_decision_view_page_fields =  array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_decision_print_page_fields =   array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_decision_update_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_decision_change_status_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_decision_custom_fields =  array('responsable', 'estimate_end', 'reestimate_end' ,'real_end');
#Priority
$g_decision_allow_no_priority = "ON";
#Category
$g_decision_allow_no_category = "OFF";
$g_decision_categories = array('commitment_backlog', 'climb_backlog', 'management_backlog');
#Status
$g_decision_fixed_status = "ON";
$g_decision_fixed_status_prev = "50";
$g_decision_fixed_status_next = "80";
$g_decision_bug_reopen_status = "20";
$g_decision_bug_feedback_status = "20";
$g_decision_bug_resolved_status_threshold = "90";
$g_decision_bug_readonly_status_threshold = "90";
$g_decision_bug_assigned_status = "20";
$g_decision_status = '10:new,20:progress,30:tovalidate,40:validate,50:invalidate,80:cancel,90:closed';
$g_decision_status_colors = array( 'new'			=> '#fcbdbd', // red    (scarlet red #ef2929)
		 'progress'		=> '#d2f5b0', // green  (chameleon   #8ae234)
		 'tovalidate'		=> '#c2dfff', // blue   (sky blue    #729fcf)
		 'validate'		=> '#e3b7eb', // purple (plum        #75507b)
		 'invalidate'	=> '#ffcd85', // orange (orango      #f57900)
		 'cancel'	=> '#fff494', // yellow (butter      #fce94f)
		 'closed'		=> '#c9ccc4'); // grey  (aluminum    #babdb6)
#Status name
$s_decision_status_french = '10:nouveau,20:en cours,30: à  valider,40:validé,50:invalidé,80:annulé,90:fermé';
$s_decision_status_english = '10:new,20:in progress,30:to validate,40:validated,50:invalidated,80:cancelled,90:closed'; 
#Status title
$s_decision_status_new_title_french = 'Nouvelle décision';
$s_decision_status_new_title_english = 'New decision';
$s_decision_status_progress_title_french = 'Décision en cours';
$s_decision_status_progress_title_english = 'Decision in progress';
$s_decision_status_tovalidate_title_french = 'Décision à  valider';
$s_decision_status_tovalidate_title_english = 'Decision to validate';
$s_decision_status_validate_title_french = 'Décision validée';
$s_decision_status_validate_title_english = 'Decision validate';
$s_decision_status_invalidate_title_french = 'Décision invalidée';
$s_decision_status_invalidate_title_english = 'Decision invalidate';
$s_decision_status_cancel_title_french = 'Décision annulée';
$s_decision_status_cancel_title_english = 'Decision cancelled';
$s_decision_status_closed_title_french = 'Décision fermé';
$s_decision_status_closed_title_english = 'Decision closed';
#Status button
$s_decision_status_new_button_french = 'Nouvelle décision';
$s_decision_status_new_button_english = 'New decision';
$s_decision_status_progress_button_french = 'Démarrer la décision';
$s_decision_status_progress_button_english = 'Start decision';
$s_decision_status_tovalidate_button_french = 'Décision à  valider';
$s_decision_status_tovalidate_button_english = 'Decision to validate';
$s_decision_status_validate_button_french = 'Valider la décision';
$s_decision_status_validate_button_english = 'Validate decision';
$s_decision_status_invalidate_button_french = 'Invalider la décision';
$s_decision_status_invalidate_button_english = 'Invalidate decision';
$s_decision_status_cancel_button_french = 'Annuler la décision';
$s_decision_status_cancel_button_english = 'Cancel decision';
$s_decision_status_closed_button_french = 'Fermer la décision';
$s_decision_status_closed_button_english = 'Close decision';
#Status notification email
#Status email
$s_decision_status_new_email_french = "La décision suivante est maintenant à  l\'état « nouveau » (encore).";
$s_decision_status_new_email_english = 'The following decision is now in status « new ».';
$s_decision_status_progress_email_french = "La décision suivante est maintenant  à  l\'état « en cours ».";
$s_decision_status_progress_email_english = 'The following decision is now in status «ï¿½ in progress ».';
$s_decision_status_tovalidate_email_french = 'La décision suivante est à  valider.';
$s_decision_status_tovalidate_email_english = 'The following decision has to be validate.';
$s_decision_status_validate_email_french = 'La décision suivante est validée.';
$s_decision_status_validate_email_english = 'The following decision has been validated.';
$s_decision_status_invalidate_email_french = 'La décision suivante est invalidée.';
$s_decision_status_invalidate_email_english = 'The following decision has been invalidated.';
$s_decision_status_cancel_email_french = 'La décision suivant est annulée.';
$s_decision_status_cancel_email_english = 'The following decision has been cancelled.';
$s_decision_status_closed_email_french = 'La décision suivante a été fermé.';
$s_decision_status_closed_email_english = 'The following decision has been closed.';



/***********************************
 * Subproject evolution attributes *
 ***********************************/ 
$g_evolution_name = 'evolution';
$g_evolution_description = 'Ce projet est le sous-projet évolution.'; 
#Fields
$g_evolution_report_page_fields =  array('category_id', 'view_state', 'handler', 'priority', 'summary', 'description', 
	'due_date', 'attachments');
$g_evolution_view_page_fields =  array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_evolution_print_page_fields =   array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 

	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_evolution_update_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_evolution_change_status_page_fields = array('id', 'project', 'category_id', 'view_state', 'date_submitted', 'last_updated', 
	'reporter', 'handler', 'priority', 'status', 'projection', 'eta', 'summary', 'description', 
	'due_date', 'attachments');
$g_evolution_custom_fields =  array('charge', 'delivery', 'mep' ,'uc', 'it');
#Priority
$g_evolution_allow_no_priority = "ON";
#Category
$g_evolution_allow_no_category = "OFF";
$g_evolution_categories = array('cig', 'ergonomy', 'general', 'business', 'archi', 'specific');
#Status
$g_evolution_fixed_status = "ON";
$g_evolution_fixed_status_prev = "40";
$g_evolution_fixed_status_next = "80";
$g_evolution_bug_reopen_status = "20";
$g_evolution_bug_feedback_status = "20";
$g_evolution_bug_resolved_status_threshold = "90";
$g_evolution_bug_readonly_status_threshold = "90";
$g_evolution_bug_assigned_status = "40";
$g_evolution_status = '10:new,20:waiting_encryption,30:waiting_moa,40:accepted,80:cancel,90:closed';
$g_evolution_status_fixed = array('new', 'waiting_encryption', 'waiting_moa', 'accepted', 'cancel', 'closed');
$g_evolution_status_colors = array( 'new'			=> '#fcbdbd', // red    (scarlet red #ef2929)
		 'waiting_encryption'		=> '#d2f5b0', // green  (chameleon   #8ae234)
		 'waiting_moa'		=> '#c2dfff', // blue   (sky blue    #729fcf)
		 'accepted'		=> '#e3b7eb', // purple (plum        #75507b)
		 'cancel'	=> '#ffcd85', 
		 'closed'		=> '#c9ccc4'); // grey  (aluminum    #babdb6)
#Status name
$s_evolution_status_french = '10:nouveau,20:en attente de chiffrage,30:en attente MOA,40:accepté,80:annulé,90:fermé';
$s_evolution_status_english = '10:new,20:waiting encryption,30:waiting MOA,40:accepted,80:cancelled,90:closed'; 
#Status title
$s_evolution_status_new_title_french = 'Nouvelle évolution';
$s_evolution_status_new_title_english = 'New evolution';
$s_evolution_status_waiting_encryption_title_french = 'Evolution en attente de chiffrage';
$s_evolution_status_waiting_encryption_title_english = 'Waiting encryption evolution';
$s_evolution_status_waiting_moa_title_french = 'Evolution en attente MOA';
$s_evolution_status_waiting_moa_title_english = 'Waiting MOA evolution';
$s_evolution_status_accepted_title_french = 'Evolution acceptée';
$s_evolution_status_accepted_title_english = 'Accepted evolution';
$s_evolution_status_cancel_title_french = 'Evolution annulée';
$s_evolution_status_cancel_title_english = 'Canceled evolution';
$s_evolution_status_closed_title_french = 'Evolution fermée';
$s_evolution_status_closed_title_english = 'Evolution closed';
#Status button
$s_evolution_status_new_button_french = 'Nouvelle évolution';
$s_evolution_status_new_button_english = 'New evolution';
$s_evolution_status_waiting_encryption_button_french = "Mettre en attente de chiffrage l\'évolution";
$s_evolution_status_waiting_encryption_button_english = 'Put the evolution pending encryption';
$s_evolution_status_waiting_moa_button_french = "Mettre en attente MOA l\'évolution";
$s_evolution_status_waiting_moa_button_english = 'Put the evolution pending MOA';
$s_evolution_status_accepted_button_french = "Accepter l\'évolution";
$s_evolution_status_accepted_button_english = 'Accept evolution';
$s_evolution_status_cancel_button_french = "Annuler l\'évolution";
$s_evolution_status_cancel_button_english = 'Cancel evolution';
$s_evolution_status_closed_button_french = "Fermer l\'évolution";
$s_evolution_status_closed_button_english = 'Close evolution';
#Status email
$s_evolution_status_new_email_french = "L\'évolution suivante est maintenant à  l\'état « nouveau » (encore).";
$s_evolution_status_new_email_english = 'The following evolution is now in status « new ».';
$s_evolution_status_waiting_encryption_email_french = "L\'évolution suivante est en attente de chiffrage.";
$s_evolution_status_waiting_encryption_email_english = 'The following evolution is pending encryption.';
$s_evolution_status_waiting_moa_email_french = "L\'évolution suivante est en attente MOA.";
$s_evolution_status_waiting_moa_email_english = 'The following evolution is pending MOA.';
$s_evolution_status_accepted_email_french = "L\'évolution suivante est acceptée.";
$s_evolution_status_accepted_email_english = 'The following evolution has been accepted.';
$s_evolution_status_cancel_email_french = "L\'évolution suivante est annulée.";
$s_evolution_status_cancel_email_english = 'The following evolution has been canceled.';
$s_evolution_status_closed_email_french = "L\'évolution suivante a été fermée.";
$s_evolution_status_closed_email_english = 'The following evolution has been closed.';
$s_evolution_status_closed_email_french = "L\'évolution suivante a été fermée.";
$s_evolution_status_closed_email_english = 'The following evolution has been closed.';

/************************************
 * General project's configurations *
 ************************************/ 
$g_management_projects_status = array( 'name' => 'development' ); // development
$g_management_projects_view_state = array( 'id' => VS_PRIVATE );
$g_management_language_status = array('french', 'english');
$g_responsables = array('AMOA', 'MOE', 'SATE', 'SADI', 'SMO', 'SSI', 'QUALITE');
#Title
$s_title_french = 'Titre';
$s_title_english = 'Title';
#Note
$s_note_french = 'Note';
$s_note_english = 'Note';

?>
