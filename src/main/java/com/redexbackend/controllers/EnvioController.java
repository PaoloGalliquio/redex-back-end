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

import com.redexbackend.models.Envio;
import com.redexbackend.service.EnvioService;

@RestController
@RequestMapping("/envio")
@CrossOrigin
public class EnvioController {
  @Autowired
  private EnvioService envioService;

  @GetMapping(value = "/list")
  List<Envio> getAll(){
    return envioService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Envio get(@PathVariable int id){
    return envioService.get(id);
  }

  @PostMapping(value = "/insert")
  Envio insert(@RequestBody Envio envio){
    return envioService.insert(envio);
  }

  @PutMapping(value = "/update")
  Envio update(@RequestBody Envio envio){
    return envioService.update(envio);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return envioService.delete(id);
  }
}
