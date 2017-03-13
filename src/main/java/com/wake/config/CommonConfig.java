package com.wake.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class CommonConfig implements BeanFactoryPostProcessor {

	public static String FIELD;
	
	private static boolean inited = false;// 是否已初始化

	public void setFIELD(String field) throws Exception {
		if (inited) {
			throw new Exception("赋值异常");
		} 
		CommonConfig.FIELD = field;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		inited = true;
	}
}