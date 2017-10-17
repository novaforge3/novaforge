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
package org.novaforge.forge.ui.article.internal.client.admin.category.create;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Button;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.article.model.ArticleCategory;
import org.novaforge.forge.article.model.ArticleCategoryContent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.client.component.ArticleContentItemProperty;
import org.novaforge.forge.ui.article.internal.client.component.Utils;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminCategoryCreateViewImpl extends VerticalLayout implements AdminCategoryCreateView
{

  /**
   * Default serial UID
   */
  private static final long      serialVersionUID = 5381221077172910575L;
  /**
   * The BackToList button
   */
  private final Button           backToListButton = new Button();
  /**
   * The FormLayout
   */
  private final FormLayout       formLayout       = new FormLayout();
  /**
   * The Name Label
   */
  private final Label            nameLabel        = new Label();
  /**
   * The create Button
   */
  private final Button           createButton     = new Button();
  /**
   * The ColorPicker field
   */
  private final ColorPicker      colorPicker      = new ColorPicker();
  /**
   * The form binders list
   */
  private final List<FieldGroup> formBinders      = new ArrayList<FieldGroup>();
  /**
   * Current working Category Item
   */
  private Item       categoryItem;
  /**
   * The ArticleCategory Binder
   */
  private FieldGroup categoryBinder;
  /**
   * Define edit or create mode
   */
  private boolean isEditMode = false;

  /**
   * Default constructor
   */
  public AdminCategoryCreateViewImpl()
  {
    addComponent(initHeader());
    addComponent(initCreateForm());
    setSpacing(true);
    setMargin(true);
  }

  /**
   * Initialize the header layout
   *
   * @return the header component
   */
  private Component initHeader()
  {
    HorizontalLayout headerLayout = new HorizontalLayout();
    backToListButton.setIcon(new ThemeResource(NovaForgeResources.ICON_GO_PREVIOUS));
    headerLayout.addComponent(backToListButton);
    headerLayout.setMargin(new MarginInfo(false, false, true, false));
    return headerLayout;
  }

  /**
   * Initialize the Create form
   *
   * @return the form component
   */
  private Component initCreateForm()
  {
    colorPicker.setSwatchesVisibility(true);
    colorPicker.setHistoryVisibility(false);
    colorPicker.setHSVVisibility(false);
    colorPicker.setRGBVisibility(false);
    colorPicker.setTextfieldVisibility(true);
    colorPicker.setDefaultCaptionEnabled(true);
    createButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    return formLayout;
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
    backToListButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.ARTICLEMANAGEMENT_CREATE_BACKTOLIST));
    if (isEditMode)
    {
      createButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.ARTICLEMANAGEMENT_UPDATE_CATEGORY));
      createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    }
    else
    {
      createButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.ARTICLEMANAGEMENT_CREATE_CATEGORY));
      createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    }
    colorPicker.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                        Messages.ARTICLEMANAGEMENT_FIELD_COLOR));
    nameLabel.setValue(ArticleModule.getPortalMessages().getMessage(pLocale, Messages.ARTICLEMANAGEMENT_FIELD_NAME));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getBackToListButton()
  {
    return backToListButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateButton()
  {
    return createButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FieldGroup getCategoryBinder()
  {
    return categoryBinder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FormLayout getFormLayout()
  {
    return formLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindToCategory(final ArticleCategory pCategory)
  {

    formLayout.removeAllComponents();
    // Create and bind Article Item
    categoryItem = new BeanItem<ArticleCategory>(pCategory);
    categoryBinder = new FieldGroup(categoryItem);

    nameLabel.setStyleName(NovaForge.LABEL_BOLD);
    formLayout.addComponent(nameLabel);

    for (ArticleCategoryContent content : pCategory.getArticleCategoryContents())
    {
      Item articleCategoryContentItem = new BeanItem<ArticleCategoryContent>(content);
      FieldGroup articleCategoryContentFormBinder = new FieldGroup(articleCategoryContentItem);
      FormLayout formContentLayout = new FormLayout();

      TextField contentTitleField = articleCategoryContentFormBinder.buildAndBind(ArticleModule.getPortalMessages()
                                                                                               .getMessage(UI.getCurrent()
                                                                                                             .getLocale(),
                                                                                                           Messages.ARTICLEMANAGEMENT_FIELD_TITLE),
                                                                                  ArticleContentItemProperty.TITLE
                                                                                      .getPropertyId(),
                                                                                  TextField.class);
      contentTitleField.setNullRepresentation(ArticleModule.NULL_REPRESENTATION);
      contentTitleField.setRequired(content.getLanguage().IsDefault());
      contentTitleField.setRequiredError(ArticleModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                                                      Messages.ARTICLEMANAGEMENT_FIELD_NAME_REQUIRED));
      formContentLayout.addComponent(contentTitleField);

      formBinders.add(articleCategoryContentFormBinder);
      contentTitleField.setCaption(content.getLanguage().getLocale().getDisplayName(getLocale()));
      formLayout.addComponent(formContentLayout);

    }
    if (pCategory.getColor() != null)
    {
      Color categoryColor = Utils.getColorFromRGB(pCategory.getColor());
      colorPicker.setColor(categoryColor);
    }
    formLayout.addComponent(colorPicker);
    formLayout.addComponent(createButton);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ColorPicker getColorPicker()
  {
    return colorPicker;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<FieldGroup> getFormBinders()
  {
    return formBinders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUpdateMode(final boolean pIsUpdateMode)
  {
    isEditMode = pIsUpdateMode;
  }

}
