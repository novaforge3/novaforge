#!/bin/bash
BASE_DIR=$(readlink -f $(dirname $0))

killTimeout() {
	local _NAME=$1
	local _SIG=$2
	local _TIMEOUT=$3

	pkill -${_SIG} -f ${_NAME}
	# wait timeout
	( cmdpid=$BASHPID; (sleep ${_TIMEOUT}; kill $cmdpid 2> /dev/null ) & while pgrep -f "${_NAME}"; do sleep 0.5; done )
}

stopMariadb() {
	killTimeout mysqld SIGTERM 3
	killTimeout mysqld SIGKILL 3
}


stopMariadb

# Launch MariaDB to reset password ...
mysqld_safe --init-file=${BASE_DIR}/resetPassword.sql &
# ... and wait 5 sec
sleep 5

stopMariadb
