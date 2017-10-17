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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.novaforge.forge.commons.technical.search.JavaSearchResult;
import org.novaforge.forge.commons.technical.search.SearchResult;
import org.novaforge.forge.commons.technical.search.SearchService;
import org.novaforge.forge.commons.technical.search.SearchServiceException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbenoist
 */
public class SearchServiceImpl implements SearchService
{
  private static final String PACKAGE_REGEX  = "^package(\\s+)((\\w+).)+[;]";

  private static final String JAVA_EXTENSION = "java";

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SearchResult> search(final String pTargetDirectoryPath, final String pRegex,
      final String... pFileExtensions) throws SearchServiceException
  {
    checkParams(pTargetDirectoryPath, pRegex);

    final List<SearchResult> results = new ArrayList<SearchResult>();

    final File target = new File(pTargetDirectoryPath);
    final Collection<File> files = FileUtils.listFiles(target, pFileExtensions, true);
    for (final File file : files)
    {
      results.addAll(searchRegexInFile(file, pRegex, false));
    }
    return results;
  }

  private void checkParams(final String pTargetDirectoryPath, final String pRegex)
      throws SearchServiceException
  {
    if ((pRegex == null) || (pRegex.trim().length() == 0))
    {
      throw new SearchServiceException("The regex is mandatory");
    }
    else if ((pTargetDirectoryPath == null) || (pTargetDirectoryPath.trim().length() == 0))
    {
      throw new SearchServiceException("The target directory path is mandatory");
    }
  }

  private List<SearchResult> searchRegexInFile(final File pFile, final String pRegex, final boolean javaSearch)
      throws SearchServiceException
  {
    try
    {
      final List<SearchResult> results = new ArrayList<SearchResult>();

      // Get the file properties
      final String fileName = pFile.getName();
      final String path = pFile.getAbsolutePath();
      final Date lastModified = new Date(pFile.lastModified());

      // Compile the regex
      final Pattern pattern = Pattern.compile(pRegex);

      // read the file in a string in order to find the regex in multiple lines if necessary
      final String flatFile = FileUtils.readFileToString(pFile);

      // put the resulted string in a list line by line in order to determine the line number and the
      // snippet in lines of each occurrence
      final List<String> lines = getLines(flatFile);
      final Matcher matcher = pattern.matcher(flatFile);
      SearchResult result = null;
      while (matcher.find())
      {
        final MatchResult matchResult = matcher.toMatchResult();
        final long nb = getLineNumber(flatFile, matchResult.start());
        if (javaSearch)
        {
          JavaSearchResult jResult = (JavaSearchResult) result;
          jResult = new JavaSearchResultImpl(matcher.group(), fileName, path, nb);
          jResult.setPackageName(getPackageName(lines));
          jResult.setClassName(fileName.split("\\.")[0]);
          jResult.setSnippet(getSnippet((int) nb, lines));
          jResult.setFilePath(path);
          jResult.setLastModified(lastModified);
          results.add(jResult);
        }
        else
        {
          result = new SearchResultImpl(matcher.group(), fileName, path, nb);
          result.setSnippet(getSnippet((int) nb, lines));
          result.setFilePath(path);
          result.setLastModified(lastModified);
          results.add(result);
        }
      }

      return results;
    }
    catch (final IOException e)
    {
      throw new SearchServiceException(String.format("An error occured while reading file with name=%s",
          pFile.getName()), e);
    }
  }

  private List<String> getLines(final String pFlatFile)
  {
    final List<String> lines = new ArrayList<String>();

    final String[] tab = pFlatFile.split("\n");
    Collections.addAll(lines, tab);
    return lines;
  }

  private long getLineNumber(final String pFlatFile, final int pStartIndex)
  {
    final Pattern pattern = Pattern.compile("\n");
    final Matcher matcher = pattern.matcher(pFlatFile.substring(0, pStartIndex));
    int count = 1;
    while (matcher.find())
    {
      count++;
    }

    return count;
  }

  private String getPackageName(final List<String> pLines)
  {
    String result = "";

    final Pattern pattern = Pattern.compile(PACKAGE_REGEX);
    Matcher matcher = null;
    String[] tab = null;
    for (final String line : pLines)
    {
      matcher = pattern.matcher(line);
      if (matcher.find())
      {
        tab = matcher.group().split(" ");
        if (tab.length == 2)
        {
          result = tab[1].substring(0, tab[1].length() - 1);
          break;
        }
      }
    }

    return result;
  }

  private String getSnippet(final int pLineNumber, final List<String> pLines)
  {
    final StringBuilder sb = new StringBuilder();
    if (pLineNumber == 1)
    {
      sb.append(pLines.get(pLineNumber - 1)).append("\n").append(pLines.get(pLineNumber));
    }
    else if (pLineNumber == pLines.size())
    {
      sb.append(pLines.get(pLineNumber - 2)).append("\n").append(pLines.get(pLineNumber - 1));
    }
    else
    {
      sb.append(pLines.get(pLineNumber - 2)).append("\n").append(pLines.get(pLineNumber - 1)).append("\n").append(pLines
                                                                                                                      .get(pLineNumber));
    }
    return sb.toString();
  }

  @Override
  public List<JavaSearchResult> searchInJavaSource(final String pTargetDirectoryPath, final String pRegex,
      final String... pFileRegexps) throws SearchServiceException
  {
    checkParams(pTargetDirectoryPath, pRegex);

    final List<JavaSearchResult> results = new ArrayList<JavaSearchResult>();

    // Create a directory filter for Non-hidden directories
    final IOFileFilter dirFilter = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(),
        HiddenFileFilter.VISIBLE);

    // Create a file filter for java files
    final IOFileFilter javaFileFilter = new SuffixFileFilter(JAVA_EXTENSION);

    // Create a file filter for each regexp
    IOFileFilter fileFilter = null;
    if ((pFileRegexps != null) && (pFileRegexps.length > 0))
    {
      final IOFileFilter[] fileFilters = new IOFileFilter[pFileRegexps.length];
      for (int i = 0; i < pFileRegexps.length; i++)
      {
        fileFilters[i] = new RegexFileFilter(pFileRegexps[i]);
      }

      // Add all the filters
      fileFilter = FileFilterUtils.and(javaFileFilter, FileFilterUtils.or(fileFilters));
    }
    else
    {
      fileFilter = javaFileFilter;
    }

    final File target = new File(pTargetDirectoryPath);
    final Collection<File> files = FileUtils.listFiles(target, fileFilter, dirFilter);

    // Get first the simple results of regex in files
    List<SearchResult> searchResults = null;
    JavaSearchResult jSearchResult = null;
    for (final File file : files)
    {
      searchResults = searchRegexInFile(file, pRegex, true);
      for (final SearchResult searchResult : searchResults)
      {
        jSearchResult = (JavaSearchResult) searchResult;
        results.add(jSearchResult);
      }
    }
    return results;
  }
}
