package com.gs.dao.impl.mysql;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.gs.model.PagePOJO;

public class TestMySql {

	@Test
	public void test() throws SQLException {
		DbcpBean d = DbcpBean.getInstance();
		PageDAOMysqlImpl dao = new PageDAOMysqlImpl();
		dao.save(new PagePOJO("http",1,"haha","hhhhhhhhhhhh"));
	}

}
