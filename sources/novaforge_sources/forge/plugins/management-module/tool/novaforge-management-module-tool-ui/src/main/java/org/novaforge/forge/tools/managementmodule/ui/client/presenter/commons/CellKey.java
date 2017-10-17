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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons;

import com.google.gwt.view.client.ProvidesKey;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorJointureDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineSharingDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitIterationMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;

/**
 * @author qsivan
 */
public class CellKey {
  /**
   * private constructor 
   */
  private CellKey()
   {
   }
   /**
    * The key provider that provides the unique ID of a plugin.
    */
   public static final ProvidesKey<ComponentDTO> COMPOSANT_KEY_PROVIDER = new ProvidesKey<ComponentDTO>() {
      @Override
      public Object getKey(ComponentDTO item) {
         return item == null ? null : item.getComposantLibelle();
      }
   };

   /**
    * The key provider that provides the unique ID of a discipline.
    */
   public static final ProvidesKey<ProjectDisciplineDTO> PROJECT_DISCIPLINE_KEY_PROVIDER = new ProvidesKey<ProjectDisciplineDTO>() {
      @Override
      public Object getKey(ProjectDisciplineDTO item) {
         return item == null ? null : item.getDisciplineDTO().getLibelle();
      }
   };

   /**
    * The key provider that provides the unique ID of a discipline.
    */
   public static final ProvidesKey<AdjustFactorJointureDTO> FACTEUR_AJUSTEMENT_KEY_PROVIDER = new ProvidesKey<AdjustFactorJointureDTO>() {
      @Override
      public Object getKey(AdjustFactorJointureDTO item) {
         return item == null ? null : item.getAdjustFactor().getFunctionalId();
      }
   };

   /**
    * The key provider that provides the unique ID of a discipline.
    */
   public static final ProvidesKey<TaskCategoryDTO> CATEGORIE_KEY_PROVIDER = new ProvidesKey<TaskCategoryDTO>() {
      @Override
      public Object getKey(TaskCategoryDTO item) {
         return item == null ? null : item.getName();
      }
   };
   
   /**
    * The key provider that provides the unique ID of a CDOParameters.
    */
   public static final ProvidesKey<CDOParametersDTO> CDO_KEY_PROVIDER = new ProvidesKey<CDOParametersDTO>() {
      @Override
      public Object getKey(CDOParametersDTO item) {
         return item == null ? null : item.getcDOParametersID();
      }
   };

   public static final ProvidesKey<LotDTO> LOT_KEY_PROVIDER = new ProvidesKey<LotDTO>() {
      @Override
      public Object getKey(LotDTO item) {
         return item == null ? null : item.getLotId();
      }
   };

   /**
    * The key provider that provides the unique ID of an iteration.
    */
   public static final ProvidesKey<IterationDTO> PROJECT_KEY_PROVIDER = new ProvidesKey<IterationDTO>() {
      @Override
      public Object getKey(IterationDTO item) {
         return item == null ? null : item.getNumIteration();
      }
   };
   
   
   public static final ProvidesKey<MarkerDTO> MARKER_KEY_PROVIDER = new ProvidesKey<MarkerDTO>() {
	      @Override
	      public Object getKey(MarkerDTO item) {
	         return item == null ? null : item.getId();
	      }
	   };

	   /**
	    * The key provider that provides the unique ID of an estimation.
	    */
	   public static final ProvidesKey<EstimationDTO> ESTIMATION_KEY_PROVIDER = new ProvidesKey<EstimationDTO>() {
	      @Override
	      public Object getKey(EstimationDTO item) {
	         return item == null ? null : item;
	      }
	   };

	   /**
	    * The key provider that provides the unique ID of an estimation.
	    */
	   public static final ProvidesKey<IterationTaskDTO> ITERATION_TASK_KEY_PROVIDER = new ProvidesKey<IterationTaskDTO>() {
	      @Override
	      public Object getKey(IterationTaskDTO item) {
	         return item == null ? null : item;
	         //TODO JCB return id's item
	      }
	   };

	   /**
	    * The key provider that provides the unique ID of a light scope unit.
	    */
	   public static final ProvidesKey<ScopeUnitLightDTO> SCOPE_UNIT_LIGHT_KEY_PROVIDER = new ProvidesKey<ScopeUnitLightDTO>() {
	      @Override
	      public Object getKey(ScopeUnitLightDTO item) {
	         return item == null ? null : item.getScopeUnitName();
	      }
	   };

	   /**
	    * The key provider that provides the unique ID of an scope unit discipline.
	    */
	   public static final ProvidesKey<ScopeUnitDisciplineDTO> SCOPE_UNIT_DISCIPLINE_KEY_PROVIDER = new ProvidesKey<ScopeUnitDisciplineDTO>() {
	      @Override
	      public Object getKey(ScopeUnitDisciplineDTO item) {
	         return item == null ? null : item;
	      }
	   };

	   /**
	    * The key provider that provides the unique ID of a discipline sharing.
	    */
	   public static final ProvidesKey<DisciplineSharingDTO> DISCIPLINE_SHARING_KEY_PROVIDER = new ProvidesKey<DisciplineSharingDTO>() {
	      @Override
	      public Object getKey(DisciplineSharingDTO item) {
	         return item == null ? null : item.getScopeUnitId();
	      }
	   };

	   /**
       * The key provider that provides the unique ID of a discipline sharing.
       */
      public static final ProvidesKey<ScopeUnitGlobalMonitoringDTO> GLOBAL_MONITORING_KEY_PROVIDER = new ProvidesKey<ScopeUnitGlobalMonitoringDTO>() {
         @Override
         public Object getKey(ScopeUnitGlobalMonitoringDTO item) {
            return item == null ? null : item.getUnitId();
         }
      };
	   
      /**
       * The key provider that provides the unique ID of a discipline sharing.
       */
      public static final ProvidesKey<ScopeUnitIterationMonitoringDTO> ITERATION_MONITORING_KEY_PROVIDER = new ProvidesKey<ScopeUnitIterationMonitoringDTO>() {
         @Override
         public Object getKey(ScopeUnitIterationMonitoringDTO item) {
            return item == null ? null : item.getUnitId();
         }
      };
      
   /** The key provider that provides the unique ID of a bug */
   public static final ProvidesKey<BugDTO> BUG_KEY_PROVIDER = new ProvidesKey<BugDTO>() {
      @Override
      public Object getKey(BugDTO item) {
         if(item == null) {
           throw new IllegalStateException("All the bug must have a bugTrackerId");
         }
         return item.getBugTrackerId();
      }
   };
   
   public static final ProvidesKey<DisciplineDTO> DISCIPLINE_KEY_PROVIDER = new ProvidesKey<DisciplineDTO>() {
       @Override
       public Object getKey(DisciplineDTO item) {
          return item == null ? null : item.getFunctionalId();
       }
    };
   
}
