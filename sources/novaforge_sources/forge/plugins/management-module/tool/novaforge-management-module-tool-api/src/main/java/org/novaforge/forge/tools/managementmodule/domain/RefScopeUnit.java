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

package org.novaforge.forge.tools.managementmodule.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * This class describes a ScopeUnit Reference imported from Obeo
 * 
 * @author fdemange
 * @author BILET-JC
 * 
 */
public interface RefScopeUnit extends Serializable
{

   String getUnitId();

   void setUnitId(String unitId);

   String getName();

   void setName(String name);

   String getDescription();

   void setDescription(String description);

   String getVersion();

   void setVersion(String version);

   ScopeType getType();


   void setType(ScopeType type);

   Set<RefScopeUnit> getChildScopeUnit();

   void setChildScopeUnit(Set<RefScopeUnit> childsScopeUnit);

   void addChildScopeUnit(RefScopeUnit refScopeUnit);

   void removeChildScopeUnit(RefScopeUnit refScopeUnit);

   Project getProject();

   void setProject(Project pProject);

   RefScopeUnit getParentScopeUnit();

   void setParentScopeUnit(RefScopeUnit parentScopeUnit);

   /**
    * Returns the benefit, priorization element of Phare method
    * @return Integer [1-5]
    */
	Integer getBenefit();
	
	/**
	 * Set the benefit, priorization element of Phare method
	 * @param benefit
	 */
	void setBenefit(Integer benefit);


   /**
    * Returns the injury, priorization element of Phare method
    * @return Integer [1-5]
    */
	Integer getInjury();
	
	/** 
	 * Set the injury, priorization element of Phare method
	 * @param injury
	 */
	void setInjury(Integer injury);


   /**
    * Returns the risk, priorization element of Phare method
    * @return Integer [1-5]
    */
	Integer getRisk();
	
	/**
	 * Set the risk, priorization element of Phare method
	 * @param risk
	 */
	void setRisk(Integer risk);

	/**
	 * return the creation date
	 * @return Date
	 */
	Date getDateCreated();

	/**
	 * Set the creation date of the ref_scop_unit
	 * @param Date
	 */
	void setDateCreated(Date dateCreated);

	/**
	 * return the modifcation date
	 * @return Date
	 */
	Date getDateModified();

	/**
	 * Set the modification date of the ref_scop_unit
	 * @param Date
	 */
	void setDateModified(Date dateModified);

	StatusScope getState();

  void setState(StatusScope state);

	CDOParameters getCdoParameters();

	void setCdoParameters(CDOParameters cdoParameters);


}
