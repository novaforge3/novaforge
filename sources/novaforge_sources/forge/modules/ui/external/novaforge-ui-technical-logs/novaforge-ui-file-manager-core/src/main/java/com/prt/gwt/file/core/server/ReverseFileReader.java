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
package com.prt.gwt.file.core.server;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ReverseFileReader
{
	private static final int   DEFAULT_BUF_SIZE = 8000;
	protected RandomAccessFile raf;
	private byte[]             buffer;
	private int                bufferCursor;

	public ReverseFileReader(final RandomAccessFile raf)
	{
		this.raf = raf;
	}

	public long getLinePointer(final long startWith, final long lines, final boolean down) throws IOException
	{
		final byte[] buffer = new byte[DEFAULT_BUF_SIZE];
		long currentLineN = 0;
		final ReverseFileReader.FileMoveStrategy strategy = new FileMoveStrategy(startWith, lines, down);
		int activeBufferSize = DEFAULT_BUF_SIZE;
		while (strategy.checkCondition(currentLineN) && (activeBufferSize != 0))
		{
			activeBufferSize = raf.read(buffer, 0, activeBufferSize);
			final int read = activeBufferSize;

			if (activeBufferSize == DEFAULT_BUF_SIZE)
			{
				activeBufferSize = strategy.moveFileCursor();
			}
			else
			{
				activeBufferSize = 0;
			}

			currentLineN += gotoLastLine(read, lines - currentLineN, strategy);
		}
		strategy.moveFileCursorWithBufferCursor();
		return raf.getFilePointer();
	}

	/**
	 * Goto last acceptable line within a buffer.
	 *
	 * @param dataSize
	 *     - read buffer size
	 * @param linesLeft
	 *     - lines left to count
	 *
	 * @return - lines read
	 */
	private long gotoLastLine(final int dataSize, final long linesLeft, final FileMoveStrategy strategy)
	{
		int  c         = -1;
		long linesRead = 0;
		strategy.setBufferCursor(dataSize);
		while ((bufferCursor < dataSize) && (bufferCursor >= 0))
		{
			c = buffer[bufferCursor];
			strategy.moveBufferCursorBackward();
			switch (c)
			{
				case -1:
				case '\n':
					linesRead++;
					break;
				case '\r':
					linesRead++;
					strategy.moveBufferCursorForward();
					if ((bufferCursor >= dataSize) || (buffer[bufferCursor] != '\n'))
					{
						strategy.moveBufferCursorBackward();
					}
					break;
			}

			if (linesRead == linesLeft)
			{
				break;
			}
		}
		return linesRead;
	}

	class FileMoveStrategy
	{
		private final long    startWith;
		private final long    lines;
		private final boolean down;
		private final long    fileLen;

		FileMoveStrategy(final long startWith, final long lines, final boolean down) throws IOException
		{
			this.startWith = startWith;
			this.lines = lines;
			this.down = down;
			fileLen = raf.length();
		}

		boolean checkCondition(final long currentLineN)
		{
			return down ? currentLineN < startWith : currentLineN < (startWith + lines);
		}

		void setBufferCursor(final int dataSize)
		{
			bufferCursor = down ? 0 : dataSize;
		}

		void moveBufferCursorForward()
		{
			bufferCursor = down ? bufferCursor + 1 : bufferCursor - 1;
		}

		void moveBufferCursorBackward()
		{
			bufferCursor = down ? bufferCursor - 1 : bufferCursor + 1;
		}

		void moveFileCursorWithBufferCursor() throws IOException
		{
			final long gotoCursor = down ? (raf.getFilePointer() - DEFAULT_BUF_SIZE) + bufferCursor : (raf
			    .getFilePointer() - (2 * DEFAULT_BUF_SIZE)) + bufferCursor;
			raf.seek(Math.max(gotoCursor, 0));
		}

		/**
		 * @return buffer active size
		 * @throws IOException
		 */
		int moveFileCursor() throws IOException
		{
			final long gotoCursor = !down ? raf.getFilePointer() - (2 * DEFAULT_BUF_SIZE) : raf.getFilePointer();
			raf.seek(Math.max(gotoCursor, 0));
			int delta = DEFAULT_BUF_SIZE;
			if (down && ((gotoCursor + DEFAULT_BUF_SIZE) > fileLen))
			{
				delta = (int) (fileLen - gotoCursor);
			}
			else if (!down && (gotoCursor < 0))
			{
				delta = (int) -gotoCursor;
			}
			return delta;
		}
	}

}
