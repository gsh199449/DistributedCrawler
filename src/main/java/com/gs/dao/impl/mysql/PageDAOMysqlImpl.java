package com.gs.dao.impl.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.gs.dao.PageDAO;
import com.gs.model.PagePOJO;

/**
 * PageDAO的MySQL实现版本
 * 
 * @author GS
 */
public class PageDAOMysqlImpl implements PageDAO {
	private Logger logger = Logger.getLogger(this.getClass());
	private Connection connection;

	public PageDAOMysqlImpl() {
		DbcpBean dbcp = DbcpBean.getInstance();
		this.connection = dbcp.getConn();
	}

	@Override
	public boolean exist(int id) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select count(*) from page where id = " + id + ";";
			stmt = (Statement) connection.createStatement(); // 创建用于执行静态sql语句的Statement对象
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("count(*)") == 0 ? false : true;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void save(PagePOJO pojo) {
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
			logger.error(e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public PagePOJO loadPage(int id) {
		Statement stmt = null;
		PagePOJO pojo = new PagePOJO();
		try {
			String sql = "select * form page where id  = " + id + ";";
			stmt = (Statement) connection.createStatement(); // 创建用于执行静态sql语句的Statement对象
			ResultSet rs = stmt.executeQuery(sql);
			pojo.setTitle(rs.getString("title"));
			pojo.setContent(rs.getString("content"));
			pojo.setId(id);
			pojo.setUrl(rs.getString("url"));
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pojo;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

}
