package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.ContinenteDao;
import com.redexbackend.models.Continente;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class ContinenteDaoImp implements ContinenteDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Continente> getAll() {
    List<Continente> list = null;
    try{
      var hql = "from Continente as c";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Continente get(int id) {
    Continente continente = null;
    try {
      continente = entityManager.find(Continente.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return continente;
  }

  @Override
  public Continente insert(Continente continente) {
    Continente result = null;
    try {
      result = entityManager.merge(continente);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Continente update(Continente continente) {
    Continente result = null;
    try {
      var nuevo = this.get(continente.getId());
      if(continente.getCodigo() != null) nuevo.setCodigo(continente.getCodigo());
      if(continente.getNombre() != null) nuevo.setNombre(continente.getNombre());
      if(continente.getPoliticaLocal() != null) nuevo.setPoliticaLocal(continente.getPoliticaLocal());
      if(continente.getPoliticaIntercontinental() != null) nuevo.setPoliticaIntercontinental(continente.getPoliticaIntercontinental());
      if(continente.getPaises() != null) nuevo.setPaises(continente.getPaises());
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
