package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.ConfiguracionDao;
import com.redexbackend.models.Configuracion;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class ConfiguracionDaoImp implements ConfiguracionDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Configuracion> getAll() {
    List<Configuracion> list = null;
    try{
      var hql = "from Configuracion as c";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Configuracion get(int id) {
    Configuracion configuracion = null;
    try {
      configuracion = entityManager.find(Configuracion.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return configuracion;
  }

  @Override
  public Configuracion insert(Configuracion configuracion) {
    Configuracion result = null;
    try {
      result = entityManager.merge(configuracion);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Configuracion update(Configuracion configuracion) {
    Configuracion result = null;
    try {
      var nuevo = this.get(configuracion.getId());
      if(configuracion.getNombre() != null) nuevo.setNombre(configuracion.getNombre());
      if(configuracion.getValor() != null) nuevo.setValor(configuracion.getValor());
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
