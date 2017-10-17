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
function normalize(str) {
	var unixname = "";
	str = replaceSpecialChar(str);
	var ereg = new RegExp("[a-zA-Z0-9_\.]+", "g");
	if ((tab = str.match(ereg)) != null) {
		for ( var i = 0; i < tab.length; i++)
			unixname += tab[i];
		return unixname.toLowerCase();
	} else {
		return "";
	}
}

function array_key_exist(key, array) {
	for ( var property in array) {
		if (key == property) {
			return true;
		}
	}
	return false;
}

function replaceSpecialChar(chaine) {
	str_returned = "";
	var unicodeTab = new Array()
	// 233=
	unicodeTab['233'] = 'e';
	// 224=
	unicodeTab['224'] = 'a';
	// 232=
	unicodeTab['232'] = 'e';
	// 242=
	unicodeTab['242'] = 'o';
	// 249=
	unicodeTab['249'] = 'u';
	// 226=
	unicodeTab['226'] = 'a';
	// 234=
	unicodeTab['234'] = 'e';
	// 238=
	unicodeTab['238'] = 'i';
	// 244=
	unicodeTab['244'] = 'o';
	// 251=
	unicodeTab['251'] = 'u';
	// 228=
	unicodeTab['228'] = 'a';
	// 235=
	unicodeTab['235'] = 'e';
	// 239=
	unicodeTab['239'] = 'i';
	// 246=
	unicodeTab['246'] = 'o';
	// 252=
	unicodeTab['252'] = 'u';

	for ( var i = 0; i < chaine.length; i++) {
		var code = chaine.charCodeAt(i);
		if (array_key_exist(code, unicodeTab)) {
			str_returned += unicodeTab[code];
		} else {
			str_returned += chaine.charAt(i);
		}
	}
	return str_returned;
}
