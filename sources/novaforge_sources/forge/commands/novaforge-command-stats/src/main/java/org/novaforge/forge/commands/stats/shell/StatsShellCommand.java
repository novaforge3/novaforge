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
package org.novaforge.forge.commands.stats.shell;

import org.apache.karaf.shell.commands.Command;
import org.novaforge.forge.commands.stats.AbstractStatsCommand;
import org.novaforge.forge.commands.stats.internal.model.Constants;
import org.novaforge.forge.commands.stats.internal.model.StatsProject;
import org.novaforge.forge.commands.stats.internal.model.StatsUsers;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
@Command(scope = "stats", name = "projects-shell", description = "Stats about projects usage")
public class StatsShellCommand extends AbstractStatsCommand
{

  /**
   * {@inheritDoc}
   */
  @Override
  protected void process(final List<StatsProject> pStatsProjects)
  {

    // Build ShellTable header
    final ShellTable table = new ShellTable();
    table.getHeader().add("Name");
    table.getHeader().add("Created");
    table.getHeader().add("Contact");
    table.getHeader().add("Members");
    for (final String app : Constants.APPS)
    {
      table.getHeader().add(app);
    }

    // Browse projects
    for (final StatsProject project : pStatsProjects)
    {
      // Add a new row
      final List<String> row = table.addRow();

      // Add project name column
      row.add(project.getName());

      // Add project created column
      final SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM yyyy");
      final String formatedDate = formatter.format(project.getCreated());
      row.add(formatedDate);

      // Add author column
      row.add(project.getContact());

      // Add members column
      final StatsUsers users = project.getUsers();
      row.add(String.format("%s (%s)", users.getUsers(), users.getExternalUsers()));

      // Process apps
      for (final String type : Constants.APPS)
      {
        if (project.getApplications().containsKey(type))
        {
          final StatsUsers statsUsers = project.getApplications().get(type);
          row.add(String.format("%s (%s)", statsUsers.getUsers(), statsUsers.getExternalUsers()));

        }
        else
        {

          row.add(" / ");
        }
      }
    }

    table.print();
  }

}
