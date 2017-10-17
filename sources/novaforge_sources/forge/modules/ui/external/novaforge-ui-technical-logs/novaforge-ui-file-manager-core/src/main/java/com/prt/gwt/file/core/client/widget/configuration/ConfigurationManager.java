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
package com.prt.gwt.file.core.client.widget.configuration;

import com.prt.gwt.file.core.client.vo.ProfileVO;

import java.util.ArrayList;
import java.util.List;

public final class ConfigurationManager
{

	private static final long      DEFAULT_LINE_COUNT = 300;
	private static final String    DEFAULT_FILE_MASK  = "*";

	private static List<ProfileVO> profiles;
	private static ProfileVO       currentProfile;

	private ConfigurationManager()
	{
	}

	public static List<ProfileVO> getProfiles()
	{
		return profiles;
	}

	public static void setProfiles(final List<ProfileVO> pProfiles)
	{
		profiles = new ArrayList<ProfileVO>();
		profiles.addAll(pProfiles);

		for (final ProfileVO profileVO : profiles)
		{
			if ((profileVO.getDefaultFileMask() == null) || ("".equals(profileVO.getDefaultFileMask())))
			{
				profileVO.setDefaultFileMask(DEFAULT_FILE_MASK);
			}
			if (profileVO.getLinesToShow() == 0)
			{
				profileVO.setLinesToShow(DEFAULT_LINE_COUNT);
			}
		}
		if (profiles.size() >= 1)
		{
			setCurrentProfile(profiles.get(0));
		}
	}

	public static ProfileVO getCurrentProfile()
	{
		return currentProfile;
	}

	public static void setCurrentProfile(final ProfileVO vo)
	{
		currentProfile = vo;
	}

}
