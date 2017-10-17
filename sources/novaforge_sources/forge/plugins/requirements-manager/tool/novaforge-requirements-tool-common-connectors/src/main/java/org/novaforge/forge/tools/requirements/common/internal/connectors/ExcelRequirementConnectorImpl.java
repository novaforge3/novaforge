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
package org.novaforge.forge.tools.requirements.common.internal.connectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.tools.requirements.common.connectors.ExternalRepositoryRequirementConnector;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementFactoryException;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.factories.RequirementFactory;
import org.novaforge.forge.tools.requirements.common.model.EDirectoryLevel;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;
import org.novaforge.forge.tools.requirements.common.services.RequirementRepositoryService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author sbenoist
 */
public class ExcelRequirementConnectorImpl extends AbstractRequirementConnector implements
    ExternalRepositoryRequirementConnector
{
  private static final Log             LOGGER = LogFactory.getLog(ExcelRequirementConnectorImpl.class);
  private static String repository;
  private int                          columnType;                                                     // Type
  private int                          columnSubtype;                                                  // Sous-type
  private int                          columnReference;                                                // Nomenclature
  private int                          columnName;                                                     // Nomenclature
  private int                          columnDescription;                                              // Description
  private int                          columnStatus;                                                   // Statut
  private int                          columnComments;                                                 // Commentaires/revues
  private int                          columnKinship;                                                  // Lien
                                                                                                        // vers
                                                                                                        // d'autres
                                                                                                        // exigences
                                                                                                        // client
  private int                          columnVersion;                                                  // Version
  private int                          rowFirstRequirement;
  private RequirementFactory           requirementFactory;

  private RequirementManagerService    requirementManagerService;
  private RequirementRepositoryService requirementRepositoryService;

  private HistorizationService         historizationService;

  public ExcelRequirementConnectorImpl()
  {
    super();
  }

  @Override
  public String getRepositoryLocation()
  {
    return "file://" + repository;
  }

  /** {@inheritDoc} */
  @Override
  public boolean validate(final IRepository pRepository)
  {
    boolean isValid = true;
    if ((pRepository != null) && (ERepositoryType.EXCEL.equals(pRepository.getType())))
    {
      try
      {
        final FileInputStream file = new FileInputStream(pRepository.getURI());
        // Get the workbook instance for XLS file
        final HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet;

        sheet = workbook.getSheet("EXIGENCES CLIENT");
        isValid = isValid && validateSheet(sheet);

        sheet = workbook.getSheet("EXIGENCES TECHNIQUES");
        isValid = isValid && validateSheet(sheet);
      }
      catch (final Exception e)
      {
        isValid = false;
      }
    }
    return isValid;
  }

  private boolean validateSheet(final HSSFSheet pSheet)
  {
    boolean isValid = true;
    if (pSheet == null)
    {
      isValid = false;
    }
    else
    {
      for (final Row row : pSheet)
      {
        if ((row.getRowNum() >= getRowFirstRequirement()) && !isRowEmpty(row))
        {
            final String ref = row.getCell(getColumnReference()).getStringCellValue();
            if ((ref == null) || (ref.isEmpty()))
            {
              isValid = false;
            }
            final String type = row.getCell(getColumnType()).getStringCellValue();
            if ((type == null) || (type.isEmpty()))
            {
              isValid = false;
            }
            final String name = row.getCell(getColumnName()).getStringCellValue();
            if ((name == null) || (name.isEmpty()))
            {
              isValid = false;
            }
        }
      }
    }
    return isValid;
  }

  /**
   * @return the rowFirstRequirement
   */
  public int getRowFirstRequirement()
  {
    return rowFirstRequirement;
  }

  /**
   * Check if a row is empty in data or not.
   *
   * @param row
   *          informations about the row
   * @return true if the row is empty else false
   */
  private boolean isRowEmpty(final Row row)
  {
    boolean result = true;
    for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++)
    {
      final Cell cell = row.getCell(c);
      if ((cell != null) && (cell.getCellType() != Cell.CELL_TYPE_BLANK))
      {
        result = false;
        break;
      }
    }
    return result;
  }

  /**
   * @return the columnReference
   */
  public int getColumnReference()
  {
    return columnReference;
  }

  /**
   * @return the columnType
   */
  public int getColumnType()
  {
    return columnType;
  }

  /**
   * @param columnType
   *     the columnType to set
   */
  public void setColumnType(final int columnType)
  {
    this.columnType = columnType;
  }

  /**
   * @return the columnName
   */
  public int getColumnName()
  {
    return columnName;
  }

  /**
   * @param columnName
   *     the columnName to set
   */
  public void setColumnName(final int columnName)
  {
    this.columnName = columnName;
  }

  /**
   * @param columnReference
   *     the columnReference to set
   */
  public void setColumnReference(final int columnReference)
  {
    this.columnReference = columnReference;
  }

  /**
   * @param rowFirstRequirement
   *     the rowFirstRequirement to set
   */
  public void setRowFirstRequirement(final int rowFirstRequirement)
  {
    this.rowFirstRequirement = rowFirstRequirement;
  }

  @Override
  @Historization(type = EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS)
  public void synchronize(@HistorizableParam(label = "ProjectId") final String pProjectID, String pCurrentUser)
      throws RequirementConnectorException
  {
    Map<String, Object> maps = new HashMap<>();
    maps.put("ProjectId", pProjectID);
    historizationService.registerEvent(pCurrentUser, EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS, EventLevel.ENTRY, maps);
    try
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("Excel synchronization for project=%s", pProjectID));
      }

      final Set<IRepository> repositories = requirementRepositoryService.findRepositoriesByType(pProjectID,
          ERepositoryType.EXCEL);
      for (final IRepository repository : repositories)
      {
        if (LOGGER.isDebugEnabled())
        {
          LOGGER.debug(String.format("Excel synchronization for repository=%s", repository.getURI()));
        }
        synchronizeRepo(repository, pProjectID);
      }
    }
    catch (final RequirementManagerServiceException e)
    {
      throw new RequirementConnectorException(String.format("unable to get project with projectID=%s",
          pProjectID), e);
    }
    historizationService.registerEvent(pCurrentUser, EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS, EventLevel.EXIT, new HashMap<String, Object>());
  }

  /**
   * This method allows to update properties for directories and requirements on CDO referential, to move and
   * to delete directories and requirements moved and deleted on CDO referential {@inheritDoc}
   *
   * @param pRepository
   * @param pProjectId
   */
  private void synchronizeRepo(final IRepository pRepository, final String pProjectId)
      throws RequirementConnectorException
  {
    try
    {
      final Set<IDirectory> dirs = requirementManagerService
          .loadAllRootDirectoryTreesByRepository(pRepository);
      IDirectory dir;
      if (!dirs.isEmpty())
      {
        // check mandatory parameters
        if (dirs.size() > 1)
        {
          throw new RequirementConnectorException("Excel repository must have only one root directory");
        }
        dir = dirs.iterator().next();
        for (final IRequirement requirement : dir.getRequirements())
        {
          dir.deleteRequirement(requirement);
        }
        requirementManagerService.updateDirectory(dir);
      }
      else
      {
        // create the new directories
        dir = requirementFactory.buildNewDirectory(EDirectoryLevel.ROOT);
        dir.setName("/");
        dir.setRepository(pRepository);
        dir.setReference("/");
      }

      // Map containing requirements by reference
      final Map<String, IRequirement> requirements = new HashMap<>();
      // Map containing for a requirements the list of the reference to it parent
      final Map<IRequirement, List<String>> parents = new HashMap<>();

      parseFile(pProjectId, new File(pRepository.getURI()), requirements, parents);
      handleRequirementsRelationship(dir, requirements, parents);

      requirementManagerService.updateDirectory(dir);
    }
    catch (final Exception e)
    {
      throw new RequirementConnectorException(String.format(
          "unable to synchronize for repository with uri=%s on Excel repository", pRepository.getURI()), e);
    }
  }

  private void parseFile(final String pProjectId, final File pFile, final Map<String, IRequirement> pRequirements,
                         final Map<IRequirement, List<String>> pParents)
      throws RequirementManagerServiceException, RequirementFactoryException, IOException
  {
    final FileInputStream file = new FileInputStream(pFile);

    // Get the workbook instance for XLS file
    final HSSFWorkbook workbook = new HSSFWorkbook(file);

    HSSFSheet sheet = workbook.getSheet("EXIGENCES CLIENT");
    parseSheet(pProjectId, sheet, pRequirements, pParents, -1);

    sheet = workbook.getSheet("EXIGENCES TECHNIQUES");
    parseSheet(pProjectId, sheet, pRequirements, pParents, getColumnKinship());
  }

  private void handleRequirementsRelationship(final IDirectory pDirectory,
      final Map<String, IRequirement> pRequirements, final Map<IRequirement, List<String>> pParents)
  {
    for (final Entry<IRequirement, List<String>> entry : pParents.entrySet())
    {
      final IRequirement requirement = entry.getKey();
      final List<String> parentReferences = entry.getValue();
      if ((parentReferences == null) || (parentReferences.size() == 0))
      {
        pDirectory.addRequirement(requirement);
      }
      else
      {
        for (final String reference : parentReferences)
        {
          final IRequirement parent = pRequirements.get(reference);
          if (parent != null)
          {
            parent.addChild(requirement);
          }
          else
          {
            // TODO: lever une erreur si la référence au parent n'a pas été trouvé
          }
        }
      }
    }
  }

  private void parseSheet(final String pProjectId, final HSSFSheet pSheet,
      final Map<String, IRequirement> pRequirements, final Map<IRequirement, List<String>> pParents,
      final int pParentColumn) throws RequirementManagerServiceException, RequirementFactoryException
  {
    for (final Row row : pSheet)
    {
      if ((row.getRowNum() >= getRowFirstRequirement()) && !isRowEmpty(row))
      {
        final String type = getStringFromCell(row, getColumnType());
        final String subtype = getStringFromCell(row, getColumnSubtype());
        final String ref = getStringFromCell(row, getColumnReference());
        final String name = getStringFromCell(row, getColumnName());
        final String description = getStringFromCell(row, getColumnDescription());
        final String status = getStringFromCell(row, getColumnStatus());
        final String comments = getStringFromCell(row, getColumnComments());
        Double versionInExcel = 1d;
        try
        {
          final Double valueInExcel = getNumericFromCell(row, getColumnVersion());
          if (valueInExcel != -1d)
          {
            versionInExcel = valueInExcel;
          }
        }
        catch (final Exception e1)
        {
          // Nothing to do. No Version in sheet.
        }
        String kinship = "";
        if (pParentColumn != -1)
        {
          kinship = row.getCell(getColumnKinship()).getStringCellValue();
        }

        final String reference = buildForgeReference(pProjectId, ref);
        if (!ref.isEmpty())
        {
          final IRequirement requirement = requirementFactory.buildNewRequirement();
          requirement.setProjectId(pProjectId);
          requirement.setType(type);
          requirement.setSubType(subtype);
          requirement.setReference(reference);
          requirement.setName(name);
          requirement.setDescription(description);
          requirement.setStatus(status);
          requirement.setAcceptanceCriteria(comments);

          int versionRequirement = 1;
          try
          {
            versionRequirement = versionInExcel.intValue();
          }
          catch (final NumberFormatException e)
          {
            // Nothing to do. No Version in sheet.
          }
          final IRequirementVersion requirementVersion = requirementFactory.buildNewRequirementVersion();
          requirementVersion.setCurrentVersion(versionRequirement);
          requirement.addRequirementVersion(requirementVersion);

          // store relationship that will be handled later
          final List<String> references = new ArrayList<>();
          if (!kinship.isEmpty())
          {
            for (final String parentRef : Arrays.asList(kinship.split(";")))
            {
              references.add(buildForgeReference(pProjectId, parentRef));
            }
          }
          pParents.put(requirement, references);

          pRequirements.put(reference, requirement);
        }
      }
    }
  }

  /**
   * @return the columnKinship
   */
  public int getColumnKinship()
  {
    return columnKinship;
  }

  /**
   * @param pRow
   * @param pCell
   * @return
   */
  private String getStringFromCell(final Row pRow, final int pColumnNumber)
  {
        String stringInExcel = null;
        final Cell cell = pRow.getCell(pColumnNumber);
        if ((cell != null) && (cell.getCellType() != Cell.CELL_TYPE_BLANK))
        {
        	stringInExcel = cell.getStringCellValue();
        }
        return stringInExcel;
  }

  /**
   * @return the columnSubtype
   */
  public int getColumnSubtype()
  {
    return columnSubtype;
  }

  /**
   * @param columnSubtype
   *          the columnSubtype to set
   */
  public void setColumnSubtype(final int columnSubtype)
  {
    this.columnSubtype = columnSubtype;
  }

  /**
   * @return the columnDescription
   */
  public int getColumnDescription()
  {
    return columnDescription;
  }

  /**
   * @param columnDescription
   *          the columnDescription to set
   */
  public void setColumnDescription(final int columnDescription)
  {
    this.columnDescription = columnDescription;
  }

  /**
   * @return the columnStatus
   */
  public int getColumnStatus()
  {
    return columnStatus;
  }

  /**
   * @param columnStatus
   *          the columnStatus to set
   */
  public void setColumnStatus(final int columnStatus)
  {
    this.columnStatus = columnStatus;
  }

  /**
   * @return the columnComments
   */
  public int getColumnComments()
  {
    return columnComments;
  }

  /**
   * @param row
   * @param pColumnNumber
   * @return
   */
  private Double getNumericFromCell(final Row row, final int pColumnNumber)
  {
    Double     numberInExcel = -1d;
    final Cell cell          = row.getCell(pColumnNumber);
    if ((cell != null) && (cell.getCellType() != Cell.CELL_TYPE_BLANK))
    {
      numberInExcel = cell.getNumericCellValue();
    }
    return numberInExcel;
  }

  /**
   * @return the columnVersion
   */
  public int getColumnVersion()
  {
    return columnVersion;
  }

  private String buildForgeReference(final String pProjectId, final String pExternalKey)
  {
    // The reference contains projectId to allow multiple projects to refer to the same requirement
    return pProjectId + ":" + pExternalKey;
  }

  /**
   * @param columnVersion
   *          the columnVersion to set
   */
  public void setColumnVersion(final int columnVersion)
  {
    this.columnVersion = columnVersion;
  }

  /**
   * @param columnComments
   *          the columnComments to set
   */
  public void setColumnComments(final int columnComments)
  {
    this.columnComments = columnComments;
  }

  /**
   * @param columnKinship
   *          the columnKinship to set
   */
  public void setColumnKinship(final int columnKinship)
  {
    this.columnKinship = columnKinship;
  }

  @Override
  protected EventType getEventType()
  {
    return EventType.SYNCHRONIZATION_EXTERNAL_REQUIREMENTS;
  }

  @Override
  protected HistorizationService getHistorizationService()
  {
    return historizationService;
  }

  public void setHistorizationService(final HistorizationService historizationService)
  {
    this.historizationService = historizationService;
  }

  public void setRepository(final String pRepository)
  {
    repository = pRepository;
    LOGGER.info(String.format("new value for repository is : %s", pRepository));
  }

  public void setRequirementFactory(final RequirementFactory pRequirementFactory)
  {
    requirementFactory = pRequirementFactory;
  }

  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    requirementManagerService = pRequirementManagerService;
  }

}
