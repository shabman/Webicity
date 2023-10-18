package com.github.webicitybrowser.webicity.renderer.frontend.thready.html.component.image;

import com.github.webicitybrowser.spec.dom.node.Element;
import com.github.webicitybrowser.spec.fetch.FetchParameters;
import com.github.webicitybrowser.spec.fetch.FetchRequest;
import com.github.webicitybrowser.spec.fetch.FetchResponse;
import com.github.webicitybrowser.spec.fetch.builder.FetchParametersBuilder;
import com.github.webicitybrowser.spec.fetch.taskdestination.TaskDestination;
import com.github.webicitybrowser.spec.htmlbrowsers.tasks.EventLoop;
import com.github.webicitybrowser.spec.url.URL;
import com.github.webicitybrowser.threadyweb.context.image.ImageEngine;
import com.github.webicitybrowser.threadyweb.context.image.ImageRequest;
import com.github.webicitybrowser.threadyweb.context.image.ImageRequest.ImageRequestState;
import com.github.webicitybrowser.threadyweb.context.image.ImageState;
import com.github.webicitybrowser.webicity.core.image.ImageData;
import com.github.webicitybrowser.webicity.core.image.ImageFrame;
import com.github.webicitybrowser.webicity.renderer.backend.html.HTMLRendererContext;
import com.github.webicitybrowser.webicity.renderer.backend.html.tasks.TaskQueueTaskDestination;

public class ImageEngineImp implements ImageEngine {

	private final HTMLRendererContext htmlRendererContext;

	public ImageEngineImp(HTMLRendererContext rendererContext) {
		this.htmlRendererContext = rendererContext;
	}

	@Override
	public ImageState createImageState() {
		return new ImageStateImp();
	}

	@Override
	public void updateImageData(ImageState imageState, Element element) {
		// TODO: Continue implementing
		String urlString = selectImageSource(element);
		URL url = tryParseURL(urlString);
		if (url == null) return;
		ImageRequest imageRequest = new ImageRequest(ImageRequestState.UNAVAILABLE, url);
		imageState.setCurrentRequest(imageRequest);
		FetchRequest request = new FetchRequest("GET", url);
		// TODO Instead use processResponse
		FetchParameters parameters = FetchParametersBuilder.create()
			.setRequest(request)
			.setConsumeBodyAction((response, success, body) -> onImageLoadComplete(imageState, imageRequest, response, success, body))
			.setTaskDestination(createTaskDestination())
			.build();
		htmlRendererContext.rendererContext().getFetchEngine().fetch(parameters);
	}

	private void onImageLoadComplete(ImageState imageState, ImageRequest request, FetchResponse response, boolean success, byte[] body) {
		byte[] rasterData = new byte[60 * 60 * 4];
		for (int i = 0; i < rasterData.length; i++) {
			rasterData[i] = (byte) Math.floor(Math.random() * 255);
			if (i % 4 == 3) rasterData[i] = (byte) 255;
		}

		request.setState(ImageRequestState.COMPLETELY_AVAILABLE);
		ImageData imageData = () -> new ImageFrame[] { new ImageFrame() {
			public int getWidth() { return 60; }
			public int getHeight() { return 60; }
			public byte[] getBitmap() { return rasterData; }
		}};

		request.setImageData(imageData);
	}

	private String selectImageSource(Element element) {
		return element.hasAttribute("src") ?
			element.getAttribute("src") :
			null;
	}

	private URL tryParseURL(String urlString) {
		if (urlString == null) return null;
		try {
			return URL.of(urlString);
		} catch (Exception e) {
			return null;
		}
	}

	private TaskDestination createTaskDestination() {
		return new TaskQueueTaskDestination(htmlRendererContext.eventLoop().getTaskQueue(EventLoop.NETWORK_TASK_QUEUE));
	}
	
}
