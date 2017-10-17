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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec;

import java.io.IOException;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;

import com.google.common.base.Strings;

/**
 * GroovyFacade is a facade to use system command through groovy process..
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class ExecFacade
{
  private ExecFacade()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  public static boolean isSuccess(final int pExitValue)
  {
    return pExitValue == 0;
  }

  public static int execCommandLine(final String pCommand)
  {
    int exitValue = 1;
    if (Strings.isNullOrEmpty(pCommand) == false)
    {
      final CommandLine cmdLine = CommandLine.parse(pCommand);

      if (BeaverLogger.getFilelogger().isDebugEnabled())
      {
        BeaverLogger.getFilelogger().debug(String.format("Execute command is [%s]", cmdLine.getExecutable()));
      }
      exitValue = execCommandLineAndGetResult(cmdLine).getExitCode();
    }
    return exitValue;
  }

  public static int execCommandWithParams(final String pCommand, final List<String> pParams)
  {
    return execCommandWithParams(pCommand, pParams.toArray(new String[pParams.size()]));
  }

  public static int execCommandWithParams(final String pCommand, final String... pParams)
  {
    return execCommandWithParamsAndGetResult(pCommand, pParams).getExitCode();
  }

  public static ExecResult execCommandWithParamsAndGetResult(final String pCommand, final String... pParams)
  {
    ExecResult retour = new ExecResult();
    if (Strings.isNullOrEmpty(pCommand) == false)
    {
      final CommandLine cmdLine = new CommandLine(pCommand);
      if (pParams.length > 0)
      {
        cmdLine.addArguments(pParams);
      }
      if (BeaverLogger.getFilelogger().isDebugEnabled())
      {
        BeaverLogger.getFilelogger().debug(String.format("Execute command is [%s]", cmdLine.getExecutable()));
      }
      retour = execCommandLineAndGetResult(cmdLine);
    }
    return retour;
  }

  private static ExecResult execCommandLineAndGetResult(final CommandLine cmdLine)
  {
    ExecResult retour = new ExecResult();
    final DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValue(0);

    final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    final PumpStreamHandler psh = new PumpStreamHandler(stdout);
    executor.setStreamHandler(psh);
    try
    {
      retour.setExitCode(executor.execute(cmdLine));
      retour.setCmdResult(stdout.toString());
      if (BeaverLogger.getFilelogger().isDebugEnabled())
      {
        BeaverLogger.getFilelogger().debug(stdout);
      }
    }
    catch (final IOException e)
    {
      if (e instanceof ExecuteException)
      {
        retour.setExitCode(((ExecuteException) e).getExitValue());
        retour.setCmdResult(stdout.toString());
        if (BeaverLogger.getFilelogger().isDebugEnabled())
        {
          BeaverLogger.getFilelogger().debug(stdout);
        }
      }
    }
    finally
    {
      try
      {
        stdout.close();
      }
      catch (final IOException e)
      {
        // Ignore this
      }
    }
    return retour;
  }
}
