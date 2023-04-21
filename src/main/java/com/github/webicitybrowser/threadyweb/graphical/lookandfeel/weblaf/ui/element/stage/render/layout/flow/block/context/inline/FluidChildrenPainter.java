package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.ui.element.stage.render.layout.flow.block.context.inline;

import com.github.webicitybrowser.thready.dimensions.AbsolutePosition;
import com.github.webicitybrowser.thready.dimensions.Rectangle;
import com.github.webicitybrowser.thready.dimensions.util.AbsolutePositionMath;
import com.github.webicitybrowser.thready.drawing.core.Canvas2D;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.PaintContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.paint.Painter;

public class FluidChildrenPainter implements Painter {

	private final Rectangle documentRect;
	private final FluidChildRenderResult[] renderResults;

	public FluidChildrenPainter(Rectangle documentRect, FluidChildRenderResult[] renderResults) {
		this.documentRect = documentRect;
		this.renderResults = renderResults;
	}

	@Override
	public void paint(PaintContext context, Canvas2D canvas) {
		for (FluidChildRenderResult renderResult: renderResults) {
			paintChild(renderResult, context, canvas);
		}
	}

	private void paintChild(FluidChildRenderResult renderResult, PaintContext context, Canvas2D canvas) {
		AbsolutePosition childPosition = AbsolutePositionMath.sum(renderResult.position(), documentRect.position());
		Rectangle childDocumentRect = new Rectangle(childPosition, renderResult.unit().getMinimumSize());
		renderResult
			.unit()
			.getPainter(childDocumentRect)
			.paint(context, canvas);
	}

}