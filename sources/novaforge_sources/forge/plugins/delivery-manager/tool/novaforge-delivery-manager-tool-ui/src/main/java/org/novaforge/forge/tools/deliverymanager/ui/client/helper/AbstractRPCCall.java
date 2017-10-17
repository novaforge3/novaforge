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

package org.novaforge.forge.tools.deliverymanager.ui.client.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.RPCInEvent;
import org.novaforge.forge.tools.deliverymanager.ui.client.event.RPCOutEvent;

public abstract class AbstractRPCCall<T> implements AsyncCallback<T>
{

   protected abstract void callService(AsyncCallback<T> cb);

   private void call(final int retriesLeft)
   {
      this.onRPCOut();

      this.callService(new AsyncCallback<T>()
      {
         @Override
         public void onFailure(final Throwable caught)
         {
            AbstractRPCCall.this.onRPCIn();
            GWT.log(caught.toString(), caught);
            try
            {
               throw caught;
            }
            catch (final StatusCodeException statusCodeException)
            {
               if (statusCodeException.getStatusCode() == 401)
               {
                  // Authentication/authorisation failure.
                  // Redirect to login page.
                  // TODO Use internationalization to display correct message
                  Window.alert("Votre session a expiré. Veuillez vous reconnecter au portail.");
               }
               else
               {
                  AbstractRPCCall.this.onFailure(statusCodeException);
               }
            }
            catch (final InvocationException invocationException)
            {
               if (retriesLeft <= 0)
               {
                  AbstractRPCCall.this.onFailure(invocationException);
               }
               else
               {
                  AbstractRPCCall.this.call(retriesLeft - 1); // retry call
               }
            }
            catch (final IncompatibleRemoteServiceException remoteServiceException)
            {
               Window.alert("The app may be out of date. Reload this page in your browser.");
            }
            catch (final SerializationException serializationException)
            {
               Window.alert("A serialization error occurred. Try again.");
            }
            catch (final RequestTimeoutException e)
            {
               Window.alert("This is taking too long, try again");
            }
            catch (final Throwable e)
            {// application exception
               AbstractRPCCall.this.onFailure(e);
            }
         }

         @Override
         public void onSuccess(final T result)
         {
            AbstractRPCCall.this.onRPCIn();
            AbstractRPCCall.this.onSuccess(result);
         }
      });
   }

   private void onRPCIn()
   {
      // Connection event
      DeliveryManagement.get().getEventBus().fireEvent(new RPCInEvent());
   }

   private void onRPCOut()
   {
      // deconnection event
      DeliveryManagement.get().getEventBus().fireEvent(new RPCOutEvent());
   }

   public void retry(final int retryCount)
   {
      this.call(retryCount);
   }
}
