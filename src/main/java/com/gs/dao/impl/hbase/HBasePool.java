package com.gs.dao.impl.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;

public final class HBasePool {
	private static HBasePool instance = null;
	private static HConnection connection = null;
	private static Configuration conf;

	public Configuration getConf() {
		return conf;
	}

	private HBasePool() throws ZooKeeperConnectionException {
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "gs-pc");
		conf.set("hbase.master", "gs-pc:60000");
		this.conf = conf;
		connection = HConnectionManager.createConnection(conf);
	}

	protected synchronized static HBasePool getInstance()
			throws ZooKeeperConnectionException {
		if (instance == null) {
			instance = new HBasePool();
		}
		return instance;
	}

	protected HTableInterface getTable(String tableName) throws IOException {
		return connection.getTable(tableName);
	}
}
