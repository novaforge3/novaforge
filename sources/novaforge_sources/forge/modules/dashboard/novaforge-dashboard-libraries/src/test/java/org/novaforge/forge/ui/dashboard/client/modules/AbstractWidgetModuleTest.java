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
package org.novaforge.forge.ui.dashboard.client.modules;

import org.junit.Test;
import org.novaforge.forge.dashboard.model.WidgetModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Guillaume Lamirand
 */
public class AbstractWidgetModuleTest
{

  /**
   * 
   */
  @Test
  public void testIsValidDataSource()
  {
    final Map<String, List<String>> dataSource = new HashMap<String, List<String>>();
    final List<String> apps = new ArrayList<String>();

    // Need nothing
    WidgetModule module = new TestWidgetModule(false, false, false, false);
    assertTrue(module.isValidDataSource(null));
    assertTrue(module.isValidDataSource(new HashMap<String, List<String>>()));
    dataSource.clear();
    dataSource.put("project1", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", apps);
    assertFalse(module.isValidDataSource(dataSource));

    // Need one project
    module = new TestWidgetModule(true, false, false, false);
    dataSource.clear();
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", null);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));

    // Need many project
    module = new TestWidgetModule(true, true, false, false);
    dataSource.clear();
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", null);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project2", null);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project2", apps);
    assertTrue(module.isValidDataSource(dataSource));

    // Need one project and one application
    module = new TestWidgetModule(true, false, true, false);
    dataSource.clear();
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));
    apps.add("app1");
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    apps.add("app2");
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", apps);
    assertFalse(module.isValidDataSource(dataSource));

    // Need one project and many application
    module = new TestWidgetModule(true, false, true, true);
    dataSource.clear();
    apps.clear();
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));
    apps.add("app1");
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    apps.add("app2");
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project2", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", apps);
    assertFalse(module.isValidDataSource(dataSource));

    // Need many projects and one application
    module = new TestWidgetModule(true, true, true, false);
    dataSource.clear();
    apps.clear();
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));
    apps.add("app1");
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project2", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", apps);
    assertTrue(module.isValidDataSource(dataSource));
    apps.add("app2");
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));

    // Need one project and many application
    module = new TestWidgetModule(true, true, true, true);
    dataSource.clear();
    apps.clear();
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project1", apps);
    assertFalse(module.isValidDataSource(dataSource));
    apps.add("app1");
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    apps.add("app2");
    dataSource.put("project1", apps);
    assertTrue(module.isValidDataSource(dataSource));
    dataSource.put("project2", null);
    assertFalse(module.isValidDataSource(dataSource));
    dataSource.put("project2", apps);
    assertTrue(module.isValidDataSource(dataSource));
  }
}
