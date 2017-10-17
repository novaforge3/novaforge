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
package org.novaforge.forge.ui.projects.internal.client.manage.view;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.novaforge.forge.ui.portal.client.util.DebugIdGenerator;
import org.novaforge.forge.ui.projects.internal.module.create.CreateProjectModule;

/**
 * Field factory used to generate project formular
 * 
 * @author Guillaume Lamirand
 */
public class ProjectFieldFactory extends DefaultFieldFactory
{

  /**
   * Define the field name for project's id
   */
  public static final String  ELEMENT_ID          = "elementId";
  /**
   * Define the field name for project's name
   */
  public static final String  NAME_FIELD          = "name";
  /**
   * Define the field name for project's description
   */
  public static final String  DESCRIPTION_FIELD   = "description";
  /**
   * Define the field name for project's license
   */
  public static final String  LICENSE_FIELD       = "licenceType";
  /**
   * Define the field name for project's organism
   */
  public static final String  ORGANISM_FIELD      = "organization";
  /**
   * Define the field name for project's template
   */
  public static final String  TEMPLATE_FIELD      = "template";
  /**
   * Define the field name for project's visibility
   */
  public static final String  PRIVATE_FIELD       = "privateVisibility";
  /**
   * Serialization id
   */
  private static final long   serialVersionUID    = -7448263324489951363L;
  /**
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION = "";
  /**
   * Default field width
   */
  private static final String COMMON_FIELD_WIDTH  = "12em";
  /**
   * Contains the project name
   */
  private TextField           name;
  /**
   * Contains the project id
   */
  private TextField           id;
  /**
   * Contains the description
   */
  private TextArea            description;
  private CheckBox            privat;

  /**
   * {@inheritDoc}
   */
  @Override
  public Field<?> createField(final Item pItem, final Object pPropertyId, final Component pUiContext)
  {
    Field<?> returnField = null;
    if (NAME_FIELD.equals(pPropertyId))
    {
      name = new TextField(pItem.getItemProperty(pPropertyId));
      name.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(), NAME_FIELD));
      name.setNullRepresentation(NULL_REPRESENTATION);
      name.setMaxLength(35);
      name.setRequired(true);
      name.setWidth(COMMON_FIELD_WIDTH);
      name.setNullSettingAllowed(true);
      name.setImmediate(true);
      returnField = name;
    }
    else if (ELEMENT_ID.equals(pPropertyId))
    {
      id = new TextField(pItem.getItemProperty(pPropertyId));
      id.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(), ELEMENT_ID));
      id.setMaxLength(26);
      id.setNullRepresentation(NULL_REPRESENTATION);
      id.setRequired(true);
      id.setWidth(COMMON_FIELD_WIDTH);
      id.setNullSettingAllowed(true);
      id.setImmediate(true);
      returnField = id;
    }
    else if (DESCRIPTION_FIELD.equals(pPropertyId))
    {
      description = new TextArea(pItem.getItemProperty(pPropertyId));
      description.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(),
          DESCRIPTION_FIELD));
      description.setMaxLength(250);
      description.setRequired(true);
      description.setNullRepresentation(NULL_REPRESENTATION);
      description.setNullSettingAllowed(true);
      description.setWidth(300, Unit.PIXELS);
      description.setImmediate(true);
      returnField = description;
    }
    else if (PRIVATE_FIELD.equals(pPropertyId))
    {
      privat = new CheckBox();
      privat.setId(DebugIdGenerator.getDynamicComponentId(CreateProjectModule.getPortalModuleId(),
          PRIVATE_FIELD));
      privat.setPropertyDataSource(pItem.getItemProperty(pPropertyId));
      returnField = privat;
    }
    return returnField;
  }

  /**
   * Return the {@link TextField} associated to the project's name
   * 
   * @return the text field for project's name
   */
  public TextField getName()
  {
    return name;
  }

  /**
   * Return the {@link TextField} associated to the project's id
   * 
   * @return the text field for project's id
   */
  public TextField getId()
  {
    return id;
  }

  /**
   * Return the {@link TextArea} associated to the project's description
   * 
   * @return the TextArea for project's description
   */
  public TextArea getDescription()
  {
    return description;
  }

  /**
   * @return the privat
   */
  public CheckBox getPrivate()
  {
    return privat;
  }

}