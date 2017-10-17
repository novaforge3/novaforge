#!/bin/bash
#set -x

###################################################
#    distribution : tests for profile reporting 
#                   
#    Author: Marc Blachon 
#
#    initial date: 10/12/2014
#           
###################################################
test00_check_files_generated_with_karaf_command_exist()
{
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/reporting_forge.txt ]"
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/reporting_organization.txt ]"
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/reporting_profile.txt ]"
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/check_project_central.txt ]"
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/check_account_central.txt ]"
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/check_project_local.txt ]"
assertTrue 'ERROR: a file does not exist!' "[ -r ${DIR_LOG}/${DATE}/check_account_local.txt ]"	
}

###########################################################################################
# on central
# get for each project the list of all roles:nbusers
# compare within assert if this list is the same with check file and with the profile file
###########################################################################################
test01_profile_role_and_users_nb_for_each_project_of_central()
{	
get_check_project_name_list ${DIR_LOG}/${DATE}/check_project_central.txt > ${DIR_LOG}/${DATE}/tmp1.txt
while read line 
do
   #### with profile distribution api
   roles_users_reporting=`get_sorted_role_users_nb_from_profile_reporting_file "$VM_CENTRAL" "$line"`    
  
   #### with check cmd
   roles_users_check=`get_sorted_role_users_nb_from_check_project "$line" "${DIR_LOG}/${DATE}/check_project_central.txt"` 
      
   # shunit test
   assertEquals "ERROR: roles:list of role:users_nb are not the same for project=$line on CENTRAL=$VM_CENTRAL." "${roles_users_reporting}" "${roles_users_check}" || return

done < ${DIR_LOG}/${DATE}/tmp1.txt
}

###########################################################################################
# on local
# for each valid project (found with check cmd), get the list of all roles:nbusers
# compare this list with one got by reporting cmd.
###########################################################################################
test02_profile_role_and_users_nb_for_each_project_of_local()
{
get_check_project_name_list ${DIR_LOG}/${DATE}/check_project_local.txt > ${DIR_LOG}/${DATE}/tmp1.txt
while read line 
do
   #### with profile distribution api
   roles_users_reporting=`get_sorted_role_users_nb_from_profile_reporting_file "$VM_LOCAL" "$line"`    
  
   #### with check cmd
   roles_users_check=`get_sorted_role_users_nb_from_check_project "$line" "${DIR_LOG}/${DATE}/check_project_local.txt"` 
      
   # shunit test
   assertEquals "ERROR: roles:list of <role:users_nb> are not the same for project=$line on LOCAL=$VM_LOCAL." "${roles_users_reporting}" "${roles_users_check}" || return

done < ${DIR_LOG}/${DATE}/tmp1.txt
}

test03_profile_project_names_list_for_central()
{
#extract data to be compared.
get_reporting_project_name_list "$VM_CENTRAL" > ${DIR_LOG}/${DATE}/tmp_projects_central_reporting.txt
get_check_project_name_list ${DIR_LOG}/${DATE}/check_project_central.txt > ${DIR_LOG}/${DATE}/tmp_projects_central_check.txt

#format data
sorted_projects_central_reporting=`cat ${DIR_LOG}/${DATE}/tmp_projects_central_reporting.txt|sort -n` 
sorted_projects_central_check=`cat ${DIR_LOG}/${DATE}/tmp_projects_central_check.txt|sort -n`

#shunit test
assertEquals "ERROR: list of projects are not the same on CENTRAL=$VM_CENTRAL." "${sorted_projects_central_reporting}" "${sorted_projects_central_check}" || return
}

test04_profile_project_names_list_for_local()
{
#extract data to be compared.
get_reporting_project_name_list "$VM_LOCAL" > ${DIR_LOG}/${DATE}/tmp_projects_local_reporting.txt
get_check_project_name_list ${DIR_LOG}/${DATE}/check_project_local.txt > ${DIR_LOG}/${DATE}/tmp_projects_local_check.txt

#format data
sorted_projects_local_reporting=`cat ${DIR_LOG}/${DATE}/tmp_projects_local_reporting.txt|sort -n` 
sorted_projects_local_check=`cat ${DIR_LOG}/${DATE}/tmp_projects_local_check.txt|sort -n`

#shunit test
assertEquals "ERROR: list of projects are not the same on CENTRAL=$VM_CENTRAL." "${sorted_projects_local_reporting}" "${sorted_projects_local_check}" || return
}

test05_forge_projects_total_for_central()
{
#format data
get_check_project_name_list "${DIR_LOG}/${DATE}/check_project_central.txt" > ${DIR_LOG}/${DATE}/tmp_projects_central_check.txt
projects_check_total=`cat ${DIR_LOG}/${DATE}/tmp_projects_central_check.txt | wc -l`
projects_reporting_total=`get_projects_reporting_total_per_vm "$VM_CENTRAL"`

#shunit test
assertEquals "ERROR: total of projects are not the same on CENTRAL=$VM_CENTRAL." ${projects_check_total} ${projects_reporting_total}
}

test06_forge_projects_total_for_local()
{
#format data
get_check_project_name_list "${DIR_LOG}/${DATE}/check_project_local.txt" > ${DIR_LOG}/${DATE}/tmp_projects_local_check.txt
projects_check_total=`cat ${DIR_LOG}/${DATE}/tmp_projects_local_check.txt | wc -l`
projects_reporting_total=`get_projects_reporting_total_per_vm "$VM_LOCAL"`

#shunit test
assertEquals "ERROR: total of projects are not the same on LOCAL=$VM_LOCAL." ${projects_check_total} ${projects_reporting_total}
}

test07_forge_accounts_nb_for_central()
{
account_check_total=`cat "${DIR_LOG}/${DATE}/check_account_central.txt"`
projects_reporting_total=`get_accounts_reporting_total_per_vm "$VM_CENTRAL"`

#shunit test
assertEquals "ERROR: total of accounts are not the same on CENTRAL=$VM_CENTRAL." ${account_check_total} ${projects_reporting_total}
}

test08_forge_accounts_nb_for_local()
{
#format data
account_check_total=`cat "${DIR_LOG}/${DATE}/check_account_local.txt"`
projects_reporting_total=`get_accounts_reporting_total_per_vm "$VM_LOCAL"`

#shunit test
assertEquals "ERROR: total of accounts are not the same on LOCAL=$VM_CENTRAL." ${account_check_total} ${projects_reporting_total}
}

test09_forge_update_date_for_central()
{
#format data
date_check_formated=`get_formated_date_from_check`

date_reporting=`get_date_reporting_per_vm "${VM_CENTRAL}"`
date_reporting_formated=`get_formated_date_from_reporting "${date_reporting}"`

#shunit test
assertEquals "ERROR: date is not the same on CENTRAL=$VM_CENTRAL." "${date_check_formated}" "${date_reporting_formated}"
}

test10_forge_update_date_for_local()
{
date_check_formated=`get_formated_date_from_check`

date_reporting=`get_date_reporting_per_vm "${VM_LOCAL}"`
date_reporting_formated=`get_formated_date_from_reporting "${date_reporting}"`

#shunit test
assertEquals "ERROR: date is not the same on LOCAL=$VM_LOCAL." "${date_check_formated}" "${date_reporting_formated}"
}

test11_organization_org_projects_nb_for_central_and_local()
{
#get ORG on CENTRAL from check
get_check_org_list ${DIR_LOG}/${DATE}/check_project_central.txt > ${DIR_LOG}/${DATE}/tmp_org_central_check.txt

#get ORG on LOCAL from check
get_check_org_list ${DIR_LOG}/${DATE}/check_project_local.txt > ${DIR_LOG}/${DATE}/tmp_org_local_check.txt

#append/merge from check

#append/merge and sort from check
cat ${DIR_LOG}/${DATE}/tmp_org_central_check.txt ${DIR_LOG}/${DATE}/tmp_org_local_check.txt > ${DIR_LOG}/${DATE}/tmp_org_check_initial.txt

get_check_org_merged ${DIR_LOG}/${DATE}/tmp_org_check_initial.txt > ${DIR_LOG}/${DATE}/tmp_org_check_merged.txt
org_check_sorted=`cat ${DIR_LOG}/${DATE}/tmp_org_check_merged.txt | sort -n`

#get ORG from reporting, format and sort
org_reporting_sorted=`cat ${DIR_LOG}/${DATE}/reporting_organization.txt | sort -n`

#shunit test
assertEquals "ERROR: ORG are not the same on LOCAL=${VM_LOCAL} or either CENTRAL=$VM_CENTRAL." "${org_check_sorted}" "${org_reporting_sorted}"
}

oneTimeSetUp()
{
	#DIR_LOG="/datas/users/mb/LogReportingDistribution"
	mkdir -p ${DIR_LOG}
	DATE=`date +"%m-%d-%H-%M"`
	mkdir -p ${DIR_LOG}/${DATE}

	echo "------------------------------------------------"
	echo "start distribution reporting tests"
	echo "------------------------------------------------"
	print_debug "oneTimeSetUp executed"
	print_debug "****************************************************"

	#######TODO: remove comment if set for dev tests
	execute_subscribe

	## make immediate synchronization
	#record system date
	DateBefore=`date -R`
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client sync-immediate-report-extraction"
	#record system date
	DateAfter=`date -R`
	#TODO: to get the test intervall the validate date ??
	
	
	## wait for propagation
	sleep 10
	
	#### record all the reporting from "forge", "organization", "profile" tab.	
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client reporting-forge list" > ${DIR_LOG}/${DATE}/reporting_forge.txt
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client reporting-organization list" > ${DIR_LOG}/${DATE}/reporting_organization.txt
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client reporting-profile list" > ${DIR_LOG}/${DATE}/reporting_profile.txt

	#### check and record from central
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client reporting-check-project" > ${DIR_LOG}/${DATE}/check_project_central.txt
	ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client reporting-check-account" > ${DIR_LOG}/${DATE}/check_account_central.txt

	#### check and record from local
	ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client reporting-check-project" > ${DIR_LOG}/${DATE}/check_project_local.txt
	ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client reporting-check-account" > ${DIR_LOG}/${DATE}/check_account_local.txt		
}

oneTimeTearDown()
{
	print_debug "oneTimeTearDown executed"
	print_debug "****************************************************"
	#######TODO: remove comment if set for dev tests
	execute_unsubscribe

	## remove all temporally files.
	#rm -f ${DIR_LOG}/${DATE}/tmp_*
	#rm -rf ${DIR_LOG}/${DATE}
}




#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

# load shunit2
. ${AUTO_TEST_PATH}/../src/shunit2
