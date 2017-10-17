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
package com.prt.gwt.file.core.server.utils;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils
{

	public static File[] getConfigurationFileList(final String pLogDir, final FilenameFilter pFileNameFilter)
	{
		final File directory = new File(pLogDir);
		assert directory.isDirectory() : "Path does not correspond to a directory";

		IOFileFilter fileFilter;
		if (pFileNameFilter != null)
		{
			fileFilter = FileFilterUtils.asFileFilter(pFileNameFilter);
		}
		else
		{
			fileFilter = FileFilterUtils.trueFileFilter();
		}

		final Collection<File> filesCollection = org.apache.commons.io.FileUtils.listFiles(directory, fileFilter,
		    TrueFileFilter.INSTANCE);
		return filesCollection.toArray(new File[filesCollection.size()]);
	}

	public static void sendZipFile(final String filePath, final Long startWith, final Long lineCount,
	    final HttpServletResponse response)
	{

		try
		{
			final File file = new File(filePath);
			final String filename = file.getName();
			final FileInputStream fis = new FileInputStream(file);
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".zip");

			final ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
			zipOutputStream.setLevel(6);
			final ZipEntry entry = new ZipEntry(filename);
			zipOutputStream.putNextEntry(entry);

			if ((startWith == null) || (lineCount == null))
			{
				writeContent(fis, zipOutputStream, entry);
			}
			else
			{
				writeContent(fis, zipOutputStream, entry, startWith, lineCount);
			}

			zipOutputStream.closeEntry();

			zipOutputStream.finish();
			zipOutputStream.flush();
			zipOutputStream.close();

		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void writeContent(final FileInputStream fis, final ZipOutputStream zos, final ZipEntry entry)
	    throws IOException
	{
		// Transfer bytes from the file to the ZIP file
		final CRC32 crc = new CRC32();
		crc.reset();
		final byte[] buf = new byte[1024];
		long size = 0;
		int len;
		while ((len = fis.read(buf)) > 0)
		{
			size += len;
			zos.write(buf, 0, len);
			crc.update(buf);
		}
		entry.setSize(size);
		entry.setCrc(crc.getValue());
	}

	private static void writeContent(final FileInputStream fis, final ZipOutputStream zos,
	    final ZipEntry entry, final long startWith, final long lineCount) throws IOException
	{
		// Transfer bytes from the file to the ZIP file
		final BufferedReader bis = new BufferedReader(new FileReader(fis.getFD()));
		final CRC32 crc = new CRC32();
		crc.reset();
		long size = 0;
		int lineNumber = 1;
		String line;
		while ((line = bis.readLine()) != null)
		{
			if (lineNumber >= startWith)
			{
				line += '\n';
				size += line.length();
				zos.write(line.getBytes(), 0, line.length());
				crc.update(line.getBytes());
			}
			lineNumber++;
			if (lineNumber >= (startWith + lineCount))
			{
				break;
			}
		}
		entry.setSize(size);
		entry.setCrc(crc.getValue());
	}
}
