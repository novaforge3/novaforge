package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.DockerHostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.HostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.MavenHostedRepositoryParam;

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
public class RepositoryManagerTest extends LocalNexusTest
{

  private final static String  HOSTED_REPOSITORY_BOWER    = "repotest_bower_hosted";
  private final static String  HOSTED_REPOSITORY_DOCKER   = "repotest_docker_hosted";
  private final static String  HOSTED_REPOSITORY_MAVEN    = "repotest_maven_hosted";
  private final static String  HOSTED_REPOSITORY_NPM      = "repotest_npm_hosted";
  private final static String  HOSTED_REPOSITORY_NUGET    = "repotest_nuget_hosted";
  private final static String  HOSTED_REPOSITORY_PYPI     = "repotest_pypi_hosted";
  private final static String  HOSTED_REPOSITORY_RAW      = "repotest_raw_hosted";
  private final static String  HOSTED_REPOSITORY_RUBYGEMS = "repotest_rubygems_hosted";

  private final static Integer DEFAULT_HTTP_PORT          = 8080;
  private final static Integer DEFAULT_HTTPS_PORT         = 443;

  @Test
  public void test01GetInstance()
  {

    if (nexusProfileActivated)
    {

      assertNotNull(getRepositoryManager());
    }
  }

  private RepositoryManager getRepositoryManager()
  {
    return RepositoryManager.getInstance(NEXUS_SCRIPT_URL_BASE, ADMIN_USER, ADMIN_USER_PASSWORD);
  }

  @Test
  public void test02CreateBowerHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(HOSTED_REPOSITORY_BOWER);

        JSONResponse jsonResponse = repositoryManager.createBowerHosted(hostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test03CreateDockerHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        DockerHostedRepositoryParam dockerHostedRepositoryParam = new DockerHostedRepositoryParam(
            HOSTED_REPOSITORY_DOCKER, DEFAULT_HTTP_PORT, DEFAULT_HTTPS_PORT);

        JSONResponse jsonResponse = repositoryManager.createDockerHosted(dockerHostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test04CreateMavenHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        MavenHostedRepositoryParam mavenHostedRepositoryParam = new MavenHostedRepositoryParam(
            HOSTED_REPOSITORY_MAVEN);

        JSONResponse jsonResponse = repositoryManager.createMavenHosted(mavenHostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test05CreateNpmHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(HOSTED_REPOSITORY_NPM);

        JSONResponse jsonResponse = repositoryManager.createNpmHosted(hostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test06CreateNugetHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(HOSTED_REPOSITORY_NUGET);

        JSONResponse jsonResponse = repositoryManager.createNugetHosted(hostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test07CreatePyPiHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(HOSTED_REPOSITORY_PYPI);

        JSONResponse jsonResponse = repositoryManager.createPyPiHosted(hostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test08CreateRawHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(HOSTED_REPOSITORY_RAW);

        JSONResponse jsonResponse = repositoryManager.createRawHosted(hostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test09CreateRubygemsHosted()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(HOSTED_REPOSITORY_RUBYGEMS);

        JSONResponse jsonResponse = repositoryManager.createRubygemsHosted(hostedRepositoryParam);

        assertTrue(jsonResponse.getStatus() == 200);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test10GetHostedMavenRepository()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        JSONResponse jsonResponse = repositoryManager.getRespository(HOSTED_REPOSITORY_MAVEN);

        assertTrue(jsonResponse.getStatus() == 200);
        assertTrue(jsonResponse.getResult().contains(HOSTED_REPOSITORY_MAVEN));

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test11DeleteRepository()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        JSONResponse jsonResponse = repositoryManager.deleteRepository(HOSTED_REPOSITORY_RUBYGEMS);

        assertTrue(jsonResponse.getStatus() == 200);
        assertTrue(jsonResponse.getResult().equals("null"));

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test12ExistRepository()
  {

    if (nexusProfileActivated)
    {

      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        assertTrue(repositoryManager.existsRepository(HOSTED_REPOSITORY_BOWER));

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }

    }
  }

  @Test
  public void test99Purge()
  {

    if (nexusProfileActivated)
    {
      RepositoryManager repositoryManager = getRepositoryManager();

      try
      {

        repositoryManager.deleteRepository(HOSTED_REPOSITORY_BOWER);
        repositoryManager.deleteRepository(HOSTED_REPOSITORY_DOCKER);
        repositoryManager.deleteRepository(HOSTED_REPOSITORY_MAVEN);
        repositoryManager.deleteRepository(HOSTED_REPOSITORY_NPM);
        repositoryManager.deleteRepository(HOSTED_REPOSITORY_NUGET);
        repositoryManager.deleteRepository(HOSTED_REPOSITORY_PYPI);
        repositoryManager.deleteRepository(HOSTED_REPOSITORY_RAW);

      }
      catch (Exception e)
      {

        fail(e.getMessage());
      }
    }
  }

}
