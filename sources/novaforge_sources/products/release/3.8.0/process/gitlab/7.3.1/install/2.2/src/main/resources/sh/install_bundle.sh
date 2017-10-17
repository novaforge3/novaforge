# Set environnement to use bundle command
export PATH=/opt/gitlab/bin:/opt/gitlab/embedded/bin:$PATH
export RAILS_ENV=production
gitlab_user=git
export HOME=$(eval echo ~${gitlab_user})

# Install new gems
cd /opt/gitlab/embedded/service/gitlab-rails
bundle install --local
