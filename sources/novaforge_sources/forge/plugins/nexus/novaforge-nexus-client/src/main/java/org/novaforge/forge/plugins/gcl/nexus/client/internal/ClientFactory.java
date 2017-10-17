package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class ClientFactory {

	public static Client getClient(String userName, String password){
		
		return ClientBuilder.newClient().register(JacksonJsonProvider.class).register(new Authenticator(userName, password))
				.register(NexusObjectMapperProvider.class);
	}
}
