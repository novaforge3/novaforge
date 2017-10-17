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
package org.novaforge.forge.ui.requirements.internal.client.testview.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.requirements.internal.client.containers.StringSplitter;
import org.novaforge.forge.ui.requirements.internal.client.containers.TestViewItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.testview.view.TestViewPresenter;

/**
 * @author Jeremy Casery
 */
public class ColumnRequirementGenerator implements ColumnGenerator
{
  /** Max size of description to show . */
  public static final int         VIEWABLE_DESCRIPTION_SIZE_MAX = 30;
  /**
   * Serial version id
   */
  private static final long serialVersionUID = -6720431165804612001L;
  /** Separator char */
  private static final String sepChar          = " - ";

  /** Associated TestViewPresenter */
  private final TestViewPresenter presenter;

  /**
   * Default constructor
   *
   * @param pPresenter
   *          , the TestViewPresenter to associate
   */
  public ColumnRequirementGenerator(final TestViewPresenter pPresenter)
  {
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    Button requirementButton = new Button();
    if (pSource.getParent() != null)
    {
      final Item item = pSource.getItem(pItemId);
      requirementButton.setCaption((String) item.getItemProperty(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId())
                                                .getValue() + sepChar + StringSplitter
                                                                            .stringCutter((String) item.getItemProperty(TestViewItemProperty.REQUIREMENT_DESCRIPTION
                                                                                                                            .getPropertyId())
                                                                                                       .getValue(),
                                                                                          VIEWABLE_DESCRIPTION_SIZE_MAX));
      requirementButton.setStyleName(NovaForge.BUTTON_LINK_TABLE);

      requirementButton.addClickListener(new Button.ClickListener()
      {

        /**
         * Default serial UID
         */
        private static final long serialVersionUID = 8714767531470974051L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void buttonClick(ClickEvent event)
        {
          presenter.showRequirementDetails((String) item.getItemProperty(TestViewItemProperty.ID.getPropertyId())
                                                        .getValue());
        }
      });
    }
    return requirementButton;
  }
}
