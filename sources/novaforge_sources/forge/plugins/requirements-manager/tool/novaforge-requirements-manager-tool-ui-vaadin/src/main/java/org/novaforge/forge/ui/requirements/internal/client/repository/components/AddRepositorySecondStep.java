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

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.vaadin.jonatan.contexthelp.ContextHelp;
import org.vaadin.teemu.wizards.WizardStep;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;

import java.io.File;
import java.util.Locale;
import java.util.List;

/**
 * @author Jeremy Casery
 */
public class AddRepositorySecondStep implements WizardStep
{
  /**
   * The {@link AddRepositoryWizard} which contains this {@link WizardStep}
   */
  private final AddRepositoryWizard wizard;

  /** The unknow type layout */
  private final VerticalLayout      unknowTypeLayout = new VerticalLayout();

  /** The unknow type label */
  private final Label               unknowTypeLabel  = new Label();

  /** The layout */
  private final VerticalLayout      repositoryLayout = new VerticalLayout();

  /** The Form */
  private final Form                repositoryForm   = new Form();
  /** The title label */
  private final Label               repositoryTitle  = new Label();
  private final ContextHelp         contextHelp      = new ContextHelp();
  /**
   * The repository field factory
   */
  private RepositoryFieldFactory repositoryFieldFactory;

  /** Default constructor, will initialize the content */
  public AddRepositorySecondStep(final AddRepositoryWizard pWizard)
  {
    wizard = pWizard;
    wizard.getNextButton().setEnabled(false);

    initLayout();
    initUnknowLayout();
  }

  /** Initialize the obeo layout */
  private void initLayout()
  {
    repositoryLayout.setMargin(true);
    repositoryLayout.setSpacing(true);
    repositoryForm.setInvalidCommitted(false);
    repositoryLayout.addComponent(repositoryTitle);
    repositoryLayout.addComponent(repositoryForm);
  }

  /**
   * Initialize the unknow layout
   */
  private void initUnknowLayout()
  {
    unknowTypeLabel.setStyleName(NovaForge.LABEL_H2);
    unknowTypeLabel.addStyleName(NovaForge.LABEL_ORANGE);
    unknowTypeLayout.addComponent(unknowTypeLabel);
  }

  /** {@inheritDoc} */
  @Override
  public String getCaption()
  {
    return RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                             Messages.REPOSITORY_ADD_STEPTWO_TITLE);
  }

  /** {@inheritDoc} */
  @Override
  public Component getContent()
  {
    return repositoryLayout;
  }

  /** {@inheritDoc} */
  @Override
  public boolean onAdvance()
  {
    boolean result;
    if (isFormValidValues())
    {
      if (RepositoryType.EXCEL.equals(repositoryFieldFactory.getRepositoryType()))
      {
        final File excelFile = (File) ((UploadFieldExcelRepository) repositoryFieldFactory.getUri()).getValue();
        if (excelFile != null)
        {
          wizard.getRepository().setURI(excelFile.getPath());
        }
      }
      else
      {
        wizard.getRepository().setURI(((TextField) repositoryFieldFactory.getUri()).getValue());
      }
      wizard.getRepository().setDescription(repositoryFieldFactory.getDescription().getValue());
      result = true;
    }
    else
    {
      repositoryForm.focus();
      result = false;
    }
    return result;

  }

  private boolean isFormValidValues()
  {
    boolean result = true;

    repositoryForm.setComponentError(null);
    repositoryForm.setValidationVisible(false);

    if (!repositoryFieldFactory.getUri().isValid())
    {
      repositoryForm.setComponentError(new UserError(repositoryFieldFactory.getUri().getRequiredError()));
      result = false;
    }
    if (!repositoryFieldFactory.getDescription().isValid())
    {
      repositoryForm.setComponentError(repositoryFieldFactory.getDescription().getErrorMessage());
      result = false;
    }
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean onBack()
  {
    return true;
  }

  /** Empty the content datas */
  public void emptyDatas()
  {
    repositoryForm.discard();
    repositoryForm.setValidationVisible(false);
    if (repositoryFieldFactory != null)
    {
      repositoryFieldFactory.getUri().setValue(null);
      repositoryFieldFactory.getDescription().setDescription(null);
    }
  }

  /**
   * Refresh internationalized content with given {@link Locale}
   * 
   * @param pLocale
   *          the locale to use
   */
  public void refreshLocale(final Locale pLocale)
  {
    unknowTypeLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REPOSITORY_ADD_TYPE_UNKNOW));
    if (repositoryFieldFactory != null)
    {
      if (RepositoryType.EXCEL.equals(repositoryFieldFactory.getRepositoryType()))
      {
        repositoryTitle.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.REPOSITORY_ADD_EXCEL_CONF_TITLE));
        if (repositoryFieldFactory.getUri() != null)
        {
          ((UploadFieldExcelRepository) repositoryFieldFactory.getUri()).setCaption(RequirementsModule
              .getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_ADD_EXCEL_CONF_FILE));
          ((UploadFieldExcelRepository) repositoryFieldFactory.getUri()).setButtonCaption(RequirementsModule
              .getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_ADD_EXCEL_UPLOAD_BUTTON));
          ((UploadFieldExcelRepository) repositoryFieldFactory.getUri()).setRequiredError(RequirementsModule
              .getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_ADD_FILE_INVALID_CONTENT));
          ((UploadFieldExcelRepository) repositoryFieldFactory.getUri()).removeAllValidators();
          ((UploadFieldExcelRepository) repositoryFieldFactory.getUri())
              .addValidator(new RequirementsExcelFileContentValidator(RequirementsModule.getPortalMessages()
                  .getMessage(pLocale, Messages.REPOSITORY_ADD_FILE_INVALID_CONTENT)));
        }
        if (repositoryFieldFactory.getDescription() != null)
        {
          repositoryFieldFactory.getDescription().setCaption(
              RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REPOSITORY_ADD_EXCEL_CONF_DESC));
          repositoryFieldFactory.getDescription().removeAllValidators();
          repositoryFieldFactory.getDescription().addValidator(
              new StringLengthValidator(RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REQUIREMENT_FIELD_DESCRIPTION_LENGTH), 0, 255, true));
        }
      }
      else if (RepositoryType.OBEO.equals(repositoryFieldFactory.getRepositoryType()))
      {
        StringBuilder sb = new StringBuilder(RequirementsModule.getPortalMessages().getMessage(pLocale,
            Messages.REPOSITORY_ADD_OBEO_CONF_TITLE));
        List<ExternalRepositoryRequirementConnector> externalRepositoryRequirementConnectors = RequirementsModule.getExternalRepositoryRequirementConnectors();
        for (ExternalRepositoryRequirementConnector externalRepositoryRequirementConnector : externalRepositoryRequirementConnectors)
        {
          if (externalRepositoryRequirementConnector != null) {
              String url=externalRepositoryRequirementConnector.getRepositoryLocation();
              if ((url !=  null) && (url.startsWith("cdo:"))) {
                sb.append(" - ").append(RequirementsModule.getPortalMessages().getMessage(pLocale,Messages.GLOBAL_MENU_OBEOSERVER)).append(" : ").append(url);
              }
          }
        }
        repositoryTitle.setValue(sb.toString());
        if (repositoryFieldFactory.getUri() != null)
        {
          repositoryFieldFactory.getUri().setCaption(
              RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REPOSITORY_ADD_OBEO_CONF_URI));
          contextHelp.addHelpForComponent(repositoryFieldFactory.getUri(), RequirementsModule
              .getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_ADD_URI_MALFORMED));
          repositoryFieldFactory.getUri().setRequiredError(
              RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REPOSITORY_ADD_URI_MALFORMED));
          repositoryFieldFactory.getUri().removeAllValidators();
          repositoryFieldFactory.getUri().addValidator(
              new ObeoUriValidator(RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REPOSITORY_ADD_URI_MALFORMED)));
        }
        if (repositoryFieldFactory.getDescription() != null)
        {
          repositoryFieldFactory.getDescription().setCaption(
              RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REPOSITORY_ADD_OBEO_CONF_DESC));
          repositoryFieldFactory.getDescription().removeAllValidators();
          repositoryFieldFactory.getDescription().addValidator(
              new StringLengthValidator(RequirementsModule.getPortalMessages().getMessage(pLocale,
                  Messages.REQUIREMENT_FIELD_DESCRIPTION_LENGTH), 0, 255, true));
        }
      }
    }

  }

  public Form getRepositoryForm()
  {
    return repositoryForm;
  }

  public RepositoryFieldFactory getRepositoryFieldFactory()
  {
    return repositoryFieldFactory;
  }

  public void setRepositoryFieldFactory(final RepositoryFieldFactory repositoryFieldFactory)
  {
    this.repositoryFieldFactory = repositoryFieldFactory;
    repositoryForm.setFormFieldFactory(repositoryFieldFactory);
  }

}
