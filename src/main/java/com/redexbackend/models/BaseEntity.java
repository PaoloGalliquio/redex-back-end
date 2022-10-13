package com.redexbackend.models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter 
@Setter
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Integer id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "DATETIME", nullable = false)
  protected Date fechaCreacion = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "DATETIME", nullable = false)
  protected Date fechaModificacion = new Date();

  @Column(name = "estado")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Boolean estado = true;

  @PreUpdate
  private void onUpdate(){
    fechaCreacion = addHoursToJavaUtilDate(new Date(), -5);
  }

  @PrePersist
  private void onCreate(){
    fechaCreacion = fechaModificacion = addHoursToJavaUtilDate(new Date(), -5);
  }

  private Date addHoursToJavaUtilDate(Date date, int hours) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR_OF_DAY, hours);
    return calendar.getTime();
  }
}
