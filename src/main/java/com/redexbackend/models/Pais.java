package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Pais")
@SQLDelete(sql = "update Pais set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Pais extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @Column(name = "nombre")
  private String nombre;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idContinente")
  private Continente continente;

  @Transient
  @JsonIgnore
  private List<Ciudad> ciudades;

  public Pais(int id, String codigo, String nombre, Continente continente, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.ciudades = new ArrayList<Ciudad>();
    this.continente = continente;
  }

  public Pais(String id, String codigo, String nombre, Continente continente, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.ciudades = new ArrayList<Ciudad>();
    this.continente = continente;
  }
}
