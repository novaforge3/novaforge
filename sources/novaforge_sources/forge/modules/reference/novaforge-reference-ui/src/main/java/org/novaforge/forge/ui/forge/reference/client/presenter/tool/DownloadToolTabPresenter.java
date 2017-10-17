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
package org.novaforge.forge.ui.forge.reference.client.presenter.tool;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.commons.client.cells.ClickableImageResourceHasCell;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.forge.reference.client.DownloadFrame;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.properties.UploadMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.tool.DownloadToolTabView;
import org.novaforge.forge.ui.forge.reference.shared.FileInfoDTO;
import org.novaforge.forge.ui.forge.reference.shared.tools.ToolProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DownloadToolTabPresenter implements Presenter
{
	private final UploadMessage       messages   = (UploadMessage) GWT.create(UploadMessage.class);
	private final ReferenceResources  ressources = GWT.create(ReferenceResources.class);
	private final DownloadToolTabView display;
	private final boolean             isPublic;
	protected FileInfoDTO             fileInfoCurrent;
	protected Boolean                 isAuthorizedUpdate;
	private List<FileInfoDTO> toolList;

	public DownloadToolTabPresenter(final DownloadToolTabView display, final boolean isPublic,
	    final boolean pIsAuthorizedUpdate)
	{
		super();
		this.display = display;
		this.isPublic = isPublic;
		isAuthorizedUpdate = pIsAuthorizedUpdate;
		initActionColumn();
		bind();
	}

	private void initActionColumn()
	{
		final List<HasCell<FileInfoDTO, ?>> cells = new LinkedList<HasCell<FileInfoDTO, ?>>();
		if (isAuthorizedUpdate)
		{
			cells.add(new ClickableImageResourceHasCell<FileInfoDTO>(ressources.ko_small(), messages.delete(),
			    new FieldUpdater<FileInfoDTO, ImageResource>()
			    {
				    @Override
				    public void update(final int index, final FileInfoDTO object, final ImageResource value)
				    {
					    fileInfoCurrent = object;
					    DownloadToolTabPresenter.this.getDisplay().getConfimeDeleteBox().show();
				    }
			    }));
		}
		cells.add(new ClickableImageResourceHasCell<FileInfoDTO>(ressources.download(), messages.download(),
		    new FieldUpdater<FileInfoDTO, ImageResource>()
		    {
			    @Override
			    public void update(final int index, final FileInfoDTO object, final ImageResource value)
			    {
				    final String url = GWT.getModuleBaseURL() + "download?" + ToolProperties.NAME_PARAMETER + "="
				        + object.getId();

				    // Open download frame
				    new DownloadFrame(url);

			    }
		    }));
		final CompositeCell<FileInfoDTO> actions = new CompositeCell<FileInfoDTO>(cells);
		final Column<FileInfoDTO, FileInfoDTO> actionsColumn = new Column<FileInfoDTO, FileInfoDTO>(actions)
		{
			@Override
			public FileInfoDTO getValue(final FileInfoDTO object)
			{
				return object;
			}
		};
		display.getToolTable().addColumn(actionsColumn, messages.actions());
		display.getToolTable().setColumnWidth(actionsColumn, 110, Unit.PX);
	}

	protected void bind()
	{
		display.getConfimeDeleteBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getConfimeDeleteBox().hide();
				new AbstractReferenceRPCCall<Boolean>()
				{
					@Override
					protected void callService(final AsyncCallback<Boolean> pCb)
					{
						ForgeReferenceEntryPoint.getServiceAsync().deleteFile(fileInfoCurrent.getId(), pCb);
					}

					@Override
					public void onFailure(final Throwable caught)
					{

						ErrorManagement.displayErrorMessage(caught);
					}

					@Override
					public void onSuccess(final Boolean pResult)
					{
						if (pResult)
						{
							final InfoDialogBox box = new InfoDialogBox(messages.isDeletedFile(), InfoTypeEnum.SUCCESS);
							box.addContentStyleName(ressources.style().important());
							box.show();
							DownloadToolTabPresenter.this.refreshTab();
						}
						else
						{
							if (pResult)
							{
								final InfoDialogBox box = new InfoDialogBox(messages.impossibleDeletedFile(),
								    InfoTypeEnum.ERROR);
								box.show();
							}
						}
					}


				}.retry(0);
			}
		});
		display.getNameSearchTB().addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(final KeyUpEvent event)
			{
				DownloadToolTabPresenter.this.searchList(display.getNameSearchTB().getValue());
			}
		});
		display.getNameSearchTB().addValueChangeHandler(new ValueChangeHandler<String>()
		{
			@Override
			public void onValueChange(final ValueChangeEvent<String> pEvent)
			{
				DownloadToolTabPresenter.this.searchList(display.getNameSearchTB().getValue());
			}
		});
	}

	public DownloadToolTabView getDisplay()
	{
		return display;
	}

	public void refreshTab()
	{
		loadToolList();
		display.toolListSortHandler();
	}

	private void searchList(final String pName)
	{
		final Collection<FileInfoDTO> filter = searchOnFile(toolList, pName);
		refreshList(new ArrayList<FileInfoDTO>(filter));
	}

	/**
	 * This method call a service to invalidate a project with a reason.
	 *
	 * @param reason
	 */
	private void loadToolList()
	{
		toolList = new ArrayList<FileInfoDTO>();
		new AbstractReferenceRPCCall<List<FileInfoDTO>>()
		{
			@Override
			protected void callService(final AsyncCallback<List<FileInfoDTO>> callback)
			{
				if (isPublic)
				{
					ForgeReferenceEntryPoint.getServiceAsync().getPublicTools(callback);
				}
				else
				{
					ForgeReferenceEntryPoint.getServiceAsync().getPrivateTools(callback);
				}

			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(final List<FileInfoDTO> pResult)
			{
				if (pResult != null)
				{
					toolList.clear();
					toolList.addAll(pResult);
					DownloadToolTabPresenter.this.refreshList(toolList);
				}
			}

		}.retry(0);
	}

	private Collection<FileInfoDTO> searchOnFile(final List<FileInfoDTO> pFileInfosList, final String pName)
	{
		return Collections2.filter(pFileInfosList, new Predicate<FileInfoDTO>()
		{
			@Override
			public boolean apply(final FileInfoDTO pInput)
			{
				if (pInput.getName().contains(pName))
				{
					return true;
				}
				else
				{
					return false;
				}

			}
		});
	}

	protected void refreshList(final List<FileInfoDTO> pList)
	{
		display.getToolsDataProvider().setList(pList);
		display.getToolTable().redraw();
		display.toolListSortHandler();
	}

	@Override
	public void go(final HasWidgets pContainer)
	{
		pContainer.clear();
		pContainer.add(display.asWidget());
		loadToolList();

	}
}