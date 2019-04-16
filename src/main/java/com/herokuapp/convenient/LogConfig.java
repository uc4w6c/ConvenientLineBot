package com.herokuapp.convenient;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.context.annotation.Bean;

@Configuration
public class LogConfig {
  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeClientInfo(true);
    filter.setIncludeQueryString(true);
    filter.setIncludeHeaders(true);
    filter.setIncludePayload(true);
    filter.setMaxPayloadLength(1024);
    return filter;
  }
}
