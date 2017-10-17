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
package org.novaforge.forge.ui.requirements.internal.client.repository.components;

import com.vaadin.data.validator.AbstractValidator;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.File;

/**
 * Excel validator for upload Excel fields for requirements plugin.
 * 
 * @author B-Martinelli
 */
public class RequirementsExcelFileContentValidator extends AbstractValidator<File>
{
  /** Serial uid */
  private static final long serialVersionUID = 647360964295868842L;

  /**
   * Constructeur
   * 
   * @param pErrorMessagecodeForContent
   *          Error message if file content is invalid
   */
  public RequirementsExcelFileContentValidator(final String pErrorMessage)
  {
    super(pErrorMessage);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValidValue(final File value)
  {
    boolean result = true;
    if (value != null)
    {
      // Creating a minimalist repo with uri and type
      final IRepository repo = RequirementsModule.getRequirementFactory().buildNewRepository();
      repo.setURI(value.getPath());
      repo.setType(RepositoryType.EXCEL.getERepositoryType());

      for (final ExternalRepositoryRequirementConnector validator : RequirementsModule
          .getExternalRepositoryRequirementConnectors())
      {
        result = result && validator.validate(repo);
      }
    }
    else
    {
      result = false;
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<File> getType()
  {
    return File.class;
  }
}
