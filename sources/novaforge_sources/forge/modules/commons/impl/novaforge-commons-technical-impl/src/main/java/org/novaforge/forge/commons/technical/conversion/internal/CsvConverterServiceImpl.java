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
package org.novaforge.forge.commons.technical.conversion.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.conversion.CsvConversionException;
import org.novaforge.forge.commons.technical.conversion.CsvConverterService;
import org.novaforge.forge.commons.technical.conversion.internal.model.CsvConverterErrorImpl;
import org.novaforge.forge.commons.technical.conversion.internal.model.CsvConverterResponseImpl;
import org.novaforge.forge.commons.technical.conversion.internal.processors.Mandatory;
import org.novaforge.forge.commons.technical.conversion.model.CsvCellDescriptor;
import org.novaforge.forge.commons.technical.conversion.model.CsvConverterDescriptor;
import org.novaforge.forge.commons.technical.conversion.model.CsvConverterResponse;
import org.novaforge.forge.commons.technical.conversion.model.ErrorType;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

/**
 * @author sbenoist
 * @date Mar 1, 2010
 */
public class CsvConverterServiceImpl implements CsvConverterService
{
  private static final Log    LOG                   = LogFactory.getLog(CsvConverterServiceImpl.class);

  private static final String DEFAULT_ENCODING_FILE = "UTF-8";

  @Override
  public <T> CsvConverterResponse<T> importFromFile(final String pFileIn, final Class<T> pClass,
      final CsvConverterDescriptor pDescriptor) throws CsvConversionException
  {
    if ((pFileIn == null) || (pClass == null) || (pDescriptor == null))
    {
      throw new CsvConversionException("one or many parameters are missing.");
    }

    if (LOG.isDebugEnabled())
    {
      LOG.debug(MessageFormat.format("convert from CSV file {0} to class of beans {1}", pFileIn,
          pClass.toString()));
    }
    final CsvConverterResponse<T> response = new CsvConverterResponseImpl<T>();
    ICsvBeanReader reader = null;
    try
    {
      final Reader fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(pFileIn),
          DEFAULT_ENCODING_FILE));

      // select the CSV preferences in function of the delimiter
      final CsvPreference csvPrefs = getCSVPreference(pDescriptor);

      reader = new CsvBeanReader(fileIn, csvPrefs);

      // if any header is provided by the file, we don't take it in account for name mapping
      // but we don't parse the first line
      if (pDescriptor.isRequiredHeader())
      {
        reader.getHeader(true);
      }

      final String[] cellsBinding = getCellsBinding(pDescriptor.getCells());
      final CellProcessor[] processors = buildProcessors(pDescriptor.getCells(), pClass);

      T bean;
      boolean end = false;
      while (!end)
      {
        try
        {
          bean = reader.read(pClass, cellsBinding, processors);
          if (bean != null)
          {
            response.addResult(bean);
          }
          else
          {
            end = true;
          }
        }
        catch (final SuperCsvConstraintViolationException e)
        {
          final int col = e.getCsvContext().getColumnNumber() + 1;
          final int line = e.getCsvContext().getLineNumber();
          LOG.error(MessageFormat.format("a mandatory field is not provided at line {0}, column {1}.", line,
              col));
          response.addError(new CsvConverterErrorImpl(ErrorType.MANDATORY_FIELD_EXCEPTION, line, col));
        }
        catch (final SuperCsvReflectionException e)
        {
          final int col = e.getCsvContext().getColumnNumber() + 1;
          final int line = e.getCsvContext().getLineNumber();
          LOG.error(MessageFormat.format("the value column {0}, at line {1} is not of the expected format.",
              col, line));
          response.addError(new CsvConverterErrorImpl(ErrorType.FIELD_FORMAT_EXCEPTION, line, col));
        }
        catch (final SuperCsvException e)
        {
          final int line = e.getCsvContext().getLineNumber();
          LOG.error(MessageFormat.format("the number of fields at line {0} is not the expected one.", line));
          response.addError(new CsvConverterErrorImpl(ErrorType.FIELDS_NUMBER_EXCEPTION, line, 0));
        }
      }

      return response;
    }

    catch (final FileNotFoundException e)
    {
      throw new CsvConversionException(MessageFormat.format("unable to find the file : {0}", pFileIn), e);
    }
    catch (final IOException e)
    {
      throw new CsvConversionException(MessageFormat.format("unable to read the file : {0}", pFileIn), e);
    }
    catch (final CsvConversionException e)
    {
      throw e;
    }
    catch (final Exception e)
    {
      throw new CsvConversionException("a technical error occured during CSV export", e);
    }
    finally
    {
      try
      {
        if (reader != null)
        {
          reader.close();
        }
      }
      catch (final IOException e)
      {
        final String mess = MessageFormat.format("unable to close the file : {0}", pFileIn);
        LOG.error(mess);
      }
    }
  }

  @Override
  public <T> void exportToFile(final String pFileOut, final Class<T> pClass, final Collection<T> pBeans,
      final CsvConverterDescriptor pDescriptor) throws CsvConversionException
  {
    if ((pFileOut == null) || (pClass == null) || (pBeans == null) || (pBeans.size() == 0)
        || (pDescriptor == null))
    {
      throw new CsvConversionException("one or many parameters are missing.");
    }

    if (LOG.isDebugEnabled())
    {
      LOG.debug(MessageFormat.format("convert to CSV file {0} from class of beans {1}", pFileOut,
          pClass.toString()));
    }

    Writer outFile = null;
    try
    {
      outFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pFileOut),
          DEFAULT_ENCODING_FILE));

      // select the CSV preferences in function of the delimiter
      final CsvPreference csvPrefs = getCSVPreference(pDescriptor);

      final CsvBeanWriter writer = new CsvBeanWriter(outFile, csvPrefs);

      final String[] header = getHeader(pDescriptor.getCells());
      final CellProcessor[] processors = buildProcessors(pDescriptor.getCells(), pClass);
      if (pDescriptor.isRequiredHeader())
      {
        writer.writeHeader(header);
      }

      final String[] cellsBinding = getCellsBinding(pDescriptor.getCells());
      for (final T bean : pBeans)
      {
        writer.write(bean, cellsBinding, processors);
      }

      writer.close();
    }
    catch (final SuperCsvConstraintViolationException e)
    {
      final int col = e.getCsvContext().getColumnNumber() + 1;
      final int line = e.getCsvContext().getLineNumber();
      throw new CsvConversionException(MessageFormat.format(
          "a mandatory field is not provided at line {0}, column {1}.", line, col), e);
    }
    catch (final IOException e)
    {
      throw new CsvConversionException(MessageFormat.format("unable to write on the file : {0}", pFileOut), e);
    }
    catch (final CsvConversionException e)
    {
      throw e;
    }
    catch (final Exception e)
    {
      throw new CsvConversionException("a technical error occured during CSV export", e);
    }
    finally
    {
      try
      {
        outFile.close();
      }
      catch (final IOException e)
      {
        final String mess = MessageFormat.format("unable to close the file : {0}", pFileOut);
        LOG.error(mess);
      }
    }
  }

  /**
   * constructs an header for cells
   *
   * @param pCells
   * @return
   */
  private String[] getHeader(final List<CsvCellDescriptor> pCells)
  {
    final String[] result = new String[pCells.size()];
    for (int i = 0; i < pCells.size(); i++)
    {
      result[i] = pCells.get(i).getName();
    }

    return result;
  }

  /**
   * returns the CSV preferences
   *
   * @param pDescriptor
   * @return
   */
  private CsvPreference getCSVPreference(final CsvConverterDescriptor pDescriptor)
  {
    CsvPreference csvPrefs;
    if (pDescriptor.getDelimiter() > 0)
    {
      csvPrefs = new CsvPreference.Builder('"', pDescriptor.getDelimiter(), "\n").build();
    }
    else
    {
      csvPrefs = CsvPreference.STANDARD_PREFERENCE;
    }

    return csvPrefs;
  }

  /**
   * returns the binding between cells and bean fields
   * 
   * @param pCells
   * @return
   */
  private String[] getCellsBinding(final List<CsvCellDescriptor> pCells)
  {
    final String[] result = new String[pCells.size()];
    for (int i = 0; i < pCells.size(); i++)
    {
      result[i] = pCells.get(i).getBindName();
    }

    return result;
  }

  /**
   * constructs cell processors to format CSV in function of cells and class of the bean
   * 
   * @param <T>
   * @param pCells
   * @param pClass
   * @return
   * @throws CsvConversionException
   */
  private <T> CellProcessor[] buildProcessors(final List<CsvCellDescriptor> pCells, final Class<T> pClass)
      throws CsvConversionException
  {
    final CellProcessor[] processors = new CellProcessor[pCells.size()];
    for (int i = 0; i < pCells.size(); i++)
    {
      // Get the field of the class mapped with the cell in function of the cell name
      final Field field = getFieldMappedWithCell(pClass, pCells.get(i).getBindName());

      // get the cell processor associated with the cell
      processors[i] = getCellProcessor(field, pCells.get(i).getFormat());

      // add a mandatory processor if the field is required
      if (pCells.get(i).isMandatory())
      {
        if (processors[i] != null)
        {
          processors[i] = new Mandatory(processors[i]);
        }
        else
        {
          processors[i] = new Mandatory();
        }
      }
    }
    return processors;
  }

  /**
   * return the Field Object mapped with the cell in function of the name of the cell
   * 
   * @param <T>
   * @param pClass
   * @param pFieldName
   * @return
   * @throws CsvConversionException
   */
  private <T> Field getFieldMappedWithCell(final Class<T> pClass, final String pFieldName)
      throws CsvConversionException
  {
    Field field;

    // try first with formatted class field (fName...)
    try
    {
      field = pClass.getDeclaredField(pFieldName);
    }
    catch (final NoSuchFieldException e)
    {
      // try next with unformatted class field (name...)
      try
      {
        field = pClass.getDeclaredField((pFieldName));
      }
      catch (final NoSuchFieldException nsfe)
      {
        final String mess = MessageFormat.format("unable to find the type of the field : {0}", pFieldName);
        throw new CsvConversionException(mess, nsfe);
      }
    }
    return field;
  }

  /**
   * instanciate a cell processor in function of the Field Object and the format associated
   * 
   * @param pField
   * @param pFormat
   * @return
   * @throws CsvConversionException
   */
  private CellProcessor getCellProcessor(final Field pField, final String pFormat)
      throws CsvConversionException
  {
    CellProcessor cellProcessor = null;

    final Class<?> processorClass = getProcessorClass(pField);
    if (processorClass != null)
    {
      // get an instance of CellProcessor in function of the format if defined
      try
      {
        final Constructor<?> defaultConstruct = processorClass.getDeclaredConstructor();
        if (pFormat == null)
        {
          cellProcessor = (CellProcessor) defaultConstruct.newInstance();
        }
        else
        {

          final Constructor<?> specifiedConstruct = getConstructorWithStringTypeParameter(processorClass);
          // construct = processorClass.getDeclaredConstructor(String.class);
          if (specifiedConstruct != null)
          {
            cellProcessor = (CellProcessor) specifiedConstruct.newInstance(pFormat);
            if (LOG.isDebugEnabled())
            {
              LOG.debug(MessageFormat.format(
                  "instanciate processor of class : {0} with specified format {1} ",
                  processorClass.getName(), pFormat));
            }
          }
          else
          {
            cellProcessor = (CellProcessor) defaultConstruct.newInstance();
            if (LOG.isDebugEnabled())
            {
              LOG.debug(MessageFormat
                  .format(
                      "unable to instanciate processor of class : {0} with specified format {1} : default processor used.",
                      processorClass.getName(), pFormat));
            }
          }
        }
      }
      catch (final Exception e)
      {
        throw new CsvConversionException(MessageFormat.format(
            "unable to instanciate processor of class : {0}", processorClass.getName()), e);
      }
    }

    return cellProcessor;
  }

  /**
   * return the processor class mapped with the field type
   * 
   * @param pField
   * @return
   */
  private Class<?> getProcessorClass(final Field pField)
  {
    Class<?> processorClass = null;

    String type;
    // if the type is a primitive, use the linked wrapper associated
    if (pField.getType().isPrimitive())
    {
      type = getWrapperName(pField.getType().getName());
    }
    else
    {
      type = pField.getType().getName();
    }

    // find an existing processor class in the package processor
    final String thisPackage = this.getClass().getPackage().getName();
    final int ind = thisPackage.lastIndexOf('.');
    final String processorPackName = thisPackage.substring(0, ind + 1).concat("processors");
    final String processorName = processorPackName + "." + getShortClassName(type) + "Processor";

    try
    {
      processorClass = Class.forName(processorName);
      if (LOG.isDebugEnabled())
      {
        LOG.debug(MessageFormat.format("{0} class processor found for : {1}", processorClass.getName(), type));
      }

    }
    catch (final ClassNotFoundException e)
    {
      if (LOG.isDebugEnabled())
      {
        LOG.debug(MessageFormat.format("no class processor found for : {0}", type));
      }
    }

    return processorClass;
  }

  private Constructor<?> getConstructorWithStringTypeParameter(final Class<?> pClass)
  {
    Constructor<?> result = null;

    Constructor<?>[] constructors;

    constructors = pClass.getDeclaredConstructors();
    for (final Constructor<?> constructor : constructors)
    {
      final Class<?>[] clazzs = constructor.getParameterTypes();
      if ((clazzs.length == 1) && clazzs[0].equals(String.class))
      {
        result = constructor;
        break;
      }
    }

    return result;
  }

  /**
   * returns the wrapper name for primitive types
   * 
   * @param pPrimitiveName
   * @return
   */
  private String getWrapperName(final String pPrimitiveName)
  {
    String result = null;
    if (pPrimitiveName.equals(Double.TYPE.getName()))
    {
      result = Double.class.getName();
    }
    else if (pPrimitiveName.equals(Boolean.TYPE.getName()))
    {
      result = Boolean.class.getName();
    }
    else if (pPrimitiveName.equals(Character.TYPE.getName()))
    {
      result = Character.class.getName();
    }
    else if (pPrimitiveName.equals(Byte.TYPE.getName()))
    {
      result = Byte.class.getName();
    }
    else if (pPrimitiveName.equals(Short.TYPE.getName()))
    {
      result = Short.class.getName();
    }
    else if (pPrimitiveName.equals(Long.TYPE.getName()))
    {
      result = Long.class.getName();
    }
    else if (pPrimitiveName.equals(Float.TYPE.getName()))
    {
      result = Float.class.getName();
    }
    else if (pPrimitiveName.equals(Integer.TYPE.getName()))
    {
      result = Integer.class.getName();
    }

    if (result == null)
    {
      LOG.error(MessageFormat.format("unable to find the wrapper for the primitive {0}", pPrimitiveName));
      result = pPrimitiveName;
    }

    return result;
  }

  /**
   * returns the short class name without the package name (ex : CSVConverter for
   * org.novaforge.services.management.converters.CSVConverters)
   *
   * @param <T>
   * @param pFullClassName
   *
   * @return
   */
  private <T> String getShortClassName(final String pFullClassName)
  {
    String    result    = pFullClassName;
    final int firstChar = result.lastIndexOf('.') + 1;
    if (firstChar > 0)
    {
      result = result.substring(firstChar);
    }
    return result;
  }
}
