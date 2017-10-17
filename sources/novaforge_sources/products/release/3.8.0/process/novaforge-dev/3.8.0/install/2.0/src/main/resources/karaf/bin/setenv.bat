@echo off
rem
rem
rem    Licensed to the Apache Software Foundation (ASF) under one or more
rem    contributor license agreements.  See the NOTICE file distributed with
rem    this work for additional information regarding copyright ownership.
rem    The ASF licenses this file to You under the Apache License, Version 2.0
rem    (the "License"); you may not use this file except in compliance with
rem    the License.  You may obtain a copy of the License at
rem
rem       http://www.apache.org/licenses/LICENSE-2.0
rem
rem    Unless required by applicable law or agreed to in writing, software
rem    distributed under the License is distributed on an "AS IS" BASIS,
rem    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem    See the License for the specific language governing permissions and
rem    limitations under the License.
rem

rem
rem handle specific scripts; the SCRIPT_NAME is exactly the name of the Karaf
rem script; for example karaf.bat, start.bat, stop.bat, admin.bat, client.bat, ...
rem
rem if "%KARAF_SCRIPT%" == "SCRIPT_NAME" (
rem   Actions go here...
rem )

rem
rem general settings which should be applied for all scripts go here; please keep
rem in mind that it is possible that scripts might be executed more than once, e.g.
rem in example of the start script where the start script is executed first and the
rem karaf script afterwards.
rem

rem
rem The following section shows the possible configuration options for the default 
rem karaf scripts
rem
rem Window name of the windows console
rem SET KARAF_TITLE
rem Location of Java installation
SET JAVA_HOME=@JAVA_HOME@
rem Minimum memory for the JVM
SET JAVA_MIN_MEM=512m
rem Maximum memory for the JVM
SET JAVA_MAX_MEM=1024m
rem Minimum perm memory for the JVM
rem SET JAVA_PERM_MEM
rem Maximum perm memory for the JVM
SET JAVA_MAX_PERM_MEM=512m
rem Karaf home folder
rem SET KARAF_HOME
rem Karaf data folder
SET KARAF_DATA=@KARAF_DATA@
rem Karaf base folder
rem SET KARAF_BASE
rem Karaf etc folder
rem SET KARAF_ETC
rem Additional available Karaf options
SET KARAF_OPTS"-Djavax.net.ssl.keyStore=@KEYSTORE@ -Djavax.net.ssl.keyStorePassword=@STORE_PASS@ -Djavax.net.ssl.trustStore=@KEYSTORE@ -Djavax.net.ssl.trustStorePassword=@STORE_PASS@ -Djavax.net.ssl.keyStore=@KEYSTORE@ -Djavax.net.ssl.keyStorePassword=@STORE_PASS@ -Djavax.net.ssl.trustStore=@KEYSTORE@ -Djavax.net.ssl.trustStorePassword=@STORE_PASS@ -XX:-UseSplitVerifier -Djava.io.tmpdir=@KARAF_TMP@"
rem Enable debug mode
rem SET KARAF_DEBUG

