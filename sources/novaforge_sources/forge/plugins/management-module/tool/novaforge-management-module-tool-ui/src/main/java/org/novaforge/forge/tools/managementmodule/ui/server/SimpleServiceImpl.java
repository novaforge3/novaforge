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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.business.IndicatorsManager;
import org.novaforge.forge.tools.managementmodule.business.IterationManager;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.business.TaskManager;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.Role;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.domain.transfer.IterationTaskIndicators;
import org.novaforge.forge.tools.managementmodule.ui.client.service.SimpleService;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeRightsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationTaskDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class SimpleServiceImpl extends RemoteServiceServlet implements SimpleService
{

  private static final String SESSION_CONNECTED_USER = "connectedUser";

  /**
   * UID
   */
  private static final long   serialVersionUID       = 4402851314251357065L;

  private static final Log    LOG                    = LogFactory.getLog(SimpleServiceImpl.class);

  protected IterationManager getIterationManager()
  {
    return OSGiServiceGetter.getService(IterationManager.class);
  }

  protected ReferentielManager getReferentielManager()
  {
    return OSGiServiceGetter.getService(ReferentielManager.class);
  }

  protected ProjectPlanManager getProjectPlanManager()
  {
    return OSGiServiceGetter.getService(ProjectPlanManager.class);
  }

  @Override
  public String checkAndRegisterUser() throws ManagementModuleException
  {

    final String remoteUser = getThreadLocalRequest().getRemoteUser();
    try
    {
      final User user = getManagementModuleManager().getUser(remoteUser);
      getSession().setAttribute(SESSION_CONNECTED_USER, user);
      if (user == null)
      {
        LOG.error(String.format("The %s login is not known in application", remoteUser));
        throw new ManagementModuleException(ErrorEnumeration.ERR_AUTHENTIFICATION_OR_INSUFFICIENT_RIGHTS);
      }
    }
    catch (final Exception e)
    {
      throw manageException(String.format("Unable to get identificate user %s", remoteUser), e);
    }
    return remoteUser;
  }

  protected ManagementModuleManager getManagementModuleManager()
  {
    return OSGiServiceGetter.getService(ManagementModuleManager.class);
  }

  public HttpSession getSession()
  {
    return getThreadLocalRequest().getSession();
  }

  /**
   * This method manages exceptions in order to write LOG error and get
   * ForgeCodeException
   *
   * @param pMessage
   * @param e
   */
  protected ManagementModuleException manageException(final String pMessage, final Exception e)
  {
    // handle functional exceptions
    LOG.error(pMessage, e);
    if ((e instanceof org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException)
            && (((org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException) e).getCode()
                    != null))
    {
      final org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException mme = (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException) e;
      try
      {
        final ErrorEnumeration error = ErrorEnumeration.valueOf(mme.getCode().name());
        return new ManagementModuleException(error, pMessage, e);
      }
      catch (final IllegalArgumentException iaex)
      {
        LOG.debug("Unable to find " + mme.getCode().name() + " in the UI error enumeration");
        return new ManagementModuleException(ErrorEnumeration.TECHNICAL_ERROR, pMessage, iaex);
      }
    }
    else if (e instanceof ManagementModuleException)
    {
      return new ManagementModuleException(((ManagementModuleException) e).getErrors(), pMessage, e);
    }
    else
    {
      return new ManagementModuleException(ErrorEnumeration.TECHNICAL_ERROR, pMessage, e);
    }
  }

  @Override
  public Set<DisciplineDTO> getDisciplinesOfConnectedUser(final String projectId)
      throws ManagementModuleException
  {
    try
    {
      final Role role = getRoleConnectedUser(projectId);
      final Set<DisciplineDTO> setDiscipline = new HashSet<DisciplineDTO>();
      for (final Discipline discipline : role.getListDisciplines())
      {
        setDiscipline.add(BuildResources.buildDisciplineDTO(discipline));
      }
      return setDiscipline;
    }
    catch (final Exception e)
    {
      throw manageException("Unable to get Displines of connected user", e);
    }
  }

  protected Role getRoleConnectedUser(final String projectId) throws ManagementModuleException
  {
    try
    {
      final Membership membership = getManagementModuleManager().getMembership(getConnectedUser().getLogin(),
                                                                               projectId);
      return membership.getRole();
    }
    catch (final Exception e)
    {
      throw manageException("Unable to get Role of connected user", e);
    }
  }

  /**
   * Get the connected user
   *
   * @return the connected user
   * @throws ManagementModuleException
   *           if user is null
   */
  protected User getConnectedUser() throws ManagementModuleException
  {
    final User user = (User) getSession().getAttribute(SESSION_CONNECTED_USER);
    if (user == null)
    {
      throw new ManagementModuleException(ErrorEnumeration.ERR_AUTHENTIFICATION_OR_INSUFFICIENT_RIGHTS);
    }
    return user;
  }

  @Override
  public Set<ApplicativeRightsDTO> getAbilitiesOfConnectedUser(final String projectId) throws ManagementModuleException
  {
    try
    {
      final Role role = getRoleConnectedUser(projectId);
      final Set<ApplicativeRights> functionalAbilities = role.getFunctionalAbilityList();
      final Set<ApplicativeRightsDTO> setAppRightsDTO = new HashSet<ApplicativeRightsDTO>();
      for (final ApplicativeRights applicativeRights : functionalAbilities)
      {
        setAppRightsDTO.add(BuildResources.buildApplicativeRightsDTO(applicativeRights));
      }
      return setAppRightsDTO;
    }
    catch (final Exception e)
    {
      throw manageException("Unable to get abilities of connected user", e);
    }
  }

  protected IterationDTO getComplexIterationDTOFromIteration(final Iteration it)
      throws ManagementModuleException
  {

    try
    {
      final IterationDTO dto = BuildResources.buildSimpleIterationDTOFromIteration(it);
      final Set<IterationTaskDTO> iterationTasks = new HashSet<IterationTaskDTO>();
      for (final IterationTask iterationTask : getTaskManager().getFullIterationTasksList(it))
      {
        final IterationTaskIndicators indicators = getIndicatorsManager().getIterationTaskIndicators(
            iterationTask);
        iterationTasks.add(BuildResources.buildIterationTaskDTOFromIterationTask(iterationTask, indicators));
      }
      dto.setIterationTasks(iterationTasks);
      return dto;
    }
    catch (org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException e)
    {
      throw manageException("Unable to get iterationTask list with iterationId=" + it.getId(), e);
    }
  }

  protected TaskManager getTaskManager()
  {
    return OSGiServiceGetter.getService(TaskManager.class);
  }

  protected IndicatorsManager getIndicatorsManager()
  {
    return OSGiServiceGetter.getService(IndicatorsManager.class);
  }

  /**
   * Check if given data is null, if so return ""
   * 
   * @param data
   * @return the data or ""
   */
  protected String checkData(final String data)
  {
    String ret = "";
    if (data != null)
    {
      ret = data;
    }
    return ret;
  }

}
