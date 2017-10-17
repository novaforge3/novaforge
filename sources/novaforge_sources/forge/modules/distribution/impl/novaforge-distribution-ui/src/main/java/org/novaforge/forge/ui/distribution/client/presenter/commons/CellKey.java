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
package org.novaforge.forge.ui.distribution.client.presenter.commons;

import com.google.gwt.view.client.ProvidesKey;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.distribution.ForgeRequestDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ForgeViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.OrganizationViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ProfilViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.UpdatedViewDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.SynchonizationResultDTO;
import org.novaforge.forge.ui.distribution.shared.diffusion.TargetForgeDTO;

/**
 * @author BILET-JC
 */
public class CellKey
{

   /**
    * The key provider that provides the unique ID of a forge affiliation.
    */
   public static final ProvidesKey<ForgeDTO>        FORGE_AFFILIATION_KEY_PROVIDER        = new ProvidesKey<ForgeDTO>()
   {
      @Override
      public Object getKey(
            ForgeDTO item)
      {
         return item == null ? null
               : item.getForgeId();
      }
   };

   /**
    * The key provider that provides the unique ID of a forge affiliation demand.
    */
   public static final ProvidesKey<ForgeRequestDTO> FORGE_AFFILIATION_DEMAND_KEY_PROVIDER = new ProvidesKey<ForgeRequestDTO>()
   {
      @Override
      public Object getKey(ForgeRequestDTO item)
      {
         return item == null ? null
               : item.getSourceForgeId();
      }
   };


   /**
    * The key provider that provides the unique ID of a forge view type table.
    */
   public static final ProvidesKey<ForgeViewDTO>        FORGE_VIEW_TYPE_KEY_PROVIDER        = new ProvidesKey<ForgeViewDTO>()
   {
      @Override
      public Object getKey(
            ForgeViewDTO item)
      {
         return item == null ? null
               : item.getForgeName();
      }
   };


   /**
    * The key provider that provides the unique ID of a organization view type table.
    */
   public static final ProvidesKey<OrganizationViewDTO>        ORGANIZATION_VIEW_TYPE_KEY_PROVIDER        = new ProvidesKey<OrganizationViewDTO>()
   {
      @Override
      public Object getKey(
            OrganizationViewDTO item)
      {
         return item == null ? null
               : item.getOrganizationName();
      }
   };


   /**
    * The key provider that provides the unique ID of a profil view type table.
    */
   public static final ProvidesKey<ProfilViewDTO>        PROFIL_VIEW_TYPE_KEY_PROVIDER        = new ProvidesKey<ProfilViewDTO>()
   {
      @Override
      public Object getKey(
            ProfilViewDTO item)
      {
         return item == null ? null
               : item.getForgeName()+item.getProjectName();
      }
   };
   
   

   /**
    * The key provider that provides the unique ID of a updated view type table.
    */
   public static final ProvidesKey<UpdatedViewDTO>        UPDATED_VIEW_TYPE_KEY_PROVIDER        = new ProvidesKey<UpdatedViewDTO>()
   {
      @Override
      public Object getKey(
    		  UpdatedViewDTO item)
      {
         return item == null ? null
               : item.getForgeName()+item.getLastUpdated(); 
      }
   };
   
   
   
   /**
    * /** The key provider that provides the unique ID of a target forge.
    */
    public static final ProvidesKey<TargetForgeDTO>          TARGET_FORGE_KEY_PROVIDER            = new ProvidesKey<TargetForgeDTO>()
    {
      @Override
      public Object getKey(
            TargetForgeDTO item)
      {
         return item == null ? null
               : item.getLabel();
      }
    };

    public static final ProvidesKey<SynchonizationResultDTO> SYNCHRO_RESULT_KEY_PROVIDER          = new ProvidesKey<SynchonizationResultDTO>()
          {
       @Override
       public Object getKey(
             SynchonizationResultDTO item)
       {
          return item == null ? null
                : item.getForgeIP();
       }
    };

    public static final ProvidesKey<SynchonizationResultDTO> SYNCHRO_RESULT_TEMPLATE_KEY_PROVIDER = new ProvidesKey<SynchonizationResultDTO>()
       {
    @Override
    public Object getKey(
          SynchonizationResultDTO item)
       {
          return item == null ? null
                : item.getForgeIP();
       }
    };

}
