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
package org.novaforge.forge.plugins.categories.impl.definitions;

import org.novaforge.forge.core.plugins.categories.Association;
import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListCategoryService;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Guillaume Lamirand
 */
public class MailingListDefinitionServiceImpl extends AbstractDefinitionService implements
    CategoryDefinitionService
{

  /**
   * The name of the property file used
   */
  private static final String                PROPERTY_FILE         = "categories.mailinglist.definition";
  /**
   * The default role use for system management
   */
  private static final String                DEFAULT_ROLE          = "owner/subscriber";
  /**
   * Contains the list of associations available
   */
  private final        List<Association>     associations          = new ArrayList<Association>();
  /**
   * Map each action and its class paramater
   */
  private final        Map<String, Class<?>> actionParameter       = new HashMap<String, Class<?>>();
  /**
   * Map each notification and its class paramater
   */
  private final        Map<String, Class<?>> notificationParameter = new HashMap<String, Class<?>>();

  /**
   * Init method used by container to initialize this bean
   */
  public void start()
  {
    buildAssociations();
    setParameters();
  }

  /**
   * Build assocations list
   */
  private void buildAssociations()
  {
    // Cleanup list
    associations.clear();

  }

  /**
   * Build parameters
   */
  private void setParameters()
  {
    // Set action parameter
    actionParameter.clear();

    // Set notification parameter
    notificationParameter.clear();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Category getCategory()
  {
    return Category.MAILINGLIST;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ResourceBundle getResourceBundle(final Locale pLocale)
  {
    return ResourceBundle.getBundle(PROPERTY_FILE, pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends PluginCategoryService> getCategoryServiceInterface()
  {
    return MailingListCategoryService.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Association> getAssociations()
  {
    return associations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName(final Locale pLocale)
  {
    return getResourceBundle(pLocale).getString(CATEGORY_KEY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Class<?> getParameterClass(final Association pAssociation)
  {
    Class<?> returnClazz = null;
    switch (pAssociation.getType())
    {
      case ACTION:
        returnClazz = actionParameter.get(pAssociation.getName());
        break;
      case NOTIFICATION:
        returnClazz = notificationParameter.get(pAssociation.getName());
        break;
      default:
        throw new IllegalArgumentException(String.format("The association given is not supported [type=%s]",
                                                         pAssociation.getType()));
    }
    return returnClazz;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginRealm getRealm()
  {
    return PluginRealm.SYSTEM;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultRole() throws PluginServiceException
  {
    return DEFAULT_ROLE;
  }

}
