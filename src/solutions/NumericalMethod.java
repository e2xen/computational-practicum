package solutions;

import java.util.ArrayList;

public abstract class NumericalMethod extends Solution
{
    ArrayList<Double> localErrors = new ArrayList<>();

    public NumericalMethod(double x0, double y0, double X, int N) {
        super(x0, y0, X, N);
        generateData();

        AnalyticalSolution as = new AnalyticalSolution(x0, y0, X, N);
        computeErrors(as);
        computeMaxGTE(as);
    }

    public ArrayList<Double> getLocalErrors() {
        return this.localErrors;
    }

    double func(double x, double y) {
        return 2*Math.pow(x, 3) + 2*y/x;
    }

    abstract void computeErrors(AnalyticalSolution as);

    public double computeMaxGTE(AnalyticalSolution as) {
        double max = 0;
        for (int i = 0; i < nPoints; i++) {
            double error = Math.abs(yData.get(i) - as.getYData().get(i));
            max = Math.max(error, max);
        }
        return max;
    }
}
