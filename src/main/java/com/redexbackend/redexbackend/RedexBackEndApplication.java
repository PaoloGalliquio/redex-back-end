package com.redexbackend.redexbackend;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.redexbackend.models.AStar;
import com.redexbackend.models.Aeropuerto;
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
	public static String aws_access_key;
	public static String aws_secret_key;
	public static String aws_session_token;

	public static void main(String[] args) {
		HashMap<String, Integer> timeZones = new HashMap<>();
		HashMap<String, Continente> continentes = new HashMap<>();
		HashMap<String, Pais> paises = new HashMap<>();
		HashMap<String, Ciudad> ciudades = new HashMap<>();
		HashMap<String, Node> aeropuertos = new HashMap<>();
		HashMap<String, Vuelo> vuelos = new HashMap<>();
		LeerArchivos lector = new LeerArchivos(timeZones, continentes, paises, ciudades, aeropuertos, vuelos);
		HashMap<String, Envio> envios = lector.leerEnvios(aeropuertos, "BIKF");

		// HashMap<String, Integer> timeZonesA = lector.leerTimeZones();
		// HashMap<String, Continente> continentesA = lector.leerContinentes();
		// HashMap<String, Pais> paisesA = lector.leerPaises(continentes);
		// HashMap<String, Ciudad> ciudadesA = lector.leerCiudades(paises);
		// HashMap<String, Node> aeropuertosA = lector.leerAeropuertos(ciudades, timeZones);
		// HashMap<String, Vuelo> vuelosA = lector.leerVuelos(aeropuertos);
		
		int numPaquetes, minActual = 0;
		String origen, destino;
		Date momento;
		HashMap<Date, HashMap<String, Integer>> cambios = new HashMap<>();
		HashMap<String, Integer> cambioOrigen, cambioDestino;
				
		for (HashMap.Entry<String, Envio> envio : envios.entrySet()) {
			origen = envio.getValue().getAeropuertoPartida().getCodigo();
			destino = envio.getValue().getAeropuertoDestino().getCodigo();
			numPaquetes = envio.getValue().getNumeroPaquetes();

			momento = envio.getValue().getFechaEnvio();

			if(!cambios.containsKey(momento))
				cambios.put(momento, new HashMap<>());
			
			cambioOrigen = cambios.get(momento);
			cambioDestino = cambios.get(momento);
			//cambioDestino = cambios.get(momento + envio.getValue().getDuracionTotal());

			if(cambioOrigen.containsKey(origen))
				cambioOrigen.put(origen, cambioOrigen.get(origen) - numPaquetes);
			else
				cambioOrigen.put(origen, -numPaquetes);

			if(cambioDestino.containsKey(destino)) 
				cambioDestino.put(origen, cambioDestino.get(destino) + numPaquetes);
			else
				cambioDestino.put(origen, numPaquetes);

			imprimirAstar(aeropuertos, origen, destino, timeZones, numPaquetes);
		}
		// resultado(mapa, aeropuertos, origen, destino, timeZones);
		// Graph mapa = new Graph();
		// SpringApplication.run(RedexBackEndApplication.class, args);
	}

	private static void resultado(Graph mapa, HashMap<String, Node> aeropuertos, String origen, String destino,
			HashMap<String, Integer> timeZones) {
		Scanner lectura = new Scanner(System.in);
		System.out.println("Escoja el algoritmo: D para Dijkstra - A para A*");
		String algoritmo = lectura.next();

		if (algoritmo.equalsIgnoreCase("A"))
			imprimirAstar(aeropuertos, origen, destino, timeZones, 250);
		else if (algoritmo.equalsIgnoreCase("D"))
			imprimirDijkstra(mapa, aeropuertos, origen, destino);
		else
			System.out.println("No es una opción correcta");

		lectura.close();
	}

	private static void imprimirAstar(HashMap<String, Node> aeropuertos, String origen, String destino, HashMap<String, Integer> timeZones, Integer nroPaquetes) {

		aeropuertos.get(origen).getAeropuerto().g = 0;

		Aeropuerto answer = AStar.aStar(aeropuertos.get(origen).getAeropuerto(), aeropuertos.get(destino).getAeropuerto(),timeZones, nroPaquetes);

		AStar.printPath(answer, aeropuertos.get(origen).getAeropuerto(), aeropuertos.get(destino).getAeropuerto(),timeZones);
	}

	private static void imprimirDijkstra(Graph mapa, HashMap<String, Node> aeropuertos, String origen, String destino) {
		for (HashMap.Entry<String, Node> aeropuerto : aeropuertos.entrySet()) {
			mapa.addNode(aeropuerto.getKey(), aeropuerto.getValue());
		}

		mapa = Dijkstra.calculateShortestPathFromSource(mapa, aeropuertos.get(origen));

		String minutos;

		if (mapa.getNodes().get(destino).getDistance() % 60 < 10) {
			minutos = "0" + mapa.getNodes().get(destino).getDistance() % 60;
		} else {
			minutos = "" + mapa.getNodes().get(destino).getDistance() % 60;
		}

		System.out.println("Solucion A*");
		System.out.println("==============================================");
		System.out.println(
				"Origen: " + aeropuertos.get(origen).getAeropuerto().getCiudad().getNombre() + " "
						+ aeropuertos.get(origen).getAeropuerto().getCodigo() + " (UTC: "
						+ aeropuertos.get(origen).getAeropuerto().getUTC()
						+ ")");
		System.out.println(
				"Destino: " + aeropuertos.get(destino).getAeropuerto().getCiudad().getNombre() + " "
						+ aeropuertos.get(destino).getAeropuerto().getCiudad().getCodigo() + " (UTC: "
						+ aeropuertos.get(destino).getAeropuerto().getUTC()
						+ ")");
		System.out.println("Duracion: " + minutos);

		// System.out.println(
		// aeropuertos.get(origen).getAeropuerto().getCiudad().getNombre() +
		// " -> " +
		// mapa.getNodes().get(destino).getAeropuerto().getCiudad().getNombre() +
		// " : " +
		// mapa.getNodes().get(destino).getDistance() / 60 +
		// ":" +
		// minutos);
		System.out.println("==============================================");
		System.out.println("Ruta:");
		for (Vuelo vuelo : mapa.getNodes().get(destino).getShortestPath()) {
			System.out.println("    " + vuelo.getAeropuertoPartido().getCiudad().getNombre());
		}

		System.out.println("==============================================");
		System.out.println("Ruta vuelos:");
		for (Vuelo vuelo : mapa.getNodes().get(destino).getShortestPath()) {
			System.out.println("    " + vuelo.getCodigo() + ": " + vuelo.getFechaPartida());
		}
	}
}
