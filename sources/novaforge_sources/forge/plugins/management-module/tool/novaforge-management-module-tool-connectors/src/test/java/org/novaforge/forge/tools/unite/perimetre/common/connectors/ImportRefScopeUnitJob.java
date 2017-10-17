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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author pratmarty-p
 *
 * Class Job Quartz
 */
public class ImportRefScopeUnitJob implements Job {
	

   private static final Log LOG = LogFactory.getLog(ImportRefScopeUnitJob.class);
	   				
	   	@Override
         public void execute(final JobExecutionContext context) throws JobExecutionException {
	   		final JobDataMap map = context.getJobDetail().getJobDataMap();
	   		final TestReadCDOTaskCron test = (TestReadCDOTaskCron)map.get("this");
	   		LOG.info("t  :"+ test.toString() + ":"+new Date());
	   	}
}
