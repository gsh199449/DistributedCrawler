package com.gs.dao.impl.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public final class HBaseCleaner {
	public static void main(String[] args) throws ZooKeeperConnectionException, IOException {
		clean();
	}
	
	private static void clean() throws IOException{
		HBaseAdmin admin = new HBaseAdmin(HBasePool.getInstance().getConf());
		admin.disableTable("page");
		admin.deleteTable("page");
		HTableDescriptor tableDescriptor = new HTableDescriptor("page");  
        tableDescriptor.addFamily(new HColumnDescriptor("content"));  
        tableDescriptor.addFamily(new HColumnDescriptor("title"));  
        tableDescriptor.addFamily(new HColumnDescriptor("url"));  
		admin.createTable(tableDescriptor);
		admin.close();
	}
}
