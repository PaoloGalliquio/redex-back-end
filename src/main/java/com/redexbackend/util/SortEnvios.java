package com.redexbackend.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import com.redexbackend.models.Envio;

public class SortEnvios implements Comparator<Envio>{

  @Override
  public int compare(Envio o1, Envio o2) {
    LocalDateTime d1 = o1.getFechaLimite().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    LocalDateTime d2 = o2.getFechaLimite().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    long diff = ChronoUnit.MINUTES.between(d2, d1);
    return (int)diff;
  }
}
