package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Ciudad;

public interface CiudadDao {
  public List<Ciudad> getAll();
  public Ciudad get(int id);
  public Ciudad insert(Ciudad ciudad);
  public Ciudad update(Ciudad ciudad);
  public boolean delete(int id);
}
