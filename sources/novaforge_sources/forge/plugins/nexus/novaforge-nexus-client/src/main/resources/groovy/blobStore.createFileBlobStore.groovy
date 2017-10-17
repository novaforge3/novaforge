import groovy.json.JsonSlurper;
def blobStoreParam = new JsonSlurper().parseText(args);
def repo=blobStore.createFileBlobStore(blobStoreParam.name,blobStoreParam.path);
return groovy.json.JsonOutput.toJson(repo)