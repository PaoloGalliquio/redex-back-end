package com.redexbackend.models;

import java.util.Date;
import java.text.SimpleDateFormat;
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
@Table(name = "Envio")
@SQLDelete(sql = "update Envio set estado = 0 where id = ?")
@Where(clause = "estado = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Envio extends BaseEntity {
  @Column(name = "codigo")
  private String codigo;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idAeropuertoPartida")
  private Aeropuerto aeropuertoPartida;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idAeropuertoDestino")
  private Aeropuerto aeropuertoDestino;
  
  @Column(name = "fechaEnvio")
  private Date fechaEnvio;
  
  @Column(name = "fechaFinalizado")
  private Date fechaFinalizado;
  
  @Column(name = "fechaLimite")
  private Date fechaLimite;
  
  @Column(name = "duracionTotal")
  private Integer duracionTotal;
  
  @Column(name = "numeroPaquetes")
  private Integer numeroPaquetes;
  
  @Column(name = "correoCliente")
  private String correoCliente;
  
  @Transient
  @JsonIgnore
  private List<PlanDeVuelo> planesDeVuelo;

  public Envio(int id, String codigo, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, Date fechaEnvio, Date fechaFinalizado, Date fechaLimite, int duracionTotal, int numeroPaquetes, String correoCliente, int estado) {
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.fechaEnvio = fechaEnvio;
    this.fechaFinalizado = fechaFinalizado;
    this.fechaLimite = fechaLimite;
    this.duracionTotal = duracionTotal;
    this.numeroPaquetes = numeroPaquetes;
    this.correoCliente = correoCliente;
    this.planesDeVuelo = new ArrayList<PlanDeVuelo>();
  }

  public Envio(String codigo, String fechaEnvio, String horaEnvio, Aeropuerto aeropuertoPartida, Aeropuerto aeropuertoDestino, String numeroPaquetes) {
    this.codigo = codigo;
    this.aeropuertoPartida = aeropuertoPartida;
    this.aeropuertoDestino = aeropuertoDestino;
    this.numeroPaquetes = Integer.parseInt(numeroPaquetes);
    String yy = fechaEnvio.substring(0,3);
    String mm = fechaEnvio.substring(4,5);
    String dd = fechaEnvio.substring(6,7);
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try{
      this.fechaEnvio = formato.parse(yy + "-" + mm + "-" + dd + " " + horaEnvio + ":00");
    }catch(Exception ex){
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }
}
