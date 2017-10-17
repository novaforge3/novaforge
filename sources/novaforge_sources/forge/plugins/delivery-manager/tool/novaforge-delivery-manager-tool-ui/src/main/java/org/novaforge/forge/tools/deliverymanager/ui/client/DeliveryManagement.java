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
package org.novaforge.forge.tools.deliverymanager.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.GlobalPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.service.DeliveryManagementService;
import org.novaforge.forge.tools.deliverymanager.ui.client.service.DeliveryManagementServiceAsync;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DeliveryManagement implements EntryPoint
{

  private static final String PARAMETER_NAME_INSTANCEID = "instance_id";
  private static boolean                       isDebug;
  private static DeliveryManagement            singleton;
  private final DeliveryManagementServiceAsync deliveryManagementServiceAsync = GWT.create(DeliveryManagementService.class);
  private SimpleEventBus                       eventBus;
  private String                               projectId;
  private boolean                              canEdit;

  public static DeliveryManagement get()
  {
    return singleton;
  }

  public static boolean isDebugMode()
  {
    return isDebug;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onModuleLoad()
  {
    singleton = this;

    final String parameter = Window.Location.getParameter("gwt.codesvr");
    if (parameter != null)
    {
      isDebug = true;
    }
    this.eventBus = new SimpleEventBus();
    final String instanceId = Window.Location.getParameter(PARAMETER_NAME_INSTANCEID);

    new AbstractRPCCall<String>()
    {
      @Override
      protected void callService(final AsyncCallback<String> pCb)
      {
        DeliveryManagement.this.deliveryManagementServiceAsync.getProjectId(instanceId, pCb);
      }

      @Override
      public void onSuccess(final String pResult)
      {
        DeliveryManagement.this.projectId = pResult;
        new AbstractRPCCall<Boolean>()
        {
          @Override
          public void onFailure(final Throwable caught)
          {
            DeliveryCommon.displayErrorMessage(caught);
          }

          @Override
          protected void callService(final AsyncCallback<Boolean> pCb)
          {
            DeliveryManagement.this.deliveryManagementServiceAsync.canEdit(DeliveryManagement.this.projectId,
                pCb);
          }

          @Override
          public void onSuccess(final Boolean pResult)
          {
            DeliveryManagement.this.canEdit = pResult;
            final GlobalPresenter main = new GlobalPresenter();
            main.go();
          }

        }.retry(0);
      }

      @Override
      public void onFailure(final Throwable caught)
      {
        DeliveryCommon.displayErrorMessage(caught);
      }
    }.retry(0);
  }

  public SimpleEventBus getEventBus()
  {
    return this.eventBus;
  }

  public DeliveryManagementServiceAsync getServiceAsync()
  {
    return this.deliveryManagementServiceAsync;
  }

  public String getProjectId()
  {
    return this.projectId;
  }

  public boolean isCanEdit()
  {
    return canEdit;
  }
}
