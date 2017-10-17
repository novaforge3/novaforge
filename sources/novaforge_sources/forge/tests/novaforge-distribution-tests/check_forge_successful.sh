#!/bin/bash
#set -x

#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

###################################################
#     For Central and Local Forge, sequentially
#    - check at least processes: mysql, apache, alfresco, karaf are running
#      otherwise exit
#    - check access to portal page is successful
#      otherwise stop and restart karaf (dependending on properties file)
#
#     Author: Marc Blachon 31/03/2014
###################################################

#--------------------------------------------------------
#   check portal response (expected http response is 200)
#--------------------------------------------------------
function check_public_portal_page () {
	VM_IP=$1
	VM_NAME=$2
	response=`wget --server-response -t 5 --timeout 30 --no-check-certificate https://${VM_IP}/portal/public 2>&1|awk '/^  HTTP/{print $2}'`
	print_debug "response wget = $response"	
	if [[ -z ${response} ]];
         	then
		 print_debug "response wget vide !!!"
		 return 1
		else
		if [ ${response} -eq 200 ]
		 	then
			  print_debug "page trouvée sur la VM: $VM_IP ($VM_NAME)"
			  efface_public_page
			  return 0
			else
			  print_debug "ERROR Acces portail: Le code retour du portail sur la VM: $VM_IP ($VM_NAME) est: $response"
			  efface_public_page
			  return 1
		fi
	fi

		
}

function efface_public_page () {
if [ -f public ] 
		then
		 rm -f public
	fi
}

#--------------------------------------------------------
#   check is karaf is running
#--------------------------------------------------------
check_process_is_running () {
	VM_IP=$1
	PROC=$2
	res=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "ps aux | grep $PROC | fgrep -v grep"`
	echo ${res} | grep ${PROC} > /dev/null
	response=$?
	if [ ${response} -ne 0 ]
         	then
		 print_debug "$PROC is not running"
		 return 1	
	else 
		return 0
	fi
}

#--------------------------------------------------------
#   stop karaf and remove logs
#--------------------------------------------------------
function stop_karaf_remove_log () {
 	VM_IP=$1
	ssh -o "StrictHostKeyChecking no" root@${VM_IP} "pkill -9 -f karaf"
	sleep 2
	res=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "ps aux | grep karaf | fgrep -v grep"`
	echo ${res} | grep karaf > /dev/null
	response=$?
	if [ ${response} -ne 0 ]; then
		 print_debug "karaf has been stopped"
		 ssh root@${VM_IP} "rm -f /datas/safran/logs/tech/karaf/karaf.log"
		 # check log has been removed.		 
		 if ssh root@${VM_IP} "stat /datas/safran/logs/tech/karaf/karaf.log" \> /dev/null; then
		 	 print_debug "Error when removing karaf log! file log is still existing....."
		 else 
			print_debug "karaf log has been removed"
		 fi

		# Clear Karaf caches
		print_debug "removing karaf caches ......"
		ssh root@${VM_IP} "rm -rf /datas/safran/datas/karaf/*"
		# removing datasources
		print_debug "removing datasources files ....."
		ssh root@${VM_IP} "rm -f /datas/safran/engines/karaf/deploy/datasource.*"
		
	else 
		print_debug "Pb. to kill karaf"
	fi
}

#--------------------------------------------------------
#   start karaf
#--------------------------------------------------------
function start_karaf () {
	VM_IP=$1
	ssh -o "StrictHostKeyChecking no" root@${VM_IP} "cd /datas/safran; ./bin/karaf start clean debug"
	print_debug "starting karaf ............."	
}




#--------------------------------------------------------
#   stop karaf and remove logs
#--------------------------------------------------------
function check_and_loop () {
VM_IP=$1
VM_NAME=$2


x=1
while [ ${x} -le ${RETRY} ]
do
  x=$(( $x + 1 ))
  check_public_portal_page ${VM_IP} ${VM_NAME}
  res_check=$?
  if [ ${res_check} -ne 0 ]
         then
		print_debug "Le portail est KO !!! sur $VM_NAME"
		stop_karaf_remove_log ${VM_IP}
  		check_process_is_running ${VM_IP} "karaf"
  		response=$?
  		if [ ${response} -ne 0 ]
         	then
			start_karaf ${VM_IP}
			print_debug "attente $FORGE_START_TIME sec. pour le demmarage complet de la forge "
			sleep ${FORGE_START_TIME}
			#TODO: add check for active bundle ??
  		fi
  else
	print_debug "Le portail est OK !!!"
	break
	return 0
  fi

  
done

check_public_portal_page ${VM_IP} ${VM_NAME}
res_check=$?
if [ ${res_check} -ne 0 ]
      then
	print_debug "Comme la réponse du portail est toujours KO apres $RETRY redemarrages, arret des tentatives!"
	return 1
fi
}

#-------------------------------------------
#     MAIN
#-------------------------------------------
#check processes on CENTRAL: mysql, apache, alfresco, karaf
check_process_is_running ${VM_IP_CENTRAL} "mysql"
mysql=$?
check_process_is_running ${VM_IP_CENTRAL} "apache"
apache=$?
check_process_is_running ${VM_IP_CENTRAL} "alfresco"
alfresco=$?
check_process_is_running ${VM_IP_CENTRAL} "karaf"
karaf=$?

if [[ ${mysql} -eq 0 ]] && [[ ${apache} -eq 0 ]];
	then
		if [[ ${alfresco} -ne 0 ]] || [[ ${karaf} -ne 0 ]];
		  then
	  		print_debug "Problem: a processes is not running"
			exit 1
		fi
	else
		print_debug "Problem: a processes is not running"
		exit 1
fi

#check processes on LOCAL: apache, alfresco, karaf
check_process_is_running ${VM_IP_CENTRAL} "mysql"
mysql=$?
check_process_is_running ${VM_IP_LOCAL} "apache"
apache=$?
# not launched on LOCAL !!
#check_process_is_running ${VM_IP_LOCAL} "alfresco"
alfresco=0
check_process_is_running ${VM_IP_LOCAL} "karaf"
karaf=$?

if [[ ${mysql} -eq 0 ]] && [[ ${apache} -eq 0 ]];
	then
		if [[ ${alfresco} -ne 0 ]] || [[ ${karaf} -ne 0 ]];
		  then
	  		print_debug "Problem: a processes is not running"
			exit 1
		fi
	else
		print_debug "Problem: a processes is not running"
		exit 1
fi

echo ""
print_debug "************************************"
print_debug "checking portal on CENTRAL and ZONAL"
print_debug "************************************"
#check on CENTRAL
check_and_loop ${VM_IP_CENTRAL} ${VM_CENTRAL}
ret=$?
if [ ${ret} -ne 0 ];
         then
		exit 1
fi

#check on LOCAL
check_and_loop ${VM_IP_LOCAL} ${VM_LOCAL}
ret=$?
if [ ${ret} -ne 0 ];
         then
		exit 1
fi
#return code 0 if OK
exit 0




