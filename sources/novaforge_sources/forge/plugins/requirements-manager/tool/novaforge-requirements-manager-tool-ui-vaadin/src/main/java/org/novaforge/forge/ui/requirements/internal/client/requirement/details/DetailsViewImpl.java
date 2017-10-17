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
package org.novaforge.forge.ui.requirements.internal.client.requirement.details;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Locale;

/**
 * This view is used to display requirement details
 * 
 * @author Jeremy Casery
 */
public class DetailsViewImpl extends VerticalLayout implements DetailsView
{
  /**
   * Serialization id
   */
  private static final long      serialVersionUID  = -2297757369169962496L;
  /**
   * The header layout
   */
  private final HorizontalLayout headerLayout      = new HorizontalLayout();
  /**
   * The header title label
   */
  private final Label            headerTitle       = new Label();
  /**
   * The header close button
   */
  private final Button           headerCloseButton = new Button();
  /**
   * The type label
   */
  private final Label            typeLabel         = new Label();
  /**
   * The type value label
   */
  private final Label            typeValue         = new Label();
  /**
   * The version label
   */
  private final Label            versionLabel      = new Label();
  /**
   * The version value label
   */
  private final Label            versionValue      = new Label();
  /**
   * The object reference label
   */
  private final Label            objRefLabel       = new Label();
  /**
   * The object reference value label
   */
  private final Label            objRefValue       = new Label();
  /**
   * The name value label
   */
  private final Label            nameValue         = new Label();
  /**
   * The status label
   */
  private final Label            statusLabel       = new Label();
  /**
   * The status value label
   */
  private final Label            statusValue       = new Label();
  /**
   * The description form
   */
  private final Form             descForm          = new Form();
  /**
   * The description value label
   */
  private final Label            descValue         = new Label();
  /**
   * The acceptance criteria form
   */
  private final Form             accepCritForm     = new Form();
  /**
   * The acceptance criteria value label
   */
  private final Label            accepCritValue    = new Label();
  /**
   * The requirement id
   */
  private String requirementID = null;

  /**
   * Default constructor.
   */
  public DetailsViewImpl()
  {
    setWidth(100, Unit.PERCENTAGE);
    headerLayout.addComponent(headerTitle);
    headerLayout.addComponent(headerCloseButton);
    headerLayout.setComponentAlignment(headerTitle, Alignment.MIDDLE_LEFT);
    headerLayout.setComponentAlignment(headerCloseButton, Alignment.MIDDLE_RIGHT);
    headerLayout.setMargin(true);
    headerLayout.setWidth(100, Unit.PERCENTAGE);
    headerLayout.setStyleName(NovaForge.LAYOUT_LIGHTBLUE);
    headerLayout.setExpandRatio(headerTitle, 1);
    addComponent(headerLayout);
    final HorizontalLayout typeLayout = new HorizontalLayout();
    typeLayout.setSpacing(true);
    typeLayout.addComponent(typeLabel);
    typeLayout.addComponent(typeValue);
    final HorizontalLayout versionLayout = new HorizontalLayout();
    versionLayout.setSpacing(true);
    versionLayout.addComponent(versionLabel);
    versionLayout.addComponent(versionValue);
    final HorizontalLayout objRefLayout = new HorizontalLayout();
    objRefLayout.setSpacing(true);
    objRefLayout.addComponent(objRefLabel);
    objRefLayout.addComponent(objRefValue);
    final HorizontalLayout statusLayout = new HorizontalLayout();
    statusLayout.setSpacing(true);
    statusLayout.addComponent(statusLabel);
    statusLayout.addComponent(statusValue);
    final GridLayout subGrid = new GridLayout(2, 2);
    subGrid.addComponent(typeLayout, 0, 0);
    subGrid.addComponent(versionLayout, 1, 0);
    subGrid.addComponent(statusLayout, 0, 1);
    subGrid.addComponent(objRefLayout, 1, 1);
    subGrid.setWidth(100, Unit.PERCENTAGE);
    subGrid.setMargin(new MarginInfo(true, false, true, false));

    descForm.getLayout().addComponent(descValue);
    accepCritForm.getLayout().addComponent(accepCritValue);

    final VerticalLayout contentLayout = new VerticalLayout();
    contentLayout.setSizeFull();
    contentLayout.setMargin(true);
    contentLayout.setSpacing(true);
    contentLayout.addComponent(subGrid);
    contentLayout.addComponent(nameValue);
    contentLayout.addComponent(descForm);
    contentLayout.addComponent(accepCritForm);
    addComponent(contentLayout);

    contentLayout.setComponentAlignment(nameValue, Alignment.MIDDLE_CENTER);
    contentLayout.setComponentAlignment(descForm, Alignment.MIDDLE_CENTER);
    contentLayout.setComponentAlignment(accepCritForm, Alignment.MIDDLE_CENTER);

    headerTitle.setStyleName(NovaForge.LABEL_BOLD);
    headerTitle.addStyleName(NovaForge.TEXT_WRAP);
    typeLabel.setStyleName(NovaForge.LABEL_BOLD);
    versionLabel.setStyleName(NovaForge.LABEL_BOLD);
    objRefLabel.setStyleName(NovaForge.LABEL_BOLD);
    nameValue.setStyleName(NovaForge.LABEL_H2);
    nameValue.addStyleName(NovaForge.TEXT_WRAP);
    descValue.setStyleName(NovaForge.TEXT_WRAP);
    accepCritValue.setStyleName(NovaForge.TEXT_WRAP);
    headerCloseButton.setStyleName(NovaForge.BUTTON_IMAGE);
    headerCloseButton.setIcon(new ThemeResource(NovaForgeResources.ICON_CLOSE));
    setExpandRatio(contentLayout, 1);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    final String colonChar = " :";

    headerTitle.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.DETAILS_TITLE,
        requirementID));
    typeLabel.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_TYPE)
        + colonChar);
    versionLabel.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_VERSION)
        + colonChar);
    objRefLabel.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_REFERENCE)
        + colonChar);
    statusLabel.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_STATUS)
        + colonChar);
    descForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_DESCRIPTION));
    accepCritForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_ACCEPTANCE_CRITERIA));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getHeaderTitle()
  {
    return headerTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getHeaderCloseButton()
  {
    return headerCloseButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getTypeValue()
  {
    return typeValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getVersionValue()
  {
    return versionValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getObjRefValue()
  {
    return objRefValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNameValue()
  {
    return nameValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getStatusValue()
  {
    return statusValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getDescValue()
  {
    return descValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getAccepCritValue()
  {
    return accepCritValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRequirementID(String requirementID)
  {
    this.requirementID = requirementID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getStatusLabel()
  {
    return statusLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPopupMode(final boolean pIsPopupMode)
  {
    if (pIsPopupMode)
    {
      headerLayout.setVisible(false);
    }
    else
    {
      headerLayout.setVisible(true);
    }
  }

}
