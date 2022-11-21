package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Configuracion;

public interface ConfiguracionDao {
  public List<Configuracion> getAll();
  public Configuracion get(int id);
  public Configuracion insert(Configuracion configuracion);
  public Configuracion update(Configuracion configuracion);
  public boolean delete(int id);
}
