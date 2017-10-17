/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.model;

import java.util.StringTokenizer;

import org.codehaus.plexus.util.StringUtils;
import org.novaforge.beaver.exception.BeaverException;

/**
 * This object is a wrapper used to retrieve artefact meta information from full property. The property should
 * be follow the template (groupId/artefactId/version/type/classifier).
 * 
 * @author Guillaume Lamirand
 */
public class PropertyArtifact
{

  /**
   * Separator used to define artefact reference (groupId/artefactId/version/type/classifier)
   */
  private static final String ARTEFACT_SEPARATOR = "/";
  private String              groupId;
  private String              artifactId;
  private String              version;
  private String              type;
  private String              classifier;

  /**
   * Contructur new {@link PropertyArtifact}
   * 
   * @param pPropertyString
   *          the
   * @throws BeaverException
   */
  public PropertyArtifact(final String pPropertyString) throws BeaverException
  {
    final StringTokenizer propertyToken = new StringTokenizer(pPropertyString, ARTEFACT_SEPARATOR);

    if (propertyToken.countTokens() >= 3)
    {
      groupId = propertyToken.nextToken();
      artifactId = propertyToken.nextToken();
      version = propertyToken.nextToken();
      if (propertyToken.hasMoreTokens())
      {
        type = propertyToken.nextToken();
      }
      if (propertyToken.hasMoreTokens())
      {
        classifier = propertyToken.nextToken();
      }
    }
    else
    {
      throw new BeaverException(
          String
              .format(
                  "The property who defines the data product is malformed for [property=%s]. It final should be groupId/artifactId/version/[type]/[classifier].",
                  pPropertyString));

    }
  }

  /**
   * Returns the groups Id
   * 
   * @return the groups Id
   */
  public String getGroupId()
  {
    return groupId;
  }

  /**
   * Returns the groups Id
   * 
   * @return the groups Id
   */
  public String getArtifactId()
  {
    return artifactId;
  }

  /**
   * Returns the groups Id
   * 
   * @return the groups Id
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * Returns the type, can be <code>null</code> depending on the original property
   * 
   * @return the type
   */
  public String getType()
  {
    return type;
  }

  /**
   * Returns <code>true</code> if the artefact has a type defined
   * 
   * @return <code>true</code> if the artefact has a type defined
   */
  public boolean hasType()
  {
    return StringUtils.isNotEmpty(type);
  }

  /**
   * Returns the classifier, can be <code>null</code> depending on the original property
   * 
   * @return the classifier
   */
  public String getClassifier()
  {
    return classifier;
  }

  /**
   * Returns <code>true</code> if the artefact has a classifier defined
   * 
   * @return <code>true</code> if the artefact has a classifier defined
   */
  public boolean hasClassifier()
  {
    return StringUtils.isNotEmpty(classifier);
  }
}
