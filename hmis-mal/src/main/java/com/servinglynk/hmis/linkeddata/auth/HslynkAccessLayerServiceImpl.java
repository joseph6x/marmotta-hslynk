/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servinglynk.hmis.linkeddata.auth;

import com.servinglynk.hmis.linkeddata.services.HslynkAccessLayerService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class HslynkAccessLayerServiceImpl implements HslynkAccessLayerService {

  private static final String REST_URI = "https://www.hslynk.com/hmis-user-service/rest/apimethodauthcheck";
  private Client client = ClientBuilder.newClient();

  public HslynkAccessLayerServiceImpl() {
    
  }

  @Override
  public boolean validate(String method, String token) {
    Response get = client.target(REST_URI).path(method).request(MediaType.APPLICATION_JSON_TYPE).get();
    return true;
  }

  
  
  
}
