package mixedreality.lab.exercise6;

import com.jme3.math.Vector2f;
import math.MathF;
import math.Vectors;

public class Turtle {

    public final float stepLength;
    public final float rotationAngle;

    public Vector2f position;
    public float angle;

    public Turtle(float stepLength, float rotationAngle, Vector2f position, float angle) {
        this.stepLength = stepLength;
        this.rotationAngle = rotationAngle;
        this.position = position;
        this.angle = angle;
    }

    public Turtle step() {
        Vector2f direction = Vectors.fromPolar(stepLength, MathF.toRadians(angle));
        position = position.add(direction);
        return this;
    }

    public Turtle rotate(int direction) {
        if (direction < 0) angle -= rotationAngle;
        else if (direction > 0) angle += rotationAngle;
        return this;
    }

    public Turtle clone() {
        return new Turtle(this.stepLength, this.rotationAngle, this.position, this.angle);
    }

}
