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
package org.novaforge.forge.plugins.categories.beans;

import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestReferenceBean;

import java.util.ArrayList;
import java.util.List;

public class CoveredRequirementBeanImpl implements CoveredRequirementBean
{

  /**
    * 
    */
  private static final long       serialVersionUID = 8334647929057773764L;
  private String                  name;
  private String                  reference;
  private String                  version;

  private List<TestReferenceBean> tests;

  public CoveredRequirementBeanImpl()
  {
    // Default
  }

  /**
   * @param id
   * @param reqDocId
   * @param version
   */
  public CoveredRequirementBeanImpl(final String pRef, final String pName, final String pVersion)
  {
    super();
    name = pName;
    reference = pRef;
    version = pVersion;
    tests = new ArrayList<TestReferenceBean>();
  }

  @Override
  public String getReference()
  {
    return reference;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getVersion()
  {
    return version;
  }

  @Override
  public List<TestReferenceBean> getTests()
  {
    return tests;
  }

  @Override
  public void addTest(final TestReferenceBean pTest)
  {
    tests.add(pTest);

  }

  @Override
  public void removeTest(final TestReferenceBean pTest)
  {
    tests.remove(pTest);

  }

  public void setTests(final List<TestReferenceBean> pTests)
  {
    tests = pTests;
  }

  public void setVersion(final String pVersion)
  {
    version = pVersion;
  }

  public void setName(final String pName)
  {
    name = pName;
  }

  public void setReference(final String pReference)
  {
    reference = pReference;
  }

}
