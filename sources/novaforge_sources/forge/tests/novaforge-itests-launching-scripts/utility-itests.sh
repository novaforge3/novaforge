#!/bin/bash

###################################################
#    utility-itests.sh
#    Author: Marc Blachon 16/04/2005
###################################################


print_debug () {
	if [ "$PRINT_DEBUG" = "yes" ]; then
	echo $1
	fi
}

check_utility () {
	echo "the utility function is OK"
}

#-------------------------------------------------------
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
#   stop karaf and remove logs
#--------------------------------------------------------

function std_stop_karaf_remove_log () {
 	VM_IP=$1
	PROFILE_FOLDER=$2
	### stop karaf normally to generate the dump file from Jacoco
	ssh -o "StrictHostKeyChecking no" root@${VM_IP} "cd /datas/${PROFILE_FOLDER}; ./bin/karaf stop"
	## wait for 200 sec to be sure .....
	echo "Waiting **** 200 sec. ***** until karaf has been stopped with: ./bin/karaf stop"
	### TODO: put it into property parameters ??? bof ????
	sleep 200
	res=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "ps aux | grep karaf | fgrep -v grep | fgrep -v tail"`
	echo ${res} | grep karaf > /dev/null
	response=$?
	if [ ${response} -ne 0 ]; then
		 echo "karaf has been stopped"
		 ssh root@${VM_IP} "rm -f /datas/${PROFILE_FOLDER}/logs/karaf/karaf.log"
		 # check log has been removed.		 
		 if ssh root@${VM_IP} "stat /datas/${PROFILE_FOLDER}/logs/karaf/karaf.log" \> /dev/null; then
		 	 echo "Error when removing karaf log! file log is still existing....."
		 else 
			echo "karaf log has been removed"
		 fi

		# Clear Karaf caches
		#echo "removing karaf caches ......"
		#ssh root@$VM_IP "rm -rf /datas/${PROFILE_FOLDER}/datas/karaf/*"
		# removing datasources
		#echo "removing datasources files ....."
		#ssh root@$VM_IP "rm -f /datas/${PROFILE_FOLDER}/engines/karaf/deploy/datasource.*"
		return 0
		
	else 
		echo "ERROR when stoping karaf"
		return 1
	fi
}

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
#   start karaf
#--------------------------------------------------------
function start_karaf () {
	VM_IP=$1
	PROFILE_FOLDER=$2
	ssh -o "StrictHostKeyChecking no" root@${VM_IP} "cd /datas/${PROFILE_FOLDER}; ./bin/karaf start clean"
	echo "starting karaf ............."	
}


#--------------------------------------------------------
#   install environment for Jacoco measurement
#
#   -> this phase has been put into install process within itests profile
#        (keep it until not already done!)
#--------------------------------------------------------
function create_jacoco_environment () {
VM_IP=$1
PROFILE_FOLDER=$2
#### create Jacoco folders
ssh root@${VM_IP} "mkdir -p /datas/${PROFILE_FOLDER}/datas/jacoco/jar"
ssh root@${VM_IP} "mkdir -p /datas/${PROFILE_FOLDER}/datas/jacoco/reports/jacococlasses"

### Copy Jacoco agent: jacocoagent.jar
scp ${AUTO_TEST_PATH}/Jacoco_Agent/lib/jacocoagent.jar root@${VM_IP}:/datas/${PROFILE_FOLDER}/datas/jacoco/jar
RET=$?
if [ ${RET} -ne 0 ]; then
	echo "ERROR copying jacocoagent.jar to ${VM_IP}:/datas/${PROFILE_FOLDER}/datas/jacoco/jar"
	exit ${RET}
fi

## setting rights on all Jacoco folder
ssh root@${VM_IP_LOCAL} "chown -R ${PROFILE_USER}:${PROFILE_USER} /datas/${PROFILE_FOLDER}/"
}

#--------------------------------------------------------
#
#   save reposts from the current run. prepare for new one
#
#--------------------------------------------------------
function save_and_init_jacoco_reports () {
VM_IP=$1
PROFILE_FOLDER=$2
# save results with time stamp
end_jacoco_date=`date +"%m-%d-%Y-%H-%M"`
ssh root@${VM_IP} "cd /datas/${PROFILE_FOLDER}/datas/jacoco/reports; tar cvf jacococlasses_${end_jacoco_date}.tar jacococlasses; mv jacoco.exec jacoco_${end_jacoco_date}.exec; rm -Rf jacococlasses/* " > /dev/null
echo ${end_jacoco_date}
}

#--------------------------------------------------------
#
#   send reposts to the itests-trunk job 
#
#--------------------------------------------------------
function send_jacoco_results_to_pic4_itests_trunk_job () {
#set -x
VM_IP=$1
PROFILE_FOLDER=$2
TIMESTAMP=$3
PIC_USER=$4
PIC_SERVER=$5
PIC_JOB_FOR_JACOCO_REPORTS=$6

##### delete old results
ssh ${PIC_USER}@${PIC_SERVER} "cd /datas/novaforge3/datas/jenkins/jobs/${PIC_JOB_FOR_JACOCO_REPORTS}/workspace/jacoco; rm -fR jacoco*"

##### transfert new results
scp ${AUTO_TEST_PATH}/${JACOCO_RESULT}/jacoco_${TIMESTAMP}.exec ${PIC_USER}@${PIC_SERVER}:/datas/novaforge3/datas/jenkins/jobs/${PIC_JOB_FOR_JACOCO_REPORTS}/workspace/jacoco/jacoco.exec
scp ${AUTO_TEST_PATH}/${JACOCO_RESULT}/jacococlasses_${TIMESTAMP}.tar ${PIC_USER}@${PIC_SERVER}:/datas/novaforge3/datas/jenkins/jobs/${PIC_JOB_FOR_JACOCO_REPORTS}/workspace/jacoco/jacococlasses.tar

##### extract tar classes archive
ssh ${PIC_USER}@${PIC_SERVER} "cd /datas/novaforge3/datas/jenkins/jobs/${PIC_JOB_FOR_JACOCO_REPORTS}/workspace/jacoco; tar xvf jacococlasses.tar" >/dev/null

##### remove the tar archive
ssh ${PIC_USER}@${PIC_SERVER} "cd /datas/novaforge3/datas/jenkins/jobs/${PIC_JOB_FOR_JACOCO_REPORTS}/workspace/jacoco; rm -f jacococlasses.tar"
}



