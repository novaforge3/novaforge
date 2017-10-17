package org.novaforge.forge.ui.novadeploy.internal.client.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;

public class OrgResources
{
	@JsonProperty
	private Integer storageLimitMB;
	@JsonProperty
	private Integer storageUsedMB;

	@JsonProperty
	private Long cpuLimit;
	@JsonProperty
	private Long cpuAllocated;
	@JsonProperty
	private Long cpuReserved;
	@JsonProperty
	private Long cpuOverhead;
	@JsonProperty
	private Long cpuUsed;

	@JsonProperty
	private Long memoryLimit;
	@JsonProperty
	private Long memoryAllocated;
	@JsonProperty
	private Long memoryReserved;
	@JsonProperty
	private Long memoryOverhead;
	@JsonProperty
	private Long memoryUsed;

	public Integer getStorageLimitMB()
	{
		return storageLimitMB;
	}

	public void setStorageLimitMB(Integer storageLimitMB)
	{
		this.storageLimitMB = storageLimitMB;
	}

	public Integer getStorageUsedMB()
	{
		return storageUsedMB;
	}

	public void setStorageUsedMB(Integer storageUsedMB)
	{
		this.storageUsedMB = storageUsedMB;
	}

	public Long getCpuLimit()
	{
		return cpuLimit;
	}

	public void setCpuLimit(Long cpuLimit)
	{
		this.cpuLimit = cpuLimit;
	}

	public Long getCpuAllocated()
	{
		return cpuAllocated;
	}

	public void setCpuAllocated(Long cpuAllocated)
	{
		this.cpuAllocated = cpuAllocated;
	}

	public Long getCpuReserved()
	{
		return cpuReserved;
	}

	public void setCpuReserved(Long cpuReserved)
	{
		this.cpuReserved = cpuReserved;
	}

	public Long getCpuOverhead()
	{
		return cpuOverhead;
	}

	public void setCpuOverhead(Long cpuOverhead)
	{
		this.cpuOverhead = cpuOverhead;
	}

	public Long getCpuUsed()
	{
		return cpuUsed;
	}

	public void setCpuUsed(Long cpuUsed)
	{
		this.cpuUsed = cpuUsed;
	}

	public Long getMemoryLimit()
	{
		return memoryLimit;
	}

	public void setMemoryLimit(Long memoryLimit)
	{
		this.memoryLimit = memoryLimit;
	}

	public Long getMemoryAllocated()
	{
		return memoryAllocated;
	}

	public void setMemoryAllocated(Long memoryAllocated)
	{
		this.memoryAllocated = memoryAllocated;
	}

	public Long getMemoryReserved()
	{
		return memoryReserved;
	}

	public void setMemoryReserved(Long memoryReserved)
	{
		this.memoryReserved = memoryReserved;
	}

	public Long getMemoryOverhead()
	{
		return memoryOverhead;
	}

	public void setMemoryOverhead(Long memoryOverhead)
	{
		this.memoryOverhead = memoryOverhead;
	}

	public Long getMemoryUsed()
	{
		return memoryUsed;
	}

	public void setMemoryUsed(Long memoryUsed)
	{
		this.memoryUsed = memoryUsed;
	}
}
