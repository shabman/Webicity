package com.github.webicitybrowser.spec.css.parser.property.border.width;

import com.github.webicitybrowser.spec.css.parser.TokenLike;
import com.github.webicitybrowser.spec.css.parser.property.PropertyValueParseResult;
import com.github.webicitybrowser.spec.css.parser.property.PropertyValueParser;
import com.github.webicitybrowser.spec.css.parser.property.imp.PropertyValueParseResultImp;
import com.github.webicitybrowser.spec.css.property.CSSValue;
import com.github.webicitybrowser.spec.css.property.borderwidth.BorderWidthValue;

public class BorderWidthShorthandValueParser implements PropertyValueParser<BorderWidthValue> {
	
	private final BorderWidthLonghandValueParser longhandParser = new BorderWidthLonghandValueParser();

	@Override
	public PropertyValueParseResult<BorderWidthValue> parse(TokenLike[] tokens, int offset, int length) {
		if (!checkSelectorValid(tokens, offset, length)) {
			return PropertyValueParseResultImp.empty();
		}

		CSSValue[] values = new CSSValue[4];
		for (int i = 0; i < length; i++) {
			PropertyValueParseResult<CSSValue> parseResult = longhandParser.parse(tokens, offset + i, 1);
			if (parseResult.getResult().isEmpty()) {
				return PropertyValueParseResultImp.empty();
			}

			values[i] = parseResult.getResult().get();
		}

		for (int i = length; i < 4; i++) {
			values[i] = values[0];
		}

		return convertValuesToBorderWidthValue(values);
	}

	private boolean checkSelectorValid(TokenLike[] tokens, int offset, int length) {
		return length == 1 || length == 4;
	}

	private PropertyValueParseResult<BorderWidthValue> convertValuesToBorderWidthValue(CSSValue[] values) {
		BorderWidthValue value = new BorderWidthValue(values[0], values[1], values[2], values[3]);
		return PropertyValueParseResultImp.of(value, 4);
	}

}