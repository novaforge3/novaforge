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
package org.novaforge.forge.it.suite;

import junit.framework.Test;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.novaforge.forge.it.test.AlfrescoPropagationTest;
import org.novaforge.forge.it.test.DokuWikiPropagationTest;
import org.novaforge.forge.it.test.JenkinsPropagationTest;
import org.novaforge.forge.it.test.LimesurveyPropagationTest;
import org.novaforge.forge.it.test.MantisPropagationTest;
import org.novaforge.forge.it.test.NexusPropagationTest;
import org.novaforge.forge.it.test.PhpBBPropagationTest;
import org.novaforge.forge.it.test.SVNPropagationTest;
import org.novaforge.forge.it.test.SonarPropagationTest;
import org.novaforge.forge.it.test.SpecificGroupsPropagationTest;
import org.novaforge.forge.it.test.SpipPropagationTest;
import org.novaforge.forge.it.test.TestLinkPropagationTest;
import org.novaforge.forge.it.test.XmlDataTest;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.osgi.framework.BundleContext;

/**
 * @author blachon-m
 */
public class ToolsPropagationTestSuite
{
  public static Test suite(final BundleContext bc)
  {
    ReportTest.newTestResult();
    OSGiTestSuite suite = new OSGiTestSuite("Tools_Propagation_Test_Suite", bc);
    suite.addTestSuite(XmlDataTest.class);
    suite.addTestSuite(MantisPropagationTest.class);
    suite.addTestSuite(SVNPropagationTest.class);
    suite.addTestSuite(AlfrescoPropagationTest.class);
    suite.addTestSuite(DokuWikiPropagationTest.class);
    suite.addTestSuite(NexusPropagationTest.class);
    suite.addTestSuite(TestLinkPropagationTest.class);
    suite.addTestSuite(PhpBBPropagationTest.class);
    suite.addTestSuite(JenkinsPropagationTest.class);
    suite.addTestSuite(SonarPropagationTest.class);
    suite.addTestSuite(SpipPropagationTest.class);
    suite.addTestSuite(LimesurveyPropagationTest.class);
    suite.addTestSuite(SpecificGroupsPropagationTest.class);
    return suite;
  }
}
