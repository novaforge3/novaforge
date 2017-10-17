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
package org.novaforge.forge.remote.services.model.management;

/**
 * The enum which presents the different status possible of a task
 */
public enum TaskStatus {

   NOT_AFFECTED,
   NOT_STARTED,
   IN_PROGRESS,
   DONE,
 CANCELLED;

   public static TaskStatus fromStatus(String status) {
      if (status == null) {
         return null;
      }
      else if (status.equalsIgnoreCase("Not started")) { //$NON-NLS-1$
         return NOT_STARTED;
      }
      else if (status.equalsIgnoreCase("Not affected")) { //$NON-NLS-1$
         return NOT_AFFECTED;
      }
      else if (status.equalsIgnoreCase("In progress")) { //$NON-NLS-1$
         return IN_PROGRESS;
      }
      else if (status.equalsIgnoreCase("Done")) { //$NON-NLS-1$
         return DONE;
      }
      else if (status.equalsIgnoreCase("Cancelled")) { //$NON-NLS-1$
         return CANCELLED;
      }
      return null;
   }

   public String toStatusString() {
      switch (this) {
      case NOT_STARTED:
         return "Not started"; //$NON-NLS-1$
      case NOT_AFFECTED:
         return "Not affected"; //$NON-NLS-1$
      case IN_PROGRESS:
         return "In progress"; //$NON-NLS-1$
      case DONE:
         return "Done"; //$NON-NLS-1$
      case CANCELLED:
         return "Cancelled";
      default:
         return ""; //$NON-NLS-1$
      }
   }

   @Override
   public String toString() {
      switch (this) {
      case NOT_STARTED:
         return "Not started"; //$NON-NLS-1$
      case NOT_AFFECTED:
         return "Not affected"; //$NON-NLS-1$
      case IN_PROGRESS:
         return "In progress"; //$NON-NLS-1$
      case DONE:
         return "Done"; //$NON-NLS-1$
      case CANCELLED:
         return "Cancelled";
      default:
         return ""; //$NON-NLS-1$
      }
   }
}
