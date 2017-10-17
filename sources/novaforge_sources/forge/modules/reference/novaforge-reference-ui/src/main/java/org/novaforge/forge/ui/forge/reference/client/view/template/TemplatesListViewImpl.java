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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.novaforge.forge.ui.commons.client.celllist.ListResources;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

/**
 * @author lamirang
 */
public class TemplatesListViewImpl extends Composite implements TemplatesListView
{
  private static TemplatesListViewImplUiBinder uiBinder   = GWT.create(TemplatesListViewImplUiBinder.class);
  private static ReferenceResources            ressources = GWT.create(ReferenceResources.class);
  private final TemplateMessage messages = (TemplateMessage) GWT.create(TemplateMessage.class);
  @UiField
  Button                       buttonPreloadTemplate;
  @UiField
  Button                       buttonAddTemplate;
  @UiField(provided = true)
  Image                        reloadImage;
  @UiField
  Label                        templatesTitle;
  @UiField(provided = true)
  CellList<String>             templatesList;
  ListDataProvider<String>     provider;
  SingleSelectionModel<String> selectionModel;
  @UiField
  Panel                        rightPanel;

  public TemplatesListViewImpl()
  {
    ressources.style().ensureInjected();

    // Initialization of groups list
    templatesList = new CellList<String>(new TextCell(), (Resources) GWT.create(ListResources.class));
    final Label emptyLabel = new Label(messages.emptyMessage());
    emptyLabel.setStyleName(ressources.style().emptyLabel());
    templatesList.setEmptyListWidget(emptyLabel);

    reloadImage = new Image(ressources.refresh());
    // Generate ui
    initWidget(uiBinder.createAndBindUi(this));

    // Set data provider used to display data into celllist
    provider = new ListDataProvider<String>();
    provider.addDataDisplay(templatesList);

    templatesList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    templatesList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

    // Add a selection model so we can select cells.
    selectionModel = new SingleSelectionModel<String>();
    templatesList.setSelectionModel(selectionModel);

    // Set title of group label
    templatesTitle.setText(messages.listTitle());

    buttonAddTemplate.setText(messages.buttonAdd());
    buttonPreloadTemplate.setText(messages.buttonPreload());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SingleSelectionModel<String> getListSelectionModel()
  {
    return selectionModel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CellList<String> getCellList()
  {
    return templatesList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ListDataProvider<String> getListDataProvider()
  {
    return provider;
  }

  @Override
  public Panel getContentPanel()
  {
    return rightPanel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAddButton()
  {
    return buttonAddTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getReloadImage()
  {
    return reloadImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getPreloadButton()
  {
    return buttonPreloadTemplate;
  }

  interface TemplatesListViewImplUiBinder extends UiBinder<Widget, TemplatesListViewImpl>
  {
  }
}
