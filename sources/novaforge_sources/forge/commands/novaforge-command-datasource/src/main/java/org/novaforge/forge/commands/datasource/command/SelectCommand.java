/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.commands.datasource.command;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.felix.service.command.CommandSession;
import org.novaforge.forge.commands.datasource.model.DataSourceInfo;
import org.novaforge.forge.commands.datasource.service.DataSourceSelect;
import org.osgi.framework.ServiceReference;

/**
 * This class defines a command used to select a datasource. The datasource selected will be used for the next
 * command.
 * 
 * @author Guillaume Lamirand
 */
@Command(scope = "ds", name = "select", description = "Selects a datasource")
public class SelectCommand implements Action
{
	/**
	 * Parameter given to this command, is jdbc/mydatasource
	 */
	@Argument(index = 0, name = "name", required = false, description = "DataSource JNDI name", multiValued = false)
	String										name;
	/**
	 * Reference to {@link DataSourceSelect}
	 */
	private DataSourceSelect	dbSelect;

	/**
	 * Use by container to inject the bean
	 * 
	 * @param pDbSelect
	 *          the dbSelect to set
	 */
	public void setDbSelect(final DataSourceSelect pDbSelect)
	{
		this.dbSelect = pDbSelect;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(final CommandSession session) throws Exception
	{
		if (name == null)
		{
			final ShellTable table = getDataSourcesTable();
			table.print();
		}
		else
		{
			dbSelect.selectDataSource(name);
		}
		return null;
	}

	/**
	 * @return {@link ShellTable} containing all datasource available
	 */
	public ShellTable getDataSourcesTable()
	{
		final Collection<ServiceReference<DataSource>> dataSources = dbSelect.getDataSources();
		final ShellTable table = new ShellTable();
		table.getHeader().add("Sel");
		table.getHeader().add("Name");
		table.getHeader().add("Product");
		table.getHeader().add("Version");
		table.getHeader().add("URL");
		for (final ServiceReference<DataSource> ref : dataSources)
		{
			final List<String> row = table.addRow();
			final DataSourceInfo info = dbSelect.getDataSourceInfo(ref);
			row.add(info.isSelected() ? "*" : "");
			row.add(info.getJndiName());
			row.add(info.getProduct());
			row.add(info.getVersion());
			row.add(info.getUrl());
		}
		return table;
	}
}
