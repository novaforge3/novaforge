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
package org.novaforge.forge.ui.portal.client.component;

import com.google.common.base.Strings;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Notification;

/**
 * @author Jeremy Casery
 */
public class TrayNotification extends Notification
{

  /**
   *
   */
  private static final long serialVersionUID = 5941139356315074785L;
  private static String HTML_BR = "<br/>";

  public TrayNotification(final String pCaption)
  {
    this(pCaption, null, TrayNotificationType.SUCCESS);
  }

  public TrayNotification(final String pCaption, final String pDescription, final TrayNotificationType pType)
  {
    super(pCaption, Type.TRAY_NOTIFICATION);
    if (!Strings.isNullOrEmpty(pDescription))
    {
      final String brDescription = HTML_BR + pDescription;
      setDescription(brDescription);
    }
    setStyleName(pType.getStyleName());
    setIcon(new ThemeResource(pType.getIcon()));
    setHtmlContentAllowed(true);
  }

  public TrayNotification(final String pCaption, final TrayNotificationType pType)
  {
    this(pCaption, null, pType);
  }

  public TrayNotification(final TrayNotificationType pType)
  {
    this(null, null, pType);
  }

  public static void show(final String caption)
  {
    new TrayNotification(caption).show(Page.getCurrent());
  }

  public static void show(final String caption, final TrayNotificationType type)
  {
    new TrayNotification(caption, type).show(Page.getCurrent());
  }

  public static void show(final String caption, final String description, final TrayNotificationType type)
  {
    new TrayNotification(caption, description, type).show(Page.getCurrent());
  }
}
