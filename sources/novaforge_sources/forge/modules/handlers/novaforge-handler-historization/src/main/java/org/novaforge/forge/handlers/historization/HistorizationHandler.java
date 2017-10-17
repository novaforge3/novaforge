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
package org.novaforge.forge.handlers.historization;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.Nullable;
import org.apache.felix.ipojo.PrimitiveHandler;
import org.apache.felix.ipojo.metadata.Element;
import org.apache.felix.ipojo.parser.MethodMetadata;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines a iPOJO handler used to manage historization event
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 */
public class HistorizationHandler extends PrimitiveHandler
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(HistorizationHandler.class);
  /**
   * Reference to service implementation of {@link HistorizationService}
   */
  private HistorizationService    historizationService;
  /**
   * Reference to service implementation of {@link AuthentificationService}
   */
  private AuthentificationService authentificationService;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("rawtypes")
  @Override
  public void configure(final Element pMetadata, final Dictionary pConfiguration)
  {
    final Method[] methods = getInstanceManager().getClazz().getDeclaredMethods();
    for (final Method method : methods)
    {
      final Annotation[] annotations = method.getAnnotations();
      for (final Annotation annotation : annotations)
      {
        if (annotation instanceof Historization)
        {
          final MethodMetadata methodMetadata = getPojoMetadata().getMethod(method.getName());
          getInstanceManager().register(methodMetadata, this);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop()
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("stop...");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start()
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("start...");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onEntry(final Object pPojo, final Member pMember, final Object[] pArgs)
  {
    if ((historizationService.isActivated()) && (pMember != null) && (pMember instanceof Method))
    {
      final Method method = (Method) pMember;
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("HistoryHandler :: historize onEntry event for method='%s' from class='%s'",
                                   method.getName(), method.getDeclaringClass()));
      }

      for (final Annotation annotation : method.getAnnotations())
      {
        if (annotation instanceof Historization)
        {
          final Historization historization = (Historization) annotation;
          historizeOnEntryEvent(method, historization.type(), historization.ignoreCurrentUser(), pArgs);
          break;
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onExit(final Object pPojo, final Member pMember, final Object pReturnedObj)
  {
    if ((historizationService.isActivated()) && (pMember != null) && (pMember instanceof Method))
    {
      final Method method = (Method) pMember;
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format("HistoryHandler :: historize onExit event for method='%s' from class='%s'",
                                   method.getName(), method.getDeclaringClass()));
      }

      for (final Annotation annotation : method.getAnnotations())
      {
        if (annotation instanceof Historization)
        {
          final Historization historization = (Historization) annotation;
          final String onExitLabel = historization.returnLabel();
          historizeOnExitEvent(method, historization.type(), historization.ignoreCurrentUser(), onExitLabel,
              pReturnedObj);
          break;
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onError(final Object pPojo, final Member pMember, final Throwable pThrowable)
  {

    if ((historizationService.isActivated()) && (pMember != null) && (pMember instanceof Method))
    {
      final Method method = (Method) pMember;
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug(String.format(
            "HistoryHandler :: historize onError event for method='%s' from class='%s'", method.getName(),
            method.getDeclaringClass()));
      }

      for (final Annotation annotation : method.getAnnotations())
      {
        if (annotation instanceof Historization)
        {
          final Historization historization = (Historization) annotation;
          historizeonErrorEvent(historization.type(), historization.ignoreCurrentUser(), pThrowable);
          break;
        }
      }
    }
  }

  private void historizeonErrorEvent(final EventType pType, final boolean pIgnoreCurrentUser,
                                     final Throwable pThrowable)
  {
    final Map<String, Object> maps = new HashMap<String, Object>();
    if ((pThrowable != null))
    {
      maps.put("error", (pThrowable.getMessage() != null) ? pThrowable.getMessage() : pThrowable.toString());

    }
    // get the author of the event
    final String author = getEventAuthor(pIgnoreCurrentUser);

    historizationService.registerEvent(author, pType, EventLevel.ERROR, maps);
  }

  private void historizeOnExitEvent(final Method pMethod, final EventType pType, final boolean pIgnoreCurrentUser,
                                    final String pOnExitLabel, final Object pReturnedObj)
  {
    final Map<String, Object> maps = new HashMap<String, Object>();
    if ((pOnExitLabel != null) && (!"".equals(pOnExitLabel)))
    {
      maps.put(pOnExitLabel, pReturnedObj);

    }
    // get the author of the event
    final String author = getEventAuthor(pIgnoreCurrentUser);

    // historize the event
    historizationService.registerEvent(author, pType, EventLevel.EXIT, maps);
  }

  private void historizeOnEntryEvent(final Method pMethod, final EventType pType,
      final boolean pIgnoreCurrentUser, final Object[] pArgs)
  {
    final Map<String, Object> maps = new HashMap<String, Object>();
    if ((pArgs != null) && (pArgs.length > 0))
    {
      final Annotation[][] parameterAnnotations = pMethod.getParameterAnnotations();
      for (int i = 0; i < parameterAnnotations.length; i++)
      {
        for (final Annotation annotation : parameterAnnotations[i])
        {
          if (annotation instanceof HistorizableParam)
          {
            final String label = ((HistorizableParam) annotation).label();
            final Object object = pArgs[i];
            maps.put(label, object);
          }
        }
      }
    }
    // get the author of the event
    final String author = getEventAuthor(pIgnoreCurrentUser);

    // historize the event
    historizationService.registerEvent(author, pType, EventLevel.ENTRY, maps);
  }

  private String getEventAuthor(final boolean pIgnoreCurrentUser)
  {
    String author = null;
    if ((!pIgnoreCurrentUser) && !(authentificationService instanceof Nullable))
    {
      author = authentificationService.getCurrentUser();
    }

    return author;
  }
}
