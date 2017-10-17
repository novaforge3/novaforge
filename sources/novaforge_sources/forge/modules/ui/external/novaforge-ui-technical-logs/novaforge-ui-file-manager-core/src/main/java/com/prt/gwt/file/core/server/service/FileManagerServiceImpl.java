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
package com.prt.gwt.file.core.server.service;

import com.prt.gwt.file.core.client.service.FileManagerService;
import com.prt.gwt.file.core.client.vo.BaseFileDataVO;
import com.prt.gwt.file.core.client.vo.BaseFileVO;
import com.prt.gwt.file.core.client.vo.FileVO;
import com.prt.gwt.file.core.client.vo.GetFileDataVO;
import com.prt.gwt.file.core.client.vo.GetFileListCallbackVO;
import com.prt.gwt.file.core.server.utils.EscapeUtils;
import com.prt.gwt.file.core.server.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class FileManagerServiceImpl implements FileManagerService
{

	private static final Log    log                 = LogFactory.getLog(FileManagerServiceImpl.class);
	private static final String BUNDLE_CONTEXT_JNDI = "java:comp/env/BundleContext";

	@Override
	public GetFileListCallbackVO getFileList()
	{
		final File[] files = FileUtils.getConfigurationFileList(getLogDirectory(), null);
		Arrays.sort(files, new Comparator<File>()
		{
			@Override
			public int compare(final File f1, final File f2)
			{
				return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
			}
		});
		return new GetFileListCallbackVO(convertToVos(files));
	}

	@Override
	public BaseFileDataVO getFileData(final GetFileDataVO vo)
	{
		return vo.isTail() ? getFileTailData(vo.getFile()) : this.getFileData(vo.getFile(), vo.getStartWith(),
																																					vo.getLineCount());
	}

	private BaseFileDataVO getFileTailData(final FileVO fileVO)
	{

		final BaseFileVO baseFileVO = new BaseFileVO(fileVO);
		final BaseFileDataVO result = new BaseFileDataVO(baseFileVO);
		log.debug("Getting tail for file '" + fileVO.getFileName() + "'");
		RandomAccessFile raf = null;
		try
		{

			final File file = new File(fileVO.getFilePath());
			final long lastModified = file.lastModified();
			log.debug("Processed file modification since " + fileVO.getLastModified()
			    + "; Current file modification is " + lastModified);
			result.setChanged(lastModified != fileVO.getLastModified());
			if (result.isChanged())
			{
				baseFileVO.setLastModified(lastModified);
				raf = new RandomAccessFile(file, "r");

				// goto last pointer
				raf.seek(fileVO.getFileLength());

				// Result data
				final List<String> data = new ArrayList<String>();

				// Maximum number of lines to be read one time
				final long maxLines = 1000;

				// Current count of read lines
				long lineN = 0;

				// Current line
				String line;

				while ((line = raf.readLine()) != null)
				{
					data.add(EscapeUtils.escapeHTML(line));
					if (lineN++ > maxLines)
					{
						break;
					}
				}
				log.debug(data.size() + " lines have been read from the file.");

				// Remove last line - it could be not full!!
				final String s = data.size() > 0 ? data.remove(data.size() - 1) : "";
				baseFileVO.setFileLength(raf.getFilePointer() - s.getBytes().length);
				result.setFileData(data);

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
				if (raf != null)
				{
					raf.close();
				}
			}
			catch (final IOException e)
			{
				throw new RuntimeException("IOException: " + e.getLocalizedMessage());
			}
		}

		return result;
	}

	private BaseFileDataVO getFileData(final FileVO fileVO, final long startWith, final long lineCount)
	{
		final BaseFileVO baseFileVO = new BaseFileVO(fileVO);
		final BaseFileDataVO result = new BaseFileDataVO(baseFileVO);
		BufferedReader br;
		long lastModified;
		try
		{
			final File file = new File(fileVO.getFilePath());
			lastModified = file.lastModified();
			br = new BufferedReader(new FileReader(file));
		}
		catch (final FileNotFoundException e)
		{
			throw new RuntimeException("File not found (" + fileVO.getFilePath() + ")");
		}

		final long currentLinesInFile = fileVO.getLinesInFile();
		baseFileVO.setLastModified(lastModified);
		result.setChanged(lastModified != fileVO.getLastModified());
		result.setActivePage((startWith / lineCount) + 1);
		result.setStartLine(startWith);
		try
		{
			final List<String> data = new ArrayList<String>();
			long lineNumber = 1;
			String line;
			while ((line = br.readLine()) != null)
			{
				if ((lineNumber >= startWith) && (lineNumber < (startWith + lineCount)))
				{
					data.add(EscapeUtils.escapeHTML(line));
				}

				// Do not process rest of the file if it was not changed and we already know it's size in lines
				if (!result.isChanged() && (currentLinesInFile > 0) && (lineNumber >= (startWith + lineCount)))
				{
					lineNumber = currentLinesInFile;
					break;
				}

				lineNumber++;
			}
			result.setFileData(data);
			baseFileVO.setLinesInFile(lineNumber);
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

	public String getLogDirectory()
	{
		return OSGiHelper.getLogTechnicalService().getLogDirectory();
	}

	public static List<BaseFileVO> convertToVos(final File[] files)
	{
		final List<BaseFileVO> result = new LinkedList<BaseFileVO>();
		for (final File file : files)
		{
			result.add(getFileVO(file));
		}
		return result;
	}

	public static BaseFileVO getFileVO(final File file)
	{
		final BaseFileVO baseFileVO = new BaseFileVO();
		baseFileVO.setFileName(file.getName());
		baseFileVO.setDirectory(file.isDirectory());
		baseFileVO.setFilePath(EscapeUtils.escapeFileName(file.getPath()));
		baseFileVO.setFileLength(file.length());
		return baseFileVO;
	}

}