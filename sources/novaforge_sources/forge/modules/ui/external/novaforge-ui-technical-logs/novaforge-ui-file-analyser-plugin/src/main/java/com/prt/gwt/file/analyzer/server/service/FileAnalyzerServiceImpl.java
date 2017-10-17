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
package com.prt.gwt.file.analyzer.server.service;

import com.prt.gwt.file.analyzer.client.service.FileAnalyzerService;
import com.prt.gwt.file.analyzer.client.vo.AnalyzedFileVO;
import com.prt.gwt.file.analyzer.client.vo.GetFileListCallbackVO;
import com.prt.gwt.file.analyzer.client.vo.SearchVO;
import com.prt.gwt.file.core.server.service.FileManagerServiceImpl;
import com.prt.gwt.file.core.server.utils.EscapeUtils;
import com.prt.gwt.file.core.server.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class FileAnalyzerServiceImpl implements FileAnalyzerService
{

	private static final Log    log                 = LogFactory.getLog(FileManagerServiceImpl.class);
	private static final String BUNDLE_CONTEXT_JNDI = "java:comp/env/BundleContext";

	@Override
	public GetFileListCallbackVO getFileList(final SearchVO vo)
	{
		final ArrayList<AnalyzedFileVO> result = new ArrayList<AnalyzedFileVO>();
		// get file list
		FilenameFilter fileNameFilter = null;
		if ((vo.getFileMask() != null) && !"".equals(vo.getFileMask().trim()))
		{
			final FileAcceptor fileAcceptor = new FileAcceptor(vo.getFileMask());
			fileNameFilter = new FilenameFilter()
			{
				@Override
				public boolean accept(final File dir, final String name)
				{
					return fileAcceptor.isAcceptable(name);
				}
			};
		}

		final File[] files = FileUtils.getConfigurationFileList(getLogDirectory(), fileNameFilter);

		// sort files
		Arrays.sort(files, new Comparator<File>()
		{
			@Override
			public int compare(final File f1, final File f2)
			{
				return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
			}
		});

		// analyse files
		for (final File file : files)
		{
			final Map<Long, String> entries = vo.getSearchString() != null ? getEntries(file, vo.getSearchString())
			    : new LinkedHashMap<Long, String>();
			if (!entries.isEmpty() || (vo.getSearchString() == null))
			{
				final AnalyzedFileVO analyzedFileVO = new AnalyzedFileVO(FileManagerServiceImpl.getFileVO(file));
				analyzedFileVO.setEntries(entries);
				result.add(analyzedFileVO);
			}
		}
		final GetFileListCallbackVO getFileListCallbackVO = new GetFileListCallbackVO();
		getFileListCallbackVO.setFilesData(result);
		return getFileListCallbackVO;
	}

	public String getLogDirectory()
	{
		return OSGiHelper.getLogTechnicalService().getLogDirectory();
	}

	private Map<Long, String> getEntries(final File file, final String entry)
	{
		final Map<Long, String> result = new LinkedHashMap<Long, String>();
		BufferedReader br;
		try
		{
			br = new BufferedReader(new FileReader(file));
		}
		catch (final FileNotFoundException e)
		{
			throw new RuntimeException("File not found (" + file.getName() + ")");
		}

		try
		{
			long lineNumber = 1;
			String line;
			while ((line = br.readLine()) != null)
			{
				final int index = line.indexOf(entry);
				if (index >= 0)
				{
					result.put(
					    lineNumber,
					    EscapeUtils.escapeHTML(line.substring(Math.max(index - 5, 0),
					        Math.min(line.length(), index + entry.length() + 5))));
				}
				lineNumber++;
			}
		}
		catch (final IOException e)
		{
			throw new RuntimeException("IOException: " + e.getLocalizedMessage());
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (final IOException e)
			{
				throw new RuntimeException("IOException: " + e.getLocalizedMessage());
			}
		}
		return result;
	}

	class FileAcceptor
	{
		private final List<String> fileMaskTokens = new ArrayList<String>();

		FileAcceptor(final String fileMask)
		{
			final StringTokenizer st = new StringTokenizer(fileMask, "*", false);
			while (st.hasMoreTokens())
			{
				fileMaskTokens.add(st.nextToken());
			}
		}

		public boolean isAcceptable(final String fileName)
		{
			boolean result = true;
			int index = 0;
			for (final String fileMaskToken : fileMaskTokens)
			{
				final int curIndex = fileName.indexOf(fileMaskToken);
				result = result && (curIndex >= index);
				if (result)
				{
					index = curIndex + fileMaskToken.length();
				}
				else
				{
					break;
				}
			}

			return result;
		}
	}

}