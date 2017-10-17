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
package org.novaforge.forge.plugins.quality.sonar.internal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.NoResultException;

import org.novaforge.forge.core.plugins.categories.ecm.ECMServiceException;
import org.novaforge.forge.core.plugins.categories.quality.QualityCategoryService;
import org.novaforge.forge.core.plugins.categories.quality.QualityMeasureBean;
import org.novaforge.forge.core.plugins.categories.quality.QualityMetric;
import org.novaforge.forge.core.plugins.categories.quality.QualityResourceBean;
import org.novaforge.forge.core.plugins.categories.quality.QualityServiceException;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.categories.beans.QualityMeasureBeanImpl;
import org.novaforge.forge.plugins.categories.beans.QualityResourceBeanImpl;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarRestClient;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContext;
import org.novaforge.forge.plugins.quality.sonar.ws.SonarWSContextFactory;
import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine;
import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine.TimeMachineCell;
import org.novaforge.forge.plugins.quality.sonar.ws.models.TimeMachine.TimeMachineColumn;
import org.novaforge.forge.plugins.quality.sonar.ws.models.User;
import org.sonarqube.ws.WsComponents.Component;



/**
 * @author rols-p
 */
public class SonarCategoryServiceImpl extends AbstractPluginCategoryService implements QualityCategoryService {
	private static final String PROPERTY_FILE = "sonar";

	private final static String SONAR_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";
	/**
	 * SonarRestClient service injected by container.
	 */
	private SonarRestClient sonarRestClient;

	/**
	 * SonarWSContextFactory service injected by container
	 */
	private SonarWSContextFactory sonarWSContextFactory;
	/**
	 * PluginConfigurationService service injected by container.
	 */
	private PluginConfigurationService pluginConfigurationService;
	/**
	 * InstanceConfigurationDAO service injected by container.
	 */
	private InstanceConfigurationDAO instanceConfigurationDAO;

	@Override
	public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
			throws PluginServiceException {
		return getMessage(KEY_ACCESS_INFO, pLocale);
	}

	@Override
	protected String getPropertyFileName() {
		return PROPERTY_FILE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws QualityServiceException
	 */
	@Override
	public Map<QualityMetric, List<QualityMeasureBean>> getMeasures(final String pForgeId, final String pInstanceId,
			final String pCurrentUser, final String pResourceId, final Set<QualityMetric> pMetrics, final Date pFrom,
			final Date pTo) throws QualityServiceException {

		// Check the parameters
		if ((pForgeId == null) || (pForgeId.trim().length() == 0) || (pInstanceId == null)
				|| (pInstanceId.trim().length() == 0) || (pCurrentUser == null)
				|| (pCurrentUser.trim().length() == 0)) {
			throw new QualityServiceException(
					"One of the following mandatory parameters [forge_id, instance_id, current_user] is null.");
		}

		if ((pMetrics == null) || pMetrics.isEmpty()) {
			throw new QualityServiceException("The set of metrics used to return measures is null or empty.");
		}

		if ((pResourceId == null) || (pResourceId.trim().length() == 0)) {
			throw new QualityServiceException("The resource_id is null or empty.");
		}

		try {
			// Get the instanceConfiguration
			final InstanceConfiguration instanceConfiguration = instanceConfigurationDAO.findByInstanceId(pInstanceId);

			// Check if instance got is mapped to the correct forge id
			checkForgeId(pForgeId, instanceConfiguration);

			// Get the sonar context
			final SonarWSContext sonarWSContext = getSonarWSContext(instanceConfiguration);

			return toMap(sonarRestClient.getTimeMachine(sonarWSContext, pResourceId, toString(pMetrics), pFrom, pTo));

		} catch (final NoResultException e) {
			throw new QualityServiceException(String.format(
					"Unable to find any sonar plugin instanceConfiguration with [forge_id=%s, instance_id=%s]",
					pForgeId, pInstanceId), e);
		} catch (final PluginServiceException e) {
			throw new QualityServiceException(
					String.format("Unable to get sonar client configuration with [forge_id=%s, instance_id=%s]",
							pForgeId, pInstanceId),
					e);
		} catch (final Exception e) {
			throw new QualityServiceException(
					String.format("Unable to get Quality Measures with [forge_id=%s, instance_id=%s, login=%s]",
							pForgeId, pInstanceId, pCurrentUser),
					e);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws QualityServiceException
	 */
	@Override
	public List<QualityResourceBean> getResourcesByProject(final String pForgeId, final String pInstanceId,
			final String pCurrentUser) throws QualityServiceException {

		final List<QualityResourceBean> ret = new ArrayList<QualityResourceBean>();
		// Check the parameters
		if ((pForgeId == null) || (pForgeId.trim().length() == 0) || (pInstanceId == null)
				|| (pInstanceId.trim().length() == 0) || (pCurrentUser == null)
				|| (pCurrentUser.trim().length() == 0)) {
			throw new QualityServiceException(
					"One of the following mandatory parameters [forge_id, instance_id, current_user] is null.");
		}

		try {
			// Get the instanceConfiguration
			final InstanceConfiguration instanceConfiguration = instanceConfigurationDAO.findByInstanceId(pInstanceId);

			// Check if instance got is mapped to the correct forge id
			checkForgeId(pForgeId, instanceConfiguration);

			// Get the sonar context
			final SonarWSContext sonarWSContext = getSonarWSContext(instanceConfiguration);

			// Get the user's groups
			// Filter the groups by project
			// Filter the resources by group permission
			final String forgeProjectId = instanceConfiguration.getForgeProjectId();
			
			final User sonarUser = sonarRestClient.findUser(
					sonarWSContext, pCurrentUser);

			if(sonarUser != null) {
				
				final List<Component> components;
				
				// select projects matching the forgeProjectId
				components = sonarRestClient.getProjectComponents(sonarWSContext, forgeProjectId, pCurrentUser);
				
//				if(sonarUser.isSonarAdmin()) {
//					
//					// select all projects (any forgeProjectId)
//					components = sonarRestClient.getAllComponents(sonarWSContext);
//					
//				} else {
//					
//					// select projects matching the forgeProjectId
//					components = sonarRestClient.getProjectComponents(sonarWSContext, forgeProjectId, pCurrentUser);
//				}
				
				// convert sonar components to Novaforge portal objects
				if(components != null) {
					
					for (final Component component : components) {

						ret.add(new QualityResourceBeanImpl(
								component.getKey(), 
								component.getName()));
					}
				}
			}
		
		} catch (final NoResultException e) {
			throw new QualityServiceException(String.format(
					"Unable to find any sonar plugin instanceConfiguration with [forge_id=%s, instance_id=%s]",
					pForgeId, pInstanceId), e);
		} catch (final PluginServiceException e) {
			throw new QualityServiceException(
					String.format("Unable to get sonar client configuration with [forge_id=%s, instance_id=%s]",
							pForgeId, pInstanceId),
					e);
		} catch (final Exception e) {
			throw new QualityServiceException(
					String.format("Unable to get Quality resources with [forge_id=%s, instance_id=%s, login=%s]",
							pForgeId, pInstanceId, pCurrentUser),
					e);
		}
		
		return ret;
	}

	/**
	 * Return the web service context (URL base and connection with the sonar administrator credentials)
	 * @param pInstanceConfiguration
	 * @return
	 * @throws PluginServiceException
	 */
	private SonarWSContext getSonarWSContext(final InstanceConfiguration pInstanceConfiguration)
			throws PluginServiceException {
		final SonarWSContext sonarWSContext = sonarWSContextFactory.getWSContext(
				pluginConfigurationService.getClientURL(pInstanceConfiguration.getToolInstance().getBaseURL()),
				pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
		return sonarWSContext;
	}

	/**
	 * @param pForgeId
	 * @param instance
	 * @throws ECMServiceException
	 */
	private void checkForgeId(final String pForgeId, final InstanceConfiguration instance)
			throws QualityServiceException {
		if (instance != null) {
			if (!instance.getForgeId().equals(pForgeId)) {
				throw new QualityServiceException(
						"The forge id given as parameter doesn''t match with the instance id");
			}
		}
	}

	private Map<QualityMetric, List<QualityMeasureBean>> toMap(final TimeMachine timeMachine) throws QualityServiceException {
		final Map<QualityMetric, List<QualityMeasureBean>> map = new HashMap<QualityMetric, List<QualityMeasureBean>>();

		DateFormat dateFormat = new SimpleDateFormat(SONAR_DATE_PATTERN);
				
		final List<TimeMachineColumn> columns = timeMachine.getCols();
		final List<TimeMachineCell> cells = timeMachine.getCells();
		
		QualityMetric metric = null;
		QualityMeasureBean measure = null;
		
		
		
		try {
			
			int nbColumns = columns.size();
			
			for(int i=0; i<nbColumns; i++){
	
				metric = QualityMetric.fromLabel(
						columns.get(i).getMetric());
				
				final List<QualityMeasureBean> measures = new LinkedList<QualityMeasureBean>();
				
	
				for (final TimeMachineCell cell : cells) {
					measure = new QualityMeasureBeanImpl();
					measure.setDate(dateFormat.parse(cell.getD()));
					measure.setValue(cell.getV()[i]);
					measures.add(measure);
				}
	
				map.put(metric, measures);
			}

		} catch (ParseException e) {
			
			throw new QualityServiceException(e);
		}
		return map;
	}

	private List<String> toString(final Set<QualityMetric> pMetrics) {

		final List<String> ret = new ArrayList<String>();

		final Iterator<QualityMetric> it = pMetrics.iterator();

		while (it.hasNext()) {

			ret.add(it.next().getLabel());

		}
		return ret;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param pSonarRestClient
	 *            the sonarRestClient to set
	 */
	public void setSonarRestClient(final SonarRestClient pSonarRestClient) {
		sonarRestClient = pSonarRestClient;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param pPluginConfigurationService
	 *            the pluginConfigurationService to set
	 */
	public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService) {
		pluginConfigurationService = pPluginConfigurationService;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param pInstanceConfigurationDAO
	 *            the instanceConfigurationDAO to set
	 */
	public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO) {
		instanceConfigurationDAO = pInstanceConfigurationDAO;
	}

	/**
	 * Used by the OSGi container (Karaf ) to inject the instance (see the
	 * blueprint.xml file of novaforge-sonar-impl module)
	 * 
	 * @param sonarWSContextFactory
	 */
	public void setSonarWSContextFactory(SonarWSContextFactory sonarWSContextFactory) {
		this.sonarWSContextFactory = sonarWSContextFactory;
	}
}
