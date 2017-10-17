package org.novaforge.forge.plugins.gcl.nexus.domain;

import org.sonatype.nexus.repository.maven.VersionPolicy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HostedRepository extends Repository {

	public final static String TYPE = "hosted";
	
	public HostedRepository() {
		super();
	}

	public HostedRepository(RepositoryFormat format, String name, String url, VersionPolicy versionPolicy) {
		super(format, name, url, versionPolicy);
	}

	@JsonCreator
	public HostedRepository(@JsonProperty("format")String format, @JsonProperty("name")String name, @JsonProperty("url")String url, @JsonProperty("versionPolicy")String versionPolicy) {
		super(format, name, url, versionPolicy);
	}

	@Override
	public String getType() {
	
		return TYPE;
	}

	@Override
	public String toString() {
		
		String ret;
		
		if(versionPolicy == null){
			
			ret = "HostedRepository [format=" + format + ", name=" + name + ", url=" + url + "]";
		} else {
			
			ret = "HostedRepository [format=" + format + ", name=" + name + ", url=" + url + ", versionPolicy=" + versionPolicy + "]";
		}
		return ret;
	}
}
