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
package org.novaforge.forge.ui.historization.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.ui.historization.shared.FunctionalLogDTO;

import java.util.Date;
import java.util.List;

/**
 * The async counterpart of <code>historization</code>.
 */
public interface HistorizationRemoteAsync
{
  void getListFunctionalLogs(String pLevel, String pType, String pUser, String pKeyword, Date pStartDate,
      Date pEndDate, int pStart, int pLength, String pFixedDate,
      AsyncCallback<List<FunctionalLogDTO>> callback);

  void exportCSVFromCriterias(String pLevel, String pType, String pUser, String pKeyword, Date pStartDate,
      Date pEndDate, String pFixedDate, String pLocale, AsyncCallback<String> callback);

  void getListTypes(AsyncCallback<List<String>> callback);

  void getListLevels(AsyncCallback<List<String>> callback);

  void getListUsers(AsyncCallback<List<String>> callback);

  void purgeFunctionnalLogs(Date pLimitDate, AsyncCallback<Void> callback);

  void countEventsByCriterias(String pLevel, String pType, String pUser, String pKeyword, Date pStartDate,
      Date pEndDate, String pFixedDate, AsyncCallback<Integer> callback);
}
