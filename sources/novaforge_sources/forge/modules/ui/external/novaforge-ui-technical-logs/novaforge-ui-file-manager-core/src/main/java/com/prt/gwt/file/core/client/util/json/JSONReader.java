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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Reader for reading objects and arrays from a JSON compliant character stream.
 * 
 * @author Daan Kets
 */
public class JSONReader
{
	char   current = (char) -1; // We haven't read anything yet.
	int    offset  = -1;
	String data;

	public JSONReader(final String s)
	{
		data = s;
		offset = 0;
	}

	private static boolean isEndOfStream(final char character)
	{
		return character == -1;
	}

	private static boolean isWhiteSpace(final char character)
	{
		return (character == ' ') || (character == '\t') || (character == '\r') || (character == '\n');
	}

	private static boolean isStartObject(final char character)
	{
		return character == '{';
	}

	private static boolean isObjectEnd(final char character)
	{
		return character == '}';
	}

	private static boolean isArrayStart(final char character)
	{
		return character == '[';
	}

	private static boolean isArrayEnd(final char character)
	{
		return character == ']';
	}

	private static boolean isStringStart(final char character)
	{
		return character == '"';
	}

	private static boolean isStringEnd(final char character)
	{
		return isStringStart(character);
	}

	private static boolean isBooleanStart(final char character)
	{
		return (character == 'f') || (character == 't');
	}

	private static boolean isNullStart(final char character)
	{
		return character == 'n';
	}

	private static boolean isNumberStart(final char character)
	{
		return (character >= '0') && (character <= '9');
	}

	/**
	 * Reads an object from the JSON input. This will typically return either an array, or a map.
	 *
	 * @return The read object. Returns null of the end of the stream is encountered before the entire
	 *         object was read.
	 */
	public synchronized Object readJSONObject()
	{
		if (current == (char) -1)
		{ // Read first character.
			current = next();
		}
		return readJSON();
	}

	/**
	 * Reads a JSON Object, array or value.
	 *
	 * @return The JSON Object read.
	 */
	private Object readJSON()
	{
		skipWhiteSpace(); // Skip all white space, and read up to the first character?
		if (isStartObject(current))
		{
			return readObject();
		}
		else if (isArrayStart(current))
		{
			return readArray();
		}
		else
		{
			return readValue();
		}
	}

	/**
	 * Read a JSON Object from the input.
	 *
	 * @return A Map<String, Object> containing the key-value pairs.
	 */
	private Map<String, Object> readObject()
	{
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		skipWhiteSpace();
		findChar(new StringMatcher("{"));
		// Until end of object or stream.
		for (; !(isEndOfStream(current) || isObjectEnd(current)); skipWhiteSpace())
		{
			final Map.Entry<String, Object> pair = readPair();
			map.put(pair.getKey(), pair.getValue());
			skipWhiteSpace();
			if (current == ',')
			{ // Skip comma.
				current = next();
			}
		}
		next();
		return map;

	}

	/**
	 * Reads an array from the input.
	 *
	 * @return A List<Object> containing all values in order.
	 */
	private List<Object> readArray()
	{
		final List<Object> array = new LinkedList<Object>();
		skipWhiteSpace();
		findChar(new StringMatcher("["));
		for (; !(isEndOfStream(current) || isArrayEnd(current)); skipWhiteSpace())
		{
			final Object value = readJSON();
			array.add(value);
			skipWhiteSpace();
			if (current == ',')
			{ // Skip comma.
				current = next();
			}
		}
		next();
		return array;
	}

	/**
	 * Reads a pair from the input.
	 *
	 * @return A Map.Entry<String, Object> containing the entry name and value.
	 */
	private Map.Entry<String, Object> readPair()
	{
		final String name = readString();
		skipWhiteSpace();
		findChar(new StringMatcher(":"));
		final Object value = readJSON();
		return new JSONReaderMapEntry(name, value);
	}

	/**
	 * Reads a value (String, Boolean Number or null) from the input.
	 *
	 * @return The value that was read.
	 */
	private Object readValue()
	{
		if (isStringStart(current))
		{
			return readString();
		}
		else if (isBooleanStart(current))
		{
			return readBoolean();
		}
		else if (isNullStart(current))
		{
			return readNull();
		}
		else if (isNumberStart(current))
		{
			return readNumber();
		}
		else
		{
			throw new JSONParseException(offset);
		}
	}

	/**
	 * This method will read a JSON stream from the input. It will read from the given start character
	 * (look-ahead)
	 * up till the second quote. If the first non-whitespace character found is not a double quote, an exception
	 * is
	 * thrown.
	 *
	 * @param start
	 *          The start character.
	 * @return The read string.
	 */
	private String readString()
	{
		skipWhiteSpace();
		if (!isStringStart(current))
		{
			throw new JSONParseException();
		}
		next();
		return unescapeChars(new CharFinder()
		{
			@Override
			public boolean matches(final char character)
			{
				return isStringEnd(character);
			}
		});
	}

	/**
	 * Reads a number from the input. The number is automatically converted to the most suitable type.
	 *
	 * @return The read number.
	 */
	private Number readNumber()
	{
		skipWhiteSpace();
		if (!isNumberStart(current))
		{
			throw new JSONParseException();
		}
		final String number = findChars(new CharFinder()
		{
			@Override
			public boolean matches(final char character)
			{
				return "-+0123456789. eE".indexOf(character) == -1;
			}
		});

		if (number.contains("."))
		{
			final Double value = Double.parseDouble(number);
			if ((value >= Float.MIN_VALUE) && (value <= Float.MAX_VALUE))
			{
				return value.floatValue();
			}
			else
			{
				return value;
			}
		}
		else
		{
			// Assume long
			/*
			 * if (value>=Integer.MIN_VALUE && value <=Integer.MAX_VALUE){
			 * return value.intValue();
			 * } else {
			 * return value;
			 * }
			 */
			return Long.parseLong(number);
		}

	}

	/**
	 * Moves to the next character. Also updates the offset.
	 *
	 * @return The current character after the move.
	 * @throws IOException
	 *           If an IO exception occurs.
	 */
	private char next()
	{
		current = (data.length() == offset ? (char) -1 : data.charAt(offset++));
		return current;
	}

	/**
	 * @param start
	 *          The start character.
	 * @return The end character.
	 */
	private Boolean readBoolean()
	{
		skipWhiteSpace();
		if (current == 't')
		{
			findChars(new StringMatcher("true"));
			return true;
		}
		else if (current == 'f')
		{
			findChars(new StringMatcher("false"));
			return false;
		}
		else
		{
			throw new JSONParseException();
		}
	}

	/**
	 * @param start
	 *          The start character.
	 * @return The end character.
	 */
	private Object readNull()
	{
		findChars(new StringMatcher("null"));
		return null;
	}

	/**
	 * This will read from the stream, as long as the current character represents whitespace.
	 *
	 * @param start
	 *          The character to start with (look ahead).
	 * @return The first non-whitespace character found, or -1 if at the end of the stream.
	 * @throws IOException
	 *           If an IO exception occurs.
	 */
	private char skipWhiteSpace()
	{
		return findChar(new CharFinder()
		{
			@Override
			public boolean matches(final char character)
			{
				return !isWhiteSpace(character);
			}
		});
	}

	/**
	 * This method will read up to the point where the found character matches the specified finder.
	 *
	 * @param start
	 *          The start character.
	 * @param finder
	 *          The character finder.
	 * @return The character found. -1 if the stream is at an end.
	 */
	private char findChar(final CharFinder finder)
	{
		while (!finder.matches(current) || isEndOfStream(current))
		{
			current = next();
		}
		return current;
	}

	/**
	 * Method for reading a set of characters from the input, as long as the delimiter does not match.
	 *
	 * @param delimiter
	 *          The char finder delimiter.
	 * @return The read string.
	 * @throws IOException
	 *           If an IO exception occurs.
	 */
	private String findChars(final CharFinder delimiter)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		while (!delimiter.matches(current) || isEndOfStream(current))
		{
			stringBuilder.append(current);
			current = next();
		}
		return stringBuilder.toString();
	}

	/**
	 * Reads a string while un-escaping characters.
	 *
	 * @param delimiter
	 *          The delimiter to use.
	 * @return The unescaped string.
	 * @throws IOException
	 *           If an IO exception occurs.
	 */
	private String unescapeChars(final CharFinder delimiter)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		char last = (char) -1;
		while (!delimiter.matches(last) || isEndOfStream(current))
		{
			if ((last != (char) -1) && (last != '\\'))
			{ // No escape sequence!
				stringBuilder.append(last);
			}
			else if (last == '\\')
			{ // Escape sequence!
				switch (current)
				{
					case 'b':
						current = '\b';
						break;
					case 'f':
						current = '\f';
						break;
					case 'n':
						current = '\n';
						break;
					case 'r':
						current = '\r';
						break;
					case 't':
						current = '\t';
						break;
					case 'u':
						// Read unicode!
						final char[] unicode = new char[] { 4 };
						for (int i = 0; i < unicode.length; i++)
						{
							unicode[i] = next();
						}
						final String string = String.valueOf(unicode);
						current = (char) Integer.parseInt(string, 16);
						break;
				}
			}
			last = current;
			current = next();
		}
		return stringBuilder.toString();
	}

	private interface CharFinder
	{
		/**
		 * Checks if a character matches the character to find.
		 *
		 * @param current
		 *          The current character.
		 * @return true if match.
		 */
		boolean matches(char current);
	}

	/**
	 * Map.Entry implementation for holding the read name-value pair.
	 *
	 * @author Daan Kets
	 */
	private static class JSONReaderMapEntry implements Map.Entry<String, Object>
	{
		private final String key;
		private       Object value;

		public JSONReaderMapEntry(final String name, final Object value)
		{
			key = name;
			this.value = value;
		}

		@Override
		public String getKey()
		{
			return key;
		}

		@Override
		public Object getValue()
		{
			return value;
		}

		@Override
		public Object setValue(final Object value)
		{
			final Object old = this.value;
			this.value = value;
			return old;
		}
	}

	@SuppressWarnings("unused")
	private class CharMatcher implements CharFinder
	{
		private final char charToFind;

		/**
		 * Constructs a new char matcher.
		 *
		 * @param charToFind
		 *          The character to find.
		 */
		public CharMatcher(final char charToFind)
		{
			this.charToFind = charToFind;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean matches(final char current)
		{
			return (current == charToFind);
		}
	}

	private class StringMatcher implements CharFinder
	{
		private final String stringToFind;
		int                  position = 0;

		/**
		 * Constructs a new char matcher.
		 *
		 * @param charToFind
		 *          The character to find.
		 */
		public StringMatcher(final String stringToFind)
		{
			this.stringToFind = stringToFind;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean matches(final char current)
		{
			if (position == stringToFind.length())
			{
				return true;
			}
			else
			{
				if (stringToFind.charAt(position) != current)
				{
					mismatch(stringToFind, position, current);
				}
			}
			position++;
			return false;
		}

		public void mismatch(final String stringToFind, final int position, final char current)
		{
			throw new RuntimeException("Expected " + stringToFind.charAt(position) + " found " + current + '!');
		}
	}

	/**
	 * Exception thrown when parsing finds an error.
	 * 
	 * @author Daan Kets
	 */
	class JSONParseException extends RuntimeException
	{
		private static final long serialVersionUID = -6633848303396277196L;
		int                       position;

		public JSONParseException()
		{
			this(offset);
		}

		public JSONParseException(final int position)
		{
			this.position = position;
		}

		@Override
		public String getMessage()
		{
			return "JSON Parse exception at position " + position;
		}
	}

}
