package com.gs.dao.impl.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gs.crawler.Crawler;
import com.gs.dao.PageDAO;
import com.gs.model.PagePOJO;

/**
 * PageDAO的MySQL实现版本
 * 
 * @author GS
 */
public class PageDAOMysqlImpl implements PageDAO {
	private static final Logger LOG = LoggerFactory.getLogger(PageDAOMysqlImpl.class);
	private Connection connection;

	public PageDAOMysqlImpl() {
		DbcpBean dbcp = DbcpBean.getInstance();
		this.connection = dbcp.getConn();
	}

	@Override
	public boolean exist(String url) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from page where url = " + url + ";";
			stmt = (Statement) connection.createStatement(); // 创建用于执行静态sql语句的Statement对象
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("count(*)") == 0 ? false : true;
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.error(e.getMessage());
				throw e;
			}
		}
		return false;
	}

	@Override
	public void save(PagePOJO pojo) throws SQLException {
		LOG.info("Save PagePOJO : "+pojo);
		Statement stmt = null;
		try {
			String sql = "INSERT INTO `page`.`page` (`title`, `url`, `content`, `id`) VALUES ('"
					+ pojo.getTitle()
					+ "', '"
					+ pojo.getUrl()
					+ "', '"
					+ pojo.getContent() + "', " + pojo.getId() + ");";
			stmt = (Statement) connection.createStatement(); // 创建用于执行静态sql语句的Statement对象
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			throw e;
		}

	}

	@Override
	public PagePOJO loadPage(String url) throws SQLException {
		Statement stmt = null;
		PagePOJO pojo = new PagePOJO();
		try {
			String sql = "select * form page where url  = " + url + ";";
			stmt = (Statement) connection.createStatement(); // 创建用于执行静态sql语句的Statement对象
			ResultSet rs = stmt.executeQuery(sql);
			pojo.setTitle(rs.getString("title"));
			pojo.setContent(rs.getString("content"));
			pojo.setUrl(url);
		} catch (SQLException e) {
			LOG.error(e.getMessage());
			throw e;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.error(e.getMessage());
				throw e;
			}
		}
		return pojo;
	}


	@Override
	public void save(Set<PagePOJO> set) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String url) {
		// TODO Auto-generated method stub
		
	}

}
