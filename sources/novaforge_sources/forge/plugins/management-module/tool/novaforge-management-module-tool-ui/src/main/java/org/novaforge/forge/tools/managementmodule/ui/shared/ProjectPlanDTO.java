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

public class ProjectPlanDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5299874284324395638L;
	
	private Long projectPlanId;
    private Integer version;
    private Date   date;
    private String statusLabel;
    private ProjectPlanStatus status;
    private String projectId;
    private String projectName;
    private List<AdjustFactorJointureDTO> adjustFactorJointureList;
    private EstimationComponentDetailDTO estimationComponentDetail;
    
    
    public ProjectPlanDTO() {
       super();
    }

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String status) {
		this.statusLabel = status;
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String idProject) {
		this.projectId = idProject;
	}
	 
	public Long getProjectPlanId() {
		return projectPlanId;
	}
	public void setProjectPlanId(Long projectPlanId) {
		this.projectPlanId = projectPlanId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
    * @return the status
    */
   public ProjectPlanStatus getStatus() {
      return status;
   }

   /**
    * @param status the status to set
    */
   public void setStatus(ProjectPlanStatus status) {
      this.status = status;
   }
   
   

	   /**
	 * @return the adjustFactorJointureList
	 */
	public List<AdjustFactorJointureDTO> getAdjustFactorJointureList() {
		return adjustFactorJointureList;
	}
	
	/**
	 * @param adjustFactorJointureList the adjustFactorJointureList to set
	 */
	public void setAdjustFactorJointureList(
			List<AdjustFactorJointureDTO> adjustFactorJointureList) {
		this.adjustFactorJointureList = adjustFactorJointureList;
}

/**
 * @return the estimationComponentDetail
 */
public EstimationComponentDetailDTO getEstimationComponentDetail() {
	return estimationComponentDetail;
}

/**
 * @param estimationComponentDetail the estimationComponentDetail to set
 */
public void setEstimationComponentDetail(
		EstimationComponentDetailDTO estimationComponentDetail) {
	this.estimationComponentDetail = estimationComponentDetail;
}

@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((projectId == null) ? 0 : projectId.hashCode());
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result
				+ ((projectPlanId == null) ? 0 : projectPlanId.hashCode());
		result = prime * result + ((statusLabel == null) ? 0 : statusLabel.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ProjectPlanDTO other = (ProjectPlanDTO) obj;
		if (date == null) {
			if (other.date != null) {
            return false;
         }
		} else if (!date.equals(other.date)) {
         return false;
      }
		if (projectId == null) {
			if (other.projectId != null) {
            return false;
         }
		} else if (!projectId.equals(other.projectId)) {
         return false;
      }
		if (projectName == null) {
			if (other.projectName != null) {
            return false;
         }
		} else if (!projectName.equals(other.projectName)) {
         return false;
      }
		if (projectPlanId == null) {
			if (other.projectPlanId != null) {
            return false;
         }
		} else if (!projectPlanId.equals(other.projectPlanId)) {
         return false;
      }
		if (statusLabel == null) {
			if (other.statusLabel != null) {
            return false;
         }
		} else if (!statusLabel.equals(other.statusLabel)) {
         return false;
      }
		if (version == null) {
			if (other.version != null) {
            return false;
         }
		} else if (!version.equals(other.version)) {
         return false;
      }
		return true;
	}
	@Override
	public String toString() {
		return "ProjectPlanDTO [projectPlanId=" + projectPlanId + ", version="
				+ version + ", date=" + date + ", status=" + statusLabel
				+ ", projectId=" + projectId + ", projectName=" + projectName
				+ "]";
	}
	
	
    
}
