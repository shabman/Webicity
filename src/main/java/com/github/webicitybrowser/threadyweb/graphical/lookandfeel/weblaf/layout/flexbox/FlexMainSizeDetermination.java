package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flexbox;

import java.util.ArrayList;
import java.util.List;

import com.github.webicitybrowser.thready.dimensions.AbsoluteSize;
import com.github.webicitybrowser.thready.dimensions.RelativeDimension;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.Box;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.GlobalRenderContext;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.LocalRenderContext;
import com.github.webicitybrowser.threadyweb.graphical.directive.layout.flexbox.FlexDirectionDirective.FlexDirection;
import com.github.webicitybrowser.threadyweb.graphical.directive.layout.flexbox.FlexWrapDirective.FlexWrap;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flexbox.item.FlexItem;

public final class FlexMainSizeDetermination {

	private FlexMainSizeDetermination() {}

	public static List<FlexLine> determineLinesWithMainSizes(
		Box parentBox, List<FlexItem> flexItems, GlobalRenderContext globalRenderContext, LocalRenderContext localRenderContext
	) {
		FlexWrap flexWrap = FlexUtils.getFlexWrap(parentBox);
		switch (flexWrap) {
		case NOWRAP:
			return determineLinesWithMainSizesNoWrap(parentBox, flexItems, globalRenderContext, localRenderContext);
		case WRAP:
		case WRAP_REVERSE:
			return determineLinesWithMainSizesWrap(parentBox, flexItems, globalRenderContext, localRenderContext);
		default:
			throw new IllegalArgumentException("Unknown flex wrap: " + flexWrap);
		}
	}

	private static List<FlexLine> determineLinesWithMainSizesNoWrap(
		Box parentBox, List<FlexItem> flexItems, GlobalRenderContext globalRenderContext, LocalRenderContext localRenderContext
	) {
		FlexDirection flexDirection = FlexUtils.getFlexDirection(parentBox);
		float crossSize = FlexDimension.createFrom(localRenderContext.getPreferredSize(), flexDirection).cross();
		FlexLine flexLine = new FlexLine(flexDirection);
		setLineInitialMainSize(flexLine, flexDirection, localRenderContext);
		for (FlexItem flexItem: flexItems) {
			flexItem.setCrossSize(crossSize);
			determineMainSize(flexItem, flexDirection, globalRenderContext, localRenderContext);
			flexLine.addFlexItem(flexItem);
		}

		return List.of(flexLine);
	}

	private static List<FlexLine> determineLinesWithMainSizesWrap(
		Box parentBox, List<FlexItem> flexItems, GlobalRenderContext globalRenderContext, LocalRenderContext localRenderContext
	) {
		FlexDirection flexDirection = FlexUtils.getFlexDirection(parentBox);
		float crossSize = FlexDimension.createFrom(localRenderContext.getPreferredSize(), flexDirection).cross();
		for (FlexItem flexItem: flexItems) {
			flexItem.setCrossSize(crossSize);
			determineMainSize(flexItem, FlexUtils.getFlexDirection(parentBox), globalRenderContext, localRenderContext);
		}

		List<FlexLine> flexLines = new ArrayList<>();
		float availableMainSpace = FlexDimension.createFrom(localRenderContext.getPreferredSize(), flexDirection).main();
		for (int i = 0; i < flexItems.size();) {
			FlexLine flexLine = new FlexLine(flexDirection);
			setLineInitialMainSize(flexLine, flexDirection, localRenderContext);

			FlexItem flexItem = flexItems.get(i);
			flexLine.addFlexItem(flexItem);
			float remainingMainSpace = availableMainSpace - flexItem.getHypotheticalMainSize();
			
			i++;
			for (; i < flexItems.size(); i++) {
				flexItem = flexItems.get(i);
				float hypotheticalMainSize = flexItem.getHypotheticalMainSize();
				if (availableMainSpace != RelativeDimension.UNBOUNDED && hypotheticalMainSize > remainingMainSpace) break;
				flexLine.addFlexItem(flexItem);
				remainingMainSpace -= hypotheticalMainSize;
			}
			flexLines.add(flexLine);
		}

		return flexLines;
	}

	private static void setLineInitialMainSize(FlexLine flexLine, FlexDirection flexDirection, LocalRenderContext localRenderContext) {
		float mainSize = FlexDimension.createFrom(localRenderContext.getPreferredSize(), flexDirection).main();
		if (mainSize != RelativeDimension.UNBOUNDED) {
			flexLine.setMainSize(mainSize);
		}
	}

	private static void determineMainSize(
		FlexItem flexItem, FlexDirection flexDirection, GlobalRenderContext globalRenderContext, LocalRenderContext localRenderContext
	) {
		// TODO: Check flex basis
		float preferredWidth = flexItem.getSizePreferences().getMainSize(flexDirection);
		if (preferredWidth != RelativeDimension.UNBOUNDED) {
			flexItem.setBaseSize(preferredWidth);
			flexItem.setHypotheticalMainSize(clampMinMax(preferredWidth, flexItem, flexDirection));
			return;
		}
		
		FlexItemRenderer.FlexItemRenderContext flexItemRenderContext = new FlexItemRenderer.FlexItemRenderContext(
			globalRenderContext, flexDirection, localRenderContext.getParentFontMetrics()
		);
		AbsoluteSize fitSize = FlexItemRenderer.render(flexItem, flexItemRenderContext);
		FlexDimension flexDimension = FlexDimension.createFrom(fitSize, flexDirection);
		flexItem.setBaseSize(flexDimension.main());
		flexItem.setHypotheticalMainSize(clampMinMax(flexDimension.main(), flexItem, flexDirection));
	}

	private static float clampMinMax(float main, FlexItem flexItem, FlexDirection flexDirection) {
		float maxMainSize = flexItem.getSizePreferences().getMaxMainSize(flexDirection);
		if (maxMainSize != RelativeDimension.UNBOUNDED) {
			main = Math.min(main, maxMainSize);
		}

		float minMainSize = flexItem.getSizePreferences().getMinMainSize(flexDirection);
		if (minMainSize != RelativeDimension.UNBOUNDED) {
			main = Math.max(main, minMainSize);
		}

		return Math.max(0, main);
	}

}
