package com.sr.casclient.security.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.sr.casclient.security.SecurityProperties;
import com.sr.casclient.security.handler.MyAuthenticationSuccessHandler;
import com.sr.casclient.security.handler.MyLoginUrlAuthenticationEntryPoint;
import com.sr.casclient.security.service.impl.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
  
  @Autowired
  private SecurityProperties securityProperties;
  
  @Autowired
  private UserDetailServiceImpl userDetailsService;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder build) {
    build.authenticationProvider(casAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
    .antMatchers(securityProperties.getAnonymousUrlArray()).permitAll()
    .anyRequest().authenticated()
    .and().exceptionHandling()
    // 自定义认证失败处理器
    .authenticationEntryPoint(new MyLoginUrlAuthenticationEntryPoint(securityProperties.getCasUrlLogin()))
    .and().csrf()
    .disable();
    
    // 单点拦截器
    http.addFilter(casAuthenticationFilter())
    .addFilterBefore(casLogoutFilter(), LogoutFilter.class)
    .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);

    // 单点认证提供者
    http.authenticationProvider(casAuthenticationProvider());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // 设置忽略资源
    web.ignoring().antMatchers(securityProperties.getIgnoringUrlArray());
  }
  
  /** CAS认证过滤器 */
  @Bean
  public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
    CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
    casAuthenticationFilter.setAuthenticationManager(authenticationManager());
    casAuthenticationFilter.setFilterProcessesUrl(securityProperties.getAppServiceSecurity());
    casAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
    return casAuthenticationFilter;
  }

  /**
   * cas认证提供者
   */
  public CasAuthenticationProvider casAuthenticationProvider() {
    CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
    casAuthenticationProvider.setAuthenticationUserDetailsService(userDetailsService);
    casAuthenticationProvider.setServiceProperties(serviceProperties());
    casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
    casAuthenticationProvider.setKey("casAuthenticationProviderKey");
    return casAuthenticationProvider;
  }
  
  /**认证的入口*/
  @Bean
  public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
      CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
      casAuthenticationEntryPoint.setLoginUrl(securityProperties.getCasUrlLogin());
      casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
      return casAuthenticationEntryPoint;
  }

  /** 指定service相关信息 */
  @Bean
  public ServiceProperties serviceProperties() {
    ServiceProperties sp = new ServiceProperties();
    sp.setService(securityProperties.getDomain() + securityProperties.getAppServiceSecurity());
    sp.setSendRenew(false);
    return sp;
  }
  
  /**
   * 票据验证器
   */
  @Bean
  public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
      return new Cas20ServiceTicketValidator(securityProperties.getCasUrlPrefix());
  }
  
  /**单点登出过滤器*/
  @Bean
  public SingleSignOutFilter singleSignOutFilter() {
      SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
      singleSignOutFilter.setCasServerUrlPrefix(securityProperties.getCasUrlPrefix());
      singleSignOutFilter.setIgnoreInitConfiguration(true);
      return singleSignOutFilter;
  }
  
  /**请求单点退出过滤器*/
  @Bean
  public LogoutFilter casLogoutFilter() {
      LogoutFilter logoutFilter = new LogoutFilter(securityProperties.getCasUrlLogout(), new SecurityContextLogoutHandler());
      logoutFilter.setFilterProcessesUrl("/logout");
      return logoutFilter;
  }

}
