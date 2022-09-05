package com.redexbackend.models;

public class Aeropuerto {
  private int id;
  private String codigo;
  private String ciudad;
  private String pais;
  private String ciudadAbre;
  private String continente;

  public Aeropuerto(int id, String codigo, String ciudad, String pais, String ciudadAbre, String continente) {
    this.id = id;
    this.codigo = codigo;
    this.ciudad = ciudad;
    this.pais = pais;
    this.ciudadAbre = ciudadAbre;
    this.continente = continente;
  }
  public Aeropuerto(String id, String codigo, String ciudad, String pais, String ciudadAbre, String continente) {
    this.id = Integer.parseInt(id);
    this.codigo = codigo;
    this.ciudad = ciudad;
    this.pais = pais;
    this.ciudadAbre = ciudadAbre;
    this.continente = continente;
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
  
  public String getCiudad() {
    return this.ciudad;
  }
  
  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }
  
  public String getPais() {
    return this.pais;
  }
  
  public void setPais(String pais) {
    this.pais = pais;
  }
  
  public String getCiudadAbre() {
    return this.ciudadAbre;
  }
  
  public void setCiudadAbre(String ciudadAbre) {
    this.ciudadAbre = ciudadAbre;
  }

  public String getContinente() {
    return this.continente;
  }

  public void setContinente(String continente) {
    this.continente = continente;
  }
}

