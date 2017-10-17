#!/bin/bash
#set -x

###################################################
#    utility
#    Author: Marc Blachon 10/09/2014
###################################################


print_debug () {
	if [ "$PRINT_DEBUG" = "yes" ]; then
	echo $1
	fi
}


#----------------------------------
#    subscribewait
#----------------------------------
execute_subscribe () {

## on the CENTRAL check no received request for subscribe and unsubscribe is pending before starting the test
## use ssh option to get permanently the DSA key to the list of known hosts.
list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`
echo ${list}|grep "No SUBSCRIBE request found" >/dev/null
RET=$?
        if [ ${RET} -ne 0 ]
        then            
            echo "ERROR: subscribe request already exists. remove it before starting the test !!!!!!!"
            echo ""
            exit ${RET}
        fi

list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"`
echo ${list}|grep "No UNSUBSCRIBE request found" >/dev/null
RET=$?
        if [ ${RET} -ne 0 ]
        then            
            echo "ERROR: unsubscribe request already exists. remove it before starting the test !!!!!!!"
            echo ""
            exit ${RET}
        fi


# on the LOCAL, execute dummy check to get permanently the DSA key to the list of known hosts.
ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe" >/dev/null


# on the LOCAL, subscribing 
ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client subscribe-forge"
print_debug "${VM_LOCAL}: subscribing has bean executed."

sleep 2

# on CENTRAL, check-received-request for subscribing 
list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`
echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep SUBSCRIBE >/dev/null
RET=$?
        if [ ${RET} -ne 0 ]
         then                       
            echo "${VM_CENTRAL}: ERROR: no subscribe request received !"
            echo ""
            exit ${RET}
         else            
            print_debug "${VM_CENTRAL}: subscribe request has been received."  
	    echo ""        
        fi


# on CENTRAL, validate subscribing
ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client validate-subscription"
print_debug "${VM_CENTRAL}: validate subscribing has been executed."
sleep 2

# on CENTRAL, check no more subscribe request into request list
list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request subscribe"`
echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep SUBSCRIBE >/dev/null
RET=$?
        if [ ${RET} -ne 0 ]
         then            
            print_debug "${VM_CENTRAL}: no more subscribe request into request list."                       
            echo ""                
         else            
            echo "${VM_CENTRAL}: ERROR: subscribe request already existing into requests list !!"
            exit ${RET}
        fi

# on CENTRAL, list forge registered for diffusion
#TODO
  
} #end execute subscribe

#----------------------------------
#    unsubscribe
#----------------------------------

execute_unsubscribe () {
# on LOCAL unsubscribe
ssh root@${VM_IP_LOCAL} "/datas/safran/engines/karaf/bin/client unsubscribe-forge" >/dev/null
print_debug "${VM_LOCAL}: unsubscribing has bean executed."

sleep 2

# on CENTRAL, check the received unsubscribe request
list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"`
echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep UNSUBSCRIBE >/dev/null
RET=$?
        if [ ${RET} -ne 0 ]
         then                   
            echo "${VM_CENTRAL}: ERROR: no unsubscribe request received !"
            echo ""
            exit ${RET}
         else         
            print_debug "${VM_CENTRAL}: unsubscribe request has been received."    
	    echo ""        
        fi

# on CENTRAL, validate the unsubscribe request
ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client validate-unsubscription"
print_debug "${VM_CENTRAL}: validate unsubscribing has been executed."

sleep 2

# on CENTRAL, check the list of registered forge for diffusion is empty
#TODO

# on CENTRAL, check the received unsubscribe request no more exists
list=`ssh root@${VM_IP_CENTRAL} "/datas/safran/engines/karaf/bin/client check-received-request unsubscribe"` >/dev/null
echo ${list}|grep ${VM_LOCAL}|grep ${VM_CENTRAL}|grep UNSUBSCRIBE >/dev/null
RET=$?
        if [ ${RET} -ne 0 ]
         then           
            print_debug "${VM_CENTRAL}: no more unsubscribe request into request list."                       
            echo ""                   
         else
           
            echo "${VM_CENTRAL}: ERROR: unsubscribe request already existing into requests list !!"
 	    echo ""
            exit 1
        fi

## check there is target forge  
## otherwise  the subscription
# TODO with list-forge-diffusion cmd
}



wait_propagation () {
##### 
echo "Waiting propagation ........"
for i in {1..10}; do 
  printf '\r%2d' ${i}
  sleep 1
done
printf '\n'
}

########################################
#  utility functions for reporting tests
########################################
#---------------------------------------------------------------------
# get_check_project_name_list()
#---------------------------------------------------------------------
get_check_project_name_list()
{
FILE_PATH="$1"
while read line 
do
	projectName=`echo "$line" | cut -d '|' -f 2`
	echo "$projectName"
done < ${FILE_PATH}
#echo $list | rev | cut -c 2- | rev 
}

#---------------------------------------------------------------------
# get_reporting_project_name_list()
#---------------------------------------------------------------------
get_reporting_project_name_list()
{
## param1 = <ip name of the forge>
VM_NAME="$1"

# getting not sorted string of roles:users nb 
awk -F "|" '{		
        if ( $1 != VM )  {next} 
        else
	  {	
		print $2				
	  }
        }' VM="$VM_NAME" ${DIR_LOG}/${DATE}/reporting_profile.txt
}

#---------------------------------------------------------------------
# get_sorted_role_users_nb_from_profile_reporting_file()
#---------------------------------------------------------------------
get_sorted_role_users_nb_from_profile_reporting_file()
{
## param1 = <ip name of the forge>
## param2 = <project name> 
VM_NAME="$1"
PROJECT="$2"

# getting not sorted string of roles:users nb 
not_sorted_roles=`awk -F "|" '{		
        if ( $1 != VM )  {next} 
        else
	  {
	    if ( $2 == PROJ )
		{		
		print $3;		
		}
	  }
        }' VM="$VM_NAME" PROJ="$PROJECT" ${DIR_LOG}/${DATE}/reporting_profile.txt`

sorted_roles=`sort_string "${not_sorted_roles}"`
echo "${sorted_roles}"
  }

#---------------------------------------------------------------------
# get_sorted_role_users_nb_from_check_project()
#---------------------------------------------------------------------
get_sorted_role_users_nb_from_check_project()
{
PROJECT="$1"
FILE_INPUT="$2"

# getting not sorted string of roles:users nb 
not_sorted_roles=`awk -F "|" '{		        
if ( $2 == PROJ )
	{		
	print $4;		
	}	  
}' PROJ="${PROJECT}" ${FILE_INPUT}`

## sorting roles (Administrator:5 testeur:3 developpeur:4)
sorted_roles=`sort_string "${not_sorted_roles}"`
echo "${sorted_roles}"
}

#---------------------------------------------------------------------
# sort_string()
#---------------------------------------------------------------------
sort_string()
{
#INSTR="Administrator:5 testeur:3 developpeur:4"
INSTR=$1
arr=$(echo ${INSTR})
for x in ${arr}
do
    echo "$x"
done > /tmp/dummy.txt

OUTSTR=`cat /tmp/dummy.txt|sort -n`
echo ${OUTSTR}
}

#---------------------------------------------------------------------
# get_projects_reporting_total_per_vm()
#---------------------------------------------------------------------
get_projects_reporting_total_per_vm()
{
## param1 = <ip name of the forge>
VM_NAME="$1"

# getting not sorted string of roles:users nb 
awk -F "|" '{		
  if ( $1 != VM )  {next} 
     else
          {
		print $2;				
	  }
}' VM="$VM_NAME" ${DIR_LOG}/${DATE}/reporting_forge.txt
}

#---------------------------------------------------------------------
# get_accounts_reporting_total_per_vm()
#---------------------------------------------------------------------
get_accounts_reporting_total_per_vm()
{
## param1 = <ip name of the forge>
VM_NAME="$1"

# getting not sorted string of roles:users nb 
awk -F "|" '{		
  if ( $1 != VM )  {next} 
     else
          {
		print $3;				
	  }
}' VM="$VM_NAME" ${DIR_LOG}/${DATE}/reporting_forge.txt
}

get_formated_date_from_check()
{
#initial check date format:          Thu, 11 Dec 2014 16:12:57 +0100

#get 1st field and remove last character
check_day_=`echo ${DateBefore} | cut -d ' ' -f 1`
check_day=`echo "${check_day_}" | rev | cut -c 2- | rev`

check_month=`echo ${DateBefore} | cut -d ' ' -f 3`

check_day_number=`echo ${DateBefore} | cut -d ' ' -f 2`

check_HMS=`echo ${DateBefore} | cut -d ' ' -f 5`
check_H=`echo ${check_HMS} | cut -d ':' -f 1`

check_formated_date="$check_day $check_month $check_day_number $check_H"
echo "${check_formated_date}"
}

get_formated_date_from_reporting()
{
#expected parameter (Ex. Thu Dec 11 11:08:16 CET 2014)
Date_reporting=$1

reporting_day=`echo ${Date_reporting} | cut -d ' ' -f 1`

reporting_month=`echo ${Date_reporting} | cut -d ' ' -f 2`

reporting_day_number=`echo ${Date_reporting} | cut -d ' ' -f 3`

reporting_HMS=`echo ${Date_reporting} | cut -d ' ' -f 4`
reporting_H=`echo ${reporting_HMS} | cut -d ':' -f 1`

reporting_formated_date="$reporting_day $reporting_month $reporting_day_number $reporting_H"
echo "${reporting_formated_date}"
}

#---------------------------------------------------------------------
# get_date_reporting_per_vm()
#---------------------------------------------------------------------
get_date_reporting_per_vm()
{
## param1 = <ip name of the forge>
VM_NAME="$1"

# getting not sorted string of roles:users nb 
awk -F "|" '{		
  if ( $1 != VM )  {next} 
     else
          {
		print $4;				
	  }
}' VM="$VM_NAME" ${DIR_LOG}/${DATE}/reporting_forge.txt
}

#---------------------------------------------------------------------
# get_check_org_list()
#---------------------------------------------------------------------
get_check_org_list()
{
FILE_PATH="$1"
while read line 
do
	org=`echo "$line" | cut -d '|' -f 3`
	echo "${org}|1"
done < ${FILE_PATH}
}

#---------------------------------------------------------------------
# get_check_org_merged()
#---------------------------------------------------------------------
get_check_org_merged()
{
FILE_PATH="$1"
#keep only 1 lines if several ones are identical > UNIQUE_LINE_FILE
UNIQUE_LINE_FILE=${DIR_LOG}/${DATE}/tmp_org_check_single_line.txt
awk '!a[$0]++' ${FILE_PATH} > ${UNIQUE_LINE_FILE}

OUTPUT_FILE=${DIR_LOG}/${DATE}/tmp_org_check_merged.txt
while read line 
do
	org=`echo "$line" | cut -d '|' -f 1`
	#pb if ${org} = ""
	nb=`cat ${FILE_PATH} | grep -x "${org}|1" | wc -l`
	echo "${org}|$nb" >> ${OUTPUT_FILE}
done < ${UNIQUE_LINE_FILE}
}



