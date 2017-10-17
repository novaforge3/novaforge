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
 
# This page display either the specific value in url or a new form 

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



$t_pref = user_pref_get( auth_get_current_user_id() );
$language_status = array('french', 'english');
$t_label_status = '';


function section_begin() {
	echo '<div class="form-container">';
 
	echo '<div class="space-10"></div>';
	echo '<div class="widget-box widget-color-blue2">';
	echo '   <div class="widget-header widget-header-small">';
	echo '        <h4 class="widget-title lighter uppercase">';
	echo '            <i class="ace-icon fa fa-random"></i>';
	echo strtoupper( lang_get( 'manage_status' ) );
	echo '       </h4>';
	echo '   </div>';
	echo '   <div class="widget-body">';
	echo '   <div class="widget-main no-padding">';
	echo '       <div class="table-responsive">';
	echo "\t<table  class=\"table table-striped table-bordered table-condensed\">\n";
	echo "\t\t<thead>\n";
	echo "\t\t</thead>\n";
	echo "\t\t<tbody>\n";
}

function section_end() {
	echo '</tbody></table></div>' . "\n";
	echo '</div></div>' . "\n";
	echo '<div class="space-10"></div>';
}

# ------------------
# Display a row with an input type text
function display_row_input(&$p_action, $p_field, $p_required) {
	global $language_status, $filled;
	
	echo '<tr '.helper_alternate_class().' valign="top"><td>';
	if ( $p_required ) {
		echo '<span class="required">*</span>';
	}
	echo lang_get( $p_field.'_status' ).'</td><td>';
	foreach ( $language_status as $language ) {
		display_input($filled, $p_field, $language);
	} 
	echo '</td></tr>';
}	

# ------------------
# Display a row with an submit type text
function display_row_submit($p_actions, $p_label_status = false) {
	global $t_label_status;
	
	$button = '<div class="widget-toolbox padding-8 clearfix">'.
			  '<span class="required pull-right"> * '.lang_get( 'required' ) .'</span>';
	foreach ( $p_actions as $p_action ) {
       	$t_value = "status_".$p_action;
		if ( $p_label_status ) {
			$t_value .= '_'.$t_label_status;
		}
   		$button.='<input ' .helper_get_tab_index(). ' type="submit" name="'.$t_value.'" class="btn btn-primary btn-sm btn-white btn-round" value="' .lang_get( $p_action.'_status' ). '" />';
	}
	
	$button.='</div>';
	
	echo $button;
	
	echo '</div></form>' . "\n";
	echo '</div>' . "\n";
			  
}
		
		

# ------------------
# Display an input type text
function display_input(&$filled, $p_field, &$p_language) {
	global $g_language_status, $t_label_status;
	$t_name_field = $p_field.'_status';
	if ( $t_label_status != '' ) {
		
		#if form filled
		if ( $filled != null ) {
			if ( array_key_exists($p_field.'_status_'.$p_language, $filled) ) {
				$value = $filled[$p_field.'_status_'.$p_language];
			}
			echo '<input type="text" name="'.$t_name_field.'_'.$p_language.'" value="'.$value.'" size="32" maxlength="32" />&nbsp&nbsp'.$p_language.'<br/>';
		}
		#elseif existing data about input (first display)
		elseif ( in_array($p_language, $g_language_status) ) {
			if ( $p_field === NAME ) {
				$t_status_names = MantisEnum::getAssocArrayIndexedByValues( lang_get( 'status_enum_string_'.$p_language ) );
				$t_status_enum_string = MantisEnum::getAssocArrayIndexedByValues( config_get( 'status_enum_string' ) );
				$t_id_status = array_search($t_label_status, $t_status_enum_string);
				$value = $t_status_names[$t_id_status];	
				echo '<input type="text" name="'.$t_name_field.'_'.$p_language.'" value="'.$value.'" size="32" maxlength="32" />&nbsp&nbsp'.$p_language.'<br/>';
			}
			else {
				switch ( $p_field ) {
					case TITLE:
						$value = $t_label_status.'_bug_title_'.$p_language;
						break;
					case BUTTON:
						$value = $t_label_status.'_bug_button_'.$p_language;
						break;
					case EMAIL:
						$value = 'email_notification_title_for_status_bug_'.$t_label_status.'_'.$p_language;
						break;
				}
				echo '<input type="text" name="'.$t_name_field.'_'.$p_language.'" value="'.lang_get($value).'" size="32" maxlength="32" />&nbsp&nbsp'.$p_language.'<br/>';
			}
			
		}
		else {
			echo '<input type="text" name="'.$t_name_field.'_'.$p_language.'" value="" size="32" maxlength="32" />&nbsp&nbsp'.$p_language.'<br/>';
		}			
	}
	else {
		if ( $filled != null ) {
			if ( array_key_exists($p_field.'_status_'.$p_language, $filled) ) {
				$value = $filled[$p_field.'_status_'.$p_language];
			}
		}
		echo '<input type="text" name="'.$t_name_field.'_'.$p_language.'" value="'.$value.'" size="32" maxlength="32" />&nbsp&nbsp'.$p_language.'<br/>';
	}
}


# --DELETE--
if (  isset($_GET['action']) and $_GET['action'] == "delete" ) {
	$t_action = DELETE;	
	if (  isset($_GET['label']) and $_GET['label'] != "" ) {
		$t_label_status = $_GET['label'];
	}
}
# --UPDATE--
elseif ( (isset($_GET['action']) and $_GET['action'] == "update") or isset($_POST['status_update'])) {
	$t_action = UPDATE;	
	if (  isset($_GET['label']) and $_GET['label'] != "" ) {
		$t_label_status = $_GET['label'];
		#data filled by the user	
		if (isset( $_SESSION[config_get_global( 'session_key' )]['filled_status'] ) and count(session_get('filled_status')) != 0)
		{
			$filled = session_get('filled_status');
			$value_color_status = $filled['color_status'];
		} 
		else {
			#get color
			$t_status_colors = config_get( 'status_colors' );
			$value_color_status = strtoupper(substr($t_status_colors[$t_label_status], 1));
		}
	}
}
# --CREATE--
else {
	$t_action = CREATE;
	# --Date filled
	if (isset( $_SESSION[config_get_global( 'session_key' )]['filled_status'] ) and count(session_get('filled_status')) != 0 and !isset($_POST['create_status']) )
	{
		$filled = session_get('filled_status');
		$value_label_status = $filled['label_status'];
		$value_color_status = $filled['color_status'];
		$value_name_status = $filled['name_status'];
	} 
	else {
		$value_label_status = "";
		$value_color_status = "";
		$value_name_status = "";
	}		
}

if ($t_action === CREATE or $t_action === UPDATE)
{
	layout_page_header( lang_get( 'manage_status_config' ) );
	layout_page_begin( 'manage_overview_page.php' );
	print_manage_menu( 'adm_config_report.php' );
	print_manage_config_menu( 'manage_config_status_page.php' );
}

# --Displaying
if ( $t_action === CREATE ) {  ?>

	<!-- Config Set Form -->
	<form method="post" action="manage_config_status_create.php">
		<?php 
			echo '<fieldset>';
			echo form_security_field( 'manage_config_status_create' );
				
			# -- Table Title 
			section_begin();
		?>
		<!-- Label -->
		<tr <?php echo helper_alternate_class() ?> valign="top">
			<td>
				<?php echo '<span class="required">*</span>', lang_get( 'label_status' ) ?>
			</td>
			<td colspan="2">
				<input type="text" name="label_status" value="<?php echo $value_label_status ?>" size="64" maxlength="64" />
				<?php echo lang_get( 'label_condition'); ?>
			</td>
		</tr>
		<!-- Color -->
		<tr <?php echo helper_alternate_class() ?> valign="top">
			<td>
				<?php echo lang_get( 'color_status' ) ?>
			</td>
			<td colspan="2">
					<input type="text" name="color_status" value="<?php echo $value_color_status ?>" size="6" maxlength="6" />
					<?php echo lang_get( 'color_condition'); ?>
			</td>
		</tr>
		<?php
		# --Status name--
		display_row_input($t_action, NAME, true); 
		
		# --Status title--
		display_row_input($t_action, TITLE, true); 
		
		# --Status button--
		display_row_input($t_action, BUTTON, true); 
		
		# --Email--
		display_row_input($t_action, EMAIL, false); 
		
		echo '</fieldset>';
		
		section_end();
		
		# --Submit Button--
		display_row_submit(array('create'));
		
		layout_page_end();	
		
		echo '</form>';
}
# -- UPDATE
elseif ( $t_action === UPDATE ) {  	?>
	<!-- Config Set Form -->
	<form method="post" action="manage_config_status_update.php">
		<?php 
			echo '<fieldset>';
			echo form_security_field( 'manage_config_update_status' );
				
			# -- Table Title 
			section_begin();
		?>
		<!-- Label -->
		<tr <?php echo helper_alternate_class() ?> valign="top">
			<td>
				<?php echo '<span class="required">*</span>', lang_get( 'label_status' ) ?>
			</td>
			<td colspan="2">
				<?php echo $t_label_status; ?>
			</td>
		</tr>
		<!-- Color -->
		<tr <?php echo helper_alternate_class() ?> valign="top">
			<td>
				<?php echo lang_get( 'color_status' ) ?>
			</td>
			<td colspan="2">
					<input type="text" name="color_status" value="<?php echo $value_color_status; ?>" size="6" maxlength="6" />
					<?php echo lang_get( 'color_condition'); ?>
			</td>
		</tr>
		<?php
		# --Status name--
		display_row_input($t_action, NAME, true);
		
		# --Status title--
		display_row_input($t_action, TITLE, true);
		
		# --Status button--
		display_row_input($t_action, BUTTON, true); 
		
		# --Email button--
		display_row_input($t_action, EMAIL, false); 
		
		echo '</fieldset>';
		
		section_end();
		
		# --Submit Button--
		display_row_submit(get_actions_with_status($t_label_status) , true);
		
		layout_page_end();	
		
		echo '</form>';
}
elseif ( $t_action === DELETE ) {
	layout_page_header( lang_get( 'manage_status_config' ) );
	layout_page_begin( 'manage_overview_page.php' );
	echo '<div class="col-md-12 col-xs-12">';
	echo '<div class="space-10"></div>';
	echo '<div class="alert alert-warning center">';
	echo '<p class="bigger-110">';
	echo "\n" . lang_get( 'delete_status_conf_msg' ) . "\n";
	echo '</p>';
	echo '<div class="space-10"></div>';
	
	echo '<form method="post" class="center" action="manage_config_status_update.php">' . "\n";
	echo '<fieldset>';
	echo form_security_field( 'manage_config_update_status' );
	echo '</fieldset>';

	echo '<input type="submit" class="btn btn-primary btn-white btn-round" value="' . lang_get( 'delete_status' ) . '" name="status_confirm_delete_'.$t_label_status. '" />';
	echo "\n</form>\n";

	echo '<div class="space-10"></div>';
	echo '</div></div>';

	layout_page_end();
	
	echo '</form>';
} 

# delete session
if (isset( $_SESSION[config_get_global( 'session_key' )]['filled_status'] ) and count(session_get('filled_status')) != 0) {
	session_delete('filled_status');
}

//EOF
?>
