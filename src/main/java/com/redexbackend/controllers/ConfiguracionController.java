package com.redexbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redexbackend.models.Configuracion;
import com.redexbackend.service.ConfiguracionService;

@RestController
@RequestMapping("/configuracion")
@CrossOrigin
public class ConfiguracionController {
  @Autowired
  private ConfiguracionService configuracionService;
  
  @GetMapping(value = "/list")
  List<Configuracion> getAll(){
    return configuracionService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Configuracion get(@PathVariable int id){
    return configuracionService.get(id);
  }

  @PostMapping(value = "/insert")
  Configuracion insert(@RequestBody Configuracion configuracion){
    return configuracionService.insert(configuracion);
  }

  @PutMapping(value = "/update")
  Configuracion update(@RequestBody Configuracion configuracion){
    return configuracionService.update(configuracion);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return configuracionService.delete(id);
  }
}
