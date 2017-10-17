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
package org.novaforge.studio.core.client;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.novaforge.studio.core.client.impl.StudioManagementClientImpl;
import org.novaforge.studio.core.client.impl.StudioProjectClientImpl;

/**
 * Factory used to get clients to access remote services exposed by the Forge.
 * 
 * @author rols-p
 */
public class StudioClientFactory
{

   private static ConcurrentHashMap<AbstractWebLocation, StudioProjectClient> studioProjectClients = new ConcurrentHashMap<AbstractWebLocation, StudioProjectClient>();

   private static ConcurrentHashMap<AbstractWebLocation, StudioManagementClient> studioManagementClients = new ConcurrentHashMap<AbstractWebLocation, StudioManagementClient>();

   /**
    * Returns a unique instance of StudioProjectClient accodrding to a Location.
    * 
    * @param location
    *           url of the Remote Services
    * @return a not null instance of StudioProjectClient
    */
   public synchronized static StudioProjectClient getProjectClient(AbstractWebLocation location)
   {
      StudioProjectClient client = null;
      synchronized (studioProjectClients)
      {
         client = studioProjectClients.get(location);
         if (client == null)
         {
            client = new StudioProjectClientImpl(location);
            studioProjectClients.put(location, client);
         }
      }
      return client;
   }

   /**
    * Returns a unique instance of StudioManagementClient according to a Location.
    * 
    * @param location
    *           url of the Remote Services
    * @return a not null instance of StudioManagementClient
    */
   public synchronized static StudioManagementClient getManagementClient(AbstractWebLocation location)
   {
      StudioManagementClient client = null;
      synchronized (studioManagementClients)
      {
         client = studioManagementClients.get(location);
         if (client == null)
         {
            client = new StudioManagementClientImpl(location);
            studioManagementClients.put(location, client);
         }
      }
      return client;
   }

}
