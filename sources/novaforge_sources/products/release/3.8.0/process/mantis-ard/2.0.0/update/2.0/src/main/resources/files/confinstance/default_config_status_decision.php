<?php
/**
* Custom Configuration Variables for project : decision
*/

/**
* Project ID
*/
$g_project_id = 4;
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
	$g_default_status_prev = 50;
	$g_default_status_next = 80;
	$g_default_status_enum_string = '10:new,20:progress,30:tovalidate,40:validate,50:invalidate,80:cancel,90:closed';
	$g_bug_reopen_status = 20;
	$g_bug_feedback_status = 20;
	$g_bug_resolved_status_threshold = 90;
	$g_bug_readonly_status_threshold = 90;
	$g_bug_assigned_status = 20;
	$g_status_enum_string = '10:new,20:progress,30:tovalidate,40:validate,50:invalidate,80:cancel,90:closed';
	$g_language_status = array('french', 'english', );
	$g_status_colors = array(
		'new' => '#fcbdbd',
		'progress' => '#d2f5b0',
		'tovalidate' => '#c2dfff',
		'validate' => '#e3b7eb',
		'invalidate' => '#ffcd85',
		'cancel' => '#fff494',
		'closed' => '#c9ccc4',
	);
#name
	$s_status_enum_string_french = '10:nouveau,20:en cours,30: à  valider,40:validé,50:invalidé,80:annulé,90:fermé';
	$s_status_enum_string_english = '10:new,20:in progress,30:to validate,40:validated,50:invalidated,80:cancelled,90:closed';
#title
	$s_new_bug_title_french = 'Nouvelle décision';
	$s_new_bug_title_english = 'New decision';
	$s_progress_bug_title_french = 'Décision en cours';
	$s_progress_bug_title_english = 'Decision in progress';
	$s_tovalidate_bug_title_french = 'Décision à  valider';
	$s_tovalidate_bug_title_english = 'Decision validate';
	$s_validate_bug_title_french = 'Décision validée';
	$s_validate_bug_title_english = 'Decision to validate';
	$s_invalidate_bug_title_french = 'Décision invalidée';
	$s_invalidate_bug_title_english = 'Decision invalidate';
	$s_cancel_bug_title_french = 'Décision annulée';
	$s_cancel_bug_title_english = 'Decision cancelled';
	$s_closed_bug_title_french = 'Décision fermé';
	$s_closed_bug_title_english = 'Decision closed';
#button
	$s_new_bug_button_french = 'Nouvelle décision';
	$s_new_bug_button_english = 'New decision';
	$s_progress_bug_button_french = 'Démarrer la décision';
	$s_progress_bug_button_english = 'Start decision';
	$s_tovalidate_bug_button_french = 'Décision à  valider';
	$s_tovalidate_bug_button_english = 'Validate decision';
	$s_validate_bug_button_french = 'Valider la décision';
	$s_validate_bug_button_english = 'Decision to validate';
	$s_invalidate_bug_button_french = 'Invalider la décision';
	$s_invalidate_bug_button_english = 'Invalidate decision';
	$s_cancel_bug_button_french = 'Annuler la décision';
	$s_cancel_bug_button_english = 'Cancel decision';
	$s_closed_bug_button_french = 'Fermer la décision';
	$s_closed_bug_button_english = 'Close decision';
#notification_email
#email
	$s_email_notification_title_for_status_bug_new_french = 'La décision suivante est maintenant à  l\'état « nouveau » (encore).';
	$s_email_notification_title_for_status_bug_new_english = 'The following decision is now in status « new ».';
	$s_email_notification_title_for_status_bug_progress_french = 'La décision suivante est maintenant  à  l\'état « en cours ».';
	$s_email_notification_title_for_status_bug_progress_english = 'The following decision is now in status «ï¿½ in progress ».';
	$s_email_notification_title_for_status_bug_tovalidate_french = 'La décision suivante est à  valider.';
	$s_email_notification_title_for_status_bug_tovalidate_english = 'The following decision has been validated.';
	$s_email_notification_title_for_status_bug_validate_french = 'La décision suivante est validée.';
	$s_email_notification_title_for_status_bug_validate_english = 'The following decision has to be validate.';
	$s_email_notification_title_for_status_bug_invalidate_french = 'La décision suivante est invalidée.';
	$s_email_notification_title_for_status_bug_invalidate_english = 'The following decision has been invalidated.';
	$s_email_notification_title_for_status_bug_cancel_french = 'La décision suivant est annulée.';
	$s_email_notification_title_for_status_bug_cancel_english = 'The following decision has been cancelled.';
	$s_email_notification_title_for_status_bug_closed_french = 'La décision suivante a été fermé.';
	$s_email_notification_title_for_status_bug_closed_english = 'The following decision has been closed.';
#workflow
	$g_status_enum_workflow[10] ='20:progress,30:to_validate,80:cancel';
	$g_status_enum_workflow[20] ='30:to_validate,80:cancel';
	$g_status_enum_workflow[30] ='40:validate,50:invalidate,80:cancel';
	$g_status_enum_workflow[40] ='20:progress,90:closed';
	$g_status_enum_workflow[50] ='20:progress,90:closed';
	$g_status_enum_workflow[80] ='20:progress,90:closed';
	$g_status_enum_workflow[90] ='20:progress,30:to_validate';

