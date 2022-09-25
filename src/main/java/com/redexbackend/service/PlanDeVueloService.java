package com.redexbackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redexbackend.dao.PlanDeVueloDao;
import com.redexbackend.models.PlanDeVuelo;

@Service
public class PlanDeVueloService {

  @Autowired
  private PlanDeVueloDao daoPlanDeVuelo;

  public List<PlanDeVuelo> getAll(){
    return daoPlanDeVuelo.getAll();
  }
  public PlanDeVuelo get(int id){
    return daoPlanDeVuelo.get(id);
  }
  public PlanDeVuelo insert(PlanDeVuelo planDeVuelo){
    return daoPlanDeVuelo.insert(planDeVuelo);
  }
  public PlanDeVuelo update(PlanDeVuelo planDeVuelo){
    return daoPlanDeVuelo.update(planDeVuelo);
  }
  public boolean delete(int id){
    return daoPlanDeVuelo.delete(id);
  }
  
}
