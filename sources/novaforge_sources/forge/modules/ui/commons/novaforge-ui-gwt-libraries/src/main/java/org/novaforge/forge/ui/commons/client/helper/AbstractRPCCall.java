/*
 * Copyright (c) 2011- 2015, BULL SAS, NovaForge Version 3 and above.
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
package org.novaforge.forge.ui.commons.client.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.SerializationException;

public abstract class AbstractRPCCall<T> implements AsyncCallback<T>
{

  protected abstract void callService(AsyncCallback<T> cb);

  private void call(final int retriesLeft)
  {
    onRPCOut();

    callService(new AsyncCallback<T>()
    {
      @Override
      public void onFailure(final Throwable pThrowable)
      {
        onRPCIn();
        GWT.log(pThrowable.toString(), pThrowable);
        try
        {
          throw pThrowable;
        }
        catch (final InvocationException invocationException)
        {
          if (retriesLeft <= 0)
          {
            AbstractRPCCall.this.onFailure(invocationException);
          }
          else
          {
            call(retriesLeft - 1); // retry call
          }
        }
        catch (final IncompatibleRemoteServiceException pRemoteServiceException)
        {
          Window.alert("The app may be out of date. Reload this page in your browser.");
        }
        catch (final SerializationException pSerializationException)
        {
          Window.alert("A serialization error occurred. Try again.");
        }
        catch (final RequestTimeoutException pRequestTimeoutException)
        {
          Window.alert("This is taking too long, try again");
        }
        catch (final Throwable pThrow)
        {
          // application exception
          AbstractRPCCall.this.onFailure(pThrow);
        }
      }

      @Override
      public void onSuccess(final T result)
      {
        onRPCIn();
        AbstractRPCCall.this.onSuccess(result);
      }
    });
  }

  private void onRPCIn()
  {
    // Connection event
    getEventBus().fireEvent(new RPCInEvent());
  }

  private void onRPCOut()
  {
    // Deconnection event
    getEventBus().fireEvent(new RPCOutEvent());
  }

  protected abstract EventBus getEventBus();

  public void retry(final int retryCount)
  {
    call(retryCount);
  }
}
