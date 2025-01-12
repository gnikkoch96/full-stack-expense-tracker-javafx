package org.example.dialogs;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.example.models.MonthlyFinance;
import org.example.models.User;

public class ViewChartDialog extends CustomDialog{
    private ObservableList<MonthlyFinance> monthlyFinances; // will store data for the correspnoding year
    private PieChart pieChart;

    public ViewChartDialog(User user, ObservableList<MonthlyFinance> monthlyFinances) {
        super(user);
        this.monthlyFinances = monthlyFinances;
        pieChart = new PieChart();

        for(MonthlyFinance monthlyFinance : monthlyFinances){
            PieChart.Data slice = new PieChart.Data(monthlyFinance.getMonth(), monthlyFinance.getIncome().doubleValue());
            pieChart.getData().add(slice);
        }

        getDialogPane().getChildren().add(pieChart);
    }
}
