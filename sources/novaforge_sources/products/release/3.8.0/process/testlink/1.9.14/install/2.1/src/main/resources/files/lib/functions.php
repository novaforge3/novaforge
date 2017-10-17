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
require_once(dirname(__FILE__) . '/../configCheck.php');
require_once(dirname(__FILE__) . '/../../../config.inc.php');
require_once(dirname(__FILE__) . '/../common.php');
require_once(dirname(__FILE__) . '/../../../cfg/const.inc.php');
require_once(dirname(__FILE__) . '/../testproject.class.php');
require_once(dirname(__FILE__) . '/../object.class.php');
require_once(dirname(__FILE__) . '/../tlRole.class.php');
require_once(dirname(__FILE__) . '/../users.inc.php');
require_once(dirname(__FILE__) . '/../email_api.php');


function createUser($login,$password,$firstname,$lastname,$mail,$lang="en",$roleID="")
{
        $ok = false;    
        $db = 0;
        doDBConnect($db);

        $op = new stdClass();
        $op->user = new tlUser();

        $op->user->dbID = $op->user->doesUserExist($db,$name);
        if(!$op->user->dbID){
                $op->status = $op->user->setPassword($password);
                $op->template = 'usersEdit.tpl';
                $op->operation = '';
                $op->user->login = $login;
                $op->user->firstName = $firstname;
                $op->user->lastName = $lastname;
                $op->user->emailAddress = $mail;
				if($roleID!="")
				{
					$op->user->globalRoleID=$roleID;
				}
                if($lang=="fr"){
                        $op->user->locale="fr_FR";
                }else{
                        $op->user->locale="en_GB";
                }
                
                $op->status = $op->user->writeToDB($db);        
                if($op->status >= tl::OK)
                {
                        $ok = true;
                }else{
                        $message = getUserErrorMessage($op->status);
                }
        }else{
                $ok = false;
                $message = "User already exist.";
        }
		
		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
}


function removeUser($name)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $op = new stdClass();
        $op->user = new tlUser();
        $op->user->login = $name;
        $op->status = $op->user->readFromDB($db,tlUser::USER_O_SEARCH_BYLOGIN);
	
        if($op->status == tl::ERROR)
        {
                $message = "The user ".$name." doesn't exist.";
        }else{
                $op->user->isActive = 0;
                
                $op->status = $op->user->unactiveFromDBAndReleaseLogin($db);
				
				
                if($op->status >= tl::OK)
                {
                        $ok = true;
                }else{
						$ok = false;
                        $message = getUserErrorMessage($op->status);
                }
        }      
		
		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }
        return $resultInfo;
}

function updateUser ($name,$password,$firstname,$lastname,$mail,$lang="en",$roleID="")
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $op = new stdClass();
        $op->user = new tlUser();
        $op->user->login = $name;
        $op->status = $op->user->readFromDB($db,tlUser::USER_O_SEARCH_BYLOGIN);

        if($op->status == tl::ERROR)
        {
                $message = "The user ".$name." doesn't exist.";
        }else{
                $op->user->setPassword($password);
                $op->user->firstName = $firstname;
                $op->user->lastName = $lastname;
                $op->user->emailAddress = $mail;
				if($roleID!="")
				{
					$op->user->globalRoleID=$roleID;
				}

                if($lang=="fr"){
                        $op->user->locale="fr_FR";
                }else{
                        $op->user->locale="en_GB";
                }

                $op->status = $op->user->writeToDB($db);
				
				
                if($op->status >= tl::OK)
                {
                        $ok = true;
                }else{
						$ok = false;
                        $message = getUserErrorMessage($op->status);
                }
        }      
		
		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
		
		
}

function getRole ($pProjectID, $pUsername)
{
        $ok = true;
        $db = 0;
        doDBConnect($db);

        if(!isset($pProjectID))
        {
                $ok = false;
                $message = "Project name not specified.";
        }
        if(!isset($pUsername))
        {
                $ok = false;
                $message = "Username not specified.";
        }

        if($ok == true)
        {
                $op = new stdClass();
                $op->user = new tlUser();
                $op->user->dbID = $op->user->doesUserExist($db,$pUsername);

                $tprojectMgr = new testproject($db);
                $TestLinkProject = $tprojectMgr->get_by_id($pProjectID);

                $role = new tlRole($db);
                if (($ok == true) && !$TestLinkProject)
                {
                        $ok = false;
                        $message = "The project " . $pProjectID . " does not exist";
                }
                if($ok == true)
                {
                        $existingUserRoles = $tprojectMgr->getUserRoleIDs($TestLinkProject["id"]);
                        foreach ($existingUserRoles as $key=>$val){
                                        if(in_array($op->user->dbID,$val))
                                        {
                                                $role = $role->getByID($db,$val["role_id"]);
                                        }
                        }       
                }
        }

		
		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success", "id" => $role->id , "name" => $role->name, "username" => $pUsername);		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
		
}


function getRoles ()
{
        $ok = true;
        $db = 0;
        doDBConnect($db);
        
		$op = new stdClass();
		$op->role = new tlRole();

		$existingRoles = _getAllRoles($db, null);
		
		$resultInfo = array();
		$resultInfo[]= array("STATUS" => "success");
		foreach ($existingRoles as $key=>$val){
		
			foreach ($val as $roleKey=>$roleVal)
			{
				if ($roleKey == "name")
				{							
					$rolename = $roleVal;
				}
				if ($roleKey == "dbID")
				{							
					$roleid = $roleVal;
				}						
			}					
			$resultInfo[] = array("id" => $roleid , "name" => $rolename);
				
		}
        return $resultInfo;
		
}

/*
  function: getAllRoles
  args:
  returns:
*/
function _getAllRoles(&$db,$order_by = null)
{
    $tables = tlObject::getDBTables(array('roles'));
    
	$sql = "SELECT roles.id FROM {$tables['roles']} roles WHERE roles.id>3 ORDER BY roles.description";
	
	$roles = tlDBObject::createObjectsFromDBbySQL($db,$sql,"id","tlRole",false,tlRole::TLOBJ_O_GET_DETAIL_MINIMUM);
	return $roles;
}


function hasUser($username='')
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $op = new stdClass();
        $op->user = new tlUser();
        $op->user->login = $username;
        $op->status = $op->user->readFromDB($db,tlUser::USER_O_SEARCH_BYLOGIN);

        if($op->status == tl::ERROR)
        {
                $ok=true;
                $res = "no";
        }else{
                $ok=true;
                $res = "yes";
        }

		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success", "result" => $res);		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;

}


function getUsers ($username='')
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $op = new stdClass();
        $op->user = new tlUser();
        $op->user->login = $username;
        $op->status = $op->user->readFromDB($db,tlUser::USER_O_SEARCH_BYLOGIN);

        if($op->status == tl::ERROR)
        {
                $message = "The user ".$name." doesn't exist.";
        }else{
                $ok = true;
        }
        
		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success", "id" => $op->user->dbID , "name" => $op->user->login, "email" => $op->user->emailAddress);		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
}

function setRoles ($pProjectID,$array_users)
{
        $ok = true;
        $db = 0;
        doDBConnect($db);

        if (isset ($pProjectID) == false)
        {
                $ok = false;
                $message = "The project identifier is empty";
        }
        if ($ok == true)
        {
                if (count ($array_users) <= 0)
                {
                        $ok = false;
                        $message = "The array of users is empty";
                }
        }
        $tprojectMgr = new testproject($db);
        $TestLinkProject = $tprojectMgr->get_by_id($pProjectID);
        if (($ok == true) && !$TestLinkProject)
        {
                $ok = false;
                $message = "The project " . $projectName . " does not exist";
        }

        if  ($ok == true)
        {
                $op = new stdClass();
                $op->user = new tlUser();
                $role = new tlRole();
                $array_users_clean = array();
                
                $existingUserRoles = $tprojectMgr->getUserRoleIDs($TestLinkProject["id"]);
							
				foreach($array_users AS $userName=>$roleName)
                {
					$argsMapping = $roleName;	
					$userID = $op->user->doesUserExist($db,$argsMapping["userName"]);      
                    $roleID = $role->doesRoleExist($db,$argsMapping["roleName"],0);       
                    $array_users_clean[$userID]=$roleID;				    
                }
				
                $existingUserRoles_clean = array();
                foreach ($existingUserRoles as $key=>$val){
                        $existingUserRoles_clean[$val["user_id"]] = $val["role_id"];    
                }
				
                foreach ($array_users_clean as $user_id=>$role_id)
                {
                        if(isset($existingUserRoles_clean[$user_id]) && ($existingUserRoles_clean[$user_id] != $role_id))
                        {
                                $sql = "UPDATE user_testproject_roles SET role_id='".$role_id."' WHERE user_id = '".$user_id."' AND testproject_id = '".$TestLinkProject["id"]."'";
                                																
								if(!$db->exec_query($sql))
                                {
                                        $ok = false;
                                        $message = "Can't update role (".$role_id.") for user ".$userName." for project ".$project_id.".";
                                }
                        }elseif(!isset($existingUserRoles_clean[$user_id])){
                                if($tprojectMgr->addUserRole($user_id,$TestLinkProject["id"],$role_id) < tl::OK){
                                        $ok = false;
                                        $message = "Can't set role (".$role_id.") for user ".$userName." for project ".$project_id.".";
                                }       
                        }
                }
        }
        
		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
		
        
}

function removeRole($pProjectID, $pUserName)
{
        $ok = true;
        $db = 0;
        if($pProjectID == null)
        {
                $ok = false;
                $message = "Project id not specified";
        }
        if($pUserName == null)
        {
                $ok = false;
                $message = "Username not specified";
        }
        if($ok == true)
        {
                doDBConnect($db);

                $tprojectMgr = new testproject($db);
                $TestLinkProject = $tprojectMgr->get_by_id($pProjectID);    
                if($TestLinkProject == null)
                {
                        $ok = false;
                        $message = "Project ".$pProjectID." not found.";
                }else{
                        $op = new stdClass();
                        $op->user = new tlUser();
                        $userID = $op->user->doesUserExist($db,$pUserName);     
                        if($userID == null)
                        {
                                $ok = false;
                                $message = "User ".$pUserName." not found.";
                        }else{
                                $existingUserRoles = $tprojectMgr->getUserRoleIDs($TestLinkProject["id"]);
                                if($existingUserRoles == null)
                                {
                                        $ok = false;
                                        $message = "No role found in project ".$pProjectID." for user ".$pUserName.".";       
                                }else{
                                        $existingUserRoles_clean = array();
                                        foreach ($existingUserRoles as $key=>$val){
                                                $existingUserRoles_clean[$val["user_id"]] = $val["role_id"];
                                        }                               
                                        if(!isset($existingUserRoles_clean[$userID]))
                                        {
                                                $ok = false;
                                                $message = "No role found in project ".$pProjectID." for user ".$pUserName.".";
                                        }else{
                                                $sql = "DELETE FROM user_testproject_roles WHERE user_id = '".$userID."' AND testproject_id = '".$TestLinkProject["id"]."' AND role_id = '".$existingUserRoles_clean[$userID]."'";
                                                if(!$db->exec_query($sql))
                                                {
                                                        $ok = false;
                                                        $message = "Can't remove role (".$existingUserRoles_clean[$userID].") for user ".$pUserName." for project ".$pProjectID.".";
                                                }                                               
                                        }
                                }
                        }
                }
        }
		
        $resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
}


function createProject($name,$descr='')
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $tprojectMgr = new testproject($db);
        $item = new stdClass();
        $item->name = $name;
        $item->notes = $descr;
        //$item->color = '';
        $item->options = new stdClass();
        $item->options->requirementsEnabled = 1;
        $item->options->testPriorityEnabled = 1;
        $item->options->automationEnabled = 1;
        $item->options->inventoryEnabled = 1;
        $item->active=1;
        $item->is_public=1;
        $item->prefix = $name;
        
        $tprojectExist = $tprojectMgr->get_by_name($name);
        if($tprojectExist){
                $message = "The project ".$name." already exist.";
        }else{
                $new_id = $tprojectMgr->create($item);

                if ($new_id)
                {
                        $ok = true;     
                }else{
                        $message = "Unable to create ".$name." project.";
                }
        }

		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success", "id" => $new_id, "name" => $name);		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "id" => $tprojectExist["0"]["id"], "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo; 
}


function deleteProject($pProjectID)
{

        
        $ok = false;
        $db = 0;
        doDBConnect($db);
        
        $tprojectMgr = new testproject($db);

        $existProject = $tprojectMgr->get_by_id($pProjectID);
        if(!$existProject){
                $message = "The project ".$pProjectID." doesn't exist.";
        }else{
                if ($tprojectMgr->delete($existProject["id"]))
                {
                        $ok = true;
                }else{
                        $message = "Unable to delete ".$name." project.";
                }
        }

		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }	
	return 	$resultInfo;
}

function updateProject($pProjectID='', $description='', $name='')
{       
        $ok = false;
        $db = 0;
        doDBConnect($db);
        $tprojectMgr = new testproject($db);
        $existProject = $tprojectMgr->get_by_id($pProjectID);
        if(!$existProject){	
                $message = "The project ".$pProjectID." doesn't exist.";
        }else{
                $options = new stdClass();
                $options->requirementsEnabled = 1;
                $options->testPriorityEnabled = 1;
                $options->automationEnabled = 1;
                $options->inventoryEnabled = 1;

                if ($tprojectMgr->update($existProject["id"],$name,$existProject["color"],$description,$options,null,$name))
                {
                        $ok = true;
                }else{
                        $message = "Unable to update ".$name." project.";
                }
        }

        $resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success");		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }
	return $resultInfo;
		
}

function getProjectByName ($projectName)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $tprojectMgr = new testproject($db);

        $existProject = $tprojectMgr->get_by_name($projectName);
        if(!$existProject){
                $message = "The project ".$name." doesn't exist.";
        }else{
                $ok = true;
        }

		$resultInfo = array();
        if ($ok == true)
        {
			$resultInfo[]= array("STATUS" => "success", "id" => $existProject["0"]["id"], "name" => $existProject["0"]["name"], "description" => $existProject["0"]["notes"]);		
        }
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }

        return $resultInfo;
}


function getProjectById ($projectId)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $tprojectMgr = new testproject($db);	

        $existProject = $tprojectMgr->get_by_id($projectId);
        if(!$existProject){
                $message = "The project ".$name." doesn't exist.";
        }else{
                $ok = true;
        }
        
        $resultInfo = array();
        if ($ok == true)
        {                
			$resultInfo[]= array("STATUS" => "success", "id" => $existProject["id"], "name" => $existProject["name"], "description" => $existProject["notes"]);		
		}
        else
        {
            $resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
        }
        return $resultInfo;
}


function getRoleID($rolename)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $id=0;
        $role = new tlRole();
        return $role->doesRoleExist($db,$rolename,$id);
}


function getRoleById($roleId)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);

        $role = new tlRole();
		$role->id = $roleId;

		$resultInfo = array();
		if ($role->readFromDB($db,self::TLOBJ_O_SEARCH_BY_ID) >= tl::OK && $role->dbID != $id)
		{
			
			$resultInfo[]= array( "id" => $role->id, "name" => $role->name, "description" => $role->description);		
					
			
		}
		else
		{
			$message = "Unknown role";
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
		}		
		
		return $resultInfo;
}


function getRequirementsByProject($projectId)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);
	$requirements = array();
	$resultInfo = array();
        
	$tprojectMgr = new testproject($db);
        $existProject = $tprojectMgr->get_by_id($projectId);		
        
	if(!$existProject){
                $message = "The project ".$projectId." doesn't exist.";
        }else{
                $ok = true;
        }
        if ($ok == true)
        {
		// Recherche du type de maniere recursive les requirements
		$i = 0;
		$requirements = _getRequirements($db,$projectId, $requirements, $i);	
	}
        else
        {
		$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
		syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
		return $resultInfo;
        }
        return $requirements;
}

function _getRequirements(&$db,$id,&$requirements,&$i)
{   
	$sql = "SELECT nodes_hierarchy.id AS id, nodes_hierarchy.name AS name, nodes_hierarchy.node_type_id AS node_type_id FROM nodes_hierarchy WHERE nodes_hierarchy.parent_id = '".$id."'";
	$result = $db->exec_query($sql);
	if ($result)
	{
		
		while($row =  $db->fetch_array($result))
		{
			if ($row["node_type_id"] == "7")
			{
				// Recherche de la version de la derniere exigence
				$requirements = _getRequirementDocIdAndVersion($db,$requirements,$row['id'],$row['name'],$i);
			}
			else
			{
				$rowId = $row["id"];
				_getRequirements($db,$rowId,$requirements,$i);
			}	
		}		
	}
	else
	{	    
			$requirements[0]["id"] = "0";
			$requirements[0]["name"] = "erreur";
			$requirements[0]["node_type_id"] = "0";
	}
	return $requirements;
}

function _getRequirementDocIdAndVersion(&$db,&$requirements,$req_id,$name,&$i)
{   
	$sql = "SELECT req_versions.version AS version, requirements.req_doc_id AS req_doc_id FROM nodes_hierarchy, req_versions, requirements WHERE nodes_hierarchy.id = req_versions.id and requirements.id = '".$req_id."' and nodes_hierarchy.parent_id = '".$req_id."' ORDER BY nodes_hierarchy.id DESC";
	$result = $db->exec_query($sql);
	if ($result)
	{
		$row =  $db->fetch_array($result);	
		$requirements[$i]["id"] = $req_id;
		$requirements[$i]["name"] = $name;
		$requirements[$i]["req_doc_id"] = $row['req_doc_id'];
		$requirements[$i]["version"] = $row['version'];
		$i++;			
	}
	return $requirements;
}

function getTestsByRequirement ($requirementId,$projectId)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);
	    $testsCase = array();
		$resultInfo = array();
        $tprojectMgr = new testproject($db);
        $existProject = $tprojectMgr->get_by_id($projectId);		
        if(!$existProject){
                $message = "The project ".$projectId." doesn't exist.";
        }else{
                $ok = true;
        }
        if ($ok == true)
        {
			// Recherche du prefix pour avoir le nom externe du test
			$prefix = $existProject["prefix"];
			
			// Recherche des testsCase
			$testsCase = _getTests($db,$requirementId,$testsCase,$prefix);	
		}
        else
        {
			$resultInfo[]= array(  "STATUS" => "failure", "MESSAGE" => xmlEncodeString ($message));		
			syslog (LOG_ERR, __FILE__ . " - " . __FUNCTION__ . " - " . $message);
			return $resultInfo;
        }
        return $testsCase;
}

function _getTests(&$db,$req_id,&$testsCase,$prefix)
{   
	$sql = "SELECT req_coverage.req_id  AS req_id , req_coverage.testcase_id  AS testcase_id , nodes_hierarchy.name AS name FROM req_coverage, nodes_hierarchy WHERE req_coverage.testcase_id = nodes_hierarchy.id and req_coverage.req_id = '".$req_id."'";
	$result = $db->exec_query($sql);
	if ($result)
	{
		$i = 0;
		while($row =  $db->fetch_array($result))
		{
			// Recherche de l'id externe et des versions
			$testsCase = _getTestExternalIdAndVersion($db,$testsCase,$prefix,$row['req_id'],$row['testcase_id'],$row['name'],$i);				
		}		
	}
	return $testsCase;
}

function _getTestExternalIdAndVersion(&$db,&$testsCase,$prefix,$req_id,$testcase_id,$name,&$i)
{   
	$sql = "SELECT tcversions.tc_external_id AS tc_external_id, tcversions.version AS version FROM nodes_hierarchy, tcversions WHERE nodes_hierarchy.id = tcversions.id and nodes_hierarchy.parent_id = '".$testcase_id."' ORDER BY nodes_hierarchy.id DESC";
	$result = $db->exec_query($sql);
	$externalId = "0";

	while($row =  $db->fetch_array($result))
	{
		$testsCase[$i]['req_id'] = $req_id; 	
		$testsCase[$i]['testcase_id'] = $testcase_id; 	
		$testsCase[$i]['name'] = $name; 
		$testsCase[$i]['tc_external_id'] = $row['tc_external_id'];
		$testsCase[$i]['version'] = $row['version'];	
		$testsCase[$i]['prefix'] = $prefix;	
		$i++;			
	}
	return $testsCase;
}

function getRequirementId($docid)
{
        $ok = false;
        $db = 0;
        doDBConnect($db);
        
	    $requirementId = _getRequirementId($db,$docid);	

        return $requirementId;
}

function _getRequirementId(&$db,$docid)
{   
	$sql = "SELECT requirements.id AS id FROM requirements WHERE requirements.req_doc_id = '".$docid."'";
	$result = $db->exec_query($sql);
	if ($result)
	{
		$row =  $db->fetch_array($result);	
		$requirementId = $row['id'];			
	}
	return $requirementId;
}


function xmlEncodeString ($string)
{
        $value = $string;
        if (strchr ($string, "&") !== false)
        {
                $value = str_replace ("&", "&amp;", $value);
        }
        if (strchr ($string, "<") !== false)
        {
                $value = str_replace ("<", "&lt;", $value);
        }
        if (strchr ($string, ">") !== false)
        {
                $value = str_replace (">", "&gt;", $value);
        }
        if (strchr ($string, "'") !== false)
        {
                $value = str_replace ("'", "&apos;", $value);
        }
        if (strchr ($string, "\"") !== false)
        {
                $value = str_replace ("\"", "&quot;", $value);
        }
        return $value;
}

?>
