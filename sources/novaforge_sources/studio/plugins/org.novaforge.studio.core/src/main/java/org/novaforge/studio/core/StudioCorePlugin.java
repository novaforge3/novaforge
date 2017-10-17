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

package org.novaforge.studio.core;

import java.net.MalformedURLException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.mylyn.tasks.core.RepositoryStatus;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.studio.core.project.ProjectException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Main class of the Novastudio plugin.
 */
public class StudioCorePlugin implements BundleActivator
{

  public static final String        CONNECTOR_KIND       = "NovaStudio Connector";

  public static final String        ID_PLUGIN            = "org.novaforge.studio.core";

  public static final String        REPOSITORY_PROP_TYPE = "repository.type";

  private static BundleContext      context;

  private static StudioCorePlugin   INSTANCE;

  private StudioRepositoryConnector connector;

  /*
   * (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
  public void start(BundleContext bundleContext) throws Exception
  {
    StudioCorePlugin.context = bundleContext;
    INSTANCE = this;
  }

  /*
   * (non-Javadoc)
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
  public void stop(BundleContext bundleContext) throws Exception
  {
    StudioCorePlugin.context = null;
    INSTANCE = null;
  }

  public static StudioCorePlugin getDefault()
  {
    return INSTANCE;
  }

  public static BundleContext getContext()
  {
    return context;
  }

  public StudioRepositoryConnector getConnector()
  {
    return connector;
  }

  public void setConnector(StudioRepositoryConnector connector)
  {
    this.connector = connector;
  }

  /**
   * Returns the path to the file caching repository attributes.
   */
  public IPath getRepostioryCachePath()
  {
    IPath stateLocation = Platform.getStateLocation(getContext().getBundle());
    IPath cacheFile = stateLocation.append("repositoryConfigurations"); //$NON-NLS-1$
    return cacheFile;
  }

  public static IStatus toStatus(Throwable e, TaskRepository repository)
  {
    if (e instanceof RemoteServiceException)
    {
      return new RepositoryStatus(IStatus.ERROR, ID_PLUGIN, RepositoryStatus.ERROR_INTERNAL,
          "Remote_Service_error", e);
    }
    else if (e instanceof ProjectException)
    {

      return new RepositoryStatus(IStatus.ERROR, ID_PLUGIN, RepositoryStatus.ERROR_INTERNAL,
          ((ProjectException) e).getCode().toString(), e);
    }
    else if (e instanceof MalformedURLException)
    {
      return new RepositoryStatus(IStatus.ERROR, ID_PLUGIN, RepositoryStatus.ERROR_IO,
          "Repository URL is invalid", e);
    }
    else
    {
      return new RepositoryStatus(IStatus.ERROR, ID_PLUGIN, RepositoryStatus.ERROR_INTERNAL,
          "Unexpected_error", e);
    }

  }

}
