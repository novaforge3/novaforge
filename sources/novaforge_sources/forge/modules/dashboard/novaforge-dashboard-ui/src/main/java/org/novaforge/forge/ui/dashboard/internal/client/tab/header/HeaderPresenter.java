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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.dashboard.xml.Layout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.dashboard.internal.client.AbstractDashBoardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.container.CategoryContainer;
import org.novaforge.forge.ui.dashboard.internal.client.container.CategoryItemProperty;
import org.novaforge.forge.ui.dashboard.internal.client.container.WidgetCategoryFilter;
import org.novaforge.forge.ui.dashboard.internal.client.container.WidgetComponentGenerator;
import org.novaforge.forge.ui.dashboard.internal.client.container.WidgetModuleContainer;
import org.novaforge.forge.ui.dashboard.internal.client.container.WidgetModuleItemProperty;
import org.novaforge.forge.ui.dashboard.internal.client.event.DeleteTabEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateTabContentEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateTabInfoEvent;
import org.novaforge.forge.ui.dashboard.internal.client.event.UpdateTabLayoutEvent;
import org.novaforge.forge.ui.dashboard.internal.client.tab.header.component.LayoutMapper;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import com.google.common.base.Strings;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

/**
 * @author Guillaume Lamirand
 */
public class HeaderPresenter extends AbstractDashBoardPresenter implements Serializable
{
  /**
   * Serial version id
   */
  private static final long     serialVersionUID  = -3067819403919493755L;
  private final HeaderView      view;
  private final UUID            currentTabUUID;
  /**
   * All category itemID
   */
  private final Object          allCategoryItemId = "allwidgetcategory";
  /**
   * None category itemID
   */
  private final Object          noCategoryItemId  = "nowidgetcategory";
  /**
   * Widgets string filter
   */
  protected SimpleStringFilter  widgetsFilter;
  private Tab                   currentTab;
  private Layout                currentLayout;
  /**
   * Used to store the {@link LayoutMapper} when adding the confirm window
   */
  private LayoutMapper          newSelectedLayout;
  /**
   * Widget container
   */
  private WidgetModuleContainer widgetModuleContainer;
  /**
   * Widget category container
   */
  private CategoryContainer     categoryContainer;
  private WidgetCategoryFilter  categoriesFilter;
  private String                currentWidgetKey;

  /**
   * Default constructor
   *
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pTabUUID
   *          the associated tab uuid
   * @param pView
   *          the view associated to this presenter
   */
  public HeaderPresenter(final PortalContext pPortalContext, final UUID pTabUUID, final HeaderView pView)
  {
    super(pPortalContext);
    // Init the view
    view = pView;
    currentTabUUID = pTabUUID;

    initAddWidgetList();

    addListeners();
  }

  private void initAddWidgetList()
  {
    // Widget List
    widgetModuleContainer = new WidgetModuleContainer(this);
    /*
     * Widget component generator
     */
    final WidgetComponentGenerator widgetComponentGenerator = new WidgetComponentGenerator(this);
    view.getWidgetList().setContainerDataSource(widgetModuleContainer);
    view.getWidgetList().setItemGenerator(widgetComponentGenerator);
    // Widget Filter
    categoryContainer = new CategoryContainer();
    view.getAddWidgetFilterCategoryComboBox().setContainerDataSource(categoryContainer);
    view.getAddWidgetFilterCategoryComboBox()
        .setItemCaptionPropertyId(CategoryItemProperty.LABEL.getPropertyId());
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getBackButtonLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -924170652943628038L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        if (MouseButton.LEFT.equals(event.getButton()))
        {
          showAddWidgetList(false);
        }
      }
    });
    view.getTabName().addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -528919065974444450L;

      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        if (view.getTabName().isValid())
        {
          currentTab.setName(view.getTabName().getValue());
          DashboardModule.getDashBoardService().updateTab(currentTab);
          getEventBus().publish(new UpdateTabInfoEvent(currentTab));
        }
      }
    });
    view.getIconConfirmButton().addClickListener(new ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 4972032507461947475L;

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        final byte[] value = (byte[]) view.getImageUpload().getValue();
        if ((value != null) && (value.length > 0))
        {
          if (currentTab.getImage() == null)
          {
            currentTab.setImage(DashboardModule.getDashBoardService().newIcon());
          }
          currentTab.getImage().setFile(value);
          currentTab.getImage().setMimeType(view.getImageUpload().getMimeType());
          currentTab.getImage().setName(currentTab.getName() + "-" + currentTab.getUUID().toString());
          view.getTabIcon().setSource(ResourceUtils.buildImageResource(value, UUID.randomUUID().toString()));
          DashboardModule.getDashBoardService().updateTab(currentTab);
          getEventBus().publish(new UpdateTabInfoEvent(currentTab));
          view.getImageUpload().discard();
          UI.getCurrent().removeWindow(view.getUpdatePictureWindow());
        }
        else
        {
          final Locale locale = getCurrentLocale();
          Notification.show(
              DashboardModule.getPortalMessages().getMessage(locale, Messages.COMPONENT_IMAGE_ERROR_NULL),
              Type.WARNING_MESSAGE);
        }

      }

    });
    view.getIconDeleteButton().addClickListener(new ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7861649120755681975L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        view.getTabIcon().setSource(new ThemeResource(NovaForgeResources.ICON_APN));
        currentTab.setImage(null);
        DashboardModule.getDashBoardService().updateTab(currentTab);
        getEventBus().publish(new UpdateTabInfoEvent(currentTab));
      }
    });
    view.getTabDeleteWindow().getYesButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 641183805053727835L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new DeleteTabEvent(currentTab));

      }
    });
    view.getLayoutSelectCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7909167066272773963L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        UI.getCurrent().removeWindow(view.getLayoutSelectWindow());

      }
    });
    view.getLayoutSelectConfirmButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7357557213907162455L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        UI.getCurrent().removeWindow(view.getLayoutSelectWindow());
        final String key = newSelectedLayout.getKeyGridMapping().keySet().iterator().next();
        updateLayout(newSelectedLayout.getBoxes(), key);
        newSelectedLayout = null;

      }
    });
    view.getDeleteButtonLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -7778630426076435381L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        if (MouseButton.LEFT.equals(event.getButton()))
        {
          UI.getCurrent().addWindow(view.getTabDeleteWindow());
        }
      }
    });
    view.getAddWidgetButtonLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 5016045014473983161L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {
        if (MouseButton.LEFT.equals(event.getButton()))
        {
          refreshWidgetList();
          showAddWidgetList(true);
        }
      }
    });
    view.getWidgetDetailsCloseButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -7208170382348350973L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        currentWidgetKey = null;
        UI.getCurrent().removeWindow(view.getWidgetDetailsWindow());
      }
    });
    view.getAddWidgetFilterTextField().addTextChangeListener(new TextChangeListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -3656821850941869636L;

      @Override
      public void textChange(final TextChangeEvent event)
      {
        final Filterable filterableWidget = widgetModuleContainer;
        if (widgetsFilter != null)
        {
          filterableWidget.removeContainerFilter(widgetsFilter);
          widgetsFilter = null;
        }
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          widgetsFilter = new SimpleStringFilter(WidgetModuleItemProperty.NAME.getPropertyId(),
              event.getText(), true, false);
          filterableWidget.addContainerFilter(widgetsFilter);
        }
      }
    });
    view.getAddWidgetFilterCategoryComboBox().addValueChangeListener(new ValueChangeListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 3362371022905633480L;

      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        final Filterable filterableWidget = widgetModuleContainer;
        String selectedItemId = (String) view.getAddWidgetFilterCategoryComboBox().getValue();

        if (categoriesFilter != null)
        {
          filterableWidget.removeContainerFilter(categoriesFilter);
          categoriesFilter = null;
        }
        if ((selectedItemId != null) && !selectedItemId.isEmpty()
            && !allCategoryItemId.equals(selectedItemId))
        {
          if (noCategoryItemId.equals(selectedItemId))
          {
            selectedItemId = null;
          }
          categoriesFilter = new WidgetCategoryFilter(selectedItemId);
          filterableWidget.addContainerFilter(categoriesFilter);
        }
      }
    });
    view.getWidgetDetailsAddWidgetButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 6536285269628969479L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getWidgetDetailsWindow());
        addWidgetToDashboard(currentWidgetKey);
      }
    });
  }

  /**
   * Shows or hides the addWidgetList in the header depending on the boolean given
   *
   * @param pShow
   *          true to show it, false to hide
   */
  private void showAddWidgetList(final boolean pShow)
  {
    if (pShow)
    {
      view.showAddWidgetLayout();
    }
    else
    {
      view.showHeaderGeneralLayout();
    }
  }

  private void updateLayout(final int pBoxes, final String pKey)
  {
    view.getLayoutHeader().setSelectLayout(pBoxes, pKey);
    currentTab.setLayoutKey(pKey);
    DashboardModule.getDashBoardService().updateTab(currentTab);
    currentLayout = DashboardModule.getLayoutService().getLayout(pKey);
    getEventBus().publish(new UpdateTabLayoutEvent(currentTab));

  }

  private void refreshWidgetList()
  {
    this.categoriesFilter = null;

    // Refresh Widget List
    final List<WidgetModule> widgetModules = DashboardModule.getWidgetModuleService().getModules();

    widgetModuleContainer.removeAllContainerFilters();

    widgetModuleContainer.setWidgetModules(widgetModules);

    // Refresh Widget Category List
    boolean hasWidgetWithoutCategory = false;
    final Set<String> widgetCategIds = new HashSet<String>();
    for (final WidgetModule widgetModule : widgetModules)
    {
      final List<String> categories = widgetModule.getCategories();
      if ((categories != null) && !categories.isEmpty())
      {
        widgetCategIds.addAll(categories);
      }
      else
      {
        hasWidgetWithoutCategory = true;
      }
    }
    categoryContainer.setCategories(getCategoryDefinitionByIds(widgetCategIds), getCurrentLocale());
    // Add all category and none to category container
    categoryContainer.addItemAt(0, allCategoryItemId);
    categoryContainer.getItem(allCategoryItemId).getItemProperty(CategoryItemProperty.LABEL.getPropertyId())
        .setValue(DashboardModule.getPortalMessages().getMessage(getCurrentLocale(),
            Messages.DASHBOARD_WIDGET_CATEGORY_ALL));
    if (hasWidgetWithoutCategory)
    {
      categoryContainer.addItem(noCategoryItemId);
      categoryContainer.getItem(noCategoryItemId).getItemProperty(CategoryItemProperty.LABEL.getPropertyId())
          .setValue(DashboardModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.DASHBOARD_WIDGET_CATEGORY_NONE));
    }
    // Set allcategory selected by default
    view.getAddWidgetFilterCategoryComboBox().setValue(allCategoryItemId);

    view.getWidgetList().markAsDirtyRecursive();
  }

  /**
   * @param pWidgetKey
   */
  public void addWidgetToDashboard(final String pWidgetKey)
  {
    if (!Strings.isNullOrEmpty(pWidgetKey))
    {
      final Widget widget = DashboardModule.getDashBoardService().newWidget(pWidgetKey);
      widget.setName(
          DashboardModule.getPortalMessages().getMessage(getCurrentLocale(), getI18NNameKey(pWidgetKey)));
      DashboardModule.getDashBoardService().addWidget(currentTabUUID, widget);
      getEventBus().publish(new UpdateTabContentEvent(currentTab));
    }
  }

  private List<CategoryDefinitionService> getCategoryDefinitionByIds(final Set<String> pCategoryIds)
  {
    final List<CategoryDefinitionService> categoryDefinitionServices = new ArrayList<CategoryDefinitionService>();
    for (final String categId : pCategoryIds)
    {
      categoryDefinitionServices.add(DashboardModule.getPluginsCategoryManager().getCategoryService(categId));
    }
    return categoryDefinitionServices;
  }

  /**
   * Get the I18N name for this widgetModule key
   *
   * @param pWidgetModuleKey
   *          the WidgetModule key
   * @return the I18N name associated
   */
  public String getI18NNameKey(final String pWidgetModuleKey)
  {
    return Messages.WIDGET_I18N_PREFIX + pWidgetModuleKey + Messages.WIDGET_I18N_NAME_SUFFIX;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshContent()
  {
    currentTab = DashboardModule.getDashBoardService().getTab(currentTabUUID);

    // Init form detail
    view.getTabName().setValue(currentTab.getName());
    Resource image = new ThemeResource(NovaForgeResources.ICON_APN);
    if (currentTab.getImage() != null)
    {
      image = ResourceUtils.buildImageResource(currentTab.getImage().getFile(), UUID.randomUUID().toString());
    }
    view.getTabIcon().setSource(image);

    // Init layout detail
    final List<Layout> layouts = DashboardModule.getLayoutService().getLayouts();
    view.getLayoutHeader().refresh(layouts);
    addLayoutComponentListener();

    currentLayout = DashboardModule.getLayoutService().getLayout(currentTab.getLayoutKey());
    view.getLayoutHeader().setSelectLayout(currentLayout.getArea().size(), currentLayout.getKey());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getCurrentLocale()
  {
    return super.getCurrentLocale();
  }

  /**
   * Refrehs the atatched view with tab uuid given
   *
   * @param pTabUUID
   *          Add a widget
   *          tab uuid
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(UI.getCurrent().getLocale());
  }

  /**
   *
   */
  private void addLayoutComponentListener()
  {
    for (final LayoutMapper layoutMapper : view.getLayoutHeader().getLayoutsMapper())
    {
      layoutMapper.getButton().addClickListener(new ClickListener()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = -3103435238062774925L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void buttonClick(final ClickEvent pEvent)
        {
          if (layoutMapper.getBoxes() > currentLayout.getArea().size())
          {
            final String key = layoutMapper.getKeyGridMapping().keySet().iterator().next();
            updateLayout(layoutMapper.getBoxes(), key);
          }
          else if (layoutMapper.getBoxes() < currentLayout.getArea().size())
          {
            UI.getCurrent().addWindow(view.getLayoutSelectWindow());
            newSelectedLayout = layoutMapper;

          }
        }
      });

      final Set<Entry<String, GridLayout>> gridLayoutEntrySet = layoutMapper.getKeyGridMapping().entrySet();
      for (final Entry<String, GridLayout> entry : gridLayoutEntrySet)
      {
        entry.getValue().addLayoutClickListener(new LayoutClickListener()
        {

          /**
           * Serial version id
           */
          private static final long serialVersionUID = 5786749413892495569L;

          /**
           * {@inheritDoc}
           */
          @Override
          public void layoutClick(final LayoutClickEvent pEvent)
          {
            final String key = entry.getKey();
            updateLayout(layoutMapper.getBoxes(), key);

          }
        });
      }
    }

  }

  /**
   * Set widgetDetailsWindow values and show it
   *
   * @param pWidgetModule
   *          the {@link WidgetModule} to show
   */
  public void showWidgetDetails(final WidgetModule pWidgetModule)
  {
    currentWidgetKey = pWidgetModule.getKey();
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    view.getWidgetDetailsName()
        .setValue(portalMessages.getMessage(getCurrentLocale(), getI18NNameKey(pWidgetModule.getKey())));
    view.getWidgetDetailsIcon()
        .setSource(ResourceUtils.buildImageResource(pWidgetModule.getIcon(), pWidgetModule.getKey()));
    view.getWidgetDetailsDescriptionLabel()
        .setValue(portalMessages.getMessage(getCurrentLocale(), getI18NDescKey(pWidgetModule.getKey())));
    final byte[] preview = pWidgetModule.getPreview();
    if (preview == null)
    {
      view.getWidgetDetailsThumbImage().setSource(new ThemeResource(NovaForgeResources.ICON_PICTURE_UNKNOW));
      view.getWidgetDetailsThumbImage()
          .setDescription(portalMessages.getMessage(getCurrentLocale(), Messages.DASHBOARD_WIDGET_NOPREVIEW));
    }
    else
    {
      view.getWidgetDetailsThumbImage()
          .setSource(ResourceUtils.buildImageResource(preview, pWidgetModule.getKey()));
    }

    final List<String> categories = pWidgetModule.getCategories();
    int addedCategoryCount = 0;
    view.getWidgetDetailsCategoriesLayout().removeAllComponents();
    if ((categories != null) && !categories.isEmpty())
    {
      final Set<String> widgetCategorySet = new HashSet<String>(categories);
      for (final CategoryDefinitionService category : getCategoryDefinitionByIds(widgetCategorySet))
      {
        if (addedCategoryCount > 0)
        {
          view.getWidgetDetailsCategoriesLayout().addComponent(new Label(", "));
        }
        final Button categoryButton = new Button();
        categoryButton.setStyleName(NovaForge.BUTTON_LINK);
        categoryButton.setCaption(category.getName(getCurrentLocale()));
        categoryButton.setDescription(portalMessages.getMessage(getCurrentLocale(),
            Messages.DASHBOARD_WIDGET_DETAIL_CATEGORY_TOOLTIP, category.getName(getCurrentLocale())));
        categoryButton.addClickListener(new Button.ClickListener()
        {

          /**
           *
           */
          private static final long serialVersionUID = -8685076871771912807L;

          @Override
          public void buttonClick(final ClickEvent event)
          {
            showWidgetsFromCategory(category.getCategory().getId());
          }
        });
        view.getWidgetDetailsCategoriesLayout().addComponent(categoryButton);
        addedCategoryCount = addedCategoryCount + 1;
      }
      view.getWidgetDetailsCategoriesLayout().setVisible(true);
    }
    else
    {
      view.getWidgetDetailsCategoriesLayout().setVisible(false);
    }

    view.getWidgetDetailsAddWidgetButton().setDescription(
        portalMessages.getMessage(getCurrentLocale(), Messages.DASHBOARD_ADDWIDGET_BUTTON_DESCRIPTION,
            portalMessages.getMessage(getCurrentLocale(), getI18NNameKey(pWidgetModule.getKey()))));
    UI.getCurrent().addWindow(view.getWidgetDetailsWindow());
  }

  /**
   * Get the I18N description for this widgetModule key
   *
   * @param pWidgetModuleKey
   *          the WidgetModule key
   * @return the I18N description associated
   */
  public String getI18NDescKey(final String pWidgetModuleKey)
  {
    return Messages.WIDGET_I18N_PREFIX + pWidgetModuleKey + Messages.WIDGET_I18N_DESC_SUFFIX;
  }

  private void showWidgetsFromCategory(final String pCategoryId)
  {
    UI.getCurrent().removeWindow(view.getWidgetDetailsWindow());
    final Filterable filterableWidget = widgetModuleContainer;
    String selectedItemId = pCategoryId;
    view.getAddWidgetFilterCategoryComboBox().select(pCategoryId);

    if (categoriesFilter != null)
    {
      filterableWidget.removeContainerFilter(categoriesFilter);
      categoriesFilter = null;
    }
    if ((selectedItemId != null) && !selectedItemId.isEmpty() && !allCategoryItemId.equals(selectedItemId))
    {
      if (noCategoryItemId.equals(selectedItemId))
      {
        selectedItemId = null;
      }
      categoriesFilter = new WidgetCategoryFilter(selectedItemId);
      filterableWidget.addContainerFilter(categoriesFilter);
    }
  }

}
