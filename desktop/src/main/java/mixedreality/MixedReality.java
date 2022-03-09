package mixedreality;

import com.jme3.math.Vector2f;
import mixedreality.base.math.Curve;
import mixedreality.lab.exercise1.BasisFunctionDummy;
import mixedreality.lab.exercise1.CurveScene2D;
import ui.GenericCGApplication;

/**
 * This launcher is used to start the 3D application with a Java Swing user interface.
 */
public class MixedReality extends GenericCGApplication {

  public MixedReality() {
    super("Mixed Reality");

    // Assignment 1
    Curve curve = new Curve(new BasisFunctionDummy());
    curve.addControlPoint(new Vector2f(0, 0));
    curve.addControlPoint(new Vector2f(0.5f, 0.5f));
    curve.addControlPoint(new Vector2f(1, 0));
    addScene2D(new CurveScene2D(600, 600, curve));

    // Asssignment 2
    //setScene3D(new TransformationsScene());

    // Assignment 3
    //addScene2D(new MyRendererScene(600, 600));

    // Assignment 4
    //addScene2D(new SimplificationScene(600, 600));

    // Assignment 5
    // addScene3D(new StereoScene());

    // Assignment 6
    //addScene3D(new HexMapScene());

    // Assignment 7
    //addScene3D(new VolumeDataScene());
  }

  public static void main(String[] args) {
    new MixedReality();
  }
}
