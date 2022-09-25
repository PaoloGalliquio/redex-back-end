package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.Aeropuerto;

public interface AeropuertoDao {
  public List<Aeropuerto> getAll();
  public Aeropuerto get(int id);
  public Aeropuerto insert(Aeropuerto aeropuerto);
  public Aeropuerto update(Aeropuerto aeropuerto);
  public boolean delete(int id);
}
