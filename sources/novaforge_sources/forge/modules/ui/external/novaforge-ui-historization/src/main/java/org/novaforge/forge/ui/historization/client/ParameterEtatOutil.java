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
package org.novaforge.forge.ui.historization.client;

import java.util.HashMap;
import java.util.Map;

public enum ParameterEtatOutil
{
  TOUS
  {
    @Override
    public String getLabel()
    {
      return "Tous";
    }

    @Override
    public int getIndex()
    {
      return -1;
    }
  },
  INSTALLE
  {
    @Override
    public String getLabel()
    {
      return "Install\u00E9";
    }

    @Override
    public int getIndex()
    {
      return 0;
    }
  },
  ACTIVE
  {
    @Override
    public String getLabel()
    {
      return "Activ\u00E9";
    }

    @Override
    public int getIndex()
    {
      return 1;
    }
  },
  DEPRECIE
  {
    @Override
    public String getLabel()
    {
      return "D\u00E9pr\u00E9ci\u00E9";
    }

    @Override
    public int getIndex()
    {
      return 2;
    }
  },
  INACCESSIBLE
  {
    @Override
    public String getLabel()
    {
      return "Inaccessible";
    }

    @Override
    public int getIndex()
    {
      return 3;
    }
  },
  ARRETE
  {
    @Override
    public String getLabel()
    {
      return "Arr\u00EAt\u00E9";
    }

    @Override
    public int getIndex()
    {
      return 4;
    }
  },
  DESINSTALLE
  {
    @Override
    public String getLabel()
    {
      return "D\u00E9sinstall\u00E9";
    }

    @Override
    public int getIndex()
    {
      return 5;
    }
  };

  private static final Map<String, ParameterEtatOutil> etatsOutil = new HashMap<String, ParameterEtatOutil>();

  static
  {
    for (final ParameterEtatOutil etatOutil : values())
    {
      etatsOutil.put(etatOutil.getLabel(), etatOutil);
    }
  }

  public static ParameterEtatOutil fromLabel(final String label)
  {
    return etatsOutil.get(label);
  }

  public abstract String getLabel();

  public abstract int getIndex();
}
