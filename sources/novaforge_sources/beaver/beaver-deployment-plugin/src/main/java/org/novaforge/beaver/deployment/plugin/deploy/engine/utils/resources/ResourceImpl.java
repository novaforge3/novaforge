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

import java.util.StringTokenizer;

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.deployment.plugin.deploy.services.BeaverServices;
import org.novaforge.beaver.exception.BeaverException;
import org.novaforge.beaver.resource.MavenPropertyMode;
import org.novaforge.beaver.resource.Resource;

/**
 * <b>The class {@link ResourceImpl} wraps a requires resource to find the following information:</b>
 * <ul>
 * <li>the server id</li>
 * <li>the product id</li>
 * <li>the key of the resource</li>
 * </ul>
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class ResourceImpl implements Resource
{

  private String  serverId;
  private String  productId;
  private String  key;
  private boolean format;

  /**
   * Build a {@link Resource} from the full resource value given. It will automaticaly retrieve the server id,
   * the productId and the key.
   * 
   * @param pFullResource
   *          the product id to set
   */
  public ResourceImpl(final String pFullResource)
  {
    if (pFullResource.startsWith(Resource.IGNORE_PREFIX) == false)
    {
      final StringTokenizer tokenProperty = new StringTokenizer(pFullResource, Resource.SERVER_SEPARATOR);
      final int countTokens = tokenProperty.countTokens();
      if (countTokens == 1)
      {
        serverId = Resource.LOCAL_SERVER;
        format = buildResourceInfo(false, tokenProperty.nextToken());
      }
      else if (countTokens == 2)
      {
        serverId = tokenProperty.nextToken();
        format = buildResourceInfo(true, tokenProperty.nextToken());
      }
    }

  }

  private boolean buildResourceInfo(final boolean pHasServer, final String pProductReference)
  {
    boolean isCorrectlyFormated = false;
    final StringTokenizer tokenProperty = new StringTokenizer(pProductReference, Resource.PRODUCT_SEPARATOR);

    final int countTokens = tokenProperty.countTokens();
    if (countTokens == 1)
    {
      if (pHasServer == false)
      {
        productId = Resource.PRODUCT;
      }
      key = tokenProperty.nextToken();
      isCorrectlyFormated = true;
    }
    else if (countTokens == 2)
    {
      productId = tokenProperty.nextToken();
      key = tokenProperty.nextToken();
      isCorrectlyFormated = true;
    }
    return isCorrectlyFormated;
  }

  /**
   * Build a {@link Resource} from a specific productId and a key. The server will be set to
   * {@link Resource#LOCAL_SERVER}
   * 
   * @param pProductId
   *          the product id to set
   * @param pKey
   *          the key of the resource
   */
  public ResourceImpl(final String pProductId, final String pKey)
  {
    this(Resource.LOCAL_SERVER, pProductId, pKey);
  }

  /**
   * Build a {@link Resource} from a server id, a specific productId and a key.
   * 
   * @param pServerId
   *          the server id to set
   * @param pProductId
   *          the product id to set
   * @param pKey
   *          the key of the resource
   */
  public ResourceImpl(final String pServerId, final String pProductId, final String pKey)
  {
    if (StringUtils.isNotEmpty(pServerId))
    {
      serverId = pServerId;
    }
    else
    {
      serverId = Resource.LOCAL_SERVER;
    }
    if (StringUtils.isNotEmpty(pProductId))
    {
      productId = pProductId;
    }
    key = pKey;
    format = true;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws BeaverException
   */
  @Override
  public String getMavenProperty(final MavenPropertyMode pMode) throws BeaverException
  {
    final StringBuilder property = new StringBuilder();
    if (pMode.displayServer())
    {
      if (MavenPropertyMode.FULL_RESOLVED.equals(pMode))
      {
        property.append(getServerId());
      }
      else if (MavenPropertyMode.SERVER_MAIN_PRODUCT_ID.equals(pMode))
      {
        property.append(Resource.MAIN_SERVER);
      }
      else if (MavenPropertyMode.SERVER_LOCAL_PRODUCT_ID.equals(pMode))
      {
        property.append(Resource.LOCAL_SERVER);
      }
      else
      {
        property.append(serverId);
      }
      property.append(Resource.SERVER_SEPARATOR);
    }
    if ((pMode.displayProduct()) && (isProductRelated()))
    {
      if ((MavenPropertyMode.SERVER_PRODUCT.equals(pMode)) || (MavenPropertyMode.PRODUCT.equals(pMode)))
      {
        property.append(Resource.PRODUCT);
      }
      else
      {
        property.append(productId);
      }
      property.append(Resource.PRODUCT_SEPARATOR);
    }
    else if ((pMode.displayProduct() == false) && (isProductRelated()))
    {
      throw new BeaverException(
          "The current resource is product related but you have requested SERVER mode, it is incompatible.");
    }
    property.append(key);
    return property.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServerId()
  {
    String resolvedServerId = serverId;
    if ((isMainServerRelated()) && (BeaverServices.getDeploymentContext() != null))
    {
      resolvedServerId = BeaverServices.getDeploymentContext().getMainServerId();

    }
    else if ((isLocaleServerRelated()) && (BeaverServices.getLauncherService() != null)
        && (BeaverServices.getLauncherService().getServerInfo() != null))
    {
      resolvedServerId = BeaverServices.getLauncherService().getServerInfo().getServerId();

    }
    return resolvedServerId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isServerRelated()
  {
    return isCorrectFormat() && (StringUtils.isNotEmpty(serverId) && (isProductRelated() == false));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLocaleServerRelated()
  {
    return isCorrectFormat() && Resource.LOCAL_SERVER.equals(serverId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMainServerRelated()
  {
    return isCorrectFormat() && Resource.MAIN_SERVER.equals(serverId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductId()
  {
    return productId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProductRelated()
  {
    return isCorrectFormat() && StringUtils.isNotEmpty(productId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCurrentProductRelated()
  {
    return isProductRelated() && Resource.PRODUCT.equals(productId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey()
  {
    return key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCorrectFormat()
  {
    return format;
  }

  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("ResourceImpl [serverId=").append(serverId).append(", productId=").append(productId)
        .append(", key=").append(key).append(", format=").append(format).append("]");
    return builder.toString();
  }

}
