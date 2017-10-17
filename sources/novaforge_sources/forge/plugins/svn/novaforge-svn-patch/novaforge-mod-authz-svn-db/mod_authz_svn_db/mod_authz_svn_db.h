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
 */

#ifndef MOD_AUTHZ_SVN_DB_MOD
#define MOD_AUTHZ_SVN_DB_MOD

/* Make sure they define the module name
 *
 * NOTE:  YOU MUST #define AUTHZ_SVN_DB_MODULE_NAME as it will re-write the .h/.c files so that your module can be called what you want it to be called.  It is very important that this is specified.  You should also specify AUTHZ_SVN_DB_MODULE_NAME, which is the same name as the AUTHZ_SVN_DB_MODULE_NAME only surrounded in quotes. This is important to avoid memory collisions.
 */
#ifndef AUTHZ_SVN_DB_MODULE_NAME
#error You Must Define AUTHZ_SVN_DB_MODULE_NAME with the name of your module: i.e. authz_svn_db_pgsql_module or authz_svn_db_mysql_module
#endif
#ifndef AUTHZ_SVN_DB_MODULE_NAME_STRING
/* This string ID's the modules. It's OK if you only plan on using ONE type of authz_svn_db module, but you need to change this if you want postgre and mysql driven auth to interoperate (or any other 2 database modules) */
#define AUTHZ_SVN_DB_MODULE_NAME_STRING "authz_svn_db_module"
#endif

#include <stdbool.h>

#include <httpd.h>
#include <http_config.h>
#include <http_core.h>
#include <http_request.h>
#include <http_protocol.h>
#include <http_log.h>
#include <ap_config.h>
#include <apr_uri.h>
#include <mod_dav.h>
#if AP_MODULE_MAGIC_AT_LEAST(20060110,0)
#include <mod_auth.h>
APR_OPTIONAL_FN_TYPE(ap_satisfies) *ap_satisfies;
#endif

/* Take note: You must install svn with dav_svn enabled, see the build flags
 * You must then install it (if not done for you) into Apache */
#include "mod_dav_svn.h"
#include "svn_path.h"
#include "svn_config.h"
#include "svn_string.h"
#include "svn_repos.h"

/** Access Return Type
 *
 * This is the status of the database checking permissions.
 *
 * @see authz_svn_db_check_access
 */
typedef int ACCESS_RETURN;

/** Permissions
 *
 * Container for the permissions required by the current request.  I suppose it could be used for other things too, but this is the primary purpose.
 *
 */
typedef struct
{
	/** Read permission is required */
	bool read;
	/** Write permission is required */
	bool write;
	/** Recursive permissions are required */
	bool recursive;
}authz_svn_db_access_t;

/** Generic Database Query result
 *
 * This structure is used as a conduit so that database results can be
 * processed in a uniform manner.
 */
typedef struct
{
	/** The repository path which has the following permissions */
	char *repo_path;
	/** The permissions associated with this repository path */
	authz_svn_db_access_t	perms;
}authz_svn_db_query_result;

/** Generic Database Query result set
 *
 * This structure is used as a conduit so that database results can be
 * processed in a uniform manner.
 */
typedef struct
{
	/** The number of results */
	unsigned int count;
	/** The result array */
	authz_svn_db_query_result *results;
}authz_svn_db_query_result_set;

/** Create a Database Query Result Set
 *
 * The following function is designed to facilitate proper memory allocation in Apache when copying the result set into memory for processing.
 *
 * @param[in] pool The Apache Memory pool in which to create the result set.
 * @param[in] count The number of results to create
 * @return A authz_svn_db_query_result structure.
 */
static authz_svn_db_query_result_set*
create_authz_svn_db_query_result_set(
		apr_pool_t* pool,
		unsigned int count );

/** This is the svn structure required by apache,
 * All configuration data should go here
 * Learn from my mistake: this structure will disappear/move in the midst of connections, so never point to it (or anything in it) directly if the reference will persist beyond the request lifetime. */
typedef struct {
	/** to enabled or not the module
	* false by default */
	bool is_active;

	/** We are the final word as to who has access:
	 * 1 by default, you can overwrite (but you need not set it) */
	bool authoritative;

	/** this is the path base path of reference, if the <Location>
	 * tag names a URI, you will get the full URI here.
	 * NOTE: during initialization, this will be filled in for
	 * you (don't touch it, it will be overwritten)  NOT OVERWRITABLE */
	const char *base_path;

	/** This is the machine on which the database is located */
	const char *database_host;

	/** This is the port number to contact the database */
	unsigned int database_port;

	/** This is the database name to connect to */
	const char *database_name;

	/** This is the username to use to connect to the database, this user should have READ ONLY permissions! (no reason for this module to modify the database) */
	const char *database_user_name;

	/** This is the path to the password file (hopefully) located outside of the world readable ares */
	const char *password_file;

	/** This is the path to the password file key, this key is used to look up the password in memory. */
	const char *password_file_key;

	/** This is where we cache the database password (to avoid keeping a file open) */
	const char *database_password;

	/** This is the name of the table in which user names are found */
	const char *database_user_table_name;

	/** This is the name of the column in the table in which user names are found */
	const char *database_user_name_column_name;

	/** This is the name of the column in the table in which user id's are found */
	const char *database_user_id_column_name;

	/** This is the name of the table in which repository names are found */
	const char *database_repository_table_name;

	/** This is the name of the column in the table in which repository names are found. Note: Each entry/row should take the form: 'name:path' where name is the repository name, and path is the path WITHIN the repository */
	const char *database_repository_name_column_name;

	/** This is the name of the column in the table in which repository id's are found. */
	const char *database_repository_id_column_name;

	/** This is the name of the table in which users are linked to groups */
	const char *database_groupmembership_table_name;

	/** This is the name of the column in the table in which users are linked to groups */
	const char *database_groupmembership_user_column_name;

	/** This is the name of the column in the table in which groups are linked to users */
	const char *database_groupmembership_group_column_name;

	/** This is the name of the table in which user's repository path permissions are found */
	const char *database_userpermission_table_name;

	/** This is the name of the column for the user id */
	const char *database_userpermission_user_column_name;

	/** This is the name of the column for the repository path id */
	const char *database_userpermission_repopath_column_name;

	/** This is the name of the column for the read permission */
	const char *database_userpermission_read_column_name;

	/** This is the name of the column for the write permission */
	const char *database_userpermission_write_column_name;

	/** This is the name of the column for the permissions to be able to recurse down into the directories */
	const char *database_userpermission_recursive_column_name;

	/** This is the name of the table in which group's repository path permissions are found */
	const char *database_grouppermission_table_name;

	/** This is the name of the column for the group id */
	const char *database_grouppermission_group_column_name;

	/** This is the name of the column for the repository path id */
	const char *database_grouppermission_repopath_column_name;

	/** This is the name of the column for the read permission */
	const char *database_grouppermission_read_column_name;

	/** This is the name of the column for the write permission */
	const char *database_grouppermission_write_column_name;

	/** This is the name of the column for the permissions to be able to recurse down into the directories */
	const char *database_grouppermission_recursive_column_name;

	/** This is the name of the table in which repository paths are delineated */
	const char *database_repopath_table_name;

	/** This is the name of the column in the table in which repository paths are delineated */
	const char *database_repopath_path_column_name;

	/** This is the name of the column in the table in which repository paths are linked to repository names */
	const char *database_repopath_repo_column_name;

	/** This is the name of the column in the table in which repository path id's are found */
	const char *database_repopath_id_column_name;

	/** Database Connection */
	void **database_connection_handle;

	/** Database Connection Handle Key used to look up the database handle in memory */
	const char *database_connection_key;

	/** Unique String: for global data management */
	const char *unique_key_name;

	/** An extra place to put things, just in case something is overlooked here */
	void* reserved;

} authz_svn_db_config_rec;

/** Open the database connection  FOR MODULE AUTHORS (define)
  *
  * Create the framework required for a connection and open the connection.
  *
  * Module authors must define this function, but never actually call it directly.
  *
  * @param[in] r The configuration for this module (see above).  Use it to configure the connection
  * @param[in] pool Use the apache pool here to store your data (anything that is non-transitory).  You should definitely store your database structure here.
  * @param[out] dbconn_handle Because you don't have write permission for the configuration structure (don't monkey around in there!), when you allocate your database's connection, make sure to save a copy here so we can install it in the configuration.
  * @param[out] errorbuffer This is where you get to write up any error messages.  These will be passed to the Apache logs.
  * @param[in] buffermax The errorbuffer above has a limited amount of space.  Never use above buffermax characters or you'll cause a buffer overrun and I'll be mad.
  * @return 0 for Success, and any number you please for failure.  These numbers will be printed with the errorbuffer in the Apache log.  Just in case you were lazy with the error messages.
  */
static int authz_svn_db_conn_open(
		const authz_svn_db_config_rec *r,
		apr_pool_t *pool,
		void** dbconn_handle,
		char *errorbuffer,
		unsigned int buffermax );

/** Close the database connection  FOR MODULE AUTHORS (define)
  *
  * Close it and clean up data.  This means the DB is done being used.
  *
  * Module authors must define this function, but never actually call it directly.
  *
  * @param[in] d This is the pointer to the database handle.
  */
static void authz_svn_db_conn_close( void* d );

/** Check Access  FOR MODULE AUTHORS (define)
  *
  * This is where you, the module maker, access the database and determine if the user is allowed to access the repository and path.  This is very involved.
  *
  * Module authors must define this function, but never actually call it directly.
  *
  * Steps: (stuff you should do when you implement this function)
  *  1. Check to see if the database is still around/responsive
  *  2. Escape the username
  *  3. Escape the repository name and paths (only if used in the query)
  *  4. Build the database query (see the file sql.txt) to return repository paths and permissions for the query.
  *  5. Execute the query with the database
  *  6. Error check the query
  *  7. Copy the query results into a query results structure, use create_authz_svn_db_query_result_set function to create the memory block for the results
  *
  * @param[in] conf This is the configuration information, filled in so you can format the database query. You don't have to use everything in it.
  * @param[in] user_name This is the user name of the user trying to access the resource. This should match the permissions in the database.  If anonymous access, the username will always be "anonymous".
  * @param[in] repo_name This is the repository to check permissions of
  * @param[in] repo_path This is the full path _within_ the repository to the resource being requested.
  * @param[in] req_permissions The permissions required to access the resource.  If the permission is true, it must be enforced.  If false, it's good if it's true, but ok if it's false.
  * @param[out] results This is the place to store the results of the database query so that the base-module can interpret it.
  * @param[in] pool This is the pool to use when doing string manipulation or memory allocation, but there's really no need for memory allocation.
  * @param[in] errbuf This is a buffer where you can put error messages.  If you change the first character, it will be printed as a notice in the apache logs. VERY useful for debugging.
  * @param[in] errsize This is the maximum size of errbuf, do not exceed this value or you will cause a segfault/memory corruption.
  * @return ACCOK if access is granted, ACCREJ if access is rejected, ACCDBERR if there was an error contacting the database/executing the query.
  */
static ACCESS_RETURN authz_svn_db_check_access(
		const authz_svn_db_config_rec *conf,
		const char* user_name,
		const char* repo_name,
		authz_svn_db_query_result_set **results,
		apr_pool_t *pool,
	    char* errbuf,
		int errsize	);

/** Configure Directory Specific  FOR MODULE AUTHORS (define)
 *
 * This allows you, the module writer, to configure the defaults to the configuration specific to your database implementation, such as port number.  Feel free to override any defaults.
 *
  * Module authors must define this function, but never actually call it directly.
  *
 * @param[in,out] conf The configuration structure
 * @param[in] pool This is the memory pool to use when allocating memory
 */
static void authz_svn_db_config_dir_spec( authz_svn_db_config_rec *conf, apr_pool_t *pool );

static bool strlen_safe( const char *test_str, size_t *unesc_str_len );
static bool strlen_arith_overflow_check_times_2_plus_1( size_t str_len );

#endif/*MOD_AUTHZ_SVN_DB_MOD*/

