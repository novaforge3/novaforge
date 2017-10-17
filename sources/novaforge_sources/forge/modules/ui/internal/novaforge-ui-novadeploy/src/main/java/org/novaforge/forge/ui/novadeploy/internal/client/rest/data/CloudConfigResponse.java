package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@SuppressWarnings("serial")
@XmlRootElement(name = "response")
public class CloudConfigResponse extends ServiceResponse implements Serializable
{
	@JsonSerialize
	public String[] network;
	@XmlElement
	public String[] vdc;

	public String[] getVdc()
	{
		return vdc;
	}

	public String[] getNetwork()
	{
		return network;
	}

	public void setNetwork(List<String> network)
	{
		this.network = new String[network.size()];
		network.toArray(this.network);
	}

	public void setVdc(List<String> vdc)
	{
		this.vdc = new String[vdc.size()];
		vdc.toArray(this.vdc);
	}

}
