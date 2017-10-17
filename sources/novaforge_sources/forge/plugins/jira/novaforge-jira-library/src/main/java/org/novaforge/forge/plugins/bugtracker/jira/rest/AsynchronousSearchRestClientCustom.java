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
package org.novaforge.forge.plugins.bugtracker.jira.rest;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.IssueRestClient.Expandos;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.jira.rest.client.internal.json.FilterJsonParser;
import com.atlassian.jira.rest.client.internal.json.GenericJsonArrayParser;
import com.atlassian.jira.rest.client.internal.json.SearchResultJsonParser;
import com.atlassian.util.concurrent.Promise;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.annotation.Nullable;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AsynchronousSearchRestClientCustom extends AbstractAsynchronousRestClient implements
    SearchRestClient
{

  private static final String                  START_AT_ATTRIBUTE          = "startAt";
  private static final String                  MAX_RESULTS_ATTRIBUTE       = "maxResults";
  private static final int                     MAX_JQL_LENGTH_FOR_HTTP_GET = 500;
  private static final String                  JQL_ATTRIBUTE               = "jql";
  private static final String                  FILTER_FAVOURITE_PATH       = "filter/favourite";
  private static final String                  FILTER_PATH_FORMAT          = "filter/%s";
  private static final String                  SEARCH_URI_PREFIX           = "search";
  private static final String                  EXPAND_ATTRIBUTE            = "expand";
  private static final String                  FIELDS_ATTRIBUTE            = "fields";
  private static final Function<IssueRestClient.Expandos, String> EXPANDO_TO_PARAM = new Function<IssueRestClient.Expandos, String>()
  {
    @Override
    public String apply(IssueRestClient.Expandos from)
    {
      return from.name().toLowerCase();
    }
  };
  private final URI                            searchUri;
  private final URI                            favouriteUri;
  private final URI                            baseUri;
  private final SearchResultJsonParser         searchResultJsonParser      = new SearchResultJsonParser();
  private final FilterJsonParser               filterJsonParser            = new FilterJsonParser();
  private final GenericJsonArrayParser<Filter> filtersParser               = GenericJsonArrayParser
                                                                               .create(new FilterJsonParser());

  public AsynchronousSearchRestClientCustom(URI baseUri, HttpClient asyncHttpClient)
  {
    super(asyncHttpClient);
    this.baseUri = baseUri;
    this.searchUri = UriBuilder.fromUri(baseUri).path(SEARCH_URI_PREFIX).build();
    this.favouriteUri = UriBuilder.fromUri(baseUri).path(FILTER_FAVOURITE_PATH).build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Promise<SearchResult> searchJql(@Nullable String jql)
  {
    return searchJql(jql, null, null, null, null);
  }

  public Promise<SearchResult> searchJql(@Nullable String jql, @Nullable Integer maxResults, @Nullable Integer startAt,
                                         @Nullable Set<String> fields)
  {
    return searchJql(jql, maxResults, startAt, fields, null);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Promise<Iterable<Filter>> getFavouriteFilters()
  {
    return getAndParse(favouriteUri, filtersParser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Promise<Filter> getFilter(URI filterUri)
  {
    return getAndParse(filterUri, filterJsonParser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Promise<Filter> getFilter(long id)
  {
    return getFilter(UriBuilder.fromUri(baseUri).path(String.format(FILTER_PATH_FORMAT, id)).build());
  }

  public Promise<SearchResult> searchJql(@Nullable String jql, @Nullable Integer maxResults,
      @Nullable Integer startAt, @Nullable Set<String> fields, List<Expandos> pExpand)
  {
    List<Expandos> listExpandos = new ArrayList<>();
    if (pExpand != null)
    {
      listExpandos.addAll(pExpand);
    }
    ImmutableList<Expandos> immutableListExpandos = ImmutableList.of(Expandos.SCHEMA, Expandos.NAMES);
    listExpandos.addAll(immutableListExpandos);
    final Iterable<String> expandosValues = Iterables.transform(listExpandos, EXPANDO_TO_PARAM);
    final String notNullJql = StringUtils.defaultString(jql);
    if (notNullJql.length() > MAX_JQL_LENGTH_FOR_HTTP_GET)
    {
      return searchJqlImplPost(maxResults, startAt, expandosValues, notNullJql, fields);
    }
    else
    {
      return searchJqlImplGet(maxResults, startAt, expandosValues, notNullJql, fields);
    }

  }

  private Promise<SearchResult> searchJqlImplPost(@Nullable Integer maxResults, @Nullable Integer startAt,
      Iterable<String> expandosValues, String jql, @Nullable Set<String> fields)
  {
    final JSONObject postEntity = new JSONObject();

    try
    {
      postEntity.put(JQL_ATTRIBUTE, jql).put(EXPAND_ATTRIBUTE, ImmutableList.copyOf(expandosValues))
          .putOpt(START_AT_ATTRIBUTE, startAt).putOpt(MAX_RESULTS_ATTRIBUTE, maxResults);

      if (fields != null)
      {
        postEntity.put(FIELDS_ATTRIBUTE, fields); // putOpt doesn't work with collections
      }
    }
    catch (JSONException e)
    {
      throw new RestClientException(e);
    }
    return postAndParse(searchUri, postEntity, searchResultJsonParser);
  }

  private Promise<SearchResult> searchJqlImplGet(@Nullable Integer maxResults, @Nullable Integer startAt,
                                                 Iterable<String> expandosValues, String jql,
                                                 @Nullable Set<String> fields)
  {
    final UriBuilder uriBuilder = UriBuilder.fromUri(searchUri).queryParam(JQL_ATTRIBUTE, jql)
                                            .queryParam(EXPAND_ATTRIBUTE, Joiner.on(",").join(expandosValues));

    if (fields != null)
    {
      uriBuilder.queryParam(FIELDS_ATTRIBUTE, Joiner.on(",").join(fields));
    }
    addOptionalQueryParam(uriBuilder, MAX_RESULTS_ATTRIBUTE, maxResults);
    addOptionalQueryParam(uriBuilder, START_AT_ATTRIBUTE, startAt);

    return getAndParse(uriBuilder.build(), searchResultJsonParser);
  }

  private void addOptionalQueryParam(final UriBuilder uriBuilder, final String key, final Object... values)
  {
    if (values != null && values.length > 0 && values[0] != null)
    {
      uriBuilder.queryParam(key, values);
    }
  }

  public Promise<SearchResult> searchJql(@Nullable String jql, List<Expandos> pExpand)
  {

    return searchJql(jql, null, null, null, pExpand);
  }

}
