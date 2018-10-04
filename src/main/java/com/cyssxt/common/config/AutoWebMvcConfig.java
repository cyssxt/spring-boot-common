package com.cyssxt.common.config;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.serializer.MapSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cyssxt.common.config.bean.AutoSortHashMap;
import com.cyssxt.common.config.bean.FilterJSONSerializer;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.handler.VersionRequestMappingHandlerMapping;
import com.cyssxt.common.utils.FilterUtils;
import com.cyssxt.common.utils.ReflectUtils;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.thymeleaf.util.ArrayUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zqy on 15/05/2018.
 */
@Component
public class AutoWebMvcConfig extends WebMvcConfigurationSupport {
    class SortMapSerializer extends MapSerializer {
        @Override
        public void write(com.alibaba.fastjson.serializer.JSONSerializer jsonSerializer, Object object, Object fieldName, Type type, int features) throws IOException {
            List<String> filters = FilterUtils.get();
            if (CollectionUtils.isEmpty(filters)) {
                super.write(jsonSerializer, object, fieldName, type, features);
            }
            SerializeWriter out = jsonSerializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            out.write('{');
            Map map = (Map) object;
            for (int i = 0; i < filters.size(); i++) {
                String key = filters.get(i);
                Object value = map.get(key);
                out.writeFieldName(key);
                out.writeString(value + "");
                if (i < filters.size() - 1) {
                    out.write(',');
                }
            }
            out.write('}');
            return;
        }
    }

    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new VersionRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter() {
            @Override
            protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                super.writeInternal(object, outputMessage);
            }

        };
        //2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat, SerializerFeature.DisableCircularReferenceDetect);
        fastJsonConfig.setSerializeFilters((PropertyFilter) (o, s, o1) -> {
            if (ArrayUtils.contains(new String[]{"excludeFields", "includeFields"}, s)) {
                return false;
            }
            boolean includeFlag = true;
            boolean excludeFlag = false;
            boolean btoFlag = true;
            if (o instanceof BaseEntity && ((BaseEntity) o).getIncludeFields() != null) {
                includeFlag = ArrayUtils.contains(((BaseEntity) o).getIncludeFields(), s);
            }
            if (o instanceof BaseEntity && ((BaseEntity) o).getExcludeFields() != null) {
                excludeFlag = ArrayUtils.contains(((BaseEntity) o).getExcludeFields(), s);
            }
            if (o instanceof BaseEntity && ((BaseEntity) o).loadBtoClass() != null) {
                btoFlag = ReflectUtils.hasField(((BaseEntity) o).loadBtoClass(), s);
            }
            if (o instanceof FilterJSONSerializer && ((FilterJSONSerializer) o).getExcludeFields() != null) {
                excludeFlag = ArrayUtils.contains(((FilterJSONSerializer) o).getExcludeFields(), s);
            }
            List<String> filters = FilterUtils.get();
            //主要返回结果是map,则进行全局过滤
            if (o instanceof Map && !CollectionUtils.isEmpty(filters)) {
                includeFlag = filters.contains(s);
            }
            return !excludeFlag && btoFlag && includeFlag;
        });
        fastJsonConfig.getSerializeConfig().put(AutoSortHashMap.class, new SortMapSerializer());
//        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        ParserConfig.getGlobalInstance().putDeserializer(Timestamp.class, new SqlDateDeserializer(true) {
            @Override
            public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
                return super.deserialze(parser, clazz, fieldName);
            }
        });
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //5.将convert添加到converters当中
        return new HttpMessageConverters((HttpMessageConverter<?>) fastJsonHttpMessageConverter);
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter(){
//            @Override
//            protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//                System.out.println("asdasd");
//                super.writeInternal(object, outputMessage);
//            }
//
//        };
//        //2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        //3处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        //4.在convert中添加配置信息.
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//        //5.将convert添加到converters当中.
//        converters.add(fastJsonHttpMessageConverter);
//    }
}
