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

package org.novaforge.forge.tools.unite.perimetre.common.internal.connectors;

import fr.gouv.mindef.safran.graalextensions.GraalExtensionsPackage;
import fr.gouv.mindef.safran.graalextensions.Risk;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.EresourcePackage;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.om.OMPlatform;
import org.novaforge.forge.tools.managementmodule.connectors.CDOPerimeterUnitConnector;
import org.novaforge.forge.tools.managementmodule.exceptions.CDOPerimeterUnitConnectorException;
import org.obeonetwork.dsl.environment.EnvironmentPackage;
import org.obeonetwork.dsl.environment.MetaData;
import org.obeonetwork.dsl.environment.MetaDataContainer;
import org.obeonetwork.graal.AbstractTask;
import org.obeonetwork.graal.Activity;
import org.obeonetwork.graal.GraalPackage;
import org.obeonetwork.graal.Loop;
import org.obeonetwork.graal.NamedElement;
import org.obeonetwork.graal.System;
import org.obeonetwork.graal.Task;
import org.obeonetwork.graal.UseCase;
import org.obeonetwork.graal.UserStory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PRATMARTY-P
 */
public class CDOPerimeterUnitConnectorImpl implements CDOPerimeterUnitConnector
{
	private static final String		 TASK						= "task";

	private static final String		 VERSION				 = "Version";

	private static final String		 ID							= "id";

	private static final String		 DESCRIPTION		 = "description";

	private static final String		 NAME						= "name";

	private static final String		 DRAWBACKS_LEVEL = "DrawbacksLevel";

	private static final String		 BENEFITS_LEVEL	= "BenefitsLevel";

	private static final String		 RISK_LEVEL			= "RiskLevel";

	private static final String		 MODIFIED_ON		 = "ModifiedON";

	private static final String		 CREATED_ON			= "CreatedON";

	private static final String		 EMPTY_STRING		= "";

	private static final Log				LOG						 = LogFactory.getLog(CDOPerimeterUnitConnectorImpl.class);

	static
	{
		if (!OMPlatform.INSTANCE.isOSGiRunning())
		{
			Net4jUtil.prepareContainer(IPluginContainer.INSTANCE);
			TCPUtil.prepareContainer(IPluginContainer.INSTANCE);
			CDONet4jUtil.prepareContainer(IPluginContainer.INSTANCE);

		}
	}

	private String                  repository;
	private String                  host;
	private String                  port;
	private CDOSessionConfiguration config;
	private CDOSession              instanceSession;
	private IConnector              connector;
	private CDOView                 view;

	// private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG,
	// CDOPerimeterUnitConnectorAPI.class);

	public CDOPerimeterUnitConnectorImpl(final String repository, final String host, final String port)
	{
		super();
		this.repository = repository;
		this.host = host;
		this.port = port;
		IPluginContainer.INSTANCE.clearElements();
	}

	public CDOPerimeterUnitConnectorImpl()
	{
		super();
		IPluginContainer.INSTANCE.clearElements();
	}

	public void starting()
	{
		LOG.info("start...");
	}

	public void stopping()
	{
		LOG.info("stop...");
		if (instanceSession != null && !instanceSession.isClosed())
		{
			instanceSession.close();
		}
	}

	private Element buildTask(final Task t, final Document doc)
	{
		final Element task = doc.createElement(TASK);
		task.setAttribute(NAME, t.getName());
		task.setAttribute(DESCRIPTION, t.getDescription());
		task.setAttribute(ID, t.eResource().getURIFragment(t));
		task.setAttribute(VERSION, t.getVersion() + EMPTY_STRING);
		final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		if (t.getCreatedOn() == null)
		{
			task.setAttribute(CREATED_ON, formatter.format(new Date()));
		}
		else
		{
			task.setAttribute(CREATED_ON, formatter.format(t.getCreatedOn()));
		}
		if (t.getModifiedOn() == null)
		{
			task.setAttribute(MODIFIED_ON, formatter.format(new Date()));
		}
		else
		{
			task.setAttribute(MODIFIED_ON, formatter.format(t.getModifiedOn()));
		}
		task.setAttribute(RISK_LEVEL, this.getRiskUnitPerimeter(t));
		task.setAttribute(BENEFITS_LEVEL, this.getBenefitsUnitPerimeter(t));
		task.setAttribute(DRAWBACKS_LEVEL, this.getDrawbacksUnitPerimeter(t));
		return task;
	}

	private Element buildUserStory(final UserStory us, final Document doc)
	{
		final Element userStory = doc.createElement("userStory");
		userStory.setAttribute(NAME, us.getName());
		userStory.setAttribute(DESCRIPTION, us.getDescription());
		userStory.setAttribute(ID, us.eResource().getURIFragment(us));
		userStory.setAttribute(VERSION, us.getVersion() + EMPTY_STRING);
		final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		if (us.getCreatedOn() == null)
		{
			userStory.setAttribute(CREATED_ON, formatter.format(new Date()));
		}
		else
		{
			userStory.setAttribute(CREATED_ON, formatter.format(us.getCreatedOn()));
		}
		if (us.getModifiedOn() == null)
		{
			userStory.setAttribute(MODIFIED_ON, formatter.format(new Date()));
		}
		else
		{
			userStory.setAttribute(MODIFIED_ON, formatter.format(us.getModifiedOn()));
		}
		userStory.setAttribute(RISK_LEVEL, this.getRiskUnitPerimeter(us));
		userStory.setAttribute(BENEFITS_LEVEL, this.getBenefitsUnitPerimeter(us));
		userStory.setAttribute(DRAWBACKS_LEVEL, this.getDrawbacksUnitPerimeter(us));
		return userStory;
	}

	private Element addChild(final Element task, final Task point, final Document doc)
	{
		final EList<Loop> listNode = point.getSubActivities();
		for (Loop n : listNode)
		{
			if (n instanceof Task)
			{
				final Task t = (Task) n;
				Element taskchild = buildTask(t, doc);
				taskchild = addChild(taskchild, t, doc);
				task.appendChild(taskchild);
			}
		}
		return task;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.tools.unite.perimetre.common.internal.connectors.ICDOPerimeterUnitConnector#importTasks
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public List<org.w3c.dom.Element> importTasks(final String pSystemeGraal, final String pProject)
			throws CDOPerimeterUnitConnectorException
	{
		final List<org.w3c.dom.Element> retour = new ArrayList<org.w3c.dom.Element>();
		// openVu();
		try
		{
			final DocumentBuilderFactory factorydoc = DocumentBuilderFactory.newInstance();
			final Document doc = factorydoc.newDocumentBuilder().newDocument();
			final System systemeGraal = this.getSystemGraal(pSystemeGraal, pProject);
			final EList<Task> listTask = systemeGraal.getOwnedTasks();
			for (Task t : listTask)
			{
				Element task = buildTask(t, doc);
				task = addChild(task, t, doc);
				retour.add(task);
			}

		}
		catch (ParserConfigurationException e)
		{
			LOG.error("importTasks Error CDO Connection/Session Host=" + this.host + " port=" + this.port
					+ " repository=" + this.repository + " pSystemeGraal=" + pSystemeGraal + " pProject=" + pProject,e);
			instanceSession = null;
			throw new CDOPerimeterUnitConnectorException(e);
		}
		// view.close();
		return retour;

	}

	@Override
	public List<Element> importUseCases(final String pSystemeGraal, final String pProject)
			throws CDOPerimeterUnitConnectorException
	{
		final List<org.w3c.dom.Element> retour = new ArrayList<org.w3c.dom.Element>();
		// openVu();
		try
		{
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final System systemeGraal = this.getSystemGraal(pSystemeGraal, pProject);
			final EList<UseCase> listUseCase = systemeGraal.getUseCases();
			for (UseCase uc : listUseCase)
			{
				StringBuilder listIdTaskLinked = new StringBuilder();
				/*
				 * String listIdTaskLinked = userStory.getAttribute("listIdTaskLinked");
				 */
				final Element useCase = buildUseCase(uc, doc);
				final EList<AbstractTask> listTasksLinked = uc.getTasks();
				for (AbstractTask t : listTasksLinked)
				{
					if (listIdTaskLinked.length() == 0)
					{
						listIdTaskLinked.append(t.eResource().getURIFragment(t));
					}
					else
					{
						listIdTaskLinked.append('|').append(t.eResource().getURIFragment(t));
					}
				}
				useCase.setAttribute("listIdTaskLinked", listIdTaskLinked.toString());
				retour.add(useCase);
			}

		}
		catch (ParserConfigurationException e)
		{
			LOG.error("importUseCases Error CDO Connection/Session Host=" + this.host + " port=" + this.port
					+ " repository=" + this.repository + " pSystemeGraal=" + pSystemeGraal + " pProject=" + pProject,e);
			instanceSession = null;
			throw new CDOPerimeterUnitConnectorException(e);
		}
		// view.close();
		return retour;
	}

	private System getSystemGraal(final String pSystemeGraal, final String pProject)
			throws CDOPerimeterUnitConnectorException
	{
		System systemVoulu;
		this.getSession();
		// initialize the task graal modelpackage
		GraalPackage.eINSTANCE.eClass();
		EnvironmentPackage.eINSTANCE.eClass();
		GraalExtensionsPackage.eINSTANCE.eClass();
		EresourcePackage.eINSTANCE.getClass();
		this.instanceSession.getPackageRegistry().putEPackage(GraalPackage.eINSTANCE);
		this.instanceSession.getPackageRegistry().putEPackage(EnvironmentPackage.eINSTANCE);
		this.instanceSession.getPackageRegistry().putEPackage(GraalExtensionsPackage.eINSTANCE);
		this.instanceSession.getPackageRegistry().putEPackage(EresourcePackage.eINSTANCE);
		try
		{
			final CDOView view = openVu();
			final CDOResource resource = view.getResource(pProject + "/" + pSystemeGraal + ".graal");
			final EList<EObject> eol = resource.getContents();
			systemVoulu = (System) eol.get(0);
		}
		catch (Exception e)
		{
			LOG.error("import Error CDO Connection/Session Host=" + this.host + " port=" + this.port + " repository="
										+ this.repository + " pSystemeGraal=" + pSystemeGraal + " pProject=" + pProject, e);
			throw new CDOPerimeterUnitConnectorException(e);
		}
		return systemVoulu;
	}

	private Element buildUseCase(final UseCase uc, final Document doc)
	{
		final Element useCase = doc.createElement("useCase");
		useCase.setAttribute(NAME, uc.getName());
		useCase.setAttribute(DESCRIPTION, uc.getDescription());
		useCase.setAttribute(ID, uc.eResource().getURIFragment(uc));
		useCase.setAttribute(VERSION, uc.getVersion() + EMPTY_STRING);
		final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		if (uc.getCreatedOn() == null)
		{
			useCase.setAttribute(CREATED_ON, formatter.format(new Date()));
		}
		else
		{
			useCase.setAttribute(CREATED_ON, formatter.format(uc.getCreatedOn()));
		}
		if (uc.getModifiedOn() == null)
		{
			useCase.setAttribute(MODIFIED_ON, formatter.format(new Date()));
		}
		else
		{
			useCase.setAttribute(MODIFIED_ON, formatter.format(uc.getModifiedOn()));
		}

		useCase.setAttribute(RISK_LEVEL, this.getRiskUnitPerimeter(uc));
		useCase.setAttribute(BENEFITS_LEVEL, this.getBenefitsUnitPerimeter(uc));
		useCase.setAttribute(DRAWBACKS_LEVEL, this.getDrawbacksUnitPerimeter(uc));
		return useCase;
	}

	private CDOSession getSession() throws CDOPerimeterUnitConnectorException
	{
		try
		{
			if (config == null)
			{
				config = CDONet4jUtil.createSessionConfiguration();
			}
			config.setConnector(this.getConnector());
			config.setRepositoryName(repository);
			config.setActivateOnOpen(true);
			// config.setMaxReconnectAttempts(1);
			if (instanceSession != null)
			{
				instanceSession.close();
			}
			instanceSession = config.openSession();
		}
		catch (Exception e)
		{
			LOG.error("connector=" + connector + "instanceSession=" + instanceSession);
			LOG.error(e.getCause() + " " + e.getMessage() + " " + e);
			LOG.error("Error CDO Connection/Session Host=" + this.host + " port=" + this.port + " repository="
										+ this.repository);
			instanceSession = null;
			throw new CDOPerimeterUnitConnectorException(e);
		}
		return instanceSession;
	}

	private CDOView openVu() throws CDOPerimeterUnitConnectorException
	{
		if (this.instanceSession == null)
		{
			instanceSession = this.getSession();
		}
		if (this.view == null)
		{
			view = this.instanceSession.openView();
		}
		if (this.view.isClosed())
		{
			view = this.instanceSession.openView();
		}
		if (this.view.isDirty())
		{
			view.close();
			view = this.instanceSession.openView();
		}
		return this.view;
	}

	private String getRiskUnitPerimeter(final NamedElement elt)
	{
		String                  retour      = null;
		final MetaDataContainer mdContainer = elt.getMetadatas();
		if (mdContainer != null)
		{
			final EList<MetaData> mdList = mdContainer.getMetadatas();
			for (MetaData md : mdList)
			{
				if (md instanceof Risk)
				{
					retour = ((Risk) md).getRisk().toString();
				}
			}
		}
		return retour;
	}

	private String getBenefitsUnitPerimeter(final NamedElement elt)
	{
		String                  retour      = null;
		final MetaDataContainer mdContainer = elt.getMetadatas();
		if (mdContainer != null)
		{
			for (MetaData md : mdContainer.getMetadatas())
			{
				if (md instanceof Risk)
				{
					retour = ((Risk) md).getBenefits().toString();
				}
			}
		}
		return retour;
	}

	private String getDrawbacksUnitPerimeter(final NamedElement elt)
	{
		String                  retour      = null;
		final MetaDataContainer mdContainer = elt.getMetadatas();
		if (mdContainer != null)
		{
			for (MetaData md : mdContainer.getMetadatas())
			{
				if (md instanceof Risk)
				{
					retour = ((Risk) md).getDrawbacks().toString();
				}
			}
		}
		return retour;
	}

	private IConnector getConnector() throws NumberFormatException, UnknownHostException, IOException
	{
		final Socket socket = new Socket(host, Integer.parseInt(port));
		socket.close();
		if (this.connector != null)
		{
			this.connector.close();
		}
		IPluginContainer.INSTANCE.clearElements();
		this.connector = (IConnector) IPluginContainer.INSTANCE.getElement("org.eclipse.net4j.connectors", "tcp", host);
		// this.connector.waitForConnection(30000);
		return this.connector;
	}

	@Override
	public List<Element> importUserStories(final String pSystemeGraal, final String pProject)
			throws CDOPerimeterUnitConnectorException
	{
		final List<org.w3c.dom.Element> retour = new ArrayList<org.w3c.dom.Element>();
		// openVu();
		try
		{
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final System systemeGraal = this.getSystemGraal(pSystemeGraal, pProject);
			final EList<UserStory> listUserStory = systemeGraal.getUserStories();
			for (UserStory us : listUserStory)
			{
				/*
				 * String listIdTaskLinked = userStory.getAttribute("listIdTaskLinked");
				 */
				final Element useStory = buildUserStory(us, doc);
				// on regarde si les taches appartiennent au us et on les liste
				String listIdTaskLinked = EMPTY_STRING;
				listIdTaskLinked = listLinkedTaskTOUserStory(listIdTaskLinked, systemeGraal, us);
				useStory.setAttribute("listIdTaskLinked", listIdTaskLinked);
				retour.add(useStory);
			}

		}
		catch (ParserConfigurationException e)
		{
			LOG.error("importUserStories Error CDO Connection/Session Host=" + this.host + " port=" + this.port
					+ " repository=" + this.repository + " pSystemeGraal=" + pSystemeGraal + " pProject=" + pProject);
			instanceSession = null;
			throw new CDOPerimeterUnitConnectorException(e);
		}
		// view.close();
		return retour;
	}

	@Override
	public String getRepository()
	{
		return repository;
	}

	@Override
	public void setRepository(final String repository)
	{
		this.repository = repository;
	}

	@Override
	public String getHost()
	{
		return host;
	}

	@Override
	public void setHost(final String host)
	{
		this.host = host;
	}

	@Override
	public String getPort()
	{
		return port;
	}

	@Override
	public void setPort(final String port)
	{
		this.port = port;
	}

	@Override
	public void changeParameters(final String host, final String port, final String repository)
	{

		this.setPort(port);
		this.setHost(host);
		this.setRepository(repository);
		this.close();
		IPluginContainer.INSTANCE.clearElements();
	}

	@Override
	public void close()
	{
		if (this.instanceSession != null)
		{
			this.instanceSession.close();
		}
		if (this.view != null)
		{
			this.view.close();
		}
		if (connector != null)
		{
			this.connector.close();
		}
		IPluginContainer.INSTANCE.clearElements();
	}

	private String listLinkedTaskTOUserStory(final String listIdTaskLinked, final System pSystemeGraal,
			final UserStory us) throws CDOPerimeterUnitConnectorException
	{
		final StringBuilder retour = new StringBuilder(listIdTaskLinked);
		openVu();
		final EList<Task> listTask = pSystemeGraal.getOwnedTasks();
		for (Task t : listTask)
		{
			if (t.isConcernedByUserStory(us))
			{
				if (listIdTaskLinked.equals(EMPTY_STRING))
				{
					retour.append(t.eResource().getURIFragment(t));
				}
				else
				{
					retour.append('|').append(t.eResource().getURIFragment(t));
				}
			}
		}

		return retour.toString();
	}

	// public void afficheElt(final List<Element> eltList, final String niveau) {
	// String lvl;
	// if (niveau == null) {
	// lvl = EMPTY_STRING;
	// }
	// else {
	// lvl = niveau + "\tr";
	// }
	// for (Element elt : eltList) {
	// java.lang.System.out.print(lvl + elt.getNodeName());
	// final NamedNodeMap attMap = elt.getAttributes();
	// for (int vv = 0; vv < attMap.getLength();vv++) {
	// final Attr att = (Attr)attMap.item(vv);
	// java.lang.System.out.print(" [" + att.getName() + "=" + att.getValue() +"]");
	// }
	// java.lang.System.out.println();
	// if (elt.hasChildNodes()) {
	// final NodeList listNode =elt.getChildNodes();
	// afficheChildElt(listNode,lvl);
	// }
	// }
	// }
	//
	// private void afficheChildElt(final NodeList listNode, String niveau) {
	// niveau = niveau + "\t";
	// for (int vv = 0; vv < listNode.getLength();vv++) {
	// Element elt1 = (Element)listNode.item(vv);
	// java.lang.System.out.print(niveau + elt1.getNodeName());
	// NamedNodeMap attMap = elt1.getAttributes();
	// for (int i = 0; i < attMap.getLength();i++) {
	// Attr att = (Attr)attMap.item(i);
	// java.lang.System.out.print(" [" + att.getName() + "=" + att.getValue() +"]");
	// }
	// java.lang.System.out.println();
	// if (elt1.hasChildNodes()) {
	// NodeList listNodeFils =elt1.getChildNodes();
	// afficheChildElt(listNodeFils,niveau);
	// }
	// }
	// }

}