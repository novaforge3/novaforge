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
package org.novaforge.forge.ui.article.internal.client.admin.information;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.article.model.Article;
import org.novaforge.forge.article.model.ArticleContent;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.article.internal.client.component.ArticleContentItemProperty;
import org.novaforge.forge.ui.article.internal.module.ArticleModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class AdminInformationViewImpl extends VerticalLayout implements AdminInformationView
{

  /**
   * Default serial UID
   */
  private static final long      serialVersionUID = -4644112401153968730L;
  /**
   * FieldGroup List
   */
  private final List<FieldGroup> formBinders      = new ArrayList<FieldGroup>();
  /**
   * Content Accordion
   */
  private final Accordion        contentAccordion = new Accordion();
  /**
   * Form Layout
   */
  private final FormLayout       formLayout       = new FormLayout();
  /**
   * Save button
   */
  private final Button           saveButton       = new Button();

  /**
   * Default Constructor
   */
  public AdminInformationViewImpl()
  {
    initLayout();
  }

  /**
   * Initialize layout
   */
  private void initLayout()
  {
    saveButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    addComponent(formLayout);
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
    saveButton.setCaption(ArticleModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS_SAVE));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void bindToInformation(final Article pInformation)
  {
    formBinders.clear();
    formLayout.removeAllComponents();
    contentAccordion.removeAllComponents();
    // Create and bind article content item
    for (ArticleContent content : pInformation.getArticleContents())
    {
      Item articleContentItem = new BeanItem<ArticleContent>(content);
      FieldGroup articleContentFormBinder = new FieldGroup(articleContentItem);
      FormLayout formContentLayout = new FormLayout();

      TextField contentTitleField = articleContentFormBinder.buildAndBind(ArticleModule.getPortalMessages()
          .getMessage(UI.getCurrent().getLocale(), Messages.ARTICLEMANAGEMENT_FIELD_TITLE),
          ArticleContentItemProperty.TITLE.getPropertyId(), TextField.class);
      contentTitleField.setNullRepresentation(ArticleModule.NULL_REPRESENTATION);
      contentTitleField.setWidth(95, Unit.PERCENTAGE);
      formContentLayout.addComponent(contentTitleField);
      RichTextArea contentShortTextField = articleContentFormBinder.buildAndBind(
          ArticleModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
              Messages.ARTICLEMANAGEMENT_FIELD_TEXT), ArticleContentItemProperty.SHORTTEXT.getPropertyId(),
          RichTextArea.class);
      contentShortTextField.setNullRepresentation(ArticleModule.NULL_REPRESENTATION);
      contentShortTextField.setWidth(95, Unit.PERCENTAGE);
      contentShortTextField.setHeight(450, Unit.PIXELS);
      formContentLayout.addComponent(contentShortTextField);

      formBinders.add(articleContentFormBinder);
      Tab addedTab = contentAccordion.addTab(formContentLayout, content.getLanguage().getLocale()
          .getDisplayName(UI.getCurrent().getLocale()));
      if (content.getLanguage().IsDefault())
      {
        addedTab.setStyleName(NovaForge.ARTICLE_ACCORDION_REQUIRED);
      }
    }
    formLayout.addComponent(contentAccordion);
    formLayout.addComponent(saveButton);
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
  public Button getSaveButton()
  {
    return saveButton;
  }

}
