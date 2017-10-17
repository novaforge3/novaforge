/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy;

/*
 * http://pierre-luc.paour.9online.fr/NaturalOrderComparator.java
 NaturalOrderComparator.java -- Perform 'natural order' comparisons of strings in Java.
 Copyright (C) 2003 by Pierre-Luc Paour <>

 Based on the C version by Martin Pool, of which this is more or less a straight conversion.
 Copyright (C) 2000 by Martin Pool <.au>

 This software is provided 'as-is', without any express or implied
 warranty.  In no event will the authors be held liable for any damages
 arising from the use of this software.

 Permission is granted to anyone to use this software for any purpose,
 including commercial applications, and to alter it and redistribute it
 freely, subject to the following restrictions:

 1. The origin of this software must not be misrepresented; you must not
 claim that you wrote the original software. If you use this software
 in a product, an acknowledgment in the product documentation would be
 appreciated but is not required.
 2. Altered source versions must be plainly marked as such, and must not be
 misrepresented as being the original software.
 3. This notice may not be removed or altered from any source distribution.
 */

import java.io.Serializable;
import java.util.Comparator;

/**
 * NaturalOrderComparator is used to compare two strings
 * 
 */
public class NaturalOrderComparator implements Comparator<Object>, Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 8464361289407836078L;

   private int compareRight(final String numberA, final String numberB)
   {
      int bias = 0;
      int indiceA = 0;
      int indiceB = 0;

      // The longest run of digits wins. That aside, the greatest
      // value wins, but we can't know that it will until we've scanned
      // both numbers to know that they have the same magnitude, so we
      // remember it in BIAS.
      for ( ;; indiceA++, indiceB++ )
      {
         char characterA = charAt(numberA, indiceA);
         char characterB = charAt(numberB, indiceB);

         if ( !Character.isDigit(characterA) && !Character.isDigit(characterB) )
         {
            return bias;
         } else if ( !Character.isDigit(characterA) )
         {
            return -1;
         } else if ( !Character.isDigit(characterB) )
         {
            return +1;
         } else if ( characterA < characterB )
         {
            if ( bias == 0 )
            {
               bias = -1;
            }
         } else if ( characterA > characterB )
         {
            if ( bias == 0 )
            {
               bias = +1;
            }
         } else if ( (characterA == 0) && (characterB == 0) )
         {
            return bias;
         }
      }
   }

   /**
    * Compare to object.
    * <p>
    * This methode return a number :
    * <ul>
    * <li>Negative if object1 is inferior to object2</li>
    * <li>0 if object1 is equal to object2</li>
    * <li>Positive if object1 is superior to object2</li>
    * </ul>
    * </p>
    * 
    * @param o1
    *           represents the first object to compare.
    * @param o2
    *           represents the second object to compare.
    */
   public int compare(final Object object1, final Object object2)
   {
      String stringA = object1.toString();
      String stringB = object2.toString();

      int indiceA = 0;
      int indiceB = 0;
      int nza = 0;
      int nzb = 0;
      char characterA;
      char characterB;
      int result;

      while ( true )
      {
         // only count the number of zeroes leading the last number compared
         nza = nzb = 0;

         characterA = charAt(stringA, indiceA);
         characterB = charAt(stringB, indiceB);

         // skip over leading spaces or zeros
         while ( Character.isSpaceChar(characterA) || (characterA == '0') )
         {
            if ( characterA == '0' )
            {
               nza++;
            } else
            {
               // only count consecutive zeroes
               nza = 0;
            }

            characterA = charAt(stringA, ++indiceA);
         }

         while ( Character.isSpaceChar(characterB) || (characterB == '0') )
         {
            if ( characterB == '0' )
            {
               nzb++;
            } else
            {
               // only count consecutive zeroes
               nzb = 0;
            }

            characterB = charAt(stringB, ++indiceB);
         }

         // process run of digits
         if ( Character.isDigit(characterA) && Character.isDigit(characterB) )
         {
            result = compareRight(stringA.substring(indiceA), stringB.substring(indiceB));
            if ( result != 0 )
            {
               return result;
            }
         }

         if ( (characterA == 0) && (characterB == 0) )
         {
            // The strings compare the same. Perhaps the caller
            // will want to call strcmp to break the tie.
            return nza - nzb;
         }

         if ( characterA < characterB )
         {
            return -1;
         } else if ( characterA > characterB )
         {
            return +1;
         }

         ++indiceA;
         ++indiceB;
      }
   }

   private static char charAt(final String pString, final int pInt)
   {
      if ( pInt >= pString.length() )
      {
         return 0;
      } else
      {
         return pString.charAt(pInt);
      }
   }
}
