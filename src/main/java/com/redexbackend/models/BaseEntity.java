package com.redexbackend.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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

  @Column(name = "estado")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Boolean estado = true;
}
