package com.company.runman.utils;

import com.company.news.query.PaginationData;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/23.
 */
public class InvokeUtils {
    /**
     * @param o
     * @param list
     * @param parentName
     */
    public static void objectAttSToStringList(Object o, List list, String parentName) {
        if (o == null) return;
        Class c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            Method method = null;
            Object value = null;
            String name = field.getName();
            String upperName = name.substring(0, 1).toUpperCase()
                    + name.substring(1);
            try {
                method = c.getMethod("get" + upperName);
                value = method.invoke(o);
                if (value == null) {
                    continue;
                }
                //特殊处理子类
                if (value instanceof PaginationData) {

                    objectAttSToStringList(value, list, name);
                    continue;
                }else if(value instanceof Map){
                   if( ((Map) value).isEmpty()){
                       continue;
                   }
                }
                else if(value instanceof List){
                    if( ((List) value).isEmpty()){
                        continue;
                    }
                }
                if (parentName != null) {
                    list.add(parentName + "." + name + "=" + value);
                } else {
                    list.add(name + "=" + value);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
              //  e.printStackTrace();
            }

        }
    }
}
