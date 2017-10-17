<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2007-2009 BULL SAS
* Copyright (C) 2017  Atos, NovaForge Version 3 and above.
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

require_once( 'core.php' );
require_api( 'access_api.php' );
require_api( 'authentication_api.php' );
require_api( 'config_api.php' );
require_api( 'constant_inc.php' );
require_api( 'current_user_api.php' );
require_api( 'event_api.php' );
require_api( 'helper_api.php' );
require_api( 'html_api.php' );
require_api( 'lang_api.php' );

auth_reauthenticate();
layout_page_header( lang_get( 'manage_status_config' ) );
layout_page_begin( 'manage_overview_page.php' );
print_manage_menu( 'adm_config_report.php' );
print_manage_config_menu( 'manage_config_status_page.php' );

$t_project = helper_get_current_project();

$t_fixed_status = array('new', 'closed');

if (isset( $_SESSION[config_get_global( 'session_key' )]['language_status'] ) and count(session_get('language_status')) != 0) {
	session_delete('language_status');
}
if (isset( $_SESSION[config_get_global( 'session_key' )]['filled_status'] ) and count(session_get('filled_status')) != 0) {
	session_delete('filled_status');
}


#update classification
if ( isset($_POST['update_classification']) ) {

    form_security_validate( 'manage_config_status_page' );
	
	
	$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
	$t_number_array = array();
	$t_classification_status['new'] = 1;
	$t_classification_status['closed'] = count($t_enum_status);
	foreach ( $t_enum_status as $t_to_status_id => $t_to_status_label ) {
		if ( 'new' != $t_to_status_label && 'closed' != $t_to_status_label ) {
			$f_classification_status = gpc_get_int( 'weight_'.$t_to_status_label );
			if( is_blank($f_classification_status) ) {
				error_parameters( ' classification '.$t_to_status_label );
				trigger_error( ERROR_EMPTY_FIELD, ERROR );
			}	
			if ( in_array($f_classification_status, $t_number_array) ) {
				error_parameters( 'classification number '.$t_to_status_label );
				trigger_error( ERROR_STATUS_CLASSIFICATION_NUMBER, ERROR );
			}	
			if ( $t_classification_status['new'] >= $f_classification_status || 
				$t_classification_status['closed'] <= $f_classification_status ) {
				error_parameters( 'classification number '.$t_to_status_label );
				trigger_error( ERROR_STATUS_CLASSIFICATION_NUMBER_BORNS, ERROR );
			}
			array_push($t_number_array, $f_classification_status);
			$t_classification_status[$t_to_status_label] = $f_classification_status;
		}   		
	}
	update_classification($t_classification_status);	
	
	form_security_purge( 'manage_config_status_page' );
	
	print_successful_redirect( 'manage_config_status_page.php' );
}

function indications($p_enable_status_creation) {
	global $t_project;
	if ( ALL_PROJECTS == $t_project ) {
	    $t_project_title = lang_get( 'config_all_projects' );
	} else {
	    $t_project_title = sprintf( lang_get( 'config_project' ) , string_display( project_get_name( $t_project ) ) );
	}
	
	if ($p_enable_status_creation ){
		echo '<div class="well">' . "\n";
		echo '<p class="bold"><i class="fa fa-info-circle"></i> ' . $t_project_title . '</p>' . "\n";
		
		echo lang_get( 'warning_classification_status' );
		
		echo '</div>' . "\n";
	}
}

function section_begin(&$p_enum_status,$p_enable_status_creation) {
    echo '<div class="form-container">';
	if ($p_enable_status_creation ){
		echo '<form method="post" action="manage_config_status_view.php">';
		echo '<input type="submit" name="create_status" class="btn btn-primary btn-sm btn-white btn-round" value="'. lang_get( 'create_status' ) . '"/>' . "\n";
		echo '</form>' . "\n";
	}
	echo '<form method="post" action="manage_config_status_page.php">';
	echo '<div class="space-10"></div>';
	echo '<div class="widget-box widget-color-blue2">';
	echo '   <div class="widget-header widget-header-small">';
	echo '        <h4 class="widget-title lighter uppercase">';
	echo '            <i class="ace-icon fa fa-random"></i>';
	echo strtoupper( lang_get( 'list_status' ) );
	echo '       </h4>';
	echo '   </div>';
	echo '   <div class="widget-body">';
	echo '   <div class="widget-main no-padding">';
	echo '       <div class="table-responsive">';
	echo "\t<table  class=\"table table-striped table-bordered table-condensed\">\n";
	echo "\t\t<thead>\n";
	
	if ( count($p_enum_status) != 0 ) {
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'status' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'classification_status' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'title_status' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'email_status' ) . '</th>' . "\n";
	}
	echo "\t\t" . '</tr>' . "\n";
	echo "\t\t</thead>\n";
	echo "\t\t<tbody>\n";
	
	echo '<fieldset>';
	echo form_security_field( 'manage_config_status_page' );	
}

function status_row(&$p_enum_status) {
	global $t_fixed_status;
	$t_language = user_pref_get( auth_get_current_user_id() )->language;
		
	if( $t_language != user_pref_get( auth_get_current_user_id() )->language) $t_language = 'english';
	$i = 1;
	foreach ( $p_enum_status as $t_to_status_id => $t_to_status_label ) {
	
		$t_name_status = MantisEnum::getLabel( lang_get('status_enum_string_'.$t_language), $t_to_status_id);
			
		echo "\t\t" .'<tr '.helper_alternate_class().' valign="top">';
    	echo "\t\t\t" . '<td bgcolor="'.get_status_color( $t_to_status_id ).'"><a href="manage_config_status_view.php?action=update&label='.$t_to_status_label.'" title="'.$t_to_status_label.' status">'.$t_name_status.'</a></td>';
    	if ( in_array($t_to_status_label, $t_fixed_status) ) {	
    		echo "\t\t\t" .'<td><input size="3" type"text" disabled="disabled" name="weight_'.$t_to_status_label.'" value="'.$i.'" /></td>';		
		}
		else {	
			echo "\t\t\t" .'<td><input size="3" type"text" name="weight_'.$t_to_status_label.'" value="'.$i.'" /></td>';			
		}
    	echo "\t\t\t" .'<td>'.lang_get( $t_to_status_label.'_bug_title_'.$t_language ).'</td>';
		echo "\t\t\t" .'<td>'.lang_get( 'email_notification_title_for_status_bug_'.$t_to_status_label.'_'.$t_language ).'</td>';
		echo '</tr>' . "\n";
		$i++;
	}
}

function section_end(&$p_enum_status, $p_enable_status_creation) {
    echo '</fieldset>';
	echo '</tbody></table></div>' . "\n";
	echo '</div></div>' . "\n";
	echo '<div class="space-10"></div>';
	
	if ( $p_enable_status_creation && count($p_enum_status) != 0 ) {
		$button= '<div class="widget-toolbox padding-8 clearfix">'.
		 '<input ' .helper_get_tab_index(). ' type="submit" name="update_classification" class="btn btn-primary btn-sm btn-white btn-round" value="' .lang_get( 'update_classification' ). '" />'.
		 '</div>';
		 echo $button;
	}
	echo '</div></form>' . "\n";
    echo '</div>' . "\n";
}


echo '<div class="space-10"></div>';

$enable_status_creation = false;
if( ON == config_get( 'enable_status_creation' )) {
	$enable_status_creation = true;
} 

indications($enable_status_creation);

# Bugs status list
$t_enum_status = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );

section_begin($t_enum_status,$enable_status_creation);
if ( count($t_enum_status) != 0 ) {
	status_row($t_enum_status);
} 
section_end($t_enum_status,$enable_status_creation);

layout_page_end();

//EOF
?>
