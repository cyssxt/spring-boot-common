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
### 三、用户校验
1、Authorization（授权校验）
默认登陆校验
existSession校验入参是否校验sessionId
### UserLoginListener  用户登陆校验需要override



    

