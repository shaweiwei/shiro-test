package com.shiro.session.dao;

import com.shiro.util.JdbcHelper;

public class BaseDao {

	protected static JdbcHelper jdbcHelper;
	static{
		jdbcHelper = new JdbcHelper();
		jdbcHelper.getConnection();
	}
}
