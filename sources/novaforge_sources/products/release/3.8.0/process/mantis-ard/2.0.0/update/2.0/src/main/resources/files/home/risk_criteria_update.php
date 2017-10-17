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

form_security_validate( 'risk_criteria_update' );


$t_risk_criterias = risk_criteria_get_all();
if ( isset($_POST['delete']) ) {
	print_successful_redirect( 'risk_criteria_view.php?action=delete&id='.$_POST['id'] );	
}
elseif ( isset($_POST['confirm_delete']) ) {
	$risk_criteria = risk_criteria_get($_POST['id']);
	$risk_criteria->delete();
	form_security_purge( 'risk_criteria_update' );
	print_successful_redirect( 'risk_criteria_page.php');
}
#update weighting
if ( isset($_POST['update_weight']) ) {
	$risk_criteria_by_category = array();
	foreach ( $t_risk_criterias as $t_risk_criteria ) {
   		$f_weight = gpc_get_int( 'weight_'.$t_risk_criteria->id );
   		#each weight has to be a pourcentage
   		if( (0 > $f_weight) || (100 < $f_weight) ) {
			error_parameters( ' weighting '.$t_risk_criteria->label );
			trigger_error( ERROR_RISK_CRITERIA_WEIGHT_INVALID, ERROR );
		}
		#sum each weight by category
		if ( !in_array($t_risk_criteria->category_id, $risk_criteria_by_category) ) {
			$risk_criteria_by_category[$t_risk_criteria->category_id] += $f_weight;
		}
	}
	#check no weight sum is bigger than 100%
	foreach ( $risk_criteria_by_category as $category_id => $sum_weight ) {
       if ( $sum_weight != 100 ) {
			trigger_error( ERROR_RISK_CRITERIA_SUM_WEIGHT_INVALID, ERROR );
		}
	}
	foreach ( $t_risk_criterias as $t_risk_criteria ) {
   		$f_weight = gpc_get_int( 'weight_'.$t_risk_criteria->id );
   		if ( $t_risk_criteria->weight != $f_weight) {
			risk_criteria_update_attribute( $t_risk_criteria->id, 'weight', $f_weight );
		}
	}
	form_security_purge( 'risk_criteria_update' );
	print_successful_redirect( 'risk_criteria_page.php' );
}
#update risk criteria
else {
	$t_risk_criteria = new risk_criteria;
	$t_risk_criteria->id = gpc_get_string( 'id', 0 );
	$t_risk_criteria->label = gpc_get_string( 'label', '' );
	$t_risk_criteria->project_id = project_get_other_type_project_id('management');
	$t_risk_criteria->category_id = gpc_get_int( 'category_id', 0 );
	$t_risk_criteria->evaluation1 = gpc_get_string( 'evaluation1', '' );
	$t_risk_criteria->evaluation2 = gpc_get_string( 'evaluation2', '' );
	$t_risk_criteria->evaluation3 = gpc_get_string( 'evaluation3', '' );
	$t_risk_criteria->evaluation4 = gpc_get_string( 'evaluation4', '' );
	$t_risk_criteria->evaluation5 = gpc_get_string( 'evaluation5', '' );
	$t_risk_criteria->evaluation6 = gpc_get_string( 'evaluation6', '' );
	
	# Update the risk criteria
	$t_risk_criteria->update();	
	
	form_security_purge( 'risk_criteria_update' );
	print_successful_redirect( 'risk_criteria_page.php' );
}
?>
