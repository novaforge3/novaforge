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
package org.novaforge.forge.core.configuration.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.dao.ForgeConfigurationDAO;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.internal.keys.ForgeCfg;
import org.novaforge.forge.core.configuration.model.ForgeConfiguration;
import org.novaforge.forge.core.initialization.services.ForgeInitializationListener;
import org.novaforge.forge.core.initialization.services.ForgeInitializationService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ForgeInitializationService}
 * 
 * @author Guillaume Lamirand
 * @see ForgeInitializationService
 */
public class ForgeInitializationServiceImpl implements ForgeInitializationService
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(ForgeConfigurationServiceImpl.class);
  /**
   * Service reference of {@link ForgeConfigurationDAO}
   */
  private ForgeConfigurationDAO             forgeConfigurationDAO;
  /**
   * Contains all {@link ForgeInitializationListener} registered
   */
  private List<ForgeInitializationListener> listeners;

  /**
   * Call on when the container initialize this component
   */
  public void init()
  {
    listeners = new ArrayList<ForgeInitializationListener>();
  }

  /**
   * Call on when the container destroy this component
   */
  public void destroy()
  {
    listeners.clear();
    listeners = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void setInitializationSuccessfull(final boolean pSendEvent)
  {
    final ForgeConfiguration newForgeConfiguration = forgeConfigurationDAO.newForgeConfiguration(ForgeCfg.INITIALIZATION
                                                                                                     .getKey(),
                                                                                                 Boolean.TRUE
                                                                                                     .toString());
    forgeConfigurationDAO.createOrupdate(newForgeConfiguration);

    if ((pSendEvent) && (listeners != null))
    {
      for (final ForgeInitializationListener listener : listeners)
      {
        listener.onInitializationSuccessfull();
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialized()
  {
    return getInitializationValue();
  }

  /**
   * Returns from persistence value of the key given
   *
   * @return the value found
   * @throws ForgeConfigurationException
   *           thrown if key is not found
   */
  private boolean getInitializationValue()
  {
    final ForgeCfg initialization = ForgeCfg.INITIALIZATION;
    String         value          = initialization.getDefaultValue();
    try
    {
      final ForgeConfiguration forgeConfiguration = forgeConfigurationDAO.findByKey(initialization.getKey());
      value = forgeConfiguration.getValue();
    }
    catch (final NoResultException e)
    {
      LOGGER.warn("The initialization property hasn't been persisted yet.");
    }
    return Boolean.parseBoolean(value);
  }

  /**
   * This will method will be called by container when a {@link ForgeInitializationListener} appeared
   *
   * @param pListener
   *          the listener which appeared
   */
  public synchronized void addListener(final ForgeInitializationListener pListener)
  {
    if ((pListener != null) && (!listeners.contains(pListener)))
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("A new initialization listener has been registered");
      }
      listeners.add(pListener);
    }
  }

  /**
   * This will method will be called by container when a {@link ForgeInitializationListener}
   * disapeared
   * 
   * @param pListener
   *          the listener which disapeared
   */
  public synchronized void removeListener(final ForgeInitializationListener pListener)
  {
    if ((pListener != null) && (listeners.contains(pListener)))
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("A new initialization listener has been unregistered");
      }
      listeners.remove(pListener);
    }

  }

  /**
   * Bind method used by the container to inject {@link ForgeConfigurationDAO} service.
   * 
   * @param pForgeConfigurationDAO
   *          the forgeConfigurationDAO to set
   */
  public void setForgeConfigurationDAO(final ForgeConfigurationDAO pForgeConfigurationDAO)
  {
    forgeConfigurationDAO = pForgeConfigurationDAO;
  }

}
