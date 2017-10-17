#!/bin/bash
set -x
BASE_DIR=`readlink -f $(dirname $0)`
PROPERTIES=`readlink -f $1`
shift

################################################################################
#run
readProperties()
{
	echo "Reading '$PROPERTIES' file..."
	while read line
	do
		if [[ ${line} =~ .*=.* ]] && [[ ! ${line} =~ ^#.* ]] ; then
			key=$(cut -d '=' -f1 <<< ${line})
			value=$(cut -d '=' -f2 <<< ${line})
	test_sendmail_distribution		eval "value=$value"
			export ${key}="$value"
		fi
	done < ${PROPERTIES}
	echo "'$PROPERTIES' is read."
}


if [ ! -f ${PROPERTIES} ]; then
  echo "$PROPERTIES does not exist. Exiting..."
  exit 1
else
	readProperties
fi

if [ ! -f ${PROPERTIES} ]; then
  echo "$PROPERTIES does not exist. Exiting..."
  exit 1
else
	readProperties
fi

if [ -z "$VM_IP_CENTRAL"  -o -z "$VM_IP_LOCAL" -o -z "$VM_IP_LOCAL" -o -z "$VM_LOCAL" -o -z "$FORGE_START_TIME" -o -z "$RETRY" ]; then echo "At least one of the properties seem empty. Check properties file provided as first argument for starting the tests. Exiting..."
	exit 1;
fi



echo ${LIST_TO0}
echo ${SUBJECT}


function send_email () {
	logfile=`ls -rt /tmp|grep testDistrib_${SOURCE_VERSION}|tail -1`
	full_path_logfile=/tmp/${logfile}
	subject="${SUBJECT}_${SOURCE_VERSION}"
	for i in "${LIST_TO[@]}"
		do
		cat ${full_path_logfile} | /bin/mail -s ${subject} ${i}
	done
}





LIST_TOO=( "marc.blachon@bull.net" "marc.blachon@bull.net" )
send_email
