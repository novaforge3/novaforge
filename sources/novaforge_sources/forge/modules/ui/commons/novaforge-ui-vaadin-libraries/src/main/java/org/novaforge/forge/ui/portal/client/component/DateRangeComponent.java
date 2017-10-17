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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PopupDateField;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author bruno
 */
@SuppressWarnings("serial")
public class DateRangeComponent extends FormLayout
{
  
  private ComboBox rangeTypeComboBox;
  private PopupDateField startDate;
  private PopupDateField endDate;

  /**
   *
   */
  public DateRangeComponent(Date pStartDate, Date pEndDate)
  {
    this();
    setStartValue(pStartDate);
    setEndValue(pEndDate);
  }

  /**
   * 
   */
  public DateRangeComponent()
  {
    initDateRangeComponents();
    setResolution(Resolution.DAY);
    startDate.setRequired(false);
    endDate.setRequired(false);
    markAsDirty();
  }

  public void initDateRangeComponents()
  {
    startDate = new PopupDateField();
    startDate.setValue(new Date());
    startDate.setImmediate(true);

    endDate = new PopupDateField();
    endDate.setValue(new Date());
    endDate.setImmediate(true);

    rangeTypeComboBox = new ComboBox();
    rangeTypeComboBox.setImmediate(true);
    rangeTypeComboBox.addItem(RangeType.LAST_WEEK);
    rangeTypeComboBox.addItem(RangeType.LAST_MONTH);
    rangeTypeComboBox.addItem(RangeType.CUSTOM);
    rangeTypeComboBox.setNullSelectionAllowed(false);
    rangeTypeComboBox.addValueChangeListener(new ValueChangeListener()
    {
      @Override
      public void valueChange(ValueChangeEvent event)
      {
        RangeType value = (RangeType) event.getProperty().getValue();
        boolean enabled;
        switch (value)
        {
          case LAST_WEEK:
          case LAST_MONTH:
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            DateRangeComponent.this.setEndValue(cal.getTime());
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.add(value.getCalendarField(), -1);
            DateRangeComponent.this.setStartValue(cal.getTime());
            enabled = false;
            break;
          default:
            enabled = true;
            break;
        }
        DateRangeComponent.this.startDate.setVisible(enabled);
        DateRangeComponent.this.endDate.setVisible(enabled);
      }
    });
    setRangeType(RangeType.CUSTOM);

    addComponent(rangeTypeComboBox);
    addComponent(startDate);
    addComponent(endDate);
}

  public void setResolution(Resolution pResolution)
  {
    startDate.setResolution(pResolution);
    endDate.setResolution(pResolution);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void markAsDirty()
  {
    super.markAsDirty();
    if (rangeTypeComboBox != null)
    {
      rangeTypeComboBox.markAsDirty();
    }
    if (startDate != null)
    {
      startDate.markAsDirty();
    }
    if (endDate != null)
    {
      endDate.markAsDirty();
    }
  }
  
  public RangeType getRangeType() {
    return (RangeType) rangeTypeComboBox.getValue();
  }

  public void setRangeType(RangeType pRangeType)
  {
    rangeTypeComboBox.setValue(pRangeType);
  }

  public Date getStartValue()
  {
    switch ((RangeType) rangeTypeComboBox.getValue())
    {
      case CUSTOM:
        return startDate.getValue();
      default:
        return null;
    }
  }

  public void setStartValue(Date pDate)
  {
    startDate.setValue(pDate);
  }

  public Date getEndValue()
  {
    switch ((RangeType) rangeTypeComboBox.getValue())
    {
      case CUSTOM:
        return endDate.getValue();
      default:
        return null;
    }
  }

  public void setEndValue(Date pDate)
  {
    endDate.setValue(pDate);
  }

  /**
   * Should be called to refresh all messages
   * 
   * @param pPortalMessages
   *          should refere to service implementation
   * @param pLocale
   *          the locale userd to localized messages
   */
  public void refreshLocale(final PortalMessages pPortalMessages, final Locale pLocale)
  {
    if ((pPortalMessages != null) && (pLocale != null))
    {
      startDate.setCaption(pPortalMessages.getMessage(pLocale, Messages.DATE_RANGE_START));
      endDate.setCaption(pPortalMessages.getMessage(pLocale, Messages.DATE_RANGE_END));
      for (Object itemId : rangeTypeComboBox.getItemIds())
      {
        rangeTypeComboBox.setItemCaption(itemId, pPortalMessages.getMessage(pLocale, itemId.toString()));
      }
    }
  }

  public enum RangeType
  {
    LAST_WEEK,
    LAST_MONTH,
    CUSTOM;

    public int getCalendarField()
    {
      switch (this)
      {
        case LAST_WEEK:
          return Calendar.WEEK_OF_MONTH;
        case LAST_MONTH:
          return Calendar.MONTH;
        default:
          return -1;
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
      switch (this)
      {
        case LAST_WEEK:
          return Messages.DATE_RANGE_LAST_WEEK;
        case LAST_MONTH:
          return Messages.DATE_RANGE_LAST_MONTH;
        default:
          return Messages.DATE_RANGE_CUSTOM;
      }
    }
  }
}
