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
package org.novaforge.forge.tools.deliverymanager.ui.client.presenter.tabs;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.helper.AbstractRPCCall;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs.BugsView;
import org.novaforge.forge.tools.deliverymanager.ui.shared.BugTrackerIssueDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.IssueContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.DeliveryManagementServiceException;
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.ExceptionCode;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;

import java.util.List;

public class BugsPresenter implements TabPresenter
{

   private final BugsView display;
   private String         reference;

   public BugsPresenter(final BugsView display)
   {
      super();
      this.display = display;
      this.bind();

   }

   public void bind()
   {
      this.display.getVersionList().addChangeHandler(new ChangeHandler()
      {
         @Override
         public void onChange(final ChangeEvent pEvent)
         {
            BugsPresenter.this.display.getBugsListPanel().setVisible(false);
            BugsPresenter.this.display.getListDataProvider().getList().clear();
            if (BugsPresenter.this.display.getVersionList().getSelectedIndex() != 0)
            {
               BugsPresenter.this.display.getDisplayButton().setEnabled(true);
               BugsPresenter.this.display.getSaveButton().setEnabled(true);
            }
            else
            {
               BugsPresenter.this.display.getDisplayButton().setEnabled(false);
               BugsPresenter.this.display.getSaveButton().setEnabled(false);

            }

         }
      });

      BugsPresenter.this.display.getDisplayButton().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(final ClickEvent pEvent)
         {
            final int selectedIndex = BugsPresenter.this.display.getVersionList().getSelectedIndex();

            BugsPresenter.this.refreshIssues(BugsPresenter.this.display.getVersionList().getItemText(
                  selectedIndex));
         }

      });

      BugsPresenter.this.display.getSaveButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(final ClickEvent pEvent)
         {
            new AbstractRPCCall<Boolean>()
            {
               @Override
               protected void callService(final AsyncCallback<Boolean> pCb)
               {
                  final int selectedIndex = BugsPresenter.this.display.getVersionList().getSelectedIndex();
                  final String version = BugsPresenter.this.display.getVersionList().getItemText(
                        selectedIndex);
                  DeliveryManagement
                        .get()
                        .getServiceAsync()
                        .updateBugsVersion(DeliveryManagement.get().getProjectId(),
                              BugsPresenter.this.reference, version, pCb);
               }

               @Override
               public void onFailure(final Throwable caught)
               {
                  DeliveryCommon.displayErrorMessage(caught);
               }

               @Override
               public void onSuccess(final Boolean pResult)
               {
                  if (pResult)
                  {
                     final InfoDialogBox box = new InfoDialogBox(DeliveryCommon.getMessages()
                           .versionSaveSuccess(), InfoTypeEnum.SUCCESS, 2500);

                     box.show();
                  }
               }


            }.retry(0);

         }

      });
   }

   private void refreshIssues(final String pVersion)
   {
      new AbstractRPCCall<List<BugTrackerIssueDTO>>()
      {
         @Override
         protected void callService(final AsyncCallback<List<BugTrackerIssueDTO>> pCb)
         {
            DeliveryManagement.get().getServiceAsync().getIssues(DeliveryManagement.get().getProjectId(),
                                                                 BugsPresenter.this.reference, pVersion, pCb);
         }

         @Override
         public void onSuccess(final List<BugTrackerIssueDTO> pResult)
         {
            if (pResult != null)
            {
               BugsPresenter.this.display.getListDataProvider().getList().clear();
               BugsPresenter.this.display.getListDataProvider().getList().addAll(pResult);
               BugsPresenter.this.display.getBugsListPanel().setVisible(true);
            }
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            BugsPresenter.this.display.getListDataProvider().getList().clear();
            if ((caught instanceof DeliveryManagementServiceException) && (((DeliveryManagementServiceException) caught)
                                                                               .getCode() != null))
            {
               final ExceptionCode code = ((DeliveryManagementServiceException) caught).getCode();
               if (code.equals(ExceptionCode.ASSOCIATION_DO_NOT_EXIST))
               {
                  final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages().bugListUnavailable(),
                                                               InfoTypeEnum.ERROR);
                  info.show();
               }
               else
               {
                  final InfoDialogBox info = new InfoDialogBox(code.getLocalizedMessage(), InfoTypeEnum.WARNING);
                  info.show();
               }
            }
            else
            {
               final InfoDialogBox info = new InfoDialogBox(ExceptionCode.TECHNICAL_ERROR.getLocalizedMessage(),
                                                            InfoTypeEnum.WARNING);
               info.show();
            }
         }
      }.retry(0);

   }

   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }

   @Override
   public void refreshView(final String pReference)
   {
      this.reference = pReference;
      BugsPresenter.this.display.getDisplayButton().setEnabled(false);
      BugsPresenter.this.display.getSaveButton().setEnabled(false);
      BugsPresenter.this.display.getVersionList().setEnabled(true);
      BugsPresenter.this.display.getVersionList().clear();
      BugsPresenter.this.display.getSaveButton().setEnabled(false);
      BugsPresenter.this.display.getListDataProvider().getList().clear();
      BugsPresenter.this.display.getBugsListPanel().setVisible(false);

      this.refreshIssueContent();

   }

   private void refreshIssueContent()
   {
      BugsPresenter.this.display.getVersionList().clear();
      BugsPresenter.this.display.getVersionList().addItem(DeliveryCommon.getMessages().issueVersionBox());
      new AbstractRPCCall<IssueContentDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<IssueContentDTO> pCb)
         {
            DeliveryManagement
                  .get()
                  .getServiceAsync()
                  .getIssueContent(DeliveryManagement.get().getProjectId(), BugsPresenter.this.reference, pCb);
         }

         @Override
         public void onSuccess(final IssueContentDTO pResult)
         {
            if (pResult != null)
            {
               final List<String> versions = pResult.getVersions();

               if ((versions != null) && (!versions.isEmpty()))
               {

                  for (int i = 0; i < versions.size(); i++)
                  {
                     final String version = versions.get(i);
                     BugsPresenter.this.display.getVersionList().addItem(version);
                     if ((pResult.getContent().getRoot() != null) & (version.equals(pResult.getContent().getRoot()
                                                                                           .getName())))
                     {
                        BugsPresenter.this.display.getVersionList().setSelectedIndex(i + 1);
                        BugsPresenter.this.display.getDisplayButton().setEnabled(true);
                        BugsPresenter.this.display.getSaveButton().setEnabled(true);
                        BugsPresenter.this.refreshIssues(version);
                     }
                  }
               }
            }
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            BugsPresenter.this.display.getVersionList().setEnabled(false);
            if ((caught instanceof DeliveryManagementServiceException)
                  && (((DeliveryManagementServiceException) caught).getCode() != null))
            {
               final ExceptionCode code = ((DeliveryManagementServiceException) caught).getCode();
               if (code.equals(ExceptionCode.ASSOCIATION_DO_NOT_EXIST))
               {
                  final InfoDialogBox info = new InfoDialogBox(DeliveryCommon.getMessages()
                        .bugListUnavailable(), InfoTypeEnum.ERROR);
                  info.show();
               }
               else
               {
                  final InfoDialogBox info = new InfoDialogBox(code.getLocalizedMessage(),
                        InfoTypeEnum.WARNING);
                  info.show();
               }
            }
            else
            {
               final InfoDialogBox info = new InfoDialogBox(
                     ExceptionCode.TECHNICAL_ERROR.getLocalizedMessage(), InfoTypeEnum.WARNING);
               info.show();
            }
         }
      }.retry(0);

   }
}
