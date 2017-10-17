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
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateEditDetailView;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;

/**
 * @author lamirang
 */
public class TemplateManageDetailPresenter implements Presenter
{

	private static final String          EMPTY_TEXT      = "";
	private final TemplateEditDetailView display;
	private final ManagePresenterType    type;
	private final ProjectMessage         projectMessages = (ProjectMessage) GWT.create(ProjectMessage.class);
	private TemplateDTO currentTemplate;

	public TemplateManageDetailPresenter(final TemplateEditDetailView display, final ManagePresenterType pType)
	{
		super();
		this.display = display;
		type = pType;

		bind();
	}

	public void bind()
	{
		if (ManagePresenterType.CREATE.equals(type))
		{
			display.getTemplateName().addKeyUpHandler(new KeyUpHandler()
			{
				@Override
				public void onKeyUp(final KeyUpEvent event)
				{
					final String value = display.getTemplateName().getValue();
					display.getTemplateId().setValue(normalize(value), true);
				}
			});
			display.getTemplateName().addValueChangeHandler(new ValueChangeHandler<String>()
			{
				@Override
				public void onValueChange(final ValueChangeEvent<String> pEvent)
				{
					final String value = display.getTemplateName().getValue();
					display.getTemplateId().setValue(normalize(value), true);

				}
			});
		}
		display.getTemplateName().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return !((pValue == null) || EMPTY_TEXT.equals(pValue));
			}

			@Override
			public String getErrorMessage()
			{
				return projectMessages.projectNameValidation();
			}
		});
		display.getTemplateId().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return !((pValue == null) || EMPTY_TEXT.equals(pValue) || pValue.matches(".*[^a-z0-9_].*"));
			}

			@Override
			public String getErrorMessage()
			{
				return projectMessages.projectIdentifiantValidation();
			}
		});
		display.getTemplateDescription().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				boolean valid = false;
				if ((pValue != null) && (!EMPTY_TEXT.equals(pValue)) && (pValue.length() < 250))
				{
					valid = true;
				}
				return valid;
			}

			@Override
			public String getErrorMessage()
			{
				return projectMessages.projectDescriptionValidation();
			}
		});
	}

	public final native String normalize(String pValue)
	/*-{
	return $wnd.normalize(pValue);
	}-*/;

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
	}

	/**
	 * @return the currentTemplateDTO
	 */
	public TemplateDTO getCurrentTemplateDTO()
	{
		currentTemplate.setId(TemplateManageDetailPresenter.this.display.getTemplateId().getValue());
		currentTemplate.setName(TemplateManageDetailPresenter.this.display.getTemplateName().getValue());
		currentTemplate.setDescription(TemplateManageDetailPresenter.this.display.getTemplateDescription()
		    .getValue());
		currentTemplate.getRootNode().setName(currentTemplate.getName());
		currentTemplate.getRootNode().setUri(currentTemplate.getName());
		return currentTemplate;
	}

	/**
	 *
	 */
	public void setCurrentTemplateDTO(final TemplateDTO pTemplateDTO)
	{
		if (pTemplateDTO != null)
		{
			currentTemplate = pTemplateDTO;
			TemplateManageDetailPresenter.this.display.getTemplateName().setValue(currentTemplate.getName());
			TemplateManageDetailPresenter.this.display.getTemplateId().setValue(currentTemplate.getId());
			TemplateManageDetailPresenter.this.display.getTemplateDescription().setValue(currentTemplate.getDescription());
		}
	}

	public boolean isValid()
	{
		boolean isValid = false;
		if (display.getTemplateDescription().isValid() && display.getTemplateName().isValid()
		    && display.getTemplateId().isValid())
		{
			isValid = true;
		}
		else
		{
			// The following has to be done to display an error message
			display.getTemplateDescription().isValid();
			display.getTemplateName().isValid();
			display.getTemplateId().isValid();
		}
		display.getTemplateName().isValid();
		return isValid;
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
