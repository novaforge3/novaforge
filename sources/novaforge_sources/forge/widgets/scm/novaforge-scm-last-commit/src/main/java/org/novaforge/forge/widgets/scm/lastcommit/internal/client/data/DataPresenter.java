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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import de.nlh.graphics2dimages.FixedWidthGraphics2DImage;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.categories.scm.SCMCategoryService;
import org.novaforge.forge.core.plugins.categories.scm.SCMCommitBean;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.ui.dashboard.client.modules.AbstractWidgetDataPresenter;
import org.novaforge.forge.ui.portal.client.util.WidgetColorGenerator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.admin.PropertiesFactory;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.component.ColumnDateGenerator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.component.ColumnIconGenerator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.component.ColumnUserGenerator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.component.RepositoryItemGenerator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container.CommitItemProperty;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container.CommitsContainer;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container.RepositoriesContainer;
import org.novaforge.forge.widgets.scm.lastcommit.internal.module.LastCommitModule;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * Presenter used to manage data view fo lastcommit module
 * 
 * @author Guillaume Lamirand
 */
public class DataPresenter extends AbstractWidgetDataPresenter implements Serializable
{
  /**
   * 
   */
  private static final int                       REPOSITORY_SIZE   = 16;

  /**
   * Serial version id used for serialization
   */
  private static final long                      serialVersionUID  = 1158368564534063818L;

  /**
   * View associated to this presenter
   */
  private final DataView                         view;
  /**
   * Random object used to generate random color.
   */
  private final WidgetColorGenerator             colorGenerator    = new WidgetColorGenerator();
  private final List<String>                     enableRepository  = new ArrayList<String>();
  private final Map<String, List<SCMCommitBean>> repositoryMapping = new HashMap<String, List<SCMCommitBean>>();
  private CommitsContainer      commitsContainer;
  private RepositoriesContainer repositoriesContainer;

  /**
   * Default constructor
   * 
   * @param pWidgetContext
   *          the widgetContext
   * @param pView
   *          the view associated to this presenter
   */
  public DataPresenter(final WidgetContext pWidgetContext, final DataView pView)
  {
    super(pWidgetContext);
    // Init the view
    view = pView;
    initCommitsList();
    initRepositoriesList();

  }

  private void initCommitsList()
  {
    commitsContainer = new CommitsContainer();
    view.getCommitsTable().setContainerDataSource(commitsContainer);

    view.getCommitsTable().addGeneratedColumn(CommitItemProperty.DATE.getPropertyId(),
        new ColumnDateGenerator());
    view.getCommitsTable().addGeneratedColumn(CommitItemProperty.ICON.getPropertyId(),
        new ColumnIconGenerator());
    view.getCommitsTable().addGeneratedColumn(CommitItemProperty.AUTHOR_PROFILE.getPropertyId(),
        new ColumnUserGenerator(getEventBus()));

    // Define visibles columns
    view.getCommitsTable().setVisibleColumns(CommitItemProperty.ICON.getPropertyId(),
        CommitItemProperty.REVISION.getPropertyId(), CommitItemProperty.DATE.getPropertyId(),
        CommitItemProperty.CHANGES.getPropertyId(), CommitItemProperty.AUTHOR_PROFILE.getPropertyId(),
        CommitItemProperty.COMMENT.getPropertyId());

    view.getCommitsTable().setColumnHeader(CommitItemProperty.ICON.getPropertyId(), "");

    // Define special column width
    view.getCommitsTable().setColumnExpandRatio(CommitItemProperty.COMMENT.getPropertyId(), 1);
  }

  private void initRepositoriesList()
  {
    repositoriesContainer = new RepositoriesContainer();
    view.getRepositoryLayout().setContainerDataSource(repositoriesContainer);
    view.getRepositoryLayout().setItemGenerator(new RepositoryItemGenerator(this));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshData()
  {
    repositoryMapping.clear();
    try
    {
      final Map<String, List<String>> applicationsByProject = getWidgetContext().getApplicationsByProject();
      final String projectId = applicationsByProject.keySet().iterator().next();
      final List<String> instances = getWidgetContext().getApplicationsByProject().get(projectId);
      for (final String instanceUUID : instances)
      {
        final ProjectApplication application = LastCommitModule.getApplicationPresenter().getApplication(
            projectId, UUID.fromString(instanceUUID));
        final SCMCategoryService pluginCategoryService = LastCommitModule.getPluginsManager()
            .getPluginCategoryService(application.getPluginUUID().toString(), SCMCategoryService.class);

        final UUID forgeId = LastCommitModule.getForgeId();
        final String currentUser = LastCommitModule.getCurrentUser();
        final String repositoryId = pluginCategoryService.getRepositoryId(forgeId.toString(), instanceUUID);
        final List<SCMCommitBean> commits = pluginCategoryService.getCommits(forgeId.toString(),
            instanceUUID, currentUser, PropertiesFactory.readProperties(getWidgetContext().getProperties()));
        repositoryMapping.put(repositoryId, commits);
      }
    }
    catch (final Exception e)
    {
      // TODO: handle exception
      e.printStackTrace();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshComponent()
  {
    // Clean previous data
    view.clearInstances();
    colorGenerator.resetUsedColors();
    enableRepository.clear();
    commitsContainer.removeAllContainerFilters();
    commitsContainer.removeAllItems();
    repositoriesContainer.removeAllContainerFilters();
    repositoriesContainer.removeAllItems();

    // Browse new data
    final Set<Entry<String, List<SCMCommitBean>>> entrySet = repositoryMapping.entrySet();
    for (final Entry<String, List<SCMCommitBean>> entry : entrySet)
    {
      final String repositoryId = entry.getKey();
      final List<SCMCommitBean> commits = entry.getValue();
      enableRepository.add(repositoryId);
      final FixedWidthGraphics2DImage icon = generateRepositoryIcon(repositoryId);
      final StreamResource repositoryIcon = icon.getResource();
      repositoriesContainer.addRepository(repositoryId, repositoryIcon);

      commitsContainer.addCommits(repositoryId, repositoryIcon, commits);

    }
    refreshLocalized(getCurrentLocale());

  }

  private FixedWidthGraphics2DImage generateRepositoryIcon(final String pName)
  {
    final String iconName = pName + System.currentTimeMillis() + ".png";
    final Color color = colorGenerator.getNextColor();
    return new FixedWidthGraphics2DImage(iconName, REPOSITORY_SIZE, REPOSITORY_SIZE)
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -67968535689302630L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void paint(final Graphics2D g)
      {
        g.setColor(color);
        g.fillRect(0, 0, width - 1, height - 1);
        g.setColor(color);
        g.drawRect(0, 0, width - 1, height - 1);
      }

    };
  }

  public void onRepositoryClick(final String repositoryId, final Button pButton)
  {
    if (enableRepository.contains(repositoryId))
    {
      enableRepository.remove(repositoryId);
      pButton.setIcon(generateDisableIcon(repositoryId).getResource());
    }
    else
    {
      enableRepository.add(repositoryId);
      final StreamResource iconResource = (StreamResource) repositoriesContainer.getContainerProperty(
          repositoryId, CommitItemProperty.ICON.getPropertyId()).getValue();
      pButton.setIcon(iconResource);
    }
    commitsContainer.removeAllContainerFilters();
    final List<Filter> repositoryFilter = new ArrayList<Filter>();
    for (final String repository : enableRepository)
    {
      repositoryFilter.add(new SimpleStringFilter(CommitItemProperty.REPOSITORY.getPropertyId(), repository,
          true, false));
    }
    commitsContainer
        .addContainerFilter(new Or(repositoryFilter.toArray(new Filter[repositoryFilter.size()])));
  }

  private FixedWidthGraphics2DImage generateDisableIcon(final String pRepositoryId)
  {

    final String iconName = pRepositoryId + System.currentTimeMillis() + ".png";
    return new FixedWidthGraphics2DImage(iconName, REPOSITORY_SIZE,
        REPOSITORY_SIZE)
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -67968535689302630L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void paint(final Graphics2D g)
      {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, width - 1, height - 1);
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width - 1, height - 1);
      }

    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);
  }

}
