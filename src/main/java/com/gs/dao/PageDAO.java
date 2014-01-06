package com.gs.dao;

import com.gs.model.PagePOJO;

public interface PageDAO {
	public PagePOJO loadPage(String id);
	public boolean exist(String id);
	public void delete(String id);
}
