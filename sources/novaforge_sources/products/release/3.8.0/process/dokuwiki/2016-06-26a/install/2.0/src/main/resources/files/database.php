<?php
/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
 
// must be run within Dokuwiki
if(!defined('DOKU_INC')) die();
include(DOKU_CONF . 'local.protected.php');

/**
 * MySQL authentication backend
 *
 * @license    GPL 2 (http://www.gnu.org/licenses/gpl.html)
 * @author     Andreas Gohr <andi@splitbrain.org>
 * @author     Chris Smith <chris@jalakai.co.uk>
 * @author     Matthias Grimm <matthias.grimmm@sourceforge.net>
 * @author     Jan Schumann <js@schumann-it.com>
 */
class database {
	
	/** @var resource holds the database connection */
    private $dbcon = 0;
    /** @var int database version*/
    private $dbver = 0;
    /** @var int database revision */
    private $dbrev = 0;
    /** @var int database subrevision */
    private $dbsub = 0;
	
	/** Requête **/
	private $getUserInfo = "SELECT pass, CONCAT(firstname,' ',lastname) AS name, email AS mail
                                               FROM users
                                               WHERE login='%{user}'";
	//get all groups a user is member of                                            
	private $getGroups = "SELECT name as `group`FROM groups g, users u, usergroup ug
                                                WHERE u.uid = ug.uid
                                                AND g.gid = ug.gid
                                                AND u.login='%{user}'";
	private $getCheckPass   = "SELECT DISTINCT(pass)
                    FROM usergroup AS ug
                    JOIN users AS u ON u.uid=ug.uid
                    JOIN groups AS g ON g.gid=ug.gid
                    WHERE login='%{user}'
                    AND name='%{dgroup}'";
	
        private $addUser     = "INSERT INTO users
                                               (login, pass, email, firstname, lastname)
                                               VALUES ('%{user}', '%{pass}', '%{email}',
                                               SUBSTRING('%{name}', 1, LOCATE(' ', '%{name}')),
                                               IF(Length('%{name}') = Length(REPLACE('%{name}', ' ', '')),'',SUBSTRING_INDEX('%{name}', ' ',-((Length('%{name}')-Length(REPLACE('%{name}', ' ', '')))))) )";

											   
	private $addUseraddUserGroup = "INSERT INTO usergroup (uid, gid)
                                               VALUES ('%{uid}', '%{gid}')";
	
	private $getGroupID  = "SELECT gid AS id FROM groups WHERE name='%{group}'";
	
	private $getUserID   = "SELECT uid AS id FROM users WHERE login='%{user}'";
	
	//should add a group to the database
	private $addGroup    = "INSERT INTO groups (name)
                                               VALUES ('%{group}')";
        //should connect a user to a group (a user become member of that group).
	private $addUserGroup  = "INSERT INTO usergroup (uid, gid)
                                               VALUES ('%{uid}', '%{gid}')";
        //This statement should remove a group fom the database
	private $adelGroup    = "DELETE FROM groups
                                               WHERE gid='%{gid}'";
        //should remove a user fom the database.
        private $delUser     = "DELETE FROM users
                                                       WHERE uid='%{uid}'";
        //This statement should remove all connections from a user to any group
        private $delUserRefs = "DELETE FROM usergroup
                                                       WHERE uid='%{uid}'";
	//should remove a single connection from a user to a group
        private $delUserGroup = "DELETE FROM usergroup
                                               WHERE uid='%{uid}'
                                                 AND gid='%{gid}'";
        
        //should modify a user entry in the database
        private $updateUser  = "UPDATE users SET";
        private $UpdateLogin = "login='%{user}'";
        private $UpdatePass  = "pass='%{pass}'";
        private $UpdateEmail = "email='%{email}'";
        private $UpdateName  = "firstname=SUBSTRING_INDEX('%{name}',' ', 1),
                                                       lastname=IF(Length('%{name}') = Length(REPLACE('%{name}', ' ', '')),'', SUBSTRING_INDEX('%{name}', ' ',-((Length('%{name}')-Length(REPLACE('%{name}', ' ', ''))))))";
       private $UpdateTarget = "WHERE uid=%{uid}";
	public function __construct() {
	}
	/**
     * Opens a connection to a database and saves the handle for further
     * usage in the object. The successful call to this functions is
     * essential for most functions in this object.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @return bool
     */
    public function _openDB($server = null,$userBDD = null,$passBDD = null,$bdd = null) {
        if(!$this->dbcon) {
            $con = @mysql_connect($server, $userBDD, $passBDD);
            if($con) {
                if((mysql_select_db($bdd, $con))) {
                    if((preg_match('/^(\d+)\.(\d+)\.(\d+).*/', mysql_get_server_info($con), $result)) == 1) {
                        $this->dbver = $result[1];
                        $this->dbrev = $result[2];
                        $this->dbsub = $result[3];
                    }
                    $this->dbcon = $con;
                    //if($this->getConf('charset')) {
                      //  mysql_query('SET CHARACTER SET "'.$this->getConf('charset').'"', $con);
                    //}
                    return true; // connection and database successfully opened
                } else {
                    mysql_close($con);
                }
            } else {
               
            }

            return false; // connection failed
        }
        return true; // connection already open
    }
	
	public function _closeDB() {
        if($this->dbcon) {
            mysql_close($this->dbcon);
            $this->dbcon = 0;
        }
    }
	
	 /**
     * retrieveUserInfo
     *
     * Gets the data for a specific user. The database connection
     * must already be established for this function to work.
     * Otherwise it will return 'false'.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $user  user's nick to get data for
     * @return false|array false on error, user info on success
     */
    public function _retrieveUserInfo($user) {
        $sql    = str_replace('%{user}', $this->_escape($user), $this->getUserInfo);
        $result = $this->_queryDB($sql);
        if($result !== false && count($result)) {
            $info         = $result[0];
            return $info;
        }
        return false;
    }
	

	 /**
     * Give user membership of a group
     *
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param   string $user
     * @param   string $group
     * @return  bool   true on success, false on error
     */
    protected function joinGroup($user, $group) {
        $rc = false;

        if($this->_openDB()) {
            $rc = $this->_addUserToGroup($user, $group);
            $this->_closeDB();
        }
        return $rc;
    }

    /**
     * Remove user from a group
     *
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param   string $user  user that leaves a group
     * @param   string $group group to leave
     * @return  bool
     */
    protected function leaveGroup($user, $group) {
        $rc = false;

        if($this->_openDB()) {
            $rc  = $this->_delUserFromGroup($user, $group);
            $this->_closeDB();
        }
        return $rc;
    }
	
	/**
     * Checks if the given user exists and the given plaintext password
     * is correct. Furtheron it might be checked wether the user is
     * member of the right group
     *
     * Depending on which SQL string is defined in the config, password
     * checking is done here (getpass) or by the database (passcheck)
     *
     * @param  string $user user who would like access
     * @param  string $pass user's clear text password to check
     * @return bool
     *
     * @author  Andreas Gohr <andi@splitbrain.org>
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     */
	public function checkPass($user, $pass, $defaultGroup, $forwardClearPass) {

        $rc = false;
        $sql    = str_replace('%{user}', $this->_escape($user), $this->getCheckPass);
        $sql    = str_replace('%{pass}', $this->_escape($pass), $sql);
        $sql    = str_replace('%{dgroup}', $this->_escape($defaultGroup), $sql);
		
        $result = $this->_queryDB($sql);

        if($result !== false && count($result) == 1) {
			if($forwardClearPass == 1) {
                $rc = true;
            } else {
				$rc = auth_verifyPassword($pass, $result[0]['pass']);
            }
        }
        $this->_closeDB();
    
        return $rc;
    }
	
	 /**
     * Create a new User. Returns false if the user already exists,
     * null when an error occurred and true if everything went well.
     *
     * The new user will be added to the default group by this
     * function if grps are not specified (default behaviour).
     *
     * @author  Andreas Gohr <andi@splitbrain.org>
     * @author  Chris Smith <chris@jalakai.co.uk>
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param string $user  nick of the user
     * @param string $pwd   clear text password
     * @param string $name  full name of the user
     * @param string $mail  email address
     * @param array  $grps  array of groups the user should become member of
     * @return bool|null
     */
    public function createUser($user, $pwd, $name, $mail, $grps = null,$forwardClearPass) {
		global $conf;
		
        if(($info = $this->_getUserInfo($user)) !== false) {
			return false; // user already exists
        }
        // set defaultgroup if no groups were given
        if($grps == null) {
            $grps = array($conf['defaultgroup']);
        }

        $pwd = $forwardClearPass ? $pwd : auth_cryptPassword($pwd);
        $rc  = $this->_addUser($user, $pwd, $name, $mail, $grps);

        $this->_closeDB();
        if(!$rc) {
            return null;
        }
        return true;
    }
	
	/**
     * Get a user's information
     *
     * The database connection must already be established for this function to work.
     *
     * @author Christopher Smith <chris@jalakai.co.uk>
     *
     * @param  string  $user  username of the user whose information is being reterieved
     * @param  bool    $requireGroups  true if group memberships should be included
     * @param  bool    $useCache       true if ok to return cached data & to cache returned data
     *
     * @return mixed   false|array     false if the user doesn't exist
     *                                 array containing user information if user does exist
     */
    public function _getUserInfo($user, $requireGroups=true, $useCache=true) {
        $info = null;

        if ($useCache && isset($this->cacheUserInfo[$user])) {
            $info = $this->cacheUserInfo[$user];
        }

        if (is_null($info)) {
            $info = $this->_retrieveUserInfo($user);
        }

        if (($requireGroups == true) && $info && !isset($info['grps'])) {
            $info['grps'] = $this->_getGroups($user);
        }

        if ($useCache) {
            $this->cacheUserInfo[$user] = $info;
        }

        return $info;
    }
	
	/**
     * Adds a new User to the database.
     *
     * The database connection must already be established
     * for this function to work. Otherwise it will return
     * false.
     *
     * @author  Andreas Gohr <andi@splitbrain.org>
     * @author  Chris Smith <chris@jalakai.co.uk>
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $user  login of the user
     * @param  string $pwd   encrypted password
     * @param  string $name  full name of the user
     * @param  string $mail  email address
     * @param  array  $grps  array of groups the user should become member of
     * @return bool
     */
    protected function _addUser($user, $pwd, $name, $mail, $grps) {
        if($this->dbcon && is_array($grps)) {
            $sql = str_replace('%{user}', $this->_escape($user), $this->addUser);
            $sql = str_replace('%{pass}', $this->_escape($pwd), $sql);
            $sql = str_replace('%{name}', $this->_escape($name), $sql);
            $sql = str_replace('%{email}', $this->_escape($mail), $sql);
            $uid = $this->_modifyDB($sql);
            $gid = false;
            $group = '';

            if($uid) {
                foreach($grps as $group) {
                    $gid = $this->_addUserToGroup($user, $group, true);
                    if($gid === false) break;
                }

                if($gid !== false){
                    $this->_flushUserInfoCache($user);
                    return true;
                } else {
                    /* remove the new user and all group relations if a group can't
                     * be assigned. Newly created groups will remain in the database
                     * and won't be removed. This might create orphaned groups but
                     * is not a big issue so we ignore this problem here.
                     */
                    $this->_delUser($user);
                    $this->_debug("MySQL err: Adding user '$user' to group '$group' failed.", -1, __LINE__, __FILE__);
                }
            }
        }
        return false;
    }
	
    /**
     * Modify user data
     *
     * An existing user dataset will be modified. Changes are given in an array.
     *
     * The dataset update will be rejected if the user name should be changed
     * to an already existing one.
     *
     * The password must be provided unencrypted. Pasword encryption is done
     * automatically if configured.
     *
     * If one or more groups can't be updated, an error will be set. In
     * this case the dataset might already be changed and we can't rollback
     * the changes. Transactions would be really useful here.
     *
     * modifyUser() may be called without SQL statements defined that are
     * needed to change group membership (for example if only the user profile
     * should be modified). In this case we assure that we don't touch groups
     * even when $changes['grps'] is set by mistake.
     *
     * @author  Chris Smith <chris@jalakai.co.uk>
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param   string $user    nick of the user to be changed
     * @param   array  $changes array of field/value pairs to be changed (password will be clear text)
     * @return  bool   true on success, false on error
     */
    public function modifyUser($user, $changes,$forwardClearPass) {
        $rc = false;

        if(!is_array($changes) || !count($changes)) {
            return true; // nothing to change
        }

        $rc = $this->_updateUserInfo($user, $changes,$forwardClearPass);

        if(!$rc) {
            msg($this->getLang('usernotexists'), -1);
        } elseif(isset($changes['grps']) && $this->cando['modGroups']) {
            $groups = $this->_getGroups($user);
            $grpadd = array_diff($changes['grps'], $groups);
            $grpdel = array_diff($groups, $changes['grps']);

            foreach($grpadd as $group) {
                if(($this->_addUserToGroup($user, $group, true)) == false) {
                    $rc = false;
                }
            }

            foreach($grpdel as $group) {
                if(($this->_delUserFromGroup($user, $group)) == false) {
                    $rc = false;
                }
            }

           if(!$rc) msg($this->getLang('writefail'));
        }

        return $rc;
    }
    
    
    /**
     * Updates the user info in the database
     *
     * Update a user data structure in the database according changes
     * given in an array. The user name can only be changes if it didn't
     * exists already. If the new user name exists the update procedure
     * will be aborted. The database keeps unchanged.
     *
     * The database connection has already to be established for this
     * function to work. Otherwise it will return 'false'.
     *
     * The password will be encrypted if necessary.
     *
     * @param  string $user    user's nick being updated
     * @param  array $changes  array of items to change as pairs of item and value
     * @return bool true on success or false on error
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     */
    protected function _updateUserInfo($user, $changes,$forwardClearPass) {
        $sql = $this->updateUser." ";
        $cnt = 0;
        $err = 0;

        if($this->dbcon) {
            $uid = $this->_getUserID($user);
            if ($uid === false) {
                return false;
            }

            foreach($changes as $item => $value) {
                if($item == 'user') {
                    if(($this->_getUserID($changes['user']))) {
                        $err = 1; /* new username already exists */
                        break; /* abort update */
                    }
                    if($cnt++ > 0) $sql .= ", ";
                    $sql .= str_replace('%{user}', $value, $this->UpdateLogin);
                } else if($item == 'name') {
                    if($cnt++ > 0) $sql .= ", ";
                    $sql .= str_replace('%{name}', $value, $this->UpdateName);
                } else if($item == 'pass') {
                    if(!$forwardClearPass)
                        $value = auth_cryptPassword($value);
                    if($cnt++ > 0) $sql .= ", ";
                    $sql .= str_replace('%{pass}', $value, $this->UpdatePass);
                } else if($item == 'mail') {
                    if($cnt++ > 0) $sql .= ", ";
                    $sql .= str_replace('%{email}', $value, $this->UpdateEmail);
                }
            }

            if($err == 0) {
                if($cnt > 0) {
                    $sql .= " ".str_replace('%{uid}', $uid, $this->UpdateTarget);
                    if(get_class($this) == 'auth_mysql') $sql .= " LIMIT 1"; //some PgSQL inheritance comp.
                    $this->_modifyDB($sql);
                    $this->_flushUserInfoCache($user);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a user to a group.
     *
     * If $force is set to true non existing groups would be created.
     *
     * The database connection must already be established. Otherwise
     * this function does nothing and returns 'false'. It is strongly
     * recommended to call this function only after all participating
     * tables (group and usergroup) have been locked.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param   string $user    user to add to a group
     * @param   string $group   name of the group
     * @param   bool   $force   create missing groups
     * @return  bool   true on success, false on error
     */
    public function _addUserToGroup($user, $group, $force = false) {
        $newgroup = 0;

        if(($this->dbcon) && ($user)) {
            $gid = $this->_getGroupID($group);
            if(!$gid) {
                if($force) { // create missing groups
                    $sql      = str_replace('%{group}', $this->_escape($group), $this->getConf('addGroup'));
                    $gid      = $this->_modifyDB($sql);
                    $newgroup = 1; // group newly created
                }
                if(!$gid) return false; // group didn't exist and can't be created
            }

            $sql = $this->addUseraddUserGroup;
            if(strpos($sql, '%{uid}') !== false) {
                $uid = $this->_getUserID($user);
                $sql = str_replace('%{uid}', $this->_escape($uid), $sql);
            }
            $sql = str_replace('%{user}', $this->_escape($user), $sql);
            $sql = str_replace('%{gid}', $this->_escape($gid), $sql);
            $sql = str_replace('%{group}', $this->_escape($group), $sql);
            if($this->_modifyDB($sql) !== false) {
                $this->_flushUserInfoCache($user);
                return true;
            }

            if($newgroup) { // remove previously created group on error
                $sql = str_replace('%{gid}', $this->_escape($gid), $this->getConf('delGroup'));
                $sql = str_replace('%{group}', $this->_escape($group), $sql);
                $this->_modifyDB($sql);
            }
        }
        return false;
    }
	
	 /**
     * Retrieves the group id of a given group name
     *
     * The database connection must already be established
     * for this function to work. Otherwise it will return
     * false.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $group   group name which id is desired
     * @return false|string group id
     */
    protected function _getGroupID($group) {
		
		/**
			Novaforge Modification
		*/
		if($group == 16)
		{
			$group = "admin";
		}
		else if($group == 1)
		{
			$group = "user";
		}
		
        if($this->dbcon) {
            $sql    = str_replace('%{group}', $this->_escape($group), $this->getGroupID);
            $result = $this->_queryDB($sql);
            return $result === false ? false : $result[0]['id'];
        }
        return false;
    }
	
	 /**
     * Retrieves the user id of a given user name
     *
     * The database connection must already be established
     * for this function to work. Otherwise it will return
     * false.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $user user whose id is desired
     * @return mixed  user id
     */
    protected function _getUserID($user) {
        if($this->dbcon) {
            $sql    = str_replace('%{user}', $this->_escape($user), $this->getUserID);
            $result = $this->_queryDB($sql);
            return $result === false ? false : $result[0]['id'];
        }
        return false;
    }

	
	/**
     * Retrieves a list of groups the user is a member off.
     *
     * The database connection must already be established
     * for this function to work. Otherwise it will return
     * false.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $user user whose groups should be listed
     * @return bool|array false on error, all groups on success
     */
    public function _getGroups($user) {
        $groups = array();
        if($this->dbcon) {
            $sql = str_replace('%{user}', $this->_escape($user), $this->getGroups);
            $result = $this->_queryDB($sql);
			
            if($result !== false && count($result)) {
                foreach($result as $row) {
                    $groups[] = $row['group'];
				}
            }
			
            return $groups;
        }
        return false;
    }
	
	 /**
     * Flush cached user information
     *
     * @author Christopher Smith <chris@jalakai.co.uk>
     *
     * @param  string  $user username of the user whose data is to be removed from the cache
     *                       if null, empty the whole cache
     */
    protected function _flushUserInfoCache($user=null) {
        if (is_null($user)) {
            $this->cacheUserInfo = array();
        } else {
            unset($this->cacheUserInfo[$user]);
        }
    }
	/**
     * Escape a string for insertion into the database
     *
     * @author Andreas Gohr <andi@splitbrain.org>
     *
     * @param  string  $string The string to escape
     * @param  boolean $like   Escape wildcard chars as well?
     * @return string
     */
    private function _escape($string, $like = false) {
        if($this->dbcon) {
            $string = mysql_real_escape_string($string, $this->dbcon);
        } else {
            $string = addslashes($string);
        }
        if($like) {
            $string = addcslashes($string, '%_');
        }
        return $string;
    }
	
	 /**
     * Sends a SQL query to the database and transforms the result into
     * an associative array.
     *
     * This function is only able to handle queries that returns a
     * table such as SELECT.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param string $query  SQL string that contains the query
     * @return array|false with the result table
     */
    protected function _queryDB($query) {

        $resultarray = array();
        if($this->dbcon) {
            $result = @mysql_query($query, $this->dbcon);
            if($result) {
                while(($t = mysql_fetch_assoc($result)) !== false)
                    $resultarray[] = $t;
                mysql_free_result($result);
                return $resultarray;
            }
            error_log('MySQL err: '.mysql_error($this->dbcon), -1, __LINE__, __FILE__);
        }
        return false;
    }
	
	 /**
     * Sends a SQL query to the database
     *
     * This function is only able to handle queries that returns
     * either nothing or an id value such as INPUT, DELETE, UPDATE, etc.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param string $query  SQL string that contains the query
     * @return int|bool insert id or 0, false on error
     */
    protected function _modifyDB($query) {

        if($this->dbcon) {
            $result = @mysql_query($query, $this->dbcon);
            if($result) {
                $rc = mysql_insert_id($this->dbcon); //give back ID on insert
                if($rc !== false) return $rc;
            }
        }
        return false;
    }

    
    /**
     * Transforms the filter settings in an filter string for a SQL database
     * The database connection must already be established, otherwise the
     * original SQL string without filter criteria will be returned.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $sql     SQL string to which the $filter criteria should be added
     * @param  array $filter  array of filter criteria as pairs of item and pattern
     * @return string SQL string with attached $filter criteria on success, original SQL string on error
     */
    protected function _createSQLFilter($sql, $filter) {
        $SQLfilter = "";
        $cnt       = 0;

        if($this->dbcon) {
            foreach($filter as $item => $pattern) {
                $tmp = '%'.$this->_escape($pattern).'%';
                if($item == 'user') {
                    if($cnt++ > 0) $SQLfilter .= " AND ";
                    $SQLfilter .= str_replace('%{user}', $tmp, $this->getConf('FilterLogin'));
                } else if($item == 'name') {
                    if($cnt++ > 0) $SQLfilter .= " AND ";
                    $SQLfilter .= str_replace('%{name}', $tmp, $this->getConf('FilterName'));
                } else if($item == 'mail') {
                    if($cnt++ > 0) $SQLfilter .= " AND ";
                    $SQLfilter .= str_replace('%{email}', $tmp, $this->getConf('FilterEmail'));
                } else if($item == 'grps') {
                    if($cnt++ > 0) $SQLfilter .= " AND ";
                    $SQLfilter .= str_replace('%{group}', $tmp, $this->getConf('FilterGroup'));
                }
            }

            // we have to check SQLfilter here and must not use $cnt because if
            // any of cnf['Filter????'] is not defined, a malformed SQL string
            // would be generated.

            if(strlen($SQLfilter)) {
                $glue = strpos(strtolower($sql), "where") ? " AND " : " WHERE ";
                $sql  = $sql.$glue.$SQLfilter;
            }
        }

        return $sql;
    }
    
    /**
     * [public function]
     *
     * Remove one or more users from the list of registered users
     *
     * @param   array  $users   array of users to be deleted
     * @return  int             the number of users deleted
     *
     * @author  Christopher Smith <chris@jalakai.co.uk>
     * @author  Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     */
    function deleteUsers($users) {
        $count = 0;

        if($this->_openDB()) {
            if(is_array($users) && count($users)) {
                foreach($users as $user) {
                    if($this->_delUser($user)) {
                        $count++;
                    }
                }
            }
            $this->_closeDB();
        } else {
            msg($this->getLang('connectfail'), -1);
        }
        return $count;
    }
    
    /**
     * Deletes a given user and all his group references.
     *
     * The database connection must already be established
     * for this function to work. Otherwise it will return
     * false.
     *
     * @author Matthias Grimm <matthiasgrimm@users.sourceforge.net>
     *
     * @param  string $user username of the user to be deleted
     * @return bool
     */
    protected function _delUser($user) {
        if($this->dbcon) {
            $uid = $this->_getUserID($user);
            if($uid) {
                $sql = str_replace('%{uid}', $this->_escape($uid), $this->delUserRefs);
                $this->_modifyDB($sql);
                $sql = str_replace('%{uid}', $this->_escape($uid), $this->delUser);
                $sql = str_replace('%{user}', $this->_escape($user), $sql);
                $this->_modifyDB($sql);
                $this->_flushUserInfoCache($user);
                return true;
            }
        }
        return false;
    }
	/**
     * Wrapper around msg() but outputs only when debug is enabled
     *
     * @param string $message
     * @param int    $err
     * @param int    $line
     * @param string $file
     * @return void
     */
    protected function _debug($message, $err, $line, $file) {
        if(!$this->getConf('debug')) return;
        msg($message, $err, $line, $file);
    }
}