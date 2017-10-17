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
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox.Format;

/**
 * @author falsquelle-e
 */
public class DateBoxValidation extends Composite {

	private static final String EMPTY_TEXT = "";
	private static ValidationResources ressource = null;
	private static DateBoxValidationUiBinder uiBinder = GWT
			.create(DateBoxValidationUiBinder.class);
	@UiField
	CustomDateBox dateBox;
	@UiField
	Label error;
	private Validator validator;
	private Date startRangeDate;
	
	private static final String TIME_FORMAT_YYYYMMDD = "yyyyMMdd";
	public DateBoxValidation() {
		this((ValidationResources) GWT.create(ValidationResources.class));
	}

	public DateBoxValidation(final ValidationResources pResources) {
		ressource = pResources;
		ressource.style().ensureInjected();
		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));
		validator = new Validator() {
			@Override
			public boolean isValid(String pValue) {
				return pValue != null || !EMPTY_TEXT.equals(pValue);
			}

			@Override
			public String getErrorMessage() {
				return "Unknown validation error";
			}
		};
		bind();
	}

	private void bind() {

		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {

				setStateStyle();
			}
		});
	}

	private void setStateStyle() {
		if (!isValid())
		{
			dateBox.setStyleName(ressource.style().textInvalid());
			error.setText(validator.getErrorMessage());
		} else {
			setCleanStyle();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {

		String toValidate = null;
		if (dateBox.getValue() != null){

			toValidate = dateBox.getValue().toString();
			if (toValidate != null){
				toValidate = toValidate.trim();
			}
		}
		boolean valid = validator.isValid(toValidate);
//		boolean valid = dateBox.getValue() != null ? true : false;
		if (!valid) {
			dateBox.setStyleName(ressource.style().textInvalid());
			error.setText(validator.getErrorMessage());
		}
		return valid;
	}

	/**
	 * restore clean state of the field' style
	 */
	public void setCleanStyle()
	{
		dateBox.setStyleName(ressource.style().text());
		error.setText(EMPTY_TEXT);
	}

	public void setValidator(final Validator pValidator) {
		validator = pValidator;
	}

	public Date getValue() {
		return dateBox.getValue();
	}

	public void setValue(final Date pValue) {
		dateBox.setValue(pValue);
	}

	public void setValue(final Date value, boolean fireEvents) {
		dateBox.setValue(value, fireEvents);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear()
	{
		dateBox.setStyleName(ressource.style().text());
		dateBox.setValue(null);
		error.setText(EMPTY_TEXT);
	}
	
	

	// public HandlerRegistration addKeyUpHandler(KeyUpHandler handler)
	// {
	// return dateBox.addKeyUpHandler(handler);
	// }
	//
	// public HandlerRegistration
	// addValueChangeHandler(ValueChangeHandler<String> handler)
	// {
	// return dateBox.addValueChangeHandler(handler);
	// }

	public void setId(String pValue) {
		dateBox.ensureDebugId(pValue);
	}

	public void setFocus() {
		dateBox.setFocus(true);
	}

	public void setEnabled(boolean b) {
		dateBox.setEnabled(b);
	}

	public void setFormat(Format format) {
		dateBox.setFormat(format);
	}

	public void addValueChangeHandler(ValueChangeHandler<Date> dateChangeHandler)
	{
	  dateBox.addValueChangeHandler(dateChangeHandler);
	}
	
	
	/**
	 * set current month DateBoxValidation to given date
	 * @param month month to set
	 */
	public void setCurrentMonth(Date month)
	{
	  dateBox.setDefaultMonth(month);
	  startRangeDate=month;
	  dateBox.getDatePicker().addShowRangeHandlerAndFire(new ShowRangeHandler<Date>()
	  {
	      @Override
	      public void onShowRange(final ShowRangeEvent<Date> dateShowRangeEvent)
	      {
            final Date startRange = zeroTime(DateBoxValidation.this.startRangeDate);
            Date d ;
            if (dateShowRangeEvent != null)
            {
              d = zeroTime(dateShowRangeEvent.getStart());
            } 
            else
            {
              d = zeroTime(today());
            }
            while (d.before(startRange))
            {
                dateBox.getDatePicker().setTransientEnabledOnDates(false, d);
                d = nextDay(d);
            }
	      }
	  });
	  
	}
	
	/** return current date 
	 * @return current date
	 */
	private static Date today()
	{
	    return zeroTime(new Date());
	}

	/** this is important to get rid of the time portion, including ms */
	private static Date zeroTime(final Date date)
	{
	  return DateTimeFormat.getFormat(TIME_FORMAT_YYYYMMDD).parse(DateTimeFormat.getFormat(TIME_FORMAT_YYYYMMDD).format(date));
	}

	/**
	 * returns next day from given date
	 * @param date date 
	 * @return next day from given date
	 */
	private static Date nextDay(final Date date)
	{
	    return zeroTime(new Date(date.getTime() + 24 * 60 * 60 * 1000));
	}
	
	interface DateBoxValidationUiBinder extends UiBinder<Widget, DateBoxValidation>
	{
	}
}