package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.util;

import com.github.webicitybrowser.thready.color.Colors;
import com.github.webicitybrowser.thready.color.format.ColorFormat;
import com.github.webicitybrowser.thready.gui.directive.core.pool.DirectivePool;
import com.github.webicitybrowser.thready.gui.graphical.directive.BackgroundColorDirective;
import com.github.webicitybrowser.thready.gui.graphical.directive.ForegroundColorDirective;
import com.github.webicitybrowser.threadyweb.graphical.directive.HeightDirective;
import com.github.webicitybrowser.threadyweb.graphical.directive.InnerDisplayDirective;
import com.github.webicitybrowser.threadyweb.graphical.directive.OuterDisplayDirective;
import com.github.webicitybrowser.threadyweb.graphical.directive.WidthDirective;
import com.github.webicitybrowser.threadyweb.graphical.value.InnerDisplay;
import com.github.webicitybrowser.threadyweb.graphical.value.OuterDisplay;
import com.github.webicitybrowser.threadyweb.graphical.value.SizeCalculation;

public final class WebDirectiveUtil {

	private WebDirectiveUtil() {}

	public static ColorFormat getForegroundColor(DirectivePool directives) {
		return directives
			.inheritDirectiveOrEmpty(ForegroundColorDirective.class)
			.map(directive -> directive.getColor())
			.orElse(Colors.BLACK);
	}
	
	public static ColorFormat getBackgroundColor(DirectivePool directives) {
		return directives
			.getDirectiveOrEmpty(BackgroundColorDirective.class)
			.map(directive -> directive.getColor())
			.orElse(Colors.TRANSPARENT);
	}
	
	public static OuterDisplay getOuterDisplay(DirectivePool directives) {
		return directives
			.getDirectiveOrEmpty(OuterDisplayDirective.class)
			.map(directive -> directive.getOuterDisplay())
			.orElse(OuterDisplay.INLINE);
	}

	public static InnerDisplay getInnerDisplay(DirectivePool directives) {
       return directives
			.getDirectiveOrEmpty(InnerDisplayDirective.class)
			.map(directive -> directive.getInnerDisplay())
			.orElse(InnerDisplay.FLOW);
    }

	public static SizeCalculation getHeight(DirectivePool directives) {
		return directives
			.getDirectiveOrEmpty(HeightDirective.class)
			.map(directive -> directive.getHeightCalculation())
			.orElse(SizeCalculation.SIZE_AUTO);
	}

	 public static SizeCalculation getWidth(DirectivePool directives) {
		  return directives
			.getDirectiveOrEmpty(WidthDirective.class)
			.map(directive -> directive.getWidthCalculation())
			.orElse(SizeCalculation.SIZE_AUTO);
	 }
	
}
