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
require_api( 'category_api.php' );
require_api( 'risk_criteria_api.php' );

auth_reauthenticate();

# don't index bug report page
html_robots_noindex();


/* Barre de titre */
layout_page_header( lang_get( 'risk_appreciation_link' ) );
/* Side bar */
layout_page_begin( 'manage_overview_page.php' );
/* titre du haut */
print_prism_menu( 'risk_appreciation_page.php' );


$t_risk_criterias = risk_criteria_get_all();
$categories = category_risk_criteria_get_all();

$t_indicators = array();
$t_indicators_by_categorie = array();
foreach ( $t_risk_criterias as $t_risk_criteria ) {
	$t_indicators[$t_risk_criteria->id] = $t_risk_criteria->weight * $t_risk_criteria->evaluation / 100;
	$t_indicators_by_categorie[$t_risk_criteria->category_id] += $t_indicators[$t_risk_criteria->id];
}
//get previous date
$previous_risk_evaluation = risk_evaluation_get_previous(0);
if ( $previous_risk_evaluation != null ) {
	$t_previous_date = '('.DateTime::createFromFormat('Y-m-d', $previous_risk_evaluation['date_modified'])->format('d/m/Y').')';
}
else {
	$t_previous_date = '('.lang_get('no_indicator').')';
}

function get_global_risk() {
	global $t_indicators_by_categorie;
	$categories = category_risk_criteria_get_all();
	$global_risk = 0;
	$others = false;
	foreach ( $categories as $category ) {
       	if ( null != $t_indicators_by_categorie[$category['id']] ) {
			switch ( $category['name'] ) {
			case 'duration':
				$weight = 1;
				break;
			case 'integrity':
				$weight = 2;
				break;
			case 'management_commitment':
				$weight = 2;
				break;
			case 'team_commitment':
				$weight = 1;
				break;
			case 'effort':
				$weight = 1;
				break;
			case 'others':
				$others = true;
				$weight = 1;
				break;
			default:
				break;
			}
			$global_risk += $weight * $t_indicators_by_categorie[$category['id']] ;
		}
	}
	if ( $others ) $global_risk *= 7/8;
	return $global_risk;
}

function section_begin() {
	global $t_previous_date;
	echo '<div class="form-container">';
	echo '<form method="post" action="risk_appreciation_update.php">';	

	echo '<div class="widget-box widget-color-blue2">';	
	echo '   <div class="widget-header widget-header-small">';
	echo '        <h4 class="widget-title lighter uppercase">';
	echo '            <i class="ace-icon fa fa-random"></i>';
	echo strtoupper( lang_get( 'manage_risk_appreciation' ) );
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
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'evaluation' ) . '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'last_indicator' ) . ' ' .$t_previous_date. '</th>' . "\n";
	echo "\t\t\t" . '<th class="bold" style="text-align:center">' . lang_get( 'indicator' ) .  ' ('.date('d/m/Y').')</th>' . "\n";
	echo "\t\t" . '</tr>' . "\n";
	echo "\t\t</thead>\n";
	echo "\t\t<tbody>\n";
	
	echo '<fieldset>';
	echo form_security_field( 'risk_appreciation_update' );
}

/**
 * represents category row UI
 */
function category_row( &$p_category ) {
	global $t_indicators_by_categorie;
	$previous_risk_evaluation = risk_evaluation_get_previous($p_category['id']);
	$row = "<tr >".
				"<td bgcolor='#edf3f4' colspan='3'>".strtoupper(lang_get($p_category['name'])) ."</td>" .
				"<td bgcolor='#edf3f4'>".get_previous_evaluation($previous_risk_evaluation)."</td>".
				"<td bgcolor='#edf3f4'>".$t_indicators_by_categorie[$p_category['id']]."</td>".
			"</tr>" ;			
	echo $row;
}

/**
 * represents a risk_criteria row UI
 */
function risk_criteria_row(&$p_risk_criteria) {
	global $t_indicators;
	$t_category = category_risk_criteria_full_name( $p_risk_criteria->category_id );
	$t_evaluations = array(1 => $p_risk_criteria->evaluation1, 2 => $p_risk_criteria->evaluation2, 
		3 => $p_risk_criteria->evaluation3, 4 => $p_risk_criteria->evaluation4);
	if ( $p_risk_criteria->evaluation5 != '' ) $t_evaluations[5] = $p_risk_criteria->evaluation5;
	if ( $p_risk_criteria->evaluation6 != '' ) $t_evaluations[6] = $p_risk_criteria->evaluation6;
	$row = "<tr " .helper_alternate_class(). ">".
		"<td>$p_risk_criteria->label</td>".
		"<td>$p_risk_criteria->weight</td>".
		"<td><select name='".$p_risk_criteria->id."_appreciation'>";
	foreach ( $t_evaluations as $i => $t_evaluation ) {
   		$row .= "<option value='".$p_risk_criteria->id."_$i' ";
   		if($p_risk_criteria->evaluation == $i) {
   			$row .= "selected='selected'";
   		} 
		$row .= ">$t_evaluation</option>";
	} 
	$row .= "</select></td>".
		"<td></td>".
		"<td>".$t_indicators[$p_risk_criteria->id]."</td></tr>";
	echo $row;
}

function risk_criteria_none() {
	$row = '<tr><td colspan="5" >' .lang_get( 'no_risk_criteria' ). '</td></tr>';
	echo $row;
}
function appreciation_update_button() {	
	$appreciation_update_button =
	'<div class="widget-toolbox padding-8 clearfix">'.
	'<input type="submit" class="btn btn-primary btn-white btn-round" name="update_appreciation" value="'. lang_get( 'update_appreciation' ) . '"/>'.
	'</div>';
	echo $appreciation_update_button;
		
}

function display_global_risk() {
	$previous_risk_evaluation = risk_evaluation_get_previous(0);
	$global_risk = '<tr class="spacer"><td colspan="6"></td></tr>'.
		'<tr>'.
			'<td bgcolor="#edf3f4" colspan="3">'.lang_get('total').' '.lang_get('categories_formule').'</td>'.
			'<td bgcolor="#edf3f4">'.get_previous_evaluation($previous_risk_evaluation).'</td>'.
			'<td bgcolor="#edf3f4">'.get_global_risk().'</td>'.
		'</tr>';
	echo $global_risk;
}

function get_previous_evaluation($p_previous_risk_evaluation) {
	
	if ( $p_previous_risk_evaluation['evaluation'] == 0 ) {
		return lang_get( 'no_indicator' );
	}
	else {
		return $p_previous_risk_evaluation['evaluation'];
	}
}

function section_end() {	
    echo '</fieldset>';
	echo '</tbody></table></div>' . "\n";
	echo '</div></div>' . "\n";
}

?>

<?php
echo '<div class="space-10"></div>';
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
	display_global_risk();
	section_end();
	appreciation_update_button();
}

echo '</div></form>' . "\n";
echo '</div>';
layout_page_end();

//EOF
?>
