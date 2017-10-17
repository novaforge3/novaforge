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
package org.novaforge.forge.commons.technical.historization.internal.services;

import org.junit.Test;
import org.novaforge.forge.commons.technical.historization.exceptions.HistorizationException;
import org.novaforge.forge.commons.technical.historization.internal.services.mock.ComplexObject;
import org.novaforge.forge.commons.technical.historization.internal.services.mock.UserHistorizable;
import org.novaforge.forge.commons.technical.historization.internal.services.mock.UserHistorizableImpl;
import org.novaforge.forge.commons.technical.historization.model.HistorizableObject;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Guillaume Lamirand
 */
public class HistorizableObjectImplTest
{

  private static final String MY_LABEL = "my label";
  private static final String MY_OBJ   = "myObj";

  /**
   * Test method for
   * {@link org.novaforge.forge.commons.technical.historization.internal.services.HistorizableObjectImpl#HistorizableObjectImpl(java.lang.String, java.lang.Object)}
   * .
   * 
   * @throws HistorizationException
   */
  @Test
  public final void testHistorizableObjectImpl() throws HistorizationException
  {
    final HistorizableObject obj = new HistorizableObjectImpl(MY_LABEL, MY_OBJ);
    assertNotNull(obj);
    assertNotNull(obj.getLabel());
    assertThat(obj.getLabel(), is(MY_LABEL));
    assertNotNull(obj.getDetails());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.commons.technical.historization.internal.services.HistorizableObjectImpl#getLabel()}
   * .
   */
  @Test
  public final void testGetLabel()
  {
    final HistorizableObject obj = new HistorizableObjectImpl(MY_LABEL, MY_OBJ);
    assertNotNull(obj);
    assertNotNull(obj.getLabel());
    assertThat(obj.getLabel(), is(MY_LABEL));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.commons.technical.historization.internal.services.HistorizableObjectImpl#getDetails()}
   * .
   * 
   * @throws HistorizationException
   */
  @Test
  public final void testGetDetails() throws HistorizationException
  {
    // Simple details
    final HistorizableObject obj = new HistorizableObjectImpl(MY_LABEL, MY_OBJ);
    assertNotNull(obj);
    assertNotNull(obj.getDetails());
    assertThat(obj.getDetails(), is("my label=myObj"));
    // List simple details
    final List<String> listString = new ArrayList<String>();
    listString.add("1");
    listString.add("2");
    listString.add("3");
    listString.add("4");
    final HistorizableObject obj3 = new HistorizableObjectImpl(MY_LABEL, listString);
    assertNotNull(obj3);
    assertNotNull(obj3.getDetails());
    assertThat(obj3.getDetails(), is("my label: [1, 2, 3, 4]"));

    // object details
    final HistorizableObject obj2 = new HistorizableObjectImpl(MY_LABEL, new UserHistorizableImpl("myLogin",
        "myName"));
    assertNotNull(obj2);
    assertNotNull(obj2.getDetails());
    assertThat(obj2.getDetails(), is("my label: [name=myName, login=myLogin]"));

    // object details
    final List<UserHistorizable> listUsers = new ArrayList<UserHistorizable>();
    listUsers.add(new UserHistorizableImpl("myLogin1", "myName1"));
    listUsers.add(new UserHistorizableImpl("myLogin2", "myName2"));
    listUsers.add(new UserHistorizableImpl("myLogin3", "myName3"));
    listUsers.add(new UserHistorizableImpl("myLogin4", "myName4"));
    final HistorizableObject obj4 = new HistorizableObjectImpl(MY_LABEL, listUsers);
    assertNotNull(obj4);
    assertNotNull(obj4.getDetails());
    assertThat(
        obj4.getDetails(),
        is("my label: [[name=myName1, login=myLogin1], [name=myName2, login=myLogin2], [name=myName3, login=myLogin3], [name=myName4, login=myLogin4]]"));

    // complex details
    final List<String> listString2 = new ArrayList<String>();
    listString2.add("1");
    listString2.add("2");
    listString2.add("3");
    listString2.add("4");
    final ComplexObject cmplx = new ComplexObject(new UserHistorizableImpl("myLogin1", "myName1"),
        listString2);
    final HistorizableObject obj5 = new HistorizableObjectImpl(MY_LABEL, cmplx);
    assertNotNull(obj5);
    assertNotNull(obj5.getDetails());
    assertTrue(obj5.getDetails().contains("my label:"));
    assertTrue(obj5.getDetails().contains("strings: [1, 2, 3, 4]"));
    assertTrue(obj5.getDetails().contains("user: [name=myName1, login=myLogin1]"));
  }
}
