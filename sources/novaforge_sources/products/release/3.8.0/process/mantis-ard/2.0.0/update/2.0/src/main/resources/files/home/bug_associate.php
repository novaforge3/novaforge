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
 

	$f_bug_id = gpc_get_int( 'bug_id', 0 );
	$f_associate_bug_id = gpc_get_int( 'associate_bug_id', 0 );
	
	if ( $f_bug_id > 0 &&  $f_associate_bug_id > 0 ) {
		relationship_add( $f_bug_id, $f_associate_bug_id, 1 );
	}
	
	print_successful_redirect_to_bug( $f_bug_id );
	
?>
