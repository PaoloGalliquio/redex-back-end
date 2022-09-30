package com.redexbackend.models;

import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Ciudad")
@SQLDelete(sql = "update Ciudad set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ciudad extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "latitud")
  private double latitud;

  @Column(name = "longitud")
  private double longitud;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "idAeropuerto")
  private Aeropuerto aeropuerto;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idPais")
  private Pais pais;

  public Ciudad(int id, String codigo, String nombre, String UTC, int husoHorario, double latitud, double longitud,
      Pais pais, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.latitud = latitud;
    this.longitud = longitud;
    this.pais = pais;
  }

  public Ciudad(String id, String codigo, String nombre, String latitud, String longitud, Pais pais, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.pais = pais;
  }

  public Ciudad(String id, String codigo, String nombre, String latitud, String longitud, Pais pais, int estado,
      int UTC) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.latitud = Double.parseDouble(latitud);
    this.longitud = Double.parseDouble(longitud);
    this.pais = pais;
  }
}
