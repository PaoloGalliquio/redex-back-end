package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.VueloPorPlanDeVueloDao;
import com.redexbackend.models.VueloPorPlanDeVuelo;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class VueloPorPlanDeVueloDaoImp implements VueloPorPlanDeVueloDao {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<VueloPorPlanDeVuelo> getAll() {
    List<VueloPorPlanDeVuelo> list = null;
    try{
      var hql = "from VueloPorPlanDeVuelo as vxpv";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public VueloPorPlanDeVuelo get(int id) {
    VueloPorPlanDeVuelo vueloPorPlanDeVuelo = null;
    try {
      vueloPorPlanDeVuelo = entityManager.find(VueloPorPlanDeVuelo.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return vueloPorPlanDeVuelo;
  }

  @Override
  public VueloPorPlanDeVuelo insert(VueloPorPlanDeVuelo vueloPorPlanDeVuelo) {
    VueloPorPlanDeVuelo result = null;
    try {
      result = entityManager.merge(vueloPorPlanDeVuelo);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public VueloPorPlanDeVuelo update(VueloPorPlanDeVuelo vueloPorPlanDeVuelo) {
    VueloPorPlanDeVuelo result = null;
    try {
      var nuevo = this.get(vueloPorPlanDeVuelo.getId());
      if(vueloPorPlanDeVuelo.getFechaVuelo() != null) nuevo.setFechaVuelo(vueloPorPlanDeVuelo.getFechaVuelo());
      if(vueloPorPlanDeVuelo.getPlanDeVuelo() != null) nuevo.setPlanDeVuelo(vueloPorPlanDeVuelo.getPlanDeVuelo());
      if(vueloPorPlanDeVuelo.getVuelo() != null) nuevo.setVuelo(vueloPorPlanDeVuelo.getVuelo());
      result = entityManager.merge(nuevo);
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
