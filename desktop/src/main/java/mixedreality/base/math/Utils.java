package mixedreality.base.math;

import com.jme3.math.*;
import math.MathF;
import mixedreality.lab.exercise3.Camera;

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

    public static Matrix3f scaleMatrix2D(Vector2f scale) {
        return new Matrix3f(scale.x, 0, 0,
                0, scale.y, 0,
                0, 0, 1);
    }

    public static Matrix3f translationMatrix2D(Vector2f translation) {
        return new Matrix3f(1, 0, translation.x,
                0, 1, translation.y,
                0, 0, 1);
    }

    public static Matrix4f rotationMatrix3D(Vector3f angles) {
        var a = angles.x;
        var b = angles.y;
        var c = angles.z;
        return new Matrix4f(MathF.cos(a) * MathF.cos(b), MathF.cos(a) * MathF.sin(b) * MathF.sin(c) - MathF.sin(a) * MathF.cos(c), MathF.cos(a) * MathF.sin(b) * MathF.cos(c) + MathF.sin(a) * MathF.sin(c), 0,
                MathF.sin(a) * MathF.cos(b), MathF.sin(a) * MathF.sin(b) * MathF.sin(c) + MathF.cos(a) * MathF.cos(c), MathF.sin(a) * MathF.sin(b) * MathF.cos(c) - MathF.cos(a) * MathF.sin(c), 0,
                -MathF.sin(b), MathF.cos(b) * MathF.sin(c), MathF.cos(b) * MathF.cos(c), 0,
                0, 0, 0, 1);
    }


    public static Matrix4f scaleMatrix3D(Vector3f scale) {
        return new Matrix4f(scale.x, 0, 0, 0,
                0, scale.y, 0, 0,
                0, 0, scale.z, 0,
                0, 0, 0, 1);
    }

    public static Matrix4f translationMatrix3D(Vector3f translation) {
        return new Matrix4f(1, 0, 0, translation.x,
                            0, 1, 0, translation.y,
                            0, 0, 1, translation.z,
                            0, 0, 0, 1);
    }

    public static Matrix3f rotationMatrix2D(float angle) {
        return new Matrix3f(MathF.cos(angle), -MathF.sin(angle), 0,
                MathF.sin(angle), MathF.cos(angle), 0,
                0, 0, 1);
    }

    public static Matrix4f modelMatrix(Vector3f scale, Vector3f translation, Vector3f rotation) {
        var scaleMat = scaleMatrix3D(scale);
        var transMat = translationMatrix3D(translation);
        var rotMat = rotationMatrix3D(rotation);
        return transMat.mult(rotMat).mult(scaleMat);
    }

    public static Matrix4f screenMapping(Camera camera) {
        var fx = camera.getWidth() / (2 * MathF.tan(camera.getFovX() / 2));
        var fy = camera.getHeight() / (2 * MathF.tan(camera.getFovY() / 2));
        return new Matrix4f(fx, 0, 0, camera.getWidth() / 2,
                0, fy, 0, camera.getHeight() / 2,
                0, 0, 0, 0,
                0, 0, 0, 0);
    }

    public static Matrix4f projectionMatrix(Camera cam, float zNear, float zFar) {
        var yScale = (1f / MathF.tan(cam.getFovY() / 2)) * cam.getAspectRatio();
        var xScale = yScale / cam.getAspectRatio();
        var frustumLength = zFar - zNear;
        return new Matrix4f(xScale, 0, 0, 0,
                0, yScale, 0, 0,
                0, 0, ((zFar + zNear) / frustumLength), ((2f * zFar * zNear) / frustumLength),
                0, 0, 1, 0);
    }

    public static Vector3f calcTriNormal(Vector4f a, Vector4f b, Vector4f c) {
        var a2 = new Vector3f(a.x, a.y, a.z);
        var b2 = new Vector3f(b.x, b.y, b.z);
        var c2 = new Vector3f(c.x, c.y, c.z);

        var edge1 = a2.subtract(b2);
        var edge2 = a2.subtract(c2);

        return edge1.cross(edge2).normalize();
    }

    public static Vector4f vec4(Vector3f vec) {
        return new Vector4f(vec.x, vec.y, vec.z, 1);
    }

}
