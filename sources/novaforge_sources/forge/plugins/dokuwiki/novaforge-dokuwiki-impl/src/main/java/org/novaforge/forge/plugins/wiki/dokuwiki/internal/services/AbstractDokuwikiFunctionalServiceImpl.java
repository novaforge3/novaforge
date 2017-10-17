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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.services;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiXmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiConfigurationService;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiFunctionalException;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiFunctionalService;

import java.util.List;

public abstract class AbstractDokuwikiFunctionalServiceImpl implements DokuwikiFunctionalService
{

  protected DokuwikiXmlRpcClient         dokuwikiXmlRpcClient;

  protected DokuwikiResourceBuilder      dokuwikiResourceBuilder;

  protected DokuwikiConfigurationService dokuwikiConfigurationService;

  private InstanceConfigurationDAO       instanceConfigurationDAO;

  @Override
  public List<ItemReferenceDTO> getDataInfos(final String forgeId, final String instanceId)
      throws DokuwikiFunctionalException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(instanceId);

    // Check if instance got is mapped to the correct forge id
    checkForgeIf(forgeId, instance);

    // Build the return list
    List<ItemReferenceDTO> pagesInfo;
    try
    {

      final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
          dokuwikiConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

      pagesInfo = this.getDataInfos(connector, instance.getToolProjectId());
    }
    catch (final DokuwikiClientException e)
    {
      throw new DokuwikiFunctionalException(String.format(
          "Unable to get wiki data info with [wiki_project_id=%s]", instance.getToolProjectId()), e);
    }
    catch (final PluginServiceException e)
    {
      throw new DokuwikiFunctionalException(String.format("Unable to get build connecter with [instance=%s]",
          instance), e);
    }
    return pagesInfo;
  }

  @Override
  public DataDTO getData(final String forgeId, final String instanceId, final ItemDTO item)
      throws DokuwikiFunctionalException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(instanceId);

    // Check if instance got is mapped to the correct forge id
    checkForgeIf(forgeId, instance);

    DataDTO dataContent = null;
    if (item != null)
    {
      try
      {
        final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
            dokuwikiConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
            dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

        dataContent = getDataContent(connector, item);
      }
      catch (final DokuwikiClientException e)
      {
        throw new DokuwikiFunctionalException(String.format(
            "Unable to get wiki content with [wiki_project_id=%s, page_id=%s]", instance.getToolProjectId(),
            item), e);
      }
      catch (final PluginServiceException e)
      {
        throw new DokuwikiFunctionalException(String.format(
            "Unable to get build connecter with [instance=%s]", instance), e);
      }
    }
    return dataContent;
  }

  @Override
  public void putData(final String forgeId, final String instanceId, final DataDTO data)
      throws DokuwikiFunctionalException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(instanceId);

    // Check if instance got is mapped to the correct forge id
    checkForgeIf(forgeId, instance);

    if (data != null)
    {
      try
      {
        final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
            dokuwikiConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
            dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

        createData(connector, data);
      }
      catch (final DokuwikiClientException e)
      {
        throw new DokuwikiFunctionalException(String.format(
            "Unable to put wiki content with [wiki_project_id=%s]", instance.getToolProjectId()), e);
      }
      catch (final PluginServiceException e)
      {
        throw new DokuwikiFunctionalException(String.format(
            "Unable to get build connecter with [instance=%s]", instance), e);
      }
    }
  }

  @Override
  public void deleteData(final String forgeId, final String instanceId, final ItemDTO item)
      throws DokuwikiFunctionalException
  {
    // Get instance object
    final InstanceConfiguration instance = getInstance(instanceId);

    // Check if instance got is mapped to the correct forge id
    checkForgeIf(forgeId, instance);

    if (item != null)
    {
      try
      {
        final XmlRpcClient connector = dokuwikiXmlRpcClient.getConnector(
            dokuwikiConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
            dokuwikiConfigurationService.getClientAdmin(), dokuwikiConfigurationService.getClientPwd());

        this.deleteData(connector, item);
      }
      catch (final DokuwikiClientException e)
      {
        throw new DokuwikiFunctionalException(String.format(
            "Unable to delete wiki content with [wiki_project_id=%s]", instance.getToolProjectId()), e);
      }
      catch (final PluginServiceException e)
      {
        throw new DokuwikiFunctionalException(String.format(
            "Unable to get build connecter with [instance=%s]", instance), e);
      }
    }
  }

  protected abstract void deleteData(XmlRpcClient connector, ItemDTO item) throws DokuwikiClientException;

  protected abstract void createData(XmlRpcClient connector, DataDTO data) throws DokuwikiClientException;

  protected abstract DataDTO getDataContent(XmlRpcClient connector, ItemDTO item)
      throws DokuwikiClientException;

  /**
   * @param pInstanceId
   * @return
   * @throws DokuwikiFunctionalException
   */
  private InstanceConfiguration getInstance(final String pInstanceId) throws DokuwikiFunctionalException
  {
    return instanceConfigurationDAO.findByInstanceId(pInstanceId);
  }

  /**
   * @param pForgeId
   * @param instance
   * @throws DokuwikiFunctionalException
   */
  private void checkForgeIf(final String pForgeId, final InstanceConfiguration instance)
      throws DokuwikiFunctionalException
  {
    if (instance != null)
    {
      if (!instance.getForgeId().equals(pForgeId))
      {
        throw new DokuwikiFunctionalException(
            "The forge id given in parameter doesn''t match with the instance id");
      }
    }
  }

  protected abstract List<ItemReferenceDTO> getDataInfos(XmlRpcClient connector, String projectId)
      throws DokuwikiClientException;

  /**
   * Use by container to inject {@link DokuwikiXmlRpcClient}
   * 
   * @param pDokuwikiXmlRpcClient
   *          the dokuwikiXmlRpcClient to set
   */
  public void setDokuwikiXmlRpcClient(final DokuwikiXmlRpcClient pDokuwikiXmlRpcClient)
  {
    dokuwikiXmlRpcClient = pDokuwikiXmlRpcClient;
  }

  /**
   * Use by container to inject {@link DokuwikiResourceBuilder}
   * 
   * @param pDokuwikiResourceBuilder
   *          the dokuwikiResourceBuilder to set
   */
  public void setDokuwikiResourceBuilder(final DokuwikiResourceBuilder pDokuwikiResourceBuilder)
  {
    dokuwikiResourceBuilder = pDokuwikiResourceBuilder;
  }

  /**
   * Use by container to inject {@link DokuwikiConfigurationService}
   * 
   * @param pDokuwikiConfigurationService
   *          the dokuwikiConfigurationService to set
   */
  public void setDokuwikiConfigurationService(final DokuwikiConfigurationService pDokuwikiConfigurationService)
  {
    dokuwikiConfigurationService = pDokuwikiConfigurationService;
  }

  /**
   * Use by container to inject {@link InstanceConfigurationDAO}
   * 
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

}
