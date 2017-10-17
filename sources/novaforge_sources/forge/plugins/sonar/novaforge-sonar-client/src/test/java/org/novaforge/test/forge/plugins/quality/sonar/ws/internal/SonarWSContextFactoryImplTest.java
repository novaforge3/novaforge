package org.novaforge.test.forge.plugins.quality.sonar.ws.internal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.novaforge.forge.plugins.quality.sonar.SonarDataTest;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContextFactory;
import org.novaforge.forge.plugins.quality.sonar.ws.internal.SonarWSContextFactoryImpl;

public class SonarWSContextFactoryImplTest {

	@Test
	public void testGetWSContext() {


		SonarWSContextFactory factory = new SonarWSContextFactoryImpl();
		
		SonarWSContext context = factory.getWSContext(SonarDataTest.BASE_URL, SonarDataTest.ADMIN_USER, SonarDataTest.ADMIN_PASWORD);
		
		assertNotNull(context);
		assertTrue(SonarDataTest.BASE_URL.equals(context.getBaseURL()));
	}

}
