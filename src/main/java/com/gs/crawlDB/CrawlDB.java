package com.gs.crawlDB;

import java.util.Set;

import javax.swing.JEditorPane;

import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.gs.crawler.Redis;
import com.gs.utils.URL;

public class CrawlDB {
	private static final JedisPool tocrawl = new JedisPool(new Config(),"localhost",6377,1000,"940409",0);
	private static final JedisPool crawled = new JedisPool(new Config(),"localhost",6377,1000,"940409",1);
	public Set<URL> generate() {
		//TODO:tocrawl.getResource().randomKey()
		return null;
	}

	public void inject(Set<URL> set) {

	}
}
