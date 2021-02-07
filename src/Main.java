import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import solutions.AnalyticalSolution;
import solutions.EulerMethod;
import solutions.ImprovedEuler;
import solutions.RungeKutta;

import java.util.ArrayList;

public class Main extends Application {

    private static String graphChartTitle = "Numerical Methods Comparison";
    private static String errorsChartTitle = "Local Errors Comparison";

    private static final double defaultX0 = 1.0;
    private static final double defaultY0 = 2.0;
    private static final double defaultX = 10.0;
    private static final int defaultN = 100;

    private static final int defaultN0 = 10;

    private static AnalyticalSolution analyticalSolution = new AnalyticalSolution(defaultX0, defaultY0, defaultX, defaultN);
    private static EulerMethod eulerMethod = new EulerMethod(defaultX0, defaultY0, defaultX, defaultN);
    private static ImprovedEuler improvedEuler = new ImprovedEuler(defaultX0, defaultY0, defaultX, defaultN);
    private static RungeKutta rungeKutta = new RungeKutta(defaultX0, defaultY0, defaultX, defaultN);

    @Override
    public void start(Stage stage) {
        stage.setTitle("Computational Practicum");

        TabPane root = new TabPane();
        root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        HBox firstTabContent = createFirstTab();
        HBox secondTabContent = createSecondTab();

        Tab tab1 = new Tab("Solutions", firstTabContent);
        Tab tab2 = new Tab("Errors Analysis", secondTabContent);

        root.getTabs().addAll(tab1, tab2);

        Scene scene  = new Scene(root,1200,1000);
        stage.setScene(scene);
        stage.show();
    }

    private static HBox createSecondTab() {
        LineChart<Number, Number> analysisChart = populateAnalysisChart(defaultN0, defaultN);

        final Label n0Label = new Label("n0:");
        final TextField n0TextField = new TextField(String.valueOf(defaultN0));
        final FlowPane n0Pane = new FlowPane(10, 10, n0Label, n0TextField);

        final Label NLabel = new Label("N:");
        final TextField NTextField = new TextField(String.valueOf(defaultN));
        final FlowPane NPane = new FlowPane(10, 10, NLabel, NTextField);

        final Label invalidArgumentsLabel = new Label("Invalid Arguments");
        invalidArgumentsLabel.setTextFill(Color.RED);
        invalidArgumentsLabel.setVisible(false);

        Button computeBtn = new Button("Compute");

        VBox inputBox = new VBox(20, n0Pane, NPane, computeBtn, invalidArgumentsLabel);
        inputBox.setMaxWidth(200);
        inputBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(n0Pane, new Insets(10, 0, 0, 0));

        final HBox root = new HBox(0, analysisChart, inputBox);
        HBox.setHgrow(inputBox, Priority.NEVER);
        HBox.setHgrow(analysisChart, Priority.ALWAYS);

        computeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LineChart<Number, Number> newAnalysisChart;
                int n0, N;
                invalidArgumentsLabel.setVisible(false);
                try {
                    n0 = Integer.parseInt(n0TextField.getText());
                    N = Integer.parseInt(NTextField.getText());

                    if ((n0 <= 0) || (n0 >= N))
                        throw new IllegalArgumentException();
                } catch (IllegalArgumentException e) {
                    invalidArgumentsLabel.setVisible(true);
                    return;
                }

                newAnalysisChart = populateAnalysisChart(n0, N);

                root.getChildren().set(0, newAnalysisChart);

                HBox.setHgrow(newAnalysisChart, Priority.ALWAYS);
            }
        });

        root.setAlignment(Pos.CENTER);
        return root;
    }

    private static HBox createFirstTab() {
        LineChart<Number, Number> graphLineChart = populateChart(graphChartTitle, "y", false);
        LineChart<Number, Number> errorsLineChart = populateChart(errorsChartTitle, "error", true);
        VBox chartBox = new VBox(20, graphLineChart, errorsLineChart);
        VBox.setVgrow(graphLineChart, Priority.ALWAYS);
        VBox.setVgrow(errorsLineChart, Priority.ALWAYS);


        final Label x0Label = new Label("x0:");
        final TextField x0TextField = new TextField(String.valueOf(defaultX0));
        x0TextField.setMaxWidth(Double.MAX_VALUE);
        final FlowPane x0Pane = new FlowPane(10, 10, x0Label, x0TextField);

        final Label y0Label = new Label("y0:");
        final TextField y0TextField = new TextField(String.valueOf(defaultY0));
        final FlowPane y0Pane = new FlowPane(10, 10, y0Label, y0TextField);

        final Label XLabel = new Label("X:");
        final TextField XTextField = new TextField(String.valueOf(defaultX));
        final FlowPane XPane = new FlowPane(10, 10, XLabel, XTextField);

        final Label NLabel = new Label("N:");
        final TextField NTextField = new TextField(String.valueOf(defaultN));
        final FlowPane NPane = new FlowPane(10, 10, NLabel, NTextField);

        final Label invalidArgumentsLabel = new Label("Invalid Arguments");
        invalidArgumentsLabel.setTextFill(Color.RED);
        invalidArgumentsLabel.setVisible(false);

        Button computeBtn = new Button("Compute");

        VBox inputBox = new VBox(20, x0Pane, y0Pane, XPane, NPane, computeBtn, invalidArgumentsLabel);
        inputBox.setMaxWidth(200);
        inputBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(x0Pane, new Insets(10, 0, 0, 0));

        final HBox root = new HBox(0, chartBox, inputBox);
        HBox.setHgrow(inputBox, Priority.NEVER);
        HBox.setHgrow(chartBox, Priority.ALWAYS);

        computeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LineChart<Number, Number> newGraph, newErrors;
                invalidArgumentsLabel.setVisible(false);
                try {
                    double x0 = Double.parseDouble(x0TextField.getText());
                    double y0 = Double.parseDouble(y0TextField.getText());
                    double X = Double.parseDouble(XTextField.getText());
                    int N = Integer.parseInt(NTextField.getText());

                    analyticalSolution = new AnalyticalSolution(x0, y0, X, N);
                    eulerMethod = new EulerMethod(x0, y0, X, N);
                    improvedEuler = new ImprovedEuler(x0, y0, X, N);
                    rungeKutta = new RungeKutta(x0, y0, X, N);
                } catch (IllegalArgumentException e) {
                    invalidArgumentsLabel.setVisible(true);
                    return;
                }

                newGraph = populateChart(graphChartTitle, "y", false);
                newErrors = populateChart(errorsChartTitle, "error", true);

                VBox chartBox = (VBox) root.getChildren().get(0);
                chartBox.getChildren().set(0, newGraph);
                chartBox.getChildren().set(1, newErrors);

                VBox.setVgrow(newGraph, Priority.ALWAYS);
                VBox.setVgrow(newErrors, Priority.ALWAYS);
            }
        });

        root.setAlignment(Pos.CENTER);
        return root;
    }

    private static LineChart<Number, Number> populateAnalysisChart(int n0, int N) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("N");
        yAxis.setLabel("error");

        final LineChart<Number, Number> analysisLineChart = new LineChart<>(xAxis, yAxis);

        analysisLineChart.setTitle("Errors Analysis");

        ArrayList<Double> eulerGTE = new ArrayList<>();
        ArrayList<Double> improvedGTE = new ArrayList<>();
        ArrayList<Double> rungeGTE = new ArrayList<>();
        ArrayList<Double> nValues = new ArrayList<>();
        for (int i = n0; i <= N; i++) {
            AnalyticalSolution as = new AnalyticalSolution(defaultX0, defaultY0, defaultX, i);
            nValues.add((double) i);
            eulerGTE.add(new EulerMethod(defaultX0, defaultY0, defaultX, i).computeMaxGTE(as));
            improvedGTE.add(new ImprovedEuler(defaultX0, defaultY0, defaultX, i).computeMaxGTE(as));
            rungeGTE.add(new RungeKutta(defaultX0, defaultY0, defaultX, i).computeMaxGTE(as));
        }

        analysisLineChart.getData().add(createSeries("Euler's Method", nValues, eulerGTE));
        analysisLineChart.getData().add(createSeries("Improved Euler", nValues, improvedGTE));
        analysisLineChart.getData().add(createSeries("Runge-Kutta", nValues, rungeGTE));

        return analysisLineChart;
    }

    private static LineChart<Number, Number> populateChart(String title, String yLabel, boolean errors) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel(yLabel);

        final LineChart<Number, Number> graphLineChart = new LineChart<>(xAxis, yAxis);

        graphLineChart.setTitle(title);

        if (!errors) {
            graphLineChart.getData().add(createSeries("Euler's Method", eulerMethod.getXData(), eulerMethod.getYData()));
            graphLineChart.getData().add(createSeries("Improved Euler", improvedEuler.getXData(), improvedEuler.getYData()));
            graphLineChart.getData().add(createSeries("Runge-Kutta", rungeKutta.getXData(), rungeKutta.getYData()));
            graphLineChart.getData().add(createSeries("Analytical solution", analyticalSolution.getXData(), analyticalSolution.getYData()));
            graphLineChart.setCreateSymbols(false);
        } else {
            graphLineChart.getData().add(createSeries("Euler's Method", eulerMethod.getXData(), eulerMethod.getLocalErrors()));
            graphLineChart.getData().add(createSeries("Improved Euler", improvedEuler.getXData(), improvedEuler.getLocalErrors()));
            graphLineChart.getData().add(createSeries("Runge-Kutta", rungeKutta.getXData(), rungeKutta.getLocalErrors()));
            graphLineChart.setCreateSymbols(true);
        }

        return graphLineChart;
    }

    private static XYChart.Series<Number, Number> createSeries(String name, ArrayList<Double> xData, ArrayList<Double> yData) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for (int i = 0; i < xData.size(); i++)
            series.getData().add(new XYChart.Data<>(xData.get(i), yData.get(i)));

        return series;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
