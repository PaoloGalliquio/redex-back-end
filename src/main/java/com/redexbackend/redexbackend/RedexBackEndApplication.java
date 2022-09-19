package com.redexbackend.redexbackend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.redexbackend.models.AStar;
import com.redexbackend.models.Aeropuerto;
import com.redexbackend.models.AeropuertoF;
import com.redexbackend.models.Ciudad;
import com.redexbackend.models.Continente;
import com.redexbackend.models.Dijkstra;
import com.redexbackend.models.Envio;
import com.redexbackend.models.Graph;
import com.redexbackend.models.Node;
import com.redexbackend.models.Pais;

@SpringBootApplication
public class RedexBackEndApplication {
	public static void main(String[] args){
		Graph mapa = new Graph();
		HashMap<String, Node> aeropuertos = leerAeropuertos();
		HashMap<String, Integer> timeZones = leerTimeZones();
		HashMap<String, Envio> envios = leerEnvios(aeropuertos);

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

	private static HashMap<String, Node> leerAeropuertos(){
		HashMap<String, Node> aeropuertos = new HashMap<>();
		String[] informacion;
		String line;
		File aeropuertosFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\redexbackend\\redexbackend\\aeropuertos.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(aeropuertosFile));
			while((line = br.readLine()) != null){
				informacion = line.split(";");
				Node aeropuerto = new Node(new AeropuertoF(informacion[0], informacion[1], informacion[2], informacion[3], informacion[4], informacion[5], informacion[6], informacion[7]));
				aeropuertos.put(informacion[1], aeropuerto);
			}
			br.close();
		}
		catch(Exception ex){
			System.out.println("Se ha producido un error: " + ex.getMessage());
		}
		return aeropuertos;
	}

	private static HashMap<String, Integer> leerTimeZones(){
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

	private static HashMap<String, Envio> leerEnvios(HashMap<String, Node> aeropuertos){
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
				AeropuertoF salidaF = aeropuertos.get("BIKF").getAeropuerto();
				AeropuertoF destinoF = aeropuertos.get(destinoNumPaquetes[0]).getAeropuerto();
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
