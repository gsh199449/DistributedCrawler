package com.gs.dao.impl.habase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.KeyValue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.hsqldb.Result;
import org.junit.Test;

import com.google.common.primitives.Bytes;
import com.gs.dao.PageDAO;
import com.gs.dao.impl.hbase.PageDAOHBaseImpl;
import com.gs.model.PagePOJO;

public class TestHBaseDAO {
	@Test
	public void test() {
		try {
			Configuration configuration = HBaseConfiguration.create();
			// HTablePool pool = new HTablePool(configuration, 1000);
			System.out.println(configuration.get("hbase.zookeeper.property.clientPort"));
			System.out.println(configuration.get("hbase.zookeeper.quorum"));
			System.out.println(configuration.get("hbase.master"));
			HTable table = new HTable(configuration, "page".getBytes());
			try {
				Get scan = new Get("1".getBytes());// žùŸÝrowkey²éÑ¯
				org.apache.hadoop.hbase.client.Result r = table.get(scan);
				System.out.println("获得到rowkey:" + new String(r.getRow()));
				for (org.apache.hadoop.hbase.KeyValue keyValue : r.raw()) {
					System.out.println("列" + new String(keyValue.getFamily())
							+ "====值：" + new String(keyValue.getValue()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoad() {
		Configuration configuration = HBaseConfiguration.create();
		HTablePool pool = new HTablePool(configuration, 1000);
		HTableInterface table = pool.getTable("page".getBytes());
		try {
			Get scan = new Get("3".getBytes());// žùŸÝrowkey²éÑ¯
			org.apache.hadoop.hbase.client.Result r = table.get(scan);
			System.out.println("获得到rowkey:" + new String(r.getRow()));
			for (org.apache.hadoop.hbase.KeyValue keyValue : r.raw()) {
				System.out.println("列" + new String(keyValue.getFamily())
						+ "====值：" + new String(keyValue.getValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			pool.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPut() throws IOException {
		Configuration configuration = HBaseConfiguration.create();
		HConnection connection = HConnectionManager.createConnection(configuration);
		 HTableInterface table = connection.getTable("page");
		 // use the table as needed, for a single operation and a single thread
		 Put put = new Put("2".getBytes());
		 put.add("content".getBytes(), null, "我吃包子".getBytes());
		 put.add("title".getBytes(), null, "吃包子".getBytes());
		 put.add("url".getBytes(), null, "http://www.sina.com.cn".getBytes());
		 table.put(put);
		 table.close();
		 connection.close();
	}
	
	@Test
	public void testSave() throws ZooKeeperConnectionException, IOException{
		PagePOJO pojo = new PagePOJO("http://haha",3,"XIXIX","ppppp");
		PageDAO dao = new PageDAOHBaseImpl("page");
		dao.save(pojo);
		dao.close();
	}
	
}
