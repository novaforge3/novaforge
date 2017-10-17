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
package org.novaforge.forge.tools.deliverymanager.ui.server;

import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.CustomFieldType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.ECMNode;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;
import org.novaforge.forge.tools.deliverymanager.model.SCMNode;
import org.novaforge.forge.tools.deliverymanager.model.TemplateCustomField;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ArtefactNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.BugTrackerIssueDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentTypeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryStatusDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.FolderNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateFieldTypeDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class Resources
{
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static DeliveryDTO buildDTO(final Delivery pDelivery)
  {
    final DeliveryDTO dto = new DeliveryDTO();

    dto.setReference(pDelivery.getReference());
    dto.setType(pDelivery.getType().getLabel());
    dto.setName(pDelivery.getName());
    dto.setVersion(pDelivery.getVersion());
    if (pDelivery.getDate() != null)
    {
      dto.setDeliveryDate(new Date(pDelivery.getDate().getTime()));
    }
    dto.setStatus(buildDTO(pDelivery.getStatus()));

    return dto;

  }

  public static DeliveryStatusDTO buildDTO(final DeliveryStatus pDelivery)
  {
    return DeliveryStatusDTO.valueOf(pDelivery.name());

  }

  public static ContentTypeDTO buildDTO(final ContentType pContentType)
  {
    return ContentTypeDTO.getById(pContentType.getId());

  }

  public static ContentDTO buildDTO(final Content pContent)
  {
    final ContentDTO content = new ContentDTO();
    content.setType(buildDTO(pContent.getType()));
    content.setRoot((FolderNode) buildDTO(pContent.getNode()));
    return content;

  }

  public static NodeDTO buildDTO(final Node pNode)
  {
    NodeDTO returnNodeDTO = null;
    if (pNode instanceof Folder)
    {
      returnNodeDTO = buildDTO((Folder) pNode);
    }
    else if (pNode instanceof Artefact)
    {
      returnNodeDTO = buildDTO((Artefact) pNode);
    }
    return returnNodeDTO;

  }

  public static NodeDTO buildDTO(final Folder pNode)
  {
    final FolderNode folder = new FolderNode();
    folder.setName(pNode.getName());
    folder.setPath(pNode.getPath());

    // Build children
    final List<NodeDTO> children = new ArrayList<NodeDTO>();
    final List<Node> childNodes = pNode.getChildNodes();
    for (final Node node : childNodes)
    {
      children.add(buildDTO(node));
    }
    folder.setChildren(children);
    return folder;

  }

  public static Node build(final NodeDTO pNodeDTO)
  {
    Node returnNode = null;
    if (pNodeDTO instanceof FolderNode)
    {
      final FolderNode folder = (FolderNode) pNodeDTO;
      final Folder node = OSGiServiceGetter.getDeliveryManager().newFolder();
      node.setName(pNodeDTO.getName());
      node.setPath(pNodeDTO.getPath());
      final List<NodeDTO> childrenDTO = folder.getChildren();
      if (childrenDTO != null)
      {
        for (final NodeDTO childDTO : childrenDTO)
        {
          node.addChildNode(build(childDTO));
        }
      }
      returnNode = node;
    }
    else if (pNodeDTO instanceof ArtefactNode)
    {
      final ArtefactNode artefact = (ArtefactNode) pNodeDTO;
      final Artefact node = OSGiServiceGetter.getDeliveryManager().newArtefact();
      node.setName(pNodeDTO.getName());
      node.setPath(pNodeDTO.getPath());
      node.setIdentifiant(artefact.getID());
      node.setParameters(build(artefact.getFields()));

      returnNode = node;
    }
    return returnNode;
  }

  public static List<ArtefactParameter> build(final Map<String, String> pFieldDTO)
  {
    final List<ArtefactParameter> parameters = new ArrayList<ArtefactParameter>();
    for (final String key : pFieldDTO.keySet())
    {
      final ArtefactParameter param = OSGiServiceGetter.getDeliveryManager().newArtefactParameter();
      param.setKey(key);
      param.setValue(pFieldDTO.get(key));
      parameters.add(param);
    }
    return parameters;
  }

  public static NodeDTO buildDTO(final Artefact pNode)
  {
    final ArtefactNode artefact = new ArtefactNode();
    artefact.setName(pNode.getName());
    artefact.setPath(pNode.getPath());
    artefact.setID(pNode.getIdentifiant());
    final Map<String, String> params = new HashMap<String, String>();
    final List<ArtefactParameter> artefactParams = pNode.getParameters();
    if (artefactParams != null)
    {
      for (final ArtefactParameter param : artefactParams)
      {
        params.put(param.getKey(), param.getValue());
      }
      artefact.setFields(params);
    }
    return artefact;

  }

  public static ContentType build(final ContentTypeDTO pContentType)
  {
    return ContentType.getById(pContentType.getId());

  }

  public static BugTrackerIssueDTO buildDTO(final BugTrackerIssue pIssue)
  {
    final BugTrackerIssueDTO issueDTO = new BugTrackerIssueDTO();
    issueDTO.setId(pIssue.getId());
    issueDTO.setTitle(pIssue.getTitle());
    issueDTO.setDescription(pIssue.getDescription());
    issueDTO.setCategory(pIssue.getCategory());
    issueDTO.setSeverity(pIssue.getSeverity());
    issueDTO.setReporter(pIssue.getReporter());
    return issueDTO;
  }

  public static List<NodeDTO> buildECMDTO(final List<ECMNode> pECMNodes, final String pPath)
  {
    final List<NodeDTO> result = new ArrayList<NodeDTO>();
    for (final ECMNode ecmNode : pECMNodes)
    {
      final NodeDTO currentNode = Resources.buildDTO(ecmNode);
      currentNode.setPath(pPath);
      result.add(currentNode);
      if (!ecmNode.isDocument())
      {
        ((FolderNode) currentNode).setChildren(buildECMDTO(ecmNode.getChildren(), currentNode.getPath()));

      }
    }
    return result;
  }

  public static List<NodeDTO> buildSCMDTO(final List<SCMNode> pSCMNodes, final String pPath)
  {
    final List<NodeDTO> result = new ArrayList<NodeDTO>();
    for (final SCMNode scmNode : pSCMNodes)
    {
      final NodeDTO currentNode = Resources.buildDTO(scmNode);
      currentNode.setPath(pPath);
      result.add(currentNode);
      if (scmNode.isDirectory())
      {
        ((FolderNode) currentNode).setChildren(buildSCMDTO(scmNode.getChildren(), currentNode.getPath()));

      }
    }
    return result;
  }

  public static NodeDTO buildDTO(final ECMNode pECMNode)
  {
    NodeDTO result;
    if (pECMNode.isDocument())
    {
      result = new ArtefactNode();
      ((ArtefactNode) result).setID(pECMNode.getId());
      ((ArtefactNode) result).getFields().put("author", pECMNode.getAuthor());
      if (pECMNode.getCreatedDate() != null)
      {
        ((ArtefactNode) result).getFields().put("createdDate",
            dateFormat.format(new Date(pECMNode.getCreatedDate().getTime())));
      }

      ((ArtefactNode) result).getFields().put("lastModifiedAuthor", pECMNode.getLastModified());

      if (pECMNode.getLastModifiedDate() != null)
      {
        ((ArtefactNode) result).getFields().put("lastModifiedDate",
            dateFormat.format(new Date(pECMNode.getLastModifiedDate().getTime())));
      }

    }
    else
    {
      result = new FolderNode();
    }
    result.setName(pECMNode.getName());
    return result;
  }

  public static NodeDTO buildDTO(final SCMNode pSCMNode)
  {
    // Get node name from its path
    NodeDTO result;
    if (pSCMNode.isDirectory())
    {
      result = new FolderNode();
    }
    else
    {
      result = new ArtefactNode();
      ((ArtefactNode) result).setID(pSCMNode.getPath());
      ((ArtefactNode) result).getFields().put("author", pSCMNode.getAuthor());
      ((ArtefactNode) result).getFields().put("revision", pSCMNode.getRevision());
    }

    result.setPath(pSCMNode.getPath());
    final String[] split = pSCMNode.getPath().split("/");
    final String name = split[split.length - 1];
    result.setName(name);
    return result;
  }

  public static List<TemplateDTO> buildDTO(final List<TemplateReport> pTemplates)
  {
    final List<TemplateDTO> templateList = new ArrayList<TemplateDTO>();
    for (final TemplateReport templateReport : pTemplates)
    {
      templateList.add(Resources.buildDTO(templateReport));
    }
    return templateList;

  }

  public static TemplateDTO buildDTO(final TemplateReport pTemplate)
  {
    final TemplateDTO templateDTO = new TemplateDTO();
    templateDTO.setName(pTemplate.getName());
    templateDTO.setDescription(pTemplate.getDescription());
    templateDTO.setFileName(pTemplate.getFileName());
    final List<TemplateFieldDTO> fields = new ArrayList<TemplateFieldDTO>();
    for (final TemplateCustomField field : pTemplate.getFields())
    {
      fields.add(builDTO(field));
    }
    templateDTO.setFields(fields);
    return templateDTO;
  }

  private static TemplateFieldDTO builDTO(final TemplateCustomField pField)
  {
    final TemplateFieldDTO templateFieldDTO = new TemplateFieldDTO();
    templateFieldDTO.setName(pField.getName());
    templateFieldDTO.setDescription(pField.getDescription());
    templateFieldDTO.setNew(false);
    templateFieldDTO.setType(TemplateFieldTypeDTO.valueOf(pField.getType().name()));
    return templateFieldDTO;
  }

  public static TemplateReport build(final TemplateDTO pTemplate)
  {
    final TemplateReport template = OSGiServiceGetter.getTemplateReportManager().newTemplate();
    template.setName(pTemplate.getName());
    template.setDescription(pTemplate.getDescription());
    template.setFileName(pTemplate.getFileName());
    for (final TemplateFieldDTO field : pTemplate.getFields())
    {
      template.addField(build(field));
    }
    return template;
  }

  private static TemplateCustomField build(final TemplateFieldDTO pField)
  {
    final TemplateCustomField field = OSGiServiceGetter.getTemplateReportManager().newCustomField();
    field.setName(pField.getName());
    field.setDescription(pField.getDescription());
    field.setType(CustomFieldType.valueOf(pField.getType().name()));
    return field;
  }
}
