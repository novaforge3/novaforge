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
package org.novaforge.forge.ui.distribution.client.presenter.reporting;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.distribution.client.Common;
import org.novaforge.forge.ui.distribution.client.DistributionEntryPoint;
import org.novaforge.forge.ui.distribution.client.helper.AbstractDistributionRPCCall;
import org.novaforge.forge.ui.distribution.client.presenter.Presenter;
import org.novaforge.forge.ui.distribution.client.view.reporting.ReportingView;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ForgeViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.OrganizationViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.ProfilViewDTO;
import org.novaforge.forge.ui.distribution.shared.DTO.reporting.UpdatedViewDTO;
import org.novaforge.forge.ui.distribution.shared.enumeration.StatusDataAccessEnum;
import org.novaforge.forge.ui.distribution.shared.enumeration.ViewTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BILET-JC
 */
public class ReportingPresenter implements Presenter
{

  private final ReportingView display;
  private ViewTypeEnum        viewType;
  private boolean             createdColumn;

  /**
   * @param pRpcService
   * @param pEventBus
   * @param pDisplay
   */
  public ReportingPresenter(final ReportingView pDisplay)
  {
    super();
    display = pDisplay;
    createdColumn = false;
    bind();
  }

  private void bind()
  {
    display.getViewType().addChangeHandler(new ChangeHandler()
    {
      @Override
      public void onChange(final ChangeEvent event)
      {
        viewType(display.getViewType().getValue(display.getViewType().getSelectedIndex()));
      }
    });

  }

  /**
   * This method indicates which kind of view type the user wants to see
   *  return new ForgeViewDTO(pForgeView.getForgeName(), pForgeView.getNumberProject(),
        pForgeView.getNumberAccount(), convertStatusDataAccessEnum(pForgeView.getStatus()));
   * @param pViewType
   */
  private void viewType(final String pViewType)
  {
    if (ViewTypeEnum.FORGE.getLabel().equals(pViewType))
    {
      viewType = ViewTypeEnum.FORGE;
      getIndicatorsByForgeView();
    }
    else if (ViewTypeEnum.ORGANIZATION.getLabel().equals(pViewType))
    {
      viewType = ViewTypeEnum.ORGANIZATION;
      getIndicatorsByOrganizationView();
    }
    else if (ViewTypeEnum.PROFIL.getLabel().equals(pViewType))
    {
      viewType = ViewTypeEnum.PROFIL;
      getIndicatorsByProfilView();
    }
    display.selectView(viewType);
  }

  private void getIndicatorsByForgeView()
  {
    new AbstractDistributionRPCCall<List<ForgeViewDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<ForgeViewDTO>> pCb)
      {
        DistributionEntryPoint.getServiceAsync().getIndicatorsByForgeView(pCb);
      }

      @Override
      public void onFailure(final Throwable pCaught)
      {
        Common.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(final List<ForgeViewDTO> pResult)
      {
        if (pResult != null)
        {
          refreshForgeView(pResult);
        }
      }



    }.retry(0);

  }

  private void getIndicatorsByOrganizationView()
  {
    new AbstractDistributionRPCCall<List<OrganizationViewDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<OrganizationViewDTO>> pCb)
      {
        DistributionEntryPoint.getServiceAsync().getIndicatorsByOrganizationView(pCb);
      }

      @Override
      public void onSuccess(final List<OrganizationViewDTO> pResult)
      {
        if (pResult != null)
        {
          refreshOrganizationView(pResult);
        }
      }

      @Override
      public void onFailure(final Throwable pCaught)
      {
        Common.displayErrorMessage(pCaught);
      }

    }.retry(0);

  }

  private void getIndicatorsByProfilView()
  {
    new AbstractDistributionRPCCall<List<ProfilViewDTO>>()
    {
      @Override
      protected void callService(final AsyncCallback<List<ProfilViewDTO>> pCb)
      {
        DistributionEntryPoint.getServiceAsync().getIndicatorsByProfilView(pCb);
      }

      @Override
      public void onSuccess(final List<ProfilViewDTO> pResult)
      {
        if (pResult != null)
        {
          refreshProfilView(pResult);
        }
      }

      @Override
      public void onFailure(final Throwable pCaught)
      {
        Common.displayErrorMessage(pCaught);
      }

    }.retry(0);

  }

  private void refreshForgeView(final List<ForgeViewDTO> pList)
  {
    boolean noDistribution = false;

    List<UpdatedViewDTO> updatedDTOList = new ArrayList<UpdatedViewDTO>();

    if (pList.size() != 0)
    {
      Integer numberProject = 0;
      Integer numberAccount = 0;
      for (final ForgeViewDTO forgeViewDTO : pList)
      {
    	/**
       * Added to view last updated forge
       */
    	UpdatedViewDTO updatedDTO = new  UpdatedViewDTO(forgeViewDTO.getForgeName(),forgeViewDTO.getLastUpdated());
        updatedDTOList.add(updatedDTO);

        if (StatusDataAccessEnum.DISTRIBUTION_DOWN.equals(forgeViewDTO.getStatus()))
        {
          noDistribution = true;
        }
        numberProject += forgeViewDTO.getNumberProject();
        numberAccount += forgeViewDTO.getNumberAccount();
      }
      pList.add(new ForgeViewDTO(Common.MESSAGES.total().toUpperCase(), numberProject, numberAccount, null));
    }
    if (noDistribution)
    {
      display.noDistributionPanel();
    }
    display.getViewTypeTitle().setText(Common.MESSAGES.forgeViewTypeTitle());
    display.getForgeViewDataProvider().setList(pList);
    display.updateForgeViewSortHandler();
    display.getUpdatedViewDataProvider().setList(updatedDTOList);
  }

  private void refreshOrganizationView(final List<OrganizationViewDTO> pList)
  {
    boolean noDistribution = false;
    if (pList.size() != 0)
    {
      Integer numberProject = 0;
      for (final OrganizationViewDTO organizationViewDTO : pList)
      {
        if (StatusDataAccessEnum.DISTRIBUTION_DOWN.equals(organizationViewDTO.getStatus()))
        {
          noDistribution = true;
        }
        numberProject += organizationViewDTO.getNumberProject();
      }
      pList.add(new OrganizationViewDTO(Common.MESSAGES.total(), numberProject, null));
    }
    if (noDistribution)
    {
      display.noDistributionPanel();
    }
    display.getViewTypeTitle().setText(Common.MESSAGES.organizationViewTypeTitle());
    display.getOrganizationViewDataProvider().setList(pList);
    display.updateForgeViewSortHandler();
  }

  private void refreshProfilView(final List<ProfilViewDTO> pList)
  {

    if (pList.size() != 0)
    {
      if (!createdColumn)
      {
        display.createRolesColumn(pList);
        createdColumn = true;
      }
    }
    display.getViewTypeTitle().setText(Common.MESSAGES.profilViewTypeTitle());
    display.getProfilViewDataProvider().setList(pList);
    display.updateProfilViewSortHandler();
  }

  @Override
  public void go(final HasWidgets container)
  {
    container.clear();
    container.add(display.asWidget());
    refreshPresenter();

  }

  public void refreshPresenter()
  {
    display.getViewType().setSelectedIndex(0);
    viewType(ViewTypeEnum.FORGE.getLabel());

  }

  public IsWidget getDisplay()
  {
    return display;
  }

}
