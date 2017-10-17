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
package org.novaforge.forge.ui.historization.client.view.purge;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import org.novaforge.forge.ui.historization.client.properties.LoggingMessage;
import org.novaforge.forge.ui.historization.client.resources.LoggingResources;
import org.novaforge.forge.ui.historization.client.view.commons.dialogbox.ValidatePurgeDialogBox;

/**
 * @author qsivan
 */
public class LoggingPurgeViewImpl extends Composite implements LoggingPurgeView
{

  private static LoggingPurgeViewImplUiBinder uiBinder        = GWT
                                                                  .create(LoggingPurgeViewImplUiBinder.class);
  private static LoggingResources             ressources      = GWT.create(LoggingResources.class);
  private static String                       pathSeparator   = "/";
  private final LoggingMessage loggingMessages = (LoggingMessage) GWT.create(LoggingMessage.class);
  private final ValidatePurgeDialogBox validatePurgeDialogBox;
  @UiField
  Label                                loggingPurgeTitle;
  @UiField
  Label                                limiteDateLabel;
  @UiField
  DateBox                              limitDateBox;
  @UiField
  Button                               buttonValidatePurge;

  public LoggingPurgeViewImpl()
  {
    ressources.css().ensureInjected();
    initWidget(uiBinder.createAndBindUi(this));
    loggingPurgeTitle.setText(loggingMessages.loggingPurgeTitle());
    limiteDateLabel.setText(loggingMessages.limitDateLabel());
    buttonValidatePurge.setText(loggingMessages.buttonValidatePurge());
    validatePurgeDialogBox = new ValidatePurgeDialogBox(loggingMessages.popupPurgeValidate());
  }

  @Override
  public Button getButtonValidatePurge()
  {
    return buttonValidatePurge;
  }

  @Override
  public ValidatePurgeDialogBox getValidatePurgeDialogBox()
  {
    return validatePurgeDialogBox;
  }

  @Override
  public DateBox getLimitDateBox()
  {
    return limitDateBox;
  }

  interface LoggingPurgeViewImplUiBinder extends UiBinder<Widget, LoggingPurgeViewImpl>
  {
  }
}
