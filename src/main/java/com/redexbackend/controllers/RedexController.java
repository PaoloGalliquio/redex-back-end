package com.redexbackend.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.redexbackend.models.AStar;
import com.redexbackend.models.Aeropuerto;
import com.redexbackend.models.Ciudad;
import com.redexbackend.models.Continente;
import com.redexbackend.models.Envio;
import com.redexbackend.models.LeerArchivos;
import com.redexbackend.models.Pais;
import com.redexbackend.models.Vuelo;
import com.redexbackend.service.AeropuertoService;
import com.redexbackend.service.CiudadService;
import com.redexbackend.service.ContinenteService;
import com.redexbackend.service.PaisService;
import com.redexbackend.service.VueloService;
import com.redexbackend.util.SortVuelos;

@RestController
@RequestMapping("/Redex")
@CrossOrigin
public class RedexController {
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

  LeerArchivos lector = new LeerArchivos();

  List<Continente> continentesList;
  List<Pais> paisesList;
  List<Ciudad> ciudadesList;
  HashMap<String, Aeropuerto> aeropuertos = new HashMap<>();
  List<Aeropuerto> aeropuertosList;
  List<Vuelo> vuelosList = new ArrayList<>();
  List<Envio> enviosList = new ArrayList<>();

  @GetMapping(value = "/test")
  String test() {
    return "Test";
  }

  @GetMapping(value = "/init")
  List<Aeropuerto> init(){
    continentesList = continenteService.getAll();
    paisesList = paisService.getAll();
    ciudadesList = ciudadService.getAll();

    aeropuertosList = aeropuertoService.getAll();
    for (Aeropuerto aeropuerto : aeropuertosList){
      aeropuertos.put(aeropuerto.getCodigo(), aeropuerto);

      List<Vuelo> listaDeVuelos = vueloService.getVuelos(aeropuerto.getId());
      vuelosList.addAll(listaDeVuelos);

      aeropuerto.setVuelos(listaDeVuelos);
    }

    Collections.sort(vuelosList, new SortVuelos());

    return aeropuertosList;
  }

  @PostMapping(value = "/envio/sendFile")
  List<Envio> fileEnvios(@RequestParam(value = "file",required = true) MultipartFile archivo, @RequestParam(value = "fecha",required = true) Date fecha) {
    lector.leerEnviosTXT(aeropuertos, enviosList, archivo, fecha);
    return enviosList;
  }

  @PostMapping(value = "/simulador")
  List<Envio> simulador(@RequestParam(value = "file",required = true) MultipartFile archivo, @RequestParam(value = "fecha",required = true) Date fecha) {
    lector.leerEnviosTXT(aeropuertos, enviosList, archivo, fecha);

    for (Envio envio : enviosList) {
      Aeropuerto answer = AStar.aStar(envio);
      AStar.obtenerPlanesDeVuelo(answer, envio);
    }
    
    return enviosList;
  }

  @GetMapping(value = "/aeropuerto/list")
  List<Aeropuerto> listAeropuertos() {
    return aeropuertosList;
  }

  @GetMapping(value = "/vuelo/list")
  List<Vuelo> listVuelos() {
    return vuelosList;
  }

  @PostMapping(value = "/aeropuerto/getVuelos")
  List<Vuelo> listVuelosDeAeropuerto(@RequestBody Aeropuerto aeropuerto) {
    return aeropuertos.get(aeropuerto.getCodigo()).getVuelos();
  }

  @GetMapping(value = "/continente/list")
  List<Continente> listContinentes() {
    return continentesList;
  }

  @GetMapping(value = "/fillDataBase")
  String fillDataBase() {
    HashMap<String, Integer> timeZones = lector.leerTimeZones();

    HashMap<String, Continente> continentes = lector.leerContinentes();
    try {
      for (HashMap.Entry<String, Continente> continente : continentes.entrySet())
        continente.getValue().setId(continenteService.insert(continente.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    HashMap<String, Pais> paises = lector.leerPaises(continentes);
    try {
      for (HashMap.Entry<String, Pais> pais : paises.entrySet())
        pais.getValue().setId(paisService.insert(pais.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    HashMap<String, Ciudad> ciudades = lector.leerCiudades(paises);
    try {
      for (HashMap.Entry<String, Ciudad> ciudad : ciudades.entrySet())
        ciudad.getValue().setId(ciudadService.insert(ciudad.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    aeropuertos = lector.leerAeropuertos(ciudades, timeZones);
    try {
      for (HashMap.Entry<String, Aeropuerto> aeropuerto : aeropuertos.entrySet())
        aeropuerto.getValue().setId(aeropuertoService.insert(aeropuerto.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    lector.leerVuelosTXT(aeropuertos, vuelosList);
    lector.escribirSQL(vuelosList);

    return "Data inicializada";
  }
}
