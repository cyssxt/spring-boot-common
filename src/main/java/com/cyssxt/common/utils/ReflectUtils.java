package com.cyssxt.common.utils;

import com.cyssxt.common.bean.ReflectBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zqy on 18/05/2018.
 */
public class ReflectUtils {
    private final static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    static Map<String, Map<String, Method>> sourceReader = new HashMap<>();
    static Map<String, Map<String, Method>> targetWriter = new HashMap<>();
    static Map<Class, Map<String, Boolean>> fieldMap = new HashMap<>();
    static Map<Class, List<String>> excludeMap = new HashMap<>();
    public static final int READ = 0;//读方法
    public static final int WRITE = 1;//写方法

    public static Map<String, Method> getReadMapper(Class clazz) throws IntrospectionException {
        return getMap(clazz, READ);
    }

    public static boolean hasField(Class clazz, String fieldName) {

        Map<String, Boolean> map = fieldMap.get(clazz);
        if (map == null) {
            map = new HashMap<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                map.put(name, true);
            }
            fieldMap.put(clazz, map);
        }
        return map.get(fieldName) == null ? false : map.get(fieldName);
    }

    public static boolean exclude(Class clazz, String fieldName) {
        List list = excludeMap.get(clazz);
        if (list == null) {
            try {
                String[] excludes = (String[]) clazz.getField("excludes").get(null);
                list = Arrays.asList(excludes);
                excludeMap.put(clazz, list);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                if (list == null) {
                    excludeMap.put(clazz, new ArrayList<>());
                }
            }
        }
        boolean flag = list.indexOf(fieldName) == -1 ? false : true;
        return flag;
    }

    public static Map<String, Method> getMap(Class clazz, int type) {
        Map<String, Map<String, Method>> mapper = type == READ ? sourceReader : targetWriter;
        String className = clazz.getName();
        Map<String, Method> sourceMap = mapper.get(className);
        if (sourceMap == null) {
            sourceMap = new HashMap<>();
            mapper.put(className, sourceMap);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Class dataType = field.getType();
                if (dataType == Logger.class) {
                    continue;
                }
                PropertyDescriptor pd = null;
                try {
                    pd = new PropertyDescriptor(fieldName, clazz);
                    Method read = type == READ ? pd.getReadMethod() : pd.getWriteMethod();
                    if (read == null) {
                        continue;
                    }
                    sourceMap.put(fieldName, read);
                } catch (IntrospectionException e) {
                    logger.error("e={}", e);
                }
            }
            Class supperClass = clazz.getSuperclass();
            if (supperClass != null) {
                Map<String, Method> parentMap = getMap(supperClass, type);
                sourceMap.putAll(parentMap);
            }
        }

        return sourceMap;
    }

    public static Map<String, Method> getWriteMap(Class clazz) throws IntrospectionException {
        return getMap(clazz, WRITE);
    }

    public static Map<String, ReflectBean> getBeanMap(Class clazz, int type, Boolean ignoreCase) {
        Map<String, ReflectBean> result = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Class dataType = field.getType();
            if (dataType == Logger.class) {
                continue;
            }
            PropertyDescriptor pd = null;
            String keyName = ignoreCase ? fieldName.toLowerCase() : fieldName;
            try {
                pd = new PropertyDescriptor(fieldName, clazz);
                Method method = type == READ ? pd.getReadMethod() : pd.getWriteMethod();
                if (method == null) {
                    continue;
                }
                Class fieldType = pd.getPropertyType();
                result.put(keyName, new ReflectBean(fieldName, method, fieldType));
            } catch (IntrospectionException e) {
                logger.error("e={}", e);
            }
        }
        Class supperClass = clazz.getSuperclass();
        if (supperClass != null) {
            Map<String, ReflectBean> parentMap = getBeanMap(supperClass, type, ignoreCase);
            result.putAll(parentMap);
        }
        return result;
    }

    public static void copyValue(ReflectBean reflectBean, Object object, Object result) throws InvocationTargetException, IllegalAccessException {
        Class type = reflectBean.getFieldType();
        logger.info("copyValue,key={}", object + "");
        Object param = object;
        if (object != null) {
            String obj = object + "";
            if (!StringUtils.isEmpty(obj.trim())) {
                if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                    param = "1".equals(obj) ? true : "true".equals(obj)?true:Boolean.valueOf(obj);
                } else if (type.equals(Integer.class) || type.equals(int.class)) {
                    param = Integer.valueOf(obj);
                } else if (type.equals(Double.class) || type.equals(double.class)) {
                    param = Double.valueOf(obj);
                } else if (type.equals(Float.class) || type.equals(float.class)) {
                    param = Float.valueOf(obj);
                } else if (type.equals(Long.class) || type.equals(Long.class)) {
                    param = Long.valueOf(obj);
                } else if (type.equals(Date.class)) {
                    if (CommonUtils.isInteger(obj)) {
                        param = new Date(Long.valueOf(obj));
                    } else {
                        param = new Date(obj);
                    }
                } else if (type.equals(Timestamp.class)) {
                    if (CommonUtils.isInteger(obj)) {
                        param = new Timestamp(Long.valueOf(obj));
                    } else {
                        param = Timestamp.valueOf(obj);
                    }
                } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                    if("false".equals(obj)){
                        param=(byte)0;
                    }else if("true".equals(obj)){
                        param=(byte)1;
                    }else {
                        param = Byte.valueOf(obj);
                    }
                } if(type.equals(String.class)){
                    param = obj;
                }
            }else{
                if(type.equals(String.class)){
                    param = object;
                }else{
                    param = null;
                }
            }
            logger.info("param={},{}", param, type);
            Method method = reflectBean.getMethod();
            method.invoke(result, param);
        }
    }
}
