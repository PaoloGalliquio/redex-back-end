package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.ConfiguracionDao;
import com.redexbackend.models.Configuracion;

@Service
public class ConfiguracionService {

  @Autowired
  private ConfiguracionDao daoConfiguracion;

  public List<Configuracion> getAll(){
    return daoConfiguracion.getAll();
  }
  public Configuracion get(int id){
    return daoConfiguracion.get(id);
  }
  public Configuracion insert(Configuracion configuracion){
    return daoConfiguracion.insert(configuracion);
  }
  public Configuracion update(Configuracion configuracion){
    return daoConfiguracion.update(configuracion);
  }
  public boolean delete(int id){
    return daoConfiguracion.delete(id);
  }
}
