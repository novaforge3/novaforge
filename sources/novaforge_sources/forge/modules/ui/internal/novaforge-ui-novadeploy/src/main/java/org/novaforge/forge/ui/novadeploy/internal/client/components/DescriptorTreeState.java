package org.novaforge.forge.ui.novadeploy.internal.client.components;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * @author Vincent Chenal
 */

public class DescriptorTreeState extends JavaScriptComponentState
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1877996430979580850L;

	public String whatToDo;
	public String currentDescriptor;
	public int currentDescriptorVersion;
	public String descriptorList;
	public String isTester;
}
