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



public class TransformationDTO implements Serializable {
	



	/**
	 * 
	 */
	private static final long serialVersionUID = 6436280800902162725L;
	int nbJoursAn;
	int nbJoursMois;
	int nbJoursSemaine;
	int nbHeuresJour;
	int  nbJoursNonTravail;
	private Long id;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nbJoursAn
	 */
	public int getNbJoursAn() {
		return nbJoursAn;
	}

	/**
	 * @param nbJoursAn the nbJoursAn to set
	 */
	public void setNbJoursAn(int nbJoursAn) {
		this.nbJoursAn = nbJoursAn;
	}

	/**
	 * @return the nbJoursMois
	 */
	public int getNbJoursMois() {
		return nbJoursMois;
	}

	/**
	 * @param nbJoursMois the nbJoursMois to set
	 */
	public void setNbJoursMois(int nbJoursMois) {
		this.nbJoursMois = nbJoursMois;
	}

	/**
	 * @return the nbJoursSemaine
	 */
	public int getNbJoursSemaine() {
		return nbJoursSemaine;
	}

	/**
	 * @param nbJoursSemaine the nbJoursSemaine to set
	 */
	public void setNbJoursSemaine(int nbJoursSemaine) {
		this.nbJoursSemaine = nbJoursSemaine;
	}

	/**
	 * @return the nbHeuresJour
	 */
	public int getNbHeuresJour() {
		return nbHeuresJour;
	}

	/**
	 * @param nbHeuresJour the nbHeuresJour to set
	 */
	public void setNbHeuresJour(int nbHeuresJour) {
		this.nbHeuresJour = nbHeuresJour;
	}

	/**
	 * @return the nbJoursNonTravail
	 */
	public int getNbJoursNonTravail() {
		return nbJoursNonTravail;
	}

	/**
	 * @param nbJoursNonTravail the nbJoursNonTravail to set
	 */
	public void setNbJoursNonTravail(int nbJoursNonTravail) {
		this.nbJoursNonTravail = nbJoursNonTravail;
	}
	
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + nbHeuresJour;
      result = prime * result + nbJoursAn;
      result = prime * result + nbJoursMois;
      result = prime * result + nbJoursNonTravail;
      result = prime * result + nbJoursSemaine;
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
      TransformationDTO other = (TransformationDTO) obj;
      if (nbHeuresJour != other.nbHeuresJour) {
         return false;
      }
      if (nbJoursAn != other.nbJoursAn) {
         return false;
      }
      if (nbJoursMois != other.nbJoursMois) {
         return false;
      }
      if (nbJoursNonTravail != other.nbJoursNonTravail) {
         return false;
      }
		 return nbJoursSemaine == other.nbJoursSemaine;
	 }

   @Override
	public String toString() {
		return "TransformationDTO [id=" + id + ", nbJoursAn=" + nbJoursAn
				+ ", nbJoursMois=" + nbJoursMois + ", nbJoursSemaine="
				+ nbJoursSemaine + ", nbHeuresJour=" + nbHeuresJour
				+ ", nbJoursNonTravail=" + nbJoursNonTravail + "]";
	}

}
