<?php

/**
  NovaForge(tm)
  Copyright (c) 2010-2011 Bull S.A.S.
  
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

require_once(APPPATH.'libraries/nusoap/nusoap.php');

function getLimesurveyGroupId($conf,$p_instance)
{
	ini_set("soap.wsdl_cache_enabled", "0");
							
  $clientSOAP = new nusoap_client($conf['forge']['instance']['endpoint'],true);
  $entities = array();
  
  $clientSOAP->setUseCurl(false);
  $result = $clientSOAP->call($conf['forge']['instance']['method'], array('arg0' => $p_instance));
       
  // Check for a fault
  if ($clientSOAP->fault) 
  {
  	error_log( "fault :'" . $result['faultstring'] ."'");
  	return false;
  } 
  else 
  {
  	// Check for errors
  	$err = $clientSOAP->getError();
  	if ($err) {
  	  // Display the error
  		error_log( "err :'" . $err ."'");
  		return false;
  	} 
  }
  return $result['return'];
}
?>