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
package org.novaforge.forge.core.organization.internal.services;

import org.novaforge.forge.core.organization.dao.LanguageDAO;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.services.LanguageService;

import java.util.List;
import java.util.Locale;

/**
 * Implementation of {@link LanguageService}
 * 
 * @author benoists
 * @see LanguageService
 */
public class LanguageServiceImpl implements LanguageService
{

  /**
   * Reference to {@link LanguageDAO} service injected by the container
   */
  private LanguageDAO languageDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public Language newLanguage()
  {
    return languageDAO.newLanguage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Language getLanguage(final String pName) throws LanguageServiceException
  {
    try
    {
      return languageDAO.findByName(pName);
    }
    catch (final Exception e)
    {
      throw new LanguageServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Language> getAllLanguages() throws LanguageServiceException
  {
    try
    {
      return languageDAO.findAll();
    }
    catch (final Exception e)
    {
      throw new LanguageServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createLanguage(final Language pLanguage) throws LanguageServiceException
  {
    try
    {
      languageDAO.persist(pLanguage);
    }
    catch (final Exception e)
    {
      throw new LanguageServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Language getLanguageFromLocale(Locale pLocale) throws LanguageServiceException
  {
    if (pLocale == null)
    {
      throw new LanguageServiceException("locale can not be null");
    }

    return languageDAO.findByName(pLocale.getLanguage());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Language getDefault()
  {
    return languageDAO.findDefault();
  }

  /**
   * Use by container to inject {@link LanguageDAO} implementation
   *
   * @param pLanguageDAO
   *     the languageDAO to set
   */
  public void setLanguageDAO(final LanguageDAO pLanguageDAO)
  {
    languageDAO = pLanguageDAO;
  }
}
