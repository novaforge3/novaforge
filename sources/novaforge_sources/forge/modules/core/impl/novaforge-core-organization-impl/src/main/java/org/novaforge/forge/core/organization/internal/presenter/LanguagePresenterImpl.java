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
package org.novaforge.forge.core.organization.internal.presenter;

import org.novaforge.forge.core.organization.dao.LanguageDAO;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;
import org.novaforge.forge.core.organization.services.LanguageService;

import java.util.List;

/**
 * Implementation of {@link LanguagePresenter}
 * 
 * @author benoists
 * @see LanguageService
 */
public class LanguagePresenterImpl implements LanguagePresenter
{
  // FIXME We should manage some permissions here
  /**
   * Reference to {@link LanguageDAO} service injected by the container
   */
  private LanguageService languageService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Language newLanguage()
  {
    return languageService.newLanguage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Language getLanguage(final String pName) throws LanguageServiceException
  {
    return languageService.getLanguage(pName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Language> getAllLanguages() throws LanguageServiceException
  {
    return languageService.getAllLanguages();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createLanguage(final Language pLanguage) throws LanguageServiceException
  {
    languageService.createLanguage(pLanguage);
  }

  /**
   * Use by container to inject {@link LanguageService} implementation
   * 
   * @param pLanguageService
   *          the languageService to set
   */
  public void setLanguageService(final LanguageService pLanguageService)
  {
    languageService = pLanguageService;
  }

}
