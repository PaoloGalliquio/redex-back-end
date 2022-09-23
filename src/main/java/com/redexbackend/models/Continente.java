package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

public class Continente {
  private int id;
  private String codigo;
  private String nombre;
  private double politicaLocal;
  private double politicaIntercontinental;
  private List<Pais> paises;
  private int estado;

  public Continente(int id, String codigo, String nombre, double politicaLocal, double politicaIntercontinental, List<Pais> paises, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.politicaLocal = politicaLocal;
    this.politicaIntercontinental = politicaIntercontinental;
    this.paises = paises;
    this.estado = estado;
  }

  public Continente(int id, String codigo, String nombre, double politicaLocal, double politicaIntercontinental, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.politicaLocal = politicaLocal;
    this.politicaIntercontinental = politicaIntercontinental;
    this.paises = new ArrayList<Pais>();
    this.estado = estado;
  }

  public Continente(String id, String codigo, String nombre, String politicaLocal, String politicaIntercontinental, int estado) {
    this.id = Integer.parseInt(id);
    this.codigo = codigo;
    this.nombre = nombre;
    this.politicaLocal = Double.parseDouble(politicaLocal);
    this.politicaIntercontinental = Double.parseDouble(politicaIntercontinental);
    this.paises = new ArrayList<Pais>();
    this.estado = estado;
  }

  public void addPais(Pais pais){
    if(paises == null)
      paises = new ArrayList<Pais>();
    this.paises.add(pais);
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

  public double getPoliticaLocal() {
    return this.politicaLocal;
  }

  public void setPoliticaLocal(double politicaLocal) {
    this.politicaLocal = politicaLocal;
  }

  public double getPoliticaIntercontinental() {
    return this.politicaIntercontinental;
  }

  public void setPoliticaIntercontinental(double politicaIntercontinental) {
    this.politicaIntercontinental = politicaIntercontinental;
  }

  public List<Pais> getPaises() {
    return this.paises;
  }

  public void setPaises(List<Pais> paises) {
    this.paises = paises;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }

}
