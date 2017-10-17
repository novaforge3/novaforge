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
package org.novaforge.forge.ui.mailing.internal.client.mailing.components;

import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * This {@link MailingListFormFieldFactory} is used to build a mailinglist form
 * 
 * @author sbenoist
 */
public class MailingListFormFieldFactory extends DefaultFieldFactory
{

  /**
   * Define the field name for mailinglist name
   */
  public static final String  NAME_FIELD          = "name";               //$NON-NLS-1$
  /**
   * Define the field name for mailinglist description
   */
  public static final String  DESCRIPTION_FIELD   = "description";
  /**
   * Define the field name for mailinglist subject
   */
  public static final String  SUBJECT_FIELD       = "subject";            //$NON-NLS-1$
  /**
   * Define the field name for mailinglist type
   */
  public static final  String TYPE_FIELD          = "type";               //$NON-NLS-1$
  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID    = -6333146596523819345L;
  /**
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION = "";                   //$NON-NLS-1$
  private TextField name;
  private TextField subject;
  private TextArea  description;
  private ComboBox  type;

  /**
   * {@inheritDoc}
   */
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
    else if (SUBJECT_FIELD.equals(propertyId))
    {
      subject = new TextField();
      subject.setRequired(true);
      subject.setNullRepresentation(NULL_REPRESENTATION);
      subject.setNullSettingAllowed(false);
      subject.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = subject;
    }
    else if (TYPE_FIELD.equals(propertyId))
    {
      type = new ComboBox();
      type.setRequired(true);
      type.setNullSelectionAllowed(false);
      type.setTextInputAllowed(false);
      type.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = type;
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
   * @return the subject
   */
  public TextField getSubject()
  {
    return subject;
  }

  /**
   * @return the description
   */
  public TextArea getDescription()
  {
    return description;
  }

  /**
   * @return the type
   */
  public ComboBox getType()
  {
    return type;
  }

}
