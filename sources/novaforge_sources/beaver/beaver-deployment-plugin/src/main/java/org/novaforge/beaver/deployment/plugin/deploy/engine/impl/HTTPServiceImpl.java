/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2014  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.novaforge.beaver.deployment.plugin.deploy.engine.HTTPService;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Gauthier Cart
 */
public class HTTPServiceImpl implements HTTPService
{

  @Override
  public String post(String pURL, Map<String, String> pParams) throws BeaverException, IOException
  {
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost httppost = new HttpPost(pURL);

    // Query parameters and other properties.
    List<NameValuePair> params = new ArrayList<NameValuePair>(pParams.size() - 1);
    for (Entry<String, String> entry : pParams.entrySet())
    {
      params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    }
    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

    // Execute and get the response.
    HttpResponse httpResponse = httpclient.execute(httppost);
    String response = EntityUtils.toString(httpResponse.getEntity());

    return response;
  }
}
