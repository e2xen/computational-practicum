package solutions;

public class RungeKutta extends NumericalMethod
{
    public RungeKutta(double x0, double y0, double X, int N) {
        super(x0, y0, X, N);
    }

    protected void generateData() {
        double x = x0;
        xData.add(x);
        yData.add(y0);
        x += step;
        double k1, k2, k3, k4;
        for (int i = 1; i < nPoints; i++) {
            xData.add(x);
            k1 = func(xData.get(i-1), yData.get(i-1));
            k2 = func(xData.get(i-1) + step/2, yData.get(i-1) + step/2*k1);
            k3 = func(xData.get(i-1) + step/2, yData.get(i-1) + step/2*k2);
            k4 = func(xData.get(i-1) + step, yData.get(i-1) + step*k3);

            yData.add(yData.get(i-1) + step/6*(k1 + 2*k2 + 2*k3 + k4));
            x += step;
        }
    }

    void computeErrors(AnalyticalSolution as) {
        double k1, k2, k3, k4;
        localErrors.add(0.0);
        for (int i = 1; i < nPoints; i++) {
            double exactY = as.getYData().get(i-1);
            k1 = func(xData.get(i-1), exactY);
            k2 = func(xData.get(i-1) + step/2, exactY + step/2*k1);
            k3 = func(xData.get(i-1) + step/2, exactY + step/2*k2);
            k4 = func(xData.get(i-1) + step, exactY + step*k3);
            double y = yData.get(i-1) + step/6*(k1 + 2*k2 + 2*k3 + k4);
            localErrors.add(Math.abs(y - as.getYData().get(i)));
        }
    }
}
