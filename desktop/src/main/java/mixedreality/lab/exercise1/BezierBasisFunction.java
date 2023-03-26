package mixedreality.lab.exercise1;

import mixedreality.base.math.BasisFunction;
import mixedreality.base.math.Utils;

public class BezierBasisFunction implements BasisFunction {
    @Override
    public float eval(float t, int index, int degree) {
        var binomial = Utils.binomial(degree, index);
        return (float) (binomial * Math.pow(t, index) * Math.pow((1 - t), degree - index));
    }

    @Override
    public float evalDerivative(float t, int i, int degree) {
        //Catch first and last control point
        if (t == 0 && i == 0) {
            return (float) (-degree * Math.pow(1 - t, degree - 1));
        }
        if (t == 1 && i == degree) {
            return (float) (degree * Math.pow(t, degree - 1));
        }
        return degree * (eval(t, i - 1, degree - 1) - eval(t, i, degree - 1));
    }
}
