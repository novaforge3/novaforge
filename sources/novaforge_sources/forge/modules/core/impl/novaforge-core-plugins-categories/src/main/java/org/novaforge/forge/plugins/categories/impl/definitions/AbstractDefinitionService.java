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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.Association;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.FieldDescription;
import org.novaforge.forge.core.plugins.categories.Parameter;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.FieldDescriptorBuilder;
import org.novaforge.forge.plugins.categories.impl.models.ParameterImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Guillaume Lamirand
 */
public abstract class AbstractDefinitionService implements CategoryDefinitionService
{
  /**
   * Allowed to log
   */
  private static final Log       log = LogFactory.getLog(AbstractDefinitionService.class);
  /**
   * Service injected by the container
   */
  private FieldDescriptorBuilder fieldDescriptorBuilder;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName(final Locale pLocale)
  {
    log.debug(String.format("Getting category's name for [local=%s]", pLocale));
    return getResourceBundle(pLocale).getString(CATEGORY_KEY);
  }

  /**
   * @param pLocale
   *          represensts a locale
   * @return the {@link ResourceBundle} found
   */
  protected abstract ResourceBundle getResourceBundle(Locale pLocale);

  /**
   * {@inheritDoc}
   */
  @Override
  public Association getAssociation(final AssociationType pType, final String pName)
  {
    Association returnAssociation = null;
    for (final Association asso : getAssociations())
    {
      if ((asso.getType().equals(pType)) && (asso.getName().equals(pName)))
      {
        returnAssociation = asso;
        break;
      }

    }
    return returnAssociation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Association> getCompatibleAssociations(final Association pAssociation)
  {
    return new ArrayList<Association>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Parameter getParamater(final Association pAssociation, final Locale pLocale)
  {
    final Class<?> clazz = getParameterClass(pAssociation);
    final List<FieldDescription> description = fieldDescriptorBuilder.buildFieldsDescription(getResourceBundle(pLocale),
                                                                                             clazz);
    return new ParameterImpl(clazz, description);
  }

  /**
   * @param pAssociation
   *          represents the association object
   * @return class of the parameter defined for the association
   */
  protected abstract Class<?> getParameterClass(final Association pAssociation);

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginRealm getRealm()
  {
    return PluginRealm.USER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDefaultRole() throws PluginServiceException
  {
    throw new PluginServiceException("This is not implemented for this category !");
  }

  /**
   * @return the fieldDescriptorBuilder
   */
  public FieldDescriptorBuilder getFieldDescriptorBuilder()
  {
    return fieldDescriptorBuilder;
  }

  /**
   * @param pFieldDescriptorBuilder
   *     the fieldDescriptorBuilder to set
   */
  public void setFieldDescriptorBuilder(final FieldDescriptorBuilder pFieldDescriptorBuilder)
  {
    fieldDescriptorBuilder = pFieldDescriptorBuilder;
  }

}
