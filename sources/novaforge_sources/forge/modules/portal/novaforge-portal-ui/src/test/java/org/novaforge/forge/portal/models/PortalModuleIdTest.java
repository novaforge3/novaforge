/*
 * Copyright (c) 2011- 2015, BULL SAS, NovaForge Version 3 and above.
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

package org.novaforge.forge.portal.models;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class PortalModuleIdTest
{

  @Test
  public void testGetId() throws Exception
  {
    for (PortalModuleId portalModuleId : PortalModuleId.values())
    {
      assertThat(portalModuleId.getId(), notNullValue());
      assertThat(portalModuleId.getId(), is(not("")));
    }

  }

  @Test
  public void testIsExist() throws Exception
  {
    boolean notExistNull = PortalModuleId.isExist(null);
    assertThat(notExistNull, is(false));
    boolean notExistEmpty = PortalModuleId.isExist("");
    assertThat(notExistEmpty, is(false));
    boolean notExistFalse = PortalModuleId.isExist("noexist");
    assertThat(notExistFalse, is(false));

    for (PortalModuleId portalModuleId : PortalModuleId.values())
    {
      boolean exist = PortalModuleId.isExist(portalModuleId.getId());
      assertThat(exist, is(true));

    }

  }

  @Test
  public void testGetFromId() throws Exception
  {

    PortalModuleId moduleIdNull = PortalModuleId.getFromId(null);
    assertThat(moduleIdNull, nullValue());
    PortalModuleId moduleIdEmpty = PortalModuleId.getFromId("");
    assertThat(moduleIdEmpty, nullValue());
    PortalModuleId moduleIdNotExist = PortalModuleId.getFromId("noexist");
    assertThat(moduleIdNotExist, nullValue());

    for (PortalModuleId portalModuleId : PortalModuleId.values())
    {
      PortalModuleId moduleIdExist = PortalModuleId.getFromId(portalModuleId.getId());
      assertThat(moduleIdExist, is(portalModuleId));

    }
  }
}