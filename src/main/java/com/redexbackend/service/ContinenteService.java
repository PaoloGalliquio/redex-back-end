package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.ContinenteDao;
import com.redexbackend.models.Continente;

@Service
public class ContinenteService {

  @Autowired
  private ContinenteDao daoContinente;

  public List<Continente> getAll(){
    return daoContinente.getAll();
  }
  public Continente get(int id){
    return daoContinente.get(id);
  }
  public Continente insert(Continente continente){
    return daoContinente.insert(continente);
  }
  public Continente update(Continente continente){
    return daoContinente.update(continente);
  }
  public boolean delete(int id){
    return daoContinente.delete(id);
  }
  
}
