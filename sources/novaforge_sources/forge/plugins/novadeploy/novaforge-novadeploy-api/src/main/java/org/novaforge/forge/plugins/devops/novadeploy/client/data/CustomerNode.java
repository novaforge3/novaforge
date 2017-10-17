package org.novaforge.forge.plugins.devops.novadeploy.client.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonRootName(value = "node")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Entity
@Table(name = "NODES")
public class CustomerNode
{
	@XmlElement
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "NODENAME")
	@JsonProperty
	private String nodeName;

	@Column(name = "CLOUDID")
	private String cloudID;

	@Column(name = "IP")
	@JsonProperty
	private String ip;

	@Column(name = "DNS")
	@JsonProperty
	private String dns;

	@Column(name = "archive")
	@JsonProperty
	private String archive;

	public String getNodeName()
	{
		return nodeName;
	}

	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}

	public String getCloudID()
	{
		return cloudID;
	}

	public void setCloudID(String cloudID)
	{
		this.cloudID = cloudID;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public void setDNS(String dns)
	{
		this.dns = dns;
	}

	public String getDNS()
	{
		return this.dns;
	}

	public String getArchive()
	{
		return archive;
	}

	public void setArchive(String archive)
	{
		this.archive = archive;
	}

}
