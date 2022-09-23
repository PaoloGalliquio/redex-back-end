package com.redexbackend.redexbackend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.redexbackend.models.AStar;
import com.redexbackend.models.Ciudad;
import com.redexbackend.models.Continente;
import com.redexbackend.models.Dijkstra;
import com.redexbackend.models.Envio;
import com.redexbackend.models.Graph;
import com.redexbackend.models.LeerArchivos;
import com.redexbackend.models.Node;
import com.redexbackend.models.Pais;

@SpringBootApplication
public class RedexBackEndApplication {
	public static void main(String[] args){
		LeerArchivos lector = new LeerArchivos();
		HashMap<String, Continente> continentes = lector.leerContinentes();
		HashMap<String, Pais> paises = lector.leerPaises(continentes);
		HashMap<String, Ciudad> ciudades = lector.leerCiudades(paises);
		HashMap<String, Node> aeropuertos = lector.leerAeropuertos(ciudades);
		HashMap<String, Integer> timeZones = lector.leerTimeZones();
		HashMap<String, Envio> envios = lector.leerEnvios(aeropuertos);
		
		Graph mapa = new Graph();

		String origen = "LZIB";
		String destino = "BIKF";
		
		agregarDestinos(aeropuertos, timeZones);

		resultado(mapa, aeropuertos, origen, destino);

		//SpringApplication.run(RedexBackEndApplication.class, args);
	}
	
	private static void resultado(Graph mapa, HashMap<String, Node> aeropuertos, String origen, String destino){
		Scanner lectura = new Scanner (System.in);
		System.out.println("Escoja el algoritmo: D para Dijkstra - A para A*");
		String algoritmo = lectura.next();

		if(algoritmo.equalsIgnoreCase("A"))
			imprimirAstar(aeropuertos, origen, destino);
		else if(algoritmo.equalsIgnoreCase("D"))
			imprimirDijkstra(mapa, aeropuertos, origen, destino);
		else
			System.out.println("No es una opción correcta");
		
		lectura.close();
	}

	private static void imprimirAstar(HashMap<String, Node> aeropuertos, String origen, String destino){
		aeropuertos.get(origen).g = 0;

		Node answer = AStar.aStar(aeropuertos.get(origen), aeropuertos.get(destino));
		AStar.printPath(answer);
	}

	private static void imprimirDijkstra(Graph mapa, HashMap<String, Node> aeropuertos, String origen, String destino){
		for(HashMap.Entry<String, Node> aeropuerto : aeropuertos.entrySet()){
			mapa.addNode(aeropuerto.getKey(), aeropuerto.getValue());
		}

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
	}

	private static void agregarDestinos(HashMap<String, Node> aeropuertos, HashMap<String, Integer> timeZones){
		String[] informacion;
		int tiempo;
		String line;
		File vuelosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\vuelos.txt");
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

		return duracion + 60;
	}

}
