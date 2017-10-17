package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.HostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.RoleParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.UserParam;
import org.novaforge.forge.plugins.gcl.nexus.domain.GroupRepository;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;

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
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestClientHelperTest extends LocalNexusTest
{

  private static final String  NPM_REPO_NAME         = "npm-test-private";
  private static final String  DUMMY_ROLE_ID         = "dummy-role-id";
  private final static String  USER_ID               = "foo";
  private final static String  FIRSTNAME             = "firstname";
  private final static String  LASTNAME              = "lastname";
  private final static String  PASSWORD              = "password";
  private final static String  ROLE_ID_1             = "roleId1";
  private final static String  ROLE_ID_2             = "roleId2";
  private final static String  EMAIL                 = "firstnanme.lastname@example.com";
  private final static String  EXPECTED_VALUE        = "{\"id\":\"foo\",\"sourceId\":null,\"firstName\":\"firstname\",\"lastName\":\"lastname\",\"email\":\"firstnanme.lastname@example.com\",\"active\":true,\"password\":\"password\",\"roleIds\":[\"roleId1\",\"roleId2\"]}";

  private final static String  REPOSITORY_GROUP_NAME = "repotest-rubygems-group";
  private final static String  JSON_REPOSITORY       = "{\"type\":\"group\",\"format\":\"rubygems\",\"name\":\"repotest-rubygems-group\",\"url\":\"http://localhost:8081/repository/repotest-rubygems-group\",\"members\":[\"repotest-rubygems-hosted\",\"repotest-rubygems-proxy\"]}";

  private final static boolean ACTIVE                = true;

  @Test
  public void test01ConvertJavaToJSon()
  {

    if (nexusProfileActivated)
    {

      UserParam userParam = new UserParam(USER_ID, FIRSTNAME, LASTNAME, EMAIL, ACTIVE, PASSWORD);

      userParam.addRoleId(ROLE_ID_1);
      userParam.addRoleId(ROLE_ID_2);

      try
      {

        String result = RestClientHelper.convertJavaToJSon(userParam);

        assertNotNull(result);
        assertTrue(result.equals(EXPECTED_VALUE));

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }
    }
  }

  @Test
  public void test02ExecutePost()
  {

    if (nexusProfileActivated)
    {

      ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).delete(ADMIN_USER, ADMIN_USER_PASSWORD,
          ScriptOperation.ADD_ROLE);
      ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER,
          ADMIN_USER_PASSWORD, ScriptOperation.ADD_ROLE);

      RoleParam roleParam = new RoleParam(DUMMY_ROLE_ID, "dummy-role", "test connecteur novaforge");

      roleParam.addPrivilege("nx-all");
      roleParam.addRole("nx-admin");
      try
      {

        JSONResponse jsonResponse = RestClientHelper.executePost(NEXUS_SCRIPT_URL_BASE, ADMIN_USER,
            ADMIN_USER_PASSWORD, ScriptOperation.ADD_ROLE, roleParam);

        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }
    }
  }

  @Test
  public void test03ExecutePostWithEnum()
  {

    if (nexusProfileActivated)
    {

      ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).delete(ADMIN_USER, ADMIN_USER_PASSWORD,
          ScriptOperation.CREATE_NPM_HOSTED);
      ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER,
          ADMIN_USER_PASSWORD, ScriptOperation.CREATE_NPM_HOSTED);

      HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(NPM_REPO_NAME);

      try
      {

        JSONResponse jsonResponse = RestClientHelper.executePost(NEXUS_SCRIPT_URL_BASE, ADMIN_USER,
            ADMIN_USER_PASSWORD, ScriptOperation.CREATE_NPM_HOSTED, hostedRepositoryParam);

        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }
    }
  }

  @Test
  public void test04ConvertJsonToJava()
  {

    try
    {

      Repository repository = RestClientHelper.convertJsonToJava(JSON_REPOSITORY, Repository.class);

      assertNotNull(repository);
      assertTrue(repository instanceof GroupRepository);

      GroupRepository groupRepository = (GroupRepository) repository;

      assertTrue(groupRepository.getName().equals(REPOSITORY_GROUP_NAME));
      assertTrue(groupRepository.getMembers().size() == 2);

    }
    catch (Exception e)
    {

      fail(e.getMessage());
    }
  }

  @Test
  public void test99Purge()
  {

    if (nexusProfileActivated)
    {
      try
      {
        ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER,
            ADMIN_USER_PASSWORD, ScriptOperation.DELETE_ROLE);
        RoleParam roleParam = new RoleParam(DUMMY_ROLE_ID, "default");
        RestClientHelper.executePost(NEXUS_SCRIPT_URL_BASE, ADMIN_USER, ADMIN_USER_PASSWORD,
            ScriptOperation.DELETE_ROLE, roleParam);

        ScriptManager.getInstance(NEXUS_SCRIPT_URL_BASE).createScriptIfDoesNotExist(ADMIN_USER,
            ADMIN_USER_PASSWORD, ScriptOperation.DELETE_REPOSITORY);
        RestClientHelper.executePost(NEXUS_SCRIPT_URL_BASE, ADMIN_USER, ADMIN_USER_PASSWORD,
            ScriptOperation.DELETE_REPOSITORY, NPM_REPO_NAME);
      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }
    }
  }
}
