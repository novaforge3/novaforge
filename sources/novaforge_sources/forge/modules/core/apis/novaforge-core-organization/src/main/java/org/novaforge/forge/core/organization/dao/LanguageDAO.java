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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Language;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to {@link Language} data from persistence
 * 
 * @author sbenoist
 * @see Language
 */
public interface LanguageDAO
{
  /**
   * Find {@link Language} according to its name
   * 
   * @param pName
   *          represents the language name to find
   * @return {@link Language} found
   * @throws NoResultException
   *           thrown if no {@link Language} are existing for the name given
   */
  Language findByName(final String pName) throws NoResultException;

  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link Language}
   */
  Language newLanguage();

  /**
   * This method will persist the object given in parameter
   * 
   * @param pLanguage
   *          the language to persist
   * @return {@link Language} attached to persistence context
   */
  Language persist(Language pLanguage);

  /**
   * This method will try all {@link Language} stored
   * 
   * @return the whole {@link List} of {@link Language} stored
   */
  List<Language> findAll();

  /**
   * This method will return the default language
   * 
   * @return the default {@link Language} stored
   */
  Language findDefault();
}
