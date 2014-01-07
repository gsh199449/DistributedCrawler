package com.gs.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.utils.URL;

import redis.clients.jedis.Jedis;
@Deprecated
public class Redis {
	private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
	private static Jedis jj;
	private final static Redis INSTANCE = new Redis();

	private Redis() {
		try {
			jj = new Jedis("localhost");
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	

	public static Redis getInstance() {
		return INSTANCE;
	}

	public boolean hasFetched(URL u) {
		return jj.exists(u.url);
	}

	public void add(Object k,Object v) {
		jj.set(k.toString(),v.toString());
	}
}
