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
import java.util.Set;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 26 déc. 2011
 */
public interface Forge extends Serializable
{
	UUID getForgeId();

	void setForgeId(UUID forgeId);

	String getLabel();

	void setLabel(String label);

	String getDescription();

	void setDescription(String description);

	int getForgeLevel();

	void setForgeLevel(int forgeLevel);

	Date getAffiliationDate();

	void setAffiliationDate(Date affiliationDate);

	String getPortalUri();

	void setPortalUri(String url);

	URL getForgeUrl();

	void setForgeUrl(URL pForgeUrl);

	String getCertificatePublicKey();

	void setCertificatePublicKey(String certificatePublicKey);

	Set<Forge> getChildren();

	void setChildren(Set<Forge> forges);

	void addChild(Forge forge);

	void removeChild(Forge forge);

	Forge getParent();

	void setParent(Forge forge);

}
