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
package org.novaforge.forge.commons.technical.search.internal;

import junit.framework.TestCase;
import org.junit.Assert;
import org.novaforge.forge.commons.technical.search.JavaSearchResult;
import org.novaforge.forge.commons.technical.search.SearchResult;
import org.novaforge.forge.commons.technical.search.SearchService;
import org.novaforge.forge.commons.technical.search.SearchServiceException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbenoist
 */
public class TestSearchService extends TestCase
{
  private static final String REGEX_REQ                  = "@Requirement(\\s*)[(](\\s*)(value(\\s*)[=](\\s*))?[{]?(\\s*)(Requirement_Enum.REQ_(\\w*)(\\s*)[,]?(\\s*))+(\\s*)[}]?(\\s*)[)]";
  private final SearchService searchService = new SearchServiceImpl();
  private boolean             testSearchServiceActivated = false;

  public TestSearchService()
  {
    super();
    final String property = System.getProperty("test.search.service.profile");
    if ("true".equals(property))
    {
      testSearchServiceActivated = true;
    }
  }

  // @Requirement(Requirement_Enum.REQ_MY_REQ_ID)
  // @Requirement(value = Requirement_Enum.REQ_MY_REQ_ID)
  // @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID })
  // @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })
  public void testMatch()
  {
    // String test = "@Requirement(   value =  Requirement_Enum.REQ_FCT_DEV)";

    // String test = "@Requirement( Requirement_Enum.REQ_FCT_DEV)";
    // String test = "@Requirement( value = { Requirement_Enum.REQ_FCT_DEV } )";
    final String test = "@Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })";
    System.out.println("occurrence = " + getOccurrence(REGEX_REQ, test));

    // @Requirement(value = {Requirement_Enum.REQ_FCT_PERS_01, Requirement_Enum.REQ_TEC_PERS_02 })

  }

  private String getOccurrence(final String pRegex, final String pStr)
  {

    String result = null;

    // Compile the regular expression
    final Pattern pattern = Pattern.compile(pRegex);

    // Check for the existence of the pattern
    final Matcher matcher = pattern.matcher(pStr);
    if (matcher.find())
    {
      // Retrieve matching string
      result = matcher.group(); // b
    }

    return result;
  }

  public void testPackageMatch()
  {
    final String  regex   = "^package(\\s+)((\\w+).)+[;]";
    final String  str     = "package org.novaforge.forge.commons.search.internal;";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(str);
    // System.out.println(matcher.find());
    assertEquals(true, matcher.find());
    final String packName = matcher.group().split(" ")[1];
    System.out.println("packName =" + packName);
  }

  // This is the occurrence to find in the text below
  // @Requirement( value = REQ_FCT_DEV, version = 1.1)
  public void testSearch()
  {
    if (testSearchServiceActivated)
    {
      try
      {
        final String regex = "@Requirement(\\s*)[(](\\s*)value(\\s*)[=](\\s*)(\\w*)(\\s*)[,](\\s*)version(\\s*)[=](\\s*)(\\d*)(\\.)(\\d*)(\\s*)[)]";

        final List<SearchResult> results = searchService
            .search(
                "/home/benoists/Documents/workspace-sts-2.5.2.RELEASE/sources/modules/commons/impl/novaforge-commons-search-impl",
                regex, "java");
        assertEquals(1, results.size());
        for (final SearchResult searchResult : results)
        {
          System.out.println(searchResult.toString());
        }
      }
      catch (final SearchServiceException e)
      {
        e.printStackTrace();
        Assert.fail();
      }
    }
  }

  public void testJavaSearch()
  {
    if (testSearchServiceActivated)
    {
      try
      {
        final List<JavaSearchResult> results = searchService
            .searchInJavaSource(
                "/home/benoists/Documents/workspace-sts-2.5.2.RELEASE/sources/modules/commons/impl/novaforge-commons-search-impl",
                "package", "TestSearchService.java");
        assertEquals(4, results.size());
        for (final JavaSearchResult searchResult : results)
        {
          System.out.println(searchResult.toString());
        }
      }
      catch (final SearchServiceException e)
      {
        e.printStackTrace();
        Assert.fail();
      }
    }
  }

  public void testMatch2()
  {
    final String regex = "\\n\\s*@Requirement";
    final String str = "zzz" + "\n" + "   @Requirement zzz" + "\n" + "kkkkk";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(str);
    if (matcher.find())
    {
      System.out.println("res = " + matcher.group());
    }
    else
    {
      fail();
    }

  }

  public void testSearch2()
  {
    if (testSearchServiceActivated)
    {
      try
      {
        final String regex = "\\n\\s*@Requirement(\\s*)[(](\\s*)(value(\\s*)[=](\\s*))?[{]?(\\s*)(Requirement_Enum.(\\w*)(\\s*)[,]?(\\s*))+(\\s*)[}]?(\\s*)[)]";

        final List<SearchResult> results = searchService.search("/home/sbenoist/workspace/demo.code", regex,
            "java");
        assertEquals(1, results.size());
        for (final SearchResult searchResult : results)
        {
          System.out.println(searchResult.toString());
        }
      }
      catch (final SearchServiceException e)
      {
        e.printStackTrace();
        Assert.fail();
      }
    }
  }

  public void testJavaSearch2()
  {
    if (testSearchServiceActivated)
    {
      try
      {
        final List<JavaSearchResult> results = searchService
            .searchInJavaSource(
                "/home/benoists/Documents/workspace-sts-2.5.2.RELEASE/sources/modules/commons/impl/novaforge-commons-search-impl/src/main/resources",
                REGEX_REQ, "Test.java");
        assertEquals(4, results.size());
        for (final JavaSearchResult searchResult : results)
        {
          System.out.println(searchResult.toString());
        }
      }
      catch (final SearchServiceException e)
      {
        e.printStackTrace();
        Assert.fail();
      }
    }
  }
}
