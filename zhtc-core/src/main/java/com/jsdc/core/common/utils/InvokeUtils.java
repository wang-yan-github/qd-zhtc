package com.jsdc.core.common.utils;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 */
public class InvokeUtils {

    /**
     * 获取值为null的属性名
     */
    public static String[] getIgnoreProperties(Object object, String... ignoreProperties) {
        // 需要排除的属性列表
        List<String> ignoreList = new ArrayList<>();
        // 默认将id作为排除属性
        ignoreList.add("id");
        // 将自定义排除属性加入列表
        for (String s : ignoreProperties) {
            ignoreList.add(s);
        }
        // 对象的全部属性
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(object.getClass());
        // 遍历全部属性
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value = null;
            try {
                // 反射取值
                value = propertyDescriptor.getReadMethod().invoke(object);
            } catch (Throwable throwable) {
                //e.printStackTrace();
            }
            // 值为null就加入排除列表
            if (value == null || value.equals("")) {
                ignoreList.add(propertyDescriptor.getName());
            }
        }
        // List转Array
        String[] strings = new String[ignoreList.size()];
        ignoreList.toArray(strings);
        return strings;
    }
}
