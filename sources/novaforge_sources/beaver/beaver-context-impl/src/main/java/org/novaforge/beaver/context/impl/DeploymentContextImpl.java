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
package org.novaforge.beaver.context.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.novaforge.beaver.context.DeploymentContext;
import org.novaforge.beaver.context.ServerType;
import org.novaforge.beaver.context.oxm.MarshallerProvider;
import org.novaforge.beaver.context.oxm.marshaller.Contexts;
import org.novaforge.beaver.context.oxm.marshaller.Deployment;
import org.novaforge.beaver.context.oxm.marshaller.Product;
import org.novaforge.beaver.context.oxm.marshaller.Products;
import org.novaforge.beaver.context.oxm.marshaller.Properties;
import org.novaforge.beaver.context.oxm.marshaller.Server;
import org.novaforge.beaver.context.oxm.marshaller.Status;
import org.novaforge.beaver.context.oxm.marshaller.Type;
import org.novaforge.beaver.exception.BeaverException;

/**
 * Default implementation of {@link DeploymentContext}
 * 
 * @author Guillaume Lamirand
 * @version 2.0
 */
public class DeploymentContextImpl implements DeploymentContext
{
  private final Deployment deploymentOxm;
  private final String     pathFile;

  public DeploymentContextImpl(final String pFilePath) throws Exception
  {
    final File deploymentFile = new File(pFilePath);
    if (deploymentFile.exists())
    {
      deploymentOxm = MarshallerProvider.loadObjectFromXML(Deployment.class, pFilePath);
    }
    else
    {
      deploymentOxm = new Deployment();
    }
    pathFile = pFilePath;
  }

  public DeploymentContextImpl(final DeploymentContextImpl pDeploymentContext) throws Exception
  {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    MarshallerProvider.writeObjectToXML(Deployment.class, pDeploymentContext.deploymentOxm, outputStream);
    final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    deploymentOxm = MarshallerProvider.loadObjectFromXML(Deployment.class, inputStream);

    pathFile = pDeploymentContext.pathFile;
  }

  @Override
  public String getMainServerId()
  {
    String serverIdFound = null;
    final List<Server> servers = deploymentOxm.getServerList();
    if (servers != null)
    {
      for (final Server server : servers)
      {
        if (Type.MAIN.equals(server.getType()))
        {
          serverIdFound = server.getId();
          break;
        }
      }
    }
    return serverIdFound;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addServer(final String pServerId, final ServerType pType) throws BeaverException
  {
    final List<Server> servers = deploymentOxm.getServerList();

    if (existServer(pServerId) == false)
    {
      final Server server = new Server();
      server.setId(pServerId);
      Type type = Type.SIMPLE;
      if (pType != null)
      {
        type = Type.convert(pType.getValue());
        final String mainServerId = getMainServerId();
        if ((mainServerId != null) && (Type.MAIN.equals(type)) && (mainServerId.equals(pServerId) == false))
        {
          throw new BeaverException(
              "A main server is already defined in the deployment context, you cannot set another one");
        }
      }
      server.setType(type);
      final Properties properties = new Properties();
      server.setProperties(properties);
      final Products products = new Products();
      server.setProducts(products);
      servers.add(server);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeServer(final String pServerId)
  {
    final List<Server> servers = deploymentOxm.getServerList();
    final Server server = getServer(pServerId);

    if (server != null)
    {
      servers.remove(server);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existServer(final String pServerId)
  {
    boolean serverExisting = false;
    final Server server = getServer(pServerId);

    if (server != null)
    {
      serverExisting = true;
    }
    return serverExisting;
  }

  private Server getServer(final String pServerId)
  {
    Server serverFound = null;
    if (pServerId != null)
    {
      final List<Server> servers = deploymentOxm.getServerList();
      for (final Server server : servers)
      {
        if (server.getId().equals(pServerId))
        {
          serverFound = server;
          break;
        }
      }
    }
    return serverFound;
  }

  private List<Product> getServerProducts(final String pServerId)
  {
    List<Product> returnList = null;
    final Server server = getServer(pServerId);
    if (server != null)
    {
      Products products = server.getProducts();
      if (products == null)
      {
        products = new Products();
        server.setProducts(products);
      }
      returnList = products.getProductList();
    }
    return returnList;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServerProperty(final String pServerId, final String pPropertyKey)
  {
    String returnValue = null;
    final Server server = getServer(pServerId);
    if (server != null)
    {
      final ListWrapper wrapContexts = ListWrapper.wrapProperties(server.getProperties());
      returnValue = wrapContexts.get(pPropertyKey);
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addServerProperty(final String pServerId, final String pPropertyKey, final String pPropertyValue)
  {
    final Server server = getServer(pServerId);
    if (server != null)
    {
      if (server.getProperties() == null)
      {
        final Properties properties = new Properties();
        server.setProperties(properties);
      }
      final ListWrapper wrapContexts = ListWrapper.wrapProperties(server.getProperties());
      wrapContexts.put(pPropertyKey, pPropertyValue);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existServerProperty(final String pServerId, final String pPropertyKey)
  {
    boolean propertyExisting = false;
    final Server server = getServer(pServerId);
    if ((server != null) && (server.getProperties() != null))
    {
      final ListWrapper wrapContexts = ListWrapper.wrapProperties(server.getProperties());
      propertyExisting = wrapContexts.exist(pPropertyKey);
    }
    return propertyExisting;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addProduct(final String pServerId, final String pProductId, final String pVersion)
  {
    Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      product.setVersion(pVersion);
    }
    else
    {
      final List<Product> productslist = getServerProducts(pServerId);
      if (productslist != null)
      {
        product = new Product();
        product.setId(pProductId);
        product.setStatus(Status.NEW);
        product.setUpdateVersion("0");
        product.setVersion(pVersion);
        final Properties properties = new Properties();
        final Contexts contexts = new Contexts();
        product.setProperties(properties);
        product.setContexts(contexts);
        productslist.add(product);
      }
    }
  }

  private Product getProduct(final String pServerId, final String pProductId)
  {
    Product productFound = null;
    final List<Product> productslist = getServerProducts(pServerId);
    if (productslist != null)
    {
      for (final Product product : productslist)
      {
        if (product.getId().equals(pProductId))
        {
          productFound = product;
          break;
        }
      }
    }
    return productFound;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeProduct(final String pServerId, final String pProductId)
  {
    final List<Product> productslist = getServerProducts(pServerId);
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      productslist.remove(product);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existProduct(final String pServerId, final String pProductId)
  {
    final Product product = getProduct(pServerId, pProductId);

    return product != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductVersion(final String pServerId, final String pProductId)
  {
    String version = null;
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      version = product.getVersion();
    }
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProductVersion(final String pServerId, final String pProductId, final String pVersion)
  {
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      product.setVersion(pVersion);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProductStatusInstalling(final String pServerId, final String pProductId)
  {
    return Status.INSTALLING.xmlValue().equals(getProductStatus(pServerId, pProductId));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProductStatusInstalled(final String pServerId, final String pProductId)
  {
    return Status.INSTALLED.xmlValue().equals(getProductStatus(pServerId, pProductId));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductStatus(final String pServerId, final String pProductId)
  {
    String productStatus = null;
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      productStatus = product.getStatus().xmlValue();
    }
    return productStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProductStatusInstalling(final String pServerId, final String pProductId)
  {
    setProductStatus(pServerId, pProductId, Status.INSTALLING);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProductStatusInstalled(final String pServerId, final String pProductId)
  {
    setProductStatus(pServerId, pProductId, Status.INSTALLED);
  }

  private void setProductStatus(final String pServerId, final String pProductId, final Status pStatus)
  {
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      product.setStatus(pStatus);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductUpdateVersion(final String pServerId, final String pProductId)
  {
    String updateVersion = null;
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      updateVersion = product.getUpdateVersion();
    }
    return updateVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProductUpdateVersion(final String pServerId, final String pProductId, final String pVersion)
  {
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      product.setUpdateVersion(pVersion);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addProductContext(final String pServerId, final String pProductId, final String pContextKey,
      final String pContextValue)
  {
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      if (product.getContexts() == null)
      {
        final Contexts contexts = new Contexts();
        product.setContexts(contexts);
      }
      final ListWrapper wrapContexts = ListWrapper.wrapContexts(product.getContexts());
      wrapContexts.put(pContextKey, pContextValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductContext(final String pServerId, final String pProductId, final String pContextKey)
  {
    String contextValue = null;

    final Product product = getProduct(pServerId, pProductId);

    if ((product != null) && (product.getContexts() != null))
    {
      final ListWrapper wrapContexts = ListWrapper.wrapContexts(product.getContexts());
      contextValue = wrapContexts.get(pContextKey);
    }

    return contextValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existProductContext(final String pServerId, final String pProductId, final String pContextKey)
  {
    boolean contextExisting = false;
    final Product product = getProduct(pServerId, pProductId);
    if ((product != null) && (product.getContexts() != null))
    {
      final ListWrapper wrapContexts = ListWrapper.wrapContexts(product.getContexts());
      contextExisting = wrapContexts.exist(pContextKey);
    }
    return contextExisting;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addProductProperty(final String pServerId, final String pProductId, final String pPropertyKey,
      final String pPropertyValue)
  {
    final Product product = getProduct(pServerId, pProductId);
    if (product != null)
    {
      if (product.getProperties() == null)
      {
        final Properties properties = new Properties();
        product.setProperties(properties);
      }
      final ListWrapper wrapContexts = ListWrapper.wrapProperties(product.getProperties());
      wrapContexts.put(pPropertyKey, pPropertyValue);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductProperty(final String pServerId, final String pProductId, final String pPropertyKey)
  {
    String propertyValue = null;
    final Product product = getProduct(pServerId, pProductId);

    if ((product != null) && (product.getProperties() != null)
        && (product.getProperties().getPropertyList() != null))
    {
      final ListWrapper wrapContexts = ListWrapper.wrapProperties(product.getProperties());
      propertyValue = wrapContexts.get(pPropertyKey);
    }
    return propertyValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existProductProperty(final String pServerId, final String pProductId,
      final String pPropertyKey)
  {
    boolean propertyExisting = false;
    final Product product = getProduct(pServerId, pProductId);
    if ((product != null) && (product.getProperties() != null)
        && (product.getProperties().getPropertyList() != null))
    {
      final ListWrapper wrapContexts = ListWrapper.wrapProperties(product.getProperties());
      propertyExisting = wrapContexts.exist(pPropertyKey);
    }
    return propertyExisting;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeFile() throws Exception
  {
    final File deploymentFile = new File(pathFile);
    if (deploymentFile.exists() == false)
    {
      if (deploymentFile.getParentFile().exists() == false)
      {
        final boolean succeedDir = deploymentFile.getParentFile().mkdirs();
        if (succeedDir == false)
        {
          throw new IOException("Fail to create directory used to write persistence file.");
        }
      }
      final boolean succeedFile = deploymentFile.createNewFile();
      if (succeedFile == false)
      {
        throw new IOException("Fail to create new persistence.");
      }
    }
    MarshallerProvider.writeObjectToXML(Deployment.class, deploymentOxm, pathFile);

  }

}
