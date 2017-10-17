#!/bin/bash 
BASE_DIR=$(readlink -f $(dirname $0))
EXIT=0
DATE=`date +%Y%m%d-%H%M%S`
#
# Functions
#

echoq()
{
	if [ $QUIET -eq 0 ] ; then
			echo -e $1
	fi
}
processError()
{
	if [ $1 -ne 0 ] ; then
		echo "********************************************************************************"
		if [ -n "$2" ] ; then
			echo -e "*** Error : $2"
		else
			echo "*** Error !!!"
		fi
		echo "********************************************************************************"
		EXIT=$1
	fi
}
usage()
{
	echoq ""
	echoq "################################################################################"
	echoq "Usage : install.sh"
	echoq "\tMandaty parameters: "
	echoq "\t\t-t [aio|portal|ged|svn|pic|jira]    type of server to install"
	echoq "\t\t-c <config_file> |-h <nova_home>    novaforge.cfg file OR path of novaforge home" 
	echoq "\tOption: "
	echoq "\t\t-i <id>                             id of server; If unset the type will be used"
	echoq "\t\t-p <profile>                        profile installation"
	echoq "\t\t-l <repository>                     local repository; If unset default maven settings will be used"
	echoq "\t\t-m [config|simulation|resolve]      execution mode"
	echoq "\t\t-k <productId>                      productId to konfigure"
	echoq "\t\t-o                                  offline mode"
	echoq "\t\t-r                                  ignore requisite"
	echoq "\t\t-v                                  verbose mode; If unset only errors will be displayed" 
	echoq "\t\t-w <dir>                            working directory; If unset a tmp directory will be used" 
	echoq "################################################################################"
 
}

checkServer()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Check server type" 
	if [ -z "$SERVER_TYPE" ]
	then
	   processError  1 "You must specify a server type; Check mandatory parameters"
	   usage
	fi
}

retrieveHome()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Retrieve home directory" 
	if [ -z "$HOME_PARAM" ] && [ -z "$CFG_FILE" ]; then
		
		processError  1 "Either cfg file  (-c) or home directory (-h) must be set."
		usage
		
	elif [ "x$HOME_PARAM" != "x" ] && [ "x$CFG_FILE" != "x" ]; then
		
		processError  1 "Either cfg file (-c) or home directory (-h) must be set."
		usage
		
	elif [ "x$HOME_PARAM" != "x" ]; then
		
		if [ ! -d "$HOME_PARAM" ]; then
			processError  1 "The home directory (-h) '$HOME_PARAM' does not exist. It should point to a valid installation directory."
		    usage
		else
	    	NOVAFORGE_HOME=$(echo ${HOME_PARAM%/})
		fi
		
	elif [ "x$CFG_FILE" != "x" ]; then
		
		if [ ! -f "$CFG_FILE" ]; then
			
			processError  1 "The cfg file (-c) '$CFG_FILE' does not exist. It should refer an existing file."
		    usage
		    
		else
			
			LOCAL_HOME=$(grep "^local:home" $CFG_FILE | cut -f2 -d"=" | head -n 1)
			SERVER_HOME=$(grep "^$SERVER_TYPE:home" $CFG_FILE | cut -f2 -d"=" | head -n 1)
			
			if [ "x$LOCAL_HOME" = "x" ] && [ "x$SERVER_HOME" = "x" ]; then
				
				processError  1 "The home directory found in configuration (-c) '$CFG_FILE' is invalid. Check local:home or $SERVER_TYPE:home property."
			    usage
			    
			elif [ "x$SERVER_HOME" != "x" ]; then
				
				if [ ! -d "$SERVER_HOME" ]; then
					mkdir -p $SERVER_HOME
				fi
		    	NOVAFORGE_HOME=$(echo ${SERVER_HOME%/})	
		    	
			elif [ "x$LOCAL_HOME" != "x" ]; then
				
				if [ ! -d "$LOCAL_HOME" ]; then
					mkdir -p $LOCAL_HOME
				fi
		    	NOVAFORGE_HOME=$(echo ${LOCAL_HOME%/})	
		    	
			fi
		fi
	fi
	if [ -z $NOVAFORGE_HOME ]; then
		
		processError  1 "The home directory cannot be retrieved from options given; Check cfg file (-c) or home directory (-h)."
		usage
		
	fi
}

checkIP()
{
  echoq "---------------------------------------------------------------------------"
  echoq "----- Check server IP" 
  if [ "$CFG_FILE" != "" ]; then
    if [ "$SERVER_TYPE" = "aio" ]; then  
      CRITERIA=local
    else
      CRITERIA=$SERVER_TYPE
    fi  
    IP_SERVER=$(grep "^$CRITERIA:ip" $CFG_FILE | cut -f2 -d"=" | head -n 1)
    HOST_SERVER=$(grep "^$CRITERIA:host" $CFG_FILE | cut -f2 -d"=" | head -n 1)      

    if [ "$IP_SERVER" != "" ] && [ "$HOST_SERVER" != "" ]; then
      if [ "`ip addr | grep inet | grep $IP_SERVER`" = "" ]; then
        processError 1 "The IP set for the current server [$IP_SERVER] is not assigned to one network card. Process stopped due to wrong server."       
      else
        echoq "Server IP check OK  [$SERVER_TYPE / $HOST_SERVER / $IP_SERVER]."         
      fi 
    else
      processError 1 "The cfg file $CFG_FILE does not contain ip and/or host information for current server."
    fi
  else
    echoq "Server IP not checked because not in cfg file (-c) mode." 
  fi
}

buildPom()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Build POM file" 
	local _TOKEN=Custom
	local _POM_FILE=$BASE_DIR/pom/pom-$SERVER_TYPE.xml
	POM_RESULTAT=$BASE_DIR/pom/pom-${DATE}.xml
	if [ ! -f "$_POM_FILE" ]
	then
		
		processError 1 "The pom '$_POM_FILE' doesn't exist."
		usage
		
	else 
		## Copy to tmp file
		cp $_POM_FILE $POM_RESULTAT 	  
		  
		## Read cfg
	    shopt -s extglob
		tr -d '\r' < $CFG_FILE > $CFG_FILE.tmp
		while IFS='= ' read KEY VALUE
		do
		    if [[ ! $KEY =~ ^\ *# && -n $KEY ]]; then ## Skips single line comments and empty lines
			VALUE="${VALUE%%\#*}"    ## Del in line right comments
			VALUE="${VALUE%%*( )}"   ## Del trailing spaces
			VALUE="${VALUE#\"}"     ## Del starting string quotes
			VALUE="${VALUE%\"}"     ## Del ending string quotes 
	        sed -i "/$_TOKEN/a\\\t\t<$KEY>$VALUE</$KEY>" $POM_RESULTAT
		    fi
		done < $CFG_FILE.tmp
		## Remove tmp file
		if [ -f $CFG_FILE.tmp ]; then
			rm -f $CFG_FILE.tmp
		fi		
	    
	fi
}

buildMavenOptions()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Build Maven options" 
	if [ -n "$LOCAL_REPOSITORY" ]; then
		if [ ! -d "$LOCAL_REPOSITORY" ]; then
			mkdir -p $LOCAL_REPOSITORY
			EXIT=$?
		fi
		MVN_OPT_REPO="-Dmaven.repo.local=$LOCAL_REPOSITORY"
	fi
	if [ $EXIT -eq 0 ] && [ -n "$PROFIL" ];	then
	    MVN_OPT_PROFIL="-P$PROFIL"
	    MVN_BEAVER_PROFILE=""
	    OIFS=$IFS
		IFS=$'\n' PROFIL_arr=(${PROFIL//,/$'\n'})
	    IFS=$OIFS
	    for p in "${PROFIL_arr[@]}"; do
		    # process "$i"
			MVN_BEAVER_PROFILE="$MVN_BEAVER_PROFILE -D$p=true"
		done
	fi
}

extractJRE()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Extract JRE" 
	if [ ! -d "$WORK_DIR/jre1.7.0_67" ]
	then
	   mkdir -p $WORK_DIR
	   if [ -f "$BIN_DIR/jre-7u80-linux-x64.tar.gz" ] 
	   then
			tar -xzf $BIN_DIR/jre-7u80-linux-x64.tar.gz -C  $WORK_DIR
			processError $? "Errors when extracting JRE archive to '$WORK_DIR'."
	   else
			processError 1 "The file $BIN_DIR/jre-7u80-linux-x64.tar.gz doesn't exist"
			usage
	   fi
	fi
}
	
extractMaven()
{
	echoq "---------------------------------------------------------------------------"
    echoq "----- Extract Apache Maven" 
	if [ ! -d "$WORK_DIR/apache-maven-3.0.5" ]
	then
	   if [ -f "$BIN_DIR/apache-maven-3.0.5-bin.tar.gz" ] 
	   then
	    	tar -xzf $BIN_DIR/apache-maven-3.0.5-bin.tar.gz -C $WORK_DIR
			processError $? "Errors when extracting Apache Maven archive to '$WORK_DIR'."
	   else
			processError 1 "The file $BIN_DIR/apache-maven-3.0.5-bin.tar.gz doesn't exist"
			usage
	   fi 
	fi
}

runMvn()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Run Maven command" 
	export M2_HOME=$WORK_DIR/apache-maven-3.0.5
	export JAVA_HOME=$WORK_DIR/jre1.7.0_80
	export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
  echo "Maven command : mvn -U clean install -Dresolved=$RESOLVED_MODE -Dsimulate=$SIMULATE_MODE -Dconfig=$CONFIG_MODE -f $POM_RESULTAT $MVN_OPTS_SERVER_ID $MVN_OPT_PROFIL $MVN_BEAVER_PROFILE $MVN_OPT_REPO $MVN_OPTS_OFFLINE $MVN_OPTS_VERBOSE $EXTRA_MVN_OPTS"
	mvn -U clean install -Dresolved=$RESOLVED_MODE -Dsimulate=$SIMULATE_MODE -Dconfig=$CONFIG_MODE -f $POM_RESULTAT $MVN_OPTS_SERVER_ID $MVN_OPT_PROFIL $MVN_BEAVER_PROFILE $MVN_OPT_REPO $MVN_OPTS_OFFLINE $MVN_OPTS_VERBOSE $EXTRA_MVN_OPTS
	processError $? "Installation has failed. Please check your configuration."
}


#Delete a directory
deleteDirectory()
{
	local _DIR=$1
	if [ -n "$_DIR" ] ; then
		if [ "$_DIR" != "/" ] ; then
			if [ -d "$_DIR" ] ; then
				rm -rf $_DIR
				processError $? "Errors when deleting the directory '$_DIR'."
			else
				echoq "The directory '$_DIR' doesn't exist."
			fi
		else
			processError 1 "The directory '$_DIR' shoudn't be /."
		fi
	fi
}

clean()
{
	echoq "---------------------------------------------------------------------------"
	echoq "----- Clean working directory" 
	deleteDirectory ${WORK_DIR}
	if [ -f $POM_RESULTAT ]; then
		rm -f $POM_RESULTAT
	fi
}

QUIET=0
CONFIG_MODE=false
SIMULATE_MODE=false
RESOLVED_MODE=false
MVN_OPTS_VERBOSE=-e
while getopts ":t:h:c:m:k:p:i:l:ovqrw:h" OPTION
do
	case $OPTION in
		t)
			SERVER_TYPE=$OPTARG
			;;
		h)		
			HOME_PARAM=$OPTARG
			;;
		c)		
			CFG_FILE=$OPTARG
			;;
		m)
			case $OPTARG in
				config)
					CONFIG_MODE=true
					;;
				simulate)
					SIMULATE_MODE=true
					;;
				resolve)
					RESOLVED_MODE=true
					;;
				*)
					processError 1 "$OPTARG is not a valid mode."
					usage
					exit $EXIT
					;;
			esac
			;;
		k)
			EXTRA_MVN_OPTS="$EXTRA_MVN_OPTS -DproductId=$OPTARG"
			;;
		p)
			PROFIL=$OPTARG
			;;
		i)
			MVN_OPTS_SERVER_ID="-DserverId=$OPTARG"
			;;
		l)
			LOCAL_REPOSITORY=$OPTARG
			;;
		o)	
			MVN_OPTS_OFFLINE="-o"
			;;
		v)	
			MVN_OPTS_VERBOSE="-X -e"
			;;
		q)	
			QUIET=1
			MVN_OPTS_VERBOSE="-q"
			;;
		r)
			EXTRA_MVN_OPTS="$EXTRA_MVN_OPTS -DignorePrerequisite=true"
			;;
		w)	
			WORK_DIR=$OPTARG
			;;
		h)
			usage
			exit 0
			;;
		:)
			processError 1 "The option '$OPTARG' requiert an argument."
			usage
			exit $EXIT
			;;
		\? )
			processError 1 "The option '$OPTARG' is invalid."
			usage
			exit $EXIT
			;;
	esac
done
shift $((OPTIND-1))

echoq "################################################################################"
echoq "######"
echoq "######  Starting installation"
echoq "######"
echoq "################################################################################"
echoq ""

## Check server type
checkServer

## Retrieve home
if [ $EXIT -eq 0 ]; then
	retrieveHome
fi

## Check IP
if [ "$CONFIG_MODE" != "true" ] && [ "$SIMULATE_MODE" != "true" ] && [ "$RESOLVED_MODE" != "true" ]; then
  if [ $EXIT -eq 0 ]; then
	 checkIP
  fi
fi

## Build pom file home
if [ $EXIT -eq 0 ]; then
	buildPom
fi

## Build Maven parameters
if [ $EXIT -eq 0 ]; then
	buildMavenOptions
fi

## Prepare installation
if [ $EXIT -eq 0 ]; then
	BIN_DIR=$BASE_DIR/bin
	if [ -z $WORK_DIR ]; then
		WORK_DIR=`mktemp -d /tmp/installTmp.XXXXXXXX`
	fi
fi

if [ $EXIT -eq 0 ]; then
	extractJRE
fi

if [ $EXIT -eq 0 ]; then
	extractMaven
fi

## Run installation
if [ $EXIT -eq 0 ]; then
	runMvn
fi

## Clean up working directory
clean

echoq "################################################################################"
echoq "######"
echoq "######  End of installation"
echoq "######"
echoq "################################################################################"
exit $EXIT
