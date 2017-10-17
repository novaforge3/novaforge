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

package org.novaforge.forge.ui.portal.client.component;

import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.TargetDetailsImpl;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.Component;
import com.vaadin.ui.LegacyComponent;
import fi.jasoft.dragdroplayouts.DDTabSheet;
import fi.jasoft.dragdroplayouts.client.ui.Constants;
import fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;
import fi.jasoft.dragdroplayouts.interfaces.DragFilter;
import fi.jasoft.dragdroplayouts.interfaces.DragFilterSupport;
import fi.jasoft.dragdroplayouts.interfaces.DragImageProvider;
import fi.jasoft.dragdroplayouts.interfaces.DragImageReferenceSupport;
import fi.jasoft.dragdroplayouts.interfaces.LayoutDragSource;
import fi.jasoft.dragdroplayouts.interfaces.ShimSupport;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.dd.shared.DDTabsheetImprovedState;

import java.util.Iterator;
import java.util.Map;

/**
 * Custom DDTabsheet based on TabsheetImproved
 *
 * @author John Ahlroos / www.jasoft.fi
 * @author Guillaume Lamirand
 * @see DDTabSheet
 */
@SuppressWarnings("serial")
public class DDTabsheetImproved extends TabsheetImproved
    implements LayoutDragSource, DropTarget, ShimSupport, LegacyComponent, DragFilterSupport, DragImageReferenceSupport
{

  /**
   * The drop handler which handles dropped components in the layout.
   */
  private DropHandler dropHandler;

  // A filter for dragging components.
  private DragFilter dragFilter = DragFilter.ALL;

  private DragImageProvider dragImageProvider;

  public DDTabsheetImproved()
  {
    addStyleName(NovaForge.DASHBOARD_TAB_DD);
  }

  public Transferable getTransferable(Map<String, Object> rawVariables)
  {
    if (rawVariables.get(Constants.TRANSFERABLE_DETAIL_INDEX) != null)
    {
      // We dragged a tab, substitute component with tab content
      int index = Integer.parseInt(rawVariables.get(Constants.TRANSFERABLE_DETAIL_INDEX).toString());
      Iterator<Component> iter = getComponentIterator();
      int counter = 0;
      Component c = null;
      while (iter.hasNext())
      {
        c = iter.next();
        if (counter == index)
        {
          break;
        }
        counter++;
      }

      rawVariables.put(Constants.TRANSFERABLE_DETAIL_COMPONENT, c);
    }
    else if (rawVariables.get("component") == null)
    {
      rawVariables.put(Constants.TRANSFERABLE_DETAIL_COMPONENT, DDTabsheetImproved.this);
    }

    return new LayoutBoundTransferable(this, rawVariables);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.vaadin.event.dd.DropTarget#getDropHandler()
   */
  public DropHandler getDropHandler()
  {
    return dropHandler;
  }

  /**
   * Sets the current handler which handles dropped components on the layout. By setting a drop
   * handler dropping components on the layout is enabled. By setting the dropHandler to null
   * dropping is disabled.
   *
   * @param dropHandler
   *     The drop handler to handle drop events or null to disable dropping
   */
  public void setDropHandler(DropHandler dropHandler)
  {
    if (this.dropHandler != dropHandler)
    {
      this.dropHandler = dropHandler;
      requestRepaint();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.vaadin.event.dd.DropTarget#translateDropTargetDetails(java.util.Map)
   */
  public TargetDetails translateDropTargetDetails(Map<String, Object> clientVariables)
  {
    return new TabSheetTargetDetails(clientVariables);
  }

  /*
   * (non-Javadoc)
   *
   * @see fi.jasoft.dragdroplayouts.interfaces.LayoutDragSource#getDragMode ()
   */
  public LayoutDragMode getDragMode()
  {
    return getState().ddState.dragMode;
  }

  /*
   * (non-Javadoc)
   *
   * @see fi.jasoft.dragdroplayouts.interfaces.LayoutDragSource#setDragMode
   * (fi.jasoft.dragdroplayouts.client.ui.LayoutDragMode)
   */
  public void setDragMode(LayoutDragMode mode)
  {
    getState().ddState.dragMode = mode;
  }

  /**
   * {@inheritDoc}
   */
  public DragFilter getDragFilter()
  {
    return dragFilter;
  }

  /**
   * {@inheritDoc}
   */
  public void setDragFilter(DragFilter dragFilter)
  {
    this.dragFilter = dragFilter;
  }

  @Override
  public DDTabsheetImprovedState getState()
  {
    return (DDTabsheetImprovedState) super.getState();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.vaadin.ui.AbstractOrderedLayout#translateDropTargetDetails(java.util .Map)
   */
  @Override
  public void paintContent(PaintTarget target) throws PaintException
  {
    if (dropHandler != null && isEnabled())
    {
      dropHandler.getAcceptCriterion().paint(target);
    }
  }

  /**
   * Sets the ratio which determines how a tab is divided into drop zones. The ratio is measured
   * from the left and right borders. For example, setting the ratio to 0.3 will divide the drop
   * zone in three equal parts (left,middle,right). Setting the ratio to 0.5 will disable dropping
   * in the middle and setting it to 0 will disable dropping at the sides.
   *
   * @param ratio
   *     A ratio between 0 and 0.5. Default is 0.2
   */
  public void setComponentHorizontalDropRatio(float ratio)
  {
    if (getState().tabLeftRightDropRatio != ratio)
    {
      if (ratio >= 0 && ratio <= 0.5)
      {
        getState().tabLeftRightDropRatio = ratio;
      }
      else
      {
        throw new IllegalArgumentException("Ratio must be between 0 and 0.5");
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setShim(boolean shim)
  {
    getState().ddState.iframeShims = shim;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isShimmed()
  {
    return getState().ddState.iframeShims;
  }

  @Override
  public void beforeClientResponse(boolean initial)
  {
    super.beforeClientResponse(initial);
    fi.jasoft.dragdroplayouts.DDUtil.onBeforeClientResponse(this, getState());
  }

  @Override
  public void changeVariables(Object source, Map<String, Object> variables)
  {
    // FIXME Remove when drag&drop is no longer legacy
  }

  public class TabSheetTargetDetails extends TargetDetailsImpl
  {

    private Component over;

    private int index = -1;

    protected TabSheetTargetDetails(Map<String, Object> rawDropData)
    {
      super(rawDropData, DDTabsheetImproved.this);

      // Get over which component (if any) the drop was made and the
      // index of it
      if (rawDropData.get(Constants.DROP_DETAIL_TO) != null)
      {
        Object to = rawDropData.get(Constants.DROP_DETAIL_TO);
        index = Integer.valueOf(to.toString());
      }

      if (index >= 0 && index < getComponentCount())
      {
        Iterator<Component> iter = getComponentIterator();
        int counter = 0;
        while (iter.hasNext())
        {
          over = iter.next();
          if (counter == index)
          {
            break;
          }
          counter++;
        }
      }
      else
      {
        over = DDTabsheetImproved.this;
      }
    }

    /**
     * The component over which the drop was made.
     *
     * @return Null if the drop was not over a component, else the component
     */
    public Component getOverComponent()
    {
      return over;
    }

    /**
     * The index over which the drop was made. If the drop was not made over any component then it
     * returns -1.
     *
     * @return The index of the component or -1 if over no component.
     */
    public int getOverIndex()
    {
      return index;
    }

    /**
     * Some details about the mouse event
     *
     * @return details about the actual event that caused the event details. Practically mouse move
     * or mouse up.
     */
    public MouseEventDetails getMouseEvent()
    {
      return MouseEventDetails.deSerialize((String) getData(Constants.DROP_DETAIL_MOUSE_EVENT));
    }

    /**
     * Get the horizontal position of the dropped component within the underlying cell.
     *
     * @return The drop location
     */
    public HorizontalDropLocation getDropLocation()
    {
      if (getData(Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION) != null)
      {
        return HorizontalDropLocation.valueOf((String) getData(Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION));
      }
      return null;
    }
  }

  @Override
  public void setDragImageProvider(fi.jasoft.dragdroplayouts.interfaces.DragImageProvider provider)
  {
    this.dragImageProvider = provider;
    markAsDirty();
  }

  @Override
  public fi.jasoft.dragdroplayouts.interfaces.DragImageProvider getDragImageProvider()
  {
    return this.dragImageProvider;
  }
}

