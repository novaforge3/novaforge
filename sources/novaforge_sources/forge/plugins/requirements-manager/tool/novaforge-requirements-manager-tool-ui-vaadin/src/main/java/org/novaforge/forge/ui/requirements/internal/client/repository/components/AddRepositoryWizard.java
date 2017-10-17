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

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.UI;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import java.io.File;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AddRepositoryWizard extends Wizard
{

  /** Serial uid */
  private static final long             serialVersionUID = -9042491897609720249L;
  private final AddRepositoryFirstStep  stepOne   = new AddRepositoryFirstStep(this);
  private final AddRepositorySecondStep stepTwo   = new AddRepositorySecondStep(this);
  private final AddRepositoryFinishStep stepThree = new AddRepositoryFinishStep(this);
  private IRepository                   repository;
  private BeanItem<IRepository>         repositoryItem;
  private boolean                       isEditMode       = false;
  private String                        oldRepositoryURI = null;

  /** Constructor */
  public AddRepositoryWizard()
  {
    super();
    initContent();
    addListeners();
    restartWizard();
  }

  private void initContent()
  {
    // Wizard properties
    getCancelButton().setStyleName(NovaForge.BUTTON_LINK);
    getNextButton().setStyleName(NovaForge.BUTTON_PRIMARY);
    getNextButton().addStyleName(NovaForge.BUTTON_DISABLED_HIDDEN);
    getNextButton().setIcon(new ThemeResource(NovaForgeResources.ICON_GO_NEXT_COLORED));
    getBackButton().setIcon(new ThemeResource(NovaForgeResources.ICON_GO_PREVIOUS_COLORED));
    getBackButton().addStyleName(NovaForge.BUTTON_DISABLED_HIDDEN);
    getFinishButton().setStyleName(NovaForge.BUTTON_PRIMARY);
    getFinishButton().addStyleName(NovaForge.BUTTON_DISABLED_HIDDEN);
    getFinishButton().setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    // Add steps
    addStep(stepOne);
    addStep(stepTwo);
    addStep(stepThree);
    // Disable / hide not needed button
    getNextButton().setEnabled(false);
  }

  private void addListeners()
  {
    addListener(new WizardProgressListener()
    {

      @Override
      public void activeStepChanged(final WizardStepActivationEvent event)
      {
        if (event.getActivatedStep().equals(stepThree))
        {
          // Step 3
          getBackButton().setEnabled(true);
          stepThree.refreshLocale(getLocale());
          stepThree.getRepositoryURIValue().setValue(getCleanUri());
          stepThree.getRepositoryDescValue().setValue(repository.getDescription());
          stepThree.getRepositoryTypeValue().setSource(
              new ThemeResource(RepositoryType.get(repository.getType()).getLogo()));
        }
        else if (event.getActivatedStep().equals(stepTwo))
        {
          // Step 2
          // stepTwo.getRepositoryForm().focus();
          stepTwo.refreshLocale(UI.getCurrent().getLocale());
          getBackButton().setEnabled(!isEditMode);
        }
        else if (event.getActivatedStep().equals(stepOne))
        {
          // Step 1
          // Nothing to do
          stepTwo.getRepositoryForm().setValidationVisible(false);
          stepTwo.getRepositoryForm().setComponentError(null);
        }
      }

      @Override
      public void stepSetChanged(final WizardStepSetChangedEvent event)
      {
        // Nothing to do
      }

      @Override
      public void wizardCompleted(final WizardCompletedEvent event)
      {
      }

      @Override
      public void wizardCancelled(final WizardCancelledEvent event)
      {
      }
    });
  }

  /**
   * Restart wizard for a new {@link IRepository}
   */
  public void restartWizard()
  {
    restartWizard(null, null);
  }

  private String getCleanUri()
  {
    String cleanUri;
    if (RepositoryType.OBEO.getERepositoryType().equals(repository.getType()))
    {
      cleanUri = repository.getURI();
    }
    else
    {
      cleanUri = getExcelFileName();
    }
    return cleanUri;
  }

  /**
   * Restart wizard for an existing {@link IRepository}, i.e. update this repository
   *
   * @param pRepository
   *          the repository to update
   */
  public void restartWizard(final String pOldUri, final IRepository pRepository)
  {
    if (pRepository != null)
    {
      isEditMode = true;
      oldRepositoryURI = pOldUri;
      getFinishButton().setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
      repository = pRepository;
      stepOne.setRepositoryType(RepositoryType.get(repository.getType()));
      repositoryItem = new BeanItem<IRepository>(repository);
      stepTwo.setRepositoryFieldFactory(new RepositoryFieldFactory(stepOne.getRepositoryType()));
      stepTwo.getRepositoryForm().setItemDataSource(repositoryItem);
      if (RepositoryType.EXCEL.equals(stepTwo.getRepositoryFieldFactory().getRepositoryType()))
      {
        final UploadFieldExcelRepository uploadFieldExcelRepository = (UploadFieldExcelRepository) stepTwo
            .getRepositoryFieldFactory().getUri();
        uploadFieldExcelRepository.setValue(new File(pRepository.getURI()));
        uploadFieldExcelRepository.refreshDisplay();
      }
      setStepActive(stepTwo);
    }
    else
    {
      isEditMode = false;
      oldRepositoryURI = null;
      getFinishButton().setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
      repository = RequirementsModule.getRequirementFactory().buildNewRepository();
      emptyStepsDatas();
      setStepActive(stepOne);
      getNextButton().setEnabled(false);
      stepTwo.getRepositoryForm().setValidationVisible(false);
    }
    refreshLocale(UI.getCurrent().getLocale());
  }

  /**
   * Returns the excel filename if exists or empty string if not.
   *
   * @return the excel filename if exists or empty string if not
   */
  public String getExcelFileName()
  {
    String result = "";
    if (stepTwo != null)
    {
      final UploadFieldExcelRepository excelFile = (UploadFieldExcelRepository) stepTwo.getRepositoryFieldFactory()
                                                                                       .getUri();
      if ((excelFile != null) && (excelFile.getExcelFileName() != null))
      {
        result = excelFile.getExcelFileName();
      }
    }
    return result;
  }

  public void setStepActive(final WizardStep pStep)
  {
    if (getSteps().contains(pStep))
    {
      activateStep(pStep);
    }
  }

  private void emptyStepsDatas()
  {
    stepOne.emptyDatas();
    stepTwo.emptyDatas();
    stepThree.emptyDatas();
  }

  public void refreshLocale(final Locale pLocale)
  {
    getNextButton().setCaption(
        RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_WIZARD_NEXT));
    getBackButton().setCaption(
        RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_WIZARD_BACK));
    if (isEditMode)
    {
      getFinishButton().setCaption(
          RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_WIZARD_UPDATE));
    }
    else
    {
      getFinishButton().setCaption(
          RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_WIZARD_ADD));
    }
    getCancelButton().setCaption(
        RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REPOSITORY_WIZARD_CANCEL));
    stepOne.refreshLocale(pLocale);
    stepTwo.refreshLocale(pLocale);
    stepThree.refreshLocale(pLocale);
  }

  public void setRepositoryType(final RepositoryType pType)
  {
    if (pType != null)
    {
      if (repository.getType() == null)
      {
        repository.setType(pType.getERepositoryType());
        repositoryItem = new BeanItem<IRepository>(repository);
        stepTwo.setRepositoryFieldFactory(new RepositoryFieldFactory(stepOne.getRepositoryType()));
        stepTwo.getRepositoryForm().setItemDataSource(repositoryItem);
      }
      else
      {
        if (!pType.name().equals(repository.getType().name()))
        {
          stepTwo.getRepositoryFieldFactory().getUri().removeAllValidators();
          repository.setURI(null);
          repository.setDescription(null);
          repository.setType(pType.getERepositoryType());
          repositoryItem = new BeanItem<IRepository>(repository);
          stepTwo.setRepositoryFieldFactory(new RepositoryFieldFactory(stepOne.getRepositoryType()));
          stepTwo.getRepositoryForm().setItemDataSource(repositoryItem);
        }
      }

    }
  }

  public IRepository getRepository()
  {
    return repository;
  }

  public boolean isEditMode()
  {
    return isEditMode;
  }

  public String getOldRepositoryURI()
  {
    return oldRepositoryURI;
  }

  /**
   * Returns the path to the excel file if exists or empty string if not.
   *
   * @return the path to the excel file if exists or empty string if not
   */
  public String getExcelFilePath()
  {
    String result = "";
    if ((stepTwo != null) && (stepTwo.getRepositoryFieldFactory().getUri() != null)
        && (stepTwo.getRepositoryFieldFactory().getUri().getValue() != null))
    {
      result = stepTwo.getRepositoryFieldFactory().getUri().getValue().toString();
    }
    return result;
  }

  public AddRepositoryFirstStep getStepOne()
  {
    return stepOne;
  }

  public AddRepositorySecondStep getStepTwo()
  {
    return stepTwo;
  }

  public AddRepositoryFinishStep getStepThree()
  {
    return stepThree;
  }

}
