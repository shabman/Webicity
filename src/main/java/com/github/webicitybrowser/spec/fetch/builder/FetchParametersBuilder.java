package com.github.webicitybrowser.spec.fetch.builder;

import com.github.webicitybrowser.spec.fetch.FetchConsumeBodyAction;
import com.github.webicitybrowser.spec.fetch.FetchParameters;
import com.github.webicitybrowser.spec.fetch.FetchRequest;
import com.github.webicitybrowser.spec.fetch.builder.imp.FetchParametersBuilderImp;

public interface FetchParametersBuilder {
	
	void setRequest(FetchRequest request);

	void setConsumeBodyAction(FetchConsumeBodyAction consumeBodyAction);

	FetchParameters build();

	static FetchParametersBuilder create() {
		return new FetchParametersBuilderImp();
	}

}