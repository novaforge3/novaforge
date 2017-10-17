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


/**
* MantisBT Core API's
*/
require_once( 'core.php' );
require_api( 'risk_criteria_api.php' );

auth_reauthenticate();

# don't index bug report page
html_robots_noindex();

/* Barre de titre */
layout_page_header( lang_get( 'risk_criteria_configuration_link' ) );
/* Side bar */
layout_page_begin( 'manage_overview_page.php' );
/* titre du haut */
print_prism_menu( 'risk_criteria_page.php' );
/* titre du bas */
print_risk_criteria_menu( 'risk_criteria_page.php' );

$others = false;
$delete = false;

function section_begin($p_form, $p_fieldset) {
    echo '<div class="form-container">';
	echo $p_form;

	echo '<div class="widget-box widget-color-blue2">';
	echo '   <div class="widget-header widget-header-small">';
	echo '        <h4 class="widget-title lighter uppercase">';
	echo '            <i class="ace-icon fa fa-random"></i>';
	echo strtoupper( lang_get( 'manage_risk_criteria' ) );
	echo '       </h4>';
	echo '   </div>';
	echo '   <div class="widget-body">';
	echo '   <div class="widget-main no-padding">';
	echo '       <div class="table-responsive">';
	echo "\t<table  class=\"table table-striped table-bordered table-condensed\">\n";
	echo "\t\t<tbody>\n";
	echo $p_fieldset;
	
}   

function section_end() {
    echo '</fieldset>';
	echo '</tbody></table></div>' . "\n";
	echo '</div></div>' . "\n"; 
}


#delete
if ( null != $_GET['action'] && 'delete' == $_GET['action'] ) {
	$delete = true;
	$form = '<form method="post" action="risk_criteria_update.php">';
	$fieldset='<fieldset>'.form_security_field( 'risk_criteria_update' );
	$button = 	'<div class="widget-toolbox padding-8 clearfix">'.
	            '<input type="hidden" name="id" value="'. $_GET['id']. '" />'.
				'<input type="submit" name="confirm_delete" class="btn btn-primary btn-sm btn-white btn-round" value="' .lang_get( 'delete_risk_criteria_button' ). '" />'.
				'</div>';				
}
#update
elseif ( null != $_GET['id'] ) {
	$risk_criteria = risk_criteria_get($_GET['id']); 
	$f_weight = $risk_criteria->weight;
	$form = '<form name="risk_criteria_form" method="post" action="risk_criteria_update.php">';	
	$fieldset='<fieldset>'.form_security_field( 'risk_criteria_update' );
	$button = '<div class="widget-toolbox padding-8 clearfix">'.
			  '<span class="required pull-right"> * '.lang_get( 'required' ).'</span>'.
			   '<input type="hidden" name="id" value="' .$_GET['id']. '" />'.
			  '<input ' .helper_get_tab_index(). ' type="submit" class="btn btn-primary btn-sm btn-white btn-round" value="' .lang_get( 'update_risk_criteria_button' ). '" />';
	if ( category_get_id_by_name('others', -1) == $risk_criteria->category_id ) {
		$others = true;
		$button .= '</div>';	
	}
	else {
		$button .= '<input ' .helper_get_tab_index(). ' type="submit" class="btn btn-primary btn-sm btn-white btn-round" name="delete" value="' .lang_get( 'delete_risk_criteria_button' ). '" />'.
				   '</div>';			
	}		
}
#create
else {
	$f_weight = 0;
	$form = '<form name="risk_criteria_form" method="post" action="risk_criteria_create.php">';
	
	$fieldset='<fieldset>'.form_security_field( 'risk_criteria_create' );
	
	$button = '<div class="widget-toolbox padding-8 clearfix">'.
			  '<span class="required pull-right"> * '.lang_get( 'required' ) .'</span>'.
	          '<input ' .helper_get_tab_index(). ' type="submit" class="btn btn-primary btn-sm btn-white btn-round" value="' .lang_get( 'submit_risk_criteria_button' ). '" />'.
   			  '</div>';
}

echo '<div class="space-10"></div>'; 
section_begin($form,$fieldset);
#IHM
if ( !$delete ) { 
?>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category" width="30%">
			<span class="required">*</span><?php print_documentation_link( 'category' ) ?>
		</td>
		<td width="70%">
			<?php if ( $others ) { 
				echo lang_get( 'others'); ?>
				<input type="hidden" name="category_id" value="<?php echo $risk_criteria->category_id; ?>"/>	
			<?php }	
			else { ?>
				<select <?php echo helper_get_tab_index() ?> name="category_id">
					<?php print_category_option_list( $risk_criteria->category_id, -1, false, false );
					?>
				</select>
			<?php } ?>
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<span class="required">*</span><?php print_documentation_link( 'label' ) ?>
		</td>
		<td>
			<?php if ( $others ) { 
				echo $risk_criteria->label; ?>
				<input type="hidden" name="label" value="<?php echo $risk_criteria->label; ?>"/>	
			<?php }	
			else { ?>	
				<input <?php echo helper_get_tab_index() ?> type="text" name="label" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->label ) ?>" />
			<?php } ?>
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<?php print_documentation_link( 'weight' ) ?>
		</td>
		<td>
			<?php echo string_attribute( $f_weight ) ."&nbsp&nbsp". lang_get( 'weight_info') ?>
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<span class="required">*</span><?php print_documentation_link( 'evaluation1' ) ?>
		</td>
		<td>
			<input <?php echo helper_get_tab_index() ?> type="text" name="evaluation1" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->evaluation1 ) ?>" />
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<span class="required">*</span><?php print_documentation_link( 'evaluation2' ) ?>
		</td>
		<td>
			<input <?php echo helper_get_tab_index() ?> type="text" name="evaluation2" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->evaluation2 ) ?>" />
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<span class="required">*</span><?php print_documentation_link( 'evaluation3' ) ?>
		</td>
		<td>
			<input <?php echo helper_get_tab_index() ?> type="text" name="evaluation3" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->evaluation3 ) ?>" />
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<span class="required">*</span><?php print_documentation_link( 'evaluation4' ) ?>
		</td>
		<td>
			<input <?php echo helper_get_tab_index() ?> type="text" name="evaluation4" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->evaluation4 ) ?>" />
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<?php print_documentation_link( 'evaluation5' ) ?>
		</td>
		<td>
			<input <?php echo helper_get_tab_index() ?> type="text" name="evaluation5" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->evaluation5 ) ?>" />
		</td>
	</tr>
	<tr <?php echo helper_alternate_class() ?>>
		<td class="category">
			<?php print_documentation_link( 'evaluation6' ) ?>
		</td>
		<td>
			<input <?php echo helper_get_tab_index() ?> type="text" name="evaluation6" size="105" maxlength="128" value="<?php echo string_attribute( $risk_criteria->evaluation6 ) ?>" />
		</td>
	</tr>
		<?php 	
			section_end();
			echo $button;
		?>
<?php }
else {  ?>
			<tr class="row-1">
				<td class="category" colspan="2">
					<?php echo lang_get( 'delete_risk_criteria_conf_msg' ); ?>
				</td>
			</tr>			
			<?php 	
			section_end();
			echo $button;
			?>
<?php } 

echo '</div></form>' . "\n";
echo '</div>' . "\n";
layout_page_end();

//EOF
?>


