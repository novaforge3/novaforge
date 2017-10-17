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
package org.novaforge.forge.commands.datasource.model;

/**
 * Defines a datasource object
 * 
 * @author Guillaume Lamirand
 */
public class DataSourceInfo
{
	/**
	 * Define if the current datasource is selected
	 */
	private boolean	selected;
	/**
	 * Define jndi name to contact datasource
	 */
	private String	jndiName;
	/**
	 * Define product used by the datasource (Mysql, derby, etc.)
	 */
	private String	product;
	/**
	 * Define le product version
	 */
	private String	version;
	/**
	 * Define the url to contact product
	 */
	private String	url;

	/**
	 * @return the selected
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * @param pSelected
	 *          the selected to set
	 */
	public void setSelected(boolean pSelected)
	{
		selected = pSelected;
	}

	/**
	 * @return the jndiName
	 */
	public String getJndiName()
	{
		return jndiName;
	}

	/**
	 * @param pJndiName
	 *          the jndiName to set
	 */
	public void setJndiName(String pJndiName)
	{
		jndiName = pJndiName;
	}

	/**
	 * @return the product
	 */
	public String getProduct()
	{
		return product;
	}

	/**
	 * @param pProduct
	 *          the product to set
	 */
	public void setProduct(String pProduct)
	{
		product = pProduct;
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param pVersion
	 *          the version to set
	 */
	public void setVersion(String pVersion)
	{
		version = pVersion;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param pUrl
	 *          the url to set
	 */
	public void setUrl(String pUrl)
	{
		url = pUrl;
	}
}
