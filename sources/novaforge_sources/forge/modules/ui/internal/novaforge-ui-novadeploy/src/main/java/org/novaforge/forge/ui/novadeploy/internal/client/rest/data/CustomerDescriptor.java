package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "descriptor")
public class CustomerDescriptor
{

	@Column(name = "ID")
	public long id;

	@Column(name = "PROJECT")
	public String project;

	@Column(name = "PARENT")
	public long parent;

	@Column(name = "VERSION")
	public int version;

	@Column(name = "NAME")
	public String name;

	@Transient
	@JsonProperty
	public CustomerDescriptor[] sons;

	@JsonIgnore
	public CustomerDescriptor[] getSons()
	{
		return sons;
	}

	public void setSons(List<CustomerDescriptor> sons)
	{

		this.sons = new CustomerDescriptor[sons.size()];
		sons.toArray(this.sons);
	}

	public long getId()
	{
		return id;
	}

	@XmlTransient
	public void setId(long id)
	{
		this.id = id;
	}

	public String getProject()
	{
		return project;
	}

	public void setProject(String project)
	{
		this.project = project;
	}

	public long getParent()
	{
		return parent;
	}

	@XmlTransient
	public void setParent(long parent)
	{
		this.parent = parent;
	}

	public int getVersion()
	{
		return version;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
