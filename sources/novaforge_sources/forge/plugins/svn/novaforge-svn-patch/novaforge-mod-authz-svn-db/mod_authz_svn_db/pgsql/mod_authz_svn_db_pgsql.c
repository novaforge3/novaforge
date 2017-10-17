/* Copyright (c) 2007, Christopher Wojno
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
 * mod_authz_svn_db_pgsql.c: Defines the module so that PostgreSQL can be used to authorize users for SVN repository access.
 */
#include <libpq-fe.h>
#define AUTHZ_SVN_DB_MODULE_NAME authz_svn_db_pgsql_module
#define AUTHZ_SVN_DB_MODULE_NAME_STRING "authz_svn_db_pgsql_module"
#include "mod_authz_svn_db.c"

static void authz_svn_db_config_dir_spec( authz_svn_db_config_rec *conf, apr_pool_t *pool )
{
	conf->database_port = 5432;
}

int authz_svn_db_conn_open(
		const authz_svn_db_config_rec *r,
		apr_pool_t *pool,
		void** dbconn_handle,
		char *errorbuffer,
		unsigned int buffermax )
{
	char intconv[16];
	PGconn *dbconn = NULL;
	sprintf( intconv, "%d", r->database_port );
	/* create the connection */
	dbconn = PQsetdbLogin( r->database_host, intconv, NULL, NULL, r->database_name, r->database_user_name, r->database_password );

	/* something went wrong */
	if (PQstatus(dbconn) != CONNECTION_OK)
	{
		strncpy( errorbuffer, "Connection to database failed: ", buffermax );
		strncpy( errorbuffer + strlen(errorbuffer), PQerrorMessage(dbconn), buffermax - strlen(errorbuffer) );
		return -2;
	}

	/* Connection up and running! */

	/* set the outgoing handle (save it) */
	*dbconn_handle = (void*)dbconn;
	return 0;
}

void authz_svn_db_conn_close( void* d )
{
	/* if created, clean it up */
	if( NULL != d )
	{
		PQfinish((PGconn*)d);
	}
}

ACCESS_RETURN authz_svn_db_check_access(
		const authz_svn_db_config_rec *r,
		const char* user_name,
		const char* repo_name,
		authz_svn_db_query_result_set **results,
		apr_pool_t *pool,
	    char *errbuf,
		int errsize	)
{
	size_t str_len;
	int rowsreturned;
	int i;
	PGresult *res = NULL;
	const char *cmdq;
	char* esc_username;
	char* esc_repo;
	int escErr;
	authz_svn_db_query_result* result;

	/* Make sure DB is still available */
	if( PQstatus( (PGconn*)*r->database_connection_handle ) != CONNECTION_OK )
	{
		return ACCDBERR;
	}

	str_len = 0;
	/* Create enough room for the incoming user name */
	if( !strlen_safe(user_name, &str_len) || !strlen_arith_overflow_check_times_2_plus_1( str_len ) )
	{
		return ACCREJ;
	}

	esc_username = apr_palloc( pool, str_len*2+1 );
	/* Escape the username (prevent SQL-injection) */
	PQescapeStringConn( (PGconn*)*r->database_connection_handle,
                           esc_username, user_name, str_len,
                           &escErr);
	/* Error escaping string, FAIL and reject! */
	if( escErr != 0 )
	{
		return ACCREJ;
	}

	/* Create enough room for the incoming repo name */
	str_len = 0;
	if( !strlen_safe(repo_name, &str_len) || !strlen_arith_overflow_check_times_2_plus_1( str_len ) )
	{
		return ACCREJ;
	}
	esc_repo = apr_palloc( pool, str_len*2+1 );
	/* Escape the username (prevent SQL-injection) */
	PQescapeStringConn( (PGconn*)*r->database_connection_handle,
                           esc_repo, repo_name, str_len,
                           &escErr);
	/* Error escaping string, FAIL and reject! */
	if( escErr != 0 )
	{
		return ACCREJ;
	}

	cmdq = apr_pstrcat(pool,"SELECT ",
			r->database_repopath_table_name, ".",
		    r->database_repopath_path_column_name, " AS repository_path, ",
			r->database_userpermission_table_name, ".",
		    r->database_userpermission_read_column_name, ", ",
			r->database_userpermission_table_name, ".",
		    r->database_userpermission_write_column_name, ", ",
			r->database_userpermission_table_name, ".",
		    r->database_userpermission_recursive_column_name,
			" FROM ",
		    r->database_repository_table_name, " INNER JOIN ",
			r->database_repopath_table_name, " ON ",
			r->database_repopath_table_name, ".",
			r->database_repopath_repo_column_name, " = ",
			r->database_repository_table_name, ".",
			r->database_repository_id_column_name, " AND ",
			r->database_repository_table_name, ".",
			r->database_repository_name_column_name, " = ",
			"'", esc_repo, "'", " INNER JOIN ",
			r->database_userpermission_table_name, " ON ",
			r->database_userpermission_table_name, ".",
			r->database_userpermission_repopath_column_name, " = ",
			r->database_repopath_table_name, ".",
			r->database_repopath_id_column_name, " INNER JOIN ",
			r->database_user_table_name, " ON ",
			r->database_user_table_name, ".",
			r->database_user_id_column_name, " = ",
			r->database_userpermission_table_name, ".",
			r->database_userpermission_user_column_name, " AND ",
			r->database_user_table_name, ".",
			r->database_user_name_column_name, " = ",
			"'", esc_username, "'",
			" UNION SELECT ",
			r->database_repopath_table_name, ".",
		    r->database_repopath_path_column_name, " AS repository_path, ",
			r->database_grouppermission_table_name, ".",
		    r->database_grouppermission_read_column_name, ", ",
			r->database_grouppermission_table_name, ".",
		    r->database_grouppermission_write_column_name, ", ",
			r->database_grouppermission_table_name, ".",
		    r->database_grouppermission_recursive_column_name,
			" FROM ",
		    r->database_repository_table_name, " INNER JOIN ",
			r->database_repopath_table_name, " ON ",
			r->database_repopath_table_name, ".",
			r->database_repopath_repo_column_name, " = ",
			r->database_repository_table_name, ".",
			r->database_repository_id_column_name, " AND ",
			r->database_repository_table_name, ".",
			r->database_repository_name_column_name, " = ",
			"'", esc_repo, "'", " INNER JOIN ",
			r->database_grouppermission_table_name, " ON ",
			r->database_grouppermission_table_name, ".",
			r->database_grouppermission_repopath_column_name, " = ",
			r->database_repopath_table_name, ".",
			r->database_repopath_id_column_name, " INNER JOIN ",
			r->database_groupmembership_table_name, " ON ",
			r->database_groupmembership_table_name, ".",
			r->database_groupmembership_group_column_name, " = ",
			r->database_grouppermission_table_name, ".",
			r->database_grouppermission_group_column_name, " INNER JOIN ",
			r->database_user_table_name, " ON ",
			r->database_user_table_name, ".",
			r->database_user_id_column_name, " = ",
			r->database_groupmembership_table_name, ".",
			r->database_groupmembership_user_column_name, " AND ",
			r->database_user_table_name, ".",
			r->database_user_name_column_name, " = ",
			"'", esc_username, "' ORDER BY repository_path DESC",
			NULL );

	res = PQexec( (PGconn*)*r->database_connection_handle, cmdq ); 

	if( PQresultStatus(res) != PGRES_COMMAND_OK && PQresultStatus(res) != PGRES_TUPLES_OK )
	{
		strncpy( errbuf, PQresStatus(PQresultStatus(res)), errsize );
		PQclear(res);
		return ACCDBERR;
	}
	if( ( rowsreturned = PQntuples(res) ) < 1 )
	{
		PQclear(res);
		return ACCREJ;
	}

	*results = create_authz_svn_db_query_result_set( pool, rowsreturned );
	result = (*results)->results;

	for( i = 0; i < rowsreturned; ++i, ++result )
	{
		/* copy the path */
		result->repo_path = apr_pstrcat( pool, PQgetvalue( res, i, 0 ), NULL );
		result->perms.read = *PQgetvalue( res, i, 1 ) == 't';
		result->perms.write = *PQgetvalue( res, i, 2 ) == 't';
		result->perms.recursive = *PQgetvalue( res, i, 3 ) == 't';
		/* go to the next result */
	}

	/* Data release (avoid memory leak) */
	PQclear(res);
	return ACCOK;
}

