package com.redexbackend.dao;

import java.util.List;

import com.redexbackend.models.VueloPorPlanDeVuelo;

public interface VueloPorPlanDeVueloDao {
  public List<VueloPorPlanDeVuelo> getAll();
  public VueloPorPlanDeVuelo get(int id);
  public VueloPorPlanDeVuelo insert(VueloPorPlanDeVuelo pais);
  public VueloPorPlanDeVuelo update(VueloPorPlanDeVuelo pais);
  public boolean delete(int id);
}
