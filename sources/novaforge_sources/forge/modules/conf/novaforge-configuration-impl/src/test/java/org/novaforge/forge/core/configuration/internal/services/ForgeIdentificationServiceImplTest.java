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
package org.novaforge.forge.core.configuration.internal.services;

import org.junit.Test;
import org.mockito.Matchers;
import org.novaforge.forge.core.configuration.dao.ForgeIdentificationDAO;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.model.ForgeIdentification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Guillaume Lamirand
 */
public class ForgeIdentificationServiceImplTest
{

  /**
   * UUID
   */
  private static final UUID ID = UUID.fromString("d5a992e0-fc2a-4f17-bfda-ef16e298af01");

  /**
   * Test method for {@link ForgeIdentificationServiceImpl#getForgeId()} when there is not one already
   * existing.
   * 
   * @throws DataAccessException
   * @throws ForgeConfigurationException
   */
  @Test
  public void testGetNewForgeId()
  {
    // Build mock objects
    final ForgeIdentificationServiceImpl identification = new ForgeIdentificationServiceImpl();
    // Mock for ForgeIdentification
    final ForgeIdentification id = mock(ForgeIdentification.class);
    when(id.getIdentifiant()).thenReturn(ID);
    // Mock for ForgeIdentificationDataMapper
    final ForgeIdentificationDAO idDAO = mock(ForgeIdentificationDAO.class);
    when(idDAO.existIdentifiant()).thenReturn(false);
    when(idDAO.create(Matchers.any(UUID.class))).thenReturn(id);
    identification.setForgeIdentificationDAO(idDAO);

    // Test method
    assertNotNull(identification.getForgeId());
    assertThat(identification.getForgeId(), is(ID));

  }

  /**
   * Test method for {@link ForgeIdentificationServiceImpl#getForgeId()} when there is one already
   * existing.
   * 
   * @throws DataAccessException
   * @throws ForgeConfigurationException
   */
  @Test
  public void testgetOldForgeId() throws ForgeConfigurationException
  {
    // Build mock objects
    final ForgeIdentificationServiceImpl identification = new ForgeIdentificationServiceImpl(); // Mock for

    // Mock for ForgeIdentification
    final List<ForgeIdentification> list = new ArrayList<ForgeIdentification>();
    final ForgeIdentification id = mock(ForgeIdentification.class);
    when(id.getIdentifiant()).thenReturn(ID);
    list.add(id);
    // Mock for ForgeIdentificationDataMapper
    final ForgeIdentificationDAO idDAO = mock(ForgeIdentificationDAO.class);
    when(idDAO.existIdentifiant()).thenReturn(true);
    when(idDAO.get()).thenReturn(id);
    identification.setForgeIdentificationDAO(idDAO);

    // Test method
    assertNotNull(identification.getForgeId());
    assertThat(identification.getForgeId(), is(ID));

  }

}
