package com.redexbackend.models;

public class Vuelo {
  private Aeropuerto origen;
  private Aeropuerto destino;
  private int horaSalida;
  private int minutoSalida;
  private int horaLlegada;
  private int minutoLlegada;


  public Vuelo(Aeropuerto origen, Aeropuerto destino, int horaSalida, int minutoSalida, int horaLlegada, int minutoLlegada) {
    this.origen = origen;
    this.destino = destino;
    this.horaSalida = horaSalida;
    this.minutoSalida = minutoSalida;
    this.horaLlegada = horaLlegada;
    this.minutoLlegada = minutoLlegada;
  }

  public Aeropuerto getOrigen() {
    return this.origen;
  }

  public void setOrigen(Aeropuerto origen) {
    this.origen = origen;
  }

  public Aeropuerto getDestino() {
    return this.destino;
  }

  public void setDestino(Aeropuerto destino) {
    this.destino = destino;
  }

  public int getHoraSalida() {
    return this.horaSalida;
  }

  public void setHoraSalida(int horaSalida) {
    this.horaSalida = horaSalida;
  }

  public int getMinutoSalida() {
    return this.minutoSalida;
  }

  public void setMinutoSalida(int minutoSalida) {
    this.minutoSalida = minutoSalida;
  }

  public int getHoraLlegada() {
    return this.horaLlegada;
  }

  public void setHoraLlegada(int horaLlegada) {
    this.horaLlegada = horaLlegada;
  }

  public int getMinutoLlegada() {
    return this.minutoLlegada;
  }

  public void setMinutoLlegada(int minutoLlegada) {
    this.minutoLlegada = minutoLlegada;
  }

}
