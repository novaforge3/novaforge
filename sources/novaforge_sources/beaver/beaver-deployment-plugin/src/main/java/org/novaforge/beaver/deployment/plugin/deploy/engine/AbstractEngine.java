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

import groovy.json.JsonSlurper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.ant.AntFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourcesFacade;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.Resource;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

/**
 * AbstractEngine is abstract class use by engine implementation.
 * <p>
 * It contains algorithm to look for resources into whole process.
 * </p>
 * 
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
 * @see org.novaforge.beaver.deployment.plugin.deploy.engine.impl.EngineImpl
 * @see org.novaforge.beaver.deployment.plugin.deploy.api.impl.EngineSimulateImpl
 * @author Guillaume Lamirand
 * @version 2.0
 */
public abstract class AbstractEngine implements BeaverEngine
{

  public AbstractEngine()
  {
    // Nothing to do yet
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSimulateMode()
  {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServerId()
  {
    return BeaverServices.getLauncherService().getServerInfo().getServerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResource(final String pRessourceNeeded) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Ask for ressource: " + pRessourceNeeded);

    final Resource resource = new ResourceImpl(pRessourceNeeded);
    return resolveResource(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResource(final String pProductId, final String pRessourceNeeded) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Ask for ressource [%s] for product [%s]", pRessourceNeeded, pProductId));

    final Resource resource = new ResourceImpl(pProductId, pRessourceNeeded);
    return resolveResource(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResource(final String pServerId, final String pProductId, final String pRessourceNeeded)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Ask for ressource [%s] for product [%s] on server [%s]", pRessourceNeeded, pProductId,
            pServerId));

    final Resource resource = new ResourceImpl(pServerId, pProductId, pRessourceNeeded);
    return resolveResource(resource);
  }

  private String resolveResource(final Resource resource) throws BeaverException
  {
    final String ressourceFound = ResourcesFacade.resolveRessource(resource);

    if ((StringUtils.isNotEmpty(ressourceFound)) && (BeaverServices.getCurrentProductProcess() != null))
    {
      BeaverServices.getCurrentProductProcess().storeContexts(resource);
    }
    else if (ressourceFound == null)
    {
      throw new BeaverException(String.format("The ressource [%s] wasn't found.", resource));
    }
    BeaverLogger.getFilelogger().info("Answer: " + ressourceFound);
    return ressourceFound;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPreviousResource(final String pRessourceNeeded) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Getting previous ressource: " + pRessourceNeeded);

    final Resource resource = new ResourceImpl(pRessourceNeeded);
    return resolvePreviousResource(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPreviousResource(final String pProductId, final String pRessourceNeeded)
      throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Getting previous ressource [%s] for product [%s]", pRessourceNeeded, pProductId));

    final Resource resource = new ResourceImpl(pProductId, pRessourceNeeded);
    return resolvePreviousResource(resource);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPreviousResource(final String pServerId, final String pProductId,
      final String pRessourceNeeded) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Getting previous ressource [%s] for product [%s] on server [%s]", pRessourceNeeded,
            pProductId, pServerId));

    final Resource resource = new ResourceImpl(pServerId, pProductId, pRessourceNeeded);
    return resolvePreviousResource(resource);
  }

  private String resolvePreviousResource(final Resource resource) throws BeaverException
  {
    final String ressourceFound = ResourcesFacade.resolvePreviousRessource(resource);

    if (StringUtils.isNotEmpty(ressourceFound))
    {
      BeaverLogger.getFilelogger().info("Answer: " + ressourceFound);
    }
    else
    {
      throw new BeaverException(String.format("The previous ressource [%s] wasn't found.", resource));
    }
    return ressourceFound;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValueFromRegex(final String pFile, final String pPatern) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Get value in [%s] with regular expression [%s]", pFile, pPatern));
    String returnValue = "";
    final File file = new File(pFile);
    if (file.isFile())
    {
      Scanner scanner = null;
      try
      {
        scanner = new Scanner(file);
        while ((StringUtils.isEmpty(returnValue)) && (scanner.hasNext()))
        {
          final String currentLine = scanner.nextLine();
          try
          {
            final Pattern patern = Pattern.compile(pPatern);
            final Matcher matcher = patern.matcher(currentLine);

            if (matcher.find())
            {
              returnValue = matcher.group();
            }
          }
          catch (final PatternSyntaxException e)
          {
            throw new BeaverException("Patern used is not correctly written.", e);
          }
        }
      }
      catch (final FileNotFoundException e)
      {
        throw new BeaverException("getValueFromRegex : File not found", e);
      }
      finally
      {
        if (scanner != null)
        {
          scanner.close();
        }
      }
    }
    else
    {
      throw new BeaverException(String.format("File [%s] doesn't exist or is a directory.", pFile));
    }
    BeaverLogger.getFilelogger().info(String.format("Value found is [%s]", returnValue));
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEnvironment()
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        "Get environment setting: " + BeaverServices.getLauncherService().getEnvironment());
    return BeaverServices.getLauncherService().getEnvironment();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLinux()
  {
    return BeaverServices.getLauncherService().onLinux();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWindows()
  {
    return BeaverServices.getLauncherService().onWindows();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unpackFile(final Path pSrcFile, final Path pDesDir) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Unpack file");
    BeaverLogger.getFilelogger().info("File: " + pSrcFile);
    BeaverLogger.getFilelogger().info("Destination: " + pDesDir);
    if (Files.exists(pSrcFile))
    {
      final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.{jar,war}");
      final Path filename = pSrcFile.getFileName();
      Archiver archiver;
      if (matcher.matches(filename))
      {
        archiver = ArchiverFactory.createArchiver(ArchiveFormat.ZIP);
      }
      else
      {
        archiver = ArchiverFactory.createArchiver(pSrcFile.toFile());
      }
      try
      {
        archiver.extract(pSrcFile.toFile(), pDesDir.toFile());
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format(
            "An error occurend when unpacking the file [%s] to destination [%s]", pSrcFile, pDesDir), e);
      }
    }
    else
    {
      throw new BeaverException(String.format("Unable to unpack the file [%s] because it doesn't exist",
          pSrcFile));

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pack(final File pSrc, final File pDes) throws BeaverException
  {

    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info("Pack file");
    BeaverLogger.getFilelogger().info("File: " + pSrc);
    BeaverLogger.getFilelogger().info("Destination: " + pDes);
    if (pDes.getParentFile().exists() == false)
    {
      pDes.mkdirs();
    }
    final boolean warFile = pDes.getName().endsWith(".war");
    final boolean jarFile = pDes.getName().endsWith(".jar");
    if (warFile)
    {
      AntFacade.packToWar(pSrc, pDes);
    }
    else if (jarFile)
    {
      AntFacade.packToJar(pSrc, pDes);
    }
    else
    {
      final Archiver archiver = ArchiverFactory.createArchiver(pDes);
      try
      {
        archiver.create(pDes.getName(), pDes.getParentFile(), pSrc);
      }
      catch (final IOException e)
      {
        throw new BeaverException(String.format(
            "An error occurend when packing the source [%s] to destination [%s]", pSrc, pDes), e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMyStatus()
  {
    BeaverLogger.getFilelogger().info("------------------------------------------------------------------");
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
    final String status = BeaverServices.getDeploymentContext().getProductStatus(serverId,
        BeaverServices.getCurrentProductProcess().getProductId());
    BeaverLogger.getFilelogger().info("Get MyStatus: " + status);

    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMyPreviousVersion()
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
    final String productId = BeaverServices.getCurrentProductProcess().getProductId();
    final String version = BeaverServices.getBackDeploymentContext().getProductVersion(serverId, productId);
    BeaverLogger.getFilelogger().info("Getting previous version of current product: " + version);

    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMyVersion()
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final String version = BeaverServices.getCurrentProductProcess().getProductVersion();
    BeaverLogger.getFilelogger().info("Getting version of current product: " + version);

    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductVersion(final String pProductId)
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
    final String version = BeaverServices.getDeploymentContext().getProductVersion(serverId, pProductId);
    BeaverLogger.getFilelogger().info(
        String.format("Getting version of product [%s] : %s", pProductId, version));

    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDataVersion() throws BeaverException
  {
    String version = null;
    if (BeaverServices.getCurrentProductProcess().getDataArtifact() != null)
    {
      BeaverServices.getCurrentProductProcess().resolveDataArtifact();
      version = BeaverServices.getCurrentProductProcess().getDataArtifact().getBaseVersion();
    }
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMyUpdateVersion()
  {
    BeaverLogger.getFilelogger().info("------------------------------------------------------------------");
    final String serverId = BeaverServices.getLauncherService().getServerInfo().getServerId();
    final String updateVersion = BeaverServices.getDeploymentContext().getProductUpdateVersion(serverId,
        BeaverServices.getCurrentProductProcess().getProductId());
    BeaverLogger.getFilelogger().info("Get MyVersion: " + updateVersion);

    return updateVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMyProductId()
  {
    BeaverLogger.getFilelogger().info("------------------------------------------------------------------");
    final String productId = BeaverServices.getCurrentProductProcess().getProductId();
    BeaverLogger.getFilelogger().info("Get MyProductId: " + productId);

    return productId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object parseJson(final String pJson)
  {
    Object result = pJson;
    final char firstChar = pJson.trim().charAt(0);
    switch (firstChar)
    {
      case '{':
      case '[':
        result = new JsonSlurper().parseText(pJson);
        break;
      default:
        break;
    }
    return result;
  }

}
