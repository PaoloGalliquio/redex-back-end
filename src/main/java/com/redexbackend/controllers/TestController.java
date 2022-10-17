package com.redexbackend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redexbackend.models.Ciudad;
import com.redexbackend.models.Continente;
import com.redexbackend.models.LeerArchivos;
import com.redexbackend.models.Node;
import com.redexbackend.models.Pais;
import com.redexbackend.models.Vuelo;
import com.redexbackend.service.AeropuertoService;
import com.redexbackend.service.CiudadService;
import com.redexbackend.service.ContinenteService;
import com.redexbackend.service.PaisService;
import com.redexbackend.service.VueloService;

@RestController
@RequestMapping("/test")
@CrossOrigin
public class TestController {
  @Autowired
  private ContinenteService continenteService = new ContinenteService();
  @Autowired
  private PaisService paisService = new PaisService();
  @Autowired
  private CiudadService ciudadService = new CiudadService();
  @Autowired
  private AeropuertoService aeropuertoService = new AeropuertoService();
  @Autowired
  private VueloService vueloService = new VueloService();

  @GetMapping(value = "/test")
  String test() {
    return "Test";
  }

  @GetMapping(value = "/load")
  String get() {
    HashMap<String, Integer> timeZones;
    HashMap<String, Continente> continentes;
    HashMap<String, Pais> paises;
    HashMap<String, Ciudad> ciudades;
    HashMap<String, Node> aeropuertos;
    HashMap<String, Vuelo> vuelos = new HashMap<>();
    LeerArchivos lector = new LeerArchivos();

    timeZones = lector.leerTimeZones();
    continentes = lector.leerContinentes();
    try {
      for (HashMap.Entry<String, Continente> continente : continentes.entrySet())
        continente.getValue().setId(continenteService.insert(continente.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    paises = lector.leerPaises(continentes);
    try {
      for (HashMap.Entry<String, Pais> pais : paises.entrySet())
        pais.getValue().setId(paisService.insert(pais.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    ciudades = lector.leerCiudades(paises);
    try {
      for (HashMap.Entry<String, Ciudad> ciudad : ciudades.entrySet())
        ciudad.getValue().setId(ciudadService.insert(ciudad.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    aeropuertos = lector.leerAeropuertos(ciudades, timeZones);
    try {
      for (HashMap.Entry<String, Node> aeropuerto : aeropuertos.entrySet())
        aeropuerto.getValue().getAeropuerto().setId(aeropuertoService.insert(aeropuerto.getValue().getAeropuerto()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    lector.leerVuelosTXT(aeropuertos, vuelos);
    lector.escribirSQL(vuelos);
    // for (HashMap.Entry<String, Vuelo> vuelo : vuelos.entrySet()){
    //   System.out.println(vuelo.getKey());
    // }
    // try {
    //   for (HashMap.Entry<String, Vuelo> vuelo : vuelos.entrySet())
    //     vuelo.getValue().setId(vueloService.insert(vuelo.getValue()).getId());
    // } catch (Exception ex) {
    //   return ex.getMessage();
    // }

    return "Data inicializada";
  }
}
