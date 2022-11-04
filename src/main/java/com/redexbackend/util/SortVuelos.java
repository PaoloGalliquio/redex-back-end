package com.redexbackend.util;

import java.util.Comparator;

import com.redexbackend.models.Vuelo;

public class SortVuelos implements Comparator<Vuelo>{
  
  @Override
  public int compare(Vuelo o1, Vuelo o2) {    
    int duration = (int) (o1.getFechaPartidaUTC0().getTime() - o2.getFechaPartidaUTC0().getTime()) / 60000;
    return duration;
  }
}
