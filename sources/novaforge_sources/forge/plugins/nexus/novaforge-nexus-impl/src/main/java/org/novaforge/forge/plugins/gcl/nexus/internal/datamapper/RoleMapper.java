package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

public class RoleMapper {

	/**
	 * Constant blank space close
	 */
	private static final char BLANK_SPACE = ' ';
	/**
	 * Constant bracket close
	 */
	private static final char BRACKET_CLOSE = ')';
	/**
	 * Role id separator
	 */
	private static final char FIELD_DELIMITER = '_';
	/**
	 * Constant bracket open
	 */ 
	private static final char BRACKET_OPEN = '(';

	
	/**
	 * Build the role Id
	 * <br/>
	 * <pre>
   * <projectId>_<applicationId>_<format>_<version>_<roleShortName>
	 * 	
	 * 		For example : myproject_nexus_docker_release_administrator
	 * </pre>
	 * 	
	 * @param repositoryId
	 * @param roleShortName
	 * @return
	 */
	public static String getRoleId(String repositoryId, String roleShortName) {

		StringBuilder roleIdBuilder = new StringBuilder();

		roleIdBuilder.append(repositoryId);
		roleIdBuilder.append(FIELD_DELIMITER);
		roleIdBuilder.append(roleShortName);

		return roleIdBuilder.toString();
	}

	/**
	 * Build the role Name
	 * <br/>
	 * <pre>
	 * <projectId>_<applicationId>_<format>_<version> (<roleLabel>)
	 * 	
	 * 		For example : myproject_nexus_docker_release (Administrator)
	 * </pre>
	 * 	
	 * @param repositoryId
	 * @param roleId
	 * @return
	 */
	public static String getRoleName(String repositoryId, String roleLabel) {

		StringBuilder roleIdBuilder = new StringBuilder();

		roleIdBuilder.append(repositoryId);
		roleIdBuilder.append(BLANK_SPACE);
		roleIdBuilder.append(BRACKET_OPEN);
		roleIdBuilder.append(roleLabel);
		roleIdBuilder.append(BRACKET_CLOSE);

		return roleIdBuilder.toString();
	}
	
	/**
	 * Build and return a description of a role.
	 * @param repositoryId
	 * @param roleShortName
	 * @param roleDescription
	 * @return
	 */
	public static String getRoleDescription(String repositoryId, String roleLabel) {

		StringBuilder roleIdBuilder = new StringBuilder();

		roleIdBuilder.append(roleLabel);
		roleIdBuilder.append(" role on ");
		roleIdBuilder.append(repositoryId);
		roleIdBuilder.append(" repository");
		
		return roleIdBuilder.toString();
	}
}
