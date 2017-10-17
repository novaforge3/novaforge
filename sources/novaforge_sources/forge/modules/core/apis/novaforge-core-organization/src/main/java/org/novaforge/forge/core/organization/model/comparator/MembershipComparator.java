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
package org.novaforge.forge.core.organization.model.comparator;

import org.novaforge.forge.core.organization.model.Membership;

import java.util.Comparator;

/**
 * @author sbenoist
 */
public class MembershipComparator implements Comparator<Membership>
{

  /**
   * This comparator allow to sort memberships from the weaker to the stronger. Membership1 > Membership2
   * means that tha membership1 is prior to the membership2. It allows to sort a collection from the weaker
   * to the stronger membership
   * 
   * @inheritDoc
   */
  @Override
  public int compare(final Membership pMembership1, final Membership pMembership2)
  {
    int ret = 0;
    if (pMembership1.getPriority() != pMembership2.getPriority())
    {
      if (pMembership1.getPriority())
      {
        ret = 1;
      }
      else
      {
        ret = -1;
      }
    }
    else
    {
      ret = pMembership1.getRole().compareTo(pMembership2.getRole());
    }

    return ret;
  }

}
