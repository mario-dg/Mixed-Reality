package mixedreality.lab.exercise6.shortestPath;

import java.util.*;

/**
 * Computes the shortest path from a start node to a target node in a graph using Dijkstras algorithm.
 * <p>
 * Usage: setup(), compute(), get
 */
public class ShortestPathDijkstra<T> {

  protected Graph<T> graph;
  protected Node<T> startNode;
  protected Node<T> destinationNode;

  protected PriorityQueue<Node<T>> Q;
  protected Map<Node<T>, Node<T>> preds;
  protected Set<Node<T>> marked;
  protected Map<Node<T>, Float> cost;

  /**
   * Call this method first to initialize a search.
   */
  public void setup(Graph<T> graph, Node<T> startNode, Node<T> destinationNode) {
    this.graph = graph;
    this.startNode = startNode;
    this.destinationNode = destinationNode;
    cost = new HashMap<>();
    preds = new HashMap<>();
    marked = new HashSet<>();
    Q = new PriorityQueue<>((k1, k2) -> cost.get(k1) < cost.get(k2) ? -1 : 1);

    // Initialisierung
    for (int i = 0; i < graph.getAnzahlKnoten(); i++) {
      Node<T> knoten = graph.getKnoten(i);
      cost.put(knoten, Float.POSITIVE_INFINITY);
      preds.put(knoten, null);
      marked.clear();
    }

    // Startknoten
    cost.put(startNode, 0.0f);
    preds.put(startNode, null);
    Q.add(startNode);
  }

  /**
   * Compute the shortest path.
   */
  public void compute() {
    while (!Q.isEmpty()) {
      step();
    }
  }

  /**
   * Retreive the computation result.
   */
  public List<Node<T>> getPath() {
    List<Node<T>> path = new ArrayList<>();
    Node<T> node = destinationNode;
    while (node != null) {
      path.add(node);

      node = preds.get(node);
    }
    Collections.reverse(path);
    return path;
  }

  /**
   * Apply one step in the algorithm
   */
  private void step() {
    Node<T> aktueller = null;
    if (Q.isEmpty() || ((aktueller = Q.poll()) == null)) {
      return;
    }

    if (aktueller == destinationNode) {
      System.out.println("Target found.");
      Q.clear();
      return;
    }

    marked.add(aktueller);

    // Fuege Nachbarn hinzu
    List<Node<T>> neighbors = graph.getNachbarn(aktueller);
    for (Node<T> nachbar : neighbors) {
      // Prüfe, ob nachbar bereits markiert ist, fall nein: fuege in rand ein
      if (!marked.contains(nachbar)) {
        float t = cost.get(aktueller) + (float) graph.getKantenGewicht(aktueller, nachbar);
        if (cost.get(nachbar) > t) {
          preds.put(nachbar, aktueller);
          cost.put(nachbar, t);
        }
        Q.remove(nachbar);
        Q.add(nachbar);
      }
    }
  }
}
