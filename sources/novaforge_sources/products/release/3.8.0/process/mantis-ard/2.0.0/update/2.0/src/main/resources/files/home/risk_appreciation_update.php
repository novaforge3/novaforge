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

form_security_validate( 'risk_appreciation_update' );

$t_risk_criterias = risk_criteria_get_all();
$categories = category_risk_criteria_get_all();
$t_evaluation_by_categorie = array ();
foreach ($t_risk_criterias as $t_risk_criteria) {
	if (isset ($_POST[$t_risk_criteria->id . '_appreciation'])) {
		$t_evaluations = explode('_', $_POST[$t_risk_criteria->id . '_appreciation']);
		$t_evaluation = $t_evaluations[1];
		$t_evaluation_by_categorie[$t_risk_criteria->category_id] += $t_risk_criteria->weight * $t_evaluation / 100;
		;
		risk_criteria_update_attribute($t_risk_criteria->id, 'evaluation', $t_evaluation);
	}
}
foreach ($categories as $category) {
	if (null == $t_evaluation_by_categorie[$category['id']])
		$t_evaluation_by_categorie[$category['id']] = 0;
	risk_evaluation_create_history($category['id'], $t_evaluation_by_categorie[$category['id']]);
	switch ($category['name']) {
		case 'duration' :
			$weight = 1;
			break;
		case 'integrity' :
			$weight = 2;
			break;
		case 'management_commitment' :
			$weight = 2;
			break;
		case 'team_commitment' :
			$weight = 1;
			break;
		case 'effort' :
			$weight = 1;
			break;
		case 'others' :
			if ($t_evaluation_by_categorie[$category['id']] != 0) 
				$others = true;
			$weight = 1;
			break;
		default :
			break;
	}
	$global_risk += $weight * $t_evaluation_by_categorie[$category['id']];
}
if ($others)
	$global_risk *= 7 / 8;
$last_risk_evaluation = risk_evaluation_get_last(0);
risk_evaluation_create_history(0, $global_risk);

form_security_purge( 'risk_appreciation_update' );

print_successful_redirect('risk_appreciation_page.php');
?>
