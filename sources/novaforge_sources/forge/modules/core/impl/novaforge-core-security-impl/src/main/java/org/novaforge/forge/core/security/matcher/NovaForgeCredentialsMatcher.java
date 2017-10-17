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
package org.novaforge.forge.core.security.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha1Hash;

/**
 * @author sbenoist
 */
public class NovaForgeCredentialsMatcher extends HashedCredentialsMatcher
{
  /**
   * Default constructor.
   * <p>
   * Will init the matcher with Sha1Hash.ALGORITHM_NAME.
   */
  public NovaForgeCredentialsMatcher()
  {
    super();
    setHashAlgorithmName(Sha1Hash.ALGORITHM_NAME);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean doCredentialsMatch(final AuthenticationToken pToken, final AuthenticationInfo pInfo)
  {
    boolean doMatch = false;
    // special authentication with SHA1-crypted password
    final UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) pToken;
    final String passwd1 = new String(usernamePasswordToken.getPassword());

    final Object credentials = pInfo.getCredentials();
    final byte[] storedBytes = toBytes(credentials);
    final String passwd2 = new String(storedBytes);
    if (passwd1.equals(passwd2))
    {
      doMatch = true;
    }
    else
    {
      // usual case of authentication with clear password
      final Object tokenHashedCredentials = hashProvidedCredentials(pToken, pInfo);
      final Object accountCredentials = getCredentials(pInfo);
      doMatch = equals(tokenHashedCredentials, accountCredentials);
    }
    return doMatch;
  }
}
