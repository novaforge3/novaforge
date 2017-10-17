/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */


package org.novaforge.studio.ui.wizard;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.tasks.core.RepositoryStatus;
import org.eclipse.mylyn.tasks.core.RepositoryTemplate;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.TaskRepositoryLocationFactory;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.widgets.Composite;
import org.novaforge.forge.remote.services.exception.ExceptionCode;
import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.studio.core.StudioCorePlugin;
import org.novaforge.studio.core.StudioRepositoryType;
import org.novaforge.studio.core.client.StudioClientFactory;
import org.novaforge.studio.core.client.StudioProjectClient;
import org.novaforge.studio.ui.editor.Messages;

/**
 * Novastudio repository settings page.
 *
 */
public class StudioRepositorySettingsPage extends AbstractRepositorySettingsPage {

   public StudioRepositorySettingsPage(TaskRepository taskRepository) {
      super(Messages.ProjectRepositorySettingsPage_Title, Messages.ProjectRepositorySettingsPage_Description, taskRepository);
      setNeedsAdvanced(false);
      setNeedsAnonymousLogin(false);
      setNeedsEncoding(false);
      setNeedsProxy(false);
      setNeedsTimeZone(false);
   }

   @Override
   public void createControl(Composite parent)
   {
      super.createControl(parent);
      addRepositoryTemplatesToServerUrlCombo();
   }

   @Override
   protected void applyValidatorResult(Validator validator)
   {
      super.applyValidatorResult(validator);
   }

   @Override
   protected void repositoryTemplateSelected(RepositoryTemplate template) {
      repositoryLabelEditor.setStringValue(template.label);
      setUrl(template.repositoryUrl);
      getContainer().updateButtons();
   }

   @Override
   public String getConnectorKind() {
      return StudioCorePlugin.CONNECTOR_KIND;
   }

   @Override
   protected void createAdditionalControls(Composite parent)
   {
      // TODO Auto-generated method stub
   }

   @Override
   public boolean isPageComplete()
   {
      // make sure "Automatic" is not selected as a version
      return super.isPageComplete();
   }

   @Override
   protected boolean isValidUrl(String name)
   {
      if ((name.startsWith(URL_PREFIX_HTTPS) || name.startsWith(URL_PREFIX_HTTP)) && !name.endsWith("/")) { //$NON-NLS-1$
         try
         {
            new URL(name);
            return true;
         }
         catch (MalformedURLException e)
         {
         }
      }
      return false;
   }

   @Override
   protected Validator getValidator(TaskRepository repository) {
      return new StudioValidator(repository);
   }


   @Override
   public void performFinish(TaskRepository repository) {
      repository.setProperty(StudioCorePlugin.REPOSITORY_PROP_TYPE, StudioRepositoryType.PROJECT.toString());
      super.performFinish(repository);
   }

   /*
    * class to validate settings
    */
   public class StudioValidator extends Validator
   {

      private final String         repositoryUrl;

      private final TaskRepository taskRepository;

      public StudioValidator(TaskRepository taskRepository)
      {
         this.repositoryUrl = taskRepository.getRepositoryUrl();
         this.taskRepository = taskRepository;
      }

      @Override
      public void run(IProgressMonitor monitor) throws CoreException
      {
         try
         {
            validate(monitor);
         }
         catch (MalformedURLException e)
         {
            throw new CoreException(RepositoryStatus.createStatus(repositoryUrl, IStatus.ERROR,
                  StudioCorePlugin.ID_PLUGIN, INVALID_REPOSITORY_URL));
         }
         catch (RemoteServiceException e)
         {
            if (ExceptionCode.ERR_UN_AUTHORIZED.equals(e.getCode()))
            {
               throw new CoreException(RepositoryStatus.createStatus(repositoryUrl, IStatus.ERROR,
                     StudioCorePlugin.ID_PLUGIN, INVALID_LOGIN));
            }
            else if (ExceptionCode.ERR_UNKNOWN_SOAPFAULT.equals(e.getCode()))
            {
               throw new CoreException(RepositoryStatus.createStatus(repositoryUrl, IStatus.ERROR,
                     StudioCorePlugin.ID_PLUGIN, e.getMessage()));
            }
         }

      }

      public void validate(IProgressMonitor monitor) throws MalformedURLException, RemoteServiceException
      {
         AbstractWebLocation location = new TaskRepositoryLocationFactory().createWebLocation(taskRepository);

         StudioProjectClient client = StudioClientFactory.getProjectClient(location);

         client.isAuthenticated();

      }

   }
}
