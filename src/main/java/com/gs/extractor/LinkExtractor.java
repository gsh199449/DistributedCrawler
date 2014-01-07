/**
 * GS
 */
package com.gs.extractor;

import java.util.Set;

import com.gs.utils.URL;

/**
 * @author GaoShen
 * @packageName com.gs.extractor
 */
public interface LinkExtractor {
	public Set<URL> extractFromHtml(String html, final int level);
}