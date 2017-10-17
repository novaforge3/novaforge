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
package org.novaforge.forge.tests.jpa.naming;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Hashtable;

/**
 * This factory is used to create a {@link InitialContextFactoryBuilder} based on {@link MemoryContextFactory}
 * 
 * @author Guillaume Lamirand
 */
public class MemoryContextFactoryBuilder implements InitialContextFactoryBuilder
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InitialContextFactory createInitialContextFactory(Hashtable<?, ?> env) throws NamingException
	{
		InitialContextFactory returnFactory = null;
		String requestedFactory = null;
		if (env != null)
		{
			requestedFactory = (String) env.get(Context.INITIAL_CONTEXT_FACTORY);
		}
		if (requestedFactory != null)
		{
			returnFactory = retrieveRequestedFactory(requestedFactory);
		}
		else
		{
			returnFactory = new MemoryContextFactory();
		}
		return returnFactory;
	}

	private InitialContextFactory retrieveRequestedFactory(String requestedFactory)
			throws NoInitialContextException
	{
		try
		{
			return (InitialContextFactory) loadClass(requestedFactory).newInstance();
		}
		catch (Exception e)
		{
			NoInitialContextException ne = new NoInitialContextException("Cannot instantiate class: "
					+ requestedFactory);
			ne.setRootCause(e);
			throw ne;
		}
	}

	private Class<?> loadClass(String className) throws ClassNotFoundException
	{
		ClassLoader cl = getContextClassLoader();
		return Class.forName(className, true, cl);
	}

	private ClassLoader getContextClassLoader()
	{
		return (AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
		{
			@Override
			public ClassLoader run()
			{
				return Thread.currentThread().getContextClassLoader();
			}
		}));
	}

}