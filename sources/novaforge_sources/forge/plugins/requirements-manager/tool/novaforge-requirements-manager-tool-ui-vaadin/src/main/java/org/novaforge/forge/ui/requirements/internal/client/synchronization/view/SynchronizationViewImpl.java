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
package org.novaforge.forge.ui.requirements.internal.client.synchronization.view;

import java.util.Locale;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Jeremy Casery
 */
public class SynchronizationViewImpl extends VerticalLayout implements SynchronizationView
{

  /** Serialization id */
  private static final long serialVersionUID              = -4420222948069676402L;

  /** The synchronization title label */
  private final Label       synchroTitleLabel             = new Label();

  /** The select datas to synchronize label */
  private final Label       selectDatasToSynchronizeLabel = new Label();

  /** Synchronize button */
  private final Button      synchroButton                 = new Button();

  /** Horizontal separator label */
  private final Label       separator                     = new Label("<hr/>", Label.CONTENT_XHTML);

  /** OptionGroup radio button */
  private final OptionGroup synchroGroup                  = new OptionGroup();

  /** Option Requirements of the OptionGroup. */
  public static final String RADIO_OPT_REQUIREMENTS        = "requirements";

  /** Option Requirements of the OptionGroup. */
  public static final String RADIO_OPT_TESTS               = "tests";

  /** Option Requirements of the OptionGroup. */
  public static final String RADIO_OPT_CODE                = "code";

  /** Default constructor. */
  public SynchronizationViewImpl()
  {

    // Create options for synchroGroup
    synchroGroup.addItem("requirements");
    synchroGroup.addItem("tests");
    synchroGroup.addItem("code");
    synchroGroup.setHtmlContentAllowed(true);
    synchroGroup.addStyleName("radio-synchro");

    // Create Layout
    final VerticalLayout radioButtonLayout = new VerticalLayout();
    final VerticalLayout dataLayout = new VerticalLayout();

    // Layout properties
    setMargin(true);
    setSpacing(true);
    dataLayout.setMargin(true);
    dataLayout.setSpacing(true);
    radioButtonLayout.setSpacing(true);

    // Components properties
    synchroTitleLabel.setStyleName(NovaForge.LABEL_H2);
    synchroButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SYNCHRONIZE));
    synchroButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    selectDatasToSynchronizeLabel.setStyleName(NovaForge.LABEL_BOLD);

    // Add Component
    addComponent(synchroTitleLabel);
    radioButtonLayout.addComponent(synchroGroup);
    dataLayout.addComponent(selectDatasToSynchronizeLabel);
    dataLayout.addComponent(separator);
    dataLayout.addComponent(radioButtonLayout);
    dataLayout.addComponent(synchroButton);
    addComponent(dataLayout);
  }

  /** {@inheritDoc} */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /** {@inheritDoc} */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    synchroTitleLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_SYNCHRONIZATION));
    selectDatasToSynchronizeLabel.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.SYNCHRO_SELECT_DATAS));
    synchroButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.SYNCHRO_SYNCHRONIZE));

    // For radio Button
    synchroGroup.setItemCaption("requirements",
        "<b>" + RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.SYNCHRO_REQUIREMENTS)
            + "</b>");
    synchroGroup.setItemCaption("tests",
        "<b>" + RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.SYNCHRO_TESTS) + "</b>");
    synchroGroup.setItemCaption("code",
        "<b>" + RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.SYNCHRO_CODES) + "</b>");

  }

  /** {@inheritDoc} */
  @Override
  public Button getSynchroButton()
  {
    return synchroButton;
  }

  @Override
  public OptionGroup getSynchroGroup()
  {
    return synchroGroup;
  }
}
