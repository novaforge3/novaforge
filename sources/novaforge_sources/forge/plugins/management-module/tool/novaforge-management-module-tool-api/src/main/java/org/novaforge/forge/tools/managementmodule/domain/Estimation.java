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

/**
 * This class describes the estimation operation
 * 
 * @author BILET-JC
 * 
 */
public interface Estimation extends Serializable
{
	Long getId();
	
	void setId(Long pId);
	
	ScopeUnit getScopeUnit();
	
	void setScopeUnit(ScopeUnit pScopeUnit);

	Integer getGDIsimple();
	
	void setGDIsimple(Integer pGDIsimple);

	Integer getGDImedian();
	
	void setGDImedian(Integer pGDImedian);

	Integer getGDIcomplex();
	
	void setGDIcomplex(Integer pGDIcomplex);

	Integer getGDEsimple();
	
	void setGDEsimple(Integer pGDEsimple);

	Integer getGDEmedian();
	
	void setGDEmedian(Integer pGDEmedian);
	
	Integer getGDEcomplex();
	
	void setGDEcomplex(Integer pGDEcomplex);
	
	Integer getINsimple();
	
	void setINsimple(Integer pINsimple);
	
	Integer getINmedian();
	
	void setINmedian(Integer pINmedian);
	
	Integer getINcomplex();
	
	void setINcomplex(Integer pINcomplex);
	
	Integer getOUTsimple();
	
	void setOUTsimple(Integer pOUTsimple);
	
	Integer getOUTmedian();
	
	void setOUTmedian(Integer pOUTmedian);
	
	Integer getOUTcomplex();
	
	void setOUTcomplex(Integer pOUTcomplex);
	
	Integer getINTsimple();
	
	void setINTsimple(Integer pINTsimple);
	
	Integer getINTmedian();
	
	void setINTmedian(Integer pINTmedian);
	
	Integer getINTcomplex();
	
	void setINTcomplex(Integer pINTcomplex);
	
	Integer getGlobalSimple();
	
	void setGlobalSimple(Integer pGlobalSimple);
	
	Integer getGlobalMedian();
	
	void setGlobalMedian(Integer pGlobalMedian);
	
	Integer getGlobalComplex();
	
	void setGlobalComplex(Integer pGlobalComplex);

	Integer getFPRaw();
	
	void setFPRaw(Integer pFPRaw);
	
	Integer getBenefit();
	
	void setBenefit(Integer pBenefit);
	
	Integer getInjury();
	
	void setInjury(Integer pInjury);

	Integer getRisk();
	
	void setRisk(Integer pRisk);

	Integer getWeight();

	void setWeight(Integer weight);
	
	/**
	 * Indicates if the estimation charge is manual or with function points
	 * @return boolean, true if manual
	 */
	boolean isManual();

	void setManual(boolean pIsManual);

	/**
	 * When estimation charge is not manual, indicates if function points are simple or detail way
	 * If simple, the param is the reference component which is use to count charge 
	 * @return
	 */
	String getSimple();

	void setSimple(String pIsSimple);
	
	Integer getLastCharge();
	
	void setCharge(Integer pCharge);

	Date getLastDate();

	void setLastDate(Date pLastDate);

	Float getRemaining();

	void setRemaining(Float remaining);
	
}
