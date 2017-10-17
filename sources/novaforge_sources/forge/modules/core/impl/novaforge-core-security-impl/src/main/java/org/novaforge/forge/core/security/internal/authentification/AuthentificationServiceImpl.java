/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.security.internal.authentification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.core.security.realms.ForgeStandardRealm;

import java.util.Random;

/**
 * Implementation of Authentification Service. It is based on Shiro Components.
 *
 * @author lamirang
 */
public class AuthentificationServiceImpl implements AuthentificationService
{
  /**
   * Logger
   */
  private static final Log LOGGER = LogFactory.getLog(AuthentificationServiceImpl.class);
  
  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.LOGIN)
  public void login(@HistorizableParam(label = "login") final String pLogin, final String pPwd)
  {
    ensureUserIsLoggedOut();   
    final Subject subject = SecurityUtils.getSubject();
    final UsernamePasswordToken token   = new UsernamePasswordToken(pLogin, pPwd);
    token.setRememberMe(true);
    subject.login(token);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.LOGIN)
  public boolean doCredentialsMatch(@HistorizableParam(label = "login") final String pLogin, final String pPwd)
  {
    boolean isMatching = false;
    try
    {
      final ForgeStandardRealm realm = new ForgeStandardRealm();
      final UsernamePasswordToken token = new UsernamePasswordToken(pLogin, pPwd);
      final AuthenticationInfo info = realm.getAuthenticationInfo(token);
      if (info != null)
      {
        isMatching = true;
      }
    }
    catch (final AuthenticationException e)
    {
      // Ignore this exception
      LOGGER.debug("doCredentialsMatch exception occured", e);
    }

    return isMatching;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String hashPassword(final String pPwd)
  {
    return new Sha1Hash(pPwd).toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean checkLogin()
  {
    final Subject subject = SecurityUtils.getSubject();

    return subject.isAuthenticated();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.LOGOUT)
  public void logout()
  {
    final Subject subject = SecurityUtils.getSubject();
    subject.logout();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String generateRandomPassword()
  {
    // May be we could externalize this method. But right now, we will keep it here.
    final int length = 8;
    final char[] prefix = new char[length];
    int       c;
    int       r1;
    for (int i = 0; i < (length - 1); i++)
    {
      r1 = (int) (Math.random() * 3);
      switch (r1)
      {
        case 0:
          c = '0' + (int) (Math.random() * 10);
          break;
        case 1:
          c = 'a' + (int) (Math.random() * 26);
          break;

        default:
        case 2:
          c = 'A' + (int) (Math.random() * 26);
          break;
      }
      prefix[i] = (char) c;
    }

    // Add a special character in the password
    final int randomNumber = new Random().nextInt(length);

    // copy the array and insert the special character randomly
    final char[] tmpPrefix = prefix.clone();
    prefix[randomNumber] = '_';
    System.arraycopy(tmpPrefix, randomNumber, prefix, randomNumber + 1, length - 1 - randomNumber);

    return String.valueOf(prefix);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCurrentUser()
  {
    String        username = null;
    final Subject subject  = SecurityUtils.getSubject();
    try {
      if ((subject != null) && (subject.getPrincipal() != null))
      {
        username = subject.getPrincipal().toString();
      }
    } catch (UnknownSessionException use)
    {
      //to avoid session timeout when user is already logged
      //as @Historization is put on login method and historisation
      //use getCurrentUser => it fails on UnknownSessionException
      LOGGER.debug("getCurrentUser exception occured", use);
    }
    return username;
  }
  
  

  /**
   * ensure that user is logged out
   * if not logout the user fully before continuing.
   * to avoid https://issues.apache.org/jira/browse/SHIRO-446 issue which is not fixed
   * cf.  http://stackoverflow.com/questions/14516851/shiro-complaining-there-is-no-session-with-id-xxx-with-defaultsecuritymanager
   */
  private void ensureUserIsLoggedOut()
  {
     try
     {
         // Get the user if one is logged in.
         Subject currentUser = SecurityUtils.getSubject();
         if (currentUser == null)
         {
           LOGGER.debug("ensureUserIsLoggedOut : user is already logged out");
           return;
         }
  
         // Log the user out and kill their session if possible.
         currentUser.logout();
         Session session = currentUser.getSession(false);
         if (session == null)
         {
           LOGGER.debug("ensureUserIsLoggedOut : user is logged out");
           return;
         }
  
         session.stop();
     }
     catch (Exception e)
     {
         // Ignore all errors, as we're trying to silently 
         // log the user out.
       LOGGER.debug("ensureUserIsLoggedOut exception occured", e);
     }
  }
}
