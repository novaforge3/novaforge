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
package org.novaforge.forge.tools.managementmodule.entity;

import org.novaforge.forge.tools.managementmodule.domain.SteeringParameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "steering_parameter")
@NamedQuery(name = "SteeringParameterEntity.findAll", query = "SELECT p FROM SteeringParameterEntity p")
public class SteeringParameterEntity implements SteeringParameter, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7825788028495989039L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "days", nullable = false, unique = true, updatable = false)
	private String						days;

	@Column(name = "etp", nullable = false)
	private String						etp;

	@Column(name = "csimple", nullable = false)
	private String						csimple;

	@Column(name = "cmid", nullable = false)
	private String						cmid;

	@Column(name = "ccomplex", nullable = false, unique = true, updatable = false)
	private String						ccomplex;

	@Column(name = "msimple", nullable = false)
	private String						msimple;

	@Column(name = "mmid", nullable = false)
	private String						mmid;

	@Column(name = "mcomplex", nullable = false)
	private String						mcomplex;

	@Column(name = "gdivalue", nullable = false)
	private String						gdivalue;

	@Column(name = "abaque", nullable = false)
	private String						abaque;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public String getDays()
	{
		return days;
	}

	@Override
	public void setDays(final String days)
	{
		this.days = days;
	}

	@Override
	public String getEtp()
	{
		return etp;
	}

	@Override
	public void setEtp(final String etp)
	{
		this.etp = etp;
	}

	@Override
	public String getCsimple()
	{
		return csimple;
	}

	@Override
	public void setCsimple(final String csimple)
	{
		this.csimple = csimple;
	}

	@Override
	public String getCmid()
	{
		return cmid;
	}

	@Override
	public void setCmid(final String cmid)
	{
		this.cmid = cmid;
	}

	@Override
	public String getCcomplex()
	{
		return ccomplex;
	}

	@Override
	public void setCcomplex(final String ccomplex)
	{
		this.ccomplex = ccomplex;
	}

	@Override
	public String getMsimple()
	{
		return msimple;
	}

	@Override
	public void setMsimple(final String msimple)
	{
		this.msimple = msimple;
	}

	@Override
	public String getMmid()
	{
		return mmid;
	}

	@Override
	public void setMmid(final String mmid)
	{
		this.mmid = mmid;
	}

	@Override
	public String getMcomplex()
	{
		return mcomplex;
	}

	@Override
	public void setMcomplex(final String mcomplex)
	{
		this.mcomplex = mcomplex;
	}

	@Override
	public String getGdivalue()
	{
		return gdivalue;
	}

	@Override
	public void setGdivalue(final String gdivalue)
	{
		this.gdivalue = gdivalue;
	}

	@Override
	public String getAbaque()
	{
		return abaque;
	}

	@Override
	public void setAbaque(final String abaque)
	{
		this.abaque = abaque;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abaque == null) ? 0 : abaque.hashCode());
		result = prime * result + ((ccomplex == null) ? 0 : ccomplex.hashCode());
		result = prime * result + ((cmid == null) ? 0 : cmid.hashCode());
		result = prime * result + ((csimple == null) ? 0 : csimple.hashCode());
		result = prime * result + ((days == null) ? 0 : days.hashCode());
		result = prime * result + ((etp == null) ? 0 : etp.hashCode());
		result = prime * result + ((gdivalue == null) ? 0 : gdivalue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mcomplex == null) ? 0 : mcomplex.hashCode());
		result = prime * result + ((mmid == null) ? 0 : mmid.hashCode());
		result = prime * result + ((msimple == null) ? 0 : msimple.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		SteeringParameterEntity other = (SteeringParameterEntity) obj;
		if (abaque == null)
		{
			if (other.abaque != null)
			{
				return false;
			}
		}
		else if (!abaque.equals(other.abaque))
		{
			return false;
		}
		if (ccomplex == null)
		{
			if (other.ccomplex != null)
			{
				return false;
			}
		}
		else if (!ccomplex.equals(other.ccomplex))
		{
			return false;
		}
		if (cmid == null)
		{
			if (other.cmid != null)
			{
				return false;
			}
		}
		else if (!cmid.equals(other.cmid))
		{
			return false;
		}
		if (csimple == null)
		{
			if (other.csimple != null)
			{
				return false;
			}
		}
		else if (!csimple.equals(other.csimple))
		{
			return false;
		}
		if (days == null)
		{
			if (other.days != null)
			{
				return false;
			}
		}
		else if (!days.equals(other.days))
		{
			return false;
		}
		if (etp == null)
		{
			if (other.etp != null)
			{
				return false;
			}
		}
		else if (!etp.equals(other.etp))
		{
			return false;
		}
		if (gdivalue == null)
		{
			if (other.gdivalue != null)
			{
				return false;
			}
		}
		else if (!gdivalue.equals(other.gdivalue))
		{
			return false;
		}
		if (id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		}
		else if (!id.equals(other.id))
		{
			return false;
		}
		if (mcomplex == null)
		{
			if (other.mcomplex != null)
			{
				return false;
			}
		}
		else if (!mcomplex.equals(other.mcomplex))
		{
			return false;
		}
		if (mmid == null)
		{
			if (other.mmid != null)
			{
				return false;
			}
		}
		else if (!mmid.equals(other.mmid))
		{
			return false;
		}
		if (msimple == null)
		{
			if (other.msimple != null)
			{
				return false;
			}
		}
		else if (!msimple.equals(other.msimple))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "SteeringParameterEntity [id=" + id + ", days=" + days + ", etp=" + etp + ", csimple=" + csimple
				+ ", cmid=" + cmid + ", ccomplex=" + ccomplex + ", msimple=" + msimple + ", mmid=" + mmid
				+ ", mcomplex=" + mcomplex + ", gdivalue=" + gdivalue + ", abaque=" + abaque + "]";
	}
}
