#!/bin/bash
#set -x



BASE_DIR=`readlink -f $(dirname $0)`
PROPERTIES=`readlink -f $1`
shift

################################################################################
#run
readProperties()
{
	#echo "Reading '$PROPERTIES' file..."
	while read line
	do
		if [[ ${line} =~ .*=.* ]] && [[ ! ${line} =~ ^#.* ]] ; then
			key=$(cut -d '=' -f1 <<< ${line})
			value=$(cut -d '=' -f2 <<< ${line})
			eval "value=${value}"
			export ${key}="$value"
		fi
	done < ${PROPERTIES}

	#echo "'$PROPERTIES' is read."
}

if [ ! -f ${PROPERTIES} ]; then
  echo "$PROPERTIES does not exist. Exiting..."
  exit 1
else
	readProperties
fi

if [ -z "$VM_IP_CENTRAL"  -o -z "$VM_CENTRAL" -o -z "$VM_IP_LOCAL" -o -z "$VM_LOCAL" -o -z "$FORGE_START_TIME" -o -z "$RETRY" ]; then 
	echo "At least one of the properties seem empty. Check properties file provided as first argument for starting the tests. Exiting..."
	exit 1;
fi

function send_email () {
	logfile=`ls -rt /tmp|grep testDistrib_${SOURCE_VERSION}|tail -1`
	full_path_logfile=/tmp/${logfile}
	subject="${SUBJECT}_${SOURCE_VERSION}"
	for i in "${LIST_TO[@]}"
		do
		cat ${full_path_logfile} | /bin/mail -s ${subject} ${i}
	done
}

function launch_xml_import () {
VM_IP=$1
VM_NAME=$2

## ------------------------- pushing xml files ------------------------------
#use IMPORT_XML_MAILING_LIST=import_itests_mailinglist.xml to get data on local.
scp ${XML_DIR_PATH}/${IMPORT_XML_MAILING_LIST} root@${VM_IP}:${XML_TARGET_VM_PATH}
RET=$?
if [ ${RET} -ne 0 ]; then
	print_debug "ERROR copying $IMPORT_XML_MAILING_LIST to $VM_IP:$XML_TARGET_VM_PATH"
	exit ${RET}
fi

ssh root@${VM_IP} "chown -R safran:safran $XML_TARGET_VM_PATH"

print_debug "pushing xml files for itests tools propagation to $XML_TARGET_VM_PATH"
## -------------------- starting import -----------------------------------------
### for mailing suite test suite
ssh root@${VM_IP} "/datas/safran/engines/karaf/bin/client forge:importdatas $XML_TARGET_VM_PATH/$IMPORT_XML_MAILING_LIST"
print_debug "importing xml file to get data on local vm has been done."

return 0
}

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

#----------------------------------------------
# 
### Title and contexte for the test as it's read into email message
echo "Distribution tests between central(AIO): $VM_CENTRAL and zonal: $VM_LOCAL for version: $SOURCE_VERSION"	

#launch checkn forge is well installed (portal up)
if [ "$CHECK_FORGE_SUCCESSFUL" = "yes" ]; then
	#############################
        # LAUNCH CHECK BEFORE TESTS: check portal
	#############################
	${AUTO_TEST_PATH}/check_forge_successful.sh
	ret=$?
	print_debug "check_forge_successful is successfully executed" 
else
	ret=0
fi

if [ ${ret} -eq 0 ]; then

	#launch actions before starting auto tests
	# initialize distribution bundle and Install tests features

	if [ "$ACTIONS_BEFORE_DISTRIB_TESTS" = "yes" ]; then
                #############################
                # LAUNCH ACTIONS BEFORE TESTS: check portal, install tests features, ..
		#############################
		${AUTO_TEST_PATH}/actions_before_distribution_tests.sh
		ret_action=$?
	else
		# set 0 by default.
		ret_action=0
	fi
	
	if [ ${ret_action} -eq 0 ]; then
		
		#launch xml import on central vm in order to get indicators propagation from central
		launch_xml_import ${VM_IP_CENTRAL} ${VM_CENTRAL}
				
		#launch xml import on fille vm in order to get indicators propagation from local
		launch_xml_import ${VM_IP_LOCAL} ${VM_LOCAL}
		
		#wait for propagation......
		sleep 15

		print_debug "actions_before_distribution_tests.sh successfully executed."		

		if [ "$LAUNCH_SUBSCRIBE_TESTS" = "yes" ]; then
		############################	
		# LAUNCH SUBSCRIPTION tests
		############################
		${AUTO_TEST_PATH}/forge_distribution_subscription_test.sh
		fi
				
		############################
		## LAUNCH REFERENTIEL ALFRESCO tests
		############################
		if [ "$LAUNCH_REFERENTIEL_ALFRESCO_TESTS" = "yes" ]; then
			${AUTO_TEST_PATH}/forge_distribution_referentiel_alfresco_test.sh
		fi

		############################
		## LAUNCH REFERENTIEL DOKUWIKI tests
		############################

		if [ "$LAUNCH_REFERENTIEL_DOKUWIKI_TESTS" = "yes" ]; then
			${AUTO_TEST_PATH}/forge_distribution_referentiel_dokuwiki_test.sh
		fi

		############################
		## LAUNCH POSTPONED SYNC tests
		############################

		if [ "$LAUNCH_POSTPONED_SYNC_TESTS" = "yes" ]; then
			${AUTO_TEST_PATH}/forge_distribution_postponed_Sync_test.sh
			${AUTO_TEST_PATH}/forge_distribution_postponed_Sync_test_for_template.sh
		fi

		############################
		## LAUNCH TEMPLATE tests
		############################
		if [ "$LAUNCH_TEMPLATE_TESTS" = "yes" ]; then
			${AUTO_TEST_PATH}/forge_distribution_referentiel_template_test.sh
		fi

		############################
		## LAUNCH REPORTING tests
		############################
		if [ "$LAUNCH_REPORTING_TESTS" = "yes" ]; then
			${AUTO_TEST_PATH}/forge_distribution_reporting_test.sh
		fi	
	fi
	
else
	echo "check_forge_successful has failed !"
fi

### send email to validation team, ....
LIST_TO=( "novaforge-team-realisation@novaforge.bull.com" "iuliana.onofrei@bull.net" )
#LIST_TO=( "marc.blachon@bull.net" )
	if [ "$NOTIFICATION_EMAIL" = "yes" ]; then      
		send_email
	fi





