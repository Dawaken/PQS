package com.wake.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wake.dao.base.BaseDao;
import com.wake.dao.base.helper.PageBean;
import com.wake.entity.Users;

@Repository
public class UsersDao extends BaseDao<Users, Long> {

	public Users getUsersListByUsername(String username) {
		return getUniqueByHQL("from Users r where r.username = ?", username); 
	}
	
	@SuppressWarnings("unchecked")
	public List<Users> queryUsers(PageBean pageBean) {
		Map<String, Object> condition = new HashMap<String, Object>();
		StringBuffer sqlsb=new StringBuffer();
		sqlsb.append("from Users r where 1=1");
		sqlsb.append(" order by r.adddate desc");
		return queryPageListByHQL(pageBean, sqlsb.toString(), condition);
	}
}
