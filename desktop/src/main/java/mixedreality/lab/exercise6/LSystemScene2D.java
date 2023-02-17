/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6;

import com.jme3.math.Vector2f;
import ui.Scene2D;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of an L-System
 */
public class LSystemScene2D extends Scene2D {

    /**
     * The axiom is a single character
     */
    protected Character axiom;
    /**
     * All rules are in a map which maps a character to its replacement.
     */
    protected Map<Character, String> rules;
    /**
     * Number of iterations during derivation
     */
    protected int numIterations;

    /**
     * Result of the last derivation.
     */
    protected String currentWord;

    public LSystemScene2D(int width, int height) {
        super(width, height, new Vector2f(-1, -1), new Vector2f(1, 1));
        this.axiom = 'F';
        this.rules = new HashMap<>();
        this.rules.put('F', "F+F--F+F");
        this.rules.put('-', "-");
        this.rules.put('+', "+");
        this.numIterations = 3;
        this.currentWord = "";

        // Run derivation
        derive();
        // Debugging: show derived word.
        System.out.println(currentWord);
    }

    /**
     * Derive the axiom for the given number of iterations. The result of the
     * derivation must be saved in the variable currentWord.
     */
    protected void derive() {
        // Your task
        currentWord = "";
    }

    @Override
    public void paint(Graphics g) {
        // Your task
    }

    @Override
    public String getTitle() {
        return "L-System";
    }
}
