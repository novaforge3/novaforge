/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;

/**
 * {@link AntLogger} is used to change the way to display {@link Ant} message.
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class AntLogger extends DefaultLogger
{
  public AntLogger()
  {
    setErrorPrintStream(System.err);
    setOutputPrintStream(System.out);
    setMessageOutputLevel(Project.MSG_INFO);
  }

  /*
   * Here we want to use maven Log instead of original output use by Ant Project (non-Javadoc)
   * @see org.apache.tools.ant.DefaultLogger#messageLogged(org.apache.tools.ant .BuildEvent)
   */
  /**
   * {@inheritDoc}
   */
  @Override
  public void messageLogged(final BuildEvent event)
  {
    final int priority = event.getPriority();
    // Filter out messages based on priority
    if (priority <= msgOutputLevel)
    {

      final StringBuilder message = new StringBuilder();
      if ((event.getTask() != null) && !emacsMode)
      {
        // Print out the name of the task if we're in one
        final String name = event.getTask().getTaskName();
        String label = "[" + name + "] ";
        final int size = LEFT_COLUMN_SIZE - label.length();
        final StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < size; i++)
        {
          tmp.append(" ");
        }
        tmp.append(label);
        label = tmp.toString();

        try
        {
          final BufferedReader msgReader = new BufferedReader(new StringReader(event.getMessage()));
          String line = msgReader.readLine();
          boolean first = true;
          do
          {
            if (first)
            {
              if (line == null)
              {
                message.append(label);
                break;
              }
            }
            else
            {
              message.append(StringUtils.LINE_SEP);
            }
            first = false;
            message.append(label).append(line);
            line = msgReader.readLine();
          }
          while (line != null);
        }
        catch (final IOException e)
        {
          // shouldn't be possible
          message.append(label).append(event.getMessage());
        }
      }
      else
      {
        message.append(event.getMessage());
      }
      final Throwable eventException = event.getException();
      if ((Project.MSG_DEBUG <= msgOutputLevel) && (eventException != null))
      {
        message.append(StringUtils.getStackTrace(eventException));
      }

      final String msg = message.toString();

      if (priority >= Project.MSG_INFO)
      {
        BeaverLogger.getFilelogger().debug(msg);
        if ("Execute".equals(event.getTask().getTaskName()))
        {
          event.getProject().setNewProperty("ErrorExecute", msg);
        }
      }
      else
      {
        BeaverLogger.getFilelogger().error(msg);
        event.getProject().setNewProperty("ErrorTask", event.getTask().getTaskName());
        event.getProject().setNewProperty("ErrorMsg", msg);
      }
      log(msg);
    }
  }
}
