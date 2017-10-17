/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;


public class CDOParametersDTO implements Serializable{

	private static final long serialVersionUID = 631522320939028667L;
	
	private long			  cDOParametersID;

	private String host;	
	
	private Integer port;
	
	private String repository;
	
	private String projetCdo;
	
	private String systemGraal;
	
	private String cronExpression;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getProjetCdo() {
		return projetCdo;
	}

	public void setProjetCdo(String projetCdo) {
		this.projetCdo = projetCdo;
	}

	public String getSystemGraal() {
		return systemGraal;
	}

	public void setSystemGraal(String systemGraal) {
		this.systemGraal = systemGraal;
	}
	

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public long getcDOParametersID()
	{
		return cDOParametersID;
	}

	public void setcDOParametersID(long cDOParametersID) {
		this.cDOParametersID = cDOParametersID;
	}
	
//	@Override
//	public String toString() {
//		return "CDOParametersDTO [host=" + host + ", port=" + port
//				+ ", repository=" + repository + ", projetCdo=" + projetCdo
//				+ ", systemGraal=" + systemGraal + "]";
//	}
	
	

}
