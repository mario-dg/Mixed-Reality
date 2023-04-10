package mixedreality.base.math;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import math.MathF;

public class Utils {
    public static int factorial(int n) {
        if (n == 0)
            return 1;
        else
            return (n * factorial(n - 1));
    }

    public static int binomial(int n, int i) {
        if (i < 0 || i > n) return 0;

        return factorial(n) / (factorial(i) * factorial(n - i));
    }

    public static Matrix3f translationMatrix2D(Vector2f translation) {
        return new Matrix3f(1, 0, translation.x,
                0, 1, translation.y,
                0, 0, 1);
    }

    public static Matrix3f rotationMatrix2D(float angle) {
        return new Matrix3f(MathF.cos(angle), -MathF.sin(angle), 0,
                            MathF.sin(angle), MathF.cos(angle), 0,
                            0, 0, 1);
    }
}
