package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.CiudadDao;
import com.redexbackend.models.Ciudad;

@Service
public class CiudadService {

  @Autowired
  private CiudadDao daoCiudad;

  public List<Ciudad> getAll(){
    return daoCiudad.getAll();
  }
  public Ciudad get(int id){
    return daoCiudad.get(id);
  }
  public Ciudad insert(Ciudad ciudad){
    return daoCiudad.insert(ciudad);
  }
  public Ciudad update(Ciudad ciudad){
    return daoCiudad.update(ciudad);
  }
  public boolean delete(int id){
    return daoCiudad.delete(id);
  }
  
}
