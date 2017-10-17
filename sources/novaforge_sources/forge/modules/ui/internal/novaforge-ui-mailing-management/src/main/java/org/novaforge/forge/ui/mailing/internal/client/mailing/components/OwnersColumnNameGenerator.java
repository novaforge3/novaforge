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
package org.novaforge.forge.ui.mailing.internal.client.mailing.components;

import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.mailing.update.MailingListsPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.client.component.MailToLink;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;

import java.util.List;

/**
 * @author B-Martinelli
 */
public class OwnersColumnNameGenerator extends AbstractColumnNameGenerator
{
  /** Serial version id */
  private static final long           serialVersionUID = 1175065031848795493L;

  private static final Log            LOGGER           = LogFactory.getLog(OwnersColumnNameGenerator.class);
  private final MailingListsPresenter mailingListsPresenter;

  /**
   * @param mailingListsPresenter
   */
  public OwnersColumnNameGenerator(final MailingListsPresenter mailingListsPresenter)
  {
    super();
    this.mailingListsPresenter = mailingListsPresenter;
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final HorizontalLayout layout = new HorizontalLayout();
    final Item item = pSource.getItem(pItemId);
    final List<MailingListUser> ownersInfo = (List<MailingListUser>) item.getItemProperty(
        MailingListItemProperty.OWNERS.getPropertyId()).getValue();
    if ((ownersInfo != null) && (!ownersInfo.isEmpty()))
    {
      for (final MailingListUser anOwner : ownersInfo)
      {
        AbstractComponent ownerComponent = null;
        if (anOwner.isExternal())
        {
          ownerComponent = new MailToLink(anOwner.getEmail());
        }
        else
        {
          try
          {
            final UserProfile userProfile = MailingModule.getUserPresenter().getUserProfile(
                anOwner.getLogin());
            final User user = userProfile.getUser();
            ownerComponent = new Button(user.getFirstName() + " " + user.getName());
            // ownerComponent = new Button(getSubscriberInfo(user));
            ownerComponent.setStyleName(Reindeer.BUTTON_LINK);
            final Button ownerLink = (Button) ownerComponent;
            if (userProfile.getImage() != null)
            {
              ownerLink.setIcon(ResourceUtils.buildImageResource(userProfile.getImage().getFile(),
                  user.getLogin()));
            }
            else
            {
              ownerLink.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_UNKNOW));
            }
            ownerLink.addClickListener(new ClickListener()
            {
              /**
               * Serial version id
               */
              private static final long serialVersionUID = 4799439581656895309L;

              @Override
              public void buttonClick(final ClickEvent event)
              {
                mailingListsPresenter.getPortalContext().getEventBus()
                    .publish(new OpenUserProfileEvent(userProfile));
              }
            });
          }
          catch (final UserServiceException e)
          {
            LOGGER.error(e.getLocalizedMessage(), e);
            ownerComponent = new MailToLink(anOwner.getEmail());
          }

        }
        ownerComponent.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MAILING_LISTS_OWNERS_DESCRIPTION));
        layout.addComponent(ownerComponent);
      }
    }
    else
    {
      final Label noRole = new Label(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MAILING_LISTS_OWNERS_NO_OWNER));
      noRole.setDescription(MailingModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MAILING_LISTS_OWNERS_NO_OWNER_DESCRIPTION));
      layout.addComponent(noRole);
    }
    return layout;
  }
}
