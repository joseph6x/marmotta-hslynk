/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servinglynk.hmis.linkeddata.filters;

import java.io.IOException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.marmotta.platform.core.api.config.ConfigurationService;
import org.apache.marmotta.platform.core.api.modules.MarmottaHttpFilter;
import org.apache.marmotta.platform.core.api.user.UserService;
import org.apache.marmotta.platform.core.exception.security.AccessDeniedException;
import org.slf4j.Logger;

/**
 *
 * @author joseph6x
 */
@ApplicationScoped
public class MarmottaAccessLayerFilter implements MarmottaHttpFilter {

  @Inject
  private Logger log;

  @Inject
  private ConfigurationService configurationService;

  @Inject
  private UserService userService;

  @Override
  public String getPattern() {
    return "sdfsdfsdfsdfsd^/.*";
  }

  @Override
  public int getPriority() {
    return PRIO_ACL;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    log.info("Access Control Filter starting up. Access control is {}.", configurationService.getBooleanConfiguration("security.enabled", true) ? "enabled" : "disabled");
    if (!configurationService.getBooleanConfiguration("security.configured", true)) {
      //securityService.loadSecurityProfile(configurationService.getStringConfiguration("security.profile"));
    } else {
      //securityService.ping();
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    if (configurationService.getBooleanConfiguration("security.enabled", true)) {
      // check whether access is granted
      if (request instanceof HttpServletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        log.debug("Checking access for user '{}' with roles '{}'", httpRequest.getAttribute("user.name"),
            httpRequest.getAttribute("user.roles"));

        if (/*securityService.grantAccess(httpRequest)*/ false) {
          chain.doFilter(request, response);
        } else // handled by LMFAuthenticationFilter 
        {
          throw new AccessDeniedException();
        }
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
  }

}
