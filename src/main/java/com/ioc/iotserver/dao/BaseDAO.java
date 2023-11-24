package com.ioc.iotserver.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.lang.Nullable;

public abstract class BaseDAO { 
	
	@Autowired
    protected JdbcTemplate db; 
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	public <T> T getObject(JdbcTemplate db, String sql, Class<T> elementType, @Nullable Object... args) {

		return DataAccessUtils.singleResult(db.query(sql, args, SingleColumnRowMapper.newInstance(elementType) ));
	} 
	
	public int updateHistory(String org, String history, String idK, int idV) {

		return db.update("insert into " + history + " (select null, o.* from " + org + " o where " + idK + " = " + idV + ")");

	} 
	
	public int getUserId(String apikey) { 
		
		return getObject(db, "select uId from account where apikey = ? and status = 1 ", Integer.class, apikey);
	}

}
