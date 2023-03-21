package mixedreality.base.math;

public class Utils {
    public static int factorial(int n) {
        if (n == 0)
            return 1;
        else
            return (n * factorial(n - 1));
    }

    public static int binomial(int n, int i){
        if(i < 0 || i > n) return 0;

        return factorial(n) / (factorial(i) * factorial(n - i));
    }
}
