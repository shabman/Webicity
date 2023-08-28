package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.ui.element;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.webicitybrowser.thready.dimensions.Rectangle;
import com.github.webicitybrowser.thready.gui.directive.core.pool.DirectivePool;
import com.github.webicitybrowser.thready.gui.directive.core.style.StyleGenerator;
import com.github.webicitybrowser.thready.gui.graphical.layout.core.LayoutResult;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.ComponentUI;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.UIDisplay;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.BoxContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.ChildrenBox;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.GlobalPaintContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.LocalPaintContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.GlobalRenderContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.LocalRenderContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.unit.RenderedUnit;
import com.github.webicitybrowser.thready.gui.message.MessageHandler;
import com.github.webicitybrowser.thready.gui.message.NoopMessageHandler;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flow.FlowInnerDisplayLayout;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.stage.unit.BuildableRenderedUnit;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.ui.element.inline.ElementInlineDisplay;

public class ElementDisplay implements UIDisplay<ElementContext, ChildrenBox, ElementUnit> {

	private static final UIDisplay<?, ?, ?> ELEMENT_INLINE_DISPLAY = new ElementInlineDisplay();
	private final BiFunction<LayoutResult, DirectivePool, RenderedUnit> anonBoxGenerator =
		(layoutResult, directives) -> new ElementUnit(this, directives, layoutResult);
	private final Function<DirectivePool, BuildableRenderedUnit> innerUnitGenerator =
		directives -> BuildableRenderedUnit.create(ELEMENT_INLINE_DISPLAY, directives);

	private final ElementBoxGenerator elementBoxGenerator = new ElementBoxGenerator(anonBoxGenerator, innerUnitGenerator);
	private final FlowInnerDisplayLayout defaultLayout = new FlowInnerDisplayLayout(anonBoxGenerator, innerUnitGenerator);
	
	@Override
	public ElementContext createContext(ComponentUI componentUI) {
		return new ElementContext(this, componentUI);
	}

	@Override
	public List<ChildrenBox> generateBoxes(ElementContext displayContext, BoxContext boxContext, StyleGenerator styleGenerator) {
		return elementBoxGenerator.generateBoxes(displayContext, boxContext, styleGenerator);
	}

	@Override
	public ElementUnit renderBox(ChildrenBox box, GlobalRenderContext globalRenderContext, LocalRenderContext localRenderContext) {
		LayoutResult layoutResult = box instanceof ElementBlockBox elementBox ?
			elementBox.layout().renderBox(box, globalRenderContext, localRenderContext) :
			defaultLayout.renderBox(box, globalRenderContext, localRenderContext);
		return new ElementUnit(this, box.styleDirectives(), layoutResult);
	}

	@Override
	public void paint(ElementUnit unit, GlobalPaintContext globalPaintContext, LocalPaintContext localPaintContext) {
		ElementPainter.paint(unit, globalPaintContext, localPaintContext);
	}

	@Override
	public MessageHandler createMessageHandler(ElementUnit unit, Rectangle documentRect) {
		return new NoopMessageHandler();
	}
	
}
