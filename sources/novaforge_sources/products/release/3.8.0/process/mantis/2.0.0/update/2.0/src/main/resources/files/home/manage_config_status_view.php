<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
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
 
# This page display either the specific value in url or a new form 

require_once( 'core.php' );

auth_reauthenticate();

$t_pref = user_pref_get( auth_get_current_user_id() );
$language_status = array();
$t_label_status = '';

/**
 * Retrieve currentProjectId from the session
 * @return currentProjectId
 */
function getCurProjectId(){

	if (count(session_get('filled_status',array())) != 0 )
	{
		$filled = session_get('filled_status');
		$cur_project_id = $filled['cur_project_id'];
	} 
	else {
		$cur_project_id = '';
	}	
	return $cur_project_id;
}

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
	global $t_label_status;
	$t_name_field = $p_field.'_status';
	if ( $t_label_status != '' ) {
		#get existing languages
		$status_enum_language = lang_get( $t_label_status.'_enum_language');
		$language_array = explode(",", $status_enum_language);
		
		#if form filled
		if ( $filled != null ) {
			if ( array_key_exists($p_field.'_status_'.$p_language, $filled) ) {
				$value = $filled[$p_field.'_status_'.$p_language];
			}
			echo '<input type="text" name="'.$t_name_field.'_'.$p_language.'" value="'.$value.'" size="32" maxlength="32" />&nbsp&nbsp'.$p_language.'<br/>';
		}
		#elseif existing data about input (first display)
		elseif ( in_array($p_language, $language_array) ) {
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
    $cur_project_id	= getCurProjectId();
}
# --UPDATE--
# --  add/delete language or update Status
elseif ( (isset($_GET['action']) and $_GET['action'] == "update") or isset($_POST['status_update'])) {
	$t_action = UPDATE;	
	if (  isset($_GET['label']) and $_GET['label'] != "" ) {
		$t_label_status = $_GET['label'];
		// language status retrieved from URL
		if ( isset($_GET['language_status']) and $_GET['language_status'] != "")
		{
		   $language_status=explode(",", gpc_get_string('language_status'));
		}
		// language status loaded from file
		else 
		{
		    $status_enum_language = lang_get( $t_label_status.'_enum_language');
			$language_array = explode(",", $status_enum_language);
			foreach ( $language_array as $language ) {
				array_push($language_status, $language);
			}
		}

		$hidden_languages = array_merge($language_status, session_get('error_language'));
		
		#data filled by the user	
		if (count(session_get('filled_status',array())) != 0 )
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
	# --Language
	#initialize language session variable when create action
	#Retrieved all language from URL
	if ( isset($_GET['language_status']) and $_GET['language_status'] != "")
	{
		  $language_status=explode(",", gpc_get_string('language_status'));
	}
	#if no add default language
	else {
		array_push($language_status, DEFAULT_LANGUAGE);	
	}
	
	$hidden_languages = array_merge($language_status, session_get('error_language'));
	
	# --Date filled
	if ((count(session_get('filled_status',array())) != 0 ) and !isset($_POST['create_status']) )
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
	/* Barre de titre */
	layout_page_header( lang_get( 'manage_status_config' ) );
	/* Side bar */
	layout_page_begin( 'manage_overview_page.php' );
	/* titre du haut */
	print_manage_menu( 'adm_permissions_report.php' );
	print_manage_config_menu( 'manage_config_status_page.php' );
}


# --Displaying
if ( $t_action === CREATE ) {  ?>
	
	<!-- Config Set Form -->
	<form method="post" action="manage_config_status_create.php">
		<?php 
			echo '<fieldset>';
			echo form_security_field( 'manage_config_status_create' );
				
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
		<!-- Other language -->
		<tr <?php echo helper_alternate_class() ?> valign="top">
			<td>
				<?php echo lang_get( 'language_status' ) ?>
			</td>
			<td colspan="2">
				<select name="language_to_add">
					<?php print_language_option_list( $t_pref->language, $hidden_languages) ?>
				</select>
				<input type="submit" name="add_language" class="button" value="<?php echo lang_get( 'add_language' ) ?>" />
				<?php if ( count($language_status) > 1 ) { ?>
					<select name="language_to_delete">
						<?php foreach ( $language_status as $language ) { 
							if ( $language != DEFAULT_LANGUAGE ) {
								echo '<option value="'.$language.'">'.$language.'</option>';
							}
						} ?>
					</select>
					<input type="submit" name="delete_language" class="button" value="<?php echo lang_get( 'delete_language' ) ?>" />
				<?php } ?>
			</td>
		</tr>
		
		<input type="hidden" name ="language_status" value="<?php echo implode(",",$language_status) ?>" />
		
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
elseif ( $t_action === UPDATE ) {  ?>
	<!-- Config Set Form -->
	<form method="post" action="manage_config_status_update.php">
		<?php 
			echo '<fieldset>';
			echo form_security_field( 'manage_config_update_status' );
				
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
		<!-- Other language -->
		<tr <?php echo helper_alternate_class() ?> valign="top">
			<td>
				<?php echo lang_get( 'language_status' ) ?>
			</td>
			<td colspan="2">
				<select name="language_to_add">
					<?php print_language_option_list( $t_pref->language,  $hidden_languages) ?>
				</select>
				<input type="submit" name="<?php echo "add_language_$t_label_status" ?>" class="button" value="<?php echo lang_get( 'add_language' ) ?>" />
				<?php if ( count($language_status) > 1 ) { ?>
					<select name="language_to_delete">
						<?php foreach ( $language_status as $language ) { 
							if ( $language != DEFAULT_LANGUAGE ) {
								echo '<option value="'.$language.'">'.$language.'</option>';
							}
						} ?>
					</select>
					<input type="submit" name="<?php echo "delete_language_$t_label_status" ?>" class="button" value="<?php echo lang_get( 'delete_language' ) ?>" />
				<?php } ?>
			</td>
		</tr>
		
		<input type="hidden" name ="language_status" value="<?php echo implode(",",$language_status) ?>" />
				
				
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
		#display_row_submit(array('update','delete')  , true);
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


//EOF
?>
