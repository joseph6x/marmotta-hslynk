/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servinglynk.hmis.linkeddata.services;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface HslynkAccessLayerService {

  public boolean validate(String method, String token);

  public void ping();
}
