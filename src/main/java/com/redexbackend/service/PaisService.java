package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.PaisDao;
import com.redexbackend.models.Pais;

@Service
public class PaisService {

  @Autowired
  private PaisDao daoPais;

  public List<Pais> getAll(){
    return daoPais.getAll();
  }
  public Pais get(int id){
    return daoPais.get(id);
  }
  public Pais insert(Pais pais){
    return daoPais.insert(pais);
  }
  public Pais update(Pais pais){
    return daoPais.update(pais);
  }
  public boolean delete(int id){
    return daoPais.delete(id);
  }
  
}
