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
package org.novaforge.forge.ui.mailing.internal.client.mailing.add;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.events.CreateMailingListEvent;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowMailingListsEvent;
import org.novaforge.forge.ui.mailing.internal.module.AbstractMailingListsPresenter;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author sbenoist
 */
public class AddMailingListPresenter extends AbstractMailingListsPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long        serialVersionUID = 2704843315038570848L;
  private static final Log LOGGER = LogFactory.getLog(AddMailingListPresenter.class);
  /*
   * Content of project view
   */
  private final AddMailingListView view;
  private MailingListBean          currentMailingList;

  /**
   * @param pPortalContext
   */
  public AddMailingListPresenter(final AddMailingListView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {

    view.getReturnButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -5090013824553391627L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowMailingListsEvent(getUuid()));

      }
    });

    view.getCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1932864170201793457L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowMailingListsEvent(getUuid()));

      }
    });

    view.getAddButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4716744769773705933L;

      /**
       * {@inheritDoc}
       */
      @SuppressWarnings("deprecation")
      @Override
      public void buttonClick(final ClickEvent event)
      {
        try
        {
          view.getMailingListForm().commit();
          try
          {
            final boolean existMailingList = MailingModule.getMailingListCategoryService().existMailingList(
                null, getMailingListInstance().toString(), getCurrentUser(), currentMailingList.getName());
            if (!existMailingList)
            {
              getEventBus().publish(new CreateMailingListEvent(currentMailingList, getUuid()));
            }
            else
            {
              final TrayNotification existNotif = new TrayNotification(MailingModule.getPortalMessages()
                  .getMessage(getCurrentLocale(), Messages.MAILING_LISTS_ADD_EXISTS,
                      currentMailingList.getName()), TrayNotificationType.WARNING);
              existNotif.show(Page.getCurrent());
            }
          }
          catch (final Exception e)
          {
            ExceptionCodeHandler.showNotificationError(MailingModule.getPortalMessages(), e, view.getLocale());
            LOGGER.error(e.getLocalizedMessage(), e);
          }
        }
        catch (final Exception e)
        {
          TrayNotification.show(
              MailingModule.getPortalMessages().getMessage(getCurrentLocale(), e.getLocalizedMessage()),
              TrayNotificationType.WARNING);

        }
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  @Override
  protected void refreshContent()
  {
    currentMailingList = MailingModule.getMailingListCategoryService().newMailingList();
    final BeanItem<MailingListBean> mailingListItem = new BeanItem<MailingListBean>(currentMailingList);
    view.getMailingListForm().setItemDataSource(mailingListItem);
    view.getMailingListForm().setVisibleItemProperties(MailingListItemProperty.NAME.getPropertyId(),
        MailingListItemProperty.TYPE.getPropertyId(), MailingListItemProperty.SUBJECT.getPropertyId(),
        MailingListItemProperty.DESCRIPTION.getPropertyId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());

  }

}
