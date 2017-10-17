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
package org.novaforge.forge.ui.memberships.internal.client.groups.components;

import com.vaadin.data.Item;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * This {@link GroupFieldFactory} is used to build a group form
 * 
 * @author Guillaume Lamirand
 */
public class GroupFieldFactory extends DefaultFieldFactory
{
  /**
   * Define the field name for group name
   */
  public static final String  NAME_FIELD          = "name";              //$NON-NLS-1$
  /**
   * Define the field name for group description
   */
  public static final String  DESCRIPTION_FIELD   = "description";       //$NON-NLS-1$
  /**
   * Define the field name for group visibility
   */
  public static final String  VISIBILITY_FIELD    = "visible";           //$NON-NLS-1$
  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID    = 1526046573410387707L;
  /**
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION = "";                  //$NON-NLS-1$
  private TextField           name;
  private TextArea            description;
  private CheckBox            visibility;

  @Override
  public Field createField(final Item item, final Object propertyId, final Component uiContext)
  {
    Field returnField = null;
    if (NAME_FIELD.equals(propertyId))
    {
      name = new TextField();
      name.setRequired(true);
      name.setNullRepresentation(NULL_REPRESENTATION);
      name.setNullSettingAllowed(false);
      name.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = name;
    }
    else if (DESCRIPTION_FIELD.equals(propertyId))
    {
      description = new TextArea();
      description.setRequired(true);
      description.setNullSettingAllowed(false);
      description.setNullRepresentation(NULL_REPRESENTATION);
      description.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = description;
    }
    else if (VISIBILITY_FIELD.equals(propertyId))
    {
      visibility = new CheckBox();
      returnField = visibility;
    }
    return returnField;
  }

  /**
   * Return {@link Group} name
   * 
   * @return {@link Group} name field
   */
  public TextField getName()
  {
    return name;
  }

  /**
   * Return {@link Group} description
   * 
   * @return {@link Group} description field
   */
  public TextArea getDescription()
  {
    return description;
  }

  /**
   * Return {@link Group} visibility
   * 
   * @return {@link Group} visibility field
   */
  public CheckBox getVisibility()
  {
    return visibility;
  }

}
