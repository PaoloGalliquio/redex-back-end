package com.redexbackend.util;

import java.util.Comparator;

import com.redexbackend.models.Envio;

public class SortEnvios implements Comparator<Envio>{

  @Override
  public int compare(Envio o1, Envio o2) {
    int duration = (int) (o2.getFechaLimite().getTime() - o1.getFechaLimite().getTime()) / 60000;
    return duration;
  }
}
