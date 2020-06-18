package com.ucas.paper.config;

import com.ucas.paper.interceptor.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**上传地址*/
    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.mappath}")
    private String mapPath;


//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/admin/main.html").setViewName("admin/dashboard");
//    }

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login","/admin");
    }


    //        addResourceHandler是指你想在url请求的路径
    // addResourceLocations是图片存放的真实路
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(mapPath + "/**").addResourceLocations("file:"+filePath);
    }
}


