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
public class EstimationComponentDTO implements Serializable {

   /**
	 * 
	 */
   private static final long serialVersionUID = 7580135958851933755L;

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
   private Integer INTsimple;
   private Integer INTmedian;
   private Integer INTcomplex;
   private Float GDI;
   private Float GDE;
   private Float IN;
   private Float OUT;
   private Float INT;
   private float abacusChargeMenByDay;
   private float adjustementCoef;
   private Float nbHourByDay;

   public EstimationComponentDTO() {
      super();
   }

   /**
    * @return the gDIsimple
    */
   public Integer getGDIsimple() {
      return GDIsimple;
   }

   /**
    * @param gDIsimple
    *           the gDIsimple to set
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
    * @param gDImedian
    *           the gDImedian to set
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
    * @param gDIcomplex
    *           the gDIcomplex to set
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
    * @param gDEsimple
    *           the gDEsimple to set
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
    * @param gDEmedian
    *           the gDEmedian to set
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
    * @param gDEcomplex
    *           the gDEcomplex to set
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
    * @param iNsimple
    *           the iNsimple to set
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
    * @param iNmedian
    *           the iNmedian to set
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
    * @param iNcomplex
    *           the iNcomplex to set
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
    * @param oUTsimple
    *           the oUTsimple to set
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
    * @param oUTmedian
    *           the oUTmedian to set
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
    * @param oUTcomplex
    *           the oUTcomplex to set
    */
   public void setOUTcomplex(Integer oUTcomplex) {
      OUTcomplex = oUTcomplex;
   }

   /**
    * @return the iNTsimple
    */
   public Integer getINTsimple() {
      return INTsimple;
   }

   /**
    * @param iNTsimple
    *           the iNTsimple to set
    */
   public void setINTsimple(Integer iNTsimple) {
      INTsimple = iNTsimple;
   }

   /**
    * @return the iNTmedian
    */
   public Integer getINTmedian() {
      return INTmedian;
   }

   /**
    * @param iNTmedian
    *           the iNTmedian to set
    */
   public void setINTmedian(Integer iNTmedian) {
      INTmedian = iNTmedian;
   }

   /**
    * @return the iNTcomplex
    */
   public Integer getINTcomplex() {
      return INTcomplex;
   }

   /**
    * @param iNTcomplex
    *           the iNTcomplex to set
    */
   public void setINTcomplex(Integer iNTcomplex) {
      INTcomplex = iNTcomplex;
   }

   /**
    * @return the gDI
    */
   public Float getGDI() {
      return GDI;
   }

   /**
    * @param gDI
    *           the gDI to set
    */
   public void setGDI(Float gDI) {
      GDI = gDI;
   }

   /**
    * @return the gDE
    */
   public Float getGDE() {
      return GDE;
   }

   /**
    * @param gDE
    *           the gDE to set
    */
   public void setGDE(Float gDE) {
      GDE = gDE;
   }

   /**
    * @return the iN
    */
   public Float getIN() {
      return IN;
   }

   /**
    * @param iN
    *           the iN to set
    */
   public void setIN(Float iN) {
      IN = iN;
   }

   /**
    * @return the oUT
    */
   public Float getOUT() {
      return OUT;
   }

   /**
    * @param oUT
    *           the oUT to set
    */
   public void setOUT(Float oUT) {
      OUT = oUT;
   }

   /**
    * @return the iNT
    */
   public Float getINT() {
      return INT;
   }

   /**
    * @param iNT
    *           the iNT to set
    */
   public void setINT(Float iNT) {
      INT = iNT;
   }

   /**
    * @return the abacusChargeMenByDay
    */
   public float getAbacusChargeMenByDay() {
      return abacusChargeMenByDay;
   }

   /**
    * @param abacusChargeMenByDay
    *           the abacusChargeMenByDay to set
    */
   public void setAbacusChargeMenByDay(float abacusChargeMenByDay) {
      this.abacusChargeMenByDay = abacusChargeMenByDay;
   }
	/**
	 * @return the adjustementCoef
	 */
	public float getAdjustementCoef() {
		return adjustementCoef;
	}
	/**
	 * @param adjustementCoef the adjustementCoef to set
	 */
	public void setAdjustementCoef(float adjustementCoef) {
		this.adjustementCoef = adjustementCoef;
	}
	/**
	 * @return the nbHourByDay
	 */
	public Float getNbHourByDay() {
		return nbHourByDay;
	}
	/**
	 * @param nbHourByDay the nbHourByDay to set
	 */
	public void setNbHourByDay(Float nbHourByDay) {
		this.nbHourByDay = nbHourByDay;
	}

}
