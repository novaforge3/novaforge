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
package org.novaforge.forge.ui.applications.internal.client.spaces;

import com.google.common.base.Strings;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.applications.internal.client.events.CreateApplicationEvent;
import org.novaforge.forge.ui.applications.internal.client.events.DisplayDefaultRightEvent;
import org.novaforge.forge.ui.applications.internal.client.events.RefreshTreeEvent;
import org.novaforge.forge.ui.applications.internal.client.spaces.components.SpaceFieldFactory;
import org.novaforge.forge.ui.applications.internal.module.AbstractApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * This presenter handles spaces components displayed.
 * 
 * @author Guillaume Lamirand
 */
public class SpacesPresenter extends AbstractApplicationsPresenter implements Serializable
{

  /**
   * Logger component
   */
  private static final Log      LOGGER           = LogFactory.getLog(SpacesPresenter.class);
  /**
   * Serial version uid used for serialization
   */
  private static final long     serialVersionUID = -5042299647493799344L;
  /**
   * Contains the array of fields to display in the form
   */
  private static final String[] SPACE_FIELDS     = new String[] { SpaceFieldFactory.NAME_FIELD,
      SpaceFieldFactory.DESCRIPTION_FIELD       };
  /**
   * Content of project view
   */
  private final SpacesView view;
  private final String          projectId;
  private       Space      currentSpace;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the init context
   * @param pProjectId
   *          the current project id
   */
  public SpacesPresenter(final SpacesView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    view = pView;
    projectId = pProjectId;

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getAddAppButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4007391546749871601L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new CreateApplicationEvent(currentSpace.getUri(), getUuid()));

      }

    });
    view.getEditButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4007391546749871601L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSpaceForm().setReadOnly(false);
        view.getSpaceForm().getFooter().setVisible(true);
        view.getHeaderLayout().setVisible(false);

      }
    });
    view.getCancelButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1118059378603697848L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getSpaceForm().discard();
        if (Strings.isNullOrEmpty(currentSpace.getUri()))
        {
          getEventBus().publish(new DisplayDefaultRightEvent(getUuid()));
        }
        else
        {
          view.getSpaceForm().setReadOnly(true);
          view.getSpaceForm().getFooter().setVisible(false);
          view.getHeaderLayout().setVisible(true);
        }

      }
    });
    view.getApplyButton().addClickListener(new ClickListener()
    {
      /**
			 * 
			 */
      private static final long serialVersionUID = 3063292794092695535L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final String oldName = currentSpace.getName();
        try
        {
          view.getSpaceForm().commit();
          if (Strings.isNullOrEmpty(currentSpace.getUri()))
          {
            currentSpace = ApplicationsModule.getSpacePresenter().addSpace(projectId, currentSpace);
          }
          else
          {
            currentSpace = ApplicationsModule.getSpacePresenter().updateSpace(projectId, oldName,
                currentSpace);
          }
          getEventBus().publish(new RefreshTreeEvent(currentSpace, getUuid()));

        }
        catch (final InvalidValueException v)
        {
          Notification.show(v.getLocalizedMessage(), Type.WARNING_MESSAGE);
        }
        catch (final SpaceServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
              view.getLocale());

          currentSpace.setName(oldName);
        }
        catch (final Exception e)
        {
          // Ignored, we'll let the Form handle the errors
          LOGGER.error("Unable add or update the current space", e);
        }

      }
    });
    view.getDelButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3893425584628753368L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          final List<ProjectApplication> allSpaceApplications = ApplicationsModule.getApplicationPresenter()
              .getAllSpaceApplications(currentSpace.getUri(), projectId);
          if ((allSpaceApplications == null) || (allSpaceApplications.isEmpty()))
          {
            ApplicationsModule.getSpacePresenter().removeSpace(projectId, currentSpace.getUri());
            getEventBus().publish(new DisplayDefaultRightEvent(getUuid()));
            getEventBus().publish(new RefreshTreeEvent(null, getUuid()));
          }
          else
          {
            Notification.show(
                ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
                    Messages.APPLICATIONS_SPACES_DELETE_NOTEMPTY), Type.WARNING_MESSAGE);
          }
        }
        catch (final Exception e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
              view.getLocale());

        }

      }
    });

  }

  /**
   * Will refresh {@link SpacePresenter} with the given {@link Space}
   * 
   * @param pSpace
   *          represents the space to display
   * @param pReadOnly
   *          if true so form is in readonly mode
   */
  public void refresh(final Space pSpace, final boolean pReadOnly)
  {

    currentSpace = pSpace;
    final boolean readOnly = pReadOnly;

    final BeanItem<Space> spaceItem = new BeanItem<Space>(currentSpace);
    view.getSpaceForm().setItemDataSource(spaceItem);
    view.getSpaceForm().setVisibleItemProperties(Arrays.asList(SPACE_FIELDS));
    view.getSpaceForm().setReadOnly(readOnly);
    view.getSpaceForm().getFooter().setVisible(!readOnly);
    view.getHeaderLayout().setVisible(readOnly);
    if (Strings.isNullOrEmpty(currentSpace.getName()))
    {
      view.getApplyButton().setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
      view.getApplyButton().setCaption(
          ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
              Messages.APPLICATIONS_SPACES_CREATE_CONFIRM));
      view.getAddAppButton().setVisible(false);
    }
    else
    {
      view.getApplyButton().setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
      view.getApplyButton().setCaption(
          ApplicationsModule.getPortalMessages().getMessage(view.getLocale(), Messages.ACTIONS_SAVE));
      view.getAddAppButton().setVisible(true);
    }

    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    // Handle by GlobalPresenter
  }

  /**
   * @return the currentSpace
   */
  public Space getCurrentSpace()
  {
    return currentSpace;
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
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
