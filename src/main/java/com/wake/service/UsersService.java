package com.wake.service;

import java.util.List;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.MySQLCodec;
import org.owasp.esapi.codecs.MySQLCodec.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wake.dao.UsersDao;
import com.wake.dao.base.helper.PageBean;
import com.wake.entity.Users;

@Service
public class UsersService {

	private Logger logger = LoggerFactory.getLogger(UsersService.class);

	Codec MYSQL_CODEC = new MySQLCodec(Mode.ANSI);
	
	@Autowired
	private UsersDao usersDao;

	public Users getByPrimaryKey(Long id) {
		Users users = null;
		try {
			users = usersDao.getByPrimaryKey(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return users;
	}

	public boolean deleteByPrimaryKey(Long id) {
		boolean result = false;
		try {
			usersDao.deleteByPrimaryKey(id);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public boolean delete(Users users) {
		boolean result = false;
		try {
			usersDao.delete(users);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public boolean save(Users users) {
		boolean result = false;
		try {
			usersDao.save(users);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public boolean update(Users users) {
		boolean result = false;
		try {
			usersDao.update(users);
			result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public List<Users> listAll() {
		List<Users> list = null;
		try {
			list = usersDao.listAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}

	public Users getUsersListByUsername(String username) {
		username = ESAPI.encoder().encodeForSQL(MYSQL_CODEC, username); 
		Users users = null;
		try {
			users = usersDao.getUsersListByUsername(username);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return users;
	}

	public List<Users> queryUsers(PageBean pageBean) {
		List<Users> list=null;
		try {
			list = usersDao.queryUsers(pageBean);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
}
