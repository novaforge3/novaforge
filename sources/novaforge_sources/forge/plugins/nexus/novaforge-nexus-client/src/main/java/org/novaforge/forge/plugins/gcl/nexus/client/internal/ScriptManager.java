package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONScript;

public class ScriptManager {

	/**
	 * Logger component
	 */
	private static final Log LOGGER = LogFactory.getLog(ScriptManager.class);

	private final static String GROOVY_EXTENSION = ".groovy";

	private final static String SCRIPT_TYPE = "groovy";

	private static ScriptManager instance = new ScriptManager();

	private final Map<ScriptOperation, JSONScript> scriptRepository = new HashMap<>();

	// URL base of the REST url
	private String urlBase;

	private ScriptManager() {
		super();
		loadRepository();
	}

	/**
	 * load the scripts to replicate on the Nexus Server to constitute the REST
	 * Api.
	 */
	private void loadRepository() {

			for (ScriptOperation scriptOperation : ScriptOperation.values()) {

				InputStream inStream = null;
				BufferedReader bufferedReader = null;
				try {
				
					inStream = new java.io.BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream("groovy/"+scriptOperation.getScriptName() + GROOVY_EXTENSION));
					String line;
					StringBuilder scriptContentBuilder = new StringBuilder();
	
					bufferedReader = new BufferedReader(new InputStreamReader(inStream));
					
					while (((line = bufferedReader.readLine()) != null)) {
	
						scriptContentBuilder.append(line);
					}
	
					bufferedReader.close();
	
					JSONScript jsonScript = new JSONScript(scriptOperation.getScriptName(),
							scriptContentBuilder.toString(), SCRIPT_TYPE);
	
					this.scriptRepository.put(scriptOperation, jsonScript);
					
					inStream.close();
				} catch (IOException e) {
					LOGGER.error(e);
				} finally{
					try{
						if ( bufferedReader != null ) 
						{
							bufferedReader.close();
						}
						if (inStream != null)
						{
							inStream.close();
						}
					} catch (IOException e) {

						LOGGER.error(e);
					}
				}

			}
	}

	/**
	 * Create a groovy script on Nexus server side.
	 * 
	 * @param adminUser
	 * @param adminPassword
	 * @param scriptOperation
	 */
	public void create(String adminUser, String adminPassword, ScriptOperation scriptOperation) {

		if (scriptOperation != null) {

			Client client = ClientFactory.getClient(adminUser, adminPassword);

			WebTarget webTarget = client.target(this.urlBase).path("script");

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			JSONScript script = this.scriptRepository.get(scriptOperation);

			invocationBuilder.post(Entity.entity(script, MediaType.APPLICATION_JSON));

			client.close();
		}
	}

	/**
	 * Create the script on the Nexus server if it does not exist
	 * 
	 * @param adminUser
	 * @param adminPassword
	 * @param scriptOperation
	 */
	public void createScriptIfDoesNotExist(String adminUser, String adminPassword, ScriptOperation scriptOperation) {

		try {

			get(adminUser, adminPassword, scriptOperation);

		} catch (NotFoundException e) {

			create(adminUser, adminPassword, scriptOperation);
		}
	}

	/**
	 * Remove the groovy script from the Nexus server.
	 * 
	 * @param adminUser
	 * @param adminPassword
	 * @param scriptOperation
	 */
	public void delete(String adminUser, String adminPassword, ScriptOperation scriptOperation) {

		if (scriptOperation != null) {

			Client client = ClientFactory.getClient(adminUser, adminPassword);

			WebTarget webTarget = client.target(this.urlBase).path("script").path(scriptOperation.getScriptName());

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			invocationBuilder.delete();

			client.close();
		}
	}

	/**
	 * Get the script stored from the Nexus server.
	 * 
	 * @param adminUser
	 * @param adminPassword
	 * @param scriptName
	 * @return
	 */
	public JSONScript get(String adminUser, String adminPassword, ScriptOperation scriptOperation) {

		JSONScript ret = null;

		if (scriptOperation != null) {

			Client client = ClientFactory.getClient(adminUser, adminPassword);

			WebTarget webTarget = client.target(this.urlBase).path("script").path(scriptOperation.getScriptName());

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			ret = invocationBuilder.get(JSONScript.class);

			client.close();
		}
		return ret;
	}

	/**
	 * Get the list of scripts stored
	 * 
	 * @param adminUser
	 * @param adminPassword
	 * @return
	 */
	public List<JSONScript> getScripts(String adminUser, String adminPassword) {

		Client client = ClientFactory.getClient(adminUser, adminPassword);

		WebTarget webTarget = client.target(this.urlBase).path("script");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		List<JSONScript> scripts = invocationBuilder.get(new GenericType<List<JSONScript>>() {
		});

		client.close();

		return scripts;

	}

	/**
	 * remove the scripts declared in the ScriptOperation enum from the Nexus
	 * server.
	 */
	public void purgeCustomScripts(String adminUser, String adminPassword) {

		Iterator<JSONScript> iterator = this.getScripts(adminUser, adminPassword).iterator();
		
		Client client = ClientFactory.getClient(adminUser, adminPassword);

	
		
		while(iterator.hasNext()){

			String scriptName = iterator.next().getName();
			
			try {
				
				WebTarget webTarget = client.target(this.urlBase).path("script").path(scriptName);

				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

				invocationBuilder.delete();

				LOGGER.info("Script purge : " + scriptName + " sucessfully removed.");

			} catch (NotFoundException e) {

				LOGGER.info(
						"Script purge : " + scriptName + " does not exists - nothing to remove.");
			}
		}
		client.close();
	}
	// /**
	// *
	// * @param adminUser
	// * @param adminPassword
	// * @param scriptOperation
	// */
	// public void update(String adminUser, String adminPassword,
	// ScriptOperation scriptOperation) {
	// // TODO Auto-generated method stub
	//
	// }

	/**
	 * Return the unique instance of the ScriptManager
	 * 
	 * @param urlbase
	 * @return
	 */
	public static ScriptManager getInstance(String urlbase) {

		instance.urlBase = urlbase;
		return instance;
	}

}
