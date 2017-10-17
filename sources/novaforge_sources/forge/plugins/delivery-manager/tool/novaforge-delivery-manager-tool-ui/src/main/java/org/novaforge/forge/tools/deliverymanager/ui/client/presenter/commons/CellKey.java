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
package org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons;

import com.google.gwt.view.client.ProvidesKey;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ArtefactNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.BugTrackerIssueDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;

/**
 * @author BILET-JC
 */
public class CellKey
{

   /**
    * The key provider that provides the unique ID of a user.
    */
   public static final ProvidesKey<NodeDTO>            NODE_KEY_PROVIDER     = new ProvidesKey<NodeDTO>()
                                                                             {
                                                                                @Override
                                                                                public Object getKey(
                                                                                      final NodeDTO item)
                                                                                {
                                                                                   return item == null ? null
                                                                                         : item.getPath()
                                                                                               + "/"
                                                                                               + item.getName();
                                                                                }
                                                                             };
   /**
    * The key provider that provides the unique ID of a artefact.
    */
   public static final ProvidesKey<ArtefactNode>       ARTEFACT_KEY_PROVIDER = new ProvidesKey<ArtefactNode>()
                                                                             {
                                                                                @Override
                                                                                public Object getKey(
                                                                                      final ArtefactNode item)
                                                                                {
                                                                                   return item == null ? null
                                                                                         : item.getID();
                                                                                }
                                                                             };
   /**
    * The key provider that provides the unique ID of a bugtrackerissue.
    */
   public static final ProvidesKey<BugTrackerIssueDTO> BUG_KEY_PROVIDER      = new ProvidesKey<BugTrackerIssueDTO>()
                                                                             {
                                                                                @Override
                                                                                public Object getKey(
                                                                                      final BugTrackerIssueDTO item)
                                                                                {
                                                                                   return item == null ? null
                                                                                         : item.getId();
                                                                                }
                                                                             };

}
