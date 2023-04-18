package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.ui.element.stage.box;

import java.util.function.BiFunction;

import com.github.webicitybrowser.thready.gui.directive.core.DirectivePool;
import com.github.webicitybrowser.thready.gui.directive.core.StyleGenerator;
import com.github.webicitybrowser.thready.gui.graphical.cache.MappingCache;
import com.github.webicitybrowser.thready.gui.graphical.cache.imp.MappingCacheImp;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.base.stage.box.BasicSolidBox;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.ComponentUI;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.LookAndFeel;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.Box;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.BoxContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.SolidRenderer;
import com.github.webicitybrowser.thready.gui.tree.core.Component;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.util.WebBoxGenerator;
import com.github.webicitybrowser.threadyweb.tree.ElementComponent;
import com.github.webicitybrowser.threadyweb.tree.WebComponent;

public class ElementUIBoxGenerator {
	
	private final ElementComponent component;
	private final ComponentUI parentUI;
	private final BiFunction<Box, Box[], SolidRenderer> rendererGenerator;
	private final MappingCache<Component, ComponentUI> childCache = new MappingCacheImp<>(ComponentUI[]::new, ui -> ui.getComponent());

	public ElementUIBoxGenerator(ElementComponent component, ComponentUI parentUI, BiFunction<Box, Box[], SolidRenderer> rendererGenerator) {
		this.component = component;
		this.parentUI = parentUI;
		this.rendererGenerator = rendererGenerator;
	}
	
	public Box[] generateBoxes(BoxContext context, DirectivePool directives, StyleGenerator styleGenerator) {
		return WebBoxGenerator.generateBoxes(() -> performBoxing(context, directives, styleGenerator));
	}
	
	//
	
	private Box[] performBoxing(BoxContext context, DirectivePool directives, StyleGenerator styleGenerator) {
		Box rootBox = generateBlockRootBox(context, directives, styleGenerator);
		
		return new Box[] { rootBox };
	}
	
	private Box generateBlockRootBox(BoxContext context, DirectivePool directives, StyleGenerator styleGenerator) {
		return createBlockRootBox(context, directives, styleGenerator);
	}

	private Box createBlockRootBox(BoxContext context, DirectivePool directives, StyleGenerator styleGenerator) {
		Box rootBox = new BasicSolidBox(directives, rendererGenerator);
		
		ComponentUI[] children = computeCurrentChildUIs(context.getLookAndFeel(), directives);
		StyleGenerator[] childStyleGenerators = styleGenerator.createChildStyleGenerators(children);
		for (int i = 0; i < children.length; i++) {
			addChildBoxes(rootBox, children[i], context, directives, childStyleGenerators[i]);
		}
		
		return rootBox;
	}

	private ComponentUI[] computeCurrentChildUIs(LookAndFeel lookAndFeel, DirectivePool directives) {
		WebComponent[] componentChildren = component.getChildren();
		childCache.recompute(componentChildren, component -> lookAndFeel.createUIFor(component, parentUI));
		
		return childCache.getComputedMappings();
	}
	
	private void addChildBoxes(
		Box rootBox, ComponentUI child, BoxContext context, DirectivePool directives, StyleGenerator styleGenerator
	) {
		for (Box box: child.generateBoxes(context, directives, styleGenerator)) {
			for (Box adjustedBox: box.getAdjustedBoxTree()) {
				rootBox.addChild(adjustedBox);
			}
		};
	}
	
}
