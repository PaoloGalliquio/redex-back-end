package com.redexbackend.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class LeerArchivos {

  public HashMap<String, Continente> leerContinentes(){
    HashMap<String, Continente> continentes = new HashMap<>();
		String[] informacion;
		String line;
		File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\continentes.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
				Continente continente = new Continente(informacion[0], informacion[1], informacion[2], informacion[3], informacion[4], 1);
				continentes.put(informacion[4], continente);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return continentes;
  }

  public HashMap<String, Pais> leerPaises(HashMap<String, Continente> continentes){
    HashMap<String, Pais> paises = new HashMap<>();
		String[] informacion;
		String line;
		File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
        Pais pais = new Pais(informacion[0], informacion[3], informacion[3], continentes.get(informacion[5]), 1);
        continentes.get(informacion[5]).addPais(pais);
				paises.put(informacion[3], pais);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return paises;
  }

  public HashMap<String, Ciudad> leerCiudades(HashMap<String, Pais> paises){
    HashMap<String, Ciudad> ciudades = new HashMap<>();
		String[] informacion;
		String line;
		File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
        Ciudad ciudad = new Ciudad(informacion[0], informacion[2], informacion[2], informacion[6], informacion[7], paises.get(informacion[3]), 1);
				ciudades.put(informacion[4], ciudad);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return ciudades;
  }

  public HashMap<String, Node> leerAeropuertos(HashMap<String, Ciudad> ciudades){
    HashMap<String, Node> aeropuertos = new HashMap<>();
		String[] informacion;
		String line, codigoContinente;
    int capacidad;
		File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
        codigoContinente = ciudades.get(informacion[4]).getPais().getContinente().getCodigo();
        if(codigoContinente == "AMN" || codigoContinente == "AMS" || codigoContinente == "AMC") capacidad = 850;
        else capacidad = 900;
				Aeropuerto aeropuerto = new Aeropuerto(informacion[0], informacion[1], informacion[1], capacidad, 1000, informacion[6], informacion[7], ciudades.get(informacion[4]), 1);
        ciudades.get(informacion[4]).setAeropuerto(aeropuerto);
        Node node = new Node(aeropuerto);
				aeropuertos.put(informacion[1], node);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return aeropuertos;
  }

	public HashMap<String, Integer> leerTimeZones(){
		HashMap<String, Integer> timezones = new HashMap<>();
		String[] informacion;
		String line;
		File timezonesFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\timezones.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(timezonesFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
				timezones.put(informacion[0], Integer.parseInt(informacion[1]));
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return timezones;
	}

	public HashMap<String, Envio> leerEnvios(HashMap<String, Node> aeropuertos){
		HashMap<String, Envio> envios = new HashMap<>();
		Continente continente = new Continente(0, "ASur", "América del Sur", 0, 0, 1);
		Pais pais = new Pais(0, "PER", "Perú", continente, 1);
		Ciudad ciudad = new Ciudad(0, "LIM", "Lima", "UTC-5", -5, -12.024105, -77.112165, pais, 1);
		String[] informacion, destinoNumPaquetes;
		String line;
		File enviosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\envios_historicos.v01\\pack_enviado_BIKF.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(enviosFile));
			while((line = br.readLine()) != null){
				informacion = line.split("-");
				destinoNumPaquetes = informacion[3].split(":");
				Aeropuerto salidaF = aeropuertos.get("BIKF").getAeropuerto();
				Aeropuerto destinoF = aeropuertos.get(destinoNumPaquetes[0]).getAeropuerto();
				Aeropuerto salida = new Aeropuerto(0, salidaF.getCodigo(), salidaF.getCodigo(), 300, 300, salidaF.getLatitud(), salidaF.getLongitud(), ciudad, 1);
				Aeropuerto destino = new Aeropuerto(1, destinoF.getCodigo(), destinoF.getCodigo(), 300, 300, destinoF.getLatitud(), destinoF.getLongitud(), ciudad, 1);
				Envio envio = new Envio(informacion[0], informacion[1],informacion[2], salida, destino, destinoNumPaquetes[1]);
				envios.put(envio.getCodigo(), envio);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return envios;
	}
}
