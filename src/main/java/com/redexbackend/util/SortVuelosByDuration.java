package com.redexbackend.util;

import java.util.Comparator;

import com.redexbackend.models.Vuelo;

public class SortVuelosByDuration implements Comparator<Vuelo> {
  @Override
  public int compare(Vuelo o1, Vuelo o2) { 
    return o2.getDuracion() - o1.getDuracion();
  }
}
