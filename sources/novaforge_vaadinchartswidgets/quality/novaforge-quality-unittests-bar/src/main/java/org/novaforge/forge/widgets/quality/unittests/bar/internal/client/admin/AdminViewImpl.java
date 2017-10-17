/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.widgets.quality.unittests.bar.internal.client.admin;

import java.util.Locale;

import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.widgets.quality.unittests.bar.internal.module.UnittestsBarModule;
import org.vaadin.risto.stepper.IntStepper;

import com.vaadin.data.validator.RangeValidator;
import com.vaadin.ui.Component;

/**
 * @author Gauthier Cart
 */
public class AdminViewImpl extends org.novaforge.forge.widgets.quality.client.admin.AdminViewImpl implements
    AdminView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID    = -2126786228774879908L;

  private IntStepper        numberOfBuild;

  private static int        MIN_NUMBER_OF_BUILD = 1;
  private static int        MAX_NUMBER_OF_BUILD = 100;

  public AdminViewImpl()
  {
    super();
    final Component content = initContent();
    addComponent(content);
  }

  private Component initContent()
  {
    numberOfBuild = new IntStepper();
    numberOfBuild.setMinValue(MIN_NUMBER_OF_BUILD);
    numberOfBuild.setMaxValue(MAX_NUMBER_OF_BUILD);
    numberOfBuild.setImmediate(true);
    numberOfBuild.setInvalidValuesAllowed(false);
    numberOfBuild.setVisible(true);
    return numberOfBuild;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {

    super.refreshLocale(pLocale);

    final String captionNomberOfCommit = UnittestsBarModule.getPortalMessages().getMessage(pLocale,
        Messages.UNITTESTSBAR_ADMIN_NUMBEROFCOMMIT);
    numberOfBuild.setCaption(captionNomberOfCommit);

    numberOfBuild.removeAllValidators();
    numberOfBuild.addValidator(new RangeValidator<Integer>(UnittestsBarModule.getPortalMessages().getMessage(
        pLocale, Messages.UNITTESTSBAR_ADMIN_NUMBEROFCOMMIT_TOOLTIP), Integer.class, MIN_NUMBER_OF_BUILD,
        MAX_NUMBER_OF_BUILD));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IntStepper getNumberOfBuild()
  {
    return numberOfBuild;
  }
}
