package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.DockerHostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.HostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.MavenHostedRepositoryParam;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * API to manage repositories.
 * 
 * API is based on groovy files (ex: https://github.com/savoirfairelinux/ansible-nexus3-oss/tree/master/files/groovy)
 * @author s241664
 *
 */
public class RepositoryManager extends AbstractNexusManager {


	/**
	 * Logger component
	 */
	private static final Log LOGGER = LogFactory.getLog(RepositoryManager.class);

	private static RepositoryManager instance = new RepositoryManager();

	private RepositoryManager() {
		super();
	}
	
	
	/**
	 * Return the unique instance of the RepositoryManager
	 * 
	 * @param urlbase
	 * @return
	 */
	public static RepositoryManager getInstance(String urlbase, String adminUser, String adminPassword) {

		instance.urlBase = urlbase;
		instance.adminUser = adminUser;
		instance.adminPassword = adminPassword;

		return instance;
	}

	/**
	 * Create a Bower hosted repository
	 * 
	 * @param hostedRepositoryParam
	 * @return
	 */
	public JSONResponse createBowerHosted(HostedRepositoryParam hostedRepositoryParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_BOWER_HOSTED);

		LOGGER.debug("REST Call : createBowerHosted [name=" + hostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_BOWER_HOSTED, hostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a Docker hosted repository
	 * 
	 * @param dockerHostedRepositoryParam
	 * @return
	 */
	public JSONResponse createDockerHosted(DockerHostedRepositoryParam dockerHostedRepositoryParam)
			throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_DOCKER_HOSTED);

		LOGGER.debug("REST Call : createDockerHosted [name=" + dockerHostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_DOCKER_HOSTED, dockerHostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a Maven hosted repository
	 * 
	 * @param mavenHostedRepositoryParam
	 * @return
	 */
	public JSONResponse createMavenHosted(MavenHostedRepositoryParam mavenHostedRepositoryParam)
			throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_MAVEN_HOSTED);

		LOGGER.debug("REST Call : createMavenHosted [name=" + mavenHostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_MAVEN_HOSTED, mavenHostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a NPM hosted repository
	 * 
	 * @param hostedRepositoryParam
	 * @return
	 */
	public JSONResponse createNpmHosted(HostedRepositoryParam hostedRepositoryParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_NPM_HOSTED);

		LOGGER.debug("REST Call : createNpmHosted [name=" + hostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_NPM_HOSTED, hostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a Nuget hosted repository
	 * 
	 * @param hostedRepositoryParam
	 * @return
	 */
	public JSONResponse createNugetHosted(HostedRepositoryParam hostedRepositoryParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_NUGGET_HOSTED);

		LOGGER.debug("REST Call : createNugetHosted [name=" + hostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_NUGGET_HOSTED, hostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a PyPi hosted repository
	 * 
	 * @param hostedRepositoryParam
	 * @return
	 */
	public JSONResponse createPyPiHosted(HostedRepositoryParam hostedRepositoryParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_PYPI_HOSTED);

		LOGGER.debug("REST Call : createPyPiHosted [name=" + hostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_PYPI_HOSTED, hostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a Raw hosted repository
	 * 
	 * @param hostedRepositoryParam
	 * @return
	 */
	public JSONResponse createRawHosted(HostedRepositoryParam hostedRepositoryParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_RAW_HOSTED);

		LOGGER.debug("REST Call : createRawHosted [name=" + hostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_RAW_HOSTED, hostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Create a Ruby gems hosted repository
	 * 
	 * @param hostedRepositoryParam
	 * @return
	 */
	public JSONResponse createRubygemsHosted(HostedRepositoryParam hostedRepositoryParam) throws JsonProcessingException, IOException {

		createScriptIfDoesNotExist(ScriptOperation.CREATE_RUBYGEMS_HOSTED);

		LOGGER.debug("REST Call : createRubygemsHosted [name=" + hostedRepositoryParam.getName() + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.CREATE_RUBYGEMS_HOSTED, hostedRepositoryParam);

		return jsonResponse;
	}

	/**
	 * Delete the named repository.
	 * @param repositoryName
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse deleteRepository (String repositoryName) throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.DELETE_REPOSITORY);
		
		LOGGER.debug("REST Call : deleteRepository [name=" + repositoryName + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.DELETE_REPOSITORY, repositoryName);
		
		return jsonResponse;
	}
	
	/**
	 * Return the named repository.
	 * @param repositoryName
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JSONResponse getRespository(String repositoryName) throws JsonProcessingException, IOException {
			
		createScriptIfDoesNotExist(ScriptOperation.GET_REPOSITORY);
		
		LOGGER.debug("REST Call : getRepository [name=" + repositoryName + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.GET_REPOSITORY, repositoryName);

		
		return jsonResponse;
	}
	
	/**
	 * Return true if the directory exists
	 * @param repositoryName
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public boolean existsRepository(String repositoryName) throws JsonProcessingException, IOException {
		
		createScriptIfDoesNotExist(ScriptOperation.EXISTS_REPOSITORY);
		
		LOGGER.debug("REST Call : exists [name=" + repositoryName + "]");
		
		JSONResponse jsonResponse = RestClientHelper.executePost(urlBase, adminUser, adminPassword,
				ScriptOperation.EXISTS_REPOSITORY, repositoryName);

		boolean ret = jsonResponse != null 
				&& jsonResponse.getStatus() == 200
				&& jsonResponse.getResult() != null
				&& jsonResponse.getResult().equals("true");
				
		return ret;
	}
}
