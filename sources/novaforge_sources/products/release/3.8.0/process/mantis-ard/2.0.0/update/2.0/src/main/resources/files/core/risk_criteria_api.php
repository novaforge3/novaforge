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
* @package MantisBT
* @subpackage classes
*/
class risk_criteria {
	protected $id;
	protected $label = '';
	protected $weight = 0;
	/* the project id has to be the parent of the current project, the management one */
	protected $project_id = 0;
	protected $category_id = 0;
	protected $evaluation = null;
	protected $evaluation1 = '';
	protected $evaluation2 = '';
	protected $evaluation3 = '';
	protected $evaluation4 = '';
	protected $evaluation5 = '';
	protected $evaluation6 = '';

	public function __set($name, $value) {
		$this-> $name = $value;
	}

	public function __get($name) {
		return $this-> {
			$name };
	}

	private function validate($p_update = false) {
		# Category cannot be blank
		if (0 == $this->category_id) {
			error_parameters(lang_get('category'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		if (0 == $this->project_id) {
			error_parameters(lang_get('project'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		# Label cannot be blank
		if (is_blank($this->label)) {
			error_parameters(lang_get('label'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		if (!$p_update) {
			# Label has to be unique
			if (!risk_criteria_is_label_unique($this->label)) {
				error_parameters(lang_get('label'));
				trigger_error(ERROR_RISK_CRITERIA_DUPLICATE_LABEL, ERROR);
			}
		} else {
			# Label has to be unique
			if ($t_risk_criteria_name = risk_criteria_get($this->id)->label != $this->label) {
				if (!risk_criteria_is_label_unique($this->label)) {
					error_parameters(lang_get('label'));
					trigger_error(ERROR_RISK_CRITERIA_DUPLICATE_LABEL, ERROR);
				}
			}
			# Id has  to be gave
			if (0 == $this->id) {
				error_parameters(lang_get('id'));
				trigger_error(ERROR_GENERIC, ERROR);
			}
		}
		# Evaluation label 1 cannot be blank
		if (is_blank($this->evaluation1)) {
			error_parameters(lang_get('evaluation1'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		# Evaluation label 2 cannot be blank
		if (is_blank($this->evaluation2)) {
			error_parameters(lang_get('evaluation2'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		# Evaluation label 3 cannot be blank
		if (is_blank($this->evaluation3)) {
			error_parameters(lang_get('evaluation3'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		# Evaluation label 4 cannot be blank
		if (is_blank($this->evaluation4)) {
			error_parameters(lang_get('evaluation4'));
			trigger_error(ERROR_EMPTY_FIELD, ERROR);
		}
		# Evaluation label 5 cannot be blank if 6 is not
		if (!is_blank($this->evaluation6) && is_blank($this->evaluation5)) {;
			error_parameters(lang_get('evaluation5'));
			trigger_error(ERROR_RISK_CRITERIA_LABEL, ERROR);
		}
	}

	/**
	 * Insert a new risk criteria into the db
	 * @return int which is the risk criteria id that was created
	 * @access public
	 * @uses database_api.php
	 * @uses lang_api.php
	 */
	function create() {
		self :: validate();
		$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

		$query = "INSERT INTO $t_risk_criteria_table
									( label, weight, project_id, category_id, evaluation1, 
									evaluation2, evaluation3, evaluation4, evaluation5, evaluation6
									)
									VALUES
									    ( " . db_param() . ',' . db_param() . ',' . db_param() . ',' . db_param() . ',' . db_param() . ",
									    " . db_param() . ',' . db_param() . ',' . db_param() . ',' . db_param() . ',' . db_param() . ')';

		db_query_bound($query, Array (
			$this->label,
			$this->weight,
			$this->project_id,
			$this->category_id,
			$this->evaluation1,
			$this->evaluation2,
			$this->evaluation3,
			$this->evaluation4,
			$this->evaluation5,
			$this->evaluation6
		));

		$this->id = db_insert_id($t_risk_criteria_table);

		return $this->id;

	}

	function update() {
		self :: validate(true);
		$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

		$query = "UPDATE $t_risk_criteria_table
									SET label = " . db_param() . ",
									category_id = " . db_param() . ",
									evaluation1 = " . db_param() . ",
									evaluation2 = " . db_param() . ",
									evaluation3 = " . db_param() . ",
									evaluation4 = " . db_param() . ",
									evaluation5 = " . db_param() . ",
									evaluation6 = " . db_param() . "
									WHERE id=" . db_param();

		db_query_bound($query, Array (
			$this->label,
			$this->category_id,
			$this->evaluation1,
			$this->evaluation2,
			$this->evaluation3,
			$this->evaluation4,
			$this->evaluation5,
			$this->evaluation6,
			$this->id
		));

		return true;
	}

	function delete() {
		$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

		$query = "DELETE FROM $t_risk_criteria_table
									WHERE id=" . db_param();

		db_query_bound($query, Array (
			$this->id
		));

		return true;
	}

}

///**
// * This class represent a label of a risk criteria
// */
//class risk_criteria_label {
//	protected $id;
//	protected $label = '';
//	
//	public function __set($name, $value) {
//		$this-> $name = $value;
//	}
//
//	public function __get($name) {
//		return $this-> {
//			$name };
//	}
//	
//	private function validate() {
//		# Label cannot be blank
//		if (is_blank($this->label)) {
//			error_parameters(lang_get('label'));
//			trigger_error(ERROR_EMPTY_FIELD, ERROR);
//		}
//	}
//	
//	
//	/**
//	 * Create a new label for a risk criteria
//	 */
//	function create() {
//		self :: validate();
//		$t_risk_criteria_label_table = db_get_table('mantis_risk_criteria_label_table');
//
//		$query = "INSERT INTO $t_risk_criteria_label_table
//									( label )
//									VALUES
//									    ( " . db_param() . ')';
//
//		db_query_bound($query, Array (
//			$this->label
//		));
//
//		$this->id = db_insert_id($t_risk_criteria_label_table);
//
//		return $this->id;
//	}
//	
//	function update() {
//		self :: validate(true);
//		$t_risk_criteria_label_table = db_get_table('mantis_risk_criteria_label_table');
//
//		$query = "UPDATE $t_risk_criteria_label_table
//									SET label = " . db_param() . ",
//									WHERE id=" . db_param();
//
//		db_query_bound($query, Array (
//			$this->label,
//			$this->id
//		));
//
//		return true;
//	}
//	
//	function delete() {
//		$t_risk_criteria_label_table = db_get_table('mantis_risk_criteria_label_table');
//
//		$query = "DELETE FROM $t_risk_criteria_label_table
//									WHERE id=" . db_param();
//
//		db_query_bound($query, Array (
//			$this->id
//		));
//
//		return true;
//	}
//}


# check to see if risk criteria exists by label
function risk_criteria_is_label_unique($p_label ) {
    $t_project_id = project_get_other_type_project_id('management');
	$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

	$query = "SELECT COUNT(*)
						 FROM $t_risk_criteria_table
						 WHERE label =" . db_param(). "
						 AND project_id = " . db_param() ;
						 				
	$result = db_query_bound($query, Array ($p_label, $t_project_id ));
	
	if (0 == db_result($result)) {
		return true;
	} else {
		return false;
	}
}

function risk_criteria_get_id_by_name($p_risk_criteria_name) {
    $t_project_id = project_get_other_type_project_id('management');
	$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

	$query = "SELECT id 
					FROM $t_risk_criteria_table 
					WHERE label = " . db_param() ."
					AND project_id = " . db_param() ;
					
	$t_result = db_query_bound($query, Array ($p_risk_criteria_name, $t_project_id ), 1);

	if (db_num_rows($t_result) == 0) {
		return 0;
	} else {
		return db_result($t_result);
	}
}

function risk_criteria_get($p_risk_criteria_id) {
	$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

	$query = "SELECT * 
			 			FROM $t_risk_criteria_table
			 			WHERE id= " . db_param();
	$result = db_query_bound($query, Array (
		$p_risk_criteria_id
	));

	$row = db_fetch_array($result);

	return to_risk_criteria_object($row);

}
function risk_criteria_get_all() {
	$t_project_id = project_get_other_type_project_id('management');
	$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

	$query = "SELECT * 
			 			FROM $t_risk_criteria_table
			 			WHERE project_id= " . db_param();
	
	$result = db_query_bound($query, Array ($t_project_id));

	$row_count = db_num_rows($result);
		
	$t_risk_criterias = array ();

	for ($i = 0; $i < $row_count; $i++) {
	    
		$row = db_fetch_array($result);
		$t_risk_criterias[] = to_risk_criteria_object($row);
	}
	
	return $t_risk_criterias;

}

function to_risk_criteria_object($p_row) {
	$t_risk_criteria = new risk_criteria;
	$t_risk_criteria->id = $p_row['id'];
	$t_risk_criteria->project_id = $p_row['project_id'];
	$t_risk_criteria->category_id = $p_row['category_id'];
	$t_risk_criteria->label = $p_row['label'];
	$t_risk_criteria->weight = $p_row['weight'];
	$t_risk_criteria->evaluation = $p_row['evaluation'];
	$t_risk_criteria->evaluation1 = $p_row['evaluation1'];
	$t_risk_criteria->evaluation2 = $p_row['evaluation2'];
	$t_risk_criteria->evaluation3 = $p_row['evaluation3'];
	$t_risk_criteria->evaluation4 = $p_row['evaluation4'];
	$t_risk_criteria->evaluation5 = $p_row['evaluation5'];
	$t_risk_criteria->evaluation6 = $p_row['evaluation6'];
	return $t_risk_criteria;
}
function bug_to_risk_criteria_object($p_bug) {
	$t_risk_criteria = new risk_criteria;
	$t_risk_criteria->id = null;
	$t_risk_criteria->project_id = project_get_other_type_project_id('management');
	$t_risk_criteria->category_id = category_get_id_by_name('others', -1);
	$t_risk_criteria->label = $p_bug->summary;
	$t_risk_criteria->weight = 0;
	$t_risk_criteria->evaluation = null;
	$t_risk_criteria->evaluation1 = "1";
	$t_risk_criteria->evaluation2 = "2";
	$t_risk_criteria->evaluation3 = "3";
	$t_risk_criteria->evaluation4 = "4";
	$t_risk_criteria->evaluation5 = "5";
	$t_risk_criteria->evaluation6 = "6";
	return $t_risk_criteria;
}

function risk_criteria_update_attribute($p_risk_criteria_id, $p_attribute, $p_weight) {
	$t_risk_criteria_table = db_get_table('mantis_risk_criteria_table');

	$query = "UPDATE $t_risk_criteria_table
			 		SET $p_attribute = " . db_param() .
					" WHERE id = " . db_param();

	$params[] = (int) $p_weight;
	$params[] = (int) $p_risk_criteria_id;
	$result = db_query_bound($query, $params);
}

/**
 * Create a risk_criteria evaluation. 
 * If an evaluation already exists for a given day, it is update, otherwise, a new one is create.
 * Return true if update is ok, return the last inserted id if create is ok
 */
function risk_evaluation_create_history($p_category_id, $p_evaluation) {
	$t_risk_evaluation_history_table = db_get_table('mantis_risk_evaluation_history_table');
	$last_risk_evaluation = risk_evaluation_get_last($p_category_id);
	$t_date = date('Y-m-d');
	$t_project_id = project_get_other_type_project_id('management');
	//update
	if ($last_risk_evaluation['date_modified'] == $t_date) {
		$query = "UPDATE $t_risk_evaluation_history_table " .
					" SET evaluation = " . db_param() .
					" WHERE project_id = " . db_param() .
					" AND category_id = " . db_param() .
					" AND date_modified = " . db_param();
		db_query_bound($query, Array (
			$p_evaluation,
			$t_project_id,
			$p_category_id,
			$t_date
		));
		return true;
	} 
	//	create
	else {
		$query = "INSERT INTO $t_risk_evaluation_history_table
					( project_id, category_id, evaluation, date_modified
					)
					VALUES
					( " . db_param() . ',' . db_param() . ',' . db_param() . ',' . db_param() . ')';
		db_query_bound($query, Array (
			$t_project_id,
			$p_category_id,
			$p_evaluation,
			$t_date
		));
		
		return db_insert_id($t_risk_evaluation_history_table);

	}
}

/**
 * Return the previous evaluation (previous date)
 */
function risk_evaluation_get_previous($p_category_id) {
	$t_risk_evaluation_history_table = db_get_table('mantis_risk_evaluation_history_table');
	$t_project_id = project_get_other_type_project_id('management');
	
	$query = "SELECT * FROM $t_risk_evaluation_history_table
					WHERE project_id = " . db_param() . "
					AND category_id = " . db_param() . "
					ORDER BY date_modified DESC";
	
	$result = db_query_bound($query, Array (
		$t_project_id,
		$p_category_id
	));
	
	$row_count = db_num_rows($result);
	
	for ($i = 0; $i < $row_count; $i++) {
		$previous_risk_evaluation = db_fetch_array($result);
		if ( $previous_risk_evaluation['date_modified'] != date('Y-m-d') ) {
			$previous_risk_evaluations[] = $previous_risk_evaluation;
		}
	}
	if ( $previous_risk_evaluations != null ) {
		uasort($previous_risk_evaluations, 'date_cmp');
		return $previous_risk_evaluations[0];
	}
	else {
		return null;
	}
}

/**
 * Compares two risk_evaluation object's date attribute
 */
function date_cmp($a, $b) {
	$date_a = DateTime::createFromFormat('Y-m-d', $a['date_modified'])->format('ymd');
	$date_b = DateTime::createFromFormat('Y-m-d', $b['date_modified'])->format('ymd');
	if ($date_a == $date_b) {
        return 0;
    }
    return ($date_a < $date_b) ? -1 : 1;
}
/**
 * Return the last evaluation saved (whatever the date)
 */
function risk_evaluation_get_last($p_category_id) {
	$t_risk_evaluation_history_table = db_get_table('mantis_risk_evaluation_history_table');
	$t_project_id = project_get_other_type_project_id('management');
	
	$query = "SELECT * FROM $t_risk_evaluation_history_table
					WHERE project_id = " . db_param() . "
					AND category_id = " . db_param() . "
					ORDER BY date_modified DESC";
	
	$result = db_query_bound($query, Array (
		$t_project_id,
		$p_category_id
	));

	$row_count = db_num_rows($result);

	for ($i = 0; $i < $row_count; $i++) {
		$last_risk_evaluation = db_fetch_array($result);
		break;
	}

	return $last_risk_evaluation;
}
?>
