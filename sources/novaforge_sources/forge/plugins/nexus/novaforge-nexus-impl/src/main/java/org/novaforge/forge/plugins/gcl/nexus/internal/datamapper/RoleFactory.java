package org.novaforge.forge.plugins.gcl.nexus.internal.datamapper;

import java.util.Iterator;

import org.novaforge.forge.plugins.gcl.nexus.domain.NexusConstant;
import org.novaforge.forge.plugins.gcl.nexus.domain.NexusRolePrivilege;
import org.novaforge.forge.plugins.gcl.nexus.domain.PrivilegeActionType;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;
import org.sonatype.nexus.security.role.Role;

/**
 * Build and return instance of role 
 * @author s241664
 *
 */
public class RoleFactory {
	
	
	/**
	 * Create a role relative to a project
	 * @param nexusRolePrivilege
	 * @param targetRepository
	 * @return
	 */
	public static Role getRole(NexusRolePrivilege nexusRolePrivilege, Repository targetRepository ){
		
		Role role = new Role();
		
		String repositoryId = targetRepository.getName();
		
		role.setRoleId(RoleMapper.getRoleId(repositoryId, nexusRolePrivilege.getId()));
		role.setName(RoleMapper.getRoleName(repositoryId, nexusRolePrivilege.getLabel()));
		role.setDescription(RoleMapper.getRoleDescription(repositoryId, nexusRolePrivilege.getLabel()));
		role.setReadOnly(false);
		role.setSource(NexusConstant.DEFAULT_SOURCE);

		
		// add privileges
		Iterator<PrivilegeActionType> iterator = nexusRolePrivilege.getPrivileges().iterator();
		
		while(iterator.hasNext()){
		
			PrivilegeActionType privilegeActionType = iterator.next();
			
			role.addPrivilege(
					PrivilegeMapper.getPrivilegeId(
							nexusRolePrivilege.getActionCategory(), 
							privilegeActionType, targetRepository));
			
		}
		
		// administrator takes the senior developer privileges
		if(nexusRolePrivilege == NexusRolePrivilege.ADMINISTRATOR){
			
			iterator = NexusRolePrivilege.DEVELOPER_SENIOR.getPrivileges().iterator();
			
			while(iterator.hasNext()){
			
				PrivilegeActionType privilegeActionType = iterator.next();
				
				role.addPrivilege(
						PrivilegeMapper.getPrivilegeId(
								NexusRolePrivilege.DEVELOPER_SENIOR.getActionCategory(), 
								privilegeActionType, targetRepository));
				
			}
		}
		
		// add sub-roles
		Iterator<String> roleShortNameIterator = nexusRolePrivilege.getSubRoles().iterator();
		
		while(roleShortNameIterator.hasNext()){
			
			role.addRole(
					RoleMapper.getRoleId(
							repositoryId,
							roleShortNameIterator.next()));
		}
		return role;
	}
}
