package com.github.webicitybrowser.webicity.renderer.frontend.thready.html.style.cssbinding.imp.decparser;

import java.util.function.Function;

import com.github.webicitybrowser.spec.css.parser.property.PropertyValueParser;
import com.github.webicitybrowser.spec.css.parser.property.padding.PaddingLonghandValueParser;
import com.github.webicitybrowser.spec.css.property.CSSValue;
import com.github.webicitybrowser.thready.gui.directive.core.Directive;
import com.github.webicitybrowser.threadyweb.graphical.value.SizeCalculation;
import com.github.webicitybrowser.webicity.renderer.frontend.thready.html.style.cssbinding.imp.CSSOMNamedDeclarationParser;
import com.github.webicitybrowser.webicity.renderer.frontend.thready.html.style.cssbinding.imp.decparser.size.SizeParser;

public class CSSOMLonghandPaddingParser implements CSSOMNamedDeclarationParser<CSSValue> {

	private final PaddingLonghandValueParser longhandPaddingValueParser = new PaddingLonghandValueParser();
	private final Function<SizeCalculation, ? extends Directive> directiveFactory;

	public CSSOMLonghandPaddingParser(Function<SizeCalculation, ? extends Directive> directiveFactory) {
		this.directiveFactory = directiveFactory;
	}

	@Override
	public PropertyValueParser<CSSValue> getPropertyValueParser() {
		return longhandPaddingValueParser;
	}

	@Override
	public Directive[] translatePropertyValue(CSSValue value) {
		SizeCalculation sizeCalculation = SizeParser.parseWithBoxPercents(value);
		return new Directive[] {
			directiveFactory.apply(sizeCalculation)
		};
	}
	
}
