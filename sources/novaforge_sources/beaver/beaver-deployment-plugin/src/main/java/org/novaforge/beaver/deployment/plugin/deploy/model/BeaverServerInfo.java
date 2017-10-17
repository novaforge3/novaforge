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
package org.novaforge.beaver.deployment.plugin.deploy.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.novaforge.beaver.context.ServerType;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.Resource;

/**
 * This class describes meta information for a Beaver server
 * 
 * @author Guillaume Lamirand
 */
public class BeaverServerInfo
{

  /**
   * The serverId value
   */
  private final String              serverId;
  /**
   * The serverType value
   */
  private final ServerType          serverType;
  /**
   * The setup script value
   */
  private final String              install;
  /**
   * The update script value
   */
  private final String              update;
  private final Map<String, String> serverProperties;

  /**
   * Default constructor. It will validate if server id has correct format
   * 
   * @param pServerId
   *          the server id
   * @param pServerType
   *          the server type
   * @param pSetup
   *          the setup script value
   * @param pUpdate
   *          the update script value, can be empty
   * @param pServerProperties
   *          some properties associated to the server
   * @throws BeaverException
   *           thrown if server id is incorrect
   */
  public BeaverServerInfo(final String pServerId, final ServerType pServerType, final String pSetup,
      final String pUpdate, final Map<String, String> pServerProperties) throws BeaverException
  {
    serverId = pServerId;
    serverType = pServerType;
    install = pSetup;
    update = pUpdate;
    serverProperties = pServerProperties;
  }

  /**
   * This method is called when instantiate a new {@link BeaverServerInfo} to validate that the server id
   * follow the rules.
   * 
   * @param pServerId
   *          the server id to validate
   * @throws BeaverException
   *           thrown if server id is incorrect
   */
  public static void validateId(final String pServerId) throws BeaverException
  {
    if ((pServerId == null) || ("".equals(pServerId)) || (Resource.LOCAL_SERVER.equals(pServerId))
        || (Resource.MAIN_SERVER.equals(pServerId)))
    {
      throw new BeaverException(
          String
              .format(
                  "The server id [%s] defined is incorrect. It has to be non empty and different from [local] or [main]",
                  pServerId));
    }
  }

  /**
   * Returns the server id
   * 
   * @return the serverId
   */
  public String getServerId()
  {
    return serverId;
  }

  /**
   * Returns the server type
   * 
   * @return the serverType
   */
  public ServerType getServerType()
  {
    return serverType;
  }

  /**
   * Returns the install script
   * 
   * @return the install
   */
  public String getInstallScript()
  {
    return install;
  }

  /**
   * Returns the update script
   * 
   * @return the update
   */
  public String getUpdateScript()
  {
    return update;
  }

  /**
   * Returns a Set view of the property mappings contained for the server
   * 
   * @return a set view of the mappings contained for the server
   * @see java.util.Map#entrySet()
   */
  public Set<Entry<String, String>> propertyEntrySet()
  {
    return serverProperties.entrySet();
  }

  /**
   * Returns the value of the property given as parameter
   * 
   * @param pKey
   *          the key to look for
   * @return value found in laucnher properties
   */
  public String getProperty(final String pKey)
  {
    return serverProperties.get(pKey);
  }

  /**
   * Returns the value of the property given as parameter
   * 
   * @param pKey
   *          the key to look for
   * @return value found in laucnher properties
   */
  public boolean containsProperty(final String pKey)
  {
    return serverProperties.containsKey(pKey);
  }

  /**
   * Returns the value of the property given as parameter
   * 
   * @param pKey
   *          the key to insert
   * @param pValue
   *          the value associated
   */
  public void putProperty(final String pKey, final String pValue)
  {
    serverProperties.put(pKey, pValue);
  }
}
