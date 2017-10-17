#!/bin/bash
#set -x

##############################################################
#
#    author: Marc Blachon
#    date: 07/2014
#
#    modification: 
#          18/09/14:  add delivery itests, remove logs and junit report.
#          2015:
#              adding delivery, mailing list test suites
#              improving summury displaying
#          21/04/15: adding jacoco measurement
#
########################################################################



BASE_DIR=`readlink -f $(dirname $0)`
PROPERTIES=`readlink -f $1`
shift

################################################################################



readProperties()
{
	echo "Reading '$PROPERTIES' file..."
	while read line
	do
		if [[ ${line} =~ .*=.* ]] && [[ ! ${line} =~ ^#.* ]] ; then
			key=$(cut -d '=' -f1 <<< ${line})
			value=$(cut -d '=' -f2 <<< ${line})
			eval "value=${value}"
			export ${key}="$value"
		fi
	done < ${PROPERTIES}
	echo "'$PROPERTIES' is read."
}




function send_email () {
	itests_launched=$1
	SCRIPT_LOG=/tmp/${logfile}
	if [ "$itests_launched" = "yes" ]; then    

		#debug sending mail
		#end_tests_date=09-18-2014-05-47
	
		## results files copied to Junit_result directory
		##TODO: as result file is no more used, it should be removed from propagation test suite.
		#PROPAGATION_RESULT=$AUTO_TEST_PATH/${JUNIT_RESULT}/tools-propagation-itests_${end_tests_date}.result
		JUNIT_PROPAGATION_REPORT=${AUTO_TEST_PATH}/${JUNIT_RESULT}/${TEST_SUITE_PROPAGATION}_${end_tests_date}.xml
		JUNIT_DELIVERY_REPORT=${AUTO_TEST_PATH}/${JUNIT_RESULT}/${TEST_SUITE_DELIVERY}_${end_tests_date}.xml
		JUNIT_MAILINGLIST_REPORT=${AUTO_TEST_PATH}/${JUNIT_RESULT}/${TEST_SUITE_MAILINGLIST}_${end_tests_date}.xml
		## summary sent within boby email
		SUMMARY_HEADER=${AUTO_TEST_PATH}/${JUNIT_RESULT}/summary.txt
		
		

		##adding headers for files
		#sed -i '1s/^/\n-----tests results for Tool propagation tests ---------------\n/' $PROPAGATION_RESULT
		#sed -i '1s/^/\n----------------junit results---------------\n/' $JUNIT_PROPAGATION_REPORT

		## ------------------- create summary_header and its title.
		echo "                Integration tests SUMMARY on $VM_LOCAL ($SOURCE_VERSION)">${SUMMARY_HEADER}
		echo "                ----------------------------------------------------------">>${SUMMARY_HEADER}
		echo ""
		echo "        Junit files repots have been copied onto: management-dev under folder:/datas/workspace/AutoTest/Itests/Junit_Results."
		echo "        More stack traces within junit files could be then analysed."
		echo ""

		## -------------------- display nb tests/failure/success
		
		# For tools propagation
		if [ "$LAUNCH_PROPAGATION_TESTS" = "yes" ]; then
			Failure_propagation=`cat ${JUNIT_PROPAGATION_REPORT}|grep "testsuite failures"|awk '{print $2}'`
			Error_propagation=`cat ${JUNIT_PROPAGATION_REPORT}|grep "testsuite failures"|awk '{print $4}'`
			Test_propagation=`cat ${JUNIT_PROPAGATION_REPORT}|grep "testsuite failures"|awk '{print $5}'`
			Suite=`cat ${JUNIT_PROPAGATION_REPORT}|grep "testsuite failures"|awk '{print $6 $7 $8 $9}'`
			Suite_propagation=`echo ${Suite%?}`

			echo "suite $Suite_propagation">>${SUMMARY_HEADER}
			echo "nb $Test_propagation">>${SUMMARY_HEADER}
			echo "nb $Failure_propagation">>${SUMMARY_HEADER}
			echo "nb $Error_propagation">>${SUMMARY_HEADER}
		fi

		# for delivery
		if [ "$LAUNCH_DELIVERY_TESTS" = "yes" ]; then	
			Failure_delivery=`cat ${JUNIT_DELIVERY_REPORT}|grep "testsuite failures"|awk '{print $2}'`
			Error_delivery=`cat ${JUNIT_DELIVERY_REPORT}|grep "testsuite failures"|awk '{print $4}'`
			Test_delivery=`cat ${JUNIT_DELIVERY_REPORT}|grep "testsuite failures"|awk '{print $5}'`
			Suite=`cat ${JUNIT_DELIVERY_REPORT}|grep "testsuite failures"|awk '{print $6 $7 $8 $9}'`
			Suite_delivery=`echo ${Suite%?}`

			echo "">>${SUMMARY_HEADER}
			echo "suite $Suite_delivery">>${SUMMARY_HEADER}
			echo "nb $Test_delivery">>${SUMMARY_HEADER}
			echo "nb $Failure_delivery">>${SUMMARY_HEADER}
			echo "nb $Error_delivery">>${SUMMARY_HEADER}
		fi

		# for mailing list	
		if [ "$LAUNCH_MAILINGLIST_TESTS" = "yes" ]; then
			Failure_mailing_list=`cat ${JUNIT_MAILINGLIST_REPORT}|grep "testsuite failures"|awk '{print $2}'`
			Error_mailing_list=`cat ${JUNIT_MAILINGLIST_REPORT}|grep "testsuite failures"|awk '{print $4}'`
			Test_mailing_list=`cat ${JUNIT_MAILINGLIST_REPORT}|grep "testsuite failures"|awk '{print $5}'`
			Suite=`cat ${JUNIT_MAILINGLIST_REPORT}|grep "testsuite failures"|awk '{print $6 $7 $8 $9}'`
			Suite_mailing_list=`echo ${Suite%?}`

			echo "">>${SUMMARY_HEADER}
			echo "suite $Suite_mailing_list">>${SUMMARY_HEADER}
			echo "nb $Test_mailing_list">>${SUMMARY_HEADER}
			echo "nb $Failure_mailing_list">>${SUMMARY_HEADER}
			echo "nb $Error_mailing_list">>${SUMMARY_HEADER}
		fi	

		##---------------- adding junit result tests. --------------------------------
		

		

		


		echo "              Details on junit tests:">>${SUMMARY_HEADER}
		echo "              -----------------------">>${SUMMARY_HEADER}

		# For tools propagation
		if [ "$LAUNCH_PROPAGATION_TESTS" = "yes" ]; then
			Synthese_propagation=`awk -F " " '{
 			if ( $1 == "<testcase" ) {print $1 $4}
 			if ( $1 == "<error" ) {print $0}
 			if ( $1 == "<failure" ) {print $0}
			}' ${JUNIT_PROPAGATION_REPORT}`
		
			echo "For suite $Suite_propagation">>${SUMMARY_HEADER}
			echo "$Synthese_propagation">>${SUMMARY_HEADER}
		fi

		# for delivery
		if [ "$LAUNCH_DELIVERY_TESTS" = "yes" ]; then	
			Synthese_delivery=`awk -F " " '{
 			if ( $1 == "<testcase" ) {print $1 $4}
 			if ( $1 == "<error" ) {print $0}
 			if ( $1 == "<failure" ) {print $0}
			}' ${JUNIT_DELIVERY_REPORT}`
		
			echo "">>${SUMMARY_HEADER}
			echo "For suite $Suite_delivery">>${SUMMARY_HEADER}
			echo "$Synthese_delivery">>${SUMMARY_HEADER}
		fi

		# for mailing list
		if [ "$LAUNCH_MAILINGLIST_TESTS" = "yes" ]; then
			Synthese_mailing_list=`awk -F " " '{
 			if ( $1 == "<testcase" ) {print $1 $4}
 			if ( $1 == "<error" ) {print $0}
 			if ( $1 == "<failure" ) {print $0}
			}' ${JUNIT_MAILINGLIST_REPORT}`
		
			echo "">>${SUMMARY_HEADER}
			echo "For suite $Suite_mailing_list">>${SUMMARY_HEADER}
			echo "$Synthese_mailing_list">>${SUMMARY_HEADER}
		fi
			
		echo "---------------------------------------------------------------------------------------">>${SUMMARY_HEADER}
		echo "">>${SUMMARY_HEADER}
		echo "">>${SUMMARY_HEADER}

		#title put on at the beginning of the header
		#sed -i '1s/^/\n------------- Integration tests SUMMARY on $VM_LOCAL -------------------\n/' $SUMMARY_HEADER
		
		# send email for all persons in the list
		for i in "${LIST_TO[@]}"
			do
			## send only the summary into the email body.
			#cat ${SUMMARY_HEADER} ${SCRIPT_LOG} ${PROPAGATION_RESULT} ${JUNIT_PROPAGATION_REPORT} | /bin/mail -s ${SUBJECT} $i
			#cat ${SUMMARY_HEADER} ${PROPAGATION_RESULT} | /bin/mail -s ${SUBJECT} $i
			cat ${SUMMARY_HEADER} | /bin/mail -s ${SUBJECT} ${i}
		done
	else
		for i in "${LIST_TO[@]}"
			do
			cat ${SCRIPT_LOG} | /bin/mail -s ${SUBJECT} ${i}
		done
	fi	
}


function launch_xml_import () {
VM_IP=$1
VM_NAME=$2

## ------------------------- pushing xml files ------------------------------
#import_itests_validation.xml
scp ${AUTO_TEST_PATH}/XML/${IMPORT_XML} root@${VM_IP}:${XML_TARGET_VM_PATH}
RET=$?
if [ ${RET} -ne 0 ]; then
	echo "ERROR copying $IMPORT_XML to $VM_IP:$XML_TARGET_VM_PATH"
	exit ${RET}
fi

#import_itests_validation2.xml
scp ${AUTO_TEST_PATH}/XML/${IMPORT_XML2} root@${VM_IP}:${XML_TARGET_VM_PATH}
RET=$?
if [ ${RET} -ne 0 ]; then
	echo "ERROR copying $IMPORT_XML2 to $VM_IP:$XML_TARGET_VM_PATH"
	exit ${RET}
fi

#IMPORT_XML_MAILING_LIST=import_itests_mailinglist.xml to 
scp ${AUTO_TEST_PATH}/XML/${IMPORT_XML_MAILING_LIST} root@${VM_IP}:${XML_TARGET_VM_PATH}
RET=$?
if [ ${RET} -ne 0 ]; then
	echo "ERROR copying $IMPORT_XML_MAILING_LIST to $VM_IP:$XML_TARGET_VM_PATH"
	exit ${RET}
fi

ssh root@${VM_IP} "chown -R ${PROFILE_USER}:${PROFILE_USER} $XML_TARGET_VM_PATH"

echo "pushing xml files for itests tools propagation to $XML_TARGET_VM_PATH"

## -------------------- starting import -----------------------------------------
### for propagation itests
#/datas/safran/engines/karaf/bin/client forge:importdatas /datas/safran/tmp/import_itests_validation.xml
ssh root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client forge:importdatas $XML_TARGET_VM_PATH/$IMPORT_XML"

### for mailing suite test suite
ssh root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client forge:importdatas $XML_TARGET_VM_PATH/$IMPORT_XML_MAILING_LIST"
echo "importing xml files for itests: tools propagation & mailing list has been done."

return 0
}


function launch_itests () {
VM_IP=$1

#get Id of the bundle tools propagation itests
#498 | Active    |  80 | 3.4.3.SNAPSHOT  | NovaForge(tm) :: Tools :: Propagation :: it
bundle_id=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "su ${PROFILE_USER} -c \"/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:list|grep Propagation|grep Tools|cut -c1-3\""`

if [ -z "$bundle_id" ]; then
	echo "BIG ERROR: either karaf client is not accessible (Failed to get the session.) or the itests feature has not been deployed !!!"
	exit 1
else
	# stop
	resultat=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:stop $bundle_id"`
	#echo "resultat= $resultat"
	echo "bundle with id: $bundle_id has been stopped."

	sleep 10

	# update (only useful if restart scripts after pushing new jer for propagation tests.
	resultat=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:update $bundle_id"`
	#echo "resultat= $resultat"
	echo "bundle with id: $bundle_id has been updated."
	
	sleep 10

	# start
	resultat=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:start $bundle_id"`
	#echo "resultat= $resultat"
	echo "bundle with id: $bundle_id has been started."
fi
return 0
}

##############################################################################################
##### work around to launch test by stopping/starting the runner instead of the test bundle !!
############################################################################################
function launch_itests_with_work_around_runner () {
VM_IP=$1

#get Id of the bundle tools propagation itests
#498 | Active    |  80 | 3.4.3.SNAPSHOT  | NovaForge(tm) :: Tools :: Propagation :: it
bundle_id=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "su ${PROFILE_USER} -c \"/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:list|grep JUnit4OSGi|grep iTests|cut -c1-3\""`

if [ -z "$bundle_id" ]; then
	echo "BIG ERROR: either karaf client is not accessible (Failed to get the session.) or the itests feature has not been deployed !!!"
	exit 1
else
	# stop
	resultat=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:stop $bundle_id"`
	#echo "resultat= $resultat"
	echo "bundle with id: $bundle_id has been stopped."

	sleep 10	

	# start
	resultat=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client bundle:start $bundle_id"`
	#echo "resultat= $resultat"
	echo "bundle with id: $bundle_id has been started."
fi
return 0
}

#############################################################################################
# launch itests with the new karaf command: 
#   ex.:    >novaforge-itests:junit Tools_Propagation_Test_Suite
###########################################################################################
function launch_itests_with_karaf_starter_command () {
VM_IP=$1

############################
## LAUNCH TEMPLATE tests
############################
	
# start Tool Propagation suite
	if [ "$LAUNCH_PROPAGATION_TESTS" = "yes" ]; then
		ssh root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client novaforge-itests:junit Tools_Propagation_Test_Suite"
	fi	

# start Delivery test suite
	if [ "$LAUNCH_DELIVERY_TESTS" = "yes" ]; then
		ssh root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client novaforge-itests:junit Delivery_Plugin_Test_Suite"
	fi
	
# start Mailing list test suite
	if [ "$LAUNCH_MAILINGLIST_TESTS" = "yes" ]; then
		ssh root@${VM_IP} "/datas/${PROFILE_DIR}/engines/karaf/bin/client novaforge-itests:junit Mailinglist_Test_Suite"
	fi
	
}

#############################################################################################
#    perform all actions for Jacoco code coverage analysis 
#   
###########################################################################################
function stop_karaf_dump_jacoco_reports () {

## Phases supposed to be done by installation phase within "itests" profile
#  options for jacoco for KARAF_OPTS
#  jar of jacoco agent installation
#  jacoco reports forlders created

#### Stop karaf
std_stop_karaf_remove_log ${VM_IP_LOCAL} ${PROFILE_DIR}
ret=$?
if [ ${ret} -ne 0 ]; then
	echo "ERROR:  Karaf has not been stopped !!!!!"
	exit 1
else
	echo "Karaf has been stopped! "
fi

# save reposts from the current run; prepare for new one
TIMESTAMP=`save_and_init_jacoco_reports ${VM_IP_LOCAL} ${PROFILE_DIR}`

## wait results generation, save current results, prepare for others
sleep ${WAIT_GENERATE_JACOCO_RESULTS}
}

function start_karaf_check_portal () {
## start karaf
start_karaf ${VM_IP_LOCAL} ${PROFILE_DIR}
echo "waiting ${FORGE_START_TIME} sec. for the complete start of the forge ....... "
sleep ${FORGE_START_TIME}
echo "wait time karaf is completly started"

## check at least the portal is responding
check_public_portal_page ${VM_IP_LOCAL} ${VM_LOCAL}
  res_check=$?
  if [ ${res_check} -ne 0 ]; then
		echo "Le portail est KO !!! sur $VM_NAME"
  fi
}


#-----------------------------------------------------------------------------------------------------------------------
#                          MAIN                MAIN              MAIN              MAIN
#-----------------------------------------------------------------------------------------------------------------------

##### READ PROPERTIES FILE

if [ ! -f ${PROPERTIES} ]; then
  echo "$PROPERTIES does not exist. Exiting..."
  exit 1
else
	readProperties
fi

## check if some properties has been set ...... but not full check ....
if [ -z "$VM_IP_LOCAL" -o -z "$VM_LOCAL" -o -z "$FORGE_START_TIME" -o -z "$RETRY" -o -z "$AUTO_TEST_PATH" -o -z "$XML_TARGET_VM_PATH" ]; then 
	echo "At least one of the properties seem empty. Check properties file provided as first argument for starting the tests. Exiting..."
	exit 1;
fi

#load utility functions
. ${AUTO_TEST_PATH}/utility-itests.sh

#### CHECK MAIN_PROFILE
if [ "$MAIN_PROFILE" = "bull" ]; then
	export PROFILE_USER=novaforge
	export PROFILE_DIR=novaforge3
else
	if [ "$MAIN_PROFILE" = "safran" ]; then
		export PROFILE_USER=safran
		export PROFILE_DIR=safran
	else
		echo "ERROR ON THE MAIN_PROFILE: set either bull or safran"
	fi
fi


#### CHECK PORTAL UP
#launch actions only if forge is well installed (portal up)
if [ "$CHECK_PORTAL" = "yes" ]; then
	${AUTO_TEST_PATH}/check_forge_successful.sh
	ret_protal_ok=$?
else
	ret_protal_ok=0
fi

if [ ${ret_protal_ok} -eq 0 ]; then
	echo "forge is successfully running or check portal has been de-actived!!!!"

	######## XML IMPORT
	if [ "$LAUNCH_XMLIMPORT" = "yes" ]; then		
		#launch xml import for itests data (if import already done, no 	
		launch_xml_import ${VM_IP_LOCAL} ${VM_LOCAL}
		ret=$?
		if [ ${ret} -ne 0 ]; then
			echo ""	
			echo "Error when copying xml import files!"
				if [ "$NOTIFICATION_EMAIL" = "yes" ]; then      
					send_email no
				fi

			exit 1
		fi
	
		# wait creation of applications and propagation are finished.
		echo "waiting ${WAIT_TIME_APPLICATIONS_CREATION} sec. for applications creation ..."
		sleep ${WAIT_TIME_APPLICATIONS_CREATION}
	fi	

	####### ITESTS SUITES		
	if [ "$LAUNCH_ITESTS" = "yes" ]; then
		#launch  itests
		###### use work around that stop/start junit4osgi itests runner instead of the test bundle !!! ########################################
		#launch_itests ${VM_IP_LOCAL}
		launch_itests_with_karaf_starter_command ${VM_IP_LOCAL}
		echo ""
		echo "WARNING    WARNING   WARNING   WARNING    WARNING"
		echo "Please wait ${WAIT_TIME_END_TESTS} sec. until tests will finish....."
		echo "Then results of junit suite will be generated onto remote VM under ${REMOTE_JUNIT_RESULT}."
		echo "either log in to: ${VM_LOCAL} and get test results under ${REMOTE_JUNIT_RESULT}."
		echo "or get junit result file also pulled to $AUTO_TEST_PATH/Junit_Results directory"	

	
		#waiting for the end of the tests before getting results from remote vm. 
		echo "waiting ${WAIT_TIME_END_TESTS} sec. for the completion of the tests ..." 
		sleep ${WAIT_TIME_END_TESTS}
		echo "itests execution finished."

		### pulling junit and results file onto vm
		end_tests_date=`date +"%m-%d-%Y-%H-%M"`
		# For propagation
			scp root@${VM_IP_LOCAL}:${REMOTE_JUNIT_RESULT}/${TEST_SUITE_PROPAGATION}.xml ${AUTO_TEST_PATH}/${JUNIT_RESULT}/${TEST_SUITE_PROPAGATION}_${end_tests_date}.xml
			# results tests
			#scp root@${VM_IP_LOCAL}:${REMOTE_JUNIT_RESULT}/../tools-propagation-itests.result $AUTO_TEST_PATH/${JUNIT_RESULT}/tools-propagation-itests_${end_tests_date}.result

		# For delivery
			scp root@${VM_IP_LOCAL}:${REMOTE_JUNIT_RESULT}/${TEST_SUITE_DELIVERY}.xml ${AUTO_TEST_PATH}/${JUNIT_RESULT}/${TEST_SUITE_DELIVERY}_${end_tests_date}.xml

		# For mailing list
			scp root@${VM_IP_LOCAL}:${REMOTE_JUNIT_RESULT}/${TEST_SUITE_MAILINGLIST}.xml ${AUTO_TEST_PATH}/${JUNIT_RESULT}/${TEST_SUITE_MAILINGLIST}_${end_tests_date}.xml

		echo "junit test results have been copied onto: management-dev under folder:/datas/workspace/AutoTest/Itests/Junit_Results."
		echo "more stack trace within junit files could be analysed"
	fi

	### SENDING EMAIL
	###------ WARNING:WARNING:WARNING:
	LIST_TO=( "novaforge-team-realisation@novaforge.bull.com" "iuliana.onofrei@bull.net" )  
	#LIST_TO=( "marc.blachon@bull.net" )

	if [ "$NOTIFICATION_EMAIL" = "yes" ]; then      
		send_email yes
	fi
	
	####### JACOCO CODE COVERAGE ANALYSIS
	if [ "$LAUNCH_JACOCO_ANALYSIS" = "yes" ]; then
		### dump coverage measurements
		stop_karaf_dump_jacoco_reports

		### pulling results from the vm
		scp root@${VM_IP_LOCAL}:/datas/${PROFILE_DIR}/datas/jacoco/reports/jacococlasses_${TIMESTAMP}.tar ${AUTO_TEST_PATH}/${JACOCO_RESULT}
		scp root@${VM_IP_LOCAL}:/datas/${PROFILE_DIR}/datas/jacoco/reports/jacoco_${TIMESTAMP}.exec ${AUTO_TEST_PATH}/${JACOCO_RESULT}

		send_jacoco_results_to_pic4_itests_trunk_job ${VM_IP_LOCAL} ${PROFILE_DIR} ${TIMESTAMP} ${PIC_USER} ${PIC_SERVER} ${PIC_JOB_FOR_JACOCO_REPORTS}

		## restart karaf
		start_karaf_check_portal
	fi	
	 
#### PORTAL KO
else
	echo ""
	echo "Aie aie aie"
	echo "forge is NOT successfully running !"
	if [ "$NOTIFICATION_EMAIL" = "yes" ]; then      
		send_email no
	fi
fi



