#!/bin/bash

su @NOVAFORGE_USER@ -c "/usr/bin/ssh-keygen -q -t rsa -f /home/@NOVAFORGE_USER@/.ssh/id_rsa -N \"\""
su @NOVAFORGE_USER@ -c "/bin/touch /home/@NOVAFORGE_USER@/.ssh/authorized_keys"
su @NOVAFORGE_USER@ -c "/bin/chmod 600 /home/@NOVAFORGE_USER@/.ssh/authorized_keys"
su @NOVAFORGE_USER@ -c "echo \"StrictHostKeyChecking no\" > /home/@NOVAFORGE_USER@/.ssh/config"
su @NOVAFORGE_USER@ -c "/bin/chmod 600 /home/@NOVAFORGE_USER@/.ssh/config"

