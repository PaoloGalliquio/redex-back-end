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

import com.redexbackend.models.PlanDeVuelo;
import com.redexbackend.service.PlanDeVueloService;

@RestController
@RequestMapping("/planDeVuelo")
@CrossOrigin
public class PlanDeVueloController {
  @Autowired
  private PlanDeVueloService planDeVueloService;

  @GetMapping(value = "/list")
  List<PlanDeVuelo> getAll(){
    return planDeVueloService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  PlanDeVuelo get(@PathVariable int id){
    return planDeVueloService.get(id);
  }

  @PostMapping(value = "/insert")
  PlanDeVuelo insert(@RequestBody PlanDeVuelo planDeVuelo){
    return planDeVueloService.insert(planDeVuelo);
  }

  @PutMapping(value = "/update")
  PlanDeVuelo update(@RequestBody PlanDeVuelo planDeVuelo){
    return planDeVueloService.update(planDeVuelo);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return planDeVueloService.delete(id);
  }
}
