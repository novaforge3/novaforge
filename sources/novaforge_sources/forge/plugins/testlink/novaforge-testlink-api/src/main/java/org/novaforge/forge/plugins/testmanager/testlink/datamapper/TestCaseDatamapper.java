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
package org.novaforge.forge.plugins.testmanager.testlink.datamapper;

import java.io.Serializable;

public class TestCaseDatamapper implements Serializable
{

   private static final long serialVersionUID = 1191213146080628314L;

   private Integer           id;
   private String            name;
   private Integer           externalId;
   private String            projectPrefix;
   private Integer           version;

   /**
    * 
    */
   public TestCaseDatamapper()
   {
      super();

   }

   /**
    * @param id
    * @param name
    * @param externalId
    * @param projectPrefix
    * @param version
    */
   public TestCaseDatamapper(final Integer id, final String name, final Integer externalId,
         final String projectPrefix, final Integer version)
   {
      super();
      this.id = id;
      this.name = name;
      this.externalId = externalId;
      this.projectPrefix = projectPrefix;
      this.version = version;
   }

   /**
    * @return the version
    */
   public Integer getVersion()
   {
      return this.version;
   }

   /**
    * @param version
    *           the version to set
    */
   public void setVersion(final Integer version)
   {
      this.version = version;
   }

   /**
    * @return the id
    */
   public Integer getId()
   {
      return this.id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId(final Integer id)
   {
      this.id = id;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return this.name;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /*
    * (non-Javadoc)
    *
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return "TestCase [id=" + this.id + ", name=" + this.name + ", externalId=" + this.externalId + ", projectPrefix="
                 + this.projectPrefix + ", version=" + this.version + ", externalName=" + this.getExternalName() + "]";
   }

   /**
    * @return the ExternalName
    */
   public String getExternalName()
   {
      return this.getProjectPrefix() + "-" + this.getExternalId();
   }

   /**
    * @return the projectPrefix
    */
   public String getProjectPrefix()
   {
      return this.projectPrefix;
   }

   /**
    * @return the testSuiteId
    */
   public Integer getExternalId()
   {
      return this.externalId;
   }

   /**
    * @param externalId
    *           the externalId to set
    */
   public void setExternalId(final Integer externalId)
   {
      this.externalId = externalId;
   }

   /**
    * @param projectPrefix
    *           the projectPrefix to set
    */
   public void setProjectPrefix(final String projectPrefix)
   {
      this.projectPrefix = projectPrefix;
   }

}
