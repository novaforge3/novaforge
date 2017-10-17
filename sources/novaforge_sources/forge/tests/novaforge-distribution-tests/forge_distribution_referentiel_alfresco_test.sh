#!/bin/bash
#set -x

###################################################
#    distribution : tests for alfresco into
#                   referentiel project
#    Author: Marc Blachon 31/03/2014
#
#    modification 10/09/14: integrate shunit2
#           
###################################################


test01AddFolderAlfresco()
{
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client add-folder-ged-ref"`
	echo ${list}|grep "Alfresco Folder created" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Alfresco folder not created.' ${RET} || return
}

test02SynchronizationAddFolder()
{
	sleep 5
	###### synchronize on CENTRAL forge
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-ref-project"

	sleep 5

	###### check on LOCAL the created folder under Alfresco
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-folder-ged-ref"`
	echo ${list}|grep "Alfresco folder: alfresco_test_folder" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Alfresco folder not propagated on LOCAL.' ${RET} || return
}

test03DeleteFolderAlfresco()
{
	# check folder has been created onto the central
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-folder-ged-ref"`
	echo ${list}|grep "Alfresco folder: alfresco_test_folder" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Alfresco folder could no be deleted on CENTRAL because it could not be created!.' ${RET} || return
	
	###### delete alfresco folder on CENTRAL forge
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client delete-folder-ged-ref"`
	### check
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-folder-ged-ref"`
	echo ${list}|grep "Alfresco folder: alfresco_test_folder" >/dev/null
	RET_DELETE_CENTRAL=$?
	#expected 1=> assertFalse
	assertFalse 'ERROR: Alfresco folder net deleted on CENTRAL.' ${RET_DELETE_CENTRAL} || return
}

test04SynchronizationDeleteFolder()
{	
	## check folder exists onto local before synchronization with assert!
	###### check on LOCAL the created folder under Alfresco
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-folder-ged-ref"`
	echo ${list}|grep "Alfresco folder: alfresco_test_folder" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Alfresco folder does not exist on LOCAL before the synchronization!.' ${RET} || return

	sleep 1
	###### synchronize on CENTRAL forge
	print_debug "synchronize referentiel to zonale forge"
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-ref-project"
	sleep 5

	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-folder-ged-ref"`
	echo ${list}|grep "no folder found!" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: delete of Alfresco folder not propagated on LOCAL.' ${RET} || return
}

oneTimeSetUp()
{
	echo "------------------------------------------------"
	echo "start distribution referentiel tests for alfresco"
	echo "------------------------------------------------"
	print_debug "oneTimeSetUp executed"
	print_debug "****************************************************"
	execute_subscribe
}

oneTimeTearDown()
{
	print_debug "oneTimeTearDown executed"
	print_debug "****************************************************"
	execute_unsubscribe
}

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

# load shunit2
. ${AUTO_TEST_PATH}/../src/shunit2
