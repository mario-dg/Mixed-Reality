package mixedreality;

import mixedreality.lab.exercise2.Assignment2Scene2D;
import mixedreality.lab.solution.exercise2.Assignment2Scene2DSolution;
import ui.CG2DApplication;

public class MixedReality2D extends CG2DApplication {
    public MixedReality2D() {
        super("Mixed Reality");

        // Assignment 1
//        Curve curve = new Curve(new BasisFunctionDummy());
//        curve.addControlPoint(new Vector2f(0, 0));
//        curve.addControlPoint(new Vector2f(0.5f, 0.5f));
//        curve.addControlPoint(new Vector2f(1, 0));
//        addScene2D(new CurveScene2D(600, 600, curve));

        // Assignment 2
        addScene2D(new Assignment2Scene2D(800, 600));
        addScene2D(new Assignment2Scene2DSolution(800, 600));

        // Assignment 3
        //addScene2D(new MyRendererScene(600, 600));

        // Assignment 4
        //addScene2D(new SimplificationScene(600, 600));
    }

    public static void main(String[] args) {
        new MixedReality2D();
    }
}
