package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Vuelo;

public interface VueloDao {
  public List<Vuelo> getAll();
  public Vuelo get(int id);
  public Vuelo insert(Vuelo vuelo);
  public Vuelo update(Vuelo vuelo);
  public boolean delete(int id);
}
