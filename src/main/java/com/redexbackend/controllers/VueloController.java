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

import com.redexbackend.models.Vuelo;
import com.redexbackend.service.VueloService;

@RestController
@RequestMapping("/vuelo")
@CrossOrigin
public class VueloController {
  @Autowired
  private VueloService vueloService;

  @GetMapping(value = "/list")
  List<Vuelo> getAll(){
    return vueloService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Vuelo get(@PathVariable int id){
    return vueloService.get(id);
  }

  @PostMapping(value = "/insert")
  Vuelo insert(@RequestBody Vuelo vuelo){
    return vueloService.insert(vuelo);
  }

  @PutMapping(value = "/update")
  Vuelo update(@RequestBody Vuelo vuelo){
    return vueloService.update(vuelo);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return vueloService.delete(id);
  }

  @GetMapping(value = "/getVuelos/{id}")
  List<Vuelo> getVuelos(@PathVariable int id){
    return vueloService.getVuelos(id);
  }
}
