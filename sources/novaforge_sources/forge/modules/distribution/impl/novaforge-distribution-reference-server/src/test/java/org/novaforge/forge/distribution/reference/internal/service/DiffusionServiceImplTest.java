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

import org.junit.Test;
import org.mockito.Mockito;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;

import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * @author rols-p
 */
public class DiffusionServiceImplTest extends AbstractDistributionTest
{
  @Test
  public void getAllForgeChildrenTree() throws Exception
  {
    final DiffusionServiceImpl svc = new DiffusionServiceImpl();

    final ForgeDistributionService distribClient = Mockito.mock(ForgeDistributionService.class);
    Mockito.when(distribClient.getForge("rootForgeId")).thenReturn(forgeDTO);
    svc.setForgeDistributionService(distribClient);

    final Set<ForgeDTO> allChildrenForges = svc.getAllChildrenForgesTree("rootForgeId");
    assertEquals(7, allChildrenForges.size());
  }

  @Test
  public void getAllForgeTree() throws Exception
  {
    final DiffusionServiceImpl svc = new DiffusionServiceImpl();

    final ForgeDistributionService distribClient = Mockito.mock(ForgeDistributionService.class);
    Mockito.when(distribClient.getForge("rootForgeId")).thenReturn(forgeDTO);
    svc.setForgeDistributionService(distribClient);

    final Set<ForgeDTO> allForges = svc.getAllForgesTree("rootForgeId");
    assertEquals(8, allForges.size());
  }
}
