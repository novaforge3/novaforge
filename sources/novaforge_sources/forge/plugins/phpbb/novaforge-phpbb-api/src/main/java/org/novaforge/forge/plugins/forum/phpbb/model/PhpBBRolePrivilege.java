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
package org.novaforge.forge.plugins.forum.phpbb.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This enumeration declares default roles which are used on PhpBB
 * 
 * @author Guillaume Lamirand
 */
public enum PhpBBRolePrivilege
{

	/**
	 * Represents moderation full
	 */
	MOD_FULL
	{
		@Override
		public String getLabel()
		{
			return "Full Moderator";
		}

		@Override
		public int getId()
		{
			return 10;
		}

	},
	/**
	 * Represents moderation standard
	 */
	MOD_STANDARD
	{
		@Override
		public String getLabel()
		{
			return "Standard Moderator";
		}

		@Override
		public int getId()
		{
			return 11;
		}

	},
	/**
	 * Represents moderation simple
	 */
	MOD_SIMPLE
	{
		@Override
		public String getLabel()
		{
			return "Simple Moderator";
		}

		@Override
		public int getId()
		{
			return 12;
		}

	},
	/**
	 * Represents moderation simple
	 */
	MOD_QUEUE
	{
		@Override
		public String getLabel()
		{
			return "Queue Moderator";
		}

		@Override
		public int getId()
		{
			return 13;
		}

	},
	/**
	 * Represents forum full
	 */
	FORUM_FULL
	{
		@Override
		public String getLabel()
		{
			return "Full Access";
		}

		@Override
		public int getId()
		{
			return 14;
		}

	},
	/**
	 * Represents forum standard
	 */
	FORUM_STANDARD
	{
		@Override
		public String getLabel()
		{
			return "Standard Access";
		}

		@Override
		public int getId()
		{
			return 15;
		}

	},
	/**
	 * Represents forum standard
	 */
	FORUM_NOACCESS
	{
		@Override
		public String getLabel()
		{
			return "No Access";
		}

		@Override
		public int getId()
		{
			return 16;
		}

	},
	/**
	 * Represents forum readonly
	 */
	FORUM_READONLY
	{
		@Override
		public String getLabel()
		{
			return "Read Only Access";
		}

		@Override
		public int getId()
		{
			return 17;
		}

	},
	/**
	 * Represents forum limited access
	 */
	FORUM_LIMITED
	{
		@Override
		public String getLabel()
		{
			return "Limited Access";
		}

		@Override
		public int getId()
		{
			return 18;
		}

	},
	/**
	 * Represents forum bot access
	 */
	FORUM_BOT
	{
		@Override
		public String getLabel()
		{
			return "Bot Access";
		}

		@Override
		public int getId()
		{
			return 19;
		}

	},
	/**
	 * Represents forum queue
	 */
	FORUM_ONQUEUE
	{
		@Override
		public String getLabel()
		{
			return "On Moderation Queue";
		}

		@Override
		public int getId()
		{
			return 20;
		}

	},
	/**
	 * Represents forum standard polls
	 */
	FORUM_POLLS
	{
		@Override
		public String getLabel()
		{
			return "Standard Access + Polls";
		}

		@Override
		public int getId()
		{
			return 21;
		}

	},
	/**
	 * Represents forum limited polls
	 */
	FORUM_LIMITED_POLLS
	{
		@Override
		public String getLabel()
		{
			return "Limited Access + Polls";
		}

		@Override
		public int getId()
		{
			return 22;
		}

	},
	/**
	 * Represents forum limited polls
	 */
	FORUM_NEW_MEMBER
	{
		@Override
		public String getLabel()
		{
			return "Newly registered User";
		}

		@Override
		public int getId()
		{
			return 24;
		}

	};

	/**
	 * Contains map which link a label to a specific role element
	 */
	private static final Map<String, PhpBBRolePrivilege>	roles	= new HashMap<String, PhpBBRolePrivilege>();

	static
	{
		for (final PhpBBRolePrivilege role : values())
		{
			roles.put(role.getLabel(), role);
		}
	}

	/**
	 * Return element from enumeration regarding a specific label.
	 *
	 * @param pLabel
	 *           represents the label used to search a element in the enumeration
	 * @return specific element
	 */
	public static PhpBBRolePrivilege fromLabel(final String pLabel)
	{
		return roles.get(pLabel);
	}

	/**
	 * Return the label of a role
	 *
	 * @return label of a role
	 */
	public abstract String getLabel();

	/**
	 * Return the id of a privilege
	 *
	 * @return id of a role
	 */
	public abstract int getId();
}
