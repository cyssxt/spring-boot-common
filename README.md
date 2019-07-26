# spring-boot-common

### 一、全局异常抛出
<pre><code>
 throw new ValidException("user.should.login");
 //return 
     {
        "retCode":"user.should.login"  
     }
 throw new ValidException("user.should.login","login");
 
 //return 
 {
    "exception":"login"
    "retCode":"user.should.login"
 }
 //返回默认错误：
 return ResponseData.getDefaultFailResponse();
</code></pre>
### 二、国际化
#### 1、配置
<pre><code>resources 资源目录
└── message  消息文件夹
        ├── default.properties 默认提示
        ├── en.properties 英文提示
        └── zh-cn.properties 中文简体提示 
</code></pre>
#### 2、code提示
##### default.properties
<pre><code>user.should.login=用户没有登陆</code></pre>
##### en.properties
<pre><code>user.should.login=user should be login</code></pre>
##### zh-cn.properties
<pre><code>user.should.login=用户需要登陆</code></pre>
#### 3、注意default.properties优先级最低，如果其他国际化没有配置，则会读取default.properties
#### 4、使用
<pre><code>throw new ValidException("user.should.login")</code></pre>
### 5、配置docbase tomcat启动的context路径
<pre><code>
server.tomcat.docbase:/data/tmp/springboot默认值
</code></pre>
### 三、用户校验
#### 1、实现UserLoginListener方法 示例
<pre><code>
@Component("userLoginListener")
public class UserLoginListenerImpl extends UserLoginListener {
    @Override
    public void cacheUserInfo(String sessionId) throws ValidException {
    //缓存用户信息
    }

    @Override
    public boolean login(Authorization authorization) throws ValidException {
        //判断用户是否登陆授权 false表示未授权
        return false;
    }
}
</code></pre>
#### 2、Authorization（授权校验）
默认登陆校验
existSession校验入参是否校验sessionId
### UserLoginListener  用户登陆校验需要override

### 实体基础类
<pre><code>
    CommonEntity 是实体类基础类，包含row_id ,del_flag,create_time,update_time4个基础字段
</code></pre>

### Copy类介绍
<pre><code>
//可以直接拷贝当前实例 到T对象中
public <T>T parse(T t,Filter filter);
//如果valid返回false则表示不拷贝，o是当前实例对象 key是字段名
public interface Filter {
    boolean valid(String key,Object o);
}
</code></pre>

### 工具类
#### 1.DateUtils
<pre><code>
public final static String YYYYMMDD = "yyyyMMdd";
public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
public final static String YYYY_MM_DD = "yyyy-MM-dd";
public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
public final static String HH_MM = "HH:mm";
public static final String HHMM = "HHmm";
public static final String YYYYMM = "yyyyMM";
public static final String MM = "MM";
//获取当前时间
public static Timestamp getCurrentTimestamp()
//向后退小时数
public static Timestamp getCurrentTimestampBeforeHour(int hour);
//获取时间格式化
public static String getDataFormatString(Date date,String format,Locale locale)
//获取时间格式化
public static String getDataFormatString(Date date,String format);
//获取时间整型值
public static Integer getDataFormatInteger(Date date,String format);
//获取时间整型值
public static Integer getDataFormatInteger(Calendar date,String format);
//获取时间整型值
public static Integer getDataFormatInteger(Calendar date);
//同上
public static Integer getDataFormatInteger(Date date);
public static Integer getCurrentDataFormatInteger();
//格式化时间
public static String format(Date timestamp,String format);
//字符串转时间
public static Date strToDate(String date,String format) throws ValidException ;
//获取一天的开始时间
public static Timestamp getStartTimeOfDay(Date date);
//获取一天结束时间
public static Timestamp getEndTimeOfDay(Date date);
//Date转Timestamp
public static Timestamp dateToTimestamp(Date date);
//获取当前的时间格式
public static String getCurrentDateFormatStr(String format);
</code></pre>


    

