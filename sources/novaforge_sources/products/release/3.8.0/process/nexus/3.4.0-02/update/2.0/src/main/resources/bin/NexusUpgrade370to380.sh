#!/bin/sh

#@(#)---------------------------------------------------------------------------
#@(#) Nexus upgrade script from 3.7.0 to 3.8.0 Forge release
#@(#)---------------------------------------------------------------------------

# DEPXMLFILE : Path to File deployment.xml
# ROOTDIR    : Root folder of Forge application (for example : /datas/safran)
# JSONAWK    : AWK file to generate json data file

# Hardcoded variables
# - Nexus admin account & password
NEXUSADM=admin
NEXUSPWD=admin123
# - MariaDB admin account & password
MYSQLADM=root
MYSQLPWD=root
# - Nexus URL for REST calls : hardcoded
URLNEXUS="http://127.0.0.1:8081/nexus-default/nexus/service/siesta/rest/v1/script"    
# - Public Repositories (Nexus 2 and 3)
PUBREPOLST="apache-snapshots central public maven-central maven-public nuget-group nuget.org-proxy"
# - New Managed Nexus repositories
TYPREPOLST="bower docker npm nuget pypi raw rubygems"
# - Managed Nexus roles  (id, not label)
#   anonymous : for release repositories only
TYPROLELST="anonymous administrator developper developper_senior observer"
# Anonymous Poxy Role Id
ANPRROLEID="anonymous_proxy"

# Internal variables
DEBUG=0          # Debug mode enabling
GRVUNZIP=0       # Groovy jar file unzipped ?

# Fonctions
do_log() 
{
  grav=$1
  shift
  DATELOG=$( date +"%d/%m/%y %H:%M:%S" )
  logtofile=1
  if [ "$grav" = "ERROR" ]; then
    echo -e "\033[1;31;40m$DATELOG [$grav] $* \033[0m"
  	do_exit 1
  # ERROR with no exit
  elif [ "$grav" = "ERROR_NE" ]; then
    grav="ERROR"
    echo -e "\033[1;31;40m$DATELOG [$grav] $* \033[0m"
  elif [ "$grav" = "STEP" ]; then
    echo -e "\033[1;33;40m$DATELOG [$grav] $* \033[0m"
  elif [ "$grav" = "WARN" ]; then
    echo -e "\033[1;35;40m$DATELOG [$grav] $* \033[0m"
  elif [ "$grav" = "INFO" ]; then 
    echo "$DATELOG [$grav] $*"
  elif [ "$grav" = "DEBUG" ]; then
    if [ $DEBUG -eq 1 ]; then 
      echo "$DATELOG [$grav] $*"
    else
      logtofile=0
    fi
  fi
  if [ -f "${LOGFILE}" ] && [ $logtofile -eq 1 ]; then
    echo "$DATELOG [$grav] $*" >> ${LOGFILE}
  fi
}

do_exit()
{
  rm -rf ${DIRTMP} 2> /dev/null
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

is_in_list()
{
	elt=$1
	shift 1
	while [ $# -gt 0 ]; do
	   	if [ "$1" = "$elt" ]; then
	   		echo 1
	   		return
	   	else
				shift
	   	fi
	done
	echo 0
}

is_in_array()
{
  elt=$1
  shift
  tab=($@)
  i=0
  while [ $i -ne ${#tab[*]} ]
  do
    if [ "${tab[$i]}" = "$elt" ]; then
      echo 1
      return
    fi
    let i+=1
  done
	echo 0
}


do_get_xmlprop()
{
  do_log "DEBUG" "do_get_xmlprop() - [$*]"
  fn=$1
  p1=$2
  p2=$3
  p3=$4 
  case $fn in 
    srvtype) RET=`xmllint --xpath "//deployment/server[attribute::id='$p1']/properties" $DEPXMLFILE 2> /dev/null | wc -l`
        ;;
    frgver)  RET=`xmllint --xpath "//deployment/server[attribute::id='$p1']/products/product[attribute::id='$p2']/properties/property[attribute::key='$p3']" $DEPXMLFILE | cut -d'>' -f2 | cut -d'<' -f1`
        ;;
    * )  do_log "ERROR" "Incorrect service $fn for function do_get_xmlprop()"
        ;;
  esac
}

do_init_prm()
{
  do_log "DEBUG" "do_init_prm() - [$*]"
  # Deployment.xml
  if [ "`ls -1 /datas/*/deployment.xml | wc -l`" != "1" ]; then
    do_log "ERROR" "File deployment.xml not found"
  else
    DEPXMLFILE=`ls -1 /datas/*/deployment.xml`
  fi  
  ROOTDIR=`dirname $DEPXMLFILE`
  
  # Server type
  SRVTYPE=portal
  RET=""
  do_get_xmlprop srvtype $SRVTYPE
  if [ $RET -eq 0 ]; then
    SRVTYPE=aio 
    RET=""
    do_get_xmlprop srvtype $SRVTYPE
    if [ $RET -eq 0 ]; then
      do_log "ERROR" "Server type not equals to aio or portal"
    fi
  fi 

  # Forge release
  RET=""
  do_get_xmlprop frgver $SRVTYPE "novaforge-runtime" "novaforge-version"
  FRGVER=$RET
  if [ "`echo $FRGVER | cut -c1-5`" != "3.8.0" ]; then
    do_log "ERROR" "Forge release not equals to 3.8.0"
  fi

  # Groovy scripts location
  GRVZIP=$ROOTDIR/engines/karaf/system/org/novaforge/forge/plugins/novaforge-nexus-client/$FRGVER/novaforge-nexus-client-$FRGVER.jar
}

do_gen_json()
{
  do_log "DEBUG" "do_gen_json() - [$*]"
  scr=$1
  scrfile=$2
  # Perform dos to unix conversion if needed
  if [ "`file $scrfile | grep CRLF`" != "" ]; then
    do_log "DEBUG" "Perform dos to unix conversion for $scrfile"
    mv $scrfile ${scrfile}.tmp
    tr -d '\r' < ${scrfile}.tmp > $scrfile
    rm -f ${scrfile}.tmp 2> /dev/null
  fi

  # Create AWK to generate json if needed
  if [ ! -f "$JSONAWK" ]; then
    do_log "DEBUG" "Create AWK file to generate json"
    cat >> $JSONAWK << EOF
BEGIN {
  printf("{\n");
  printf("  \\"name\\": \\"%s\\",\n",scr);
  printf("  \\"type\\": \\"groovy\\",\n");
  printf("  \\"content\\": \\"");
}
{
  printf("%s",\$0);
}
END {
  printf("\\"\n");
  printf("}\n");
}
EOF
  fi

  # Generate json for Groovy script
  do_log "DEBUG" "Generate json for Groovy script : ${scrfile}"
  # ATTENTION : lines containing jsonResult pattern are deleted because using ', \ and " characters which fail script loading ...
  grep -v jsonResult $scrfile | sed 's/\t//g' | awk -v scr=$scr -f $JSONAWK > ${scrfile}.json
}

do_curl_cmd()
{
  do_log "DEBUG" "do_curl_cmd() - [$*]"
  fn=$1
  p1=$2
  p2=$3
  case $fn in 
    getlist) curl -sv -u $NEXUSADM:$NEXUSPWD --header "Content-Type: application/json" -X GET $URLNEXUS -o $TMPFIC 2> $ERRFIC
        ;;
    delscr)  curl -sv -u $NEXUSADM:$NEXUSPWD --header "Content-Type: application/json" -X DELETE $URLNEXUS/$p1 -o $TMPFIC 2> $ERRFIC
        ;;
    loadscr) curl -sv -u $NEXUSADM:$NEXUSPWD --header "Content-Type: application/json" -X POST $URLNEXUS -d @$p1 -o $TMPFIC 2> $ERRFIC
        ;;
    runscr)  if [ "$p2" != "" ]; then
               curl -sv -u $NEXUSADM:$NEXUSPWD --header "Content-Type: text/plain" -X POST $URLNEXUS/$p1/run -d @$p2 -o $TMPFIC 2> $ERRFIC
             else
               curl -sv -u $NEXUSADM:$NEXUSPWD --header "Content-Type: text/plain" -X POST $URLNEXUS/$p1/run -o $TMPFIC 2> $ERRFIC
             fi
        ;;
    * )  do_log "ERROR" "Incorrect service $fn for function do_curl_cmd()"
        ;;
  esac 
  if [ "`grep 'HTTP/1.1 20[0-9]' $ERRFIC`" = "" ]; then
    do_log "ERROR_NE" "Error received from curl command :"
    cat $TMPFIC
    echo
    cat $TMPFIC >> ${LOGFILE}
    echo >> ${LOGFILE}
    do_exit 1
  fi
  rm -f $ERRFIC 2> /dev/null
}

do_delete_loaded_scripts()
{
  do_log "DEBUG" "do_delete_loaded_scripts() - [$*]"
  # Retrieve already loaded scripts
  do_curl_cmd getlist 
  if [ -f $TMPFIC ]; then
    if [ "`grep '  \"name\" : ' $TMPFIC`" != "" ]; then
      for s in `grep '  \"name\" : ' $TMPFIC | cut -d'"' -f4`
      do
        do_log "DEBUG" "Delete Groovy script "$s
        do_curl_cmd delscr $s 
      done
    fi
    rm -f $TMPFIC 2> /dev/null
  fi
  
  # Check that all scripts have been deleted
  do_curl_cmd getlist 
  if [ -f $TMPFIC ]; then
    if [ "`grep '  \"name\" : ' $TMPFIC`" != "" ]; then
      do_log "ERROR_NE" "Groovy scripts remain on server :"  
      grep '  \"name\" : ' $TMPFIC | cut -d'"' -f4
      echo
      grep '  \"name\" : ' $TMPFIC | cut -d'"' -f4 >> ${LOGFILE}
      echo >> ${LOGFILE}
      do_exit 1
    fi
  fi
}

do_load_script()
{
  do_log "DEBUG" "do_load_script() - [$*]"
  # Unzip Groovy scripts jar file if needed
  scr=$1
  if [ $GRVUNZIP -eq 0 ]; then
    do_log "DEBUG" "Perform Groovy scripts jar file unzip"
    unzip $GRVZIP groovy/* -d $DIRTMP > /dev/null 2>&1
    GRVDIR=$DIRTMP/groovy
    GRVUNZIP=1
  fi
  
  # Perform script loading
  scrfile=$GRVDIR/${scr}.groovy
  if [ -f "$scrfile" ]; then
    # Generate related json file
    do_gen_json $scr $scrfile
    if [ -f "${scrfile}.json" ]; then
      do_curl_cmd loadscr ${scrfile}.json 
    else
      do_log "ERROR" "Json file ${scrfile}.json not found"
    fi
  else
    do_log "ERROR" "Groovy file $scrfile not found"
  fi
}

do_run_script()
{
  do_log "DEBUG" "do_run_script() - [$*]"
  # Perform script load if needed
  scr=$1
  fic=$2
  if [ `is_in_array $scr ${tabScripts[@]}` -eq 0 ]; then
    do_load_script $scr
    tabScripts[${nbscripts}]=$scr
    let nbscripts+=1
  fi

  # Perform script run
  if [ -f "$fic" ]; then
    do_curl_cmd runscr $scr $fic
  else
    do_log "ERROR" "File $fic not found"
  fi
}

do_mysql_cmd()
{
  do_log "DEBUG" "do_mysql_cmd() - [$*]"
  fn=$1
  p1=$2
  case $fn in 
    getprojects) echo "select CONCAT('@',forge_project_id,'@',tool_project_id,'@') from plugin_nexus.plugin_instance;" > $TMPFIC1
        ;;
    getusers) echo "select DISTINCT(CONCAT('@',A.login,'@')) " > $TMPFIC1
              echo "from novaforge.ACTOR_USER A, plugin_nexus.plugin_instance B, novaforge.MEMBERSHIP C, novaforge.PROJECT_ELEMENT D" >> $TMPFIC1
              echo "where A.id=C.actor_id and D.id=C.project_id and D.element_id=B.forge_project_id and B.tool_project_id='$p1';" >> $TMPFIC1
        ;;
    *)  do_log "ERROR" "Incorrect service $fn for function do_mysql_cmd"
        ;;
  esac 
  mysql -u$MYSQLADM -p$MYSQLPWD < $TMPFIC1 > $TMPFIC
  if [ $? -ge 1 ]; then
    do_log "ERROR" "Error when executing mysql command"
  fi
  rm -f $TMPFIC1 2> /dev/null
}

do_get_roles()
{
  do_log "DEBUG" "do_get_roles() - [$*]"
  echo {\"userId\":\"dummy\"} > $TMPFIC 
  do_run_script "security.listRoles" $TMPFIC 
  if [ -f $TMPFIC ]; then
    rm -f $AWKFIC 2> /dev/null
    cat >> $AWKFIC << EOF
{
  gsub(/\"/,"");
  if (index(\$0,"  result : ")==1) {
    gsub(/\\\/,"");
    nb=split(\$0,a,"{roleId:");
    for (i=2; i<=nb; i++) {
      split(a[i],b,",");
      printf("%s\n",b[1]);  
    }
  }
}
EOF
    # Extract roles
    do_log "DEBUG" "Extract roles"
    nbroles=0
    for r in `awk -f $AWKFIC $TMPFIC | sort -u`
    do
      tabRoleIds[${nbroles}]=$r
      let nbroles+=1
    done
    rm -f $TMPFIC $AWKFIC 2> /dev/null
  fi

  if [ $DEBUG -eq 1 ]; then
    if [ $nbroles -gt 0 ]; then
      do_log "DEBUG" "Roles defined in Nexus"
      r=0
      while [ $r -ne ${#tabRoleIds[*]} ]
      do
        echo ${tabRoleIds[$r]}
        echo ${tabRoleIds[$r]} >> ${LOGFILE}
        let r+=1
      done
    else
      do_log "WARN" "No role defined in Nexus"
    fi
  fi
}

do_recreate_anonymous_proxy_role()
{
  do_log "DEBUG" "do_recreate_anonymous_proxy_role() - [$*]"
  aproleid=$ANPRROLEID
  # Deletion
  if [ `is_in_array $aproleid ${tabRoleIds[@]}` -eq 1 ]; then
    do_log "INFO" "-- Delete $aproleid role"
    echo "{\"sourceId\":\"default\",\"id\":\"$aproleid\"}" > $TMPFIC 
    do_run_script "security.deleteRole" $TMPFIC 
    rm -f $TMPFIC 2> /dev/null
  fi 
  
  # Creation
  echo -n "{\"id\":\"$aproleid\",\"sourceId\":null,\"name\":\"Anonymous Proxy Role\",\"description\":\"Anonymous role for proxy repositories\"," > $TMPFIC
  echo -n "\"privilegeIds\":[\"nx-healthcheck-read\",\"nx-search-read\"" >> $TMPFIC

  for r in $PUBREPOLST
  do
    if [ "`echo $r | grep nuget`" != "" ]; then
      repotype=nuget
    else
      repotype=maven2
    fi
    echo -n ",\"nx-repository-view-${repotype}-${r}-browse\",\"nx-repository-view-${repotype}-${r}-read\"" >> $TMPFIC
  done
  echo "],\"roleIds\":[]}" >> $TMPFIC
  
  if [ -s $TMPFIC ]; then
    do_log "INFO" "-- Create $aproleid role"
    do_run_script "security.addRole" $TMPFIC 
    rm -f $TMPFIC 2> /dev/null
  fi
}

do_get_user_roles()
{
  do_log "DEBUG" "do_get_user_roles() - [$*]"
  user=$1 
  # Check if user exists
  echo \"$user\" > $TMPFIC
  do_run_script "security.existsUser" $TMPFIC
  userexist=0
  if [ -f $TMPFIC ]; then
    if [ "`grep '  \"result\" : \"true\"' $TMPFIC`" != "" ]; then
      userexist=1
    fi
    rm -f $TMPFIC 2> /dev/null
  fi
  
  # Get user's roles if user exists
  if [ $userexist -eq 1 ]; then
    echo \"$user\" > $TMPFIC
    do_run_script "security.getUser" $TMPFIC
    if [ -f $TMPFIC ]; then
      if [ ! -f $UROLEAWK ]; then
        cat >> $UROLEAWK << EOF
{
  gsub(/\"/,"");
  if (index(\$0,"  result : ")==1) {
    gsub(/\\\/,"");
    split(\$0,c,",roles:");
    nb=split(c[2],a,"{roleId:");
    for (i=2; i<=nb; i++) {
      split(a[i],b,",");
      printf("%s\n",b[1]);  
    }
  }
}
EOF
      fi
      # Extract roles
      do_log "DEBUG" "Extract user roles"
      awk -f $UROLEAWK $TMPFIC | sort -u > $TMPFIC1
      if [ -s $TMPFIC1 ]; then
        nbuserroles=0
        for ur in `cat $TMPFIC1`
        do
          tabUserRoleIds[${nbuserroles}]=$ur
          let nbuserroles+=1
        done
      fi
      rm -f $TMPFIC $TMPFIC1 2> /dev/null
    fi
  
    if [ $DEBUG -eq 1 ]; then
      do_log "DEBUG" "Roles assigned to user "$user
      ur=0
      while [ $ur -ne ${#tabUserRoleIds[*]} ]
      do
        echo ${tabUserRoleIds[$ur]}
        echo ${tabUserRoleIds[$ur]} >> ${LOGFILE}
        let ur+=1
      done
    fi
  else
    do_log "WARN" "User $user does not exist in Nexus"
  fi
}

do_manage_anonymous_user_roles()
{
  do_log "DEBUG" "do_manage_anonymous_user_roles() - [$*]"
  anuser="anonymous"
  aproleid=$ANPRROLEID
  uptodo=0
  # Get Anonymous user's roles
  do_get_user_roles $anuser
  
  ur=0
  while [ $ur -ne ${#tabUserRoleIds[*]} ]
  do
    # Delete default Anonymous role
    if [ "${tabUserRoleIds[$ur]}" = "nx-anonymous" ]; then
      tabUserRoleIds[${ur}]=""
      uptodo=1
      break
    fi
    let ur+=1
  done
  # Add Anonymous Proxy role
  if [ `is_in_array $aproleid ${tabUserRoleIds[@]}` -eq 0 ]; then
    tabUserRoleIds[${nbuserroles}]=$aproleid
    let nbuserroles+=1
    uptodo=1
  fi

  # Perform user roles update in Nexus if needed
  if [ $uptodo -eq 0 ]; then
    do_log "INFO" "-- Existing roles already OK : no update to perform"
  else
    # Clean roles list
    ur=0
    nur=0
    while [ $ur -ne ${#tabUserRoleIds[*]} ]
    do
      if [ "${tabUserRoleIds[$ur]}" != "" ]; then
        tabUserRoleIdsNew[$nur]=${tabUserRoleIds[$ur]}
        let nur+=1
      fi
      let ur+=1
    done
    unset tabUserRoleIds
     
    echo -n "{\"userId\":\"$anuser\",\"roleIds\":[\"${tabUserRoleIdsNew[0]}\"" > $TMPFIC
    nur=1
    while [ $nur -ne ${#tabUserRoleIdsNew[*]} ]
    do
      echo -n ",\"${tabUserRoleIdsNew[$nur]}\"" >> $TMPFIC
      let nur+=1
    done
    echo "]}" >> $TMPFIC 
    unset tabUserRoleIdsNew
    if [ -s $TMPFIC ]; then
      do_run_script "security.setUserRoles" $TMPFIC
      rm -f $TMPFIC 2> /dev/null
    fi
  fi
}

do_get_projects()
{
  do_log "DEBUG" "do_get_projects() - [$*]"
  do_mysql_cmd getprojects
  nbprojects=0
  if [ -f $TMPFIC ]; then
    if [ "`grep '^@.*@$' $TMPFIC`" != "" ]; then
      for l in `grep '^@.*@$' $TMPFIC`
      do
        tabProjectIds[${nbprojects}]=`echo $l | cut -d'@' -f2`
        tabToolIds[${nbprojects}]=`echo $l | cut -d'@' -f3`
        let nbprojects+=1
      done
    fi
    rm -f $TMPFIC 2> /dev/null
  fi
  if [ $DEBUG -eq 1 ]; then
    if [ $nbprojects -gt 0 ]; then
      do_log "DEBUG" "Projects concerned with Nexus tool :"
      p=0
      while [ $p -ne ${#tabProjectIds[*]} ]
      do
        echo ${tabProjectIds[$p]}" ["${tabToolIds[$p]}"]"
        echo ${tabProjectIds[$p]}" ["${tabToolIds[$p]}"]" >> ${LOGFILE}
        let p+=1
      done
    else
      do_log "DEBUG" "No project concerned with Nexus tool"
    fi
  fi
}

do_create_repo()
{
  do_log "DEBUG" "do_create_repo() - [$*]"
  typ=$1
  name=$2
  
  # Check if repository already exists
  repoexist=0
  echo \"$name\" > $TMPFIC
  do_run_script "repository.exists" $TMPFIC
  if [ -f $TMPFIC ]; then
    if [ "`grep '  \"result\" : \"true\"' $TMPFIC`" != "" ]; then
      repoexist=1
      do_log "INFO" "-- Repository $name already exists"
    fi
    rm -f $TMPFIC 2> /dev/null
  fi
  
  # Perform repository creation if repository does not exist
  repocreated=0;
  if [ $repoexist -eq 0 ]; then
    case $typ in 
      bower) echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\"}" > $TMPFIC
             do_run_script "repository.createBowerHosted" $TMPFIC
             repocreated=1;
          ;;
      docker) echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\",\"httpPort\":null,\"httpsPort\":null,\"v1Enabled\":true}" > $TMPFIC
              do_run_script "repository.createDockerHosted" $TMPFIC
             repocreated=1;
          ;;
      npm)   echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\"}" > $TMPFIC
             do_run_script "repository.createNpmHosted" $TMPFIC
             repocreated=1;
          ;;
      nuget) echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\"}" > $TMPFIC
             do_run_script "repository.createNugetHosted" $TMPFIC
             repocreated=1;
          ;;
      pypi)  echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\"}" > $TMPFIC
             do_run_script "repository.createPyPiHosted" $TMPFIC
             repocreated=1;
          ;;
      raw)   echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\"}" > $TMPFIC
             do_run_script "repository.createRawHosted" $TMPFIC
             repocreated=1;
          ;;
      rubygems) echo "{\"name\":\"$name\",\"blobStoreName\":\"default\",\"strictContentTypeValidation\":true,\"writePolicy\":\"ALLOW\"}" > $TMPFIC
                do_run_script "repository.createRubygemsHosted" $TMPFIC
             repocreated=1;
          ;;
      *)  do_log "ERROR" "Incorrect repository type $typ for function do_create_repo()"
          ;;
    esac
    if [ $repocreated -eq 1 ]; then
      do_log "INFO" "-- Repository $name created"
    fi
    rm -f $TMPFIC 2> /dev/null
  fi
}

do_create_role()
{
  do_log "DEBUG" "do_create_role() - [$*]"
  repotype=$1
  reponame=$2
  roletype=$3 
  roleid=${reponame}"_"${roletype}
  
  # Perform role creation if role does not exist
  if [ `is_in_array $roleid ${tabRoleIds[@]}` -eq 0 ]; then
    # Depending on role type
    # - General part
    isrelease=0
    isgenpart=0
    if [ `is_in_list $roletype $TYPROLELST` -eq 1 ]; then
      isgenpart=1
      if [ "$roletype" = "anonymous" ]; then
        if [ "`echo $reponame | grep '_release$'`" != "" ]; then
          isrelease=1
        else
          isgenpart=0
        fi  
      fi 
      if [ $isgenpart -eq 1 ]; then
        # Role name
        stt=`echo $roletype|cut -c1|tr [a-z] [A-Z]`
        roletypil=$stt`echo $roletype | cut -c2- | sed 's/_s/ S/'`
        rolename=${reponame}" ("$roletypil")"
        # Role description
        roledesc=$roletypil" role on "$reponame" repository"
        echo -n "{\"id\":\"$roleid\",\"sourceId\":null,\"name\":\"$rolename\",\"description\":\"$roledesc\"," > $TMPFIC
      fi
    fi
    
    # - Specific part : privileges associated to role
    if [ $isgenpart -eq 1 ]; then
      case $roletype in 
        anonymous) # Only if repo is a release one
                   if [ $isrelease -eq 1 ]; then
                     echo "\"privilegeIds\":[\"nx-repository-view-${repotype}-${reponame}-browse\",\"nx-repository-view-${repotype}-${reponame}-read\"],\"roleIds\":[]}" >> $TMPFIC
                   fi
            ;;
        administrator) echo "\"privilegeIds\":[\"nx-repository-view-${repotype}-${reponame}-*\",\"nx-repository-admin-${repotype}-${reponame}-*\"],\"roleIds\":[]}" >> $TMPFIC
            ;;
        developper_senior) echo "\"privilegeIds\":[\"nx-repository-view-${repotype}-${reponame}-*\"],\"roleIds\":[]}" >> $TMPFIC
            ;;
        developper) echo -n "\"privilegeIds\":[\"nx-repository-view-${repotype}-${reponame}-browse\",\"nx-repository-view-${repotype}-${reponame}-add\"," >> $TMPFIC
                    echo    "\"nx-repository-view-${repotype}-${reponame}-read\",\"nx-repository-view-${repotype}-${reponame}-edit\"],\"roleIds\":[]}" >> $TMPFIC
            ;;
        observer) echo "\"privilegeIds\":[\"nx-repository-view-${repotype}-${reponame}-browse\",\"nx-repository-view-${repotype}-${reponame}-read\"],\"roleIds\":[]}" >> $TMPFIC
            ;;
        *)  do_log "ERROR" "Incorrect role type $roletype for function do_create_role()"
            ;;
      esac 
      if [ -s $TMPFIC ]; then
        do_run_script "security.addRole" $TMPFIC
        if [ `is_in_list $roletype $TYPROLELST` -eq 1 ]; then
          do_log "INFO" "-- Role $roleid created"
        fi
        rm -f $TMPFIC 2> /dev/null
      fi
    fi
  else
    do_log "INFO" "-- Role $roleid already exists"
  fi
}

do_get_users()
{
  do_log "DEBUG" "do_get_users() - [$*]"
  tid=$1
  do_mysql_cmd getusers $tid
  if [ -f $TMPFIC ]; then
    if [ "`grep '^@.*@$' $TMPFIC`" != "" ]; then
      # Extract users
      do_log "DEBUG" "Extract users"
      nbusers=0
      for u in `grep '^@.*@$' $TMPFIC | cut -d'@' -f2 | sort -u`
      do
        tabUserIds[${nbusers}]=$u
        let nbusers+=1
      done
    fi
    rm -f $TMPFIC 2> /dev/null
  fi
  if [ $DEBUG -eq 1 ]; then
    do_log "DEBUG" "Users concerned with current Nexus instance :"
    u=0
    while [ $u -ne ${#tabUserIds[*]} ]
    do
      echo ${tabUserIds[$u]}
      echo ${tabUserIds[$u]} >> ${LOGFILE}
      let u+=1
    done
  fi
}

do_add_user_new_roles()
{
  do_log "DEBUG" "do_add_user_new_roles() - [$*]"
  user=$1
  tid=$2
  uptodo=0
  
  # Initialization of tabUserRoleIdsNew
  ur=0
  nur=0
  while [ $ur -ne ${#tabUserRoleIds[*]} ]
  do
    tabUserRoleIdsNew[$nur]=${tabUserRoleIds[$ur]}
    let nur+=1
    let ur+=1
  done
  for v in snapshot release
  do
    for rt in $TYPROLELST
    do
      mrid=${tid}${v}"_"$rt
      ur=0
      while [ $ur -ne ${#tabUserRoleIds[*]} ]
      do
        # If Maven role exists, add equivalent role for others repository type
        if [ "${tabUserRoleIds[$ur]}" = "$mrid" ]; then
          for rf in $TYPREPOLST
          do
            urid=${tid}${rf}"_"${v}"_"$rt
            do_log "INFO" "-- RoleId : "$urid
            if [ `is_in_array $urid ${tabUserRoleIdsNew[@]}` -eq 1 ]; then
              do_log "DEBUG" "RoleId already existing : not added"
            else
              tabUserRoleIdsNew[${nur}]=$urid
              let nur+=1
              uptodo=1
            fi
          done
        fi
        let ur+=1
      done
    done
  done

  # Add Anonymous Role Id if not assigned  
  aproleid=$ANPRROLEID
  if [ `is_in_array $aproleid ${tabUserRoleIdsNew[@]}` -eq 0 ]; then
    do_log "INFO" "-- Adding Anonymous Proxy role"
    tabUserRoleIdsNew[${nur}]=$aproleid
    let nur+=1
    uptodo=1
  fi
  
  # Perform user roles update in Nexus only if needed
  if [ $uptodo -eq 0 ]; then
    do_log "INFO" "-- Existing roles already OK : no update to perform"
  else
    echo -n "{\"userId\":\"$user\",\"roleIds\":[\"${tabUserRoleIdsNew[0]}\"" > $TMPFIC
    nur=1
    while [ $nur -ne ${#tabUserRoleIdsNew[*]} ]
    do
      echo -n ",\"${tabUserRoleIdsNew[$nur]}\"" >> $TMPFIC
      let nur+=1
    done
    echo "]}" >> $TMPFIC 
    if [ -s $TMPFIC ]; then
      do_run_script "security.setUserRoles" $TMPFIC
      rm -f $TMPFIC 2> /dev/null
    fi
  fi
  unset tabUserRoleIds
  unset tabUserRoleIdsNew
}

do_recreate_maven_roles()
{
  do_log "DEBUG" "do_recreate_maven_roles() - [$*]"
  tid=$1
  
  # Deletion
  r=0
  fnd=0
  while [ $r -ne ${#tabRoleIds[*]} ]
  do
    if [ "`echo ${tabRoleIds[$r]} | grep '^'${tid}snapshot`" != "" ] || 
       [ "`echo ${tabRoleIds[$r]} | grep '^'${tid}release`" != "" ]; then
      do_log "INFO" "-- Delete Maven role "${tabRoleIds[$r]}
      echo "{\"sourceId\":\"default\",\"id\":\"${tabRoleIds[$r]}\"}" > $TMPFIC 
      do_run_script "security.deleteRole" $TMPFIC 
      tabRoleIds[$r]=""
      fnd=1
    fi
    let r+=1
  done
  if [ $fnd -eq 0 ]; then
    do_log "WARN" "No Maven role found for project"
  fi
  rm -f $TMPFIC 2> /dev/null
  
  # Creation
  for v in snapshot release
  do
    reponame=${tid}${v}
    for roletype in $TYPROLELST
    do
      roletocreate=1
      if [ "$v" = "snapshot" ] && [ "$roletype" = "anonymous" ]; then
        roletocreate=0
      fi
      if [ $roletocreate -eq 1 ]; then
        do_log "INFO" "-- Create Maven role "$reponame"_"$roletype
        do_create_role maven2 $reponame $roletype
      fi
    done
  done
}

do_add_user_maven_roles()
{
  do_log "DEBUG" "do_add_user_maven_roles() - [$*]"
  user=$1
  tid=$2
  uptodo=0
  
  # Initialization of tabUserRoleIdsNew
  ur=0
  nur=0
  while [ $ur -ne ${#tabUserRoleIds[*]} ]
  do
    tabUserRoleIdsNew[$nur]=${tabUserRoleIds[$ur]}
    let nur+=1
    let ur+=1
  done

  for v in snapshot release
  do
    for rt in $TYPROLELST
    do
      drid=${tid}"docker_"${v}"_"$rt
      ur=0
      while [ $ur -ne ${#tabUserRoleIds[*]} ]
      do
        # If Docker role exists, add equivalent role for Maven repository
        if [ "${tabUserRoleIds[$ur]}" = "$drid" ]; then
          urid=${tid}${v}"_"$rt
          do_log "INFO" "-- RoleId : "$urid
          if [ `is_in_array $urid ${tabUserRoleIdsNew[@]}` -eq 1 ]; then
            do_log "DEBUG" "RoleId already existing : not added"
          else
            tabUserRoleIdsNew[${nur}]=$urid
            let nur+=1
            uptodo=1
          fi
        fi
        let ur+=1
      done
    done
  done
  # Perform user roles update in Nexus only if needed
  if [ $uptodo -eq 0 ]; then
    do_log "INFO" "-- Existing roles already OK : no update to perform"
  else
    echo -n "{\"userId\":\"$user\",\"roleIds\":[\"${tabUserRoleIdsNew[0]}\"" > $TMPFIC
    nur=1
    while [ $nur -ne ${#tabUserRoleIdsNew[*]} ]
    do
      echo -n ",\"${tabUserRoleIdsNew[$nur]}\"" >> $TMPFIC
      let nur+=1
    done
    echo "]}" >> $TMPFIC 
    if [ -s $TMPFIC ]; then
      do_run_script "security.setUserRoles" $TMPFIC
      rm -f $TMPFIC 2> /dev/null
    fi
  fi
  unset tabUserRoleIds
  unset tabUserRoleIdsNew
}

# --------
# - Main -
# --------
do_log "STEP" "Initialization"
BNSCR=`basename $0 '.sh'`
DIRTMP=/tmp/$BNSCR
if [ -d $DIRTMP ]; then
  rm -rf $DIRTMP/* 2> /dev/null
else
  mkdir $DIRTMP
fi

# Fichiers de travail
JSONAWK=${DIRTMP}/json.awk
UROLEAWK=${DIRTMP}/urole.awk
AWKFIC=${DIRTMP}/awk.$$
TMPFIC=${DIRTMP}/tmp.$$
TMPFIC1=${DIRTMP}/tmp1.$$
ERRFIC=${DIRTMP}/err.$$

# Fichier de logs
if [ "`ls -1d /datas/*/logs/* | grep 'nexus$' | wc -l`" != "1" ]; then
  do_log "ERROR" "Nexus log folder not found"
else
  DIRLOG=`ls -1d /datas/*/logs/* | grep 'nexus$'`
  LOGFILE=${DIRLOG}/${BNSCR}'.log'
  # Backup previous log file if it exists
  if [ -f ${LOGFILE} ]; then
    datelog=`date +"%Y%m%d%H%M%S"`
    mv ${LOGFILE} ${LOGFILE}.${datelog}
  fi
  touch $LOGFILE
fi  

do_init_prm
do_check_tools grep awk sed cut sort xmllint curl unzip tr file mysql
do_log "STEP" "Arguments analysis"
do_log "INFO" "- Log file = ${LOGFILE}"
do_log "INFO" "- Server type = aio"
do_log "INFO" "- Forge release = $FRGVER"
do_log "INFO" "- Groovy scripts location = $GRVZIP"

# = Algorithm :
# Delete previous loaded groovy scripts 
# Retrieve all existing Nexus roles
# Recreate Anonymous Proxy role to match Nexus 3 privileges and new public repositories
# Anonymous user : assign Anonymous Proxy role and deassign Default anonymous role if it exists
# Retrieve all projects which host Nexus instance
# Per Nexus instance of the project
# - Per new repository type :
# -- Create repository, if not existing
# -- Create roles linked to the repository, if not existing
# - Retrieve all users linked to this Nexus instance
# - Per user - PASS 1
# -- Get user's existing Nexus roles
# -- Assign Anonymous Proxy role if not already assigned
# -- Assign Nexus roles of new repositories, accordindgly to user's existing Nexus roles for Maven repositories
# - Recreate existing Nexus Maven roles (to fix https://issues.sonatype.org/browse/NEXUS-12222) if not already recreated
# - Per user - PASS 2
# -- Get user's existing Nexus roles
# -- Reassign Nexus Maven roles, accordindgly to user's existing Nexus roles for Docker repository
# Delete loaded groovy scripts 

nbscripts=0
do_log "STEP" "Delete previous loaded Groovy scripts"
do_delete_loaded_scripts

do_log "STEP" "Retrieve all existing Nexus roles"
do_get_roles
  
do_log "STEP" "Recreate Anonymous Proxy role"
do_recreate_anonymous_proxy_role

do_log "STEP" "Manage roles assigned to Anonymous user"
do_manage_anonymous_user_roles

do_log "STEP" "Retrieve all projects which host Nexus instance"
do_get_projects

do_log "STEP" "Performing actions per project"
p=0
while [ $p -ne ${#tabProjectIds[*]} ]
do
  do_log "INFO" "- Project : "${tabProjectIds[$p]}
  do_log "INFO" "- Nexus instance : "${tabToolIds[$p]}
  do_log "STEP" "= Actions per new repository type"
  for repotype in $TYPREPOLST
  do
    do_log "INFO" "- Repository type : "$repotype
    for v in snapshot release
    do
      reponame=${tabToolIds[$p]}${repotype}"_"$v
      do_log "STEP" "== Create repository "$reponame
      do_create_repo $repotype $reponame
      do_log "STEP" "== Create linked nexus roles"
      for roletype in $TYPROLELST
      do
        do_log "INFO" "-- Role type : "$roletype
        do_create_role $repotype $reponame $roletype
      done
    done
  done
  
  do_log "STEP" "= Retrieve all users assigned to current Nexus instance"
  do_get_users ${tabToolIds[$p]}
  do_log "STEP" "= Performing actions per user - PASS 1"
  u=0
  while [ $u -ne ${#tabUserIds[*]} ]
  do
    do_log "INFO" "- User : "${tabUserIds[$u]}
    do_log "STEP" "== Get user existing nexus roles"
    do_get_user_roles ${tabUserIds[$u]}
    if [ $userexist -eq 1 ]; then
      do_log "STEP" "== Assign user new roles"
      do_add_user_new_roles ${tabUserIds[$u]} ${tabToolIds[$p]}
    fi
    let u+=1
  done
  do_log "STEP" "= Recreate Nexus Maven roles"
  do_recreate_maven_roles ${tabToolIds[$p]}
  do_log "STEP" "= Performing actions per user - PASS 2"
  u=0
  while [ $u -ne ${#tabUserIds[*]} ]
  do
    do_log "INFO" "- User : "${tabUserIds[$u]}
    do_log "STEP" "== Get user existing nexus roles"
    do_get_user_roles ${tabUserIds[$u]}
    if [ $userexist -eq 1 ]; then
      do_log "STEP" "== Reassign user Maven roles"
      do_add_user_maven_roles ${tabUserIds[$u]} ${tabToolIds[$p]}
    fi
    let u+=1
  done
  let p+=1
done
unset tabRoleIds 
unset tabProjectIds
unset tabToolIds 

do_log "STEP" "Delete loaded Groovy scripts"
do_delete_loaded_scripts
unset tabScripts

do_exit 0
