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
package org.novaforge.forge.plugins.mailinglist.sympa.utils;

import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListType;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;

import java.util.UUID;

/**
 * @author sbenoist
 */
public final class Utils
{
  /**
   * The unique accessible role in SYMPA for NovaForge's users : potential list owner or subscriber
   */
  public static final String  DEFAULT_SYMPA_USER_ROLE     = "owner/subscriber";

  public static final String  FR_SYMPA_SUPPORTED_LANGUAGE = "fr_FR";

  public static final String  EN_SYMPA_SUPPORTED_LANGUAGE = "en_US";

  private static final String PRIVATE_TEMPLATE            = "private_working_group";

  private static final String PUBLIC_TEMPLATE             = "discussion_list";

  private static final String NEWSLETTER_TEMPLATE         = "news-letter";

  private static final String HOTLINE_TEMPLATE            = "hotline";

  private static final String INTRANET_TEMPLATE           = "intranet_list";

  private static final String FORUM_TEMPLATE              = "public_web_forum";

  public static final String buildGecos(final String pFirstName, final String pName)
  {
    return pFirstName + " " + pName;
  }

  public static final String getSympaProjectName(final InstanceConfiguration pInstanceConfiguration)
  {
    return pInstanceConfiguration.getForgeProjectId() + "_" + pInstanceConfiguration.getConfigurationId();
  }

  public static final String getSympaProjectId(final InstanceConfiguration pInstanceConfiguration)
  {
    return pInstanceConfiguration.getForgeProjectId() + "/" + UUID.randomUUID().toString();
  }

  public static final String toSympaLanguage(final String pForgeLanguage)
  {
    String lang = null;
    if ("fr".equalsIgnoreCase(pForgeLanguage))
    {
      lang = FR_SYMPA_SUPPORTED_LANGUAGE;
    }
    else
    // default language
    {
      lang = EN_SYMPA_SUPPORTED_LANGUAGE;
    }
    return lang;
  }

  public static MailingListType toMailingListType(final String pTemplate)
  {
    MailingListType result = null;
    if (pTemplate != null)
    {
      switch (pTemplate)
      {
        case PRIVATE_TEMPLATE:
          result = MailingListType.PRIVATE_LIST;
          break;
        case PUBLIC_TEMPLATE:
          result = MailingListType.PUBLIC_LIST;
          break;
        case NEWSLETTER_TEMPLATE:
          result = MailingListType.NEWSLETTER_LIST;
          break;
        case HOTLINE_TEMPLATE:
          result = MailingListType.HOTLINE_LIST;
          break;
        case INTRANET_TEMPLATE:
          result = MailingListType.INTRANET_LIST;
          break;
        case FORUM_TEMPLATE:
          result = MailingListType.FORUM_LIST;
          break;
        default:
          throw new IllegalArgumentException("this template is not referenced");
      }
    }
    return result;
  }

  public static String toTemplate(final MailingListType pMailingListType)
  {
    String result = "undefined";
    switch (pMailingListType)
    {
      case PUBLIC_LIST:
        result = PUBLIC_TEMPLATE;
        break;
      case PRIVATE_LIST:
        result = PRIVATE_TEMPLATE;
        break;
      case NEWSLETTER_LIST:
        result = NEWSLETTER_TEMPLATE;
        break;
      case HOTLINE_LIST:
        result = HOTLINE_TEMPLATE;
        break;
      case INTRANET_LIST:
        result = INTRANET_TEMPLATE;
        break;
      case FORUM_LIST:
        result = FORUM_TEMPLATE;
        break;
      default:
        throw new IllegalArgumentException("this type is not referenced");
    }
    return result;
  }

  public static String buildDefaultListname(final String pProjectId, final String pSuffix)
  {
    return pProjectId + "-" + pSuffix;
  }
}
