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
package org.novaforge.forge.ui.forge.reference.client.view.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.validation.TextAreaValidation;
import org.novaforge.forge.ui.commons.client.validation.TextBoxValidation;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

/**
 * @author lamirang
 */
public class TemplateEditDetailViewImpl extends Composite implements TemplateEditDetailView
{
	private static TemplateDetailViewImplUiBinder uiBinder         = GWT
	                                                                   .create(TemplateDetailViewImplUiBinder.class);
	private static ReferenceResources             ressources       = GWT.create(ReferenceResources.class);
	private final TemplateMessage templateMessages = (TemplateMessage) GWT.create(TemplateMessage.class);
	@UiField
	Label              templateDetailTitle;
	@UiField
	Grid               templateGrid;
	@UiField
	Label              templateNameLabel;
	@UiField
	TextBoxValidation  templateNameValidator;
	@UiField
	Label              templateIdLabel;
	@UiField
	TextBoxValidation  templateIdValidator;
	@UiField
	Label              templateDescLabel;
	@UiField
	TextAreaValidation templateDesc;
	public TemplateEditDetailViewImpl(final ManagePresenterType pType)
	{
		ressources.style().ensureInjected();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Initialization of group detail form
		templateDetailTitle.setText(templateMessages.infoTitle());
		templateNameLabel.setText(templateMessages.name());
		templateIdLabel.setText(templateMessages.id());
		templateDescLabel.setText(templateMessages.description());

		// Initialization row style
		for (int row = 0; row < templateGrid.getRowCount(); row++)
		{
			if ((row % 2) == 0)
			{
				templateGrid.getRowFormatter().addStyleName(row, ressources.style().gridRowPair());
			}
			templateGrid.getCellFormatter().addStyleName(row, 0, ressources.style().labelCell());
		}
		if (ManagePresenterType.UPDATE.equals(pType))
		{
			templateIdValidator.setEnable(false);
		}
	}

	@Override
	public TextBoxValidation getTemplateName()
	{
		return templateNameValidator;
	}

	@Override
	public TextBoxValidation getTemplateId()
	{
		return templateIdValidator;
	}

	@Override
	public TextAreaValidation getTemplateDescription()
	{
		return templateDesc;
	}

	interface TemplateDetailViewImplUiBinder extends UiBinder<Widget, TemplateEditDetailViewImpl>
	{
	}
}
