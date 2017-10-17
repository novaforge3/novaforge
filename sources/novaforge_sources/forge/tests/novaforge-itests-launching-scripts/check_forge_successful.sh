#!/bin/bash
#set -x

###########################################################################
#     For Central and Local Forge, sequentially
#    - check at least processes: mysql, apache, alfresco, karaf are running
#      otherwise exit
#    - check access to portal page is successful
#      otherwise stop and restart karaf (dependending on properties file)
#
#     Author: Marc Blachon 31/03/2014
#
#    - modification 20/06/2014
#       . restrict checks to 1 vm (configured as LOCAL into properties file)
#       . 
##########################################################################

#--------------------------------------------------------
#   check portal response (expected http response is 200)
#--------------------------------------------------------
function check_public_portal_page () {
	VM_IP=$1
	VM_NAME=$2
	response=`wget --server-response -t 5 --timeout 30 --no-check-certificate https://${VM_IP}/portal/public 2>&1|awk '/^  HTTP/{print $2}'`
	echo "response wget = $response"	
	if [[ -z ${response} ]];
         	then
		 echo "response wget vide !!!"
		 return 1
		else
		if [ ${response} -eq 200 ]
		 	then
			  echo "page trouvée sur la VM: $VM_IP ($VM_NAME)"
			  efface_public_page
			  return 0
			else
			  echo "ERROR Acces portail: Le code retour du portail sur la VM: $VM_IP ($VM_NAME) est: $response"
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
#   check karaf is running
#--------------------------------------------------------
check_process_is_running () {
	VM_IP=$1
	PROC=$2
	res=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "ps aux | grep $PROC | fgrep -v grep"`
	echo ${res} | grep ${PROC} > /dev/null
	response=$?
	if [ ${response} -ne 0 ]
         	then
		 echo "$PROC is not running"
		 return 1	
	else 
		echo "$PROC is running on $VM_IP"
		return 0
	fi
}

#--------------------------------------------------------
#   stop karaf (with karaf) and remove logs
#--------------------------------------------------------
function stop_karaf_remove_log () {
 	VM_IP=$1
	ssh -o "StrictHostKeyChecking no" root@${VM_IP} "pkill -9 -f karaf"
	sleep 2
	res=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "ps aux | grep karaf | fgrep -v grep"`
	echo ${res} | grep karaf > /dev/null
	response=$?
	if [ ${response} -ne 0 ]; then
		 echo "karaf has been stopped"
		 ssh root@${VM_IP} "rm -f /datas/${PROFILE_DIR}/logs/tech/karaf/karaf.log"
		 # check log has been removed.		 
		 if ssh root@${VM_IP} "stat /datas/${PROFILE_DIR}/logs/tech/karaf/karaf.log" \> /dev/null; then
		 	 echo "Error when removing karaf log! file log is still existing....."
		 else 
			echo "karaf log has been removed"
		 fi

		# Clear Karaf caches
		echo "removing karaf caches ......"
		ssh root@${VM_IP} "rm -rf /datas/${PROFILE_DIR}/datas/karaf/*"
		# removing datasources
		echo "removing datasources files ....."
		ssh root@${VM_IP} "rm -f /datas/${PROFILE_DIR}/engines/karaf/deploy/datasource.*"
		
	else 
		echo "Pb. to kill karaf"
	fi
}

#--------------------------------------------------------
#   start karaf
#--------------------------------------------------------
function start_karaf () {
	VM_IP=$1
	ssh -o "StrictHostKeyChecking no" root@${VM_IP} "cd /datas/${PROFILE_DIR}; ./bin/karaf start clean"
	echo "starting karaf ............."	
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
		echo "Le portail est KO !!! sur $VM_NAME"
		stop_karaf_remove_log ${VM_IP}
  		check_process_is_running ${VM_IP} "karaf"
  		response=$?
  		if [ ${response} -ne 0 ]
         	then
			start_karaf ${VM_IP}
			echo "attente $FORGE_START_TIME sec. pour le demmarage complet de la forge "
			sleep ${FORGE_START_TIME}
			#TODO: add check for active bundle ??
  		fi
  else
	echo "Le portail est OK !!!"
	break
	return 0
  fi

  
done

check_public_portal_page ${VM_IP} ${VM_NAME}
res_check=$?
if [ ${res_check} -ne 0 ]
      then
	echo "Comme la réponse du portail est toujours KO apres $RETRY redemarrages, arret des tentatives!"
	return 1
fi
}

#-----------------------------------
#  checking processes is running
#-----------------------------------
function check_services () {
VM_IP=$1
VM_NAME=$2

#check services on CENTRAL: mysql, apache, alfresco, karaf
check_process_is_running ${VM_IP} "mysql"
mysql=$?
check_process_is_running ${VM_IP} "apache"
apache=$?

#### alfresco is not running on central (distributed afresco)
#check_process_is_running ${VM_IP} "alfresco"
#alfresco=$?
alfresco=0

check_process_is_running ${VM_IP} "karaf"
karaf=$?

## Bug bash: can't put 4 conditions into if !!!!!=> work around done.
if [[ ${mysql} -eq 0 ]] && [[ ${apache} -eq 0 ]];
	then
		if [[ ${alfresco} -ne 0 ]] || [[ ${karaf} -ne 0 ]];
		  then
	  		echo "Problem: a service (either alfresco or/and karaf) is not running on $VM_NAME."
			exit 1
		fi
	else
		echo "Problem: a service (either mysql or/and apache) is not running on $VM_NAME."
		exit 1
fi
return 0
}

#-------------------------------------------
#     MAIN   MAIN   MAIN    MAIN
#-------------------------------------------

echo ""
echo "************************************"
echo "checking some services mysql, apache, karaf"
echo "************************************"

check_services ${VM_IP_LOCAL} ${VM_LOCAL}
ret=$?
if [ ${ret} -ne 0 ];
         then
		exit 1
fi

echo ""
echo "************************************"
echo "checking portal"
echo "************************************"
#check on LOCAL
check_and_loop ${VM_IP_LOCAL} ${VM_LOCAL}
ret=$?
if [ ${ret} -ne 0 ];
         then
		exit 1
fi
#return code 0 if OK
exit 0

