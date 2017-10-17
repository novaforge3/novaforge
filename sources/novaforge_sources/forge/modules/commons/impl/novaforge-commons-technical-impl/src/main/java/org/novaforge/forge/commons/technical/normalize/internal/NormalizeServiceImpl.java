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
package org.novaforge.forge.commons.technical.normalize.internal;

import org.novaforge.forge.commons.technical.normalize.NormalizeService;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Implementation of {@link NormalizeService}
 * 
 * @author Guillaume Lamirand
 */
public class NormalizeServiceImpl implements NormalizeService

{
  private final static String REGEX = "[^\\w]";

  /**
   * {@inheritDoc}. Special characters are replaced by '' and
   * Normalizer.Form.NFD is used
   */
  @Override
  public String normalize(final String pSource)
  {
    return normalize(pSource, "", Normalizer.Form.NFD);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String normalize(final String pSource, final String pReplace)
  {
    return normalize(pSource, pReplace, Normalizer.Form.NFD);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String normalize(final String pSource, final String pReplace, final Form pForm)
  {
    final String normalized = Normalizer.normalize(pSource, pForm);

    return normalized.replaceAll(REGEX, pReplace).toLowerCase();
  }
}
