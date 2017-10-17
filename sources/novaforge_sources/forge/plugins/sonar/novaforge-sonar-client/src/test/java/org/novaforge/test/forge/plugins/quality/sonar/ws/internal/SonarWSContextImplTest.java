package org.novaforge.test.forge.plugins.quality.sonar.ws.internal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.plugins.quality.sonar.SonarDataTest;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.internal.SonarWSContextImpl;

public class SonarWSContextImplTest {
	
	
	private SonarWSContext sonarWSContext = null;

	
	@Before
	public void init(){
		
		this.sonarWSContext = new SonarWSContextImpl(SonarDataTest.BASE_URL, SonarDataTest.ADMIN_USER, SonarDataTest.ADMIN_PASWORD);

	}
	@Test
	public void testSonarWSContextImpl() {
			
		assertNotNull(this.sonarWSContext);
		
		assertNotNull(this.sonarWSContext.getBaseURL());
		assertNotNull(this.sonarWSContext.getSonarAdminLogin());
		assertNotNull(this.sonarWSContext.getSonarAdminPassword());
	}

	@Test
	public void testGetBaseURL() {

		assertTrue(this.sonarWSContext.getBaseURL().equals(SonarDataTest.BASE_URL));
	}

	@Test
	public void testGetSonarAdminLogin() {
		
		assertTrue(this.sonarWSContext.getSonarAdminLogin().equals(SonarDataTest.ADMIN_USER));
	}

	@Test
	public void testGetSonarAdminPassword() {

		assertTrue(this.sonarWSContext.getSonarAdminPassword().equals(SonarDataTest.ADMIN_PASWORD));
	}

}
