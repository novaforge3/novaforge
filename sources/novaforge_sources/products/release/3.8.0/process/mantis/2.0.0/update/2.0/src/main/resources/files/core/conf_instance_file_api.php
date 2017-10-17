<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2007-2009 BULL SAS
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
 * Return the instance file containt
 * @return array
 */
function get_instance_file() {
	//TODO make it work !
	$t_project_id = helper_get_current_project();
	return file("conf-instance/config_status_$t_project_id.php");
}

 /**
 * Return the line of the indice in parameter from the specified file delimited if indicated
 * @param array $p_file
 * @param string $p_indice
 * @return int $line, false if line is not found
 */
function get_line_in_file(&$p_file, $p_indice) {	
	$t_line = false;
	
	for($i=0; $i<count($p_file); $i++) {
		# get the line where $p_indice match
		if( 1 == preg_match("/$p_indice/i", $p_file[$i]) ) $t_line = $i;
	}
	
	return $t_line;
}

/**
 * Insert a new element in an array at the specified indice
 * @param array $p_array
 * @param string $p_element
 * @param string $p_indice
 * @return array $array_tmp
 */
function insert_in_file(&$p_array, &$p_element, &$p_indice) {
	$array_tmp = array() ;
    $indice_tmp = 0 ;
 
    while($indice_tmp != $p_indice)
    {
        array_push($array_tmp,$p_array[$indice_tmp]);
        $indice_tmp++ ;
    }
 
    array_push($array_tmp,$p_element);
 
    while($indice_tmp != (count($p_array)))
    {
        array_push($array_tmp,$p_array[$indice_tmp]);
        $indice_tmp++ ;
    }
 
    return $array_tmp ;
}

/**
 * Remove a case in an array at the specified indice
 * @param array $p_array
 * @param string $p_indice
 * @return array $p_array
 */
function remove_in_file(&$p_array, &$p_indice) {	
	unset($p_array[$p_indice]);
	return array_values($p_array);	
}

/**
 * Write in the file specified the array specified
 * @param string $p_file
 * @param array $p_indice
 */
function write_file($p_file, &$p_array) {
	$file = fopen($p_file, 'w'); 
	for ($i=0; $i<count($p_array); $i++) {
		fputs ($file, $p_array[$i]);
	}
	fclose($file); 
}
?>
