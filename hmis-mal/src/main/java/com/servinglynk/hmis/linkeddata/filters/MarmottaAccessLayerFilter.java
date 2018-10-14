/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servinglynk.hmis.linkeddata.filters;

import com.servinglynk.hmis.linkeddata.auth.HslynkAccessLayerServiceImpl;
import com.servinglynk.hmis.linkeddata.services.HslynkAccessLayerService;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.marmotta.platform.core.exception.security.AccessDeniedException;
import org.apache.marmotta.platform.ldp.webservices.LdpWebService;
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
  private HslynkAccessLayerService hslynkAccessLayerService;

  @Override
  public String getPattern() {
    return "/ldp/.*";
  }

  @Override
  public int getPriority() {
    return PRIO_ACL;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    hslynkAccessLayerService.ping();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (configurationService.getBooleanConfiguration("hmis-mal.enabled", true)
        && request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      String LDP_methodID = httpRequest.getServletPath();
      //Is it a LDP request?
      if (LDP_methodID.startsWith(LdpWebService.PATH)) {
        String token = httpRequest.getHeader(HslynkAccessLayerServiceImpl.AUTH_HEADER);
        if (!hslynkAccessLayerService.validate(LDP_methodID, token)) {
          throw new AccessDeniedException();
        }
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

}
