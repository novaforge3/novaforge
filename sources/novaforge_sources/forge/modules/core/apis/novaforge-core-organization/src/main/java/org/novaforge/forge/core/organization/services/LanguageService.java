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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;

import java.util.List;
import java.util.Locale;

/**
 * @author petrettof
 */
public interface LanguageService
{
  /**
   * This method returns an instance of Language
   * 
   * @return Language
   */
  Language newLanguage();

  /**
   * This method returns the language with the name in argument
   * 
   * @param name
   *          The language name
   * @return The Language
   * @throws LanguageServiceException
   */
  Language getLanguage(String name) throws LanguageServiceException;

  /**
   * This method returns the list of all languages
   * 
   * @return The list of Language
   * @throws LanguageServiceException
   */
  List<Language> getAllLanguages() throws LanguageServiceException;

  /**
   * This method allows to create a language
   * 
   * @param language
   * @throws LanguageServiceException
   */
  void createLanguage(Language language) throws LanguageServiceException;

  /**
   * This method return the Language for a given locale
   * 
   * @param pLocale
   * @throws LanguageServiceException
   */
  Language getLanguageFromLocale(Locale pLocale) throws LanguageServiceException;

  /**
   * This method return the default Language
   */
  Language getDefault();
}
