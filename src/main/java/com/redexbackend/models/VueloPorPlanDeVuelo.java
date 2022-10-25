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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "VueloPorPlanDeVuelo")
@SQLDelete(sql = "update VueloPorPlanDeVuelo set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class VueloPorPlanDeVuelo extends BaseEntity {
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idPlanDeVuelo")
  @JsonIgnore
  private PlanDeVuelo planDeVuelo;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idVuelo")
  private Vuelo vuelo;

  @Column(name = "fechaVuelo")
  private Date fechaVuelo;
}
