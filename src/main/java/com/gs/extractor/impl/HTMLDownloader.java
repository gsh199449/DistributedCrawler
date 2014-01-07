package com.gs.extractor.impl;

import java.io.IOException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.crawler.Crawler;
import com.gs.utils.URL;

public class HTMLDownloader {
	private static final Logger LOG = LoggerFactory
			.getLogger(HTMLDownloader.class);

	public static String down(URL u) {
		String s = null;
		HttpClient hc = new HttpClient();
		GetMethod get;
		try {
			hc.setConnectionTimeout(3000);
			get = new GetMethod(u.url);
			hc.executeMethod(get);
			s = get.getResponseBodyAsString();
			get.releaseConnection();
		} catch (ConnectTimeoutException e) {
			LOG.warn(u.level + "连接超时");
		} catch (HttpException e) {
			LOG.warn(u.url + "Http错误");
		} catch (IOException e) {
			LOG.error(u.url + "IO错误");
		}
		return s;
	}
}
