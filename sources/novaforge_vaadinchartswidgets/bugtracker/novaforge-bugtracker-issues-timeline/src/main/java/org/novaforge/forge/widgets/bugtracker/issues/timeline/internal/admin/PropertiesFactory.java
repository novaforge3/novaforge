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
package org.novaforge.forge.widgets.bugtracker.issues.timeline.internal.admin;

import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

import org.novaforge.forge.ui.portal.client.component.DateRangeComponent.RangeType;

import com.google.common.base.Strings;

/**
 * @author Gauthier Cart
 */
public class PropertiesFactory
{
  /**
   * Key value for properties
   */
  private static final String VERSION    = "VERSION";
  private static final String RANGE_TYPE = "RANGE_TYPE";
  private static final String START_DATE = "START_DATE";
  private static final String END_DATE   = "END_DATE";

  /**
   * Read properties string and return concerned version.( any (<code>null</code>) by default)
   * 
   * @param pProperties
   *          the string properties
   * @return concerned version or any (<code>null</code>) by default
   */
  public static String readVersionProperties(final String pProperties)
  {
    String version = null;
    if (Strings.isNullOrEmpty(pProperties) == false)
    {
      final JSONObject json = JSONObject.fromObject(pProperties);
      version = json.optString(VERSION, null);
    }
    return version;
  }

  /**
   * Read properties string and return concerned version.( any (<code>null</code>) by default)
   * 
   * @param pProperties
   *          the string properties
   * @return concerned version or any (<code>null</code>) by default
   */
  public static RangeType readRangeTypeProperties(final String pProperties)
  {
    RangeType rangeType = RangeType.LAST_WEEK;
    try
    {
      if (Strings.isNullOrEmpty(pProperties) == false)
      {
        final JSONObject json = JSONObject.fromObject(pProperties);
        rangeType = RangeType.valueOf(json.optString(RANGE_TYPE, RangeType.LAST_WEEK.toString()));
      }
    }
    catch (final Exception e)
    {
      // nothing to do (and return custom)
    }
    return rangeType;
  }

  /**
   * Read properties string and return start date.(<code>null</code> by default)
   * 
   * @param pProperties
   *          the string properties
   * @return start date or <code>null</code> by default
   */
  public static Date readStartDateProperties(final String pProperties)
  {
    final RangeType rangeType = readRangeTypeProperties(pProperties);
    switch (rangeType)
    {
      case CUSTOM:
        return readDateProperties(pProperties, START_DATE);
      default:
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(rangeType.getCalendarField(), -1);
        return cal.getTime();
    }
  }

  /**
   * Read properties string and return end date.(<code>null</code> by default)
   * 
   * @param pProperties
   *          the string properties
   * @return end date or <code>null</code> by default
   */
  public static Date readEndDateProperties(final String pProperties)
  {
    final RangeType rangeType = readRangeTypeProperties(pProperties);
    switch (rangeType)
    {
      case CUSTOM:
        return readDateProperties(pProperties, END_DATE);
      default:
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
  }

  private static Date readDateProperties(final String pProperties, final String pKey)
  {
    Date date = null;
    if (Strings.isNullOrEmpty(pProperties) == false)
    {
      final JSONObject json = JSONObject.fromObject(pProperties);
      final long timestamp = json.optLong(pKey, -1);
      if (timestamp == -1)
      {
        date = null;
      }
      else
      {
        date = new Date(timestamp);
      }
    }
    return date;
  }

  /**
   * Build properties string from argument
   * 
   * @param pVersions
   *          the list of versions defined
   * @return properties string
   */
  public static String buildProperties(final String pVersion, final RangeType pRangeType,
      final Date pStartDate, final Date pEndDate)
  {
    final JSONObject json = new JSONObject();
    json.element(VERSION, pVersion);
    json.element(RANGE_TYPE, pRangeType.name());
    if (pStartDate != null)
    {
      json.element(START_DATE, pStartDate.getTime());
    }
    if (pEndDate != null)
    {
      json.element(END_DATE, pEndDate.getTime());
    }
    return json.toString();

  }

}
