package com.tss.user_service.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：xiangjun.yang
 * @date ：Created in 2019/3/21 17:58
 * @description：配置fastjson作为消息转换器
 */
@Configuration
public class MessageConversionConfig {
    @Bean
    public HttpMessageConverters messageConverters() {
        //json
        //创建fastJson消息转换器
        FastJsonHttpMessageConverter jsonMessageConverter = new FastJsonHttpMessageConverter();
        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("utf-8"));
        //过滤并修改配置返回内容
        fastJsonConfig.setSerializerFeatures(
            //List字段如果为null,输出为[],而非null
                //SerializerFeature.WriteNullListAsEmpty,
                //字符类型字段如果为null,输出为"",而非null
//                SerializerFeature.WriteNullStringAsEmpty,
                //Boolean字段如果为null,输出为falseJ,而非null
                //SerializerFeature.WriteNullBooleanAsFalse,
                //消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
                SerializerFeature.DisableCircularReferenceDetect,
                //是否输出值为null的字段,默认为false。
                SerializerFeature.WriteMapNullValue
        );
        jsonMessageConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> jsonMediaTypes = new ArrayList<>();
        jsonMediaTypes.add(MediaType.APPLICATION_JSON);
        jsonMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        jsonMessageConverter.setSupportedMediaTypes(jsonMediaTypes);

        //xml
        MappingJackson2XmlHttpMessageConverter xmlMessageConverter = new MappingJackson2XmlHttpMessageConverter();
        xmlMessageConverter.setObjectMapper(new XmlMapper());
        xmlMessageConverter.setDefaultCharset(Charset.forName("utf-8"));
        List<MediaType> xmlMediaTypes = new ArrayList<>();
        xmlMediaTypes.add(MediaType.APPLICATION_XML);
        xmlMediaTypes.add(MediaType.TEXT_XML);
        xmlMessageConverter.setSupportedMediaTypes(xmlMediaTypes);

        return new HttpMessageConverters(Arrays.asList(jsonMessageConverter, xmlMessageConverter));
    }
}
