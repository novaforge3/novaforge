# Changing directory for bin
user['home'] = "@THIRDPARTY@" 
postgresql['dir'] = "@THIRDPARTY@/postgresql"
postgresql['home'] = "@THIRDPARTY@/postgresql"
postgresql['data_dir'] = "@THIRDPARTY@/postgresql/data"
unicorn['socket'] = "@THIRDPARTY@/gitlab-rails/sockets/gitlab.socket"
redis['dir'] = "@THIRDPARTY@/redis"
redis['home'] = "@THIRDPARTY@/redis"
redis['unixsocket'] = "@THIRDPARTY@/redis/redis.socket"
gitlab_rails['redis_socket'] = "@THIRDPARTY@/redis/redis.socket"
nginx['dir'] = "@THIRDPARTY@/nginx"
gitlab_shell['git_data_directory'] = "@THIRDPARTY@/git-data"

# Change the external_url to the address your users will type in their browser
external_url '@BASE_URL@'
gitlab_rails['gitlab_relative_url_root'] = '/@DEFAULT_ALIAS@/@TOOL_ALIAS@'
gitlab_rails['internal_api_url'] = "http://127.0.0.1:8080/@DEFAULT_ALIAS@/@TOOL_ALIAS@"
gitlab_rails['gitlab_shell_ssh_port'] = @SSH_PORT@

# Set datas directories
git_data_dir "@DATAS@"
gitlab_rails['uploads_directory'] = "@DATAS@/uploads"
gitlab_rails['backup_path'] = "@DATAS@/backups"
gitlab_rails['dir'] = "@DATAS@/gitlab-rails"
gitlab_rails['satellites_path'] = "@DATAS@/git-data/gitlab-satellites"
gitlab_rails['gitlab_shell_repos_path'] = "@DATAS@/git-data/repositories"

# Disable nginx
nginx['enable'] = false

# Disable sign-in form
gitlab_rails['gitlab_signin_enabled'] = false
gitlab_rails['gitlab_username_changing_enabled'] = false

# Allow www-data and apache users to use gitlab
web_server['external_users'] = ['@USER@', 'apache']

# Lower the amount of shared memory
postgresql['shared_buffers'] = "100MB"

# Smtp settings
gitlab_rails['smtp_enable'] = true  
gitlab_rails['smtp_address'] = "@SMTP_HOST@"  
gitlab_rails['smtp_port'] = @SMTP_PORT@  
gitlab_rails['smtp_user_name'] = "@SMTP_USER@"  
gitlab_rails['smtp_password'] = "@SMTP_PWD@"
gitlab_rails['smtp_authentication'] = "login"  
gitlab_rails['smtp_enable_starttls_auto'] = true

# Set e-mail
gitlab_rails['gitlab_email_from'] = '@E_MAIL@'

# Enable CAS
gitlab_rails['omniauth_enabled'] = true
gitlab_rails['omniauth_allow_single_sign_on'] = false
gitlab_rails['omniauth_block_auto_created_users'] = false
gitlab_rails['omniauth_providers'] = [
  {
    "name" => "cas",
    "args" => { "host" => "@MAIN_HOST@", "path" => "/cas", "disable_ssl_verification" => true }
  }
]

# Configure logs
gitlab_rails['log_directory']  = "@LOGS_DIR@/gitlab-rails"
unicorn['log_directory']  = "@LOGS_DIR@/unicorn"
sidekiq['log_directory']  = "@LOGS_DIR@/sidekiq"
gitlab_shell['log_directory']  = "@LOGS_DIR@/gitlab-shell"
postgresql['log_directory']  = "@LOGS_DIR@/postgresql"
redis['log_directory']  = "@LOGS_DIR@/redis"
remote_syslog['log_directory']  = "@LOGS_DIR@/remote-syslog"
