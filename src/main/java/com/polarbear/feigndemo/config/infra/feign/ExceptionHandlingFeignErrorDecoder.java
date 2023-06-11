package com.polarbear.feigndemo.config.infra.feign;

import feign.Feign;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class ExceptionHandlingFeignErrorDecoder implements ErrorDecoder {

    private static final Set<String>LOGGING_ENABLED_CONTENT_TYPE= new HashSet<>();

    private final ApplicationContext applicationContext;
    private final FeignDefaultErrorDecoder defaultDecoder;
    private final Map<String, FeignExceptionHandler> exceptionHandlerMap = new HashMap<>();

    public ExceptionHandlingFeignErrorDecoder(ApplicationContext applicationContext,
                                              FeignDefaultErrorDecoder defaultDecoder) {
        this.applicationContext = applicationContext;
        this.defaultDecoder = defaultDecoder;

        LOGGING_ENABLED_CONTENT_TYPE.add(MediaType.APPLICATION_JSON_VALUE);
        LOGGING_ENABLED_CONTENT_TYPE.add(MediaType.APPLICATION_XML_VALUE);
        LOGGING_ENABLED_CONTENT_TYPE.add(MediaType.TEXT_HTML_VALUE);
        LOGGING_ENABLED_CONTENT_TYPE.add(MediaType.TEXT_PLAIN_VALUE);
    }

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<String, Object> feignClients = applicationContext.getBeansWithAnnotation(FeignClient.class);
        List<Method> clientMethods = feignClients.values().stream()
                .map(Object::getClass)
                .map(aClass -> aClass.getInterfaces()[0])
                .map(ReflectionUtils::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        for (Method m : clientMethods) {
            String configKey = Feign.configKey(m.getDeclaringClass(), m);
            HandleFeignError handlerAnnotation = getHandleFeignErrorAnnotation(m);
            if (handlerAnnotation != null) {
                FeignExceptionHandler handler = applicationContext.getBean(handlerAnnotation.value());
                exceptionHandlerMap.put(configKey, handler);
            }
        }
    }

    private HandleFeignError getHandleFeignErrorAnnotation(Method m) {
        HandleFeignError result = m.getAnnotation(HandleFeignError.class);
        if (result == null) {
            result = m.getDeclaringClass().getAnnotation(HandleFeignError.class);
        }
        return result;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignExceptionHandler handler = exceptionHandlerMap.get(methodKey);
        if (handler != null) {
            return handler.handle(methodKey, response, getBody(response));
        }
        return defaultDecoder.handle(methodKey, response, getBody(response));
    }


    private String getBody(Response response) {
        Map<String, Collection<String>> headers = response.headers();
        String contentType = Optional.ofNullable(headers.get(HttpHeaders.CONTENT_TYPE))
                .flatMap(collection -> collection.stream().findFirst()).orElse(null);

        if (contentType == null ||LOGGING_ENABLED_CONTENT_TYPE.stream().noneMatch(contentType::contains)) {
            return "";
        }

        Response.Body body = response.body();
        if (body == null) {
            return "";
        }

        try {
            return StreamUtils.copyToString(response.body().asInputStream(), Charset.defaultCharset());
        } catch (Exception e) {
            return "";
        }
    }
}