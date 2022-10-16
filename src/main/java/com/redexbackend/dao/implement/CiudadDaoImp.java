package com.redexbackend.dao.implement;

import com.redexbackend.dao.CiudadDao;
import com.redexbackend.models.Ciudad;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class CiudadDaoImp implements CiudadDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Ciudad> getAll() {
    List<Ciudad> list = null;
    try{
      var hql = "from Ciudad as c";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Ciudad get(int id) {
    Ciudad ciudad = null;
    try {
      ciudad = entityManager.find(Ciudad.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return ciudad;
  }

  @Override
  public Ciudad insert(Ciudad ciudad) {
    Ciudad result = null;
    try {
      result = entityManager.merge(ciudad);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Ciudad update(Ciudad ciudad) {
    Ciudad result = null;
    try {
      var nuevo = this.get(ciudad.getId());
      if(ciudad.getCodigo() != null) nuevo.setCodigo(ciudad.getCodigo());
      if(ciudad.getNombre() != null) nuevo.setNombre(ciudad.getNombre());
      if(ciudad.getLatitud() != null) nuevo.setLatitud(ciudad.getLatitud());
      if(ciudad.getLongitud() != null) nuevo.setLongitud(ciudad.getLongitud());
      if(ciudad.getAeropuerto() != null) nuevo.setAeropuerto(ciudad.getAeropuerto());
      if(ciudad.getPais() != null) nuevo.setPais(ciudad.getPais());
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
