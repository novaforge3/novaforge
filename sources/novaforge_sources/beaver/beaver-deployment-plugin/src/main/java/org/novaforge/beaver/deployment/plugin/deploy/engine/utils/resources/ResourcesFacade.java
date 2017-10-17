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
package org.novaforge.beaver.deployment.plugin.deploy.engine.utils.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.novaforge.beaver.deployment.plugin.deploy.model.BeaverMavenProject;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.MavenPropertyMode;
import org.novaforge.beaver.resource.Resource;

public class ResourcesFacade
{
  /**
   * Token used to retrieve resource dependency
   */
  public static final String TOKEN_PATTERN = "\\@\\{[a-zA-Z0-9_\\-\\.:]*\\}";

  private ResourcesFacade()
  {
    // Utility class should have private explicit constructor ( Sonar rule : Hide Utility Class Constructor )
  }

  /**
   * It will look for your resource inside the global process.
   * 
   * @param pRessource
   *          represents the name of the ressource needed.
   * @return the ressource found after the search
   * @throws BeaverException
   */
  public static String resolveRessource(final Resource pRessource) throws BeaverException
  {
    final String ressourceToken = ResourcesFacade.lookupResource(pRessource);
    final String ressourceCleanup = ResourcesFacade.cleanToken(ressourceToken, pRessource);
    return ResourcesFacade.getRelatedResource(ressourceCleanup);
  }

  /**
   * <p>
   * Look for the property asked in the following steps
   * <ul>
   * <li>Check if the property exists for the current product</li>
   * <li>Check if the property is defined by parents</li>
   * <li>Get the property from the persistence file</li>
   * </ul>
   * </p>
   * 
   * @param pResource
   *          the property you want to look for
   * @return the resultat
   */
  private static String lookupResource(final Resource pResource) throws BeaverException
  {
    if (pResource.isCorrectFormat() == false)
    {
      throw new BeaverException("The format of " + pResource
          + " is incorrect. It should be *productId.property*.");
    }
    String ressourceFound = null;
    if (pResource.isProductRelated())
    {
      ressourceFound = getPropertyFromProduct(pResource);
      if (ressourceFound == null)
      {
        final String ressourceToken = getPropertyFromParent(pResource);
        if (ressourceToken == null)
        {
          if (BeaverServices.getDeploymentContext().existProductProperty(pResource.getServerId(),
              pResource.getProductId(), pResource.getKey()))
          {
            ressourceFound = BeaverServices.getDeploymentContext().getProductProperty(
                pResource.getServerId(), pResource.getProductId(), pResource.getKey());
          }
        }
        else
        {
          ressourceFound = ressourceToken;
        }
      }
    }
    else
    {
      ressourceFound = getPropertyFromServer(pResource);
    }

    return ressourceFound;
  }

  /**
   * If the property has correct format (serverId:key) and if it exists in the persistence file, return the
   * resultat.
   * 
   * @param pResource
   *          the property you want to look for
   * @return the resultat
   */
  private static String getPropertyFromServer(final Resource pResource) throws BeaverException
  {
    String ressourceFound = null;

    final boolean existingProperty = BeaverServices.getDeploymentContext().existServerProperty(
        pResource.getServerId(), pResource.getKey());
    if (existingProperty)
    {
      ressourceFound = BeaverServices.getDeploymentContext().getServerProperty(pResource.getServerId(),
          pResource.getKey());
    }
    // Check if the server property is not overred in the launcher project
    else
    {
      final String mavenProperty = pResource.getMavenProperty(MavenPropertyMode.FULL_RESOLVED);
      if (BeaverServices.getLauncherService().containsProperty(mavenProperty))
      {
        ressourceFound = BeaverServices.getLauncherService().getProperty(mavenProperty).toString();
      }
    }

    return ressourceFound;

  }

  /**
   * If the property has correct format (productId.key) and if it exists in the persistence file, return the
   * resultat.
   * 
   * @param pResource
   *          the property you want to look for
   * @return the resultat
   */
  private static String getPropertyFromProduct(final Resource pResource) throws BeaverException
  {
    String ressourceFound = null;

    if (pResource.isCurrentProductRelated())
    {
      final String productId = BeaverServices.getCurrentProductProcess().getProductId();
      final boolean existingProperty = BeaverServices.getDeploymentContext().existProductProperty(
          pResource.getServerId(), productId, pResource.getKey());
      if (existingProperty)
      {
        ressourceFound = BeaverServices.getDeploymentContext().getProductProperty(pResource.getServerId(),
            productId, pResource.getKey());
      }
    }
    return ressourceFound;

  }

  /**
   * Search the property in the POM launcher if exists or in the parents of current products
   * 
   * @param pResource
   *          the property you want to look for
   * @return the resultat
   * @throws BeaverException
   */
  public static String getPropertyFromParent(final Resource pResource) throws BeaverException
  {
    String propertyFound = null;

    // "server_id:product_id.property"
    final String mavenServerProductProperty = pResource.getMavenProperty(MavenPropertyMode.SERVER_PRODUCT_ID);
    // "main:product_id.property"
    final String mavenServerMainProductProperty = pResource
        .getMavenProperty(MavenPropertyMode.SERVER_MAIN_PRODUCT_ID);
    // "local:product_id.property"
    final String mavenServerLocalProductProperty = pResource
        .getMavenProperty(MavenPropertyMode.SERVER_LOCAL_PRODUCT_ID);
    final String mavenProductProperty = pResource.getMavenProperty(MavenPropertyMode.PRODUCT_ID);
    // Check first if the property is not covered in the launcher project with the syntax
    // "server_id:product_id.property"
    if (BeaverServices.getLauncherService().containsProperty(mavenServerProductProperty))
    {
      propertyFound = BeaverServices.getLauncherService().getProperty(mavenServerProductProperty).toString();
    }
    // "main:product_id.property"
    else if (BeaverServices.getLauncherService().containsProperty(mavenServerMainProductProperty))
    {
      propertyFound = BeaverServices.getLauncherService().getProperty(mavenServerMainProductProperty)
          .toString();
    }
    // "local:product_id.property"
    else if (BeaverServices.getLauncherService().containsProperty(mavenServerLocalProductProperty))
    {
      propertyFound = BeaverServices.getLauncherService().getProperty(mavenServerLocalProductProperty)
          .toString();
    }
    // next with the syntax "product_id.property"
    else if (BeaverServices.getLauncherService().containsProperty(mavenProductProperty))
    {
      propertyFound = BeaverServices.getLauncherService().getProperty(mavenProductProperty).toString();
    }
    else
    {
      // Then, check the parents of current node
      propertyFound = lookupFromParent(pResource);
    }
    return propertyFound;
  }

  private static String lookupFromParent(final Resource pResource) throws BeaverException
  {
    DependencyNode currentNodeSearch = BeaverServices.getCurrentProductProcess().getNode();
    String propertyFound = null;

    while (currentNodeSearch != null)
    {
      final BeaverMavenProject parentProject = BeaverServices.getMojoService().getProjectFromArtifact(
          currentNodeSearch.getArtifact());
      final String mavenProductProperty = pResource.getMavenProperty(MavenPropertyMode.PRODUCT_ID);
      if (parentProject.getMavenProject().getProperties().containsKey(mavenProductProperty))
      {
        propertyFound = parentProject.getMavenProject().getProperties().get(mavenProductProperty).toString();
      }
      currentNodeSearch = currentNodeSearch.getParent();
    }
    return propertyFound;
  }

  /**
   * Replace product reference to concret product id
   * 
   * @param pRessourceToken
   *          the resource token
   * @param pResource
   *          the original resource
   * @return resource with concret productid
   * @throws BeaverException
   */
  private static String cleanToken(final String pRessourceToken, final Resource pResource)
      throws BeaverException
  {
    String ressourceTokenClean = pRessourceToken;
    if (ressourceTokenClean != null)
    {
      try
      {
        final Pattern patern = Pattern.compile(TOKEN_PATTERN);
        final Matcher matcher = patern.matcher(ressourceTokenClean);

        while (matcher.find())
        {
          final String token = matcher.group();

          // Replace local server by resource server id
          String cleanToken = token.replace(Resource.LOCAL_SERVER + Resource.SERVER_SEPARATOR,
              pResource.getServerId() + Resource.SERVER_SEPARATOR);

          // Replace product token by resource product id
          cleanToken = cleanToken.replace(Resource.PRODUCT + Resource.PRODUCT_SEPARATOR,
              pResource.getProductId() + Resource.PRODUCT_SEPARATOR);

          // Add server id if missing
          if (cleanToken.indexOf(Resource.SERVER_SEPARATOR) == -1)
          {
            final String resourceId = cleanToken.substring(2, cleanToken.length() - 1);
            final String resourceIdWithServer = new StringBuilder(pResource.getServerId())
                .append(Resource.SERVER_SEPARATOR).append(resourceId).toString();
            cleanToken = cleanToken.replace(resourceId, resourceIdWithServer);
          }
          ressourceTokenClean = ressourceTokenClean.replaceAll(Pattern.quote(token), cleanToken);
        }
      }
      catch (final PatternSyntaxException e)
      {
        throw new BeaverException("Patern used is not correctly written.", e);
      }
    }
    return ressourceTokenClean;
  }

  /**
   * Build the full property in resolving all token (@{product.key}) and sub-property
   * 
   * @param pResource
   *          the property you want to build
   * @return the full property
   */
  private static String getRelatedResource(final String pResource) throws BeaverException
  {
    String resolveResource = pResource;
    if (resolveResource != null)
    {
      try
      {
        final Pattern patern = Pattern.compile(TOKEN_PATTERN);
        final Matcher matcher = patern.matcher(pResource);

        if (matcher.find())
        {
          final String token = matcher.group();
          final String tokenRessource = pResource.substring(matcher.start() + 2, matcher.end() - 1);

          final Resource resource = new ResourceImpl(tokenRessource);
          if (resource.isCorrectFormat())
          {
            final String resourceTokenAnswer = lookupResource(resource);
            final String ressourceTokenCleanup = ResourcesFacade.cleanToken(resourceTokenAnswer, resource);
            if (ressourceTokenCleanup != null)
            {
              final String resourceAnswer = pResource.replaceAll(Pattern.quote(token), ressourceTokenCleanup);
              resolveResource = getRelatedResource(resourceAnswer);
            }
            else
            {
              throw new BeaverException(String.format("The ressource [%s] wasn't found.", resource));
            }
          }
        }
      }
      catch (final PatternSyntaxException e)
      {
        throw new BeaverException("Patern used is not correctly written.", e);
      }
    }
    return resolveResource;
  }

  /**
   * It will look for the previous value of the property inside the deployment context file.
   * 
   * @param pPreviousRessource
   *          represents the name of the old ressource .
   * @return the ressource found after the search
   * @throws BeaverException
   */
  public static String resolvePreviousRessource(final Resource pPreviousRessource) throws BeaverException
  {
    final String ressourceToken = ResourcesFacade.lookupPreviousProperty(pPreviousRessource);
    final String ressourceCleanup = ResourcesFacade.cleanToken(ressourceToken, pPreviousRessource);
    return ResourcesFacade.getRelatedPreviousProperty(ressourceCleanup);

  }

  /**
   * <p>
   * Look for a previous property asked in the following steps
   * <ul>
   * <li>Get the property from the persistence file</li>
   * </ul>
   * </p>
   * 
   * @param pResource
   *          the property you want to look for
   * @return the resultat
   */
  private static String lookupPreviousProperty(final Resource pResource) throws BeaverException
  {
    if (pResource.isCorrectFormat() == false)
    {
      throw new BeaverException("The format of " + pResource
          + " is incorrect. It should be *productId.property*.");
    }

    String ressourceFound = null;
    if (pResource.isProductRelated())
    {
      String productId = pResource.getProductId();
      if (pResource.isCurrentProductRelated() == true)
      {
        productId = BeaverServices.getCurrentProductProcess().getProductId();
      }
      if (BeaverServices.getBackDeploymentContext().existProductProperty(pResource.getServerId(), productId,
          pResource.getKey()))
      {
        ressourceFound = BeaverServices.getBackDeploymentContext().getProductProperty(
            pResource.getServerId(), productId, pResource.getKey());
      }
    }
    else
    {
      final boolean existingProperty = BeaverServices.getBackDeploymentContext().existServerProperty(
          pResource.getServerId(), pResource.getKey());
      if (existingProperty)
      {
        ressourceFound = BeaverServices.getBackDeploymentContext().getServerProperty(pResource.getServerId(),
            pResource.getKey());
      }
    }

    return ressourceFound;
  }

  /**
   * Build the full resource in resolving all token (@{product.key}) and sub-property using only persistence
   * file's
   * backup
   * 
   * @param pResource
   *          the resource you want to build
   * @return the full property
   */
  private static String getRelatedPreviousProperty(final String pResource) throws BeaverException
  {
    String newProperty = pResource;
    if (newProperty != null)
    {
      try
      {
        final Pattern patern = Pattern.compile(TOKEN_PATTERN);
        final Matcher matcher = patern.matcher(pResource);

        if (matcher.find())
        {
          final String token = matcher.group();
          final String tokenRessource = pResource.substring(matcher.start() + 2, matcher.end() - 1);

          final Resource resource = new ResourceImpl(tokenRessource);
          final String resourceTokenAnswer = lookupPreviousProperty(resource);
          final String ressourceTokenCleanup = ResourcesFacade.cleanToken(resourceTokenAnswer, resource);
          if (ressourceTokenCleanup != null)
          {
            final String resourceAnswer = pResource.replaceAll(Pattern.quote(token), ressourceTokenCleanup);
            newProperty = getRelatedPreviousProperty(resourceAnswer);
          }
          else
          {
            throw new BeaverException(String.format("The ressource [%s] wasn't found.", resource));
          }
        }
      }
      catch (final PatternSyntaxException e)
      {
        throw new BeaverException("Patern used is not correctly written.", e);
      }
    }
    return newProperty;
  }

  /**
   * It will look for the previous value of the property inside the deployment persistence file.
   * 
   * @param pPreviousRessource
   *          represents the name of the old ressource .
   * @return the ressource found after the search
   * @throws BeaverException
   */
  public static String getPreviousRessourcesForProduct(final String pPreviousRessource)
      throws BeaverException
  {
    final Resource property = new ResourceImpl(pPreviousRessource);
    String ressourceFound = "";
    if (BeaverServices.getCurrentProductProcess().getPreviousProperties().containsKey(property.getKey()))
    {
      ressourceFound = BeaverServices.getCurrentProductProcess().getPreviousProperties()
          .get(property.getKey());
    }
    return ressourceFound;
  }

}
