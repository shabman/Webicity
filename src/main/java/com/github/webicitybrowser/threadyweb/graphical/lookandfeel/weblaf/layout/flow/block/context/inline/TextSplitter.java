package com.github.webicitybrowser.threadyweb.graphical.lookandfeel.weblaf.layout.flow.block.context.inline;

import com.github.webicitybrowser.thready.dimensions.RelativeDimension;
import com.github.webicitybrowser.thready.drawing.core.text.Font2D;
import com.github.webicitybrowser.thready.drawing.core.text.FontMetrics;


public class TextSplitter {
	
	private final String text;
	private final Font2D font;
	private final float[] charWidths;

	private int windowStart = 0;
	private int windowEnd = 0;

	private float lastTextWidth = 0;

	public TextSplitter(String text, Font2D font) {
		this.text = text;
		this.font = font;
		this.charWidths = generateCharWidths();
	}

	public String getFittingText(boolean forceFit, float maxWidth) {
		if (completed()) {
			throw new IllegalStateException("Already completed");
		}

		float currentWidth = 0;
		windowStart = windowEnd;
		while (!completed() && ((forceFit && currentWidth == 0) || !nextCharWillOverflow(currentWidth, maxWidth))) {
			currentWidth += charWidths[windowEnd];
			windowEnd++;
		}

		if (windowStart == windowEnd) {
			return null;
		}

		this.lastTextWidth = currentWidth;

		return text.substring(windowStart, windowEnd);
	}

	public boolean completed() {
		return windowEnd >= text.length();
	}

	public float getLastTextWidth() {
		return lastTextWidth;
	}

	private float[] generateCharWidths() {
		FontMetrics metrics = font.getMetrics();
		float[] sizes = new float[text.length()];
		for (int i = 0; i < sizes.length; i++) {
			sizes[i] = metrics.getCharacterWidth(text.codePointAt(i));
		}
		
		return sizes;
	}

	private boolean nextCharWillOverflow(float currentWidth, float maxWidth) {
		return maxWidth == RelativeDimension.UNBOUNDED ?
			false :
			currentWidth + charWidths[windowEnd] > maxWidth;
	}

}