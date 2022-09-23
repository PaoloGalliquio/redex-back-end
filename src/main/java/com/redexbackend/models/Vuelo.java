package com.redexbackend.models;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class Vuelo {
  private static final AtomicInteger count = new AtomicInteger(0); 
  private int id;
  private String codigo;
  private Aeropuerto aeropuertoPartido;
  private Aeropuerto aeropuertoDestino;
  private Date fechaPartida;
  private Date fechaDestino;
  private PlanDeVuelo planDeVuelo;
  private int capacidad;
  private int estado;

  public Vuelo(int id, String codigo, Aeropuerto aeropuertoPartido, Aeropuerto aeropuertoDestino, Date fechaPartida, Date fechaDestino, int capacidad, PlanDeVuelo planDeVuelo, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.aeropuertoPartido = aeropuertoPartido;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaPartida = fechaPartida;
    this.fechaDestino = fechaDestino;
    this.capacidad = capacidad;
    this.planDeVuelo = planDeVuelo;
    this.estado = estado;
  }

  public Vuelo(String codigo, Aeropuerto aeropuertoPartido, Aeropuerto aeropuertoDestino, Date fechaPartida, int capacidad, int estado) {
    id = count.incrementAndGet();
    this.codigo = codigo;
    this.aeropuertoPartido = aeropuertoPartido;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaPartida = fechaPartida;
    this.capacidad = capacidad;
    this.estado = estado;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCodigo() {
    return this.codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public Aeropuerto getAeropuertoPartido() {
    return this.aeropuertoPartido;
  }

  public void setAeropuertoPartido(Aeropuerto aeropuertoPartido) {
    this.aeropuertoPartido = aeropuertoPartido;
  }

  public Aeropuerto getAeropuertoDestino() {
    return this.aeropuertoDestino;
  }

  public void setAeropuertoDestino(Aeropuerto aeropuertoDestino) {
    this.aeropuertoDestino = aeropuertoDestino;
  }

  public Date getFechaPartida() {
    return this.fechaPartida;
  }

  public void setFechaPartida(Date fechaPartida) {
    this.fechaPartida = fechaPartida;
  }

  public Date getFechaDestino() {
    return this.fechaDestino;
  }

  public void setFechaDestino(Date fechaDestino) {
    this.fechaDestino = fechaDestino;
  }

  public PlanDeVuelo getPlanDeVuelo() {
    return this.planDeVuelo;
  }

  public void setPlanDeVuelo(PlanDeVuelo planDeVuelo) {
    this.planDeVuelo = planDeVuelo;
  }

  public int getCapacidad() {
    return this.capacidad;
  }

  public void setCapacidad(int capacidad) {
    this.capacidad = capacidad;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }
  

}
