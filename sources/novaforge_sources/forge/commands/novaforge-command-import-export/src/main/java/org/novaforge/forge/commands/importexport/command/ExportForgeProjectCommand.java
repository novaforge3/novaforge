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
package org.novaforge.forge.commands.importexport.command;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.importexport.services.ExportService;

/**
 * @author sbenoist
 */
@Command(scope = "forge", name = "exportDatas", description = "Export datas from current forge")
public class ExportForgeProjectCommand extends OsgiCommandSupport
{
  @Argument(index = 0, name = "exportFilePath", description = "The absolute export file path",
      required = true, multiValued = false)
  private String        exportFilePath = null;

  @Argument(index = 1, name = "projectId", description = "The projectId of the project to export",
      required = true, multiValued = false)
  private String        projectId      = null;

  private ExportService exportService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    exportService.exportProject(exportFilePath, projectId);
    return null;
  }

  public void setExportService(final ExportService pExportService)
  {
    exportService = pExportService;
  }

}
