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

$t_risk_criterias = risk_criteria_get_all();
$categories = category_risk_criteria_get_all();

function indications() {
	echo '<div class="well">' . "\n";	
	echo '<p><i class="fa fa-info-circle"></i>'."\n";
	echo lang_get( 'note_list_risk_criteria' );
	echo '</p>' . "\n";
	echo '</div>' . "\n";
}


function section_begin() {
	echo '<div class="form-container">';
	echo '<form method="post" action="risk_criteria_view.php">';
	echo '<input type="submit" name="create_risk_criteria" class="btn btn-primary btn-sm btn-white btn-round" value="'. lang_get( 'create_risk_criteria' ) . '"/>' . "\n";
	echo '</form>' . "\n";
	
	echo '<form method="post" action="risk_criteria_update.php">';

	echo '<div class="widget-box widget-color-blue2">';
	echo '   <div class="widget-header widget-header-small">';
	echo '        <h4 class="widget-title lighter uppercase">';
	echo '            <i class="ace-icon fa fa-random"></i>';
	echo strtoupper( lang_get( 'list_risk_criteria' ) );
	echo '       </h4>';
	echo '   </div>';
	echo '   <div class="widget-body">';
	echo '   <div class="widget-main no-padding">';
	echo '       <div class="table-responsive">';
	echo "\t<table  class=\"table table-striped table-bordered table-condensed\">\n";
	echo "\t\t<thead>\n";	
	echo "\t\t" . '<tr>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'label' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'weight' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation1' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation2' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation3' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation4' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation5' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation6' ) . '</th>' . "\n";

	echo "\t\t" . '</tr>' . "\n";
	echo "\t\t</thead>\n";
	echo "\t\t<tbody>\n";
	
	echo '<fieldset>';
	echo form_security_field( 'risk_criteria_update' );
}   
    
function category_row( $p_category ) {
	$row = "<tr ><td bgcolor='#edf3f4'>".strtoupper(str_replace('é', 'e', lang_get($p_category['name']))) ."</td><td bgcolor='#edf3f4' colspan='7' id='category".$p_category['id']."'></td></tr>";
	echo $row;
}
function risk_criteria_row(&$p_risk_criteria) {
	$t_category = category_risk_criteria_full_name( $p_risk_criteria->category_id );
	$row = "<tr " .helper_alternate_class(). ">".
		"<td><a href='risk_criteria_view.php?id=$p_risk_criteria->id' alt='lien'>$p_risk_criteria->label</a></td>".
		"<td><input id='$p_risk_criteria->id|$p_risk_criteria->category_id' size='3' type='text' name='weight_$p_risk_criteria->id' value='$p_risk_criteria->weight' " .
		"onClick='checkWeight($p_risk_criteria->category_id)' onFocus='checkWeight($p_risk_criteria->category_id)' onchange='checkWeight($p_risk_criteria->category_id)'/></td>".
		"<td>$p_risk_criteria->evaluation1</td>".
		"<td>$p_risk_criteria->evaluation2</td>".
		"<td>$p_risk_criteria->evaluation3</td>".
		"<td>$p_risk_criteria->evaluation4</td>".
		"<td>$p_risk_criteria->evaluation5</td>".
		"<td>$p_risk_criteria->evaluation6</td></tr>";
	echo $row;
}

function risk_criteria_none() {
	$row = '<tr><td colspan="8" >' .lang_get( 'no_risk_criteria' ). '</td></tr>';
	echo $row;
}
function weight_update_button() {
	$weight_update_button = 
	'<div class="widget-toolbox padding-8 clearfix">'.
	'<input type="submit" class="btn btn-primary btn-white btn-round" name="update_weight" value="'. lang_get( 'update_weight' ) . '"/>'.
	'</div>';
	echo $weight_update_button;	
}
function section_end() {
	echo '</fieldset>';
	echo '</tbody></table></div>' . "\n";
	echo '</div></div>' . "\n";	
}
?>

<SCRIPT language="Javascript">
function checkWeight(category_id) {
	var elements = document.getElementsByTagName("input");
	var sum = 0;
	for( i=0; i<elements.length; i++) {
		var cat = elements[i].id.substr(elements[i].id.lastIndexOf('|') + 1);
		if(cat == category_id) {
			sum += parseInt(document.getElementById(elements[i].id).value);
			var idCat = 'category' + cat;
			if( sum == 100 ) document.getElementById(idCat).style.color='green';
			else document.getElementById(idCat).style.color='red';
			document.getElementById(idCat).innerHTML = sum;
		}
	}

}
</SCRIPT>
<?php
echo '<div class="space-10"></div>';

indications();
section_begin();

if ( 0 == count($t_risk_criterias) ) {
	risk_criteria_none();
	section_end();
} 
else {
	foreach ( $categories as $category ) {
		$risk_criteria_by_category = array();
		foreach ( $t_risk_criterias as $t_risk_criteria ) {
			if ( $category['id'] == $t_risk_criteria->category_id ) {
				$risk_criteria_by_category[] = $t_risk_criteria;
			}
		}
		if ( 0 != count($risk_criteria_by_category) ) {
			category_row($category);
			foreach ( $risk_criteria_by_category as $t_risk_criteria ) {
		       	risk_criteria_row($t_risk_criteria);
			}
		}
	}
	section_end();
	weight_update_button();
}

echo '</div></form>' . "\n";
echo '</div>';
layout_page_end();

//EOF
?>
