package com.ucas.paper.config;

import com.ucas.paper.interceptor.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
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

    @Bean
    public MultipartConfigElement multipartConfigElement() {
       MultipartConfigFactory factory = new MultipartConfigFactory();
       //单个文件最大
         factory.setMaxFileSize(DataSize.ofMegabytes(80)); //MB
        //factory.setMaxFileSize(DataSize.ofKilobytes(80)); //KB
       //factory.setMaxFileSize(DataSize.ofGigabytes(80)); //Gb
        /// 设置总上传数据总大小
//        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
        return factory.createMultipartConfig();
   }
}


