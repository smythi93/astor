package fr.inria.astor.approaches.deeprepair;

import java.util.List;

import com.martiansoftware.jsap.JSAPException;

import fr.inria.astor.core.ingredientbased.IngredientBasedRepairApproachImpl;
import fr.inria.astor.core.manipulation.MutationSupporter;
import fr.inria.astor.core.manipulation.filters.TargetElementProcessor;
import fr.inria.astor.core.setup.ConfigurationProperties;
import fr.inria.astor.core.setup.ProjectRepairFacade;
import fr.inria.astor.core.solutionsearch.spaces.ingredients.scopes.CtLocationIngredientSpace;
import fr.inria.astor.core.solutionsearch.spaces.ingredients.scopes.ctscopes.CtClassIngredientSpace;
import fr.inria.astor.core.solutionsearch.spaces.ingredients.scopes.ctscopes.CtGlobalIngredientScope;
import fr.inria.astor.core.solutionsearch.spaces.ingredients.scopes.ctscopes.CtPackageIngredientScope;
import fr.inria.main.evolution.ExtensionPoints;
import fr.inria.main.evolution.PlugInLoader;
import spoon.reflect.declaration.CtType;

/**
 * DeepRepair engine.
 * 
 * @author Matias Martinez
 *
 */
public class DeepRepairEngine extends IngredientBasedRepairApproachImpl {

	public DeepRepairEngine(MutationSupporter mutatorExecutor, ProjectRepairFacade projFacade) throws Exception {
		super(mutatorExecutor, projFacade);
		ConfigurationProperties.setProperty("transformingredient", Boolean.TRUE.toString());
		setPropertyIfNotDefined(ExtensionPoints.INGREDIENT_PROCESSOR.identifier, "statements");
		setPropertyIfNotDefined(ExtensionPoints.INGREDIENT_TRANSFORM_STRATEGY.identifier, "name-cluster-based");
		setPropertyIfNotDefined(ExtensionPoints.INGREDIENT_SEARCH_STRATEGY.identifier, "code-similarity-based");

		String learningdir = ConfigurationProperties.getProperty("learningdir");
		if (learningdir == null || learningdir.isEmpty()) {
			log.error("Error, missing argument: -learningdir ");
			throw new IllegalArgumentException("Missing argument -learningdir");
		}

		ExtensionPoints cloneGranularityEP = ExtensionPoints.CLONE_GRANULARITY;
		Class cloneGranularity = PlugInLoader.loadClassFromProperty(cloneGranularityEP);
		if (cloneGranularity == null) {
			cloneGranularity = CtType.class;
		}
		ConfigurationProperties.setProperty("clonegranularity", cloneGranularity.getName());

	}

	@Override
	public void loadIngredientPool() throws JSAPException, Exception {

		List<TargetElementProcessor<?>> ingredientProcessors = this.getTargetElementProcessors();

		// The ingredients for build the patches
		String scope = ConfigurationProperties.properties.getProperty("scope");
		CtLocationIngredientSpace ingredientspace = null;
		if ("global".equals(scope)) {
			ingredientspace = (new CtGlobalIngredientScope(ingredientProcessors));
		} else if ("package".equals(scope)) {
			ingredientspace = (new CtPackageIngredientScope(ingredientProcessors));
		} else if ("local".equals(scope)) {
			ingredientspace = (new CtClassIngredientSpace(ingredientProcessors));
		} else {
			ingredientspace = (CtLocationIngredientSpace) PlugInLoader.loadPlugin(
					ExtensionPoints.INGREDIENT_STRATEGY_SCOPE, new Class[] { List.class },
					new Object[] { ingredientProcessors });

		}
		this.setIngredientPool(ingredientspace);
	}

	@Override
	protected void loadOperatorSpaceDefinition() throws Exception {

		this.setOperatorSpace(new DeepRepairOperatorSpace());
	}

}
