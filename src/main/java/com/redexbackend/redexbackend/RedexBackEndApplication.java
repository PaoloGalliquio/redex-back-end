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
import com.redexbackend.models.Vuelo;

@SpringBootApplication
public class RedexBackEndApplication {
	public static void main(String[] args){
		LeerArchivos lector = new LeerArchivos();
		HashMap<String, Continente> continentes = lector.leerContinentes();
		HashMap<String, Pais> paises = lector.leerPaises(continentes);
		HashMap<String, Ciudad> ciudades = lector.leerCiudades(paises);
		HashMap<String, Node> aeropuertos = lector.leerAeropuertos(ciudades);
		HashMap<String, Integer> timeZones = lector.leerTimeZones();
		HashMap<String, Vuelo> vuelos = lector.leerVuelos(aeropuertos, timeZones);
		HashMap<String, Envio> envios = lector.leerEnvios(aeropuertos);
		//lector.agregarDestinos(aeropuertos, timeZones);

		Graph mapa = new Graph();

		String origen = "LZIB";
		String destino = "BIKF";
		
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
			aeropuertos.get(origen).getAeropuerto().getCiudad().getNombre() + 
			" -> " + 
			mapa.getNodes().get(destino).getAeropuerto().getCiudad().getNombre() + 
			" : " +
			mapa.getNodes().get(destino).getDistance()/60 +
			":" +
			minutos
		);
		for(Node node : mapa.getNodes().get(destino).getShortestPath()){
			System.out.println(" - " + node.getAeropuerto().getCiudad().getNombre());
		}
	}
}
