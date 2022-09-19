package com.redexbackend.models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class PlanDeVuelo {
  private int id;
  private String codigo;
  private Date fechaPlan;
  private Date fechaCompletado;
  private int duracionTotal;
  private List<Vuelo> vuelos;
  private Envio envio;
  private int estado;

  public PlanDeVuelo(int id, String codigo, Date fechaPlan, Date fechaCompletado, int duracionTotal, List<Vuelo> vuelos, Envio envio, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.fechaPlan = fechaPlan;
    this.fechaCompletado = fechaCompletado;
    this.duracionTotal = duracionTotal;
    this.vuelos = vuelos;
    this.envio = envio;
    this.estado = estado;
  }

  public PlanDeVuelo(int id, String codigo, Date fechaPlan, Date fechaCompletado, int duracionTotal, Envio envio, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.fechaPlan = fechaPlan;
    this.fechaCompletado = fechaCompletado;
    this.duracionTotal = duracionTotal;
    this.vuelos = new ArrayList<Vuelo>();
    this.envio = envio;
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

  public Date getFechaPlan() {
    return this.fechaPlan;
  }

  public void setFechaPlan(Date fechaPlan) {
    this.fechaPlan = fechaPlan;
  }

  public Date getFechaCompletado() {
    return this.fechaCompletado;
  }

  public void setFechaCompletado(Date fechaCompletado) {
    this.fechaCompletado = fechaCompletado;
  }

  public int getDuracionTotal() {
    return this.duracionTotal;
  }

  public void setDuracionTotal(int duracionTotal) {
    this.duracionTotal = duracionTotal;
  }

  public List<Vuelo> getVuelos() {
    return this.vuelos;
  }

  public void setVuelos(List<Vuelo> vuelos) {
    this.vuelos = vuelos;
  }

  public Envio getEnvio() {
    return this.envio;
  }

  public void setEnvio(Envio envio) {
    this.envio = envio;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }

}
