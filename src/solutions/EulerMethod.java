package solutions;

public class EulerMethod extends NumericalMethod
{
    public EulerMethod(double x0, double y0, double X, int N) {
        super(x0, y0, X, N);
    }

    protected void generateData() {
        double x = x0;
        xData.add(x);
        yData.add(y0);
        x += step;
        for (int i = 1; i < nPoints; i++) {
            xData.add(x);
            yData.add(yData.get(i-1) + step*func(xData.get(i-1), yData.get(i-1)));
            x += step;
        }
    }

    void computeErrors(AnalyticalSolution as) {
        localErrors.add(0.0);
        for (int i = 1; i < nPoints; i++) {
            double exactY = as.getYData().get(i-1);
            double y = exactY + step*func(xData.get(i-1), exactY);
            localErrors.add(Math.abs(y - as.getYData().get(i)));
        }
    }
}
