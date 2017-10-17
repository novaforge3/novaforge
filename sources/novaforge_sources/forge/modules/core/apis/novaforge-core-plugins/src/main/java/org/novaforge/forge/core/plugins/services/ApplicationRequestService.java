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
package org.novaforge.forge.core.plugins.services;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;
import org.novaforge.forge.core.plugins.exceptions.ApplicationRequestException;

import java.util.List;

/**
 * A service exposed used by an application in order to request another one. The association between two are
 * configured by project.
 * 
 * @author lamirang
 */
public interface ApplicationRequestService
{

   /**
    * @param <T>
    *           the object wanted
    * @param pInstanceUUID
    *           the instance uuid of the application which send the request
    * @param pRequest
    *           name of the request
    * @param pJSONParameter
    * @param pUser
    *           the user who request
    * @return a list of object asking
    * @throws ApplicationRequestException
    */
   <T extends PluginExchangeableBean> List<T> notifyForRequest(final String pInstanceUUID,
         final String pRequest,
         final String pJSONParameter, final String pUser) throws ApplicationRequestException;

   /**
    * @param <T>
    *           represents the object wanted
    * @param pInstanceUUID
    *           the instance uuid of the application which send the request
    * @param pRequest
    *           name of the request
    * @param pUser
    *           the user who request
    * @return a list of object asking
    * @throws ApplicationRequestException
    */
   <T extends PluginExchangeableBean> List<T> notifyForRequest(final String pInstanceUUID,
         final String pRequest,
         final String pUser) throws ApplicationRequestException;

   /**
    * Check if a request has reply association configured. If any error occured false is returned.
    * 
    * @param pInstanceUUID
    *          the instance uuid of the application which send the request
    * @param pRequest
    *          name of the request
    * @return true if an association is set up, otherwise false
    */
   boolean isAssociated(final String pInstanceUUID, final String pRequest);
}
