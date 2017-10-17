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
package org.novaforge.forge.core.plugins.categories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This enum describes the cateogry id for each category available.
 * 
 * @author lamirang
 */
public enum Category
{
  BUGTRACKER
  {
    @Override
    public String getId()
    {
      return "bugtracker";
    }
  },
  MAILINGLIST
  {
    @Override
    public String getId()
    {
      return "mailinglist";
    }
  },
  FORUM
  {
    @Override
    public String getId()
    {
      return "forum";
    }
  },
  QUALITY
  {
    @Override
    public String getId()
    {
      return "quality";
    }
  },
  ECM
  {
    @Override
    public String getId()
    {
      return "ecm";
    }
  },
  DELIVERY
  {
    @Override
    public String getId()
    {
      return "delivery";
    }
  },
  CI
  {
    @Override
    public String getId()
    {
      return "ci";
    }
  },
  CMS
  {
    @Override
    public String getId()
    {
      return "cms";
    }
  },
  REPOSITORYMANAGEMENT
  {
    @Override
    public String getId()
    {
      return "repositorymanagement";
    }
  },
  REQUIREMENTMANAGEMENT

  {
    @Override
    public String getId()
    {
      return "requirementmanagement";
    }
  },
  SCM
  {
    @Override
    public String getId()
    {
      return "scm";
    }
  },
  SURVEY
  {
    @Override
    public String getId()
    {
      return "survey";
    }
  },
  TESTMANAGEMENT
  {
    @Override
    public String getId()
    {
      return "testmanagement";
    }
  },
  WIKI
  {
    @Override
    public String getId()
    {
      return "wiki";
    }
  },
  MANAGEMENT
  {
    @Override
    public String getId()
    {
      return "management";
    }
  },
  ARD
  {
    @Override
    public String getId()
    {
      return "ard";
    }
  },
  DEVOPSMANAGEMENT
  {
    @Override
    public String getId()
    {
      return "devopsmanagement";
    }
  };

  public static Category getById(final String pId)
  {
    Category returnType = null;
    final Category[] values = Category.values();
    for (final Category type : values)
    {
      if (type.getId().equals(pId))
      {
        returnType = type;
        break;
      }
    }
    return returnType;
  }

  public abstract String getId();

  public static List<Category> list()
  {
    final List<Category> typeList = new ArrayList<Category>();
    final Category[] values = Category.values();
    Collections.addAll(typeList, values);
    return typeList;
  }

  @Override
  public String toString()
  {
    return getId();

  }
}
