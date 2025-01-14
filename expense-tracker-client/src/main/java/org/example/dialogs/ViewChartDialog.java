package org.example.dialogs;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import org.example.models.MonthlyFinance;
import org.example.models.User;

public class ViewChartDialog extends CustomDialog{
    private ObservableList<MonthlyFinance> monthlyFinances;
    private BarChart<String, Number> barChart;

    public ViewChartDialog(User user, ObservableList<MonthlyFinance> monthlyFinances) {
        super(user);
        this.monthlyFinances = monthlyFinances;
        setTitle("View Chart");
        setWidth(700);
        setHeight(595);

        // needed to add a vbox for the chart to show
        VBox vbox = new VBox();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setMinWidth(getWidth() - 25);
        barChart.setMinHeight(getHeight() - 50);
        barChart.getStyleClass().addAll("text-size-md");

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("expense");

        for(MonthlyFinance monthlyFinance : monthlyFinances){
            incomeSeries.getData().add(new XYChart.Data<>(monthlyFinance.getMonth(), monthlyFinance.getIncome()));
            expenseSeries.getData().add(new XYChart.Data<>(monthlyFinance.getMonth(), monthlyFinance.getExpense()));
        }

        barChart.getData().addAll(incomeSeries, expenseSeries);


        // update the colors for income
        incomeSeries.getData().forEach(data ->
                data.getNode().setStyle("-fx-bar-fill: green;")
        );

        // update the colors for expense
        expenseSeries.getData().forEach(data ->
                data.getNode().setStyle("-fx-bar-fill: red;")
        );


        vbox.getChildren().add(barChart);
        getDialogPane().getChildren().add(vbox);
    }
}