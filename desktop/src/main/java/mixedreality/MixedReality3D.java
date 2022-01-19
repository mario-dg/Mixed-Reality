package mixedreality;

import mixedreality.lab.exercise3.MyRendererScene;
import mixedreality.lab.solution.exercise3.MyRendererSceneSolution;
import ui.GenericCGApplication;

/**
 * This launcher is used to start the 3D application with a Java Swing user interface.
 */
public class MixedReality3D extends GenericCGApplication {

  public MixedReality3D() {
    super("Mixed Reality");

    // Change scene object here
    //setScene3D(new TransformationsScene());
    addScene2D(new MyRendererSceneSolution(600, 600));
    //setScene3D(new StereoScene());
    //setScene3D(new HexMapScene());
  }

  public static void main(String[] args) {
    new MixedReality3D();
  }
}
