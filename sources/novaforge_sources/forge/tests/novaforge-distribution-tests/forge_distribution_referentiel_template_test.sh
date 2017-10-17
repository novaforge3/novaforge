#!/bin/bash
#set -x

###################################################
#    distribution : tests for template into
#                   referentiel project
#    Author: Marc Blachon 31/03/2014
#
#    initial date: 29/10/14
#           
###################################################

test01AddTemplateIntoReferentielOnCentral()
{
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
}

test02CreateProjectWithCreatedTemplateOnCentral()
{
	## check template is available
	#template with ID: template_itests_project is available.
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been found on the central.' ${RET} || return

	## create project with the template
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client create-project-template-ref"`
	echo ${list}|grep "project with ID: distributionproject" | grep "has been created with template ID: template_itests_project" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: project not created (with ref template).' ${RET} || return
}


test03PropagateTemplateCreation()
{
	## check template is available
	#template with ID: template_itests_project is available.
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been found on the central.' ${RET} || return

	### propagate template
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-templates"
	sleep 10

	###### check on LOCAL the template propagation
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been propagated onto the local.' ${RET} || return

	## added 4 mars 2015
	## TO BE TESTED !!!!
	## create project with the template
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client create-project-template-ref"`
	echo ${list}|grep "project with ID: distributionproject" | grep "has been created with template ID: template_itests_project" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: project not created (with ref template).' ${RET} || return 	
}

test04DeleteTemplateOnCentral()
{	
	## check template is available
	#template with ID: template_itests_project is available.
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been found on the central.' ${RET} || return

	## delete the template
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client delete-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project has been deleted" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template is not created.' ${RET} || return

	## check template is not available
	#template with ID: template_itests_project is available.
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 1 => assertFalse
	assertFalse 'ERROR: template has not been deleted' ${RET} || return
}

test05PropagateTemplateDeletion()
{
	### check template exists on the local	
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 0 => assertTrue
	assertTrue 'ERROR: template has not been found on the local.' ${RET} || return

	### check template does not exist on the central
	list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 1 => assertFalse
	assertFalse 'ERROR: template exists on the central' ${RET} || return

	### propagate the deletion
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-templates"
	sleep 10

	### check the template does no more exist on the local
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client fetch-template-ref-project"`
	echo ${list}|grep "template with ID: template_itests_project is available" >/dev/null
	RET=$?
	#expected 1 => assertFalse
	assertFalse 'ERROR: template has not been deleted' ${RET} || return
}

oneTimeSetUp()
{
	echo "------------------------------------------------"
	echo "start distribution templates tests"
	echo "------------------------------------------------"
	print_debug "oneTimeSetUp executed"
	print_debug "****************************************************"
	execute_subscribe
}

oneTimeTearDown()
{
	print_debug "oneTimeTearDown executed"
	print_debug "****************************************************"
	## force delete of the template on local
	list=`ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client delete-template-ref-project"` >/dev/null
	execute_unsubscribe
}

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

# load shunit2
. ${AUTO_TEST_PATH}/../src/shunit2
