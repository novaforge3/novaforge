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
package com.prt.gwt.file.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.prt.gwt.file.core.client.util.json.Jsonable;
import com.prt.gwt.file.core.client.widget.configuration.ConfigurationManager;

import java.util.Map;
import java.util.Map.Entry;

public class RequestUtil
{
	public static final String  REDIRECT_CALL_PARAM = "servletRedirect";
	public static final String  REMOTE_URL_PARAM    = "remoteURL";
	private static final String servletName         = "jsonServiceServlet";

	@SuppressWarnings("unchecked")
	public static AsyncCallback<String> getProxyAsyncCallback(final Jsonable jsonable,
	    final AsyncCallback callback)
	{
		return new AsyncCallback<String>()
		{

			@Override
			public void onFailure(final Throwable caught)
			{
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(final String result)
			{
				if (jsonable != null)
				{
					jsonable.fromJson(result);
				}
				callback.onSuccess(jsonable);
			}
		};
	}

	public static void makeACall(final Map<String, String> params, final AsyncCallback<String> callback)
	{
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, createBaseUrl() + "&"
		    + getParams(params));
		builder.setHeader("Content-Type", "application/json; charset=utf-8");
		builder.setCallback(new RequestCallback()
		{
			@Override
			public void onResponseReceived(final Request request, final Response response)
			{
				if (200 == response.getStatusCode())
				{
					callback.onSuccess(response.getText());
				}
				else
				{
					callback.onFailure(new RuntimeException("Status code = " + response.getStatusCode() + "; "
					    + response.getText()));
				}
			}

			@Override
			public void onError(final Request request, final Throwable exception)
			{
				callback.onFailure(exception);
			}
		});
		try
		{
			@SuppressWarnings("unused")
			final Request request = builder.send();
		}
		catch (final RequestException e)
		{
			callback.onFailure(e);
		}
	}

	public static String createBaseUrl()
	{
		final String remoteUrl = ConfigurationManager.getCurrentProfile().getAppUrl();
		String       localUrl  = GWT.getModuleBaseURL() + servletName + "?";
		if ((remoteUrl != null) && !"".equals(remoteUrl))
		{
			localUrl += REMOTE_URL_PARAM + "=" + URL.encode(remoteUrl + GWT.getModuleName() + "/" + servletName);
		}
		return localUrl;
	}

	private static String getParams(final Map<String, String> data)
	{
		final StringBuffer params = new StringBuffer();
		params.append("request=json");

		for (final Entry<String, String> entry : data.entrySet())
		{
			params.append("&").append(entry.getKey()).append("=").append(URL.encodeComponent(entry.getValue()));
		}
		return params.toString();
	}
}
