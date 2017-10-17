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

public class MarkerDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -4327675453970049126L;

   private Long id;
   private String name;
   private Date date;
   private Long pPlanId;
   private String desc;
   private String markerTypeFunctionalId;
   private String markerTypeName;

   public MarkerDTO(Long id, String name, Date date, Long pPlanId, String desc, String markerTypeFunctionalId, String markerTypeName) {
      super();
      this.id = id;
      this.name = name;
      this.date = date;
      this.pPlanId = pPlanId;
      this.desc = desc;
      this.markerTypeFunctionalId = markerTypeFunctionalId;
      this.markerTypeName = markerTypeName;
   }

   public MarkerDTO() {
      super();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date endDate)
   {
      this.date = endDate;
   }

   public String getMarkerTypeFunctionalId() {
	   return markerTypeFunctionalId;
   }

   public void setMarkerTypeFunctionalId(String markerTypeFunctionalId) {
	   this.markerTypeFunctionalId = markerTypeFunctionalId;
   }

   public String getMarkerTypeName() {
	   return markerTypeName;
   }

   public void setMarkerTypeName(String markerTypeName) {
	   this.markerTypeName = markerTypeName;
   }

   public Long getpPlanId() {
      return pPlanId;
   }

   public void setpPlanId(Long pPlanId) {
      this.pPlanId = pPlanId;
   }

   public String getDesc()
   {
      return desc;
   }

   public void setDesc(String pDesc) {
      this.desc = pDesc;
   }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((markerTypeFunctionalId == null) ? 0 : markerTypeFunctionalId.hashCode());

		return result;
	}

   @Override
	public boolean equals(Object obj) {
		if (this == obj) {
         return true;
      }
		if (obj == null) {
         return false;
      }
		if (getClass() != obj.getClass()) {
         return false;
      }
		MarkerDTO other = (MarkerDTO) obj;
		if (desc == null) {
			if (other.desc != null) {
            return false;
         }
		} else if (!desc.equals(other.desc)) {
         return false;
      }
		if (date == null) {
			if (other.date != null) {
            return false;
         }
		} else if (!date.equals(other.date)) {
         return false;
      }
		if (name == null) {
			if (other.name != null) {
            return false;
         }
		} else if (!name.equals(other.name)) {
         return false;
      }
		if (markerTypeFunctionalId == null) {
			if (other.markerTypeFunctionalId != null) {
            return false;
         }
		} else if (!markerTypeFunctionalId.equals(other.markerTypeFunctionalId)) {
         return false;
      }

      return true;
	}

   @Override
   public String toString()
   {
      return "LotDTO [id=" + id + ", name=" + name + ", date=" + date + ", pPlanId=" + pPlanId + ", desc=" + desc
                 + ", markerTypeFunctionalId=" + markerTypeFunctionalId + ", markerTypeName=" + markerTypeName + "]";
   }
}
