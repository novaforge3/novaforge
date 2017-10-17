package org.novaforge.forge.plugins.gcl.nexus.domain;

import org.sonatype.nexus.repository.maven.VersionPolicy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Cf. http://www.baeldung.com/jackson-inheritance
 * http://www.davismol.net/2015/03/05/jackson-json-deserialize-a-list-of-objects-of-subclasses-of-an-abstract-class/
 * @author s241664
 *
 */
@JsonTypeInfo(
		  use = JsonTypeInfo.Id.NAME, 
		  include = JsonTypeInfo.As.PROPERTY, 
		  property = "type")
		@JsonSubTypes({ 
		  @Type(value = GroupRepository.class, name = "group"), 
		  @Type(value = HostedRepository.class, name = "hosted"),
		  @Type(value = ProxyRepository.class, name = "proxy") 
		})
public abstract class Repository {
	
	protected RepositoryFormat format;
	
	protected String name;
	
	protected String url;
	
	protected VersionPolicy versionPolicy;
	
	public Repository() {
		super();
	}

	public Repository(String format, String name, String url, String versionPolicy) {
		super();
		
		this.name = name;
		this.url = url;
		this.setFormat(format);
		this.setVersionPolicy(versionPolicy);
	}
	
	public Repository(RepositoryFormat format, String name, String url, VersionPolicy versionPolicy) {
		super();
		this.format = format;
		this.name = name;
		this.url = url;
		this.versionPolicy = versionPolicy;
	}
	
	public Repository(String format, String name, String url) {
		super();
		this.setFormat(format);
		this.name = name;
		this.url = url;
	}
	
	public Repository(RepositoryFormat format, String name) {
		super();
		this.format = format;
		this.name = name;
	}

	public RepositoryFormat getFormat() {
		return format;
	}

	public void setFormat(String format) {
		
		if(format != null){
			this.format = RepositoryFormat.getFormat(format);
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		
		String ret;
		
		if(versionPolicy == null){
			
			ret = "Repository [format=" + format + ", name=" + name + ", url=" + url + "]";
		} else {
			
			ret = "Repository [format=" + format + ", name=" + name + ", url=" + url + ", versionPolicy=" + versionPolicy+ "]";
		}
		return ret;
	}

	public String getVersionPolicy() {
		return versionPolicy.toString();
	}

	public void setVersionPolicy(String versionPolicy) {
		
		if(versionPolicy != null){
		
			this.versionPolicy = VersionPolicy.valueOf(versionPolicy);
		}
	}
	
	public abstract String getType();
}
