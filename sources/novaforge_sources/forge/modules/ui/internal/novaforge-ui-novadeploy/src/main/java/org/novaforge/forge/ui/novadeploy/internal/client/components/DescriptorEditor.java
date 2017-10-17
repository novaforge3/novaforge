package org.novaforge.forge.ui.novadeploy.internal.client.components;

import java.io.Serializable;
import java.util.ArrayList;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;

/**
 * @author Vincent Chenal
 */

@JavaScript(
{ "vaadin://themes/novaforge_blue/modules/novadeploy/js/jquery-1.11.2.min.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/descriptorEditor-connector.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/descriptorEditorLibrary.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/script2.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/ace/ace.js" })
public class DescriptorEditor extends AbstractJavaScriptComponent
{
	/**
	 *
	 */
	private static final long serialVersionUID = -628773215534461254L;
	ArrayList<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

	public DescriptorEditor()
	{
		addFunction("onClick", new JavaScriptFunction()
		{
			@Override
			public void call(JsonArray arguments)
			{
				getState().descriptorContent = arguments.getString(0);

				for (ValueChangeListener listener : listeners)
					listener.valueChange();
			}
		});
	}

	public enum Mode
	{
		NEW, READ, EDIT;

		public String toString()
		{
			switch (this)
			{
			case NEW:
				return "NEW";
			case EDIT:
				return "EDIT";
			case READ:
				return "READ";
			default:
				return null;
			}
		}

		public static DescriptorEditor.Mode fromString(String mode)
		{
			switch (mode)
			{
			case "NEW":
				return DescriptorEditor.Mode.NEW;
			case "EDIT":
				return DescriptorEditor.Mode.EDIT;
			case "READ":
				return DescriptorEditor.Mode.READ;
			default:
				return null;
			}
		}
	}

	public interface ValueChangeListener extends Serializable
	{
		void valueChange();
	}

	public void addValueChangeListener(ValueChangeListener listener)
	{
		listeners.add(listener);
	}

	public void setDescriptorContent(String descriptorContent)
	{
		getState().descriptorContent = descriptorContent;
	}

	public String getDescriptorContent()
	{
		return getState().descriptorContent;
	}

	@Override
	protected DescriptorEditorState getState()
	{
		return (DescriptorEditorState) super.getState();
	}
}
