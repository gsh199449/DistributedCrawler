package com.gs.dao.impl.mysql;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.crawler.Crawler;

/**
 * @author gaoshen
 * 
 *         dbcp 实用类，提供了dbcp连接，不允许继承；
 * 
 *         该类需要有个地方来初始化 DS ，通过调用initDS
 *         方法来完成，可以在通过调用带参数的构造函数完成调用，可以在其它类中调用，也可以在本类中加一个static{}来完成；
 */
public final class DbcpBean implements Closeable{
	private static final Logger LOG = LoggerFactory.getLogger(DbcpBean.class);
	private static DbcpBean dbcpBean = null;

	/** 数据源,static */
	private static DataSource DS;

	/** 获得数据源连接状态 */
	protected static Map<String, Integer> getDataSourceStats()
			throws SQLException {
		BasicDataSource bds = (BasicDataSource) DS;
		Map<String, Integer> map = new HashMap<String, Integer>(2);
		map.put("active_number", bds.getNumActive());
		map.put("idle_number", bds.getNumIdle());
		return map;
	}

	/**
	 * 创建数据源，除了数据库外，都使用硬编码默认参数；
	 * 
	 * @param connectURI
	 *            数据库
	 * @return
	 */
	protected static void initDS(String connectURI) {
		initDS(connectURI, "root", "940409", "com.mysql.jdbc.Driver", 5, 100,
				30, 10000);
	}

	/**
	 * 指定所有参数连接数据源
	 * 
	 * @param connectURI
	 *            数据库
	 * @param username
	 *            用户名
	 * @param pswd
	 *            密码
	 * @param driverClass
	 *            数据库连接驱动名
	 * @param initialSize
	 *            初始连接池连接个数
	 * @param maxActive
	 *            最大激活连接数
	 * @param maxIdle
	 *            最大闲置连接数
	 * @param maxWait
	 *            获得连接的最大等待毫秒数
	 * @return
	 */
	protected static void initDS(String connectURI, String username,
			String pswd, String driverClass, int initialSize, int maxActive,
			int maxIdle, int maxWait) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClass);
		ds.setUsername(username);
		ds.setPassword(pswd);
		ds.setUrl(connectURI);
		ds.setInitialSize(initialSize); // 初始的连接数；
		ds.setMaxActive(maxActive);
		ds.setMaxIdle(maxIdle);
		ds.setMaxWait(maxWait);
		DS = ds;
		LOG.info("数据源初始化完成");
	}

	/** 关闭数据源 */
	protected static void shutdownDataSource() throws SQLException {
		BasicDataSource bds = (BasicDataSource) DS;
		bds.close();
		LOG.info("数据源正常关闭");
	}

	/** 默认的构造函数 */
	private DbcpBean() {
	}

	/** 构造函数，初始化了 DS ，指定 数据库 */
	private DbcpBean(String connectURI) {
		initDS(connectURI);
	}

	/** 构造函数，初始化了 DS ，指定 所有参数 */
	private DbcpBean(String connectURI, String username, String pswd,
			String driverClass, int initialSize, int maxActive, int maxIdle,
			int maxWait) {
		initDS(connectURI, username, pswd, driverClass, initialSize, maxActive,
				maxIdle, maxWait);
	}

	/** 从数据源获得一个连接 */
	protected Connection getConn() {
		try {
			return DS.getConnection();
		} catch (SQLException e) {
			LOG.error("获得连接出错！");
			e.printStackTrace();
			return null;
		}
	}

	protected synchronized static DbcpBean getInstance() {
		if (dbcpBean == null) {
			dbcpBean = new DbcpBean("jdbc:mysql://localhost:3306/page");
		}
		return dbcpBean;
	}

	public void close() throws IOException {
		try {
			shutdownDataSource();
		} catch (SQLException e) {
			LOG.error("数据源关闭出现错误"+e.getMessage());
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		DbcpBean db = new DbcpBean("jdbc:mysql://localhost:3306/movie");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = db.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from user limit 1 ");
			System.out.println("Results:");
			int numcols = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print("\t" + rs.getString(i) + "\t");
				}
				System.out.println("");
			}
			System.out.println(getDataSourceStats());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				if (db != null)
					shutdownDataSource();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

}
