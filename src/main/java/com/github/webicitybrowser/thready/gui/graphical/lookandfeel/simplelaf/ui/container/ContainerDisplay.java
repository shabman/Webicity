package com.github.webicitybrowser.thready.gui.graphical.lookandfeel.simplelaf.ui.container;

import java.util.List;

import com.github.webicitybrowser.thready.dimensions.Rectangle;
import com.github.webicitybrowser.thready.gui.graphical.cache.MappingCache;
import com.github.webicitybrowser.thready.gui.graphical.cache.imp.MappingCacheImp;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.ComponentUI;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.UIDisplay;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.BoxContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.ChildrenBox;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.composite.GlobalCompositeContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.composite.LocalCompositeContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.GlobalPaintContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.LocalPaintContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.GlobalRenderContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.LocalRenderContext;
import com.github.webicitybrowser.thready.gui.message.MessageHandler;
import com.github.webicitybrowser.thready.gui.tree.core.Component;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.util.WebDefaults;

public class ContainerDisplay implements UIDisplay<ContainerContext, ChildrenBox, ContainerRenderedUnit> {

	@Override
	public ContainerContext createContext(ComponentUI componentUI) {
		MappingCache<Component, ContainerChildEntry> childCache = new MappingCacheImp<>(entry -> entry.component());
		return new ContainerContext(this, componentUI, childCache);
	}

	@Override
	public List<ChildrenBox> generateBoxes(ContainerContext displayContext, BoxContext boxContext) {
		ContainerBox rootBox = new ContainerBox(
			this, displayContext.owningComponent(),
			displayContext.styleDirectives(), WebDefaults.INLINE_DISPLAY);
		ContainerChildrenBoxGenerator.addChildrenBoxes(displayContext, rootBox, boxContext);
		return List.of(rootBox);
	}

	@Override
	public ContainerRenderedUnit renderBox(ChildrenBox box, GlobalRenderContext globalRenderContext, LocalRenderContext localRenderContext) {
		return ContainerRenderer.render(box, globalRenderContext, localRenderContext);
	}

	@Override
	public void composite(ContainerRenderedUnit unit, GlobalCompositeContext compositeContext, LocalCompositeContext localCompositeContext) {
		ContainerCompositor.composite(unit, compositeContext, localCompositeContext);
	}

	@Override
	public void paint(ContainerRenderedUnit unit, GlobalPaintContext globalPaintContext, LocalPaintContext localPaintContext) {
		ContainerPainter.paint(unit, globalPaintContext, localPaintContext);
	}

	@Override
	public MessageHandler createMessageHandler(ContainerRenderedUnit unit, Rectangle documentRect) {
		return ContainerMessageHandler.createMessageHandler(unit, documentRect);
	}

}
