/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 * @copyright Copyright 2017  Atos, NovaForge Version 3 and above.
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
 
 /**
 * Mantis-427: get the projectId selected in the combobox with id=form-set-project-id
 */
function getProjectId() {
	var e = document.getElementById("ul-set-project");
	var result;
	if (e) {
	   var e1 = e.getElementsByClassName( "active") 
	   if (e1) {
			result = e1[0].getAttribute("value");
		}
	}
	return result;
}

/**
 * Mantis-427: add the given project Id to nearly all links and forms
 */
function addProjectIdToRequest(pProjectId){
	var links = document.getElementsByTagName('a');
	for (var i = 0, c = links.length ; i < c ; i++) {
		var linkRef = links[i].href;
		if (('' !== linkRef) && (linkRef.indexOf('mailto:') == -1) && (linkRef.indexOf('www.mantisbt.org') == -1)){
			if (linkRef.indexOf('php?') != -1){
				links[i].href+='&cur_project_id='+pProjectId;
			} else {
				links[i].href+='?cur_project_id='+pProjectId;
			}
		}
	}
	
	var forms = document.getElementsByTagName('form');
	for (var i = 0, c = forms.length ; i < c ; i++) {
       //NOVAFHELP-452 : Complete Mantis-427
		if (forms[i].id!="form-config-status-delete"){
			var input = document.createElement("input");
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "cur_project_id");
			input.setAttribute("value", pProjectId);
			forms[i].appendChild(input);
		}
	}
}
