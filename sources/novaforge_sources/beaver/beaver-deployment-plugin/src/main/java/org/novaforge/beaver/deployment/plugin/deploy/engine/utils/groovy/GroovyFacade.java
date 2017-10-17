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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * GroovyFacade is a facade to use system command through groovy process..
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class GroovyFacade
{
  private static final String COMMAND = "command";
  private static final String OUT     = "out";
  private static final String ERR     = "err";

  private GroovyFacade()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  public static GroovyLog mysqlExecute(final String pMysqlBin, final String pMysqlPort, final String pUser,
      final String pPwd, final String pCommand)
  {
    final List<String> command = new ArrayList<String>();
    command.add(pMysqlBin);
    if (StringUtils.isNotBlank(pMysqlPort))
    {
      command.add("--port=" + pMysqlPort);
    }
    if (StringUtils.isNotBlank(pUser))
    {
      command.add("--user=" + pUser);
    }
    if (StringUtils.isNotBlank(pPwd))
    {
      command.add("--password=" + pPwd);
    }
    command.add("-e");
    command.add(pCommand);
    if (BeaverLogger.getFilelogger().isDebugEnabled())
    {
      BeaverLogger.getFilelogger().debug(String.format("Mysql command is [%s]", command));
    }
    final GroovyLog mysqlReturn = executeList(command);

    return mysqlReturn;
  }

  public static GroovyLog executeLine(final String pCommand)
  {

    final Binding binding = new Binding();
    binding.setVariable(COMMAND, pCommand);

    return execute(binding);
  }

  public static GroovyLog executeList(final List<String> pCommand)
  {

    final Binding binding = new Binding();
    binding.setVariable(COMMAND, pCommand);

    return execute(binding);
  }

  private static GroovyLog execute(final Binding pBinding)
  {

    final GroovyShell shell = new GroovyShell(pBinding);

    final Object chown = shell
        .evaluate("def process = command.execute(); process.waitFor(); out=process.in.text; err = process.err.text; return process.exitValue()");

    return new GroovyLog(chown.toString(), pBinding.getVariable(OUT).toString(), pBinding.getVariable(ERR)
        .toString());
  }
}
