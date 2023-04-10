package com.github.webicitybrowser.thready.gui.graphical.lookandfeel.simplelaf.ui.container.stage.render.fluid;

import com.github.webicitybrowser.thready.dimensions.AbsoluteSize;
import com.github.webicitybrowser.thready.dimensions.Rectangle;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.Painter;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.unit.Unit;

public class FluidChildrenUnit implements Unit {

	private final AbsoluteSize size;
	private final FluidChildrenResult[] renderResults;

	public FluidChildrenUnit(AbsoluteSize size, FluidChildrenResult[] renderResults) {
		this.size = size;
		this.renderResults = renderResults;
	}

	public Painter getPainter(Rectangle documentRect) {
		return new FluidChildrenPainter(documentRect, renderResults);
	}

	@Override
	public AbsoluteSize getMinimumSize() {
		return this.size;
	}

}