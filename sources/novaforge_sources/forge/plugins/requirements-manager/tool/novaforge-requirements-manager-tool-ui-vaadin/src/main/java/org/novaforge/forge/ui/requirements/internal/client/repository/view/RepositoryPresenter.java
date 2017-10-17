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

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.containers.RepositoryContainer;
import org.novaforge.forge.ui.requirements.internal.client.containers.RepositoryItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.repository.components.ColumnActionsGenerator;
import org.novaforge.forge.ui.requirements.internal.client.repository.components.RepositoryType;
import org.novaforge.forge.ui.requirements.internal.client.repository.components.UploadFieldExcelRepository;
import org.novaforge.forge.ui.requirements.internal.module.AbstractRequirementsPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

/**
 * This presenter handles configuration view.
 * 
 * @author Jeremy Casery
 */
public class RepositoryPresenter extends AbstractRequirementsPresenter implements Serializable
{

  /** Serial version uid used for serialization */
  private static final long         serialVersionUID = 7107034541114430211L;
  private static final Log LOGGER = LogFactory.getLog(RepositoryPresenter.class);
  /** Content of project view */
  private final RepositoryView      view;
  /** Repositories container */
  private final RepositoryContainer container        = new RepositoryContainer();
  /** Repository URI to delete */
  private String                    repositoryURIToDelete;

  /**
   * Default constructor. It will initialize the tree component associated to
   * the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   * @param pProjectId
   *          project id
   */
  public RepositoryPresenter(final RepositoryView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;

    addListeners();
    initTableContainerAndGeneratedColumn();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getAddRepositoryWindow().addCloseListener(new CloseListener()
    {

      /** Default serial UID */
      private static final long serialVersionUID = -3774251572597068872L;

      @Override
      public void windowClose(final CloseEvent e)
      {
        checkTempExcelFile();
        view.getAddRepositoryWizard().restartWizard();

      }
    });
    view.getAddRepositoryButton().addClickListener(new Button.ClickListener()
    {

      /** Default serial UID */
      private static final long serialVersionUID = -4760521678219411245L;

      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        refreshLocalized(UI.getCurrent().getLocale());
        UI.getCurrent().addWindow(view.getAddRepositoryWindow());
      }
    });
    view.getAddRepositoryWizard().getCancelButton().addClickListener(new Button.ClickListener()
    {

      /** Default serial UID */
      private static final long serialVersionUID = -4585177870324828008L;

      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getAddRepositoryWindow());
      }
    });
    view.getAddRepositoryWizard().getFinishButton().addClickListener(new Button.ClickListener()
    {

      /** Default serial UID */
      private static final long serialVersionUID = 8626858636136202517L;

      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (view.getAddRepositoryWizard().isEditMode())
        {
          editRepositoryWizard();
        }
        else
        {
          addRepositoryWizard();
        }
        UI.getCurrent().removeWindow(view.getAddRepositoryWindow());
        view.getAddRepositoryWizard().restartWizard();
      }
    });
    view.getDeleteRepositoryWindow().getYesButton().addClickListener(new Button.ClickListener()
    {

      /** Default serial UID */
      private static final long serialVersionUID = -128970604491808793L;

      /** {@inheritDoc} */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getDeleteRepositoryWindow());
        if ((repositoryURIToDelete != null) && !repositoryURIToDelete.isEmpty())
        {
          try
          {
            RequirementsModule.getRequirementRepositoryService().deleteRepository(getProjectToolId(),
                repositoryURIToDelete);

            final TrayNotification notification = new TrayNotification(RequirementsModule.getPortalMessages()
                .getMessage(UI.getCurrent().getLocale(), Messages.REPOSITORY_DELETED_TITLE),
                RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                    Messages.REPOSITORY_DELETED_DESC), TrayNotificationType.SUCCESS);
            notification.show(Page.getCurrent());
            refreshContent();
          }
          catch (final RequirementManagerServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI
                .getCurrent().getLocale());
          }
        }

      }
    });
    view.getRepositoriesTable().addActionHandler(new RefreshAction()
    {

      /** Serial version id */
      private static final long serialVersionUID = 9173589293794578238L;

      /** {@inheritDoc} */
      @Override
      public void refreshAction()
      {
        refreshContent();

      }

      /** {@inheritDoc} */
      @Override
      public PortalMessages getPortalMessages()
      {
        return RequirementsModule.getPortalMessages();
      }
    });
  }

  /**
   * Initialize the table container and add generated columns
   */
  private void initTableContainerAndGeneratedColumn()
  {
    view.getRepositoriesTable().setContainerDataSource(container);
    view.getRepositoriesTable().addGeneratedColumn(RepositoryItemProperty.ACTIONS.getPropertyId(),
                                                   new ColumnActionsGenerator(this));
    view.getRepositoriesTable().setVisibleColumns(RepositoryItemProperty.URI.getPropertyId(),
                                                  RepositoryItemProperty.DESCRIPTION.getPropertyId(),
                                                  RepositoryItemProperty.TYPE.getPropertyId(),
                                                  RepositoryItemProperty.ACTIONS.getPropertyId());
    view.getRepositoriesTable().setColumnWidth(RepositoryItemProperty.ACTIONS.getPropertyId(), 75);
  }

  private void checkTempExcelFile()
  {
    if (RepositoryType.EXCEL.getERepositoryType().equals(
        view.getAddRepositoryWizard().getRepository().getType())
        && view.getAddRepositoryWizard().getStepTwo().getRepositoryFieldFactory() != null
        && view.getAddRepositoryWizard().isEditMode())
    {
      // Test if delete of temp folder is needed
      final UploadFieldExcelRepository excelUploader = (UploadFieldExcelRepository) view
          .getAddRepositoryWizard().getStepTwo().getRepositoryFieldFactory().getUri();

      if ((excelUploader != null) && (excelUploader.getValue() != null)
          && (StringUtils.isNotBlank(excelUploader.getValue().toString())))
      {
        final String uri = excelUploader.getValue().toString();
        try
        {
          RequirementsModule.getRequirementRepositoryService().cancelRepository(
              uri,
              view.getAddRepositoryWizard().getStepTwo().getRepositoryFieldFactory().getRepositoryType()
                  .getERepositoryType());
        }
        catch (final RequirementManagerServiceException e)
        {
          LOGGER.warn(String.format("Unable to cancel repository edition [uri=%s]", uri), e);
        }
      }
    }
  }

  private void editRepositoryWizard()
  {
    final String      oldRepositoryURI   = view.getAddRepositoryWizard().getOldRepositoryURI();
    final IRepository repositoryToUpdate = view.getAddRepositoryWizard().getRepository();

    try
    {
      if (RepositoryType.EXCEL.getERepositoryType().equals(repositoryToUpdate.getType()))
      {
        RequirementsModule.getRequirementRepositoryService().editExcelRepository(getProjectToolId(), oldRepositoryURI,
                                                                                 repositoryToUpdate,
                                                                                 view.getAddRepositoryWizard()
                                                                                     .getExcelFilePath(),
            view.getAddRepositoryWizard().getExcelFileName());
      }
      else
      {
        RequirementsModule.getRequirementRepositoryService().editRepository(getProjectToolId(), oldRepositoryURI,
                                                                            repositoryToUpdate);
      }

      final TrayNotification notification = new TrayNotification(RequirementsModule.getPortalMessages()
                                                                                   .getMessage(UI.getCurrent()
                                                                                                 .getLocale(),
                                                                                               Messages.REPOSITORY_UPDATED_TITLE),
                                                                 RequirementsModule.getPortalMessages()
                                                                                   .getMessage(UI.getCurrent()
                                                                                                 .getLocale(),
                                                                                               Messages.REPOSITORY_UPDATED_DESC),
          TrayNotificationType.SUCCESS);
      notification.show(Page.getCurrent());
      refreshContent();
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  private void addRepositoryWizard()
  {
    final IRepository repositoryToAdd = view.getAddRepositoryWizard().getRepository();

    try
    {
      if (RepositoryType.EXCEL.getERepositoryType().equals(repositoryToAdd.getType()))
      {
        RequirementsModule.getRequirementRepositoryService().addExcelRepository(getProjectToolId(), repositoryToAdd,
                                                                                view.getAddRepositoryWizard()
                                                                                    .getExcelFilePath(),
            view.getAddRepositoryWizard().getExcelFileName());
      }
      else
      {
        RequirementsModule.getRequirementRepositoryService().addRepository(getProjectToolId(), repositoryToAdd);
      }

      final TrayNotification notification = new TrayNotification(RequirementsModule.getPortalMessages()
                                                                                   .getMessage(UI.getCurrent()
                                                                                                 .getLocale(),
                                                                                               Messages.REPOSITORY_ADDED_TITLE),
                                                                 RequirementsModule.getPortalMessages()
                                                                                   .getMessage(UI.getCurrent()
                                                                                                 .getLocale(),
                                                                                               Messages.REPOSITORY_ADDED_DESC),
          TrayNotificationType.SUCCESS);
      notification.show(Page.getCurrent());
      refreshContent();
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Call when user want ot delete a repository
   *
   * @param pRepositoryUri
   *          the repository uri to delete
   */
  public void onDeleteRepositoryAction(final String pRepositoryUri)
  {
    repositoryURIToDelete = pRepositoryUri;
    UI.getCurrent().addWindow(view.getDeleteRepositoryWindow());
  }

  /**
   * Edit the given repository
   *
   * @param pRepositoryURI
   *          the repository URI to edit
   */
  public void showEditRepository(final String pRepositoryURI)
  {
    try
    {
      UI.getCurrent().addWindow(view.getAddRepositoryWindow());
      final IRepository repositoryToEdit = RequirementsModule.getRequirementRepositoryService()
          .findRepositoryByURI(getProjectToolId(), pRepositoryURI);
      final IRepository repositoryPrepared = RequirementsModule.getRequirementRepositoryService()
          .prepareEditRepository(repositoryToEdit);
      view.getAddRepositoryWizard().restartWizard(pRepositoryURI, repositoryPrepared);
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * Get the view element
   *
   * @return the view
   */
  public RepositoryView getView()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    try
    {
      final IProject project = RequirementsModule.getRequirementManagerService().findProjectByID(getProjectToolId());
      final Set<IRepository> repositories = project.getRepositories();
      container.setRepositories(repositories);
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
