package com.gs.dao;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

import com.gs.model.PagePOJO;

public interface PageDAO extends Closeable {
	public PagePOJO loadPage(int id);
	public boolean exist(int id);
	public void delete(int id);
	/**
	 * 向数据库写入单个PagePOJO
	 * @param pojo
	 * @throws IOException
	 */
	public void save(PagePOJO pojo) throws IOException;
	/**
	 * 以集合的方式存入一批PagePOJO
	 * @param set
	 * @throws IOException
	 */
	public void save(Set<PagePOJO> set) throws IOException;
	public void close() throws IOException;
}
