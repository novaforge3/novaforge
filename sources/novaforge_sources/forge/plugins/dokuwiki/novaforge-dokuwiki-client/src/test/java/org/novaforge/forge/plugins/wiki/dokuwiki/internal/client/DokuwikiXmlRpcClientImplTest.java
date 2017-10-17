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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.client;

import org.apache.ws.commons.util.Base64;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.junit.Test;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiXmlRpcClient;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuWikiUserImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiPageImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachment;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachmentInfo;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPage;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPageInfo;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiRolePrivilege;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author lamirang
 */
public class DokuwikiXmlRpcClientImplTest
{

  private static final String TEST_LOGIN               = "test_login";
  private static final String TEST_LASTNAME            = "test_lastname";
  private static final String TEST_FIRSTNAME           = "test_firstname";
  private static final String TEST_EMAIL               = "test_";
  private static final String TEST_PWD                 = "test123";

  private static final String TEST_PWD_UPDATED         = "test321";

  private static final String USER_UPDATED             = "test2";

  private static final String TEST_INSTANCE            = "testinstance";
  private static final String TEST_PROJECT             = "testproject";
  private static final String TEST_SUMMARY             = "a test page";
  private static final String TEST_CONTENT             = "test content";

  private static final String DOKUWIKI_RPC_SERVER      = "http://127.0.0.1/dokuwiki/lib/exe/xmlrpc.php";
  private static final String ADMIN_USER               = "root";
  private static final String ADMIN_PWD                = "root";

  private static final String UNDERSCORE               = "_";
  private static final String START_PAGE               = ":start";

  private boolean             dokuwikiProfileActivated = false;

  public DokuwikiXmlRpcClientImplTest()
  {
    final String property = System.getProperty("dokuwiki.profile");
    if ("true".equals(property))
    {
      dokuwikiProfileActivated = true;
    }
  }

  @Test
  public void connectTest() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClient dokuClient = new DokuwikiXmlRpcClientImpl();
      dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
    }
  }

  @Test
  public void createUser() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();
      final DokuWikiUser user = new DokuWikiUserImpl();
      user.setUserName(TEST_LOGIN);
      user.setFisrtName(TEST_FIRSTNAME);
      user.setLastName(TEST_LASTNAME);
      user.setEmail(TEST_EMAIL);
      user.setPassword(TEST_PWD);
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.createUser(connector, user, false);
    }
  }

  /**
   * @return
   *
   * @throws DokuwikiClientException
   */
  protected DokuwikiXmlRpcClientImpl getClient() throws DokuwikiClientException
  {
    return new DokuwikiXmlRpcClientImpl();
  }

  @Test
  public void updateUser() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final DokuWikiUser user2 = new DokuWikiUserImpl();
      user2.setUserName(USER_UPDATED);
      user2.setFisrtName(TEST_FIRSTNAME);
      user2.setLastName(TEST_LASTNAME);
      user2.setEmail("");
      user2.setPassword(TEST_PWD_UPDATED);
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.updateUser(connector, TEST_LOGIN, user2, false);
    }
  }

  @Test
  public void hasUser() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      assertTrue(dokuClient.hasUser(connector, USER_UPDATED));
    }
  }

  @Test
  public void createPage() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = new DokuwikiXmlRpcClientImpl();
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      final DokuwikiPage wikiPage = new DokuwikiPageImpl();
      wikiPage.setName(TEST_PROJECT + UNDERSCORE + TEST_INSTANCE + START_PAGE);
      wikiPage.setDescription(TEST_SUMMARY);
      wikiPage.setContent(TEST_CONTENT);
      dokuClient.createPage(connector, wikiPage);
    }
  }

  @Test
  public void setUserPermissionToNameSpace() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final StringBuilder project = new StringBuilder();
      project.append(TEST_PROJECT).append(UNDERSCORE).append(TEST_INSTANCE);
      final String projectName = project.toString();
      final DokuwikiResourceBuilder dokuwikiResourceBuilder = mock(DokuwikiResourceBuilder.class);
      when(dokuwikiResourceBuilder.buildStartingPage(projectName)).thenReturn(
          project.append(START_PAGE).toString());

      dokuClient.setDokuwikiResourceBuilder(dokuwikiResourceBuilder);
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.setUserPermissionToNameSpace(connector, projectName, USER_UPDATED,
          DokuwikiRolePrivilege.ADMINISTRATOR.getId());
    }
  }

  @Test
  public void updateUserPermissionToNameSpace() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final StringBuilder project = new StringBuilder();
      project.append(TEST_PROJECT).append(UNDERSCORE).append(TEST_INSTANCE);
      final String projectName = project.toString();
      final DokuwikiResourceBuilder dokuwikiResourceBuilder = mock(DokuwikiResourceBuilder.class);
      when(dokuwikiResourceBuilder.buildStartingPage(projectName)).thenReturn(
          project.append(START_PAGE).toString());

      dokuClient.setDokuwikiResourceBuilder(dokuwikiResourceBuilder);

      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.setUserPermissionToNameSpace(connector, projectName, USER_UPDATED,
          DokuwikiRolePrivilege.ADMINISTRATOR.getId());
    }
  }

  @Test
  public void deleteUserPermissionToNameSpace() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final StringBuilder project = new StringBuilder();
      project.append(TEST_PROJECT).append(UNDERSCORE).append(TEST_INSTANCE);
      final String projectName = project.toString();
      final DokuwikiResourceBuilder dokuwikiResourceBuilder = mock(DokuwikiResourceBuilder.class);
      when(dokuwikiResourceBuilder.buildStartingPage(projectName)).thenReturn(
          project.append(START_PAGE).toString());

      dokuClient.setDokuwikiResourceBuilder(dokuwikiResourceBuilder);
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.deleteUserPermission(connector, projectName, USER_UPDATED);
    }
  }

  @Test
  public void deletePage() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.deletePage(connector, TEST_PROJECT + UNDERSCORE + TEST_INSTANCE + START_PAGE);
    }
  }

  @Test
  public void deleteUser() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final DokuWikiUser user2 = new DokuWikiUserImpl();
      user2.setUserName(USER_UPDATED);
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.deleteUser(connector, user2);
    }
  }

  @Test
  public void getPageList() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final DokuwikiPage start = new DokuwikiPageImpl();
      start.setContent("Content of test");
      start.setDescription("Content of test");
      start.setName("test_project:start");
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      dokuClient.createPage(connector, start);
      final List<DokuwikiPageInfo> pages = dokuClient.getNameSpacePages(connector, "test_project");
      for (final DokuwikiPageInfo dokuwikiPageInfo : pages)
      {
        final DokuwikiPage pageContent = dokuClient.getPageContent(connector, dokuwikiPageInfo.getId());
        pageContent.setName("new_" + pageContent.getName());
        dokuClient.createPage(connector, pageContent);
      }
    }
  }

  @Test
  public void getAttachments() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      final List<DokuwikiAttachmentInfo> attachments = dokuClient.getAttachments(connector, "wiki", true);
      assertThat(attachments, not(nullValue()));
      for (final DokuwikiAttachmentInfo attachmentInfo : attachments)
      {
        System.out.println("Attachment ID: " + attachmentInfo.getId());
        System.out.println("Attachment size: " + attachmentInfo.getSize());
        System.out.println("Attachment last modified date: " + attachmentInfo.getLastModified());
        System.out.println("Attachment isImg: " + attachmentInfo.isImg());
        System.out.println("Attachment writable: " + attachmentInfo.isWritable());
        System.out.println("Attachment permissions: " + attachmentInfo.getPermissions());
        System.out.println("\n");
      }
    }
  }

  @Test
  public void getAttachment() throws DokuwikiClientException, Base64.DecodingException
  {
    if (dokuwikiProfileActivated)
    {
      final DokuwikiXmlRpcClientImpl dokuClient = getClient();

      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      final DokuwikiAttachment attachment = dokuClient.getAttachment(connector, "wiki:dokuwiki-128.png");
      assertThat(attachment, not(nullValue()));
      System.out.println("Attachment ID: " + attachment.getId());
      System.out.println("Attachment content (base64): " + attachment.getContent());
      System.out.println("Attachment content : " + Arrays.toString(Base64.decode(attachment.getContent())));
      System.out.println("\n");
    }
  }

  @Test
  public void putAndDeleteAttachment() throws DokuwikiClientException
  {
    if (dokuwikiProfileActivated)
    {

      final DokuwikiXmlRpcClientImpl dokuClient = getClient();
      final XmlRpcClient connector = dokuClient.getConnector(DOKUWIKI_RPC_SERVER, ADMIN_USER, ADMIN_PWD);
      DokuwikiAttachment attachment = dokuClient.getAttachment(connector, "wiki:dokuwiki-128.png");

      dokuClient.putAttachment(connector, attachment);

      attachment = dokuClient.getAttachment(connector, "wiki:dokuwiki-128-backup.png");
      assertThat(attachment, not(nullValue()));
      assertThat(attachment.getContent(), not(nullValue()));

      dokuClient.deleteAttachment(connector, "wiki:dokuwiki-128-backup.png");
    }
  }
}