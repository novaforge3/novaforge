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
package org.novaforge.forge.ui.requirements.internal.client.repository.view;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.repository.components.AddRepositoryWizard;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;

import java.util.Locale;
import java.util.List;

/**
 * @author Jeremy Casery
 */
public class RepositoryViewImpl extends VerticalLayout implements RepositoryView
{
  /** Default serial UID */
  private static final long         serialVersionUID       = -4420222948069676402L;

  /** Repositories title {@link Label} */
  private final Label               repositoriesTitle      = new Label();

  /** Repositories {@link Table} */
  private final Table               repositoriesTable      = new Table();

  /** Add repository button */
  private final Button              addRepositoryButton    = new Button();

  /** Add repository wizard */
  private final AddRepositoryWizard addRepositoryWizard    = new AddRepositoryWizard();

  /** Add repository window */
  private final Window              addRepositoryWindow    = new Window();

  /** Delete repository window */
  private final DeleteConfirmWindow deleteRepositoryWindow = new DeleteConfirmWindow();

  /** Default constructor. */
  public RepositoryViewImpl()
  {
    setMargin(true);
    setSpacing(true);

    repositoriesTable.setSelectable(false);
    repositoriesTable.setImmediate(true);
    repositoriesTable.setWidth(100, Unit.PERCENTAGE);
    repositoriesTable.setStyleName(Reindeer.TABLE_STRONG);

    repositoriesTitle.setStyleName(NovaForge.LABEL_H2);
    addRepositoryButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    addRepositoryButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    addComponent(repositoriesTitle);
    addComponent(addRepositoryButton);
    addComponent(repositoriesTable);

    initAddRepositoryWindow();
    initDeleteRepositoryWindow();
  }

  /**
   * Initialize the add repository window
   */
  private void initAddRepositoryWindow()
  {
    addRepositoryWindow.setModal(true);
    addRepositoryWindow.setResizable(false);
    addRepositoryWindow.center();
    addRepositoryWindow.setWidth(600, Unit.PIXELS);
    addRepositoryWindow.setHeight(500, Unit.PIXELS);
    VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.addComponent(addRepositoryWizard);
    windowLayout.setSizeFull();
    addRepositoryWindow.setContent(windowLayout);
  }

  /**
   * Initialize the delete repository confirmation window
   */
  private void initDeleteRepositoryWindow()
  {
    deleteRepositoryWindow.setWindowMessageTextId(Messages.REPOSITORY_DELETE_CONFIRM);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    StringBuilder sb = new StringBuilder(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_REPOSITORY));
    List<ExternalRepositoryRequirementConnector> externalRepositoryRequirementConnectors = RequirementsModule.getExternalRepositoryRequirementConnectors();
    for (ExternalRepositoryRequirementConnector externalRepositoryRequirementConnector : externalRepositoryRequirementConnectors)
    {
      if (externalRepositoryRequirementConnector != null) {
          String url=externalRepositoryRequirementConnector.getRepositoryLocation();
          if ((url !=  null) && (url.startsWith("cdo:")) && (!(url.startsWith("cdo://@CDO_SERVER_HOST")))) {          
            sb.append(" - ").append(RequirementsModule.getPortalMessages().getMessage(pLocale,Messages.GLOBAL_MENU_OBEOSERVER)).append(" : ").append(url);
          }
      }
    }
    repositoriesTitle.setValue(sb.toString());
    addRepositoryButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REPOSITORY_ADD_REPOSITORY));
    if (addRepositoryWizard.isEditMode())
    {
      addRepositoryWindow.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
          Messages.REPOSITORY_ACTIONS_EDIT));
    }
    else
    {
      addRepositoryWindow.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
          Messages.REPOSITORY_ADD_REPOSITORY));
    }
    addRepositoryWizard.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAddRepositoryButton()
  {
    return addRepositoryButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getRepositoriesTable()
  {
    return repositoriesTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getRepositoriesTitle()
  {
    return repositoriesTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AddRepositoryWizard getAddRepositoryWizard()
  {
    return addRepositoryWizard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getAddRepositoryWindow()
  {
    return addRepositoryWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getDeleteRepositoryWindow()
  {
    return deleteRepositoryWindow;
  }

}
