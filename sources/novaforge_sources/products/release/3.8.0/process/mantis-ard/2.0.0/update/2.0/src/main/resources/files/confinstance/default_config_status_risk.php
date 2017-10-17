<?php
/**
* Custom Configuration Variables for project : risk
*/

/**
* Project ID
*/
$g_project_id = 3;
/**
* Particular fields
*/
#fields
	$g_bug_report_page_fields = array(
		 'category_id', 
		 'view_state', 
		 'handler', 
		 'priority', 
		 'severity', 
		 'reproducibility', 
		 'summary', 
		 'description', 
		 'steps_to_reproduce', 
		 'due_date', 
		 'attachments', 
	);
	$g_bug_view_page_fields = array(
		 'id', 
		 'project', 
		 'category_id', 
		 'view_state', 
		 'date_submitted', 
		 'last_updated', 
		 'reporter', 
		 'handler', 
		 'priority', 
		 'severity', 
		 'reproducibility', 
		 'status', 
		 'resolution', 
		 'gravity', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
		 'steps_to_reproduce', 
		 'due_date', 
		 'attachments', 
	);
	$g_bug_print_page_fields = array(
		 'id', 
		 'project', 
		 'category_id', 
		 'view_state', 
		 'date_submitted', 
		 'last_updated', 
		 'reporter', 
		 'handler', 
		 'priority', 
		 'severity', 
		 'reproducibility', 
		 'status', 
		 'resolution', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
		 'steps_to_reproduce', 
		 'due_date', 
		 'attachments', 
	);
	$g_bug_update_page_fields = array(
		 'id', 
		 'project', 
		 'category_id', 
		 'view_state', 
		 'date_submitted', 
		 'last_updated', 
		 'reporter', 
		 'handler', 
		 'priority', 
		 'severity', 
		 'reproducibility', 
		 'status', 
		 'resolution', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
		 'steps_to_reproduce', 
		 'due_date', 
		 'attachments', 
	);
	$g_bug_change_status_page_fields = array(
		 'id', 
		 'project', 
		 'category_id', 
		 'view_state', 
		 'date_submitted', 
		 'last_updated', 
		 'reporter', 
		 'handler', 
		 'priority', 
		 'severity', 
		 'reproducibility', 
		 'status', 
		 'resolution', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
		 'steps_to_reproduce', 
		 'due_date', 
		 'attachments', 
	);
#priority
	$g_priority_enum_string = '10:urgency_none,20:urgency_month,30:urgency_fifteen,40:urgency_week,50:urgency_immediate';
	$g_default_bug_priority = 10;
	$g_allow_no_priority = OFF;
	$s_priority_enum_string_french = '10:non urgent,20:dans le mois,30:dans la quinzaine,40:dans la semaine,50:immédiate';
	$s_priority_enum_string_english = '10:not urgent,20:in the month,30:in the fifteen,40:in the weed,50:immediate';
	$s_priority_french = 'Urgence';
	$s_priority_english = 'Urgency';
#category
#severity
	$g_severity_enum_string = '10:minor,20:middle,30:important,40:major';
	$g_default_bug_severity = 10;
	$g_allow_no_severity = OFF;
	$s_severity_enum_string_french = '10:mineur [1],20:moyen [2],30:important [3],40:majeur [4]';
	$s_severity_enum_string_english = '10:minor [1],20:middle [2],30:important [3],40:major [4]';
	$s_severity_french = 'Impact';
	$s_severity_english = 'Impact';
#reproducibility
	$g_reproducibility_enum_string = '10:low,20:middle,30:high,40:vhigh';
	$g_default_bug_reproducibility = 10;
	$g_allow_no_reproducibility = OFF;
	$s_reproducibility_enum_string_french = '10:faible [1],20:moyen [2],30:fort [3],40:très fort [4]';
	$s_reproducibility_enum_string_english = '10:low [1],20:middle [2],30:high [3],40:very high [4]';
	$s_reproducibility_french = 'Probabilité';
	$s_reproducibility_english = 'Probability';
#summary
	$s_summary_french = 'Titre';
	$s_summary_english = 'Title';
#steps_to_reproduce
	$g_default_bug_steps_to_reproduce = '';
	$s_steps_to_reproduce_french = 'Condition de déclenchement';
	$s_steps_to_reproduce_english = 'Trigger condition';
#additional_information
	$s_additional_information_french = 'Note';
	$s_additional_information_english = 'Note';
#control

/**
* Status management
*/
#status
	$g_default_status_prev = 20;
	$g_default_status_next = 90;
	$g_default_status_enum_string = '10:new,20:progress,90:closed';
	$g_bug_reopen_status = 20;
	$g_bug_feedback_status = 20;
	$g_bug_resolved_status_threshold = 90;
	$g_bug_readonly_status_threshold = 90;
	$g_bug_assigned_status = 20;
	$g_status_enum_string = '10:new,20:progress,90:closed';
	$g_language_status = array('french', 'english', );
	$g_status_colors = array(
	'new' => '#fcbdbd',
	'progress' => '#d2f5b0',
	'closed' => '#c9ccc4',
	);
#name
	$s_status_enum_string_french = '10:nouveau,20:en cours,90:fermé';
	$s_status_enum_string_english = '10:new,20:in progress,90:closed';
#title
	$s_new_bug_title_french = 'Nouveau risque';
	$s_new_bug_title_english = 'New risk';
	$s_progress_bug_title_french = 'Risque en cours';
	$s_progress_bug_title_english = 'Risk in progress';
	$s_closed_bug_title_french = 'Risque fermé';
	$s_closed_bug_title_english = 'Risk closed';
#button
	$s_new_bug_button_french = 'Nouveau risque';
	$s_new_bug_button_english = 'New risk';
	$s_progress_bug_button_french = 'Démarrer le risque';
	$s_progress_bug_button_english = 'Start risk';
	$s_closed_bug_button_french = 'Fermer le risque';
	$s_closed_bug_button_english = 'Close risk';
#notification_email
#email
	$s_email_notification_title_for_status_bug_new_french = 'Le risque suivant est maintenant à  l\'état « nouveau » (encore).';
	$s_email_notification_title_for_status_bug_new_english = 'The following risk is now in status « new ».';
	$s_email_notification_title_for_status_bug_progress_french = 'Le risque suivant est maintenant  à  l\'état « en cours »';
	$s_email_notification_title_for_status_bug_progress_english = 'The following risk is now in status « in progress ».';
	$s_email_notification_title_for_status_bug_closed_french = 'Le risque suivant a été fermé.';
	$s_email_notification_title_for_status_bug_closed_english = 'The following risk has been closed.';
#workflow
	$g_status_enum_workflow[10] ='20:progress';
	$g_status_enum_workflow[20] ='90:closed';
	$g_status_enum_workflow[90] ='20:progress';

