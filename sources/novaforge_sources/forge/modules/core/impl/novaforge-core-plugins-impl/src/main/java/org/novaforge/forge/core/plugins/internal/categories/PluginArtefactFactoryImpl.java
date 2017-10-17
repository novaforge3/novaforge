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
package org.novaforge.forge.core.plugins.internal.categories;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.plugins.categories.Association;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.Parameter;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;
import org.novaforge.forge.core.plugins.exceptions.PluginArtefactFactoryException;
import org.novaforge.forge.core.plugins.services.PluginArtefactFactory;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume Lamirand
 */
public class PluginArtefactFactoryImpl implements PluginArtefactFactory
{
  private static final String    GETTER      = "get";
  private static final String    SETTER      = "set";
  private static final String    ATTACHMENTS = "attachments";
  /**
   * {@link PluginsCategoryManager} injected by container
   */
  private PluginsCategoryManager pluginsCategoryManager;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object buildTargetArtefact(final String pSourceCategory, final String pSourceNotification,
                                    final Object pSource, final String pTargetCategory, final String pTargetAction,
                                    final String pTemplate) throws PluginArtefactFactoryException
  {
    // Get source category information
    final CategoryDefinitionService sourceService = pluginsCategoryManager.getCategoryService(pSourceCategory);
    if (sourceService == null)
    {
      throw new IllegalArgumentException(String
                                             .format("Unable to find category service definition with [category_name=%s]",
                                                     pSourceCategory));
    }
    final Association sourceAssociation = sourceService.getAssociation(AssociationType.NOTIFICATION,
                                                                       pSourceNotification);
    final Parameter sourceParamater = sourceService.getParamater(sourceAssociation, Locale.ENGLISH);
    final Class<?>  sourceClass     = sourceParamater.getParameterClass();

    // Get target category information
    final CategoryDefinitionService targetService = pluginsCategoryManager.getCategoryService(pTargetCategory);
    if (targetService == null)
    {
      throw new IllegalArgumentException(String
                                             .format("Unable to find category service definition with [category_name=%s]",
                                                     pTargetCategory));
    }
    final Association targetAssociation = targetService.getAssociation(AssociationType.ACTION, pTargetAction);
    final Parameter   targetParamater   = targetService.getParamater(targetAssociation, Locale.ENGLISH);
    final Class<?>    targetClass       = targetParamater.getParameterClass();
    // Build target category artefact
    final Object targetObject = newInstance(targetClass);

    try
    {
      // Building JSON object from template
      final JSONObject json = JSONObject.fromObject(pTemplate);
      for (@SuppressWarnings("unchecked") final Iterator<Object> keys = json.keys(); keys.hasNext(); )
      {
        // Getting target field name
        final String targetField = (String) keys.next();
        // Getting target field content from template
        final String targetContent = json.getString(targetField);
        if ((isAttachmentsField(targetField)) && (Boolean.parseBoolean(targetContent)))
        {
          @SuppressWarnings("unchecked")
          final List<Attachment> attachmentsValue = (List<Attachment>) this.getFieldValue(pSource, sourceClass,
                                                                                          ATTACHMENTS);
          // Set the field value to target object
          this.setFieldValue(targetObject, targetClass, targetField, attachmentsValue);
        }
        else if (!isAttachmentsField(targetField))
        {
          // Resolved the content value
          final String targetValue = getContent(targetContent, pSource, sourceClass);
          // Set the field value to target object
          this.setFieldValue(targetObject, targetClass, targetField, targetValue);

        }

      }
    }
    catch (final JSONException e)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to build JSON Object from template parameter with [template=%s]", pTemplate), e);
    }

    return targetObject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String buildArtefactTemplate(final Map<String, String> pTemplate)
  {
    // Building JSON object from template
    final JSONObject json = JSONObject.fromObject(pTemplate);
    return json.toString();
  }

  private boolean isAttachmentsField(final String pFieldName)
  {
    return ATTACHMENTS.equals(pFieldName);
  }

  private Object newInstance(final Class<?> clazz) throws PluginArtefactFactoryException
  {
    try
    {
      final Constructor<?> constructor = clazz.getConstructor((Class<?>[]) null);
      return constructor.newInstance((Object[]) null);
    }
    catch (final SecurityException e)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to find a empty constructor with [class=%s]", clazz), e);
    }
    catch (final NoSuchMethodException e)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to find a empty constructor with [class=%s]", clazz), e);
    }
    catch (final Exception e)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to build target object from empty constructor with [class=%s]", clazz), e);
    }
  }

  /**
   * Get resolved field content from template content
   * 
   * @param pFieldContent
   * @param instance
   * @param clazz
   * @return content value
   * @throws PluginArtefactFactoryException
   */
  private String getContent(final String pFieldContent, final Object instance, final Class<?> clazz)
      throws PluginArtefactFactoryException
  {
    String content = pFieldContent;
    final Pattern sourcePatern = Pattern.compile("\\@[\\w]*\\.[\\w]*");
    final Matcher sourceMatcher = sourcePatern.matcher(content);
    if (sourceMatcher.find())
    {
      final String groupToken = sourceMatcher.group();
      final String fieldName = getFieldName(groupToken);
      String fieldValue = " ";
      final Object value = this.getFieldValue(instance, clazz, fieldName);
      if (value != null)
      {
        fieldValue = value.toString();
      }
      content = content.replaceAll(Pattern.quote(groupToken), fieldValue);
      content = getContent(content, instance, clazz);
    }
    return content;
  }

  /**
   * Get from variable the name of artefact's field name
   * 
   * @param pString
   * @return field name
   */

  private String getFieldName(final String pGroup) throws PluginArtefactFactoryException
  {
    String returnValue = "";
    final StringTokenizer name = new StringTokenizer(pGroup, ".");
    if (name.countTokens() != 2)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to get field name from original content with [group_matcher=%s]", pGroup));
    }
    else
    {
      while (name.hasMoreTokens())
      {
        returnValue = name.nextToken();
      }
    }
    return returnValue;
  }

  /**
   * Get field value from a specific object, a class and field name This method will try to get the value of
   * a field for a object. It will use first the getter method, if it cannot be found the field will be used.
   * 
   * @param instance
   * @param clazz
   * @param fieldName
   * @return field name
   * @throws PluginArtefactFactoryException
   */
  private <T> Object getFieldValue(final Object instance, final Class<T> clazz, final String fieldName)
      throws PluginArtefactFactoryException
  {
    Object returnValue = null;
    try
    {
      final String getterName = buildGetterMethodName(fieldName);
      final Method method = clazz.getDeclaredMethod(getterName);
      returnValue = method.invoke(instance);
    }
    catch (final NoSuchMethodException e)
    {
      try
      {
        returnValue = this.getFieldValueFromField(instance, clazz, fieldName);
      }
      catch (final IllegalAccessException e1)
      {
        throw new PluginArtefactFactoryException(String.format(
            "Unable to get field value with [class=%s, field_name=%s]", clazz, fieldName), e);
      }
    }
    catch (final Exception e)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to get field value with [class=%s, field_name=%s]", clazz, fieldName), e);
    }
    return returnValue;
  }

  /**
   * @param instance
   * @param clazz
   * @param fieldName
   * @param returnValue
   * @return
   * @throws IllegalAccessException
   */
  private <T> Object getFieldValueFromField(final Object instance, final Class<T> clazz,
      final String fieldName) throws IllegalAccessException
  {
    Object returnValue;
    try
    {
      final Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      returnValue = field.get(instance);
    }
    catch (final NoSuchFieldException e)
    {
      returnValue = fieldName;
    }
    return returnValue;
  }

  private String buildGetterMethodName(final String pFieldName)
  {
    final String capitalized = StringUtils.capitalize(pFieldName);
    return GETTER + capitalized;
  }

  /**
   * This method will try to set the value to a field for a object. It will use first the setter method, if
   * an error occured the field will be used.
   * 
   * @param instance
   * @param clazz
   * @param fieldName
   * @param pValue
   * @throws PluginArtefactFactoryException
   */
  private <T> void setFieldValue(final Object instance, final Class<T> clazz, final String fieldName,
      final Object pValue) throws PluginArtefactFactoryException
  {
    try
    {
      final Method method = clazz.getDeclaredMethod(buildSetterMethodName(fieldName), java.lang.String.class);
      method.invoke(instance, pValue);
    }
    catch (final NoSuchMethodException e)
    {
      try
      {
        this.setFieldValueFromField(instance, clazz, fieldName, pValue);
      }
      catch (final Exception e1)
      {
        throw new PluginArtefactFactoryException(String.format(
            "Unable to set field value with [class=%s, field_name=%s, value=%s]", clazz, fieldName,
            pValue.toString()), e1);
      }
    }
    catch (final Exception e)
    {
      throw new PluginArtefactFactoryException(String.format(
          "Unable to set field value with [class=%s, field_name=%s, value=%s]", clazz, fieldName,
          pValue.toString()), e);
    }
  }

  private String buildSetterMethodName(final String pFieldName)
  {
    final String capitalized = StringUtils.capitalize(pFieldName);
    return SETTER + capitalized;
  }

  private <T> void setFieldValueFromField(final Object instance, final Class<T> clazz,
      final String fieldName, final Object pValue) throws IllegalAccessException, SecurityException,
      NoSuchFieldException
  {

    final Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, pValue);

  }

  /**
   * Use by container to inject {@link PluginsCategoryManager}
   * 
   * @param pluginsCategoryManager
   *          the pluginsCategoryManager to set
   */
  public void setPluginsCategoryManager(final PluginsCategoryManager pluginsCategoryManager)
  {
    this.pluginsCategoryManager = pluginsCategoryManager;
  }
}
