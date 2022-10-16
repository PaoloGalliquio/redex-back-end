package com.redexbackend.models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PlanDeVuelo")
@SQLDelete(sql = "update PlanDeVuelo set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PlanDeVuelo extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @Column(name = "fechaPlan")
  private Date fechaPlan;
  
  @Column(name = "fechaCompletado")
  private Date fechaCompletado;
  
  @Column(name = "duracionTotal")
  private Integer duracionTotal;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idEnvio")
  private Envio envio;
  
  @Transient
  private List<Vuelo> vuelos;

  public PlanDeVuelo(int id, String codigo, Date fechaPlan, Date fechaCompletado, int duracionTotal, Envio envio, int estado) {
    this.codigo = codigo;
    this.fechaPlan = fechaPlan;
    this.fechaCompletado = fechaCompletado;
    this.duracionTotal = duracionTotal;
    this.vuelos = new ArrayList<Vuelo>();
    this.envio = envio;
  }
}
