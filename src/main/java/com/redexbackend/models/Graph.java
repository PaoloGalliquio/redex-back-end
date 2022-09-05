package com.redexbackend.models;

import java.util.HashSet;
import java.util.Set;

public class Graph {
  private Set<Node> nodes = new HashSet<>();

  public void addNode(Node nodeA) {
    nodes.add(nodeA);
  }

  //region Constructores, Gettes y Setters
  public Set<Node> getNodes() {
    return this.nodes;
  }

  public void setNodes(Set<Node> nodes) {
    this.nodes = nodes;
  }
  //endregion
}
