package mixedreality;

import com.jme3.math.Vector2f;
import mixedreality.base.math.Curve;
import mixedreality.lab.exercise1.BasisFunctionDummy;
import mixedreality.lab.exercise1.CurveScene2D;
import mixedreality.lab.exercise3.MyRendererScene;
import mixedreality.lab.exercise4.SimplificationScene;
import ui.CG2DApplication;

public class MixedReality2D extends CG2DApplication {
    public MixedReality2D() {
        super("Mixed Reality");

        // Assignment 1
        Curve curve = new Curve(new BasisFunctionDummy());
        curve.addControlPoint(new Vector2f(0, 0));
        curve.addControlPoint(new Vector2f(0.5f, 0.5f));
        curve.addControlPoint(new Vector2f(1, 0));
        addScene2D(new CurveScene2D(600, 600, curve));

        // Assignment 3
        //addScene2D(new MyRendererScene(600, 600));

        // Assignment 4
        //addScene2D(new SimplificationScene(600, 600));
    }

    public static void main(String[] args) {
        new MixedReality2D();
    }
}
