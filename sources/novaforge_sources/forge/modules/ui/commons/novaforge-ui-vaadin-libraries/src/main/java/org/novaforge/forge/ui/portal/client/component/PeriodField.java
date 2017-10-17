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
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.vaadin.risto.stepper.IntStepper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caseryj
 */
public class PeriodField extends HorizontalLayout {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 5420450779677417473L;
	/**
	 * The default start value
	 */
	private static int startValue = 1;
	/**
	 * The caption at the left of the component
	 */
	private final Label             captionAfter;
	/**
	 * The caption at the right of the component
	 */
	private final Label             captionBefore;
	/**
	 * The field stepper for int input
	 */
	private final IntStepper        valueStepper;
	/**
	 * The combobox field for range input
	 */
	private final ComboBox          valueComboBox;
	/**
	 * List of allowed ranges
	 */
	private       List<PeriodRange> ranges;
	/**
	 * The default range value selected
	 */
	private PeriodRange defaultRange = PeriodRange.MONTH;

	/**
	 * Default constructor
	 */
	public PeriodField()
	{
		setSpacing(true);
		captionAfter = new Label();
		captionBefore = new Label();
		valueStepper = new IntStepper();
		valueComboBox = new ComboBox();

		initRanges();

		valueStepper.setValue(startValue);
		valueStepper.setMinValue(1);
		valueStepper.setMaxValue(999);
		valueStepper.setImmediate(true);
		valueStepper.setWidth(10, Unit.EX);
		valueComboBox.setWidth(15, Unit.EX);
		valueComboBox.setTextInputAllowed(false);
		valueComboBox.setNullSelectionAllowed(false);
		valueComboBox.setImmediate(true);

		addComponent(captionBefore);
		addComponent(valueStepper);
		addComponent(valueComboBox);
		addComponent(captionAfter);

		addListeners();
	}

	/**
	 * Initialize the allowed range By defualt, all are allowed
	 */
	private void initRanges()
	{
		ranges = new ArrayList<PeriodRange>();
		ranges.add(PeriodRange.HOUR);
		ranges.add(PeriodRange.DAY);
		ranges.add(PeriodRange.WEEK);
		ranges.add(PeriodRange.MONTH);
		ranges.add(PeriodRange.YEAR);
	}

	/**
	 * Add view listeners
	 */
	private void addListeners()
	{
		valueStepper.addValueChangeListener(new ValueChangeListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = 5655608641313622709L;

			/**
			 * {@inheritDoc}
			 */
			@Override
			public void valueChange(final ValueChangeEvent event)
			{
				if (valueStepper.getValue() > valueStepper.getMaxValue())
				{
					valueStepper.setValue(valueStepper.getMaxValue());
				}
				if (valueStepper.getValue() < valueStepper.getMinValue())
				{
					valueStepper.setValue(valueStepper.getMinValue());
				}
			}
		});
	}

	/**
	 * Set the left caption of the component
	 *
	 * @param pCaption
	 *            The caption to set
	 */
	public void setCaptionBefore(final String pCaption)
	{
		captionBefore.setValue(pCaption);
	}

	/**
	 * Set the right caption of the component
	 *
	 * @param pCaption
	 *            The caption to set
	 */
	public void setCaptionAfter(final String pCaption)
	{
		captionAfter.setValue(pCaption);
	}

	/**
	 * Set the default range
	 *
	 * @param pPeriodRange
	 *            the {@link PeriodRange} to set as default
	 */
	public void setRange(final PeriodRange pPeriodRange)
	{
		defaultRange = pPeriodRange;
	}

	/**
	 * Get the compenent value in hour
	 *
	 * @return the period time in hour
	 */
	public int getValue() {
		final int numberValue = valueStepper.getValue();
		final PeriodRange selectedRange = getRangeValue();
		return numberValue * selectedRange.getFactorToHour();
	}

	/**
	 * Set the compenent value in hour
	 */
	public void setValue(final int pHourValue) {
		final PeriodRange range = PeriodRange.getForHour(pHourValue);
		valueComboBox.select(range.getId());
		valueStepper.setValue(pHourValue / range.getFactorToHour());
	}

	/**
	 * Get the selected range
	 *
	 * @return {@link PeriodRange} selected
	 */
	public PeriodRange getRangeValue()
	{
		return getRangeById((String) valueComboBox.getValue());
	}

	/**
	 * Find a range by it's rangeId
	 *
	 * @param pRangeId
	 *     The {@link PeriodRange} id
	 *
	 * @return the {@link PeriodRange} found
	 */
	private PeriodRange getRangeById(final String pRangeId)
	{
		PeriodRange range = null;
		for (final PeriodRange currentRange : ranges)
		{
			if (currentRange.getId().equals(pRangeId))
			{
				range = currentRange;
			}
		}
		return range;
	}

	/**
	 * Set the start value of the compenent
	 *
	 * @param pStartValue
	 *            the value to start
	 */
	public void setStartValue(final int pStartValue) {
		valueStepper.setValue(pStartValue);
	}

	/**
	 * Set the min int value
	 *
	 * @param pMinValue
	 *            the min value of component
	 */
	public void setMinValue(final int pMinValue) {
		valueStepper.setMinValue(pMinValue);
	}

	/**
	 * Set the max int value
	 *
	 * @param pMaxValue
	 *            the max value of component
	 */
	public void setMaxValue(final int pMaxValue) {
		valueStepper.setMaxValue(pMaxValue);
	}

	/**
	 * Set the step amount
	 *
	 * @param pStepAmount
	 *            the delta to add/remove at each up/down action
	 */
	public void setStepAmount(final int pStepAmount) {
		valueStepper.setStepAmount(pStepAmount);
	}

	/**
	 * Validate the content
	 *
	 * @throws InvalidValueException
	 */
	public void validate() throws InvalidValueException {
		valueStepper.validate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(final boolean enabled) {
		valueComboBox.setEnabled(enabled);
		valueStepper.setEnabled(enabled);
		captionAfter.setEnabled(enabled);
		captionBefore.setEnabled(enabled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		super.attach();
		fillRangeComboBox();
	}

	/**
	 * Fill the combobox range with allowed ranges
	 */
	private void fillRangeComboBox() {
		valueComboBox.removeAllItems();
		for (final PeriodRange range : ranges) {
			valueComboBox.addItem(range.getId());
			valueComboBox.setItemCaption(
					range.getId(),
					OSGiHelper.getPortalMessages().getMessage(
							UI.getCurrent().getLocale(),
							range.getI18NMessageId()));
		}
		valueComboBox.select(defaultRange.getId());
	}

	/**
	 * Sets the field required. Required fields must filled by the user. If the
	 * field is required, it is visually indicated in the user interface.
	 * Furthermore, setting field to be required implicitly adds "non-empty"
	 * validator and thus isValid() == false or any isEmpty() fields. In those
	 * cases validation errors are not painted as it is obvious that the user
	 * must fill in the required fields. On the other hand, for the non-required
	 * fields isValid() == true if the field isEmpty() regardless of any
	 * attached validators.
	 *
	 * @param required
	 *            Is the field required.
	 */
	public void setRequired(final boolean required) {
		valueStepper.setRequired(required);
		valueComboBox.setRequired(required);
	}

}
