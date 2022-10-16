package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.PlanDeVueloDao;
import com.redexbackend.models.PlanDeVuelo;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class PlanDeVueloDaoImp implements PlanDeVueloDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<PlanDeVuelo> getAll() {
    List<PlanDeVuelo> list = null;
    try{
      var hql = "from PlanDeVuelo as p";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public PlanDeVuelo get(int id) {
    PlanDeVuelo planDeVuelo = null;
    try {
      planDeVuelo = entityManager.find(PlanDeVuelo.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return planDeVuelo;
  }

  @Override
  public PlanDeVuelo insert(PlanDeVuelo planDeVuelo) {
    PlanDeVuelo result = null;
    try {
      result = entityManager.merge(planDeVuelo);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public PlanDeVuelo update(PlanDeVuelo planDeVuelo) {
    PlanDeVuelo result = null;
    try {
      var nuevo = this.get(planDeVuelo.getId());
      if(planDeVuelo.getCodigo() != null) nuevo.setCodigo(planDeVuelo.getCodigo());
      if(planDeVuelo.getFechaPlan() != null) nuevo.setFechaPlan(planDeVuelo.getFechaPlan());
      if(planDeVuelo.getFechaCompletado() != null) nuevo.setFechaCompletado(planDeVuelo.getFechaCompletado());
      if(planDeVuelo.getDuracionTotal() != null) nuevo.setDuracionTotal(planDeVuelo.getDuracionTotal());
      if(planDeVuelo.getEnvio() != null) nuevo.setEnvio(planDeVuelo.getEnvio());
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public boolean delete(int id) {
    boolean result = false;
    try {
      entityManager.remove(entityManager.merge(this.get(id)));
      result = true;
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return result;
  }
  
}
