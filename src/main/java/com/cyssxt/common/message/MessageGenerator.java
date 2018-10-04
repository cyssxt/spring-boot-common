package com.cyssxt.common.message;

import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import static org.objectweb.asm.Opcodes.*;

public class MessageGenerator {

    private final static Logger logger = LoggerFactory.getLogger(MessageGenerator.class);

    private static BufferedReader templateReader = null;


    public static void generator(String basePackage,Map<String, Map<String, String>> langMap) throws IOException {
        System.out.println(langMap);
        Iterator<String> item = langMap.keySet().iterator();
        while (item.hasNext()) {
            String key = item.next();
            Map<String, String> codeMap = langMap.get(key);
            generator(String.format("%s","MessageCode"),basePackage,codeMap);
        }
    }

    public static void generator(String filename,String basePackage, Map<String,String> fieldMap) throws IOException {
        String outputDir = "src/main/java";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+basePackage.replaceAll("/","\\.")+";");
        stringBuffer.append("\r\n");
        stringBuffer.append("public interface "+filename+"{");
        stringBuffer.append("\r\n");
        Iterator<String> iterator = fieldMap.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String fieldName = key.replaceAll("\\.","_");
            stringBuffer.append("\tString "+fieldName.toUpperCase() +"=\""+key+"\";");
            stringBuffer.append("\r\n");
        }
        stringBuffer.append("}");
//        ClassWriter classWriter = new ClassWriter(0);
        basePackage = basePackage.replaceAll("\\.","/");
//        classWriter.visit(52, ACC_PUBLIC  + ACC_INTERFACE, basePackage+"/"+filename, null, "java/lang/Object",new String[]{});
//        Iterator<String> iterator = fieldMap.keySet().iterator();
//        while(iterator.hasNext()){
//            String key = iterator.next();
//            String fieldName = key.replaceAll("\\.","_");
//            String value = fieldMap.get(key);
//            classWriter.visitField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, fieldName.toUpperCase(), "Ljava/lang/String;", null, value).visitEnd();
//        }
//        classWriter.visitEnd();
        File pFile = new File(String.format("%s/%s",outputDir,basePackage));
        if(!pFile.exists()){
            pFile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(new File(String.format("%s/%s/%s.java",outputDir,basePackage,filename)));
        fos.write(stringBuffer.toString().getBytes());
        fos.close();
    }
}
