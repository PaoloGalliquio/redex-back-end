package com.redexbackend.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import com.redexbackend.util.SortEnvios;

public class LeerArchivos {
  public LeerArchivos(){}

  public LeerArchivos(HashMap<String, Integer> timeZones, HashMap<String, Continente> continentes,
      HashMap<String, Pais> paises, HashMap<String, Ciudad> ciudades, HashMap<String, Aeropuerto> aeropuertos,
      HashMap<String, Vuelo> vuelos) {
    leerTimeZonesTXT(timeZones);
    leerContinentesTXT(continentes);
    leerAeropuertosTXT(continentes, paises, ciudades, aeropuertos, timeZones);
    leerVuelosTXT(aeropuertos, vuelos);
  }

  public void leerTimeZonesTXT(HashMap<String, Integer> timeZones){
    String[] informacion;
    String line;
    File timezonesFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\timezones.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(timezonesFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        timeZones.put(informacion[0], Integer.parseInt(informacion[1]));
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public void leerContinentesTXT(HashMap<String, Continente> continentes) {
    String[] informacion;
    String line;
    File aeropuertosFile = new File(
        System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\continentes.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        Continente continente = new Continente(informacion[0], informacion[1], informacion[4], informacion[2],
            informacion[3], 1);
        continentes.put(informacion[4], continente);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }
  
  public void leerAeropuertosTXT(HashMap<String, Continente> continentes, HashMap<String, Pais> paises, HashMap<String, Ciudad> ciudades, HashMap<String, Aeropuerto> aeropuertos, HashMap<String, Integer> timeZones){
    String[] informacion;
    String line, codigoContinente;
    int capacidad;
    File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        Pais pais = new Pais(informacion[0], informacion[3], informacion[3], continentes.get(informacion[5]), 1);
        continentes.get(informacion[5]).addPais(pais);
        paises.put(informacion[3], pais);
        Ciudad ciudad = new Ciudad(informacion[0], informacion[2], informacion[2], informacion[6], informacion[7], pais, 1);
        ciudades.put(informacion[4], ciudad);
        codigoContinente = continentes.get(informacion[5]).getCodigo();
        if (codigoContinente == "AMN" || codigoContinente == "AMS" || codigoContinente == "AMC")
          capacidad = 850;
        else
          capacidad = 900;
        Aeropuerto aeropuerto = new Aeropuerto(informacion[0], informacion[1], informacion[1], capacidad, 1000, informacion[6], informacion[7], ciudad, timeZones.get(informacion[1]), 1);
        ciudad.setAeropuerto(aeropuerto);
        aeropuertos.put(informacion[1], aeropuerto);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public void leerVuelosTXT(HashMap<String, Aeropuerto> aeropuertos, HashMap<String, Vuelo> vuelos){
    String[] informacion;
    String line, key, yy, mm, dd;
    int tiempo, i = 0;
    Calendar horaSalida = Calendar.getInstance(), horaLlegada = Calendar.getInstance(), horaSalidaUTC0 = Calendar.getInstance(), horaLlegadaUTC0 = Calendar.getInstance();
    File vuelostxt = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\vuelos.txt");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    yy = "2022";
    mm = "10";
    dd = "24";
    try {
      BufferedReader br = new BufferedReader(new FileReader(vuelostxt));
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");

        horaSalida.setTime(dateFormat.parse(yy + "-" + mm + "-" + dd + " " + informacion[2] + ":00"));
        horaLlegada.setTime(dateFormat.parse(yy + "-" + mm + "-" + dd + " " + informacion[3] + ":00"));

        horaSalidaUTC0.setTime(horaSalida.getTime());
        horaLlegadaUTC0.setTime(horaLlegada.getTime());

        horaSalidaUTC0.add(Calendar.HOUR, -aeropuertos.get(informacion[0]).getHusoHorario());
        horaLlegadaUTC0.add(Calendar.HOUR, -aeropuertos.get(informacion[1]).getHusoHorario());

        if (horaSalidaUTC0.getTime().before(horaLlegadaUTC0.getTime())){
          horaLlegada.add(Calendar.DAY_OF_MONTH, 1);
          horaLlegadaUTC0.add(Calendar.DAY_OF_MONTH, 1);
        }

        while(true){
          key = informacion[0] + informacion[1] + Integer.toString(i);
          if(vuelos.containsKey(key) == false){
            i = 0;
            break;
          }else i++;
        }
        
        int capacidad = obtenerCapacidad(aeropuertos, informacion[0], informacion[1]);
        Vuelo vuelo = new Vuelo(key, aeropuertos.get(informacion[0]), aeropuertos.get(informacion[1]), horaSalida.getTime(), horaLlegada.getTime(), horaSalidaUTC0.getTime(), horaLlegadaUTC0.getTime(), capacidad, 1, true);
        
        tiempo = tiempoEntreFechas(vuelo.getFechaPartidaUTC0(), vuelo.getFechaDestinoUTC0());
        vuelo.setDuracion(tiempo);
        vuelos.put(key, vuelo);
        aeropuertos.get(informacion[0]).addVuelo(vuelo);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public void leerEnviosTXT(HashMap<String, Aeropuerto> aeropuertos, HashMap<String, Envio> envios, List<Envio> enviosList, MultipartFile file){
    var convertedFile = toFile(file);
    String[] informacion, destinoNumPaquetes;
    String line;
    try{
      BufferedReader br = new BufferedReader(new FileReader(convertedFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");
        destinoNumPaquetes = informacion[3].split(":");
        Envio envio = new Envio(informacion[0], informacion[1], informacion[2], aeropuertos.get(informacion[0].substring(0, 4)), aeropuertos.get(destinoNumPaquetes[0]), destinoNumPaquetes[1]);
        envios.put(informacion[0], envio);
        enviosList.add(envio);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public void leerEnviosTXT(HashMap<String, Aeropuerto> aeropuertos, HashMap<String, Envio> envios, List<Envio> enviosList, MultipartFile file, Date fechaInicio){
    var convertedFile = toFile(file);
    String[] informacion, destinoNumPaquetes;
    String line, yy, mm, dd;
    Calendar fechaLimite = Calendar.getInstance();
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try{
      BufferedReader br = new BufferedReader(new FileReader(convertedFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");
        destinoNumPaquetes = informacion[3].split(":");
        yy = informacion[1].substring(0,4);
        mm = informacion[1].substring(4,6);
        dd = informacion[1].substring(6,8);
        var fechaEnvio = formato.parse(yy + "-" + mm + "-" + dd + " " + informacion[2] + ":00");
        if(enRangoSimulacro(fechaEnvio, fechaInicio)){
          Envio envio = new Envio();
          envio.setCodigo(informacion[0]);
          envio.setFechaEnvio(fechaEnvio);
          envio.setAeropuertoPartida(aeropuertos.get(informacion[0].substring(0, 4)));
          envio.setAeropuertoDestino(aeropuertos.get(destinoNumPaquetes[0]));
          envio.setNumeroPaquetes(Integer.parseInt(destinoNumPaquetes[1]));
          fechaLimite.setTime(fechaEnvio);
          if(esIntercontinental(envio))
          fechaLimite.add(Calendar.DATE, 1);
          else
          fechaLimite.add(Calendar.DATE, 2);
          envio.setFechaLimite(fechaLimite.getTime());
          envios.put(informacion[0], envio);
          enviosList.add(envio);
        }
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    Collections.sort(enviosList, new SortEnvios());
  }

  private File toFile(MultipartFile multipartFile){
    var file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
    try(var fout = new FileOutputStream(file)){
      fout.write(multipartFile.getBytes());
    }
    catch (Exception exception){
      System.out.println("Se ha producido un error: " + exception.getMessage());
    }
    return file;
  }

  private boolean esIntercontinental(Envio envio){
    if(envio.getAeropuertoPartida().getCiudad().getPais().getContinente().getCodigo().charAt(0) == 
       envio.getAeropuertoDestino().getCiudad().getPais().getContinente().getCodigo().charAt(0))
      return true;
    return false;
  }

  private boolean enRangoSimulacro(Date fechaEnvio, Date fechaInicio){
    Calendar fechaFinCal = Calendar.getInstance();
    fechaFinCal.setTime(fechaInicio);
    fechaFinCal.add(Calendar.DATE, 5);
    Date fechaFin = fechaFinCal.getTime();
    if(fechaEnvio.compareTo(fechaInicio) >= 0 && fechaEnvio.compareTo(fechaFin) <= 0)
      return true;
    return false;
  }

  public HashMap<String, Continente> leerContinentes() {
    HashMap<String, Continente> continentes = new HashMap<>();
    String[] informacion;
    String line;
    File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\continentes.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        Continente continente = new Continente(informacion[0], informacion[1], informacion[4], informacion[2],
            informacion[3], 1);
        continentes.put(informacion[4], continente);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return continentes;
  }

  public HashMap<String, Pais> leerPaises(HashMap<String, Continente> continentes) {
    HashMap<String, Pais> paises = new HashMap<>();
    String[] informacion;
    String line;
    File aeropuertosFile = new File(
        System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        Pais pais = new Pais(informacion[0], informacion[3], informacion[3], continentes.get(informacion[5]), 1);
        continentes.get(informacion[5]).addPais(pais);
        paises.put(informacion[3], pais);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return paises;
  }

  public HashMap<String, Ciudad> leerCiudades(HashMap<String, Pais> paises) {
    HashMap<String, Ciudad> ciudades = new HashMap<>();
    String[] informacion;
    String line;
    File aeropuertosFile = new File(
        System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        Ciudad ciudad = new Ciudad(informacion[0], informacion[2], informacion[2], informacion[6], informacion[7],
            paises.get(informacion[3]), 1);
        ciudades.put(informacion[4], ciudad);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return ciudades;
  }

  public HashMap<String, Aeropuerto> leerAeropuertos(HashMap<String, Ciudad> ciudades, HashMap<String, Integer> timeZones) {
    HashMap<String, Aeropuerto> aeropuertos = new HashMap<>();
    String[] informacion;
    String line, codigoContinente;
    int capacidad;
    File aeropuertosFile = new File(
        System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        codigoContinente = ciudades.get(informacion[4]).getPais().getContinente().getCodigo();
        if (codigoContinente == "AMN" || codigoContinente == "AMS" || codigoContinente == "AMC")
          capacidad = 850;
        else
          capacidad = 900;
        Aeropuerto aeropuerto = new Aeropuerto(informacion[0], informacion[1], informacion[1], capacidad, 1000,
            informacion[6], informacion[7], ciudades.get(informacion[4]), timeZones.get(informacion[1]), 1);
        ciudades.get(informacion[4]).setAeropuerto(aeropuerto);
        aeropuertos.put(informacion[1], aeropuerto);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return aeropuertos;
  }

  public HashMap<String, Integer> leerTimeZones() {
    HashMap<String, Integer> timezones = new HashMap<>();
    String[] informacion;
    String line;
    File timezonesFile = new File(
        System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\timezones.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(timezonesFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(";");
        timezones.put(informacion[0], Integer.parseInt(informacion[1]));
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return timezones;
  }

  public HashMap<String, Envio> leerEnvios(HashMap<String, Node> aeropuertos, String aeropuerto) {
    HashMap<String, Envio> envios = new HashMap<>();
    String[] informacion, destinoNumPaquetes;
    String line;
    int count = 0;
    File enviosFile = new File(System.getProperty("user.dir")
        + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\envios_historicos.v01\\pack_enviado_" + aeropuerto
        + ".txt");

    try {
      BufferedReader br = new BufferedReader(new FileReader(enviosFile));
      while ((line = br.readLine()) != null) {
        if(count > 15)break;
        informacion = line.split("-");
        destinoNumPaquetes = informacion[3].split(":");

        Envio envio = new Envio(informacion[0], informacion[1], informacion[2],
            aeropuertos.get(aeropuerto).getAeropuerto(), aeropuertos.get(destinoNumPaquetes[0]).getAeropuerto(),
            destinoNumPaquetes[1]);

        envios.put(envio.getCodigo(), envio);
        count++;
      }

      br.close();

    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return envios;
  }

  public Date obtenerFecha(String hora) {
    String[] hm = hora.split(":", 2);
    Date horaTransformada = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Integer.parseInt(hm[0]), Integer.parseInt(hm[1])).getTime();
    return horaTransformada;
  }

  public int obtenerCapacidad(HashMap<String, Aeropuerto> aeropuertos, String origen, String destino) {
    String continenteOrigen = aeropuertos.get(origen).getCiudad().getPais().getContinente().getCodigo();
    String continenteDestino = aeropuertos.get(destino).getCiudad().getPais().getContinente().getCodigo();

    if (continenteOrigen == continenteDestino) {
      if (continenteOrigen == "EUR")
        return 250;
      else
        return 300;
    } else {
      return 350;
    }
  }

  public int tiempoEntreFechas(Date partida, Date destino) {
    long diff = ((destino.getTime() - partida.getTime()) / 60000);

    if (diff <= 0) diff += 1440;

    return (int)diff;
  }

  public void escribirSQL(HashMap<String, Vuelo> vuelos){
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try{
      File file = new File("vuelos.sql");
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write("use `redex-db`;");
      bw.newLine();
      bw.newLine();
      for (HashMap.Entry<String, Vuelo> vuelo : vuelos.entrySet()){
        bw.write(
          "INSERT INTO vuelo " + 
          "(estado,fecha_creacion,fecha_modificacion,capacidad,capacidad_actual,codigo,disponible,duracion,fecha_destino,fecha_partida,fecha_destinoutc0,fecha_partidautc0,id_aeropuerto_destino,id_aeropuerto_partida)"
        );
        bw.newLine();
        bw.write(
          "VALUES (1,'" + 
          dateFormat.format(vuelo.getValue().getFechaCreacion()) + "','" + 
          dateFormat.format(vuelo.getValue().getFechaCreacion()) + "'," + 
          vuelo.getValue().getCapacidad() + "," + 
          vuelo.getValue().getCapacidadActual() + ",'" + 
          vuelo.getValue().getCodigo() + "',1," + 
          vuelo.getValue().getDuracion() + ",'" + 
          dateFormat.format(vuelo.getValue().getFechaDestino()) + "','" + 
          dateFormat.format(vuelo.getValue().getFechaPartida()) + "','" + 
          dateFormat.format(vuelo.getValue().getFechaDestinoUTC0()) + "','" + 
          dateFormat.format(vuelo.getValue().getFechaPartidaUTC0()) + "'," + 
          vuelo.getValue().getAeropuertoDestino().getId() + "," + 
          vuelo.getValue().getAeropuertoPartida().getId() + ");");
        bw.newLine();
        bw.newLine();
      }
      bw.close();
    } catch (Exception ex){
      System.out.println(ex.getMessage());
    }
  }

  public void crearArchivoEnvios(){
    String[] aeropuertos = new String[]{"BIKF","EBCI","EDDI","EETN","EFHK","EGLL","EHAM","EIDW","EKCH","ELLX","ENGM","EPMO","ESKN","EVRA","LATI","LBSF","LDZA","LEMD","LFPG","LGAV","LHBP","LIRA","LJLJ","LKPR","LMML","LOWW","LPPT","LSZB","LZIB","SABE","SBBR","SCEL","SEQM","SGAS","SKBO","SLLP","SPIM","SUAA","SVMI","UMMS"};
    String line;
    BufferedReader br;
    File archivoLeido, envios = new File("envios.txt");
    try{
      FileOutputStream fos = new FileOutputStream(envios);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      for (String aeropuerto : aeropuertos) {
        archivoLeido = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\envios_historicos.v01\\pack_enviado_" + aeropuerto + ".txt");
        br = new BufferedReader(new FileReader(archivoLeido));
        for(int i = 0; i < 200; i++){
          line = br.readLine();
          if(i % 2 == 0){
            bw.write(line);
            bw.newLine();
          }
        }
      }
      bw.close();
    } catch (Exception ex){
      System.out.println(ex.getMessage());
    }

  }
}
