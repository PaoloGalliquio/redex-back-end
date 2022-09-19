package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

public class Aeropuerto {
  private int id;
  private String codigo;
  private String nombre;
  private int capacidad;
  private int cantidadReservada;
  private double latitud;
  private double longitud;
  private Ciudad ciudad;
  private List<Vuelo> vuelos;
  private int estado;

  public Aeropuerto(int id, String codigo, String nombre, int capacidad, int cantidadReservada, double latitud, double longitud, Ciudad ciudad, List<Vuelo> vuelos, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = latitud;
    this.longitud = longitud;
    this.ciudad = ciudad;
    this.vuelos = vuelos;
    this.estado = estado;
  }

  public Aeropuerto(int id, String codigo, String nombre, int capacidad, int cantidadReservada, double latitud, double longitud, Ciudad ciudad, int estado) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = latitud;
    this.longitud = longitud;
    this.ciudad = ciudad;
    this.vuelos = new ArrayList<Vuelo>();
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

  public int getCapacidad() {
    return this.capacidad;
  }

  public void setCapacidad(int capacidad) {
    this.capacidad = capacidad;
  }

  public int getCantidadReservada() {
    return this.cantidadReservada;
  }

  public void setCantidadReservada(int cantidadReservada) {
    this.cantidadReservada = cantidadReservada;
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

  public Ciudad getCiudad() {
    return this.ciudad;
  }

  public void setCiudad(Ciudad ciudad) {
    this.ciudad = ciudad;
  }

  public List<Vuelo> getVuelos() {
    return this.vuelos;
  }

  public void setVuelos(List<Vuelo> vuelos) {
    this.vuelos = vuelos;
  }

  public int getEstado() {
    return this.estado;
  }

  public void setEstado(int estado) {
    this.estado = estado;
  }

}

