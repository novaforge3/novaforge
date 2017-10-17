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
package org.novaforge.forge.core.plugins.categories.quality;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sbenoist
 */
public enum QualityMetric
{
  VIOLATIONS
  {
    @Override
    public String getLabel()
    {
      return "violations";
    }
  },
  BLOCKER_VIOLATIONS
  {
    @Override
    public String getLabel()
    {
      return "blocker_violations";
    }
  },
  CRITICAL_VIOLATIONS
  {
    @Override
    public String getLabel()
    {
      return "critical_violations";
    }
  },
  MAJOR_VIOLATIONS
  {
    @Override
    public String getLabel()
    {
      return "major_violations";
    }
  },
  MINOR_VIOLATIONS
  {
    @Override
    public String getLabel()
    {
      return "minor_violations";
    }
  },
  WEIGHTED_VIOLATIONS
  {
    @Override
    public String getLabel()
    {
      return "weighted_violations";
    }
  },
  TESTS
  {

    @Override
    public String getLabel()
    {
      return "tests";
    }

  },
  TEST_ERRORS
  {

    @Override
    public String getLabel()
    {
      return "test_errors";
    }
  },

  SKIPPED_TESTS
  {

    @Override
    public String getLabel()
    {
      return "skipped_tests";
    }
  },

  TEST_FAILURES
  {

    @Override
    public String getLabel()
    {
      return "test_failures";
    }
  },
  TEST_EXECUTION_TIME
  {

    @Override
    public String getLabel()
    {
      return "test_execution_time";
    }
  },
  TEST_SUCCESS_DENSITY
  {

    @Override
    public String getLabel()
    {
      return "test_success_density";
    }
  },
  COVERAGE
  {

    @Override
    public String getLabel()
    {
      return "coverage";
    }
  },
  LINE_COVERAGE
  {

    @Override
    public String getLabel()
    {
      return "line_coverage";
    }
  },
  BRANCH_COVERAGE
  {

    @Override
    public String getLabel()
    {
      return "branch_coverage";
    }
  };

  /**
   * Contains map which link a label to a specific role element
   */
  private static final Map<String, QualityMetric> metrics = new HashMap<String, QualityMetric>();

  static
  {
    for (final QualityMetric metric : values())
    {
      metrics.put(metric.getLabel(), metric);
    }
  }

  /**
   * Return element from enumeration regarding a specific label.
   *
   * @param pLabel
   *          represents the label used to search a element in the enumeration
   * @return specific element
   */
  public static QualityMetric fromLabel(final String pLabel)
  {
    return metrics.get(pLabel);
  }

  public abstract String getLabel();

}
