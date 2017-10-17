#!/bin/bash
#set -x

###################################################
#    distribution : tests for postponed synchronization 
#                    (with alfresco application)
#    Author: Marc Blachon 31/03/2014
#           
###################################################




test01PostponedSynchronizationByPropagatingAlfrescoFolder()
{
	echo "****************************************************"
	## adding alfresco folder
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client add-folder-ged-ref"`
	echo ${list}|grep "Alfresco Folder created" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: could not create Alfresco folder.' ${RET} || return
	sleep 5

	###### postponed synchronization on CENTRAL: will calculate date with delay = 60 + 10 sec compared to the current date
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-postponed-ref-project"
	sleep 70
	echo "70 sec elapsed"

	###### check on LOCAL the created folder under Alfresco
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-folder-ged-ref"`
	echo ${list}|grep "Alfresco folder: alfresco_test_folder" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Alfresco folder not been propagated on LOCAL by the postponed synchronization.' ${RET} || return

}


oneTimeSetUp()
{
	echo "------------------------------------------------"
	echo "start postponed synchronization tests"
	echo "------------------------------------------------"
	print_debug "oneTimeSetUp executed"
	print_debug "****************************************************"
	execute_subscribe >/dev/null
}

oneTimeTearDown()
{
	print_debug "oneTimeTearDown executed"
	print_debug "****************************************************"
	#delete Alfresco folder on Central and local
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client delete-folder-ged-ref" >/dev/null
	ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client delete-folder-ged-ref" >/dev/null		

	#un-subscribe
	execute_unsubscribe >/dev/null
}

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

# load shunit2
. ${AUTO_TEST_PATH}/../src/shunit2
