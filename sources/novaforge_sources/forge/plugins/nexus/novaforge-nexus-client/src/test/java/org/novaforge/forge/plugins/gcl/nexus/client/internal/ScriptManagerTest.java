package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONScript;

/**
 * To activate Junit tests inside Eclipse : 
 * <ol>
 * <li>menu Run > the Run Configurations..</li>
 * <li>Select the Run configuration corresponding to the test case</li>
 * <li>add the environment variable -Dnexus.profile=true into the VM arguments section</li>
 * <li>click Apply button>/li>
 * </ol>
 * 
 * @author s241664
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScriptManagerTest  extends LocalNexusTest  {
	
	@Test
	public void test01GetInstance() {
		
		ScriptManager scriptManager  = ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE);
		
		assertNotNull(scriptManager);
		
	}
	
	@Test
	public void test02GetScripts() {
		
		if(nexusProfileActivated){

			ScriptManager scriptManager  = ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE);
			
			List<JSONScript> result = scriptManager.getScripts(ADMIN_USER, ADMIN_USER_PASSWORD);
			
			assertNotNull(result);
			assertTrue(result.size() > 0);
			assertTrue(result.get(0).getType().equals("groovy"));
		}
	}
	
	@Test
	public void test03Create() {

		if(nexusProfileActivated){
			
			ScriptManager scriptManager  = ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE);
			
			scriptManager.create(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.DUMMY);
			
			List<JSONScript> result = scriptManager.getScripts(ADMIN_USER, ADMIN_USER_PASSWORD);
			
			assertNotNull(result);
			
			Iterator<JSONScript> iterator = result.iterator();
			
			while (iterator.hasNext()){
				
				
				JSONScript jsonScript = iterator.next();
				
				if(jsonScript.getName().equals(ScriptOperation.DUMMY.getScriptName())){
					
					assertTrue(true);
					break;
				}
			}		
		}
	}

	@Test
	public void test04Get() {

		if(nexusProfileActivated){
			
			ScriptManager scriptManager  = ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE);
			
			JSONScript result = scriptManager.get(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.DUMMY);
	
			assertNotNull(result);
			assertTrue(result.getName().equals(ScriptOperation.DUMMY.getScriptName()));
		}
	}
	

	@Test
	public void test06Delete() {
		
		if(nexusProfileActivated){

			ScriptManager scriptManager  = ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE);
			
			scriptManager.delete(ADMIN_USER, ADMIN_USER_PASSWORD, ScriptOperation.DUMMY);
			
			List<JSONScript> result = scriptManager.getScripts(ADMIN_USER, ADMIN_USER_PASSWORD);
			
			assertNotNull(result);
			
			Iterator<JSONScript> iterator = result.iterator();
			
			while (iterator.hasNext()){
				
				
				JSONScript jsonScript = iterator.next();
				
				if(jsonScript.getName().equals(ScriptOperation.DUMMY.getScriptName())){
					
					fail("Dummy Script has not been removed");
					break;
				}
			}
		}
	}

	@Test
	public void test99Purge() {
		
		if(nexusProfileActivated){
		
			ScriptManager scriptManager  = ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE);
			
			List<JSONScript> scripts = scriptManager.getScripts(ADMIN_USER, ADMIN_USER_PASSWORD);
			
			assertTrue(scripts.size() > 0);
						
			
			scriptManager.purgeCustomScripts(ADMIN_USER, ADMIN_USER_PASSWORD);
			
			scripts = scriptManager.getScripts(ADMIN_USER, ADMIN_USER_PASSWORD);
			
			assertTrue(scripts.size() == 0);

		}
	}
}
