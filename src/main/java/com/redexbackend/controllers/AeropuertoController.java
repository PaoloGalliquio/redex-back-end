package com.redexbackend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AeropuertoController {
  
  @RequestMapping(value = "prueba")
  public List<String> prueba() {
    return List.of("Manzana", "Platano", "Poto");
  }
}
