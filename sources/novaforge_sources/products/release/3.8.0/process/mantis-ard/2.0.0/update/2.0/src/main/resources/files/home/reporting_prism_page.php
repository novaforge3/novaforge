<?php
/**
* NovaForge(TM) is a web-based forge offering a Collaborative Development and
* Project Management Environment.
*
* Copyright (C) 2007-2009 BULL SAS
* Copyright (C) 2017  Atos, NovaForge Version 3 and above.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Affero General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

/**
* MantisBT Core API's
*/
require_once( 'core.php' );

auth_reauthenticate();
call_birt();

function do_post_request($url, $postdata, $files = null)
{
	$data = "";
	$boundary = "---------------------".substr(md5(rand(0,32000)), 0, 10);

	//Collect Postdata
	foreach($postdata as $key => $val)
	{
		$data .= "--$boundary\n";
		$data .= "Content-Disposition: form-data; name=\"".$key."\"\n\n".$val."\n";
	}

	$data .= "--$boundary\n";

	$params = array('http' => array(
			'method' => 'POST',
			'header' => 'Content-Type: text/html; boundary='.$boundary,
			'content' => $data
	));

	$ctx = stream_context_create($params);
	$fp = fopen($url, 'rb', false, $ctx);

	if (!$fp) {
		throw new Exception("Problem with $url, $php_errormsg");
	}

	$response = @stream_get_contents($fp);

	if ($response === false) {
		throw new Exception("Problem reading data from $url, $php_errormsg");
	}
	return $response;
}

function call_birt(){

	$t_project_id = project_get_other_type_project_id('management');

	//sample data
	$postdata = array(
			'projectId' => $t_project_id
	);

	//sample image
	$files['rptdesign'] = 'birt\resources\prism.rptdesign';

	$response = do_post_request("http://localhost:8181/mantisARD/reportServlet?projectId=$t_project_id&birt_report_name=prism", $postdata, $files);

	header('Content-Description: File Transfer');
	header('Content-Type: application/pdf');
	header('Content-Disposition: attachment; filename=prism.pdf');
	header('Content-Transfer-Encoding: binary');
	header('Expires: 0');
	header('Cache-Control: must-revalidate');
	header('Pragma: public');
	ob_clean();
	flush();
	echo $response;
	exit;
}

?>