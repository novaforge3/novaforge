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
package org.novaforge.forge.configuration.initialization.internal.creator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.configuration.initialization.exceptions.ForgeInitializationException;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;

/**
 * @author Guillaume Lamirand
 */
public class LanguageCreator
{

  private static final Log    LOGGER           = LogFactory.getLog(LanguageCreator.class);
  private static final String LANGUAGE_FR_NAME = "FR";
  private static final String LANGUAGE_EN_NAME = "EN";
  private LanguagePresenter   languagePresenter;

  public void createLanguages() throws ForgeInitializationException
  {
    LOGGER.info("Default languages are being created.");
    createLanguage(LANGUAGE_EN_NAME, true);
    createLanguage(LANGUAGE_FR_NAME, false);

  }

  private void createLanguage(final String pLanguageName, final boolean pIsDefault)
      throws ForgeInitializationException
  {
    try
    {
      final Language language = languagePresenter.getLanguage(pLanguageName);
      if (language != null)
      {
        LOGGER.warn(String.format("Language with [name=%s] is already existing.", pLanguageName));
      }
    }
    catch (final LanguageServiceException e1)
    {
      final Language lang = languagePresenter.newLanguage();
      lang.setName(pLanguageName);
      lang.setIsDefault(pIsDefault);
      try
      {
        languagePresenter.createLanguage(lang);
      }
      catch (final LanguageServiceException e)
      {
        throw new ForgeInitializationException(String.format("Unable to create language with [name=%s]",
            pLanguageName), e);
      }
      LOGGER.info(String.format("Language created with [name=%s]", pLanguageName));
    }
  }

  public void setLanguagePresenter(final LanguagePresenter pLanguagePresenter)
  {
    languagePresenter = pLanguagePresenter;
  }
}
