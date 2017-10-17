<?php

/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
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

# return user_id if successful, otherwise "".
function check_login( $p_username, $p_password ) {
	//	$user_id = auth_identifier_login($p_username, $p_password,'');
	if (!$auteur = auth_identifier_login($p_username, $p_password, '')){
		return false;
	}
	return $auteur;
}

function has_administrator_access( $p_user_id ) {
	$statut = sql_getfetsel("statut", "spip_auteurs", "login=" . sql_quote($p_user_id));
	$webmestre = sql_getfetsel("webmestre", "spip_auteurs", "login=" . sql_quote($p_user_id));
	if ( (_SPIP_0minirezo==$statut) && (_SPIP_WEBMESTRE_OUI==$webmestre)){
		return true;
	}
	else {
		return false;
	}
}

function check_login_config_file( $p_username, $p_password ) {
	//	$user_id = auth_identifier_login($p_username, $p_password,'');
	if ( (_NF_LOGIN_ADMIN_SPIP == $p_username) && (_NF_PASS_ADMIN_SPIP == $p_password)){
		return true;
	}
	return false;
}

function has_administrator_access_config_file( $p_user_id ) {
	return true;
}

/**
 * Return true if the parameter is an empty string or a string
 * containing only whitespace, false otherwise
 * @param string $p_var string to test
 * @return bool
 * @access public
 */
function is_blank( $p_var ) {
	$p_var = trim( $p_var );
	$str_len = strlen( $p_var );
	if( 0 == $str_len ) {
		return true;
	}
	return false;
}
?>