package com.redexbackend.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Dijkstra {

  public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
    source.setDistance(0);

    Set<Node> settledNodes = new HashSet<>();
    Set<Node> unsettledNodes = new HashSet<>();

    unsettledNodes.add(source);

    while (unsettledNodes.size() != 0) {
      Node currentNode = getLowestDistanceNode(unsettledNodes);
      unsettledNodes.remove(currentNode);
      for (HashMap.Entry<Node, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
        Node adjacentNode = adjacencyPair.getKey();
        if (!settledNodes.contains(adjacentNode)) {
          calculateMinimumDistance(adjacentNode, currentNode);
          unsettledNodes.add(adjacentNode);
        }
      }
      settledNodes.add(currentNode);
    }
    return graph;
  }

  private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
    Node lowestDistanceNode = null;
    int lowestDistance = Integer.MAX_VALUE;
    for (Node node : unsettledNodes) {
      int nodeDistance = node.getDistance();
      if (nodeDistance < lowestDistance) {
        lowestDistance = nodeDistance;
        lowestDistanceNode = node;
      }
    }
    return lowestDistanceNode;
  }

  private static void calculateMinimumDistance(Node evaluationNode, Node sourceNode) {
    Integer sourceDistance = sourceNode.getDistance();
    Vuelo vueloMasCorto = new Vuelo();
    vueloMasCorto.setDuracion(Integer.MAX_VALUE);
    for (Vuelo v : evaluationNode.getAeropuerto().getVuelos())
      if (v.getDuracion() < vueloMasCorto.getDuracion())
        vueloMasCorto = new Vuelo(v);
    if (sourceDistance + vueloMasCorto.getDuracion() < evaluationNode.getDistance()) {
      evaluationNode.setDistance(sourceDistance + vueloMasCorto.getDuracion());
      LinkedList<Vuelo> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
      shortestPath.add(vueloMasCorto);
      evaluationNode.setShortestPath(shortestPath);
    }
  }
}
