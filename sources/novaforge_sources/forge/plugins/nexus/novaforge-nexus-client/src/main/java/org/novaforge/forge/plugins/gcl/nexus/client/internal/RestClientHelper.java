package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.QueryParam;
import org.sonatype.nexus.security.role.Role;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import net.sf.json.JSONException;

/**
 * use Jersey JAX-RS Client API: https://jersey.java.net/documentation/latest/user-guide.html
 * Jersey Client API: https://jersey.java.net/documentation/latest/user-guide.html#client
 * @author s241664
 *
 */
public class RestClientHelper {
	
	/**
	 * Logger component
	 */
	private static final Log LOGGER = LogFactory.getLog(RestClientHelper.class);
	
	
	public static JSONResponse executePost(String urlBase, String adminUser, String adminPassword, ScriptOperation scriptOperation, Object pojoParameter)  throws JsonProcessingException, IOException {
				
		JSONResponse jsonResponse = null;
		
		Client client = ClientFactory.getClient(adminUser, adminPassword);
		
		WebTarget webTarget = client.target(urlBase)
				.path("script")
				.path(scriptOperation.getScriptName())
				.path("run");

		Invocation.Builder invocationBuilder = webTarget.request().header("Content-type", MediaType.APPLICATION_JSON);
		
		LOGGER.debug("invoke POST operation with parameter: " + pojoParameter);
		
		Response response =invocationBuilder.post(
				Entity.entity(convertJavaToJSon(pojoParameter), 
				MediaType.TEXT_PLAIN));
		
	
		jsonResponse = response.readEntity(JSONResponse.class);
		
		jsonResponse.setStatus(response.getStatus());	
		
		LOGGER.debug("Return a response: " + jsonResponse);
		
		client.close();
		
		return jsonResponse;
	}

	public static String convertJavaToJSon(Object obj) throws JsonProcessingException {
		String result = null;
		ObjectMapper mapper = new ObjectMapper();
		try{
			result = mapper.writeValueAsString(obj);
		} catch (IOException ioe){
			LOGGER.error("error while converting java to json", ioe);
			throw new JSONException(ioe);
		}
		return result;
	}
	
	public static <T> T executeGet(String urlBase, String adminUser, String adminPassword, ScriptOperation scriptOperation, Class<T> responseType, QueryParam...  params)  throws JsonProcessingException, IOException {
		
		T response = null;
		
		Client client = ClientFactory.getClient(adminUser, adminPassword);
		
		WebTarget webTarget = client.target(urlBase)
				.path("script")
				.path(scriptOperation.getScriptName())
				.path("/run");
		
		for (int i = 0; i < params.length; i++) {
			
			webTarget.queryParam(
					params[i].getKey(), 
					params[i].getValue());
		}

		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		
		response = invocationBuilder.get(responseType);	
		
		client.close();
		
		return response;
	}
	
	public static void executeGet(String urlBase, String adminUser, String adminPassword, ScriptOperation scriptOperation, QueryParam...  params)  throws JsonProcessingException, IOException {
		
		Client client = ClientFactory.getClient(adminUser, adminPassword);
		
		WebTarget webTarget = client.target(urlBase)
				.path("script")
				.path(scriptOperation.getScriptName());
		
		for (int i = 0; i < params.length; i++) {
			
			webTarget.queryParam(
					params[i].getKey(), 
					params[i].getValue());
		}
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		
		invocationBuilder.get();	
		
		client.close();
	}

	public static <T> T convertJsonToJava(String jsonString, Class<T> javaType) throws IOException, JsonParseException, JsonMappingException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.readValue(jsonString, javaType);
	}

	 public static <T> List<Role> convertJsonToJavaListRoles(String jsonString) throws IOException, JsonParseException, JsonMappingException {
	    
	    ObjectMapper mapper = new ObjectMapper();
	    return mapper.readValue(jsonString, new TypeReference<List<Role>>(){});
	}

	public static <T> T convertJsonToJava(String jsonString, Class<T> javaType, SimpleModule module) throws IOException, JsonParseException, JsonMappingException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.registerModule(module);
		
		return mapper.readValue(jsonString, javaType);
	}
}
