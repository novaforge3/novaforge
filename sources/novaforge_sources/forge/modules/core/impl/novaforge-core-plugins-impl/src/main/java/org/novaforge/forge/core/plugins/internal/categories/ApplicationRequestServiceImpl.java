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
package org.novaforge.forge.core.plugins.internal.categories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.dao.CompositionDAO;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.ApplicationRequestException;
import org.novaforge.forge.core.plugins.exceptions.ExceptionCode;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.ApplicationRequestService;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service implemetation used to send request to another plugin.
 * 
 * @author Guillaume Lamirand
 */
public class ApplicationRequestServiceImpl implements ApplicationRequestService
{

   /**
    * Logger component
    */
   private static final Log           LOGGER = LogFactory.getLog(ApplicationRequestServiceImpl.class);
   /**
    * {@link PluginsManager} injected by container
    */
   private PluginsManager             pluginsManager;
   /**
    * {@link PluginsCategoryManager} injected by container
    */
   private PluginsCategoryManager     categoriesManager;
   /**
    * {@link CompositionDAO} injected by container
    */
   private CompositionDAO             compositionDAO;
   /**
    * {@link ForgeIdentificationService} injected by container
    */
   private ForgeIdentificationService forgeIdentification;

   /**
    * Use by container to inject {@link PluginsManager}
    *
    * @param pPluginsManager
    *           the pluginsManager to set
    */
   public void setPluginsManager(final PluginsManager pPluginsManager)
   {
      pluginsManager = pPluginsManager;
   }

   /**
    * Use by container to inject {@link PluginsCategoryManager}
    *
    * @param pCategoriesManager
    *           the categoriesManager to set
    */
   public void setCategoriesManager(final PluginsCategoryManager pCategoriesManager)
   {
      categoriesManager = pCategoriesManager;
   }

   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   @Override
   public <T extends PluginExchangeableBean> List<T> notifyForRequest(final String pInstanceUUID,
         final String pRequest, final String pJSONParameter, final String pUser)
               throws ApplicationRequestException
               {

      final List<Composition> compositions = getCompositions(pInstanceUUID, pRequest);
                  if (compositions.isEmpty())
      {
         throw new ApplicationRequestException(ExceptionCode.ERR_REQUEST_DO_HAVE_EXISTING_ASSOCIATION,
               String.format("Unable to find any composition for [instance_uuid=%s, request=%s]",
                     pInstanceUUID, pRequest));
      }


      // loop on each composition and wrap the objects into a list
                  T returnBean;
      List<T> returned = new ArrayList<T>();
      for (Composition composition : compositions)
      {
         // Getting target plugin uuid
         final String targetUUID = composition.getTarget().getPluginUUID().toString();
         // Getting target plugin categorie clazz and its service
         final PluginCategoryService target = getPluginCategoryService(targetUUID);
         // Get forge id
         final String forgeId = forgeIdentification.getForgeId().toString();
         // Build method parameter
         final Object[] parameters = buildMethodParameters(forgeId, composition.getTarget()
               .getPluginInstanceUUID().toString(), pJSONParameter, pUser);

         // Build method parameter
         returnBean = (T) invokeManager(target, composition.getTargetName(), parameters);
         returned.add(returnBean);
      }

      return returned;
               }

   /**
    * Use by container to inject {@link CompositionDAO}
    *
    * @param pCompositionDAO
    *     the compositionDAO to set
    */
   public void setCompositionDAO(final CompositionDAO pCompositionDAO)
   {
      compositionDAO = pCompositionDAO;
   }

   /**
    * Use by container to inject {@link ForgeIdentificationService}
    *
    * @param pForgeIdentification
    *           the forgeIdentification to set
    */
   public void setForgeIdentification(final ForgeIdentificationService pForgeIdentification)
   {
      forgeIdentification = pForgeIdentification;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public <T extends PluginExchangeableBean> List<T> notifyForRequest(final String pInstanceUUID,
         final String pRequest, final String pUser) throws ApplicationRequestException
         {
            return this.notifyForRequest(pInstanceUUID, pRequest, pUser, null);

         }



   private PluginCategoryService getPluginCategoryService(final String targetUUID)
         throws ApplicationRequestException
         {
            PluginCategoryService target;
      try
      {
         final PluginMetadata pluginMetadataByUUID = pluginsManager.getPluginMetadataByUUID(targetUUID);
         final CategoryDefinitionService service = categoriesManager.getCategoryService(pluginMetadataByUUID
               .getCategory());
         target = pluginsManager.getPluginCategoryService(targetUUID, service.getCategoryServiceInterface());
      }
      catch (final PluginManagerException e)
      {
         throw new ApplicationRequestException(
               String.format(
                     "Unable to get the plugin category service defined for the plugin with [uuid=%s]",
                     targetUUID), e);
      }
      return target;
         }



   private List<Composition> getCompositions(final String pInstance, final String pEvent) throws ApplicationRequestException
         {
      List<Composition> compositions = new ArrayList<Composition>();
      LOGGER.debug(String.format("Instance UUID: %s and event source: %s", pInstance, pEvent));
      final List<Composition> comps = compositionDAO.findBySourceAndAssociation(pInstance,
            CompositionType.REQUEST_DATA, pEvent);

      if ((comps != null) && (comps.size() > 0))
      {
         for (Composition composition : comps)
         {
            if (composition.isActivated())
            {
               compositions.add(composition);
            }
         }
      }
      return compositions;
         }

   private Object[] buildMethodParameters(final String pForgeId, final String pInstanceId,
         final String pParameter, final String pUser)
   {
      final List<Object> parameters = new ArrayList<Object>();
      if (pForgeId != null)
      {
         parameters.add(pForgeId);
      }
      if (pInstanceId != null)
      {
         parameters.add(pInstanceId);

      }
      if (pParameter != null)
      {
         parameters.add(pParameter);

      }
      if (pUser != null)
      {
         parameters.add(pUser);

      }
      return parameters.toArray();

   }

   private Object invokeManager(final PluginCategoryService service, final String pMethodName,
         final Object... args) throws ApplicationRequestException
         {
            Object     returnObject;
            Class<?>[] parameterTypes;
      if (args != null)
      {
         parameterTypes = new Class[args.length];
         for (int i = 0; i < args.length; ++i)
         {
            final Object object = args[i];
            parameterTypes[i] = object.getClass();
            LOGGER.debug(String.format("Argument[%s;%s;%s]", i, object.getClass(), object));
         }
      }
      Method m = null;
      final Method[] methods = service.getClass().getMethods();
      for (final Method method : methods)
      {
         if (method.getName().equals(pMethodName))
         {
            m = method;
            break;
         }
      }
      try
      {
         returnObject = m.invoke(service, args);
      }
      catch (final IllegalArgumentException e)
      {
         throw new ApplicationRequestException(String.format(
               "The parameters given to the method are not supported [method=%s,args=%s]", pMethodName,
               Arrays.toString(args)), e);
      }
      catch (final IllegalAccessException e)
      {
         throw new ApplicationRequestException(String.format(
               "Unable to acces to the object given with [method=%s,args=%s]", pMethodName,
               Arrays.toString(args)), e);
      }
      catch (final InvocationTargetException e)
      {
         throw new ApplicationRequestException(String.format(
               "The method called has thrown an exception [method=%s,args=%s]", pMethodName,
               Arrays.toString(args)), e);
      }
      return returnObject;

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAssociated(final String pInstanceUUID, final String pRequest)
   {
      boolean isAssociated;
      try
      {
         isAssociated = !getCompositions(pInstanceUUID, pRequest).isEmpty();
      }
      catch (final ApplicationRequestException e)
      {
         LOGGER.warn(String.format("Unable to find any composition for [instance_uuid=%s, request=%s]",
               pInstanceUUID, pRequest), e);
         isAssociated = false;
      }
      return isAssociated;
   }









}
