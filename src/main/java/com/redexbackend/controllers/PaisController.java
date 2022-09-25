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

import com.redexbackend.models.Pais;
import com.redexbackend.service.PaisService;

@RestController
@RequestMapping("/pais")
@CrossOrigin
public class PaisController {
  @Autowired
  private PaisService paisService;

  @GetMapping(value = "/list")
  List<Pais> getAll(){
    return paisService.getAll();
  }

  @GetMapping(value = "/get/{id}")
  Pais get(@PathVariable int id){
    return paisService.get(id);
  }

  @PostMapping(value = "/insert")
  Pais insert(@RequestBody Pais pais){
    return paisService.insert(pais);
  }

  @PutMapping(value = "/update")
  Pais update(@RequestBody Pais pais){
    return paisService.update(pais);
  }

  @DeleteMapping(value = "/delete/{id}")
  boolean delete(@PathVariable int id){
    return paisService.delete(id);
  }
}
