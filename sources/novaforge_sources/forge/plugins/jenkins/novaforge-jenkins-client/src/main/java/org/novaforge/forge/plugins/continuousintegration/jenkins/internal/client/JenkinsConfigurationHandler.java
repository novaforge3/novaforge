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
package org.novaforge.forge.plugins.continuousintegration.jenkins.internal.client;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.novaforge.forge.plugins.continuousintegration.jenkins.constant.Constants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class JenkinsConfigurationHandler
{
  private static final String TEMPLATE_CONFIG       = "/job/config_template.xml";

  private static final String PERMISSION_MATRIX_TAG = "hudson.security.AuthorizationMatrixProperty";

  private static final String PERMISSION_TAG        = "permission";

  private static final String JOB_TAG               = "job";

  private static final String NAME_TAG              = "name";

  private static final String PROPERTIES_TAG        = "properties";

  private static final String DESCRIPTION_TAG       = "description";

  @SuppressWarnings("unchecked")
  public List<String> getAllJobsByToken(final InputStream pConfig, final String pToken)
      throws DocumentException
  {
    List<String> jobs = new ArrayList<String>();

    Document document = new SAXReader().read(pConfig);

    for (Element job : (List<Element>) document.getRootElement().elements(JOB_TAG))
    {
      String jobName = job.elementText(NAME_TAG);
      if ((pToken == null) || ((pToken != null) && jobName.startsWith(pToken)))
      {
        jobs.add(jobName);
      }
    }

    return jobs;
  }

  public String buildConfig(final String pDescription, final Map<String, String> pMemberships)
      throws DocumentException
  {
    // build dom from the config template
    InputStream input = getClass().getResourceAsStream(TEMPLATE_CONFIG);
    Document document = new SAXReader().read(input);

    // fill description
    fillDescription(document, pDescription);

    // add permissions matrix
    buildPermissions(document, pMemberships);

    // return XML dom in String format
    return document.asXML();
  }

  private void fillDescription(final Document pDocument, final String pDescription)
  {
    Element descriptionElt = pDocument.getRootElement().element(DESCRIPTION_TAG);
    descriptionElt.setText(pDescription);
  }

  private void buildPermissions(final Document pDocument, final Map<String, String> pMemberships)
  {
    String role = null;
    String username = null;
    for (Map.Entry<String, String> entry : pMemberships.entrySet())
    {
      role = entry.getValue();
      username = entry.getKey();
      addPermissions(pDocument, username, role);
    }
  }

  private void addPermissions(final Document pDocument, final String pUserName, final String pRole)
  {
    Element permissionsMatrix = pDocument.getRootElement().element(PROPERTIES_TAG)
        .element(PERMISSION_MATRIX_TAG);

    if (permissionsMatrix == null)
    {
      permissionsMatrix = pDocument.getRootElement().element(PROPERTIES_TAG)
          .addElement(PERMISSION_MATRIX_TAG);
    }
    // default read permission
    permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Read:" + pUserName);
    if (Constants.JENKINS_ADMINISTRATOR_ROLE_ID.equals(pRole))
    {
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Run.Update:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Run.Delete:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Delete:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Build:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Workspace:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Configure:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.scm.SCM.Tag:" + pUserName);
    }
    else if (Constants.JENKINS_SCM_MANAGER_ROLE_ID.equals(pRole))
    {
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.scm.SCM.Tag:" + pUserName);
    }
    else if (Constants.JENKINS_OPERATOR_ROLE_ID.equals(pRole))
    {
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Run.Update:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Run.Delete:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Workspace:" + pUserName);
      permissionsMatrix.addElement(PERMISSION_TAG).setText("hudson.model.Item.Build:" + pUserName);
    }
  }

  public String updateConfig(final Action pAction, final InputStream pInput, final String pUserName, final String pRole)
      throws DocumentException
  {
    // build dom from input stream of initial config
    Document document = new SAXReader().read(pInput);

    switch (pAction)
    {
      case ADD_PERMISSIONS:
        addPermissions(document, pUserName, pRole);
        break;

      case UPDATE_PERMISSIONS:
        removePermissions(document, pUserName);
        addPermissions(document, pUserName, pRole);
        break;

      case DELETE_PERMISSIONS:
        removePermissions(document, pUserName);
        break;
    }

    // return XML dom in String format
    return document.asXML();
  }

  private void removePermissions(final Document pDocument, final String pUserName)
  {
    Element permissionsMatrix = pDocument.getRootElement().element(PROPERTIES_TAG).element(PERMISSION_MATRIX_TAG);

    if (permissionsMatrix != null)
    {
      @SuppressWarnings("unchecked") List<Element> elements = permissionsMatrix.elements(PERMISSION_TAG);
      for (Element permission : elements)
      {
        String userInPermission = permission.getText().split(":")[1];
        if (pUserName.equals(userInPermission))
        {
          permissionsMatrix.remove(permission);
        }
      }
    }
  }

  public enum Action
  {
    ADD_PERMISSIONS, UPDATE_PERMISSIONS, DELETE_PERMISSIONS
  }
}
