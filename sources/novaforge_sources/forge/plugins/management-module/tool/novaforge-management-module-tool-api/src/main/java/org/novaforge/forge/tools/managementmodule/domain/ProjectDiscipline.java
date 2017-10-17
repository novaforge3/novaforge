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

package org.novaforge.forge.tools.managementmodule.domain;

import java.io.Serializable;

/**
 * ProjectDiscipline is the representation of the join table between a project and a discipline, it gives the
 * information for the couple (repartition...)
 */
public interface ProjectDiscipline extends Serializable
{
   /**
    * Get the repartition
    * @return the repartition
    */
   int getRepartition();

   /**
    * Set the repartition
    * @param repartition the repartition to set
    */
   void setRepartition(int repartition);

   /**
    * Get the id
    * @return the id
    */
   Long getId();

   /**
    * Get the project
    * @return the project
    */
   Project getProject();

   /**
    * Set the project
    * @param project the project to set
    */
   void setProject(Project project);

   /**
    * Get the discipline
    * @return the discipline
    */
   Discipline getDiscipline();

   /**
    * Set the discipline
    * @param discipline the discipline to set
    */
   void setDiscipline(Discipline discipline);

   @Override
   int hashCode();

   @Override
   boolean equals(Object obj);

   @Override
   String toString();
}
