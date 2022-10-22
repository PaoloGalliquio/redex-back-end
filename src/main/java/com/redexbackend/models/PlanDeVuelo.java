package com.redexbackend.models;

import java.util.Date;
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
  
  @Column(name = "numeroPaquetes")
  private Integer numeroPaquetes;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idEnvio")
  @JsonIgnore
  private Envio envio;
  
  @Transient
  @JsonIgnore
  private List<Vuelo> vuelos;
}
