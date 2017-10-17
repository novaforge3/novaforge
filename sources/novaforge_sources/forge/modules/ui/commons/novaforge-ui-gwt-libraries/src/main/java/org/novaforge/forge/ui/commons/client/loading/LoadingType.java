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
package org.novaforge.forge.ui.commons.client.loading;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author caseryj
 */
public enum LoadingType
{
   CIRCLESMALL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderSMALL();
      }
   },
   CIRCLEMEDIUM
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderMEDIUM();
      }
   },
   CIRCLEBIG
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBIG();
      }
   },
   CIRCLESMALLWITHOUTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderSMALL();
      }
   },
   CIRCLEMEDIUMWITHOUTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderMEDIUM();
      }
   },
   CIRCLEBIGWITHOUTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBIG();
      }
   },
   CIRCLESMALLRIGHTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getRightLabel()
      {
         return LoadingPanel.getMessages().loadingDefault();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderSMALL();
      }
   },
   CIRCLEMEDIUMRIGHTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getRightLabel()
      {
         return LoadingPanel.getMessages().loadingDefault();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderMEDIUM();
      }
   },
   CIRCLEBIGRIGHTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getRightLabel()
      {
         return LoadingPanel.getMessages().loadingDefault();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBIG();
      }
   },
   BARSMALL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarSMALL();
      }
   },
   BARMEDIUM
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarMEDIUM();
      }
   },
   BARBIG
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarBIG();
      }
   },
   BARSMALLWITHOUTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarSMALL();
      }
   },
   BARMEDIUMWITHOUTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarMEDIUM();
      }
   },
   BARBIGWITHOUTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarBIG();
      }
   },
   BARSMALLRIGHTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getRightLabel()
      {
         return LoadingPanel.getMessages().loadingDefault();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarSMALL();
      }
   },
   BARMEDIUMRIGHTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getRightLabel()
      {
         return LoadingPanel.getMessages().loadingDefault();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarMEDIUM();
      }
   },
   BARBIGRIGHTLABEL
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public String getBottomLabel()
      {
         return null;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getRightLabel()
      {
         return LoadingPanel.getMessages().loadingDefault();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public ImageResource getImage()
      {
         return LoadingPanel.getRessources().loaderBarBIG();
      }
   };

   /**
    * Get the label to show under the loader image
    * 
    * @return the Bottom Label
    */
   public String getBottomLabel()
   {
      return LoadingPanel.getMessages().loadingDefault();
   }

   /**
    * Get the label to show at the right of the loader image
    * 
    * @return the Right Label
    */
   public String getRightLabel()
   {
      return null;
   }

   /**
    * Get the loader image
    * 
    * @return the loader image
    */
   public abstract ImageResource getImage();
}
