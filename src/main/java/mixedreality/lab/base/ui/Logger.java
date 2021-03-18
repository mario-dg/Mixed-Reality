/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 *
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.base.ui;

/**
 * Generic logger for debugging messages. Implemented as singleton.
 */
public class Logger {

  /**
   * Singleton instance.
   */
  private static Logger instance = null;

  private Logger() {
  }

  public static Logger getInstance() {
    if (instance == null) {
      instance = new Logger();
    }
    return instance;
  }

  /**
   * Log message.
   */
  public void msg(String msg) {
    System.out.println(msg);
  }

  /**
   * Log error.
   */
  public void error(String msg) {
    System.err.println(msg);
  }

  /**
   * Log debug information.
   */
  public void debug(String s) {
    // Debug messages are currently ignored
  }
}
