import groovy.json.JsonSlurper;
def repositoryNameParam = new JsonSlurper().parseText(args);
(repository.asType( org.sonatype.nexus.script.plugin.internal.provisioning.RepositoryApiImpl )).getRepositoryManager().exists(repositoryNameParam)