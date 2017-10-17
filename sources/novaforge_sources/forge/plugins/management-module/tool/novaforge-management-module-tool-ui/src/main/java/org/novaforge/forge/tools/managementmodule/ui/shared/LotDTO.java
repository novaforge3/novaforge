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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LotDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -4327675453970049126L;

   private Long lotId;
   private String name;
   private Date endDate;
   private Date startDate;
   private Long pPlanId;
   private Long parentLotId;
   private String parentLotName;
   private String desc;
   private List<LotDTO> childs;

   public LotDTO(Long lotId, String name, Date startDate, Date endDate, Long pPlanId, String desc, Long parentLotId, String parentLotName) {
      this.lotId = lotId;
      this.name = name;
      this.startDate = startDate;
      this.endDate = endDate;
      this.pPlanId = pPlanId;
      this.parentLotId = parentLotId;
      this.parentLotName = parentLotName;
      this.desc = desc;
      this.childs = new ArrayList<LotDTO>();
   }

   public LotDTO() {
      super();
   }

   public Long getLotId() {
      return lotId;
   }

   public void setLotId(Long lotId) {
      this.lotId = lotId;
   }
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public Date getStartDate() {
      return startDate;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   public Long getpPlanId() {
      return pPlanId;
   }

   public void setpPlanId(Long pPlanId) {
      this.pPlanId = pPlanId;
   }

   public Long getParentLotId() {
      return parentLotId;
   }

   public void setParentLotId(Long parentLotId) {
      this.parentLotId = parentLotId;
   }

   public String getParentLotName()
   {
      return parentLotName;
   }

   public void setParentLotName(String parentLotName)
   {
      this.parentLotName = parentLotName;
   }

   public String getDesc()
   {
      return desc;
   }

   public void setDesc(String pDesc) {
      this.desc = pDesc;
   }


   public List<LotDTO> getChilds() {
      return childs;
   }

   public void setChilds(List<LotDTO> childs) {
      this.childs = childs;
   }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		LotDTO other = (LotDTO) obj;
		if (desc == null) {
			if (other.desc != null) {
            return false;
         }
		} else if (!desc.equals(other.desc)) {
         return false;
      }
		if (endDate == null) {
			if (other.endDate != null) {
            return false;
         }
		} else if (!endDate.equals(other.endDate)) {
         return false;
      }
		if (name == null) {
			if (other.name != null) {
            return false;
         }
		} else if (!name.equals(other.name)) {
         return false;
      }
		if (startDate == null) {
			if (other.startDate != null) {
            return false;
         }
		} else if (!startDate.equals(other.startDate)) {
         return false;
      }
		return true;
	}

   @Override
   public String toString()
   {
      return "LotDTO [lotId=" + lotId + ", name=" + name + ", endDate=" + endDate + ", startDate=" + startDate
                 + ", pPlanId=" + pPlanId + ", parentLotId=" + parentLotId + ", parentLotName=" + parentLotName
                 + ", desc=" + desc + "]";
   }
}
