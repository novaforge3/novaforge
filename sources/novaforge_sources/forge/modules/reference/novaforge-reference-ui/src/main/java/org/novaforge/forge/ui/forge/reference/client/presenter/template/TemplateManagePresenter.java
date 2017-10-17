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
package org.novaforge.forge.ui.forge.reference.client.presenter.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.template.CancelCreateTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.CancelEditTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.SaveCreateTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.SaveEditTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.StatusEnum;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.TemplateManageSpacesPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.roles.TemplateManageRolesPresenter;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateEditDetailViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateEditSummaryViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateManageView;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.TemplateEditSpacesViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.roles.RoleTabViewImpl;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;

/**
 * @author lamirang
 */
public class TemplateManagePresenter implements Presenter
{

	private static ReferenceResources            ressources = GWT.create(ReferenceResources.class);

	private final TemplateManageView             display;
	private final TemplateManageDetailPresenter  templateManageDetailPresenter;
	private final TemplateManageSummaryPresenter templateManageSummaryPresenter;
	private final TemplateManageSpacesPresenter  templateManageSpacesPresenter;
	private final TemplateManageRolesPresenter   templateManageRolesPresenter;
	private final ManagePresenterType            type;
	private final TemplateMessage                messages   = (TemplateMessage) GWT
	                                                            .create(TemplateMessage.class);
	private TemplateDTO currentTemplate;
	private StatusEnum  status;

	public TemplateManagePresenter(final TemplateManageView display, final ManagePresenterType pType)
	{
		super();
		this.display = display;
		type = pType;

		templateManageDetailPresenter = new TemplateManageDetailPresenter(new TemplateEditDetailViewImpl(type),
		    type);
		templateManageSummaryPresenter = new TemplateManageSummaryPresenter(new TemplateEditSummaryViewImpl());
		templateManageSpacesPresenter = new TemplateManageSpacesPresenter(new SimpleEventBus(),
		    new TemplateEditSpacesViewImpl());
		templateManageRolesPresenter = new TemplateManageRolesPresenter(new SimpleEventBus(),
		    new RoleTabViewImpl());
		
		Window.addResizeHandler(new ResizeHandler()
    {
      
      @Override
      public void onResize(ResizeEvent arg0)
      {
        //very very strange behaviour with firefox... => we force display reloading
        //from previous to current page for component including another component (ie. roles & spaces)
        Timer t ;
        if (status != null)
        {
        switch (status)
        {
          case DETAIL:
            displayDetail();
            break;
          case ROLES:
            displayDetail();
            
            t = new Timer() {
              @Override
              public void run() {
                displayRoles();
              }
            };
            t.schedule(1);
            break;
          case SPACES:
            displayDetail();
            
              t = new Timer() {
              @Override
              public void run() {
                displaySpaces();
              }
            };
            t.schedule(1);
            break;
          case SUMMARY:
            displaySummary();
            break;
          default:
            break;
        }
        }
      }
    });

		bind();
	}

	public void bind()
	{
		display.getPreviousButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
			  switch (status)
				{
					case DETAIL:
						// no action to perform
						break;
					case ROLES:
						currentTemplate = templateManageRolesPresenter.getCurrentTemplateDTO();
						displayDetail();
						break;
					case SPACES:
						currentTemplate = templateManageSpacesPresenter.getCurrentTemplateDTO();
						displayRoles();
						break;
					case SUMMARY:
						displaySpaces();
						break;
					default:
						break;
				}
			}
		});
		display.getNextButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				switch (status)
				{
					case DETAIL:
						if (templateManageDetailPresenter.isValid())
						{
							currentTemplate = templateManageDetailPresenter.getCurrentTemplateDTO();
							displayRoles();
						}
						else
						{
							final InfoDialogBox info = new InfoDialogBox(messages.templateErrorValidation());
							info.show();
						}
						break;
					case ROLES:
						currentTemplate = templateManageRolesPresenter.getCurrentTemplateDTO();
						displaySpaces();
						break;
					case SPACES:
						currentTemplate = templateManageSpacesPresenter.getCurrentTemplateDTO();
						displaySummary();
						break;
					case SUMMARY:
						// no action to perform
						break;
					default:
						break;
				}
			}

		});

		display.getSaveButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{

				display.getValidateDialogBox().show();

			}
		});

		display.getCancelButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(final ClickEvent event)
			{
				switch (type)
				{
					case CREATE:
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelCreateTemplateEvent());
						break;
					case UPDATE:
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelEditTemplateEvent());
						break;
					default:
						break;
				}
			}
		});

		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();
				switch (type)
				{

					case CREATE:
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new SaveCreateTemplateEvent(currentTemplate));
						break;
					case UPDATE:
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new SaveEditTemplateEvent(currentTemplate));
						break;
					default:
						break;
				}
			}
		});
	}

	/**
	 *
	 */
	private void displayDetail()
	{
		status = StatusEnum.DETAIL;

		TemplateManagePresenter.this.display.getPreviousButton().setEnabled(false);
		TemplateManagePresenter.this.display.getNextButton().setEnabled(true);
		TemplateManagePresenter.this.display.getSaveButton().setEnabled(false);

		TemplateManagePresenter.this.display.getRoles().setStyleName(ressources.style().menuLabel());
		TemplateManagePresenter.this.display.getDetail().setStyleName(ressources.style().menuLabelSelected());
		TemplateManagePresenter.this.display.getSpaces().setStyleName(ressources.style().menuLabel());
		TemplateManagePresenter.this.display.getSummary().setStyleName(ressources.style().menuLabel());
		TemplateManagePresenter.this.templateManageDetailPresenter.setCurrentTemplateDTO(currentTemplate);
		TemplateManagePresenter.this.templateManageDetailPresenter.go(TemplateManagePresenter.this.display
		    .getCenterPanel());
	}


  /**
   *
   */
  private void displayRoles()
  {
    status = StatusEnum.ROLES;

    TemplateManagePresenter.this.display.getPreviousButton().setEnabled(true);
    TemplateManagePresenter.this.display.getNextButton().setEnabled(true);
    TemplateManagePresenter.this.display.getSaveButton().setEnabled(false);

    TemplateManagePresenter.this.display.getDetail().setStyleName(ressources.style().menuLabel());
    TemplateManagePresenter.this.display.getRoles().setStyleName(ressources.style().menuLabelSelected());
    TemplateManagePresenter.this.display.getSpaces().setStyleName(ressources.style().menuLabel());
    TemplateManagePresenter.this.display.getSummary().setStyleName(ressources.style().menuLabel());

    TemplateManagePresenter.this.templateManageRolesPresenter.setCurrentTemplateDTO(currentTemplate);
    TemplateManagePresenter.this.templateManageRolesPresenter.go(TemplateManagePresenter.this.display.getCenterPanel());
    TemplateManagePresenter.this.templateManageRolesPresenter.refreshRoleList();
  }

  /**
    * 
    */
  private void displaySpaces()
  {
    status = StatusEnum.SPACES;

    TemplateManagePresenter.this.display.getPreviousButton().setEnabled(true);
    TemplateManagePresenter.this.display.getNextButton().setEnabled(true);
    TemplateManagePresenter.this.display.getSaveButton().setEnabled(false);

    TemplateManagePresenter.this.display.getDetail().setStyleName(ressources.style().menuLabel());
    TemplateManagePresenter.this.display.getRoles().setStyleName(ressources.style().menuLabel());
    TemplateManagePresenter.this.display.getSpaces().setStyleName(ressources.style().menuLabelSelected());
    TemplateManagePresenter.this.display.getSummary().setStyleName(ressources.style().menuLabel());

    TemplateManagePresenter.this.templateManageSpacesPresenter.go(TemplateManagePresenter.this.display.getCenterPanel());
    TemplateManagePresenter.this.templateManageSpacesPresenter.refreshTemplateTree(currentTemplate);
  }


	/**
	 *
	 */
	private void displaySummary()
	{
		status = StatusEnum.SUMMARY;
		TemplateManagePresenter.this.display.getPreviousButton().setEnabled(true);
		TemplateManagePresenter.this.display.getNextButton().setEnabled(false);
		TemplateManagePresenter.this.display.getSaveButton().setEnabled(true);

		TemplateManagePresenter.this.display.getDetail().setStyleName(ressources.style().menuLabel());
		TemplateManagePresenter.this.display.getRoles().setStyleName(ressources.style().menuLabel());
		TemplateManagePresenter.this.display.getSpaces().setStyleName(ressources.style().menuLabel());
		TemplateManagePresenter.this.display.getSummary().setStyleName(ressources.style().menuLabelSelected());
		TemplateManagePresenter.this.templateManageSummaryPresenter.setCurrentTemplateDTO(currentTemplate);
		TemplateManagePresenter.this.templateManageSummaryPresenter.go(TemplateManagePresenter.this.display
		    .getCenterPanel());
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
		displayDetail();

	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}

	public TemplateDTO getCurrentTemplate()
	{
		return currentTemplate;
	}

	public void setCurrentTemplate(final TemplateDTO pCurrentTemplateDTO)
	{
		currentTemplate = pCurrentTemplateDTO;
	}

	public StatusEnum getStatus()
	{
		return status;
	}

	public void setStatus(final StatusEnum status)
	{
		this.status = status;
	}

}
