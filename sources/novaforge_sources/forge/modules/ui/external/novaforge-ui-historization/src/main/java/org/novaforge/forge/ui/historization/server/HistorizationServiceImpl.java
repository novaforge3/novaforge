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
package org.novaforge.forge.ui.historization.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.exceptions.HistorizationException;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.ui.historization.client.service.HistorizationRemote;
import org.novaforge.forge.ui.historization.shared.FunctionalLogDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The server side implementation of the RPC service.
 * 
 * @author qsivan
 */
public class HistorizationServiceImpl extends RemoteServiceServlet implements HistorizationRemote
{

  /**
    * 
    */
  private static final long             serialVersionUID = 6346447256283547956L;

  private static final Log              LOGGER           = LogFactory.getLog(HistorizationServiceImpl.class);

  // private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  private static final SimpleDateFormat FORMAT_FILE_OUT  = new SimpleDateFormat("yyyyMMddHHmmssSSS");
  private static ThreadLocal<SimpleDateFormat> format = new ThreadLocal<SimpleDateFormat>()
                                                      {
                                                        @Override
                                                        protected synchronized SimpleDateFormat initialValue()
                                                        {
                                                          return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                                        }
                                                      };

  @Override
  public List<FunctionalLogDTO> getListFunctionalLogs(final String pLevel, final String pType,
      final String pUser, final String pKeyword, final Date pStartDate, final Date pEndDate,
      final int pStart, final int pLength, final String pFixedDate)
  {
    final HistorizationService historizationService = getHistorizationService();

    final List<FunctionalLogDTO> resultList = new ArrayList<FunctionalLogDTO>();
    List<Event> listEvents = new ArrayList<Event>();

    // We fill 2 maps for likes and equals criterias
    Map<String, Object> likeCriterias = new HashMap<String, Object>();
    Map<String, Object> equalCriterias = new HashMap<String, Object>();

    fillCriterias(equalCriterias, likeCriterias, EventLevel.fromLabel(pLevel), EventType.fromLabel(pType),
        pUser, pKeyword);

    // If a map is empty, we set it null value
    if (likeCriterias.isEmpty())
    {
      likeCriterias = null;
    }
    if (equalCriterias.isEmpty())
    {
      equalCriterias = null;
    }

    if ((pStartDate != null) && (pEndDate != null))
    {
      try
      {
        listEvents = historizationService.findEventsByCriterias(likeCriterias, equalCriterias, pStartDate,
            pEndDate, pStart, pLength);
      }
      catch (final HistorizationException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
    }
    else if (pFixedDate != null)
    {
      try
      {
        listEvents = historizationService.findEventsByCriterias(likeCriterias, equalCriterias, null,
            parse(pFixedDate), pStart, pLength);
      }
      catch (final HistorizationException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
      catch (final ParseException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
    }
    else
    {
      try
      {
        listEvents = historizationService.findEventsByCriterias(likeCriterias, equalCriterias, null, null,
            pStart, pLength);
      }
      catch (final HistorizationException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
    }

    for (final Event event : listEvents)
    {
      resultList.add(convertEventToFunctionalLogDTO(event));
    }

    return resultList;
  }

  @Override
  public String exportCSVFromCriterias(final String pLevel, final String pType, final String pUser,
      final String pKeyword, final Date pStartDate, final Date pEndDate, final String pFixedDate,
      final String pLocale)
  {

    final HistorizationService historizationService = getHistorizationService();

    new ArrayList<FunctionalLogDTO>();
    new ArrayList<Event>();

    final String fileOut = FORMAT_FILE_OUT.format(new Date());
    final Locale locale = new Locale(pLocale);

    // We fill 2 maps for likes and equals criterias
    Map<String, Object> likeCriterias = new HashMap<String, Object>();
    Map<String, Object> equalCriterias = new HashMap<String, Object>();

    fillCriterias(equalCriterias, likeCriterias, EventLevel.fromLabel(pLevel), EventType.fromLabel(pType),
        pUser, pKeyword);

    // If a map is empty, we set it null value
    if (likeCriterias.isEmpty())
    {
      likeCriterias = null;
    }
    if (equalCriterias.isEmpty())
    {
      equalCriterias = null;
    }

    if ((pStartDate != null) && (pEndDate != null))
    {
      try
      {
        historizationService.exportEventsInCsvFromCriterias(likeCriterias, equalCriterias, pStartDate,
            pEndDate, DownloadServlet.getDownloadFullPathName(fileOut), locale);
      }
      catch (final HistorizationException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
    }
    else if (pFixedDate != null)
    {
      try
      {
        historizationService.exportEventsInCsvFromCriterias(likeCriterias, equalCriterias, null,
            parse(pFixedDate), DownloadServlet.getDownloadFullPathName(fileOut), locale);
      }
      catch (final HistorizationException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
      catch (final ParseException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
    }
    else
    {
      try
      {
        historizationService.exportEventsInCsvFromCriterias(likeCriterias, equalCriterias, null, null,
            DownloadServlet.getDownloadFullPathName(fileOut), locale);
      }
      catch (final HistorizationException e)
      {
        // TODO Auto-generated catch block
        LOGGER.error(e);
      }
    }

    return fileOut;
  }

  @Override
  public List<String> getListTypes()
  {
    final List<String> resultList = new ArrayList<String>();

    for (final EventType type : EventType.values())
    {
      resultList.add(type.getLabel());
    }

    return resultList;
  }

  @Override
  public List<String> getListLevels()
  {
    final List<String> resultList = new ArrayList<String>();

    for (final EventLevel level : EventLevel.values())
    {
      resultList.add(level.getLabel());
    }

    return resultList;
  }

  @Override
  public List<String> getListUsers()
  {
    final List<String> resultList = new ArrayList<String>();

    try
    {
      getHistorizationService().setActivatedMode(false);
      final UserPresenter userPresenter = getUserPresenter();
      final List<User> listUsers = userPresenter.getAllUsers(true);

      getHistorizationService().setActivatedMode(true);

      for (final User user : listUsers)
      {
        resultList.add(user.getLogin());
      }
    }
    catch (final UserServiceException e)
    {
      // TODO Auto-generated catch block
      LOGGER.error(e);
    }

    return resultList;
  }

  @Override
  public void purgeFunctionnalLogs(final Date pLimitDate)
  {
    final HistorizationService historizationService = getHistorizationService();

    try
    {
      historizationService.deleteEventsBeforeDate(pLimitDate);
    }
    catch (final HistorizationException e)
    {
      LOGGER.error(e);
    }
  }

  @Override
  public int countEventsByCriterias(final String pLevel, final String pType, final String pUser,
      final String pKeyword, final Date pStartDate, final Date pEndDate, final String pFixedDate)
  {

    int result = 0;

    final HistorizationService historizationService = getHistorizationService();

    // We fill 2 maps for likes and equals criterias
    Map<String, Object> likeCriterias = new HashMap<String, Object>();
    Map<String, Object> equalCriterias = new HashMap<String, Object>();

    fillCriterias(equalCriterias, likeCriterias, EventLevel.fromLabel(pLevel), EventType.fromLabel(pType),
        pUser, pKeyword);

    // If a map is empty, we set it null value
    if (likeCriterias.isEmpty())
    {
      likeCriterias = null;
    }
    if (equalCriterias.isEmpty())
    {
      equalCriterias = null;
    }

    try
    {
      if (pFixedDate != null)
      {
        result = historizationService.countEventsByCriterias(likeCriterias, equalCriterias, pStartDate,
            parse(pFixedDate));
      }
      else
      {
        result = historizationService.countEventsByCriterias(likeCriterias, equalCriterias, pStartDate,
            pEndDate);
      }
    }
    catch (final HistorizationException e)
    {
      LOGGER.error(e);
    }
    catch (final ParseException e)
    {
      LOGGER.error(e);
    }

    return result;
  }

  /**
   * @return UserPresenter service
   */
  private UserPresenter getUserPresenter()
  {
    return getService(UserPresenter.class);
  }

  /**
   * @return HistorizationService service
   */
  private HistorizationService getHistorizationService()
  {
    return getService(HistorizationService.class);
  }

  private void fillCriterias(final Map<String, Object> pEqualCriterias, final Map<String, Object> pLikeCriterias,
                             final EventLevel pLevel, final EventType pType, final String pUser, final String pKeyword)
  {
    if (pLevel != null)
    {
      pEqualCriterias.put(HistorizationService.LEVEL_SEARCH_CRITERIA_KEY, pLevel);
    }
    if (pType != null)
    {
      pEqualCriterias.put(HistorizationService.TYPE_SEARCH_CRITERIA_KEY, pType);
    }
    if (pUser != null)
    {
      pEqualCriterias.put(HistorizationService.ACTOR_SEARCH_CRITERIA_KEY, pUser);
    }
    if (pKeyword != null)
    {
      pLikeCriterias.put(HistorizationService.DETAILS_SEARCH_CRITERIA_KEY, pKeyword);
    }
  }

  private static Date parse(final String pStrDate) throws ParseException
  {
    Date ret = null;
    if ((pStrDate != null) && (pStrDate.trim().length() > 0))
    {
      ret = format.get().parse(pStrDate);
    }

    return ret;
  }

  private FunctionalLogDTO convertEventToFunctionalLogDTO(final Event pEvent)
  {
    final FunctionalLogDTO logDTO = new FunctionalLogDTO();

    logDTO.setLogDate(format(pEvent.getDate()));
    logDTO.setLogLevel(pEvent.getLevel().getLabel());
    logDTO.setLogLevelColor(pEvent.getLevel().getColor());
    logDTO.setLogMessage(pEvent.getDetails());
    logDTO.setLogType(pEvent.getType().getLabel());
    logDTO.setLogUser(pEvent.getActor());

    return logDTO;
  }

  @SuppressWarnings("unchecked")
  private <T> T getService(final Class<T> pClassService)
  {
    final String canonicalName = pClassService.getCanonicalName();
    T service = null;
    try
    {
      final InitialContext initialContext = new InitialContext();
      service = (T) initialContext.lookup(String.format("osgi:service/%s", canonicalName));

    }
    catch (final NamingException e)
    {
      throw new IllegalArgumentException(String.format("Unable to get OSGi service with [interface=%s]",
          canonicalName), e);
    }
    return service;
  }

  private static String format(final Date pDate)
  {
    final String ret = null;
    if (pDate != null)
    {
      return format.get().format(pDate);
    }

    return ret;
  }
}
