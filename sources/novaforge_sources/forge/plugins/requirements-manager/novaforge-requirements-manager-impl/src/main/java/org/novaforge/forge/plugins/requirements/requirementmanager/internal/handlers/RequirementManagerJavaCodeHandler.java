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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultBean;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementConfigurationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerCodeHandler;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerFunctionalService;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.facades.RequirementCodeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sbenoist
 */
public class RequirementManagerJavaCodeHandler implements RequirementManagerCodeHandler
{
  private static final String                 REQUIREMENT_ENUM_REGEX            = "REQ_ID[(](\\s*)[\"]([\\w-]+)[\"][,](\\s*)(\\w*)";

  private static final String                 REQUIREMENT_ID_TOKEN_TO_REPLACE   = "REQ_ID";

  private static final String                 REQUIREMENT_ENUM_TOKEN_TO_REPLACE = "REQ_ENUM";
  private static final Log log = LogFactory.getLog(RequirementManagerJavaCodeHandler.class);
  private RequirementManagerFunctionalService requirementManagerFunctionalService;
  private RequirementCodeService              requirementCodeService;
  /** Access to the requirement configuration service. */
  private RequirementConfigurationService     requirementConfigurationService;
  private String                              type;

  /*
   * we assume many things to manage the link between Requirements and SCM coverage...
   * - we assume that requirements are defined in the enumeration Requirement_Enum
   * - we assume that requirement is coded like this :
   * @Requirement( Requirement_Enum.REQ_FCT_DEV)
   * @Requirement( value = { Requirement_Enum.REQ_FCT_DEV } )
   * @Requirement( value = Requirement_Enum.REQ_FCT_DEV)
   * @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })
   * - we assume requirement id and version are defined in the enum Requirement_Enum except for version which
   * is not implemented (cf appliwhite COSI)
   * - we expect to have at least 2 resources for each covered requirement : in XXXService class (the
   * reference to the requirement implementation) and in XXXServiceRequirements (the implementation of the
   * requirement) but we do nothing actually on this assertion
   */

  /** {@inheritDoc} */
  @Override
  public void getCodeResources(final String pProjectId, final String pCodeRepositoryPath,
      final String pCurrentUser, final String pEnumClassName) throws RequirementsManagementServiceException
  {
    try
    {
      String regExp = requirementConfigurationService.getParserRegexp();
      List<SCMSearchResultBean> results = requirementManagerFunctionalService.searchRequirementInSourceCode(
          pProjectId, regExp.replaceAll(REQUIREMENT_ENUM_TOKEN_TO_REPLACE, pEnumClassName), "javaOnly",
          pCodeRepositoryPath, pCurrentUser);

      // clear precedent code resources for last requirement version
      requirementCodeService.clearCodeResourcesForLastVersion(pProjectId);

      if (results != null)
      {
        // extract all the requirements id/name from the occurrences
        String id = null;
        for (SCMSearchResultBean result : results)
        {
          Set<String> eenums = extractReqEnumFromOccurrence(result.getOccurrence(), pEnumClassName);
          for (String eenum : eenums)
          {
            log.debug("requirement enum =  " + eenum);
            id = getRequirementIdFromEnumClass(pProjectId, eenum, pCodeRepositoryPath, pCurrentUser,
                pEnumClassName);
            log.debug("requirement id =  " + id);
            if (id == null)
            {
              log.error(String.format(
                  "Unable to get requirement id from Requirement Enum Class with enum=%s", eenum));
            }
            else
            {
              // FIXME add version when implemented
              String version = null;
              requirementCodeService.updateCodeResources(pProjectId, id, version, result.getClassName(),
                  result.getPackageName(), result.getPath());
            }
          }
        }
      }
    }
    catch (RequirementManagerServiceException e)
    {
      throw new RequirementsManagementServiceException(
          "an error occured during searching resources code for requirements", e);
    }
  }

  /*
   * Here are the following syntaxes for regex :
   * @Requirement( Requirement_Enum.REQ_FCT_DEV)
   * @Requirement( value = { Requirement_Enum.REQ_FCT_DEV } )
   * @Requirement( value = Requirement_Enum.REQ_FCT_DEV)
   * @Requirement(value = { Requirement_Enum.REQ_MY_REQ_ID1, Requirement_Enum.REQ_MY_REQ_ID2 })
   */
  private Set<String> extractReqEnumFromOccurrence(final String pOccurrence, final String pEnumClassName)
  {
    Set<String> ids = new HashSet<String>();

    String str = pOccurrence.replaceAll("\\s+", "");
    String splitter = pEnumClassName + ".";
    String[] tab = str.split(splitter);
    if (tab.length == 2)
    {
      // only one requirement id to extract
      str = tab[1];

      // remove the ) or the }
      int index = str.indexOf('}');
      if (index < 0)
      {
        index = str.indexOf(')');
      }

      str = str.substring(0, index);
      ids.add(str);
    }
    else if (tab.length > 2)
    {
      // 2 requirements
      int index = 0;
      for (int i = 1; i < tab.length; i++)
      {
        if (i < tab.length - 1)
        {
          index = tab[i].indexOf(',');
          str = tab[i].substring(0, index);
        }
        else
        {
          index = tab[i].indexOf('}');
          str = tab[i].substring(0, index);
        }

        ids.add(str);
      }
    }

    return ids;
  }

  private String getRequirementIdFromEnumClass(final String pProjectId, final String pReqEnum,
                                               final String pCodeRepositoryPath, final String pCurrentUser,
                                               final String pEnumClassName)
      throws RequirementsManagementServiceException
  {
    String id = null;

    List<SCMSearchResultBean> results = requirementManagerFunctionalService.searchRequirementInSourceCode(pProjectId,
                                                                                                          REQUIREMENT_ENUM_REGEX
                                                                                                              .replaceAll(REQUIREMENT_ID_TOKEN_TO_REPLACE,
                                                                                                                          pReqEnum),
                                                                                                          pEnumClassName
                                                                                                              + ".java",
                                                                                                          pCodeRepositoryPath,
                                                                                                          pCurrentUser);

    if (results != null && results.size() == 1)
    {
      id = results.get(0).getOccurrence();
      id = id.replaceAll("\\s+", "");
      String[] tab = id.split(pReqEnum);
      if (tab.length == 2)
      {
        id = tab[1].substring(2, tab[1].length() - 2);

        if (id.endsWith("\""))
        {
          id = id.substring(0, id.length() - 1);
        }

      }
    }
    return id;
  }

  /** {@inheritDoc} */
  @Override
  public void setRequirementManagerFunctionalService(
      final RequirementManagerFunctionalService pRequirementManagerFunctionalService)
  {
    requirementManagerFunctionalService = pRequirementManagerFunctionalService;
  }

  /** {@inheritDoc} */
  @Override
  public void setRequirementCodeService(final RequirementCodeService pRequirementCodeService)
  {
    requirementCodeService = pRequirementCodeService;
  }

  /** {@inheritDoc} */
  @Override
  public void setRequirementConfigurationService(
      final RequirementConfigurationService pRequirementConfigurationService)
  {
    requirementConfigurationService = pRequirementConfigurationService;
  }

  @Override
  public String getType()
  {
    return type;
  }

  public void setType(String pType)
  {
    type = pType;
  }

}
