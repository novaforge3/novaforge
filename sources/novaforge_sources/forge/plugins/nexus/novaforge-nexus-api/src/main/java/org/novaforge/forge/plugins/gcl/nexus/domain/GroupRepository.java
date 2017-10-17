package org.novaforge.forge.plugins.gcl.nexus.domain;

import java.util.List;

import org.sonatype.nexus.repository.maven.VersionPolicy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupRepository extends Repository {
	
	public final static String TYPE = "group";

	public GroupRepository() {
		super();
	}

	public GroupRepository(RepositoryFormat format, String name, String url, VersionPolicy versionPolicy) {
		super(format, name, url, versionPolicy);
	}
	
	public GroupRepository(RepositoryFormat format, String name) {
		super(format, name);
	}

	public GroupRepository(String format, String name, String url, String versionPolicy) {
		super(format, name, url, versionPolicy);
	}
	
	public GroupRepository(String format, String name, String url) {
		super(format, name, url);
	}

	public GroupRepository(List<String> members) {
		super();
		this.members = members;
	}

	@JsonCreator
	public GroupRepository(@JsonProperty("format")String format, @JsonProperty("name")String name, @JsonProperty("url")String url, @JsonProperty("versionPolicy")String versionPolicy, @JsonProperty("members")List<String> members) {
		super(format, name, url, versionPolicy);
		this.members = members;
	}
	
	private List<String> members;

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	@Override
	public String getType() {
	
		return TYPE;
	}

	@Override
	public String toString() {
		
		String strVersionPolicy = "null";
		String strUrl = "null";
		
		if(versionPolicy != null){
			
			strVersionPolicy = this.versionPolicy.toString();
		}
		
		if(url != null){
			
			strUrl = this.url;
		}
		
		return "GroupRepository [format=" + format + ", name=" + name + ", url=" + strUrl + ", versionPolicy=" + strVersionPolicy + ", members=" + members + "]";
	}
}
