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

import com.redexbackend.models.Continente;
import com.redexbackend.service.ContinenteService;

@RestController
@RequestMapping("/continente")
@CrossOrigin
public class ContinenteController {
  @Autowired
  private ContinenteService continenteService;

  @GetMapping(value = "/list")
  List<Continente> getAll(){
    return continenteService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Continente get(@PathVariable int id){
    return continenteService.get(id);
  }

  @PostMapping(value = "/insert")
  Continente insert(@RequestBody Continente continente){
    return continenteService.insert(continente);
  }

  @PutMapping(value = "/update")
  Continente update(@RequestBody Continente continente){
    return continenteService.update(continente);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return continenteService.delete(id);
  }
}
