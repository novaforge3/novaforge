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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ScopeUnitDTO implements Serializable
{


   /*
    * public static final String ISMANUAL_TRUE = "ISMANUAL_TRUE"; public static final String ISMANUAL_FALSE =
    * "ISMANUAL_FALSE"; public static final String ISMANUAL_BOTH = "ISMANUAL_BOTH";
    */

   /**
    *
    */
   private static final long serialVersionUID = 9178888934859887173L;
   private String             unitId;
   private String            name;
   private String             description;
   private Date              date;
   private String            type;
   private String            status;
   private String             version;
   private String             lotId;
   private String            lotName;
   private String             sousLotId;
   private String             sousLotName;
   private ScopeUnitDTO      parentScopeUnit;
   private boolean hasChild;
   private List<ScopeUnitDTO> childrenScopeUnit;
   private String            projectId;
   private String            projectName;
   private boolean           isManual;
   private boolean           isInScope;
   private boolean           isFinished;
   private String             refScopeUnitId;
   private String             refScopeUnitVersion;
   private List<Long>       listTaskId;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getUnitId()
   {
      return unitId;
   }

   public void setUnitId(String unitId)
   {
      this.unitId = unitId;
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public String getStatus()
   {
      return status;
   }

   public void setStatus(String status)
   {
      this.status = status;
   }
   
   public String getVersion()
   {
      return version;
   }

   public void setVersion(String version)
   {
      this.version = version;
   }

   public String getLotId()
   {
      return lotId;
   }

   public void setLotId(String lotId)
   {
      this.lotId = lotId;
   }

   public String getLotName()
   {
      return lotName;
   }

   public void setLotName(String lotName)
   {
      this.lotName = lotName;
   }

   public ScopeUnitDTO getParentScopeUnit()
   {
      return parentScopeUnit;
   }

   public void setParentScopeUnit(ScopeUnitDTO parentScopeUnit)
   {
      this.parentScopeUnit = parentScopeUnit;
   }

   public String getProjectId()
   {
      return projectId;
   }

   public void setProjectId(String projectId)
   {
      this.projectId = projectId;
   }

   public String getProjectName()
   {
      return projectName;
   }

   public void setProjectName(String projectName)
   {
      this.projectName = projectName;
   }

   public boolean isManual()
   {
      return isManual;
   }

   public void setManual(boolean isManual)
   {
      this.isManual = isManual;
   }

   public boolean isInScope()
   {
      return isInScope;
   }

   public void setInScope(boolean isInScope)
   {
      this.isInScope = isInScope;
   }

	public boolean isFinished() {
	return isFinished;
}

public void setFinished(boolean isFinished) {
	this.isFinished = isFinished;
}

	public boolean hasChild() {
		return hasChild;
	}
	
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}

	public List<ScopeUnitDTO> getChildrenScopeUnit()
   {
      return childrenScopeUnit;
   }

   public void setChildrenScopeUnit(List<ScopeUnitDTO> childrenScopeUnit)
   {
      this.childrenScopeUnit = childrenScopeUnit;
   }

   public String getParentLotId()
   {
      return sousLotId;
   }

   public void setParentLotId(String sousLotId)
   {
      this.sousLotId = sousLotId;
   }

   public String getParentLotName()
   {
      return sousLotName;
   }

   public void setParentLotName(String sousLotName)
   {
      this.sousLotName = sousLotName;
   }


   public String getRefScopeUnitId()
   {
      return refScopeUnitId;
   }

   public void setRefScopeUnitId(String refScopeUnitId)
   {
      this.refScopeUnitId = refScopeUnitId;
   }

   public String getRefScopeUnitVersion()
   {
      return refScopeUnitVersion;
   }

   public void setRefScopeUnitVersion(String refScopeUnitVersion)
   {
      this.refScopeUnitVersion = refScopeUnitVersion;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public List<Long> getListTaskId() {
      return listTaskId;
   }

   public void setListTaskId(List<Long> taskId) {
      this.listTaskId = taskId;
   }
   

@Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((date == null) ? 0 : date.hashCode());
      result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
      result = prime * result + (isInScope ? 1231 : 1237);
      result = prime * result + (isManual ? 1231 : 1237);
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((refScopeUnitId == null) ? 0 : refScopeUnitId.hashCode());
      result = prime * result + ((refScopeUnitVersion == null) ? 0 : refScopeUnitVersion.hashCode());
      result = prime * result + ((status == null) ? 0 : status.hashCode());
      result = prime * result + ((type == null) ? 0 : type.hashCode());
      result = prime * result + ((version == null) ? 0 : version.hashCode());
      return result;
   }


   @Override
   public boolean equals(Object obj)
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
      ScopeUnitDTO other = (ScopeUnitDTO) obj;
      if (date == null)
      {
         if (other.date != null)
         {
            return false;
         }
      }
      else if (!date.equals(other.date))
      {
         return false;
      }
      if (unitId == null)
      {
         if (other.unitId != null)
         {
            return false;
         }
      }
      else if (!unitId.equals(other.unitId))
      {
         return false;
      }
      if (isInScope != other.isInScope)
      {
         return false;
      }
      if (isManual != other.isManual)
      {
         return false;
      }
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      if (refScopeUnitId == null)
      {
         if (other.refScopeUnitId != null)
         {
            return false;
         }
      }
      else if (!refScopeUnitId.equals(other.refScopeUnitId))
      {
         return false;
      }
      if (refScopeUnitVersion == null)
      {
         if (other.refScopeUnitVersion != null)
         {
            return false;
         }
      }
      else if (!refScopeUnitVersion.equals(other.refScopeUnitVersion))
      {
         return false;
      }
      if (status == null)
      {
         if (other.status != null)
         {
            return false;
         }
      }
      else if (!status.equals(other.status))
      {
         return false;
      }
      if (type == null)
      {
         if (other.type != null)
         {
            return false;
         }
      }
      else if (!type.equals(other.type))
      {
         return false;
      }
      if (version == null)
      {
         if (other.version != null)
         {
            return false;
         }
      }
      else if (!version.equals(other.version))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "ScopeUnitDTO [id=" + unitId + ", name=" + name + ", date=" + date + ", TYPE=" + type
      + ", status="
      + status + ", version="
      + version + ", isManual=" + isManual + ", isInScope=" + isInScope + ", refScopeUnitId="
      + refScopeUnitId + ", refScopeUnitVersion=" + refScopeUnitVersion + "]";
   }


}
