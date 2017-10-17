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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal.compiler;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.NotNull;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.generated.RequirementsBaseListener;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.generated.RequirementsLexer;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.generated.RequirementsParser;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.generated.RequirementsParser.RequirementIdContext;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.generated.RequirementsParser.RequirementVersionContext;


public class RequirementCompiler extends RequirementsBaseListener {

	private String requirementId = null;
	private String requirementVersion = null;

	public void compile(String expression) throws RecognitionException {
		RequirementsLexer lexer = new RequirementsLexer(new ANTLRInputStream(expression));
		RequirementsParser parser = new RequirementsParser(new CommonTokenStream(lexer));

		parser.addParseListener(this);
		parser.requirement();
	}

	@Override
	public void exitRequirementVersion(@NotNull RequirementVersionContext ctx) {
		requirementVersion = ctx.getText();
		super.exitRequirementVersion(ctx);
	}

	@Override
	public void exitRequirementId(@NotNull RequirementIdContext ctx)
	{
		requirementId = ctx.getText();
		super.exitRequirementId(ctx);
	}

	public String getRequirementId() {
		return requirementId;
	}

	public String getRequirementVersion() {
		return requirementVersion;
	}

}
