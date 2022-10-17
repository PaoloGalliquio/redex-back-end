package com.redexbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redexbackend.models.Aeropuerto;
import com.redexbackend.service.AeropuertoService;

@RestController
@RequestMapping("/aeropuerto")
@CrossOrigin
public class AeropuertoController {
  @Autowired
  private AeropuertoService aeropuertoService;
  
  @GetMapping(value = "/list")
  List<Aeropuerto> getAll(){
    return aeropuertoService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Aeropuerto get(@PathVariable int id){
    return aeropuertoService.get(id);
  }

  @PostMapping(value = "/insert")
  Aeropuerto insert(@RequestBody Aeropuerto aeropuerto){
    return aeropuertoService.insert(aeropuerto);
  }

  @PutMapping(value = "/update")
  Aeropuerto update(@RequestBody Aeropuerto aeropuerto){
    return aeropuertoService.update(aeropuerto);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return aeropuertoService.delete(id);
  }
}
