package mixedreality.lab.exercise4;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import mixedreality.base.mesh.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuadricErrorMetricsSimplification2DTest {

    private static final float EPSILON = 1e-6f;

    private void assertMatrixEquals(Matrix3f expected, Matrix3f actual, float epsilon) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(expected.get(i, j), actual.get(i, j), epsilon, "The computed distance matrix should match the expected matrix");
            }
        }
    }

    @Test
    void testComputeDistanceMatrix_case1() {
        var instance = new QuadricErrorMetricsSimplification2D(new Polygon());

        var startVertex = new PolygonVertex(new Vector2f(0, 0));
        var endVertex = new PolygonVertex(new Vector2f(1, 1));
        PolygonEdge edge = new PolygonEdge(startVertex, endVertex);

        var expectedMatrix = new Matrix3f(
                0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f,
                0f, 0f, 0f
        );

        Matrix3f actualMatrix = instance.computeDistanceMatrix(edge);
        assertMatrixEquals(expectedMatrix, actualMatrix, EPSILON);
    }

    @Test
    void testComputeDistanceMatrix_case2() {
        var instance = new QuadricErrorMetricsSimplification2D(new Polygon());

        var startVertex = new PolygonVertex(new Vector2f(-1, 2));
        var endVertex = new PolygonVertex(new Vector2f(0, 4));
        PolygonEdge edge = new PolygonEdge(startVertex, endVertex);

        Matrix3f expectedMatrix = new Matrix3f(
                0.8f, -0.4f, 1.6f,
                -0.4f, 0.2f, -0.8f,
                1.6f, -0.8f, 3.2f
        );

        Matrix3f actualMatrix = instance.computeDistanceMatrix(edge);

        assertMatrixEquals(expectedMatrix, actualMatrix, EPSILON);
    }

    @Test
    void testComputePointQem_case1() {
        var instance = new QuadricErrorMetricsSimplification2D(new Polygon());

        var v1 = new PolygonVertex(new Vector2f(0, 0));
        var v2 = new PolygonVertex(new Vector2f(1, 1));
        var v3 = new PolygonVertex(new Vector2f(2, 0));

        PolygonEdge edge1 = new PolygonEdge(v1, v2);
        PolygonEdge edge3 = new PolygonEdge(v3, v1);

        v1.setIncomingEdge(edge3);
        v1.setOutgoingEdge(edge1);

        Matrix3f expectedMatrix = new Matrix3f(
                0.5f, -0.5f, 0.0f,
                -0.5f, 1.5f, 0f,
                0f, 0f, 0f
        );

        Matrix3f actualMatrix = instance.computePointQem(v1);
        System.out.println(instance.computePointQem(v1));
        assertMatrixEquals(expectedMatrix, actualMatrix, EPSILON);
    }
}