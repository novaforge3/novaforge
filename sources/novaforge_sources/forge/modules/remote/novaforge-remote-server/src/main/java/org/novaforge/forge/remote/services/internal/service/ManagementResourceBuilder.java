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
package org.novaforge.forge.remote.services.internal.service;

import org.novaforge.forge.core.plugins.categories.management.DisciplineBean;
import org.novaforge.forge.core.plugins.categories.management.IssueBean;
import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ScopeUnitBean;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskCategoryBean;
import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;
import org.novaforge.forge.remote.services.internal.bean.DisciplineBeanImpl;
import org.novaforge.forge.remote.services.internal.bean.IterationBeanImpl;
import org.novaforge.forge.remote.services.internal.bean.ScopeUnitBeanImpl;
import org.novaforge.forge.remote.services.internal.bean.TaskBeanImpl;
import org.novaforge.forge.remote.services.internal.bean.TaskCategoryBeanImpl;
import org.novaforge.forge.remote.services.model.management.BugTrackerIssue;
import org.novaforge.forge.remote.services.model.management.Discipline;
import org.novaforge.forge.remote.services.model.management.Iteration;
import org.novaforge.forge.remote.services.model.management.ScopeUnit;
import org.novaforge.forge.remote.services.model.management.Task;
import org.novaforge.forge.remote.services.model.management.TaskCategory;
import org.novaforge.forge.remote.services.model.management.TaskInfo;
import org.novaforge.forge.remote.services.model.management.TaskStatus;
import org.novaforge.forge.remote.services.model.management.TaskType;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper class used to convert the Task API defined into the plugin management category into the Remote
 * Management Service API.
 * 
 * @author rols-p
 */
public class ManagementResourceBuilder
{
  /**
   * Build a Task in function of a TaskBean.
   * 
   * @param taskBean
   *          the input TaskBean
   * @return a nullable Task.
   */
  public static Task buildTask(final TaskBean taskBean)
  {
    Task task = null;
    if (taskBean != null)
    {
      task = new Task();
      task.setCategory(buildTaskCategory(taskBean.getCategory()));
      task.setComment(taskBean.getComment());
      task.setConsumedTime(taskBean.getConsumedTime());
      task.setDescription(taskBean.getDescription());
      task.setDiscipline(buildDiscipline(taskBean.getDiscipline()));
      task.setEndDate(taskBean.getEndDate());
      task.setId(taskBean.getId());
      task.setInitialEstimation(taskBean.getInitialEstimation());
      task.setIssue(buildIssue(taskBean.getIssue()));
      task.setIteration(buildIteration(taskBean.getIteration()));
      task.setLastUpdateDate(taskBean.getLastUpdateDate());
      task.setRemainingTime(taskBean.getRemainingTime());
      task.setScopeUnit(buildScopeUnit(taskBean.getScopeUnit()));
      task.setStartDate(taskBean.getStartDate());
      task.setStatus(buildStatus(taskBean.getStatus()));
      task.setTitle(taskBean.getTitle());
      task.setType(buildType(taskBean.getType()));
    }
    return task;

  }

  /**
   * Builds an TaskCategory in function of a TaskCategoryBean
   *
   * @param taskCategoryBean
   *          the incomming TaskCategoryBean
   * @return a nullable TaskCategory
   */
  private static TaskCategory buildTaskCategory(final TaskCategoryBean taskCategoryBean)
  {
    TaskCategory taskCategory = null;
    if (taskCategoryBean != null)
    {
      taskCategory = new TaskCategory();
      taskCategory.setId(taskCategoryBean.getId());
      taskCategory.setDescription(taskCategoryBean.getDescription());
    }
    return taskCategory;
  }

  /**
   * Builds an Discipline in function of a DisciplineBean
   *
   * @param disciplineBean
   *          the incomming DisciplineBean
   * @return a nullable Discipline
   */
  private static Discipline buildDiscipline(final DisciplineBean disciplineBean)
  {
    Discipline discipline = null;
    if (disciplineBean != null)
    {
      discipline = new Discipline();
      discipline.setId(disciplineBean.getId());
      discipline.setDescription(disciplineBean.getDescription());
    }
    return discipline;
  }

  /**
   * Builds an BugTrackerIssue in function of a IssueBean
   *
   * @param issueBean
   *          the incomming IssueBean
   * @return a nullable BugTrackerIssue
   */
  private static BugTrackerIssue buildIssue(final IssueBean issueBean)
  {
    BugTrackerIssue issue = null;
    if (issueBean != null)
    {
      issue = new BugTrackerIssue();
      issue.setBugId(issueBean.getBugId());
      issue.setBugTrackerId(issueBean.getBugTrackerId());
      issue.setTitle(issueBean.getTitle());
      issue.setDescription(issueBean.getDescription());
      issue.setAdditionalInfo(issueBean.getAdditionalInfo());
      issue.setAssignedTo(issueBean.getAssignedTo());
      issue.setCategory(issueBean.getCategory());
      issue.setPriority(issueBean.getPriority());
      issue.setProductVersion(issueBean.getProductVersion());
      issue.setReporter(issueBean.getReporter());
      issue.setReproducibility(issueBean.getReproducibility());
      issue.setResolution(issueBean.getResolution());
      issue.setSeverity(issueBean.getSeverity());
      issue.setStatus(issueBean.getStatus());
    }
    return issue;
  }

  /**
   * Builds an Iteration in function of a IterationBean
   *
   * @param iterationBean
   *          the incomming IterationBean
   * @return a nullable Iteration
   */
  private static Iteration buildIteration(final IterationBean iterationBean)
  {
    Iteration iteration = null;
    if (iterationBean != null)
    {
      iteration = new Iteration();
      iteration.setId(iterationBean.getId());
      iteration.setEndDate(iterationBean.getEndDate());
      iteration.setStartDate(iterationBean.getStartDate());
      iteration.setIterationNumber(iterationBean.getIterationNumber());
    }
    return iteration;
  }

  /**
   * Builds a ScopeUnit in function of a ScopeUnitBean
   *
   * @param scopeUnitBean
   *          the incoming scopeUnitBean
   * @return a nullable ScopeUnit
   */
  private static ScopeUnit buildScopeUnit(final ScopeUnitBean scopeUnitBean)
  {
    ScopeUnit scopeUnit = null;
    if (scopeUnitBean != null)
    {
      scopeUnit = new ScopeUnit();
      scopeUnit.setId(scopeUnitBean.getId());
      scopeUnit.setDescription(scopeUnitBean.getDescription());
      scopeUnit.setTitle(scopeUnitBean.getTitle());
      scopeUnit.setTypeDescription(scopeUnitBean.getTypeDescription());
    }
    return scopeUnit;
  }

  /**
   * Builds a TaskStatus in function of a TaskStatusEnum
   *
   * @param status
   *          TaskStatusEnum
   * @return a TaskStatus
   */
  private static TaskStatus buildStatus(final TaskStatusEnum status)
  {
    TaskStatus taskStatus = null;
    switch (status)
    {
      case CANCELED:
        taskStatus = TaskStatus.CANCELLED;
        break;
      case DONE:
        taskStatus = TaskStatus.DONE;
        break;
      case IN_PROGRESS:
        taskStatus = TaskStatus.IN_PROGRESS;
        break;
      case NOT_AFFECTED:
        taskStatus = TaskStatus.NOT_AFFECTED;
        break;
      case NOT_STARTED:
        taskStatus = TaskStatus.NOT_STARTED;
        break;
    }
    return taskStatus;
  }

  /**
   * Builds a TaskType in function of a TaskTypeEnum
   *
   * @param type
   *          TaskTypeEnum
   * @return a TaskType
   */
  private static TaskType buildType(final TaskTypeEnum type)
  {
    TaskType taskType = null;
    switch (type)
    {
      case BUG:
        taskType = TaskType.BUG;
        break;
      case WORK:
        taskType = TaskType.WORK;
        break;
    }
    return taskType;
  }

  /**
   * Builds a Set of TaskInfo in function of a Set of TaskInfoBean.
   *
   * @param taskInfoBeans
   *          the Set of TaskInfobeans
   * @return a not-null but possibly empty Set<TaskInfo>
   */
  public static Set<TaskInfo> buildTaskInfos(final Set<TaskInfoBean> taskInfoBeans)
  {
    final Set<TaskInfo> taskInfos = new HashSet<TaskInfo>();
    if ((taskInfoBeans != null) && !taskInfoBeans.isEmpty())
    {

      for (final TaskInfoBean taskInfoBean : taskInfoBeans)
      {
        final TaskInfo taskInfo = buildTaskInfo(taskInfoBean);
        taskInfos.add(taskInfo);
      }
    }
    return taskInfos;
  }

  /**
   * Build a TaskInfo in function of a TaskInfoBean.
   *
   * @param taskInfoBean
   *          the input TaskBean
   * @return a nullable TaskInfo.
   */
  public static TaskInfo buildTaskInfo(final TaskInfoBean taskInfoBean)
  {
    TaskInfo taskInfo = null;
    if (taskInfoBean != null)
    {
      taskInfo = new Task();
      taskInfo.setDescription(taskInfoBean.getDescription());
      taskInfo.setId(taskInfoBean.getId());
      taskInfo.setTitle(taskInfoBean.getTitle());
      taskInfo.setType(buildType(taskInfoBean.getType()));
      taskInfo.setLastUpdateDate(taskInfoBean.getLastUpdateDate());
      taskInfo.setStatus(buildStatus(taskInfoBean.getStatus()));
      taskInfo.setStartDate(taskInfoBean.getStartDate());
      taskInfo.setEndDate(taskInfoBean.getEndDate());
      taskInfo.setInitialEstimation(taskInfoBean.getInitialEstimation());
    }
    return taskInfo;
  }

  /**
   * @param iterationBeans
   * @return
   */
  public static Set<Iteration> buildIterations(final Set<IterationBean> iterationBeans)
  {
    final Set<Iteration> iterations = new HashSet<Iteration>();
    if ((iterationBeans != null) && !iterationBeans.isEmpty())
    {

      for (final IterationBean iterationBean : iterationBeans)
      {
        final Iteration iteration = buildIteration(iterationBean);
        iterations.add(iteration);
      }
    }
    return iterations;
  }

  /**
   * Returns a TaskBean in function of a Task. The Issuebean is not set as it will never be modified through
   * the Management API.
   *
   * @param pTaskToModify
   *          the incoming task to be updated.
   * @return a nullable Taskbean
   */
  public static TaskBean buildTaskBean(final Task pTaskToModify)
  {
    final TaskBean taskBean = new TaskBeanImpl();
    if (pTaskToModify != null)
    {
      taskBean.setCategory(buildTaskCategoryBean(pTaskToModify.getCategory()));
      taskBean.setComment(pTaskToModify.getComment());
      taskBean.setConsumedTime(pTaskToModify.getConsumedTime());
      taskBean.setDescription(pTaskToModify.getDescription());
      taskBean.setDiscipline(buildDisciplineBean(pTaskToModify.getDiscipline()));
      taskBean.setEndDate(pTaskToModify.getEndDate());
      taskBean.setId(pTaskToModify.getId());
      taskBean.setInitialEstimation(pTaskToModify.getInitialEstimation());
      taskBean.setIteration(buildIterationBean(pTaskToModify.getIteration()));
      taskBean.setLastUpdateDate(pTaskToModify.getLastUpdateDate());
      taskBean.setRemainingTime(pTaskToModify.getRemainingTime());
      taskBean.setScopeUnit(buildScopeUnitBean(pTaskToModify.getScopeUnit()));
      taskBean.setStartDate(pTaskToModify.getStartDate());
      taskBean.setStatus(buildTaskStatusBean(pTaskToModify.getStatus()));
      taskBean.setTitle(pTaskToModify.getTitle());
      taskBean.setType(buildTaskTypeBean(pTaskToModify.getType()));
    }
    return taskBean;
  }

  /**
   * Builds an TaskCategoryBean in function of a TaskCategory
   *
   * @param pTaskCategory
   *          the incomming TaskCategory
   * @return a nullable TaskCategoryBean
   */
  private static TaskCategoryBean buildTaskCategoryBean(final TaskCategory pTaskCategory)
  {
    TaskCategoryBean taskCategoryBean = null;
    if (pTaskCategory != null)
    {
      taskCategoryBean = new TaskCategoryBeanImpl();
      taskCategoryBean.setId(pTaskCategory.getId());
      taskCategoryBean.setDescription(pTaskCategory.getDescription());
    }
    return taskCategoryBean;
  }

  /**
   * Builds an DisciplineBean in function of a Discipline
   *
   * @param pDiscipline
   *          the incomming Discipline
   * @return a nullable DisciplineBean
   */
  private static DisciplineBean buildDisciplineBean(final Discipline pDiscipline)
  {
    DisciplineBean disciplineBean = null;
    if (pDiscipline != null)
    {
      disciplineBean = new DisciplineBeanImpl();
      disciplineBean.setId(pDiscipline.getId());
      disciplineBean.setDescription(pDiscipline.getDescription());
    }
    return disciplineBean;
  }

  /**
   * Builds an IterationBean in function of a Iteration
   *
   * @param pIteration
   *          the incomming Iteration
   * @return a nullable IterationBean
   */
  private static IterationBean buildIterationBean(final Iteration pIteration)
  {
    IterationBean iterationBean = null;
    if (pIteration != null)
    {
      iterationBean = new IterationBeanImpl();
      iterationBean.setId(pIteration.getId());
      iterationBean.setIterationNumber(pIteration.getIterationNumber());
      iterationBean.setStartDate(pIteration.getStartDate());
      iterationBean.setEndDate(pIteration.getEndDate());
    }
    return iterationBean;
  }

  /**
   * Builds an ScopeUnitBean in function of a ScopeUnit
   *
   * @param pScopeUnit
   *          the incomming ScopeUnit
   * @return a nullable ScopeUnitBean
   */
  private static ScopeUnitBean buildScopeUnitBean(final ScopeUnit pScopeUnit)
  {
    ScopeUnitBean scopeUnitBean = null;
    if (pScopeUnit != null)
    {
      scopeUnitBean = new ScopeUnitBeanImpl();
      scopeUnitBean.setId(pScopeUnit.getId());
      scopeUnitBean.setDescription(pScopeUnit.getDescription());
      scopeUnitBean.setTitle(pScopeUnit.getTitle());
      scopeUnitBean.setTypeDescription(pScopeUnit.getTypeDescription());
    }
    return scopeUnitBean;
  }

  /**
   * Builds a TaskStatusEnum in function of a TaskStatus
   *
   * @param status
   *          TaskStatus
   * @return a TaskStatusEnum
   */
  public static TaskStatusEnum buildTaskStatusBean(final TaskStatus pTaskStatus)
  {
    TaskStatusEnum taskStatusEnum = null;
    switch (pTaskStatus)
    {
      case CANCELLED:
        taskStatusEnum = TaskStatusEnum.CANCELED;
        break;
      case DONE:
        taskStatusEnum = TaskStatusEnum.DONE;
        break;
      case IN_PROGRESS:
        taskStatusEnum = TaskStatusEnum.IN_PROGRESS;
        break;
      case NOT_AFFECTED:
        taskStatusEnum = TaskStatusEnum.NOT_AFFECTED;
        break;
      case NOT_STARTED:
        taskStatusEnum = TaskStatusEnum.NOT_STARTED;
        break;
    }
    return taskStatusEnum;
  }

  /**
   * Builds a TaskTypeEnum in function of a TaskType
   *
   * @param type
   *     pTaskType
   *
   * @return a TaskTypeEnum
   */
  public static TaskTypeEnum buildTaskTypeBean(final TaskType pTaskType)
  {
    TaskTypeEnum taskTypeEnum = null;
    switch (pTaskType)
    {
      case BUG:
        taskTypeEnum = TaskTypeEnum.BUG;
        break;
      case WORK:
        taskTypeEnum = TaskTypeEnum.WORK;
        break;
    }
    return taskTypeEnum;
  }
}
