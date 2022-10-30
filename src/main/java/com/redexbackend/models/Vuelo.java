package com.redexbackend.models;

import java.util.Date;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Vuelo")
@SQLDelete(sql = "update Vuelo set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Vuelo extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idAeropuertoPartida")
  private Aeropuerto aeropuertoPartida;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idAeropuertoDestino")
  private Aeropuerto aeropuertoDestino;

  @Column(name = "fechaPartida")
  private Date fechaPartida;

  @Column(name = "fechaPartidaUTC0")
  private Date fechaPartidaUTC0;

  @Column(name = "fechaDestino")
  private Date fechaDestino;

  @Column(name = "fechaDestinoUTC0")
  private Date fechaDestinoUTC0;

  @Column(name = "capacidad")
  private Integer capacidad;

  @Column(name = "capacidadActual")
  private Integer capacidadActual;

  @Column(name = "duracion")
  private Integer duracion;

  @Column(name = "disponible")
  private Boolean disponible; // 1: Para disponible, 0: Para no disponible

  public Vuelo(String codigo, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, Date fechaPartida,
      Date fechaDestino, Date fechaPartidaUTC0, Date fechaDestinoUTC0, int capacidad, int estado, boolean disponible) {
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaPartida = fechaPartida;
    this.fechaPartidaUTC0 = fechaPartidaUTC0;
    this.fechaDestino = fechaDestino;
    this.fechaDestinoUTC0 = fechaDestinoUTC0;
    this.capacidad = capacidad;
    this.capacidadActual = capacidad;
    this.disponible = disponible;
  }

  public Vuelo(String codigo, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, Date fechaPartida,
      Date fechaDestino, Date fechaPartidaUTC0, Date fechaDestinoUTC0, int capacidad, int duracion, int estado, boolean disponible) {
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaPartida = fechaPartida;
    this.fechaPartidaUTC0 = fechaPartidaUTC0;
    this.fechaDestino = fechaDestino;
    this.fechaDestinoUTC0 = fechaDestinoUTC0;
    this.capacidad = capacidad;
    this.duracion = duracion;
    this.capacidadActual = capacidad;
    this.disponible = disponible;
  }

  public Vuelo(Vuelo vuelo) {
    this.codigo = vuelo.codigo;
    this.aeropuertoPartida = vuelo.aeropuertoPartida;
    this.aeropuertoDestino = vuelo.aeropuertoDestino;
    this.fechaPartida = vuelo.fechaPartida;
    this.fechaDestino = vuelo.fechaDestino;
    this.capacidad = vuelo.capacidad;
    this.capacidadActual = capacidad;
    this.duracion = vuelo.duracion;
    this.disponible = vuelo.disponible;
  }
}
