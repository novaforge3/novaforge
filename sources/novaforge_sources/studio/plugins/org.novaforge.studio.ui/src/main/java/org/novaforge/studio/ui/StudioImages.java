/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */


package org.novaforge.studio.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Novastudio image descriptors manager
 *
 */
public class StudioImages 
{

	private static final URL baseURL = StudioUiPlugin.getDefault().getBundle().getEntry("/icons/");
	
	public static final ImageDescriptor OVERLAY_BUG = create("nf-bug-overlay.gif");
	
	
	private static ImageDescriptor create(String name) 
	{
		try 
		{
			return ImageDescriptor.createFromURL(new URL(baseURL, name));
		}
		catch (MalformedURLException e) 
		{
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
}