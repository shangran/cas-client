package com.sr.casclient.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.sr.casclient.security.util.RequestUtils;
import com.sr.casclient.security.util.ResponseUtils;


public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint{

  public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
    super(loginFormUrl);
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    if (RequestUtils.isAjaxRequest(request)) {
      ResponseUtils.printJson(response, getLoginFormUrl());
    } else {
      super.commence(request, response, authException);
    }
  }
  
  

}
