package mixedreality.lab.exercise2;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import mixedreality.base.math.Utils;
import mixedreality.lab.exercise1.BezierBasisFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.jemalloc.ExtentPurgeI;

import static org.junit.jupiter.api.Assertions.*;

class AvatarTest {

    private static final float EPSILON = 1e-6f;

    private Avatar avatar;

    @BeforeEach
    public void setUp() {
        avatar = new Avatar(new Vector2f(3, 5), 90);
    }
    @Test
    void makePose() {
        var result = avatar.makePose();

        var correct = new Matrix3f(0, -1, -5, 1, 0, 3, 0, 0, 1);
        assertTrue(areEqual(correct, result, EPSILON));
    }

    private static boolean areEqual(Matrix3f a, Matrix3f b, float epsilon){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Math.abs(a.get(i, j) - b.get(i, j)) > epsilon) {
                    return false;
                }
            }
        }
        return true;
    }
}