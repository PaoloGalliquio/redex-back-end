package com.redexbackend.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.web.multipart.MultipartFile;

import com.redexbackend.service.EnvioService;
import com.redexbackend.util.SortEnvios;
import com.redexbackend.util.SortVuelosByDuration;

public class LeerArchivos {
  public LeerArchivos(){}

  private boolean esIntercontinental(Aeropuerto salida, Aeropuerto destino){
    if(salida.getCiudad().getPais().getContinente().getCodigo() != 
      destino.getCiudad().getPais().getContinente().getCodigo())
      return true;
    return false;
  }

  private boolean esIntercontinental(Envio envio){
    if(envio.getAeropuertoPartida().getCiudad().getPais().getContinente().getCodigo() != 
       envio.getAeropuertoDestino().getCiudad().getPais().getContinente().getCodigo())
      return true;
    return false;
  }

  public int obtenerCapacidad(HashMap<String, Aeropuerto> aeropuertos, String origen, String destino, HashMap<String, Configuracion> configuracion) {
    String continenteOrigen = aeropuertos.get(origen).getCiudad().getPais().getContinente().getCodigo();
    String continenteDestino = aeropuertos.get(destino).getCiudad().getPais().getContinente().getCodigo();

    if (continenteOrigen == continenteDestino) {
      if (continenteOrigen == "EUR")
        return configuracion.get("CapacidadAvionEuropa").getValor();
      else
        return configuracion.get("CapacidadAvionAmerica").getValor();
    } else {
      return configuracion.get("CapacidadAvionInterc").getValor();
    }
  }

  public int obtenerDuracion(Calendar partida, Calendar destino, Aeropuerto aPartida, Aeropuerto aDestino) {
    long diff = TimeUnit.MINUTES.convert((destino.getTime().getTime() - partida.getTime().getTime()), TimeUnit.MILLISECONDS);

    if(diff <= 0){
      diff += 1440;
      destino.add(Calendar.HOUR, 24);
    }

    if(esIntercontinental(aPartida, aDestino)){
      while(diff > 1440){
        diff -= 1440;
        destino.add(Calendar.HOUR, -24);
      }
    }else{
      while(diff > 720){
        diff -= 720;
        destino.add(Calendar.HOUR, -24);
      }
    }

    return (int)diff;
  }

  public void leerVuelosTXT(HashMap<String, Aeropuerto> aeropuertos, List<Vuelo> vuelos, HashMap<String, Configuracion> configuracion){
    String[] informacion;
    String line;
    int duracion, capacidad;
    Aeropuerto salida, llegada;
    Calendar horaSalida = Calendar.getInstance(), 
      horaLlegada = Calendar.getInstance(), 
      horaSalidaUTC0 = Calendar.getInstance(), 
      horaLlegadaUTC0 = Calendar.getInstance(),
      diaActual = Calendar.getInstance(),
      diaSiguie = Calendar.getInstance();
    File vuelostxt = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\vuelos.txt");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      diaActual.setTime(dateFormat.parse("2022-10-24 00:00:00"));
      diaSiguie.setTime(dateFormat.parse("2022-10-25 00:00:00"));
      BufferedReader br = new BufferedReader(new FileReader(vuelostxt));
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");

        horaSalida.setTime(dateFormat.parse("2022-10-24" + " " + informacion[2] + ":00"));
        horaLlegada.setTime(dateFormat.parse("2022-10-24" + " " + informacion[3] + ":00"));

        horaSalidaUTC0.setTime(horaSalida.getTime());
        horaLlegadaUTC0.setTime(horaLlegada.getTime());

        horaSalidaUTC0.add(Calendar.HOUR, -aeropuertos.get(informacion[0]).getHusoHorario());
        horaLlegadaUTC0.add(Calendar.HOUR, -aeropuertos.get(informacion[1]).getHusoHorario());

        if(horaLlegadaUTC0.getTime().before(horaSalidaUTC0.getTime())){
          horaLlegadaUTC0.add(Calendar.HOUR, 24);
          horaLlegada.add(Calendar.HOUR, 24);
        }
        
        if(horaSalidaUTC0.getTime().before(diaActual.getTime())){
          horaSalidaUTC0.add(Calendar.HOUR, 24);
          horaLlegadaUTC0.add(Calendar.HOUR, 24);
          horaSalida.add(Calendar.HOUR, 24);
          horaLlegada.add(Calendar.HOUR, 24);
        }

        if(horaSalidaUTC0.getTime().after(diaSiguie.getTime())){
          horaSalidaUTC0.add(Calendar.HOUR, -24);
          horaLlegadaUTC0.add(Calendar.HOUR, -24);
          horaSalida.add(Calendar.HOUR, -24);
          horaLlegada.add(Calendar.HOUR, -24);
        }
        
        salida = aeropuertos.get(informacion[0]);
        llegada = aeropuertos.get(informacion[1]);
        
        capacidad = obtenerCapacidad(aeropuertos, informacion[0], informacion[1], configuracion);
        duracion = obtenerDuracion(horaSalidaUTC0, horaLlegadaUTC0, salida, llegada);

        Vuelo vuelo = new Vuelo(informacion[0] + informacion[1], salida, llegada, horaSalida.getTime(), horaLlegada.getTime(), horaSalidaUTC0.getTime(), horaLlegadaUTC0.getTime(), capacidad, duracion, 1, true);
        horaSalidaUTC0.add(Calendar.HOUR, 24);
        horaLlegadaUTC0.add(Calendar.HOUR, 24);
        horaSalida.add(Calendar.HOUR, 24);
        horaLlegada.add(Calendar.HOUR, 24);
        
        vuelo.setDuracion(duracion);

        vuelos.add(vuelo);

        aeropuertos.get(informacion[0]).addVuelo(vuelo);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public void leerEnviosTXT(HashMap<String, Aeropuerto> aeropuertos, List<List<Envio>> enviosList, MultipartFile file, Date fechaInicio){
    var convertedFile = toFile(file);
    String[] informacion, destinoNumPaquetes;
    String line, yy, mm, dd, ddtemp = "";
    Calendar fechaLimite = Calendar.getInstance(), fechaFin = Calendar.getInstance(), fechaEnvio = Calendar.getInstance();
    boolean first = true;
    fechaFin.setTime(fechaInicio);
    fechaFin.add(Calendar.DAY_OF_MONTH, 5);
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Aeropuerto aeropuertoSalida;
    List<Envio> envios = new ArrayList<>();
    try{
      BufferedReader br = new BufferedReader(new FileReader(convertedFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");
        destinoNumPaquetes = informacion[3].split(":");
        yy = informacion[1].substring(0,4);
        mm = informacion[1].substring(4,6);
        dd = informacion[1].substring(6,8);
        if(first){
          ddtemp = dd;
        }
        else{
          if(!dd.equals(ddtemp)){
            enviosList.add(envios);
            envios = new ArrayList<>();
          }
        }
        var fechaEnvioTemp = formato.parse(yy + "-" + mm + "-" + dd + " " + informacion[2] + ":00");
        aeropuertoSalida = aeropuertos.get(informacion[0].substring(0, 4));
        fechaEnvio.setTime(fechaEnvioTemp);
        fechaEnvio.add(Calendar.HOUR, -aeropuertoSalida.getHusoHorario());
        if(fechaInicio.before(fechaEnvio.getTime()) && fechaEnvio.getTime().before(fechaFin.getTime())){
          Envio envio = new Envio();
          envio.setCodigo(informacion[0]);
          envio.setFechaEnvio(fechaEnvio.getTime());
          envio.setAeropuertoPartida(aeropuertoSalida);
          envio.setAeropuertoDestino(aeropuertos.get(destinoNumPaquetes[0]));
          envio.setNumeroPaquetes(Integer.parseInt(destinoNumPaquetes[1]));
          fechaLimite.setTime(fechaEnvio.getTime());
          if(esIntercontinental(envio))
            fechaLimite.add(Calendar.DATE, 1);
          else
            fechaLimite.add(Calendar.DATE, 2);
          envio.setFechaLimite(fechaLimite.getTime());
          envios.add(envio);
        }
        else break;
        ddtemp = dd;
        first = false;
      }
      if(enviosList.size() == 0){
        enviosList.add(envios);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    for(int i = 0; i < enviosList.size(); i++){
      Collections.sort(enviosList.get(i), new SortEnvios());
    }
  }

  public void leerEnviosTXT(HashMap<String, Aeropuerto> aeropuertos, MultipartFile file, Date fechaInicio, EnvioService envioService){
    var convertedFile = toFile(file);
    String[] informacion, destinoNumPaquetes;
    String line;
    Calendar fechaFin = Calendar.getInstance(), 
      fechaLimite = Calendar.getInstance(), fechaEnvio = Calendar.getInstance(), 
      fechaLimiteUTC = Calendar.getInstance(), fechaEnvioUTC = Calendar.getInstance();
    fechaFin.setTime(fechaInicio);
    fechaFin.add(Calendar.DAY_OF_MONTH, 5);
    Aeropuerto aeropuertoSalida;
    try{
      BufferedReader br = new BufferedReader(new FileReader(convertedFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");
        destinoNumPaquetes = informacion[3].split(":");
        aeropuertoSalida = aeropuertos.get(informacion[0].substring(0, 4));
        obtenerTiemposDeEnvio(fechaEnvio, fechaEnvioUTC, fechaLimite, fechaLimiteUTC, informacion, aeropuertoSalida.getHusoHorario());
        if(fechaInicio.before(fechaEnvio.getTime()) && fechaEnvio.getTime().before(fechaFin.getTime())){
          Envio envio = new Envio();
          envio.setCodigo(informacion[0]);
          envio.setFechaEnvio(fechaEnvio.getTime());
          envio.setFechaEnvioUTC(fechaEnvioUTC.getTime());
          envio.setAeropuertoPartida(aeropuertoSalida);
          envio.setAeropuertoDestino(aeropuertos.get(destinoNumPaquetes[0]));
          envio.setNumeroPaquetes(Integer.parseInt(destinoNumPaquetes[1]));
          if(esIntercontinental(envio))
            fechaLimite.add(Calendar.HOUR_OF_DAY, 24);
          else
            fechaLimite.add(Calendar.HOUR_OF_DAY, 48);
          fechaLimiteUTC.setTime(fechaLimite.getTime());
          fechaLimiteUTC.add(Calendar.HOUR_OF_DAY, -aeropuertoSalida.getHusoHorario());
          envio.setFechaLimite(fechaLimite.getTime());
          envio.setFechaLimiteUTC(fechaLimiteUTC.getTime());
          envioService.insert(envio);
          
        }
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public void escribirEnviosSQL(HashMap<String, Aeropuerto> aeropuertos, MultipartFile file, Date fechaInicio){
    var convertedFile = toFile(file);
    String[] informacion, destinoNumPaquetes;
    String line;
    File enviosSQL = new File("envios.sql");
    Calendar fechaFin = Calendar.getInstance(), 
      fechaLimite = Calendar.getInstance(), fechaEnvio = Calendar.getInstance(), 
      fechaLimiteUTC = Calendar.getInstance(), fechaEnvioUTC = Calendar.getInstance();
    fechaFin.setTime(fechaInicio);
    fechaFin.add(Calendar.DAY_OF_MONTH, 5);
    Aeropuerto aeropuertoSalida;
    try{
      BufferedReader br = new BufferedReader(new FileReader(convertedFile));
      FileOutputStream fos = new FileOutputStream(enviosSQL);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write("use `redex-db`;");
      bw.newLine();
      bw.newLine();
      while ((line = br.readLine()) != null) {
        informacion = line.split("-");
        destinoNumPaquetes = informacion[3].split(":");
        aeropuertoSalida = aeropuertos.get(informacion[0].substring(0, 4));
        obtenerTiemposDeEnvio(fechaEnvio, fechaEnvioUTC, fechaLimite, fechaLimiteUTC, informacion, aeropuertoSalida.getHusoHorario());
        Envio envio = new Envio();
        envio.setCodigo(informacion[0]);
        envio.setFechaEnvio(fechaEnvio.getTime());
        envio.setFechaEnvioUTC(fechaEnvioUTC.getTime());
        envio.setAeropuertoPartida(aeropuertoSalida);
        envio.setAeropuertoDestino(aeropuertos.get(destinoNumPaquetes[0]));
        envio.setNumeroPaquetes(Integer.parseInt(destinoNumPaquetes[1]));
        if(esIntercontinental(envio))
          fechaLimite.add(Calendar.HOUR_OF_DAY, 24);
        else
          fechaLimite.add(Calendar.HOUR_OF_DAY, 48);
        fechaLimiteUTC.setTime(fechaLimite.getTime());
        fechaLimiteUTC.add(Calendar.HOUR_OF_DAY, -aeropuertoSalida.getHusoHorario());
        envio.setFechaLimite(fechaLimite.getTime());
        envio.setFechaLimiteUTC(fechaLimiteUTC.getTime());
        bw.write(lineaEnvioSQL(envio));
        bw.newLine();
        bw.newLine();
      }
      bw.close();
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
  }

  public List<Envio> getEnviosInRange(HashMap<String, Aeropuerto> aeropuertos, Date fechaInicio, Date fechaFinal){
    List<Envio> envios = new ArrayList<>();
    String[] aeroCodes = new String[]{
      "BIKF","EBCI","EDDI","EETN","EFHK","EGLL","EHAM","EIDW","EKCH","ELLX","ENGM","EPMO","ESKN","EVRA","LATI","LBSF","LDZA","LEMD","LFPG","LGAV",
      "LHBP","LIRA","LJLJ","LKPR","LMML","LOWW","LPPT","LSZB","LZIB","SABE","SBBR","SCEL","SEQM","SGAS","SKBO","SLLP","SPIM","SUAA","SVMI","UMMS"};
    String[] informacion, destinoNumPaquetes;
    String line;
    Calendar fechaLimite = Calendar.getInstance(), fechaEnvio = Calendar.getInstance(), 
      fechaLimiteUTC = Calendar.getInstance(), fechaEnvioUTC = Calendar.getInstance();
    Aeropuerto aeropuertoSalida;
    for(String code : aeroCodes){
      try {
        File archivo = new File(System.getProperty("user.dir") + 
          "\\src\\main\\java\\com\\redexbackend\\redexbackend\\envios_historicos.v01\\pack_enviado_" + code + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        while ((line = br.readLine()) != null){
          informacion = line.split("-");
          destinoNumPaquetes = informacion[3].split(":");
          aeropuertoSalida = aeropuertos.get(code);
          obtenerTiemposDeEnvio(fechaEnvio, fechaEnvioUTC, fechaLimite, fechaLimiteUTC, informacion, aeropuertoSalida.getHusoHorario());
          if(fechaEnvioUTC.getTime().compareTo(fechaFinal) > 0){
            // System.out.println(code + " - primer envío: " + envios.get(0).getCodigo() + " - " + envios.get(0).getFechaEnvioUTC());
            // System.out.println(code + " - primer último: " + envios.get(envios.size() - 1).getCodigo() + " - " + envios.get(envios.size() - 1).getFechaEnvioUTC() +"\n");
            break;
          }
          if(fechaEnvioUTC.getTime().compareTo(fechaInicio) >= 0 && fechaEnvioUTC.getTime().compareTo(fechaFinal) <= 0){
            Envio envio = new Envio();
            envio.setCodigo(informacion[0]);
            envio.setFechaEnvio(fechaEnvio.getTime());
            envio.setFechaEnvioUTC(fechaEnvioUTC.getTime());
            envio.setAeropuertoPartida(aeropuertoSalida);
            envio.setAeropuertoDestino(aeropuertos.get(destinoNumPaquetes[0]));
            envio.setNumeroPaquetes(Integer.parseInt(destinoNumPaquetes[1]));
            if(esIntercontinental(envio)) fechaLimite.add(Calendar.HOUR_OF_DAY, 24);
            else fechaLimite.add(Calendar.HOUR_OF_DAY, 48);
            fechaLimiteUTC.setTime(fechaLimite.getTime());
            fechaLimiteUTC.add(Calendar.HOUR_OF_DAY, -aeropuertoSalida.getHusoHorario());
            envio.setFechaLimite(fechaLimite.getTime());
            envio.setFechaLimiteUTC(fechaLimiteUTC.getTime());
            envios.add(envio);
          }
        }
        br.close();
      } catch (Exception ex) {
        System.out.println("Se ha producido un error: " + ex.getMessage());
      }
    }
    return envios;
  }

  public void escribirEnviosSQL(HashMap<String, Aeropuerto> aeropuertos){
    String[] aeroNames = new String[]{"BIKF","EBCI","EDDI","EETN","EFHK","EGLL","EHAM","EIDW","EKCH","ELLX","ENGM","EPMO","ESKN","EVRA","LATI","LBSF","LDZA","LEMD","LFPG","LGAV","LHBP","LIRA","LJLJ","LKPR","LMML","LOWW","LPPT","LSZB","LZIB","SABE","SBBR","SCEL","SEQM","SGAS","SKBO","SLLP","SPIM","SUAA","SVMI","UMMS"};
    String[] informacion, destinoNumPaquetes;
    String line;
    Calendar fechaLimite = Calendar.getInstance(), fechaEnvio = Calendar.getInstance(), 
      fechaLimiteUTC = Calendar.getInstance(), fechaEnvioUTC = Calendar.getInstance();
    Aeropuerto aeropuertoSalida;
    int i = 0;
    for(String aero : aeroNames){
      try{
        File enviosSQL = new File("envios_" + aero + ".sql");
        File archivoLeido = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\envios_historicos.v01\\pack_enviado_" + aero + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(archivoLeido));
        FileOutputStream fos = new FileOutputStream(enviosSQL);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write("use `redex-db`;");
        bw.newLine();
        bw.newLine();
        while ((line = br.readLine()) != null) {
          informacion = line.split("-");
          destinoNumPaquetes = informacion[3].split(":");
          aeropuertoSalida = aeropuertos.get(informacion[0].substring(0, 4));
          obtenerTiemposDeEnvio(fechaEnvio, fechaEnvioUTC, fechaLimite, fechaLimiteUTC, informacion, aeropuertoSalida.getHusoHorario());
          Envio envio = new Envio();
          envio.setCodigo(informacion[0]);
          envio.setFechaEnvio(fechaEnvio.getTime());
          envio.setFechaEnvioUTC(fechaEnvioUTC.getTime());
          envio.setAeropuertoPartida(aeropuertoSalida);
          envio.setAeropuertoDestino(aeropuertos.get(destinoNumPaquetes[0]));
          envio.setNumeroPaquetes(Integer.parseInt(destinoNumPaquetes[1]));
          if(esIntercontinental(envio))
            fechaLimite.add(Calendar.HOUR_OF_DAY, 24);
          else
            fechaLimite.add(Calendar.HOUR_OF_DAY, 48);
          fechaLimiteUTC.setTime(fechaLimite.getTime());
          fechaLimiteUTC.add(Calendar.HOUR_OF_DAY, -aeropuertoSalida.getHusoHorario());
          envio.setFechaLimite(fechaLimite.getTime());
          envio.setFechaLimiteUTC(fechaLimiteUTC.getTime());
          if (i % 5000 == 0){
            bw.write(";\n\n");
            bw.write(lineaEnvioInsertSQL());
          }else bw.write(",\n");
          bw.write(lineaEnvioValuesSQL(envio));
          i++;
        }
        bw.write(";\n");
        bw.close();
        br.close();
      } catch (Exception ex) {
        System.out.println("Se ha producido un error: " + ex.getMessage());
      }
    }
  }

  String lineaEnvioSQL(Envio envio){
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String datos = "'" + 
      envio.getCodigo() + "','" + 
      format.format(envio.getFechaEnvio()) + "','" + 
      format.format(envio.getFechaEnvioUTC()) + "','" +
      format.format(envio.getFechaLimite()) + "','" +
      format.format(envio.getFechaLimiteUTC()) + "'," +
      envio.getNumeroPaquetes() + "," +
      envio.getAeropuertoDestino().getId() + "," +
      envio.getAeropuertoPartida().getId();
    String linea = "INSERT INTO envio (estado,codigo,fecha_envio,fecha_envioutc,fecha_limite,fecha_limiteutc,numero_paquetes,id_aeropuerto_destino,id_aeropuerto_partida)\nVALUES(1," + datos + ");";
    return linea;
  }

  String lineaEnvioInsertSQL(){
    String linea = 
    "INSERT INTO envio (estado,codigo,fecha_envio,fecha_envioutc,fecha_limite,fecha_limiteutc,numero_paquetes,id_aeropuerto_destino,id_aeropuerto_partida)\n" + 
    "VALUES\n";
    return linea;
  }

  String lineaEnvioValuesSQL(Envio envio){
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String datos = "'" + 
      envio.getCodigo() + "','" + 
      format.format(envio.getFechaEnvio()) + "','" + 
      format.format(envio.getFechaEnvioUTC()) + "','" +
      format.format(envio.getFechaLimite()) + "','" +
      format.format(envio.getFechaLimiteUTC()) + "'," +
      envio.getNumeroPaquetes() + "," +
      envio.getAeropuertoDestino().getId() + "," +
      envio.getAeropuertoPartida().getId();
    String linea = "(1," + datos + ")";
    return linea;
  }

  void obtenerTiemposDeEnvio(Calendar fechaEnvio, Calendar fechaEnvioUTC, Calendar fechaLimite, Calendar fechaLimiteUTC, String[] informacion, Integer husoHorario){
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String yy, mm, dd;
    Date fecha = new Date();
    yy = informacion[1].substring(0,4);
    mm = informacion[1].substring(4,6);
    dd = informacion[1].substring(6,8);
    try{
      fecha = formato.parse(yy + "-" + mm + "-" + dd + " " + informacion[2] + ":00");
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    fechaEnvio.setTime(fecha);
    fechaEnvioUTC.setTime(fecha);
    fechaEnvioUTC.add(Calendar.HOUR, -husoHorario);
    fechaLimite.setTime(fecha);
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

  public HashMap<String, Aeropuerto> leerAeropuertos(HashMap<String, Ciudad> ciudades, HashMap<String, Integer> timeZones, HashMap<String, Configuracion> configuracion) {
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
          capacidad = configuracion.get("CapacidadAeropuertoAmerica").getValor();
        else
          capacidad = configuracion.get("CapacidadAeropuertoEuropa").getValor();
        Aeropuerto aeropuerto = new Aeropuerto(informacion[0], informacion[1], informacion[1], capacidad, capacidad,
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

  public void escribirSQL(List<Vuelo> vuelos){
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Collections.sort(vuelos, new SortVuelosByDuration());
    try{
      File file = new File("vuelos.sql");
      FileOutputStream fos = new FileOutputStream(file);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      bw.write("use `redex-db`;");
      bw.newLine();
      bw.newLine();
      bw.write(
        "INSERT INTO vuelo " + 
        "(estado,capacidad,capacidad_actual,codigo,disponible,duracion,fecha_destino,fecha_partida,fecha_destinoutc0,fecha_partidautc0,id_aeropuerto_destino,id_aeropuerto_partida) VALUES\n"
      );
      for (Vuelo vuelo : vuelos){
        bw.write(
          "(1," + 
          vuelo.getCapacidad() + "," + 
          vuelo.getCapacidadActual() + ",'" + 
          vuelo.getCodigo() + "',1," + 
          vuelo.getDuracion() + ",'" + 
          dateFormat.format(vuelo.getFechaDestino()) + "','" + 
          dateFormat.format(vuelo.getFechaPartida()) + "','" + 
          dateFormat.format(vuelo.getFechaDestinoUTC0()) + "','" + 
          dateFormat.format(vuelo.getFechaPartidaUTC0()) + "'," + 
          vuelo.getAeropuertoDestino().getId() + "," + 
          vuelo.getAeropuertoPartida().getId() + "),\n");
      }
      bw.write(";\n");
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

  public void escribirEnvios(){
    String[] aeropuertos = new String[]{"BIKF","EBCI","EDDI","EETN","EFHK","EGLL","EHAM","EIDW","EKCH","ELLX","ENGM","EPMO","ESKN","EVRA","LATI","LBSF","LDZA","LEMD","LFPG","LGAV","LHBP","LIRA","LJLJ","LKPR","LMML","LOWW","LPPT","LSZB","LZIB","SABE","SBBR","SCEL","SEQM","SGAS","SKBO","SLLP","SPIM","SUAA","SVMI","UMMS"};
    String line;
    BufferedReader br;
    File archivoLeido, envios = new File("enviosCompletos.txt");
    try{
      FileOutputStream fos = new FileOutputStream(envios);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      for (String aeropuerto : aeropuertos) {
        archivoLeido = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\envios_historicos.v01\\pack_enviado_" + aeropuerto + ".txt");
        br = new BufferedReader(new FileReader(archivoLeido));
        while((line = br.readLine()) != null){
          bw.write(line);
          bw.newLine();
        }
      }
      bw.close();
    } catch (Exception ex){
      System.out.println(ex.getMessage());
    }
  }

  public HashMap<String, Configuracion> leerConfiguracion(){
    HashMap<String, Configuracion> configuraciones = new HashMap<>();
    String[] informacion;
    String line;
    File configFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\configuracion.txt");
    try {
      BufferedReader br = new BufferedReader(new FileReader(configFile));
      while ((line = br.readLine()) != null) {
        informacion = line.split(",");
        Configuracion configuracion = new Configuracion();
        configuracion.setNombre(informacion[0]);
        configuracion.setValor(Integer.parseInt(informacion[1]));
        configuraciones.put(informacion[0], configuracion);
      }
      br.close();
    } catch (Exception ex) {
      System.out.println("Se ha producido un error: " + ex.getMessage());
    }
    return configuraciones;
  }

}
