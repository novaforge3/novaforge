/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.distribution.reference.internal.service;

import org.junit.Before;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;

/**
 * salvat-a
 */
public abstract class AbstractDistributionTest
{
	protected ForgeDTO forgeDTO;

	@Before
	public void setUpTest()
	{
		forgeDTO = new ForgeDTO();
		forgeDTO.setLabel("rootForge");

		final ForgeDTO child1 = new ForgeDTO();
		child1.setLabel("child1");
		forgeDTO.addChild(child1);

		final ForgeDTO child2 = new ForgeDTO();
		child2.setLabel("child2");
		forgeDTO.addChild(child2);

		final ForgeDTO child11 = new ForgeDTO();
		child11.setLabel("child11");
		child1.addChild(child11);

		final ForgeDTO child12 = new ForgeDTO();
		child12.setLabel("child12");
		child1.addChild(child12);

		final ForgeDTO child13 = new ForgeDTO();
		child13.setLabel("child13");
		child1.addChild(child13);

		final ForgeDTO child121 = new ForgeDTO();
		child121.setLabel("child121");
		child12.addChild(child121);

		final ForgeDTO child122 = new ForgeDTO();
		child122.setLabel("child122");
		child12.addChild(child122);
	}
}
