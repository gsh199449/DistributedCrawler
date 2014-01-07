package com.gs.crawldb;

import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	@Test
	public void test(){
		JedisPool j = new JedisPool(new Config(),"localhost",6377,1000,"940409",0);
		System.out.println(j.getResource().get("aa"));
		j.getResource().set("java", "haha");
	}
}
