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

package org.novaforge.forge.ui.distribution.shared.diffusion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of a Diffusion of the Referentiel Project onto its children forges.
 * 
 * @author rols-p
 */
public class DiffusionResultDTO implements Serializable
{
   /**
    * 
    */
   private static final long             serialVersionUID      = 2492991572489844067L;

   private List<SynchonizationResultDTO> synchProjectResults = new ArrayList<SynchonizationResultDTO>();

   private List<SynchonizationResultDTO> synchTemplateResults = new ArrayList<SynchonizationResultDTO>();

   /**
    * @return the synchonizationResults
    */
   public List<SynchonizationResultDTO> getSynchProjectResults()
   {
      return synchProjectResults;
   }

   /**
    * @param synchProjectResults
    *           the synchonizationResults to set
    */
   public void setSynchProjectResults(List<SynchonizationResultDTO> synchProjectResults)
   {
      this.synchProjectResults = synchProjectResults;
   }

   /**
    * @return the synchTemplateResults
    */
   public List<SynchonizationResultDTO> getSynchTemplateResults()
   {
      return synchTemplateResults;
   }

   /**
    * @param synchTemplateResults
    *           the synchTemplateResults to set
    */
   public void setSynchTemplateResults(List<SynchonizationResultDTO> synchTemplateResults)
   {
      this.synchTemplateResults = synchTemplateResults;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "DiffusionResultDTO [synchProjectResults=" + synchProjectResults + ", synchTemplateResults="
                 + synchTemplateResults + "]";
   }


}
