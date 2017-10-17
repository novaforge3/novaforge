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
package org.novaforge.forge.plugins.scm.svn.client.impl;

import junit.framework.TestCase;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author sbenoist
 */
public class TestSVNClientFacade extends TestCase
{
	public static String                 REPOSITORY_PATH     = "https://novaforge7.bull.fr:443/svn/nfsafran/sandboxes/CellTreeExemple";
	public static String                 USER                = "sbenoist";
	private final SVNClientFacadeService svnService          = new SVNClientFacadeImpl();
	private boolean svnProfileActivated = false;

	public TestSVNClientFacade(final String name)
	{
		super(name);
		final String property = System.getProperty("svn.profile");
		if ("true".equals(property))
		{
			svnProfileActivated = true;
		}
	}

	public void testGetRepositoryTree() throws SVNAgentException
	{
		if (svnProfileActivated)
		{
			final SVNFacadeService svnFacadeService = svnService.getSVNFacadeService("http://localhost:9000");
			final SVNNodeEntryDTO nodeEntry = svnService.getSCMRepositoryTree(svnFacadeService, REPOSITORY_PATH,
			    USER);
			assertNotNull(nodeEntry);
		}
	}

	public void testExportSCMRepositoryNodes() throws Exception
	{
		if (svnProfileActivated)
		{
			final List<String> nodes = new ArrayList<String>();
			nodes.add("src/com/elazzouzi/exemple/client/CellTreeExemple.java");
			nodes.add("src/com/elazzouzi/exemple/server/GreetingServiceImpl.java");
			nodes.add("src/com/elazzouzi/exemple/client/GreetingService.java");
			final SVNFacadeService svnFacadeService = svnService.getSVNFacadeService("http://localhost:9000");
			final DataHandler dataHandler = svnService.exportSCMRepositoryNodes(svnFacadeService, nodes,
			    REPOSITORY_PATH, USER);
			final DataSource dataSource = dataHandler.getDataSource();
			final ZipInputStream zipInputStream = new ZipInputStream(dataSource.getInputStream());
			assertNotNull(zipInputStream);
			unzip(zipInputStream);
		}
	}

	/**
	 * Unzip a ZipInputStream object
	 * 
	 * @param zipFile
	 *          input zip file
	 * @param output
	 *          zip file output folder
	 */
	private void unzip(final ZipInputStream pZipInputStream)
	{
		final byte[] buffer = new byte[1024];
		try
		{

			// create output directory is not exists
			final File folder = new File("/tmp/svn/result");
			if (!folder.exists())
			{
				folder.mkdir();
			}

			// get the zipped file list entry
			ZipEntry ze = pZipInputStream.getNextEntry();

			while (ze != null)
			{
				final String fileName = ze.getName();
				final File newFile = new File(folder + File.separator + fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				final FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = pZipInputStream.read(buffer)) > 0)
				{
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = pZipInputStream.getNextEntry();
			}

			pZipInputStream.closeEntry();
			pZipInputStream.close();

			System.out.println("Done");

		}
		catch (final IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public boolean isSvnProfileActivated()
	{
		return svnProfileActivated;
	}

}
