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
package org.novaforge.forge.ui.dashboard.internal.client.tab.header;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.component.LayoutsHeaderComponent;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.client.component.UploadFieldCustom;
import org.novaforge.forge.ui.portal.client.component.UploadImageComponent;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;
import org.vaadin.addon.itemlayout.horizontal.ItemHorizontal;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public class HeaderViewImpl extends HorizontalLayout implements HeaderView
{

  /**
   * Serial version id
   */
  private static final long      serialVersionUID           = 4735805683085770389L;
  private static final int SUBWINDOW_SIZE             = 400;
  private static final int DETAILS_WINDOW_SIZE        = 600;
  private static final int WIDGET_DETAIL_ICON_SIZE    = 24;
  private static final int WIDGET_DETAIL_PREVIEW_SIZE = 150;
  private final Component        addWidgetLayoutComponent;
  private final HorizontalLayout headerGeneralLayout;
  private TextField              tabName;
  private Label                  imageLabel;
  private UploadImageComponent   imageComponent;
  private Form                   tabForm;
  private Form                   layoutForm;
  private Image                  deleteButton;
  private Image                  addButton;
  private Label                  addLabel;
  private Label                  deleteLabel;
  private DeleteConfirmWindow    tabDeleteWindow;
  private Window                 layoutSelectWindow;
  private Label                  layoutSelectConfirmLabel;
  private Button                 layoutSelectConfirmButton;
  private Button                 layoutSelectCancelButton;
  private Image                  addWidgetBackButton;
  private TextField              addWidgetFilterTextField;
  private ComboBox               addWidgetFilterCategoryComboBox;
  private LayoutsHeaderComponent layoutHeader;
  private Label                  addWidgetBackLabel;
  private VerticalLayout         backButtonLayout;
  private VerticalLayout         addWidgetButtonLayout;
  private VerticalLayout         deleteButtonLayout;
  private ItemHorizontal         widgetList;
  private Label                  tabNameLabel;
  private Form                   addWidgetFilterForm;
  private Window                 widgetDetailsWindow;
  private Image                  widgetDetailsIcon;
  private Label                  widgetDetailsName;
  private Image                  widgetDetailsPreviewImage;
  private Label                  widgetDetailsDescriptionLabel;
  private Label                  widgetDetailsCategoryTitleLabel;
  private Button                 widgetDetailsCloseButton;
  private Button                 widgetDetailsAddWidgetButton;
  private HorizontalLayout       widgetDetailsCategoriesLayout;

  /**
   * Default constructor
   * 
   * @param pContent
   *          the tab content
   */
  public HeaderViewImpl()
  {
    // Initialize components
    setSizeFull();

    headerGeneralLayout = new HorizontalLayout();

    // Initialize components
    final Component info = initForm();
    final Component layout = initLayout();
    final Component deleteDashboardButton = initDelete();
    final Component addWidgetButton = initAddWidget();
    addWidgetLayoutComponent = initAddWidgetLayout();

    // Add components
    headerGeneralLayout.addComponent(info);

    final Component firstSeparator = createSeparator();
    headerGeneralLayout.addComponent(firstSeparator);

    headerGeneralLayout.addComponent(layout);
    headerGeneralLayout.setExpandRatio(layout, 1f);

    final Component secondSeparator = createSeparator();
    headerGeneralLayout.addComponent(secondSeparator);

    headerGeneralLayout.addComponent(deleteDashboardButton);
    headerGeneralLayout.setComponentAlignment(deleteDashboardButton, Alignment.MIDDLE_CENTER);

    final Component thridSeparator = createSeparator();
    headerGeneralLayout.addComponent(thridSeparator);

    headerGeneralLayout.addComponent(addWidgetButton);
    headerGeneralLayout.setComponentAlignment(addWidgetButton, Alignment.MIDDLE_CENTER);

    // Init layout
    headerGeneralLayout.setWidth(100, Unit.PERCENTAGE);
    headerGeneralLayout.setHeight(100, Unit.PERCENTAGE);

    addWidgetLayoutComponent.setVisible(false);
    addWidgetLayoutComponent.setWidth(100, Unit.PERCENTAGE);
    addWidgetLayoutComponent.setHeight(100, Unit.PERCENTAGE);

    setWidth(100, Unit.PERCENTAGE);
    setHeight(135, Unit.PIXELS);

    addComponent(headerGeneralLayout);
    setComponentAlignment(headerGeneralLayout, Alignment.MIDDLE_LEFT);
    setExpandRatio(headerGeneralLayout, 1f);
    addComponent(addWidgetLayoutComponent);
    setComponentAlignment(addWidgetLayoutComponent, Alignment.MIDDLE_RIGHT);
    setExpandRatio(addWidgetLayoutComponent, 0f);

    // Init sub window
    initSelectLayoutWindow();
    initConfirmDeleteWindow();
    initWidgetDetailsWindow();
  }

  /**
   * Init the form which allows to edit tab name and icon
   */
  private Component initForm()
  {
    final VerticalLayout layout = new VerticalLayout();
    layout.setSizeUndefined();
    layout.setSpacing(true);
    tabForm = new Form();
    tabForm.setFooter(null);

    // Name field
    tabNameLabel = new Label();
    tabName = new TextField();
    tabName.setImmediate(true);
    // Image fields
    imageLabel = new Label();
    imageComponent = new UploadImageComponent(30, UploadImageComponent.MAX_SIZE);
    imageComponent.setSizeUndefined();

    // Fields grid
    final GridLayout fieldsGrid = new GridLayout(2, 2);
    fieldsGrid.setSpacing(true);
    fieldsGrid.addComponent(tabNameLabel, 0, 0);
    fieldsGrid.addComponent(tabName, 1, 0);
    fieldsGrid.addComponent(imageLabel, 0, 1);
    fieldsGrid.addComponent(imageComponent, 1, 1);
    fieldsGrid.setComponentAlignment(tabNameLabel, Alignment.MIDDLE_LEFT);
    fieldsGrid.setComponentAlignment(tabName, Alignment.MIDDLE_LEFT);
    fieldsGrid.setComponentAlignment(imageLabel, Alignment.MIDDLE_LEFT);
    fieldsGrid.setComponentAlignment(imageComponent, Alignment.MIDDLE_LEFT);

    layout.addComponent(tabForm);
    layout.setComponentAlignment(tabForm, Alignment.MIDDLE_CENTER);
    layout.addComponent(fieldsGrid);
    layout.setComponentAlignment(fieldsGrid, Alignment.MIDDLE_CENTER);
    return layout;

  }

  /**
   * Init the form which allows to choose tab layout
   */
  private Component initLayout()
  {
    final VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.setSpacing(true);
    layoutForm = new Form();
    layoutForm.setFooter(null);
    layoutHeader = new LayoutsHeaderComponent();
    layoutForm.setLayout(layoutHeader);
    mainLayout.addComponent(layoutForm);
    mainLayout.setComponentAlignment(layoutForm, Alignment.MIDDLE_CENTER);
    return mainLayout;
  }

  /**
   * Init the image which allows to delete tab
   */
  private Component initDelete()
  {
    deleteButtonLayout = new VerticalLayout();
    deleteButtonLayout.setSizeUndefined();
    deleteButtonLayout.setSpacing(true);
    deleteButton = new Image();
    deleteButton.setSource(new ThemeResource(NovaForgeResources.ICON_TRASH_RED));
    deleteLabel = new Label();
    deleteLabel.addStyleName(NovaForge.LABEL_BOLD);
    deleteButtonLayout.addStyleName(NovaForge.CURSOR_BUTTON);
    deleteButton.addStyleName(NovaForge.CURSOR_BUTTON);
    deleteLabel.addStyleName(NovaForge.CURSOR_BUTTON);
    deleteButtonLayout.addComponent(deleteButton);
    deleteButtonLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);
    deleteButtonLayout.addComponent(deleteLabel);
    deleteButtonLayout.setComponentAlignment(deleteLabel, Alignment.MIDDLE_CENTER);
    return deleteButtonLayout;
  }

  /**
   * @return
   */
  private Component initAddWidget()
  {
    addWidgetButtonLayout = new VerticalLayout();
    addWidgetButtonLayout.setSizeUndefined();
    addWidgetButtonLayout.setSpacing(true);
    addWidgetButtonLayout.setStyleName(NovaForge.CURSOR_BUTTON);
    addButton = new Image();
    addButton.setSource(new ThemeResource(NovaForgeResources.ICON_ARROW_RIGHT3));
    addButton.addStyleName(NovaForge.CURSOR_BUTTON);
    addLabel = new Label();
    addLabel.addStyleName(NovaForge.CURSOR_BUTTON);
    addLabel.addStyleName(NovaForge.LABEL_BOLD);
    addWidgetButtonLayout.addComponent(addButton);
    addWidgetButtonLayout.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
    addWidgetButtonLayout.addComponent(addLabel);
    addWidgetButtonLayout.setComponentAlignment(addLabel, Alignment.MIDDLE_CENTER);
    return addWidgetButtonLayout;
  }

  /**
   * Initialize the window displayed when adding a widget
   */
  private Component initAddWidgetLayout()
  {
    // Create components
    final HorizontalLayout addWidgetLayout = new HorizontalLayout();
    final Component backButton = initAddWidgetBackButton();
    final Component filterForm = initAddWidgetFilterForm();
    final Component widgetList = initAddWidgetList();

    // Create separators
    final Component firstSeparator = createSeparator();

    addWidgetLayout.addComponent(backButton);
    addWidgetLayout.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);
    addWidgetLayout.addComponent(firstSeparator);
    addWidgetLayout.addComponent(filterForm);
    addWidgetLayout.addComponent(widgetList);
    addWidgetLayout.setComponentAlignment(widgetList, Alignment.MIDDLE_CENTER);
    addWidgetLayout.setExpandRatio(widgetList, 1f);
    return addWidgetLayout;
  }

  /**
   * Create separator Layout
   */
  private Component createSeparator()
  {
    final VerticalLayout separator = new VerticalLayout();
    separator.setWidth(2, Unit.PIXELS);
    separator.setHeight(100, Unit.PERCENTAGE);
    separator.setStyleName(Reindeer.LAYOUT_BLACK);
    separator.addStyleName(NovaForge.DASHBOARD_SEPARATOR);
    return separator;
  }

  /**
   * Initialize the window displayed when selecting a lower boxes number
   */
  private void initSelectLayoutWindow()
  {
    layoutSelectWindow = new Window();
    layoutSelectWindow.setModal(true);
    layoutSelectWindow.setResizable(false);
    layoutSelectWindow.setWidth(SUBWINDOW_SIZE, Unit.PIXELS);

    layoutSelectConfirmLabel = new Label();
    layoutSelectConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(100, Unit.PERCENTAGE);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(new MarginInfo(true, false, false, false));
    layoutSelectConfirmButton = new Button();
    layoutSelectCancelButton = new Button();
    layoutSelectCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    layoutSelectConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE));
    layoutSelectConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(layoutSelectCancelButton);
    buttons.addComponent(layoutSelectConfirmButton);
    buttons.setComponentAlignment(layoutSelectCancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(layoutSelectConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(layoutSelectConfirmLabel);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(layoutSelectConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    layoutSelectWindow.setContent(windowLayout);
  }

  /**
   * Initialize the window displayed when deleting a tab
   */
  private void initConfirmDeleteWindow()
  {
    tabDeleteWindow = new DeleteConfirmWindow();
  }

  private void initWidgetDetailsWindow()
  {
    widgetDetailsWindow = new Window();
    widgetDetailsWindow.setModal(true);
    widgetDetailsWindow.setResizable(false);
    widgetDetailsWindow.setWidth(DETAILS_WINDOW_SIZE, Unit.PIXELS);
    widgetDetailsWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_INFORMATION));
    final VerticalLayout widgetDetailsLayout = new VerticalLayout();
    widgetDetailsLayout.setSpacing(true);
    widgetDetailsLayout.setMargin(true);
    // Title layout
    final HorizontalLayout titleLayout = new HorizontalLayout();
    titleLayout.setSpacing(true);
    widgetDetailsIcon = new Image();
    widgetDetailsIcon.setWidth(WIDGET_DETAIL_ICON_SIZE, Unit.PIXELS);
    widgetDetailsIcon.setHeight(WIDGET_DETAIL_ICON_SIZE, Unit.PIXELS);
    widgetDetailsName = new Label();
    widgetDetailsName.setStyleName(Reindeer.LABEL_H1);
    titleLayout.addComponent(widgetDetailsIcon);
    titleLayout.addComponent(widgetDetailsName);
    titleLayout.setComponentAlignment(widgetDetailsIcon, Alignment.MIDDLE_LEFT);
    titleLayout.setComponentAlignment(widgetDetailsName, Alignment.MIDDLE_LEFT);
    widgetDetailsLayout.addComponent(titleLayout);
    // Thumbnail and description layout
    final HorizontalLayout descriptionLayout = new HorizontalLayout();
    descriptionLayout.setSpacing(true);
    widgetDetailsPreviewImage = new Image();
    widgetDetailsPreviewImage.setWidth(WIDGET_DETAIL_PREVIEW_SIZE, Unit.PIXELS);
    widgetDetailsPreviewImage.setHeight(WIDGET_DETAIL_PREVIEW_SIZE, Unit.PIXELS);
    widgetDetailsDescriptionLabel = new Label();
    widgetDetailsDescriptionLabel.setWidth(DETAILS_WINDOW_SIZE - (WIDGET_DETAIL_PREVIEW_SIZE + 30), Unit.PIXELS);
    descriptionLayout.addComponent(widgetDetailsPreviewImage);
    descriptionLayout.addComponent(widgetDetailsDescriptionLabel);
    widgetDetailsLayout.addComponent(descriptionLayout);
    // Categories Layout
    final HorizontalLayout categoriesLayout = new HorizontalLayout();
    categoriesLayout.setMargin(new MarginInfo(true, false, false, false));
    categoriesLayout.setSpacing(true);
    widgetDetailsCategoryTitleLabel = new Label();
    widgetDetailsCategoryTitleLabel.setStyleName(NovaForge.LABEL_BOLD);
    categoriesLayout.addComponent(widgetDetailsCategoryTitleLabel);
    widgetDetailsCategoriesLayout = new HorizontalLayout();
    widgetDetailsCategoriesLayout.setSpacing(false);
    categoriesLayout.addComponent(widgetDetailsCategoriesLayout);
    widgetDetailsLayout.addComponent(categoriesLayout);
    // Buttons Layout
    final HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setWidth(100, Unit.PERCENTAGE);
    widgetDetailsCloseButton = new Button();
    widgetDetailsCloseButton.setStyleName(NovaForge.BUTTON_LINK);
    widgetDetailsAddWidgetButton = new Button();
    widgetDetailsAddWidgetButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    widgetDetailsAddWidgetButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    buttonsLayout.addComponent(widgetDetailsCloseButton);
    buttonsLayout.addComponent(widgetDetailsAddWidgetButton);
    buttonsLayout.setComponentAlignment(widgetDetailsCloseButton, Alignment.BOTTOM_LEFT);
    buttonsLayout.setComponentAlignment(widgetDetailsAddWidgetButton, Alignment.BOTTOM_RIGHT);
    widgetDetailsLayout.addComponent(buttonsLayout);

    widgetDetailsWindow.setContent(widgetDetailsLayout);
  }

  private Component initAddWidgetBackButton()
  {
    backButtonLayout = new VerticalLayout();
    backButtonLayout.setSizeUndefined();
    backButtonLayout.setSpacing(true);
    backButtonLayout.addStyleName(NovaForge.CURSOR_BUTTON);
    addWidgetBackButton = new Image();
    addWidgetBackButton.setSource(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT3));
    addWidgetBackButton.addStyleName(NovaForge.CURSOR_BUTTON);
    addWidgetBackLabel = new Label();
    addWidgetBackLabel.addStyleName(NovaForge.CURSOR_BUTTON);
    addWidgetBackLabel.addStyleName(NovaForge.LABEL_BOLD);
    backButtonLayout.addComponent(addWidgetBackButton);
    backButtonLayout.setComponentAlignment(addWidgetBackButton, Alignment.MIDDLE_CENTER);
    backButtonLayout.addComponent(addWidgetBackLabel);
    backButtonLayout.setComponentAlignment(addWidgetBackLabel, Alignment.MIDDLE_CENTER);
    return backButtonLayout;
  }

  private Component initAddWidgetFilterForm()
  {
    addWidgetFilterForm = new Form();
    addWidgetFilterForm.setFooter(null);
    final VerticalLayout filterLayout = new VerticalLayout();
    filterLayout.setMargin(new MarginInfo(false, true, false, false));
    filterLayout.setSpacing(true);
    addWidgetFilterTextField = new TextField();
    final ActionButtonTextField searchButtonTextField = ActionButtonTextField.extend(addWidgetFilterTextField);
    searchButtonTextField.getState().type = ActionButtonType.ACTION_SEARCH;
    addWidgetFilterTextField.setImmediate(true);
    addWidgetFilterCategoryComboBox = new ComboBox();
    addWidgetFilterCategoryComboBox.setNewItemsAllowed(false);
    addWidgetFilterCategoryComboBox.setNullSelectionAllowed(false);
    addWidgetFilterCategoryComboBox.setImmediate(true);

    filterLayout.addComponent(addWidgetFilterForm);
    filterLayout.addComponent(addWidgetFilterTextField);
    filterLayout.addComponent(addWidgetFilterCategoryComboBox);
    filterLayout.setComponentAlignment(addWidgetFilterTextField, Alignment.MIDDLE_LEFT);
    filterLayout.setComponentAlignment(addWidgetFilterCategoryComboBox, Alignment.MIDDLE_LEFT);
    filterLayout.setSizeUndefined();
    return filterLayout;
  }

  private Component initAddWidgetList()
  {
    widgetList = new ItemHorizontal();
    widgetList.setImmediate(true);
    widgetList.setSelectable(false);
    return widgetList;
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
    // Main form
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    tabForm.setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_TITLE));
    tabNameLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_NAME));
    if (tabName != null)
    {
      tabName.removeAllValidators();
      tabName.setRequiredError(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_NAME_REQUIRED));
      tabName.addValidator(new StringLengthValidator(portalMessages.getMessage(pLocale,
          Messages.DASHBOARD_TAB_NAME_REQUIRED), 1, 25, false));
    }
    imageLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_ICON_TITLE));
    // Update image component locale
    imageComponent.refreshLocale(portalMessages, pLocale);
    // Layout form
    layoutForm.setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_LAYOUT_TITLE));
    getLayoutHeader().refreshLocale(pLocale);
    // Add button
    addLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_WIDGET_ADD));
    addLabel
        .setDescription(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_WIDGET_ADD_DESCRIPTION));
    addButton.setDescription(portalMessages
        .getMessage(pLocale, Messages.DASHBOARD_TAB_WIDGET_ADD_DESCRIPTION));

    // Close button
    deleteLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_DELETE));
    deleteLabel.setDescription(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_DELETE_DESCRIPTION));
    deleteButton
        .setDescription(portalMessages.getMessage(pLocale, Messages.DASHBOARD_TAB_DELETE_DESCRIPTION));

    // Select window
    layoutSelectWindow.setCaption(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_TAB_LAYOUT_SELECT_TITLE));
    layoutSelectConfirmLabel.setValue(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_TAB_LAYOUT_SELECT_CONFIRMLABEL));
    layoutSelectCancelButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));
    layoutSelectConfirmButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_TAB_LAYOUT_SELECT_CONFIRM));

    // Delete window
    tabDeleteWindow.setWindowMessageTextId(Messages.DASHBOARD_TAB_DELETE_CONFIRMLABEL);

    // Add widget window
    addWidgetFilterForm.setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_ADDWIDGET_FILTER));
    addWidgetFilterTextField.setInputPrompt(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_ADDWIDGET_SEARCH_INPUTPROMPT));
    addWidgetBackLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_ADDWIDGET_FINISH));
    // Widget details
    widgetDetailsWindow
        .setCaption(portalMessages.getMessage(pLocale, Messages.DASHBOARD_WIDGETDETAILS_TITLE));
    widgetDetailsCategoryTitleLabel.setValue(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_WIDGETDETAILS_CATEGORIES));
    widgetDetailsCloseButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CLOSE));
    widgetDetailsAddWidgetButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_ADDWIDGET_BUTTON));
    widgetDetailsAddWidgetButton.setDescription(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_ADDWIDGET_BUTTON_DESCRIPTION));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getTabName()
  {
    return tabName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getAddButton()
  {
    return addButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UploadFieldCustom getImageUpload()
  {
    return imageComponent.getUploadImageField();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getTabIcon()
  {
    return imageComponent.getImage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUpdatePictureWindow()
  {
    return imageComponent.getUpdateImageWindow();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getIconDeleteButton()
  {
    return imageComponent.getDeleteImageButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getIconConfirmButton()
  {
    return imageComponent.getUpdateImageConfirmButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getTabDeleteWindow()
  {
    return tabDeleteWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getLayoutSelectWindow()
  {
    return layoutSelectWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getLayoutSelectCancelButton()
  {
    return layoutSelectCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getLayoutSelectConfirmButton()
  {
    return layoutSelectConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LayoutsHeaderComponent getLayoutHeader()
  {
    return layoutHeader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getAddWidgetFilterCategoryComboBox()
  {
    return addWidgetFilterCategoryComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getAddWidgetFilterTextField()
  {
    return addWidgetFilterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getAddWidgetBackLabel()
  {
    return addWidgetBackLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getAddWidgetLayoutComponent()
  {
    return addWidgetLayoutComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getHeaderGeneralLayout()
  {
    return headerGeneralLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showHeaderGeneralLayout()
  {
    addWidgetLayoutComponent.setVisible(false);
    setExpandRatio(addWidgetLayoutComponent, 0f);
    setExpandRatio(headerGeneralLayout, 1f);
    headerGeneralLayout.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAddWidgetLayout()
  {
    headerGeneralLayout.setVisible(false);
    setExpandRatio(headerGeneralLayout, 0f);
    setExpandRatio(addWidgetLayoutComponent, 1f);
    addWidgetLayoutComponent.setVisible(true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getBackButtonLayout()
  {
    return backButtonLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getAddWidgetButtonLayout()
  {
    return addWidgetButtonLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getDeleteButtonLayout()
  {
    return deleteButtonLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemHorizontal getWidgetList()
  {
    return widgetList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getWidgetDetailsCategoriesLayout()
  {
    return widgetDetailsCategoriesLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsCategoriesLayout(final HorizontalLayout widgetDetailsCategoriesLayout)
  {
    this.widgetDetailsCategoriesLayout = widgetDetailsCategoriesLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getWidgetDetailsAddWidgetButton()
  {
    return widgetDetailsAddWidgetButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsAddWidgetButton(final Button widgetDetailsAddWidgetButton)
  {
    this.widgetDetailsAddWidgetButton = widgetDetailsAddWidgetButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getWidgetDetailsCloseButton()
  {
    return widgetDetailsCloseButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsCloseButton(final Button widgetDetailsCloseButton)
  {
    this.widgetDetailsCloseButton = widgetDetailsCloseButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getWidgetDetailsDescriptionLabel()
  {
    return widgetDetailsDescriptionLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsDescriptionLabel(final Label widgetDetailsDescriptionLabel)
  {
    this.widgetDetailsDescriptionLabel = widgetDetailsDescriptionLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getWidgetDetailsThumbImage()
  {
    return widgetDetailsPreviewImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsThumbImage(final Image widgetDetailsThumbImage)
  {
    widgetDetailsPreviewImage = widgetDetailsThumbImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getWidgetDetailsName()
  {
    return widgetDetailsName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsName(final Label widgetDetailsName)
  {
    this.widgetDetailsName = widgetDetailsName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getWidgetDetailsIcon()
  {
    return widgetDetailsIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsIcon(final Image widgetDetailsIcon)
  {
    this.widgetDetailsIcon = widgetDetailsIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getWidgetDetailsWindow()
  {
    return widgetDetailsWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgetDetailsWindow(final Window widgetDetailsWindow)
  {
    this.widgetDetailsWindow = widgetDetailsWindow;
  }

}
