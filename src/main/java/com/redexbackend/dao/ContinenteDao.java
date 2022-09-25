package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Continente;

public interface ContinenteDao {
  public List<Continente> getAll();
  public Continente get(int id);
  public Continente insert(Continente continente);
  public Continente update(Continente continente);
  public boolean delete(int id);
}
