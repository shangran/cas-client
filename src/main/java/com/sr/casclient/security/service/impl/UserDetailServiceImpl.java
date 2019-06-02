package com.sr.casclient.security.service.impl;

import java.util.Collections;

import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken>{

  @SuppressWarnings("unchecked")
  @Override
  public UserDetails loadUserDetails(CasAssertionAuthenticationToken token)
      throws UsernameNotFoundException {
    String principalName = token.getName();
    UserDetails detail = new User(principalName, "", true, true, true, true, Collections.EMPTY_LIST);
    return detail;
  }

}
