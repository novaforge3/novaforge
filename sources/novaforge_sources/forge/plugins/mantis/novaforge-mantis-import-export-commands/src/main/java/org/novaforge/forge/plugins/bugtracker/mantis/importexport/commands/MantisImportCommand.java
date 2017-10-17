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
package org.novaforge.forge.plugins.bugtracker.mantis.importexport.commands;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.plugins.bucktracker.mantis.services.MantisImportService;

/**
 * @author sbenoist
 */
@Command(scope = "mantis", name = "import", description = "Import datas to mantis")
public class MantisImportCommand extends OsgiCommandSupport
{
  @Argument(index = 0, name = "importFilePath", description = "The absolute import file path",
      required = true, multiValued = false)
  private final String        importFilePath          = null;

  @Argument(index = 1, name = "projectName", description = "The mantis project name to import",
      required = true, multiValued = false)
  private final String        projectName             = null;

  @Argument(index = 2, name = "targetMantisInstanceURL", description = "The target Mantis Instance Base URL",
      required = true, multiValued = false)
  private final String        targetMantisInstanceURL = null;

  private MantisImportService mantisImportService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    mantisImportService.importMantisProject(importFilePath, projectName, targetMantisInstanceURL);
    return null;
  }

  /**
   * @param mantisImportService
   *          the mantisImportService to set
   */
  public void setMantisImportService(final MantisImportService mantisImportService)
  {
    this.mantisImportService = mantisImportService;
  }

}
