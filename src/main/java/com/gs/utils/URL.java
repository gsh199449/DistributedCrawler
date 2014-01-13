/**
 * 
 */
package com.gs.utils;

/**
 * @author GaoShen
 * @packageName com.gs.MyCrawler
 */
public class URL {
	public String url;
	public int level;

	public URL(String url, int level) {
		this.level = level;
		this.url = url;
	}

	public URL() {}

	@Override
	public String toString() {
		return "URL [" + (url != null ? "url=" + url + ", " : "") + "level="
				+ level + "]";
	}
}
