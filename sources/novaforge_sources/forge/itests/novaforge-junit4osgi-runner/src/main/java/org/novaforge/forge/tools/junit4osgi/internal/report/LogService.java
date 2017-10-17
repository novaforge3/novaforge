/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tools.junit4osgi.internal.report;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * An implementation of the log service to collect logged messages.
 * This service implementation is also {@link BundleActivator} and is
 * activated when the embedded OSGi platform starts.
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class LogService
{

   /**
    * Default output stream (not collected).
    */
   private final StringOutputStream m_defaultStream;

   /**
    * Collected output stream.
    */
   private StringOutputStream m_outputStream;

   /**
    * Creates the log service object.
    */
   public LogService() {
      m_defaultStream = new StringOutputStream();
   }

   /**
    * Enables the log messages collection.
    */
   public void enableOutputStream() {
      m_outputStream = new StringOutputStream();
   }

   /**
    * Get collected log messages.
    * @return the String containing the logged messages.
    */
   public String getLoggedMessages() {
      return m_outputStream.toString();
   }

   /**
    * Re-initializes the collected message list.
    */
   public void reset() {
      m_outputStream = null;
   }

   /**
    * Logs a message.
    * @param arg0 the log level
    * @param arg1 the message
    * @see org.osgi.service.log.LogService#log(int, java.lang.String)
    */
   public void log(int arg0, String arg1) {
      write(computeLogMessage(arg0, arg1, null));
   }

   /**
    * Logs a message with an attached exception.
    * @param arg0 the log level
    * @param arg1 the message
    * @param arg2 the associated exception
    * @see org.osgi.service.log.LogService#log(int, java.lang.String, java.lang.Throwable)
    */
   public void log(int arg0, String arg1, Throwable arg2) {
      write(computeLogMessage(arg0, arg1, arg2));
   }

   /**
    * Logs a message raised by the given service reference.
    * @param arg0 the service reference
    * @param arg1 the log level
    * @param arg2 the message
    * @see org.osgi.service.log.LogService#log(org.osgi.framework.ServiceReference, int, java.lang.String)
    */
   public void log(ServiceReference arg0, int arg1, String arg2) {
      write(computeLogMessage(arg1, arg2, null));
   }

   /**
    * Logs a message raised by the given service reference
    * associated with an exception.
    * @param arg0 the service reference
    * @param arg1 the log level
    * @param arg2 the message
    * @param arg3 the exception
    * @see org.osgi.service.log.LogService#log(org.osgi.framework.ServiceReference, int, java.lang.String)
    */
   public void log(ServiceReference arg0, int arg1, String arg2, Throwable arg3) {
      write(computeLogMessage(arg1, arg2, arg3));
   }

   /**
    * Computes the string from the message.
    * @param level the log level
    * @param msg the message
    * @param exception the exception (can be <code>null</code>
    * @return the resulting String
    */
   private String computeLogMessage(int level, String msg, Throwable exception) {
      String message = null;
      switch (level) {
      case org.osgi.service.log.LogService.LOG_DEBUG:
         message = "[DEBUG] " + msg;
         break;
      case org.osgi.service.log.LogService.LOG_ERROR:
         message = "[ERROR] " + msg;
         break;
      case org.osgi.service.log.LogService.LOG_INFO:
         message = "[INFO] " + msg;
         break;
      case org.osgi.service.log.LogService.LOG_WARNING:
         message = "[WARNING] " + msg;
         break;
      default:
         break;
      }

      if (exception != null) {
         message = message + " : " + exception.getMessage() + "\n";
      }

      return message;
   }

   /**
    * Writes the given message in the adequate output stream.
    * @param log the message
    */
   public void write(String log) {
      if (m_outputStream != null) {
         m_outputStream.write(log);
      } else {
         m_defaultStream.write(log);
      }
   }

   /**
    * Stars the log service implementation:
    * Registers the service.
    * @param bc the bundle context.
    * @throws Exception should not happen.
    * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
    */
   public void start(BundleContext bc) throws Exception {
      bc.registerService(LogService.class.getName(), this, null);
   }

   /**
    * Stops the log service implementation.
    * Does nothing.
    * @param arg0 the bundle context
    * @throws Exception should not happen.
    * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
    */
   public void stop(BundleContext arg0) throws Exception {
      // Nothing to do.

   }

}