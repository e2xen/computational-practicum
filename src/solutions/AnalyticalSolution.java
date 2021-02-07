package solutions;

public class AnalyticalSolution extends Solution
{
    private double C;

    public AnalyticalSolution(double x0, double y0, double X, int N) {
        super(x0, y0, X, N);
        this.C = (y0 - Math.pow(x0, 4)) / Math.pow(x0, 2);

        generateData();
    }

    private double exactSolution(double x) {
        return Math.pow(x, 4) + this.C * Math.pow(x, 2);
    }

    protected void generateData() {
        double x = this.x0;
        for (int i = 0; i < nPoints; i++) {
            xData.add(x);
            yData.add(exactSolution(x));
            x += this.step;
        }
    }
}
