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
package org.novaforge.forge.ui.mailing.internal.module.task;

import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author Patricia Falaise
 */
public class DeleteMailingListCallable implements Callable<DeleteMailingListCallable.DeleteResult>
{
  /**
   * The {@link UUID} defining the mailing instance
   */
  private final UUID            mailingListInstance;
  /**
   * The {@link MailingListBean} to create
   */
  private final String mailingList;
  /**
   * The user requested the mailing list deletion
   */
  private final String          user;
  /**
   * Default constructor
   *
   * @param pMailingListInstance
   *          the {@link UUID} defining the mailing instance
   * @param pMailingListBean
   *          the mailing list to delete
   * @param pUser
   *          the user who wants to delete the mailing list
   * @param pLocale
   *          the current user locale
   */
  public DeleteMailingListCallable(final UUID pMailingListInstance, final String pMailingList,
      final String pUser)
  {
    mailingListInstance = pMailingListInstance;
    mailingList = pMailingList;
    user = pUser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteMailingListCallable.DeleteResult call() throws Exception
  {
    boolean isSuccess = false;
    try
    {
      MailingModule.getMailingListCategoryService().closeMailingList(null, mailingListInstance.toString(),
          user, mailingList);
      isSuccess = true;
    }
    catch (final Exception e)
    {
      // Ignore exception
    }
    return new DeleteResult(mailingList, isSuccess);
  }

  /**
   * This class defines the value returns by {@link DeleteMailingListCallable} when task is done
   *
   * @author Patricia Falaise
   */
  public class DeleteResult
  {
    /**
     * The {@link MailingList} which has been used
     */
    private final String mailingList;
    /**
     * <code>true</code> if the mailing list has been successfuly created
     */
    private final boolean         success;

    public DeleteResult(final String pMailingList, final boolean pIsSuccess)
    {
      mailingList = pMailingList;
      success = pIsSuccess;
    }

    /**
     * Return the mailing list bean
     *
     * @return the mailinglistName
     */
    public String getMailinglistName()
    {
      return mailingList;
    }

    /**
     * @return the success
     */
    public boolean isSuccess()
    {
      return success;
    }

  }
}