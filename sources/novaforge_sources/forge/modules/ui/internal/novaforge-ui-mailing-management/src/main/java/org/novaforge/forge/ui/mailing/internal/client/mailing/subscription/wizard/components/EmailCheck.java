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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.wizard.components;

import org.novaforge.forge.portal.i18n.Messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aimen Merkich
 */
public class EmailCheck
{

  private static final String  EMAIL_REGEX  = "^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
  private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
  private final String email;

  public EmailCheck(final String pEmail)
  {

    email = pEmail;

  }
 
  public String checkEmail()
  {

    String returnMessage = "";
    String emailString =  email;
    if (!emailString.trim().isEmpty())
    {
     
        final String[] emails = emailString.split(";");
        if (emails.length != 0){
          Matcher matcher;
        for (final String email : emails)
        {
          matcher = emailPattern.matcher(email);
          if (!matcher.find())
          {
            returnMessage = Messages.MAILING_LISTS_EMAIL_INVALID_REGEX;
          }
        }
      }else
      {
        returnMessage =  Messages.MAILING_LISTS_EMAIL_INVALID_SEPARATOR;
      }
    }
    else
    {
      returnMessage =  Messages.MAILING_LISTS_EMAIL_EMPTY;
    }
    return returnMessage;
    
  
  }

}
