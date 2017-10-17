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
package org.novaforge.forge.ui.user.management.internal.client.components;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;
import org.novaforge.forge.ui.user.management.internal.module.PublicModule;

import java.util.List;
import java.util.Locale;

/**
 * This component describes a specific {@link IndexedContainer} used to build projects icon.
 * 
 * @author caseryj
 */
@SuppressWarnings("unchecked")
public class ProjectsContainer extends IndexedContainer
{

  /**
   * Default serial version UID
   */
  private static final long serialVersionUID = -981255362730667240L;

  /**
   * Default constructor. It will initialize project item property
   * 
   * @see ProjectItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public ProjectsContainer()
  {
    super();
    addContainerProperty(ProjectItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ProjectItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(ProjectItemProperty.IS_MEMBER.getPropertyId(), Boolean.class, false);
    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add project into container
   * 
   * @param pProject
   *          the project to add
   * @param pUserMemberships
   *          membership of the user for the given project, <code>null</code> means current authenticated user
   *          haven't admin rights
   * @param pLocale
   *          the current locale
   */
  public void addProject(final Project pProject, final List<MembershipInfo> pUserMemberships,
      final Locale pLocale)
  {
    final String elementId = pProject.getElementId();
    if (!containsId(elementId))
    {
      addItem(elementId);
      getContainerProperty(elementId, ProjectItemProperty.ID.getPropertyId()).setValue(elementId);
      getContainerProperty(elementId, ProjectItemProperty.NAME.getPropertyId()).setValue(pProject.getName());
      getContainerProperty(elementId, ProjectItemProperty.DESCRIPTION.getPropertyId()).setValue(
          pProject.getDescription());
      if ((pProject.getImage() != null) && (pProject.getImage().getFile() != null))
      {
        final StreamResource imageResource = ResourceUtils.buildImageResource(pProject.getImage().getFile(),
            elementId);
        imageResource.setMIMEType(pProject.getImage().getMimeType());
        getContainerProperty(elementId, ProjectItemProperty.ICON.getPropertyId()).setValue(imageResource);
      }

      try
      {
        final List<Membership> currentUserMemberships = PublicModule.getMembershipPresenter()
            .getAllEffectiveUserMembershipsForUserAndProject(pProject.getProjectId(),
                PublicModule.getAuthentificationService().getCurrentUser());

        if ((currentUserMemberships != null) && (!currentUserMemberships.isEmpty()))
        {
          getContainerProperty(elementId, ProjectItemProperty.IS_MEMBER.getPropertyId()).setValue(true);
        }
      }
      catch (final ProjectServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
            .getLocale());
      }

      if ((pUserMemberships != null) && (!pUserMemberships.isEmpty()))
      {
        final StringBuilder sb = new StringBuilder();
        sb.append("<div style='margin: 5px'>");
        sb.append("<h2>");
        sb.append(PublicModule.getPortalMessages().getMessage(pLocale, Messages.USERMANAGEMENT_USER_ROLE,
            pProject.getName()));
        sb.append("</h2>");
        sb.append("<ul>");
        for (final MembershipInfo userMembershipInfo : pUserMemberships)
        {
          final Actor userMembershipActor = userMembershipInfo.getActor();
          sb.append("<li>");
          if (userMembershipInfo.getPriority())
          {
            sb.append("<b>");
          }
          sb.append(userMembershipInfo.getRole().getName());
          if (userMembershipInfo.getPriority())
          {
            sb.append("</b>");
          }
          if (userMembershipActor instanceof Group)
          {
            sb.append(" ");
            sb.append(PublicModule.getPortalMessages().getMessage(pLocale,
                Messages.USERMANAGEMENT_USER_ROLE_GROUP, ((Group) userMembershipActor).getName()));
          }
          sb.append("</li>");
        }
        sb.append("</ul>");
        sb.append("</div>");
        getContainerProperty(elementId, ProjectItemProperty.DESCRIPTION.getPropertyId()).setValue(
            sb.toString());
      }

    }

  }
}
