package com.redexbackend.models;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Configuracion")
@SQLDelete(sql = "update Configuracion set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Configuracion extends BaseEntity {
  @Column(name = "nombre")
  private String nombre;

  @Column(name = "valor")
  private Integer valor;
}
