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
package org.novaforge.forge.configuration.csv.internal;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportManagementModuleImpl implements ImportManagementModule
{
  public static final String FORGE_GOD        = "*";
  public static final String LANGUAGE_FR_NAME = "FR";
  public static final String LANGUAGE_EN_NAME = "EN";
  private static final Log          LOG              = LogFactory.getLog(ImportManagementModuleImpl.class);
  private String                    directoryNfConf  = "/datas/novaforge3/datas/importcsv/";
  private ProjectPlanManager        projectPlanManager;
  private ManagementModuleManager   managementModuleManager;
  private ReferentielManager        referentielManager;
  private IterationManager          iterationManager;
  private AuthentificationService   authentificationService;
  private ForgeConfigurationService forgeConfigurationService;
  private UserPresenter             userPresenter;

  @Override
  public void init(final ProjectPlanManager pProjectPlanManager,
      final ManagementModuleManager pManagementModuleManager, final ReferentielManager pReferentielManager,
      final IterationManager pIterationManager, final ForgeConfigurationService pForgeManager,
      final AuthentificationService pAuthentificationService, final UserPresenter pUserPresenter,
      final String directoryNfConf) throws Exception
  {

    LOG.info("waiting 10 minutes for asynchrones insertions in Novaforge....");

    Thread.sleep(600000);

    final int decalage = 1000;

    iterationManager = pIterationManager;
    managementModuleManager = pManagementModuleManager;
    projectPlanManager = pProjectPlanManager;
    referentielManager = pReferentielManager;
    authentificationService = pAuthentificationService;
    userPresenter = pUserPresenter;
    forgeConfigurationService = pForgeManager;
    if ((directoryNfConf != null) && !directoryNfConf.trim().equalsIgnoreCase(""))
    {
      this.directoryNfConf = directoryNfConf;
    }
    login();

    final Date dateDebutMethodePP = new Date();
    final Map<String, Long> mapProjectPlan = importPlanProject();
    final Date dateFinMethodePP = new Date();
    final Long dureePP = (dateFinMethodePP.getTime() - dateDebutMethodePP.getTime()) / 1000l;
    LOG.info("Duree import ProjetPlan: " + dureePP.toString());

    final Date dateDebutMethodeL = new Date();
    final Map<String, Long> mapLot = importLots(mapProjectPlan, decalage);
    final Date dateFinMethodeL = new Date();
    final Long dureeL = (dateFinMethodeL.getTime() - dateDebutMethodeL.getTime()) / 1000l;
    LOG.info("Duree import Lots: " + dureeL.toString() + " secondes");

    final Date dateDebutMethodeI = new Date();
    importIterations(mapLot, decalage);
    final Date dateFinMethodeI = new Date();
    final Long dureeI = (dateFinMethodeI.getTime() - dateDebutMethodeI.getTime()) / 1000l;
    LOG.info("Duree import Iterations: " + dureeI.toString() + " secondes");

    final Date dateDebutMethodeM = new Date();
    importMarkers(mapProjectPlan, decalage);
    final Date dateFinMethodeM = new Date();
    final Long dureeM = (dateFinMethodeM.getTime() - dateDebutMethodeM.getTime()) / 1000l;
    LOG.info("Duree import Markers: " + dureeM.toString() + " secondes");

    // FIXME Ppr Probleme de contexte de driver MySql
    // UpdateDates majDates = new UpdateDates();
    // Execute the script for move all datas in the past.
    // majDates.run();
  }

  private void login() throws Exception
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userPresenter.getUser(superAdministratorLogin);
      authentificationService.login(superAdministratorLogin, user.getPassword());
    }
    catch (final Exception e)
    {
      throw new Exception("Unable to authenticate super administrator", e);
    }
  }

  private Map<String, Long> importPlanProject() throws Exception
  {
    LOG.info("Import Planproject");
    final String filename = directoryNfConf + "planproject.csv";
    final Map<String, Long> mapProjectPlan = new HashMap<String, Long>();

    try
    {
      final File f = new File(filename);
      if (f.exists())
      {
        final CSVReader reader = new CSVReader(new FileReader(f));
        String[] nextLine;
        final int timeOut = 1;

        final Set<String> listProjectId = new HashSet<String>();

        int compteur = 0;

        while ((nextLine = reader.readNext()) != null)
        {

          compteur++;
          if ((compteur % 20) == 0)
          {
            authentificationService.logout();
            login();
          }

          if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
          {
            LOG.info(Arrays.toString(nextLine));
            Project project = null;
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.MINUTE, timeOut);
            while ((project == null) && new Date().before(c.getTime()))
            {
              try
              {
                final String projectId = nextLine[0];
                project = managementModuleManager.getFullProject(projectId);

                if (!listProjectId.contains(projectId))
                {
                  // nettoyer tous les plan projet et les lots
                  for (final ProjectPlan pp : project.getProjectPlans())
                  {
                    // this.projectPlanManager.getList
                    projectPlanManager.deleteProjectPlan(pp.getId());
                  }
                  listProjectId.add(projectId);
                }

                else
                {
                  if (projectId != null)
                  {
                    projectPlanManager.validateProjectPlan(projectId);
                  }
                }
                final ProjectPlan pp = projectPlanManager.creeteProjectPlan(project.getProjectId());
                mapProjectPlan.put(nextLine[1], pp.getId());
                // FIXME On attend que le plan projet soit bien cree
                // en base avant de supprimer le lot qui y est
                // rattache
                Thread.sleep(5000);
                projectPlanManager.deleteLot(projectPlanManager.getParentLotsList(pp.getId()).get(0).getId());
              }
              catch (final Exception e)
              {
                LOG.info("Project " + nextLine[0] + " not found in database, maybe not propagated yet", e);
              }
              // On fait une pause pour attendre la propagation qui
              // est asynchrone.
              // Thread.sleep(20000);
            }
            if (project == null)
            {
              throw new Exception("Error in import ProjectPlan");
            }
          }
          else
          {
            LOG.info("ligne ignoree" + Arrays.toString(nextLine));
          }
        }
        LOG.info("import of " + mapProjectPlan.keySet().size() + " projectPlan for " + compteur
            + " lines done.");
      }
      else
      {
        LOG.error("file planproject.csv not exist at " + filename);
      }
    }
    catch (final Exception e)
    {
      LOG.error("Error in Import PlanProject", e);
    }

    return mapProjectPlan;
  }

  private Map<String, Long> importLots(final Map<String, Long> mapProjectPlan, final int decalage)
      throws Exception
  {
    LOG.info("Import lots");
    final Map<String, Long> mapLot = new HashMap<String, Long>();
    final String filename = directoryNfConf + "lots.csv";
    final File f = new File(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(filename));
      // SELECT `login` , `name` , `firstname` , `email` , `password` FROM
      // `ACTOR` WHERE
      // `DTYPE`='UserEntity' AND `realm_type`=2;
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          try
          {

            final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final Calendar c = Calendar.getInstance();
            Date createDate = format.parse(nextLine[0]);
            c.setTime(createDate);
            c.add(Calendar.YEAR, decalage);
            createDate = c.getTime();

            Date endDate = format.parse(nextLine[1]);
            c.setTime(endDate);
            c.add(Calendar.YEAR, decalage);
            endDate = c.getTime();

            Lot lotParent = null;

            final ProjectPlan projectPlan = projectPlanManager
                .getProjectPlan(mapProjectPlan.get(nextLine[2]));

            final String lotName = nextLine[3];

            final String desc = nextLine[4];

            if ((nextLine[5] != null) && !nextLine[5].equalsIgnoreCase(""))
            {
              lotParent = projectPlanManager.getLot(mapLot.get(nextLine[5]));
            }

            final Lot lot = projectPlanManager.creeteLot(projectPlan, lotName, createDate, endDate, desc,
                null, null, lotParent);
            mapLot.put(lotName, lot.getId());

          }
          catch (final Exception e)
          {
            throw new Exception("Error in import Lots", e);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
    return mapLot;
  }

  private void importIterations(final Map<String, Long> mapLot, final int decalage) throws Exception
  {
    LOG.info("Import iterations");
    final String filename = directoryNfConf + "iterations.csv";
    final File f = new File(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(filename));
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null)
      {
        final Calendar c = Calendar.getInstance();
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          try
          {

            final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date createDate = format.parse(nextLine[0]);
            c.setTime(createDate);
            c.add(Calendar.YEAR, decalage);
            createDate = c.getTime();

            Date endDate = format.parse(nextLine[1]);
            c.setTime(endDate);
            c.add(Calendar.YEAR, decalage);
            endDate = c.getTime();

            final Iteration iteration = iterationManager.newIteration();
            iteration.setStartDate(createDate);
            iteration.setEndDate(endDate);
            iteration.setFinished(intToBool(nextLine[2]));
            iteration.setLabel(nextLine[3]);
            iteration.setLot(projectPlanManager.getLot(mapLot.get(nextLine[5])));
            iteration.setNumIteration(Integer.parseInt(nextLine[4]));
            final PhaseType phaseType = referentielManager.getPhaseType(nextLine[6]);
            if (phaseType == null)
            {
              throw new Exception("Unable to get PhaseType with functionnalId = " + nextLine[5]);
            }
            iteration.setPhaseType(phaseType);
            iterationManager.creeteIteration(iteration);

          }
          catch (final Exception e)
          {
            throw new Exception("Error in import Iteration", e);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
  }

  private void importMarkers(final Map<String, Long> mapProjectPlan, final int decalage) throws Exception
  {
    LOG.info("Import markers");
    final String filename = directoryNfConf + "markers.csv";
    final File f = new File(filename);
    if (f.exists())
    {
      final CSVReader reader = new CSVReader(new FileReader(filename));
      String[] nextLine;
      // Lecture du fichier cible
      while ((nextLine = reader.readNext()) != null)
      {
        if ((nextLine.length > 1) && !nextLine[0].startsWith("#"))
        {
          LOG.info(Arrays.toString(nextLine));
          try
          {

            final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            // Récupération des informations nécessaires à la
            // création dans le fichier cible.
            final Calendar c = Calendar.getInstance();
            Date date = format.parse(nextLine[0]);
            c.setTime(date);
            // On décale ici la date de 1000 ans pour éviter les
            // problèmes.
            c.add(Calendar.YEAR, decalage);
            date = c.getTime();

            final Marker marker = projectPlanManager.newMarker();
            marker.setDate(date);
            marker.setDescription(nextLine[1]);
            marker.setName(nextLine[2]);
            marker.setProjectPlan(projectPlanManager.getProjectPlan(mapProjectPlan.get(nextLine[3])));
            final MarkerType type = referentielManager
                .getMarkerTypeByFuncionalId(idToFunctionalId(nextLine[4]));
            marker.setType(type);
            // Création de l'objet souhaité
            projectPlanManager.creeteMarker(marker);

          }
          catch (final Exception e)
          {
            throw new Exception("Error in import Markers", e);
          }
        }
        else
        {
          LOG.info("ligne ignoree" + Arrays.toString(nextLine));
        }
      }
    }
  }

  private Boolean intToBool(final String s) throws Exception
  {
    if (s.equals("1"))
    {
      return true;
    }
    if (s.equals("0"))
    {
      return false;
    }

    throw new Exception("Wrong is_finished value");
  }

  private String idToFunctionalId(final String s) throws Exception
  {
    if (s.equals("1"))
    {
      return "early";
    }
    if (s.equals("2"))
    {
      return "late";
    }
    throw new Exception("Wrong marker id");
  }
}
