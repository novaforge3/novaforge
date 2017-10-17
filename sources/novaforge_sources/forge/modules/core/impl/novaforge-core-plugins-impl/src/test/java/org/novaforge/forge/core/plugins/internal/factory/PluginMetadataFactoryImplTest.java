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
package org.novaforge.forge.core.plugins.internal.factory;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceQueues;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.domain.route.PluginQueues;
import org.novaforge.forge.core.plugins.entity.PluginMetadataEntity;
import org.novaforge.forge.core.plugins.entity.PluginQueuesEntity;
import org.novaforge.forge.core.plugins.services.PluginMetadataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Lamirand
 */
public class PluginMetadataFactoryImplTest
{
  /**
   * Constants for persistence
   */
  private static final UUID         ID                 = java.util.UUID.randomUUID();
  private static final String       DESCRIPTION        = "Plugin created in test unit";
  private static final String       TYPE               = "jUnit";
  private static final String       CATEGORY           = "TU";
  private static final String       VERSION            = "1";
  private static final PluginStatus STATUS             = PluginStatus.INSTALLED;
  private static final boolean      AVAILABLE          = true;
  private static final String       MEMBERSHIP_QUEUE   = "membership";
  private static final String       PROJECT_QUEUE      = "project";
  private static final String       ROLESMAPPING_QUEUE = "rolesmapping";
  private static final String       USER_QUEUE         = "user";
  /**
   * Constants for metadata
   */
  private static final UUID         NEWID              = java.util.UUID.randomUUID();
  private static final String       NEWDESC            = "Plugin updated in test unit";
  private static final String       NEWTYPE            = "new jUnit";
  private static final String       NEWCAT             = "new TU";
  private static final String       NEWVERSION         = "2";
  private static final PluginStatus NEWSTATUS          = PluginStatus.ACTIVATED;
  private static final boolean      NEWAVAI            = true;
  private static final String       NEWMEMBERSHIP      = "newMmembership";
  private static final String       NEWPROJECT         = "newProject";
  private static final String       NEWROLES           = "newRolesmapping";
  private static final String       NEWUSER            = "newUser";
  private PluginPersistenceMetadata pluginPersistence;
  private PluginPersistenceQueues   persistenceQueues;
  private PluginQueues              pluginQueues;
  private PluginMetadata            pluginServiceMetadata;
  private PluginMetadataFactory     pluginMetadataFactory;

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    pluginMetadataFactory = new PluginMetadataFactoryImpl();

    persistenceQueues = new PluginQueuesEntity();
    persistenceQueues.setProjectQueue(PROJECT_QUEUE);
    persistenceQueues.setMembershipQueue(MEMBERSHIP_QUEUE);
    persistenceQueues.setRolesMappingQueue(ROLESMAPPING_QUEUE);
    persistenceQueues.setUserQueue(USER_QUEUE);

    pluginPersistence = new PluginMetadataEntity();
    pluginPersistence.setUUID(ID);
    pluginPersistence.setDescription(DESCRIPTION);
    pluginPersistence.setCategory(CATEGORY);
    pluginPersistence.setType(TYPE);
    pluginPersistence.setVersion(VERSION);
    pluginPersistence.setStatus(STATUS);
    pluginPersistence.setJMSQueues(persistenceQueues);
    pluginPersistence.setAvailable(AVAILABLE);

    pluginQueues = new PluginQueuesImpl(NEWPROJECT, NEWMEMBERSHIP, NEWUSER, NEWROLES);
    pluginServiceMetadata = new PluginMetadataImpl(NEWID, NEWDESC, NEWTYPE, NEWCAT, pluginQueues,
        new ArrayList<PluginView>(), NEWSTATUS, NEWAVAI, NEWVERSION);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.factory.PluginMetadataFactoryImpl#createPluginMetadata(org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata)}
   * .
   */
  @Test
  public final void testCreatePluginMetadataPluginServiceMetadata()
  {

    final PluginMetadata pluginMetadata = pluginMetadataFactory.createPluginMetadata(pluginServiceMetadata);
    assertThat(pluginMetadata, notNullValue());
    assertThat(pluginMetadata.getUUID(), is(NEWID.toString()));
    assertThat(pluginMetadata.getDescription(), is(NEWDESC));
    assertThat(pluginMetadata.getType(), is(NEWTYPE));
    assertThat(pluginMetadata.getCategory(), is(NEWCAT));
    // The following hasn't change, so default value
    assertThat(pluginMetadata.getStatus(), is(PluginStatus.INSTALLED));
    assertThat(pluginMetadata.isAvailable(), is(false));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.factory.PluginMetadataFactoryImpl#updatePluginPersistenceMetadata(org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata, org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata)}
   * .
   */
  @Test
  public final void testUpdatePluginPersistenceMetadata()
  {
    final PluginPersistenceMetadata pluginMetadata = pluginMetadataFactory.updatePluginPersistenceMetadata(
        pluginPersistence, pluginServiceMetadata);

    assertThat(pluginMetadata, notNullValue());
    assertThat(pluginMetadata.getUUID(), is(NEWID));
    assertThat(pluginMetadata.getDescription(), is(NEWDESC));
    assertThat(pluginMetadata.getType(), is(NEWTYPE));
    assertThat(pluginMetadata.getCategory(), is(NEWCAT));
    // The following hasn't change
    assertThat(pluginMetadata.getStatus(), is(STATUS));
    assertThat(pluginMetadata.isAvailable(), is(AVAILABLE));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.factory.PluginMetadataFactoryImpl#createPluginMetadata(org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata)}
   * .
   */
  @Test
  public final void testCreatePluginMetadataPluginPersistenceMetadata()
  {
    final PluginMetadata pluginMetadata = pluginMetadataFactory.createPluginMetadata(pluginPersistence);

    assertThat(pluginMetadata, notNullValue());
    assertThat(pluginMetadata.getUUID(), is(ID.toString()));
    assertThat(pluginMetadata.getDescription(), is(DESCRIPTION));
    assertThat(pluginMetadata.getType(), is(TYPE));
    assertThat(pluginMetadata.getCategory(), is(CATEGORY));
    assertThat(pluginMetadata.getStatus(), is(STATUS));
    assertThat(pluginMetadata.isAvailable(), is(AVAILABLE));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.plugins.internal.factory.PluginMetadataFactoryImpl#createPluginMetadataList(java.util.List)}
   * .
   */
  @Test
  public final void testCreatePluginMetadataList()
  {
    final List<PluginPersistenceMetadata> list = new ArrayList<PluginPersistenceMetadata>();
    list.add(pluginPersistence);
    final PluginPersistenceMetadata pluginPersistence2 = pluginPersistence;
    pluginPersistence2.setUUID(UUID.randomUUID());
    list.add(pluginPersistence2);

    final List<PluginMetadata> pluginMetadata = pluginMetadataFactory.createPluginMetadataList(list);

    assertThat(pluginMetadata, notNullValue());
    assertThat(pluginMetadata.size(), is(2));
    for (final PluginMetadata plugin : pluginMetadata)
    {
      assertThat(plugin, is(new IsInstanceOf(PluginMetadata.class)));
    }
  }

}
