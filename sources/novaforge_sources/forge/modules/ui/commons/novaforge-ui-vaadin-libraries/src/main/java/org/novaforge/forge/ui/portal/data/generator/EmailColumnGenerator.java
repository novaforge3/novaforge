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
package org.novaforge.forge.ui.portal.data.generator;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.ui.portal.client.component.MailToLink;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;

/**
 * @author Guillaume Lamirand
 */
public class EmailColumnGenerator implements ColumnGenerator
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -8264106453272059490L;
  /**
   * The property id used to retrieve email property from item
   */
  private final String      emailId;

  public EmailColumnGenerator()
  {
    emailId = UserItemProperty.EMAIL.getPropertyId();
  }

  public EmailColumnGenerator(final String pEmailProperty)
  {
    emailId = pEmailProperty;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table pSource, final Object pItemId, final Object pColumnId)
  {
    final Item item = pSource.getItem(pItemId);
    if (item.getItemProperty(emailId).getValue() != null)
    {
      final String email = (String) item.getItemProperty(emailId).getValue();
      return new MailToLink(email);
    }
    return null;
  }

}
