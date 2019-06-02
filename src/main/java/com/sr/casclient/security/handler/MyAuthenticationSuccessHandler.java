package com.sr.casclient.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.sr.casclient.security.util.ResponseUtils;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    ResponseUtils.printJson(response, "登录成功。。。");
  }

}
