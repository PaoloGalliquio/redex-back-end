package com.redexbackend.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar{

    public static int obtenerTiempo(HashMap<String, Integer> timeZones, Vuelo vuelo){
		int duracion = 0;

		int UTCSalida = timeZones.get(vuelo.getAeropuertoPartido().getCodigo());
		int UTCLlegada = timeZones.get(vuelo.getAeropuertoDestino().getCodigo()); 

        //Odio las fechas

        Calendar fechaPartida = GregorianCalendar.getInstance();
        Calendar fechaLlegada = GregorianCalendar.getInstance();
        fechaPartida.setTime(vuelo.getFechaPartida());
        fechaLlegada.setTime(vuelo.getFechaDestino());

		int hSalida = fechaPartida.get(Calendar.HOUR_OF_DAY) - UTCSalida;
		int mSalida = fechaPartida.get(Calendar.MINUTE);
		
		int hLlegada = fechaLlegada.get(Calendar.HOUR_OF_DAY) - UTCLlegada;
		int mLlegada = fechaLlegada.get(Calendar.MINUTE);

		if(UTCLlegada < UTCSalida)
			duracion = (24 + hLlegada - hSalida) * 60 + (mLlegada - mSalida);
		else
			duracion = (hLlegada - hSalida) * 60 + (mLlegada - mSalida);
		
		if(duracion < 0)
			duracion += 24*60;

		return duracion;
	}

    public static Aeropuerto aStar(Aeropuerto start, Aeropuerto target, HashMap<String, Integer> timeZones){

        //Tiempo de prueba

        Date fechaPrueba = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), 
		Calendar.getInstance().get(Calendar.MONTH), 
		Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 15 , 0).getTime();

        PriorityQueue<Aeropuerto> closedList = new PriorityQueue<>();
        PriorityQueue<Aeropuerto> openList = new PriorityQueue<>();
    
        start.f = start.g + start.calculateHeuristic(start, target);
        openList.add(start);
    
        System.out.println("Solucion A*");
        System.out.println("==============================================");
        System.out.println("Origen: " + start.getCiudad().getNombre());
        System.out.println("Destino: " + target.getCiudad().getNombre());
        System.out.print("Duracion: ");

        while(!openList.isEmpty()){
            Aeropuerto n = openList.peek();
            if(n == target){
                return n;
            }
    
            for(Vuelo vuelo : n.getVuelos()){

                int resultadoComp = vuelo.getFechaPartida().compareTo(fechaPrueba);

                if (resultadoComp < 0) continue;

                //int totalWeight = n.g + (int)(Math.abs(vuelo.getFechaDestino().getTime() - vuelo.getFechaPartida().getTime())/60000);
    
                int totalWeight = n.g + obtenerTiempo(timeZones, vuelo);

                if(!openList.contains(vuelo.getAeropuertoDestino()) && !closedList.contains(vuelo.getAeropuertoDestino())){
                    //Prueba
                    vuelo.getAeropuertoDestino().comoLlegar = vuelo;
                    //Prueba
                    vuelo.getAeropuertoDestino().parent = n;
                    vuelo.getAeropuertoDestino().g = totalWeight;
                    vuelo.getAeropuertoDestino().f = vuelo.getAeropuertoDestino().g + vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
                    openList.add(vuelo.getAeropuertoDestino());
                } else {
                    if(totalWeight < vuelo.getAeropuertoDestino().g){
                        //Prueba
                        vuelo.getAeropuertoDestino().comoLlegar = vuelo;
                        //Prueba
                        vuelo.getAeropuertoDestino().parent = n;
                        vuelo.getAeropuertoDestino().g = totalWeight;
                        vuelo.getAeropuertoDestino().f = vuelo.getAeropuertoDestino().g + vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
    
                        if(closedList.contains(vuelo.getAeropuertoDestino())){
                            closedList.remove(vuelo.getAeropuertoDestino());
                            openList.add(vuelo.getAeropuertoDestino());
                        }
                    }
                }
            }
    
            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }
    public static void printPath(Aeropuerto target){
        Aeropuerto n = target;
        int minutos = 0;
        if(n==null)
            return;
    
        List<String> caminoAeropuertos = new ArrayList<>();
        List<String> caminoVuelos = new ArrayList<>();
    
        while(n.parent != null){
            caminoAeropuertos.add(n.getCiudad().getCodigo());
            caminoVuelos.add(n.comoLlegar.getCodigo() + 
            ": " + n.comoLlegar.getFechaPartida() + " - " + n.comoLlegar.getFechaDestino());
            //if(n.parent != null)minutos += n.parent.getAdjacentNodes().get(n);
            n = n.parent;
        }
        caminoAeropuertos.add(n.getCiudad().getCodigo());
        Collections.reverse(caminoAeropuertos);
        Collections.reverse(caminoVuelos);

        minAHora(target.g);
        System.out.println("==============================================");
        System.out.println("Ruta:");

        for(String id : caminoAeropuertos){
            System.out.println("    " + id);
        }

        System.out.println("==============================================");
        System.out.println("Ruta vuelos:");
        for(String id : caminoVuelos){
            System.out.println("    " + id);
        }

    }

    public static void minAHora(int min){
        int horas = min / 60;
        int minutos = min % 60;

        String minutosString;

		if(minutos < 10){
			minutosString = "0" + String.valueOf(minutos); 
		}else{
			minutosString = "" + minutos;
		}


        System.out.println(horas + ":" + minutosString);
    }
}