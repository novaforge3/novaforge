package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import org.junit.Before;

public class LocalNexusTest {
	
	protected final static String ADMIN_USER = "admin";
	protected final static String ADMIN_USER_PASSWORD = "admin123";
	protected final static String NEXUS_SCRIPT_URL_BASE = "http://localhost:8081/service/siesta/rest/v1/";
	
	protected boolean nexusProfileActivated = false;
	
	@Before
	public void setUp(){
		
		nexusProfileActivated = "true".equals(
				System.getProperty("nexus.profile"));

	}

}
