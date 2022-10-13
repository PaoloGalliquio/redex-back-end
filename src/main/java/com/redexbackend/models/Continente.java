package com.redexbackend.models;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Continente")
@SQLDelete(sql = "update Continente set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Continente extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @Column(name = "nombre")
  private String nombre;
  
  @Column(name = "politicaLocal")
  private Double politicaLocal;
  
  @Column(name = "politicaIntercontinental")
  private Double politicaIntercontinental;
  
  private List<Pais> paises;

  public Continente(int id, String codigo, String nombre, double politicaLocal, double politicaIntercontinental, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.politicaLocal = politicaLocal;
    this.politicaIntercontinental = politicaIntercontinental;
    this.paises = new ArrayList<Pais>();
  }

  public Continente(String id, String codigo, String nombre, String politicaLocal, String politicaIntercontinental, int estado) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.politicaLocal = Double.parseDouble(politicaLocal);
    this.politicaIntercontinental = Double.parseDouble(politicaIntercontinental);
    this.paises = new ArrayList<Pais>();
  }

  public void addPais(Pais pais){
    if(paises == null)
      paises = new ArrayList<Pais>();
    this.paises.add(pais);
  }
}
