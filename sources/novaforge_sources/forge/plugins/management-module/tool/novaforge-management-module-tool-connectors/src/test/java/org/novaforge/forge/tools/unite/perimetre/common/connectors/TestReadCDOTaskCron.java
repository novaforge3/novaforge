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
package org.novaforge.forge.tools.unite.perimetre.common.connectors;

import org.novaforge.forge.tools.managementmodule.connectors.CDOPerimeterUnitConnector;
import org.novaforge.forge.tools.unite.perimetre.common.internal.connectors.CDOPerimeterUnitConnectorImpl;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestReadCDOTaskCron
{

	private static final Logger LOG = Logger.getLogger("TestReadCDOTaskCron");
	private SchedulerFactory		schedFact			= new org.quartz.impl.StdSchedulerFactory();
	private Scheduler					 sched;
	private String							host					 = "safranforgefille4";
	private String							port					 = "2036";
	private String							repository		 = "safran3";
	private String							projet				 = "javatest";
	private String							sytemeGraal		= "MonProjet";
	private String							cronExpression = "0 0/1 * * * ?";

	public TestReadCDOTaskCron()
	{
		super();
	}

	public static void main(final String args[])
	{
		try
		{
			TestReadCDOTaskCron trc = new TestReadCDOTaskCron();
			trc.testRead();
		}
		catch (Exception e)
		{
			LOG.log(Level.SEVERE, "Error in testReadCDOTaskCron", e);
		}
	}

	public void testRead()
	{
		try
		{

			if (sched == null)
			{
				// On recupere le scheduler et on le demarre si pas demarre
				sched = schedFact.getScheduler();

				// creation du joab quartz
				CDOPerimeterUnitConnector connector = new CDOPerimeterUnitConnectorImpl(repository, host, port);
				JobDataMap map = new JobDataMap();
				map.put("pCDOParameters", connector);
				map.put("this", this);
				JobDetail jobDetail = new JobDetail(repository + "_" + sytemeGraal + "_" + projet + "_Job",
						Scheduler.DEFAULT_GROUP, ImportRefScopeUnitJob.class);
				jobDetail.setJobDataMap(map);
				// on lance le job quartz
				java.lang.System.out.println("sched.start();");
				sched.start();
				// creation du trigger quartz
				CronTrigger trigger = new CronTrigger(repository + "_" + sytemeGraal + "_" + projet + "_trigger",
						Scheduler.DEFAULT_GROUP, cronExpression);
				java.lang.System.out.println("sched.scheduleJob(jobDetail, trigger) ;");
				sched.scheduleJob(jobDetail, trigger);
			}
			else
			{// pas d expression cron, on le supprime
				if (sched != null)
				{
					Trigger t = sched.getTrigger(repository + "_" + sytemeGraal + "_" + projet + "_trigger",
							Scheduler.DEFAULT_GROUP);
					if (t != null)
					{
						sched.unscheduleJob(repository + "_" + sytemeGraal + "_" + projet + "_trigger",
								Scheduler.DEFAULT_GROUP);
					}
					sched.shutdown();
				}
			}
		}
		catch (ParseException e)
		{
			LOG.log(Level.SEVERE, "Error in testReadCDOTaskCron", e);
		}
		catch (SchedulerException e)
		{
			LOG.log(Level.SEVERE, "Error in testReadCDOTaskCron", e);
		}
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(final String host)
	{
		this.host = host;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(final String port)
	{
		this.port = port;
	}

	public String getRepository()
	{
		return repository;
	}

	public void setRepository(final String repository)
	{
		this.repository = repository;
	}

	public String getProjet()
	{
		return projet;
	}

	public void setProjet(final String projet)
	{
		this.projet = projet;
	}

	public String getSytemeGraal()
	{
		return sytemeGraal;
	}

	public void setSytemeGraal(final String sytemeGraal)
	{
		this.sytemeGraal = sytemeGraal;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public void setCronExpression(final String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	@Override
	public String toString()
	{
		return "TestReadCDOTaskCron [host=" + host + ", port=" + port + ", repository=" + repository
				+ ", projet=" + projet + ", sytemeGraal=" + sytemeGraal + ", cronExpression=" + cronExpression + "]";
	}

}
