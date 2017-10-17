package org.novaforge.forge.plugins.gcl.nexus.client.internal.json;

import java.util.ArrayList;
import java.util.List;

public class GroupRepositoryParam {

	protected final static String DEFAULT_BLOBSTORE_NAME = "default";

	// The name of the new Repository
	protected String name;
	
	// The BlobStore the Repository should use
	protected List<String> members = new ArrayList<>();
	
	// The BlobStore the Repository should use
	protected String blobStoreName = DEFAULT_BLOBSTORE_NAME;

	public GroupRepositoryParam() {
		super();
	}
	
	public GroupRepositoryParam(String name) {
		super();
		this.name = name;
	}

	public GroupRepositoryParam(String name, String blobStoreName) {
		super();
		this.name = name;
		this.blobStoreName = blobStoreName;
	}
	
	public void addMember(String member){
		
		if(!this.members.contains(member)){
			
			this.members.add(member);
		}
	}
	
	public void removeMember(String member){
		
		if(this.members.contains(member)){
			
			this.members.remove(member);
		}
	}
	public String getName() {
		return name;
	}

	public List<String> getMembers() {
		return members;
	}

	public String getBlobStoreName() {
		return blobStoreName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public void setBlobStoreName(String blobStoreName) {
		this.blobStoreName = blobStoreName;
	}

	@Override
	public String toString() {
		return "GroupRepositoryParam [name=" + name + ", members=" + members + ", blobStoreName=" + blobStoreName + "]";
	}

}