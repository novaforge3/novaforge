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
package org.novaforge.forge.distribution.register.server.entity;

import org.novaforge.forge.distribution.register.domain.Forge;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 29 déc. 2011
 */
@Entity
@Table(name = "FORGE")
@NamedQuery(name = "ForgeEntity.findByUUID", query = "SELECT i FROM ForgeEntity i WHERE i.forgeId = :uuid")
public class ForgeEntity implements Forge
{

  private static final long serialVersionUID = 1552936238282672065L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, updatable = false)
  private long              id;

  @Column(name = "forge_id", updatable = false, unique = true)
  private String            forgeId;

  @Column(name = "label", nullable = true)
  private String            label;

  @Column(name = "description", nullable = true)
  private String            description;

  @Column(name = "forge_level", nullable = false)
  private int               forgeLevel;

  @Column(name = "affiliation_date", nullable = true)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              affiliationDate;

  @Column(name = "portalUri", nullable = false)
  private String            portalUri;

  @Column(name = "forgeUrl", nullable = false)
  private String            forgeUrl;

  @Lob
  @Column(name = "certificate_public_key", nullable = true)
  private String            certificatePublicKey;

  @OneToMany(fetch = FetchType.EAGER, targetEntity = ForgeEntity.class, cascade = CascadeType.ALL)
  @JoinTable(name = "FORGE_PARENT_CHILD", joinColumns = { @JoinColumn(name = "parent_id") },
      inverseJoinColumns = { @JoinColumn(name = "child_id") })
  private Set<Forge>        children         = new HashSet<Forge>(0);

  @ManyToOne(targetEntity = ForgeEntity.class, cascade = CascadeType.ALL)
  @JoinTable(name = "FORGE_PARENT_CHILD", joinColumns = { @JoinColumn(name = "child_id") },
      inverseJoinColumns = { @JoinColumn(name = "parent_id") })
  private Forge             parent;

  public long getId()
  {
    return id;
  }

  public void setId(final long id)
  {
    this.id = id;
  }

  @Override
  public UUID getForgeId()
  {
    return UUID.fromString(forgeId);
  }

  @Override
  public void setForgeId(final UUID forgeId)
  {
    this.forgeId = forgeId.toString();
  }

  @Override
  public String getLabel()
  {
    return label;
  }

  @Override
  public void setLabel(final String label)
  {
    this.label = label;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setDescription(final String description)
  {
    this.description = description;
  }

  @Override
  public int getForgeLevel()
  {
    return forgeLevel;
  }

  @Override
  public void setForgeLevel(final int forgeLevel)
  {
    this.forgeLevel = forgeLevel;
  }

  @Override
  public Date getAffiliationDate()
  {
    return affiliationDate;
  }

  @Override
  public void setAffiliationDate(final Date affiliationDate)
  {
    this.affiliationDate = affiliationDate;
  }

  @Override
  public String getPortalUri()
  {
    return portalUri;
  }

  @Override
  public void setPortalUri(final String pPortalUri)
  {
    portalUri = pPortalUri;
  }

  @Override
  public URL getForgeUrl()
  {
    URL returnURl = null;
    try
    {
      returnURl = new URL(forgeUrl);
    }
    catch (final MalformedURLException e)
    {
      // Should'nt append
    }
    return returnURl;
  }

  @Override
  public void setForgeUrl(final URL pForgeUrl)
  {
    if (pForgeUrl != null)
    {
      forgeUrl = pForgeUrl.toExternalForm();
    }
  }

  @Override
  public String getCertificatePublicKey()
  {
    return certificatePublicKey;
  }

  @Override
  public void setCertificatePublicKey(final String certificatePublicKey)
  {
    this.certificatePublicKey = certificatePublicKey;
  }

  @Override
  public Set<Forge> getChildren()
  {
    return children;
  }

  @Override
  public void setChildren(final Set<Forge> children)
  {
    this.children = children;
  }

  @Override
  public void addChild(final Forge child)
  {
    children.add(child);
  }

  @Override
  public void removeChild(final Forge child)
  {
    for (final Forge chd : children)
    {
      if (chd.getForgeId().equals(child.getForgeId()))
      {
        children.remove(chd);
        return;
      }
    }

  }

  @Override
  public Forge getParent()
  {
    return parent;
  }

  @Override
  public void setParent(final Forge parent)
  {
    this.parent = parent;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ForgeEntity [id=" + id + ", forgeId=" + forgeId + ", label=" + label + ", description=" + description
               + ", forgeLevel=" + forgeLevel + ", affiliationDate=" + affiliationDate + ", portalUri=" + portalUri
               + ", forgeUrl=" + forgeUrl + ", certificatePublicKey=" + certificatePublicKey + "]";
  }
}
