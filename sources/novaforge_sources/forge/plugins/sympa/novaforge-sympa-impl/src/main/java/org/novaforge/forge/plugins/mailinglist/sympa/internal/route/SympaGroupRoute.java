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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListCategoryService;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.core.plugins.domain.plugin.PluginGroup;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueHeader;
import org.novaforge.forge.plugins.categories.beans.MailingListGroupImpl;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;
import org.novaforge.forge.plugins.commons.utils.route.RouteUtil;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.SubscriptionDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.model.GroupSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.route.SympaQueueName;

import java.util.List;

/**
 * @author sbenoist
 */
public class SympaGroupRoute extends RouteBuilder
{
  private static final Log           LOG = LogFactory.getLog(SympaGroupRoute.class);

  private MailingListCategoryService categoryService;

  private SubscriptionDAO            subscriptionDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure() throws Exception
  {
    // The recipient is dynamically determined with the content of the header message from the forge
    from(SympaQueueName.GROUP.getQueueName()).id(SympaQueueName.GROUP.getRouteId()).process(new Processor()
    {

      @Override
      public void process(final Exchange pExchange)
      {
        PluginGroup group = null;
        try
        {
          LOG.info(SympaQueueName.GROUP.getQueueName() + "-Get a message.");

          // get the informations about the instance into the header
          final String instanceId = (String) pExchange.getIn().getHeader(
              PluginQueueHeader.INSTANCE_UUID_HEADER);

          final PluginQueueAction action = PluginQueueAction.fromLabel((String) pExchange.getIn().getHeader(
              PluginQueueHeader.ACTION_HEADER));

          final String forgeId = (String) pExchange.getIn().getHeaders()
              .get(PluginQueueHeader.FORGE_ID_HEADER);

          final String currentUser = (String) pExchange.getIn().getHeader(PluginQueueHeader.USER_NAME_HEADER);

          // get user in body of message
          group = pExchange.getIn().getBody(PluginGroup.class);
          switch (action)
          {
            case CREATE:
              // Not taken in account this case
              break;
            case UPDATE:
              updateGroupSubscriptions(group, instanceId, forgeId, currentUser);
              break;
            case DELETE:
              deleteGroupSubscriptions(group, instanceId, forgeId, currentUser);
              break;
          }

          // forward all the headers from the input
          pExchange.getOut().setHeaders(pExchange.getIn().getHeaders());

          // send the success response
          pExchange.getOut().setHeader(PluginQueueHeader.STATUS_RESPONSE_HEADER, PluginQueueHeader.STATUS_OK);
        }
        catch (final Exception e)
        {
          // build the response body with the requested object and the exception
          RouteUtil.buildErrorMessage(pExchange, e, group);
          LOG.error("an error occured during group propagation", e);
        }
      }
    });
  }

  private MailingListGroup toListGroup(final PluginGroup pGroup)
  {
    final MailingListGroup listGroup = new MailingListGroupImpl();
    listGroup.setName(pGroup.getName());
    listGroup.setUUID(pGroup.getUUID());
    for (final PluginUser user : pGroup.getUsers())
    {
      listGroup.addMember(toListUser(user));
    }

    return listGroup;
  }

  private MailingListUser toListUser(final PluginUser pUser)
  {
    final MailingListUser user = new MailingListUserImpl(pUser.getLogin(), pUser.getEmail());
    user.setExternal(false);
    return user;
  }

  private void deleteGroupSubscriptions(final PluginGroup pGroup, final String pInstanceId,
      final String pForgeId, final String pCurrentUser) throws MailingListServiceException
  {
    final List<GroupSubscription> groupSubscriptions = subscriptionDAO.findAllByGroup(pGroup.getUUID());
    for (final GroupSubscription groupSubscription : groupSubscriptions)
    {
      categoryService.removeGroupSubscription(pForgeId, pInstanceId, pCurrentUser,
          groupSubscription.getListname(), toListGroup(pGroup), true);
    }
  }

  private void updateGroupSubscriptions(final PluginGroup pGroup, final String pInstanceId,
      final String pForgeId, final String pCurrentUser) throws MailingListServiceException
  {
    final List<GroupSubscription> groupSubscriptions = subscriptionDAO.findAllByGroup(pGroup.getUUID());
    for (final GroupSubscription groupSubscription : groupSubscriptions)
    {
      categoryService.updateGroupSubscription(pForgeId, pInstanceId, pCurrentUser,
          groupSubscription.getListname(), toListGroup(pGroup), true);
    }
  }

  /**
   * @param categoryService
   *          the categoryService to set
   */
  public void setCategoryService(final MailingListCategoryService categoryService)
  {
    this.categoryService = categoryService;
  }

  /**
   * @param subscriptionDAO
   *          the subscriptionDAO to set
   */
  public void setSubscriptionDAO(final SubscriptionDAO pSubscriptionDAO)
  {
    this.subscriptionDAO = pSubscriptionDAO;
  }

}
