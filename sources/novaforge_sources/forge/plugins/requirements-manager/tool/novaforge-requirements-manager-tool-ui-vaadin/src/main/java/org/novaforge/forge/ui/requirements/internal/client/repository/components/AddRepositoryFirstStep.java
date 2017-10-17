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
package org.novaforge.forge.ui.requirements.internal.client.repository.components;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AddRepositoryFirstStep implements WizardStep
{
  /**
   * The default logo width
   */
  private final static float LOGO__WIDTH  = 300;
  /**
   * The default logo height
   */
  private final static float LOGO__HEIGHT = 100;
  /**
   * The {@link AddRepositoryWizard} which contains this {@link WizardStep}
   */
  private final AddRepositoryWizard wizard;
  /**
   * The choose type {@link Label}
   */
  private final Label            chooseTypeLabel = new Label();
  /**
   * The content {@link VerticalLayout}
   */
  private final VerticalLayout   contentLayout   = new VerticalLayout();
  /**
   * The Obeo layout
   */
  private final HorizontalLayout obeoLayout      = new HorizontalLayout();
  /**
   * The Obeo {@link Embedded} logo
   */
  private final Embedded         obeoLogo        = new Embedded();
  /**
   * The excel layout
   */
  private final HorizontalLayout excelLayout     = new HorizontalLayout();
  /**
   * The Excel {@link Embedded} logo
   */
  private final Embedded         excelLogo       = new Embedded();
  /**
   * The repository type, used to remember the user choice
   */
  private       RepositoryType   repositoryType  = null;

  /**
   * Default constructor
   *
   * @param pWizard
   *          the {@link AddRepositoryWizard} associated
   */
  public AddRepositoryFirstStep(final AddRepositoryWizard pWizard)
  {

    wizard = pWizard;

    obeoLogo.setSource(new ThemeResource(RepositoryType.OBEO.getLogo()));
    obeoLogo.setWidth(LOGO__WIDTH, Unit.PIXELS);
    obeoLogo.setHeight(LOGO__HEIGHT, Unit.PIXELS);
    obeoLayout.setWidth(100, Unit.PERCENTAGE);
    obeoLayout.setStyleName(NovaForge.LAYOUT_SELECTABLE);
    obeoLayout.setSpacing(true);
    excelLogo.setSource(new ThemeResource(RepositoryType.EXCEL.getLogo()));
    excelLogo.setWidth(LOGO__WIDTH, Unit.PIXELS);
    excelLogo.setHeight(LOGO__HEIGHT, Unit.PIXELS);
    excelLayout.setWidth(100, Unit.PERCENTAGE);
    excelLayout.setStyleName(NovaForge.LAYOUT_SELECTABLE);
    excelLayout.setSpacing(true);

    obeoLayout.addComponent(obeoLogo);
    excelLayout.addComponent(excelLogo);
    contentLayout.addComponent(chooseTypeLabel);
    contentLayout.addComponent(obeoLayout);
    contentLayout.addComponent(excelLayout);

    contentLayout.setMargin(true);
    contentLayout.setSpacing(true);

    addListeners();

  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    getObeoLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -4282705521251262945L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (getObeoLayout().getStyleName().equals(NovaForge.LAYOUT_SELECTABLE))
        {
          getExcelLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getExcelLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getObeoLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
          getObeoLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
          repositoryType = RepositoryType.OBEO;
          wizard.getNextButton().setEnabled(true);
        }
        else
        {
          getObeoLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getObeoLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          repositoryType = null;
          wizard.getNextButton().setEnabled(false);
        }
      }
    });
    getExcelLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -3099336288991870456L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (getExcelLayout().getStyleName().equals(NovaForge.LAYOUT_SELECTABLE))
        {
          getObeoLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getObeoLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExcelLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExcelLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
          repositoryType = RepositoryType.EXCEL;
          wizard.getNextButton().setEnabled(true);
        }
        else
        {
          getExcelLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getExcelLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          repositoryType = null;
          wizard.getNextButton().setEnabled(false);
        }
      }
    });
  }

  /**
   * Get the Obeo {@link HorizontalLayout}
   *
   * @return the layout
   */
  public HorizontalLayout getObeoLayout()
  {
    return obeoLayout;
  }

  /**
   * Get the Excel {@link HorizontalLayout}
   *
   * @return the layout
   */
  public HorizontalLayout getExcelLayout()
  {
    return excelLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption()
  {
    return RequirementsModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.REPOSITORY_ADD_STEPONE_TITLE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getContent()
  {
    return contentLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onAdvance()
  {
    wizard.setRepositoryType(repositoryType);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onBack()
  {
    return false;
  }

  /**
   * Empty the content datas
   */
  public void emptyDatas()
  {
    getObeoLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
    getObeoLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
    getExcelLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
    getExcelLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
  }

  /**
   * Get the choose type {@link Label}
   *
   * @return the label
   */
  public Label getChooseTypeLabel()
  {
    return chooseTypeLabel;
  }

  /**
   * Refresh internationalized content with given {@link Locale}
   * 
   * @param pLocale
   *          the locale to use
   */
  public void refreshLocale(final Locale pLocale)
  {
    chooseTypeLabel.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REPOSITORY_ADD_REPOSITORY_TYPE));
  }

  /**
   * Get the {@link RepositoryType} choosen by user
   * 
   * @return
   */
  public RepositoryType getRepositoryType()
  {
    return repositoryType;
  }

  /**
   * Set the {@link RepositoryType}
   * 
   * @param repositoryType
   *          to set
   */
  public void setRepositoryType(RepositoryType repositoryType)
  {
    this.repositoryType = repositoryType;
    if (RepositoryType.OBEO.equals(repositoryType))
    {
      getExcelLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
      getExcelLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
      getObeoLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
      getObeoLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
    }
    else if (RepositoryType.EXCEL.equals(repositoryType))
    {
      getObeoLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
      getObeoLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
      getExcelLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
      getExcelLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
    }
  }

}
