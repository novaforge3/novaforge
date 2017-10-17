package org.novaforge.forge.ui.novadeploy.internal.client.containers;

/**
* @author Vincent Chenal
*/

public enum EnvironmentItemProperty
{
	ENVIRONMENT_ID {

		@Override
		public String getPropertyId()
		{
			return "environmentId";
		}
	},
	ENVIRONMENT_STATE {

		@Override
		public String getPropertyId()
		{
			return "environmentState";
		}

	},
	ENVIRONMENT_USER {

		@Override
		public String getPropertyId()
		{
			return "environmentUser";
		}

	},
	ENVIRONMENT_DESCRIPTOR {

		@Override
		public String getPropertyId()
		{
			return "environmentDescriptor";
		}

	};

	public abstract String getPropertyId();
}
