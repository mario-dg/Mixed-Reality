/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package mixedreality.lab.exercise6.shortestPath;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementierung des Interface IGraph mit einer Adjazenzliste.
 *
 * @author Philipp Jenke
 */
public class Graph<T> {

  /**
   * Liste der Knoten im Graphen
   */
  private final List<Node<T>> knoten = new ArrayList<Node<T>>();

  /**
   * Liste der Kanten-Listen. Es gibt so viele Kanten-Listen, wie es Knoten
   * gibt. Jede Kanten-Liste ist wieder eine Liste in dem Fall von Kanten, die
   * von dem ensprechenden Knoten ausgehen.
   */
  private final List<List<Kante>> kanten = new ArrayList<List<Kante>>();

  /**
   * Konstruktor.
   */
  public Graph() {
  }

  public void addKnoten(Node<T> node) {
    knoten.add(node);
  }

  public void addKante(Node<T> start, Node<T> ende, double wert) {

    int firstNodeIndex = knoten.indexOf(start);
    int secondNodeIndex = knoten.indexOf(ende);

    if (firstNodeIndex < 0 || secondNodeIndex < 0) {
      throw new IllegalArgumentException("Invalid edge index");
    }

    while (kanten.size() <= firstNodeIndex
            || kanten.size() <= secondNodeIndex) {
      kanten.add(new ArrayList<Kante>());
    }

    kanten.get(firstNodeIndex).add(new Kante(secondNodeIndex, wert));
    kanten.get(secondNodeIndex).add(new Kante(firstNodeIndex, wert));
  }

  public int getAnzahlKnoten() {
    return knoten.size();
  }

  public int getAnzahlKanten() {
    int anzahlKanten = 0;
    for (List<Kante> kantenListe : kanten) {
      anzahlKanten += kantenListe.size();
    }
    // Jeder Kante kommt doppelt vor, daher /2.
    return anzahlKanten / 2;
  }

  public Node<T> getKnoten(int nodeIndex) {
    return knoten.get(nodeIndex);
  }

  public List<Node<T>> getNachbarn(Node<T> node) {
    List<Node<T>> neighbors = new ArrayList<Node<T>>();
    int index = knoten.indexOf(node);
    if (index < 0) {
      return neighbors;
    }
    List<Integer> n = getNachbarIndices(index);
    for (Integer i : n) {
      neighbors.add(knoten.get(i));
    }
    return neighbors;
  }

  public List<Integer> getNachbarIndices(int nodeIndex) {
    List<Integer> neighbors = new ArrayList<Integer>();
    if (nodeIndex < 0 || nodeIndex >= kanten.size()) {
      return neighbors;
    }
    for (Kante edge : kanten.get(nodeIndex)) {
      neighbors.add(edge.getZielKnotenIndex());
    }
    return neighbors;
  }

  public double getKantenGewicht(int startIndex, int zielIndex) {
    for (Kante edge : kanten.get(startIndex)) {
      if (zielIndex == edge.getZielKnotenIndex()) {
        return edge.getWert();
      }
    }
    return Double.NEGATIVE_INFINITY;
  }

  public double getKantenGewicht(Node<T> startKnoten, Node<T> zielKnoten) {
    return getKantenGewicht(knoten.indexOf(startKnoten),
            knoten.indexOf(zielKnoten));
  }

  public void setzeGewicht(Node<T> startKnoten,
                           Node<T> zielKnoten, double gewicht) {

    int indexStartKnoten = knoten.indexOf(startKnoten);
    int indexZielKnoten = knoten.indexOf(zielKnoten);

    Kante kanteStartZiel = getKante(indexStartKnoten, indexZielKnoten);
    if (kanteStartZiel != null) {
      kanteStartZiel.setGewicht(gewicht);
    }
    Kante kanteZielStart = getKante(indexZielKnoten, indexStartKnoten);
    if (kanteZielStart != null) {
      kanteZielStart.setGewicht(gewicht);
    }
  }

  /**
   * Liefert die Kante zwischen dem Start- und Zielknotenindex. Liefert null,
   * wenn die Kante nicht gefunden wurde.
   */
  private Kante getKante(int indexStartKnoten, int indexZielKnoten) {

    if (indexStartKnoten < 0 || indexStartKnoten >= kanten.size()
            || indexZielKnoten < 0 || indexZielKnoten >= kanten.size()) {
      return null;
    }

    for (Kante kante : kanten.get(indexStartKnoten)) {
      if (kante.getZielKnotenIndex() == indexZielKnoten) {
        return kante;
      }
    }
    return null;
  }

  public boolean kanteExistiert(Node<T> startKnoten, Node<T> endKnoten) {
    int startIndex = knoten.indexOf(startKnoten);
    int endIndex = knoten.indexOf(endKnoten);
    return getKante(startIndex, endIndex) != null;
  }

  public void removedNode(Node<T> node) {
    int nodeIndex = knoten.indexOf(node);
    knoten.remove(node);
    kanten.get(nodeIndex).clear();
    for (int i = 0; i < kanten.size(); i++) {
      for (int j = 0; j < kanten.get(i).size(); j++) {
        Kante kante = kanten.get(i).get(j);
        if (kante.zielKnoten == nodeIndex) {
          kanten.get(i).remove(j);
          j--;
        }
      }
    }
  }

  public void removeEdge(Node<T> a, Node<T> b) {
    int aIndex = knoten.indexOf(a);
    int bIndex = knoten.indexOf(b);

    // Remove a -> b
    for (int i = 0; i < kanten.get(aIndex).size(); i++) {
      Kante edge = kanten.get(aIndex).get(i);
      if (edge.zielKnoten == bIndex) {
        kanten.get(aIndex).remove(i);
        i--;
      }
    }

    // Remove b -> a
    for (int i = 0; i < kanten.get(bIndex).size(); i++) {
      Kante edge = kanten.get(bIndex).get(i);
      if (edge.zielKnoten == aIndex) {
        kanten.get(bIndex).remove(i);
        i--;
      }
    }


  }

  @Override
  public String toString() {
    return "Graph (AdjazenzListe), #Knoten: " + getAnzahlKnoten()
            + ", #Kanten: " + getAnzahlKanten();
  }

  /**
   * Return the node containing the given element. Return null if the element is not in the
   * graph.
   */
  public Node<T> getKnoten(T element) {
    for (Node<T> node : knoten) {
      if (node.getElement() == element) {
        return node;
      }
    }
    return null;
  }

  /**
   * Innere Klasse, um eine Kante zu repräsentieren.
   *
   * @author Philipp Jenke
   */
  private class Kante {
    /**
     * Wert der Kante
     */
    private double wert = Double.POSITIVE_INFINITY;

    /**
     * Index des anderen Endes der Kante.
     */
    private int zielKnoten = -1;

    /**
     * Konstruktor.
     */
    public Kante(int zielKnoten, double wert) {
      this.zielKnoten = zielKnoten;
      this.wert = wert;
    }

    /**
     * Getter.
     */
    public double getWert() {
      return wert;
    }

    /**
     * Getter.
     */
    public int getZielKnotenIndex() {
      return zielKnoten;
    }

    /**
     * Setter.
     */
    public void setGewicht(double gewicht) {
      this.wert = gewicht;
    }
  }
}
