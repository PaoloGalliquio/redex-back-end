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
