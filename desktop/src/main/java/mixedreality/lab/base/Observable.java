package mixedreality.lab.base;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple implementation of something observable
 */
public class Observable {

  /**
   * Set of all observers
   */
  private final Set<Observer> observers = new HashSet<>();

  /**
   * Register an additional observer
   */
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  /**
   * Send a debug message to all registered observers
   *
   * @param descr Identifier of the update reason
   * @param payload (Optional) Content of the update event.
   */
  public void notifyAllObservers(String descr, Object payload) {
    observers.forEach(o -> o.update(this, descr, payload));
  }
}
