#!/bin/bash
#set -x
#load utility functions
. ${AUTO_TEST_PATH}/utility.sh

###################################################
#    - check access to portal page
#    - start initialization of the distribution bundle           
#    - install test feature fot distribution
#         on zonal and central
#
#    modifications
#          - 26/08/14:  adding dir for cmd jars (compiled manually)
#          - 27/11/14:  updating args of cut command within centOS7 (index starts at 1 instead of 0)
#	   - 01/12/14:  updating artifactId for the groupId of the tests commands
#
#     Author: Marc Blachon 31/03/2014
###################################################

## Do not change!
FEATURE_TEST_DISTRIBUTION_CENTRAL=novaforge-command-test-distribution-central
FEATURE_TEST_DISTRIBUTION_ZONAL_LOCAL=novaforge-command-test-distribution-zonal-local

################### WARNING ##########################
## due to changes onto the groupId of the tests commands for distribution (from 3.5 to 3.6) 
## and then into tests distribution feature
## for 3.6.0  (current commited version):
REP_JAR_REPO=tests
########## WARNING: it does not work with 3.5 (cmd cut, ... modified with centOS 7)
# en 3.5
#REP_JAR_REPO=modules

#---------------------------------------------------
#   push jar tests in order to install test features
#      NOTE: this is an overround function!!!
#---------------------------------------------------
function add_test_jar_into_repository () {
VM_IP=$1
TYPE=$2

DIR_JAR=Cmd_jar
CLIENT_BUNDLE="novaforge-command-test-distribution-client"
SERVER_BUNDLE="novaforge-command-test-distribution-server"
JAR_CLIENT="$CLIENT_BUNDLE-${SOURCE_VERSION}.jar"
JAR_SERVER="$SERVER_BUNDLE-${SOURCE_VERSION}.jar"

#########################
###### WARNING: checking if feature already installed has been temporally removed!
######################""
#client jar
file="/datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$CLIENT_BUNDLE/$SOURCE_VERSION/$JAR_CLIENT"
#if ! ssh root@$VM_IP stat $file \> /dev/null; then
	print_debug "***** file: $CLIENT_BUNDLE/$SOURCE_VERSION/$JAR_CLIENT does not exist into the karaf repository for client onto vm: $VM_IP"
	print_debug "pushing this jar to the karaf repository"
	#### avec 3.6.0: jar sous: tests au lieu de: modules
	#TODO: rajouter un if sur la version ???
            ssh root@${VM_IP} "mkdir -p /datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$CLIENT_BUNDLE"
	    ssh root@${VM_IP} "mkdir -p /datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$CLIENT_BUNDLE/$SOURCE_VERSION"
	    scp ${AUTO_TEST_PATH}/${DIR_JAR}/${JAR_CLIENT} root@${VM_IP}:/datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/${CLIENT_BUNDLE}/${SOURCE_VERSION}
	    ssh root@${VM_IP} "chown -R safran:safran /datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$CLIENT_BUNDLE"
#fi

#server jar
file="/datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$SERVER_BUNDLE/$SOURCE_VERSION/$JAR_SERVER"
if [ "$TYPE" = "server" ]; then	
	#if ! ssh root@$VM_IP stat $file \> /dev/null; then
		print_debug "***** file: $SERVER_BUNDLE/$SOURCE_VERSION/$JAR_SERVER does not exist into the karaf repository for server onto vm: $VM_IP"
	        print_debug "pushing this jar to the karaf repository"
		ssh root@${VM_IP} "mkdir -p /datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$SERVER_BUNDLE"
		ssh root@${VM_IP} "mkdir -p /datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$SERVER_BUNDLE/$SOURCE_VERSION"
		scp ${AUTO_TEST_PATH}/${DIR_JAR}/${JAR_SERVER} root@${VM_IP}:/datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/${SERVER_BUNDLE}/${SOURCE_VERSION}
		ssh root@${VM_IP} "chown -R safran:safran /datas/safran/engines/karaf/system/org/novaforge/forge/${REP_JAR_REPO}/$SERVER_BUNDLE"
	#fi
fi
}

#-----------------------------------------------------
#   start distribution initialization on ZONAL/CENTRAL
#----------------------------------------------------- 
function start_initialization (){
############### start distribution initialization on ZONAL or LOCAL ##############################
VM_IP=$1
VM_NAME=$2


list_dist_init=`ssh root@${VM_IP} "su safran -c \"/datas/safran/engines/karaf/bin/client bundle:list|grep Initialization|grep Distribution\""`
bundle_id=`echo ${list_dist_init}|cut -c1-3`
sleep 10
print_debug "****** bundle_id= $bundle_id"
if [ -z "$bundle_id" ]; then
	print_debug "BIG ERROR on $VM_NAME: either karaf client is not accessible (Failed to get the session.) or the distribution feature has not been deployed !!!"
	exit 1
else
	resultat=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/safran/engines/karaf/bin/client bundle:start $bundle_id"`
	#echo "resultat= $resultat"
	print_debug "bundle with id: $bundle_id has been started."
fi
return 0
}


#------------------------------------------
#   install test feature for distribution
#------------------------------------------
# case installed feature
# novaforge-command-test-distribution-central | 3.4.3 | x | novaforge-3.4.3 |
# case uninstalled feature
# novaforge-command-test-distribution-central | 3.4.3 | | novaforge-3.4.3 |

################################
### function to work around failure into: org.apache.sshd.common.io.nio2.Nio2Session (called with ssh)
################################
function getting_feature_with_retry () {
## expected result is
# novaforge-command-test-distribution-central     | 3.5.0   | x         | novaforge-3.5.0 |
VM_IP=$1
FEATURE=$2
x=1
while [ ${x} -le 4 ]
do
  	x=$(( $x + 1 ))
	feature=`ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/safran/engines/karaf/bin/client feature:list|grep $FEATURE"`
	RET=$?
        	if [ ${RET} -eq 0 ]; then
			return 0
		fi
done
return 1
}

######################################################################################
## put features for novaforge-command-test-distribution-<central or zonal> to: "installed" status
######################################################################################
function install_distribution_test () {
VM_IP=$1
FEATURE=$2
#feature=`ssh -o "StrictHostKeyChecking no" root@$VM_IP "/datas/safran/engines/karaf/bin/client feature:list|grep $FEATURE"`
getting_feature_with_retry ${VM_IP} ${FEATURE}
RET=$?
print_debug "*********************++++++********* $feature"
        if [ ${RET} -ne 0 ]; then
		### replace getting the feature by a function that retry several time the cmd (3 times) and normally return 0. If not : c'est grave !
	        ### this error is known and ahs been patched ....."	    
            print_debug "" 
            print_debug "$1: ***** ERROR ***** the feature: $2 does not exist!!!!."
	    print_debug "***** ERROR***** The auto test cannot be launched  !!!!!"                 
            print_debug ""   
	    return ${RET}
         else
	    # check feature is uninstalled
            echo ${feature}|grep " x ">/dev/null
	    RET=$?
            if [ ${RET} -eq 0 ]; then
		print_debug "feature is already installed !"                  
	    else
		   # install the feature
		   print_debug "installing the feature"
		   ssh -o "StrictHostKeyChecking no" root@${VM_IP} "/datas/safran/engines/karaf/bin/client feature:install $FEATURE"
		   #feature=`ssh -o "StrictHostKeyChecking no" root@$VM_IP "/datas/safran/engines/karaf/bin/client feature:list|grep $FEATURE"`
		   getting_feature_with_retry ${VM_IP} ${FEATURE}
		   # check feature has been installed		   
		   RET=$?
		   if [ ${RET} -eq 0 ]; then
			print_debug "feature has been installed !"
		   else
			print_debug "***** ERROR ***** Installing feature: $FEATURE on vm $VM_IP"
			return ${RET}
		   fi		   
	     fi                         
        fi
	return 0
}

#################### MAIN ##################

##### debug variables (set to:yes by default)
ADD_JAR_ACTION=no    # with itests-safran profile no more needed !
INIT_ACTION=yes     # at least one initialization of the distribution bundles are required.
INST_ACTION=no    # with itests-safran profile no more needed !

if [ "$ADD_JAR_ACTION" = "yes" ]; then
	print_debug ""
	print_debug "***********************************************"
	print_debug "add test jar into repository on CENTRAL and ZONAL"
	print_debug "**********************************************"
	add_test_jar_into_repository ${VM_IP_CENTRAL} "server"
	add_test_jar_into_repository ${VM_IP_CENTRAL} "client"
	add_test_jar_into_repository ${VM_IP_LOCAL} "client"
fi

if [ "$INIT_ACTION" = "yes" ]; then
	print_debug ""
	print_debug "***********************************************"
	print_debug "initializing osgi distribution on CENTRAL and ZONAL"
	print_debug "**********************************************"
	start_initialization ${VM_IP_CENTRAL} ${VM_CENTRAL}
	start_initialization ${VM_IP_LOCAL} ${VM_LOCAL}
fi

if [ "$INST_ACTION" = "yes" ]; then
	print_debug ""
	print_debug "***************************************************************************"
	print_debug "installing test feature for testing distribution on CENTRAL and ZONAL"
	print_debug "note: will become unuseful when M2_PROFILE:itests_safran will be introduced"
	print_debug "***************************************************************************"
	install_distribution_test ${VM_IP_CENTRAL} ${FEATURE_TEST_DISTRIBUTION_CENTRAL}
	ret=$?
	if [ ${ret} -ne 0 ]; then
		exit 1
	fi
	install_distribution_test ${VM_IP_LOCAL} ${FEATURE_TEST_DISTRIBUTION_ZONAL_LOCAL}
	if [ ${ret} -ne 0 ]; then
		exit 1
	fi
fi
exit 0



