package org.novaforge.forge.test.plugins.quality.sonar.ws.models;

import static org.junit.Assert.*;

import org.junit.Test;
import org.novaforge.forge.plugins.quality.sonar.ws.models.SonarRole;

public class SonarRoleTest {

	@Test
	public void testFromLabel() {

		assertTrue(SonarRole.fromLabel("Code viewers") == SonarRole.CODE_VIEWERS);
	}

	@Test
	public void testFromId() {

		assertTrue(SonarRole.fromId("codeviewer") == SonarRole.CODE_VIEWERS);
	}

	@Test
	public void testGetLabel() {

		assertTrue(SonarRole.CODE_VIEWERS.getLabel().equals("Code viewers"));
	}

	@Test
	public void testGetId() {

		assertTrue(SonarRole.CODE_VIEWERS.getId().equals("codeviewer"));
	}

}
