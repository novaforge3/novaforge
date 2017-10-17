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

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.constant.BeaverProcessType;
import org.novaforge.beaver.exception.BeaverException;

/**
 * This class describes meta information for a Beaver Process
 * 
 * @author Guillaume Lamirand
 */
public class BeaverProcessInfo
{
  /**
   * The version value
   */
  private final String              version;
  /**
   * The process value
   */
  private final BeaverProcessType   processType;
  /**
   * The product id value
   */
  private final String              productId;
  /**
   * The main script value
   */
  private final String              script;
  /**
   * The config script value
   */
  private final String              config;

  /**
   * The config script value
   */
  private PropertyArtifact          dataProperty;
  /**
   * Contains properties associated to this process
   */
  private final Map<String, String> productProperties;

  /**
   * Default constructor.
   * 
   * @param pVersion
   * @param pProcess
   * @param pProductId
   * @param pScript
   * @param pConfig
   * @param pData
   * @param pProperties
   * @throws BeaverException
   */
  public BeaverProcessInfo(final String pVersion, final String pProcess, final String pProductId,
      final String pScript, final String pConfig, final String pData, final Map<String, String> pProperties)
      throws BeaverException
  {
    version = pVersion;
    processType = BeaverProcessType.get(pProcess);
    productId = pProductId;
    script = pScript;
    config = pConfig;
    if (StringUtils.isNotBlank(pData))
    {
      dataProperty = new PropertyArtifact(pData);
    }
    productProperties = pProperties;
  }

  /**
   * Returns the product version
   * 
   * @return the product version
   */
  public String getProductVersion()
  {
    return version;
  }

  /**
   * Returns the process type
   * 
   * @return the process type
   */
  public BeaverProcessType getProcessType()
  {
    return processType;
  }

  /**
   * Returns the product id
   * 
   * @return the product id
   */
  public String getProductId()
  {
    return productId;
  }

  /**
   * Returns the main script
   * 
   * @return the main script
   */
  public String getScript()
  {
    return script;
  }

  /**
   * Returns the config script
   * 
   * @return the config script
   */
  public String getConfig()
  {
    return config;
  }

  /**
   * Returns the {@link PropertyArtifact} describing data associated to the process
   * 
   * @return the {@link PropertyArtifact} describing data associated to the process
   */
  public PropertyArtifact getDataProperty()
  {
    return dataProperty;
  }

  /**
   * Returns a Set view of the property mappings contained for the server
   * 
   * @return a set view of the mappings contained for the server
   * @see java.util.Map#entrySet()
   */
  public Set<Entry<String, String>> propertyEntrySet()
  {
    return productProperties.entrySet();
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
    return productProperties.get(pKey);
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
    return productProperties.containsKey(pKey);
  }
}
