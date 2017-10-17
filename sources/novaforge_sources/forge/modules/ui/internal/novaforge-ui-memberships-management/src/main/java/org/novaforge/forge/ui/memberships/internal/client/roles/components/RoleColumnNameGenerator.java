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
package org.novaforge.forge.ui.memberships.internal.client.roles.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class RoleColumnNameGenerator implements ColumnGenerator
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 1175065031848795493L;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final VerticalLayout layout = new VerticalLayout();
    final Item item = pSource.getItem(pItemId);
    final MembershipInfo defaultMember = (MembershipInfo) item.getItemProperty(
        UserItemProperty.DEFAULT.getPropertyId()).getValue();
    final List<MembershipInfo> membershipsInfo = (List<MembershipInfo>) item.getItemProperty(
        UserItemProperty.MEMBERSHIPS.getPropertyId()).getValue();
    if ((membershipsInfo != null) && (!membershipsInfo.isEmpty()))
    {
      for (final MembershipInfo membershipInfo : membershipsInfo)
      {
        final Label label = new Label(membershipInfo.getRole().getName());
        label.setDescription(membershipInfo.getRole().getDescription());
        if (membershipInfo.equals(defaultMember))
        {
          label.setStyleName(NovaForge.LABEL_BOLD);
        }
        layout.addComponent(label);
      }
    }
    else
    {
      final Label noRole = new Label(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_ROLES_NOROLE));
      noRole.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_ROLES_NOROLE_DESCRIPTION));
      layout.addComponent(noRole);
    }
    return layout;
  }
}
