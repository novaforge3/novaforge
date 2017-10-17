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
package org.novaforge.forge.dashboard.internal.service;

import com.google.common.base.Strings;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.entity.BinaryFileEntity;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.dashboard.dao.DashBoardDAO;
import org.novaforge.forge.dashboard.internal.model.DashBoardResourceImpl;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.service.DashBoardService;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link DashBoardService}
 * 
 * @author Guillaume Lamirand
 */
public class DashBoardServiceImpl implements DashBoardService
{

  /**
   * Default tab layout
   */
  private static final String DEFAULT_TAB_LAYOUT = "two_horizontal";
  /**
   * Default tab name
   */
  private static final String DEFAULT_TAB        = "Dashboard";
  /**
   * {@link DashBoardDAO} implementation injected by container
   */
  private DashBoardDAO        dashBoardDAO;
  /**
   * {@link securityDelegate} implementation injected by container
   */
  private SecurityDelegate    securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public DashBoard getDashBoard(final Type pType, final String pTypeId) throws NoResultException
  {
    if (Type.PROJECT.equals(pType))
    {
      securityDelegate.checkResource(pTypeId, new DashBoardResourceImpl(PermissionAction.READ));
    }
    DashBoard dashboard;
    try
    {
      dashboard = dashBoardDAO.findDashboardByType(pType, pTypeId);
    }
    catch (final NoResultException e)
    {
      dashboard = createDefaultDashboard(pType, pTypeId);
    }
    return dashboard;
  }

  private DashBoard createDefaultDashboard(final Type pType, final String pTypeId)
  {
    DashBoard newDashboard = null;
    // Check if current user has correct authorization
    boolean isAuthorized = true;
    if (Type.PROJECT.equals(pType))
    {
      isAuthorized = securityDelegate
          .isPermitted(pTypeId, new DashBoardResourceImpl(PermissionAction.CREATE));
    }
    if (isAuthorized)
    {
      newDashboard = dashBoardDAO.newDashboard(pType, pTypeId);
      final Tab defaultTab = newTab();
      defaultTab.setIndex(0);
      defaultTab.setName(DEFAULT_TAB);
      defaultTab.setLayoutKey(DEFAULT_TAB_LAYOUT);
      newDashboard.addTab(defaultTab);
      dashBoardDAO.persist(newDashboard);
    }
    return newDashboard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab newTab()
  {
    return dashBoardDAO.newTab();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab getTab(final UUID pUUID) throws NoResultException
  {
    return dashBoardDAO.findTabByUUID(pUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget newWidget(final String pWidgetKey)
  {
    final Widget newWidget = dashBoardDAO.newWidget();
    newWidget.setKey(pWidgetKey);
    newWidget.setAreaId(1);
    newWidget.setAreaIndex(0);
    newWidget.setDataSource(null);
    newWidget.setProperties(null);
    return newWidget;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab addTab(final Type pType, final String pTypeId, final Tab pTab)
  {
    if (Type.PROJECT.equals(pType))
    {
      securityDelegate.checkResource(pTypeId, new DashBoardResourceImpl(PermissionAction.UPDATE));
    }
    final DashBoard dashboard = dashBoardDAO.findDashboardByType(pType, pTypeId);
    if (pTab.getIndex() == 0)
    {
      pTab.setIndex(dashboard.getTabs().size());
    }
    else if (pTab.getIndex() != dashboard.getTabs().size())
    {
      final List<Tab> currentTabs = dashboard.getTabs();
      for (final Tab tab : currentTabs)
      {
        final int currentIndex = tab.getIndex();
        if (currentIndex >= pTab.getIndex())
        {
          tab.setIndex(currentIndex + 1);
        }
      }
    }
    if (Strings.isNullOrEmpty(pTab.getLayoutKey()))
    {
      pTab.setLayoutKey(DEFAULT_TAB_LAYOUT);
    }
    dashboard.addTab(pTab);
    dashBoardDAO.update(dashboard);
    return pTab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tab updateTab(final Tab pTab)
  {
    return dashBoardDAO.update(pTab);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTab(final Type pType, final String pTypeId, final UUID pTabUUID)
  {
    if (Type.PROJECT.equals(pType))
    {
      securityDelegate.checkResource(pTypeId, new DashBoardResourceImpl(PermissionAction.UPDATE));
    }
    final DashBoard dashboard = dashBoardDAO.findDashboardByType(pType, pTypeId);
    final Tab mergedTab = dashBoardDAO.findTabByUUID(pTabUUID);
    final List<Tab> currentTabs = dashboard.getTabs();
    if (currentTabs.contains(mergedTab))
    {
      for (final Tab tab : currentTabs)
      {
        final int currentIndex = tab.getIndex();
        if (currentIndex > mergedTab.getIndex())
        {
          tab.setIndex(currentIndex - 1);
        }
      }
      dashboard.removeTab(mergedTab);
      dashBoardDAO.update(dashboard);
      dashBoardDAO.remove(mergedTab);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget addWidget(final UUID pTabUUID, final Widget pWidget)
  {
    // Update area index of existing widget
    dashBoardDAO.increaseIndex(pWidget.getAreaId(), pWidget.getAreaIndex());

    // Retrieve tab
    final Tab tab = dashBoardDAO.findTabByUUID(pTabUUID);
    // Add the new wiget
    tab.addWidget(pWidget);
    dashBoardDAO.update(tab);

    return pWidget;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget updateWidget(final Widget pWidget)
  {
    return dashBoardDAO.update(pWidget);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeWidget(final UUID pTabUUID, final UUID pWidgetUUID)
  {
    // Retrieve tab
    final Tab tab = dashBoardDAO.findTabByUUID(pTabUUID);
    // Retrieve widget
    final Widget widget = dashBoardDAO.findWidgetByUUID(pWidgetUUID);
    tab.removeWidget(widget);
    dashBoardDAO.remove(widget);
    dashBoardDAO.update(tab);

    // Update area index of existing widget
    dashBoardDAO.decreaseIndex(widget.getAreaId(), widget.getAreaIndex());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Widget getWidget(final UUID pUUID) throws NoResultException
  {
    return dashBoardDAO.findWidgetByUUID(pUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BinaryFile newIcon()
  {
    return new BinaryFileEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAdminRight(final Type pType, final String pTypeId)
  {
    // TODO we should manage permission for user dashboard
    boolean hasAdminRight = true;

    if (Type.PROJECT.equals(pType))
    {
      hasAdminRight = securityDelegate.isPermitted(pTypeId, new DashBoardResourceImpl(PermissionAction.ALL));
    }
    return hasAdminRight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveWidget(final Widget pWidget, final int pNewAreaId, final int pNewIndex)
  {
    // Retrieve previous values
    final int previousArea = pWidget.getAreaId();
    final int previousIndex = pWidget.getAreaIndex();

    if (previousArea == pNewAreaId)
    {
      if (previousIndex < pNewIndex)
      {
        dashBoardDAO.decreaseIndexBetween(pNewAreaId, previousIndex, pNewIndex);
      }
      else if (previousIndex > pNewIndex)
      {
        dashBoardDAO.increaseIndexBetween(pNewAreaId, previousIndex, pNewIndex);
      }
    }
    else
    {
      dashBoardDAO.decreaseIndex(previousArea, previousIndex);
      dashBoardDAO.increaseIndex(pNewAreaId, pNewIndex);
    }
    if ((previousArea != pNewAreaId) || (previousIndex != pNewIndex))
    {

      pWidget.setAreaId(pNewAreaId);
      pWidget.setAreaIndex(pNewIndex);
      dashBoardDAO.update(pWidget);
    }

  }

  /**
   * Use by container to inject {@link DashBoardDAO}
   * 
   * @param pDashBoardDAO
   *          the dashBoardDAO to set
   */
  public void setDashBoardDAO(final DashBoardDAO pDashBoardDAO)
  {
    dashBoardDAO = pDashBoardDAO;
  }

  /**
   * Use by container to inject {@link SecurityDelegate}
   * 
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }

}
