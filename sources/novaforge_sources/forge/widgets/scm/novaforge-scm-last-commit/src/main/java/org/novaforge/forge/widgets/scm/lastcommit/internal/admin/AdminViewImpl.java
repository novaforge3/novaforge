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
package org.novaforge.forge.widgets.scm.lastcommit.internal.admin;

import com.vaadin.data.validator.RangeValidator;
import com.vaadin.ui.FormLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.widgets.scm.lastcommit.internal.module.LastCommitModule;
import org.vaadin.risto.stepper.IntStepper;

import java.util.Locale;

/**
 * @author bruno
 */
@SuppressWarnings("serial")
public class AdminViewImpl extends FormLayout implements AdminView
{

  private static int       MIN_NUMBER_OF_COMMIT = 1;
  private static int       MAX_NUMBER_OF_COMMIT = 100;

  private final IntStepper numberOfCommit;

  public AdminViewImpl()
  {
    super();
    setSpacing(false);

    numberOfCommit = new IntStepper();
    numberOfCommit.setMinValue(MIN_NUMBER_OF_COMMIT);
    numberOfCommit.setMaxValue(MAX_NUMBER_OF_COMMIT);
    numberOfCommit.setRequired(true);
    numberOfCommit.setInvalidValuesAllowed(true);
    numberOfCommit.setImmediate(true);

    addComponent(numberOfCommit);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final String caption = LastCommitModule.getPortalMessages().getMessage(pLocale,
        Messages.SCMLASTCOMMIT_ADMIN_NUMBEROFCOMMIT);
    numberOfCommit.setCaption(caption);

    numberOfCommit.setRequiredError(LastCommitModule.getPortalMessages().getMessage(pLocale,
        Messages.SCMLASTCOMMIT_ADMIN_NUMBEROFCOMMIT_TOOLTIP));
    numberOfCommit.removeAllValidators();
    numberOfCommit.addValidator(new RangeValidator<Integer>(LastCommitModule.getPortalMessages().getMessage(
        pLocale, Messages.SCMLASTCOMMIT_ADMIN_NUMBEROFCOMMIT_TOOLTIP), Integer.class, MIN_NUMBER_OF_COMMIT,
        MAX_NUMBER_OF_COMMIT));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IntStepper getNumberOfCommit()
  {
    return numberOfCommit;
  }

}
