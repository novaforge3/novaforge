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
package com.prt.gwt.file.core;

import com.prt.gwt.file.analyzer.server.service.FileAnalyzerServiceImpl;
import com.prt.gwt.file.core.client.RequestUtil;
import com.prt.gwt.file.core.client.util.json.Jsonable;
import com.prt.gwt.file.core.server.service.FileDownloadServlet;
import com.prt.gwt.file.core.server.service.FileManagerServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonableRemoteServlet extends HttpServlet
{
	/**
    * 
    */
	private static final long         serialVersionUID = 3805274069030808854L;
	private final Map<String, Object> services         = new HashMap<String, Object>();

	@Override
	public void init(final ServletConfig config) throws ServletException
	{
		super.init(config);
		services.put("FileAnalyzerServiceAsync", new FileAnalyzerServiceImpl());
		services.put("FileManagerServiceAsync", new FileManagerServiceImpl());
		services.put("FileDownloadServlet", new FileDownloadServlet());
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
	    IOException
	{

		final String remoteUrl = req.getParameter(RequestUtil.REMOTE_URL_PARAM);
		if (remoteUrl == null)
		{

			final String serviceName = req.getParameter("serviceName");
			final Object service = services.get(serviceName);
			if (service == null)
			{
				throw new RuntimeException("Service " + service + " not found !");
			}

			final String servletRedirect = req.getParameter(RequestUtil.REDIRECT_CALL_PARAM);

			if (Boolean.parseBoolean(servletRedirect))
			{
				redirectCall(service, req, resp);
			}
			else
			{
				jsonableCall(service, req, resp);
			}

		}
		else
		{
			makeCrossDomainCall(remoteUrl, req, resp);
		}
	}

	private void redirectCall(final Object service, final HttpServletRequest req, final HttpServletResponse resp)
	    throws IOException
	{
		try
		{
			final Method declaredMethod = service.getClass().getDeclaredMethod("doGet", HttpServletRequest.class,
																																				 HttpServletResponse.class);
			declaredMethod.setAccessible(true);
			declaredMethod.invoke(service, req, resp);
		}
		catch (final Exception e)
		{
			writeError(resp, e);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void jsonableCall(final Object service, final HttpServletRequest req, final HttpServletResponse resp)
	    throws IOException
	{
		final String methodName = req.getParameter("methodName");
		if (methodName == null)
		{
			throw new RuntimeException("Method " + methodName + " not found in class "
			    + service.getClass().getName());
		}

		final List<Class> paramTypes = new ArrayList<Class>();
		final List<Object> paramValues = new ArrayList<Object>();
		String paramStringValue;
		int i = 0;
		try
		{
			while ((paramStringValue = req.getParameter("_argLocal" + (i))) != null)
			{
				final String paramType = req.getParameter("_argLocalType" + (i));

				final Class<?> paramClass = Class.forName(paramType);
				paramTypes.add(paramClass);

				final Jsonable paramInstance = (Jsonable) paramClass.newInstance();
				paramInstance.fromJson(paramStringValue);
				paramValues.add(paramInstance);

				i++;
			}

			final Method method = service.getClass().getDeclaredMethod(methodName, paramTypes.toArray(new Class[paramTypes
																																																							.size()]));
			final Class<?> returnType = method.getReturnType();
			if (Jsonable.class.isAssignableFrom(returnType))
			{
				final Jsonable result = (Jsonable) method.invoke(service, paramValues.toArray());
				writeResponse(resp, result.toJson());
			}
			else if (returnType.isAssignableFrom(Void.class))
			{
				method.invoke(service, paramValues.toArray());
				writeResponse(resp, "");
			}
			else
			{
				writeError(resp, new RuntimeException("Cannot process result of type " + returnType.getName()));
			}

		}
		catch (final Exception e)
		{
			writeError(resp, e);
			e.printStackTrace();
		}
	}

	private void makeCrossDomainCall(final String remoteURL, final HttpServletRequest req,
	    final HttpServletResponse resp) throws IOException
	{
		try
		{
			final URL url = new URL(remoteURL + "?" + createParams(req));
			final URLConnection openConnection = url.openConnection();
			final java.io.InputStream is = openConnection.getInputStream();
			final java.io.BufferedReader di = new java.io.BufferedReader(new java.io.InputStreamReader(is));

			final StringBuffer result = new StringBuffer();
			String line;
			while ((line = di.readLine()) != null)
			{
				result.append(line);
			}

			writeResponse(resp, result.toString());
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			writeError(resp, e);
		}
	}

	private void writeError(final HttpServletResponse response, final Throwable t) throws IOException
	{
		response.setContentType("text/plain; charset=utf-8");
		final PrintWriter writer = response.getWriter();
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		final StringWriter sw = new StringWriter();
		final PrintWriter printStream = new PrintWriter(sw);
		t.printStackTrace(printStream);

		writer.write(sw.toString());
		writer.close();
	}

	private void writeResponse(final HttpServletResponse response, final String responseContent)
	    throws IOException
	{

		final byte[] responseBytes = responseContent.getBytes("UTF-8");
		response.setContentLength(responseBytes.length);
		response.setContentType("text/plain; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		final PrintWriter writer = response.getWriter();
		writer.write(responseContent);
		writer.close();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private String createParams(final HttpServletRequest req)
	{
		final StringBuffer result         = new StringBuffer();
		final Enumeration  parameterNames = req.getParameterNames();

		while (parameterNames.hasMoreElements())
		{
			final String key = (String) parameterNames.nextElement();

			if (!RequestUtil.REMOTE_URL_PARAM.equalsIgnoreCase(key))
			{
				result.append(key).append("=").append(URLEncoder.encode(req.getParameter(key))).append("&");
			}

		}
		return result.toString();
	}

}
