/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.services.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * @author Marc Blachon
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class LanguageServiceTest extends BaseUtil
{
  @Inject
  private LanguageService     languageService;

  private static final String LANGUAGE_FR_NAME = "FR";
  private static final String LANGUAGE_IT_NAME = "it";
  private static final String LANGUAGE_GR_NAME = "gr";

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.LanguageServiceImpl#createLanguage(org.novaforge.forge.core.organization.model.Language)}
   * .
   */
  @Test
  // @Ignore
  public void testCreateLanguage() throws Exception
  {
    System.out.println("************************* testCreateLanguage **********************");
    final Language langIT = languageService.newLanguage();
    langIT.setName(LANGUAGE_IT_NAME);
    languageService.createLanguage(langIT);
    final List<Language> allLanguages = languageService.getAllLanguages();
    assertNotNull(allLanguages);
    assertTrue(allLanguages.size() == 3);
    boolean found = false;
    for (final Language lang : allLanguages)
    {
      if (lang.getName().equals(langIT.getName()))
      {
        found = true;
      }
    }
    assertTrue(found);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.LanguageServiceImpl#getAllLanguages()}.
   */
  @Test
  // @Ignore
  public void testGetAllLanguages() throws Exception
  {
    assertNotNull(languageService);
    final Language langGR = languageService.newLanguage();
    langGR.setName(LANGUAGE_GR_NAME);
    final Language langIT = languageService.newLanguage();
    langIT.setName(LANGUAGE_IT_NAME);
    languageService.createLanguage(langIT);
    languageService.createLanguage(langGR);
    final List<Language> allLanguages = languageService.getAllLanguages();
    assertTrue(allLanguages.size() == 4);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.LanguageServiceImpl#getLanguage(java.lang.String)}
   * .
   * 
   * @throws LanguageServiceException
   */
  @Test
  // @Ignore
  public void testGetLanguage() throws LanguageServiceException
  {
    assertNotNull(languageService);
    final Language langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    assertNotNull(langFR);
    assertTrue(langFR.getName().equals(LANGUAGE_FR_NAME));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.LanguageServiceImpl#newLanguage()}.
   * 
   * @throws LanguageServiceException
   */
  @Test
  // @Ignore
  public void testNewLanguage() throws LanguageServiceException
  {
    assertNotNull(languageService);
    final Language langGR = languageService.newLanguage();
    langGR.setName(LANGUAGE_GR_NAME);
    languageService.createLanguage(langGR);
    final List<Language> allLanguages = languageService.getAllLanguages();
    assertTrue(allLanguages.size() == 3);
  }
}