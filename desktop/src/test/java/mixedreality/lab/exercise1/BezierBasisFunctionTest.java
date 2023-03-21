package mixedreality.lab.exercise1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BezierBasisFunctionTest {

    private static final float EPSILON = 0.0001f;
    private BezierBasisFunction bezierBasisFunction;

    @BeforeEach
    public void setUp() {
        bezierBasisFunction = new BezierBasisFunction();
    }

    @Test
    public void testEval() {
        assertEquals(1.0f, bezierBasisFunction.eval(0, 0, 3), EPSILON);
        assertEquals(0.375f, bezierBasisFunction.eval(0.5f, 1, 3), EPSILON);
        assertEquals(1.0f, bezierBasisFunction.eval(1, 3, 3), EPSILON);
        assertEquals(0.2646, bezierBasisFunction.eval(0.3f, 2, 4), EPSILON);
    }

    @Test
    public void testEvalDerivative() {
        assertEquals(-3.0f, bezierBasisFunction.evalDerivative(0, 0, 3), EPSILON);
        assertEquals(-0.75, bezierBasisFunction.evalDerivative(0.5f, 1, 3), EPSILON);
        assertEquals(3.0f, bezierBasisFunction.evalDerivative(1, 3, 3), EPSILON);
        assertEquals(1.008, bezierBasisFunction.evalDerivative(0.3f, 2, 4), EPSILON);
    }
}