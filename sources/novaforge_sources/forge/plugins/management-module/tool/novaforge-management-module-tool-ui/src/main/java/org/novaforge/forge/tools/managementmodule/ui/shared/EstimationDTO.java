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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

/**
 * @author BILET-JC
 *
 */
public class EstimationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6287839219289409071L;
	
	//about the scope unit
	private ScopeUnitDTO scopeUnit;
	private Integer lastCharge;
	private Integer charge;
	private Float remaining;
	private String participatingClasses;
	private Integer numberAttributs;

	//function points
	private Integer pfRaw;
	private Integer pfAdjusted;
//	private float adjustementFactor;
	private Integer GDIsimple;
	private Integer GDImedian;
	private Integer GDIcomplex;
	private Integer GDEsimple;
	private Integer GDEmedian;
	private Integer GDEcomplex;
	private Integer INsimple;
	private Integer INmedian;
	private Integer INcomplex;
	private Integer OUTsimple;
	private Integer OUTmedian;
	private Integer OUTcomplex;
	private Integer InterrogationSimple;
	private Integer InterrogationMedian;
	private Integer InterrogationComplex;
	private Integer globalSimple;
	private Integer globalMedian;
	private Integer globalComplex;
	
	//other
	private Integer benefit;
	private Integer injury;
	private Integer risk;
	private Integer weight;
	private Boolean isManual;
	private ComponentEnum simple;
	/**
	 * @return the scopeUnit
	 */
	public ScopeUnitDTO getScopeUnit() {
		return scopeUnit;
	}
	/**
	 * @param scopeUnit the scopeUnit to set
	 */
	public void setScopeUnit(ScopeUnitDTO scopeUnit) {
		this.scopeUnit = scopeUnit;
	}
	/**
	 * @return the lastCharge
	 */
	public Integer getLastCharge() {
		return lastCharge;
	}
	/**
	 * @param lastCharge the lastCharge to set
	 */
	public void setLastCharge(Integer lastCharge) {
		this.lastCharge = lastCharge;
	}
	/**
	 * @return the charge
	 */
	public Integer getCharge() {
		return charge;
	}
	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Integer charge) {
		this.charge = charge;
	}
	
	/**
	 * @return the remaining
	 */
	public Float getRemaining() {
		return remaining;
	}
	/**
	 * @param remaining the remaining to set
	 */
	public void setRemaining(Float remaining) {
		this.remaining = remaining;
	}
	/**
	 * @return the participatingClasses
	 */
	public String getParticipatingClasses() {
		return participatingClasses;
	}
	/**
	 * @param participatingClasses the participatingClasses to set
	 */
	public void setParticipatingClasses(String participatingClasses) {
		this.participatingClasses = participatingClasses;
	}
	/**
	 * @return the numberAttributs
	 */
	public Integer getNumberAttributs() {
		return numberAttributs;
	}
	/**
	 * @param numberAttributs the numberAttributs to set
	 */
	public void setNumberAttributs(Integer numberAttributs) {
		this.numberAttributs = numberAttributs;
	}
	/**
	 * @return the pfRaw
	 */
	public Integer getPfRaw() {
		return pfRaw;
	}
	/**
	 * @param pfRaw the pfRaw to set
	 */
	public void setPfRaw(Integer pfRaw) {
		this.pfRaw = pfRaw;
	}
	/**
	 * @return the pfAdjusted
	 */
	public Integer getPfAdjusted() {
		return pfAdjusted;
	}
	/**
	 * @param pfAdjusted the pfAdjusted to set
	 */
	public void setPfAdjusted(Integer pfAdjusted) {
		this.pfAdjusted = pfAdjusted;
	}
	/**
	 * @return the adjustementFactor
	 */
//	public float getAdjustementFactor() {
//		return adjustementFactor;
//	}
	/**
	 * @param adjustementFactor the adjustementFactor to set
	 */
//	public void setAdjustementFactor(float adjustementFactor) {
//		this.adjustementFactor = adjustementFactor;
//	}
	/**
	 * @return the gDIsimple
	 */
	public Integer getGDIsimple() {
		return GDIsimple;
	}
	/**
	 * @param gDIsimple the gDIsimple to set
	 */
	public void setGDIsimple(Integer gDIsimple) {
		GDIsimple = gDIsimple;
	}
	/**
	 * @return the gDImedian
	 */
	public Integer getGDImedian() {
		return GDImedian;
	}
	/**
	 * @param gDImedian the gDImedian to set
	 */
	public void setGDImedian(Integer gDImedian) {
		GDImedian = gDImedian;
	}
	/**
	 * @return the gDIcomplex
	 */
	public Integer getGDIcomplex() {
		return GDIcomplex;
	}
	/**
	 * @param gDIcomplex the gDIcomplex to set
	 */
	public void setGDIcomplex(Integer gDIcomplex) {
		GDIcomplex = gDIcomplex;
	}
	/**
	 * @return the gDEsimple
	 */
	public Integer getGDEsimple() {
		return GDEsimple;
	}
	/**
	 * @param gDEsimple the gDEsimple to set
	 */
	public void setGDEsimple(Integer gDEsimple) {
		GDEsimple = gDEsimple;
	}
	/**
	 * @return the gDEmedian
	 */
	public Integer getGDEmedian() {
		return GDEmedian;
	}
	/**
	 * @param gDEmedian the gDEmedian to set
	 */
	public void setGDEmedian(Integer gDEmedian) {
		GDEmedian = gDEmedian;
	}
	/**
	 * @return the gDEcomplex
	 */
	public Integer getGDEcomplex() {
		return GDEcomplex;
	}
	/**
	 * @param gDEcomplex the gDEcomplex to set
	 */
	public void setGDEcomplex(Integer gDEcomplex) {
		GDEcomplex = gDEcomplex;
	}
	/**
	 * @return the iNsimple
	 */
	public Integer getINsimple() {
		return INsimple;
	}
	/**
	 * @param iNsimple the iNsimple to set
	 */
	public void setINsimple(Integer iNsimple) {
		INsimple = iNsimple;
	}
	/**
	 * @return the iNmedian
	 */
	public Integer getINmedian() {
		return INmedian;
	}
	/**
	 * @param iNmedian the iNmedian to set
	 */
	public void setINmedian(Integer iNmedian) {
		INmedian = iNmedian;
	}
	/**
	 * @return the iNcomplex
	 */
	public Integer getINcomplex() {
		return INcomplex;
	}
	/**
	 * @param iNcomplex the iNcomplex to set
	 */
	public void setINcomplex(Integer iNcomplex) {
		INcomplex = iNcomplex;
	}
	/**
	 * @return the oUTsimple
	 */
	public Integer getOUTsimple() {
		return OUTsimple;
	}
	/**
	 * @param oUTsimple the oUTsimple to set
	 */
	public void setOUTsimple(Integer oUTsimple) {
		OUTsimple = oUTsimple;
	}
	/**
	 * @return the oUTmedian
	 */
	public Integer getOUTmedian() {
		return OUTmedian;
	}
	/**
	 * @param oUTmedian the oUTmedian to set
	 */
	public void setOUTmedian(Integer oUTmedian) {
		OUTmedian = oUTmedian;
	}
	/**
	 * @return the oUTcomplex
	 */
	public Integer getOUTcomplex() {
		return OUTcomplex;
	}
	/**
	 * @param oUTcomplex the oUTcomplex to set
	 */
	public void setOUTcomplex(Integer oUTcomplex) {
		OUTcomplex = oUTcomplex;
	}
	/**
	 * @return the IntegererrogationSimple
	 */
	public Integer getInterrogationSimple() {
		return InterrogationSimple;
	}
	/**
	 * @param IntegererrogationSimple the IntegererrogationSimple to set
	 */
	public void setInterrogationSimple(Integer IntegererrogationSimple) {
		this.InterrogationSimple = IntegererrogationSimple;
	}
	/**
	 * @return the IntegererrogationMedian
	 */
	public Integer getInterrogationMedian() {
		return InterrogationMedian;
	}
	/**
	 * @param IntegererrogationMedian the IntegererrogationMedian to set
	 */
	public void setInterrogationMedian(Integer IntegererrogationMedian) {
		this.InterrogationMedian = IntegererrogationMedian;
	}
	/**
	 * @return the IntegererrogationComplex
	 */
	public Integer getInterrogationComplex() {
		return InterrogationComplex;
	}
	/**
	 * @param IntegererrogationComplex the IntegererrogationComplex to set
	 */
	public void setInterrogationComplex(Integer IntegererrogationComplex) {
		this.InterrogationComplex = IntegererrogationComplex;
	}
	/**
	 * @return the globalSimple
	 */
	public Integer getGlobalSimple() {
		return globalSimple;
	}
	/**
	 * @param globalSimple the globalSimple to set
	 */
	public void setGlobalSimple(Integer globalSimple) {
		this.globalSimple = globalSimple;
	}
	/**
	 * @return the globalMedian
	 */
	public Integer getGlobalMedian() {
		return globalMedian;
	}
	/**
	 * @param globalMedian the globalMedian to set
	 */
	public void setGlobalMedian(Integer globalMedian) {
		this.globalMedian = globalMedian;
	}
	/**
	 * @return the globalComplex
	 */
	public Integer getGlobalComplex() {
		return globalComplex;
	}
	/**
	 * @param globalComplex the globalComplex to set
	 */
	public void setGlobalComplex(Integer globalComplex) {
		this.globalComplex = globalComplex;
	}
	/**
	 * @return the benefit
	 */
	public Integer getBenefit() {
		return benefit;
	}
	/**
	 * @param benefit the benefit to set
	 */
	public void setBenefit(Integer benefit) {
		this.benefit = benefit;
	}
	/**
	 * @return the injury
	 */
	public Integer getInjury() {
		return injury;
	}
	/**
	 * @param injury the injury to set
	 */
	public void setInjury(Integer injury) {
		this.injury = injury;
	}
	/**
	 * @return the risk
	 */
	public Integer getRisk() {
		return risk;
	}
	/**
	 * @param risk the risk to set
	 */
	public void setRisk(Integer risk) {
		this.risk = risk;
	}
	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	/**
	 * @return the isManual
	 */
	public Boolean isManual() {
		return isManual;
	}
	/**
	 * @param isManual the isManual to set
	 */
	public void setManual(Boolean isManual) {
		this.isManual = isManual;
	}
	/**
	 * @return the simple
	 */
	public ComponentEnum getSimple() {
		return simple;
	}
	/**
	 * @param simple the isSimple to set
	 */
	public void setSimple(ComponentEnum pSimple) {
		this.simple = pSimple;
	}
	

	
}
