/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servinglynk.hmis.linkeddata.auth;

import com.servinglynk.hmis.linkeddata.services.HslynkAccessLayerService;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.marmotta.platform.core.api.config.ConfigurationService;

public class HslynkAccessLayerServiceImpl implements HslynkAccessLayerService {

  public static final String AUTH_HEADER = "authorization";
  public static final String APP_HEADER = "x-hmis-trustedapp-id";
  private static final String TEST_AUTH = "HMISUserAuth session_token=AB4DAF678C0D41D6860873D18B83988EA55743AC82F64F4FACE1AE367D82F2F4";
  private static final String METHODID = "/CLIENT_API_SEARCH";
  private static final String APPID = "MASTER_TRUSTED_APP";

  @Inject
  private ConfigurationService configurationService;
  private Client client = ClientBuilder.newClient();

  @Override
  public boolean validate(String ldpPath, String token) {
    String halService = configurationService.getStringConfiguration("hmis-mal.hal");
    Invocation.Builder header = client.target(halService)
        .path(METHODID)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header(APP_HEADER, APPID)
        .header(AUTH_HEADER, token);
    Response get = header.get();
    return get.getStatus() == 200;
  }

  @Override
  public void ping() {
    assert validate(METHODID, TEST_AUTH);
  }

}
