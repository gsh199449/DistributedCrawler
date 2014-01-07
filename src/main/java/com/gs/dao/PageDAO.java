package com.gs.dao;

import com.gs.model.PagePOJO;

public interface PageDAO {
	public PagePOJO loadPage(int id);
	public boolean exist(int id);
	public void delete(int id);
	public void save(PagePOJO pojo);
}
