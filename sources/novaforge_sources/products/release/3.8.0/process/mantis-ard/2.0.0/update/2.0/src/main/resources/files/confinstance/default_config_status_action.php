<?php
/**
* Custom Configuration Variables for project : action
*/

/**
* Project ID
*/
$g_project_id = 2;
/**
* Particular fields
*/
#fields
	$g_bug_report_page_fields = array(
		 'category_id', 
		 'view_state', 
		 'handler', 
		 'priority', 
		 'summary', 
		 'description', 
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
		 'status', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
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
		 'status', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
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
		 'status', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
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
		 'status', 
		 'projection', 
		 'eta', 
		 'summary', 
		 'description', 
		 'due_date', 
		 'attachments', 
	);
#priority
	$g_allow_no_priority = ON;
#category
	$g_allow_no_category = OFF;
#severity
#reproducibility
#summary
	$s_summary_french = 'Titre';
	$s_summary_english = 'Title';
#steps_to_reproduce
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
	$s_new_bug_title_french = 'Nouvelle action';
	$s_new_bug_title_english = 'New action';
	$s_progress_bug_title_french = 'Action en cours';
	$s_progress_bug_title_english = 'Action in progress';
	$s_closed_bug_title_french = 'Action fermé';
	$s_closed_bug_title_english = 'Action closed';
#button
	$s_new_bug_button_french = 'Nouvelle action';
	$s_new_bug_button_english = 'New action';
	$s_progress_bug_button_french = 'Démarrer l\'action';
	$s_progress_bug_button_english = 'Start action';
	$s_closed_bug_button_french = 'Fermer l\'action';
	$s_closed_bug_button_english = 'Close action';
#notification_email
#email
	$s_email_notification_title_for_status_bug_new_french = 'L\'action suivante est maintenant à  l\'état « nouveau » (encore).';
	$s_email_notification_title_for_status_bug_new_english = 'The following action is now in status « new ».';
	$s_email_notification_title_for_status_bug_progress_french = 'L\'action suivante est maintenant  à  l\'état « en cours ».';
	$s_email_notification_title_for_status_bug_progress_english = 'The following action is now in status « in progress ».';
	$s_email_notification_title_for_status_bug_closed_french = 'L\'action suivante a été fermée.';
	$s_email_notification_title_for_status_bug_closed_english = 'The following action has been closed.';
#workflow
	$g_status_enum_workflow[10] ='20:progress';
	$g_status_enum_workflow[20] ='90:closed';
	$g_status_enum_workflow[90] ='20:progress';

