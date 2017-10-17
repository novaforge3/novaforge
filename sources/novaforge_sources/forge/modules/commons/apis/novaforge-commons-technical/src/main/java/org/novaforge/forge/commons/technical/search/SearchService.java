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
package org.novaforge.forge.commons.technical.search;

import java.util.List;

/**
 * @author sbenoist
 */
public interface SearchService
{
	/**
	 * This method allows to search a regex into a directory recursively on defined extensions file
	 * 
	 * @param pTargetDirectoryPath
	 *          the target directory path
	 * @param pRegex
	 *          the regex to search
	 * @param pFileExtensions
	 *          the file extensions of the search (ie "java", "txt",...if null every file is scanned)
	 * @return a list of SearchResult
	 * @throws SearchServiceException
	 */
	List<SearchResult> search(String pTargetDirectoryPath, String pRegex, String... pFileExtensions)
	    throws SearchServiceException;

	/**
	 * @param pTargetDirectoryPath
	 *          the target directory path
	 * @param pRegex
	 *          the regex to search
	 * @param pFileRegexs
	 *          an array of file regexs (if null, no file regex is applied)
	 * @return a list of JavaSearchResult
	 * @throws SearchServiceException
	 */
	List<JavaSearchResult> searchInJavaSource(String pTargetDirectoryPath, String pRegex, String... pFileRegexs)
	    throws SearchServiceException;
}
