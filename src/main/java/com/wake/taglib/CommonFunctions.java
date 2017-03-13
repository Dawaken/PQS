package com.wake.taglib;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;

import com.wake.entity.Users;
import com.wake.service.UsersService;
import com.wake.spring.utils.SpringUtils;

public class CommonFunctions {

	public static String esapiString(String str) {
		if(StringUtils.isNotBlank(str)){
			return ESAPI.encoder().encodeForHTML(str);
		}else{
			return "";
		}
	}
	public static Users usersInfo(Long usersid) {
		return getUsersService().getByPrimaryKey(usersid);
	}
	
	private static UsersService usersService;

	public static UsersService getUsersService() {
		if (usersService == null) {
			usersService = SpringUtils.getBean(UsersService.class);
		}
		return usersService;
	}
}
