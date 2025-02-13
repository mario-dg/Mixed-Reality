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
import java.util.Stack;

/**
 * Implementation of an L-System
 */
public class FractalPlant2D extends Scene2D {

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

    public FractalPlant2D(int width, int height) {
        super(width, height, new Vector2f(-1, -1), new Vector2f(1, 1));
        this.axiom = 'X';
        this.rules = new HashMap<>();
        this.rules.put('F', "FF");
        this.rules.put('X', "F+[[X]-X]-F[-FX]+X");
        this.rules.put('-', "-");
        this.rules.put('+', "+");
        this.rules.put('[', "[");
        this.rules.put(']', "]");
        this.numIterations = 6;
        this.currentWord = "";

        // Run derivation
        derive();
        // Debugging: show derived word.
        //System.out.println(currentWord);
    }

    /**
     * Derive the axiom for the given number of iterations. The result of the
     * derivation must be saved in the variable currentWord.
     */
    protected void derive() {
        // Your task
        this.currentWord = "" + this.axiom;
        for (int i = 0; i < this.numIterations; i++) {
            String newWord = "";
            for (String symbolString : this.currentWord.split("")) {
                char symbol = symbolString.charAt(0);
                newWord += this.rules.getOrDefault(symbol, symbolString);
            }
            this.currentWord = newWord;
        }
    }

    private Turtle turtle = new Turtle(0.01f, 25f, new Vector2f(0f, -1f), 90);
    private Stack<Turtle> stack = new Stack<>();

    @Override
    public void paint(Graphics g) {
        g.drawRect(0, 0, getWidth(), getHeight());
        // Your task
        for (String symbol : this.currentWord.split("")) {
            switch (symbol) {
                case "F" -> {
                    Vector2f oldPos = turtle.position.clone();
                    Vector2f newPos = turtle.step().position.clone();
                    drawLine(g, oldPos, newPos, Color.RED);
                }
                case "+" -> {turtle.rotate(1);}
                case "-" -> {turtle.rotate(-1);}
                case "[" -> {stack.add(turtle.clone());}
                case "]" -> {this.turtle = stack.pop();}
                default -> {}
            }
        }
    }

    @Override
    public String getTitle() {
        return "Fractal Plant";
    }
}
