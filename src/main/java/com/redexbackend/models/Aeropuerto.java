package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Aeropuerto")
@SQLDelete(sql = "update aeropuerto set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Aeropuerto extends BaseEntity {
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

  public Aeropuerto(String id, String codigo, String nombre, int capacidad, int cantidadReservada, String latitud, String longitud, Ciudad ciudad, int estado) {
    this.id = Integer.parseInt(id);
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.ciudad = ciudad;
    this.vuelos = new ArrayList<Vuelo>();
    this.estado = estado;
  }

  public void addVuelo(Vuelo vuelo){
    if(vuelos == null)
      vuelos = new ArrayList<Vuelo>();
    
    vuelos.add(vuelo);
  }
}

