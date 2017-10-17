<?php
/**
* Custom Configuration Variables for project : evolution
*/

/**
* Project ID
*/
$g_project_id = 5;
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
#steps_to_reproduce
#additional_information
#control

/**
* Status management
*/
#status
	$g_default_status_prev = 40;
	$g_default_status_next = 80;
	$g_default_status_enum_string = '10:new,20:waiting_encryption,30:waiting_moa,40:accepted,80:cancel,90:closed;
	$g_bug_reopen_status = 20;
	$g_bug_feedback_status = 20;
	$g_bug_resolved_status_threshold = 90;
	$g_bug_readonly_status_threshold = 90;
	$g_bug_assigned_status = 40;
	$g_status_enum_string = '10:new,20:waiting_encryption,30:waiting_moa,40:accepted,80:cancel,90:closed;
	$g_language_status = array('french', 'english', );
	$g_status_colors = array(
		'new' => '#fcbdbd',
		'waiting_encryption' => '#d2f5b0',
		'waiting_moa' => '#c2dfff',
		'accepted' => '#e3b7eb',
		'cancel' => '#ffcd85',
		'closed' => '#c9ccc4',
	);
#name
	$s_status_enum_string_french = '10:nouveau,20:en attente de chiffrage,30:en attente MOA,40:accepté,80:annulé,90:fermé';
	$s_status_enum_string_english = '10:new,20:waiting encryption,30:waiting MOA,40:accepted,80:cancelled,90:closed';
#title
	$s_new_bug_title_french = 'Nouvelle évolution';
	$s_new_bug_title_english = 'New evolution';
	$s_waiting_encryption_bug_title_french = 'Evolution en attente de chiffrage';
	$s_waiting_encryption_bug_title_english = 'Waiting encryption evolution';
	$s_waiting_moa_bug_title_french = 'Evolution en attente MOA';
	$s_waiting_moa_bug_title_english = 'Waiting MOA evolution';
	$s_accepted_bug_title_french = 'Evolution acceptée';
	$s_accepted_bug_title_english = 'Accepted evolution';
	$s_cancel_bug_title_french = 'Evolution annulée';
	$s_cancel_bug_title_english = 'Canceled evolution';
	$s_closed_bug_title_french = 'Evolution fermée';
	$s_closed_bug_title_english = 'Evolution closed';
#button
	$s_new_bug_button_french = 'Nouvelle évolution';
	$s_new_bug_button_english = 'New evolution';
	$s_waiting_encryption_bug_button_french = 'Mettre en attente de chiffrage l\'évolution';
	$s_waiting_encryption_bug_button_english = 'Put the evolution pending encryption';
	$s_waiting_moa_bug_button_french = 'Mettre en attente MOA l\'évolution';
	$s_waiting_moa_bug_button_english = 'Put the evolution pending MOA';
	$s_accepted_bug_button_french = 'Accepter l\'évolution';
	$s_accepted_bug_button_english = 'Accept evolution';
	$s_cancel_bug_button_french = 'Annuler l\'évolution';
	$s_cancel_bug_button_english = 'Cancel evolution';
	$s_closed_bug_button_french = 'Fermer l\'évolution';
	$s_closed_bug_button_english = 'Close evolution';
#notification_email
#email
	$s_email_notification_title_for_status_bug_new_french = 'L\'évolution suivante est maintenant à  l\'état « nouveau » (encore).';
	$s_email_notification_title_for_status_bug_new_english = 'The following evolution is now in status « new ».';
	$s_email_notification_title_for_status_bug_waiting_encryption_french = 'L\'évolution suivante est en attente de chiffrage.';
	$s_email_notification_title_for_status_bug_waiting_encryption_english = 'The following evolution is pending encryption.';
	$s_email_notification_title_for_status_bug_waiting_moa_french = 'L\'évolution suivante est en attente MOA.';
	$s_email_notification_title_for_status_bug_waiting_moa_english = 'The following evolution is pending MOA.';
	$s_email_notification_title_for_status_bug_accepted_french = 'L\'évolution suivante est acceptée.';
	$s_email_notification_title_for_status_bug_accepted_english = 'The following evolution has been accepted.';
	$s_email_notification_title_for_status_bug_cancel_french = 'L\'évolution suivante est annulée.';
	$s_email_notification_title_for_status_bug_cancel_english = 'The following evolution has been canceled.';
	$s_email_notification_title_for_status_bug_closed_french = 'L\'évolution suivante a été fermée.';
	$s_email_notification_title_for_status_bug_closed_english = 'The following evolution has been closed.';
#workflow

