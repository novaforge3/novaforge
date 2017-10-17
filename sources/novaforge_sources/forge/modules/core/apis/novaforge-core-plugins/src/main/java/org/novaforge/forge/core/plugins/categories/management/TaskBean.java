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
package org.novaforge.forge.core.plugins.categories.management;

/**
 * Generic bean interface which represents a full task to realize on a project
 */
public interface TaskBean extends TaskInfoBean
{

  /**
   * Get the consumedTime
   * 
   * @return the consumedTime
   */
  float getConsumedTime();

  /**
   * Set the consumedTime
   * 
   * @param consumedTime
   *          the consumedTime to set
   */
  void setConsumedTime(float consumedTime);

  /**
   * Get the remainingTime
   * 
   * @return the remainingTime
   */
  float getRemainingTime();

  /**
   * Set the remainingTime
   * 
   * @param remainingTime
   *          the remainingTime to set
   */
  void setRemainingTime(float remainingTime);

  /**
   * Get the comment
   * 
   * @return the comment
   */
  String getComment();

  /**
   * Set the comment
   * 
   * @param comment
   *          the comment to set
   */
  void setComment(String comment);

  /**
   * Get the discipline
   * 
   * @return the discipline
   */
  DisciplineBean getDiscipline();

  /**
   * Set the discipline
   * 
   * @param discipline
   *          the discipline to set
   */
  void setDiscipline(DisciplineBean discipline);

  /**
   * Get the scopeUnit
   * 
   * @return the scopeUnit
   */
  ScopeUnitBean getScopeUnit();

  /**
   * Set the scopeUnit
   * 
   * @param scopeUnit
   *          the scopeUnit to set
   */
  void setScopeUnit(ScopeUnitBean scopeUnit);

  /**
   * Get the category
   * 
   * @return the category
   */
  TaskCategoryBean getCategory();

  /**
   * Set the category
   * 
   * @param category
   *          the category to set
   */
  void setCategory(TaskCategoryBean category);

  /**
   * Get the issue
   * 
   * @return the issue
   */
  IssueBean getIssue();

  /**
   * Set the issue
   * 
   * @param issue
   *          the issue to set
   */
  void setIssue(IssueBean issue);

  /**
   * Get the iteration
   * 
   * @return the iteration
   */
  IterationBean getIteration();

  /**
   * Set the iteration
   * 
   * @param iteration
   *          the iteration to set
   */
  void setIteration(IterationBean iteration);

}
