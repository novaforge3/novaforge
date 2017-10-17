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
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/descriptorTree-connector.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/descriptorTreeLibrary.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/sigma.min.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/shape-library.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/sigma.renderers.customShapes.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/contextMenu.min.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/drawingFunctions.js",
		"vaadin://themes/novaforge_blue/modules/novadeploy/js/script.js" })
public class DescriptorTree extends AbstractJavaScriptComponent
{
	/**
	 *
	 */
	private static final long serialVersionUID = -5725010870532374632L;
	ArrayList<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

	public DescriptorTree()
	{
		addFunction("onClick", new JavaScriptFunction()
		{
			@Override
			public void call(JsonArray arguments)
			{
				@SuppressWarnings("unused")
				JsonArray state = arguments.getArray(0);

				getState().currentDescriptor = state.getString(0);
				getState().currentDescriptorVersion = Integer.parseInt(state.getString(1));
				getState().whatToDo = state.getString(2);

				for (ValueChangeListener listener : listeners)
					listener.valueChange();
			}
		});
	}

	public interface ValueChangeListener extends Serializable
	{
		void valueChange();
	}

	public void addValueChangeListener(ValueChangeListener listener)
	{
		listeners.add(listener);
	}

	public void setWhatToDo(String whatToDo)
	{
		getState().whatToDo = whatToDo;
	}

	public String getWhatToDo()
	{
		return getState().whatToDo;
	}

	public void setCurrentDescriptor(String currentDescriptor)
	{
		getState().currentDescriptor = currentDescriptor;
	}

	public String getCurrentDescriptor()
	{
		return getState().currentDescriptor;
	}

	public void setCurrentDescriptorVersion(int currentDescriptorVersion)
	{
		getState().currentDescriptorVersion = currentDescriptorVersion;
	}

	public int getCurrentDescriptorVersion()
	{
		return getState().currentDescriptorVersion;
	}

	public void setDescriptorList(String descriptorList)
	{
		getState().descriptorList = descriptorList;
	}

	public String getDescriptorList()
	{
		return getState().descriptorList;
	}

	public void setIsTester(String isTester)
	{
		getState().isTester = isTester;
	}

	public String isTester()
	{
		return getState().isTester;
	}

	@Override
	protected DescriptorTreeState getState()
	{
		return (DescriptorTreeState) super.getState();
	}

}
