/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.dao;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.entity.NodeEntity;
import org.novaforge.forge.core.organization.entity.ProjectApplicationEntity;
import org.novaforge.forge.core.organization.entity.SpaceEntity;
import org.novaforge.forge.core.organization.entity.TemplateApplicationEntity;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.NodeType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JPA2 implementation of {@link NodeDAO}
 *
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see NodeEntity
 * @see SpaceEntity
 * @see ProjectApplicationEntity
 * @see TemplateApplicationEntity
 */
public class NodeDAOImpl implements NodeDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  /**********************************************************
   * The following methods manage Node data
   **********************************************************/
  /**
   * @param pNodeType
   * @return
   */
  @Override
  public Node newNode(final NodeType pNodeType)
  {
    Node node = null;
    if (pNodeType != null)
    {
      switch (pNodeType)
      {
        case TemplateApplication:
          node = new TemplateApplicationEntity();
          break;
        case ProjectApplication:
          node = new ProjectApplicationEntity();
          break;
        case Space:
          node = new SpaceEntity();
          break;

        default:
          break;
      }
    }
    return node;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node update(final Node pNode)
  {
    entityManager.merge(pNode);
    entityManager.flush();
    return pNode;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node findByUri(final String pUri)
  {
    final TypedQuery<Node> q = entityManager.createNamedQuery("NodeEntity.findByUri", Node.class);
    q.setParameter("uri", pUri);
    return q.getSingleResult();
  }

  /**********************************************************
   * The following methods manage Space data
   **********************************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Space> findByProjectElement(final String pElementId)
  {
    final TypedQuery<Space> q = entityManager.createNamedQuery("SpaceEntity.findByProjectElementId",
        Space.class);
    q.setParameter("elementId", pElementId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Space, List<Application>> findSpacesWithAppsForElementId(final String pElementId)
  {
    final TypedQuery<SpaceEntity> q = entityManager.createNamedQuery("SpaceEntity.findByProjectElementId",
        SpaceEntity.class);
    // Needed to retrieve also project's image on this request
    final OpenJPAQuery<SpaceEntity> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("space_applications");
    q.setParameter("elementId", pElementId);
    final List<SpaceEntity> resultList = q.getResultList();
    final Map<Space, List<Application>> returnMap = new HashMap<Space, List<Application>>();
    for (final SpaceEntity spaceEntity : resultList)
    {
      returnMap.put(spaceEntity, spaceEntity.getApplications());
    }
    return returnMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Application> findAppsForSpace(final String pUri)
  {
    final TypedQuery<Application> q = entityManager.createNamedQuery("SpaceEntity.findAppsBySpace",
        Application.class);
    q.setParameter("uri", pUri);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space findSpaceForApp(final String pUri)
  {
    final TypedQuery<Space> q = entityManager.createNamedQuery("SpaceEntity.findSpaceByApp", Space.class);
    q.setParameter("uri", pUri);
    return q.getSingleResult();
  }

  /**********************************************************
   * The following methods manage ProjectApplication data
   **********************************************************/

  /**
   * {@inheritDoc}
   */
  @Override
  public Space update(final Space pSpace)
  {
    entityManager.merge(pSpace);
    entityManager.flush();
    return pSpace;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication findByInstanceId(final UUID pUUID)
  {
    final TypedQuery<ProjectApplication> q = entityManager.createNamedQuery("ProjectApplicationEntity.findByInstance",
                                                                            ProjectApplication.class);
    q.setParameter("instance", pUUID.toString());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateApplicationStatus(final ApplicationStatus pApplicationStatus, final UUID pInstanceID)
  {
    final TypedQuery<ProjectApplication> q = entityManager.createNamedQuery("ProjectApplicationEntity.findByInstance",
                                                                            ProjectApplication.class);
    q.setParameter("instance", pInstanceID.toString());
    final ProjectApplication app = q.getSingleResult();
    app.setStatus(pApplicationStatus);
    final ProjectApplication merge = entityManager.merge(app);
    entityManager.flush();

    final OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
    oem.evict(merge); // will evict from data cache also
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication update(final ProjectApplication pProjectApplication)
  {
    entityManager.merge(pProjectApplication);
    entityManager.flush();
    return pProjectApplication;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Application addApplicationToSpace(final String pSpaceUri, final Application pApplication)
      throws SpaceServiceException
  {
    if ((pApplication.getUri() == null) || ("".equals(pApplication.getUri())))
    {
      pApplication.setUri(buildUri(pSpaceUri, pApplication.getName()));
    }
    final Node node = findByUri(pSpaceUri);
    if (!NodeType.Space.equals(getNodeType(node)))
    {
      throw new SpaceServiceException(ExceptionCode.ERR_MANAGE_CHILD_FROM_APPLICATION_NODE, String.format(
          "parentNodeUri=%s, childNode=%s", pSpaceUri, pApplication.getUri()));
    }
    final SpaceEntity space = (SpaceEntity) node;

    // add the child node
    space.addApplication(pApplication);

    // persist it
    entityManager.merge(space);
    entityManager.flush();
    final OpenJPAEntityManager oem = OpenJPAPersistence.cast(entityManager);
    oem.evict(space); // will evict from data cache also

    return pApplication;
  }

  @Override
  public void deleteApplication(final String pSpaceUri, final String pApplicationUri)
      throws SpaceServiceException, ApplicationServiceException
  {
    final Node spaceNode = findByUri(pSpaceUri);
    if (!NodeType.Space.equals(getNodeType(spaceNode)))
    {
      throw new SpaceServiceException(ExceptionCode.ERR_MANAGE_CHILD_FROM_APPLICATION_NODE, String.format(
          "Parent node uri=%s is not a space node.", pSpaceUri));
    }

    // find the parent space entity
    final SpaceEntity space = (SpaceEntity) spaceNode;

    // find the application node
    final Node appNode = findByUri(pApplicationUri);
    if ((!NodeType.ProjectApplication.equals(getNodeType(appNode))) && (!NodeType.TemplateApplication
                                                                             .equals(getNodeType(appNode))))
    {
      throw new ApplicationServiceException(ExceptionCode.ERR_MANAGE_CHILD_FROM_APPLICATION_NODE,
          String.format("Parent node uri=%s is not a application node.", pSpaceUri));
    }
    final Application applicationNode = (Application) appNode;

    // add the child node
    space.removeApplication(applicationNode);

    // persist it
    entityManager.merge(space);
    entityManager.flush();
  }

  /**
   * Will build an uri for node
   *
   * @param pParent
   *          parent node uri
   * @param pName
   *          node space
   * @return uri built
   */
  @Override
  public String buildUri(final String pParent, final String pName)
  {
    return pParent + "/" + pName;
  }

  /**
   * {@inheritDoc}
   *
   * @throws SpaceServiceException
   */
  @Override
  public void moveApplication(final String pFromSpaceUri, final String pToSpaceUri,
      final String pApplicationUri) throws ApplicationServiceException, SpaceServiceException
  {
    final Node fromSpaceNode = findByUri(pFromSpaceUri);
    if (!NodeType.Space.equals(getNodeType(fromSpaceNode)))
    {
      throw new SpaceServiceException(ExceptionCode.ERR_MANAGE_CHILD_FROM_APPLICATION_NODE, String.format(
          "From Parent node uri=%s is not a space node.", pFromSpaceUri));
    }

    // find the from parent space entity
    final SpaceEntity fromSpace = (SpaceEntity) fromSpaceNode;

    final Node toSpaceNode = findByUri(pToSpaceUri);
    if (!NodeType.Space.equals(getNodeType(toSpaceNode)))
    {
      throw new SpaceServiceException(ExceptionCode.ERR_MANAGE_CHILD_FROM_APPLICATION_NODE, String.format(
          "to Parent node uri=%s is not a space node.", pToSpaceUri));
    }

    // find the from parent space entity
    final SpaceEntity toSpace = (SpaceEntity) toSpaceNode;

    // find the application node
    final Node appNode = findByUri(pApplicationUri);
    if ((!NodeType.ProjectApplication.equals(getNodeType(appNode))) && (!NodeType.TemplateApplication
                                                                             .equals(getNodeType(appNode))))
    {
      throw new ApplicationServiceException(ExceptionCode.ERR_MANAGE_CHILD_FROM_APPLICATION_NODE,
          String.format("node uri=%s is not a application node.", pApplicationUri));
    }
    final Application applicationNode = (Application) appNode;

    // add the app to the target space
    toSpace.addApplication(applicationNode);

    // persist it
    entityManager.merge(toSpace);
    entityManager.flush();

    // remove the app to the from space
    fromSpace.removeApplication(applicationNode);

    // persist it
    entityManager.merge(fromSpace);
    entityManager.flush();

    // update the new uri of the application
    appNode.setUri(buildUri(pToSpaceUri, appNode.getName()));
    entityManager.merge(appNode);
    entityManager.flush();
  }

  /**
   * This method returns the node type of the node referenced
   *
   * @param pNode
   *     node
   *
   * @return {@link NodeType}
   */
  private NodeType getNodeType(final Node pNode)
  {

    NodeType type = null;
    if (pNode instanceof ProjectApplication)
    {
      type = NodeType.ProjectApplication;
    }
    if (pNode instanceof TemplateApplication)
    {
      type = NodeType.TemplateApplication;
    }
    else if (pNode instanceof Space)
    {
      type = NodeType.Space;
    }

    return type;
  }
}
