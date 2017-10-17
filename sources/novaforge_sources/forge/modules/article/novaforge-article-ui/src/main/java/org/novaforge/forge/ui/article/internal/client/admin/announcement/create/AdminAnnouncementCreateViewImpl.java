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
package org.novaforge.forge.ui.article.internal.client.admin.announcement.create;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.client.admin.article.component.ArticleFieldFactory;
import org.novaforge.forge.ui.article.internal.client.component.ArticleContentItemProperty;
import org.novaforge.forge.ui.article.internal.client.component.ArticleItemProperty;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminAnnouncementCreateViewImpl extends VerticalLayout implements AdminAnnouncementCreateView
{
  /**
   * Default serial UID
   */
  private static final long      serialVersionUID = 9012102598605551477L;
  /**
   * The back to list button
   */
  private final Button           backToListButton = new Button();
  /**
   * The title label
   */
  private final Label            title            = new Label();
  /**
   * The form layout
   */
  private final FormLayout       formLayout       = new FormLayout();
  /**
   * The form binders list
   */
  private final List<FieldGroup> formBinders      = new ArrayList<FieldGroup>();
  /**
   * The Accordion content element
   */
  private final Accordion        contentAccordion = new Accordion();
  /**
   * The create announcement button
   */
  private final Button           createButton     = new Button();
  /**
   * The categories combobox field
   */
  private final ComboBox         categoryField    = new ComboBox();
  /**
   * The working announcement item
   */
  private Item announcementItem;
  /**
   * Define the mode of the view
   */
  private boolean isEditMode = false;

  /**
   * Default constructor
   */
  public AdminAnnouncementCreateViewImpl()
  {
    addComponent(initHeader());
    addComponent(initCreateForm());
    setSpacing(true);
    setMargin(true);
  }

  /**
   * Initialize the header layout
   *
   * @return the header layout componenet
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
   * Initialize the create form layout
   *
   * @return the form layout component
   */
  private Component initCreateForm()
  {
    createButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    return formLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindToArticle(final Article pArticle)
  {
    // Clear current binder and form
    formBinders.clear();
    formLayout.removeAllComponents();
    contentAccordion.removeAllComponents();
    // Create and bind Article Item
    announcementItem = new BeanItem<Article>(pArticle);
    FieldGroup articleFormBinder = new FieldGroup(announcementItem);
    articleFormBinder.setFieldFactory(new ArticleFieldFactory());
    DateField dateField = articleFormBinder.buildAndBind(ArticleModule.getPortalMessages().getMessage(UI.getCurrent()
                                                                                                        .getLocale(),
                                                                                                      Messages.ARTICLEMANAGEMENT_FIELD_DATE),
                                                         ArticleItemProperty.DATE.getPropertyId(), DateField.class);
    if (pArticle.getDate() == null)
    {
      dateField.setValue(new Date());
    }
    formLayout.addComponent(dateField);
    formLayout.addComponent(articleFormBinder.buildAndBind(ArticleModule.getPortalMessages().getMessage(UI.getCurrent()
                                                                                                          .getLocale(),
                                                                                                        Messages.ARTICLEMANAGEMENT_FIELD_PUBLISH),
                                                           ArticleItemProperty.PUBLISHED.getPropertyId(),
                                                           CheckBox.class));
    categoryField.setValue(pArticle.getArticleCategory());
    categoryField.setFilteringMode(FilteringMode.CONTAINS);
    formLayout.addComponent(categoryField);
    // Add binder to form binder list
    formBinders.add(articleFormBinder);
    // Create and bind article content item
    for (ArticleContent content : pArticle.getArticleContents())
    {
      Item articleContentItem = new BeanItem<ArticleContent>(content);
      FieldGroup articleContentFormBinder = new FieldGroup(articleContentItem);
      FormLayout formContentLayout = new FormLayout();

      TextField contentTitleField = articleContentFormBinder.buildAndBind(ArticleModule.getPortalMessages()
                                                                                       .getMessage(UI.getCurrent()
                                                                                                     .getLocale(),
                                                                                                   Messages.ARTICLEMANAGEMENT_FIELD_TITLE),
                                                                          ArticleContentItemProperty.TITLE
                                                                              .getPropertyId(), TextField.class);
      contentTitleField.setNullRepresentation(ArticleModule.NULL_REPRESENTATION);
      contentTitleField.setWidth(95, Unit.PERCENTAGE);
      formContentLayout.addComponent(contentTitleField);
      RichTextArea contentShortTextField = articleContentFormBinder.buildAndBind(ArticleModule.getPortalMessages()
                                                                                              .getMessage(UI.getCurrent()
                                                                                                            .getLocale(),
                                                                                                          Messages.ARTICLEMANAGEMENT_FIELD_SHORTTEXT),
                                                                                 ArticleContentItemProperty.SHORTTEXT
                                                                                     .getPropertyId(),
                                                                                 RichTextArea.class);
      contentShortTextField.setNullRepresentation(ArticleModule.NULL_REPRESENTATION);
      contentShortTextField.setWidth(95, Unit.PERCENTAGE);
      formContentLayout.addComponent(contentShortTextField);
      RichTextArea contentTextField = articleContentFormBinder.buildAndBind(ArticleModule.getPortalMessages()
          .getMessage(UI.getCurrent().getLocale(), Messages.ARTICLEMANAGEMENT_FIELD_TEXT),
          ArticleContentItemProperty.TEXT.getPropertyId(), RichTextArea.class);
      contentTextField.setNullRepresentation(ArticleModule.NULL_REPRESENTATION);
      contentTextField.setWidth(95, Unit.PERCENTAGE);
      formContentLayout.addComponent(contentTextField);

      formBinders.add(articleContentFormBinder);
      Tab addedTab = contentAccordion.addTab(formContentLayout, content.getLanguage().getLocale()
          .getDisplayName(UI.getCurrent().getLocale()));

      if (content.getLanguage().IsDefault())
      {
        contentTitleField.setRequired(true);
        contentTitleField.setRequiredError(ArticleModule.getPortalMessages().getMessage(
            UI.getCurrent().getLocale(), Messages.ARTICLEMANAGEMENT_FIELD_TITLE_REQUIRED));
        contentShortTextField.setRequired(true);
        contentShortTextField.setRequiredError(ArticleModule.getPortalMessages().getMessage(
            UI.getCurrent().getLocale(), Messages.ARTICLEMANAGEMENT_FIELD_SHORTTEXT_REQUIRED));
        contentTextField.setRequired(true);
        contentTextField.setRequiredError(ArticleModule.getPortalMessages().getMessage(
            UI.getCurrent().getLocale(), Messages.ARTICLEMANAGEMENT_FIELD_TEXT_REQUIRED));
        addedTab.setStyleName(NovaForge.ARTICLE_ACCORDION_REQUIRED);
      }
    }
    formLayout.addComponent(contentAccordion);
    formLayout.addComponent(createButton);
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
  public Label getTitle()
  {
    return title;
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
  public FormLayout getFormLayout()
  {
    return formLayout;
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
  public void refreshLocale(final Locale pLocale)
  {
    backToListButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.ARTICLEMANAGEMENT_CREATE_BACKTOLIST));
    if (isEditMode)
    {
      createButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.ARTICLEMANAGEMENT_UPDATE_ANNOUNCEMENT));
      createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    }
    else
    {
      createButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.ARTICLEMANAGEMENT_CREATE_ANNOUNCEMENT));
      createButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    }
    contentAccordion.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.ARTICLEMANAGEMENT_FIELD_CONTENT));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getCategoryField()
  {
    return categoryField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUpdateMode(final boolean pIsUpdateMode)
  {
    isEditMode = pIsUpdateMode;
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
}
