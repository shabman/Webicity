package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.util;

import com.github.webicitybrowser.thready.drawing.core.text.Font2D;
import com.github.webicitybrowser.thready.drawing.core.text.FontDecoration;
import com.github.webicitybrowser.thready.drawing.core.text.FontMetrics;
import com.github.webicitybrowser.thready.drawing.core.text.FontSettings;
import com.github.webicitybrowser.thready.drawing.core.text.source.FontSource;
import com.github.webicitybrowser.thready.gui.directive.core.pool.DirectivePool;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.render.GlobalRenderContext;
import com.github.webicitybrowser.threadyweb.graphical.directive.derived.DerivedFontDirective;
import com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.util.directive.WebTextDirectiveUtil;
import com.github.webicitybrowser.threadyweb.graphical.value.SizeCalculation.SizeCalculationContext;

public final class WebFontUtil {

	private WebFontUtil() {}

	public static Font2D getFont(DirectivePool styleDirectives, GlobalRenderContext globalRenderContext) {
		return styleDirectives.derive(
			DerivedFontDirective.class, (self, parent) -> deriveFont(self, parent, globalRenderContext)
		).getFont();
	}

	private static DerivedFontDirective deriveFont(DirectivePool self, DirectivePool parent, GlobalRenderContext globalRenderContext) {
		FontMetrics parentMetrics = parent != null ?
			parent.derive(
				DerivedFontDirective.class,
				(self2, parent2) -> deriveFont(self2, parent2, globalRenderContext)
			).getFont().getMetrics() :
			globalRenderContext.rootFontMetrics();

		FontSource[] fontSources = WebTextDirectiveUtil.getFontSources(self);
		float fontSize = WebTextDirectiveUtil.getFontSize(self, new SizeCalculationContext(
			null, globalRenderContext.viewportSize(),
			parentMetrics, globalRenderContext.rootFontMetrics(),
			false));
		int fontWeight = WebTextDirectiveUtil.getFontWeight(self, parentMetrics);

		return DerivedFontDirective.of(globalRenderContext.resourceLoader().loadFont(
			new FontSettings(fontSources, fontSize, fontWeight, new FontDecoration[0])
		));
	}

}
