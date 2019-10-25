package com.cyssxt.common.utils;

import java.util.ArrayList;
import java.util.List;

public class SqlUtil {

    public static String getSql(String tableName, List<String> fields, List<String> values){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("insert into %s ",tableName));
        stringBuffer.append("(");
        stringBuffer.append(String.join(",",fields));
        stringBuffer.append(") values ");
        List<String> tmp = new ArrayList<>();
        for(int i=0;i<values.size();i++){
            tmp.add("?");
        }
        stringBuffer.append("(");
        stringBuffer.append(String.join(",",tmp));
        stringBuffer.append(")");
        return stringBuffer.toString();

    }
}
