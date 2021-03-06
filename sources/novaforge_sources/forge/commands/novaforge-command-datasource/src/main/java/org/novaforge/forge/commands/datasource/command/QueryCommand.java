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

import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.felix.service.command.CommandSession;
import org.novaforge.forge.commands.datasource.service.DataSourceAccess;

/**
 * This class describes a command bean used to execute sql query on specific datasource
 * 
 * @author Guillaume Lamirand
 */
@Command(scope = "ds", name = "query", description = "Executes a SQL query and displays the result")
public class QueryCommand implements Action
{

	/**
	 * Parameter given to this command, ie "SELECT * FROM TABLE"
	 */
	@Argument(index = 0, name = "sql", required = true, description = "SQL Query", multiValued = false)
	String										sql;
	/**
	 * Reference to {@link DataSourceAccess}
	 */
	private DataSourceAccess	dbAccess;

	/**
	 * Use by container to inject the bean
	 * 
	 * @param pDbAccess
	 *          the dbAccess to set
	 */
	public void setDbAccess(final DataSourceAccess pDbAccess)
	{
		this.dbAccess = pDbAccess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(final CommandSession pSession) throws Exception
	{
		final ShellTable table = dbAccess.query(sql);
		table.print();
		return null;
	}

}
