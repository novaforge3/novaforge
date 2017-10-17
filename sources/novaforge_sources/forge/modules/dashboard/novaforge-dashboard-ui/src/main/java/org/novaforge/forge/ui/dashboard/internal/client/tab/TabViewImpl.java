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
package org.novaforge.forge.ui.dashboard.internal.client.tab;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * @author Guillaume Lamirand
 */
public class TabViewImpl extends VerticalLayout implements TabView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 3529814710722435066L;
  private final CssLayout   content;
  private Component header;

  /**
   * Default constructor
   */
  public TabViewImpl()
  {
    super();
    content = new CssLayout();
    content.setSizeFull();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(final Component pHeader, final Component pContent)
  {
    // Clean container
    removeAllComponents();
    setSizeFull();

    // Add header part
    header = pHeader;
    addComponent(pHeader);
    pHeader.addStyleName(NovaForge.DASHBOARD_HEADER);
    setExpandRatio(pHeader, 0);

    // Add content part
    addComponent(content);
    content.addStyleName(NovaForge.DASHBOARD_CONTENT);
    setExpandRatio(content, 1);

    if (pContent != null)
    {
      try
      {
        setContent(pContent);
      }
      catch (final IllegalAccessException e)
      {
        // Cannot append in this case
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setContent(final Component pContent) throws IllegalAccessException
  {
    if ((header == null) || (header.getParent() == null))
    {
      throw new IllegalAccessException(
          "You didn't define any header component yet, you have to use initialize method first");
    }
    content.removeAllComponents();
    content.addComponent(pContent);
  }
}
