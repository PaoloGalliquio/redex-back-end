package com.redexbackend.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.redexbackend.models.Aeropuerto;
import com.redexbackend.models.Ciudad;
import com.redexbackend.models.Continente;
import com.redexbackend.models.Envio;
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

  HashMap<String, Integer> timeZones = new HashMap<>();

  HashMap<String, Continente> continentes = new HashMap<>();
  List<Continente> continentesList;

  HashMap<String, Pais> paises = new HashMap<>();
  List<Pais> paisesList;
  
  HashMap<String, Ciudad> ciudades = new HashMap<>();
  List<Ciudad> ciudadesList;
  
  HashMap<String, Node> aeropuertos = new HashMap<>();
  List<Aeropuerto> aeropuertosList;
  
  HashMap<String, Vuelo> vuelos = new HashMap<>();
  List<Vuelo> vuelosList;
  
  HashMap<String, Envio> envios = new HashMap<>();
  List<Envio> enviosList = new ArrayList<>();

  @GetMapping(value = "/test")
  String test() {
    return "Test";
  }

  @GetMapping(value = "/init")
  String init(){
    Aeropuerto aeropuertoTemp;
    continentesList = continenteService.getAll();
    for (Continente continente : continentesList) 
      continentes.put(continente.getCodigo(), continente);

    paisesList = paisService.getAll();
    for (Pais pais : paisesList) 
      paises.put(pais.getCodigo(), pais);

    ciudadesList = ciudadService.getAll();
    for (Ciudad ciudad : ciudadesList) 
      ciudades.put(ciudad.getCodigo(), ciudad);

    aeropuertosList = aeropuertoService.getAll();
    for (Aeropuerto aeropuerto : aeropuertosList) 
      aeropuertos.put(aeropuerto.getCodigo(), new Node(aeropuerto));

    // for (HashMap.Entry<String, Node> aeropuerto : aeropuertos.entrySet()){
    //   aeropuertoTemp = aeropuerto.getValue().getAeropuerto();
    //   aeropuertoTemp.setVuelos(vueloService.getVuelos(aeropuertoTemp.getId()));
    // }

    return "Data inicializada";
  }

  @PostMapping(value = "/envio/sendFile")
  List<Envio> fileEnvios(@RequestParam(value = "file",required = false) MultipartFile archivo) {
    lector.leerEnviosTXT(aeropuertos, envios, enviosList, archivo);
    return enviosList;
  }

  @GetMapping(value = "/aeropuerto/list")
  List<Aeropuerto> listAeropuertos() {
    return aeropuertosList;
  }

  @GetMapping(value = "/continente/list")
  List<Continente> listContinentes() {
    return continentesList;
  }

  //Utilizar solo para llenar BD
  @GetMapping(value = "/load")
  String get() {
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
    // try {
    //   for (HashMap.Entry<String, Vuelo> vuelo : vuelos.entrySet())
    //     vuelo.getValue().setId(vueloService.insert(vuelo.getValue()).getId());
    // } catch (Exception ex) {
    //   return ex.getMessage();
    // }

    return "Data inicializada";
  }
}
