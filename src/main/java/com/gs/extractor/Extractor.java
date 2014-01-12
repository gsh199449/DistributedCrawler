package com.gs.extractor;

import java.util.Set;

import com.gs.utils.URL;

public abstract class Extractor {
	protected final String html;
	protected String title;
	protected String content;
	protected Set<URL> set;
	protected final int level;
	protected final int topN;
	protected final int depth;

	protected Extractor(final String html, final int level,final int topN,final int depth) {
		this.html = html;
		this.level = level;
		this.topN = topN;
		this.depth = depth;
	}

	public String getTitle() {
		return title;
	}

	public Set<URL> getUrls() {
		return set;
	}

	public String getContent() {
		return content;
	}

	public abstract void proceed();
}
