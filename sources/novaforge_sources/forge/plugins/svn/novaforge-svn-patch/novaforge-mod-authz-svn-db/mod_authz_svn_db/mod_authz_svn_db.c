/* 
 * ====================================================================
 * Copyright (c) 2003-2005 CollabNet.  All rights reserved.
 *
 * This software is licensed as described in the file LICENSE, which
 * you should have received as part of this distribution. 
 *
 * This software consists of voluntary contributions made by many
 * individuals.  For exact contribution history, see the revision
 * history and logs, available at http://subversion.tigris.org/.
 * ====================================================================
 * 
 * Copyright (c) 2007, Christopher Wojno
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *		 notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * 		 copyright notice, this list of conditions and the following
 *		 disclaimer in the documentation and/or other materials provided
 * 		 with the distribution.
 *     * The names of its contributors may be used to endorse or promote
 * 		 products derived from this software without specific prior
 * 		 written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * 
 * Ideally, you use this file as a jump off point to write the database
 * wrappers. The idea is to make sure everything is self-contained and 
 * the only dependency is the database-specific code.
 *
 * mod_authz_svn_db.h includes the functions a potential module developer will
 * need to create in order to develop a working module.
 */

#include "mod_authz_svn_db.h"

extern module AP_MODULE_DECLARE_DATA AUTHZ_SVN_DB_MODULE_NAME;

/** OK: Access might be granted, but nothing available to deny it */
static const ACCESS_RETURN ACCOK = 0;
/** REJECT: Access is denied */
static const ACCESS_RETURN ACCREJ = 1;
/** DATABASE ERROR: Error parsing, connecting, etc.  Problem with the database or configuration */
static const ACCESS_RETURN ACCDBERR = 2;

static bool strlen_safe( const char *unesc_str_buff, size_t *unesc_str_len )
{
	/* Detect if the strlen(unesc_str_buff) will encounter an arithmetic overflow error */
	*unesc_str_len = strlen(unesc_str_buff);
	if( unesc_str_buff[*unesc_str_len] != '\0' )
	{
		/* The str length reported by strlen is not the actual length.  This index should
		   point to the termination byte, but it doesn't.  strlen overflowed, so we won't
		   alloc enough memory for pstrcat. */
		return false;
	}
	return true;
}

static bool strlen_arith_overflow_check_times_2_plus_1( size_t str_len )
{
	if( ((SIZE_MAX - 1) >> 1) < str_len )
    {
		/* The username enterred will overflow the buffer when it's un-escaped */
		return false;
	}
	/* Everything's OK */
	return true;
}

/** Get Parent Path
 *
 * Looks at the path and returns the parent directory, or, the current directory if the path is not a directory, but a file.
 *
 * @param[in] path The path to get the parent of
 * @param[in] pool The memory pool to use when allocating strings (See the Apache docs about pools)
 * @return The parent path, or "/" if already at the root of the path
 */
static const char *getParentPath( const char *path, apr_pool_t *pool )
{
	const char *curr;
	/* Format it if necessary */
	
	/* Seek to end */
	/* At the beginning of path: nothing in path */
	if( strlen(path) == 0 ) { return apr_pstrcat( pool, "/", NULL ); }
	/* At beginning of path, should be a slash */
	else if( strlen(path) == 1 ) { return apr_pstrcat( pool, "/", NULL ); }
	curr = path+strlen(path)-1;
	/* Skip over trailing slash(s) */
	while( *curr == '/' )
	{
		--curr;
		/* See if we're at the beginning after skipping the ending slash */
		if( curr == path ) { return apr_pstrcat( pool, "/", NULL ); }
	}
	/* Have not reached the beginning */
	while( curr != path )
	{
		/* found a slash */
		if( *curr == '/' )
		{
			/* Get rid of extra slashes */
			while( *curr == '/' )
			{
				--curr;
				/* See if we're at the beginning after skipping the ending slash */
				if( curr == path ) { return apr_pstrcat( pool, "/", NULL ); }
			}
			/* Not the beginning slash, copy the little string */
			curr = apr_pstrmemdup( pool, path, curr-path+1);
			return curr;
		}
		/* Not a slash, move toward the front of the string and keep going */
		--curr;
	}
	/* No slash? malformed, return a normal slash */
	return apr_pstrcat( pool, "/", NULL );
}

/** Is Configured to Run
 *
 * Checks to make sure that the module has been properly configured in apache.  If something is missing, the module should not be allowed to execute.
 *
 * @param[in] r The request
 * @param[in] conf The configuration. Check this to make sure everything is in order so we can run properly
 */
static bool authz_svn_db_configured_to_run(
		request_rec *r,
		authz_svn_db_config_rec *conf )
{
	if( conf->database_port == 0 )
	{
		ap_log_rerror(APLOG_MARK, APLOG_ERR,
				0,
				r, "Database Connection Port: AuthzSVNDBPort Not set. Please specify this in the configuration file.  Required to run %s", AUTHZ_SVN_DB_MODULE_NAME_STRING
				);
	}
	else if( conf->database_name == NULL )
	{
		ap_log_rerror(APLOG_MARK, APLOG_ERR,
				0,
				r, "Database Name: AuthzSVNDBName Not set. Please specify this in the configuration file.  Required to run %s", AUTHZ_SVN_DB_MODULE_NAME_STRING
				);
	}
	else if( conf->database_user_name == NULL )
	{
		ap_log_rerror(APLOG_MARK, APLOG_ERR,
				0,
				r, "Database Access User Name: AuthzSVNDBUsername Not set. Please specify this in the configuration file.  Required to run %s", AUTHZ_SVN_DB_MODULE_NAME_STRING
				);
	}
	else
	{
		/* Everything is good */
		return true;
	}
	return false;
}

/** Table Prefix And...
  *
  * Creates a string compile time. Makes changing the prefix easy without the overhead of run-time concatenation.
  *
  * @param[in] text The text to append to the prefix.  Do NOT quote it.
  * @return "authz_svn_TEXT" where TEXT is the text parameter.
  */
#define TPREFIXAND( text ) "authz_svn_"#text

/* Configuration
 * 
 * This is the "constructor".  Sets the default values for the data. This is done every connection to the server. It specifies the default values.
 *
 * @param[in] p The Apache data pool (this is where we store our data so Apache can clean it up for us).
 * @param[in] d This is the base path directory.
 */
static void *create_authz_svn_db_dir_config(
	apr_pool_t *p,
	char *d)
{
	authz_svn_db_config_rec *conf = apr_pcalloc(p, sizeof(*conf));

	/* By default keep the fortress secure */
	conf->is_active = false;
	conf->authoritative = true;
	conf->database_host = "localhost";
	conf->database_port = 0;
	conf->database_name = NULL;
	conf->database_user_name = NULL;
	conf->password_file = NULL;
	conf->database_password = "";
	conf->database_user_table_name = TPREFIXAND( user );
	conf->database_user_name_column_name = "name";
	conf->database_user_id_column_name = "id";
	conf->database_repository_table_name = TPREFIXAND( repository );
	conf->database_repository_name_column_name = "name";
	conf->database_repository_id_column_name = "id";
	conf->database_groupmembership_table_name = TPREFIXAND( groupmembership );
	conf->database_groupmembership_user_column_name = "user_id";
	conf->database_groupmembership_group_column_name = "group_id";
	conf->database_userpermission_table_name = TPREFIXAND( userpermission );
	conf->database_userpermission_user_column_name = "user_id";
	conf->database_userpermission_repopath_column_name = "repository_id";
	conf->database_userpermission_read_column_name = "read";
	conf->database_userpermission_write_column_name = "write";
	conf->database_userpermission_recursive_column_name = "recursive";
	conf->database_grouppermission_table_name = TPREFIXAND( grouppermission );
	conf->database_grouppermission_group_column_name = "group_id";
	conf->database_grouppermission_repopath_column_name = "repository_id";
	conf->database_grouppermission_read_column_name = "read";
	conf->database_grouppermission_write_column_name = "write";
	conf->database_grouppermission_recursive_column_name = "recursive";
	conf->database_repopath_table_name = TPREFIXAND( repopath );
	conf->database_repopath_path_column_name = "path";
	conf->database_repopath_repo_column_name = "repository_id";
	conf->database_repopath_id_column_name = "id";
	conf->database_connection_handle = NULL;

	/* format the key */
	conf->unique_key_name = apr_pstrcat( p, AUTHZ_SVN_DB_MODULE_NAME_STRING, d, NULL );
	conf->password_file_key = apr_pstrcat(p, conf->unique_key_name, ":", "password_file_key", NULL);
	conf->database_connection_key = apr_pstrcat(p, conf->unique_key_name, ":", "database_connection_key", NULL);

	conf->base_path = d;

	/* Call the custom code */
	authz_svn_db_config_dir_spec( conf, p );

	/* return the new data pointer (so the core can track it) */
	return conf;
}
/* Don't need this any more, make sure we don't step on anyone's toes */
#undef TPREFIXAND

/* Command Encoding */
static const command_rec authz_svn_cmds[] =
{
	AP_INIT_FLAG("AuthzSVNDBIsActive", ap_set_flag_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, is_active),
			OR_AUTHCFG,
			"Set to 'Ofn' to active this module (default is Off.)"),
	AP_INIT_FLAG("AuthzSVNDBAuthoritative", ap_set_flag_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, authoritative),
			OR_AUTHCFG,
			"Set to 'Off' to allow access control to be passed along to "
			"lower modules. (default is On.)"),
	/* Database Host */
	AP_INIT_TAKE1("AuthzSVNDBHost", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_host),
			OR_AUTHCFG,
			"The host name of the machine on which the database resides."),
	/* database port */
	AP_INIT_TAKE1("AuthzSVNDBPort", ap_set_int_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_port),
			OR_AUTHCFG,
			"The host's port number through which to access the database."),
	/* Database Name */
	AP_INIT_TAKE1("AuthzSVNDBName", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_name),
			OR_AUTHCFG,
			"The name of the database to access."),
	/* Database User Name */
	AP_INIT_TAKE1("AuthzSVNDBUsername", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_user_name),
			OR_AUTHCFG,
			"The user name to use when accessing the database (database authentication)."),
	/* Database Password */
	AP_INIT_TAKE1("AuthzSVNDBPassword", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_password),
			OR_AUTHCFG,
			"Optional. The password to use when accessing the database (database authentication), overrides the specified password file. It is recommended that a password file be used instead of this directive if the configuration file is publicly visible"),
	/* Database User Table Name */
	AP_INIT_TAKE1("AuthzSVNDBUserTable", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_user_table_name),
			OR_AUTHCFG,
			"The table containing the user names to lookup."),
	/* Database User Column Name */
	AP_INIT_TAKE1("AuthzSVNDBUserColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_user_name_column_name),
			OR_AUTHCFG,
			"The column in the user table containing the user names."),
	/* Database User Column ID */
	AP_INIT_TAKE1("AuthzSVNDBUserIDColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_user_id_column_name),
			OR_AUTHCFG,
			"The column in the user table containing the user id."),
	/* Database Repository Table Name */
	AP_INIT_TAKE1("AuthzSVNDBRepositoryTable", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repository_table_name),
			OR_AUTHCFG,
			"The table containing the repository names (not the paths)."),
	/* Database Repository Column Name */
	AP_INIT_TAKE1("AuthzSVNDBRepositoryColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repository_name_column_name),
			OR_AUTHCFG,
			"The column in the repository table containing the repository names (not the paths)."),
	/* Database Repository Column ID */
	AP_INIT_TAKE1("AuthzSVNDBRepositoryIDColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repository_id_column_name),
			OR_AUTHCFG,
			"The column in the repository table containing the repository id."),
	/* Database Group Membership Table Name */
	AP_INIT_TAKE1("AuthzSVNDBGroupMembershipTable", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_groupmembership_table_name),
			OR_AUTHCFG,
			"The table containing the correlation between user id's and group id's (membership)."),
	/* Database Group Membership User Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGroupMembershipUserColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_groupmembership_user_column_name),
			OR_AUTHCFG,
			"The column in the group table containing the correlation between user id's and group id's (membership), this is where the user id is found."),
	/* Database Group Membership Group Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGroupMembershipGroupColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_groupmembership_group_column_name),
			OR_AUTHCFG,
			"The column in the group table containing the correlation between user id's and group id's (membership), this is where the group id is found."),
	/* Database User Permission Table Name */
	AP_INIT_TAKE1("AuthzSVNDBUPermissionTable", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_userpermission_table_name),
			OR_AUTHCFG,
			"The table containing user permissions for a particular repository path."),
	/* Database User Permission User Column Name */
	AP_INIT_TAKE1("AuthzSVNDBUPermissionUserColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_userpermission_user_column_name),
			OR_AUTHCFG,
			"Specifies column for which user is getting the permissions."),
	/* Database User Permission Path Column Name */
	AP_INIT_TAKE1("AuthzSVNDBUPermissionPathColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_userpermission_repopath_column_name),
			OR_AUTHCFG,
			"Specifies column for which path permissions are associated."),
	/* Database User Permission Read Column Name */
	AP_INIT_TAKE1("AuthzSVNDBUPermissionReadColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_userpermission_read_column_name),
			OR_AUTHCFG,
			"Specifies which column is the boolean read permission."),
	/* Database User Permission Write Column Name */
	AP_INIT_TAKE1("AuthzSVNDBUPermissionWriteColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_userpermission_write_column_name),
			OR_AUTHCFG,
			"Specifies which column is the boolean write permission."),
	/* Database User Permission Recursive Column Name */
	AP_INIT_TAKE1("AuthzSVNDBUPermissionRecursiveColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_userpermission_recursive_column_name),
			OR_AUTHCFG,
			"Specifies which column is the boolean recursive (right of permissions to recursively be applied to directores) permission."),
	/* Database Group Permission Table Name */
	AP_INIT_TAKE1("AuthzSVNDBGPermissionTable", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_grouppermission_table_name),
			OR_AUTHCFG,
			"The table containing group permissions for a particular repository path."),
	/* Database Group Permission Group Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGPermissionGroupColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_grouppermission_group_column_name),
			OR_AUTHCFG,
			"Specifies column for which group is getting the permissions."),
	/* Database Group Permission Path Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGPermissionPathColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_grouppermission_repopath_column_name),
			OR_AUTHCFG,
			"Specifies column for which path permissions are associated."),
	/* Database Group Permission Read Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGPermissionReadColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_grouppermission_read_column_name),
			OR_AUTHCFG,
			"Specifies which column is the boolean read permission."),
	/* Database Group Permission Write Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGPermissionWriteColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_grouppermission_write_column_name),
			OR_AUTHCFG,
			"Specifies which column is the boolean write permission."),
	/* Database Group Permission Write Column Name */
	AP_INIT_TAKE1("AuthzSVNDBGPermissionRecursiveColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_grouppermission_recursive_column_name),
			OR_AUTHCFG,
			"Specifies which column is the boolean recursive (right of permissions to recursively be applied to directores) permission."),
	/* Database Repository Path Table Name */
	AP_INIT_TAKE1("AuthzSVNDBRepoPathTable", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repopath_table_name),
			OR_AUTHCFG,
			"Specifies in which table the repository paths are located."),
	/* Database Repository Path Path Column Name */
	AP_INIT_TAKE1("AuthzSVNDBRepoPathPathColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repopath_path_column_name),
			OR_AUTHCFG,
			"Specifies in which column the repository path is named."),
	/* Database Repository Path Repository Name Column Name */
	AP_INIT_TAKE1("AuthzSVNDBRepoPathRepositoryColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repopath_repo_column_name),
			OR_AUTHCFG,
			"Specifies in which column the repository is specifed (which repository the path refers to)."),
	/* Database Repository Path ID Column Name */
	AP_INIT_TAKE1("AuthzSVNDBRepoPathIDColumn", ap_set_string_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, database_repopath_id_column_name),
			OR_AUTHCFG,
			"Specifies in which column the repository path id is located."),
	/* Database Password Storage Location */
	AP_INIT_TAKE1("AuthzSVNDBPasswordFile", ap_set_file_slot,
			(void *)APR_OFFSETOF(authz_svn_db_config_rec, password_file),
			OR_AUTHCFG,
			"Specifies the file in which the password associated with the database username is kep. Be sure it is not in an exposed location (restrict read permissions). Specify the absolute path. Is overridden by AuthzSVNDBPassword"),
	{ NULL }
};

/** Database Cleanup
 *
 * Calls the module's code to clean up the DB. Now, I do some tricky memory stuff, so I have to undo that so the module writer doesn't hhave to think about it.  Well, not too tricky. Non-the-less, it's done for convenience.  This function is called then the database memory is just about to be deleted. Therefore, we should close up the connection
 *
 * @param[in] d The data to delete, in this case, it's a pointer to the database connection handle
 * @return Always succeed, we're humped at this point if we don't succeed.
 */
static apr_status_t authz_svn_db_database_cleanup(
	void *d )
{
	authz_svn_db_conn_close( *((void**)d) );
	return APR_SUCCESS;
}

authz_svn_db_query_result_set* create_authz_svn_db_query_result_set( apr_pool_t* pool, unsigned int count )
{
	authz_svn_db_query_result_set* res;
	if( count == 0 )
		return NULL;
	res = apr_palloc( pool, sizeof(authz_svn_db_query_result_set) );
	res->count = count;
	/* pcalloc used here to default values to null/false */
	res->results = apr_pcalloc( pool, sizeof(authz_svn_db_query_result)*count );
	return res;
}

/** Generate Permission Requirements
 *
 * Based on what the user is asking for, figures out what permissions are required for that action.
 *
 * @param[in] request_method This is what is being requested
 * @return The permissions structure with the permissions required by the request
 */
static authz_svn_db_access_t authz_svn_db_generatePermissionRequirements(
	int request_method )
{
	authz_svn_db_access_t ret;
	/* initialize */
	ret.read = ret.write = ret.recursive = false;

	switch( request_method )
	{
		/* All methods requiring read access to all subtrees of r->uri */
		case M_COPY:
			ret.recursive = true;

			/* All methods requiring read access to r->uri */
		case M_OPTIONS:
		case M_GET:
		case M_PROPFIND:
		case M_REPORT:
			ret.read = true;
			break;

			/* All methods requiring write access to all subtrees of r->uri */
		case M_MOVE:
		case M_DELETE:
			ret.recursive = true;

			/* All methods requiring write access to r->uri */
		case M_MKCOL:
		case M_PUT:
		case M_PROPPATCH:
		case M_CHECKOUT:
		case M_MERGE:
		case M_MKACTIVITY:
		case M_LOCK:
		case M_UNLOCK:
			ret.write = true;
			break;

		default:
			/* Require most strict access for unknown methods */
			ret.write = ret.recursive = true;
			break;
	}
	return ret;
}

/** Sanity Check and Parse URI's
 *
 * Format the URI's requested by the user into repository name and path.  Ensure that formatting succeeded as well.
 *
 * @param[in] r The request structure
 * @param[in] base_path This is the base path from the configuration. This contains everything, the repository path and name
 * @param[in] repos_path This is the returned path parsed within the repository out of the base_path
 * @param[in] dest_repos_name This is the destination repository when something is being sent to another repository (as in a SVN copy)
 * @param[in] dest_repos_path This is the path associated with the dest_repos_name
 * @param[in] repos_path_ref This is the request URI parsed into the form: repos_name:repos_path
 * @param[in] dest_repos_path_ref This is the request URI parsed into the form: repos_name:repos_path, however it is only used for commands that send data to other locations, again, such as SVN copy.
 * @return OK for success, DECLINED on non-critical failure or that access should be denied for some reason, otherwise a standard HTTP error
 */
static int authz_svn_db_sanityCheckAndParseURIs(
	request_rec *r,
	const char *base_path,
	const char** repos_name,
	const char **repos_path,
	const char **dest_repos_name,
	const char **dest_repos_path,
	const char **repos_path_ref,
	const char **dest_repos_path_ref )
{
	/* Destination URI after un-escaping the path (used temporarily) */
	apr_uri_t parsed_dest_uri;
	/* Destination URI (from the request header) */
	const char *dest_uri;
	/* The error dav reports after a call */
	dav_error *dav_err;
	/* Never used except with the dav calls (result is discarded): use cannot be determined (other than it is required in the function call parameter) */
	const char *cleaned_uri;
	/* Also related to the dav call, it is never used */
	int trailing_slash;
	/* storage location for the relative path of ?: Not used (place holder in dav call?) */
	const char *relative_path;

	/* sanity check the uri, extract the repository path */
	dav_err = dav_svn_split_uri(r,
			r->uri,
			base_path,
			&cleaned_uri,
			&trailing_slash,
			repos_name,
			&relative_path,
			repos_path);

	/* error extracing path */
	if (dav_err)
	{
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
				"%s  [%d, #%d]",
				dav_err->desc, dav_err->status, dav_err->error_id);
		/* Ensure that we never allow access by dav_err->status */
		return (dav_err->status != OK && dav_err->status != DECLINED) ?
			dav_err->status : HTTP_INTERNAL_SERVER_ERROR;
	}

	/* Ignore the URI passed to MERGE, like mod_dav_svn does.
	 * See issue #1821.
	 * XXX: When we start accepting a broader range of DeltaV MERGE
	 * XXX: requests, this should be revisited.
	 */
	if (r->method_number == M_MERGE)
	{
		*repos_path = NULL;
	}

	/* if the repository path was found, add a preceeding slash */
	if (*repos_path)
	{
		*repos_path = svn_path_join( "/", *repos_path, r->pool );
	}
	/* repository path NOT found */
	else
	{
		/* Default to the base path */
		*repos_path = apr_pstrcat( r->pool, "/", NULL );
	}

	/* Concatenate the repository name with the path:  'name:path' */
	*repos_path_ref = apr_pstrcat( r->pool, *repos_name, ":", *repos_path, NULL);

	/* Request is a MERGE or COPY */
	if ( r->method_number == M_MOVE || r->method_number == M_COPY )
	{
		/* Determine the URI requested (extract from request headers) to copy or merge to */
		dest_uri = apr_table_get(r->headers_in, "Destination");

		/* Decline MOVE or COPY when there is no Destination uri, this will
		 * cause failure.
		 */
		if (!dest_uri)
		{
			return DECLINED;
		}

		/* Get read to unescape the URI (for safety) and then escape it */
		apr_uri_parse(r->pool, dest_uri, &parsed_dest_uri);

		ap_unescape_url(parsed_dest_uri.path);
		/* dest_uri is now the unescaped version */
		dest_uri = parsed_dest_uri.path;
		/* Base paths match (nothing funny being pulled on us) */
		if (strncmp(dest_uri, base_path, strlen(base_path)))
		{
			/* If it is not the same location, then we don't allow it.
			 * XXX: Instead we could compare repository uuids, but that
			 * XXX: seems a bit over the top.
			 */
			return HTTP_BAD_REQUEST;
		}

		/* extract the repository name and path from the escaped destination URI */
		dav_err = dav_svn_split_uri(r,
				dest_uri,
				base_path,
				&cleaned_uri,
				&trailing_slash,
				dest_repos_name,
				&relative_path,
				dest_repos_path);

		if (dav_err)
		{
			ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
					"%s  [%d, #%d]",
					dav_err->desc, dav_err->status, dav_err->error_id);
			/* Ensure that we never allow access by dav_err->status */
			return (dav_err->status != OK && dav_err->status != DECLINED) ?
				dav_err->status : HTTP_INTERNAL_SERVER_ERROR;
		}

		/* Prepend a slash */
		if (*dest_repos_path)
		{
			*dest_repos_path = svn_path_join("/", *dest_repos_path, r->pool);
		}

		/* Save the 'name:path' of the escaped destination URI */
		*dest_repos_path_ref = apr_pstrcat(r->pool, *dest_repos_name, ":",
				*dest_repos_path, NULL);
	}
	return OK;
}

/** Crunch Permissions
 *
 * Evaluate the database results and authorize the access or deny the access
 *
 * @param[in] rs The results of the database query, all the possible paths in the requested repository the user may or may not have access to (they just need to be in the database)
 * @param[in] repos_path The repository path to the resource being requested
 * @param[in] perms The permissions required of the path in order for the access to be granted (must satisfy these)
 * @param[in] pool The apache datapool in which to store allocated information
 * @return ACCOK if the access is granted (it was granted for sure), ACCREJ if rejected
 */
ACCESS_RETURN authz_svn_db_crunch_permissions( authz_svn_db_query_result_set *rs, const char *repos_path, authz_svn_db_access_t *perms, apr_pool_t *pool )
{
	unsigned int rowsreturned;
	unsigned int i, maxctr;
	bool permissionflag;
	bool onechar;
	authz_svn_db_query_result* result;
	permissionflag = false;
	result = rs->results;
	rowsreturned = rs->count;
	/* Path matches EXACTLY */
	for( i = 0; i < rowsreturned; ++i, ++result )
	{
		/* Found a match */
		if( strcmp( repos_path, result->repo_path ) == 0 )
		{
			/* Do we have permission? */
			if( !( perms->read && !result->perms.read ) && !( perms->write && !result->perms.write && !( perms->recursive && !result->perms.recursive ) ) )
			{
				permissionflag = true;
			}
			/* No Permission, keep track that permission was denied at least once */
			else
			{
				return ACCREJ;
			}
		}
	}
	/* Found a match, but it didn't let us in */
	if( permissionflag )
	{
		return ACCOK;
	}

	/* Reset */
	permissionflag = false;

	/* By this point, we no longer can have an exact match, therefore, we NEED recursive permissions */

	/* Next path */
	repos_path = getParentPath( repos_path, pool );

	maxctr = 0;
	/* Until the path is exhausted
	 * NOTE: maxctr Is a safety precaution, if it tries to go up 100
	 * or more parent paths, this ensures the server does not go into
	 * an infinite loop */
	while( strlen(repos_path) >= 1 && maxctr < 100 )
	{
		onechar = strlen(repos_path) == 1;
		result = rs->results;
		/* For each result */
		for( i = 0; i < rowsreturned; ++i, ++result )
		{
			/* Found a match */
			if( strcmp( repos_path, result->repo_path ) == 0 )
			{
				/* Do we have permission? */
				if( !( perms->read && !result->perms.read ) && !( perms->write && !result->perms.write ) && result->perms.recursive )
				{
					permissionflag = true;
				}
				/* No Permission, keep track that permission was denied at least once */
				else
				{
					return ACCREJ;
				}
			}
		}
		/* Remember when we tracked permission denied at least once?  Well, we're here and we didn't find any permissions that let us in, so we must deny access to ensure recursiveness works */
		if( permissionflag )
		{
			return ACCOK;
		}
		if( onechar )
			break;
		repos_path = getParentPath( repos_path, pool );
		++maxctr;
	}
	/* We found no entries to deny or allow, REJECT by default */

	/* something wrong, rejection */
	return ACCREJ;
}

/** Check Repository Access
 *
 * Invokes the database-specific code, of course, it also sets up the environment for it.  Essentially, while not making the decision about access, it facilitates the decision making process.
 *
 * @param[in] r The request structure
 * @param[in] conf The configuration structure (filled out)
 * @param[in] repos_name The repository name to check access for
 * @param[in] repos_path This is the repository path within the above repository. You're making sure the user has access to this resource.
 * @param[in] authz_svn_db_type These are the permissions to enforce
 * @return OK if access is granted, DECLINED if error or access denied
 */
static int authz_svn_db_check_repository_access(
	request_rec *r,
	authz_svn_db_config_rec *conf,
	const char* repos_name,
	const char* repos_path,
	authz_svn_db_access_t *authz_svn_db_type )
{
	authz_svn_db_query_result_set *results = NULL;
	char anon[] = "anonymous";
	char errbuf[512];
	ACCESS_RETURN errtmp;
	const char *t_user = r->user;
	errbuf[0] = '\0';
	/* No user name is given, we'll let our anonymous code work */
	if( r->user == NULL )
	{
		t_user = anon;
	}

	if( NULL != conf->database_connection_handle )
	{
		/* Check to see if the repository is available to the user */
		errtmp = authz_svn_db_check_access(
				conf,
				t_user,
				repos_name,
				&results,
				r->connection->pool,
			errbuf,
			512	);

		if( errbuf[0] != '\0' )
		{
			ap_log_rerror(APLOG_MARK, APLOG_ERR,
					0,
					r, errbuf
					);
		}
		if( ACCOK == errtmp )
		{
			/* no results returned, therefore no access */
			if( results == NULL )
				return DECLINED;

			errtmp = authz_svn_db_crunch_permissions( results, repos_path, authz_svn_db_type, r->connection->pool );
			if( ACCOK == errtmp )
			{
				return OK;
			}
		}


		if( ACCREJ == errtmp )
		{
			/* access rejected */
			ap_log_rerror(APLOG_MARK, APLOG_INFO,
					0,
					r, "Access Denied from module: %s: Authentication Failure.", AUTHZ_SVN_DB_MODULE_NAME_STRING
					);
			return DECLINED;
		}
		else if( ACCDBERR == errtmp )
		{
			/* DB error */
			ap_log_rerror(APLOG_MARK, APLOG_ERR,
					0,
					r, "Failed to perform access control (database query failure)."
					);
			return DECLINED;
		}
		else
		{
			/* Unknown value */
			ap_log_rerror(APLOG_MARK, APLOG_WARNING,
					0,
					r, "Unrecognized access control query error value.  Please correct your module."
					);
			return DECLINED;
		}
	}
	return DECLINED;
}



/* Check Access Request
 *
 * Check if the current request R is allowed.
 *
 * @param[in] r The request structure (see Apache's documentation)
 * @param[in] conf Our configuration data
 * @param[out] repos_path_ref Upon exit, will contain the path and repository name on which an operation was requested in the form 'name:path'
 * @param[out] dest_repos_path_ref Contains the destination path if the requested operation was a MOVE or a COPY.
 * @return Returns OK when access is allowed, DECLINED when it isn't, or an HTTP_error code when an error occurred.
 */
static int req_check_access(request_rec *r,
		authz_svn_db_config_rec *conf,
		const char **repos_path_ref,
		const char **dest_repos_path_ref)
{
	/* Temporary Error location (for code neatness) */
	int errtmp;
	/* Holds the password file handle */
	FILE* passwdf = NULL;
	/* The name of our repository, the name in 'name:path' */
	const char *repos_name;
	/* The destination repository name (see the documentation of the parameters of this fucntion) */
	const char *dest_repos_name;
	/* Path to the repository, the path in: 'name:path' */
	const char *repos_path;
	/* Destination repository path, only for certain calls (MERGE/COPY) */
	const char *dest_repos_path = NULL;
	/* This is the access type desired from the user request (from the browser/client):  initialize it to nothing (no permission flags set) */
	authz_svn_db_access_t authz_svn_db_type;
	/* work space to format error messages */
	char errbuf[256];
	char password[32];
	const char *pwd_tmp;
	int passsz;

	/* Build the type of access needed based on the incoming request */
	authz_svn_db_type = authz_svn_db_generatePermissionRequirements( r->method_number );

	errtmp = authz_svn_db_sanityCheckAndParseURIs( r, conf->base_path, &repos_name, &repos_path, &dest_repos_name, &dest_repos_path, repos_path_ref, dest_repos_path_ref );
	if( OK != errtmp )
	{
		return errtmp;
	}

	/* Retrieve/cache password file */
	apr_pool_userdata_get((void**)&pwd_tmp, conf->password_file_key, r->connection->pool);
	/* Not in memory */
	if (pwd_tmp == NULL)
	{
		/* No password set */
		if( conf->database_password[0] == '\0' )
		{
			/* Password file is given */
			if( conf->password_file != NULL )
			{
				/* Try to read the file */
				passwdf = fopen( conf->password_file, "r" );
				if( passwdf == NULL )
				{
					ap_log_rerror(APLOG_MARK, APLOG_ERR,
							0,
							r, "Failed to open the AuthzSVNDBPasswordFile"
							);
					return DECLINED;
				}

				/* Read the password, cap the string with a NULL */
				passsz = fread( password, sizeof(char), 31, passwdf );
				password[passsz] = '\0';

				--passsz;
				/* Eat the end of lines */
				while( passsz >= 0 && ( password[passsz] == '\n' || password[passsz] == '\r' ) )
				{
					--passsz;
				}
				/* no more newlines, if the next character is not the EOS,
				   That means we moved our EOS, so place the EOS in that next
				   character. If the EOS did not move, we're already capped */
				if( password[passsz+1] != '\0' )
					password[passsz+1] = '\0';

				/* error occured */
				if( ferror( passwdf ) != 0 )
				{
					ap_log_rerror(APLOG_MARK, APLOG_ERR,
							0,
							r, "Failed to read the AuthzSVNDBPasswordFile"
							);
					fclose( passwdf );
					return DECLINED;
				}

				/* release the file */
				fclose( passwdf );
			}
			/* No password file given, use the default */
			else
			{
				/* Default password is set */
				if( conf->database_password != NULL )
				{
					strcpy( password, conf->database_password );
				}
				/* Default password NOT set, use a blank PW */
				else
				{
					/* Use a safe value, NULL string, not a NULL pointer */
					password[0] = '\0';
				}
			}

			conf->database_password = apr_pstrcat( r->connection->pool, password, NULL );

			/* Cache the password for the next request on this connection */
			apr_pool_userdata_set( conf->database_password, conf->password_file_key, NULL, r->connection->pool);
		}
		/* password set or read from the file */
	}
	/* Password is already loaded, tell the configuration */
	else
	{
		conf->database_password = pwd_tmp;
	}

	/* start up the database */
	apr_pool_userdata_get((void*)&conf->database_connection_handle, conf->database_connection_key, r->connection->pool);
	/* isn't saved yet */
	if( NULL == conf->database_connection_handle )
	{
		errbuf[0] = '\0';

		/* Allocate a pointer */
		conf->database_connection_handle = (void**)apr_palloc( r->connection->pool, sizeof( void** ) );

		errtmp = authz_svn_db_conn_open( conf, r->connection->pool, conf->database_connection_handle, errbuf, 256 );
		/* error opening the connection */
		if( NULL == *conf->database_connection_handle || 0 != errtmp )
		{
			ap_log_rerror(APLOG_MARK, APLOG_ERR,
					0,
					r, "Failed to Connect to the database: %d: %s",
					errtmp, errbuf);
			return DECLINED;
		}

		/* database open, save that puppy */
		apr_pool_userdata_set( conf->database_connection_handle, conf->database_connection_key, authz_svn_db_database_cleanup, r->connection->pool );
	}
	/* Handle is ready */
	

	/* Perform authz access control.
	 *
	 * First test the special case where repos_path == NULL, and skip
	 * calling the authz routines in that case.  This is an oddity of
	 * the DAV RA method: some requests have no repos_path, but apache
	 * still triggers an authz lookup for the URI.
	 *
	 * However, if repos_path == NULL and the request requires write
	 * access, then perform a global authz lookup.  The request is
	 * denied if the user commiting isn't granted any access anywhere
	 * in the repository.  This is to avoid operations that involve no
	 * paths (commiting an empty revision, leaving a dangling
	 * transaction in the FS) being granted by default, letting
	 * unauthenticated users write some changes to the repository.
	 * This was issue #2388.
	 *
	 * XXX: For now, requesting access to the entire repository always
	 * XXX: succeeds, until we come up with a good way of figuring
	 * XXX: this out.
	 */
	if (repos_path || (!repos_path && authz_svn_db_type.write))
	{
		if( authz_svn_db_check_repository_access( r, conf, repos_name, repos_path, &authz_svn_db_type ) != OK )
		{
			return DECLINED;
		}
	}

	/* XXX: MKCOL, MOVE, DELETE
	 * XXX: Require write access to the parent dir of repos_path.
	 */

	/* XXX: PUT
	 * XXX: If the path doesn't exist, require write access to the
	 * XXX: parent dir of repos_path.
	 */

	/* Only MOVE and COPY have a second uri we have to check access to. */
	if (r->method_number != M_MOVE
			&& r->method_number != M_COPY)
	{
		return OK;
	}

	/* Check access on the destination repos_path.  Again, skip this if
	   repos_path == NULL (see above for explanations) */
	if (repos_path)
	{
		authz_svn_db_type.recursive = true;
		authz_svn_db_type.write = true;
		if( authz_svn_db_check_repository_access( r, conf, dest_repos_name, dest_repos_path, &authz_svn_db_type ) != OK )
		{
			return DECLINED;
		}
	}

	/* XXX: MOVE and COPY, if the path doesn't exist yet, also
	 * XXX: require write access to the parent dir of dest_repos_path.
	 */

	return OK;
}

/* Log a message indicating the access control decision made about a
 * request.
 *
 * @param[in] r The request structure from Apache
 * @param[in] allowed is boolean, asserts that permission to the resource was granted or not
 * @param[in] repos_path The path to which the permissions are being evaluated
 * @param[in] dest_repos_path information about the request. Same as repos_path. May be NULL if request did not involve a destination path.
 */
static void log_access_verdict(const request_rec *r,
		int allowed,
		const char *repos_path,
		const char *dest_repos_path)
{
/* Log type: info if granted, error if denied */
	int level = allowed ? APLOG_INFO : APLOG_ERR;
	const char *verdict = allowed ? "granted" : "denied";

	/* valid user */
	if (r->user)
	{
		/* destination path invalid
		 * does this mean it was omitted or that there was an error? */
		if (dest_repos_path)
		{
			ap_log_rerror(APLOG_MARK, level, 0, r,
					"Access %s: '%s' %s %s %s", verdict, r->user,
					r->method, repos_path, dest_repos_path);
		}
		/*  */
		else
		{
			ap_log_rerror(APLOG_MARK, level, 0, r,
					"Access %s: '%s' %s %s", verdict, r->user,
					r->method, repos_path);
		}
	}
	/* invalid user */
	else
	{
		if (dest_repos_path)
		{
			ap_log_rerror(APLOG_MARK, level, 0, r,
					"Access %s: - %s %s %s", verdict,
					r->method, repos_path, dest_repos_path);
		}
		else
		{
			ap_log_rerror(APLOG_MARK, level, 0, r,
					"Access %s: - %s %s", verdict,
					r->method, repos_path);
		}
	}


}

/*
 * Hooks
 */

/** Access Check
  *
  * Determine if access is allowed for this user, at the repository path and destination repository path.  This is the hook that Apache will call when attempting to determine permissions
  *
  * @param[in,out] r The request structure
  * @return the status of the check: DECLINED to defer checking to some other module, OK to grant access, HTTP_FORBIDDEN if authorization failed, there are others: see req_check_access above.
  */
static int access_checker(request_rec *r)
{
	authz_svn_db_config_rec *conf = ap_get_module_config(r->per_dir_config,
			&AUTHZ_SVN_DB_MODULE_NAME);
	const char *repos_path;
	const char *dest_repos_path = NULL;
	int status;

	/* NOVAFORGE : Check FIRSTLY if the module is Active */
	if( !conf->is_active )
	{			
		return DECLINED;
	}

	/* We are not configured to run */
	if( !authz_svn_db_configured_to_run(r,conf) )
	{
		return DECLINED;
	}

	if (ap_some_auth_required(r))
	{
		/* It makes no sense to check if a location is both accessible
		 * anonymous and by an authenticated user (in the same request!).
		 */
		if (ap_satisfies(r) != SATISFY_ANY)
			return DECLINED;

		/* If the user is trying to authenticate, let him.  If anonymous
		 * access is allowed, so is authenticated access
		 */
		if (apr_table_get(r->headers_in,
					(PROXYREQ_PROXY == r->proxyreq)
					? "Proxy-Authorization" : "Authorization"))
		{
			/* Given Satisfy Any is in effect, we have to forbid access
			 * to let the auth_checker hook have a go at it.
			 */
			return HTTP_FORBIDDEN;
		}
	}

	/* PERFORM ACCESS CHECKING
	 * If anon access is allowed, return OK */
	status = req_check_access(r, conf, &repos_path, &dest_repos_path);
	if (status == DECLINED)
	{
		if (!conf->authoritative)
		{
			return DECLINED;
		}

		if (!ap_some_auth_required(r))
		{
			log_access_verdict(r, 0, repos_path, dest_repos_path);
		}

		return HTTP_FORBIDDEN;
	}

	if (status != OK)
	{
		return status;
	}


	/* LOG ACCESS CHECK RESULT */
	log_access_verdict(r, 1, repos_path, dest_repos_path);

	return OK;
}

/** Check User ID
  *
  * Determine if the user is allowed access to the repository.
  *
  * @param[in] r The request structure
  * @return OK if granted, DECLINED if rejected, or an HTTP error
  */
static int check_user_id(request_rec *r)
{
	authz_svn_db_config_rec *conf = ap_get_module_config(r->per_dir_config,
			&AUTHZ_SVN_DB_MODULE_NAME);
	const char *repos_path;
	const char *dest_repos_path = NULL;
	int status;

	/* We are not configured to run, or, an earlier module has already
	 * authenticated this request. */
	if ( !authz_svn_db_configured_to_run(r,conf) || r->user )
	{
		return DECLINED;
	}

	/* If anon access is allowed, return OK, preventing later modules
	 * from issuing an HTTP_UNAUTHORIZED.  Also pass a note to our
	 * auth_checker hook that access has already been checked. */
	status = req_check_access(r, conf, &repos_path, &dest_repos_path);
	if (status == OK)
	{
		apr_table_setn(r->notes, "anon-ok", (const char*)1);
		log_access_verdict(r, 1, repos_path, dest_repos_path);
		return OK;
	}

	return status;
}

/** Authorization Checker
  *
  * Checks to see if authorization to the specified path is allowed
  *
  * @param[in] r The request structure
  * @return OK if granted, DECLINED if rejected, or an HTTP error
  */
static int auth_checker(request_rec *r)
{
	authz_svn_db_config_rec *conf = ap_get_module_config(r->per_dir_config,
			&AUTHZ_SVN_DB_MODULE_NAME);
	const char *repos_path;
	const char *dest_repos_path = NULL;
	int status;

	/* We are not configured to run */

	if (!authz_svn_db_configured_to_run(r,conf))
	{	
		return DECLINED;
	}

	/* Previous hook (check_user_id) already did all the work,
	 * and, as a sanity check, r->user hasn't been set since then? */
	if (!r->user && apr_table_get(r->notes, "anon-ok" ) )
	{
		return OK;
	}

	/* check access */
	status = req_check_access(r, conf, &repos_path, &dest_repos_path);
	/* we deferred checking (we didn't discover permissions that would allow access) */
	if (status == DECLINED)
	{
		/* If we require authorization, report that it failed here */
		if (conf->authoritative)
		{
			/* log authorization required */
			log_access_verdict(r, 0, repos_path, dest_repos_path);
			/* save a failure note */
			ap_note_auth_failure(r);
			return HTTP_FORBIDDEN;
		}

		/* otherwise, defer to another module */
		return DECLINED;
	}

	/* Report non-OK status */
	if (status != OK)
	{
		return status;
	}

	/* Log the (successful) access attempt */
	log_access_verdict(r, 1, repos_path, dest_repos_path);

	return OK;
}

/*
 * Module flesh
 */

/*
#if AP_MODULE_MAGIC_AT_LEAST(20060110,0)
static void import_ap_satisfies(void)
{
	ap_satisfies = APR_RETRIEVE_OPTIONAL_FN(ap_satisfies);
}
#endif
*/

/* Register our code with apache */
static void register_hooks(apr_pool_t *p)
{
	static const char * const mod_ssl[] = { "mod_ssl.c", NULL };

	ap_hook_access_checker(access_checker, NULL, NULL, APR_HOOK_LAST);
	/* Our check_user_id hook must be before any module which will return 
	 * HTTP_UNAUTHORIZED (mod_auth_basic, etc.), but after mod_ssl, to
	 * give SSLOptions +FakeBasicAuth a chance to work. */
	ap_hook_check_user_id(check_user_id, mod_ssl, NULL, APR_HOOK_FIRST);
	ap_hook_auth_checker(auth_checker, NULL, NULL, APR_HOOK_FIRST);

/*
#if AP_MODULE_MAGIC_AT_LEAST(20060110,0)
	ap_hook_optional_fn_retrieve(import_ap_satisfies, NULL, NULL, APR_HOOK_MIDDLE);
#endif
*/
}

module AP_MODULE_DECLARE_DATA AUTHZ_SVN_DB_MODULE_NAME = 
{
	STANDARD20_MODULE_STUFF,
	create_authz_svn_db_dir_config,
	NULL,NULL,NULL,authz_svn_cmds,register_hooks
};

