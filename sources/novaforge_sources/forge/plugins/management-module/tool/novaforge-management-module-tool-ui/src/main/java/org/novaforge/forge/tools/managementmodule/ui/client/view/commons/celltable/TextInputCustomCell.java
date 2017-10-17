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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanLineDTO;

/**
 * @author BILET-JC
 * 
 *         This class describes a textInputCell which can be disable
 */
public class TextInputCustomCell extends AbstractInputCell<String, TextInputCustomCell.ViewData> {

	private boolean disabled;
	private boolean dependsOnIsManual;
	private String size;
	private String maxlength;
	/**
	 * Constructs a TextInputCustomCell that renders its text.
	 *
	 * @param disabled
	 * @param size
	 * @param maxlength
	 * @param dependsOnIsManual
	 *            , indicates if the disabled param depends on isManual
	 *            attribute in EstimationDTO
	 */
	public TextInputCustomCell(final Boolean disabled, final Integer size, final Integer maxlength, final Boolean dependsOnIsManual) {
		super("change", "keyup", "click");

		if (size != null) {
			this.size = size.toString();
		}
		if (maxlength != null) {
			this.maxlength = maxlength.toString();
		}
		this.disabled = (disabled == null ? false : disabled);
		this.dependsOnIsManual = (dependsOnIsManual != null);
	}

	@Override
	public void onBrowserEvent(final Context context, final Element parent, final String value, final NativeEvent event,
	      final ValueUpdater<String> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Ignore events that don't target the input.
		final InputElement input = getInputElement(parent);
		final Element target = event.getEventTarget().cast();
		if (!input.isOrHasChild(target)) {
			return;
		}
		if (context.getKey() instanceof EstimationDTO) {
		   final EstimationDTO e = (EstimationDTO) context.getKey();
			if (!e.isManual() && dependsOnIsManual) {
				input.setDisabled(true);
			}
		} else if (context.getKey() instanceof ChargePlanLineDTO) {
		   final ChargePlanLineDTO line = (ChargePlanLineDTO) context.getKey();
			if (Constants.CHARGE_PLAN_FIRST_LINE_ID.equalsIgnoreCase(line.getIdDiscipline())
					|| Constants.CHARGE_PLAN_TOTAL_LINE_ID.equalsIgnoreCase(line.getIdDiscipline())) {
				input.setDisabled(true);
			}
		}
		final String eventType = event.getType();
		final Object key = context.getKey();
		if ("change".equals(eventType)) {
			finishEditing(parent, value, key, valueUpdater);
		} else if ("keyup".equals(eventType)) {
			// Record keys as they are typed.
			ViewData vd = getViewData(key);
			if (vd == null) {
				vd = new ViewData(value);
				setViewData(key, vd);
			}
			vd.setCurrentValue(input.getValue());
//		} else if ("click".equals(eventType) && (context.getKey() instanceof EstimationDTO)) {
//			TODO JCB event pour selectionModel
//			EstimationDTO e = (EstimationDTO) context.getKey();
//			Window.alert("la : " + e.getScopeUnit().getName());
		}
	}

	@Override
	protected void finishEditing(final Element parent, final String value, final Object key,
															 final ValueUpdater<String> valueUpdater)
	{
		final String newValue = getInputElement(parent).getValue();

		// Get the view data.
		ViewData vd = getViewData(key);
		if (vd == null)
		{
			vd = new ViewData(value);
			setViewData(key, vd);
		}
		vd.setCurrentValue(newValue);

		// Fire the value updater if the value has changed.
		if (valueUpdater != null && !vd.getCurrentValue().equals(vd.getLastValue()))
		{
			vd.setLastValue(newValue);
			valueUpdater.update(newValue);
		}

		// Blur the element.
		super.finishEditing(parent, newValue, key, valueUpdater);
	}

	@Override
	public InputElement getInputElement(Element parent)
	{
		return super.getInputElement(parent).cast();
	}

	@Override
	public void render(final com.google.gwt.cell.client.Cell.Context context, final String value, final SafeHtmlBuilder sb) {
		// Get the view data.
	   final Object key = context.getKey();
		ViewData viewData = getViewData(key);
		if (viewData != null && viewData.getCurrentValue().equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		final StringBuilder html = new StringBuilder();
		html.append("<input value=\"").append(value).append("\" TYPE=\"text\" ");
		// html.append("<input value=\"").append(value).append("\" TYPE=\"text\" tabindex=\"-1\" ");

		if (disabled) {
			html.append("disabled ");
		} else {
			// TODO must be optimized to avoid a instanceof call
			if (context.getKey() instanceof EstimationDTO) {
			   final EstimationDTO e = (EstimationDTO) context.getKey();
				if ((!e.isManual() && dependsOnIsManual) || (e.getScopeUnit().hasChild())) {
					html.append("disabled ");
				}
			} else if (context.getKey() instanceof ChargePlanLineDTO) {
			   final ChargePlanLineDTO line = (ChargePlanLineDTO) context.getKey();
				html.append("style=\"padding:0; marging:0;\" ");
				if (Constants.CHARGE_PLAN_FIRST_LINE_ID.equalsIgnoreCase(line.getIdDiscipline())
						|| Constants.CHARGE_PLAN_TOTAL_LINE_ID.equalsIgnoreCase(line.getIdDiscipline())) {
					html.append("disabled ");
				}
			}
		}

		if (size != null) {
			html.append("size=\"").append(size).append("\" ");
		}
		if (maxlength != null) {
			html.append("maxlength=\"").append(maxlength).append("\" ");
		}
		html.append("></input>");
		sb.appendHtmlConstant(html.toString());

	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled
	 *            the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the dependsOnIsManual
	 */
	public boolean isDependsOnIsManual() {
		return dependsOnIsManual;
	}

	/**
	 * @param dependsOnIsManual
	 *            the dependsOnIsManual to set
	 */
	public void setDependsOnIsManual(boolean dependsOnIsManual) {
		this.dependsOnIsManual = dependsOnIsManual;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the maxlength
	 */
	public String getMaxlength() {
		return maxlength;
	}

	/**
	 * @param maxlength
	 *            the maxlength to set
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * The {@code ViewData} for this cell.
	 */
	public static class ViewData
	{
		/**
		 * The last value that was updated.
		 */
		private String lastValue;

		/**
		 * The current value.
		 */
		private String curValue;

		/**
		 * Construct a ViewData instance containing a given value.
		 *
		 * @param value
		 *            a String value
		 */
		public ViewData(final String value)
		{
			this.lastValue = value;
			this.curValue = value;
		}

		/**
		 * Return the current value of the input element.
		 *
		 * @return the current value String
		 * @see #setCurrentValue(String)
		 */
		public String getCurrentValue()
		{
			return curValue;
		}

		/**
		 * Set the current value.
		 *
		 * @param curValue
		 *            the current value
		 * @see #getCurrentValue()
		 */
		protected void setCurrentValue(final String curValue)
		{
			this.curValue = curValue;
		}

		/**
		 * Return the last value sent to the {@link ValueUpdater}.
		 *
		 * @return the last value String
		 * @see #setLastValue(String)
		 */
		public String getLastValue()
		{
			return lastValue;
		}

		/**
		 * Set the last value.
		 *
		 * @param lastValue
		 *            the last value
		 * @see #getLastValue()
		 */
		protected void setLastValue(final String lastValue)
		{
			this.lastValue = lastValue;
		}

		/**
		 * Return a hash code based on the last and current values.
		 */
		@Override
		public int hashCode()
		{
			return (lastValue + "_*!@HASH_SEPARATOR@!*_" + curValue).hashCode();
		}

		/**
		 * Return true if the last and current values of this ViewData object
		 * are equal to those of the other object.
		 */
		@Override
		public boolean equals(final Object other)
		{
			if (!(other instanceof ViewData))
			{
				return false;
			}
			final ViewData vd = (ViewData) other;
			return equalsOrNull(lastValue, vd.lastValue) && equalsOrNull(curValue, vd.curValue);
		}

		private boolean equalsOrNull(final Object a, final Object b)
		{
			return ((a == null) ? ((b == null)) : a.equals(b));
		}
	}

}
