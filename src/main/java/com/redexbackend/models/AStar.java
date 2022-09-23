package com.redexbackend.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar{
    public static Node aStar(Node start, Node target){
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();
    
        start.f = start.g + start.calculateHeuristic(start, target);
        openList.add(start);
    
        System.out.println("Solucion A*");
        System.out.println("==============================================");
        System.out.println("Origen: " + start.getAeropuerto().getCiudad().getNombre());
        System.out.println("Destino: " + target.getAeropuerto().getCiudad().getNombre());
        System.out.print("Duracion: ");

        while(!openList.isEmpty()){
            Node n = openList.peek();
            if(n == target){
                return n;
            }
    
            for(Vuelo vuelo : n.getAeropuerto().getVuelos()){

                System.out.print(vuelo.getAeropuertoDestino().getCodigo() + " - ");
                System.out.println(vuelo.getCapacidad());

                //Agregar la hora actual

                //Transformar las horas de destino y de llegada a minutos

                //Date fecha = vuelo.getFechaDestino() - vuelo.getFechaPartida();

                //System.out.println("Hora llegada: " + vuelo.getFechaDestino());
                //System.out.println("Hora salida: " + vuelo.getFechaPartida());

                /* 
                int totalWeight = n.g;
    
                if(!openList.contains(m) && !closedList.contains(m)){
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(m, target);
                    openList.add(m);
                } else {
                    if(totalWeight < m.g){
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(m, target);
    
                        if(closedList.contains(m)){
                            closedList.remove(m);
                            openList.add(m);
                        }
                    }
                }

                */
            }
    
            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }

    public static void printPath(Node target){
        Node n = target, aux;
        int minutos = 0;
        if(n==null)
            return;
    
        List<String> ids = new ArrayList<>();
    
        while(n.parent != null){
            ids.add(n.getAeropuerto().getCiudad().getCodigo());
            if(n.parent != null)minutos += n.parent.getAdjacentNodes().get(n);
            n = n.parent;
        }
        ids.add(n.getAeropuerto().getCiudad().getCodigo());
        Collections.reverse(ids);

        minAHora(minutos);
        System.out.println("==============================================");
        System.out.println("Ruta:");

        for(String id : ids){
            System.out.println("    " + id);
        }
    }

    public static void minAHora(int min){
        int horas = min / 60;
        int minutos = min % 60;
        System.out.println(horas + ":" + minutos);
    }
}