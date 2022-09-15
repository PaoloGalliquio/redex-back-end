package com.redexbackend.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node>{
  private Aeropuerto aeropuerto;

  private List<Node> shortestPath = new LinkedList<>();

  private Integer distance = Integer.MAX_VALUE;

  HashMap<Node, Integer> adjacentNodes = new HashMap<>();

  //AStar

  public int f = Integer.MAX_VALUE;
  public int g = Integer.MAX_VALUE;

  //public int h = Integer.MAX_VALUE;

  public int h = 0;

  public Node parent = null;

  @Override
  public int compareTo(Node n) {
      return Integer.compare(this.f, n.f);
  }

  public int calculateHeuristic(Node target){
    
    /*//Propuesta de heurística:
    //Calcular el tiempo de un hipotético vuelo directo entre el punto actual
    //y el destino considerando una velocidad fija para no afectar
    //las comparaciones
    
    final int R = 6371; // radio de la Tierra
    double lat1=4.710785964156131,lon1=-74.14538536583756,lat2=-12.021256252425848,lon2=-77.11144078946839,vprom=550;

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000 / 1000; // distancia en km
    double tiempo = distance/vprom; //horas
    System.out.println(tiempo);*/
    
    return this.h;
  }

  public void addDestination(Node destination, int distance) {
    adjacentNodes.put(destination, distance);
  }

  //region Constructores, Gettes y Setters
  public Node(Aeropuerto aeropuerto) {
    this.aeropuerto = aeropuerto;
  }

  public Aeropuerto getAeropuerto() {
    return this.aeropuerto;
  }

  public void setAeropuerto(Aeropuerto aeropuerto) {
    this.aeropuerto = aeropuerto;
  }

  public List<Node> getShortestPath() {
    return this.shortestPath;
  }

  public void setShortestPath(List<Node> shortestPath) {
    this.shortestPath = shortestPath;
  }

  public Integer getDistance() {
    return this.distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public HashMap<Node, Integer> getAdjacentNodes() {
    return this.adjacentNodes;
  }

  public void setAdjacentNodes(HashMap<Node, Integer> adjacentNodes) {
    this.adjacentNodes = adjacentNodes;
  }
  //endregion
}
