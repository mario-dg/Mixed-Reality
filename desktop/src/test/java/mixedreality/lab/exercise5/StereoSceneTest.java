package mixedreality.lab.exercise5;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import mixedreality.base.mesh.Vertex;
import mixedreality.lab.exercise3.Camera;
import mixedreality.lab.exercise4.Polygon;
import mixedreality.lab.exercise4.PolygonEdge;
import mixedreality.lab.exercise4.PolygonVertex;
import mixedreality.lab.exercise4.QuadricErrorMetricsSimplification2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StereoSceneTest {

    private static final float EPSILON = 1e-3f;
    private static final StereoScene SCENE = new StereoScene();
    private void assertMatrixEquals(Matrix3f expected, Matrix3f actual, float epsilon) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected.get(i, j), actual.get(i, j), epsilon, "The computed distance matrix should match the expected matrix");
            }
        }
    }

    private void assertVectorEquals(Vector2f expected, Vector2f actual, float eps) {
        assertEquals(expected.x, actual.x, eps);
        assertEquals(expected.y, actual.y, eps);
    }

    private void assertVector3DEquals(Vector3f expected, Vector3f actual, float eps) {
        assertEquals(expected.x, actual.x, eps);
        assertEquals(expected.y, actual.y, eps);
        assertEquals(expected.z, actual.z, eps);
    }

    @Test
    void testPointToScreen() {
        Vector3f v1 = SCENE.toWorldCoordinateSystem(SCENE.leftScreenCoords, SCENE.leftCamera);
        Vector3f v2 = SCENE.toWorldCoordinateSystem(SCENE.rightScreenCoords, SCENE.rightCamera);
        assertVectorEquals(SCENE.leftScreenCoords, SCENE.positionToScreen(v1, SCENE.leftCamera), EPSILON);
        assertVectorEquals(SCENE.rightScreenCoords, SCENE.positionToScreen(v2, SCENE.rightCamera), EPSILON);
    }

    @Test
    void testError() {
        Camera cam = SCENE.leftCamera;
        Vector2f m = SCENE.leftScreenCoords;
        Vector3f t = SCENE.toWorldCoordinateSystem(m, cam);
        assertEquals(SCENE.error(List.of(m), t, List.of(cam)), 0, EPSILON);
        cam = SCENE.rightCamera;
        m = SCENE.rightScreenCoords;
        t = SCENE.toWorldCoordinateSystem(m, cam);
        assertEquals(SCENE.error(List.of(m), t, List.of(cam)), 0, EPSILON);
    }

    @Test
    void testGradient() {
        float h = Float.MIN_VALUE;
        Camera cam = SCENE.leftCamera;
        Vector2f m = SCENE.leftScreenCoords;
        Vector3f t = SCENE.toWorldCoordinateSystem(m, cam);
        assertVector3DEquals(SCENE.gradient(List.of(m), t, h, List.of(cam)), Vector3f.ZERO, EPSILON);
        cam = SCENE.rightCamera;
        m = SCENE.rightScreenCoords;
        t = SCENE.toWorldCoordinateSystem(m, cam);
        assertVector3DEquals(SCENE.gradient(List.of(m), t, h, List.of(cam)), Vector3f.ZERO, EPSILON);
    }

}