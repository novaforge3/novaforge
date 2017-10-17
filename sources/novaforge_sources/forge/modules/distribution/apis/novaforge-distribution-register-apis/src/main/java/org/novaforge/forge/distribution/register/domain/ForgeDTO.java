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

package org.novaforge.forge.distribution.register.domain;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 29 déc. 2011
 */
public class ForgeDTO implements Serializable
{

	private static final long	serialVersionUID	= 1552936238282672065L;

	private UUID					forgeId;

	private String					label;

	private String					description;

	private int						forgeLevel;

	private Date					affiliationDate;

	private String					portalUri;

	private URL						forgeUrl;

	private String					certificatePublicKey;

	private ForgeDTO				parent;

	private Set<ForgeDTO>		children				= new HashSet<ForgeDTO>(0);

	public String getLabel()
	{
		return label;
	}

	public void setLabel(final String label)
	{
		this.label = label;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public int getForgeLevel()
	{
		return forgeLevel;
	}

	public void setForgeLevel(final int forgeLevel)
	{
		this.forgeLevel = forgeLevel;
	}

	public Date getAffiliationDate()
	{
		return affiliationDate;
	}

	public void setAffiliationDate(final Date affiliationDate)
	{
		this.affiliationDate = affiliationDate;
	}

	/**
	 * @return the Portal Url
	 */
	public String getPortalUri()
	{
		return portalUri;
	}

	public void setPortalUri(final String pPortalUri)
	{
		portalUri = pPortalUri;
	}

	public String getCertificatePublicKey()
	{
		return certificatePublicKey;
	}

	public void setCertificatePublicKey(final String certificatePublicKey)
	{
		this.certificatePublicKey = certificatePublicKey;
	}

	public ForgeDTO getParent()
	{
		return parent;
	}

	public void setParent(final ForgeDTO parent)
	{
		this.parent = parent;
	}

	public Set<ForgeDTO> getChildren()
	{
		return children;
	}

	public void setChildren(final Set<ForgeDTO> children)
	{
		this.children = children;
	}

	public void addChild(final ForgeDTO child)
	{
		children.add(child);
	}

	public void removeChild(final ForgeDTO child)
	{
		for (final ForgeDTO chd : children)
		{
			if (chd.getForgeId().equals(child.getForgeId()))
			{
				System.err.println("child to remove found");

				children.remove(chd);
				return;
			}
		}

	}

	public UUID getForgeId()
	{
		return forgeId;
	}

	public void setForgeId(final UUID forgeId)
	{
		this.forgeId = forgeId;
	}

	/**
	 * @return the httpServerName
	 */
	public URL getForgeUrl()
	{
		return forgeUrl;
	}

	/**
	 * @param pForgeUrl
	 *           the httpServerName to set
	 */
	public void setForgeUrl(final URL pForgeUrl)
	{
		forgeUrl = pForgeUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "ForgeDTO [forgeId=" + forgeId + ", label=" + label + ", description=" + description + ", forgeLevel="
							 + forgeLevel + ", affiliationDate=" + affiliationDate + ", forgeUrl=" + forgeUrl + ", portalUri="
							 + portalUri + ", children=" + children + "]";
	}
}
