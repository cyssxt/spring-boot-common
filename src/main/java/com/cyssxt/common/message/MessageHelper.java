package com.cyssxt.common.message;

import com.cyssxt.common.message.bean.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MessageHelper {

    private final static Logger logger = LoggerFactory.getLogger(MessageHelper.class);
    public static final String DEFAULT = "default";

    private String lang;

    private MessageHelper(String lang) {
        this.lang = lang;
    }

    private final static Map<String,Map<String,String>> langMap = new HashMap<>();
    private final static Map<String,MessageHelper> helpers = new HashMap<>();

    static {
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MessageHelper getInstance(){
        return getInstance(DEFAULT);
    }

    public static MessageHelper getInstance(String lang){
        if(!StringUtils.isEmpty(lang)){
            lang = DEFAULT;
        }
        MessageHelper helper = helpers.get(lang);
        if(null==helper){
            helper = new MessageHelper(lang);
            helpers.put(lang,helper);
        }
        return helper;
    }

    public static void initLang(File file) throws IOException {
        String fileName = file.getName();
        fileName = fileName.split(".properties")[0];
        String lang = DEFAULT;
        if(!StringUtils.isEmpty(fileName)) {
            lang = fileName;
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        Iterator iterator = properties.keySet().iterator();
        Map<String,String> map = new HashMap<>();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            String value = properties.getProperty(key);
            map.put(key,value);
        }
        langMap.put(lang,map);
    }

    public static void init() throws FileNotFoundException {
        File pFile = ResourceUtils.getFile("classpath:message");
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
    }

    private String getLang() {
        return lang==null?DEFAULT:lang;
    }

    public Integer getInteger(String key,String lang){
        String value = getString(key,lang);
        return Integer.valueOf(value);
    }

    public Integer getInteger(String key){
        return getInteger(key,getLang());
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

    public MessageInfo getMessageInfo(String key,String lang){
        String value = getString(lang);
        return new MessageInfo(key,value);
    }

    public String getString(String key){
        return getString(key,getLang());
    }

    public static void main(String[] args) {
        String message = MessageHelper.getInstance().getString("test");
        System.out.println(message);
    }
}
