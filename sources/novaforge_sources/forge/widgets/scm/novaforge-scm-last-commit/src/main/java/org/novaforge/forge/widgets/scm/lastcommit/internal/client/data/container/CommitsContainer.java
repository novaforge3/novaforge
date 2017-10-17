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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container;

import com.google.common.base.Strings;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.StreamResource;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.plugins.categories.scm.SCMCommitBean;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.module.LastCommitModule;

import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class CommitsContainer extends IndexedContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public CommitsContainer()
  {
    super();
    addContainerProperty(CommitItemProperty.ICON.getPropertyId(), StreamResource.class, null);
    addContainerProperty(CommitItemProperty.REPOSITORY.getPropertyId(), String.class, null);
    addContainerProperty(CommitItemProperty.REVISION.getPropertyId(), String.class, null);
    addContainerProperty(CommitItemProperty.CHANGES.getPropertyId(), Long.class, null);
    addContainerProperty(CommitItemProperty.AUTHOR_LOGIN.getPropertyId(), String.class, null);
    addContainerProperty(CommitItemProperty.AUTHOR_EMAIL.getPropertyId(), String.class, null);
    addContainerProperty(CommitItemProperty.AUTHOR_PROFILE.getPropertyId(), UserProfile.class, null);
    addContainerProperty(CommitItemProperty.COMMENT.getPropertyId(), String.class, null);
    addContainerProperty(CommitItemProperty.DATE.getPropertyId(), Date.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add {@link SCMCommitBean} into container
   * 
   * @param pInstanceInfo
   *          the instance info if attached to the commits
   * @param pCommits
   *          {@link SCMCommitBean} to add
   */
  public void addCommits(final String pRepository, final StreamResource pIcon,
      final List<SCMCommitBean> pCommits)
  {
    for (final SCMCommitBean scmCommitBean : pCommits)
    {
      final Object itemId = addItem();
      getContainerProperty(itemId, CommitItemProperty.ICON.getPropertyId()).setValue(pIcon);
      getContainerProperty(itemId, CommitItemProperty.REPOSITORY.getPropertyId()).setValue(pRepository);
      getContainerProperty(itemId, CommitItemProperty.REVISION.getPropertyId()).setValue(
          scmCommitBean.getRevision());
      getContainerProperty(itemId, CommitItemProperty.CHANGES.getPropertyId()).setValue(
          scmCommitBean.getChanges());
      getContainerProperty(itemId, CommitItemProperty.COMMENT.getPropertyId()).setValue(
          scmCommitBean.getComment());
      try
      {
        getContainerProperty(itemId, CommitItemProperty.AUTHOR_LOGIN.getPropertyId()).setValue(
            scmCommitBean.getAuthor());
        getContainerProperty(itemId, CommitItemProperty.AUTHOR_EMAIL.getPropertyId()).setValue(
            scmCommitBean.getAuthorEmail());
        if (!Strings.isNullOrEmpty(scmCommitBean.getAuthor()))
        {
          getContainerProperty(itemId, CommitItemProperty.AUTHOR_PROFILE.getPropertyId()).setValue(
              LastCommitModule.getUserPresenter().getUserProfile(scmCommitBean.getAuthor()));
        }
        else if (!Strings.isNullOrEmpty(scmCommitBean.getAuthorEmail()))
        {
          getContainerProperty(itemId, CommitItemProperty.AUTHOR_PROFILE.getPropertyId()).setValue(
              LastCommitModule.getUserPresenter().getUserProfileFromEmail(scmCommitBean.getAuthorEmail()));

        }
      }
      catch (final UserServiceException e)
      {
        // Nothing to do in the case of UserServiceException

      }
      getContainerProperty(itemId, CommitItemProperty.DATE.getPropertyId()).setValue(scmCommitBean.getDate());
    }

    sort(new Object[] { CommitItemProperty.DATE.getPropertyId() }, new boolean[] { false });
  }

}
