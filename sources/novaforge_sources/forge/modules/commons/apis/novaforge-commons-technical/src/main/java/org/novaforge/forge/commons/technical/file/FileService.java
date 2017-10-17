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
package org.novaforge.forge.commons.technical.file;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Common Service used to manage Files.
 * 
 * @author germain-c
 */
public interface FileService
{
	/**
	 * Store the {@link FileItem} on a location. If the destination directory doesn't exist it is automatically
	 * created.
	 * 
	 * @param fileItem
	 *          source FileItem
	 * @param fileDest
	 *          Destination file.
	 * @throws FileServiceException
	 */
	void storeFile(final FileItem fileItem, final File fileDest) throws FileServiceException;

	/**
	 * Store a source file on a location preserving the original file (source). If the directory of the
	 * destination file doesn't exist, it will be created.
	 * 
	 * @param fileSrc
	 *          source file
	 * @param fileDest
	 *          destination file
	 * @throws FileServiceException
	 */
	void storeFile(final File fileSrc, final File fileDest) throws FileServiceException;

	/**
	 * Store the content of {@link InputStream} on a location. If the destination directory doesn't exist it is
	 * automatically created.
	 * 
	 * @param pInputStream
	 *          Source inputstream
	 * @param fileDest
	 *          Destination file.
	 * @throws FileServiceException
	 */
	void storeStream(final InputStream pInputStream, final File fileDest) throws FileServiceException;

	/**
	 * Rename a source file into the destination file. If the destination directory doesn't exist it is
	 * automatically created.
	 * 
	 * @param fileSrc
	 *          source file
	 * @param fileDest
	 *          destination file
	 * @throws FileServiceException
	 */
	void renameFile(final File fileSrc, final File fileDest) throws FileServiceException;

	/**
	 * Delete a file. If the file is a directory it will be deleted recursively.
	 * 
	 * @param fileSrc
	 *          source file
	 * @throws FileServiceException
	 */
	void deleteFile(final File fileSrc) throws FileServiceException;

	/**
	 * Retrieve all the files in the directory
	 * 
	 * @param directory
	 *          Full path of the Directory
	 * @return a not-null but possibly empty list of Files.
	 * @throws FileServiceException
	 */
	List<File> getAllFiles(final String directory) throws FileServiceException;

	/**
	 * Retrieve a FileMeta information for a given URL. A temporary file will be created with the content
	 * downloaded.
	 * 
	 * @param url
	 *          represents the url to the content to download
	 * @return
	 * @throws FileServiceException
	 *           if unable to get the information from the url.
	 */
	FileMeta downloadFile(final String url) throws FileServiceException;

	/**
	 * Test if the file exists.
	 * 
	 * @param f
	 *          represents the file to test
	 * @return true if the file exists.
	 */
	boolean existFile(final File f);

}
