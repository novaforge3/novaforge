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
package org.novaforge.forge.dashboard.internal.service;

import org.junit.Test;
import org.novaforge.forge.dashboard.service.DataSourceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class DataSourceFactoryImplTest
{

  @Test
  public void testBuildDataSource()
  {
    final DataSourceFactory factory = new DataSourceFactoryImpl();
    final Map<String, List<String>> apps = new HashMap<String, List<String>>();
    apps.put("string", new ArrayList<String>());
    final List<String> arrayList = new ArrayList<String>();
    arrayList.add("test");
    arrayList.add("test2");
    apps.put("string2", arrayList);
    final String buildDataSource = factory.buildDataSource(apps);
    assertThat(buildDataSource, is("{\"string2\":[\"test\",\"test2\"],\"string\":[]}"));
  }

  @Test
  public void testReadDataSource()
  {
    final DataSourceFactory factory = new DataSourceFactoryImpl();
    final Map<String, List<String>> buildDataSource = factory
        .readDataSource("{\"string2\":[\"test\",\"test2\"],\"string\":[]}");
    assertThat(buildDataSource.size(), is(2));
    assertThat(buildDataSource.get("string2").size(), is(2));
    assertThat(buildDataSource.get("string").size(), is(0));
  }
}
