package mixedreality.lab.exercise6.shortestPath;

/**
 * Knoten in einem Graphen. Ein Knoten beinhaltet
 * 
 * @author Philipp Jenke
 *
 */
public class Node<T> {

	/**
	 * Wert des Knotens.
	 */
	private T element;

	/**
	 * Debugging index - Ebene bei einer Suchstrategie
	 */
	private int suchEbene = -1;

	/**
	 * Konstruktor
	 */
	public Node(T element) {
		this.element = element;
	}

	/**
	 * Getter.
	 */
	public T getElement() {
		return element;
	}

	@Override
	public String toString() {
		return element.toString();
	}

	@Override
	public boolean equals(Object anderesObjekt) {
		if (!(anderesObjekt instanceof Node)) {
			return false;
		}
		Node<?> andererKnoten = (Node<?>) anderesObjekt;
		return getElement().equals(andererKnoten.getElement());
	}

	public int getSuchEbene() {
		return suchEbene;
	}

	public void setSuchEbene(int suchEbene) {
		this.suchEbene = suchEbene;
	}
}
