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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.novaforge.forge.ui.portal.data.util.converter.StringToURLConverter;

/**
 * @author Jeremy Casery
 */
public class InstanceFieldFactory extends DefaultFieldFactory
{

  /**
   * Define the field name for instance's name
   */
  public static final String  NAME_FIELD         = "name";
  /**
   * Define the field name for instance's description
   */
  public static final String  DESCRIPTION_FIELD  = "description";
  /**
   * Define the field name for instance's alias
   */
  public static final String  ALIAS_FIELD        = "alias";
  /**
   * Define the field name for instance's base URL
   */
  public static final String  BASEURL_FIELD      = "baseURL";
  /**
   * Define the field name for instance's status
   */
  public static final String  SHAREABLE_FIELD    = "shareable";
  /**
   * SerialUID
   */
  private static final long   serialVersionUID   = 4483590908435084407L;
  /**
   * Define the common field with
   */
  private static final String COMMON_FIELD_WIDTH = "180px";
  /**
   * Define the common null/empty field value
   */
  private static final String COMMON_FIELD_EMPTY = "";

  private TextField name;

  private TextArea desc;

  private TextField alias;

  private TextField baseurl;

  private CheckBox shareable;

  /**
   * @return the nameField
   */
  public static String getNameField()
  {
    return NAME_FIELD;
  }

  /**
   * @return the descriptionField
   */
  public static String getDescriptionField()
  {
    return DESCRIPTION_FIELD;
  }

  /**
   * @return the aliasField
   */
  public static String getAliasField()
  {
    return ALIAS_FIELD;
  }

  /**
   * @return the baseurlField
   */
  public static String getBaseurlField()
  {
    return BASEURL_FIELD;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Field<?> createField(final Item item, final Object propertyId, final Component uiContext)
  {
    Field<?> returnField = null;
    if (NAME_FIELD.equals(propertyId))
    {
      name = new TextField();
      name.setRequired(true);
      name.setNullSettingAllowed(true);
      name.setNullRepresentation(COMMON_FIELD_EMPTY);
      name.setWidth(COMMON_FIELD_WIDTH);
      returnField = name;
    }
    else if (DESCRIPTION_FIELD.equals(propertyId))
    {
      desc = new TextArea();
      desc.setRequired(false);
      desc.setNullSettingAllowed(true);
      desc.setNullRepresentation(COMMON_FIELD_EMPTY);
      desc.setWidth(COMMON_FIELD_WIDTH);
      returnField = desc;
    }
    else if (ALIAS_FIELD.equals(propertyId))
    {
      alias = new TextField();
      alias.setRequired(true);
      alias.setNullSettingAllowed(true);
      alias.setNullRepresentation(COMMON_FIELD_EMPTY);
      alias.setWidth(COMMON_FIELD_WIDTH);
      returnField = alias;
    }
    else if (BASEURL_FIELD.equals(propertyId))
    {
      baseurl = new TextField();
      baseurl.setRequired(true);
      baseurl.setNullSettingAllowed(true);
      baseurl.setNullRepresentation(COMMON_FIELD_EMPTY);
      baseurl.setWidth(220, Unit.PIXELS);
      baseurl.setConverter(new StringToURLConverter());
      returnField = baseurl;
    }
    else if (SHAREABLE_FIELD.equals(propertyId))
    {
      shareable = new CheckBox();
      shareable.setPropertyDataSource(item.getItemProperty(propertyId));
      returnField = shareable;
    }
    return returnField;

  }

  /**
   * @return the name
   */
  public TextField getName()
  {
    return name;
  }

  /**
   * @return the desc
   */
  public TextArea getDesc()
  {
    return desc;
  }

  /**
   * @return the alias
   */
  public TextField getAlias()
  {
    return alias;
  }

  /**
   * @return the baseurl
   */
  public TextField getBaseurl()
  {
    return baseurl;
  }

  /**
   * @return the shareable
   */
  public CheckBox getShareable()
  {
    return shareable;
  }

}
