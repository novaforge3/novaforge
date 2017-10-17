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

import org.osjava.sj.memory.MemoryContext;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * {@link InitialContextFactory} based on {@link MemoryContext}
 * 
 * @author Guillaume Lamirand
 */
public class MemoryContextFactory implements InitialContextFactory
{

	/**
	 * Used by {@link MemoryContext} to overide default property
	 */
	private static final String	SHARE_DATA_PROPERTY	= "org.osjava.sj.jndi.shared";

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Context getInitialContext(Hashtable environment) throws NamingException
	{
		Hashtable sharingEnv = (Hashtable) environment.clone();

		// all instances will share data
		if (!sharingEnv.containsKey(SHARE_DATA_PROPERTY))
		{
			sharingEnv.put(SHARE_DATA_PROPERTY, "true");
		}

		return new MemoryContext(sharingEnv);
	}

}
