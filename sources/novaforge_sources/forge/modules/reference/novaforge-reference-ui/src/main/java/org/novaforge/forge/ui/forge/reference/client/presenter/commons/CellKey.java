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
package org.novaforge.forge.ui.forge.reference.client.presenter.commons;

import com.google.gwt.view.client.ProvidesKey;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.RoleMappingObject;
import org.novaforge.forge.ui.forge.reference.shared.FileInfoDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleApplicationDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;

/**
 * @author lamirang
 */
public class CellKey
{

	/**
	 * /** The key provider that provides the unique ID of a role.
	 */
	public static final ProvidesKey<RoleDTO>            ROLE_KEY_PROVIDER             = new ProvidesKey<RoleDTO>()
	                                                                                  {
		                                                                                  @Override
		                                                                                  public Object getKey(
		                                                                                      final RoleDTO item)
		                                                                                  {
			                                                                                  return item == null ? null
			                                                                                      : item.getName();
		                                                                                  }
	                                                                                  };

	/**
	 * /** The key provider that provides the unique ID of a application role.
	 */
	public static final ProvidesKey<RoleApplicationDTO> ROLE_APPLICATION_KEY_PROVIDER = new ProvidesKey<RoleApplicationDTO>()
	                                                                                  {
		                                                                                  @Override
		                                                                                  public Object getKey(
		                                                                                      final RoleApplicationDTO item)
		                                                                                  {
			                                                                                  return item == null ? null
			                                                                                      : item
			                                                                                          .getRoleName();
		                                                                                  }
	                                                                                  };

	/**
	 * /** The key provider that provides the unique ID of a group.
	 */
	public static final ProvidesKey<RoleMappingObject>  ROLE_MAPPING_KEY_PROVIDER     = new ProvidesKey<RoleMappingObject>()
	                                                                                  {
		                                                                                  @Override
		                                                                                  public Object getKey(
		                                                                                      final RoleMappingObject item)
		                                                                                  {
			                                                                                  return item == null ? null
			                                                                                      : item
			                                                                                          .getProjectRole();
		                                                                                  }
	                                                                                  };

	public static final ProvidesKey<FileInfoDTO>        DOWLOAD_TOOL_KEY_PROVIDER     = new ProvidesKey<FileInfoDTO>()
	                                                                                  {

		                                                                                  @Override
		                                                                                  public Object getKey(
		                                                                                      final FileInfoDTO item)
		                                                                                  {

			                                                                                  return item == null ? null
			                                                                                      : item.getPath();
		                                                                                  }
	                                                                                  };

}
