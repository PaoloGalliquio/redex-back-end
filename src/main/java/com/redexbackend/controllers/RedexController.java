package com.redexbackend.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.redexbackend.models.Configuracion;
import com.redexbackend.models.Continente;
import com.redexbackend.models.Envio;
import com.redexbackend.models.LeerArchivos;
import com.redexbackend.models.Pais;
import com.redexbackend.models.Vuelo;
import com.redexbackend.service.AeropuertoService;
import com.redexbackend.service.CiudadService;
import com.redexbackend.service.ConfiguracionService;
import com.redexbackend.service.ContinenteService;
import com.redexbackend.service.EnvioService;
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
  @Autowired
  private EnvioService envioService = new EnvioService();
  @Autowired
  private ConfiguracionService configuracionService = new ConfiguracionService();

  LeerArchivos lector = new LeerArchivos();

  Calendar inicioSimulacion = Calendar.getInstance();

  HashMap<String, Configuracion> configuraciones = new HashMap<>();
  List<Continente> continentesList;
  List<Pais> paisesList;
  List<Ciudad> ciudadesList;
  HashMap<String, Aeropuerto> aeropuertos = new HashMap<>();
  List<Aeropuerto> aeropuertosList;
  List<Vuelo> vuelosList = new ArrayList<>();

  @GetMapping(value = "/test")
  String test() {
    return "Test";
  }

  @GetMapping(value = "/init")
  List<Aeropuerto> init(){
    //List<Configuracion> configuracionList = configuracionService.getAll();
    // for (Configuracion configuracion : configuracionList)
    //   configuraciones.put(configuracion.getNombre(), configuracion);

    continentesList = continenteService.getAll();
    paisesList = paisService.getAll();
    ciudadesList = ciudadService.getAll();

    aeropuertosList = aeropuertoService.getAll();
    for (Aeropuerto aeropuerto : aeropuertosList){
      //aeropuerto.setConfiguracion(configuraciones);
      aeropuertos.put(aeropuerto.getCodigo(), aeropuerto);

      List<Vuelo> listaDeVuelos = vueloService.getVuelos(aeropuerto.getId());
      // for (Vuelo vuelo : listaDeVuelos)
      //   vuelo.setConfiguracion(configuraciones);

      vuelosList.addAll(listaDeVuelos);

      aeropuerto.setVuelos(listaDeVuelos);
    }

    Collections.sort(vuelosList, new SortVuelos());

    return aeropuertosList;
  }

  @PostMapping(value = "/simulator/initial")
  Map<String, Object> simulador(@RequestParam(value = "file",required = true) MultipartFile archivo, @RequestParam(value = "fecha",required = true) Date fecha) {
    Envio lastEnvio = null;
    inicioSimulacion.setTime(fecha);
    Calendar siguienteBloque = Calendar.getInstance();
    siguienteBloque.setTime(fecha);
    siguienteBloque.add(Calendar.HOUR_OF_DAY, 6);
    //lector.leerEnviosTXT(aeropuertos, archivo, fecha, envioService);
    archivo = null;

    System.out.println("\nBloque analizado: " + inicioSimulacion.getTime().toString() + " - " + siguienteBloque.getTime().toString());

    List<Envio> enviosInDate = envioService.getInRange(inicioSimulacion.getTime(), siguienteBloque.getTime());

    for (Envio envio : enviosInDate) {
      envio.setAeropuertoPartida(aeropuertos.get(envio.getAeropuertoPartida().getCodigo()));
      envio.setAeropuertoDestino(aeropuertos.get(envio.getAeropuertoDestino().getCodigo()));
      Aeropuerto answer = AStar.aStar(envio);
      lastEnvio = AStar.obtenerPlanesDeVuelo(answer, envio, inicioSimulacion);
      if(lastEnvio != null) break;
    }

    List<Vuelo> vuelosInDate = vuelosList.stream()
      .filter(v -> (
        v.getFechaPartidaUTC0().after(inicioSimulacion.getTime()) && 
        v.getFechaPartidaUTC0().before(siguienteBloque.getTime())
      )).collect(Collectors.toList());

    System.out.println("Vuelos enviados: " + vuelosInDate.size());
      for (Vuelo vuelo : vuelosInDate)
        System.out.println("  " + vuelo.getCodigo() + ": " + vuelo.getFechaPartidaUTC0().toString());
    Map<String, Object> result = new HashMap<>();
    result.put("envios", enviosInDate);
    result.put("vuelos", vuelosInDate);
    result.put("ultimoEnvio", lastEnvio);

    return result;
  }

  @PostMapping(value = "/simulator/perBlock")
  Map<String, Object> simulatorPerDay(@RequestParam(value = "block",required = true) int block){
    Calendar bloqueActual = Calendar.getInstance(), siguienteBloque = Calendar.getInstance();
    
    bloqueActual.setTime(inicioSimulacion.getTime());
    bloqueActual.add(Calendar.HOUR, 6*block);

    siguienteBloque.setTime(bloqueActual.getTime());
    siguienteBloque.add(Calendar.HOUR, 6);

    System.out.println("\nBloque analizado: " + bloqueActual.getTime().toString() + " - " + siguienteBloque.getTime().toString());

    List<Envio> enviosInDate = envioService.getInRange(bloqueActual.getTime(), siguienteBloque.getTime());

    for (Envio envio : enviosInDate) {
      envio.setAeropuertoPartida(aeropuertos.get(envio.getAeropuertoPartida().getCodigo()));
      envio.setAeropuertoDestino(aeropuertos.get(envio.getAeropuertoDestino().getCodigo()));
      Aeropuerto answer = AStar.aStar(envio);
      AStar.obtenerPlanesDeVuelo(answer, envio, bloqueActual);
    }

    List<Vuelo> vuelosInDate = vuelosList.stream()
      .filter(v -> (
        v.getFechaPartidaUTC0().after(bloqueActual.getTime()) && 
        v.getFechaPartidaUTC0().before(siguienteBloque.getTime())
      )).collect(Collectors.toList());

    System.out.println("Vuelos enviados: " + vuelosInDate.size());
    for (Vuelo vuelo : vuelosInDate)
      System.out.println("  " + vuelo.getCodigo() + ": " + vuelo.getFechaPartidaUTC0().toString());
    Map<String, Object> result = new HashMap<>();
    result.put("envios", enviosInDate);
    result.put("vuelos", vuelosInDate);

    return result;
  }

  @GetMapping(value = "/configuraciones/list")
  Map<String, Configuracion> listConfiguraciones() {
    return configuraciones;
  }

  @PostMapping(value = "/configuraciones/update")
  Map<String, Configuracion> updateConfiguraciones() {
    configuracionService.update(null);
    return configuraciones;
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

    configuraciones = lector.leerConfiguracion();
    try {
      for (HashMap.Entry<String, Configuracion> configuracion : configuraciones.entrySet())
        configuracion.getValue().setId(configuracionService.insert(configuracion.getValue()).getId());
    } catch (Exception ex){
      return ex.getMessage();
    }

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

    aeropuertos = lector.leerAeropuertos(ciudades, timeZones, configuraciones);
    try {
      for (HashMap.Entry<String, Aeropuerto> aeropuerto : aeropuertos.entrySet())
        aeropuerto.getValue().setId(aeropuertoService.insert(aeropuerto.getValue()).getId());
    } catch (Exception ex) {
      return ex.getMessage();
    }

    lector.leerVuelosTXT(aeropuertos, vuelosList, configuraciones);
    lector.escribirSQL(vuelosList);

    return "Data inicializada";
  }
}
