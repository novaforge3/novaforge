package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * @author Gauthier Cart
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HTTPServiceImplTest {
  private final static String URL = "http://gca-2/api/v3/session";
  private final static String PARAM_LOGIN = "login";
  private final static String PARAM_LOGIN_DATA = "root";
  private final static String PARAM_PASSWORD = "password";
  private final static String PARAM_PASSWORD_DATA = "5iveL!fe";

  private static HTTPServiceImpl httpService;

  private boolean httpServiceProfileActivated = false;

  public HTTPServiceImplTest() {
    final String property = System.getProperty("httpService.profile");
    if ("true".equals(property)) {
      httpServiceProfileActivated = true;
    }
  }

  @Before
  public void init() throws BeaverException, IOException {
    httpService = new HTTPServiceImpl();
    BeaverLogger.setLogger("target/log", new SystemStreamLog());
  }

  @Test
  public void test001post() throws BeaverException, IOException {
    if (httpServiceProfileActivated) {
      Map<String, String> params = new HashMap<>();
      params.put(PARAM_LOGIN, PARAM_LOGIN_DATA);
      params.put(PARAM_PASSWORD, PARAM_PASSWORD_DATA);
      String response = httpService.post(URL, params);
      assertNotNull("Response is not null", response);
    }
  }
}
