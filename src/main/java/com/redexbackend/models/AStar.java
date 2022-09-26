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
    public static Aeropuerto aStar(Aeropuerto start, Aeropuerto target){

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

                int totalWeight = n.g + (int)(Math.abs(vuelo.getFechaDestino().getTime() - vuelo.getFechaPartida().getTime())/60000);
    
                if(!openList.contains(vuelo.getAeropuertoDestino()) && !closedList.contains(vuelo.getAeropuertoDestino())){
                    vuelo.getAeropuertoDestino().parent = n;
                    vuelo.getAeropuertoDestino().g = totalWeight;
                    vuelo.getAeropuertoDestino().f = vuelo.getAeropuertoDestino().g + vuelo.getAeropuertoDestino().calculateHeuristic(vuelo.getAeropuertoDestino(), target);
                    openList.add(vuelo.getAeropuertoDestino());
                } else {
                    if(totalWeight < vuelo.getAeropuertoDestino().g){
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
    
        List<String> ids = new ArrayList<>();
    
        while(n.parent != null){
            ids.add(n.getCiudad().getCodigo());
            //if(n.parent != null)minutos += n.parent.getAdjacentNodes().get(n);
            n = n.parent;
        }
        ids.add(n.getCiudad().getCodigo());
        Collections.reverse(ids);

        minAHora(target.g);
        System.out.println("==============================================");
        System.out.println("Ruta:");

        for(String id : ids){
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