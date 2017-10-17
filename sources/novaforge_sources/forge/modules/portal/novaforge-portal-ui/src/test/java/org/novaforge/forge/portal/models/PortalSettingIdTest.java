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
public class PortalSettingIdTest
{

  @Test
  public void testGetId() throws Exception
  {

    for (PortalSettingId settingIdTest : PortalSettingId.values())
    {
      assertThat(settingIdTest.getId(), notNullValue());
      assertThat(settingIdTest.getId(), is(not("")));
    }
  }

  @Test
  public void testGetDefaultValue() throws Exception
  {

    for (PortalSettingId settingIdTest : PortalSettingId.values())
    {
      assertThat(settingIdTest.getDefaultValue(), notNullValue());
      assertThat(settingIdTest.getDefaultValue(), is(not("")));
    }
  }

  @Test
  public void testIsExist() throws Exception
  {

    boolean notExistNull = PortalSettingId.isExist(null);
    assertThat(notExistNull, is(false));
    boolean notExistEmpty = PortalSettingId.isExist("");
    assertThat(notExistEmpty, is(false));
    boolean notExistFalse = PortalSettingId.isExist("noexist");
    assertThat(notExistFalse, is(false));

    for (PortalSettingId portalSettingId : PortalSettingId.values())
    {
      boolean exist = PortalSettingId.isExist(portalSettingId.getId());
      assertThat(exist, is(true));

    }
  }

  @Test
  public void testGetFromId() throws Exception
  {

    PortalSettingId moduleIdNull = PortalSettingId.getFromId(null);
    assertThat(moduleIdNull, nullValue());
    PortalSettingId moduleIdEmpty = PortalSettingId.getFromId("");
    assertThat(moduleIdEmpty, nullValue());
    PortalSettingId moduleIdNotExist = PortalSettingId.getFromId("noexist");
    assertThat(moduleIdNotExist, nullValue());

    for (PortalSettingId portalSettingId : PortalSettingId.values())
    {
      PortalSettingId moduleIdExist = PortalSettingId.getFromId(portalSettingId.getId());
      assertThat(moduleIdExist, is(portalSettingId));

    }
  }
}