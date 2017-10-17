# NovaForge
> NovaForge is an integrated collaborative development platform, covering all the life-cycle of Enterprise software development, from requirement definition to support and maintenance.

## Table of Contents

- [Introduction](#introduction)
- [Building](#building)
- [Installation](#installation)
- [Usage](#usage)
- [Licensing](#licensing)

## Introduction

NovaForge implements a software Forge offering access to Open Source tools in a full integrated way.

Currently, these tools are the following ones :

| Category                                | Products           |
| --------------------------------------- | ------------------ |
| Enterprise Content Management (ECM)     | Alfresco           |
| Software Configuration Management (SCM) | Subversion, GitLab |
| Bug Tracking tools                      | JIRA, Mantis       |
| Continuous Integration Platform         | Jenkins, SonarQube |
| Repository Management                   | Nexus              |
| Requirement Management                  | Testlink           |
| Mailing list Management                 | Sympa              |
| Wiki Software                           | DokuWiki           |
| Survey Management                       | Limesurvey         |
| Forum Software                          | phpBB              |
| Publishing System for Internet          | SPIP               |

Moreover, dedicated modules have been implemented to access to useful functionalities :

| Functionalities                           | Modules             |
| ----------------------------------------- | ------------------- |
| Manage the content of your deliveries     | Delivery Manager    |
| Track requirements through tests and code | Requirement Manager |
| Plan and monitor your project activities  | Management Module   |
| Publish news for all Project Users        | Articles            |

The current release of NovaForge is 3.8.0.

## Building

### Introduction
To be able to deploy your own Forge, you have first to build the installation package.

The following procedure describes how to get a package able to deploy a standard "Bull" profile (without COTS JIRA product for example) and standalone 'All-In-One' Forge.

### Set your environment
NovaForge currently only runs on Linux environment and more especially on CentOS 7.0 release (due to RPMs dependencies).

If you wish to have the same environment for building and deploying NovaForge, you have to fulfill the requirements specified in the Installation chapter.

The recommended setting needed for your building environment is the following one :
- Virtual Machine (Virtualbox with GuestAdditions for example) with 4GB RAM and 40GB of disk storage
- OS : CentOS 7.0 with only 'Infrastructure Server' package category installed
- Dedicated unix account set : user : `config`, group : `config`
  - You can create this account through these unix commands :
```sh
groupadd config
useradd -m config -g config
```
Create the `/datas` folder as `root` and provide ownership to `config` user :
```sh
mkdir /datas
chown config:config /datas
```
All following commands will have to be run with the `config` account. 

### Get the project
Since some open source tools integrated to NovaForge have an important size, several GitHub repositories are implemented :
- https://github.com/novaforge3/novaforge.git : the main project (this one)
- https://github.com/novaforge3-data1/novaforge-data1.git : distribution archives part 1 
- https://github.com/novaforge3-data2/novaforge-data2.git : distribution archives part 2 
- https://github.com/novaforge3-data3/novaforge-data3.git : distribution archives part 3 

To retrieve all these components, perform the following "git clone" commands :
```sh
cd /datas
git clone https://github.com/novaforge3/novaforge.git
git clone https://github.com/novaforge3-data1/novaforge-data1.git
git clone https://github.com/novaforge3-data2/novaforge-data2.git
git clone https://github.com/novaforge3-data3/novaforge-data3.git
```
If you're facing errors like "Failed connect to github.com:443; Connection timed out", it usually means you're accessing Internet through a web proxy.

In that case, set the `https_proxy` environment variable this way :
```sh
export https_proxy=https://<proxy-IP>:<proxy-port>/
```

Once all downloads are completed, you should have a folder tree like this one :
```sh
/datas/novaforge
/datas/novaforge-data1
/datas/novaforge-data2
/datas/novaforge-data3
```

### Prepare the environment
First, move the NovaForge main folders to their final location :
```sh
cd /datas
mv novaforge/* .
```

Perform the merge of the files previously split due to their important size :
```sh
./tools/joinFiles.sh /datas/novaforge-data1
```
You should have an output similar to this one :
```sh
[config@novapack datas]$ ./tools/joinFiles.sh /datas/novaforge-data1
[STEP] == Basic checks
[INFO] -- Target folder = /datas/novaforge-data1
[STEP] Retrieve split files
[STEP] Join split files
[INFO] -- File : /datas/novaforge-data1/novaforge_sources/products/release/3.8.0/data/gitlab/7.3.1/0/gitlab-distrib/src/main/resources/gitlab-7.3.1_omnibus-1.el7.x86_64.rpm
[INFO] - Performing merge
[INFO] - Checking merged file
[INFO] MD5 checksum matches ...
[INFO] Cleaning part files ...
[INFO] -- File : /datas/novaforge-data1/novaforge_sources/products/release/3.8.0/data/jira/6.0.7/0/jira-distrib/src/main/resources/binaries/atlassian-jira-6.0.7-war.tar.gz
[INFO] - Performing merge
[INFO] - Checking merged file
[INFO] MD5 checksum matches ...
[INFO] Cleaning part files ...
[INFO] Exiting.
```
Perform the same action to the `novaforge-data2` and `novaforge-data3` repositories :
```sh
./tools/joinFiles.sh /datas/novaforge-data2
./tools/joinFiles.sh /datas/novaforge-data3
```

Finally, move the `novaforge-data*` contents to their final location :
```sh
mv /datas/novaforge-data1/novaforge_sources/products/release/3.8.0/data/*  /datas/sources/novaforge_sources/products/release/3.8.0/data
mv /datas/novaforge-data2/novaforge_sources/products/release/3.8.0/data/*  /datas/sources/novaforge_sources/products/release/3.8.0/data
mv /datas/novaforge-data3/novaforge_sources/products/release/3.8.0/data/*  /datas/sources/novaforge_sources/products/release/3.8.0/data
```

Since the Git repositories are no longer needed for the further steps, they can be deleted, giving use the possibility to retrieve disk space :
```sh
rm -rf /datas/novaforge*
```

At the end, you should have a high-level content similar to this one :
```sh
[config@novapack /]$ find /datas -maxdepth 3 |sort'
/datas
/datas/README.md
/datas/sources
/datas/sources/novaforge_sources
/datas/sources/novaforge_sources/beaver
/datas/sources/novaforge_sources/forge
/datas/sources/novaforge_sources/parent
/datas/sources/novaforge_sources/products
/datas/sources/novaforge_sources/studio
/datas/sources/novaforge_thirdpartygpl
/datas/sources/novaforge_thirdpartygpl/COPYING
/datas/sources/novaforge_thirdpartygpl/plugins
/datas/sources/novaforge_thirdpartygpl/pom.xml
/datas/sources/novaforge_thirdpartygpl/.project
/datas/sources/novaforge_vaadinchartswidgets
/datas/sources/novaforge_vaadinchartswidgets/bugtracker
/datas/sources/novaforge_vaadinchartswidgets/pom.xml
/datas/sources/novaforge_vaadinchartswidgets/.project
/datas/sources/novaforge_vaadinchartswidgets/quality
/datas/tools
/datas/tools/genPackage.sh
/datas/tools/initRepo.tgz
/datas/tools/joinFiles.sh
/datas/tools/profiles
/datas/tools/profiles/Bull
```

### Initialize other needed folders
```sh
cd /datas
mkdir repoMaven tmp
```

### Deploy and configure Maven
```sh
mkdir /datas/tools/maven
cd !$ 
tar -zxvf /datas/sources/novaforge_sources/products/release/shared/bin/apache-maven-3.0.5-bin.tar.gz
```

In the `/datas/tools/maven/apache-maven-3.0.5/conf/settings.xml` file, 
- set the local Maven repository :
```sh
<localRepository>/datas/repoMaven</localRepository>
```
- if needed, provide the setting of a proxy to access to Internet :
```sh
    <proxy>
      <active>true</active>
      <protocol>http</protocol>
      <host>www.xxx.yyy.zzz</host>
      <port>nnn</port>
      <nonProxyHosts></nonProxyHosts>
    </proxy>
```

### Deploy JDK
```sh
mkdir /datas/tools/jdk
cd !$
tar -zxvf /datas/sources/novaforge_sources/products/release/3.8.0/data/jdk/7u80/0/jdk-distrib/src/main/resources/jdk-7u80-linux-x64.tar.gz
tar -zxvf /datas/sources/novaforge_sources/products/release/3.8.0/data/jdk/8u112/0/jdk-distrib/src/main/resources/jdk-8u112-linux-x64.tar.gz
```

### Initialize environment
Add the following lines at the end of the `config` user's `.bash_profile` file :
```sh
export JAVA_HOME=/datas/tools/jdk/jdk1.7.0_80
#export JAVA_HOME=/datas/tools/jdk/jdk1.8.0_112

export JAVA_OPTS="-Xms512M -Xmx512M -XX:PermSize=256M -XX:MaxPermSize=256M -XX:+CMSClassUnloadingEnabled  -XX:-UseSplitVerifier"
export M2_HOME=/datas/tools/maven/apache-maven-3.0.5
export MAVEN_OPTS="-Xms1024M -Xmx1024M -XX:PermSize=256M -XX:MaxPermSize=512M"
export M2_REPO=/datas/repoMaven

export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH
```
Source this file or perfom a reconnection to have this setting taken into account.

The JDK 1.7 should now be enabled :
```sh
[config@novapack /]$ java -version
java version "1.7.0_80"
Java(TM) SE Runtime Environment (build 1.7.0_80-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.80-b11, mixed mode)
```

### Initialize Maven repository
```sh
cd /datas/repoMaven
tar -zxvf /datas/tools/initRepo.tgz
```

### Build Maven artifacts
All NovaForge main components have to be built in a specific order :
```sh
cd /datas/sources/novaforge_sources/parent
mvn install
cd /datas/sources/novaforge_sources/forge
mvn install
cd /datas/sources/novaforge_vaadinchartswidgets
mvn install
cd /datas/sources/novaforge_thirdpartygpl
mvn install
cd /datas/sources/novaforge_sources/beaver
mvn install
cd /datas/sources/novaforge_sources/studio
mvn install
```
The `products` component needs to be built with JDK 1.8.

Modify your `.bash_profile` file to enable the line `export JAVA_HOME=/datas/tools/jdk/jdk1.8.0_112` and source the file.

The JDK 1.8 should now be enabled :
```sh
[config@novapack ~]$ java -version
java version "1.8.0_112"
Java(TM) SE Runtime Environment (build 1.8.0_112-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.112-b15, mixed mode)
```

```sh
cd /datas/sources/novaforge_sources/products/release
mvn install
```

### Build package
Update the `/datas/tools/maven/apache-maven-3.0.5/conf/settings.xml` file to :
- Add the following mirror :
```sh
    <mirror>
      <id>central</id>
      <mirrorOf>central</mirrorOf>
      <name>Mirror of Central Repository</name>
      <url>file:///datas/repoMaven</url>
    </mirror>
```
(This will force Maven to use the local repository as unique source for package building)
- Comment any `<proxy>...</proxy>` section.

Run the following command :
```sh
/datas/tools/genPackage.sh -x -c /datas/tools/profiles/Bull/3.8.0.package_install.cfg
```
The final package (`package-3.8.0-bull_install.tar.gz`) will be generated in a `/datas/tmp/bull_3.8.0.********` folder.

## Installation
All the following actions have to be performed as `root` user.

### Set your environment
As previously written, NovaForge currently only runs on Linux environment and more especially on CentOS 7.0 release.

The recommended setting needed for your running environment is the following one :
- Virtual Machine (Virtualbox with GuestAdditions for example) with 8GB RAM and 40GB of disk storage
- OS : CentOS 7.0 with only 'Infrastructure Server' package category installed

Additionnal configuration actions are needed :
- Disable the default Yum repository by adding the `enabled=0` line in the sections `[base]`, `[extras]` and `[updates]` in the `/etc/yum.repos.d/CentOS-Base.repo` file.
- Create a Yum repository for the Media source : file `/etc/yum.repos.d/CentOS-Media.repo` with content :
```sh
[c7-media]
name=CentOS-$releasever - Media
baseurl=file:///media/cdrom/
gpgcheck=1
enabled=0
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
```
- Load CentOS DVD (`CentOS-7.0-1406-x86_64-DVD.iso` for example)
- Enable CDROM device :
```sh
mkdir /media/cdrom
mount -t iso9660 /dev/cdrom /media/cdrom
```
- Install `createrepo` and `patch` components :
```sh
cd /media/cdrom/Packages
yum install --enablerepo=c7-media createrepo
yum install --enablerepo=c7-media patch
```
- Disable SELinux : in `/etc/sysconfig/selinux` file, replace `SELINUX=enforcing` by `SELINUX=disabled`
- Disable FireWalld : run the command `systemctl disable firewalld`
- Define the server hostname (`novatest` for example) : `hostnamectl set-hostname novatest` 

If you've set a Virtualbox VM with Network "NAT" mode, don't forget to configure ports redirection in order to have your VM accessible through SSH and HTTP/HTTPS protocols :
```sh
TCP 192.168.56.1 22   10.0.2.15 22
TCP 192.168.56.1 80   10.0.2.15 80
TCP 192.168.56.1 443  10.0.2.15 443
```

### Prepare NovaForge deployment
Create `/livraison` and `/datas` folders :
```sh
mkdir /livraison /datas
```
Retrieve and untar the recently built package (`package-3.8.0-bull_install.tar.gz`) :
```sh
cd /livraison
tar -zxvf package-3.8.0-bull_install.tar.gz
ln -s package-3.8.0-bull_install package
```

### Configure NovaForge deployment
In order to fit the Forge configuration to your requirements, a specific configuration file (`novaforge.cfg`) must be provided.

Its minimal content should match the following one (example for server hostname `novatest`) :
```sh
## Server configuration
local:ip=_your_serveur_IP_(10.0.2.15_for_example)_
local:host=novatest
local:home=/datas/novaforge3
local:user=novaforge
local:group=novaforge

## NovaForge configuration
main:novaforge-connector-distribution.urlDirectory=https://novatest
main:novaforge-connector-distribution.level=0
## end configuration
```

If you intend to use a mailserver, you can set its integration with these parameters :
```sh
main:smtp.host=_your_mail_server_hostname_
main:smtp.port=_your_mail_server_port_(25_for_example)_
main:smtp.noReply=_Email_address_for_No_Reply_mails_
main:novaforge-connector-forge.adminEmail=_Email_address_for_Forge_Administrator_
```

### Deploy NovaForge
Run the following command :
```sh
/livraison/package/install.sh -v -t aio -i aio -p bull -c /livraison/novaforge.cfg -l /livraison/package/repository -o > /livraison/install_3.8.0.log 2>&1 &
```
You can monitor the component installation by running the following command :
```sh
tail -f install_3.8.0.log | grep Processing
```
Example of output :
```sh
[root@novatest livraison]# tail -f install_3.8.0.log | grep Processing
06/10/2017 10:43:22 [INFO] - Processing server <server-aio>
06/10/2017 10:43:26 [INFO] - Processing product <httpd-2.4.6-40>
06/10/2017 10:43:33 [INFO] - Processing product <certificat-1.0>
06/10/2017 10:43:40 [INFO] - Processing product <httpd_ssl-2.4.6-40>
06/10/2017 10:43:47 [INFO] - Processing product <mariadb-10.0.17>
06/10/2017 10:44:27 [INFO] - Processing product <jre-7u80>
06/10/2017 10:44:30 [INFO] - Processing product <cas-3.5.0>
06/10/2017 10:44:39 [INFO] - Processing product <smtp-1.0>
06/10/2017 10:44:39 [INFO] - Processing product <php-5.4.16-36>
...
06/10/2017 10:57:18 [INFO] - Processing product <gitlab-7.3.1>
06/10/2017 11:00:38 [INFO] - Processing product <novaforge_svn-3.8.0>
06/10/2017 11:00:40 [INFO] - Processing product <novaforge_aio-3.8.0>
```

The final installed component is `novaforge_aio-3.8.0`.

The log file should end with lines similar to :
```sh
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 17:25.195s
[INFO] Finished at: Fri Oct 06 11:00:41 CEST 2017
[INFO] Final Memory: 39M/118M
[INFO] ------------------------------------------------------------------------
[WARNING] The requested profile "bull" could not be activated because it does not exist.
---------------------------------------------------------------------------
----- Clean working directory
################################################################################
######
###### End of installation
######
################################################################################
```

### Start/stop NovaForge
The NovaForge start is to be performed through this `systemctl` command : `systemctl start novaforge`.

The log file related to the Karaf container can be found in the `/datas/novaforge3/logs/karaf` folder.

You can consider NovaForge as available when the message "NovaForge Initialization: FINISHED SUCCESSFULLY" appears in the log file :
```sh
[root@novatest karaf]# tail -f karaf.log | grep SUCCESS
2017-10-06 11:13:12,604 | INFO  | lixDispatchQueue | RoleHandler                      | 402 - novaforge-requirements-tool-common-impl - 3.8.0 | Requirements Manager Initialization : FINISHED SUCCESSFULLY.
2017-10-06 11:13:24,656 | INFO  | lixDispatchQueue | RoleHandler                      | 425 - novaforge-delivery-manager-tool-impl - 3.8.0 | Delivery Manager Initialization : FINISHED SUCCESSFULLY.
2017-10-06 11:13:45,543 | INFO  | lixDispatchQueue | ForgeInitializator               | 503 - novaforge-initialization - 3.8.0 | NovaForge Initialization: FINISHED SUCCESSFULLY.
```

Regarding the shutdown, each component must be stopped individually.

So, to stop the whole Forge, run this command :
```sh
systemctl stop mysql httpd sendmail sympa nexus jenkins sonar gitlab cas novaforge
```
(Running the `systemctl stop novaforge` command will only stop the Karaf container.)

## Usage
Through your Internet browser, access to the URL `https://novatest`.

The main page will allow you to :
- access to NovaForge with an existing account,
- create a new account and enter NovaForge with it.

The NovaForge "Super User" account is referred as the "Forge Administrator".

By default, the associated login/password is `admin1/novaforge_1`.

To have more information regarding NovaForge functionnalities, you can browse the online help available from the drop-down list accessible by clicking on the "Firstname Lastname" text at the upper right part of the screen. 

## Licensing

```sh
Copyright (c) 2011-2017, BULL, an Atos Company.
NovaForge Version 3 and above.

This Program is free software: you may redistribute and/or modify it under
the terms of the GNU Affero General Public License as published by the
Free Software Foundation, version 3 of the License.

This Program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU Affero General Public License for more details (http://www.gnu.org/licenses).

Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.

If you modify this Program, or any covered work, by linking or combining
it with libraries listed in COPYRIGHT file at the top-level directory of
this distribution (or a modified version of that libraries), containing parts
covered by the terms of licenses cited in the COPYRIGHT file, the licensors
of this Program grant you additional permission to convey the resulting work.
```
