package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionCategoryType;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionType;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;

public class PrivilegeMapper {
	
	private final static char FIELD_DELIMITER = '-';

	// Nexus creates privileges by defaut when it creates a new repository. 
	// New privileges id are prefixes by 'nx-repository'
	private final static String DEFAULT_PRIVILEGE_ID_PREFIX = "nx-repository";
	
	public static String getPrivilegeId(PrivilegeActionCategoryType actionCategoryType, PrivilegeActionType actionType,  Repository repository){
		
		StringBuilder privilegeIdBuilder = new StringBuilder();
		
		privilegeIdBuilder.append(DEFAULT_PRIVILEGE_ID_PREFIX);
		privilegeIdBuilder.append(FIELD_DELIMITER);
		privilegeIdBuilder.append(actionCategoryType.getValue());
		privilegeIdBuilder.append(FIELD_DELIMITER);
		privilegeIdBuilder.append(repository.getFormat().getValue());
		privilegeIdBuilder.append(FIELD_DELIMITER);
		privilegeIdBuilder.append(repository.getName());
		privilegeIdBuilder.append(FIELD_DELIMITER);						
		privilegeIdBuilder.append(actionType.getValue());
		
		return privilegeIdBuilder.toString();
	}
}
