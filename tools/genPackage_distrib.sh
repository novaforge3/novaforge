#!/bin/bash
BASE_DIR=$(readlink -f $(dirname $0))
DATE=`date +%Y%m%d-%H%M%S`
EXIT=0
TMP_DIR=${TMP_DIR:=/datas/tmp}
SRC_DIR=/datas/sources/novaforge_sources
## Variables
DISTRIBUTED_SERVER_TYPES=("GED" "SVN" "PIC" "JIRA")
MAIN_SERVER_TYPES=("AIO" "PORTAL" "FRONT")
SUPPORTED_SERVER_TYPES=(${MAIN_SERVER_TYPES[@]} ${DISTRIBUTED_SERVER_TYPES[@]})
## DEFINED_SERVER_TYPES will be retrieved from cfg files
declare -a DEFINED_SERVER_TYPES
declare -a VMS
DATAS_DIRECTORY=/datas

## Set default cfg
DEFAULT_NOVAFORGE_HOME=$DATAS_DIRECTORY/novaforge3
DEFAULT_NOVAFORGE_USER=novaforge
DEFAULT_MAIN_PROFILE=bull ## Others profiles can be defined using OTHERS_PROFILES variables in your cfg file
DEFAULT_LAUNCHER_BRANCH=working
DEFAULT_LAUNCHER_TYPE=install
DEFAULT_LAUNCHER_VERSION=2.0
DEFAULT_INSTALL_BRANCH=working
DEFAULT_INSTALL_VERSION=current

#
## Utils
#
## The search string is the first argument and the rest are the array elements
containsElement() {
  	declare -u _ELEMENT
	  declare -u _VAR=$1
  	for _ELEMENT in "${@:2}"; do [[ "$_ELEMENT" == "$_VAR" ]] && return 0; done
  	return 1
}
isMainType() {
	containsElement $1 "${MAIN_SERVER_TYPES[@]}"
	return $?
}

isDistributedType() {
	containsElement $1 "${DISTRIBUTED_SERVER_TYPES[@]}"
	return $?
}
hasDeclaredType() {
	containsElement $1 "${DEFINED_SERVER_TYPES[@]}"
	return $?
}

## Display loading message and wait until PID is closed
spinner()
{
	local _PID=$1
	local _DELAY=0.5
	local _SPINSTR='|/-\'
	local _TXT="Please wait a moment ..."
	local _ESCPADE_SEQ=`expr ${#_TXT} + 5`
	while [ -d /proc/$_PID ]; do
		local _TEMP=${_SPINSTR#?}
		printf "\r [%c] %s" "$_SPINSTR" "$_TXT"
		local _SPINSTR=$_TEMP${_SPINSTR%"$_TEMP"}
		sleep $_DELAY
		for i in $(seq 1 $_ESCPADE_SEQ); do
			printf "\b"
		done
	done
	for i in $(seq 1 $_ESCPADE_SEQ); do
		printf " "
	done
	for i in $(seq 1 $_ESCPADE_SEQ); do
		printf "\b"
	done
	wait $_PID
	return $?
}

## Display an error message and set EXIT with first parameter
processError()
{
	local _CODE=$1
	local _MSG=$2
	if [ $_CODE -ne 0 ]; then
		processEcho "################################################################################"
		if [ "x$_MSG" == "x" ]; then
			$_MSG="A technical error occured; See log file for more details."
		fi
		processEcho "##### Error : $_MSG"
		MAIL_MSG_ERRORS="$MAIL_MSG_ERRORS \n\t- $_MSG"
		processEcho "################################################################################"
		EXIT=$_CODE
	fi	
}

processEcho()
{
	local _MSG=$1
	if [ "x$_MSG" != "x" ]; then
		echo -e "$_MSG"
		MAIL_MSG="$MAIL_MSG\n`date '+%D %T'` - "$_MSG""
	fi
}

#
## Functions
#
usage()
{
	processEcho "################################################################################"
	processEcho "Usage : installProfile.sh"
	processEcho "\tMandaty parameters: "
	processEcho "\t\t-c \tcfg file"
	processEcho "\tOption: "
	processEcho "\t\t-n \tenable no install mode to only prepare the installation"
	processEcho "\t\t-x \tverbose mode; If unset only errors will be displayed"
	processEcho "################################################################################"
}

init(){
	processEcho "---------------------------------------------------------------------------"
	if [ -z $WORK_DIR ]; then
		WORK_DIR=`mktemp -d $TMP_DIR/${MAIN_PROFILE}_${NOVAFORGE_VERSION}.XXXXXXXX`
	fi
	processEcho "----- Initialize working directory: $WORK_DIR" 	
}

readCFG()
{
	if [ ! -f "$CFG_FILE" ]; then
		processError  1 "You must specify an existing configuration file; Check mandatory parameters"
	   	usage
	else
		processEcho "---------------------------------------------------------------------------"
		processEcho "----- Read configurations" 

		shopt -s extglob
		tr -d '\r' < $CFG_FILE > $CFG_FILE.tmp
		while IFS='= ' read lhs rhs
		do
			if [[ ! $lhs =~ ^\ *# && -n $lhs ]]; then ## Skips single line comments and empty lines
				rhs="${rhs%%\#*}"    ## Del in line right comments
				rhs="${rhs%%*( )}"   ## Del trailing spaces
				rhs="${rhs#\"}"     ## Del starting string quotes 
				rhs="${rhs%\"}"     ## Del ending string quotes 
				declare -gr $lhs="$rhs"
			fi
		done < $CFG_FILE.tmp
		if [ -f $CFG_FILE.tmp ]; then
			rm -f $CFG_FILE.tmp
		fi
	fi
}

checkCFG()
{
	if [ $EXIT -eq 0 ]; then
		processEcho "---------------------------------------------------------------------------"
		processEcho "----- Check configurations" 
		## Check novaforge version
		if [ -z "$NOVAFORGE_VERSION" ];	then
			processError  1 "You must set a 'NOVAFORGE_VERSION' in the cfg file."	
    fi
		## Check novaforge home
		if [ -z "$NOVAFORGE_HOME" ]; then
			declare -rg NOVAFORGE_HOME=$DEFAULT_NOVAFORGE_HOME
		fi
		## Check novaforge user
		if [ -z "$NOVAFORGE_USER" ]; then
			declare -rg NOVAFORGE_USER=$DEFAULT_NOVAFORGE_USER
		fi
		## Check profile
		if [ -z "$MAIN_PROFILE" ]; then
			declare -rg MAIN_PROFILE=$DEFAULT_MAIN_PROFILE
		fi
		## Check technical versions
		if [ -z "$LAUNCHER_BRANCH" ]; then
			declare -rg LAUNCHER_BRANCH=$DEFAULT_LAUNCHER_BRANCH
		fi
		if [ -z "$LAUNCHER_TYPE" ]; then
			declare -rg LAUNCHER_TYPE=$DEFAULT_LAUNCHER_TYPE
		fi
		if [ -z "$LAUNCHER_VERSION" ]; then
			declare -rg LAUNCHER_VERSION=$DEFAULT_LAUNCHER_VERSION
		fi
		if [ -z "$INSTALL_BRANCH" ]; then
			declare -rg INSTALL_BRANCH=$DEFAULT_INSTALL_BRANCH
		fi
		if [ -z "$INSTALL_VERSION" ]; then
			declare -rg INSTALL_VERSION=$DEFAULT_INSTALL_VERSION
		fi

		if [ $EXIT -ne 0 ]; then
			usage
		fi
	fi
}

consolidateServerTypes()
{
	if [ $EXIT -eq 0 ]; then
		processEcho "---------------------------------------------------------------------------"
		processEcho "----- Consolidate server types" 	

		if [ ! -z "$SERVER_TYPES" ] && [ "x$DEFINED_SERVER_TYPES" == "x" ] ; then
			IFS=';' read -a DEFINED_SERVER_TYPES <<< "$SERVER_TYPES"
		elif [ ! -z "$SERVER_TYPES" ] && [ "x$DEFINED_SERVER_TYPES" != "x" ] ; then
			processError  1 "You shouldn't have SERVER_TYPES and VMs defined in your cfg file. Check your configuration file."
			usage		
		elif [ -z "$SERVER_TYPES" ] && [ "x$DEFINED_SERVER_TYPES" == "x" ] ; then
			processError  1 "You doesn't have any VMs or SERVER_TYPES defined in your cfg file. Check your configuration file."
			usage		
		fi
		unset RETURN_ELEMENT
	fi
}

buildPackage() 
{	
	if [ $EXIT -eq 0 ]; then
		processEcho "---------------------------------------------------------------------------"
		processEcho "----- Build installation package" 
		if [ -z $PACKAGE_DIR ]; then
			PACKAGE_DIR=$WORK_DIR/package
			## Delete package dir if existing
			if [ -d $PACKAGE_DIR ]; then
				rm -rf $PACKAGE_DIR
			fi
			## Create it
			mkdir -p $PACKAGE_DIR
		fi

		## Retrieve binary and install files
		cp -R $SRC_DIR/products/$INSTALL_BRANCH/shared/* $PACKAGE_DIR
    # Delete distributed.cfg and front.cfg files
  #  rm -f $PACKAGE_DIR/cfg/distributed.cfg
   # rm -f $PACKAGE_DIR/cfg/front.cfg
		## Retrieve launcher files
    mkdir -p $PACKAGE_DIR/pom
		cp $SRC_DIR/products/$LAUNCHER_BRANCH/$NOVAFORGE_VERSION/launcher/$LAUNCHER_TYPE/$LAUNCHER_VERSION/pom*.xml $PACKAGE_DIR/pom

		## Set exec permission on sh files
		find $PACKAGE_DIR -type f -name "*sh" -exec chmod +x {} \;
		
		## Update SUPPORTED_SERVER_TYPES for the current package
		local _STR_SUPPORTED_SERVER_TYPES="("
		local _SERVER_TYPE
		for _SERVER_TYPE in ${SUPPORTED_SERVER_TYPES[@]}
		do
			_STR_SUPPORTED_SERVER_TYPES="${_STR_SUPPORTED_SERVER_TYPES} \"$_SERVER_TYPE\""
		done
		_STR_SUPPORTED_SERVER_TYPES="${_STR_SUPPORTED_SERVER_TYPES} )"
		sed -i "s|SUPPORTED_SERVER_TYPES=.*|SUPPORTED_SERVER_TYPES=${_STR_SUPPORTED_SERVER_TYPES}|g" $PACKAGE_DIR/*.sh

		## Filter package files according declared server
		rm -rf $PACKAGE_DIR/pom/pom-dev.xml
		local _SUPPORTED_TYPE
		for _SUPPORTED_TYPE in ${SUPPORTED_SERVER_TYPES[@]}
		do
			if ! hasDeclaredType $_SUPPORTED_TYPE ; then
				rm -rf $PACKAGE_DIR/pom/pom-${_SUPPORTED_TYPE,,}.xml
			fi				
		done
	fi
}

buildMavenCache() 
{
	if [ $EXIT -eq 0 ]; then
		processEcho "---------------------------------------------------------------------------"
		processEcho "----- Build maven cache repository" 

		## Copy package to cache directory
		local _CACHE_INSTALL=$WORK_DIR/cache
		local _CACHE_CFG=$_CACHE_INSTALL/cfg/custom.cfg
		mkdir -p $_CACHE_INSTALL
		cp -r $PACKAGE_DIR/* $_CACHE_INSTALL/.
		if [ -f $_CACHE_INSTALL/cfg/aio.cfg ]; then
			cat $_CACHE_INSTALL/cfg/aio.cfg >> $_CACHE_CFG
		fi
		if [ -f $_CACHE_INSTALL/cfg/front.cfg ]; then
			cat $_CACHE_INSTALL/cfg/front.cfg >> $_CACHE_CFG
		fi
		if [ -f $_CACHE_INSTALL/cfg/distributed.cfg ]; then
			cat $_CACHE_INSTALL/cfg/distributed.cfg >> $_CACHE_CFG
		fi
		local _INSTALL_OPTS=""

		## Prepare home
		local _FAKE_HOME=$WORK_DIR/fakeHome
		sed -i "s|@NOVAFORGE_HOME@|$_FAKE_HOME|g" $_CACHE_CFG	
		mkdir -p $_FAKE_HOME
		declare -l _VERBOSE_OPTS=''
		if [ ! -z $VERBOSE_MODE ]; then
			_VERBOSE_OPTS='-v'
		fi

		## Browse all supported type and execute install in simulation mode to build maven cache
		for _TYPE in "${DEFINED_SERVER_TYPES[@]}"; do
		
			if [ $EXIT -eq 0 ]; then

				declare -l _LOWER_TYPE=$_TYPE
				declare -l _LOWER_ID=$_ID
				if isDistributedType $_TYPE; then
					cp -f ${BASE_DIR}/utils/deployment-cache.xml $_FAKE_HOME/deployment.xml
				fi

				## Try _MAX_RETRY+1 times until its ok; This is needed to avoid timeout on maven connection
        # For local repository, use value 0
        # For remote repositories, use value 30
				local _MAX_RETRY=0
				local _RETRYOTHERS_PROFILES

				## Resolve all artefact
				for _RETRY in `seq 0 $_MAX_RETRY`; do
					chmod +x $_CACHE_INSTALL/install.sh
					( $_CACHE_INSTALL/install.sh $_VERBOSE_OPTS -t $_LOWER_TYPE -p $MAIN_PROFILE,$OTHERS_PROFILES -c $_CACHE_CFG -l $PACKAGE_DIR/repository -w $_CACHE_INSTALL/tmp -m resolve $_INSTALL_OPTS >> $_CACHE_INSTALL/cache.log ) &
					spinner $!
					local _RETURN_CODE=$?
					if [ $_RETURN_CODE -eq 0 ]; then
						break
					elif [ $_RETRY -eq $_MAX_RETRY ]; then
						processError $_RETURN_CODE "Errors occured when downloading maven cache; See $_CACHE_INSTALL/cache.log for more details."
					fi		
				done

				## Launch simulate mode if all artefacts has been retrieved successfuly
				if [ $EXIT -eq 0 ] && isMainType $_CURRENT_TYPE ; then
					( $_CACHE_INSTALL/install.sh $_VERBOSE_OPTS -t $_LOWER_TYPE -p $MAIN_PROFILE,$OTHERS_PROFILES -c $_CACHE_CFG -l $PACKAGE_DIR/repository -o -w $_CACHE_INSTALL/tmp -m simulate $_INSTALL_OPTS >> $_CACHE_INSTALL/simulate.log ) &
					spinner $!
					local _RETURN_CODE=$?
					if [ $_RETURN_CODE -eq 0 ]; then
						break
					elif [ $_RETRY -eq $_MAX_RETRY ]; then
						processError $_RETURN_CODE "Errors occured when simulating installation process; See $_CACHE_INSTALL/simulate.log for more details."
					fi		
				fi
				
			else
				break
			fi
		done
		if [ $EXIT -eq 0 ] && [ -d  $_CACHE_INSTALL ]; then
			rm -rf $_CACHE_INSTALL
		fi	
		unset _TYPE
		unset _RETRY
	fi
}

createArchive()
{
	unset RETURN_ELEMENT
	if [ $EXIT -eq 0 ]; then
		local _ARCHIVE_NAME=package-$NOVAFORGE_VERSION-${MAIN_PROFILE}_${LAUNCHER_TYPE}
		local _ARCHIVE_FILE=$WORK_DIR/$_ARCHIVE_NAME.tar.gz
		processEcho "---------------------------------------------------------------------------"
		processEcho "----- Package installation files : $_ARCHIVE_FILE"
		
		## Create archive from package
		( tar -zcf $_ARCHIVE_FILE -C $WORK_DIR package --transform s/package/$_ARCHIVE_NAME/ ) &
		spinner $!

		declare -g RETURN_ELEMENT="package"
	fi
}

installProfile()
{
	## Creating working dir
	init

	## Get vms IP and HOST from config
	consolidateServerTypes

	## Build package
	buildPackage

	## Build maven repository cache
	buildMavenCache
  
	## Create archive from package
	createArchive
	PACKAGE_NAME=$RETURN_ELEMENT

	## Delete package dir
	if [ -d $PACKAGE_DIR ]; then
		rm -rf $PACKAGE_DIR
	fi 
}

processEcho "################################################################################"
processEcho "######"
processEcho "######  Processing your configuration "

while getopts ":c:hlnrx" OPTION
do
	case $OPTION in
		c)
			CFG_FILE=$OPTARG
			;;
		h)
			usage
			exit 0
			;;
		x)
			VERBOSE_MODE=1
			;;
		:)
			processError 1 "The option '$OPTARG' requiert an argument."
			usage
			exit $EXIT
			;;
		\?)
			processError 1 "The option '$OPTARG' is invalid."
			usage
			exit $EXIT
			;;
	esac
done
shift $((OPTIND-1))

processEcho "######  Profile : $CFG_FILE"

processEcho "######"
processEcho "################################################################################"
processEcho ""
## Check if cfg file exists and read the configuration
readCFG

if [ $EXIT -eq 0 ]; then
	## Check variables and set default value if some are missing
	checkCFG
fi

if [ $EXIT -eq 0 ]; then
	installProfile
fi

processEcho "################################################################################"
processEcho "######"
processEcho "######  End of process"
processEcho "######"
processEcho "################################################################################"
