#!/bin/bash
#set -x

###################################################
#    distribution : tests for dokuwiki into
#                   referentiel project
#
#    modification 10/09/14: integrate shunit2                  
#    Author: Marc Blachon 31/03/2014
###################################################


test01AddPageWiki()
{
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client add-wiki-page-ref"`
	echo ${list}|grep "Dokuwiki page: distribution-test-pagecreated" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Wiki page not created.' ${RET} || return
}

test02SynchronizationAddPageWiki()
{
	sleep 1	
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-ref-project"

	sleep 5

	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-wiki-page-ref"`
	echo ${list}|grep "Dokuwiki page: distribution-test-pagecreated" >/dev/null
	RET=$?
	# expected 0 => assertTrue
	assertTrue 'ERROR: Wiki page not propagated on LOCAL.' ${RET} || return
}


test03DeletePageWiki()
{
	## check the page has been created on the central
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-wiki-page-ref"`
	echo ${list}|grep "Dokuwiki page: distribution-test-pagecreated" >/dev/null
	RET=$?
	# expected 0 => assertTrue
	assertTrue 'ERROR: wiki page could no be deleted on CENTRAL because it could not be created!.' ${RET} || return

	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client delete-wiki-page-ref"
	#check
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-wiki-page-ref"`
	echo ${list}|grep "no page found!" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: Wiki page not deleted on CENTRAL.' ${RET} || return
}

test04SynchronizationDeletePageWiki()
{
	## check page exists onto local before synchronization with assert!
	###### check on LOCAL the created page
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-wiki-page-ref"`
	echo ${list}|grep "Dokuwiki page: distribution-test-pagecreated" >/dev/null
	RET=$?
	# expected 0 => assertTrue
	assertTrue 'ERROR: wiki page does not exist on LOCAL before the synchronization!.' ${RET} || return

	sleep 2
	###### synchronize on CENTRAL forge	
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-ref-project"
	sleep 5

	#fetch_for_deleted_wiki_page_on_local
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-wiki-page-ref"`
	echo ${list}|grep "no page found!" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: delete of Wiki page not propagated on LOCAL.' ${RET} || return

}

oneTimeSetUp()
{
	echo "------------------------------------------------"
	echo "start distribution referentiel tests for dokuwiki"
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
