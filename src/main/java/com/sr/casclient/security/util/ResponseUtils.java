package com.sr.casclient.security.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseUtils {
  public static void printJson(HttpServletResponse response, Object data) {
    response.setContentType("application/json;charset=utf-8");
    try (PrintWriter out = response.getWriter();) {
      out.print(JSON.toJSONString(data));
      out.flush();
      out.close();
    } catch (IOException ex) {
      log.error("响应异常", ex);
    }
  }
}
