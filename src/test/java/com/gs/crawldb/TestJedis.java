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
	
	@Test
	public void testMov(){
		JedisPool pool0 = new JedisPool(new Config(),"localhost",6377,1000,"940409",0);
		JedisPool pool1= new JedisPool(new Config(),"localhost",6377,1000,"940409",1);
		Jedis j0 = pool0.getResource();
		Jedis j1 = pool1.getResource();
		System.out.println(j1.get("1"));
		j0.set("1", "haha");
		j0.move("1", 1);
		System.out.println(j1.get("1"));
		
		
	}
}
