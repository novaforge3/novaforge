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
package org.novaforge.forge.tools.managementmodule.initialization.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ManagementFileConfiguration {

   private static final String INITIALISATION_PROPERTIES = "initialisation.properties";

   private static final Log LOG = LogFactory.getLog(ManagementFileConfiguration.class);
   private static ManagementFileConfiguration instance;
   Properties prop = new Properties();
   private String endpoint;
   private String nbJourParAn;
   private String nbJourParSemaine;
   private String nbJourParMois;
   private String unitTimeWeek;
   private String unitTimeMonth;
   private String taskTypeWork;
   private String taskTypeBug;
   private String taskStatusNotStarted;
   private String taskStatusNotaAffected;
   private String taskStatusCurrent;
   private String taskStatusDone;
   private String taskStatusCanceled;
   private String factorAdjustmentDataTransmission;
   private String factorAdjustmentDistributedSystem;
   private String factorAdjustmentPerfConstraint;
   private String factorAdjustmentConfiguration;
   private String factorAdjustmentTransactionRate;
   private String factorAdjustmentUsability;
   private String factorAdjustmentLiveupdate;
   private String factorAdjustmentProcessComplex;
   private String factorAdjustmentReuse;
   private String factorAdjustmentEasyOperation;
   private String factorAdjustmentAppliPortability;
   private String factorAdjustmentSimultaneousAccess;
   private String factorAdjustmentModifFlexibility;
   private String factorAdjustementInteractiveCapture;
   private String phareBusinessModeling;
   private String phareRequirementsAnalysis;
   private String phareArchitectureDesign;
   private String phareImplementation;
   private String phareReceipts;
   private String phareChangeDriving;
   private String phareConfigurationManagement;
   private String phareQualityAssurance;
   private String phareProjectManagement;
   private String chargePhareBusinessModeling;
   private String chargePhareRequirementsAnalysis;
   private String chargePhareArchitectureDesign;
   private String chargePhareImplementation;
   private String chargePhareReceipts;
   private String chargePhareChangeDriving;
   private String chargePhareConfigurationManagement;
   private String chargePhareQualityAssurance;
   private String chargePhareProjectManagement;
   private String weightAdjustmentNonexistent;
   private String weightAdjustmentSecondary;
   private String weightAdjustmentRestraint;
   private String weightAdjustmentAverage;
   private String weightAdjustmentImportant;
   private String weightAdjustmentIntensive;
   private String phaseTypeFraming;
   private String phaseTypePlanning;
   private String phaseTypeConstruction;
   private String phaseTypeTransition;
   private String phaseTypeNone;
   private String roleProjectManager;
   private String roleCustomer;
   private String roleAdmin;
   private String roleProjectDirector;
   private String roleMOELeader;
   private String roleMOEMember;
   private String roleMOALeader;
   private String roleMOAMember;
   private String roleObserver;
   private String roleSponsor;
   private String scopeStatusModified;
   private String scopeStatusObsolete;
   private String scopeStatusOngoing;
   private String markerTypeEarly;
   private String markerTypeLate;
   private String scopeTypeCu;
   private String scopeTypeTg;
   private String scopeTypeHu;
   private String scopeTypeA;
   private String projectPlanDraft;
   private String projectPlanValidate;
   private String langageFr;
   private String langageEn;
   private String valueGDI;
   private String valueINT;
   private String valueENT;
   private String valueSOR;
   private String valueGDE;
   private String nbHeureParJour;
   private String nbJourNonTravaille;
   private String valueSimpleGDI;
   private String valueMoyenGDI;
   private String valueComplexGDI;
   private String valueSimpleGDE;
   private String valueMoyenGDE;
   private String valueComplexGDE;
   private String valueSimpleIN;
   private String valueMoyenIN;
   private String valueComplexIN;
   private String valueSimpleINT;
   private String valueMoyenINT;
   private String valueComplexINT;
   private String valueSimpleOUT;
   private String valueMoyenOUT;
   private String valueComplexOUT;
   private String valuePtFonctionRisque;
   private String valueAbaChgHomJour;
   private String scopeDisciplineStatusNotStarted;
   private String scopeDisciplineStatusInProgress;
   private String scopeDisciplineStatusClosed;
   private String valueweightAdjustmentNonexistent;
   private String valueweightAdjustmentSecondary;
   private String valueweightAdjustmentRestraint;
   private String valueweightAdjustmentAverage;
   private String valueweightAdjustmentImportant;
   private String valueweightAdjustmentIntensive;

   private ManagementFileConfiguration() {
      super();
      managementFileConfigurationInit();
   }

   public void managementFileConfigurationInit() {
      try {
         final InputStream in = this.getClass().getClassLoader().getResourceAsStream(INITIALISATION_PROPERTIES);
         this.prop.load(in);
         in.close();
      } catch (IOException e) {
         LOG.error(
               "Problem while reading :" + INITIALISATION_PROPERTIES, e);
      }
      endpoint = prop.getProperty("endpoint");
      nbJourParAn = prop.getProperty("nbJourParAn");
      nbJourParSemaine = prop.getProperty("nbJourParSemaine");
      nbJourParMois = prop.getProperty("nbJourParMois");
      unitTimeWeek = prop.getProperty("unitTimeWeek");
      unitTimeMonth = prop.getProperty("unitTimeMonth");
      taskTypeWork = prop.getProperty("taskTypeWork");
      taskTypeBug = prop.getProperty("taskTypeBug");
      taskStatusNotStarted = prop.getProperty("taskStatusNotStarted");
      taskStatusNotaAffected = prop.getProperty("taskStatusNotaAffected");
      taskStatusCurrent = prop.getProperty("taskStatusCurrent");
      taskStatusDone = prop.getProperty("taskStatusDone");
      taskStatusCanceled = prop.getProperty("taskStatusCanceled");
      factorAdjustmentDataTransmission = prop.getProperty("factorAdjustmentDataTransmission");
      factorAdjustmentDistributedSystem = prop.getProperty("factorAdjustmentDistributedSystem");
      factorAdjustmentPerfConstraint = prop.getProperty("factorAdjustmentPerfConstraint");
      factorAdjustmentConfiguration = prop.getProperty("factorAdjustmentConfiguration");
      factorAdjustmentTransactionRate = prop.getProperty("factorAdjustmentTransactionRate");
      factorAdjustmentUsability = prop.getProperty("factorAdjustmentUsability");
      factorAdjustmentLiveupdate = prop.getProperty("factorAdjustmentLiveupdate");
      factorAdjustmentProcessComplex = prop.getProperty("factorAdjustmentProcessComplex");
      factorAdjustmentReuse = prop.getProperty("factorAdjustmentReuse");
      factorAdjustmentEasyOperation = prop.getProperty("factorAdjustmentEasyOperation");
      factorAdjustmentAppliPortability = prop.getProperty("factorAdjustmentAppliPortability");
      factorAdjustmentSimultaneousAccess = prop.getProperty("factorAdjustmentSimultaneousAccess");
      factorAdjustmentModifFlexibility = prop.getProperty("factorAdjustmentModifFlexibility");
      factorAdjustementInteractiveCapture = prop.getProperty("factorAdjustementInteractiveCapture");
      phareBusinessModeling = prop.getProperty("disciplineBusinessModeling");
      phareRequirementsAnalysis = prop.getProperty("disciplineRequirementsAnalysis");
      phareArchitectureDesign = prop.getProperty("disciplineArchitectureDesign");
      phareImplementation = prop.getProperty("disciplineImplementation");
      phareReceipts = prop.getProperty("disciplineReceipts");
      phareChangeDriving = prop.getProperty("disciplineChangeDriving");
      phareConfigurationManagement = prop.getProperty("disciplineConfigurationManagement");
      phareQualityAssurance = prop.getProperty("disciplineQualityAssurance");
      phareProjectManagement = prop.getProperty("disciplineProjectManagement");
      chargePhareBusinessModeling = prop.getProperty("chargePhareBusinessModeling");
      chargePhareRequirementsAnalysis = prop.getProperty("chargePhareRequirementsAnalysis");
      chargePhareArchitectureDesign = prop.getProperty("chargePhareArchitectureDesign");
      chargePhareImplementation = prop.getProperty("chargePhareImplementation");
      chargePhareReceipts = prop.getProperty("chargePhareReceipts");
      chargePhareChangeDriving = prop.getProperty("chargePhareChangeDriving");
      chargePhareConfigurationManagement = prop.getProperty("chargePhareConfigurationManagement");
      chargePhareQualityAssurance = prop.getProperty("chargePhareQualityAssurance");
      chargePhareProjectManagement = prop.getProperty("chargePhareProjectManagement");
      weightAdjustmentNonexistent = prop.getProperty("weightAdjustmentNonexistent");
      weightAdjustmentSecondary = prop.getProperty("weightAdjustmentSecondary");
      weightAdjustmentRestraint = prop.getProperty("weightAdjustmentRestraint");
      weightAdjustmentAverage = prop.getProperty("weightAdjustmentAverage");
      weightAdjustmentImportant = prop.getProperty("weightAdjustmentImportant");
      weightAdjustmentIntensive = prop.getProperty("weightAdjustmentIntensive");
      phaseTypeFraming = prop.getProperty("phaseTypeFraming");
      phaseTypePlanning = prop.getProperty("phaseTypePlanning");
      phaseTypeConstruction = prop.getProperty("phaseTypeConstruction");
      phaseTypeTransition = prop.getProperty("phaseTypeTransition");
      phaseTypeNone = prop.getProperty("phaseTypeNone");
      roleProjectManager = prop.getProperty("roleProjectManager");
      roleCustomer = prop.getProperty("roleCustomer");
      roleAdmin = prop.getProperty("roleAdmin");
      roleProjectDirector = prop.getProperty("roleProjectDirector");
      roleMOELeader = prop.getProperty("roleMOELeader");
      roleMOEMember = prop.getProperty("roleMOEMember");
      roleMOALeader = prop.getProperty("roleMOALeader");
      roleMOAMember = prop.getProperty("roleMOAMember");
      roleObserver = prop.getProperty("roleObserver");
      roleSponsor = prop.getProperty("roleSponsor");
      scopeStatusModified = prop.getProperty("scopeStatusModified");
      scopeStatusObsolete = prop.getProperty("scopeStatusObsolete");
      scopeStatusOngoing = prop.getProperty("scopeStatusOngoing");
      markerTypeEarly = prop.getProperty("markerTypeEarly");
      markerTypeLate = prop.getProperty("markerTypeLate");
      scopeTypeCu = prop.getProperty("scopeTypeCu");
      scopeTypeTg = prop.getProperty("scopeTypeTg");
      scopeTypeHu = prop.getProperty("scopeTypeHu");
      scopeTypeA = prop.getProperty("scopeTypeA");
      projectPlanDraft = prop.getProperty("projectPlanDraft");
      projectPlanValidate = prop.getProperty("projectPlanValidate");
      langageFr = prop.getProperty("langageFr");
      langageEn = prop.getProperty("langageEn");
      valueGDI = prop.getProperty("valueGDI");
      valueINT = prop.getProperty("valueINT");
      valueENT = prop.getProperty("valueENT");
      valueSOR = prop.getProperty("valueSOR");
      valueGDE = prop.getProperty("valueGDE");
      nbHeureParJour = prop.getProperty("nbHeureParJour");
      nbJourNonTravaille = prop.getProperty("nbJourNonTravaille");
      valueSimpleGDI = prop.getProperty("valueSimpleGDI");
      valueMoyenGDI = prop.getProperty("valueMoyenGDI");
      valueComplexGDI = prop.getProperty("valueComplexGDI");
      valueSimpleGDE = prop.getProperty("valueSimpleGDE");
      valueMoyenGDE = prop.getProperty("valueMoyenGDE");
      valueComplexGDE = prop.getProperty("valueComplexGDE");
      valueSimpleIN = prop.getProperty("valueSimpleIN");
      valueMoyenIN = prop.getProperty("valueMoyenIN");
      valueComplexIN = prop.getProperty("valueComplexIN");
      valueSimpleINT = prop.getProperty("valueSimpleINT");
      valueMoyenINT = prop.getProperty("valueMoyenINT");
      valueComplexINT = prop.getProperty("valueComplexINT");
      valueSimpleOUT = prop.getProperty("valueSimpleOUT");
      valueMoyenOUT = prop.getProperty("valueMoyenOUT");
      valueComplexOUT = prop.getProperty("valueComplexOUT");
      valuePtFonctionRisque = prop.getProperty("valuePtFonctionRisque");
      valueAbaChgHomJour = prop.getProperty("valueAbaChgHomJour");
      scopeDisciplineStatusNotStarted = prop.getProperty("scopeDisciplineStatusNotStarted");
      scopeDisciplineStatusInProgress = prop.getProperty("scopeDisciplineStatusInProgress");
      scopeDisciplineStatusClosed = prop.getProperty("scopeDisciplineStatusClosed");
      valueweightAdjustmentNonexistent = prop.getProperty("valueweightAdjustmentNonexistent");
      valueweightAdjustmentSecondary = prop.getProperty("valueweightAdjustmentSecondary");
      valueweightAdjustmentRestraint = prop.getProperty("valueweightAdjustmentRestraint");
      valueweightAdjustmentAverage = prop.getProperty("valueweightAdjustmentAverage");
      valueweightAdjustmentImportant = prop.getProperty("valueweightAdjustmentImportant");
      valueweightAdjustmentIntensive = prop.getProperty("valueweightAdjustmentIntensive");
   }

   public static ManagementFileConfiguration getInstance()
   {
      if (instance == null)
      {
         instance = new ManagementFileConfiguration();
      }
      return instance;
   }

   /**
    * @return the LOG
    */
   public static Log getLog()
   {
      return LOG;
   }

   public void start() {
      LOG.info("start ManagementFileConfiguration ...");
      // managementFileConfigurationInit();
   }

   public void stop() {
      LOG.info("stop ManagementFileConfiguration...");
   }

   public String getEndpoint() {
      return endpoint;
   }

   public void setEndpoint(String endpoint) {
      this.endpoint = prop.getProperty("endpoint");
   }

   public String getNbJourParAn() {
      return nbJourParAn;
   }

   public void setNbJourParAn(String nbJourParAn) {
      this.nbJourParAn = nbJourParAn;
   }

   public String getNbJourParSemaine() {
      return nbJourParSemaine;
   }

   public void setNbJourParSemaine(String nbJourParSemaine) {
      this.nbJourParSemaine = nbJourParSemaine;
   }

   public String getNbJourParMois() {
      return nbJourParMois;
   }

   public void setNbJourParMois(String nbJourParMois) {
      this.nbJourParMois = nbJourParMois;
   }

   public String getUnitTimeWeek() {
      return unitTimeWeek;
   }

   public void setUnitTimeWeek(String unitTimeWeek) {
      this.unitTimeWeek = unitTimeWeek;
   }

   public String getUnitTimeMonth() {
      return unitTimeMonth;
   }

   public void setUnitTimeMonth(String unitTimeMonth) {
      this.unitTimeMonth = unitTimeMonth;
   }

   public String getTaskTypeWork() {
      return taskTypeWork;
   }

   public void setTaskTypeWork(String taskTypeWork) {
      this.taskTypeWork = taskTypeWork;
   }

   public String getTaskTypeBug() {
      return taskTypeBug;
   }

   public void setTaskTypeBug(String taskTypeBug) {
      this.taskTypeBug = taskTypeBug;
   }

   public String getTaskStatusNotStarted() {
      return taskStatusNotStarted;
   }

   public void setTaskStatusNotStarted(String taskStatusNotStarted) {
      this.taskStatusNotStarted = taskStatusNotStarted;
   }

   public String getTaskStatusNotaAffected() {
      return taskStatusNotaAffected;
   }

   public void setTaskStatusNotaAffected(String taskStatusNotaAffected) {
      this.taskStatusNotaAffected = taskStatusNotaAffected;
   }

   public String getTaskStatusCurrent() {
      return taskStatusCurrent;
   }

   public void setTaskStatusCurrent(String taskStatusCurrent) {
      this.taskStatusCurrent = taskStatusCurrent;
   }

   public String getTaskStatusDone() {
      return taskStatusDone;
   }

   public void setTaskStatusDone(String taskStatusDone) {
      this.taskStatusDone = taskStatusDone;
   }

   public String getTaskStatusCanceled() {
      return taskStatusCanceled;
   }

   public void setTaskStatusCanceled(String taskStatusCanceled) {
      this.taskStatusCanceled = taskStatusCanceled;
   }

   public String getFactorAdjustmentDataTransmission() {
      return factorAdjustmentDataTransmission;
   }

   public void setFactorAdjustmentDataTransmission(String factorAdjustmentDataTransmission) {
      this.factorAdjustmentDataTransmission = factorAdjustmentDataTransmission;
   }

   public String getFactorAdjustmentDistributedSystem() {
      return factorAdjustmentDistributedSystem;
   }

   public void setFactorAdjustmentDistributedSystem(String factorAdjustmentDistributedSystem) {
      this.factorAdjustmentDistributedSystem = factorAdjustmentDistributedSystem;
   }

   public String getFactorAdjustmentPerfConstraint() {
      return factorAdjustmentPerfConstraint;
   }

   public void setFactorAdjustmentPerfConstraint(String factorAdjustmentPerfConstraint) {
      this.factorAdjustmentPerfConstraint = factorAdjustmentPerfConstraint;
   }

   public String getFactorAdjustmentConfiguration() {
      return factorAdjustmentConfiguration;
   }

   public void setFactorAdjustmentConfiguration(String factorAdjustmentConfiguration) {
      this.factorAdjustmentConfiguration = factorAdjustmentConfiguration;
   }

   public String getFactorAdjustmentTransactionRate() {
      return factorAdjustmentTransactionRate;
   }

   public void setFactorAdjustmentTransactionRate(String factorAdjustmentTransactionRate) {
      this.factorAdjustmentTransactionRate = factorAdjustmentTransactionRate;
   }

   public String getFactorAdjustmentUsability() {
      return factorAdjustmentUsability;
   }

   public void setFactorAdjustmentUsability(String factorAdjustmentUsability) {
      this.factorAdjustmentUsability = factorAdjustmentUsability;
   }

   public String getFactorAdjustmentLiveupdate() {
      return factorAdjustmentLiveupdate;
   }

   public void setFactorAdjustmentLiveupdate(String factorAdjustmentLiveupdate) {
      this.factorAdjustmentLiveupdate = factorAdjustmentLiveupdate;
   }

   public String getFactorAdjustmentProcessComplex() {
      return factorAdjustmentProcessComplex;
   }

   public void setFactorAdjustmentProcessComplex(String factorAdjustmentProcessComplex) {
      this.factorAdjustmentProcessComplex = factorAdjustmentProcessComplex;
   }

   public String getFactorAdjustmentReuse() {
      return factorAdjustmentReuse;
   }

   public void setFactorAdjustmentReuse(String factorAdjustmentReuse) {
      this.factorAdjustmentReuse = factorAdjustmentReuse;
   }

   public String getFactorAdjustmentEasyOperation() {
      return factorAdjustmentEasyOperation;
   }

   public void setFactorAdjustmentEasyOperation(String factorAdjustmentEasyOperation) {
      this.factorAdjustmentEasyOperation = factorAdjustmentEasyOperation;
   }

   public String getFactorAdjustmentAppliPortability() {
      return factorAdjustmentAppliPortability;
   }

   public void setFactorAdjustmentAppliPortability(String factorAdjustmentAppliPortability) {
      this.factorAdjustmentAppliPortability = factorAdjustmentAppliPortability;
   }

   public String getFactorAdjustmentSimultaneousAccess() {
      return factorAdjustmentSimultaneousAccess;
   }

   public void setFactorAdjustmentSimultaneousAccess(String factorAdjustmentSimultaneousAccess) {
      this.factorAdjustmentSimultaneousAccess = factorAdjustmentSimultaneousAccess;
   }

   public String getFactorAdjustmentModifFlexibility() {
      return factorAdjustmentModifFlexibility;
   }

   public void setFactorAdjustmentModifFlexibility(String factorAdjustmentModifFlexibility) {
      this.factorAdjustmentModifFlexibility = factorAdjustmentModifFlexibility;
   }

   /**
    * Get the factorAdjustementInteractiveCapture
    * @return the factorAdjustementInteractiveCapture
    */
   public String getFactorAdjustementInteractiveCapture() {
      return factorAdjustementInteractiveCapture;
   }

   /**
    * Set the factorAdjustementInteractiveCapture
    * @param factorAdjustementInteractiveCapture the factorAdjustementInteractiveCapture to set
    */
   public void setFactorAdjustementInteractiveCapture(String factorAdjustementInteractiveCapture) {
      this.factorAdjustementInteractiveCapture = factorAdjustementInteractiveCapture;
   }

   public String getPhareBusinessModeling() {
      return phareBusinessModeling;
   }

   public void setPhareBusinessModeling(String phareBusinessModeling) {
      this.phareBusinessModeling = phareBusinessModeling;
   }

   public String getPhareRequirementsAnalysis() {
      return phareRequirementsAnalysis;
   }

   public void setPhareRequirementsAnalysis(String phareRequirementsAnalysis) {
      this.phareRequirementsAnalysis = phareRequirementsAnalysis;
   }

   public String getPhareArchitectureDesign() {
      return phareArchitectureDesign;
   }

   public void setPhareArchitectureDesign(String phareArchitectureDesign) {
      this.phareArchitectureDesign = phareArchitectureDesign;
   }

   public String getPhareImplementation() {
      return phareImplementation;
   }

   public void setPhareImplementation(String phareImplementation) {
      this.phareImplementation = phareImplementation;
   }

   public String getPhareReceipts() {
      return phareReceipts;
   }

   public void setPhareReceipts(String phareReceipts) {
      this.phareReceipts = phareReceipts;
   }

   public String getPhareChangeDriving() {
      return phareChangeDriving;
   }

   public void setPhareChangeDriving(String phareChangeDriving) {
      this.phareChangeDriving = phareChangeDriving;
   }

   public String getPhareConfigurationManagement() {
      return phareConfigurationManagement;
   }

   public void setPhareConfigurationManagement(String phareConfigurationManagement) {
      this.phareConfigurationManagement = phareConfigurationManagement;
   }

   public String getPhareQualityAssurance() {
      return phareQualityAssurance;
   }

   public void setPhareQualityAssurance(String phareQualityAssurance) {
      this.phareQualityAssurance = phareQualityAssurance;
   }

   public String getPhareProjectManagement() {
      return phareProjectManagement;
   }

   public void setPhareProjectManagement(String phareProjectManagement) {
      this.phareProjectManagement = phareProjectManagement;
   }

   public String getChargePhareBusinessModeling() {
      return chargePhareBusinessModeling;
   }

   public void setChargePhareBusinessModeling(String chargePhareBusinessModeling) {
      this.chargePhareBusinessModeling = chargePhareBusinessModeling;
   }

   public String getChargePhareRequirementsAnalysis() {
      return chargePhareRequirementsAnalysis;
   }

   public void setChargePhareRequirementsAnalysis(String chargePhareRequirementsAnalysis) {
      this.chargePhareRequirementsAnalysis = chargePhareRequirementsAnalysis;
   }

   public String getChargePhareArchitectureDesign() {
      return chargePhareArchitectureDesign;
   }

   public void setChargePhareArchitectureDesign(String chargePhareArchitectureDesign) {
      this.chargePhareArchitectureDesign = chargePhareArchitectureDesign;
   }

   public String getChargePhareImplementation() {
      return chargePhareImplementation;
   }

   public void setChargePhareImplementation(String chargePhareImplementation) {
      this.chargePhareImplementation = chargePhareImplementation;
   }

   public String getChargePhareReceipts() {
      return chargePhareReceipts;
   }

   public void setChargePhareReceipts(String chargePhareReceipts) {
      this.chargePhareReceipts = chargePhareReceipts;
   }

   public String getChargePhareChangeDriving() {
      return chargePhareChangeDriving;
   }

   public void setChargePhareChangeDriving(String chargePhareChangeDriving) {
      this.chargePhareChangeDriving = chargePhareChangeDriving;
   }

   public String getChargePhareConfigurationManagement() {
      return chargePhareConfigurationManagement;
   }

   public void setChargePhareConfigurationManagement(String chargePhareConfigurationManagement) {
      this.chargePhareConfigurationManagement = chargePhareConfigurationManagement;
   }

   public String getChargePhareQualityAssurance() {
      return chargePhareQualityAssurance;
   }

   public void setChargePhareQualityAssurance(String chargePhareQualityAssurance) {
      this.chargePhareQualityAssurance = chargePhareQualityAssurance;
   }

   public String getChargePhareProjectManagement() {
      return chargePhareProjectManagement;
   }

   public void setChargePhareProjectManagement(String chargePhareProjectManagement) {
      this.chargePhareProjectManagement = chargePhareProjectManagement;
   }

   public String getWeightAdjustmentNonexistent() {
      return weightAdjustmentNonexistent;
   }

   public void setWeightAdjustmentNonexistent(String weightAdjustmentNonexistent) {
      this.weightAdjustmentNonexistent = weightAdjustmentNonexistent;
   }

   public String getWeightAdjustmentSecondary() {
      return weightAdjustmentSecondary;
   }

   public void setWeightAdjustmentSecondary(String weightAdjustmentSecondary) {
      this.weightAdjustmentSecondary = weightAdjustmentSecondary;
   }

   public String getWeightAdjustmentRestraint() {
      return weightAdjustmentRestraint;
   }

   public void setWeightAdjustmentRestraint(String weightAdjustmentRestraint) {
      this.weightAdjustmentRestraint = weightAdjustmentRestraint;
   }

   public String getWeightAdjustmentAverage() {
      return weightAdjustmentAverage;
   }

   public void setWeightAdjustmentAverage(String weightAdjustmentAverage) {
      this.weightAdjustmentAverage = weightAdjustmentAverage;
   }

   public String getWeightAdjustmentImportant() {
      return weightAdjustmentImportant;
   }

   public void setWeightAdjustmentImportant(String weightAdjustmentImportant) {
      this.weightAdjustmentImportant = weightAdjustmentImportant;
   }

   public String getWeightAdjustmentIntensive() {
      return weightAdjustmentIntensive;
   }

   public void setWeightAdjustmentIntensive(String weightAdjustmentIntensive) {
      this.weightAdjustmentIntensive = weightAdjustmentIntensive;
   }

   public String getPhaseTypeFraming() {
      return phaseTypeFraming;
   }

   public void setPhaseTypeFraming(String phaseTypeFraming) {
      this.phaseTypeFraming = phaseTypeFraming;
   }

   public String getPhaseTypePlanning() {
      return phaseTypePlanning;
   }

   public void setPhaseTypePlanning(String phaseTypePlanning) {
      this.phaseTypePlanning = phaseTypePlanning;
   }

   public String getPhaseTypeConstruction() {
      return phaseTypeConstruction;
   }

   public void setPhaseTypeConstruction(String phaseTypeConstruction) {
      this.phaseTypeConstruction = phaseTypeConstruction;
   }

   public String getPhaseTypeTransition() {
      return phaseTypeTransition;
   }

   public void setPhaseTypeTransition(String phaseTypeTransition) {
      this.phaseTypeTransition = phaseTypeTransition;
   }

   public String getPhaseTypeNone() {
      return phaseTypeNone;
   }

   public void setPhaseTypeNone(String phaseTypeNone) {
      this.phaseTypeNone = phaseTypeNone;
   }

   public String getRoleSponsor() {
      return roleSponsor;
   }

   public void setRoleSponsor(String roleSponsor) {
      this.roleSponsor = roleSponsor;
   }

   public String getRoleProjectManager() {
      return roleProjectManager;
   }

   public void setRoleProjectManager(String roleProjectManager) {
      this.roleProjectManager = roleProjectManager;
   }

   public String getRoleCustomer() {
      return roleCustomer;
   }

   public void setRoleCustomer(String roleCustomer) {
      this.roleCustomer = roleCustomer;
   }

   public String getRoleAdmin() {
      return roleAdmin;
   }

   public void setRoleAdmin(String roleAdmin) {
      this.roleAdmin = roleAdmin;
   }

   public String getScopeStatusModified() {
      return scopeStatusModified;
   }

   public void setScopeStatusModified(String scopeStatusModified) {
      this.scopeStatusModified = scopeStatusModified;
   }

   public String getScopeStatusObsolete() {
      return scopeStatusObsolete;
   }

   public void setScopeStatusObsolete(String scopeStatusObsolete) {
      this.scopeStatusObsolete = scopeStatusObsolete;
   }

   public String getScopeStatusOngoing()
   {
      return scopeStatusOngoing;
   }

   public String getMarkerTypeEarly() {
      return markerTypeEarly;
   }

   public void setMarkerTypeEarly(String markerTypeEarly) {
      this.markerTypeEarly = markerTypeEarly;
   }

   public String getMarkerTypeLate() {
      return markerTypeLate;
   }

   public void setMarkerTypeLate(String markerTypeLate) {
      this.markerTypeLate = markerTypeLate;
   }

   public String getScopeTypeCu() {
      return scopeTypeCu;
   }

   public void setScopeTypeCu(String scopeTypeCu) {
      this.scopeTypeCu = scopeTypeCu;
   }

   public String getScopeTypeTg() {
      return scopeTypeTg;
   }

   public void setScopeTypeTg(String scopeTypeTg) {
      this.scopeTypeTg = scopeTypeTg;
   }

   public String getScopeTypeHu() {
      return scopeTypeHu;
   }

   public void setScopeTypeHu(String scopeTypeHu) {
      this.scopeTypeHu = scopeTypeHu;
   }

   public String getScopeTypeA() {
      return scopeTypeA;
   }

   public void setScopeTypeA(String scopeTypeA) {
      this.scopeTypeA = scopeTypeA;
   }

   public String getProjectPlanDraft() {
      return projectPlanDraft;
   }

   public void setProjectPlanDraft(String projectPlanDraft) {
      this.projectPlanDraft = projectPlanDraft;
   }

   public String getProjectPlanValidate() {
      return projectPlanValidate;
   }

   public void setProjectPlanValidate(String projectPlanValidate) {
      this.projectPlanValidate = projectPlanValidate;
   }

   public String getLangageFr() {
      return langageFr;
   }

   public void setLangageFr(String langageFr) {
      this.langageFr = langageFr;
   }

   public String getLangageEn() {
      return langageEn;
   }

   public void setLangageEn(String langageEn) {
      this.langageEn = langageEn;
   }

   public String getValueGDI() {
      return valueGDI;
   }

   public void setValueGDI(String valueGDI) {
      this.valueGDI = valueGDI;
   }

   public String getValueINT() {
      return valueINT;
   }

   public void setValueINT(String valueINT) {
      this.valueINT = valueINT;
   }

   public String getValueENT() {
      return valueENT;
   }

   public void setValueENT(String valueENT) {
      this.valueENT = valueENT;
   }

   public String getValueSOR() {
      return valueSOR;
   }

   public void setValueSOR(String valueSOR) {
      this.valueSOR = valueSOR;
   }

   public String getValueGDE() {
      return valueGDE;
   }

   public void setValueGDE(String valueGDE) {
      this.valueGDE = valueGDE;
   }

   public String getNbHeureParJour() {
      return nbHeureParJour;
   }

   public void setNbHeureParJour(String nbHeureParJour) {
      this.nbHeureParJour = nbHeureParJour;
   }

   public String getNbJourNonTravaille() {
      return nbJourNonTravaille;
   }

   public void setNbJourNonTravaille(String nbJourNonTravaille) {
      this.nbJourNonTravaille = nbJourNonTravaille;
   }

   public String getValueSimpleGDI() {
      return valueSimpleGDI;
   }

   public void setValueSimpleGDI(String valueSimpleGDI) {
      this.valueSimpleGDI = valueSimpleGDI;
   }

   public String getValueMoyenGDI() {
      return valueMoyenGDI;
   }

   public void setValueMoyenGDI(String valueMoyenGDI) {
      this.valueMoyenGDI = valueMoyenGDI;
   }

   public String getValueComplexGDI() {
      return valueComplexGDI;
   }

   public void setValueComplexGDI(String valueComplexGDI) {
      this.valueComplexGDI = valueComplexGDI;
   }

   public String getValueSimpleGDE() {
      return valueSimpleGDE;
   }

   public void setValueSimpleGDE(String valueSimpleGDE) {
      this.valueSimpleGDE = valueSimpleGDE;
   }

   public String getValueMoyenGDE() {
      return valueMoyenGDE;
   }

   public void setValueMoyenGDE(String valueMoyenGDE) {
      this.valueMoyenGDE = valueMoyenGDE;
   }

   public String getValueComplexGDE() {
      return valueComplexGDE;
   }

   public void setValueComplexGDE(String valueComplexGDE) {
      this.valueComplexGDE = valueComplexGDE;
   }

   public String getValueSimpleIN() {
      return valueSimpleIN;
   }

   public void setValueSimpleIN(String valueSimpleIN) {
      this.valueSimpleIN = valueSimpleIN;
   }

   public String getValueMoyenIN() {
      return valueMoyenIN;
   }

   public void setValueMoyenIN(String valueMoyenIN) {
      this.valueMoyenIN = valueMoyenIN;
   }

   public String getValueComplexIN() {
      return valueComplexIN;
   }

   public void setValueComplexIN(String valueComplexIN) {
      this.valueComplexIN = valueComplexIN;
   }

   public String getValueSimpleINT() {
      return valueSimpleINT;
   }

   public void setValueSimpleINT(String valueSimpleINT) {
      this.valueSimpleINT = valueSimpleINT;
   }

   public String getValueMoyenINT() {
      return valueMoyenINT;
   }

   public void setValueMoyenINT(String valueMoyenINT) {
      this.valueMoyenINT = valueMoyenINT;
   }

   public String getValueComplexINT() {
      return valueComplexINT;
   }

   public void setValueComplexINT(String valueComplexINT) {
      this.valueComplexINT = valueComplexINT;
   }

   public String getValueSimpleOUT() {
      return valueSimpleOUT;
   }

   public void setValueSimpleOUT(String valueSimpleOUT) {
      this.valueSimpleOUT = valueSimpleOUT;
   }

   public String getValueMoyenOUT() {
      return valueMoyenOUT;
   }

   public void setValueMoyenOUT(String valueMoyenOUT) {
      this.valueMoyenOUT = valueMoyenOUT;
   }

   public String getValueComplexOUT() {
      return valueComplexOUT;
   }

   public void setValueComplexOUT(String valueComplexOUT) {
      this.valueComplexOUT = valueComplexOUT;
   }

   public String getValuePtFonctionRisque() {
      return valuePtFonctionRisque;
   }

   public void setValuePtFonctionRisque(String valuePtFonctionRisque) {
      this.valuePtFonctionRisque = valuePtFonctionRisque;
   }

   public String getValueAbaChgHomJour() {
      return valueAbaChgHomJour;
   }

   public void setValueAbaChgHomJour(String valueAbaChgHomJour) {
      this.valueAbaChgHomJour = valueAbaChgHomJour;
   }

   public String getRoleProjectDirector() {
      return roleProjectDirector;
   }

   public void setRoleProjectDirector(String roleProjectDirector) {
      this.roleProjectDirector = roleProjectDirector;
   }

   public String getRoleMOELeader() {
      return roleMOELeader;
   }

   public void setRoleMOELeader(String roleMOELeader) {
      this.roleMOELeader = roleMOELeader;
   }

   public String getRoleMOEMember() {
      return roleMOEMember;
   }

   public void setRoleMOEMember(String roleMOEMember) {
      this.roleMOEMember = roleMOEMember;
   }

   public String getRoleMOALeader() {
      return roleMOALeader;
   }

   public void setRoleMOALeader(String roleMOALeader) {
      this.roleMOALeader = roleMOALeader;
   }

   public String getRoleMOAMember() {
      return roleMOAMember;
   }

   public void setRoleMOAMember(String roleMOAMember) {
      this.roleMOAMember = roleMOAMember;
   }

   public String getRoleObserver() {
      return roleObserver;
   }

   public void setRoleObserver(String roleObserver) {
      this.roleObserver = roleObserver;
   }

   /**
    * @return the scopeDisciplineStatusNotStarted
    */
   public String getScopeDisciplineStatusNotStarted() {
      return scopeDisciplineStatusNotStarted;
   }

   /**
    * @param scopeDisciplineStatusNotStarted the scopeDisciplineStatusNotStarted to set
    */
   public void setScopeDisciplineStatusNotStarted(String scopeDisciplineStatusNotStarted) {
      this.scopeDisciplineStatusNotStarted = scopeDisciplineStatusNotStarted;
   }

   /**
    * @return the scopeDisciplineStatusInProgress
    */
   public String getScopeDisciplineStatusInProgress() {
      return scopeDisciplineStatusInProgress;
   }

   /**
    * @param scopeDisciplineStatusInProgress the scopeDisciplineStatusInProgress to set
    */
   public void setScopeDisciplineStatusInProgress(String scopeDisciplineStatusInProgress) {
      this.scopeDisciplineStatusInProgress = scopeDisciplineStatusInProgress;
   }

   /**
    * @return the scopeDisciplineStatusClosed
    */
   public String getScopeDisciplineStatusClosed() {
      return scopeDisciplineStatusClosed;
   }

   /**
    * @param scopeDisciplineStatusClosed the scopeDisciplineStatusClosed to set
    */
   public void setScopeDisciplineStatusClosed(String scopeDisciplineStatusClosed) {
      this.scopeDisciplineStatusClosed = scopeDisciplineStatusClosed;
   }

   /**
	 * @return the valueweightAdjustmentNonexistent
	 */
	public String getValueweightAdjustmentNonexistent() {
		return valueweightAdjustmentNonexistent;
	}
	
	/**
	 * @param valueweightAdjustmentNonexistent the valueweightAdjustmentNonexistent to set
	 */
	public void setValueweightAdjustmentNonexistent(
			String valueweightAdjustmentNonexistent) {
		this.valueweightAdjustmentNonexistent = valueweightAdjustmentNonexistent;
	}
	
	/**
	 * @return the valueweightAdjustmentSecondary
	 */
	public String getValueweightAdjustmentSecondary() {
		return valueweightAdjustmentSecondary;
	}
	
	/**
	 * @param valueweightAdjustmentSecondary the valueweightAdjustmentSecondary to set
	 */
	public void setValueweightAdjustmentSecondary(
			String valueweightAdjustmentSecondary) {
		this.valueweightAdjustmentSecondary = valueweightAdjustmentSecondary;
	}
	
	/**
	 * @return the valueweightAdjustmentRestraint
	 */
	public String getValueweightAdjustmentRestraint() {
		return valueweightAdjustmentRestraint;
	}
	
	/**
	 * @param valueweightAdjustmentRestraint the valueweightAdjustmentRestraint to set
	 */
	public void setValueweightAdjustmentRestraint(
			String valueweightAdjustmentRestraint) {
		this.valueweightAdjustmentRestraint = valueweightAdjustmentRestraint;
	}
	
	/**
	 * @return the valueweightAdjustmentAverage
	 */
	public String getValueweightAdjustmentAverage() {
		return valueweightAdjustmentAverage;
	}
	
	/**
	 * @param valueweightAdjustmentAverage the valueweightAdjustmentAverage to set
	 */
	public void setValueweightAdjustmentAverage(String valueweightAdjustmentAverage) {
		this.valueweightAdjustmentAverage = valueweightAdjustmentAverage;
	}
	
	/**
	 * @return the valueweightAdjustmentImportant
	 */
	public String getValueweightAdjustmentImportant() {
		return valueweightAdjustmentImportant;
	}
	
	/**
	 * @param valueweightAdjustmentImportant the valueweightAdjustmentImportant to set
	 */
	public void setValueweightAdjustmentImportant(
			String valueweightAdjustmentImportant) {
		this.valueweightAdjustmentImportant = valueweightAdjustmentImportant;
	}
	
	/**
	 * @return the valueweightAdjustmentIntensive
	 */
	public String getValueweightAdjustmentIntensive() {
		return valueweightAdjustmentIntensive;
	}

	/**
   * @param valueweightAdjustmentIntensive the valueweightAdjustmentIntensive to set
	 */
	public void setValueweightAdjustmentIntensive(
			String valueweightAdjustmentIntensive) {
		this.valueweightAdjustmentIntensive = valueweightAdjustmentIntensive;
	}

}
