package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.AeropuertoDao;
import com.redexbackend.models.Aeropuerto;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class AeropuertoDaoImp implements AeropuertoDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Aeropuerto> getAll() {
    List<Aeropuerto> list = null;
    try{
      var hql = "from Aeropuerto as a";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Aeropuerto get(int id) {
    Aeropuerto aeropuerto = null;
    try {
      aeropuerto = entityManager.find(Aeropuerto.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return aeropuerto;
  }

  @Override
  public Aeropuerto insert(Aeropuerto aeropuerto) {
    Aeropuerto result = null;
    try {
      result = entityManager.merge(aeropuerto);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Aeropuerto update(Aeropuerto aeropuerto) {
    Aeropuerto result = null;
    try {
      var nuevo = this.get(aeropuerto.getId());
      //if(aeropuerto.getPeriod() != null) nuevo.setPeriod(aeropuerto.getPeriod());
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
