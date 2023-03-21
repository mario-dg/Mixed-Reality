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
        // Math.pow will yield NaN for base=0 and exponent<0
        if (t == 0 && i == 0) {
            return degree;
        }
        if (t == 1 && i == degree) {
            return degree;
        }

        var binomial = Utils.binomial(degree, i);
        var term1 = i * Math.pow(t, i - 1) * Math.pow(1 - t, degree - i);
        var term2 = (degree - i) * Math.pow(t, i) * Math.pow(1 - t, degree - i - 1);
        return (float) (binomial * (term1 - term2));
    }
}
