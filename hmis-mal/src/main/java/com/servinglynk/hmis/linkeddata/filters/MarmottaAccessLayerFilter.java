/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.servinglynk.hmis.linkeddata.filters;

import com.servinglynk.hmis.linkeddata.api.Authorization;
import com.servinglynk.hmis.linkeddata.auth.HslynkAccessLayerServiceImpl;
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
  private Authorization hslynkAccessLayerService;

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
        if (token == null || !hslynkAccessLayerService.validate(httpRequest.getMethod(), token)) {
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
