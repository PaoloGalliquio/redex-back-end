package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Pais;

public interface PaisDao {
  public List<Pais> getAll();
  public Pais get(int id);
  public Pais insert(Pais pais);
  public Pais update(Pais pais);
  public boolean delete(int id);
}
