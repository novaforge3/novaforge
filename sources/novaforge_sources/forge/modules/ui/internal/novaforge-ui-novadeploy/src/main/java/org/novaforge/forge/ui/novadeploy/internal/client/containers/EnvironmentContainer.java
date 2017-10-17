package org.novaforge.forge.ui.novadeploy.internal.client.containers;

import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerEnvironment;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.EnvironmentState;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 * @author Vincent Chenal
 */

public class EnvironmentContainer extends IndexedContainer
{

	/**
	 *
	 */
	private static final long serialVersionUID = -1818840129737842541L;

	public EnvironmentContainer()
	{
		super();
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId(), String.class, null);
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId(), String.class, null);
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId(), String.class, null);
		setSampleEnvironments();
	}

	public EnvironmentContainer(CustomerEnvironment[] environments)
	{
		super();
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId(), String.class, null);
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId(), String.class, null);
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId(), EnvironmentState.class, null);
		addContainerProperty(EnvironmentItemProperty.ENVIRONMENT_USER.getPropertyId(), String.class, null);
		Item item;
		for (int i = 0; i < environments.length; i++)
		{
			item = addItem(i + 1);
			item.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).setValue(
					"" + environments[i].getTimeReferenceID());
			item.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).setValue(
					environments[i].getDescriptorName());
			item.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).setValue(
					environments[i].getEnvironmentState());
			item.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_USER.getPropertyId()).setValue(
					environments[i].getUser().toString());
		}
	}

	public void setSampleEnvironments()
	{
		Item item1 = addItem("1");
		item1.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).setValue("Env-1");
		item1.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).setValue("descriptor1");
		item1.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).setValue("status1");

		Item item2 = addItem("2");
		item2.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).setValue("Env-2");
		item2.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).setValue("descriptor2");
		item2.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).setValue("status2");

		Item item3 = addItem("3");
		item3.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).setValue("Env-3");
		item3.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).setValue("descriptor3");
		item3.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).setValue("status3");

		Item item4 = addItem("4");
		item4.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_ID.getPropertyId()).setValue("Env-4");
		item4.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_DESCRIPTOR.getPropertyId()).setValue("descriptor4");
		item4.getItemProperty(EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId()).setValue("status4");
	}
}
