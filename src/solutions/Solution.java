package solutions;

import java.util.ArrayList;

public abstract class Solution
{
    double x0, y0, X, step;
    int nPoints;

    ArrayList<Double> xData = new ArrayList<>();
    ArrayList<Double> yData = new ArrayList<>();

    private static final String exceptionMsg = "Illegal Arguments";
    private static final double eps = 1e-6;

    public ArrayList<Double> getXData() {
        return xData;
    }

    public ArrayList<Double> getYData() {
        return yData;
    }

    public int getN() {
        return nPoints;
    }

    public Solution(double x0, double y0, double X, int N)
            throws IllegalArgumentException {
        if ((x0 <= 0 && X >= 0) ||
                (x0 >= X) ||
                (N <= 0))
            throw new IllegalArgumentException(exceptionMsg);

        this.x0 = x0;
        this.y0 = y0;
        this.X = X;
        this.step = (X - x0) / N;
        this.nPoints = N+1;
    }

    abstract protected void generateData();
}
