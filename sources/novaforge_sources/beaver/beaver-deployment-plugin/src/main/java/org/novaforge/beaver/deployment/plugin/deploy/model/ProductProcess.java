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
package org.novaforge.beaver.deployment.plugin.deploy.model;

import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.context.DeploymentContext;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourceImpl;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources.ResourcesFacade;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.MavenPropertyMode;
import org.novaforge.beaver.resource.Resource;

/**
 * {@link ProductProcess} stores all information regarding a product process.
 * 
 * @author Guillaume Lamirand
 * @version 3.0
 */
public class ProductProcess extends AbstractProcess
{

  public ProductProcess(final DependencyNode pProductNode) throws BeaverException
  {
    super(pProductNode);

  }

  /**
   * Store the context of the product into the persistence file
   * 
   * @param pResource
   *          represents the context to save
   * @throws BeaverException
   */
  public void storeContexts(final Resource pResource) throws BeaverException
  {
    final DeploymentContext deployFileimpl = BeaverServices.getDeploymentContext();

    if (pResource.isCorrectFormat())
    {
      final String mavenProperty = pResource.getMavenProperty(MavenPropertyMode.FULL);
      if ((pResource.isCurrentProductRelated() == false)
          && (deployFileimpl.existProductContext(pResource.getServerId(), getProductId(), mavenProperty) == false))
      {
        deployFileimpl.addProductContext(pResource.getServerId(), getProductId(), mavenProperty, "@{"
            + mavenProperty + "}");
      }
    }
    else
    {
      throw new BeaverException("The format of " + pResource
          + " is incorrect. It should be *serverId:pResource.getServerId(),getProductId(),.property*.");
    }
  }

  /**
   * Browse the product's properties
   * 
   * @throws BeaverException
   */
  public void browseProductProperty() throws BeaverException
  {
    final Set<Entry<String, String>> propertyEntrySet = processInfo.propertyEntrySet();
    for (final Entry<String, String> entry : propertyEntrySet)
    {
      final Resource currentProperty = new ResourceImpl(entry.getKey());
      if ((currentProperty.isCorrectFormat()) && (currentProperty.isCurrentProductRelated()))
      {
        final String propertyValue = saveProperty(currentProperty);
        saveContextFromProperty(propertyValue);
      }
    }

  }

  /**
   * Save the property to the persistence file
   * 
   * @param pResource
   *          the property to save
   * @throws BeaverException
   */
  private String saveProperty(final Resource pResource) throws BeaverException
  {
    final DeploymentContext deployFileimpl = BeaverServices.getDeploymentContext();

    final Resource property = new ResourceImpl(pResource.getServerId(), getProductId(), pResource.getKey());
    String ressourceToken = ResourcesFacade.getPropertyFromParent(property);
    if (ressourceToken == null)
    {
      ressourceToken = processInfo.getProperty(pResource.getKey());
      if (ressourceToken == null)
      {
        throw new BeaverException(String.format(
            "The property [%s] is declared in process but the returned value is null", pResource));
      }
    }
    if (deployFileimpl.existProductProperty(pResource.getServerId(), getProductId(), pResource.getKey()))
    {
      final String previousProperty = deployFileimpl.getProductProperty(pResource.getServerId(),
          getProductId(), pResource.getKey());
      previousProperties.put(pResource.getKey(), previousProperty);
    }
    deployFileimpl.addProductProperty(pResource.getServerId(), getProductId(), pResource.getKey(),
        ressourceToken);

    return ressourceToken;
  }

  /**
   * Look for the context of the property saved
   * 
   * @param pProperty
   *          the property saved
   * @throws BeaverException
   */
  private void saveContextFromProperty(final String pProperty) throws BeaverException
  {
    final DeploymentContext deploymentContext = BeaverServices.getDeploymentContext();
    if (StringUtils.isNotBlank(pProperty))
    {
      try
      {
        final Pattern patern = Pattern.compile(ResourcesFacade.TOKEN_PATTERN);
        final Matcher matcher = patern.matcher(pProperty);

        if (matcher.find())
        {
          final String token = matcher.group();
          final String tokenLookFor = pProperty.substring(matcher.start() + 2, matcher.end() - 1);

          final Resource resourceFromToken = new ResourceImpl(tokenLookFor);

          if (resourceFromToken.isCorrectFormat()
              && (getProductId().equals(resourceFromToken.getProductId()) == false))
          {
            deploymentContext.addProductContext(resourceFromToken.getServerId(), getProductId(),
                tokenLookFor, token);

          }
        }
      }
      catch (final PatternSyntaxException e)
      {
        throw new BeaverException(e.toString(), e);
      }
    }
  }

}
