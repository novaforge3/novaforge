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

package org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.dd;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VTabsheetPanel;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import fi.jasoft.dragdroplayouts.client.VDragFilter;
import fi.jasoft.dragdroplayouts.client.ui.Constants;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.client.ui.VDragDropUtil;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VDDHasDropHandler;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VDDTabContainer;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VDragImageProvider;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasDragFilter;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasDragImageReferenceSupport;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasDragMode;
import fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasIframeShims;
import fi.jasoft.dragdroplayouts.client.ui.tabsheet.VDDTabSheet;
import fi.jasoft.dragdroplayouts.client.ui.util.IframeCoverUtility;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.VTabsheetImproved;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.dd.VLayoutDragDropMouseHandler.DragStartListener;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.dd.shared.DDTabsheetImprovedState;

/**
 * Client side implementation for org.novaforge.forge.ui.portal.client.component.DDTabsheetImproved
 *
 * @author John Ahlroos / www.jasoft.fi
 * @author Guillaume Lamirand
 * @see VDDTabSheet
 */
public class VDDTabsheetImproved extends VTabsheetImproved
    implements VHasDragMode, VDDHasDropHandler<VDDTabsheetImprovedDropHandler>, DragStartListener, VDDTabContainer,
               VHasDragFilter, VHasDragImageReferenceSupport, VHasIframeShims
{

  public static final String CLASSNAME_NEW_TAB        = "new-tab";
  public static final String CLASSNAME_NEW_TAB_LEFT   = "new-tab-left";
  public static final String CLASSNAME_NEW_TAB_RIGHT  = "new-tab-right";
  public static final String CLASSNAME_NEW_TAB_CENTER = "new-tab-center";
  private final ComplexPanel   tabBar;
  private final VTabsheetPanel tabPanel;
  private final Element        spacer;
  private final Element                     newTab             = DOM.createDiv();
  private final IframeCoverUtility          iframeCoverUtility = new IframeCoverUtility();
  private final VLayoutDragDropMouseHandler ddMouseHandler     = new VLayoutDragDropMouseHandler(this,
                                                                                                 LayoutDragMode.NONE);
  private VDDTabsheetImprovedDropHandler dropHandler;
  private Element                        currentlyEmphasised;
  private VDragFilter                    dragFilter;
  private double tabLeftRightDropRatio = DDTabsheetImprovedState.DEFAULT_HORIZONTAL_DROP_RATIO;

  private LayoutDragMode mode = LayoutDragMode.NONE;

  private boolean iframeCovers = false;

  public VDDTabsheetImproved()
  {
    super();

    newTab.setClassName(CLASSNAME_NEW_TAB);

    // Get the tabBar
    tabBar = (ComplexPanel) getChildren().get(0);

    // Get the content
    tabPanel = (VTabsheetPanel) getChildren().get(1);

    // Get the spacer
    Element tBody = tabBar.getElement();
    spacer = tBody.getChild(tBody.getChildCount() - 1).getChild(0).getChild(0).cast();
  }

  @Override
  protected void onLoad()
  {
    super.onLoad();
    ddMouseHandler.addDragStartListener(this);
    ddMouseHandler.setAttachTarget(tabBar);
    setDragMode(mode);
    iframeShimsEnabled(iframeCovers);
  }

  @Override
  protected void onUnload()
  {
    super.onUnload();
    ddMouseHandler.removeDragStartListener(this);
    ddMouseHandler.updateDragMode(LayoutDragMode.NONE);
    iframeCoverUtility.setIframeCoversEnabled(false, getElement(), LayoutDragMode.NONE);
  }

  @Override
  public void iframeShimsEnabled(boolean enabled)
  {
    iframeCovers = enabled;
    iframeCoverUtility.setIframeCoversEnabled(enabled, getElement(), mode);
  }  /*
   * (non-Javadoc)
   *
   * @see com.vaadin.terminal.gwt.client.ui.dd.VHasDropHandler#getDropHandler()
   */

  @Override
  public boolean isIframeShimsEnabled()
  {
    return iframeCovers;
  }

  /**
   * A hook for extended components to post process the the drop before it is sent to the server.
   * Useful if you don't want to override the whole drop handler.
   */
  protected boolean postDropHook(VDragEvent drag)
  {
    // Extended classes can add content here...
    return true;
  }

  public VDDTabsheetImprovedDropHandler getDropHandler()
  {
    return dropHandler;
  }

  /**
   * A hook for extended components to post process the the enter event. Useful if you don't want to
   * override the whole drophandler.
   */
  protected void postEnterHook(VDragEvent drag)
  {
    // Extended classes can add content here...
  }

  /**
   * A hook for extended components to post process the the leave event. Useful if you don't want to
   * override the whole drophandler.
   */
  protected void postLeaveHook(VDragEvent drag)
  {
    // Extended classes can add content here...
  }

  /**
   * A hook for extended components to post process the the over event. Useful if you don't want to
   * override the whole drophandler.
   */
  protected void postOverHook(VDragEvent drag)
  {
    // Extended classes can add content here...
  }

  public void setDropHandler(VDDTabsheetImprovedDropHandler handler)
  {
    this.dropHandler = handler;
  }

  /**
   * Can be used to listen to drag start events, must return true for the drag to commence. Return
   * false to interrupt the drag:
   */
  public boolean dragStart(Widget widget, LayoutDragMode mode)
  {
    return getDragMode() != LayoutDragMode.NONE;
  }

  /*
   * (non-Javadoc)
   *
   * @see fi.jasoft.dragdroplayouts.client.ui.VHasDragMode#getDragMode()
   */
  public LayoutDragMode getDragMode()
  {
    return ddMouseHandler.getDragMode();
  }

  @Override
  public void setDragMode(LayoutDragMode mode)
  {
    this.mode = mode;
    ddMouseHandler.updateDragMode(mode);
    iframeShimsEnabled(iframeCovers);
  }

  /**
   * Updates the drop details while dragging. This is needed to ensure client side criterias can
   * validate the drop location.
   *
   * @param widget
   *     The container which we are hovering over
   * @param event
   *     The drag event
   */
  protected void updateDragDetails(VDragEvent event)
  {
    Element element = event.getElementOver();
    if (element == null)
    {
      return;
    }

    if (tabBar.getElement().isOrHasChild(element))
    {
      Widget w = Util.findWidget(element, null);

      if (w == tabBar)
      {
        // Ove3r the spacer

        // Add index
        event.getDropDetails().put(Constants.DROP_DETAIL_TO, tabBar.getWidgetCount() - 1);

        // Add drop location
        event.getDropDetails().put(Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION, HorizontalDropLocation.RIGHT);

      }
      else
      {

        // Add index
        event.getDropDetails().put(Constants.DROP_DETAIL_TO, getTabPosition(w));

        // Add drop location
        HorizontalDropLocation location = VDragDropUtil.getHorizontalDropLocation(DOM.asOld(element),
                                                                                  Util.getTouchOrMouseClientX(event
                                                                                                                  .getCurrentGwtEvent()),
                                                                                  tabLeftRightDropRatio);
        event.getDropDetails().put(Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION, location);
      }

      // Add mouse event details
      MouseEventDetails details = MouseEventDetailsBuilder.buildMouseEventDetails(event.getCurrentGwtEvent(),
                                                                                  getElement());
      event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT, details.serialize());
    }
  }

  /**
   * Emphasisizes a container element
   *
   * @param element
   */
  protected void emphasis(Element element, VDragEvent event)
  {

    boolean internalDrag = event.getTransferable().getDragSource() == this;

    if (tabBar.getElement().isOrHasChild(element))
    {
      Widget w = Util.findWidget(element, null);

      if (w == tabBar && !internalDrag)
      {
        // Over spacer
        Element spacerContent = spacer.getChild(0).cast();
        spacerContent.appendChild(newTab);
        currentlyEmphasised = element;

      }
      else if (w instanceof VCaption)
      {

        // Over a tab
        HorizontalDropLocation location = VDragDropUtil.getHorizontalDropLocation(DOM.asOld(element),
                                                                                  Util.getTouchOrMouseClientX(event
                                                                                                                  .getCurrentGwtEvent()),
                                                                                  tabLeftRightDropRatio);

        if (location == HorizontalDropLocation.LEFT)
        {

          int index = getTabPosition(w);

          if (index == 0)
          {

            currentlyEmphasised = tabBar.getWidget(0).getElement().getFirstChildElement().cast();
            currentlyEmphasised.addClassName(CLASSNAME_NEW_TAB_LEFT);
          }
          else
          {
            Widget prevTab = tabBar.getWidget(index - 1);
            currentlyEmphasised = prevTab.getElement();
            currentlyEmphasised.addClassName(CLASSNAME_NEW_TAB_RIGHT);
          }

        }
        else if (location == HorizontalDropLocation.RIGHT)
        {
          int index = getTabPosition(w);
          currentlyEmphasised = tabBar.getWidget(index).getElement();
          currentlyEmphasised.addClassName(CLASSNAME_NEW_TAB_RIGHT);
        }
        else
        {
          int index = getTabPosition(w);
          currentlyEmphasised = tabBar.getWidget(index).getElement();
          currentlyEmphasised.addClassName(CLASSNAME_NEW_TAB_CENTER);
        }

      }
    }
  }

  /**
   * Removes any previous emphasis made by drag&drop
   */
  protected void deEmphasis()
  {
    if (currentlyEmphasised != null && tabBar.getElement().isOrHasChild(currentlyEmphasised))
    {
      Widget w = Util.findWidget(currentlyEmphasised, null);

      currentlyEmphasised.removeClassName(CLASSNAME_NEW_TAB_LEFT);
      currentlyEmphasised.removeClassName(CLASSNAME_NEW_TAB_RIGHT);
      currentlyEmphasised.removeClassName(CLASSNAME_NEW_TAB_CENTER);

      if (w == tabBar)
      {
        // Over spacer
        Element spacerContent = spacer.getChild(0).cast();
        spacerContent.removeChild(newTab);
      }

      currentlyEmphasised = null;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see fi.jasoft.dragdroplayouts.client.ui.interfaces.VDDTabContainer#
   * getTabContentPosition(com.google.gwt.user.client.ui.Widget)
   */
  public int getTabContentPosition(Widget content)
  {
    return tabPanel.getWidgetIndex(content);
  }

  /*
   * (non-Javadoc)
   *
   * @see fi.jasoft.dragdroplayouts.client.ui.interfaces.VDDTabContainer#getTabPosition
   * (com.google.gwt.user.client.ui.Widget)
   */
  public int getTabPosition(Widget tab)
  {
    if (tab instanceof TabCaption)
    {
      tab = tab.getParent();
    }
    return tabBar.getWidgetIndex(tab);
  }

  /*
   * (non-Javadoc)
   *
   * @see fi.jasoft.dragdroplayouts.client.ui.interfaces.VHasDragFilter#getDragFilter ()
   */
  public VDragFilter getDragFilter()
  {
    return dragFilter;
  }

  @Override
  public void setDragFilter(VDragFilter filter)
  {
    this.dragFilter = filter;
  }

  IframeCoverUtility getIframeCoverUtility()
  {
    return iframeCoverUtility;
  }

  VLayoutDragDropMouseHandler getMouseHandler()
  {
    return ddMouseHandler;
  }

  public double getTabLeftRightDropRatio()
  {
    return tabLeftRightDropRatio;
  }

  public void setTabLeftRightDropRatio(double tabLeftRightDropRatio)
  {
    this.tabLeftRightDropRatio = tabLeftRightDropRatio;
  }

  @Override
  public void setDragImageProvider(VDragImageProvider provider)
  {
    ddMouseHandler.setDragImageProvider(provider);
  }

}