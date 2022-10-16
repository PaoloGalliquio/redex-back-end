package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.PaisDao;
import com.redexbackend.models.Pais;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class PaisDaoImp implements PaisDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Pais> getAll() {
    List<Pais> list = null;
    try{
      var hql = "from Pais as p";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Pais get(int id) {
    Pais pais = null;
    try {
      pais = entityManager.find(Pais.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return pais;
  }

  @Override
  public Pais insert(Pais pais) {
    Pais result = null;
    try {
      result = entityManager.merge(pais);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Pais update(Pais pais) {
    Pais result = null;
    try {
      var nuevo = this.get(pais.getId());
      if(pais.getCodigo() != null) nuevo.setCodigo(pais.getCodigo());
      if(pais.getNombre() != null) nuevo.setNombre(pais.getNombre());
      if(pais.getContinente() != null) nuevo.setContinente(pais.getContinente());
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
