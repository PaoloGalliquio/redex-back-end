package com.redexbackend.models;

public class Ciudad {
  private int id;
  private String codigo;
  private String nombre;
  private String UTC;
  private int husoHorario;
  private double latitud;
  private double longitud;
  private Aeropuerto aeropuerto;
  private Pais pais;
  private int estado;

  public Ciudad(int id, String codigo, String nombre, String UTC, int husoHorario, double latitud, double longitud, Aeropuerto aeropuerto, Pais pais, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.UTC = UTC;
    this.husoHorario = husoHorario;
    this.latitud = latitud;
    this.longitud = longitud;
    this.aeropuerto = aeropuerto;
    this.pais = pais;
    this.estado = estado;
  }

  public Ciudad(int id, String codigo, String nombre, String UTC, int husoHorario, double latitud, double longitud, Pais pais, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.UTC = UTC;
    this.husoHorario = husoHorario;
    this.latitud = latitud;
    this.longitud = longitud;
    this.pais = pais;
    this.estado = estado;
  }

  public Ciudad(String id, String codigo, String nombre, String latitud, String longitud, Pais pais, int estado) {
    this.id = Integer.parseInt(id);
    this.codigo = codigo;
    this.nombre = nombre;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.pais = pais;
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

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getUTC() {
    return this.UTC;
  }

  public void setUTC(String UTC) {
    this.UTC = UTC;
  }

  public int getHusoHorario() {
    return this.husoHorario;
  }

  public void setHusoHorario(int husoHorario) {
    this.husoHorario = husoHorario;
  }

  public double getLatitud() {
    return this.latitud;
  }

  public void setLatitud(double latitud) {
    this.latitud = latitud;
  }

  public double getLongitud() {
    return this.longitud;
  }

  public void setLongitud(double longitud) {
    this.longitud = longitud;
  }

  public Aeropuerto getAeropuerto() {
    return this.aeropuerto;
  }

  public void setAeropuerto(Aeropuerto aeropuerto) {
    this.aeropuerto = aeropuerto;
  }

  public Pais getPais() {
    return this.pais;
  }

  public void setPais(Pais pais) {
    this.pais = pais;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }
  
}
