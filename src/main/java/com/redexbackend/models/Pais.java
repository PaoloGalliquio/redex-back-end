package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

public class Pais {
  private int id;
  private String codigo;
  private String nombre;
  private double latitud;
  private double longitud;
  private List<Ciudad> ciudades;
  private Continente continente;
  private int estado;

  public Pais(int id, String codigo, String nombre, double latitud, double longitud, List<Ciudad> ciudades, Continente continente, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.latitud = latitud;
    this.longitud = longitud;
    this.ciudades = ciudades;
    this.continente = continente;
    this.estado = estado;
  }

  public Pais(int id, String codigo, String nombre, double latitud, double longitud, Continente continente, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.latitud = latitud;
    this.longitud = longitud;
    this.ciudades = new ArrayList<Ciudad>();
    this.continente = continente;
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

  public List<Ciudad> getCiudades() {
    return this.ciudades;
  }

  public void setCiudades(List<Ciudad> ciudades) {
    this.ciudades = ciudades;
  }

  public Continente getContinente() {
    return this.continente;
  }

  public void setContinente(Continente continente) {
    this.continente = continente;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }
  
}
