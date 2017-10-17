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
/**
 *
 */
package org.novaforge.forge.core.security.authentification;

/**
 * Authentification Service Interface
 * 
 * @author lamirang
 */
public interface AuthentificationService
{
	/**
	 * Allow to login on a system
	 * 
	 * @param pLogin
	 *           represents user's login
	 * @param pPwd
	 *           represents user's pwd hashed
	 */
	void login(final String pLogin, final String pPwd);

	/**
	 * This method will just check that the credentials are valid for the user given.
	 * <p>
	 * After this method the user is not authenticated into the system
	 * </p>
	 * 
	 * @param pLogin
	 *           represents user's login
	 * @param pPwd
	 *           represents user's pwd hashed
	 * @return true if the couple given is valid, otherwise false. That means either the login or the password
	 *         can be invalid.
	 */
	boolean doCredentialsMatch(final String pLogin, final String pPwd);

	/**
	 * Hash password
	 * 
	 * @param pPwd
	 *           represents pwd to be hashed.
	 * @return the password hashed
	 */
	String hashPassword(final String pPwd);

	/**
	 * Allow to check if current subject is log on the system
	 * 
	 * @return true if a user is authenticated in this context
	 */
	boolean checkLogin();

	/**
	 * Allow to logout an user from the system
	 */
	void logout();

	/**
	 * Allow to create a random password
	 * 
	 * @return a generated password which respect pwd validation
	 */
	String generateRandomPassword();

	/**
	 * Allow to get username
	 * 
	 * @return the login of current user identified
	 */
	String getCurrentUser();

}
