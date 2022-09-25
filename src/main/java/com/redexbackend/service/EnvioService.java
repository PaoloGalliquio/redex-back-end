package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.EnvioDao;
import com.redexbackend.models.Envio;

@Service
public class EnvioService {

  @Autowired
  private EnvioDao daoEnvio;

  public List<Envio> getAll(){
    return daoEnvio.getAll();
  }
  public Envio get(int id){
    return daoEnvio.get(id);
  }
  public Envio insert(Envio envio){
    return daoEnvio.insert(envio);
  }
  public Envio update(Envio envio){
    return daoEnvio.update(envio);
  }
  public boolean delete(int id){
    return daoEnvio.delete(id);
  }
  
}
