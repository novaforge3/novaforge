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
package org.novaforge.forge.ui.portal.event.actions;

import com.vaadin.event.Action;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;

/**
 * This class describes default behaviour to add update action on {@link Table} or {@link Tree}
 * 
 * @author Guillaume Lamirand
 */
public abstract class RefreshAction implements Action.Handler
{

  /**
   * Serial version id
   */
  private static final long   serialVersionUID = 8749999788468344743L;

  /**
   * Refresh action id
   */
  private static final String REFRESH          = "refresh";           //$NON-NLS-1$

  /**
   * {@inheritDoc}
   */
  @Override
  public Action[] getActions(final Object pTarget, final Object pSender)
  {
    final String message = getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.ACTIONS_REFRESH);
    return new Action[] { new PortalAction(REFRESH, message) };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleAction(final Action pAction, final Object pSender, final Object pTarget)
  {
    if ((pAction instanceof PortalAction) && (REFRESH.equals(((PortalAction) pAction).getId())))
    {
      refreshAction();
    }

  }

  /**
   * This method will be called when user click on refresh action
   */
  public abstract void refreshAction();

  /**
   * This method should return service implementation of {@link PortalMessages}
   * 
   * @return {@link PortalMessages} implementation
   */
  public abstract PortalMessages getPortalMessages();

}
