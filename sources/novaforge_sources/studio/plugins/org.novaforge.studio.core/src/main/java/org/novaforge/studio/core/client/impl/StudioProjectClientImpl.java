/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.studio.core.client.impl;

import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.cxf.binding.soap.SoapFault;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.novaforge.forge.remote.services.core.RemoteProjectService;
import org.novaforge.forge.remote.services.exception.ExceptionCode;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.forge.remote.services.model.core.ForgeNode;
import org.novaforge.forge.remote.services.model.core.ForgeProject;
import org.novaforge.studio.core.client.StudioProjectClient;

public class StudioProjectClientImpl extends AbstractStudioClient implements StudioProjectClient
{

   private RemoteProjectService remoteProjectService;

   public StudioProjectClientImpl(AbstractWebLocation location)
   {
      remoteProjectService = initRemoteService(location, RemoteProjectService.class, location.getUrl());
   }

   @Override
   public boolean isAuthenticated() throws RemoteServiceException
   {
      boolean isAuthenticated = false;
      try
      {
         isAuthenticated = remoteProjectService.isAuthenticated();
      }
      catch (SoapFault f)
      {
         if ((Integer.toString(HttpStatus.SC_UNAUTHORIZED).equals(f.getMessage())))
         {
            throw new RemoteServiceException(ExceptionCode.ERR_UN_AUTHORIZED,
                  "Shiro authentication error into camel route for secure web service.");
         }
         else
         {
            throw new RemoteServiceException(ExceptionCode.ERR_UNKNOWN_SOAPFAULT,
                  "Unknown error when trying to perform authentication to novaForge server.");
         }
      }
      return isAuthenticated;
   }

   @Override
   public ForgeProject getForgeProject(String projectId) throws RemoteServiceException
   {
      return remoteProjectService.getForgeProject(projectId);
   }

   @Override
   public List<ForgeProject> getForgeProjects() throws RemoteServiceException
   {
      return remoteProjectService.getForgeProjects();
   }

   @Override
   public List<ForgeNode> getAllForgeProjectNodes(String projectId) throws RemoteServiceException
   {
      return remoteProjectService.getAllForgeProjectNodes(projectId);
   }

   public RemoteProjectService getRemoteProjectService()
   {
      return remoteProjectService;
   }

   public void setRemoteProjectService(RemoteProjectService remoteProjectService)
   {
      this.remoteProjectService = remoteProjectService;
   }

}
