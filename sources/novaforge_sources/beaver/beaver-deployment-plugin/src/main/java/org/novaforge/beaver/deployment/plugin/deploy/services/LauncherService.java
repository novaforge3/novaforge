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
package org.novaforge.beaver.deployment.plugin.deploy.services;

import java.util.List;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.novaforge.beaver.deployment.plugin.deploy.constant.SystemEnvironment;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverServerInfo;
import org.novaforge.beaver.exception.BeaverException;

/**
 * LauncherService interface offers method to retrieve information about current launcher and server
 * configuration
 * 
 * @author Guillaume Lamirand
 */
public interface LauncherService
{
  /**
   * Initialize the {@link LauncherService} with the given {@link BeaverMavenProject}
   * 
   * @param pLauncherProject
   * @throws BeaverException
   */
  void init(BeaverMavenProject pLauncherProject) throws BeaverException;

  /**
   * Returns the value of the property given as parameter
   * 
   * @param pKey
   *          the key to retrieve
   * @return value found in laucnher properties
   */
  String getProperty(final String pKey);

  /**
   * Returns the value of the property given as parameter
   * 
   * @param pKey
   *          the key to check
   * @return value found in laucnher properties
   */
  boolean containsProperty(final String pKey);

  /**
   * Returns the current server info
   * 
   * @return {@link BeaverServerInfo} retrieve
   */
  BeaverServerInfo getServerInfo();

  /**
   * Returns current {@link SystemEnvironment#getName()}
   * 
   * @return the environment
   */
  public String getEnvironment();

  /**
   * Returns <code>true</code> if current launcher is executed on linux environment
   * 
   * @return <code>true</code> if current launcher is executed on linux environment
   */
  boolean onLinux();

  /**
   * Returns <code>true</code> if current launcher is executed on windows environment
   * 
   * @return <code>true</code> if current launcher is executed on windows environment
   */
  boolean onWindows();

  /**
   * Returns list of {@link DependencyNode} defined in the launcher
   * 
   * @return list of {@link DependencyNode} defined in the launcher
   */
  List<DependencyNode> getProcessNodes();

  /**
   * Returns {@link DependencyNode} of the launcher
   * 
   * @return {@link DependencyNode} of the launcher
   */
  DependencyNode getRootNode();

  /**
   * Returns {@link BeaverMavenProject} built from launcher
   * 
   * @return {@link BeaverMavenProject} built from launcher
   */
  BeaverMavenProject getLauncherProject();
}
