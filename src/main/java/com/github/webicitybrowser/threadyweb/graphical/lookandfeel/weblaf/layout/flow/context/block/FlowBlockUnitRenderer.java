package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flow.context.block;

import java.util.function.Function;

import com.github.webicitybrowser.thready.dimensions.AbsoluteSize;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.UIPipeline;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.Box;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.GlobalRenderContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.LocalRenderContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.unit.RenderedUnit;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flow.util.BoxOffsetDimensions;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flow.util.FlowSizeUtils;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.util.LayoutSizeUtils;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.util.LayoutSizeUtils.LayoutSizingContext;
import com.github.webicitybrowser.threadyweb.graphical.value.SizeCalculation.SizeCalculationContext;

public final class FlowBlockUnitRenderer {
	
	private FlowBlockUnitRenderer() {}

	public static FlowBlockPrerenderSizingInfo prerenderChild(FlowBlockUnitRenderingContext context) {
		FlowBlockRendererState state = context.state();
		AbsoluteSize parentSize = state.getLocalRenderContext().preferredSize();
		Box childBox = context.childBox();
		BoxOffsetDimensions renderParameters = context.renderParameters();

		LayoutSizingContext layoutSizingContext = createLayoutSizingContext(state, childBox, renderParameters);
		AbsoluteSize enforcedSize = computePreferredSize(childBox, layoutSizingContext);
		AbsoluteSize precomputedSize = context.childSizeGenerator().apply(state, enforcedSize);

		AbsoluteSize enforcedChildSize = LayoutSizeUtils.subtractPadding(enforcedSize, renderParameters.totalPadding());
		AbsoluteSize precomputedChildSize = LayoutSizeUtils.subtractPadding(precomputedSize, renderParameters.totalPadding());

		return new FlowBlockPrerenderSizingInfo(enforcedChildSize, precomputedChildSize, parentSize, layoutSizingContext);
	}

	public static FlowBlockChildRenderResult generateChildUnit(FlowBlockUnitRenderingContext context, FlowBlockPrerenderSizingInfo prerenderSizingInfo) {
		// If the size is determinate, we just use that size (but clipped to constraints)
		// If not, take the fit size and clip it to constraints
		Box childBox = context.childBox();
		AbsoluteSize precomputedSize = prerenderSizingInfo.precomputedChildSize();
		// TODO: clipContentSize is actually meant to be called on the fit-content size if the size is not determinate
		// However, I need to make the proper infrastructure to allow determining the fit-content size
		precomputedSize = FlowBlockSizeCalculations.clipContentSize(childBox.styleDirectives(), precomputedSize, prerenderSizingInfo);
		FlowBlockPrerenderSizingInfo adjustedPrerenderSizingInfo = new FlowBlockPrerenderSizingInfo(
			prerenderSizingInfo.enforcedChildSize(), precomputedSize, prerenderSizingInfo.parentSize(), prerenderSizingInfo.sizingContext()
		);
		RenderedUnit childUnit = renderChildUnit(context, adjustedPrerenderSizingInfo);
		AbsoluteSize adjustedSize = FlowSizeUtils.enforcePreferredSize(childUnit.fitSize(), prerenderSizingInfo.enforcedChildSize());
		AbsoluteSize clippedAdjustedSize = FlowBlockSizeCalculations.clipContentSize(childBox.styleDirectives(), adjustedSize, prerenderSizingInfo);

		return new FlowBlockChildRenderResult(childUnit, clippedAdjustedSize);
	}

	private static LayoutSizingContext createLayoutSizingContext(FlowBlockRendererState state, Box childBox, BoxOffsetDimensions boxOffsetDimensions) {
		Function<Boolean, SizeCalculationContext> sizeCalculationContextGenerator =
			isHorizontal -> LayoutSizeUtils.createSizeCalculationContext(
				state.flowContext().layoutManagerContext(), childBox.styleDirectives(), isHorizontal);

		return LayoutSizeUtils.createLayoutSizingContext(
			childBox.styleDirectives(), sizeCalculationContextGenerator, boxOffsetDimensions
		);
	}

	private static AbsoluteSize computePreferredSize(Box childBox, LayoutSizingContext layoutSizingContext) {
		return LayoutSizeUtils.computePreferredSize(childBox.styleDirectives(), layoutSizingContext);
	}

	private static RenderedUnit renderChildUnit(FlowBlockUnitRenderingContext context, FlowBlockPrerenderSizingInfo prerenderSizingInfo) {
		FlowBlockRendererState state = context.state();
		Box childBox = context.childBox();
		GlobalRenderContext globalRenderContext = state.getGlobalRenderContext();
		LocalRenderContext childLocalRenderContext = context.localRenderContextGenerator().apply(
			state,
			context.childSizeGenerator().apply(state, prerenderSizingInfo.precomputedChildSize()));
		return UIPipeline.render(childBox, globalRenderContext, childLocalRenderContext);
	}

}
