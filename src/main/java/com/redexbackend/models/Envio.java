package com.redexbackend.models;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Envio {
  private int id;
  private String codigo;
  private Aeropuerto aeropuertoPartida;
  private Aeropuerto aeropuertoDestino;
  private Date fechaEnvio;
  private Date fechaFinalizado;
  private Date fechaLimite;
  private double duracionTotal;
  private int numeroPaquetes;
  private String correoCliente;
  private List<PlanDeVuelo> planesDeVuelo;
  private int estado;

  public Envio(int id, String codigo, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, Date fechaEnvio, Date fechaFinalizado, Date fechaLimite, double duracionTotal, int numeroPaquetes, String correoCliente, List<PlanDeVuelo> planesDeVuelo, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaEnvio = fechaEnvio;
    this.fechaFinalizado = fechaFinalizado;
    this.fechaLimite = fechaLimite;
    this.duracionTotal = duracionTotal;
    this.numeroPaquetes = numeroPaquetes;
    this.correoCliente = correoCliente;
    this.planesDeVuelo = planesDeVuelo;
    this.estado = estado;
  }

  public Envio(int id, String codigo, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, Date fechaEnvio, Date fechaFinalizado, Date fechaLimite, double duracionTotal, int numeroPaquetes, String correoCliente, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaEnvio = fechaEnvio;
    this.fechaFinalizado = fechaFinalizado;
    this.fechaLimite = fechaLimite;
    this.duracionTotal = duracionTotal;
    this.numeroPaquetes = numeroPaquetes;
    this.correoCliente = correoCliente;
    this.planesDeVuelo = new ArrayList<PlanDeVuelo>();
    this.estado = estado;
  }

  public Envio(String codigo, String fechaEnvio, String horaEnvio, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, String numeroPaquetes) {
    this.id = Integer.parseInt(codigo.substring(4,13));
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.numeroPaquetes = Integer.parseInt(numeroPaquetes);
    String yy = fechaEnvio.substring(0,3);
    String mm = fechaEnvio.substring(4,5);
    String dd = fechaEnvio.substring(6,7);
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try{
      this.fechaEnvio = formato.parse(yy + "-" + mm + "-" + dd + " " + horaEnvio + ":00");
    }catch(Exception ex){
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
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

  public Aeropuerto getAeropuertoPartida() {
    return this.aeropuertoPartida;
  }

  public void setAeropuertoPartida(Aeropuerto aeropuertoPartida) {
    this.aeropuertoPartida = aeropuertoPartida;
  }

  public Aeropuerto getAeropuertoDestino() {
    return this.aeropuertoDestino;
  }

  public void setAeropuertoDestino(Aeropuerto aeropuertoDestino) {
    this.aeropuertoDestino = aeropuertoDestino;
  }

  public Date getFechaEnvio() {
    return this.fechaEnvio;
  }

  public void setFechaEnvio(Date fechaEnvio) {
    this.fechaEnvio = fechaEnvio;
  }

  public Date getFechaFinalizado() {
    return this.fechaFinalizado;
  }

  public void setFechaFinalizado(Date fechaFinalizado) {
    this.fechaFinalizado = fechaFinalizado;
  }

  public Date getFechaLimite() {
    return this.fechaLimite;
  }

  public void setFechaLimite(Date fechaLimite) {
    this.fechaLimite = fechaLimite;
  }

  public double getDuracionTotal() {
    return this.duracionTotal;
  }

  public void setDuracionTotal(double duracionTotal) {
    this.duracionTotal = duracionTotal;
  }

  public String getCorreoCliente() {
    return this.correoCliente;
  }

  public void setCorreoCliente(String correoCliente) {
    this.correoCliente = correoCliente;
  }

  public List<PlanDeVuelo> getPlanesDeVuelo() {
    return this.planesDeVuelo;
  }

  public void setPlanesDeVuelo(List<PlanDeVuelo> planesDeVuelo) {
    this.planesDeVuelo = planesDeVuelo;
  }

  public int getNumeroPaquetes() {
    return this.numeroPaquetes;
  }

  public void setNumeroPaquetes(int numeroPaquetes) {
    this.numeroPaquetes = numeroPaquetes;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }

}
