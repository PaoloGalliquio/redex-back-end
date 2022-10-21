package com.redexbackend.dao.implement;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.VueloDao;
import com.redexbackend.models.Vuelo;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class VueloDaoImp implements VueloDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Vuelo> getAll() {
    List<Vuelo> list = null;
    try{
      var hql = "from Vuelo as v";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Vuelo get(int id) {
    Vuelo vuelo = null;
    try {
      vuelo = entityManager.find(Vuelo.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return vuelo;
  }

  @Override
  public Vuelo insert(Vuelo vuelo) {
    Vuelo result = null;
    try {
      result = entityManager.merge(vuelo);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Vuelo update(Vuelo vuelo) {
    Vuelo result = null;
    try {
      var nuevo = this.get(vuelo.getId());
      if(vuelo.getCodigo() != null) nuevo.setCodigo(vuelo.getCodigo());
      if(vuelo.getAeropuertoPartida() != null) nuevo.setCodigo(vuelo.getCodigo());
      if(vuelo.getAeropuertoDestino() != null) nuevo.setAeropuertoDestino(vuelo.getAeropuertoDestino());
      if(vuelo.getFechaPartida() != null) nuevo.setFechaPartida(vuelo.getFechaPartida());
      if(vuelo.getFechaDestino() != null) nuevo.setFechaDestino(vuelo.getFechaDestino());
      if(vuelo.getPlanDeVuelo() != null) nuevo.setPlanDeVuelo(vuelo.getPlanDeVuelo());
      if(vuelo.getCapacidad() != null) nuevo.setCapacidad(vuelo.getCapacidad());
      if(vuelo.getCapacidadActual() != null) nuevo.setCapacidadActual(vuelo.getCapacidadActual());
      if(vuelo.getDuracion() != null) nuevo.setDuracion(vuelo.getDuracion());
      if(vuelo.getDisponible() != null) nuevo.setDisponible(vuelo.getDisponible());
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

  @Override
  public List<Vuelo> getVuelos(int id) {
    List<Vuelo> list = null;
    try{
      var hql = "from Vuelo as v, Aeropuerto as a where a.id = " + id;
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }
  
}
