package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.PlanDeVuelo;

public interface PlanDeVueloDao {
  public List<PlanDeVuelo> getAll();
  public PlanDeVuelo get(int id);
  public PlanDeVuelo insert(PlanDeVuelo planDeVuelo);
  public PlanDeVuelo update(PlanDeVuelo planDeVuelo);
  public boolean delete(int id);
}
