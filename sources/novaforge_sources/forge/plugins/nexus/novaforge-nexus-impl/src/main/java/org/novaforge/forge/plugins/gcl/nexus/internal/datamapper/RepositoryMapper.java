package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.sonatype.nexus.repository.maven.VersionPolicy;

public class RepositoryMapper {
	
	private final static char FIELD_DELIMITER = '_';

  /**
   * Build base repository Id
   * <br/>
   * <pre>
   * <projectId>_<applicationId>_
   *  
   *    For example : myproject_nexus_      : matches to the InstanceConfigurationEntity.toolProjectId
   * </pre>
   *  
   * @param pProjectId
   * @param pApplicationId
   * @return
   */
  public static String buildBaseRepositoryId(final String pProjectId, final String pApplicationId)
  {
    StringBuilder repositoryIdBuilder = new StringBuilder();
    
    repositoryIdBuilder.append(pProjectId);
    repositoryIdBuilder.append(FIELD_DELIMITER);
    repositoryIdBuilder.append(pApplicationId);
    repositoryIdBuilder.append(FIELD_DELIMITER);
    
    return repositoryIdBuilder.toString();
  }

	/**
	 * Build the repository Id
	 * <br/>
	 * <pre>
	 * <baseRepositoryName><format>_<type>_<version>
	 * 	
	 * 		For example : myproject_nexus_docker_release
	 * </pre>
	 * 	
	 * @param pBaseRepositoryName
	 * @param format
	 * @param versionPolicy
	 * @return
	 */
	public static String getRepositoryId(final String pBaseRepositoryName, RepositoryFormat format, VersionPolicy versionPolicy)
	{
		StringBuilder repositoryIdBuilder = new StringBuilder();
		
		repositoryIdBuilder.append(pBaseRepositoryName);
    if (format != RepositoryFormat.MAVEN2) {
      repositoryIdBuilder.append(format.getValue());
      repositoryIdBuilder.append(FIELD_DELIMITER);
    }
		repositoryIdBuilder.append(versionPolicy.name().toLowerCase());
		
		return repositoryIdBuilder.toString();
	}
	
}
