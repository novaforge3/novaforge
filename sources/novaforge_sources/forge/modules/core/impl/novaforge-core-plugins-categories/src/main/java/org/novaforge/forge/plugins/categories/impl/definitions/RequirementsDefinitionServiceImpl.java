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
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsCategoryService;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsRequest;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsSend;
import org.novaforge.forge.core.plugins.categories.scm.SCMReply;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestManagementReceive;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestManagementReply;
import org.novaforge.forge.plugins.categories.impl.models.AssociationImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Guillaume Lamirand
 */
public class RequirementsDefinitionServiceImpl extends AbstractDefinitionService implements
    CategoryDefinitionService
{

  /**
   * The name of the property file used
   */
  private static final String                PROPERTY_FILE         = "categories.requirementsmanagement.definition";
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

    // Build requests associations
    for (final RequirementsRequest request : RequirementsRequest.values())
    {
      final Association assoc = new AssociationImpl(AssociationType.REQUEST, request.name(), getCategory());
      associations.add(assoc);
    }

    // Build replies associations
    for (final RequirementsSend send : RequirementsSend.values())
    {
      final Association assoc = new AssociationImpl(AssociationType.SEND, send.name(), getCategory());
      associations.add(assoc);

    }
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
    return Category.REQUIREMENTMANAGEMENT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends PluginCategoryService> getCategoryServiceInterface()
  {
    return RequirementsCategoryService.class;
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
  public List<Association> getAssociations()
  {
    return associations;
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
  public List<Association> getCompatibleAssociations(final Association pAssociation)
  {
    final List<Association> assoc = new ArrayList<Association>();
    if (getCategory().equals(pAssociation.getCategory()))
    {

      if (AssociationType.REQUEST.equals(pAssociation.getType()))
      {
        String name = null;
        Category category = null;
        if (RequirementsRequest.getRequirementsWithTestCoverage.name().equals(pAssociation.getName()))
        {
          name = TestManagementReply.getRequirementsCoverageByTest.name();
          category = Category.TESTMANAGEMENT;
        }
        else if (RequirementsRequest.searchRequirementInSourceCode.name().equals(pAssociation.getName()))
        {
          name = SCMReply.searchInSourceCode.name();
          category = Category.SCM;
        }

        if ((name != null) && (category != null))
        {
          assoc.add(new AssociationImpl(AssociationType.REPLY, name, category));
        }
      }
      else if (AssociationType.SEND.equals(pAssociation.getType()))
      {
        String name = null;
        Category category = null;
        if (RequirementsSend.updateRequirements.name().equals(pAssociation.getName()))
        {
          name = TestManagementReceive.synchronizeRequirements.name();
          category = Category.TESTMANAGEMENT;
        }
        if ((name != null) && (category != null))
        {
          assoc.add(new AssociationImpl(AssociationType.RECEIVE, name, category));
        }

      }
    }
    return assoc;
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

}
