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
package org.novaforge.forge.ui.forge.reference.client.presenter.template.roles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateCancelCreateRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateCancelEditRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateDeleteRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateEditRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateSaveCreateRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateSaveEditRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateSelectRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ViewEnum;
import org.novaforge.forge.ui.forge.reference.client.properties.RoleMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.roles.RoleDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.roles.RoleManageViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.roles.RoleTabView;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lamirang
 */
public class TemplateManageRolesPresenter implements Presenter
{

  private final SimpleEventBus eventBus;
  private final RoleMessage messages = (RoleMessage) GWT.create(RoleMessage.class);
  private final RoleDetailPresenter roleDetailPresenter;
  private final RoleManagePresenter roleEditPresenter;
  private final RoleManagePresenter roleCreatePresenter;
  private final RoleTabView         display;
  private       ViewEnum            activatedView;
  private       RoleDTO             currentSelectedKey;
  private       TemplateDTO         currentTemplate;

  public TemplateManageRolesPresenter(final SimpleEventBus eventBus, final RoleTabView display)
  {
    super();
    this.eventBus = eventBus;
    this.display = display;
    roleCreatePresenter = new RoleManagePresenter(eventBus, new RoleManageViewImpl(ManagePresenterType.CREATE),
                                                  ManagePresenterType.CREATE);
    roleEditPresenter = new RoleManagePresenter(eventBus, new RoleManageViewImpl(ManagePresenterType.UPDATE),
                                                ManagePresenterType.UPDATE);
    roleDetailPresenter = new RoleDetailPresenter(eventBus, new RoleDetailViewImpl());

    bind();
  }

  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    display.getAddButton().setEnabled(true);
    container.add(display.asWidget());
  }

  /**
   * @return the currentTemplateDTO
   */
  public TemplateDTO getCurrentTemplateDTO()
  {
    return currentTemplate;
  }

  /**
   * @return the currentTemplateDTO
   */
  public void setCurrentTemplateDTO(final TemplateDTO pTemplateDTO)
  {
    currentTemplate = pTemplateDTO;
  }

  public void bind()
  {
    display.getReloadImage().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        refreshRoleList();

      }
    });
    display.getArrowTop().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        switch (activatedView)
        {
          case ADD:
          case EDIT:
            final InfoDialogBox info = new InfoDialogBox(messages.editionActive());
            info.show();
            break;

          case READ:
            currentSelectedKey = display.getListSelectionModel().getSelectedObject();
            if (currentSelectedKey.getOrder() > 1)
            {
              changeRoleOrder(currentSelectedKey.getName(), false);
            }
            break;
        }

      }
    });
    display.getArrowBottom().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        switch (activatedView)
        {
          case ADD:
          case EDIT:
            final InfoDialogBox info = new InfoDialogBox(messages.editionActive());
            info.show();
            break;

          case READ:
            currentSelectedKey = display.getListSelectionModel().getSelectedObject();

            if (currentSelectedKey.getOrder() < currentTemplate.getRoles().size())
            {
              changeRoleOrder(currentSelectedKey.getName(), true);
            }
            break;
        }
      }
    });
    display.getAddButton().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        activatedView = ViewEnum.ADD;
        display.getAddButton().setEnabled(false);
        roleCreatePresenter.updateRoleDetails(null);
        roleCreatePresenter.go(display.getContentPanel());

      }
    });
    display.getListSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
    {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event)
      {
        switch (activatedView)
        {
          case ADD:
          case EDIT:
            if ((currentSelectedKey != null) && (currentSelectedKey != getRoleSelected()))
            {
              final InfoDialogBox info = new InfoDialogBox(messages.editionActive());
              info.show();
            }
            break;

          case READ:
            currentSelectedKey = display.getListSelectionModel().getSelectedObject();
            eventBus.fireEvent(new TemplateSelectRoleEvent(currentSelectedKey));
            break;
        }
      }

    });

    eventBus.addHandler(TemplateSelectRoleEvent.TYPE, new TemplateSelectRoleEvent.Handler()
    {
      @Override
      public void refreshView(final TemplateSelectRoleEvent pEvent)
      {
        activatedView = ViewEnum.READ;
        roleDetailPresenter.updateRoleDetails(pEvent.getRoleDTO());
        roleDetailPresenter.go(display.getContentPanel());
      }
    });

    eventBus.addHandler(TemplateEditRoleEvent.TYPE, new TemplateEditRoleEvent.Handler()
    {
      @Override
      public void editRole(final TemplateEditRoleEvent pEvent)
      {
        activatedView = ViewEnum.EDIT;
        display.getAddButton().setEnabled(false);
        roleEditPresenter.go(display.getContentPanel());
        roleEditPresenter.updateRoleDetails(pEvent.getRoleDTO());
      }
    });

    eventBus.addHandler(TemplateSaveEditRoleEvent.TYPE, new TemplateSaveEditRoleEvent.Handler()
    {
      @Override
      public void saveUpdateRole(final TemplateSaveEditRoleEvent pEvent)
      {
        final RoleDTO roleDTO = pEvent.getRoleDTO();
        updateRole(pEvent.getOldRoleName(), roleDTO);
        display.getAddButton().setEnabled(true);
      }
    });
    eventBus.addHandler(TemplateCancelEditRoleEvent.TYPE, new TemplateCancelEditRoleEvent.Handler()
    {
      @Override
      public void cancelEditRole(final TemplateCancelEditRoleEvent pEvent)
      {
        eventBus.fireEvent(new TemplateSelectRoleEvent(currentSelectedKey));
        display.getAddButton().setEnabled(true);
      }
    });
    eventBus.addHandler(TemplateSaveCreateRoleEvent.TYPE, new TemplateSaveCreateRoleEvent.Handler()
    {
      @Override
      public void saveCreateRole(final TemplateSaveCreateRoleEvent pEvent)
      {
        final RoleDTO roleDTO = pEvent.getRoleDTO();
        createRole(roleDTO);
        display.getAddButton().setEnabled(true);
      }
    });
    eventBus.addHandler(TemplateCancelCreateRoleEvent.TYPE, new TemplateCancelCreateRoleEvent.Handler()
    {
      @Override
      public void cancelCreateRole(final TemplateCancelCreateRoleEvent pEvent)
      {
        if (currentSelectedKey != null)
        {
          eventBus.fireEvent(new TemplateSelectRoleEvent(currentSelectedKey));
        }
        else
        {
          display.getContentPanel().clear();
        }
        display.getAddButton().setEnabled(true);
      }
    });

    eventBus.addHandler(TemplateDeleteRoleEvent.TYPE, new TemplateDeleteRoleEvent.Handler()
    {
      @Override
      public void deleteRole(final TemplateDeleteRoleEvent pEvent)
      {
        TemplateManageRolesPresenter.this.deleteRole(pEvent.getRoleDTO());
        display.getAddButton().setEnabled(true);
      }
    });
  }

  private RoleDTO getRoleSelected()
  {
    return display.getListSelectionModel().getSelectedObject();
  }

  public void refreshRoleList()
  {
    cleanRolesOrder();
    TemplateManageRolesPresenter.this.display.getAddButton().setEnabled(true);

    TemplateManageRolesPresenter.this.display.getListDataProvider()
                                             .setList(TemplateManageRolesPresenter.this.currentTemplate.getRoles());
    TemplateManageRolesPresenter.this.display.getCellList()
                                             .setPageSize(TemplateManageRolesPresenter.this.currentTemplate.getRoles()
                                                                                                           .size());

    TemplateManageRolesPresenter.this.activatedView = ViewEnum.READ;

    if (TemplateManageRolesPresenter.this.currentSelectedKey != null)
    {
      TemplateManageRolesPresenter.this.display.getListSelectionModel()
                                               .setSelected(TemplateManageRolesPresenter.this.currentSelectedKey, true);
      TemplateManageRolesPresenter.this.eventBus
          .fireEvent(new TemplateSelectRoleEvent(TemplateManageRolesPresenter.this.currentSelectedKey));

    }
    else if (!TemplateManageRolesPresenter.this.currentTemplate.getRoles().isEmpty())
    {
      TemplateManageRolesPresenter.this.currentSelectedKey = TemplateManageRolesPresenter.this.currentTemplate
                                                                 .getRoles().get(0);
      TemplateManageRolesPresenter.this.display.getListSelectionModel()
                                               .setSelected(TemplateManageRolesPresenter.this.currentSelectedKey, true);
      TemplateManageRolesPresenter.this.eventBus
          .fireEvent(new TemplateSelectRoleEvent(TemplateManageRolesPresenter.this.currentSelectedKey));
    }
    else
    {
      TemplateManageRolesPresenter.this.display.getContentPanel().clear();
    }

  }

  private void cleanRolesOrder()
  {
    Collections.sort(TemplateManageRolesPresenter.this.currentTemplate.getRoles());
    final List<RoleDTO> roles         = currentTemplate.getRoles();
    int                 previousOrder = 0;
    for (final RoleDTO roleDTO : roles)
    {
      final int newOrder = previousOrder + 1;
      if (roleDTO.getOrder() != newOrder)
      {
        roleDTO.setOrder(newOrder);
      }
      previousOrder = roleDTO.getOrder();
    }
  }

  private void createRole(final RoleDTO pRole)
  {
    pRole.setOrder(currentTemplate.getRoles().size() + 1);
    currentTemplate.getRoles().add(pRole);
    currentSelectedKey = pRole;
    refreshRoleList();
  }

  private void updateRole(final String pOldName, final RoleDTO pRole)
  {
    for (final RoleDTO role : currentTemplate.getRoles())
    {
      if (role.getName().equals(pOldName))
      {
        role.setName(pRole.getName());
        role.setDescription(pRole.getDescription());
      }

    }
    currentSelectedKey = pRole;
    refreshRoleList();
  }

  private void deleteRole(final RoleDTO pRoleDTO)
  {
    final List<RoleDTO> temp = new ArrayList<RoleDTO>(currentTemplate.getRoles());
    for (final RoleDTO role : temp)
    {
      if (role.getName().equals(pRoleDTO.getName()))
      {
        currentTemplate.getRoles().remove(role);
      }
    }
    refreshRoleList();

  }

  private void changeRoleOrder(final String pRoleName, final boolean pIncreased)
  {
    final List<RoleDTO> temp = currentTemplate.getRoles();

    int oldRoleOrder = 0;
    int newRoleOrder = 0;
    // Get role order and increased/decreased it
    for (final RoleDTO role : temp)
    {
      if (role.getName().equals(pRoleName))
      {
        oldRoleOrder = role.getOrder();
        if (pIncreased)
        {
          newRoleOrder = oldRoleOrder + 1;
          role.setOrder(newRoleOrder);
        }
        else
        {
          newRoleOrder = oldRoleOrder - 1;
          role.setOrder(newRoleOrder);
        }
        break;
      }
    }

    // Get the other role order and increased it
    for (final RoleDTO role : temp)
    {
      if ((role.getName() != null) && ((!role.getName().equals(pRoleName)) && (role.getOrder() == newRoleOrder)))
      {
        role.setOrder(oldRoleOrder);
        break;

      }
    }
    refreshRoleList();
  }

  public IsWidget getDisplay()
  {
    return display.asWidget();
  }
}
