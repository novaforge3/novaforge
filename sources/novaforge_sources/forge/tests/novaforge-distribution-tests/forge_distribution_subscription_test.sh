#!/bin/bash
#set -x

###################################################
#    distribution : subscription tests
#    Author: Marc Blachon 31/03/2014
###################################################


test01RequestSubscribe()
{
	# on the LOCAL, subscribing 
	ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client subscribe-forge"
	print_debug "${VM_LOCAL}: subscribing has bean executed."

	sleep 10

	# on CENTRAL, check-received-request for subscribing 
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`
	echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep SUBSCRIBE >/dev/null
	#expected 0 => assertTrue
	RET=$?
	assertTrue 'ERROR: no subscribe request received !' ${RET}  || return
}

test02AcceptValidationSubscribe()
{
	# on CENTRAL, validate subscribing
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client validate-subscription"
	print_debug "${VM_CENTRAL}: validate subscribing has been executed."
	sleep 10

	# on CENTRAL, check no more subscribe request into request list
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`
	echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep SUBSCRIBE >/dev/null
	#expected 1 => assertFalse
	RET=$?
	assertFalse 'ERROR: subscribe request already existing into requests list !!' ${RET}  || return

	# on CENTRAL, list forge registered for diffusion
	list=`ssh -o "StrictHostKeyChecking no" root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client distribution:list-forge-diffusion"`
	#True if $list not empty
	assertTrue 'Error: no subscribed forge.' "[ -n '${list}' ]" || return
	echo ${list}|grep ${VM_LOCAL} >/dev/null
	RET=$?
	# expected 0 => assertTrue
	assertTrue 'ERROR: ${VM_LOCAL} has not been affiliated !' ${RET}  || return
}

test03RequestUnSubscribe()
{
	# on LOCAL unsubscribe
	ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client unsubscribe-forge"
	print_debug "${VM_LOCAL}: unsubscribing has bean executed."

	sleep 10

	# on CENTRAL, check the received unsubscribe request
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"`
	echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep UNSUBSCRIBE >/dev/null
	RET=$?	
	# expected 0 => assertTrue
	assertTrue 'ERROR: no unsubscribe request received !' ${RET} || return
}

test04AcceptValidationUnSubscribe()
{
	# on CENTRAL, validate the unsubscribe request
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client validate-unsubscription"
	print_debug "${VM_CENTRAL}: validate unsubscribing has been executed."

	sleep 10

	# on CENTRAL, check the list of registered forge for diffusion is empty
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client distribution:list-forge-diffusion"`	
	#False if $list not empty
	assertFalse 'ERROR: There is still a subscribed forge although forge has been unsubscribed.' "[ -n '${list}' ]" || return

	# on CENTRAL, check the received unsubscribe request no more exists
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"`
	echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep UNSUBSCRIBE >/dev/null
	RET=$?
	#expected 1 => assertFalse
	assertFalse 'ERROR: unsubscribe request already existing into requests list !!' ${RET} || return
}

test05RefuseValidationUnSubscribe()
{
	# on LOCAL, subscribe again
	ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client subscribe-forge"
	print_debug "subscribing again from ${VM_LOCAL} has bean executed."

	# on CENTRAL, check the received subscribe request
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`
	echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep SUBSCRIBE >/dev/null
	RET=$?	
	# expected 0 => assertTrue
	assertTrue 'ERROR: no subscribe request received !' ${RET} || return

	# on CENTRAL, refuse subsription
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client refuse-subscription"
	print_debug "${VM_CENTRAL}: refuse subscribing has been executed."

	sleep 10

	# on CENTRAL, check the received subscribe request no more exists
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"`
	echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep SUBSCRIBE >/dev/null
	RET=$?	
	#expected 1 => assetFalse
	assertFalse 'ERROR: subscribe request already existing into requests list' ${RET} || return

	# on CENTRAL, check the list of registered forge for diffusion is empty
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client distribution:list-forge-diffusion"`	
	#False if $list not empty
	assertFalse 'Error: There is still a subscribed forge although forge has been unsubscribed.' "[ -n '${list}' ]" || return
}

oneTimeSetUp()
{
	echo ""
	echo "****************************************************"
	echo "Launching subscription tests suite for distribution"
	echo "***************************************************"
	print_debug "oneTimeSetUp"
	print_debug "****************************************************"

	## check there's no zonal/local forge for diffusion on central
	listi=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client distribution:list-forge-diffusion"`
	if [ -n "$listi" ]; then		 
		  echo "Either the features tests distribution have not been installed correctly or there's already a subscribed forge. Remove it before launching test. Exiting..."
		  exit 1
	fi

	## on the CENTRAL check no received request for subscribe and unsubscribe is pending before starting the test
	## use ssh option to get permanently the DSA key to the list of known hosts.
	#echo"*************"
	#echo $VM_IP_CENTRAL
	#echo "*************"
	list=`ssh -o "StrictHostKeyChecking no" root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`		
	echo ${list}|grep "No SUBSCRIBE request found" >/dev/null
	RET=$?
		if [ ${RET} -ne 0 ]
		then
		    echo "ERROR into oneTimeSetUp(): subscribe request already exists. remove it before launching the test !!!!!!!"
		    echo ""
		    exit ${RET}
		fi	

	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"`
	echo ${list}|grep "No UNSUBSCRIBE request found" >/dev/null
	RET=$?
		if [ ${RET} -ne 0 ]
		then
		    echo "ERROR into oneTimeSetUp(): unsubscribe request already exists. remove it before launching the test !!!!!!!"
		    echo ""
		    exit ${RET}
		fi


	# on the LOCAL, execute dummy check to get permanently the DSA key to the list of known hosts.
	ssh -o "StrictHostKeyChecking no" root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe" >/dev/null
}

oneTimeTearDown()
{
	print_debug "oneTimeTearDown"
	print_debug "****************************************************"
	#TODO: force setting back to initial state: no request and no subscribed forge.
	#test if requests exists onto CENTRAL and if yes refuse the requests ([un]subscription)
	#test if subscribed forges exists and if yes request and validate un-subscription.
}

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

# load shunit2
. ${AUTO_TEST_PATH}/../src/shunit2


