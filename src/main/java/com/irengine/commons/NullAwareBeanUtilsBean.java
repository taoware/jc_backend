package com.irengine.commons;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;

/*
 * deep copy but ignore null attribute
 */
public class NullAwareBeanUtilsBean extends BeanUtilsBean {

	@Override
    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
		
		if (null != value)
			super.copyProperty(bean, name, value);
        
    }
}
