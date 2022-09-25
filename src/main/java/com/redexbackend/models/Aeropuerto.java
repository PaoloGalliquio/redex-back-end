package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Aeropuerto")
@SQLDelete(sql = "update Aeropuerto set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Aeropuerto extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @Column(name = "nombre")
  private String nombre;
  
  @Column(name = "capacidad")
  private int capacidad;
  
  @Column(name = "cantidadReservada")
  private int cantidadReservada;
  
  @Column(name = "latitud")
  private double latitud;
  
  @Column(name = "longitud")
  private double longitud;
  
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "idCiudad")
  private Ciudad ciudad;

  private List<Vuelo> vuelos;

  public Aeropuerto(int id, String codigo, String nombre, int capacidad, int cantidadReservada, double latitud, double longitud, Ciudad ciudad, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = latitud;
    this.longitud = longitud;
    this.ciudad = ciudad;
    this.vuelos = new ArrayList<Vuelo>();
  }

  public Aeropuerto(String id, String codigo, String nombre, int capacidad, int cantidadReservada, String latitud, String longitud, Ciudad ciudad, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.ciudad = ciudad;
    this.vuelos = new ArrayList<Vuelo>();
  }

  public void addVuelo(Vuelo vuelo){
    if(vuelos == null)
      vuelos = new ArrayList<Vuelo>();
    vuelos.add(vuelo);
  }
}

