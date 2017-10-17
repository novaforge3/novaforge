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
package org.novaforge.forge.ui.mailing.internal.client.mailing.details;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.mailing.internal.client.containers.MailingListItemProperty;
import org.novaforge.forge.ui.mailing.internal.client.events.ShowMailingListsEvent;
import org.novaforge.forge.ui.mailing.internal.module.AbstractMailingListsPresenter;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author sbenoist
 */
public class MailingListDetailsPresenter extends AbstractMailingListsPresenter implements Serializable
{

  /**
   * Serial version uid used for serialization
   */
  private static final long            serialVersionUID = 2704843315038570848L;

  /*
   * Content of project view
   */
  private final MailingListDetailsView view;

  private MailingListBean              currentMailingList;

  /**
   * @param pPortalContext
   */
  public MailingListDetailsPresenter(final MailingListDetailsView pView, final PortalContext pPortalContext)
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
  @Override
  protected void refreshContent()
  {
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
  public void refresh(final MailingListBean pMailingList)
  {
    currentMailingList = pMailingList;
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

}
