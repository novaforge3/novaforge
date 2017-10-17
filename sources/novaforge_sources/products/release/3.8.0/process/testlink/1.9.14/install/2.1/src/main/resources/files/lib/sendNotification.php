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
require_once('../nusoap/nusoap.php' );
require_once('../../cfg/novaforge.cfg.php' );
require_once('../../config.inc.php');
require_once('exec.inc.php');
require_once("../functions/attachments.inc.php");
require_once('common.php');

// Init testlink stuff
doSessionStart();
setPaths();
if( isset($_SESSION['locale']) && !is_null($_SESSION['locale']) )
{
  setDateTimeFormats($_SESSION['locale']);
} 

// Init database connection
doDBConnect($db); 

//Get execution test id
$iParams = array("method" => array("GET",tlInputParameter::STRING_N),"exec_id" => array("GET",tlInputParameter::INT_N));
$args = new stdClass();
I_PARAMS($iParams,$args);

// Get current user
$tuser_mgr = new tlUser($db);
$currentuser = $tuser_mgr->getByID($db,$_SESSION['userID']);

//Build parameter
if ( $args->method == 'testFailed' ){
	$params = buildTestFailedParam($db,$args,$currentuser);
	echo sendNotification($args->method,$params);
} else {
	echo "unknown method";
}

function sendNotification($method,$args) 
{
	global $conf;
	ini_set("soap.wsdl_cache_enabled", "0");
	$clientSOAP = new nusoap_client($conf['forge']['functionnal']['endpoint'],true);
	$entities = array();
	$clientSOAP->setUseCurl(false);
		
	$result = $clientSOAP->call($method, $args);       

	// Check for a fault
	if ($clientSOAP->fault) {
	   	return $result['faultstring'];
	} else {
		// Check for errors
		$err = $clientSOAP->getError();
		if ($err) {
			 // Display the error
			return $err;
		} 
	}
	return $result['return'];
}

function buildTestFailedParam($db,$args,$currentUser)
{
        /* Init wanted objects */
        $result = null;
        $tcase_mgr = new testcase($db);
        $tplan_mgr = new testplan($db);
        $tproject_mgr = new testproject($db);
        $tplatform_mgr = new tlPlatform($db);
        $tbuild_mgr = new build_mgr($db);
		    $attachmentRepository = tlAttachmentRepository::create($db);
        /* Get datas for each one */
        $execData = get_execution($db,$args->exec_id);
        $platform = $tplatform_mgr->getByID($execData[0]['platform_id']);
        $build = $tbuild_mgr->get_by_id($execData[0]['build_id']);
		    $exec_ID = $execData[0]['id'];
        $testplan_ID = $execData[0]['testplan_id'];
        $testcase_ID = $execData[0]['tcversion_id'];
        $testPlan = $tplan_mgr->get_by_id($testplan_ID);
        $testproject_ID = $testPlan['testproject_id']; 
        $testCases = $tplan_mgr->getLTCVNewGeneration($testplan_ID,null,array('details' => 'full'));
        $testProject = $tproject_mgr->get_by_id($testproject_ID);
        if($execData[0]['status'] == 'f'){
                $buildStatusStr = "failed";
        }elseif($execData[0]['status'] == 'n'){
                $buildStatusStr = "not run";
        }elseif($execData[0]['status'] == 'b'){
                $buildStatusStr = "blocked";
        }elseif($execData[0]['status'] == 'p'){
                $buildStatusStr = "passed";
        }

        $tc2loop = array_keys($testCases);
        foreach($tc2loop as $tcase_id)
        {
          $p2loop = array_keys($testCases[$tcase_id]);
          foreach($p2loop as $platf_id)
          {
            if (($testCases[$tcase_id][$platf_id]['exec_id']==$exec_ID)      &&
                ($testCases[$tcase_id][$platf_id]['tcversion_id']==$testcase_ID))
            {
               $currentTestCase = $testCases[$tcase_id][$platf_id];
            }
          }  
        }    
        
        $TempCase = $tcase_mgr->get_linked_versions($currentTestCase['tc_id']);
        $Temp2Case = $TempCase[$testcase_ID];
        $Temp3Case = $Temp2Case[$testplan_ID];
        foreach($Temp3Case as $key => $val){
                $TestCaseFull = $val;
		}

		$attachmentInfos = $attachmentRepository->getAttachmentInfosFor($exec_ID,'executions');

		$attachments=null;
		if ($attachmentInfos)
		{
			foreach ($attachmentInfos as $attachmentInfo)
			{
				$attachment = null;
				$content = $attachmentRepository->getAttachmentContent($attachmentInfo["id"], $attachmentInfo);
				if ($content != null)
				{
					$attachment['name'] = $attachmentInfo["file_name"];
					$attachment['fileType'] = $attachmentInfo["file_type"];
					$attachment['title'] =  $attachmentInfo["title"];
					$attachment['content'] = base64_encode($content);
				}
				if ($attachment != null)
				{
					$attachments[] =  $attachment;
				}
			}
        }
       // arg0, arg1, etc. are mandatory naming for nusoap...
        $result = array(
                'arg0' => $currentUser->login,
                'arg1' => $testproject_ID,
                'arg2' => $currentTestCase['external_id'],
                'arg3' => utf8_decode($currentTestCase['tcase_name']),
                'arg4' => $buildStatusStr,
                'arg5' => utf8_decode($testPlan['name']),
                'arg6' => $build['name'],
                'arg7' => $platform['name'],
                'arg8' => utf8_decode($TestCaseFull['summary']),
        				'arg9' => utf8_decode($execData[0]['notes']),
        				'arg10' => $attachments,
                );
        return $result;
}
?>
