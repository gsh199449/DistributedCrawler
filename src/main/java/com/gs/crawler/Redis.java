package com.gs.crawler;

import org.apache.log4j.Logger;

import com.gs.utils.URL;

import redis.clients.jedis.Jedis;

public class Redis {
	private Logger logger = Logger.getLogger(this.getClass());
	private static Jedis jj;
	private final static Redis INSTANCE = new Redis();

	private Redis() {
		try {
			jj = new Jedis("localhost",6379);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	

	public static Redis getInstance() {
		return INSTANCE;
	}

	public boolean hasFetched(URL u) {
		return jj.exists(u.url);
	}

	public void add(URL u) {
		jj.set(u.url, String.valueOf(u.level));
	}
}
