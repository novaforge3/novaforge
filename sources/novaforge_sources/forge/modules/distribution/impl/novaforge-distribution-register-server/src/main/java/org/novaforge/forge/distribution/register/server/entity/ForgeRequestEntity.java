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
import org.novaforge.forge.distribution.register.domain.ForgeRequest;
import org.novaforge.forge.distribution.register.domain.RequestStatus;
import org.novaforge.forge.distribution.register.domain.RequestType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "FORGE_REQUEST")
@NamedQuery(name = "ForgeRequestEntity.findByUUID",
    query = "SELECT i FROM ForgeRequestEntity i WHERE i.forgeRequestId = :uuid")
public class ForgeRequestEntity implements ForgeRequest
{

  private static final long serialVersionUID = -5856520846211361430L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, updatable = false)
  private long              id;

  @Column(name = "forge_request_id", nullable = false, unique = true)
  private String            forgeRequestId;

  @Column(name = "request_type", nullable = false)
  @Enumerated
  private RequestType       requestType;

  @Column(name = "request_status", nullable = false)
  @Enumerated
  private RequestStatus     requestStatus;

  @Column(name = "request_comment")
  private String            requestComment;

  @Column(name = "request_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date              requestDate;

  @Column(name = "response_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date              responseDate;

  @OneToOne(cascade = { CascadeType.ALL }, targetEntity = ForgeEntity.class, orphanRemoval = true)
  private ForgeEntity       destinationForge;

  @OneToOne(cascade = { CascadeType.ALL }, targetEntity = ForgeEntity.class, orphanRemoval = true)
  private ForgeEntity       sourceForge;

  public long getId()
  {
    return id;
  }

  public void setId(final long id)
  {
    this.id = id;
  }

  @Override
  public UUID getForgeRequestId()
  {
    return UUID.fromString(forgeRequestId);
  }

  @Override
  public void setForgeRequestId(final UUID forgeRequestId)
  {
    this.forgeRequestId = forgeRequestId.toString();
  }

  @Override
  public RequestStatus getRequestStatus()
  {
    return requestStatus;
  }

  @Override
  public void setRequestStatus(final RequestStatus requestStatus)
  {
    this.requestStatus = requestStatus;
  }

  @Override
  public RequestType getRequestType()
  {
    return requestType;
  }

  @Override
  public void setRequestType(final RequestType requestType)
  {
    this.requestType = requestType;
  }

  @Override
  public String getRequestComment()
  {
    return requestComment;
  }

  @Override
  public void setRequestComment(final String requestComment)
  {
    this.requestComment = requestComment;
  }

  @Override
  public Date getRequestDate()
  {
    return requestDate;
  }

  @Override
  public void setRequestDate(final Date requestDate)
  {
    this.requestDate = requestDate;
  }

  @Override
  public Date getResponseDate()
  {
    return responseDate;
  }

  @Override
  public void setResponseDate(final Date responseDate)
  {
    this.responseDate = responseDate;
  }

  @Override
  public Forge getDestinationForge()
  {
    return destinationForge;
  }

  @Override
  public void setDestinationForge(final Forge destinationForge)
  {
    this.destinationForge = (ForgeEntity) destinationForge;

  }

  @Override
  public Forge getSourceForge()
  {
    return sourceForge;
  }

  @Override
  public void setSourceForge(final Forge sourceForge)
  {
    this.sourceForge = (ForgeEntity) sourceForge;
  }

}
