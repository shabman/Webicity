package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.display.wrapper;

import com.github.webicitybrowser.thready.gui.directive.core.pool.DirectivePool;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.UIDisplay;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.Box;
import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.stage.box.WrapperBox;
import com.github.webicitybrowser.thready.gui.tree.core.Component;

public record WebWrapperBox(
	UIDisplay<?, ?, ?> display, Component owningComponent, DirectivePool styleDirectives, Box innerBox
) implements WrapperBox {

	@Override
	public Box innerBox() {
		return innerBox;
	}

	@Override
	public WrapperBox rewrap(Box newInnerBox) {
		return new WebWrapperBox(display, owningComponent, styleDirectives, newInnerBox);
	}

}
