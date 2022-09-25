package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.VueloDao;
import com.redexbackend.models.Vuelo;

@Service
public class VueloService {

  @Autowired
  private VueloDao daoVuelo;

  public List<Vuelo> getAll(){
    return daoVuelo.getAll();
  }
  public Vuelo get(int id){
    return daoVuelo.get(id);
  }
  public Vuelo insert(Vuelo vuelo){
    return daoVuelo.insert(vuelo);
  }
  public Vuelo update(Vuelo vuelo){
    return daoVuelo.update(vuelo);
  }
  public boolean delete(int id){
    return daoVuelo.delete(id);
  }
  
}
