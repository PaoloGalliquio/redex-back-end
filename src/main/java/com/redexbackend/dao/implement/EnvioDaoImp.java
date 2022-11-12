package com.redexbackend.dao.implement;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.redexbackend.dao.EnvioDao;
import com.redexbackend.models.Envio;

@Transactional
@Repository
@SuppressWarnings({"unchecked"})
public class EnvioDaoImp implements EnvioDao{

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Envio> getAll() {
    List<Envio> list = null;
    try{
      var hql = "from Envio as e";
      list = entityManager.createQuery(hql).getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }

  @Override
  public Envio get(int id) {
    Envio envio = null;
    try {
      envio = entityManager.find(Envio.class, id);
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
    }
    return envio;
  }

  @Override
  public Envio insert(Envio envio) {
    Envio result = null;
    try {
      result = entityManager.merge(envio);
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return result;
  }

  @Override
  public Envio update(Envio envio) {
    Envio result = null;
    try {
      var nuevo = this.get(envio.getId());
      if(envio.getCodigo() != null) nuevo.setCodigo(envio.getCodigo());
      if(envio.getAeropuertoPartida() != null) nuevo.setAeropuertoPartida(envio.getAeropuertoPartida());
      if(envio.getAeropuertoDestino() != null) nuevo.setAeropuertoDestino(envio.getAeropuertoDestino());
      if(envio.getFechaEnvio() != null) nuevo.setFechaEnvio(envio.getFechaEnvio());
      if(envio.getFechaEnvioUTC() != null) nuevo.setFechaEnvioUTC(envio.getFechaEnvioUTC());
      if(envio.getFechaFinalizado() != null) nuevo.setFechaFinalizado(envio.getFechaFinalizado());
      if(envio.getFechaLimite() != null) nuevo.setFechaLimite(envio.getFechaLimite());
      if(envio.getDuracionTotal() != null) nuevo.setDuracionTotal(envio.getDuracionTotal());
      if(envio.getNumeroPaquetes() != null) nuevo.setNumeroPaquetes(envio.getNumeroPaquetes());
      if(envio.getNumeroPaquetes() != null) nuevo.setNumeroPaquetes(envio.getNumeroPaquetes());
      if(envio.getCorreoCliente() != null) nuevo.setCorreoCliente(envio.getCorreoCliente());
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
  public List<Envio> getInRange(Date fechaInicio, Date fechaFinal) {
    List<Envio> list = null;
    try{
      var hql = "from Envio as e where (fechaEnvio between :start and :end)";
      list = entityManager.createQuery(hql)
        .setParameter("start", fechaInicio, TemporalType.DATE)
        .setParameter("end", fechaFinal, TemporalType.DATE)
        .getResultList();
    }
    catch (Exception exception){
      System.out.println(exception.getMessage());
    }
    return list;
  }
  
}
