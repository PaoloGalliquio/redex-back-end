package com.redexbackend.redexbackend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.redexbackend.models.Aeropuerto;
import com.redexbackend.models.Dijkstra;
import com.redexbackend.models.Graph;
import com.redexbackend.models.Node;

@SpringBootApplication
public class RedexBackEndApplication {

	public static void main(String[] args){
		HashMap<String, Node> aeropuertos = leerAeropuertos();
		HashMap<String, Integer> timeZones = obtenerTimeZones();
		agregarDestinos(aeropuertos, timeZones);

		Graph mapa = new Graph();

		for(HashMap.Entry<String, Node> aeropuerto : aeropuertos.entrySet()){
			mapa.addNode(aeropuerto.getKey(), aeropuerto.getValue());
		}

		String origen = "LZIB";
		String destino = "BIKF";

		mapa = Dijkstra.calculateShortestPathFromSource(mapa, aeropuertos.get(origen));

		String minutos;

		if(mapa.getNodes().get(destino).getDistance()%60 < 10){
			minutos = "0" + mapa.getNodes().get(destino).getDistance()%60; 
		}else{
			minutos = "" + mapa.getNodes().get(destino).getDistance()%60;
		}

		System.out.println(
			aeropuertos.get(origen).getAeropuerto().getCiudad() + 
			" -> " + 
			mapa.getNodes().get(destino).getAeropuerto().getCiudad() + 
			" : " +
			mapa.getNodes().get(destino).getDistance()/60 +
			":" +
			minutos
		);
		for(Node node : mapa.getNodes().get(destino).getShortestPath()){
			System.out.println(" - " + node.getAeropuerto().getCiudad());
		}

		SpringApplication.run(RedexBackEndApplication.class, args);
	}

	private static HashMap<String, Node> leerAeropuertos(){
		HashMap<String, Node> aeropuertos = new HashMap<>();
		String[] informacion;
		String line;
		File aeropuertosFile = new File(".\\redex-back-end\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
				Node aeropuerto = new Node(new Aeropuerto(informacion[0], informacion[1], informacion[2], informacion[3], informacion[4], informacion[5]));
				aeropuertos.put(informacion[1], aeropuerto);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return aeropuertos;
	}

	private static HashMap<String, Integer> obtenerTimeZones(){
		HashMap<String, Integer> timezones = new HashMap<>();
		String[] informacion;
		String line;
		File timezonesFile = new File(".\\redex-back-end\\src\\main\\java\\com\\redexbackend\\redexbackend\\timezones.txt");
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

	private static void agregarDestinos(HashMap<String, Node> aeropuertos, HashMap<String, Integer> timeZones){
		String[] informacion;
		int tiempo;
		String line;
		File vuelosFile = new File(".\\redex-back-end\\src\\main\\java\\com\\redexbackend\\redexbackend\\vuelos.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(vuelosFile));
			while((line = br.readLine()) != null){
				informacion = line.split("-");
				tiempo = obtenerTiempo(timeZones, aeropuertos.get(informacion[0]), informacion[2], aeropuertos.get(informacion[1]), informacion[3]);
				aeropuertos.get(informacion[0]).addDestination(aeropuertos.get(informacion[1]), tiempo);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
	}

	private static int obtenerTiempo(HashMap<String, Integer> timeZones, Node aeropuertoSalida, String salida, Node aeropuertoLlegada, String llegada){
		int duracion;
		String[] salidaSplit = salida.split(":");
		String[] llegadaSplit = llegada.split(":");
		int UTCSalida = timeZones.get(aeropuertoSalida.getAeropuerto().getCodigo());
		int UTCLlegada = timeZones.get(aeropuertoLlegada.getAeropuerto().getCodigo());

		int hSalida = Integer.parseInt(salidaSplit[0]) - UTCSalida;
		int mSalida = Integer.parseInt(salidaSplit[1]);
		
		int hLlegada = Integer.parseInt(llegadaSplit[0]) - UTCLlegada;
		int mLlegada = Integer.parseInt(llegadaSplit[1]);

		if(UTCLlegada < UTCSalida)
			duracion = (24 + hLlegada - hSalida) * 60 + (mLlegada - mSalida);
		else
			duracion = (hLlegada - hSalida) * 60 + (mLlegada - mSalida);
		
		if(duracion < 0)
			duracion += 24*60;

		//if(aeropuertoSalida.getAeropuerto().getContinente().equals(aeropuertoLlegada.getAeropuerto().getContinente())){
		//if(duracion < 20 || duracion <= 0){
		// 	System.out.println("------------------");
		// 	System.out.println(aeropuertoSalida.getAeropuerto().getCiudad() + " (" + salida + ")(UTC" + timeZones.get(aeropuertoSalida.getAeropuerto().getCodigo()) + ")");
		// 	System.out.println(aeropuertoLlegada.getAeropuerto().getCiudad() + " (" + llegada + ")(UTC" + timeZones.get(aeropuertoLlegada.getAeropuerto().getCodigo()) + ")");
		// 	System.out.println("Duración: " + duracion/60 + ":" + duracion%60);
		// }

		return duracion;
	}
}
