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
package org.novaforge.forge.commons.ldap.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.ldap.exceptions.LdapException;
import org.novaforge.forge.commons.ldap.exceptions.LdapExceptionCode;
import org.novaforge.forge.commons.ldap.internal.model.LdapUserImpl;
import org.novaforge.forge.commons.ldap.model.LdapUser;
import org.novaforge.forge.commons.ldap.services.LdapService;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.text.MessageFormat;
import java.util.Hashtable;

/**
 * @author petrettf
 */

public class LdapServiceImpl implements LdapService
{
	private static final Log	log															= LogFactory.getLog(LdapServiceImpl.class);

	private DirContext				rootDirContext									= null;
	private String						baseDN;
	private String						initialContextFactory;
	private String						providerUrl;
	// simple
	private String						securityAuthentication;
	// cn=root,dc=novaforge,dc=org
	private String						securityPrincipal;
	private String						securityCredentials;
	// follow ...
	private String						referralMode;
	// uid={0}
	private String						userSearchFilter;

	// uid
	private String						uidAttribute;
	// userPassword
	private String						userPasswordAttribute;
	// sn
	private String						surnameAttribute;
	// givenname
	private String						givennameAttribute;
	// mail
	private String						mailAttribute;
	// preferredLanguage
	private String						preferredLanguageAttribute;

	private String						defaultLanguageAttribute				= "FR";

	private boolean						authorizeJpaAccessWhenLdapDown	= true;
	private boolean						authorizeCreateJpaAccess				= false;
	private boolean						authorizeToUpdateLanguage				= true;

	/**
	 * Method to be called once all the properties have been injected.
	 */
	public void init()
	{
		try
		{
			log.info("INITIAL_CONTEXT_FACTORY : " + this.initialContextFactory);
			log.info("PROVIDER_URL : " + this.providerUrl);
			log.info("SECURITY_PRINCIPAL : " + this.securityPrincipal);
			log.info("SECURITY_AUTHENTICATION : " + this.securityAuthentication);
			log.info("SECURITY_CREDENTIALS : " + this.securityCredentials);

			getDirContext();
			log.info("Connection LDAP is ok");
		}
		catch (LdapException e)
		{
			log.error("Error to initializing LDAP. Error when initializing DirContext for LDAP");
		}
	}

	private void getDirContext() throws LdapException
	{
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
		env.put(Context.PROVIDER_URL, this.providerUrl);
		env.put(Context.SECURITY_AUTHENTICATION, this.securityAuthentication);
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
		env.put(Context.REFERRAL, referralMode);
		try
		{
			this.rootDirContext = new InitialDirContext(env);
		}
		catch (AuthenticationException e)
		{
			throw new LdapException(LdapExceptionCode.LDAP_AUTHENTIFICATION_ERROR,
															"Error to authentificate LDAP. Invalid Credentials for LDAP", e);
		}
		catch (NamingException e)
		{
			throw new LdapException(LdapExceptionCode.LDAP_ACCESS_ERROR,
															"Error to initializing LDAP. Error when initializing DirContext for LDAP", e);
		}
	}

	/**
	 * Method to be called when the Blueprint Container is destroying the object instance.
	 */
	public void destroy()
	{
		try
		{
			rootDirContext.close();
		}
		catch (NamingException e)
		{
			log.warn("stopping... close DirContext for LDAP", e);
		}
		log.info("stop...");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authentification(String user, String passwd, String dn) throws LdapException
	{
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, this.initialContextFactory);
		env.put(Context.PROVIDER_URL, this.providerUrl + "/" + baseDN);
		env.put(Context.SECURITY_AUTHENTICATION, this.securityAuthentication);
		env.put(Context.SECURITY_PRINCIPAL, dn);
		env.put(Context.SECURITY_CREDENTIALS, passwd);
		env.put(Context.REFERRAL, referralMode);
		// env.put(Context.SECURITY_PROTOCOL, "ssl");

		log.info("PROVIDER_URL : " + this.providerUrl + "/" + baseDN);
		log.info("SECURITY_PRINCIPAL : " + dn);
		try
		{
			DirContext userDirContext = new InitialDirContext(env);
			userDirContext.close();
		}
		catch (AuthenticationException e)
		{
			throw new LdapException(LdapExceptionCode.LDAP_AUTHENTIFICATION_ERROR,
					"Error to authentificate LDAP. Invalid Credentials for LDAP", e);
		}
		catch (NamingException e)
		{
			throw new LdapException(LdapExceptionCode.LDAP_ACCESS_ERROR,
					"Error to authentificate LDAP. Error when initializing DirContext for LDAP", e);
		}
		return true;
	}

	public void setBaseDN(String baseDN)
	{
		this.baseDN = baseDN;
	}

	public void setInitialContextFactory(String initialContextFactory)
	{
		this.initialContextFactory = initialContextFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LdapUser searchUser(final String pLogin) throws LdapException
	{
		LdapUser ldapUser = new LdapUserImpl();
		String userId = "";
		String userPassword = "";
		String dn = "";
		String sn = "";
		String gn = "";
		String mail = "";
		String language = defaultLanguageAttribute;
		NamingEnumeration<SearchResult> resultat = null;
		String filter = "";
		int neSize = 0;

		if (log.isDebugEnabled())
		{
			log.debug(String.format("Input : login=%s", pLogin));
		}
		try
		{
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// using the User Search Filter "uid={0}"
			MessageFormat form = new MessageFormat(this.userSearchFilter);
			Object[] filterArg = { pLogin };
			filter = form.format(filterArg);

			// Search for objects that have those matching attributes
			resultat = this.rootDirContext.search(this.baseDN, filter, searchControls);

			while (resultat.hasMore())
			{
				neSize++;
				SearchResult sr = resultat.next();
				log.info("user DN : " + sr.getNameInNamespace());
				Attributes attrs = sr.getAttributes();

				// DN
				ldapUser.setDn(sr.getNameInNamespace());
				log.info("DN found : " + dn);

				// uid
				Attribute attr = attrs.get(uidAttribute);
				if (attr != null)
				{
					userId = (String) attr.get();
				}
				else
				{
					throw new LdapException(LdapExceptionCode.LDAP_USER_UID_ERROR,
							"Error to find LDAP User. User with login=%s has not an uid : " + pLogin);
				}
				ldapUser.setUid(userId);

				// uid
				attr = attrs.get(userPasswordAttribute);
				byte[] userPasswordByte = null;
				if (attr != null)
				{
					userPasswordByte = (byte[]) (attr.get());
					userPassword = new String(userPasswordByte);
				}
				ldapUser.setUserPassword(userPassword);

				// sn
				attr = attrs.get(surnameAttribute);
				if (attr != null)
				{
					sn = (String) attr.get();
				}
				ldapUser.setSurName(sn);

				// givenname
				attr = attrs.get(givennameAttribute);
				if (attr != null)
				{
					gn = (String) attr.get();
				}
				ldapUser.setGivenName(gn);

				// email
				attr = attrs.get(mailAttribute);
				if (attr != null)
				{
					mail = (String) attr.get();
				}
				ldapUser.setMail(mail);

				// language
				attr = attrs.get(preferredLanguageAttribute);
				if (attr != null)
				{
					language = (String) attr.get();
				}
				ldapUser.setPreferredLanguage(language);

			}
			resultat.close();
			if (neSize == 0)
			{
				throw new LdapException(LdapExceptionCode.LDAP_USER_NOT_FOUND,
						"Error to find LDAP User. No user found into ldap server with login=%s : " + pLogin);
			}
			else if (neSize > 1)
			{
				throw new LdapException(LdapExceptionCode.LDAP_MANY_USER_FOUND,
						"Error to find LDAP User. Multiple users found into ldap server with login=%s : " + pLogin);
			}
		}
		catch (NamingException na)
		{
			throw new LdapException("Error to find LDAP User. Error when searching user with login=%s : " + pLogin,
					na);
		}
		return ldapUser;
	}

	public void setProviderUrl(String providerUrl)
	{
		this.providerUrl = providerUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LdapUser getUser(final String pLogin, final String pPasswd) throws LdapException
	{
		LdapUser ldapUser = new LdapUserImpl();
		LdapUser ldapUserFound = searchUser(pLogin);
		if (authentification(pLogin, pPasswd, ldapUserFound.getDn()))
		{
			ldapUser = ldapUserFound;
		}
		return ldapUser;
	}

	public void setSecurityAuthentication(String securityAuthentication)
	{
		this.securityAuthentication = securityAuthentication;
	}

	public void setSecurityPrincipal(String securityPrincipal)
	{
		this.securityPrincipal = securityPrincipal;
	}

	public void setSecurityCredentials(String securityCredentials)
	{
		this.securityCredentials = securityCredentials;
	}

	public void setReferralMode(String referralMode)
	{
		this.referralMode = referralMode;
	}

	public void setUserSearchFilter(String userSearchFilter)
	{
		this.userSearchFilter = userSearchFilter;
	}

	public void setUidAttribute(String uidAttribute)
	{
		this.uidAttribute = uidAttribute;
	}

	public void setUserPasswordAttribute(String userPasswordAttribute)
	{
		this.userPasswordAttribute = userPasswordAttribute;
	}

	public void setSurnameAttribute(String surnameAttribute)
	{
		this.surnameAttribute = surnameAttribute;
	}

	public void setGivennameAttribute(String givennameAttribute)
	{
		this.givennameAttribute = givennameAttribute;
	}

	public void setMailAttribute(String mailAttribute)
	{
		this.mailAttribute = mailAttribute;
	}

	public void setPreferredLanguageAttribute(String preferredLanguageAttribute)
	{
		this.preferredLanguageAttribute = preferredLanguageAttribute;
	}

	public void setDefaultLanguageAttribute(String defaultLanguageAttribute)
	{
		this.defaultLanguageAttribute = defaultLanguageAttribute;
	}

	public void setAuthorizeJpaAccessWhenLdapDown(boolean authorizeJpaAccessWhenLdapDown)
	{
		this.authorizeJpaAccessWhenLdapDown = authorizeJpaAccessWhenLdapDown;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAuthorizeJpaAccessWhenLdapDown()
	{
		return authorizeJpaAccessWhenLdapDown;
	}

	public void setAuthorizeCreateJpaAccess(boolean authorizeCreateJpaAccess)
	{
		this.authorizeCreateJpaAccess = authorizeCreateJpaAccess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAuthorizeCreateJpaAccess()
	{
		return authorizeCreateJpaAccess;
	}

	public void setAuthorizeToUpdateLanguage(boolean authorizeUpdateLanguage)
	{
		this.authorizeToUpdateLanguage = authorizeUpdateLanguage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getAuthorizeToUpdateLanguage()
	{
		return authorizeToUpdateLanguage;
	}
}