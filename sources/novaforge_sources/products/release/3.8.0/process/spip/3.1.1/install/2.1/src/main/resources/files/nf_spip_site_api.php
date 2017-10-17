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

//Not used function .....
function get_site($username, $password){
	require_once('inc/presentation.php');
	require_once('inc/config.php');

	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	$url_site = entites_html($GLOBALS['meta']["url_site"]);
	$nom_site = entites_html($GLOBALS['meta']["nom_site"]);
	$email_webmaster = entites_html($GLOBALS['meta']["email_webmaster"]);
	$descriptif_site = entites_html($GLOBALS['meta']["descriptif_site"]);
	//	error_log("url_site = " . $url_site . " nom_site = " . $nom_site . " email_webmaster = ". $email_webmaster . "descriptif_site = " . $descriptif_site);
	$site_info=array("url_site" => $url_site, "nom_site" => $nom_site, "email_webmaster" => $email_webmaster, "descriptif_site" => $descriptif_site);
	return $site_info;
}

//not used function ....
function update_site($username, $password, $site_input){
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	if ( is_blank($site_input['site_id']) ) {
		return new soap_fault( 'Client', '', 'Spip site creation error', 'The site id is blank');
	}
	//buid site name with the site_id (which is the sub domain)
	//	$host_site = $site_input['site_id'] . "." ._SPIP_DOMAIN;
	$host_site = $_SERVER['HTTP_HOST'];

	$site_nom=$site_input['nom_site'];
	$site_description=$site_input['descriptif_site'];
	$adresse_site="http://" . $host_site . "/spip";
	ecrire_meta('nom_site', $site_nom);
	ecrire_meta('descriptif_site', $site_description);
	ecrire_meta('adresse_site', $adresse_site);
	return true;
}

//in order to change the default statut set at user creation (5poubelle) to one of possible statut:"1comite" , "0minirezo".
function add_user_site($username, $password, $login='', $siteId, $role ){
	//	require_once 'action/editer_auteur.php';
	//Note:
	//  $siteId is not used when using initial spip function (here after).
	//  Spip Mechanics is using  _DIR_CONNECT  (ex. ../sites/spip2.localhost/config/)
	//  which is initialized into mes_options.php (depends on: $site = $_SERVER['HTTP_HOST'];)
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));

	switch ($role){
		case _NF_ADMINISTRATOR:
			$user_role = array(
	'webmestre' => _SPIP_WEBMESTRE_NON,
	'statut' => _SPIP_0minirezo);
			break;
		case _NF_WEBMASTER:
			$user_role = array(
	'webmestre' => _SPIP_WEBMESTRE_OUI,
	'statut' => _SPIP_0minirezo);
			break;
		case _NF_AUTHOR:
			$user_role = array(
	'webmestre' => _SPIP_WEBMESTRE_NON,
	'statut' => _SPIP_1comite);
			break;
		case _NF_VISITOR:
			$user_role = array(
	'webmestre' => _SPIP_WEBMESTRE_NON,
	'statut' => _SPIP_6forum);
			break;
	}

	//pb. with this function to set webmestre to: oui (because of autorisation .....)
	//$err = auteurs_set($id_auteur, $user_role);
	sql_updateq('spip_auteurs', $user_role , 'id_auteur='.$id_auteur);
	if ($err==''){
		spip_log("userID = " . $id_auteur. " has been added to site with siteId = " . $siteId .  ", role = " .  $role . ", connect dir= " . _DIR_CONNECT);
		return $id_auteur;
	}
	else {
		return new soap_fault( 'Client', '', 'Add user to site error', 'Error when adding user= ' . $login . ' to site=' . $siteId );
	}
}

function delete_site($username, $password, $siteId){
	//TODO: refactor (?) the implementation. Currently, tables are dropped but database instance still exists. (Not a pb. to redo the creation).
	// But the root dorectory for the config is not removed. Only config file. The root dir under sites needs to be removed manually.

	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination was incorrect');
	}

	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	if ( is_blank($siteId) ) {
		return new soap_fault( 'Client', '', 'Spip site delete error', 'The site id is blank');
	}

	$delete= array('spip_articles','spip_auteurs','spip_auteurs_articles','spip_auteurs_messages','spip_auteurs_rubriques','spip_breves','spip_documents','spip_documents_liens','spip_forum','spip_groupes_mots','spip_messages','spip_meta','spip_mots','spip_mots_articles','spip_mots_breves','spip_mots_documents','spip_mots_forum','spip_mots_rubriques','spip_mots_syndic','spip_petitions','spip_referers','spip_referers_articles','spip_resultats','spip_rubriques','spip_signatures','spip_syndic','spip_syndic_articles','spip_types_documents','spip_urls','spip_versions','spip_versions_fragments','spip_visites' ,'spip_visites_articles');

	//call patched spip function that delete tables and configuration files.
	//Note:
	//  $siteId is not used when using initial spip function (here after).
	//  Spip Mechanics is using  _DIR_CONNECT  (ex. ../sites/projet1_spip2/config/)
	//  which is initialized into mes_options.php

	nf_base_delete_all($delete, $siteId);
	return true;
}


function create_site($username, $password, $site_input=array()){
	//this variable setting forces variable: $serveur to 'mysql' into : abstract_sql.php
	//Note: abstract_sql.php needs to be patched
	$GLOBALS['novaforge_force_serveur']="novaforge_force_serveur";

	require_once('inc/minipres.php');
	require_once('inc/install.php');
	require_once('inc/autoriser.php');

	define('_ECRIRE_INSTALL', "1");
	define('_FILE_TMP', '_install');
	if (!$auteur = check_login_config_file($username, $password)){
		return new soap_fault( 'Client', '', 'Access Denied', 'Username/password combination does not match one into the configuration file: nf_config_inc.php');
	}
	if ( !has_administrator_access_config_file( $auteur.login ) ) {
		return new soap_fault( 'Client', '', 'Access Denied', 'User does not have administrator access');
	}

	if ( is_blank($site_input['site_id']) ) {
		return new soap_fault( 'Client', '', 'Spip site creation error', 'The site id is blank');
	}
	$deja = (_FILE_CONNECT AND analyse_fichier_connection(_FILE_CONNECT));
	if ($deja) {
		// Rien a faire ici
		error_log("ERROR lors de l'installation du site = " . $site_input[site_id] .". le fichier de connection: " . _FILE_CONNECT . "existe déjà");
		return new soap_fault( 'Client', '', 'Spip site creation error', 'The connextion file for the new site: already exists');
	} else {
		require_once('base/create.php');
		utiliser_langue_visiteur();
	}

	//create repository structure for site installation (au lieu de etape_1)
	require_once('install/etape_3.php');



	//buid site name with the site_id (which is the sub domain)
	//	$host_site = $site_input['site_id'] . "." ._SPIP_DOMAIN;
	$host_site = $_SERVER['HTTP_HOST'];
	preg_match(',/spip/([a-zA-Z0-9_-]+)/?,',$_SERVER['REQUEST_URI'],$r);
	//	error_log("******* nf_spip_site_api.php ************ _SERVER['REQUEST_URI'] =". $_SERVER['REQUEST_URI']);
	//	error_log("******* nf_spip_site_api.php ************ r[0] =". $r[0]);
	//	error_log("******** nf_spip_site_api.php *********** r[1] =". $r[1]);
	$site_name=$r[1];

	//call creation of configuration file function.
	create_install_directories($site_name);
	//create database  (etape_3)
	$port_db=_NF_PORT_DB;
	$login_db=_NF_LOGIN_DB;
	$pass_db=_NF_PASS_DB;



	//build the Db shema name
	$sel_db=_NF_SAFRAN.$site_input['site_id'];

	$adresse_db=$host_site.":".$port_db;
	$server_db=_NF_SERVER_DB;
	$choix_db=_NF_CHOIX_DB;
	$chmod_db=_NF_CHMOD_DB;


	//debug
	//error_log("BEGIN create_base with: " . $adresse_db."- ". $login_db."- ". $pass_db. "- ". $server_db."- ". $choix_db. "- ".$sel_db."- ". $chmod_db);
	create_base($adresse_db, $login_db, $pass_db,  $server_db, $choix_db, $sel_db, $chmod_db);
	//webmaster infos (etape_3b)
	//$login = $initialisation_data['login'];
	$login = _NF_LOGIN_ADMIN_SPIP;
	//$email = $initialisation_data['email'];
	$email = _NF_EMAIL_ADMIN_SPIP;
	//$nom = $initialisation_data['nom'];
	$nom = _NF_NOM_ADMIN_SPIP;
	//$pass = $initialisation_data['pass'];
	$pass = _NF_PASS_ADMIN_SPIP;
	//$pass_verif = $initialisation_data['pass_verif'];
	$pass_verif = _NF_PASS_ADMIN_SPIP;

	$site_nom=$site_input['nom_site'];
	$site_description=$site_input['descriptif_site'];

	base_initialisation($login,$email,$nom,$pass,$pass_verif,$server_db, $adresse_db, $login_db, $pass_db, $server_db, $sel_db, $choix_db, $sel_db, $site_nom, $site_description);

	install_etape_4_dist();

	include_spip('inc/headers');

	//add this function to rename some files ....
	//        and test it .....
	require_once('install/etape_fin.php');
	install_etape_fin_dist();

	//patch novaforge: unset the used global variables.
	unset($GLOBALS['novaforge_site_install']);
	unset($GLOBALS['novaforge_force_serveur']);
	
	include_spip('base/upgrade');
	base_upgrade_dist();
	
	// add a slash at the end for the url 
	ecrire_meta('adresse_site', url_de_base());

	//return the site_id which will map the project id of novaforge
	return $site_name;
}

//***********************************************************************************************************
//                                      utilities functions
//***********************************************************************************************************

function create_install_directories($site='') {
	$chmod = 0755;
	//to keep the initial path
	$current_dir=getcwd();
	spip_log("create_install_directories current_dir: ".$current_dir);
	@chdir("../sites");
	$ok = @mkdir($site);
	if (!$ok) {
		error_log("ERROR creating the root directory " . $site . " under sites.");
		return new soap_fault( 'Client', '', 'Spip site creation error', 'Error when creating the root directory: ' . $site . ' under sites.');
		exit;
	}

	@chmod($site, $chmod);
	$ok= @chdir($site);
	$dirs=array('IMG', 'tmp', 'local', 'config');
	foreach ($dirs as &$my_dir) {
		$ok= @mkdir($my_dir);
		if ($ok) {
			@chmod($my_dir, $chmod);
		} else {
			error_log("ERROR creating " .$my_dir . " directory under sites: " . $site );
			return new soap_fault( 'Client', '', 'Spip site creation error', 'Error when creating the directory: ' . $my_dir . ' under' . $site . '.');
			exit;
		}

	}
	//to set back initial path
	@chdir($current_dir);
	return $ok;
}


//(etape3b)
function base_initialisation($login='',$email='',$nom='',$pass='',$pass_verif='',$server_db='', $adresse_db='', $login_db='', $pass_db='', $server_db='', $sel_db='', $choix_db='', $sel_db='', $site_nom='', $site_description='')
{
	global $spip_version_base;
	define('_ESPACE_PRIVE', true);

	if($login) {
		$echec = ($pass!=$pass_verif) ?
		_T('info_passes_identiques')
		: ((strlen($pass)<6) ?
		_T('info_passe_trop_court')
		: ((strlen($login)<3) ?
		_T('info_login_trop_court')
		: ''));
		if ($echec) {
			error_log('Incorrect login or password for the admin when initializing the database after instance creation.');
			return new soap_fault( 'Client', '', 'Spip site creation error', 'Incorrect login or password for the admin when initializing the database after instance creation.') ;
		}
	}

	if (@file_exists(_FILE_CHMOD_TMP)){
		require_once (_FILE_CHMOD_TMP);
	}
	else{
		error_log("_FILE_CHMOD_TMP = " . _FILE_CHMOD_TMP . "does not exist. Before launching again the site creation, remove created site directory and database instance." );
		spip_log("_FILE_CHMOD_TMP = " . _FILE_CHMOD_TMP . "does not exist. " );
		return new soap_fault( 'Client', '', 'Spip site creation error', '_FILE_CHMOD_TMP = ' . _FILE_CHMOD_TMP . ' does not exist.' );
	}

	if (!@file_exists(_FILE_CONNECT_TMP)){
		spip_log("_FILE_CONNECT_TMP = " . _FILE_CONNECT_TMP . "does not exist. Before launching again the site creation, remove created site directory and database instance." );
		error_log("_FILE_CONNECT_TMP = " . _FILE_CONNECT_TMP . "does not exist. Before launching again the site creation, remove created site directory and database instance." );
		return new soap_fault( 'Client', '', 'Spip site creation error', '_FILE_CONNECT_TMP = ' . _FILE_CONNECT_TMP . ' does not exist.' );
		//redirige_url_ecrire('install');
	}

	# maintenant on connait le vrai charset du site s'il est deja configure
	# sinon par defaut lire_meta reglera _DEFAULT_CHARSET
	# (les donnees arrivent de toute facon postees en _DEFAULT_CHARSET)

	lire_metas();
	if ($login) {
		include_spip('inc/charsets');
		//require_once('inc/charsets.php');

		$nom = (importer_charset($nom, _DEFAULT_CHARSET));
		$login = (importer_charset($login, _DEFAULT_CHARSET));
		$email = (importer_charset($email, _DEFAULT_CHARSET));
		# pour le passwd, bizarrement il faut le convertir comme s'il avait
		# ete tape en iso-8859-1 ; car c'est en fait ce que voit md5.js
		$pass = unicode2charset(utf_8_to_unicode($pass), 'iso-8859-1');
		include_spip('auth/sha256.inc');
		include_spip('inc/acces');
		$htpass = generer_htpass($pass);
		$alea_actuel = creer_uniqid();
		$alea_futur = creer_uniqid();
		$shapass = _nano_sha256($alea_actuel.$pass);
		// prelablement, creer le champ webmestre si il n'existe pas (install neuve
		// sur une vieille base
		$t = sql_showtable("spip_auteurs", true);
		if (!isset($t['field']['webmestre'])){
			@sql_alter("TABLE spip_auteurs ADD webmestre varchar(3)  DEFAULT 'non' NOT NULL",'mysql');
			//echo 'sql alter done';
		}

		$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));

		if ($id_auteur !== NULL) {
			sql_updateq('spip_auteurs', array("nom"=> $nom, 'email'=> $email, 'login'=>$login, 'pass'=>$shapass, 'alea_actuel'=>$alea_actuel, 'alea_futur'=> $alea_futur, 'htpass'=>$htpass, 'statut'=>'0minirezo'), "id_auteur=$id_auteur");
		}
		else {
			$id_auteur = sql_insertq('spip_auteurs', array(
				'nom' => $nom,
				'email' => $email,
				'login' => $login,
				'pass' => $shapass,
				'htpass' => $htpass,
				'alea_actuel' => $alea_actuel,
				'alea_futur' => $alea_futur,
				'statut' =>'0minirezo'),array());
		}

		//nf patch: check the user has been created
		$id_auteur = sql_getfetsel("id_auteur", "spip_auteurs", "login=" . sql_quote($login));

		if ($id_auteur == NULL) {
			error_log("Error: adding admin user into database has failed. Installation is not OK.");
			return new soap_fault( 'Client', '', 'Spip site creation error', 'Adding admin user into database has failed. Installation is not OK.' );
		}

		// le passer webmestre separrement du reste, au cas ou l'alter n'aurait pas fonctionne
		@sql_updateq('spip_auteurs', array('webmestre' => 'oui'), "id_auteur=$id_auteur");

		// inserer email comme email webmaster principal
		// (sauf s'il est vide: cas de la re-installation)
		if ($email)
		ecrire_meta('email_webmaster', $email);
		// Connecter directement celui qui vient de (re)donner son login
		// mais sans cookie d'admin ni connexion longue

		include_spip('inc/auth');
		if (!$auteur = auth_identifier_login($login, $pass, '')){
			//do not generate an error because does not prevent to log manually
			error_log("Error: login automatique impossible with login = " . $login . "during site creation");
		}

	}

	// installer les metas
	$config = charger_fonction('config', 'inc');
	//call to inc_config_dist() function from config.php which calls: actualise_metas(liste_metas())
	//liste_metas() defines the list of meta.
	$config();

	//patch novaforge: rajouter automatiquel le nom du site et sa description
	ecrire_meta('nom_site', $site_nom);
	ecrire_meta('descriptif_site', $site_description);

	// activer les plugins
	// leur installation ne peut pas se faire sur le meme hit, il faudra donc
	// poursuivre au hit suivant
	include_spip('inc/plugin');

	//TODO: A revoir car plante .......
	//actualise_plugins_actifs();

}



function etape2_initialisation($adresse_db='', $login_db='', $pass_db='',  $server_db='', $choix_db='', $sel_db='', $chmod_db=''){
	$link = spip_connect_db($adresse_db, 0, $login_db, $pass_db, $name_db, $server_db);
	$GLOBALS['connexions'][$server_db] = $link;

	$GLOBALS['connexions'][$server_db][$GLOBALS['spip_sql_version']]
	= $GLOBALS['spip_' . $server_db .'_functions_' . $GLOBALS['spip_sql_version']];


	// prenons toutes les dispositions possibles pour que rien ne s'affiche !

	/*
	 * /!\ sqlite3/PDO : erreur sur join(', ', $link)
	 * L'objet PDO ne peut pas etre transformee en chaine
	 * Un echo $link ne fonctionne pas non plus
	 * Il faut utiliser par exemple print_r($link)
	 */
	//echo "\n<!--\n", join(', ', $link), " $login_db ";
	$db_connect = 0; // revoirfunction_exists($ferrno) ? $ferrno() : 0;
	//echo join(', ', $GLOBALS['connexions'][$server_db]);
	//echo "\n-->\n";	//call patched spip function that delete tables and configuration files.
	//Note:
	//  $siteId is not used when using initial spip function (here after).
	//  Spip Mechanics is using  _DIR_CONNECT  (ex. ../sites/spip2.localhost/config/)
	//  which is initialized

	if (($db_connect=="0") && $link) {
		spip_connect_db($adresse_db, 0, $login_db, $pass_db, '',$server_db);
	}
}


function create_base($adresse_db='', $login_db='', $pass_db='',  $server_db='', $choix_db='', $sel_db='', $chmod_db=''){
	//added to do same initialisation as into etape2
	etape2_initialisation();

	global $spip_version_base;

	// Prefix des tables :
	// contrairement a ce qui est dit dans le message (trop strict mais c'est
	// pour notre bien), on va tolerer les chiffres en plus des minuscules
	// S'il n'est pas defini par mes_options/inc/mutualiser, on va le creer
	// a partir de ce qui est envoye a l'installation
	if (!defined('_INSTALL_TABLE_PREFIX')) {
		$table_prefix = ($GLOBALS['table_prefix'] != 'spip')
		? $GLOBALS['table_prefix']
		: trim(preg_replace(',[^a-z0-9],','',strtolower(_request('tprefix'))));
		// S'il est vide on remet spip
		if (!$table_prefix)
		$table_prefix = 'spip';
	} else {
		$table_prefix = _INSTALL_TABLE_PREFIX;
	}
	//spip_connect_db($adresse_db, 0, $login_db, $pass_db, $name_db, $server_db);
	$GLOBALS['connexions'][$server_db]
	= spip_connect_db($adresse_db, 0, $login_db, $pass_db, '', $server_db);

	$GLOBALS['connexions'][$server_db][$GLOBALS['spip_sql_version']]
	= $GLOBALS['spip_' . $server_db .'_functions_' . $GLOBALS['spip_sql_version']];

	$fquery = sql_serveur('query', $server_db);
	if ($choix_db == "new_spip") {
		$re = ',^[a-z_][a-z_0-9-]*$,i';
		if (preg_match($re, $sel_db))
		sql_create_base($sel_db, $server_db);
		else {
			$re = "Error: le nom de la base doit correspondre a $re";
			spip_log($re);
			error_log($re);
			return new soap_fault( 'Client', '', 'Spip site creation error', 'Error: le nom de la base doit correspondre a: ' . $re);
		}
	}

	// on rejoue la connexion apres avoir teste si il faut lui indiquer
	// un sql_mode
	install_mode_appel($server_db, false);


	$GLOBALS['connexions'][$server_db]
	= spip_connect_db($adresse_db, 0, $login_db, $pass_db, $sel_db, $server_db);

	$GLOBALS['connexions'][$server_db][$GLOBALS['spip_sql_version']]
	= $GLOBALS['spip_' . $server_db .'_functions_' . $GLOBALS['spip_sql_version']];

	// Completer le tableau decrivant la connexion

	$GLOBALS['connexions'][$server_db]['prefixe'] = $table_prefix;
	$GLOBALS['connexions'][$server_db]['db'] = $sel_db;

	$old = sql_showbase($table_prefix  . "_meta", $server_db);
	if ($old) $old = sql_fetch($old, $server_db);
	if (!$old) {

		// Si possible, demander au serveur d'envoyer les textes
		// dans le codage std de SPIP,
		$charset = sql_get_charset(_DEFAULT_CHARSET, $server_db);

		if ($charset) {
			sql_set_charset($charset['charset'], $server_db);
			$GLOBALS['meta']['charset_sql_base'] =
			$charset['charset'];
			$GLOBALS['meta']['charset_collation_sql_base'] =
			$charset['collation'];
			$GLOBALS['meta']['charset_sql_connexion'] =
			$charset['charset'];
			$charsetbase = $charset['charset'];
		} else {
			spip_log(_DEFAULT_CHARSET . " inconnu du serveur SQL");
			error_log(_DEFAULT_CHARSET . " inconnu du serveur SQL");
			$charsetbase = 'standard';
		}
		spip_log("Creation des tables. Codage $charsetbase");
		creer_base($server_db); // AT LAST
		creer_base_types_doc($server_db);
		// memoriser avec quel charset on l'a creee

		if ($charset) {
			$t = array('nom' => 'charset_sql_base',
				   'valeur' => $charset['charset'],
				   'impt' => 'non');
			@sql_insertq('spip_meta', $t, '', $server_db);
			$t['nom'] = 'charset_collation_sql_base';
			$t['valeur'] = $charset['collation'];
			@sql_insertq('spip_meta', $t, '', $server_db);
			$t['nom'] = 'charset_sql_connexion';
			$t['valeur'] = $charset['charset'];
			@sql_insertq('spip_meta', $t, '', $server_db);
		}
		$t = array('nom' => 'version_installee',
			   'valeur' => $spip_version_base,
			   'impt' => 'non');
		@sql_insertq('spip_meta', $t, '', $server_db);
		$t['nom'] = 'nouvelle_install';
		$t['valeur'] = 1;
		@sql_insertq('spip_meta', $t, '', $server_db);
		// positionner la langue par defaut du site si un cookie de lang a ete mis
		if (isset($_COOKIE['spip_lang_ecrire'])){
			@sql_insertq('spip_meta', array('nom'=>'langue_site','valeur'=>$_COOKIE['spip_lang_ecrire']), '', $server_db);
		}
	} else {

		// pour recreer les tables disparues au besoin
		spip_log("Table des Meta deja la. Verification des autres.");
		creer_base($server_db);
		$fupdateq = sql_serveur('updateq', $server_db);

		$r = $fquery("SELECT valeur FROM spip_meta WHERE nom='version_installee'", $server_db);

		if ($r) $r = sql_fetch($r, $server_db);
		$version_installee = !$r ? 0 : (double) $r['valeur'];
		if (!$version_installee OR ($spip_version_base < $version_installee)) {
			$fupdateq('spip_meta', array('valeur'=>$spip_version_base, 'impt'=>'non'), "nom='version_installee'", $server_db);
			spip_log("nouvelle version installee: $spip_version_base");
		}
		// eliminer la derniere operation d'admin mal terminee
		// notamment la mise a jour
		@$fquery("DELETE FROM spip_meta WHERE nom='import_all' OR  nom='admin'", $server_db);
	}

	$ligne_rappel = install_mode_appel($server_db);

	$result_ok = @$fquery("SELECT COUNT(*) FROM spip_meta", $server_db);
	if (!$result_ok) {
		error_log("ERROR into create_base() function when checking query to spip_meta");
	}
	error_log("*****createbase _FILE_CHMOD_TMP:"._FILE_CHMOD_TMP);
	if ($chmod_db) {
		install_fichier_connexion(_FILE_CHMOD_TMP, "if (!defined('_SPIP_CHMOD')) define('_SPIP_CHMOD', ". sprintf('0%3o',$chmod_db).");\n");
	}

	if (preg_match(',(.*):(.*),', $adresse_db, $r))
	list(,$adresse_db, $port) = $r;
	else $port = '';

	// si ce fichier existe a cette etape c'est qu'il provient
	// d'une installation qui ne l'a pas cree correctement.
	// Le supprimer pour que _FILE_CONNECT_TMP prime.

	if (_FILE_CONNECT AND file_exists(_FILE_CONNECT))
	spip_unlink(_FILE_CONNECT);

	install_fichier_connexion(_FILE_CONNECT_TMP,
	$ligne_rappel
	. install_connexion($adresse_db,
	$port,
	$login_db,
	$pass_db,
	$sel_db,
	$server_db,
	$table_prefix));
	return '';
}

function install_etape_4_dist()
{

	// creer le repertoire cache, qui sert partout !
	if(!@file_exists(_DIR_CACHE)) {
		$rep = preg_replace(','._DIR_TMP.',', '', _DIR_CACHE);
		$rep = sous_repertoire(_DIR_TMP, $rep, true,true);
	}



	// installer les extensions
	include_spip('inc/plugin');
	$afficher = charger_fonction("afficher_liste",'plugins');
	//  patch nf: avoid to list into  the web service response
	//	echo $afficher(self(), liste_plugin_files(_DIR_EXTENSIONS),array(), _DIR_EXTENSIONS,'afficher_nom_plugin');
	//Installation des deux plugins
	plugins_installer_dist('cas','install');
	plugins_installer_dist('msie_compat','install');
	
	// mettre a jour si necessaire l'adresse du site
	// securite si on arrive plus a se loger
	include_spip('inc/config');

	appliquer_adresse_site(null);
	appliquer_modifs_config();

}

// http://doc.spip.org/@base_delete_all_dist
function nf_base_delete_all($delete, $site_id)
{
	$res = array();
	if (is_array($delete)) {
		foreach ($delete as $table) {
			if (sql_drop_table($table))
			$res[] = $table;
			else {
				spip_log("SPIP n'a pas pu detruire $table. pour le siteId = ". $site);
				error_log("SPIP n'a pas pu detruire $table. pour le siteId = ". $site);
			}
		}

		// un pipeline pour detruire les tables installees par les plugins
		//		pipeline('delete_tables', '');
	}
	$d = count($delete);
	$r = count($res);
	spip_log("Tables detruites: $r sur $d: " . join(', ',$res));
	if ($r != $d ){
		error_log("Tables detruites: $r sur $d: " . join(', ',$res));
	}

	// saving the current working directory for configuration, cache, tmp, ....
	$current_dir=getcwd();
	@chdir("../sites");
	if (is_dir($site_id)) {
		$format="d-m-Y-g:i:s-a";
		$today = date($format);
		$r = @rename($site_id, $site_id . "_" . $today);
		if ($r){
			spip_log("The configuration directory: " . $site_id . " has been renamed. ");
		}
		else {
			error_log("Pb. to rename the configuration directory: " . $site_id);
		}
	}
	// go back to initial dir.
	@chdir($current_dir);

	//		spip_unlink(_FILE_CONNECT);
	//		spip_unlink(_FILE_CHMOD);
	//		spip_unlink(_FILE_META);
	//		spip_unlink(_ACCESS_FILE_NAME);
	//		spip_unlink(_CACHE_RUBRIQUES);

}
?>
