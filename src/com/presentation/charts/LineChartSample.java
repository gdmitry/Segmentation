package com.presentation.charts;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
 /*
  * http://docs.oracle.com/javafx/2/charts/pie-chart.htm
  */
public class LineChartSample extends Application {
	final LineChart<Number,Number> lineChart;
	public LineChartSample() {
		 //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        lineChart= 
                new LineChart<Number,Number>(xAxis,yAxis);                
        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
        lineChart.getData().add(series);
        
	}
    @Override public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
       
        Scene scene  = new Scene(lineChart,800,600);
        
       
        stage.setScene(scene);
        stage.show();
    }
 
    public void showLineChart(XYChart.Series series) {
    	lineChart.getData().clear();
    	//barChart.setTitle(title);
    	lineChart.getData().addAll(series);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}