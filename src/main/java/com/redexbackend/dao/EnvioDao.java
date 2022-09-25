package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Envio;

public interface EnvioDao {
  public List<Envio> getAll();
  public Envio get(int id);
  public Envio insert(Envio envio);
  public Envio update(Envio envio);
  public boolean delete(int id);
}
