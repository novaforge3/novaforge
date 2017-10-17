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

import junit.framework.TestCase;
import org.novaforge.forge.tools.managementmodule.connectors.CDOPerimeterUnitConnector;
import org.novaforge.forge.tools.managementmodule.exceptions.CDOPerimeterUnitConnectorException;
import org.novaforge.forge.tools.unite.perimetre.common.internal.connectors.CDOPerimeterUnitConnectorImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sbenoist
 */
public class TestReadCDOTask2 extends TestCase
{
	private static final Logger LOG = Logger.getLogger("TestReadCDOTask2");
	CDOPerimeterUnitConnector connector = null;
	private String							host				= "safran-forge-test.intradef.gouv.fr";
	private String							port				= "2036";
	private String							repository	= "safran";
	private String							projet			= "PAL2";
	private String							sytemeGraal = "PAL2";

	public TestReadCDOTask2()
	{
		super();
		connector = new CDOPerimeterUnitConnectorImpl();
		connector.changeParameters(host, port, repository);
		// , "javatest", "MonProjet"
	}

	public void testRead()
	{
		try
		{
			List<Element> eltListTask = connector.importTasks(sytemeGraal, projet);

			afficheElt(eltListTask, null);
			List<Element> eltListUsesCases = connector.importUseCases(sytemeGraal, projet);
			afficheElt(eltListUsesCases, null);
			List<Element> eltListUserStories = connector.importUserStories(sytemeGraal, projet);
			afficheElt(eltListUserStories, null);
		}
		catch (CDOPerimeterUnitConnectorException e)
		{
			LOG.log(Level.SEVERE, "Error in testRead", e);
			connector.changeParameters(host, port, repository);
			try
			{
				List<Element> eltListUserStories = connector.importUserStories(sytemeGraal, projet);
				afficheElt(eltListUserStories, null);
			}
			catch (CDOPerimeterUnitConnectorException e1)
			{
				LOG.log(Level.SEVERE, "Error in testRead", e);
			}

		}
	}

	private void afficheElt(final List<Element> eltList, String niveau)
	{
		if (niveau == null)
		{
			niveau = "";
		}
		else
		{
			niveau = niveau + "\tr";
		}
		for (Element elt : eltList)
		{
			java.lang.System.out.print(niveau + elt.getNodeName());
			NamedNodeMap attMap = elt.getAttributes();
			for (int vv = 0; vv < attMap.getLength(); vv++)
			{
				Attr att = (Attr) attMap.item(vv);
				java.lang.System.out.print(" [" + att.getName() + "=" + att.getValue() + "]");
			}
			java.lang.System.out.println();
			if (elt.hasChildNodes())
			{
				NodeList listNode = elt.getChildNodes();
				afficheChildElt(listNode, niveau);
			}
		}
	}

	private void afficheChildElt(final NodeList listNode, String niveau)
	{
		niveau = niveau + "\t";
		for (int vv = 0; vv < listNode.getLength(); vv++)
		{
			Element elt1 = (Element) listNode.item(vv);
			java.lang.System.out.print(niveau + elt1.getNodeName());
			NamedNodeMap attMap = elt1.getAttributes();
			for (int i = 0; i < attMap.getLength(); i++)
			{
				Attr att = (Attr) attMap.item(i);
				java.lang.System.out.print(" [" + att.getName() + "=" + att.getValue() + "]");
			}
			java.lang.System.out.println();
			if (elt1.hasChildNodes())
			{
				NodeList listNodeFils = elt1.getChildNodes();
				afficheChildElt(listNodeFils, niveau);
			}
		}
	}

}
