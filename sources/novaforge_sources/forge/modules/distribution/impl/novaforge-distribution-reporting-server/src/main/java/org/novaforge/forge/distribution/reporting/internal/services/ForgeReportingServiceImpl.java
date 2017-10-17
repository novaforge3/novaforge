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
package org.novaforge.forge.distribution.reporting.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;
import org.novaforge.forge.distribution.reporting.dao.ForgeAnalysisDAO;
import org.novaforge.forge.distribution.reporting.dao.ForgeDimensionDAO;
import org.novaforge.forge.distribution.reporting.domain.ForgeAnalysis;
import org.novaforge.forge.distribution.reporting.domain.ForgeDimension;
import org.novaforge.forge.distribution.reporting.domain.ProjectDTO;
import org.novaforge.forge.distribution.reporting.domain.ProjectDimension;
import org.novaforge.forge.distribution.reporting.domain.UserDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.StatusDataAccessEnum;
import org.novaforge.forge.distribution.reporting.exceptions.ExceptionCode;
import org.novaforge.forge.distribution.reporting.exceptions.ForgeReportingException;
import org.novaforge.forge.distribution.reporting.services.ForgeReportingService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Bilet-jc
 * @author Mohamed IBN EL AZZOUZI
 */
public class ForgeReportingServiceImpl implements ForgeReportingService
{

  private static final Log LOG = LogFactory.getLog(ForgeReportingServiceImpl.class);
  private ForgeDistributionService forgeDistributionService;
  private ForgeAnalysisDAO         forgeAnalysisDAO;
  private ForgeDimensionDAO        forgeDimensionDAO;

  @Override
  public List<ForgeViewDTO> getForgeView(final UUID forgeId) throws ForgeReportingException
  {
    if (forgeId == null)
    {
      throw new IllegalArgumentException("forgeId should not be null");
    }
    if (LOG.isDebugEnabled())
    {
      LOG.debug("getting forge view forgeId: " + forgeId.toString());
    }
    ArrayList<ForgeViewDTO> forgeViewDTOs = new ArrayList<>();

    ForgeDTO forge = null;
    StatusDataAccessEnum distributionState = StatusDataAccessEnum.ACCESS;
    try
    {
      forge = forgeDistributionService.getForge(forgeId.toString());
    }
    catch (final Exception e)
    {
      distributionState = StatusDataAccessEnum.DISTRIBUTION_DOWN;
    }
    List<String> uuids = new ArrayList<>();
    if (forge == null)
    {
      uuids.add(forgeId.toString());
    }
    else
    {
      uuids = getTreeForges(forge);
    }
    if (uuids != null)
    {
      final List<Map<String, String>> projectAndAccountNumbersByForges = forgeAnalysisDAO
                                                                             .findProjectAndAccountNumbersByForges(uuids
                                                                                                                       .toArray(new String[uuids
                                                                                                                                               .size()]));

      /**
       * That use forgeDimensionDAO, it must return a least one forge
       * and findProjectAndAccountNumbersByForges can return empty list 
       * My god ... 
       */
      if (!projectAndAccountNumbersByForges.isEmpty())
      {
        forgeViewDTOs = convertToForgeViewDTOs(projectAndAccountNumbersByForges, distributionState);

        for (String fId : uuids)
        {
          ForgeDimension f = forgeDimensionDAO.findByForgeId(UUID.fromString(fId));
          for (ForgeViewDTO fDTO : forgeViewDTOs)
          {
            if (fDTO.getForgeName().equals(f.getName()))
            {
              fDTO.setLastUpdated(f.getLastUpdated().toString());
              break;
            }
          }
        }
      }
    }

    if (LOG.isDebugEnabled())
    {
      LOG.debug("forge view list: " + forgeViewDTOs);
    }
    return forgeViewDTOs;
  }

  @Override
  public ArrayList<ProfilViewDTO> getProfilView(final UUID forgeId) throws ForgeReportingException
  {
    ArrayList<ProfilViewDTO> profilViewDTOs = new ArrayList<>();

    ForgeDTO forge = null;
    StatusDataAccessEnum distributionState = StatusDataAccessEnum.ACCESS;
    try
    {
      forge = forgeDistributionService.getForge(forgeId.toString());
    }
    catch (final Exception e)
    {
      distributionState = StatusDataAccessEnum.DISTRIBUTION_DOWN;
    }
    List<String> uuids = new ArrayList<>();
    if (forge == null)
    {
      uuids.add(forgeId.toString());
    }
    else
    {
      uuids = getTreeForges(forge);
    }
    if (uuids != null)
    {
      final List<Map<String, String>> accountNumbersByRolesAndForges = forgeAnalysisDAO
                                                                           .findAccountNumbersByRolesAndForges(uuids
                                                                                                                   .toArray(new String[uuids
                                                                                                                                           .size()]));
      profilViewDTOs = convertToProfilViewDTOs(accountNumbersByRolesAndForges, distributionState);
    }
    return profilViewDTOs;
  }

  @Override
  public List<OrganizationViewDTO> getOrganizationView(final UUID forgeId) throws ForgeReportingException
  {
    List<OrganizationViewDTO> organizationViewDTOs = new LinkedList<>();

    ForgeDTO forge = null;
    StatusDataAccessEnum distributionState = StatusDataAccessEnum.ACCESS;
    try
    {
      forge = forgeDistributionService.getForge(forgeId.toString());
    }
    catch (final Exception e)
    {
      distributionState = StatusDataAccessEnum.DISTRIBUTION_DOWN;
    }
    List<String> uuids = new ArrayList<>();
    if (forge == null)
    {
      uuids.add(forgeId.toString());
    }
    else
    {
      uuids = getTreeForges(forge);
    }
    if (uuids != null)
    {
      final List<Map<String, String>> projectNumbersByOrganizations = forgeAnalysisDAO
                                                                          .findProjectNumbersByOrganization(uuids
                                                                                                                .toArray(new String[uuids
                                                                                                                                        .size()]));
      organizationViewDTOs = convertToOrganizationViewDTOs(projectNumbersByOrganizations, distributionState);
    }
    return organizationViewDTOs;
  }

  @Override
  public void storeForgeData(final UUID forgeId, final List<ProjectDTO> projects, final List<UserDTO> users)
      throws ForgeReportingException
  {

    try
    {

      clearForgeData(forgeId);

      final ForgeDTO forge = forgeDistributionService.getForge(forgeId.toString());

      final ForgeDimension forgeDimension = forgeDimensionDAO.newForgeDimension();
      forgeDimension.setForgeId(forgeId);
      forgeDimension.setName(forge.getLabel());
      forgeDimension.setForgeLevel(forge.getForgeLevel());
      forgeDimension.setLastUpdated(new Date());

      final ForgeDimension createdForge = forgeDimensionDAO.save(forgeDimension);
      if (projects != null)
      {
        for (final ProjectDTO projectDTO : projects)
        {
          final ProjectDimension projectDimension = forgeAnalysisDAO.newProjectDimension();
          projectDimension.setName(projectDTO.getName());
          projectDimension.setOrganization(projectDTO.getOrganization());
          final ProjectDimension createdProject = forgeAnalysisDAO.save(projectDimension);

          final List<UserDTO> projectUsers = getProjectUsers(projectDTO.getProjectId(), users);
          for (final UserDTO user : projectUsers)
          {
            final String userLogin = user.getUserLogin();
            final String userRole = user.getUserRole();
            final ForgeAnalysis forgeAnalysis = forgeAnalysisDAO.newForgeAnalysis();
            forgeAnalysis.setConnexionNumber(0);
            forgeAnalysis.setAccountLogin(userLogin);
            forgeAnalysis.setUserRole(userRole);

            final ForgeAnalysis createdForgeAnalysis = forgeAnalysisDAO.save(forgeAnalysis);

            createdForgeAnalysis.setForge(createdForge);
            createdForgeAnalysis.setProject(createdProject);
            forgeAnalysisDAO.update(createdForgeAnalysis);
          }

        }
      }

    }
    catch (final ForgeDistributionException e)
    {
      throw new ForgeReportingException(ExceptionCode.TECHNICAL_ERROR, e);
    }

  }

  private void clearForgeData(final UUID pForgeId)
  {
    try
    {
      final ForgeDimension forgeDimension = forgeDimensionDAO.findByForgeId(pForgeId);
      final List<ProjectDimension> projects = forgeAnalysisDAO.findDimensionByForgeId(pForgeId);

      final List<ForgeAnalysis> forgeAnalysises = forgeAnalysisDAO.findAnalysisByForgeId(pForgeId);
      for (final ForgeAnalysis forgeAnalysis : forgeAnalysises)
      {
        forgeAnalysisDAO.delete(forgeAnalysis);
      }

      for (final ProjectDimension project : projects)
      {
        forgeAnalysisDAO.delete(project);
      }

      forgeDimensionDAO.delete(forgeDimension);
    }
    catch (final NoResultException e)
    {
      e.printStackTrace();
    }

  }

  private List<UserDTO> getProjectUsers(final String projectId, final List<UserDTO> users)
  {
    final List<UserDTO> result = new LinkedList<>();

    for (final UserDTO user : users)
    {
      final String userProjectId = user.getProjectId();
      if (projectId.equals(userProjectId))
      {
        result.add(user);
      }
    }

    return result;
  }

  private List<String> getTreeForges(final ForgeDTO forgeDTO) throws ForgeReportingException
  {

    final List<String> uuids = new LinkedList<>();
    try
    {
      uuids.add(forgeDTO.getForgeId().toString());

      final Set<ForgeDTO> children = forgeDTO.getChildren();
      if (children != null)
      {
        for (final ForgeDTO child : children)
        {
          uuids.addAll(getTreeForges(child));
        }
      }
    }
    catch (final Exception e)
    {
      throw new ForgeReportingException(ExceptionCode.ERR_LOADING_LIST_FORGE, e);
    }
    return uuids;
  }

  private ArrayList<ForgeViewDTO> convertToForgeViewDTOs(final List<Map<String, String>> projectAndAccountNumbersByForges,
                                                         final StatusDataAccessEnum distributionState)
      throws ForgeReportingException
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("convertToForgeViewDTOs size : " + projectAndAccountNumbersByForges.size());
    }

    final ArrayList<ForgeViewDTO> forgeViewDTOs = new ArrayList<>();
    try
    {
      if (projectAndAccountNumbersByForges != null)
      {
        for (final Map<String, String> projectAndAccountNumbers : projectAndAccountNumbersByForges)
        {
          if (LOG.isDebugEnabled())
          {
            LOG.debug("convertToForgeViewDTOs FORGENAME : " + projectAndAccountNumbers.get(ForgeAnalysisDAO.FORGENAME));
            LOG.debug("convertToForgeViewDTOs NBPROJECTS : " + projectAndAccountNumbers
                                                                   .get(ForgeAnalysisDAO.NBPROJECTS));

          }
          final ForgeViewDTO forgeViewDTO = new ForgeViewDTO();
          forgeViewDTO.setStatus(distributionState);
          forgeViewDTO.setForgeName(projectAndAccountNumbers.get(ForgeAnalysisDAO.FORGENAME));
          forgeViewDTO.setNumberAccount(Integer.valueOf(projectAndAccountNumbers.get(ForgeAnalysisDAO.NBACCOUNT)));
          forgeViewDTO.setNumberProject(Integer.valueOf(projectAndAccountNumbers.get(ForgeAnalysisDAO.NBPROJECTS)));
          forgeViewDTOs.add(forgeViewDTO);
        }
      }
    }
    catch (final Exception e)
    {
      throw new ForgeReportingException(ExceptionCode.TECHNICAL_ERROR, e);
    }
    return forgeViewDTOs;
  }

  private ArrayList<ProfilViewDTO> convertToProfilViewDTOs(final List<Map<String, String>> accountNumbersByRolesAndForges,
                                                           final StatusDataAccessEnum distributionState)
      throws ForgeReportingException
  {
    final ArrayList<ProfilViewDTO> profilViewDTOs = new ArrayList<>();
    try
    {
      if (accountNumbersByRolesAndForges != null)
      {

        final List<String> listRoles = getAllRoles(accountNumbersByRolesAndForges);
        if (LOG.isDebugEnabled())
        {
          LOG.debug("listRoles.size() : " + listRoles.size());
        }

        String forgeName;
        String projectName;
        String roleName;
        String nbUserAccount;
        ProfilViewDTO profilViewDTO = null;

        for (final Map<String, String> accountNumbersByRolesAndForge : accountNumbersByRolesAndForges)
        {
          roleName = accountNumbersByRolesAndForge.get(ForgeAnalysisDAO.USERROLE);
          if (!roleName.equals(""))
          {
            forgeName = accountNumbersByRolesAndForge.get(ForgeAnalysisDAO.FORGENAME);
            projectName = accountNumbersByRolesAndForge.get(ForgeAnalysisDAO.PROJECTNAME);
            nbUserAccount = accountNumbersByRolesAndForge.get(ForgeAnalysisDAO.NBUSERACCOUNT);
            if (LOG.isDebugEnabled())
            {
              LOG.debug("- forgeName : " + forgeName);
              LOG.debug("- projectName : " + projectName);
              LOG.debug("- roleName : " + roleName);
              LOG.debug("- nbUserAccount : " + nbUserAccount);
            }

            if ((profilViewDTO == null) || !profilViewDTO.getForgeName().equals(forgeName) || !profilViewDTO
                                                                                                   .getProjectName()
                                                                                                   .equals(projectName))
            {
              profilViewDTO = new ProfilViewDTO();
              profilViewDTO.setStatus(distributionState);
              profilViewDTO.setForgeName(forgeName);
              profilViewDTO.setProjectName(projectName);
              final Map<String, Integer> roles = new HashMap<>();
              for (final String roleNameFromList : listRoles)
              {
                if (roleName.equals(roleNameFromList))
                {
                  roles.put(roleName, Integer.valueOf(nbUserAccount));
                }
                else
                {
                  roles.put(roleNameFromList, 0);
                }
              }
              profilViewDTO.setRoles(roles);
              profilViewDTOs.add(profilViewDTO);
            }
            else
            {
              for (final Map.Entry<String, Integer> roleNameFromProfil : profilViewDTO.getRoles().entrySet())
              {
                if (roleName.equals(roleNameFromProfil.getKey()))
                {
                  profilViewDTO.getRoles().put(roleName, new Integer(nbUserAccount));
                }
              }
            }
          }
        }
      }
    }
    catch (final Exception e)
    {
      throw new ForgeReportingException(ExceptionCode.TECHNICAL_ERROR, e);
    }
    return profilViewDTOs;
  }

  private List<String> getAllRoles(final List<Map<String, String>> accountNumbersByRolesAndForges)
  {

    final List<String> listRoles = new ArrayList<>();
    for (final Map<String, String> rolesFromProjects : accountNumbersByRolesAndForges)
    {

      final String roleNameTmp = rolesFromProjects.get(ForgeAnalysisDAO.USERROLE);
      boolean findRole = false;
      for (final String roleName : listRoles)
      {
        if (roleNameTmp.equals(roleName))
        {
          findRole = true;
        }
      }
      if (!roleNameTmp.equals("") && !findRole)
      {
        listRoles.add(roleNameTmp);
      }
    }
    return listRoles;
  }

  private List<OrganizationViewDTO> convertToOrganizationViewDTOs(final List<Map<String, String>> projectNumbersByOrganizations,
                                                                  final StatusDataAccessEnum distributionState)
      throws ForgeReportingException
  {
    final List<OrganizationViewDTO> organizationViewDTOs = new LinkedList<>();
    try
    {
      if (projectNumbersByOrganizations != null)
      {
        for (final Map<String, String> projectNumbersByOrganization : projectNumbersByOrganizations)
        {
          final OrganizationViewDTO organizationViewDTO = new OrganizationViewDTO();
          organizationViewDTO.setStatus(distributionState);
          organizationViewDTO.setOrganizationName(projectNumbersByOrganization.get(ForgeAnalysisDAO.ORGANIZATION));
          organizationViewDTO.setNumberProject(Integer.valueOf(projectNumbersByOrganization
                                                                   .get(ForgeAnalysisDAO.NBPROJECTS)));
          organizationViewDTOs.add(organizationViewDTO);
        }
      }
    }
    catch (final Exception e)
    {
      throw new ForgeReportingException(ExceptionCode.TECHNICAL_ERROR, e);
    }
    return organizationViewDTOs;
  }

  /**
   * @param pForgeDistributionService
   *     the forgeDistributionService to set
   */
  public void setForgeDistributionService(final ForgeDistributionService pForgeDistributionService)
  {
    forgeDistributionService = pForgeDistributionService;
  }

  /**
   * @param pForgeAnalysisDAO
   *     the forgeAnalysisDAO to set
   */
  public void setForgeAnalysisDAO(final ForgeAnalysisDAO pForgeAnalysisDAO)
  {
    forgeAnalysisDAO = pForgeAnalysisDAO;
  }

  /**
   * @param pForgeDimensionDAO
   *     the forgeDimensionDAO to set
   */
  public void setForgeDimensionDAO(final ForgeDimensionDAO pForgeDimensionDAO)
  {
    forgeDimensionDAO = pForgeDimensionDAO;
  }
}
