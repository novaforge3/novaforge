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
package com.prt.gwt.file.core.client.util.json;

import java.util.Collection;
import java.util.Map;

/**
 * Writer for creating writing objects to a JSON compliant output.
 * 
 * @author DKETS
 */
public class JSONWriter
{

	private StringBuffer res;

	/**
	 * Writer a value to the JSON output.
	 * The output will be closed after ONE cycle!
	 * 
	 * @param object
	 *          The object to write.
	 */
	public String writeJSON(final Object value)
	{
		res = new StringBuffer("");
		writeJSONValue(value);
		return res.toString();
	}

	/**
	 * Inner method for writing a value to the output.
	 * 
	 * @param object
	 *          The value to write.
	 */
	@SuppressWarnings("unchecked")
	private void writeJSONValue(final Object object)
	{
		if (object == null)
		{ // Null
			writeJSONNull();
		}
		else if (object instanceof String)
		{ // Strings
			writeJSONString((String) object);
		}
		else if (object instanceof Number)
		{ // Numbers
			writeJSONNumber((Number) object);
		}
		else if (object instanceof Boolean)
		{ // Booleans
			writeJSONBoolean((Boolean) object);
			/*
			 * } else if (object.getClass().isArray()){ // Array
			 * writeJSONArray(object);
			 */}
		else if (object instanceof Map)
		{ // Map
			writeJSONMap((Map<?, ?>) object);
		}
		else if (object instanceof Collection)
		{ // Collection
			writeJSONCollection((Collection<?>) object);
		}
		else
		{ // Object
			throw new RuntimeException("Cannot write general object!");
			// writeJSONObject(object);
		}
	}

	/**
	 * Inner method for writing an object.
	 * 
	 * @param object
	 *          The object.
	 */
	/*
	 * private void writeJSONObject(Object object){
	 * res.append('{'); // Open
	 * Field[] fields = object.getClass().getFields();
	 * int finsih = fields.length -1;
	 * for (int i = 0; i < fields.length; i ++){
	 * Field field = fields[i];
	 * if (!field.isAccessible()){
	 * try {
	 * field.setAccessible(true);
	 * } catch (SecurityException securityException){}
	 * }
	 * if (field.isAccessible()){
	 * try {
	 * writeJSONPair(field.getName(), field.get(object));
	 * if (i!=finsih){
	 * res.append(", ");
	 * }
	 * } catch (IllegalAccessException illegalAccessException){
	 * illegalAccessException.printStackTrace();
	 * }
	 * }
	 * }
	 * res.append('}'); // Close
	 * }
	 */
	/**
	 * Inner method for writing a map.
	 * 
	 * @param map
	 *          The map.
	 */
	private void writeJSONMap(final Map<?, ?> map)
	{
		res.append('{'); // Open
		final int finish = map.size() - 1;
		int i = 0;
		for (final Map.Entry<?, ?> entry : map.entrySet())
		{
			writeJSONMapEntry(entry);
			if (i != finish)
			{
				res.append(", ");
			}
			i++;
		}
		res.append('}'); // Close
	}

	/**
	 * Inner method for writing an entry.
	 * 
	 * @param entry
	 *          The map entry.
	 */
	private void writeJSONMapEntry(final Map.Entry<?, ?> entry)
	{
		writeJSONPair(String.valueOf(entry.getKey()), entry.getValue());
	}

	/**
	 * Inner method for writing a collection.
	 * 
	 * @param collection
	 *          The collection.
	 */
	private void writeJSONCollection(final Collection<?> collection)
	{
		res.append('['); // Open
		final int finish = collection.size() - 1;
		int i = 0;
		for (final Object value : collection)
		{
			writeJSONValue(value);
			if (i != finish)
			{
				res.append(", ");
			}
			i++;
		}
		res.append(']'); // Close
	}

	/**
	 * Inner method for writing an array.
	 * 
	 * @param array
	 *          The array.
	 */
	/*
	 * private void writeJSONArray(Object array){
	 * res.append('[');
	 * int length = Array.getLength(array);
	 * int finish = length-1;
	 * for (int i = 0; i < length; i ++){
	 * writeJSONValue(Array.get(array, i));
	 * if (i!=finish){
	 * res.append(", ");
	 * }
	 * }
	 * res.append(']');
	 * }
	 */
	/**
	 * Inner method for writing null.
	 */
	private void writeJSONNull()
	{
		res.append("null");
	}

	/**
	 * Inner method for writing a pair.
	 * 
	 * @param name
	 *          The pair name.
	 * @param value
	 *          The pair value.
	 */
	private void writeJSONPair(final String name, final Object value)
	{
		writeJSONString(name);
		res.append(": ");
		writeJSONValue(value);
	}

	/**
	 * Inner method for writing a number.
	 * 
	 * @param number
	 *          The number.
	 */
	private void writeJSONNumber(final Number number)
	{
		res.append(number.toString());
	}

	/**
	 * Inner method for writing a boolean.
	 * 
	 * @param value
	 *          The boolean value.
	 */
	private void writeJSONBoolean(final Boolean value)
	{
		res.append(value.toString());
	}

	/**
	 * Inner method for writing a string.
	 * 
	 * @param value
	 *          The string.
	 */
	private void writeJSONString(final String value)
	{
		res.append('\"');
		escape(value);
		res.append('\"');
	}

	/**
	 * Inner method for writing an escaped string.
	 * 
	 * @param string
	 *          The string to escape.
	 */
	private void escape(final String string)
	{
		for (int i = 0; i < string.length(); i++)
		{
			final char ch = string.charAt(i);
			switch (ch)
			{
				case '"':
					res.append("\\\"");
					break;
				case '\\':
					res.append("\\\\");
					break;
				case '\b':
					res.append("\\b");
					break;
				case '\f':
					res.append("\\f");
					break;
				case '\n':
					res.append("\\n");
					break;
				case '\r':
					res.append("\\r");
					break;
				case '\t':
					res.append("\\t");
					break;
				case '/':
					res.append("\\/");
					break;
				default:
					if (((ch >= '\u0000') && (ch <= '\u001F')) || ((ch >= '\u007F') && (ch <= '\u009F'))
					    || ((ch >= '\u2000') && (ch <= '\u20FF')))
					{
						final String ss = Integer.toHexString(ch);
						res.append("\\u");
						for (int k = 0; k < (4 - ss.length()); k++)
						{
							res.append('0');
						}
						res.append(ss.toUpperCase());
					}
					else
					{
						res.append(ch);
					}
			}
		}
	}
}
