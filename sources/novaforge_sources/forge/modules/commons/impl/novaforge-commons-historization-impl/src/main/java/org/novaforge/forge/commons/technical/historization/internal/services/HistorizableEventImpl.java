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

import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.model.HistorizableEvent;
import org.novaforge.forge.commons.technical.historization.model.HistorizableObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class HistorizableEventImpl implements HistorizableEvent
{
  /**
   * Seril version id
   */
  private static final long              serialVersionUID = 7935663573842213532L;
  private final String                   author;
  private final EventType                eventType;
  private final EventLevel               eventLevel;
  private final List<HistorizableObject> objects          = new ArrayList<HistorizableObject>();

  /**
   * @param pAuthor
   * @param pEventType
   * @param pEventLevel
   * @param pObjects
   */
  public HistorizableEventImpl(final String pAuthor, final EventType pEventType,
      final EventLevel pEventLevel, final HistorizableObject... pObjects)
  {
    this(pAuthor, pEventType, pEventLevel, Arrays.asList(pObjects));
  }

  /**
   * @param pAuthor
   * @param pEventType
   * @param pEventLevel
   * @param pObjects
   */
  public HistorizableEventImpl(final String pAuthor, final EventType pEventType,
      final EventLevel pEventLevel, final List<HistorizableObject> pObjects)
  {
    author = pAuthor;
    eventType = pEventType;
    eventLevel = pEventLevel;
    objects.addAll(pObjects);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthor()
  {
    return author;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EventType getType()
  {
    return eventType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EventLevel getLevel()
  {
    return eventLevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<HistorizableObject> getObjects()
  {
    return objects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "HistorizableEventImpl [author=" + author + ", eventType=" + eventType + ", eventLevel=" + eventLevel
               + ", objects=" + objects + "]";
  }

}
