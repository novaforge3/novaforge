/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService
import org.novaforge.beaver.exception.BeaverException
import java.nio.file.Path;
import java.nio.file.Paths;

BeaverEngine beaverEngine = engine
RepositoryService repositoryService = engine.getRepositoryService()

// Define Resources
def gitlabRPMHome = "/opt/gitlab"
def railsServiceDir = gitlabRPMHome + "/embedded/service/gitlab-rails"
def cookbookDir = gitlabRPMHome + "/embedded/cookbooks"
def recipesDir = cookbookDir + "/gitlab/recipes"
def defaultTemplatesDir = cookbookDir + "/gitlab/templates/default"
def gitlabRbDir = "/etc/gitlab"
def gitlabRbFile = gitlabRbDir + "/gitlab.rb"
def patchDir = processTmpPath+"/resources/patch/"
def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")

// Clean installation
repositoryService.removeRPMs("gitlab-7.3.1_omnibus")
beaverEngine.delete(gitlabRbFile)
beaverEngine.delete(gitlabRbDir)
beaverEngine.delete(gitlabRPMHome)
beaverEngine.delete(home)
// Delete Git users
try
{
	beaverEngine.executeCommand("userdel", "gitlab-www")
}
catch (final BeaverException e)
{
	// Nothing to do if a user was already deleted
}
try
{
	beaverEngine.executeCommand("userdel", "gitlab-redis")
}
catch (final BeaverException e)
{
	// Nothing to do if a user was already deleted
}
try
{
	beaverEngine.executeCommand("userdel", "gitlab-psql")
}
catch (final BeaverException e)
{
	// Nothing to do if a user was already deleted
}
try
{
	beaverEngine.executeCommand("userdel", "git")
}
catch (final BeaverException e)
{
	// Nothing to do if a user was already deleted
}


// Create directories
beaverEngine.createDirectory(datas)
beaverEngine.createDirectory(logs)
beaverEngine.createDirectory(gitlabRbDir)
beaverEngine.createSymlink(gitlabRbDir, home)

// Install RPM
repositoryService.installRPMs(dataFile, "gitlab-7.3.1_omnibus")

// Changing @DATAS token to forge datas path then Patch default.rb, gitlab-shell.rb 
// & bootstrap.rb files to delete occurences to /var/opt/gitlab
// default.rb File
def patchFile = patchDir + "default.rb.patch"
beaverEngine.replaceElement(patchFile, "@DATAS", datas);
beaverEngine.patch(patchFile, 0, recipesDir, true)
// gitlab-shell.rb File
patchFile = patchDir + "gitlab-shell.rb.patch"
beaverEngine.replaceElement(patchFile, "@DATAS", datas);
beaverEngine.patch(patchFile, 0, recipesDir, true)
// bootstrap.rb File
patchFile = patchDir + "bootstrap.rb.patch"
beaverEngine.replaceElement(patchFile, "@DATAS", datas);
beaverEngine.patch(patchFile, 0, recipesDir, true)

// Patch users.rb files : https://gitlab.com/gitlab-org/gitlab-ce/merge_requests/167/diffs
def railsApiDir = railsServiceDir + "/lib/api"
patchFile = patchDir + "users.rb.patch"
beaverEngine.patch(patchFile, 0, railsApiDir, true)

// Patch unicorn.rb and gitlab.yml ERB template file to allow relative URL configuration
patchFiles = [
  patchDir + "unicorn.rb.erb.patch",
  patchDir + "gitlab.yml.erb.patch"
]
patchFiles.each() {
  patchFileUnicorn -> beaverEngine.patch(patchFileUnicorn, 0, defaultTemplatesDir, true)
}

// Patch application.rb file
def railsConfigDir = railsServiceDir + "/config"
patchFile = patchDir + "application.rb.patch"
beaverEngine.patch(patchFile, 0, railsConfigDir, true)

// Update session_store.rb file
def railsInitDir = railsConfigDir + "/initializers"
patchFile = patchDir + "session_store.patch"
beaverEngine.patch(patchFile, 0, railsInitDir, true)

// Patch application_controller.rb to allow to open gitlab into an iframe
def railsControllersDir = railsServiceDir + "/app/controllers"
patchFile = patchDir + "application_controller.rb.patch"
beaverEngine.patch(patchFile, 0, railsControllersDir, true)
// Patch omniauth_callbacks_controller.rb to handle propertly context project
patchFile = patchDir + "omniauth_callbacks_controller.rb.patch"
beaverEngine.patch(patchFile, 0, railsControllersDir, true)

// Patch ability.rb file to disable some feature for master
def railsModelsDir = railsServiceDir + "/app/models"
patchFile = patchDir + "ability.rb.patch"
beaverEngine.patch(patchFile, 0, railsModelsDir, true)
// Patch users_group.rb file to disable changes notification
patchFile = patchDir + "users_group.rb.patch"
beaverEngine.patch(patchFile, 0, railsModelsDir, true)

// Patch edit.html.haml file to disable project admin for master
def viewsProjectsDir = railsServiceDir + "/app/views/projects"
patchFile = patchDir + "edit.html.haml.patch"
beaverEngine.patch(patchFile, 0, viewsProjectsDir, true)

// Patch show.html.haml file to disable profile edition for user
def viewsUsersDir = railsServiceDir + "/app/views/users"
patchFile = patchDir + "user_show.html.haml.patch"
beaverEngine.patch(patchFile, 0, viewsUsersDir, true)

// Patch show.html.haml file to disable profile edition for user
def viewsProfilesDir = railsServiceDir + "/app/views/profiles"
patchFile = patchDir + "profile_show.html.haml.patch"
beaverEngine.patch(patchFile, 0, viewsProfilesDir, true)

// Patch _profile.html.haml file to disable password and email for navigation bar
def viewsLayoutNavDir = railsServiceDir + "/app/views/layouts/nav"
patchFile = patchDir + "_profile.html.haml.patch"
beaverEngine.patch(patchFile, 0, viewsLayoutNavDir, true)

// Patch auth.rb file to hash password into sha1 before authentifcating user
def viewsLibGitlabNavDir = railsServiceDir + "/lib/gitlab"
patchFile = patchDir + "auth.rb.patch"
beaverEngine.patch(patchFile, 0, viewsLibGitlabNavDir, true)

// Create bundle repository
def bundleRepository = railsServiceDir + "/vendor/cache"
beaverEngine.createDirectory(bundleRepository);
def gemFiles = [
  processTmpPath + "/resources/gem/" + "addressable-2.3.5.gem",
  processTmpPath + "/resources/gem/" + "json-1.8.1.gem",
  processTmpPath + "/resources/gem/" + "omniauth-cas-1.1.0.gem",
  processTmpPath + "/resources/gem/" + "activerecord-session_store-0.1.0.gem"
]
gemFiles.each() {
  gemFile -> beaverEngine.copy(gemFile, bundleRepository)
}

// Update Gemfile file
patchFile = patchDir + "gemfile.patch"
beaverEngine.patch(patchFile, 0, railsServiceDir, true)
