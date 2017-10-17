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
package org.novaforge.forge.core.organization.internal.services;

import org.novaforge.forge.core.organization.dao.CompositionDAO;
import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.exceptions.CompositionServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.services.CompositionService;
import org.novaforge.forge.core.plugins.exceptions.PluginArtefactFactoryException;
import org.novaforge.forge.core.plugins.services.PluginArtefactFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link CompositionService}
 * 
 * @author sbenoist
 * @see CompositionService
 */
public class CompositionServiceImpl implements CompositionService
{
   /**
    * Reference to {@link CompositionDAO} service injected by the container
    */
   private CompositionDAO        compositionDAO;
   /**
    * Reference to {@link ProjectDAO} service injected by the container
    */
   private ProjectDAO            projectDAO;
   /**
    * Reference to {@link NodeDAO} service injected by the container
    */
   private NodeDAO               nodeDAO;
   /**
    * Reference to {@link PluginArtefactFactory} service injected by the
    * container
    */
   private PluginArtefactFactory pluginArtefactFactory;

   /**
    * {@inheritDoc}
    */
   @Override
   public Composition newComposition()
   {
      return compositionDAO.newComposition();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Composition createComposition(final String pProjectId, final Composition pComposition,
         final String pSourceUri, final String pTargetUri, final Map<String, String> pTemplate)
               throws CompositionServiceException
               {
      try
      {
         if ((CompositionType.NOTIFICATION.equals(pComposition.getType())) && (pTemplate != null))
         {
            if (pTemplate != null)
            {
               final String template = pluginArtefactFactory.buildArtefactTemplate(pTemplate);
               pComposition.setTemplate(template);
            }
         }
      }
      catch (final PluginArtefactFactoryException e)
      {
         throw new CompositionServiceException(ExceptionCode.TECHNICAL_ERROR, String.format(
               "Unable to build template from given map with [map=%s]", pTemplate), e);
      }

      try
      {
         // Set source application
         final ProjectApplication appSource = (ProjectApplication) nodeDAO.findByUri(pSourceUri);
         pComposition.setSource(appSource);

         // Set target application
         final ProjectApplication appTarget = (ProjectApplication) nodeDAO.findByUri(pTargetUri);
         pComposition.setTarget(appTarget);

         // Set the composition UUID
         final UUID uuid = UUID.randomUUID();
         pComposition.setUUID(uuid);

         // Add the composition to project
         final Project project = projectDAO.findByProjectId(pProjectId);
         project.addComposition(pComposition);
         projectDAO.update(project);

         return pComposition;
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }
               }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deleteComposition(final String pProjectId, final String pUUID)
         throws CompositionServiceException
         {
      try
      {
         // Get the composition
         final Composition composition = compositionDAO.findByUUID(pUUID);

         // Get the project
         final Project project = projectDAO.findByProjectId(pProjectId);

         // Remove the composition from the project
         project.removeComposition(composition);
         projectDAO.update(project);
         return true;
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Composition> getComposition(final String pProjectId) throws CompositionServiceException
   {
      try
      {
         return compositionDAO.findByProject(pProjectId);
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<Composition> getCompositionFromSource(final String pProjectId, final String pSourceInstance,
         final CompositionType pSourceType, final String pSourceName) throws CompositionServiceException
         {
      try
      {
         return compositionDAO.findBySourceAndAssociation(pSourceInstance, pSourceType, pSourceName);
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }
         }

   /**
    * {@inheritDoc}
    * 
    * @throws CompositionServiceException
    */
   @Override
   public List<Composition> getCompositionFromSource(final String pSourceInstance)
         throws CompositionServiceException
         {
      try
      {
         return compositionDAO.findBySource(pSourceInstance);
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public void updateCompositionTemplate(final String pProjectId, final String pUUID,
         final Map<String, String> pTemplate) throws CompositionServiceException
         {
      // Get the composition
      final Composition composition = compositionDAO.findByUUID(pUUID);

            if (!CompositionType.NOTIFICATION.equals(composition.getType()))
      {
         throw new CompositionServiceException(
               ExceptionCode.ERR_COMPOSITION_DO_NOT_ACCEPT_TEMPLATE,
               String
               .format(
                     "Unable to update the template of the composition asking because its source type is not compatible [uuid=%s, template=%s]",
                     pUUID, pTemplate));
      }

      // Get the template
      try
      {
         final String template = pluginArtefactFactory.buildArtefactTemplate(pTemplate);
         composition.setTemplate(template);
         compositionDAO.update(composition);
      }
      catch (final PluginArtefactFactoryException e)
      {
         throw new CompositionServiceException(ExceptionCode.TECHNICAL_ERROR, String.format(
               "Unable to build template from given map with [map=%s]", pTemplate), e);
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setCompositionStatus(final String pProjectId, final String pUUID, final boolean pStatus)
         throws CompositionServiceException
         {
      try
      {
         final Composition composition = compositionDAO.findByUUID(pUUID);
         composition.setActivated(pStatus);
         compositionDAO.update(composition);
      }
      catch (final Exception e)
      {
         throw new CompositionServiceException("a technical error occured", e);
      }
         }

   /**
    * Use by container to inject {@link CompositionDAO} implementation
    * 
    * @param pCompositionDAO
    *          the compositionDAO to set
    */
   public void setCompositionDAO(final CompositionDAO pCompositionDAO)
   {
      compositionDAO = pCompositionDAO;
   }

   /**
    * Use by container to inject {@link ProjectDAO} implementation
    * 
    * @param pProjectDAO
    *          the projectDAO to set
    */
   public void setProjectDAO(final ProjectDAO pProjectDAO)
   {
      projectDAO = pProjectDAO;
   }

   /**
    * Use by container to inject {@link NodeDAO} implementation
    * 
    * @param pNodeDAO
    *          the nodeDAO to set
    */
   public void setNodeDAO(final NodeDAO pNodeDAO)
   {
      nodeDAO = pNodeDAO;
   }

   /**
    * Use by container to inject {@link PluginArtefactFactory} implementation
    * 
    * @param pPluginArtefactFactory
    *          the pluginArtefactFactory to set
    */
   public void setPluginArtefactFactory(final PluginArtefactFactory pPluginArtefactFactory)
   {
      pluginArtefactFactory = pPluginArtefactFactory;
   }

}
