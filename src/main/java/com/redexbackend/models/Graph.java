package com.redexbackend.models;

import java.util.HashMap;

public class Graph {
  private HashMap<String, Node> nodes = new HashMap<>();

  public void addNode(String key, Node value) {
    nodes.put(key, value);
  }

  public HashMap<String,Node> getNodes() {
    return this.nodes;
  }

  public void setNodes(HashMap<String,Node> nodes) {
    this.nodes = nodes;
  }
}
