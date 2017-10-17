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

if ( preg_match(',/spip/([a-zA-Z0-9_-]+)/?,',$_SERVER['REQUEST_URI'],$r)) {
	$site_id = $r[1];

	$path = _DIR_RACINE . 'sites/' . $site_id. '/';

	$cookie_prefix = $site_id;
	//	$table_prefix = $site_id;
	$table_prefix = "spip";

	define('_SPIP_PATH',
	$path . ':' .
	_DIR_RACINE .':' .
	_DIR_RACINE . 'squelettes-dist/:' .
	_DIR_RACINE.'prive/:' .
	_DIR_RESTREINT);

	//define log location to be take into account by NovaForge logger service.
	define('_FILE_LOG_SUFFIX', '_' . $site_id . '.log');	
	$NOVA_LOG=getenv('NOVA_LOG');
	define('_DIR_LOG',  $NOVA_LOG.'/spip_sites/');

	spip_initialisation(
	($path . _NOM_PERMANENTS_INACCESSIBLES),
	($path . _NOM_PERMANENTS_ACCESSIBLES),
	($path . _NOM_TEMPORAIRES_INACCESSIBLES),
	($path . _NOM_TEMPORAIRES_ACCESSIBLES));

	$GLOBALS['dossier_squelettes'] = $path.'squelettes';
	if (is_readable($f = $path._NOM_PERMANENTS_INACCESSIBLES._NOM_CONFIG.'.php')) {
		include($f);
	}

	//Need because of php version used by Novaforge
	date_default_timezone_set('Europe/Paris');
}
?>
