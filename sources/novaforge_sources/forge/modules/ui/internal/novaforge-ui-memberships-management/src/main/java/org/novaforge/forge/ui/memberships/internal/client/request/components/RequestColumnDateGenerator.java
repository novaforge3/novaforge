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
package org.novaforge.forge.ui.memberships.internal.client.request.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.request.history.RequestHistoryPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Guillaume Lamirand
 */
public class RequestColumnDateGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long             serialVersionUID = -2377060170281716413L;

  private final RequestHistoryPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the associated presenter
   */
  public RequestColumnDateGenerator(final RequestHistoryPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    String formatedDate = "";
    final Item item = pSource.getItem(pItemId);
    final Date createdDate = (Date) item.getItemProperty(RequestItemProperty.DATE.getPropertyId()).getValue();
    if (createdDate != null)
    {
      final SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM yyyy", presenter.getView()
          .getLocale());

      formatedDate = formatter.format(createdDate);
    }
    return new Label(formatedDate);
  }
}
