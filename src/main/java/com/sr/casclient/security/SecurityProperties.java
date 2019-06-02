package com.sr.casclient.security;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class SecurityProperties {
  
  /** 客户端地址 **/
  @Value("${domain}")
  private String domain;
  /** 客户端cas拦截登录的地址 **/
  @Value("${app.service.security}")
  private String appServiceSecurity;
  /** cas服务端地址 **/
  @Value("${cas.url.prefix}")
  private String casUrlPrefix;
  /** cas服务端登陆地址 **/
  @Value("${cas.url.login}")
  private String casUrlLogin;
  /** cas服务端登出地址 **/
  @Value("${cas.url.logout}")
  private String casUrlLogout;
  
  /** 匿名资源 **/
  @Value("${anonymousUrl}")
  private String anonymousUrl;
  /** 忽略资源 **/
  @Value("${ignoringUrl}")
  private String ignoringUrl;
  
  public String[] getAnonymousUrlArray() {
    return StringUtils.isBlank(anonymousUrl) ? ArrayUtils.EMPTY_STRING_ARRAY : anonymousUrl.split(",");
  }
  
  public String[] getIgnoringUrlArray() {
    return StringUtils.isBlank(ignoringUrl) ? ArrayUtils.EMPTY_STRING_ARRAY : ignoringUrl.split(",");
  }
  

}
