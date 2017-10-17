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
package org.novaforge.beaver.deployment.plugin.deploy.engine;

/**
 * {@link ConstantEngine} contains all constants chare by both engine and simulation engine.
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class ConstantEngine
{
  public static final String SPACE             = " ";

  public final static String DIR               = "dir";
  public final static String FILE              = "file";
  public final static String TYPE_DIR          = "d";
  public final static String TYPE_FILE         = "f";
  public final static String SHELL_TYPE        = "shell";
  public final static String SCRIPT_TYPE       = "script";
  public final static String XML_TYPE          = "xml";
  public final static String SHELL_COMMENT     = "#";
  public final static String SCRIPT_COMMENT    = "//";
  public final static String XML_COMMENT_BEGIN = "<!-- ";
  public final static String XML_COMMENT_END   = " -->";

  public static final String FIND              = "find";
  public static final String FIND_TYPE         = "-type";
  public static final String FIND_EXEC         = "-exec";
  public static final String FIND_EXEC_END     = "{} ;";
  public static final String CHMOD             = "chmod";

  public static final String START             = "start";
  public static final String STOP              = "stop";

  public static final String MOVE_LINUX        = "mv";
  public static final String MOVE_WINDOWS      = "move";
  public static final String CMD_EXE_WINDOWS = "cmd.exe";
  public static final String SLASH_C_WINDOWS = "/c";

  private ConstantEngine()
  {
    // Utility class should have private explicit constructor
  }
}
