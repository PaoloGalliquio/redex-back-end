package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

public class Pais {
  private int id;
  private String codigo;
  private String nombre;
  private List<Ciudad> ciudades;
  private Continente continente;
  private int estado;

  public Pais(int id, String codigo, String nombre, List<Ciudad> ciudades, Continente continente, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.ciudades = ciudades;
    this.continente = continente;
    this.estado = estado;
  }

  public Pais(int id, String codigo, String nombre, Continente continente, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
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
