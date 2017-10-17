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
package org.novaforge.forge.commons.technical.historization.internal.services;

import org.novaforge.forge.commons.technical.constants.Constants;
import org.novaforge.forge.commons.technical.conversion.CsvConversionException;
import org.novaforge.forge.commons.technical.conversion.CsvConverterFactory;
import org.novaforge.forge.commons.technical.conversion.CsvConverterService;
import org.novaforge.forge.commons.technical.conversion.model.CsvCellDescriptor;
import org.novaforge.forge.commons.technical.conversion.model.CsvConverterDescriptor;
import org.novaforge.forge.commons.technical.historization.dao.EventDAO;
import org.novaforge.forge.commons.technical.historization.exceptions.HistorizationException;
import org.novaforge.forge.commons.technical.historization.internal.csv.CsvEventBean;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.model.HistorizableEvent;
import org.novaforge.forge.commons.technical.historization.model.HistorizableObject;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.commons.technical.jms.MessageService;
import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author sbenoist
 */
public class HistorizationServiceImpl implements HistorizationService
{
  private static final Locale DEFAULT_LOCALE      = Locale.FRENCH;

  private static final String PROPERTY_FILE       = "csv.event";

  private static final String LABEL               = "label";

  private static final String CSV_DATETIME_FORMAT = "yyyy.MM.dd.HH.mm.ss";

  private static final char   CSV_DELIMITER       = ';';

  private static final String NOT_LOGGED_USER     = "no loggued user";
  private static final Logger LOGGER = LoggerFactory.getLogger(HistorizationServiceImpl.class);
  private EventDAO            eventDAO;
  private CsvConverterService csvConverterService;
  private CsvConverterFactory csvConverterFactory;
  private MessageService      messageService;
  /**
   * This field defines if the historization service is activated into the forge
   */
  private boolean             activated           = true;

  /**
   * @param pEventDAO
   *          the eventDAO to set
   */
  public void setEventDAO(final EventDAO pEventDAO)
  {
    eventDAO = pEventDAO;
  }

  /**
   * @param pCsvConverterService
   *          the csvConverterService to set
   */
  public void setCsvConverterService(final CsvConverterService pCsvConverterService)
  {
    csvConverterService = pCsvConverterService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActivated()
  {
    return activated;
  }

  /**
   * @param pCsvConverterFactory
   *          the csvConverterFactory to set
   */
  public void setCsvConverterFactory(final CsvConverterFactory pCsvConverterFactory)
  {
    csvConverterFactory = pCsvConverterFactory;
  }

  /**
   * @param pMessageService
   *          the messageService to set
   */
  public void setMessageService(final MessageService pMessageService)
  {
    messageService = pMessageService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActivatedMode(final boolean pActivated)
  {
    activated = pActivated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByActor(final String pLogin) throws HistorizationException
  {
    return eventDAO.findEventsByActor(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByDate(final Date pBeginDate, final Date pEndDate)
      throws HistorizationException
  {
    return eventDAO.findEventsByDate(pBeginDate, pEndDate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByKeyword(final String pKeyWord) throws HistorizationException
  {
    return eventDAO.findEventsByKeyword(pKeyWord);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByLevel(final EventLevel pLevel) throws HistorizationException
  {
    return eventDAO.findEventsByLevel(pLevel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByType(final EventType pType) throws HistorizationException
  {
    return eventDAO.findEventsByType(pType);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerEvent(final String pAuthor, final EventType pType, final EventLevel pLevel,
      final Map<String, Object> pHistorizables)
  {
    if (activated)
    {
      final List<HistorizableObject> historizableObjects = new ArrayList<HistorizableObject>();
      final Set<Entry<String, Object>> entrySet = pHistorizables.entrySet();
      for (final Entry<String, Object> entry : entrySet)
      {
        historizableObjects.add(new HistorizableObjectImpl(entry.getKey(), entry.getValue()));
      }

      String author = pAuthor;
      if ((author == null) || ("".equals(author)))
      {
        author = NOT_LOGGED_USER;
      }
      final HistorizableEvent event = new HistorizableEventImpl(author, pType, pLevel, historizableObjects);

      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("sending an historization notification with datas=%s", event.toString()));
      }

      try
      {
        messageService.publish(Constants.HISTORIZATION_TOPIC_SHORT_NAME, event);
      }
      catch (final MessageServiceException e)
      {
        LOGGER.error("an error occured during publishing the historization notification", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int deleteEventsBeforeDate(final Date pDate) throws HistorizationException
  {
    return eventDAO.deleteEventsBeforeDate(pDate);
  }

  private CsvEventBean convert(final Event pEvent)
  {
    final CsvEventBean bean = new CsvEventBean();

    bean.setActor(pEvent.getActor());
    bean.setDate(pEvent.getDate());
    bean.setDetails(pEvent.getDetails());
    bean.setLevel(pEvent.getLevel().getLabel());
    bean.setType(pEvent.getType().getLabel());

    return bean;
  }

  private CsvConverterDescriptor getExportDescriptor()
  {
    final List<CsvCellDescriptor> cells = new ArrayList<CsvCellDescriptor>();
    final CsvCellDescriptor dateCell = csvConverterFactory.createCsvCellDescriptor(
        getLabel("date", DEFAULT_LOCALE), "date");
    dateCell.setMandatory(true);
    dateCell.setFormat(CSV_DATETIME_FORMAT);
    cells.add(dateCell);
    cells.add(csvConverterFactory.createCsvCellDescriptor(getLabel("type", DEFAULT_LOCALE), "type"));
    cells.add(csvConverterFactory.createCsvCellDescriptor(getLabel("level", DEFAULT_LOCALE), "level"));
    cells.add(csvConverterFactory.createCsvCellDescriptor(getLabel("actor", DEFAULT_LOCALE), "actor"));
    cells.add(csvConverterFactory.createCsvCellDescriptor(getLabel("details", DEFAULT_LOCALE), "details"));
    return csvConverterFactory.createCsvConverterDescriptor(cells, true, CSV_DELIMITER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void exportEventsInCsv(final List<Event> pEvents, final String pFileOut, final Locale pLocale)
      throws HistorizationException
  {
    // converts to beans
    final List<CsvEventBean> beans = new ArrayList<CsvEventBean>();
    for (final Event event : pEvents)
    {
      beans.add(convert(event));
    }

    // create the cells descriptors
    final CsvConverterDescriptor descriptor = getExportDescriptor();

    // generate the file csv
    try
    {
      csvConverterService.exportToFile(pFileOut, CsvEventBean.class, beans, descriptor);
    }
    catch (final CsvConversionException e)
    {
      throw new HistorizationException(String.format(
          "an error occured during exporting events in csv format in file=%s", pFileOut), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void exportEventsInCsvFromCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias, final Date pBeginDate, final Date pEndDate,
      final String pFileOut, final Locale pLocale) throws HistorizationException
  {
    // search the events in function of the criterias
    List<Event> events = null;

    boolean hasCriterias = false;
    if (((pLikeCriterias != null) && (!pLikeCriterias.isEmpty())) || ((pLikeCriterias != null) && (!pLikeCriterias
                                                                                                        .isEmpty())))
    {
      hasCriterias = true;
    }

    if (hasCriterias)
    {
      events = findEventsByCriterias(pLikeCriterias, pEqualCriterias, pBeginDate, pEndDate, 0, 0);
    }
    else
    {
      events = getAllRegisteredEvents(0, 0, pEndDate);
    }

    // export the events
    exportEventsInCsv(events, pFileOut, pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> findEventsByCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias, final Date pBeginDate, final Date pEndDate,
      final int pFirstResult, final int pMaxResults) throws HistorizationException
  {
    return eventDAO.findEventsByCriterias(pLikeCriterias, pEqualCriterias, pBeginDate, pEndDate,
        pFirstResult, pMaxResults);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Event> getAllRegisteredEvents(final int pFirstResult, final int pMaxResults, final Date pDateMax)
      throws HistorizationException
  {
    return eventDAO.findAllEvents(pFirstResult, pMaxResults, pDateMax);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countEventsByCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias, final Date pBeginDate, final Date pEndDate)
      throws HistorizationException
  {
    return eventDAO.countEventsByCriterias(pLikeCriterias, pEqualCriterias, pBeginDate, pEndDate);
  }

  private String getLabel(final String pLabel, final Locale pLocale)
  {
    final ResourceBundle resourceBundle = getBundle(pLocale);
    return resourceBundle.getString(pLabel + "." + LABEL);
  }

  private ResourceBundle getBundle(final Locale pLocale)
  {
    return ResourceBundle.getBundle(PROPERTY_FILE, pLocale);
  }

}
