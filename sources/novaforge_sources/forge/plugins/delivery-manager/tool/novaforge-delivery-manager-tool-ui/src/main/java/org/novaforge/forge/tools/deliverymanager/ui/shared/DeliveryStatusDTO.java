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
package org.novaforge.forge.tools.deliverymanager.ui.shared;

import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This enumeration describe the status available for a delivery
 * 
 * @author Guillaume Lamirand
 */
public enum DeliveryStatusDTO
{
  CREATED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return DeliveryCommon.getMessages().deliveryCreated();
    }
  },
  MODIFIED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return DeliveryCommon.getMessages().deliveryModified();
    }
  },
  GENERATING
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return DeliveryCommon.getMessages().deliveryGenerating();
    }
  },
  GENERATED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return DeliveryCommon.getMessages().deliveryGenerated();
    }
  },
  DELIVERED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
      return DeliveryCommon.getMessages().deliveryDelivered();
    }
  };

  /**
   * Get the DeliveryStatusDTO by providing it's label
   *
   * @param pLabel
   *          The DeliveryStatusDTO label
   * @return {@link DeliveryStatusDTO} the DeliveryStatusDTO who match the label
   */
  public static DeliveryStatusDTO getByLabel(final String pLabel)
  {
    DeliveryStatusDTO returnStatus = null;
    final DeliveryStatusDTO[] values = DeliveryStatusDTO.values();
    for (final DeliveryStatusDTO deliveryStatusDTO : values)
    {
      if (deliveryStatusDTO.getLabel().equals(pLabel))
      {
        returnStatus = deliveryStatusDTO;
      }
    }
    return returnStatus;
  }

  /**
   * Get the DeliveryStatusDTO label
   *
   * @return {@link Label} the deliveryStatusDTOLabel
   */
  public abstract String getLabel();

  /**
   * Get all the DeliveryStatusDTO
   * 
   * @return The DeliveryStatusDTO list
   */
  public static List<DeliveryStatusDTO> list()
  {
    final List<DeliveryStatusDTO> statusList = new ArrayList<DeliveryStatusDTO>();
    final DeliveryStatusDTO[] values = DeliveryStatusDTO.values();
    Collections.addAll(statusList, values);
    return statusList;
  }
}
