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

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * @author Jeremy Casery
 */
public class RepositoryFieldFactory extends DefaultFieldFactory
{

  /**
   * Define the field name for repository's uri
   */
  public static final String   URI_FIELD           = "URI";
  /**
   * Define the field name for repository's description
   */
  public static final String   DESCRIPTION_FIELD   = "description";
  /**
   * Serial UID.
   */
  private static final long   serialVersionUID    = -581056077582007605L;
  /**
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION = "";
  private final RepositoryType repositoryType;
  private       Field<?>       uri;
  private       TextArea       description;

  public RepositoryFieldFactory(final RepositoryType pType)
  {
    repositoryType = pType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Field<?> createField(final Item pItem, final Object pPropertyId, final Component pUiContext)
  {
    final Form form        = (Form) pUiContext;
    Field<?>   returnField = null;
    if (URI_FIELD.equals(pPropertyId))
    {
      if (RepositoryType.OBEO.equals(repositoryType))
      {
        uri = new TextField(pItem.getItemProperty(pPropertyId));
        ((TextField) uri).setValidationVisible(false);
        ((TextField) uri).setNullRepresentation(NULL_REPRESENTATION);
      }
      else if (RepositoryType.EXCEL.equals(repositoryType))
      {
        uri = new UploadFieldExcelRepository();
        ((UploadFieldExcelRepository) uri).setValidationVisible(false);
      }
      uri.setRequired(true);
      uri.setWidth(NovaForge.FORM_FIELD_URL_WIDTH);
      form.addField(URI_FIELD, uri);
    }
    else if (DESCRIPTION_FIELD.equals(pPropertyId))
    {
      description = new TextArea(pItem.getItemProperty(pPropertyId));
      description.setRequired(false);
      description.setNullSettingAllowed(true);
      description.setWidth(NovaForge.FORM_FIELD_URL_WIDTH);
      description.setImmediate(true);
      description.setNullRepresentation(NULL_REPRESENTATION);
      returnField = description;
    }
    return returnField;

  }

  public Field<?> getUri()
  {
    return uri;
  }

  public TextArea getDescription()
  {
    return description;
  }

  public RepositoryType getRepositoryType()
  {
    return repositoryType;
  }

}
