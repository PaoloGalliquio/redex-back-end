package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Aeropuerto")
@SQLDelete(sql = "update Aeropuerto set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Aeropuerto extends BaseEntity implements Comparable<Aeropuerto> {
  @Column(name = "codigo")
  private String codigo;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "capacidad")
  private Integer capacidad;

  @Column(name = "cantidadReservada")
  private Integer cantidadReservada;

  @Column(name = "latitud")
  private Double latitud;

  @Column(name = "longitud")
  private Double longitud;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "idCiudad")
  @JsonIgnore
  private Ciudad ciudad;

  @Column(name = "UTC")
  private String UTC;

  @Column(name = "husoHorario")
  private Integer husoHorario;

  @Transient
  @JsonIgnore
  private List<Vuelo> vuelos;

  // AStar

  // f = g + h
  @Transient
  @JsonIgnore
  public int f = Integer.MAX_VALUE;
  // tiempo recorrido de inicio a aeropuerto
  @Transient
  @JsonIgnore
  public int g = Integer.MAX_VALUE;

  // public int h = Integer.MAX_VALUE;
  // tiempo desde aeropuerto actual al destino
  @Transient
  @JsonIgnore
  public int h = 0;

  @Transient
  @JsonIgnore
  public Aeropuerto parent = null;
  @Transient
  @JsonIgnore
  public Vuelo comoLlegar = null;

  @Override
  public int compareTo(Aeropuerto n) {
    return Integer.compare(this.f, n.f);
  }

  public int calculateHeuristic(Aeropuerto start, Aeropuerto target) {

    // Propuesta de heurística:
    // Calcular el tiempo de un hipotético vuelo directo entre el punto actual
    // y el destino considerando una velocidad fija para no afectar
    // las comparaciones

    final int R = 6371; // radio de la Tierra
    double lat1 = start.getLatitud(),
        lon1 = start.getLongitud(),
        lat2 = target.getLatitud(),
        lon2 = target.getLongitud(), vprom = 550;

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000 / 1000; // distancia en km
    double tiempo = distance / vprom; // horas
    int horas = (int) tiempo;
    double minutos = 60 * (tiempo - horas);
    this.h = horas * 60 + (int) minutos;

    return this.h;
  }

  public Aeropuerto(int id, String codigo, String nombre, int capacidad, int cantidadReservada, double latitud,
      double longitud, Ciudad ciudad, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = latitud;
    this.longitud = longitud;
    this.ciudad = ciudad;
    this.vuelos = new ArrayList<Vuelo>();
  }

  public Aeropuerto(String id, String codigo, String nombre, int capacidad, int cantidadReservada, String latitud,
      String longitud, Ciudad ciudad, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.ciudad = ciudad;
    this.vuelos = new ArrayList<Vuelo>();
  }

  public Aeropuerto(String id, String codigo, String nombre, int capacidad, int cantidadReservada, String latitud,
      String longitud, Ciudad ciudad, int husoHorario, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.cantidadReservada = cantidadReservada;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.ciudad = ciudad;
    this.husoHorario = husoHorario;
    this.UTC = Integer.toString(husoHorario);
    this.vuelos = new ArrayList<Vuelo>();
  }

  public void addVuelo(Vuelo vuelo) {
    if (vuelos == null)
      vuelos = new ArrayList<Vuelo>();
    vuelos.add(vuelo);
  }

  public List<Vuelo> getVuelos() {
    return this.vuelos;
  }

}