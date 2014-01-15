package com.gs.dao;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import com.gs.model.PagePOJO;

public interface PageDAO extends Closeable {
	public PagePOJO loadPage(String url) throws SQLException;
	public boolean exist(String url) throws SQLException;
	public void delete(String url);
	/**
	 * 向数据库写入单个PagePOJO
	 * @param pojo
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void save(PagePOJO pojo) throws IOException, SQLException;
	/**
	 * 以集合的方式存入一批PagePOJO
	 * @param set
	 * @throws IOException
	 */
	public void save(Set<PagePOJO> set) throws IOException;
	public void close() throws IOException;
}
