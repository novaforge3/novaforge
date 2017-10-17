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
package org.novaforge.forge.ui.requirements.internal.client.repository.components;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.vaadin.teemu.wizards.WizardStep;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;

import java.util.Locale;
import java.util.List;

/**
 * @author Jeremy Casery
 */
public class AddRepositoryFinishStep implements WizardStep
{

  /**
   * The obeo {@link VerticalLayout}
   */
  private final VerticalLayout      repositoryLayout    = new VerticalLayout();
  /**
   * The obeo type {@link Label}
   */
  private final Label               repositoryTypeLabel = new Label();
  /**
   * The obeo type value {@link Embedded}
   */
  private final Embedded            repositoryTypeValue = new Embedded();
  /**
   * The obeo Server {@link Label}
   */
  private final Label               repositoryObeoServerLabel  = new Label();
  /**
   * The obeo URI {@link Label}
   */
  private final Label               repositoryURILabel  = new Label();
  /**
   * The obeo URI value {@link Label}
   */
  private final Label               repositoryURIValue  = new Label();
  /**
   * The obeo description {@link Label}
   */
  private final Label               repositoryDescLabel = new Label();
  /**
   * The obeo description value {@link Label}
   */
  private final Label               repositoryDescValue = new Label();

  private final AddRepositoryWizard wizard;

  /**
   * Default constructor, will initialize the view content
   */
  public AddRepositoryFinishStep(final AddRepositoryWizard pWizard)
  {
    wizard = pWizard;
    initLayout();
  }

  /**
   * Initialize the Obeo layout
   */
  private void initLayout()
  {
    repositoryLayout.setMargin(true);
    repositoryLayout.setSpacing(true);
    repositoryTypeValue.setWidth(100, Unit.PIXELS);
    repositoryTypeValue.setSource(new ThemeResource(NovaForgeResources.LOGO_OBEO));
    final HorizontalLayout typeLayout = new HorizontalLayout();
    typeLayout.setSpacing(true);
    typeLayout.addComponent(repositoryTypeLabel);
    typeLayout.addComponent(repositoryTypeValue);
    typeLayout.setComponentAlignment(repositoryTypeLabel, Alignment.MIDDLE_RIGHT);
    typeLayout.setComponentAlignment(repositoryTypeValue, Alignment.MIDDLE_LEFT);
    final HorizontalLayout obeoServerLayout = new HorizontalLayout();
    obeoServerLayout.setSpacing(true);
    obeoServerLayout.addComponent(repositoryObeoServerLabel);
    obeoServerLayout.setComponentAlignment(repositoryObeoServerLabel, Alignment.MIDDLE_RIGHT);
    final HorizontalLayout uriLayout = new HorizontalLayout();
    uriLayout.setSpacing(true);
    uriLayout.addComponent(repositoryURILabel);
    uriLayout.addComponent(repositoryURIValue);
    uriLayout.setComponentAlignment(repositoryURILabel, Alignment.MIDDLE_RIGHT);
    uriLayout.setComponentAlignment(repositoryURIValue, Alignment.MIDDLE_LEFT);
    final HorizontalLayout descLayout = new HorizontalLayout();
    descLayout.setSpacing(true);
    descLayout.addComponent(repositoryDescLabel);
    descLayout.addComponent(repositoryDescValue);
    descLayout.setComponentAlignment(repositoryDescLabel, Alignment.MIDDLE_RIGHT);
    descLayout.setComponentAlignment(repositoryDescValue, Alignment.MIDDLE_LEFT);

    repositoryLayout.addComponent(typeLayout);
    repositoryLayout.addComponent(obeoServerLayout);
    repositoryLayout.addComponent(uriLayout);
    repositoryLayout.addComponent(descLayout);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption()
  {
    return RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.REPOSITORY_ADD_STEPLAST_TITLE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getContent()
  {
    return repositoryLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onAdvance()
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onBack()
  {
    return true;
  }

  /**
   * Empty the view datas
   */
  public void emptyDatas()
  {
    repositoryTypeValue.setSource(null);
    repositoryURIValue.setValue("");
    repositoryDescValue.setValue("");
  }

  /**
   * Refresh the internationalized view content
   * 
   * @param pLocale
   *          the {@link Locale} to use for internationalization
   */
  public void refreshLocale(final Locale pLocale)
  {
    repositoryTypeLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_TYPE));
    if (wizard.getRepository().getType() != null)
    {
      if (RepositoryType.OBEO.getERepositoryType().equals(wizard.getRepository().getType()))
      {
        StringBuilder sb = new StringBuilder(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.GLOBAL_MENU_OBEOSERVER));
        List<ExternalRepositoryRequirementConnector> externalRepositoryRequirementConnectors = RequirementsModule.getExternalRepositoryRequirementConnectors();
        for (ExternalRepositoryRequirementConnector externalRepositoryRequirementConnector : externalRepositoryRequirementConnectors)
        {
          if (externalRepositoryRequirementConnector != null) {
              String url=externalRepositoryRequirementConnector.getRepositoryLocation();
              if ((url !=  null) && (url.startsWith("cdo:"))) {
                
                sb.append(" : ").append(url);
              }
          }
        }
        repositoryObeoServerLabel.setValue(sb.toString());
        repositoryURILabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.REPOSITORY_ADD_OBEO_CONF_URI));
        repositoryDescLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.REPOSITORY_ADD_OBEO_CONF_DESC));
      }
      else
      {
        repositoryURILabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.REPOSITORY_ADD_EXCEL_CONF_FILE));
        repositoryDescLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.REPOSITORY_ADD_EXCEL_CONF_DESC));
      }
    }
  }

  /**
   * Get the Obeo type value {@link Embedded} field
   * 
   * @return the value field
   */
  public Embedded getRepositoryTypeValue()
  {
    return repositoryTypeValue;
  }

  /**
   * Get the Obeo URI value {@link Label}
   * 
   * @return the uri {@link Label} value
   */
  public Label getRepositoryURIValue()
  {
    return repositoryURIValue;
  }

  /**
   * Get the Obeo description value {@link Label}
   * 
   * @return the description {@link Label} value
   */
  public Label getRepositoryDescValue()
  {
    return repositoryDescValue;
  }
}
