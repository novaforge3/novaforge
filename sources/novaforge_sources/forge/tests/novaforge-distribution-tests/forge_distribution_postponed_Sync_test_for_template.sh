#!/bin/bash
#set -x

###################################################
#    distribution : tests for postponed synchronization 
#                    (with alfresco application)
#    Author: Marc Blachon 31/03/2014
#           
###################################################




test01PostponedSynchronizationByPropagatingTemplate()
{
	echo "****************************************************"
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client add-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project has been created" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template is not created.' ${RET} || return

	## check template is available
	#template with ID: template_itests_project is available.
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been found on the central.' ${RET} || return
	sleep 5

	###### postponed synchronization on CENTRAL: will calculate date with delay = 60 + 10 sec compared to the current date
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-postponed-ref-project"
	sleep 70
	echo "70 sec elapsed"

	###### check on LOCAL the created template has been propagated
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been propagated onto the local.' ${RET} || return
}


oneTimeSetUp()
{
	echo "------------------------------------------------"
	echo "start postponed synchronization tests"
	echo "------------------------------------------------"
	print_debug "oneTimeSetUp executed"
	print_debug "****************************************************"
	execute_subscribe
}

oneTimeTearDown()
{
	print_debug "oneTimeTearDown executed"
	print_debug "****************************************************"

	############################# delete template on centraland local	
	## delete the template on central
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client delete-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project has been deleted" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: when deleting the template on central.' ${RET} || return

	## delete the template on local
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client delete-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project has been deleted" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: when deleting the template on local.' ${RET} || return
		

	#un-subscribe
	execute_unsubscribe
}

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

# load shunit2
. ${AUTO_TEST_PATH}/../src/shunit2
