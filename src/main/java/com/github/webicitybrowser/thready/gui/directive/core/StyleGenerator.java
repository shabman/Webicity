package com.github.webicitybrowser.thready.gui.directive.core;

import com.github.webicitybrowser.thready.gui.graphical.lookandfeel.core.ComponentUI;

public interface StyleGenerator {

	StyleGenerator[] createChildStyleGenerators(ComponentUI[] children);
	
	DirectivePool[] getDirectivePools();
	
	// TODO: Special styles
	// TODO: Listener for invalidation
	
}
