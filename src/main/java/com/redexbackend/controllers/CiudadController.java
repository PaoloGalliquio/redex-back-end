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

import com.redexbackend.models.Ciudad;
import com.redexbackend.service.CiudadService;

@RestController
@RequestMapping("/ciudad")
@CrossOrigin
public class CiudadController {
  @Autowired
  private CiudadService ciudadService;

  @GetMapping(value = "/list")
  List<Ciudad> getAll(){
    return ciudadService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Ciudad get(@PathVariable int id){
    return ciudadService.get(id);
  }

  @PostMapping(value = "/insert")
  Ciudad insert(@RequestBody Ciudad ciudad){
    return ciudadService.insert(ciudad);
  }

  @PutMapping(value = "/update")
  Ciudad update(@RequestBody Ciudad ciudad){
    return ciudadService.update(ciudad);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return ciudadService.delete(id);
  }
}
