package mixedreality.lab.exercise2;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import math.MathF;
import mixedreality.base.math.Utils;
import mixedreality.lab.exercise1.BezierBasisFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.jemalloc.ExtentPurgeI;

import static org.junit.jupiter.api.Assertions.*;

class AvatarTest {

    private static final float EPSILON = 1e-6f;

    //private Avatar avatar;

    /*@BeforeEach
    public void setUp() {
        avatar = new Avatar(new Vector2f(3, 5), 0);
    }*/
    @Test
    void makePose0() {
        var result = new Avatar(new Vector2f(3, 5), 0).makePose();
        var correct = new Matrix3f(1, 0, 3,
                                   0, 1, 5,
                                   0, 0, 1);
        assertTrue(areEqual(correct, result, EPSILON));
    }

    @Test
    void makePose1(){
        Matrix3f result = new Avatar(new Vector2f(0, 5), 0).makePose();
        Matrix3f correct = new Matrix3f(1, 0, 0,
                                        0, 1, 5,
                                        0, 0, 1);
        assertTrue(areEqual(correct, result, EPSILON));
    }

    @Test
    void makePose2(){
        int rot = 90;
        Matrix3f result = new Avatar(new Vector2f(0, 5), rot).makePose();
        Matrix3f correct = new Matrix3f(MathF.cos(rot), -MathF.sin(rot), 0,
                                        MathF.sin(rot), MathF.cos(rot), 5,
                                        0, 0, 1);
        assertTrue(areEqual(correct, result, EPSILON));
    }

    @Test
    void moveToTarget0(){
        Avatar a1 = new Avatar(new Vector2f(0, 0), 0);
        a1.setTargetPos(new Vector2f(-1, 1));
        a1.moveToTargetPos();
        Avatar a2 = new Avatar(new Vector2f(0, 0), Avatar.ROTATION_VELOCITY);
        assertEquals(a1.pos.x, a2.pos.x, EPSILON);
        assertEquals(a1.pos.y, a2.pos.y, EPSILON);
        assertEquals(a1.rotationAngle, a2.rotationAngle, EPSILON);
    }

    @Test
    void moveToTarget1(){
        Avatar a1 = new Avatar(new Vector2f(0, 0), 0);
        a1.setTargetPos(new Vector2f(1, 0));
        a1.moveToTargetPos();
        Avatar a2 = new Avatar(new Vector2f(Avatar.MOVE_VELOCITY, 0), 0);
        assertEquals(a1.pos.x, a2.pos.x, EPSILON);
        assertEquals(a1.pos.y, a2.pos.y, EPSILON);
        assertEquals(a1.rotationAngle, a2.rotationAngle, EPSILON);
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