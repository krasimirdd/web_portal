package com.kdimitrov.edentist.config;

import com.kdimitrov.edentist.utils.StatisticsInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebConfig implements WebMvcConfigurer {

    private StatisticsInterceptor statisticsInterceptor;

    public WebConfig(StatisticsInterceptor statisticsInterceptor) {
        this.statisticsInterceptor = statisticsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statisticsInterceptor);
    }
}
