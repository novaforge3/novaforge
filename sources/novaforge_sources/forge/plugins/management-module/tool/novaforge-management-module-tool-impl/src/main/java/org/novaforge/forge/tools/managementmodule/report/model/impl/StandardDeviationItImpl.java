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
package org.novaforge.forge.tools.managementmodule.report.model.impl;

import org.novaforge.forge.tools.managementmodule.domain.report.StandardDeviationIt;

public class StandardDeviationItImpl implements StandardDeviationIt
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1859948847741937471L;

	private Float						 ecartTypeVelocite;

	private Float						 ecartTypeFocalisation;

	private Float						 moyenneErreurEstimation;

	private String						itName;

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#getEcartTypeVelocite()
	 */
	@Override
	public Float getEcartTypeVelocite()
	{
		return ecartTypeVelocite;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#setEcartTypeVelocite
	 * (java.lang.Float)
	 */
	@Override
	public void setEcartTypeVelocite(final Float ecartTypeVelocite)
	{
		this.ecartTypeVelocite = ecartTypeVelocite;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#getEcartTypeFocalisation
	 * ()
	 */
	@Override
	public Float getEcartTypeFocalisation()
	{
		return ecartTypeFocalisation;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#setEcartTypeFocalisation
	 * (java.lang.Float)
	 */
	@Override
	public void setEcartTypeFocalisation(final Float ecartTypeFocalisation)
	{
		this.ecartTypeFocalisation = ecartTypeFocalisation;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#getMoyenneErreurEstimation
	 * ()
	 */
	@Override
	public Float getMoyenneErreurEstimation()
	{
		return moyenneErreurEstimation;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#setMoyenneErreurEstimation
	 * (java.lang.Float)
	 */
	@Override
	public void setMoyenneErreurEstimation(final Float moyenneErreurEstimation)
	{
		this.moyenneErreurEstimation = moyenneErreurEstimation;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#getItName()
	 */
	@Override
	public String getItName()
	{
		return itName;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.report.model.impl.StandardDeviationIt#setItName(java.lang
	 * .String)
	 */
	@Override
	public void setItName(final String itName)
	{
		this.itName = itName;
	}

	@Override
	public String toString()
	{
		return "StandardDeviationItImpl [ecartTypeVelocite=" + ecartTypeVelocite + ", ecartTypeFocalisation="
				+ ecartTypeFocalisation + ", moyenneErreurEstimation=" + moyenneErreurEstimation + ", itName="
				+ itName + "]";
	}

}
