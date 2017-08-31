package com.shiro.session.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import com.shiro.util.JdbcHelper;
import com.shiro.util.SerializableUtils;

public class MySessionDAO extends CachingSessionDAO {
	
	private JdbcHelper jdbcHelper = BaseDao.jdbcHelper;


	@Override
	protected void doUpdate(Session session) {
		if (session instanceof ValidatingSession || !((ValidatingSession)session).isValid()) {
			return;// 如果会话过期/停止，就没有必要更新了
		}
		String sql = "update sessions set session=? where id=?";
		List<Object> params = new ArrayList<Object>();  
        params.add(SerializableUtils.serialize(session));  
        params.add(session.getId());  
        try {
			jdbcHelper.updateByPreparedStatement(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doDelete(Session session) {
		String sql = "delete from sessions where id=?";
		List<Object> params = new ArrayList<Object>();  
        params.add(session.getId());  
        try {
			jdbcHelper.updateByPreparedStatement(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		String sql = "insert into sessions(id, session) values(?,?)";
		List<Object> params = new ArrayList<Object>();  
        params.add(sessionId);  
        params.add(SerializableUtils.serialize(session));  
        try {
			jdbcHelper.updateByPreparedStatement(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return session.getId();
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		String sql = "select session from sessions where id=?";
		List<Object> params = new ArrayList<Object>();  
        params.add(sessionId);  
		List<String> sessionStrList = null;
		try {
			sessionStrList = jdbcHelper.findMoreRefResult(sql, params, String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sessionStrList.size() == 0) return null;
		return SerializableUtils.deserialize(sessionStrList.get(0));
	}

}
