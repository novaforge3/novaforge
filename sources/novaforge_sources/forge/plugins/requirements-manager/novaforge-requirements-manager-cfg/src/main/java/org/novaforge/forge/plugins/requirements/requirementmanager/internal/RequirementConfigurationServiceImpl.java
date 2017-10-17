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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.exception.RequirementManagerException;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementConfigurationService;

import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class RequirementConfigurationServiceImpl implements RequirementConfigurationService
{

  /** Reference to service implementation of {@link PluginConfigurationService} */
  private PluginConfigurationService pluginConfigurationService;

  /** Default Regexp for the parser (Safran). */
  private String                     parserRegexp     = "\\n\\s*@Requirement(\\s*)[(](\\s*)(value(\\s*)[=](\\s*))?[{]?(\\s*)(REQ_ENUM.(\\w*)(\\s*)[,]?(\\s*))+(\\s*)[}]?(\\s*)[)]";

  /** Default file extentions for the parser. */
  private String                     fileExtensions   = null;

  /** Default path to data. */
  private String                     dataPath         = "";

  /** Default code handler type. */
  private String                     codeHandlerType  = "*";

  /** Default max size. */
  private int                        excelFileMaxSize = 5120000;

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientAdmin()
   */
  @Override
  public String getClientAdmin()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientPwd()
   */
  @Override
  public String getClientPwd()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getClientURL(java.net.URL)
   */
  @Override
  public String getClientURL(final URL pURL) throws PluginServiceException
  {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getDescription()
   */
  @Override
  public String getDescription()
  {
    return pluginConfigurationService.getDescription();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getWebServerConfName()
   */
  @Override
  public String getWebServerConfName()
  {
    return pluginConfigurationService.getWebServerConfName();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#getDefaultAccess()
   */
  @Override
  public String getDefaultAccess()
  {
    return pluginConfigurationService.getDefaultAccess();
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.plugins.services.PluginConfigurationService#isDefaultToolInternal()
   */
  @Override
  public boolean isDefaultToolInternal()
  {
    return pluginConfigurationService.isDefaultToolInternal();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getDefaultToolURL()
  {
    return pluginConfigurationService.getDefaultToolURL();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxAllowedProjectInstances()
  {
    return pluginConfigurationService.getMaxAllowedProjectInstances();
  }

  /**
   * This will return file extensions in a json format
   *
   * @return the file extension
   */
  public String getFileExtensions()
  {
    return fileExtensions;
  }

  /**
   * @param pFileExtensions
   *          the default pFileExtensions
   */
  public void setFileExtensions(final String pFileExtensions)
  {
    fileExtensions = pFileExtensions;
  }

  /** {@inheritDoc} */
  @Override
  public String getDataPath()
  {
    return dataPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getParserRegexp()
  {
    return parserRegexp;
  }

  /**
   * @param pParserRegexp
   *          the default ParserRegexp
   */
  public void setParserRegexp(final String pParserRegexp)
  {
    parserRegexp = pParserRegexp;
  }

  /** {@inheritDoc} */
  @Override
  public String[] getFileExtensionsArray() throws RequirementManagerException {
    String[] result = null;
    if(fileExtensions!=null && !fileExtensions.trim().isEmpty()) {
        try
    	{
    		final JSONArray json = JSONArray.fromObject(fileExtensions);
    		result = new String[json.size()];
    		for (int index = 0; index < json.size(); index++)
    		{
    			final String extension = json.getString(index);
    			result[index] = extension;
    		}
    	}
    	catch (final JSONException e)
    	{
    		throw new RequirementManagerException("Can't parse JSON array", e);
    	}
    }
    return result;
  }

  @Override
  public int getExcelFileMaxSize()
  {
    return excelFileMaxSize;
  }

  /**
   * @param excelFileMaxSize
   *          the excelFileMaxSize to set
   */
  public void setExcelFileMaxSize(int excelFileMaxSize)
  {
    this.excelFileMaxSize = excelFileMaxSize;
  }

  @Override
  public String getCodeHandlerType() {
    return codeHandlerType;
  }

  public void setCodeHandlerType(String codeHandlerType) {
    this.codeHandlerType = codeHandlerType;
  }

  /**
   * @param dataPath
   *     the dataPath to set
   */
  public void setDataPath(final String pDataPath)
  {
    dataPath = pDataPath;
  }
  
  /**
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}
