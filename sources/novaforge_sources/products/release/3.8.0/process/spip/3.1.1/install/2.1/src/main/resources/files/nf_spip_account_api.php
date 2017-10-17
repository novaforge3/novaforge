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

function add_user($username, $password, $userData){

	//	require_once('action/editer_auteur.php');
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	$err = '';
	$nom = $userData['nom'];
	$email=$userData['email'];
	$nom_site='';
	$url_site='';
	$inst1_user = array('nom' => $nom, 'email' => $email, 'nom_site' => $nom_site, 'url_site' => $url_site);
	$login=$userData['login'];
	$pass=$userData['pass'];
	//	$webmestre=$userData['webmestre'];
	// Force to non at user creation. Give this property when adding the user the site
	$webmestre=_SPIP_WEBMESTRE_NON;
	// Force to 5poubelle at user creation. Give role when adding the user the site
	//	$statut=$userData['statut'];
	$statut=_SPIP_5poubelle;
	$inst2_user = array('login' => $login, 'pass' => $pass, 'webmestre' => $webmestre, 'statut' => $statut);

	//1st step : insert line for an auteur into spip_autheurs
	$id_auteur = insert_auteur();

	//2nd step : update the line with user inputs
	if ($id_auteur > 0){
		$err = nf_auteurs_set($id_auteur, $inst1_user, $inst2_user);
	}
	if ($err==''){
		spip_log("userID = " . $id_auteur. " has been added with connect dir= " . _DIR_CONNECT);
		return $id_auteur;
	}
	else {
		return new soap_fault( 'Client', '', 'Add user Error', 'Error when adding user');
	}
}

//return the list of statuts
function get_roles(){
	$roles = array(_NF_ADMINISTRATOR, _NF_WEBMASTER, _NF_AUTHOR, _NF_VISITOR);
	return $roles;
}

function get_user_info($username, $password, $login)
{
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	//Problem met to get right result with sql_allfetsel; use sql_getfetsel.
	//	$result = sql_allfetsel("id_auteur,nom,login", "spip_auteurs", "login=" . sql_quote($login));
	$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));
	$nom = sql_getfetsel("nom", "spip_auteurs", "login=" . sql_quote($login));
	$login = sql_getfetsel("login", "spip_auteurs", "login=" . sql_quote($login));
	$email = sql_getfetsel("email", "spip_auteurs", "login=" . sql_quote($login));
	$webmestre = sql_getfetsel("webmestre", "spip_auteurs", "login=" . sql_quote($login));
	$statut = sql_getfetsel("statut", "spip_auteurs", "login=" . sql_quote($login));
	$nom_site = sql_getfetsel("nom_site", "spip_auteurs", "login=" . sql_quote($login));
	$url_site = sql_getfetsel("url_site", "spip_auteurs", "login=" . sql_quote($login));

	//	while ($row = sql_fetch($result)) {
	//$nom = $row["nom"];
	//		$id= $row["id_auteur"];
	//	}
	$user_info = array("nom" => $nom, "id" => $id_auteur, "login" => $login, "email" => $email, "webmestre" => $webmestre, "statut" => $statut, "nom_site" => $nom_site, "url_site" => $url_site);
	return $user_info;
}

function get_user_id($username, $password, $login)
{
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}
	$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));
	return $id_auteur;
}

function delete_user($username, $password, $login=''){
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	//Warning: only put statut = 5poubelle. The user is not deleted from database.
	// Then a plugin exists to delete objects with 5poubelle status.
	//	require_once 'action/editer_auteur.php';
	$statut=_SPIP_5poubelle;
	$user_delete = array(
	'login' => $login, 
	'statut' => $statut);
	$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));
	$err = auteurs_set($id_auteur, $user_delete);
	if ($err==''){
		spip_log("userID = " . $id_auteur. " has been deleted with connect dir= " . _DIR_CONNECT);
		return true;
	}
	else {
		return new soap_fault( 'Client', '', 'Delete user Error', 'Error when deleting user');
	}
}

//permet de modifier tous les parametres donnés ds le tableau $user (y compris le login)
function update_user($username, $password, $login='', $user){
	//	require_once 'action/editer_auteur.php';
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));
	$err = auteurs_set($id_auteur, $user);
	if ($err==''){
		spip_log("userID = " . $id_auteur. " has been updated with connect dir= " . _DIR_CONNECT);
		return true;
	}
	else {
		return new soap_fault( 'Client', '', 'Update user Error', 'Error when updating user');
	}
}


//***********************************************************************************************************
//                                      utilities functions
//***********************************************************************************************************
//modified against auteurs_set() from editer_auteur()
function nf_auteurs_set($id_auteur, $set1 = null, $set2 = null) {
	require_once('inc/modifier.php');

	$r = modifier_contenu('auteur', $id_auteur,
	array(
			'nonvide' => array('nom' => _T('ecrire:item_nouvel_auteur'))
	),
	$set1);

	// .. mettre a jour les fichiers .htpasswd et .htpasswd-admin
	if (isset($c['login'])
	OR isset($c['pass'])
	OR isset($c['statut'])
	) {
		include_spip('inc/acces');
		ecrire_acces();
	}

	// .. mettre a jour les sessions de cet auteur
	include_spip('inc/session');
	$c['id_auteur'] = $id_auteur;
	actualiser_sessions($c);

	// Modification de statut, changement de rubrique ?
	$err .= instituer_auteur($id_auteur, $set2);
	return $err;
}
?>