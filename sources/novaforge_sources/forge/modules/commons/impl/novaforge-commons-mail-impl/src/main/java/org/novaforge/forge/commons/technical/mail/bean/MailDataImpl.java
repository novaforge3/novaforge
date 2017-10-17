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
/**
 * 
 */
package org.novaforge.forge.commons.technical.mail.bean;

import org.novaforge.forge.commons.technical.mail.model.MailData;

import java.io.Serializable;

/**
 * @author sbenoist
 */
public class MailDataImpl implements MailData, Serializable
{
  /**
    * 
    */
  private static final long serialVersionUID = -5120511242289990410L;

  private String            emailTo;

  private String            subject;

  private String            body;

  private String            from;

  /**
   * This constructs a MailData object without from information
   * 
   * @param emailTo
   * @param subject
   * @param body
   */
  public MailDataImpl(final String emailTo, final String subject, final String body)
  {
    super();
    this.emailTo = emailTo;
    this.subject = subject;
    this.body = body;
  }

  /**
   * This constructs a MailData object
   * 
   * @param from
   * @param emailTo
   * @param subject
   * @param body
   */
  public MailDataImpl(final String emailTo, final String subject, final String body, final String from)
  {
    super();
    this.emailTo = emailTo;
    this.subject = subject;
    this.body = body;
    this.from = from;
  }

  /*
   * (non-Javadoc)
   * @see org.novaforge.forge.commons.technical.mail.model.MailData#getEmailTo()
   */
  @Override
  public String getEmailTo()
  {
    return emailTo;
  }

  /*
   * (non-Javadoc)
   * @see org.novaforge.forge.commons.technical.mail.model.MailData#setEmailTo(java.lang.String)
   */
  @Override
  public void setEmailTo(final String pEmailTo)
  {
    emailTo = pEmailTo;
  }

  /*
   * (non-Javadoc)
   * @see org.novaforge.forge.commons.technical.mail.model.MailData#getSubject()
   */
  @Override
  public String getSubject()
  {
    return subject;
  }

  /*
   * (non-Javadoc)
   * @see org.novaforge.forge.commons.technical.mail.model.MailData#setSubject(java.lang.String)
   */
  @Override
  public void setSubject(final String pSubject)
  {
    subject = pSubject;
  }

  /*
   * (non-Javadoc)
   * @see org.novaforge.forge.commons.technical.mail.model.MailData#getBody()
   */
  @Override
  public String getBody()
  {
    return body;
  }

  /*
   * (non-Javadoc)
   * @see org.novaforge.forge.commons.technical.mail.model.MailData#setBody(java.lang.String)
   */
  @Override
  public void setBody(final String pBody)
  {
    body = pBody;
  }

  @Override
  public String getFrom()
  {
    return from;
  }

  @Override
  public void setFrom(final String pFrom)
  {
    from = pFrom;
  }

  @Override
  public MailData buildMail(final String pEmailTo, final String pSubject, final String pBody)
  {
    return new MailDataImpl(pEmailTo, pSubject, pBody);
  }

  @Override
  public String toString()
  {
    return "MailDataImpl [emailTo=" + emailTo + ", subject=" + subject + ", body=" + body + ", from=" + from + "]";
  }

}
