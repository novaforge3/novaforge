/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.plugins.ecm.alfresco.library;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * This helper is used to work with CMIS web service.
 * 
 * @author lamirang
 */
public class AlfrescoCMISHelperCustom implements AlfrescoCMISHelper
{
   private final Map<String, String> parameter;

   public AlfrescoCMISHelperCustom(String baseUrl, String username, String password)
   {
      parameter = new HashMap<String, String>();

      // user credentials
      parameter.put(SessionParameter.USER, username);
      parameter.put(SessionParameter.PASSWORD, password);

      // connection settings
      parameter.put(SessionParameter.ATOMPUB_URL, baseUrl);
      parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
      parameter.put(SessionParameter.REPOSITORY_ID, "workspace://SpacesStore/");
   }

   @Override
   public Map<String, String> getParamters()
   {
      return parameter;
   }
}
