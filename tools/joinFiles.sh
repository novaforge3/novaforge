#!/bin/sh

##---------------------------------------------------------------------------
## Shell to merge files previously split
##---------------------------------------------------------------------------

# Functions
do_log() 
{
  grav=$1
  shift
  if [ "$grav" = "ERROR" ]; then
    echo -e "\033[1;31;40m[$grav] $* \033[0m"
  	do_exit 1
  # ERROR with no exit
  elif [ "$grav" = "ERROR_NE" ]; then
    grav="ERROR"
    echo -e "\033[1;31;40m[$grav] $* \033[0m"
  elif [ "$grav" = "STEP" ]; then
    echo -e "\033[1;33;40m[$grav] $* \033[0m"
  elif [ "$grav" = "WARN" ]; then
    echo -e "\033[1;35;40m[$grav] $* \033[0m"
  elif [ "$grav" = "INFO" ]; then 
    echo "[$grav] $*"
  fi
}

do_exit()
{
  do_log "INFO" "Exiting."
  rm -f ${TMPFIC} ${ERRFIC} 2> /dev/null
  exit $1  
}

do_check_tools() 
{
  for e in $*
  do
    checktool=`which $e 2> ${ERRFIC}`
    if [ -s ${ERRFIC} ]; then
  		do_log "ERROR" "Program $e not found"
    elif [ ! -x `which $e` ]; then
  		do_log "ERROR" "Program $e non executable"
    fi
    rm -f ${ERRFIC} 2> /dev/null
  done
}

print_usage()
{
	echo "Usage: $0 folder_to_process"
	do_exit 1
}


# --------
# - Main -
# --------

# Working files
TMPFIC=/tmp/tmp.$$
ERRFIC=/tmp/err.$$

do_check_tools md5sum cat grep cut

dataDir=$1
do_log "STEP" "== Basic checks"
if [ "$dataDir" = "" ]; then
  do_log "ERROR_NE" "Target folder not provided"
  print_usage
elif [ ! -d "$dataDir" ]; then
  do_log "ERROR" "$dataDir is not a folder"
fi
do_log "INFO" "-- Target folder = $dataDir"

do_log "STEP" "Retrieve split files"
find $dataDir -type f -name '*.aa' > $TMPFIC

if [ ! -s $TMPFIC ]; then
  do_log "ERROR" "No split file found"
fi

do_log "STEP" "Join split files"
for p in `cat $TMPFIC`
do
  d=`dirname $p`
  f=`basename $p '.aa'`
  g=${d}/${f}
  do_log "INFO" "-- File : "${g}
  do_log "INFO" "- Performing merge"
  cat ${g}.a[a-z] > ${g}
  do_log "INFO" "- Checking merged file"
  i=${g}.info
  if [ ! -f "${i}" ]; then
    do_log "ERROR" "Information file ${i} not found"
  else
    md5i=`grep ';'${f}'$' ${i} | cut -d';' -f1`
    if [ "$md5i" = "" ]; then
      do_log "ERROR" "NO MD5 checksum provided for file "${f}
    else
      md5c=`md5sum ${g} | cut -d' ' -f1`
      if [ "$md5i" = "$md5c" ]; then
        do_log "INFO" "MD5 checksum matches ..."
        do_log "INFO" "Cleaning part files ..."
        rm -f ${g}.a[a-z] ${i} 2> /dev/null
      else 
        do_log "ERROR" "MD5 checksum not valid !"
      fi
    fi
  fi
done
do_exit 0
