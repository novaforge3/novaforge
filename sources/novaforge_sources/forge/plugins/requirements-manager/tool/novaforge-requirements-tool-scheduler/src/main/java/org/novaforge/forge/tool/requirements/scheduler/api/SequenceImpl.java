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
package org.novaforge.forge.tool.requirements.scheduler.api;

import org.novaforge.forge.tools.requirements.common.model.scheduling.Activity;
import org.novaforge.forge.tools.requirements.common.model.scheduling.Sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Guillaume Morin
 */
public class SequenceImpl implements Sequence
{

   private final List<Activity> fActivities = new ArrayList<Activity>();
   private final String fIdSequence;

   public SequenceImpl(final String pIdSequence, final List<Activity> pActivities)
   {
      fActivities.addAll(pActivities);
      fIdSequence = pIdSequence;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean addActivity(final Activity pActivity)
   {
      return fActivities.add(pActivity);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getSequenceID()
   {
      return fIdSequence;
   }

   @Override
   public Iterator<Activity> iterator()
   {
      return fActivities.iterator();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((fIdSequence == null) ? 0 : fIdSequence.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      SequenceImpl other = (SequenceImpl) obj;
      if (fIdSequence == null)
      {
         if (other.fIdSequence != null)
         {
            return false;
         }
      }
      else if (!fIdSequence.equals(other.fIdSequence))
      {
         return false;
      }
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      String returned = "[SEQ|" + fIdSequence + "|";
      for (Activity activity : fActivities)
      {
         returned = returned + " " + activity;
      }
      return returned + "]";
   }
}
