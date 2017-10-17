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
package org.novaforge.forge.commands.datasource.service;

import org.novaforge.forge.commands.datasource.command.ShellTable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class DataSourceAccess
{
	DataSource	dataSource;

	public DataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public void exec(String sql) throws Exception
	{
		if (dataSource == null)
		{
			throw new RuntimeException("No DataSource selected");
		}
		Connection con = dataSource.getConnection();
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			stmt.execute(sql);
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
	}

	public ShellTable getTableInfo() throws Exception
	{
		if (dataSource == null)
		{
			throw new RuntimeException("No DataSource selected");
		}
		Connection con = dataSource.getConnection();
		DatabaseMetaData metaData = con.getMetaData();
		ResultSet rs = metaData.getTables(null, null, null, null);
		ShellTable table = printResult(rs);
		rs.close();
		return table;
	}

	private ShellTable printResult(ResultSet rs) throws SQLException
	{
		ShellTable        table = new ShellTable();
		ResultSetMetaData meta  = rs.getMetaData();
		for (int c = 1; c <= meta.getColumnCount(); c++)
		{
			table.getHeader().add(meta.getColumnLabel(c));
		}
		while (rs.next())
		{
			List<String> row = table.addRow();
			for (int c = 1; c <= meta.getColumnCount(); c++)
			{
				row.add(rs.getString(c));
			}
		}
		return table;
	}

	public ShellTable query(String sql) throws Exception
	{
		if (dataSource == null)
		{
			throw new RuntimeException("No DataSource selected");
		}
		Connection con = dataSource.getConnection();
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ShellTable table = printResult(rs);
			rs.close();
			return table;
		}
		finally
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
	}

}
