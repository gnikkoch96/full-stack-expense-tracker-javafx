package org.example.dialogs;

import javafx.scene.chart.PieChart;
import org.example.models.User;

public class ViewChartDialog extends CustomDialog{
    private PieChart pieChart;

    public ViewChartDialog(User user) {
        super(user);
        pieChart = new PieChart();
    }
}
