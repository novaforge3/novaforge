# Set environnement to use bundle command
export PATH=/opt/gitlab/bin:/opt/gitlab/embedded/bin:$PATH
export RAILS_ENV=production
gitlab_user=git
export HOME=$(eval echo ~${gitlab_user})

# Precompile assets
cd /opt/gitlab/embedded/service/gitlab-rails
bundle exec rake assets:precompile RAILS_ENV=production

# With the user gitlab-psql set root private token database, password is KyPm7qzcf4m1pf9rp1wd encrypted
su gitlab-psql -c "/opt/gitlab/embedded/bin/psql -d gitlabhq_production < @CHANGE_PASSWORD_FILE_PATH@"

# Create Session table and migrate data
bundle exec rails generate active_record:session_migration
gitlab-rake db:migrate
