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
package org.novaforge.forge.commands.stats.csv;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.novaforge.forge.commands.stats.AbstractStatsCommand;
import org.novaforge.forge.commands.stats.internal.model.StatsProject;
import org.novaforge.forge.commands.stats.internal.model.StatsUsers;
import org.novaforge.forge.commons.technical.conversion.CsvConversionException;
import org.novaforge.forge.commons.technical.conversion.CsvConverterFactory;
import org.novaforge.forge.commons.technical.conversion.CsvConverterService;
import org.novaforge.forge.commons.technical.conversion.model.CsvCellDescriptor;
import org.novaforge.forge.commons.technical.conversion.model.CsvConverterDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
@Command(scope = "stats", name = "projects-csv", description = "Stats about projects usage")
public class StatsCsvCommand extends AbstractStatsCommand
{

  private static final String CSV_DATETIME_FORMAT = "yyyy.MM.dd";
  private static final char   CSV_DELIMITER       = ';';
  private static final String NO_APP              = " / ";
  @Argument(index = 0, name = "file", description = "The file to export", required = true,
            multiValued = false) private String file;
  private CsvConverterService csvConverterService;
  private CsvConverterFactory csvConverterFactory;

  /**
   * {@inheritDoc}
   * 
   * @throws Exception
   */
  @Override
  protected void process(final List<StatsProject> pStatsProjects) throws Exception
  {

    // Converts to beans
    final List<CsvProject> beans = new ArrayList<CsvProject>();
    for (final StatsProject statsProject : pStatsProjects)
    {
      beans.add(convert(statsProject));
    }

    // create the cells descriptors
    final CsvConverterDescriptor descriptor = getExportDescriptor();

    // generate the file csv
    try
    {
      csvConverterService.exportToFile(file, CsvProject.class, beans, descriptor);
    }
    catch (final CsvConversionException e)
    {
      throw new Exception(String.format("an error occured during exporting satts in csv format in file=[%s]",
          file), e);
    }
  }

  private CsvProject convert(final StatsProject pStatsProject)
  {
    final CsvProject bean = new CsvProject(pStatsProject.getName(), pStatsProject.getCreated(),
        pStatsProject.getContact(), pStatsProject.getUsers().getUsers(), pStatsProject.getUsers()
            .getExternalUsers());

    // Alfresco
    String type = "Alfresco";
    if (pStatsProject.getApplications().containsKey(type))
    {
      final StatsUsers statsUsers = pStatsProject.getApplications().get(type);
      bean.setAlfrescoUsers(String.valueOf(statsUsers.getUsers()));
      bean.setAlfrescoExtUsers(String.valueOf(statsUsers.getExternalUsers()));
    }
    else
    {
      bean.setAlfrescoUsers(NO_APP);
      bean.setAlfrescoExtUsers(NO_APP);
    }

    // Mantis
    type = "Mantis";
    if (pStatsProject.getApplications().containsKey(type))
    {
      final StatsUsers statsUsers = pStatsProject.getApplications().get(type);
      bean.setMantisUsers(String.valueOf(statsUsers.getUsers()));
      bean.setMantisExtUsers(String.valueOf(statsUsers.getExternalUsers()));
    }
    else
    {
      bean.setMantisUsers(NO_APP);
      bean.setMantisExtUsers(NO_APP);
    }

    // Jira
    type = "Jira";
    if (pStatsProject.getApplications().containsKey(type))
    {
      final StatsUsers statsUsers = pStatsProject.getApplications().get(type);
      bean.setJiraUsers(String.valueOf(statsUsers.getUsers()));
      bean.setJiraExtUsers(String.valueOf(statsUsers.getExternalUsers()));
    }
    else
    {
      bean.setJiraUsers(NO_APP);
      bean.setJiraExtUsers(NO_APP);
    }
    // Svn
    type = "Svn";
    if (pStatsProject.getApplications().containsKey(type))
    {
      final StatsUsers statsUsers = pStatsProject.getApplications().get(type);
      bean.setSvnUsers(String.valueOf(statsUsers.getUsers()));
      bean.setSvnExtUsers(String.valueOf(statsUsers.getExternalUsers()));
    }
    else
    {
      bean.setSvnUsers(NO_APP);
      bean.setSvnExtUsers(NO_APP);
    }
    // Testlink
    type = "Testlink";
    if (pStatsProject.getApplications().containsKey(type))
    {
      final StatsUsers statsUsers = pStatsProject.getApplications().get(type);
      bean.setTestlinkUsers(String.valueOf(statsUsers.getUsers()));
      bean.setTestlinkExtUsers(String.valueOf(statsUsers.getExternalUsers()));
    }
    else
    {
      bean.setTestlinkUsers(NO_APP);
      bean.setTestlinkExtUsers(NO_APP);
    }

    return bean;
  }

  private CsvConverterDescriptor getExportDescriptor()
  {
    final List<CsvCellDescriptor> cells = new ArrayList<CsvCellDescriptor>();
    cells.add(csvConverterFactory.createCsvCellDescriptor("Name", "name"));
    final CsvCellDescriptor dateCell = csvConverterFactory.createCsvCellDescriptor("Created", "created");
    dateCell.setMandatory(true);
    dateCell.setFormat(CSV_DATETIME_FORMAT);
    cells.add(dateCell);
    cells.add(csvConverterFactory.createCsvCellDescriptor("Contact", "contact"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Members", "users"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Ext users", "extUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Alfresco", "alfrescoUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Alfresco ext users", "alfrescoExtUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Mantis", "mantisUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Mantis ext users", "mantisExtUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Jira", "jiraUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Jira ext users", "jiraExtUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Svn", "svnUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Svn ext users", "svnExtUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Testlink", "testlinkUsers"));
    cells.add(csvConverterFactory.createCsvCellDescriptor("Testlink ext users", "testlinkExtUsers"));
    return csvConverterFactory.createCsvConverterDescriptor(cells, true, CSV_DELIMITER);

  }

  /**
   * @param pCsvConverterService
   *          the csvConverterService to set
   */
  public void setCsvConverterService(final CsvConverterService pCsvConverterService)
  {
    csvConverterService = pCsvConverterService;
  }

  /**
   * @param pCsvConverterFactory
   *          the csvConverterFactory to set
   */
  public void setCsvConverterFactory(final CsvConverterFactory pCsvConverterFactory)
  {
    csvConverterFactory = pCsvConverterFactory;
  }
}
