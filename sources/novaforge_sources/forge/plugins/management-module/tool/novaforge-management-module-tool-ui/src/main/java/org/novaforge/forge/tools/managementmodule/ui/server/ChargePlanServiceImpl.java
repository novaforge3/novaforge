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

package org.novaforge.forge.tools.managementmodule.ui.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;
import org.novaforge.forge.tools.managementmodule.services.Util;
import org.novaforge.forge.tools.managementmodule.ui.client.service.ChargePlanService;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.DateComparator;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanLineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanMainDataDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanToolTipDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

/**
 * The server side implementation of the RPC service.
 */
public class ChargePlanServiceImpl extends SimpleServiceImpl implements ChargePlanService {

	/**
	* 
	*/
	private static final long serialVersionUID = 9026815540750615334L;

	private static final Log LOG = LogFactory.getLog(ChargePlanServiceImpl.class);

	@Override
	public ChargePlanMainDataDTO getChargePlanMainData(final Long projectPlanId, final String projectId)
			throws ManagementModuleException {
		try {
			LOG.debug("start getChargePlanMainData() with projectPlanId = " + projectPlanId);

			final List<ChargePlanLineDTO> listLines = new ArrayList<ChargePlanLineDTO>();
			final Map<String, ChargePlanLineDTO> mapLines = new HashMap<String, ChargePlanLineDTO>();

			final Map<Date, ChargePlanToolTipDTO> tooltipsByDate = new TreeMap<Date, ChargePlanToolTipDTO>(
					new DateComparator());

			// Récupération de la liste des disciplines
			// Vérification de la somme des pourcentages des disciplines pour un
			// montant de 100% =>Exception
			Project projectFull = getManagementModuleManager().getFullProject(projectId);
			

			Set<ProjectDiscipline> projectDisciplines = projectFull.getDisciplines();
			if (projectDisciplines == null || projectDisciplines.isEmpty()) {
				throw new ManagementModuleException(ErrorEnumeration.ERR_CHARGEPLAN_NO_DISCIPLINE);
			}
			Map<String, ProjectDiscipline> mapProjectDisciplines = new HashMap<String, ProjectDiscipline>();
			for (ProjectDiscipline projectDiscipline : projectDisciplines) {
				mapProjectDisciplines.put(projectDiscipline.getDiscipline().getFunctionalId(), projectDiscipline);
			}
			int sumPercent = 0;
			for (ProjectDiscipline projectDiscipline : projectDisciplines) {
				sumPercent += projectDiscipline.getRepartition();
				mapProjectDisciplines.put(projectDiscipline.getDiscipline().getFunctionalId(), projectDiscipline);
			}
			if (sumPercent != 100) {
				throw new ManagementModuleException(ErrorEnumeration.ERR_CHARGEPLAN_WRONG_SUM_PERCENT_DISCIPLINES);
			}

			// Récupération des Lots
			List<Lot> listLots = getProjectPlanManager().getParentLotsList(projectPlanId);
			
			Collections.sort(listLots, new Comparator<Lot>() {

				@Override
				public int compare(final Lot o1, final Lot o2) {
					return o1.getStartDate().compareTo(o2.getStartDate());
				}
			});

			// find bounds
			Date firstDate = null;
			Date lastDate = null;
			for (Lot lot : listLots) {
				Date start = lot.getStartDate();
				Date end = lot.getEndDate();
				if (firstDate == null || firstDate.after(start)) {
					firstDate = start;
				}
				if (lastDate == null || lastDate.before(end)) {
					lastDate = end;
				}
			}
			if (firstDate != null) {
				firstDate = Util.clearTime(firstDate);
			}
			if (lastDate != null) {
				lastDate = Util.clearTime(lastDate);
			}
			// Récupération des Unités de périmêtres du projectPlan.
			List<ScopeUnit> listScopeUnits = getProjectPlanManager().findScopeUnitListByProjectPlanId(projectPlanId);

			// Récupération des itérations
      Long previousProjectPlanId = projectPlanId;
      final ProjectPlan draftVersion = getProjectPlanManager().getProjectPlanDraftVersion(projectId);

      // check if a draft version of the project plan exist
      if (draftVersion != null)
      {
        int version = getProjectPlanManager().getProjectPlan(projectId, draftVersion.getVersion())
            .getVersion();
        if (version != ManagementModuleConstants.PROJECT_PLAN_VERSION_DEFAULT)
        {
          version = version - 1;
          previousProjectPlanId = getProjectPlanManager().getProjectPlan(projectId, version).getId();
        }
      }
      List<Iteration> listIterations = getIterationManager().getIterationsList(previousProjectPlanId);

			Collections.sort(listIterations, new Comparator<Iteration>() {

				@Override
				public int compare(final Iteration o1, final Iteration o2) {
					return o1.getStartDate().compareTo(o2.getStartDate());
				}
			});

			// initialisation de la map des lignes du tableau
			for (ProjectDiscipline projectDiscipline : projectDisciplines) {
				mapLines.put(projectDiscipline.getDiscipline().getFunctionalId(),
						BuildResources.buildChargePlanLineDTO(null, projectDiscipline.getDiscipline().getFunctionalId(),
								projectDiscipline.getDiscipline().getName(),
								projectDiscipline.getDiscipline().getOrder(), projectPlanId, 0, 0, 0, null));
			}
			mapLines.put(Constants.CHARGE_PLAN_FIRST_LINE_ID,
					BuildResources.buildChargePlanLineDTO(null, Constants.CHARGE_PLAN_FIRST_LINE_ID,
							Constants.CHARGE_PLAN_FIRST_LINE_NAME, Integer.MIN_VALUE, projectPlanId, 0, 0, 0, null));

			mapLines.put(Constants.CHARGE_PLAN_TOTAL_LINE_ID,
					BuildResources.buildChargePlanLineDTO(null, Constants.CHARGE_PLAN_TOTAL_LINE_ID,
							Constants.CHARGE_PLAN_TOTAL_LINE_ID, Integer.MAX_VALUE, projectPlanId, 0, 0, 0, null));

			// Calcul de la charge Total

			// Pour chaque Unités de périmêtres, insérer sa charge multiplié
			// par le
			// facteur de la discipline
			// et ajouter à la ligne de Total
			for (ScopeUnit su : listScopeUnits) {
				if (su.getParentScopeUnit() != null) {
					continue;
				}
				float totalLineTotal = 0;
				for (Map.Entry<String, ChargePlanLineDTO> entry : mapLines.entrySet()) {

					if (entry.getKey().equalsIgnoreCase(Constants.CHARGE_PLAN_TOTAL_LINE_ID)
							|| entry.getKey().equalsIgnoreCase(Constants.CHARGE_PLAN_FIRST_LINE_ID)) {
						continue;
					}
					float coeff = 0.0f;
					if (mapProjectDisciplines.get(entry.getKey()) != null
							&& mapProjectDisciplines.get(entry.getKey()).getRepartition() > -1) {
						coeff = ((float) mapProjectDisciplines.get(entry.getKey()).getRepartition()) / 100;
					}
					float loadUnitPartForDiscipline = su.getEstimation().getLastCharge() * coeff;
					entry.getValue().setTotalLoad(entry.getValue().getTotalLoad() + loadUnitPartForDiscipline);

					totalLineTotal += loadUnitPartForDiscipline;
				}

				mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID).setTotalLoad(
						mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID).getTotalLoad() + totalLineTotal);
			}

			// Insertion des données déjà existantes dans les colonnes
			// correspondantes
			List<LoadDistributionDiscipline> listLDD = getProjectPlanManager()
					.getLoadDistributionDisciplineList(projectPlanId);

			if (!listLots.isEmpty()) {
				ProjectPlan projectPlan = listLots.get(0).getProjectPlan();

				UnitTime utWeek = getReferentielManager()
						.getUnitTimeByIdFunctional(ManagementModuleConstants.UNIT_TIME_WEEK);

				int calendarField = Calendar.WEEK_OF_YEAR;

				// The smaller UnitTimeEnum displayed is the week, so, if it's
				// not,
				// it's
				// regrouped for a week.
				if (projectPlan.getProject().getUnitTime().getDurationInDays() > utWeek.getDurationInDays()) {
					calendarField = Calendar.MONTH;
				}

				Calendar calendarStartDate = Calendar.getInstance();
				Calendar calendarEndDate = Calendar.getInstance();

				if (firstDate != null) {
					calendarStartDate.setTime(firstDate);
					calendarEndDate.setTime(firstDate);
				}
				if (calendarField == Calendar.WEEK_OF_YEAR) {
					calendarEndDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				} else {
					calendarEndDate.add(Calendar.MONTH, 1);
					calendarEndDate.set(Calendar.DAY_OF_MONTH, 1);
					calendarEndDate.add(Calendar.DAY_OF_YEAR, -1);
				}

				Date startPeriode;
				Date endPeriode;
				boolean firstLoop = true;
				do {
					startPeriode = calendarStartDate.getTime();
					endPeriode = calendarEndDate.getTime();

					if (firstLoop) {
						if (calendarField == Calendar.WEEK_OF_YEAR) {
							calendarStartDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
						} else {
							calendarStartDate.set(Calendar.DAY_OF_MONTH, 1);
						}
						firstLoop = false;
					}
					calendarStartDate.add(calendarField, 1);
					calendarEndDate.add(calendarField, 1);

					// définition des Unités de temps pour la répartition et
					// l'assignation des lots et sous lots, phases et itérations

					if (tooltipsByDate.get(startPeriode) == null) {
						ChargePlanToolTipDTO toolTip = new ChargePlanToolTipDTO();
						toolTip.setDate(startPeriode);
						tooltipsByDate.put(startPeriode, toolTip);
					}
					ChargePlanToolTipDTO currentToolTip = tooltipsByDate.get(startPeriode);

					for (Lot lot : listLots) {
						if (lot.getParentLot()!= null || lot.getEndDate().before(startPeriode) || lot.getStartDate().after(endPeriode)) {
							continue;
						}
						
						currentToolTipSetNameByLot( lot, startPeriode, endPeriode, currentToolTip);
					}

					for (Iteration iteration : listIterations) {
						if (iteration.getEndDate().before(startPeriode) || iteration.getStartDate().after(endPeriode)) {
							continue;
						}
						
						String previousIterationNames = currentToolTip.getIterationNames();
						String previousPhaseNames = currentToolTip.getPhaseNames();
						String currentIterationNames;
						String currentPhaseNames;
						
						if ((iteration.getEndDate().after(startPeriode)
                && (iteration.getStartDate().before(startPeriode)
                    || DateUtils.truncatedCompareTo(iteration.getStartDate(), startPeriode,
                        Calendar.DAY_OF_MONTH) == 0)) 
						    || (iteration.getStartDate().after(startPeriode)
                && iteration.getStartDate().before(endPeriode))) {
						  
						 if ( iteration.getLabel() !=  null){
  						  if (previousIterationNames != null){
                  currentIterationNames = previousIterationNames.concat(",");
                  currentIterationNames = currentIterationNames.concat(iteration.getLabel());				    
  						  } else {
  						    currentIterationNames = iteration.getLabel();
  						  }
  						  currentToolTip.setIterationNames(currentIterationNames);
						 }
						  
						  if (iteration.getPhaseType() != null) {
						    if (previousPhaseNames != null) {
                  currentPhaseNames = previousPhaseNames.concat(",");
                  currentPhaseNames = currentPhaseNames.concat(iteration.getPhaseType().getName());		      
						    } else {
						      currentPhaseNames = iteration.getPhaseType().getName();
						    }
						    currentToolTip.setPhaseNames(currentPhaseNames);
              }  
						}
					}
				} while (lastDate != null && lastDate.after(calendarStartDate.getTime()));

				ChargePlanLineDTO totalLine = mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID);
				for (LoadDistributionDiscipline ldd : listLDD) {
					ChargePlanLineDTO currentLine = mapLines
							.get(ldd.getProjectDiscipline().getDiscipline().getFunctionalId());
					if (currentLine != null) {
						Date date = new Date(ldd.getDate().getTime());

						// Add to current line
						currentLine.setVerifiedLoad(currentLine.getVerifiedLoad() + ldd.getLoad());
						currentLine.getLoadsByDate().put(date, ldd.getLoad());

						// Add to total Line
						Float totalLoad = ldd.getLoad();
						if (totalLine.getLoadsByDate().get(date) != null) {
							totalLoad += mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID).getLoadsByDate().get(date);
						}
						mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID).getLoadsByDate().put(date, totalLoad);
						mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID).setVerifiedLoad(
								mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID).getVerifiedLoad() + ldd.getLoad());

					}
				}

				// RemainingLoad's definition
				for (Map.Entry<String, ChargePlanLineDTO> entry : mapLines.entrySet()) {
					entry.getValue()
							.setRemainingLoad(entry.getValue().getTotalLoad() - entry.getValue().getVerifiedLoad());
					listLines.add(entry.getValue());
				}

				// // Add First Line
				// listLines.add(0,
				// mapLines.get(Constants.CHARGE_PLAN_FIRST_LINE_ID));
				// // Add Total Line
				// listLines.add(mapLines.get(Constants.CHARGE_PLAN_TOTAL_LINE_ID));

				// Order By Discipline's Order
				Collections.sort(listLines, new Comparator<ChargePlanLineDTO>() {

					@Override
					public int compare(final ChargePlanLineDTO o1, final ChargePlanLineDTO o2) {
						if (o1.getDisciplineOrder() > o2.getDisciplineOrder()) {
							return 1;
						} else {
							return -1;
						}
					}
				});
			}
			return BuildResources.buildChargePlanMainDataDTO(listLines, tooltipsByDate);

		} catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e) {
			throw manageException("Unable to get charge plan datas for projectPlanId : " + projectPlanId, e);
		}
	}
	
private void currentToolTipSetNameByLot( Lot lot, Date startPeriode, Date endPeriode, ChargePlanToolTipDTO currentToolTip){
	    String currentLotNames = null;
	    String currentChildLotNames = null;
	    String previousLotNames = currentToolTip.getLotNames();
	    String previousChildLotNames = currentToolTip.getChildLotNames();  
	    // set lot name
      if (lot.getChildLots() == null || lot.getParentLot() == null) {        
        if ( (lot.getEndDate().after(startPeriode) && (lot.getStartDate().before(startPeriode)
            || DateUtils.truncatedCompareTo(lot.getStartDate(), startPeriode,Calendar.DAY_OF_MONTH) == 0))
            || (lot.getStartDate().after(startPeriode) && lot.getStartDate().before(endPeriode))){
    
           if(lot.getName() != null) {
              if (previousLotNames != null){
                currentLotNames = previousLotNames.concat(",") ;
                currentLotNames = currentLotNames.concat(lot.getName());     
              } else {
                currentLotNames = lot.getName();           
              }    
              currentToolTip.setLotNames(currentLotNames);
           }
        }          
      } 
      //set sub-lot name
      else {
        if (( lot.getEndDate().after(startPeriode) 
            && (lot.getStartDate().before(startPeriode)|| DateUtils.truncatedCompareTo(lot.getStartDate(), startPeriode,Calendar.DAY_OF_MONTH) == 0))
            || (lot.getStartDate().after(startPeriode) && lot.getStartDate().before(endPeriode))) {

             if (lot.getName()!= null){
                if (previousChildLotNames != null){
                   currentChildLotNames = previousChildLotNames.concat(",") ;
                   currentChildLotNames = currentChildLotNames.concat(lot.getName());
                 } else {
                   currentChildLotNames = lot.getName();
                 }    
                 currentToolTip.setChildLotNames(currentChildLotNames);
              }
        }       
      }
                 
	    for (final Lot childLot : lot.getChildLots()){  
	      currentToolTipSetNameByLot(childLot, startPeriode, endPeriode, currentToolTip);
	    }
	  }
	      

	@Override
	public void saveChargePlan(final List<ChargePlanLineDTO> listLines, final Long projectPlanId,
			final String projectId) throws ManagementModuleException {
		try {

			// récupérer le plan projet
			ProjectPlan projectPlan = getProjectPlanManager().getProjectPlan(projectPlanId);

			// Récupération de la liste des disciplines
			// Vérification de la somme des pourcentages des disciplines pour un
			// montant de 100% =>Exception

			Project projectFull = getManagementModuleManager()
					.getFullProject(getProjectPlanManager().getProjectPlan(projectPlanId).getProject().getProjectId());

			Set<ProjectDiscipline> projectDisciplines = projectFull.getDisciplines();
			if (projectDisciplines == null || projectDisciplines.isEmpty()) {
				throw new ManagementModuleException(ErrorEnumeration.ERR_CHARGEPLAN_NO_DISCIPLINE);
			}
			Map<String, ProjectDiscipline> mapProjectDisciplines = new HashMap<String, ProjectDiscipline>();
			for (ProjectDiscipline projectDiscipline : projectDisciplines) {
				mapProjectDisciplines.put(projectDiscipline.getDiscipline().getFunctionalId(), projectDiscipline);
			}

			// Insertion des données déjà existantes dans les colonnes
			// correspondantes
			List<LoadDistributionDiscipline> listLDD = getProjectPlanManager()
					.getLoadDistributionDisciplineList(projectPlanId);

			final HashMap<String, Map<Date, LoadDistributionDiscipline>> lddByDateAndByFunctionalIdDiscipline = new HashMap<String, Map<Date, LoadDistributionDiscipline>>();

			for (LoadDistributionDiscipline ldd : listLDD) {
				if (lddByDateAndByFunctionalIdDiscipline
						.get(ldd.getProjectDiscipline().getDiscipline().getFunctionalId()) == null) {
					lddByDateAndByFunctionalIdDiscipline.put(
							ldd.getProjectDiscipline().getDiscipline().getFunctionalId(),
							new HashMap<Date, LoadDistributionDiscipline>());
				}
				lddByDateAndByFunctionalIdDiscipline.get(ldd.getProjectDiscipline().getDiscipline().getFunctionalId())
						.put(new Date(ldd.getDate().getTime()), ldd);
			}

			List<LoadDistributionDiscipline> listToSave = new ArrayList<LoadDistributionDiscipline>();
			List<LoadDistributionDiscipline> listToCreate = new ArrayList<LoadDistributionDiscipline>();
			List<Long> listToDelete = new ArrayList<Long>();

			for (ChargePlanLineDTO dto : listLines) {
				if (Constants.CHARGE_PLAN_TOTAL_LINE_ID.equalsIgnoreCase(dto.getIdDiscipline())
						|| Constants.CHARGE_PLAN_FIRST_LINE_ID.equalsIgnoreCase(dto.getIdDiscipline())) {
					continue;
				}

				Map<Date, LoadDistributionDiscipline> lddByDate = lddByDateAndByFunctionalIdDiscipline
						.get(dto.getIdDiscipline());

				if (lddByDate == null) {
					lddByDate = new HashMap<Date, LoadDistributionDiscipline>();
				}

				for (Map.Entry<Date, Float> entry : dto.getLoadsByDate().entrySet()) {

					if (entry.getValue() == null || entry.getValue() == 0) {
						// delete the old line if exist
						if (lddByDate.get(entry.getKey()) != null) {
							listToDelete.add(lddByDate.get(entry.getKey()).getId());
							lddByDate.remove(entry.getKey());
						}
					} else {
						LoadDistributionDiscipline currentLDD;
						// modify the line or create new one
						if (lddByDate.get(entry.getKey()) != null) {
							currentLDD = lddByDate.get(entry.getKey());
							if (!Util.isEqual(entry.getValue(), currentLDD.getLoad())) {
								currentLDD.setLoad(entry.getValue());
								listToSave.add(currentLDD);
								lddByDate.remove(entry.getKey());
							}
						} else {
							currentLDD = getProjectPlanManager().newLoadDistributionDiscipline();
							currentLDD.setDate(entry.getKey());
							currentLDD.setProjectDiscipline(mapProjectDisciplines.get(dto.getIdDiscipline()));
							currentLDD.setLoad(entry.getValue());
							currentLDD.setProjectPlan(projectPlan);

							listToCreate.add(currentLDD);
						}
					}
				}
			}

			getProjectPlanManager().manageLoadDistributionDisciplines(listToCreate, listToSave, listToDelete);

		} catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e) {
			throw manageException("Unable to save charge plan datas for projectPlanId : " + projectPlanId, e);
		}
	}
}
