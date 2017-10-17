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
package org.novaforge.forge.ui.commons.vaadin.theme;

import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * Define the NovaForge Vaadin theme
 *
 * @author Guillaume Lamirand
 */
public class NovaForge extends Reindeer
{

  /*******************************************************/
  /********************* Common sizes ********************/
  /*******************************************************/
  /**
   * Common field Email size
   */
  public static final String FORM_FIELD_EMAIL_WIDTH = "250px";
  /**
   * Common field URI/URL size
   */
  public static final String FORM_FIELD_URL_WIDTH   = "350px";
  /**
   * Common Icon action size
   */
  public static final String ACTION_ICON_SIZE       = "30px";

  /**
   * Define width and height for an embedded novaforge application content
   */
  public static final String NOVAFORGE_APPLICATION_CONTENT = "nf-application-content";
  /**
   * Define textfield validation icon layout
   */
  public static final String TEXTFIELD_VALIDATION          = "nf-textfield-validation";
  /**
   * Define text decoration as capitalize
   */
  public static final String CAPITALIZE_TEXT               = "nf-capitalize-text";

  /**
   * Define width and height for project application content
   */
  public static final String PROJECT_APPLICATION_CONTENT       = "project-application-content";
  /**
   * Define width and height for project public project content
   */
  public static final String PUBLICPROJECT_APPLICATION_CONTENT = "publicproject-application-content";
  /**
   * Style css name used for a application in tree node
   */
  public static final String APPLICATIONS_TREENODE_APP         = "app";
  /**
   * Style css name used for a application in tree node
   */
  public static final String APPLICATIONS_TREENODE_SPACE       = "space";
  /**
   * Style css name used for a selected component
   */
  public static final String SELECTED                          = "selected";
  /**
   * Style css name used for the sidebar first component scrollable
   */
  public static final String SPLITPANEL_FIRST_SCROLLABLE       = "nf-splitpanel-scroll";
  /**
   * Define width and height for project application content
   */
  public static final String PRIVATE_PROJECT_DESCRIPTION       = "project-description";

  /**
   * Creates a menu bar with a transparent background.
   */
  public static final String MENUBAR_LIGHT = "light";

  /**
   * Create a label with 16px icon
   */
  public static final String LABEL_ICON_16 = "nf-label-icon-16";

  /**
   * Style css name used for the all component on home view
   */
  public static final String HOME                 = "home";
  /**
   * Style css name used for the project vertical layout on header
   */
  public static final String HEADER_PROJECT       = "project";
  /**
   * Tabsheet project style name
   */
  public static final String TABSHEET_PROJECT     = "project";
  /**
   * Tabsheet application style name
   */
  public static final String TABSHEET_APPLICATION = "application";

  /**
   * Form field default width
   */
  public static final String FORM_FIELD_WIDTH = "180px";
  /**
   * NovaForge home description label style
   */
  public static final String HOME_DESCRIPTION = "home-description";

  /**
   * Define element as floating right for standard button
   */
  public static final String FORM_TOP_RIGHT_BUTTONS = "nf-form-topright-buttons";

  /**
   * Define element with relative position
   */
  public static final String POSITION_RELATIVE = "nf-relative";

  /**
   * Uncollapse style for TreeTable
   */
  public static final String TREETABLE_UNCOLLAPSE         = "uncollapse";
  /**
   * Style for the dashboard layout separator
   */
  public static final String DASHBOARD_SEPARATOR          = "separator";
  /**
   * Style for the layout containing header
   */
  public static final String DASHBOARD_HEADER             = "tabheader";
  /**
   * Define Style widget item grid
   */
  public static final String DASHBOARD_HEADER_WIDGET_ITEM = "dashboard-widgets-item";

  /**
   * Define Style widget item grid
   */
  public static final String DASHBOARD_HEADER_WIDGET_ITEM_TITLE = "dashboard-widgets-item-title";

  /**
   * Define Style widget item grid
   */
  public static final String DASHBOARD_HEADER_WIDGET_ITEM_ICON = "dashboard-widgets-item-icon";

  /**
   * Define Style for box at bottom right corner
   */
  public static final String DASHBOARD_HEADER_WIDGET_ITEM_ADD_CORNER = "dashboard-widgets-item-add-corner";
  /**
   * Style for the layout containing settings image
   */
  public static final String DASHBOARD_HEADER_SETTINGS               = "header-settings";
  /**
   * Style for the layout containing settings image
   */
  public static final String DASHBOARD_HEADER_REFRESH                = "header-refresh";
  /**
   * Style for the layout containing content
   */
  public static final String DASHBOARD_CONTENT                       = "tabcontent";
  /**
   * Style for the layout containing content
   */
  public static final String DASHBOARD_TAB_DD                        = "dd";

  /**
   * Style for the layout containing boxes number
   */
  public static final String DASHBOARD_BOXES         = "boxes";
  /**
   * Style for the box container
   */
  public static final String DASHBOARD_BOX_CONTAINER = "boxcontainer";

  /**
   * Style for the grid layout
   */
  public static final String DASHBOARD_GRID_LAYOUT                     = "layout";
  /**
   * Style for the portlet header
   */
  public static final String DASHBOARD_PORTLET_HEADER                  = "portlet-header";
  /**
   * Style for the portlet header
   */
  public static final String DASHBOARD_PORTLET_WRAP_HEADER             = "v-portlet-header";
  /**
   * Style for the portlet slot
   */
  public static final String DASHBOARD_PORTLET_WRAP_SLOT               = "v-portlet-slot";
  /**
   * Style for the portlet wrapper
   */
  public static final String DASHBOARD_PORTLET_WRAP                    = "v-portlet";
  /**
   * Style for the portlet content
   */
  public static final String DASHBOARD_PORTLET_WRAP_CONTENT            = "v-portlet-content";
  /**
   * Style for the portlet header caption
   */
  public static final String DASHBOARD_PORTLET_WRAP_HEADER_CAPTION     = "v-portletwrap-header-caption";
  /**
   * Style for the portlet header caption text
   */
  public static final String DASHBOARD_PORTLET_WRAP_HEADER_CAPTIONTEXT = "v-portletwrap-header-captiontext";
  /**
   * Style for the portlet header caption icon
   */
  public static final String DASHBOARD_PORTLET_WRAP_HEADER_CAPTIONICON = "v-portletwrap-header-captionicon";
  /**
   * Style for the portlet header button back
   */
  public static final String DASHBOARD_PORTLET_WRAP_HEADER_BUTTON      = "v-portletwrap-header-buttonback";
  /**
   * Style for the settings header
   */
  public static final String DASHBOARD_SETTINGS_HEADER                 = "dashboard-settings-header";
  /**
   * Style for the settings content
   */
  public static final String DASHBOARD_SETTINGS_CONTENT                = "dashboard-settings-content";
  /**
   * Style for the settings
   */
  public static final String DASHBOARD_SETTINGS                        = "dashboard-settings";

  /**
   * Style class name for browser waiter
   */
  public static final String BROWSER_WAITER                        = "waiter";
  /**
   * Style for the upload layout
   */
  public static final String UPLOAD_LAYOUT                         = "upload";
  /**
   * Define a style to set cursor behavior like button
   */
  public static final String CURSOR_BUTTON                         = "nf-cursor-button";
  /**
   * Define dashboard area to be show
   */
  public static final String DASHBOARD_SHOW_AREA                   = "nf-show-area";
  /**
   * Define Style widget item grid
   */
  public static final String APPLICATION_ITEM_GRID                 = "application-item-grid";
  /**
   * Define style for text overflow set to ellipsis
   */
  public static final String TEXT_ELLIPSIS                         = "nf-text-ellipsis";
  /**
   * Define style for text wrapping
   */
  public static final String TEXT_WRAP                             = "nf-text-wrap";
  /**
   * Define style for dashboard settings selected widget source box
   */
  public static final String DASHBOARD_SETTINGS_SELECTED_BOX       = "nf-dashboard-selected-box";
  /**
   * Define style for dashboard settings selected widget source box title
   */
  public static final String DASHBOARD_SETTINGS_SELECTED_BOX_TITLE = "nf-dashboard-selected-box-title";
  /**
   * Define style for dashboard settings selected item
   */
  public static final String DASHBOARD_SETTINGS_SELECTED_BOX_ITEM  = "nf-dashboard-selected-box-item";
  /**
   * Define stle for the projects list on user profile
   */
  public static final String USER_PROFILE_PROJECTS                 = "user-profile-projects";
  /**
   * Define style for layout with light blue background color
   */
  public static final String LAYOUT_LIGHTBLUE                      = "nf-layout-lightblue";
  /**
   * Define style for layout with light grey background color
   */
  public static final String LAYOUT_LIGHTGREY                      = "nf-layout-lightgrey";

  /**
   * Define style for layout for an item
   */
  public static final String LAYOUT_ITEM       = "nf-layout-item";
  /**
   * Define style for layout with light grey background color
   */
  public static final String LAYOUT_SELECTABLE = "nf-layout-selectable";
  /**
   * Define style for layout with light grey background color
   */
  public static final String LAYOUT_SELECTED   = "nf-layout-selected";
  public static final String UPLOAD_FILE       = "nf-upload-file";
  /**
   * Define a style for scrollable layout
   */
  public static final String LAYOUT_SCROLLABLE = "nf-layout-scrollable";
  /**
   * Define a left border 1px solid grey
   */
  public static final String BORDER_LEFT_GREY  = "nf-border-left-grey";

  public static final String USER_ITEM_PICTURE = "nf-user-item-picture";

  public static final String HEADER_USER_ICON = "nf-header-user-icon";

  public static final String ARTICLE_NEWS_LAYOUT = "nf-newsLayout";

  public static final String ARTICLE_INFORMATION_LAYOUT = "nf-informationLayout";

  public static final String ARTICLE_ANNOUNCEMENT_LAYOUT = "nf-announcementLayout";

  public static final String ARTICLE_CATEGORY_DEFAULT_COLOR = "nf-categoryDefaultColorLayout";

  public static final String ARTICLE_NEWS_BOX = "nf-newsbox";

  public static final String ARTICLE_NEWS_FOOTER = "nf-news-footer";

  public static final String ARTICLE_ACCORDION_REQUIRED = "nf-accordion-required";

  // FIXME The following is being refactored
  /**
   * Define the theme name used by a {@link UI}
   */
  public static final String THEME_NAME             = "novaforge_blue";
  /****************************************************************
   *                 Common components styles
   ****************************************************************/
  /**
   * Creates a primary button, i.e. the important button of the page
   */
  public static final String BUTTON_PRIMARY         = "primary";
  /**
   * A prominent button that can be used when the action is considered unsafe for the user
   */
  public static final String BUTTON_DELETE          = "delete";
  /**
   * Style for a button, hidden when disabled
   */
  public static final String BUTTON_DISABLED_HIDDEN = "disabled-hidden";

  /**
   * Create a button which looks like a hypertext link but still
   * acts like a normal button.
   */
  public static final String BUTTON_LINK = "link";

  /**
   * Creates a button which looks like a secondary hypertext link but still
   * acts like a normal button, FOR TABLE USE ONLY.
   */
  public static final String BUTTON_LINK_TABLE    = "link-table";
  /**
   * Creates a button with a icon in 24px
   */
  public static final String BUTTON_LARGE         = "large";
  /**
   * Create a button from an image
   */
  public static final String BUTTON_IMAGE         = "img";
  /**
   * Form field big size
   */
  public static final String FORM_FIELD_BIG_WIDTH = "366px";

  /**
   * Create a label striked
   */
  public static final String LABEL_STRIKE  = "label-strike";
  /**
   * Create a label disable
   */
  public static final String LABEL_DISABLE = "label-disable";

  /**
   * Create a label with bold font
   */
  public static final String LABEL_BOLD   = "label-bold";
  /**
   * Create a label with grey color
   */
  public static final String LABEL_GREY   = "label-grey";
  /**
   * Create a label with red color
   */
  public static final String LABEL_RED    = "label-red";
  /**
   * Create a label with green color
   */
  public static final String LABEL_GREEN  = "label-green";
  /**
   * Create a label with orange color
   */
  public static final String LABEL_ORANGE = "label-orange";
  /**
   * Create a label with blue color
   */
  public static final String LABEL_BLUE   = "label-blue";

  /****************************************************************
   *                 Sidebar styles
   ****************************************************************/
  /**
   * Style css name used for the sidebar application content
   */
  public static final String SIDEBAR_APPLICATION_CONTENT = "sidebar-application-content";
  /**
   * Style css name used for a sidebar
   */
  public static final String SIDEBAR                     = "sidebar";
  /**
   * Style css name used for the component displayed next to the sidebar
   */
  public static final String SIDEBAR_SECOND_COMPONENT    = "sidebar-second-component";
  /**
   * Style css name used for the sidebar menu
   */
  public static final String SIDEBAR_MENU                = "sidebar-menu";
  /**
   * Style css name used for the sidebar branding
   */
  public static final String SIDEBAR_BRANDING            = "sidebar-branding";

  /****************************************************************
   *                 Modules specific styles
   ****************************************************************/

  /**
   * Define the css name for portal header
   */
  public static final String PORTAL_HEADER  = "portal-header";
  /**
   * Define the css name for portal content
   */
  public static final String PORTAL_CONTENT = "portal-content";
  /**
   * Define the css name for portal content
   */
  public static final String PORTAL_FOOTER  = "portal-footer";
}
