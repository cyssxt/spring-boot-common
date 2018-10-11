package com.cyssxt.common.message;

import com.cyssxt.common.message.bean.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHelper {

    private final static Logger logger = LoggerFactory.getLogger(MessageHelper.class);
    public static final String DEFAULT = "default";
    private final static Pattern LANG_REG = Pattern.compile("message/([a-zA-Z]+).properties");

    private MessageHelper(){}

    private String defaultLang;

    private MessageHelper(String defaultLang) {
        this.defaultLang = defaultLang;
    }

    private final static Map<String,Map<String,String>> langMap = new HashMap<>();
    private final static Map<String,Message> messages = new HashMap<>();

    static {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageInfo getMessageInfo(String code) {
        String value = getString(code);
        return new MessageInfo(code,value);
    }

    private static class Message{
        private String lang;

        public Message(String lang) {
            this.lang = lang;
        }

        private String getLang() {
            return lang==null?DEFAULT:lang;
        }

        public MessageInfo getMessageInfo(String key,String lang){
            String value = getString(lang);
            return new MessageInfo(key,value);
        }

        public String getString(String key,String lang){
            Map<String,String> map = langMap.get(lang);
            if(null==map){
                map = langMap.get(DEFAULT);
            }
            String result = null;
            if(null!=map){
                result = map.get(key);
            }
            return result;
        }

        public String getString(String key){
            return getString(key,getLang());
        }

        public Integer getInteger(String key,String lang){
            String value = getString(key,lang);
            return Integer.valueOf(value);
        }

        public Integer getInteger(String key){
            return getInteger(key,getLang());
        }
    }

    public static Message getInstance(){
        return getInstance(DEFAULT);
    }

    public static Message getInstance(String lang){
        if(!StringUtils.isEmpty(lang)){
            lang = DEFAULT;
        }
        Message message = messages.get(lang);
        if(null==message){
            message = new Message(lang);
            messages.put(lang,message);
        }
        return message;
    }

    public static void initLang(File file) throws IOException {
        String fileName = file.getName();
        fileName = fileName.split(".properties")[0];
        String lang = DEFAULT;
        if(!StringUtils.isEmpty(fileName)) {
            lang = fileName;
        }
        initWithIS(new FileInputStream(file), lang);
    }

    private static void initWithIS(InputStream is,String lang) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        Iterator iterator = properties.keySet().iterator();
        Map<String,String> map = langMap.get(lang);
        if(map==null){
            map = new HashMap<>();
            langMap.put(lang,map);
        }
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            String value = properties.getProperty(key);
            map.put(key,value);
        }
        langMap.put(lang,map);
    }
    public static void init() throws IOException {
        init(null);
    }
    public static void init(String basePackage) throws IOException {
        ClassLoader classLoader = MessageHelper.class.getClassLoader();
        Enumeration<URL> is = classLoader.getResources("message");
        while (is!=null && is.hasMoreElements()){
            URL url = is.nextElement();
            System.out.println("======"+url.getPath());
            String protocol = url.getProtocol();
            if("file".equals(protocol)){
                File pFile = new File(url.getPath());
                if(pFile.isDirectory()){
                    File[] files  = pFile.listFiles();
                    for(File file:files){
                        try {
                            initLang(file);
                        } catch (IOException e) {
                            logger.error("init ={}",e);
                        }
                    }
                }
            }else if("jar".equals(protocol)){
                //遍历获取jar中的国际化内容
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();
                    Matcher matcher = LANG_REG.matcher(name);
                    if(matcher.find()){
                        String lang = matcher.group(1);
                        InputStream inputStream = classLoader.getResourceAsStream(name);
                        initWithIS(inputStream,lang);
                    }
                }
            }
        }
        if(!StringUtils.isEmpty(basePackage)) {
            System.out.println(basePackage);
            System.out.println(langMap+"====");
//            MessageGenerator.generator(basePackage,langMap);
        }
    }

    public static String getString(String key,String lang){
        Message message = getInstance(lang);
        return message.getString(key);
    }

    public static String getString(String key){
        return getString(key,null);
    }

    public static void main(String[] args) {
        String message = MessageHelper.getInstance().getString("test");
        System.out.println(message);
    }
}
