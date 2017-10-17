/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.studio.core.project;

import org.junit.Assert;
import org.junit.Test;
import org.novaforge.studio.core.repository.RepositoryUtil;

public class RepositoryUtilTest {
	
	@Test
	public void getBaseToolUrl(){
		
		String toolUrl = "https://vm-infra-12/mantis/login.php?instance_id=4c9a2447-9b6e-48b3-a975-9633adc25ff7";
		String baseUrl = "https://vm-infra-12/mantis";
		String url = RepositoryUtil.getBaseToolUrl(toolUrl);
		Assert.assertEquals(baseUrl, url);
	}
	
	@Test
	public void getEmptyBaseToolUrl(){
		
		String toolUrl = "";
		String baseUrl = "";
		String url = RepositoryUtil.getBaseToolUrl(toolUrl);
		Assert.assertEquals(baseUrl, url);
	}

}
