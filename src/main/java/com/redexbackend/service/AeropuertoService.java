package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.AeropuertoDao;
import com.redexbackend.models.Aeropuerto;

@Service
public class AeropuertoService {

  @Autowired
  private AeropuertoDao daoAeropuerto;

  public List<Aeropuerto> getAll(){
    return daoAeropuerto.getAll();
  }
  public Aeropuerto get(int id){
    return daoAeropuerto.get(id);
  }
  public Aeropuerto insert(Aeropuerto aeropuerto){
    return daoAeropuerto.insert(aeropuerto);
  }
  public Aeropuerto update(Aeropuerto aeropuerto){
    return daoAeropuerto.update(aeropuerto);
  }
  public boolean delete(int id){
    return daoAeropuerto.delete(id);
  }
}
