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
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import org.novaforge.beaver.deployment.plugin.deploy.engine.ConstantEngine;
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecFacade;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public class SystemdServiceImpl implements SystemdService
{

  private static final String WANTS_EXT          = ".wants";
  private static final String ETC_SYSTEMD_SYSTEM = "/etc/systemd/system/";
  private static final String SERVICE_EXT        = ".service";
  private static final String ENABLE             = "enable";
  private static final String SYSTEMCTL          = "systemctl";
  private static final String DAEMON_RELOAD      = "daemon-reload";
  private final boolean       systemdEnable;

  /**
   * @param pNoSystemd
   */
  public SystemdServiceImpl(final boolean pNoSystemd)
  {
    systemdEnable = pNoSystemd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addService(final String pWantedBy, final String pServiceDescriptor, final String pServiceName,
      final boolean pEnable) throws BeaverException
  {
    if (isSystemdEnable())
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info("Install systemd service " + pServiceDescriptor);
      final Path targetPath = targetPath(pWantedBy);
      final Path descriptorPath = Paths.get(pServiceDescriptor);
      copyDescriptor(targetPath, descriptorPath, pServiceName);
      if (pEnable)
      {
        ExecFacade.execCommandWithParams(SYSTEMCTL, ENABLE, pServiceName);
      }
    }

  }

  private void copyDescriptor(final Path pTargetPath, final Path pDescriptorPath, final String pServiceName)
      throws BeaverException
  {
    if (Files.exists(pDescriptorPath))
    {
      final StringBuilder serviceFull = new StringBuilder(pServiceName);
      if (pServiceName.endsWith(SERVICE_EXT) == false)
      {
        serviceFull.append(SERVICE_EXT);
      }
      final Path resolve = pTargetPath.resolve(serviceFull.toString());
      try
      {
        Files.copy(pDescriptorPath, resolve, StandardCopyOption.REPLACE_EXISTING);
        // Set file permissions 777
        final Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        // add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        // add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        // add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);

        Files.setPosixFilePermissions(resolve, perms);

        ExecFacade.execCommandWithParams(SYSTEMCTL, DAEMON_RELOAD);
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format(
            "Unable to copy file descriptor to target directory [file=%s, target=%s]", pDescriptorPath,
            pTargetPath), e);
      }
    }
    else
    {
      throw new BeaverException(String.format(
          "The file descriptor used to register new service doesn't exist [file=%s]", pDescriptorPath));
    }
  }

  private Path targetPath(final String pWantedBy) throws BeaverException
  {
    final StringBuilder target = new StringBuilder(ETC_SYSTEMD_SYSTEM);
    target.append(pWantedBy).append(WANTS_EXT);
    final Path targetPath = Paths.get(target.toString());
    if (Files.exists(targetPath) == false)
    {
      throw new BeaverException(String.format("The target wantedBy doesn't exist [wantedBy=%s]", pWantedBy));
    }
    return targetPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startService(final String pServiceName) throws BeaverException
  {
    if (isSystemdEnable())
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info("Start systemd service " + pServiceName);
      ExecFacade.execCommandWithParams(SYSTEMCTL, ConstantEngine.START, pServiceName);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopService(final String pServiceName) throws BeaverException
  {
    if (isSystemdEnable())
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info("Stop systemd service " + pServiceName);
      ExecFacade.execCommandWithParams(SYSTEMCTL, ConstantEngine.STOP, pServiceName);
    }

  }

  /**
   * @return the systemDEnable
   */
  public boolean isSystemdEnable()
  {
    return systemdEnable;
  }

}
