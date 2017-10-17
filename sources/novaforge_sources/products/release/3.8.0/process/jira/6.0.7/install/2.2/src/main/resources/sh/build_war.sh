# Build Jira War
pushd @jiraWarPath@ >/dev/null
export JIRA_HOME=@jiraHome@
export JAVA_HOME=@javaHome@
sh ./build.sh
popd >/dev/null
