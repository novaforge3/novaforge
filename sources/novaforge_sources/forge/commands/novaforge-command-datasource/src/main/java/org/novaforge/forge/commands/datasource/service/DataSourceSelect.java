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
package org.novaforge.forge.commands.datasource.service;

import org.novaforge.forge.commands.datasource.model.DataSourceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Command used to list all datasource or select one
 * 
 * @author Guillaume Lamirand
 */
public class DataSourceSelect
{
	/**
	 * Reference to {@link DataSourceAccess}
	 */
	private DataSourceAccess	dbAccess;
	/**
	 * Reference to {@link BundleContext}
	 */
	private BundleContext			context;

	/**
	 * Use by container to inject the bean
	 * 
	 * @param context
	 *          the context to set
	 */
	public void setContext(final BundleContext context)
	{
		this.context = context;
	}

	/**
	 * Use by container to inject the bean
	 * 
	 * @param dbAccess
	 *          the dbAccess to set
	 */
	public void setDbAccess(final DataSourceAccess dbAccess)
	{
		this.dbAccess = dbAccess;
	}

	/**
	 * @return list of jndi name for all datasources
	 */
	public List<String> getDataSourceNames()
	{
		Collection<ServiceReference<DataSource>> dataSources = getDataSources();
		List<String>                             dsNames     = new ArrayList<String>();
		for (ServiceReference<DataSource> ref : dataSources)
		{
			dsNames.add(getJndiName(ref));
		}
		return dsNames;
	}

	/**
	 * @return services references available in osgi registry
	 */
	public Collection<ServiceReference<DataSource>> getDataSources()
	{
		try
		{
			Collection<ServiceReference<DataSource>> dsRefs = context.getServiceReferences(DataSource.class, null);
			if (dsRefs == null)
			{
				dsRefs = new ArrayList<ServiceReference<DataSource>>();
			}
			return dsRefs;
		}
		catch (InvalidSyntaxException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param pServiceRef
	 *          the {@link ServiceReference} associated to the datasource
	 * @return jndi name declared
	 */
	private String getJndiName(final ServiceReference<DataSource> pServiceRef)
	{
		return (String) pServiceRef.getProperty("osgi.jndi.service.name");
	}

	/**
	 * Select a datasource with its jndi name. The next command will be execute on it.
	 * 
	 * @param pName
	 *          the datasource name
	 */
	public void selectDataSource(final String pName)
	{
		Collection<ServiceReference<DataSource>> dataSources = getDataSources();
		for (ServiceReference<DataSource> ref : dataSources)
		{
			String jndiName = getJndiName(ref);
			if (pName.equals(jndiName))
			{
				DataSource ds = context.getService(ref);
				dbAccess.setDataSource(ds);
				return;
			}
		}
		throw new RuntimeException(String.format("No DataSource with name [%s] found", pName));
	}

	/**
	 * Get complete info about a datasource
	 * 
	 * @param pServiceReference
	 *          the {@link ServiceReference} associated to the datasource
	 * @return {@link DataSourceInfo} retrieve
	 */
	public DataSourceInfo getDataSourceInfo(final ServiceReference<DataSource> pServiceReference)
	{
		DataSourceInfo info = new DataSourceInfo();
		info.setJndiName(getJndiName(pServiceReference));
		DataSource ds = context.getService(pServiceReference);
		info.setSelected((ds == dbAccess.getDataSource()));
		try
		{
			Connection con = ds.getConnection();
			DatabaseMetaData dbMeta = con.getMetaData();
			info.setProduct(dbMeta.getDatabaseProductName());
			info.setVersion(dbMeta.getDatabaseProductVersion());
			info.setUrl(dbMeta.getURL());
		}
		catch (Exception e)
		{
			info.setUrl(String.format("Connect failed [%s]", e.getMessage()));
		}
		return info;
	}

}
